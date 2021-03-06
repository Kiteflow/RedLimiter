package dev.kiteflow.redlimiter.gui;

import dev.kiteflow.redlimiter.RedLimiter;
import dev.kiteflow.redlimiter.events.OnRedstoneEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

public class FrequentGUI implements Listener {
    private final Inventory inventory;

    public FrequentGUI(Player player) {
        inventory = Bukkit.createInventory(null, 27, "Frequent Redstone");
        addItems();

        player.openInventory(inventory);
    }

    private void addItems() {
        if(OnRedstoneEvent.signalCount == null) return;

        OnRedstoneEvent.signalCount.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(27)
                .forEach(frequent -> inventory.addItem(guiItem(frequent)));
    }

    private ItemStack guiItem(Map.Entry<Block, Integer> entry) {
        ItemStack item = new ItemStack(entry.getKey().getType());
        ItemMeta meta = item.getItemMeta();

        if(meta == null) return null;

        meta.setDisplayName(ChatColor.RESET + String.format(
                "X: %s Y: %s Z: %s",
                entry.getKey().getX(),
                entry.getKey().getY(),
                entry.getKey().getZ()
        ));

        String[] lore = new String[] { ChatColor.RESET + String.format("Count: %s", entry.getValue()), "" };
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getInventory() != inventory) return;

        e.setCancelled(true);
    }
}
