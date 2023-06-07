package com.eofitg.hardcore;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigReader {
    private static FileConfiguration config = Hardcore.getInstance().getConfig();
    private static List<String> playerNames = config.getStringList("playerNames");
    private static List<String> cmdNames = config.getStringList("commandNames");
    private static boolean state = config.getBoolean("on");
    public static List<String> getPlayerNames() {
        return playerNames;
    }
    public static boolean getPlayerState(String name) {
        return config.getBoolean("alive." + name);
    }
    public static List<String> getCmdNames() {
        return cmdNames;
    }
    public static List<String> getCmdList(String cmd) {
        return config.getStringList("commands." + cmd);
    }
    public static boolean getState() {
        return state;
    }
    public static void set(String key, Object value) {
        config.set(key, value);
    }
    public static void reset(String name) {
        if (playerNames.contains(name)) {
            set("alive." + name, true);
        }
    }
    public static void reset() {
        for (String s : playerNames) {
            reset(s);
        }
    }
}
