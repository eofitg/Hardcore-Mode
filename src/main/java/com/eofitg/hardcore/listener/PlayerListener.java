package com.eofitg.hardcore.listener;

import com.eofitg.hardcore.Hardcore;
import com.eofitg.hardcore.configuration.MainConfig;
import com.eofitg.hardcore.configuration.UserDataConfig;
import com.eofitg.hardcore.util.Leaderboard;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;

import static com.eofitg.hardcore.Hardcore.leaderboards;

public class PlayerListener extends AbstractListener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        List<String> playerIdList = MainConfig.getPlayerIdList();
        List<String> uuidList = MainConfig.getUuidList();
        String uuid = player.getUniqueId().toString();
        String name = player.getName();
        String playerId = uuid + "/" + name;
        if (!state) {
            if (uuidList.contains(uuid) && new UserDataConfig(player, uuid, name).exists()) {
                player.setGameMode(GameMode.valueOf(new UserDataConfig(player, uuid, name).getGameMode()));
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
            }
            return;
        }

        if (!uuidList.contains(uuid)) {
            // New player
            playerIdList.add(playerId);
            uuidList.add(uuid);
            MainConfig.setPlayerIdList(playerIdList);
            MainConfig.setUuidList(uuidList);
            MainConfig.save();
            // Create user data config for the new player
            UserDataConfig userDataConfig = new UserDataConfig(player, uuid, name);
            userDataConfig.init();
            userDataConfig.save();
            // Change his/her game-mode and start
            player.setGameMode(GameMode.SURVIVAL);
            player.sendTitle(ChatColor.BLUE + "WELCOME, NEW PLAYER!", ChatColor.GRAY + "You only have one life and do your best to survive!", 10, 150, 10);
        } else {
            // Existing player
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
            boolean playerState = new UserDataConfig(player, uuid, name).getState();
            if (!playerState) {
                // player is dead
                player.setGameMode(GameMode.SPECTATOR);
                player.sendTitle(ChatColor.RED + "YOU ARE DIED!", ChatColor.GRAY + "Please wait for the reset!", 10, 150, 10);
            } else {
                // player is alive
                player.setGameMode(GameMode.SURVIVAL);
                player.sendTitle(ChatColor.GREEN + "YOU ARE ALIVE!", ChatColor.GRAY + "Survive and earn more points!", 10, 150, 10);
            }
        }

        // Set leaderboard for this player
        if (leaderboards.containsKey(player)) {
            leaderboards.get(player).startShowing();
        } else {
            Leaderboard leaderboard = new Leaderboard(Hardcore.getInstance(), player, "Leaderboard");
            leaderboards.put(player, leaderboard);
            leaderboard.startShowing();
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (leaderboards.containsKey(player)) {
            leaderboards.get(player).turnOff();
            leaderboards.remove(player);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (!state) {
            return;
        }

        // Write config
        UserDataConfig userDataConfig = new UserDataConfig(e.getEntity(), e.getEntity().getUniqueId().toString(), e.getEntity().getName());
        userDataConfig.setState(false);
        userDataConfig.save();

        // Send player's location when dead
        Location location = e.getEntity().getLocation();
        String world = e.getEntity().getWorld().getName();
        String x = location.getBlockX() + ", ";
        String y = location.getBlockY() + ", ";
        int z = location.getBlockZ();
        String location_ = ChatColor.GRAY + "[" + ChatColor.AQUA + x + y + z + ChatColor.GRAY + "] ";
        e.setDeathMessage(e.getDeathMessage() + " @" + location_ + ChatColor.WHITE + "in " + ChatColor.GREEN + world + ChatColor.RED + " RIP D: ");
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        if (!state) {
            return;
        }
        Player player = e.getPlayer();
        boolean playerState = new UserDataConfig(player, player.getUniqueId().toString(), player.getName()).getState();
        if (!playerState) {
            player.setGameMode(GameMode.SPECTATOR);
            player.sendTitle(ChatColor.RED + "YOU HAVE DIED IN THIS SEASON!", ChatColor.GRAY + "Please wait for the reset!", 10, 150, 10);
        } else {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendTitle(ChatColor.GREEN + "YOU ARE ALIVE!", ChatColor.GRAY + "Survive and earn more points!", 10, 150, 10);
        }
    }

    public static class PointListener extends AbstractListener {
    }
}
