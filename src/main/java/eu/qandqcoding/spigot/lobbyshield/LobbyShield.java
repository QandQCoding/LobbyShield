package eu.qandqcoding.spigot.lobbyshield;

import eu.qandqcoding.spigot.lobbyshield.listener.ShieldListener;
import eu.qandqcoding.spigot.lobbyshield.manager.CommandManager;
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

    public static LobbyShield getPlugin() {
        return instance;
    }

    @Override
    public void onLoad() {
        JavaPlugin.getPlugin( LobbyShield.class ); // Is here to prevent possible bugs for 1.8.8 servers
        instance = this;
    }

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager();
        this.configManager.setPrefix();
        instance = this;
        getLogger().info("Lobbyshield activated§8.");
        CommandManager commandManager = new CommandManager();
        registerListener();

    }

    @Override
    public void onDisable() {
        getLogger().info("Lobbyshield disabled§8.");

    }

    private void registerListener() {
        Listener[] listeners = new Listener[]{
                new ShieldListener()};

        for (Listener listener : listeners)
            Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }


}
