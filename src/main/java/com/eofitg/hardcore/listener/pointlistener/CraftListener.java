package com.eofitg.hardcore.listener.pointlistener;

import com.eofitg.hardcore.configuration.UserDataConfig;
import com.eofitg.hardcore.configuration.settingsconfig.CraftConfig;
import com.eofitg.hardcore.listener.PointListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.UUID;

public class CraftListener extends PointListener {

    private static final String configName = "craft";

    @EventHandler
    public void onCraft(CraftItemEvent e) {

        // If plugin is disabled
        if (!state) return;

        UUID uuid = e.getInventory().getViewers().get(0).getUniqueId();
        String name = e.getInventory().getViewers().get(0).getName();
        Player player = Bukkit.getPlayer(uuid);
        String itemType = e.getCurrentItem().getType().toString();

        // Get craft settings config
        CraftConfig craftConfig = new CraftConfig();
        // If craft settings is disabled
        if (!craftConfig.getState()) return;

        UserDataConfig userDataConfig = new UserDataConfig(player, uuid.toString(), name);
        // If this player is dead
        if (!userDataConfig.getState()) return;

        // This event's triggered state
        boolean triggered = userDataConfig.triggered(configName);
        // The maximum triggered limit
        int userLimit = userDataConfig.getLimit(configName);
        // The number of times this item has been triggered
        int triggeredTime = userDataConfig.getTriggeredTime(configName, itemType);

        // If it is the first time this event is triggered
        // Writing this player's trigger limit (according to the settings) to this player's data config
        if (!triggered) {
            userLimit = parseLimit(craftConfig.getLimit());
            userDataConfig.setLimit(configName, userLimit);
            userDataConfig.setTriggered(configName, true);
            userDataConfig.save();
        }

        // If the triggered time has reached the triggered limit and it's not unlimited
        if (triggeredTime >= userLimit && userLimit != -1) return;

        double userPoint = userDataConfig.getPoint() + parsePoint(craftConfig.getPoint());
        userDataConfig.setPoint(userPoint);
        userDataConfig.setTriggeredTime(configName, itemType, triggeredTime + 1);
        userDataConfig.save();

    }

}
