package com.eofitg.hardcore.listener.pointlistener;

import com.eofitg.hardcore.configuration.UserDataConfig;
import com.eofitg.hardcore.configuration.settingsconfig.KillConfig;
import com.eofitg.hardcore.listener.PointListener;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;

public class KillListener extends PointListener {

    private static final String configName = "kill";

    @EventHandler
    public void onKill(EntityDeathEvent e) {

        // If plugin is disabled
        if (!state) return;

        if (e.getEntity().getKiller() == null && !(e.getEntity().getKiller() instanceof Arrow)) return;

        UUID uuid = null;
        String name = "";
        Player player = null;
        String entityType = e.getEntityType().toString();

        if (e.getEntity().getKiller() instanceof Arrow) {
            Arrow a = (Arrow) e.getEntity().getKiller();
            if (a.getShooter() instanceof Player) {
                uuid = ((Player) a.getShooter()).getUniqueId();
                name = ((Player) a.getShooter()).getName();
                player = ((Player) a.getShooter()).getPlayer();
            }
        } else if (e.getEntity().getKiller() != null) {
            uuid = e.getEntity().getKiller().getUniqueId();
            name = e.getEntity().getKiller().getName();
            player = e.getEntity().getKiller().getPlayer();
        }

        // Get kill settings config
        KillConfig killConfig = new KillConfig();
        // If kill settings is disabled
        if (!killConfig.getState()) return;
        // Check whether players can kill others to earn points
        if (!killConfig.allowPvp() && e.getEntity() instanceof Player) return;

        if (uuid == null || name.equals("") || player == null) { // Check out
            return;
        }

        UserDataConfig userDataConfig = new UserDataConfig(player, uuid.toString(), name);
        // If this player is dead
        if (!userDataConfig.getState()) return;

        // This event's triggered state
        boolean triggered = userDataConfig.triggered(configName);
        // The maximum triggered limit
        int userLimit = userDataConfig.getLimit(configName);
        // The number of times this event has been triggered
        int triggeredTime = userDataConfig.getTriggeredTime(configName, entityType);

        // If it is the first time this event is triggered
        // Writing this player's trigger limit (according to the settings) to this player's data config
        if (!triggered) {
            userLimit = parseLimit(killConfig.getLimit());
            userDataConfig.setLimit(configName, userLimit);
            userDataConfig.setTriggered(configName, true);
            userDataConfig.save();
        }

        // If the triggered time has reached the triggered limit and it's not unlimited
        if (triggeredTime >= userLimit && userLimit != -1) return;

        double userPoint = userDataConfig.getPoint() + parsePoint(killConfig.getPoint());
        userDataConfig.setPoint(userPoint);
        userDataConfig.setTriggeredTime(configName,  entityType,triggeredTime + 1);
        userDataConfig.save();

    }

}
