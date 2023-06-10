package com.eofitg.hardcore.listener;

import com.eofitg.hardcore.ConfigReader;
import com.eofitg.hardcore.Hardcore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.text.DecimalFormat;

public class PointListener implements Listener {
    private final boolean state = ConfigReader.getState();
    @EventHandler
    public void getDamage(EntityDamageEvent e) {
        if(!state) {
            return;
        }
        if (e.getEntity() instanceof Player) {
            String name = e.getEntity().getName();
            double damage = Math.ceil(e.getFinalDamage());
            //e.getEntity().sendMessage(name + " " + e.getCause() + " " + e.getDamage());
            double point = ConfigReader.getPoint(name) - damage;
            ConfigReader.setPoint(name, point);
            Hardcore.getInstance().saveConfig();
        }
    }
}
