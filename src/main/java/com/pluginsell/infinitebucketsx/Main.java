package com.pluginsell.infinitebucketsx;

import net.milkbowl.vault.economy.Economy;
import com.pluginsell.infinitebucketsx.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public static ConfigManager data;
    public static String prefix = color("&3InfiniteBucketsX &b> ");
    private static Economy econ = null;

    @Override
    public void onEnable() {
        loadConfigManager();
        checkPlugin();
        Bukkit.getConsoleSender().sendMessage(prefix + color("&aPlugin has been enabled."));
        getServer().getPluginManager().registerEvents(new BucketEvents(), this);
        getCommand("infinitebucketsx").setExecutor(new BucketCommand());
    }

    @Override
    public void onDisable() {
        loadConfigManager();
        Bukkit.getConsoleSender().sendMessage(prefix + color("&cPlugin has been disabled."));
    }

    public static void loadConfigManager() {
        data = new ConfigManager(Main.getPlugin(Main.class));
        Bukkit.getPluginManager().getPlugin(Main.getPlugin(Main.class).getName()).getConfig().options().copyDefaults();
        Bukkit.getPluginManager().getPlugin(Main.getPlugin(Main.class).getName()).saveDefaultConfig();
    }

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void initEconomy() {
        if (!setupEconomy()) {
            Bukkit.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", Bukkit.getPluginManager().getPlugin(Main.getPlugin(Main.class).getName()).getDescription().getName()));
            Bukkit.getServer().getPluginManager().disablePlugin(Main.getPlugin(Main.class));
        }
    }

    private static boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static void checkPlugin() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("InfiniteBuckets");
        if (plugin == null) {
            disablePlugin();
        } else {
            if (!plugin.isEnabled()) {
                Bukkit.getServer().getPluginManager().enablePlugin(plugin);
                if (!plugin.isEnabled()) {
                    disablePlugin();
                }
            }
        }
    }

    public static void disablePlugin() {
        Bukkit.getConsoleSender().sendMessage(prefix + color("&cUnable to connect to the &6InfiniteBuckets &cplugin."));
        Bukkit.getServer().getPluginManager().disablePlugin(Main.getPlugin(Main.class));
    }
}
