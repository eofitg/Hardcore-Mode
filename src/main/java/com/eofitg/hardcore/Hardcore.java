package com.eofitg.hardcore;

import com.eofitg.hardcore.cmdoperation.CommandRegister;
import com.eofitg.hardcore.cmdoperation.TabCompleterRegister;
import com.eofitg.hardcore.listener.PlayerListener;
import com.eofitg.hardcore.listener.PointListener;
import com.eofitg.hardcore.util.Leaderboard;
import com.eofitg.hardcore.util.TestScordboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
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

    public static Map<Player, GameMode> playerGameModeMap = new HashMap<>();
    public static Map<Player, Leaderboard> leaderboards = new HashMap<>();


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        pluginName = instance.getName();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new PointListener(), this);
        CommandRegister.register(ConfigReader.getCmdNames());
        TabCompleterRegister.register(ConfigReader.getCmdNames());

        // Add all online players to the player list then toggle their game-mode
        if (ConfigReader.getState()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                String playerName = player.getName();
                List<String> playerNames = ConfigReader.getPlayerNames();
                if (!playerNames.contains(playerName)) {
                    // new player
                    playerNames.add(playerName);
                    ConfigReader.setPlayerNames(playerNames);
                    ConfigReader.setPlayerState(playerName, true);
                    Hardcore.getInstance().saveConfig();
                    playerGameModeMap.put(player, player.getGameMode());
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendTitle(ChatColor.BLUE + "WELCOME, NEW PLAYER!", ChatColor.GRAY + "You only have one life and do your best to survive!", 10, 150, 10);
                } else {
                    // existing player
                    boolean playerState = ConfigReader.getPlayerState(playerName);
                    playerGameModeMap.put(player, player.getGameMode());
                    if (!playerState) {
                        // player has dead
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
        if (!ConfigReader.getState()) {
            return;
        }

        // Restore players' game-mode to their original state
        for (Player player : Bukkit.getOnlinePlayers()) {
            String playerName = player.getName();
            List<String> playerNames = ConfigReader.getPlayerNames();
            if (playerNames.contains(playerName) && playerGameModeMap.containsKey(player)) {
                player.setGameMode(playerGameModeMap.get(player));
            }

            // Delete the leaderboard
            leaderboards.get(player).turnOff();
        }
        leaderboards.clear();
    }
}
