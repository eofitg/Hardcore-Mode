package com.eofitg.hardcore.configuration;

import com.eofitg.hardcore.Hardcore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
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
            setTriggered(s, false);
            setLimit(s, 0);
            setTriggeredTime(s, null);
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

    // Check out if this player is alive
    public boolean getState() {
        return this.getConfig().getBoolean("alive", true);
    }
    // Get this player's survival point
    public double getPoint() {
        return this.getConfig().getDouble("point", 0.0);
    }
    // Get this player's game-mode
    public String getGameMode() {
        return this.getConfig().getString("game-mode", "SURVIVAL");
    }

    // Check out if this event has been triggered
    public boolean triggered(String configName) {
        return this.getConfig().getBoolean(configName + ".triggered", false);
    }
    // Check out if this object of this event has been triggered
    public boolean triggered(String configName, String object) {
        return this.getConfig().getBoolean(configName + ".triggered." + object, false);
    }
    // Get the number of times this object has been triggered in this event
    public int getTriggeredTime(String configName, String object) {
        return this.getConfig().getInt(configName + ".triggered-time." + object, 0);
    }
    // Get the maximum triggered limit of this event
    public int getLimit(String configName) {
        return this.getConfig().getInt(configName + ".limit", 0);
    }
    // Get this object's maximum triggered limit of this event
    public int getLimit(String configName, String object) {
        return this.getConfig().getInt(configName + ".limit." + object, 0);
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

    // Set this player's survival state
    public void setState(boolean state) {
        set("alive", state);
    }
    // Set this player's survival point
    public void setPoint(double point) {
        set("point", point);
    }
    // Set this player's game-mode
    public void setGameMode(String gamemode) {
        set("game-mode", gamemode);
    }

    // Set this event's triggered state
    public void setTriggered(String configName, Boolean state) {
        set(configName + ".triggered", state);
    }
    // Set this object's triggered state of this event
    public void setTriggered(String configName, String object, Boolean state) {
        set(configName + ".triggered." + object, state);
    }
    // Set the number of times this object has been triggered in this event
    public void setTriggeredTime(String configName, String object, int triggeredTime) {
        set(configName + ".triggered-time." + object, triggeredTime);
    }
    public void setTriggeredTime(String configName, Objects objects) {
        set(configName + ".triggered-time", objects);
    }
    // Set the maximum triggered limit of this event
    public void setLimit(String configName, int limit) {
        set(configName + ".limit", limit);
    }
    // Set this object's maximum triggered limit of this event
    public void setLimit(String configName, String object, int limit) {
        set(configName + ".limit." + object, limit);
    }

    public void reset() {           // Reset this.state
        if (exists()) {
            setState(true);
            setPoint(0);
            for (String s : configNameList) {
                setTriggered(s, false);
                setLimit(s, 0);
                setTriggeredTime(s, null);
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
