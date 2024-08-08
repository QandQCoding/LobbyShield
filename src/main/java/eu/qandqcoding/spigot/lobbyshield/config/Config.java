package eu.qandqcoding.spigot.lobbyshield.config;


import eu.qandqcoding.spigot.lobbyshield.LobbyShield;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Config {

    private final File file;
    private FileConfiguration config;
    private final String fileName;

    public Config( String fileName ) {
        this.file = new File( LobbyShield.getInstance().getDataFolder(), fileName );
        this.fileName = fileName;
        this.initialize( fileName );
        this.config = YamlConfiguration.loadConfiguration( file );
    }

    private void initialize( String fileName ) {
        if ( !LobbyShield.getInstance().getDataFolder().exists() ) LobbyShield.getInstance().getDataFolder().mkdir();
        if ( !this.file.exists() ) LobbyShield.getInstance().saveResource( fileName, false );
    }

    public String getMessage( String path, CommandSender sender ) {
        String value = this.getString( path );
        value = value.replace( " %line% ", "\n" );
        value = value.replace( "%prefix%", LobbyShield.getInstance().getConfigManager().getPrefix() );
        return value;
    }

    public boolean getBoolean( String path ) {
        return this.config.getBoolean( path );
    }

    public int getInt( String path ) {
        return this.config.getInt( path );
    }

    public String getString(String path) {
        String value = this.config.getString( path );
        if ( value == null )
            return "§5LobbyShield §8| §cConfig entry §e" + path + " §cin §e" + this.fileName
                    + " §cdoes not exist. Please delete your configs to have the latest updates.";
        return ChatColor.translateAlternateColorCodes( '&', value );
    }

    public List<String> getStringList(String path ) {
        return this.config.getStringList( path );
    }


}