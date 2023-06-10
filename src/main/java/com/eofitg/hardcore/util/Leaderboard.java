package com.eofitg.hardcore.util;

import com.eofitg.hardcore.ConfigReader;
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
    private Plugin plugin;
    private Scoreboard scoreboard;
    private Objective objective;
    private Player player;
    private String title;
    private boolean isRun;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    private SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
    private List<Team> text;         // All text in the leaderboard
    private BukkitTask task;

    public Leaderboard(Plugin plugin, Player player, String title) {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.title = title;
        this.objective = scoreboard.registerNewObjective("LEADERBOARD", "dummy", ChatColor.DARK_BLUE + this.title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.player = player;
        this.isRun = false;
        this.plugin = plugin;
        text = Lists.newArrayList();
    }

    public void startShowing() {
        // 判断是否已经在运行
        if (isRun) {
            return;
        }
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
            List<String> playerNames = ConfigReader.getPlayerNames();
            for (int i = 0; i < playerNames.size() && i < N; i++) {
                String name = playerNames.get(i);
                playerState.put(name, ConfigReader.getPlayerState(name));
                playerPoint.put(name, ConfigReader.getPoint(name));
            }
            // sort playerPoint by point
            List<Map.Entry<String, Double>> pointRanking = new LinkedList<>(playerPoint.entrySet());
            // Collections.sort(pointRanking, Map.Entry.comparingByValue());
            Collections.sort(pointRanking, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

            for (int i = 0, j = 0; i < text.size(); i++) {
                Team row = text.get(i); // 获取每个Team
                if (i == 0) { // Date&Time HUD
                    Date date = new Date();
                    row.setPrefix(ChatColor.GRAY + format.format(date));
                    row.setSuffix(ChatColor.GRAY + " " + format2.format(date));
                    continue;
                }
                if (j < pointRanking.size()) {
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
                // i < text.size() && j < pointRanking.size() -> pointRanking.size() < i < text.size()
                row.setPrefix(ChatColor.GREEN + "#" + i + ": ");
                row.setSuffix(ChatColor.DARK_GRAY + "***" + ChatColor.WHITE + " - " + ChatColor.DARK_GRAY + "***");
            }

        }, 0L, 20L);
    }

    public void turnOff() {
        isRun = false;
        task.cancel();
        player.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard());
    }
}
