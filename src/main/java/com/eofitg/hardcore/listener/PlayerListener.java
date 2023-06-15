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
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;

import static com.eofitg.hardcore.Hardcore.leaderboards;

public class PlayerListener extends AbstractListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String playerName = e.getPlayer().getName();
        List<String> playerNames = MainConfig.getPlayerNames();
        if (!state) {
            if (playerNames.contains(playerName) && new UserDataConfig(player).exists()) {
                player.setGameMode(GameMode.valueOf(new UserDataConfig(player).getGameMode()));
            }
            return;
        }

        if (!playerNames.contains(playerName)) {
            // New player
            playerNames.add(playerName);
            MainConfig.setPlayerNames(playerNames);
            MainConfig.save();
            // Create user data config for the new player
            UserDataConfig userDataConfig = new UserDataConfig(player);
            userDataConfig.init();
            userDataConfig.save();
            // Change his/her game-mode and start
            player.setGameMode(GameMode.SURVIVAL);
            player.sendTitle(ChatColor.BLUE + "WELCOME, NEW PLAYER!", ChatColor.GRAY + "You only have one life and do your best to survive!", 10, 150, 10);
        } else {
            // Existing player
            boolean playerState = new UserDataConfig(player).getState();
            if (!playerState) {
                player.setGameMode(GameMode.SPECTATOR);
                player.sendTitle(ChatColor.RED + "YOU ARE DIED!", ChatColor.GRAY + "Please wait for the reset!", 10, 150, 10);
            } else {
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
        UserDataConfig userDataConfig = new UserDataConfig(e.getEntity());
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
        boolean playerState = new UserDataConfig(player).getState();
        if (!playerState) {
            player.setGameMode(GameMode.SPECTATOR);
            player.sendTitle(ChatColor.RED + "YOU HAVE DIED IN THIS SEASON!", ChatColor.GRAY + "Please wait for the reset!", 10, 150, 10);
        } else {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendTitle(ChatColor.GREEN + "YOU ARE ALIVE!", ChatColor.GRAY + "Survive and earn more points!", 10, 150, 10);
        }
    }

}
