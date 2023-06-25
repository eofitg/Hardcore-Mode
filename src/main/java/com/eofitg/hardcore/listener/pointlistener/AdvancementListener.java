package com.eofitg.hardcore.listener.pointlistener;

import com.eofitg.hardcore.configuration.UserDataConfig;
import com.eofitg.hardcore.configuration.settingsconfig.AdvancementConfig;
import com.eofitg.hardcore.listener.PointListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import java.util.UUID;

public class AdvancementListener extends PointListener {

    private static final String configName = "advancement";
    private static final String[] advancements = {"minecraft:adventure/", "minecraft:end/", "minecraft:nether/", "minecraft:husbandry/", "minecraft:story/"};

    @EventHandler
    public void finishAdvancement(PlayerAdvancementDoneEvent e) {

        // If plugin is disabled
        if (!state) return;

        String key = e.getAdvancement().getKey().toString();
        // Only adventure advancement
        if (isAdvancement(key) == null) return;

        UUID uuid = e.getPlayer().getUniqueId();
        String name = e.getPlayer().getName();
        Player player = e.getPlayer();
        String advancement = key.substring(isAdvancement(key).length());

        // Get advancement settings config
        AdvancementConfig advancementConfig = new AdvancementConfig();
        // If advancement settings is disabled
        if (!advancementConfig.getState()) return;

        UserDataConfig userDataConfig = new UserDataConfig(player, uuid.toString(), name);
        // If this player is dead
        if (!userDataConfig.getState()) return;

        // This event's triggered state
        boolean triggered = userDataConfig.triggered(configName);
        // The object of this event's triggered state
        boolean objectTriggered = userDataConfig.triggered(configName, advancement);

        if (!triggered) {
            userDataConfig.setLimit(configName, 1);
            userDataConfig.setTriggered(configName, true);
            userDataConfig.save();
        }

        if (objectTriggered) return;

        double userPoint = userDataConfig.getPoint() + parsePoint(advancementConfig.getPoint());
        userDataConfig.setPoint(userPoint);
        userDataConfig.setTriggered(configName, advancement, true);
        userDataConfig.save();

    }

    private static String isAdvancement(String str) {
        for (String s : advancements) {
            if (str.startsWith(s)) {
                return s;
            }
        }
        return null;
    }

}
