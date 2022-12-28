package eu.qandqcoding.spigot.lobbyshield.utils;

import eu.qandqcoding.spigot.lobbyshield.LobbyShield;
import eu.qandqcoding.spigot.lobbyshield.config.Config;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUtils {

    public static boolean hasPermission(CommandSender sender, String permission ) {
        Config config  = LobbyShield.getPlugin().getConfigManager().getCommandsConfig();
        return sender.hasPermission( config.getString( permission ) ) || sender.hasPermission( "qshield.*" );
    }

    public static boolean isPlayer( CommandSender sender ) {
        return sender instanceof Player;
    }
}
