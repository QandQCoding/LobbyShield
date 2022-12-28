package eu.qandqcoding.spigot.lobbyshield.manager;

import eu.qandqcoding.spigot.lobbyshield.LobbyShield;
import eu.qandqcoding.spigot.lobbyshield.commands.ShieldCommand;
import eu.qandqcoding.spigot.lobbyshield.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;

public class CommandManager {

    private final ShieldCommand shieldCommand;


    public CommandManager() {
        Config config = LobbyShield.getPlugin().getConfigManager().getCommandsConfig();

        this.shieldCommand = new ShieldCommand(config.getString("commands.shield.success"), config.getString("commands.shield.description"), config.getString("commands.shield.usage"), config.getStringList("commands.shield.aliases"));


        String shieldModuleCommand = config.getString("commands.shield_module.command");

        if (shieldModuleCommand != null && !shieldModuleCommand.equalsIgnoreCase("null"))
            this.registerCommandDynamically("qshield", shieldCommand);
    }


    private void registerCommandDynamically(String command, ShieldCommand commandInstance) {
        try {
            Object commandMapMethod = Bukkit.getServer().getClass().getMethod("getCommandMap").invoke(Bukkit.getServer());
            commandMapMethod.getClass().getMethod("register", String.class, Command.class).invoke(commandMapMethod, command, commandInstance);
        } catch (Exception e) {
            System.out.println("Failed to register " + command);
            e.printStackTrace();
        }
    }

    public ShieldCommand getShieldCommand() {
        return this.shieldCommand;
    }

}
