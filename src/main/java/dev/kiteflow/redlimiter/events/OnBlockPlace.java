package dev.kiteflow.redlimiter.events;

import dev.kiteflow.redlimiter.RedLimiter;
import dev.kiteflow.redlimiter.gui.FrequentGUI;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import static org.bukkit.Material.*;

public class OnBlockPlace implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Material blockType = e.getBlock().getType();
        Chunk chunk = e.getBlock().getChunk();

        int repeaters = 0;
        int observers = 0;
        int pistons = 0;

        if(blockType == REPEATER || blockType == OBSERVER || blockType == PISTON || blockType == STICKY_PISTON){
            for(int y = 0; y < 256; y++){
                for(int z = 0; z < 16; z++){
                    for(int x = 0; x < 16; x++){
                        switch(chunk.getBlock(x, y, z).getType()){
                            case REPEATER -> repeaters++;
                            case OBSERVER -> observers++;
                            case PISTON, STICKY_PISTON -> pistons++;
                        }
                    }
                }
            }

            if(
                    (blockType == REPEATER && repeaters + 1 > RedLimiter.configManager.repeaterLimits) ||
                    (blockType == OBSERVER && observers + 1 > RedLimiter.configManager.observerLimits) ||
                    ((blockType == PISTON || blockType == STICKY_PISTON) && pistons + 1 > RedLimiter.configManager.pistonLimits)
            ){
                e.getPlayer().sendMessage(RedLimiter.configManager.limitwarning);
                e.setCancelled(true);
            }
        }
    }
}
