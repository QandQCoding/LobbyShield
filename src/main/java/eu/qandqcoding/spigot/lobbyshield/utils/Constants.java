package eu.qandqcoding.spigot.lobbyshield.utils;

import eu.qandqcoding.spigot.lobbyshield.LobbyShield;

public class Constants {


    private final String prefix;

    public Constants(LobbyShield lobbyShield) {
        Config config = lobbyShield.getConfigFile();
        config.addDefault("prefix", "&6QandQProject &8| &7");
        config.copyDefaults();
        prefix = config.getString("prefix").replace("&", "§");
    }

    public String getPrefix() {
        return prefix;
    }



}
