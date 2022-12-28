package eu.qandqcoding.spigot.lobbyshield.commands;

import eu.qandqcoding.spigot.lobbyshield.LobbyShield;
import eu.qandqcoding.spigot.lobbyshield.config.Config;
import eu.qandqcoding.spigot.lobbyshield.manager.CommandManager;
import eu.qandqcoding.spigot.lobbyshield.manager.ConfigManager;
import eu.qandqcoding.spigot.lobbyshield.utils.CommandUtils;
import eu.qandqcoding.spigot.lobbyshield.utils.ItemAPI;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nonnull;
import java.util.List;

public class ShieldCommand extends BukkitCommand {
     private Config messagesConfig;
    private Config config;

    public ShieldCommand(String name, String description, String usageMessage, List<String> stringList) {
        super( name, description, usageMessage, List.of( "shield" ) );
        this.messagesConfig = LobbyShield.getPlugin().getConfigManager().getMessagesConfig();
        this.config = LobbyShield.getInstance().getConfigManager().getConfig();
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (!CommandUtils.hasPermission(sender, "messages.commands.shield.command_permission")) {
            sender.sendMessage(this.messagesConfig.getMessage("messages.commands.shield.no_permission_use", sender));
            return true;
        }
        Player player = (Player) sender;
        PlayerInventory inventory = player.getInventory();
        sender.sendMessage(this.messagesConfig.getMessage("messages.commands.shield.success", sender));
        inventory.setItem(this.config.getInt("item.on_join.slot"), new ItemAPI(this.config.getString("item.name"), Material.getMaterial(this.config.getString("item.material")), 1, this.config.getStringList("item.lore")).build());

        return false;
    }

}
