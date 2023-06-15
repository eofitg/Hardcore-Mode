package com.eofitg.hardcore.util;

import com.eofitg.hardcore.configuration.MainConfig;
import com.eofitg.hardcore.configuration.UserDataConfig;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.text.SimpleDateFormat;
import java.util.*;

public class Leaderboard {

    private final Plugin plugin;
    private String title;
    private final Scoreboard scoreboard;
    private final Objective objective;
    private final Player player;
    private boolean isRun;
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    private final SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
    private final List<Team> text;         // All text in the leaderboard
    private BukkitTask task;

    public Leaderboard(Plugin plugin, Player player, String title) {
        this.plugin = plugin;
        this.title = title;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("LEADERBOARD", "dummy", ChatColor.DARK_BLUE + this.title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.player = player;
        this.isRun = false;
        text = Lists.newArrayList();
    }

    public void startShowing() {
        // Check out if this leaderboard is running
        if (isRun) {
            return;
        }
        // Make sure this player's scoreboard is valid
        if (player == null || !player.isOnline()) {
            return;
        }
        isRun = true;
        player.setScoreboard(scoreboard);

        int N = 7;          // The number of players displayed on the leaderboard
        List<String> tempList = Lists.newArrayList();
        for (int i = 0; i <= N; i++) {
            tempList.add("§" + ChatColor.values()[i].getChar());
        }

        for (int i = 0; i <= N; i++) {
            Team row = scoreboard.registerNewTeam("" + i);
            row.addEntry(tempList.get(i));
            objective.getScore(tempList.get(i)).setScore(N-i);
            text.add(row);
        }

        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!isRun) {
                return;
            }

            Map<String, Boolean> playerState = new HashMap<>();
            Map<String, Double> playerPoint = new HashMap<>();
            List<String> playerIdList = MainConfig.getPlayerIdList();
            for (int i = 0; i < playerIdList.size() && i < N; i++) {
                String uuid = playerIdList.get(i).split("/")[0];
                String name = playerIdList.get(i).split("/")[1];
                UserDataConfig userDataConfig = new UserDataConfig(Bukkit.getOfflinePlayer(name).getPlayer(), uuid, name);
                playerState.put(name, userDataConfig.getState());
                playerPoint.put(name, userDataConfig.getPoint());
            }
            // sort playerPoint by point
            List<Map.Entry<String, Double>> pointRanking = new LinkedList<>(playerPoint.entrySet());
            // Collections.sort(pointRanking, Map.Entry.comparingByValue());
            Collections.sort(pointRanking, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

            for (int i = 0, j = 0; i < text.size(); i++) {
                Team row = text.get(i); // Get every Team
                if (i == 0) {
                    // Date & Time HUD
                    Date date = new Date();
                    row.setPrefix(ChatColor.GRAY + format.format(date));
                    row.setSuffix(ChatColor.GRAY + " " + format2.format(date));
                    continue;
                }
                if (j < pointRanking.size()) {
                    // Fill in the players' data on the leaderboard
                    String name = pointRanking.get(j).getKey();
                    double points = pointRanking.get(j).getValue();
                    String suffix = ChatColor.GOLD + name + ChatColor.WHITE + " - " + ChatColor.GOLD + points;
                    if (playerState.get(name)) {
                        suffix = suffix + ChatColor.GREEN + " √";
                    } else {
                        suffix = suffix + ChatColor.RED + " ×";
                    }
                    row.setPrefix(ChatColor.GREEN + "#" + i + ": ");
                    row.setSuffix(suffix);
                    j++;
                    continue;
                }
                // i < text.size() && j < pointRanking.size() -> pointRanking.size() < i < text.size() , mean last rows have no last players to fill
                row.setPrefix(ChatColor.GREEN + "#" + i + ": ");
                row.setSuffix(ChatColor.DARK_GRAY + "****" + ChatColor.WHITE + " - " + ChatColor.DARK_GRAY + "****");
            }

        }, 0L, 20L);
    }

    public void turnOff() {
        isRun = false;
        task.cancel();
        player.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard());
    }

}
