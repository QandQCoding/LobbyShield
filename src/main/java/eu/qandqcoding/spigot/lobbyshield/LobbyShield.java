package eu.qandqcoding.spigot.lobbyshield;

import eu.qandqcoding.spigot.lobbyshield.listener.ShieldListener;
import eu.qandqcoding.spigot.lobbyshield.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyShield extends JavaPlugin {
    private static LobbyShield instance;
    private ConfigManager configManager;

    public static LobbyShield getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        getLogger().info("LobbyShield is loading...");
    }

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager();
        configManager.setPrefix();

        getLogger().info("LobbyShield activated.");

        registerListeners();
    }

    @Override
    public void onDisable() {
        getLogger().info("LobbyShield disabled.");
    }

    private void registerListeners() {
        Listener shieldListener = new ShieldListener();
        Bukkit.getPluginManager().registerEvents(shieldListener, this);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
