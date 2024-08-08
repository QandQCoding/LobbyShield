package eu.qandqcoding.spigot.lobbyshield.manager;

import eu.qandqcoding.spigot.lobbyshield.LobbyShield;
import eu.qandqcoding.spigot.lobbyshield.commands.ShieldCommand;
import eu.qandqcoding.spigot.lobbyshield.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;

public class CommandManager {

    private final ShieldCommand shieldCommand;

    public CommandManager() {
        Config config = LobbyShield.getInstance().getConfigManager().getCommandsConfig();

        this.shieldCommand = new ShieldCommand(
                config.getString("commands.shield.success"),
                config.getString("commands.shield.description"),
                config.getString("commands.shield.usage"),
                config.getStringList("commands.shield.aliases")
        );

        String shieldModuleCommand = config.getString("commands.shield_module.command");

        if (shieldModuleCommand != null && !shieldModuleCommand.equalsIgnoreCase("null")) {
            this.registerCommandDynamically(shieldModuleCommand, shieldCommand);
        }
    }

    private void registerCommandDynamically(String command, Command commandInstance) {
        try {
            CommandMap commandMap = getCommandMap();
            if (commandMap != null) {
                commandMap.register(command, commandInstance);
            } else {
                throw new IllegalStateException("Could not retrieve the CommandMap");
            }
        } catch (Exception e) {
            LobbyShield.getInstance().getLogger().severe("Failed to register command: " + command);
            e.printStackTrace();
        }
    }

    private CommandMap getCommandMap() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            return (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ShieldCommand getShieldCommand() {
        return shieldCommand;
    }
}
