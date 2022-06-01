package eu.qandqcoding.spigot.lobbyshield.commands;

import eu.qandqcoding.spigot.lobbyshield.LobbyShield;
import eu.qandqcoding.spigot.lobbyshield.utils.ItemAPI;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nonnull;

public class ShieldCommand implements CommandExecutor {
    String prefix = LobbyShield.getInstance().getConstants().getPrefix();

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + "Dies kann nur ein Spieler§8.");
            return false;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("system.shield")) {
            player.sendMessage(prefix + "§cNicht §7genug Rechte§8.");
            return false;
        }else {
            PlayerInventory inventory = player.getInventory();
            player.sendMessage(prefix + "§aShield erhalten§8.");
            inventory.setItem(0, new ItemAPI("§6Schild", Material.ENDER_EYE, 1).build());

        }
        return false;
}

}
