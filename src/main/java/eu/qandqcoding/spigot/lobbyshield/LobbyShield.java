package eu.qandqcoding.spigot.lobbyshield;

import eu.qandqcoding.spigot.lobbyshield.commands.ShieldCommand;
import eu.qandqcoding.spigot.lobbyshield.listener.ShieldListener;
import eu.qandqcoding.spigot.lobbyshield.utils.Config;
import eu.qandqcoding.spigot.lobbyshield.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyShield extends JavaPlugin {
    private static LobbyShield instance;
    private Config config;
    private Constants constants;

    public static LobbyShield getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        config = new Config(getDataFolder().getAbsolutePath(), "config.yml");
        constants = new Constants(this);
        getLogger().info(constants.getPrefix() + "Das System wurde aktiviertĀ§8.");
        registerListener();
        registerCommands();
    }

    @Override
    public void onDisable() {
        getLogger().info(constants.getPrefix() + "Das System wurde deaktiviertĀ§8.");

    }

    public Config getConfigFile() {
        return config;
    }

    public Constants getConstants() {
        return constants;
    }

    private void registerListener() {

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ShieldListener(), this);
    }

    private void registerCommands() {
        getCommand("shield").setExecutor(new ShieldCommand());
    }

}
