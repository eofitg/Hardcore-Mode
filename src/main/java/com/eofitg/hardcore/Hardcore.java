package com.eofitg.hardcore;

import com.eofitg.hardcore.cmdoperation.CommandRegister;
import com.eofitg.hardcore.cmdoperation.TabCompleterRegister;
import com.eofitg.hardcore.configuration.MainConfig;
import com.eofitg.hardcore.configuration.SettingsConfig;
import com.eofitg.hardcore.configuration.UserDataConfig;
import com.eofitg.hardcore.listener.PlayerListener;
import com.eofitg.hardcore.listener.PointListener;
import com.eofitg.hardcore.util.Leaderboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Hardcore extends JavaPlugin {

    private static Hardcore instance;
    private static String pluginName;
    public static Hardcore getInstance() {
        return instance;
    }
    public static String getPluginName() {
        return pluginName;
    }

    // Store players' leaderboards displayed on their interfaces
    public static Map<Player, Leaderboard> leaderboards = new HashMap<>();

    @Override
    public void onEnable() {

        instance = this;
        pluginName = instance.getName();

        // Save default resource config
        MainConfig.saveDefault();
        new SettingsConfig().saveDefault();

        // Register Listeners
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new PointListener(), this);

        // Register Commands
        CommandRegister.register(MainConfig.getCmdNames());
        TabCompleterRegister.register(MainConfig.getCmdNames());

        // Add all online players to the player list then toggle their game-mode
        if (MainConfig.getState()) {
            for (Player player : Bukkit.getOnlinePlayers()) {

                // playerId = playerUuid + '/' + playerName
                List<String> playerIdList = MainConfig.getPlayerIdList();
                List<String> uuidList = MainConfig.getUuidList();
                String uuid = player.getUniqueId().toString();
                String playerId = uuid + "/" + player.getName();

                // Create config file for this player
                UserDataConfig userDataConfig;
                userDataConfig = new UserDataConfig(player, player.getUniqueId().toString(), player.getName());

                if (!uuidList.contains(uuid)) {
                    // new player
                    playerIdList.add(playerId);
                    uuidList.add(uuid);
                    MainConfig.setPlayerIdList(playerIdList);
                    MainConfig.setUuidList(uuidList);
                    MainConfig.save();
                    //
                    userDataConfig.init();
                    userDataConfig.save();
                    //
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendTitle(ChatColor.BLUE + "WELCOME, NEW PLAYER!", ChatColor.GRAY + "You only have one life and do your best to survive!", 10, 150, 10);

                } else {
                    // existing player
                    if(!playerIdList.contains(playerId)) {
                        // Player name changed
                        for(int i = 0; i < playerIdList.size(); i ++) {
                            if(playerIdList.get(i).contains(uuid)) {
                                // Update player id (uuid + '/' + name)
                                playerIdList.set(i, playerId);
                            }
                        }
                        MainConfig.setPlayerIdList(playerIdList);
                        MainConfig.save();
                    }

                    boolean playerState = userDataConfig.getState();
                    if (!playerState) {
                        // player is dead
                        player.setGameMode(GameMode.SPECTATOR);
                        player.sendTitle(ChatColor.RED + "YOU HAVE DIED IN THIS SEASON!", ChatColor.GRAY + "Please wait for the reset!", 10, 150, 10);
                    } else {
                        // player is alive
                        player.setGameMode(GameMode.SURVIVAL);
                        player.sendTitle(ChatColor.GREEN + "YOU ARE ALIVE!", ChatColor.GRAY + "Survive and earn more points!", 10, 150, 10);
                    }

                }

                // Set the leaderboard
                Leaderboard leaderboard = new Leaderboard(Hardcore.getInstance(), player, "Leaderboard");
                leaderboards.put(player, leaderboard);
                leaderboard.startShowing();

            }
        }
    }

    @Override
    public void onDisable() {

        if (!MainConfig.getState()) {
            return;
        }

        // Restore players' game-mode to their original state
        for (Player player : Bukkit.getOnlinePlayers()) {
            List<String> playerIdList = MainConfig.getPlayerIdList();
            List<String> uuidList = MainConfig.getUuidList();
            String uuid = player.getUniqueId().toString();
            String name = player.getName();
            String playerId = uuid + "/" + name;

            if (uuidList.contains(uuid) && new UserDataConfig(player, uuid, name).exists()) {
                player.setGameMode(GameMode.valueOf(new UserDataConfig(player, uuid, name).getGameMode()));

                if (!playerIdList.contains(playerId)) {
                    // Player name changed
                    for (int i = 0; i < playerIdList.size(); i++) {
                        if (playerIdList.get(i).contains(uuid)) {
                            // Update player id (uuid + '/' + name)
                            playerIdList.set(i, playerId);
                        }
                    }

                    MainConfig.setPlayerIdList(playerIdList);
                    MainConfig.save();

                }
            }

            // Delete the leaderboard
            leaderboards.get(player).turnOff();

        }

        leaderboards.clear();
        instance = null;
        pluginName = null;

    }

}
