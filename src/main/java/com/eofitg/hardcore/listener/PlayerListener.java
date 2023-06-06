package com.eofitg.hardcore.listener;

import com.eofitg.hardcore.ConfigReader;
import com.eofitg.hardcore.Hardcore;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;

public class PlayerListener implements Listener {
    boolean state = ConfigReader.getState();
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!state) {
            return;
        }
        // 新玩家加入时为该玩家申请存储空间
        Player player = e.getPlayer();
        String playerName = e.getPlayer().getName();
        List<String> playerNames = ConfigReader.getPlayerNames();
        if (!playerNames.contains(playerName)) {
            playerNames.add(playerName);
            ConfigReader.set("playerNames", playerNames);
            ConfigReader.set("alive." + playerName, true);
            Hardcore.getInstance().saveConfig();
            player.setGameMode(GameMode.SURVIVAL);
            player.sendTitle(ChatColor.BLUE + "WELCOME, NEW PLAYER!", ChatColor.GRAY + "You only have one life and do your best to survive!", 10, 150, 10);
        } else {
            boolean playerState = ConfigReader.getPlayerState(playerName);
            if (!playerState) {
                player.setGameMode(GameMode.SPECTATOR);
                player.sendTitle(ChatColor.RED + "YOU HAVE DIED IN THIS SEASON!", ChatColor.GRAY + "Please wait for the reset!", 10, 150, 10);
            } else {
                player.setGameMode(GameMode.SURVIVAL);
                player.sendTitle(ChatColor.GREEN + "YOU ARE ALIVE!", ChatColor.GRAY + "Survive and earn more points!", 10, 150, 10);
            }
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (!state) {
            return;
        }
        // 玩家死亡时触发极限模式机制
        String playerName = e.getEntity().getPlayer().getName();
        ConfigReader.set("alive." + playerName, false);
        Hardcore.getInstance().saveConfig();

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
        boolean playerState = ConfigReader.getPlayerState(playerName);
        if (!playerState) {
            player.setGameMode(GameMode.SPECTATOR);
            player.sendTitle(ChatColor.RED + "YOU HAVE DIED IN THIS SEASON!", ChatColor.GRAY + "Please wait for the reset!", 10, 150, 10);
        } else {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendTitle(ChatColor.GREEN + "YOU ARE ALIVE!", ChatColor.GRAY + "Survive and earn more points!", 10, 150, 10);
        }
    }
}
