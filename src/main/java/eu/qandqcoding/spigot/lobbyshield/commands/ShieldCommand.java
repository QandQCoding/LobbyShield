package eu.qandqcoding.spigot.lobbyshield.commands;

import eu.qandqcoding.spigot.lobbyshield.LobbyShield;
import eu.qandqcoding.spigot.lobbyshield.config.Config;
import eu.qandqcoding.spigot.lobbyshield.utils.CommandUtils;
import eu.qandqcoding.spigot.lobbyshield.utils.ItemAPI;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class ShieldCommand extends BukkitCommand {

    private final Config messagesConfig;
    private final Config config;

    public ShieldCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
        this.messagesConfig = LobbyShield.getInstance().getConfigManager().getMessagesConfig();
        this.config = LobbyShield.getInstance().getConfigManager().getConfig();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Player player = (Player) sender;

        if (!(sender instanceof Player)) {
            sender.sendMessage(this.messagesConfig.getMessage("messages.commands.shield.not_a_player", sender));
            return true;
        }

        if (!CommandUtils.hasPermission(player, "messages.commands.shield.command_permission")) {
            player.sendMessage(this.messagesConfig.getMessage("messages.commands.shield.no_permission_use", player));
            return true;
        }

        PlayerInventory inventory = player.getInventory();
        String materialName = this.config.getString("item.material");
        Material material = Material.getMaterial(materialName != null ? materialName.toUpperCase() : "SHIELD");

        if (material == null) {
            player.sendMessage(this.messagesConfig.getMessage("messages.commands.shield.invalid_material", player));
            return true;
        }

        inventory.setItem(
                this.config.getInt("item.on_join.slot"),
                new ItemAPI(
                        this.config.getString("item.name"),
                        material,
                        1,
                        this.config.getStringList("item.lore")
                ).build()
        );

        player.sendMessage(this.messagesConfig.getMessage("messages.commands.shield.success", player));
        return true;
    }
}
