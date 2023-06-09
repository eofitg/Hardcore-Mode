package com.eofitg.hardcore.listener.pointlistener;

import com.eofitg.hardcore.configuration.UserDataConfig;
import com.eofitg.hardcore.configuration.settingsconfig.DamageConfig;
import com.eofitg.hardcore.listener.PointListener;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

import static com.eofitg.hardcore.util.MathUtil.round_half_up;

public class DamageListener extends PointListener {

    private static final String configName = "damage";
    private static final String[] type = {"get", "cause"};

    @EventHandler
    public void getDamage(EntityDamageEvent e) {

        // If plugin is disabled
        if (!state) return;

        if (!(e.getEntity() instanceof Player)) return;

        UUID uuid = e.getEntity().getUniqueId();
        String name = e.getEntity().getName();
        Player player = ((Player) e.getEntity()).getPlayer();

        // Get damage settings config
        DamageConfig damageConfig = new DamageConfig();
        // If damage settings is disabled
        if (!damageConfig.getState()) return;
        // Check whether players can attack others to earn points
        if (!damageConfig.allowPvp() && e instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) e).getDamager() instanceof Player) {
                return;
            }
        }

        UserDataConfig userDataConfig = new UserDataConfig(player, uuid.toString(), name);
        // If this player is dead
        if (!userDataConfig.getState()) return;

        // This event's triggered state
        boolean triggered = userDataConfig.triggered(configName, type[0]);
        // The maximum triggered limit
        int userLimit = userDataConfig.getLimit(configName, type[0]);
        // The number of times this event has been triggered
        int triggeredTime = userDataConfig.getTriggeredTime(configName, type[0]);

        // If it is the first time this event is triggered
        // Writing this player's trigger limit (according to the settings) to this player's data config
        if (!triggered) {
            userLimit = parseLimit(damageConfig.getLimit(type[0]));
            userDataConfig.setLimit(configName, type[0], userLimit);
            userDataConfig.setTriggered(configName, type[0], true);
            userDataConfig.save();
        }

        // If the triggered time has reached the triggered limit and it's not unlimited
        if (triggeredTime >= userLimit && userLimit != -1) return;

        double point = parsePoint(damageConfig.getPoint(type[0]));
        double heart = Math.round(((LivingEntity) e.getEntity()).getHealth() * 2) / 2.0;
        double damage = Math.min(heart, Math.round(e.getFinalDamage() * 2) / 2.0);
        double userPoint = round_half_up(userDataConfig.getPoint() + damage * point, 2);
        userDataConfig.setPoint(userPoint);
        userDataConfig.setTriggeredTime(configName, type[0], triggeredTime + 1);
        userDataConfig.save();

    }

    @EventHandler
    public void causeDamage(EntityDamageByEntityEvent e) {

        // If plugin is disabled
        if (!state) return;

        if (!(e.getDamager() instanceof Player) && !(e.getDamager() instanceof Arrow)) return;
        if (!(e.getEntity() instanceof LivingEntity)) return;
        // Why Mojang made ARMOR_STAND a damageable entity???
        if (e.getEntityType() == EntityType.ARMOR_STAND) return;

        UUID uuid = null;
        String name = "";
        Player player = null;

        if (e.getDamager() instanceof Arrow) {
            Arrow a = (Arrow) e.getDamager();
            if (a.getShooter() instanceof Player) {
                uuid = ((Player) a.getShooter()).getUniqueId();
                name = ((Player) a.getShooter()).getName();
                player = ((Player) a.getShooter()).getPlayer();
            }
        } else if (e.getDamager() instanceof Player) {
            uuid = e.getDamager().getUniqueId();
            name = e.getDamager().getName();
            player = ((Player) e.getDamager()).getPlayer();
        }

        // Get damage settings config
        DamageConfig damageConfig = new DamageConfig();
        // If damage settings is disabled
        if (!damageConfig.getState()) return;
        // Check whether players can attack others to earn points
        if (!damageConfig.allowPvp() && e.getEntity() instanceof Player) return;

        if (uuid == null || name.equals("") || player == null) { // Check out
            return;
        }

        UserDataConfig userDataConfig = new UserDataConfig(player, uuid.toString(), name);
        // If this player is dead
        if (!userDataConfig.getState()) return;

        // This event's triggered state
        boolean triggered = userDataConfig.triggered(configName, type[1]);
        // The maximum triggered limit
        int userLimit = userDataConfig.getLimit(configName, type[1]);
        // The number of times this event has been triggered
        int triggeredTime = userDataConfig.getTriggeredTime(configName, type[1]);

        // If it is the first time this event is triggered
        // Writing this player's trigger limit (according to the settings) to this player's data config
        if (!triggered) {
            userLimit = parseLimit(damageConfig.getLimit(type[1]));
            userDataConfig.setLimit(configName, type[1], userLimit);
            userDataConfig.setTriggered(configName, type[1], true);
            userDataConfig.save();
        }

        // If the triggered time has reached the triggered limit and it's not unlimited
        if (triggeredTime >= userLimit && userLimit != -1) return;

        double point = parsePoint(damageConfig.getPoint(type[1]));
        double heart = Math.round(((LivingEntity) e.getEntity()).getHealth() * 2) / 2.0;
        double damage = Math.min(heart, Math.round(e.getFinalDamage() * 2) / 2.0);
        double userPoint = round_half_up(userDataConfig.getPoint() + damage * point, 2);
        userDataConfig.setPoint(userPoint);
        userDataConfig.setTriggeredTime(configName, type[1], triggeredTime + 1);
        userDataConfig.save();

    }

}
