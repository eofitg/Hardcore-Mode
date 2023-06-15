package com.eofitg.hardcore.configuration;

import com.eofitg.hardcore.Hardcore;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class MainConfig {

    private static final FileConfiguration config = Hardcore.getInstance().getConfig();
    private static final List<String> playerIdList = config.getStringList("playerIdList");
    private static final List<String> uuidList = config.getStringList("uuidList");
    private static final List<String> cmdNames = config.getStringList("commandNames");
    private static final boolean state = config.getBoolean("enable");

    public static List<String> getPlayerIdList() {
        return playerIdList;
    }
    public static List<String> getUuidList() {
        return uuidList;
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
    public static void setPlayerIdList(List<String> playerIdList) {
        set("playerIdList", playerIdList);
    }
    public static void setUuidList(List<String> uuidList) {
        set("uuidList", uuidList);
    }
    public static void setState(boolean state) {
        set("enable", state);
    }

    public static void save() {
        Hardcore.getInstance().saveConfig();
    }
    public static void saveDefault() {
        Hardcore.getInstance().saveDefaultConfig();
    }

}
