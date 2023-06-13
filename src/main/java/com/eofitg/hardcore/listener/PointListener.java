package com.eofitg.hardcore.listener;

import com.eofitg.hardcore.ConfigReader;
import com.eofitg.hardcore.Hardcore;
import com.eofitg.hardcore.util.MathUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PointListener extends AbstractListener implements Listener {
    @EventHandler
    public void getDamage(EntityDamageEvent e) {
        if(!state) {
            return;
        }
        if (e.getEntity() instanceof Player) {
            String name = e.getEntity().getName();
            if(!ConfigReader.getPlayerState(name)) return;

            double heart = Math.round(((LivingEntity) e.getEntity()).getHealth() * 2) / 20.0;
            double damage = Math.min(heart, Math.round(e.getFinalDamage() * 2) / 20.0);
            double point = MathUtil.round_half_up(ConfigReader.getPoint(name) - damage, 2);
            ConfigReader.setPoint(name, point);
            Hardcore.getInstance().saveConfig();
        }
    }
    @EventHandler
    public void causeDamage(EntityDamageByEntityEvent e) {
        if(!state) {
            return;
        }
        if (e.getDamager() instanceof Player) {
            if (e.getEntity() instanceof LivingEntity) {
                if (e.getEntityType() == EntityType.ARMOR_STAND) return;
                String name = e.getDamager().getName();
                if(!ConfigReader.getPlayerState(name)) return;

                double heart = Math.round(((LivingEntity) e.getEntity()).getHealth() * 2) / 20.0;
                double damage = Math.min(heart, Math.round(e.getFinalDamage() * 2) / 20.0);
                double point = MathUtil.round_half_up(ConfigReader.getPoint(name) + damage, 2);
                ConfigReader.setPoint(name, point);
                Hardcore.getInstance().saveConfig();
            }
        }
    }

}
