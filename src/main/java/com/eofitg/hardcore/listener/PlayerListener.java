package com.eofitg.hardcore.listener;

import com.eofitg.hardcore.Hardcore;
import com.eofitg.hardcore.configuration.DefaultConfig;
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
        List<String> playerNames = DefaultConfig.getPlayerNames();
        if (!state) {
            if (playerNames.contains(playerName) && Hardcore.playerGameModeMap.containsKey(player)) {
                player.setGameMode(Hardcore.playerGameModeMap.get(player));
            }
            return;
        }
        if (!playerNames.contains(playerName)) {
            // Request memory space for the new player
            playerNames.add(playerName);
            DefaultConfig.setPlayerNames(playerNames);
            DefaultConfig.setPlayerState(playerName, true);
            DefaultConfig.setPoint(playerName, 0);
            Hardcore.playerGameModeMap.put(player, player.getGameMode());
            DefaultConfig.save();
            player.setGameMode(GameMode.SURVIVAL);
            player.sendTitle(ChatColor.BLUE + "WELCOME, NEW PLAYER!", ChatColor.GRAY + "You only have one life and do your best to survive!", 10, 150, 10);
        } else {
            boolean playerState = DefaultConfig.getPlayerState(playerName);
            if (!playerState) {
                player.setGameMode(GameMode.SPECTATOR);
                player.sendTitle(ChatColor.RED + "YOU ARE DIED!", ChatColor.GRAY + "Please wait for the reset!", 10, 150, 10);
            } else {
                Hardcore.playerGameModeMap.put(player, player.getGameMode());
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
        String playerName = e.getEntity().getPlayer().getName();
        DefaultConfig.setPlayerState(playerName, false);
        DefaultConfig.save();

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
        String playerName = e.getPlayer().getName();
        boolean playerState = DefaultConfig.getPlayerState(playerName);
        if (!playerState) {
            player.setGameMode(GameMode.SPECTATOR);
            player.sendTitle(ChatColor.RED + "YOU HAVE DIED IN THIS SEASON!", ChatColor.GRAY + "Please wait for the reset!", 10, 150, 10);
        } else {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendTitle(ChatColor.GREEN + "YOU ARE ALIVE!", ChatColor.GRAY + "Survive and earn more points!", 10, 150, 10);
        }
    }
}
