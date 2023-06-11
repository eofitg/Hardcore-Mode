package com.eofitg.hardcore;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class ConfigReader {
    private static File languageFile = new File(Hardcore.getInstance().getDataFolder() + "language/", ConfigReader.getLanguage() + ".yml");
    private static YamlConfiguration messages = YamlConfiguration.loadConfiguration(languageFile);
    private static FileConfiguration config = Hardcore.getInstance().getConfig();
    private static List<String> playerNames = config.getStringList("playerNames");
    private static List<String> cmdNames = config.getStringList("commandNames");
    private static boolean state = config.getBoolean("enable");
    private static String language = "en";
    public static List<String> getPlayerNames() {
        return playerNames;
    }
    public static String getLanguage() {
        language = config.getString("language");
        if (language != null) {
            return language;
        } else {
            return "en";
        }
    }
    public static YamlConfiguration getMessage() {
        return messages;
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
}
