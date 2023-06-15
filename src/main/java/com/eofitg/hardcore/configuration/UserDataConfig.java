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

public class UserDataConfig {

    private final File configFile;
    private final FileConfiguration config;
    private final Player player;
    private final boolean exists;
    private static final List<String> playerNames = MainConfig.getPlayerNames();

    public UserDataConfig(Player player, String name) {
        this.configFile = new File(Hardcore.getInstance().getDataFolder() + "\\userdata", name + ".yml");
        this.config = new YamlConfiguration();
        this.player = player;
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
        setName(player.getName());
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
        return config.getString("gamemode");
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
        set("gamemode", gamemode);
    }

    public void reset() {
        if (playerNames.contains(player.getName())) {
            setState(true);
            setPoint(0);
        }
    }
    public static void reset(String name) {
        if (playerNames.contains(name)) {
            UserDataConfig userDataConfig = new UserDataConfig(Bukkit.getOfflinePlayer(name).getPlayer(), name);
            if (userDataConfig.exists()) {
                userDataConfig.setState(true);
                userDataConfig.setPoint(0);
                userDataConfig.save();
            }
        }
    }
    public static void reset_all() {
        for (String s : playerNames) {
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
