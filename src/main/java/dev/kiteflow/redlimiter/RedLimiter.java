package dev.kiteflow.redlimiter;

import dev.kiteflow.redlimiter.events.OnBlockPlace;
import dev.kiteflow.redlimiter.events.OnRedstoneEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("deprecation")
public final class RedLimiter extends JavaPlugin {
    public static Plugin plugin;
    public static ConfigManager config;

    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new OnRedstoneEvent(), this);
        getServer().getPluginManager().registerEvents(new OnBlockPlace(), this);
    }

    @Override
    public void onEnable() {
        plugin = this;

        plugin.saveDefaultConfig();
        config = new ConfigManager(plugin.getConfig());

        registerEvents();

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, OnRedstoneEvent::clearBlocks, 200, 300);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, OnRedstoneEvent::clearSignalCount, 200, 1200);
        getLogger().info("RedLimiter enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("RedLimiter disabled");
    }
}
