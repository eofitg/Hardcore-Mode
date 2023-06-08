package com.eofitg.hardcore.listener;

import com.eofitg.hardcore.ConfigReader;
import com.eofitg.hardcore.Hardcore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PointListener implements Listener {
    @EventHandler
    public void getDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            String name = e.getEntity().getName();
            int damage = (int) e.getDamage();
            //e.getEntity().sendMessage(name + " " + e.getCause() + " " + e.getDamage());
            int point = ConfigReader.getPoint(name) - damage;
            ConfigReader.setPoint(name, point);
            Hardcore.getInstance().saveConfig();
        }
    }
}
