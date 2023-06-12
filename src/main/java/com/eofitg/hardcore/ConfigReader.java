package com.eofitg.hardcore;

import org.bukkit.configuration.file.FileConfiguration;
import java.util.List;

public class ConfigReader {
    private static FileConfiguration config = Hardcore.getInstance().getConfig();
    private static List<String> playerNames = config.getStringList("playerNames");
    private static List<String> cmdNames = config.getStringList("commandNames");
    private static boolean state = config.getBoolean("enable");
    public static String getLanguage() {
        return config.getString("language");
    }
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
    public static int getPoint(String name) {
        return config.getInt("point." + name);
    }
    public static void set(String key, Object value) {
        config.set(key, value);
    }
    public static void setPoint(String name, int point) {
        config.set("point." + name, point);
    }
    public static void reset(String name) {
        if (playerNames.contains(name)) {
            set("alive." + name, true);
            set("point." + name, 0);
        }
    }
    public static void reset() {
        for (String s : playerNames) {
            reset(s);
        }
    }
    public static void reloadConfig() {
        config = Hardcore.getInstance().getConfig();
    }
}
