package com.eofitg.hardcore.configuration;

import com.eofitg.hardcore.Hardcore;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class UserDataConfig {

    private final File configFile;
    private final FileConfiguration config;
    private final Player player;
    private final String uuid;
    private final String name;
    private final String playerId;
    private final boolean exists;
    private static final List<String> playerIdList = MainConfig.getPlayerIdList();

    public UserDataConfig(Player player, String uuid, String name) {
        this.configFile = new File(Hardcore.getInstance().getDataFolder() + "\\userdata", uuid + ".yml");
        this.config = new YamlConfiguration();
        this.player = player;
        this.uuid = uuid;
        this.name = name;
        this.playerId = uuid + "/" + name;
        this.exists = configFile.exists();
        if(!exists) {
            this.configFile.getParentFile().mkdirs();
            try {
                this.configFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            this.config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        setName(name);
        setState(true);
        setPoint(0);
        setGameMode(player.getGameMode().toString());
    }
    public boolean exists() {
        return exists;
    }

    public boolean getState() {
        return config.getBoolean("alive");
    }
    public double getPoint() {
        return config.getDouble("point");
    }
    public String getGameMode() {
        return config.getString("game-mode");
    }
    public void set(String key, Object value) {
        config.set(key, value);
    }
    public void setName(String name) {
        set("name", name);
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

    public void reset() {
        if (playerIdList.contains(playerId)) {
            setState(true);
            setPoint(0);
        }
    }
    public static void reset(String playerId) {
        if (playerIdList.contains(playerId)) {
            String uuid = playerId.split("/")[0];
            String name = playerId.split("/")[1];
            UserDataConfig userDataConfig = new UserDataConfig(Bukkit.getOfflinePlayer(uuid).getPlayer(), uuid, name);
            if (userDataConfig.exists()) {
                userDataConfig.setState(true);
                userDataConfig.setPoint(0);
                userDataConfig.save();
            }
        }
    }
    public static void reset_all() {
        for (String s : playerIdList) {
            reset(s);
        }
    }
    public void save(){
        try {
            config.save(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
