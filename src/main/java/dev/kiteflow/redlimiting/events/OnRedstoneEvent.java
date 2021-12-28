package dev.kiteflow.redlimiting.events;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class OnRedstoneEvent implements Listener {
    private static LinkedHashMap<Block, Long> blocks = new LinkedHashMap<>();

    @EventHandler
    public void onRedstoneEvent(BlockRedstoneEvent e){
        Block block = e.getBlock();

        if(blocks.containsKey(block)){
            long difference =  System.currentTimeMillis() - blocks.get(block);

            switch(block.getType()){
                case PISTON:
                case STICKY_PISTON:
                    if(difference < 5000L){
                        e.setNewCurrent(0);
                    }
                    break;
                case OBSERVER:
                    if(difference < 7500L){
                        e.setNewCurrent(0);
                    }
                    break;
                case REPEATER:
                    if(difference < 3000L){
                        e.setNewCurrent(0);
                    }
                    break;
                default:
                    if(difference < 2500L){
                        e.setNewCurrent(0);
                    }
            }
        }

        blocks.put(block, System.currentTimeMillis());
    }

    public static void clearBlocks(){
        ArrayList<Block> removal = new ArrayList<>();

        for(Map.Entry<Block, Long> entry : blocks.entrySet()){
            if(System.currentTimeMillis() - entry.getValue() > 10000L){
                removal.add(entry.getKey());
            }else{
                break;
            }
        }

        removal.forEach(block -> blocks.remove(block));
    }
}