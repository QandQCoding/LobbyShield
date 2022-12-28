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
        this.file = new File( LobbyShield.getPlugin().getDataFolder(), fileName );
        this.fileName = fileName;
        this.initialize( fileName );
        this.config = YamlConfiguration.loadConfiguration( file );
    }

    private void initialize( String fileName ) {
        if ( !LobbyShield.getPlugin().getDataFolder().exists() ) LobbyShield.getPlugin().getDataFolder().mkdir();
        if ( !this.file.exists() ) LobbyShield.getPlugin().saveResource( fileName, false );
    }

    public String getMessage( String path, CommandSender sender ) {
        String value = this.getString( path );
        value = value.replace( " %line% ", "\n" );
        value = value.replace( "%prefix%", LobbyShield.getPlugin().getConfigManager().getPrefix() );
        return value;
    }

    public boolean getBoolean( String path ) {
        return this.config.getBoolean( path );
    }

    public int getInt( String path ) {
        return this.config.getInt( path );
    }

    public ConfigurationSection getSection(String path ) {
        return this.config.getConfigurationSection( path );
    }

    public String getString( String path ) {
        String value = this.config.getString( path );
        if ( value == null )
            return "§5LobbyShield §8| §cConfig entry §e" + path + " §cin §e" + this.fileName
                    + " §cdoes not exist. Please delete your configs to have the latest updates.";
        return ChatColor.translateAlternateColorCodes( '&', value );
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public List<String> getStringList(String path ) {
        return this.config.getStringList( path );
    }

    public void set( String path, Object value ) {
        this.config.set( path, value );
    }

    public void saveConfig() {
        try {
            this.config.save( this.file );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration( this.file );
    }

    public int getMessage(int parseInt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}