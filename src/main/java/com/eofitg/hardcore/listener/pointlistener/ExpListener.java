package com.eofitg.hardcore.listener.pointlistener;

import com.eofitg.hardcore.configuration.UserDataConfig;
import com.eofitg.hardcore.configuration.settingsconfig.ExpConfig;
import com.eofitg.hardcore.listener.PointListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import java.util.UUID;

public class ExpListener extends PointListener {

    private static final String configName = "exp";
    private static final String[] type = {"get", "use"};

    @EventHandler
    public void getExpPoint(PlayerExpChangeEvent e) {

        // If plugin is disabled
        if (!state) return;

        UUID uuid = e.getPlayer().getUniqueId();
        String name = e.getPlayer().getName();
        Player player = e.getPlayer();

        // Get exp settings config
        ExpConfig expConfig = new ExpConfig();
        // If exp settings is disabled
        if (!expConfig.getState()) return;
        // If the judgment is based on the level
        if (expConfig.byLevel(type[0])) return;

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
            userLimit = parseLimit(expConfig.getLimit(type[0]));
            userDataConfig.setLimit(configName, type[0], userLimit);
            userDataConfig.setTriggered(configName, type[0], true);
            userDataConfig.save();
        }

        // If the triggered time has reached the triggered limit and it's not unlimited
        if (triggeredTime >= userLimit && userLimit != -1) return;

        double point = parsePoint(expConfig.getPoint(type[0]));
        int expPoint = e.getAmount();
        double userPoint = userDataConfig.getPoint() + (double) expPoint * point;
        userDataConfig.setPoint(userPoint);
        userDataConfig.setTriggeredTime(configName, type[0], triggeredTime + 1);
        userDataConfig.save();

    }

    @EventHandler
    public void onExpLevelChange(PlayerLevelChangeEvent e) {

        // If plugin is disabled
        if (!state) return;

        UUID uuid = e.getPlayer().getUniqueId();
        String name = e.getPlayer().getName();
        Player player = e.getPlayer();
        String type_ = "";

        // Get exp settings config
        ExpConfig expConfig = new ExpConfig();
        // If exp settings is disabled
        if (!expConfig.getState()) return;

        int expLevel = e.getNewLevel() - e.getOldLevel();
        if (expLevel >= 0) { // Get levels
            type_ = type[0];
        } else { // Use levels
            type_ = type[1];
            expLevel = -expLevel;
        }

        // If the judgment is not based on the level
        if (!expConfig.byLevel(type_)) return;

        UserDataConfig userDataConfig = new UserDataConfig(player, uuid.toString(), name);
        // If this player is dead
        if (!userDataConfig.getState()) return;

        // This event's triggered state
        boolean triggered = userDataConfig.triggered(configName, type_);
        // The maximum triggered limit
        int userLimit = userDataConfig.getLimit(configName, type_);
        // The number of times this event has been triggered
        int triggeredTime = userDataConfig.getTriggeredTime(configName, type_);

        // If it is the first time this event is triggered
        // Writing this player's trigger limit (according to the settings) to this player's data config
        if (!triggered) {
            userLimit = parseLimit(expConfig.getLimit(type_));
            userDataConfig.setLimit(configName, type_, userLimit);
            userDataConfig.setTriggered(configName, type_, true);
            userDataConfig.save();
        }

        // If the triggered time has reached the triggered limit and it's not unlimited
        if (triggeredTime >= userLimit && userLimit != -1) return;

        double point = parsePoint(expConfig.getPoint(type_));
        double userPoint = userDataConfig.getPoint() + (double) expLevel * point;
        userDataConfig.setPoint(userPoint);
        userDataConfig.setTriggeredTime(configName, type_, triggeredTime + 1);
        userDataConfig.save();

    }

}
