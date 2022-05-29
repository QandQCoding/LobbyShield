package eu.qandqcoding.spigot.lobbyshield;

import eu.qandqcoding.spigot.lobbyshield.listener.ShieldListener;
import eu.qandqcoding.spigot.lobbyshield.utils.Config;
import eu.qandqcoding.spigot.lobbyshield.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyShield extends JavaPlugin {
    private Config config;
    private Constants constants;
    private static LobbyShield instance;


    @Override
    public void onEnable() {
        constants = new Constants(this);
        getLogger().info(constants.getPrefix() + "Das System wurde aktiviert§8.");
        registerListener();

    }

    @Override
    public void onDisable() {
        getLogger().info(constants.getPrefix() + "Das System wurde deaktiviert§8.");

    }
    public Config getConfigFile() {
        return config;
    }
    public static LobbyShield getInstance() {
        return instance;
    }
    public Constants getConstants() {
        return constants;
    }
    private void registerListener() {

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ShieldListener(), this);
    }

}
