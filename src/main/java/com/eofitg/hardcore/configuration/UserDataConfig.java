package com.eofitg.hardcore.configuration;

import com.eofitg.hardcore.Hardcore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class UserDataConfig extends AbstractConfig {

    private static final Set<String> configNameList = new SettingsConfig().getConfig().getKeys(false);
    private static final List<String> playerIdList = MainConfig.getPlayerIdList();
    private static final List<String> uuidList = MainConfig.getUuidList();
    public static final String PARENT_PATH = Hardcore.getInstance().getDataFolder() + "\\userdata";
    private final Player player;
    private final String uuid;
    private final String name;

    public UserDataConfig(Player player, String uuid, String name) {
        super(PARENT_PATH, uuid + ".yml");
        this.player = player;
        this.uuid = uuid;
        this.name = name;
    }

    public void init() {

        setName(getPlayerName());
        setUuid(getPlayerUuid());
        setState(true);
        setPoint(0);
        setGameMode(getPlayer().getGameMode().toString());

        for (String s : configNameList) {
            setTriggered(s, 0);
            setLimit(s, 0);
            setMemory(s, Collections.emptyList());
        }

    }

    public Player getPlayer() {
        return this.player;
    }
    public String getPlayerUuid() {
        return this.uuid;
    }
    public String getPlayerName() {
        return this.name;
    }

    public boolean getState() {
        return this.getConfig().getBoolean("alive", true);
    }
    public double getPoint() {
        return this.getConfig().getDouble("point", 0.0);
    }
    public String getGameMode() {
        return this.getConfig().getString("game-mode", "SURVIVAL");
    }

    public int getTriggered(String configName) {
        return this.getConfig().getInt(configName + ".triggered", 0);
    }
    public int getLimit(String configName) {
        return this.getConfig().getInt(configName + ".limit", 0);
    }
    public List<String> getMemory(String configName) {
        return this.getConfig().getStringList(configName + ".memory");
    }

    public void set(String key, Object value) {
        this.getConfig().set(key, value);
    }
    public void setName(String name) {
        set("name", name);
    }
    public void setUuid(String uuid) {
        set("uuid", uuid);
    }

    public void setState(boolean state) {
        set("alive", state);
    }
    public void setPoint(double point) {
        set("point", point);
    }
    public void setGameMode(String gamemode) {
        set("game-mode", gamemode);
    }

    public void setTriggered(String configName, int triggered) {
        set(configName + ".triggered", triggered);
    }
    public void setLimit(String configName, int limit) {
        set(configName + ".limit", limit);
    }
    public void setMemory(String configName, List<String> memory) {
        set(configName + ".memory", memory);
    }

    public void reset() {           // Reset this.state
        if (exists()) {
            setState(true);
            setPoint(0);
            for (String s : configNameList) {
                setTriggered(s, 0);
                setLimit(s, 0);
                setMemory(s, Collections.emptyList());
            }
        }
    }
    public static void reset(String playerId) {         // Try to reset a designated player's state
        String uuid = playerId.split("/")[0];
        String name = playerId.split("/")[1];
        if (uuidList.contains(uuid)) {
            UserDataConfig userDataConfig = new UserDataConfig(Bukkit.getOfflinePlayer(uuid).getPlayer(), uuid, name);
            if (userDataConfig.exists()) {
                userDataConfig.reset();
                userDataConfig.save();
            }
        }
    }
    public static void reset_all() {                // Reset all players' state
        for (String playerId : playerIdList) {
            reset(playerId);
        }
    }

}
