package com.eofitg.hardcore;

import com.eofitg.hardcore.cmdoperation.CommandRegister;
import com.eofitg.hardcore.cmdoperation.TabCompleterRegister;
import com.eofitg.hardcore.listener.PlayerListener;
import com.eofitg.hardcore.listener.PointListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Hardcore extends JavaPlugin {
    private static Hardcore instance;
    private static String pluginName;
    public static Hardcore getInstance() {
        return instance;
    }
    public static String getPluginName() {
        return pluginName;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        pluginName = instance.getName();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new PointListener(), this);
        CommandRegister.register(ConfigReader.getCmdNames());
        TabCompleterRegister.register(ConfigReader.getCmdNames());

        // 扫描所有在线玩家加入到玩家列表
        if (ConfigReader.getState()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                String playerName = player.getName();
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
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                String playerName = player.getName();
                List<String> playerNames = ConfigReader.getPlayerNames();
                if (playerNames.contains(playerName)) {
                    player.setGameMode(GameMode.SURVIVAL);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
        pluginName = null;
    }
}
