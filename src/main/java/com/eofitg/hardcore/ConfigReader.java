package com.eofitg.hardcore;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigReader {
    private static FileConfiguration config = Hardcore.getInstance().getConfig();
    public static List<String> getPlayerNames() {
        return config.getStringList("playerNames");
    }
    public static boolean getState(String name) {
        return config.getBoolean("alive." + name);
    }
    public static List<String> getCmdNames() {
        return config.getStringList("commandNames");
    }
    public static List<String> getCmdList(String cmd) {
        return config.getStringList("commands." + cmd);
    }
    public static void set (String key, Object value) {
        config.set(key, value);
    }
}
