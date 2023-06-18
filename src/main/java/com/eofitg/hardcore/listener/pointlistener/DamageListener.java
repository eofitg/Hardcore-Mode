package com.eofitg.hardcore.listener.pointlistener;

import com.eofitg.hardcore.configuration.UserDataConfig;
import com.eofitg.hardcore.listener.AbstractListener;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import static com.eofitg.hardcore.util.MathUtil.round_half_up;

public class DamageListener extends AbstractListener {

    @EventHandler
    public void getDamage(EntityDamageEvent e) {

        if (!state) {
            return;
        }

        if (e.getEntity() instanceof Player) {

            UserDataConfig userDataConfig = new UserDataConfig(((Player) e.getEntity()).getPlayer(), e.getEntity().getUniqueId().toString(), e.getEntity().getName());
            if (!userDataConfig.getState()) return;

            double heart = Math.round(((LivingEntity) e.getEntity()).getHealth() * 2) / 20.0;
            double damage = Math.min(heart, Math.round(e.getFinalDamage() * 2) / 20.0);
            double point = round_half_up(userDataConfig.getPoint() - damage, 2);
            userDataConfig.setPoint(point);
            userDataConfig.save();

        }

    }

    @EventHandler
    public void causeDamage(EntityDamageByEntityEvent e) {

        if (!state) {
            return;
        }

        if (e.getDamager() instanceof Player) {

            if (e.getEntity() instanceof LivingEntity) {

                // Why Mojang made ARMOR_STAND a damageable entity???
                if (e.getEntityType() == EntityType.ARMOR_STAND) return;

                UserDataConfig userDataConfig = new UserDataConfig(((Player) e.getDamager()).getPlayer(), e.getDamager().getUniqueId().toString(), e.getDamager().getName());
                if (!userDataConfig.getState()) return;

                double heart = Math.round(((LivingEntity) e.getEntity()).getHealth() * 2) / 20.0;
                double damage = Math.min(heart, Math.round(e.getFinalDamage() * 2) / 20.0);
                double point = round_half_up(userDataConfig.getPoint() + damage, 2);
                userDataConfig.setPoint(point);
                userDataConfig.save();

            }

        }

    }

}
