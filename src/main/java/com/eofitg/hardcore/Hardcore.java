package com.eofitg.hardcore;

import com.eofitg.hardcore.cmdoperation.CommandRegister;
import com.eofitg.hardcore.cmdoperation.TabCompleterRegister;
import com.eofitg.hardcore.configuration.DefaultConfig;
import com.eofitg.hardcore.listener.PlayerListener;
import com.eofitg.hardcore.listener.PointListener;
import com.eofitg.hardcore.util.Leaderboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Hardcore extends JavaPlugin {
    private static Hardcore instance;
    private static String pluginName;
    public static Hardcore getInstance() {
        return instance;
    }
    public static String getPluginName() {
        return pluginName;
    }

    // Store players' game-mode before the plugin turns on
    public static Map<Player, GameMode> playerGameModeMap = new HashMap<>();
    // Store players' leaderboards displayed on their interfaces
    public static Map<Player, Leaderboard> leaderboards = new HashMap<>();


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        pluginName = instance.getName();
        saveDefaultConfig();
        // Register Listeners
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new PointListener(), this);
        // Register Commands
        CommandRegister.register(DefaultConfig.getCmdNames());
        TabCompleterRegister.register(DefaultConfig.getCmdNames());

        // Add all online players to the player list then toggle their game-mode
        if (DefaultConfig.getState()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                String playerName = player.getName();
                List<String> playerNames = DefaultConfig.getPlayerNames();
                if (!playerNames.contains(playerName)) {
                    // new player
                    playerNames.add(playerName);
                    DefaultConfig.setPlayerNames(playerNames);
                    DefaultConfig.setPlayerState(playerName, true);
                    DefaultConfig.save();
                    playerGameModeMap.put(player, player.getGameMode());
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendTitle(ChatColor.BLUE + "WELCOME, NEW PLAYER!", ChatColor.GRAY + "You only have one life and do your best to survive!", 10, 150, 10);
                } else {
                    // existing player
                    boolean playerState = DefaultConfig.getPlayerState(playerName);
                    playerGameModeMap.put(player, player.getGameMode());
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
        // Plugin shutdown logic
        instance = null;
        pluginName = null;
        if (!DefaultConfig.getState()) {
            return;
        }

        // Restore players' game-mode to their original state
        for (Player player : Bukkit.getOnlinePlayers()) {
            String playerName = player.getName();
            List<String> playerNames = DefaultConfig.getPlayerNames();
            if (playerNames.contains(playerName) && playerGameModeMap.containsKey(player)) {
                player.setGameMode(playerGameModeMap.get(player));
            }

            // Delete the leaderboard
            leaderboards.get(player).turnOff();
        }
        leaderboards.clear();
    }
}
