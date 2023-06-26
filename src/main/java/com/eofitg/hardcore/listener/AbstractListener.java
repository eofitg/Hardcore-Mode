package com.eofitg.hardcore.listener;

import com.eofitg.hardcore.Hardcore;
import com.eofitg.hardcore.configuration.MainConfig;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public abstract class AbstractListener implements Listener {

    protected boolean state = MainConfig.getState();

    protected void reload() {
        this.state = MainConfig.getState();
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, Hardcore.getInstance());
    }

}
