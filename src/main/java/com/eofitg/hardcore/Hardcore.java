package com.eofitg.hardcore;

import com.eofitg.hardcore.cmdoperation.CommandRegister;
import com.eofitg.hardcore.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Hardcore extends JavaPlugin {
    private static Hardcore instance;
    public static Hardcore getInstance() {
        return instance;
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        CommandRegister.register(ConfigReader.getCmdNames());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
    }
}
