package com.eofitg.hardcore.configuration;

import com.eofitg.hardcore.Hardcore;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class MainConfig {

    private static FileConfiguration config = Hardcore.getInstance().getConfig();
    private static List<String> playerNames = config.getStringList("playerNames");
    private static List<String> cmdNames = config.getStringList("commandNames");
    private static boolean state = config.getBoolean("enable");

    public static List<String> getPlayerNames() {
        return playerNames;
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
    public static void setPlayerNames(List<String> names) {
        set("playerNames", names);
    }
    public static void setPlayerState(String name, boolean state) {
        set("alive." + name, state);
    }
    public static void setState(boolean state) {
        set("enable", state);
    }
    public static void setPoint(String name, double point) {
        set("point." + name, point);
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
    public static void save() {
        Hardcore.getInstance().saveConfig();
    }
    public static void saveDefault() {
        Hardcore.getInstance().saveDefaultConfig();
    }

}
