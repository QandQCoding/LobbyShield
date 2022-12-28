package eu.qandqcoding.spigot.lobbyshield.manager;

import eu.qandqcoding.spigot.lobbyshield.config.Config;
import org.bukkit.ChatColor;

public class ConfigManager {



    private final Config commandsConfig, messagesConfig, config;

    private String prefix;

    public ConfigManager() {
        this.commandsConfig = new Config( "commands.yml" );
        this.messagesConfig = new Config( "messages.yml" );
        this.config = new Config( "config.yml");
    }
    

    public Config getCommandsConfig() {
        return this.commandsConfig;
    }



    public Config getMessagesConfig() {
        return this.messagesConfig;
    }

    public Config getConfig() {
        return this.config;
    }




    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix() {
        this.prefix = ChatColor.translateAlternateColorCodes( '&', this.commandsConfig.getString( "commands.shield_module.prefix" ) );
    }


}
