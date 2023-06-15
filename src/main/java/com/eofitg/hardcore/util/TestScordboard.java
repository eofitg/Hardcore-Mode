package com.eofitg.hardcore.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.Lists;

public class TestScordboard {

    private Scoreboard scoreboard;
    private Objective objective;
    private String title;
    private Player player;
    private boolean isRun;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    private SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
    // 用作runnable的主类实例
    private Plugin plugin;
    /**
     * 用于保存所有的Team
     */
    private List<Team> timers;
    private BukkitTask task;

    public TestScordboard(Plugin plugin, Player player, String title) {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.title = title;
        this.objective = scoreboard.registerNewObjective(player.getName(), "dummy", this.title.replace("&", "§"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.player = player;
        this.isRun = false;
        this.plugin = plugin;
        timers = Lists.newArrayList();
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

        // 用于保存前15位的内容
        List<String> tempList = Lists.newArrayList();
        for (int i = 0; i <= 10; i++) {
            tempList.add("§" + ChatColor.values()[i].getChar());
        }

        for (int i = 0; i <= 10; i++) {
            // 注册Team时使用 数字的形式就行
            Team timer = scoreboard.registerNewTeam("" + i);
            // addEntry只是作为一个标识符, 用于getScore时的识别
            timer.addEntry(tempList.get(i));
            // getScore 刚才的标识符
            objective.getScore(tempList.get(i)).setScore(i);

            timers.add(timer);
        }

        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!isRun) {
                return;
            }

            for (int i = 0; i < timers.size(); i++) {
                Team timer = timers.get(i); // 获取每个Team
                Date date = new Date();
                // 设置前缀
                timer.setPrefix(tempList.get(i) + format.format(date));
                // 设置后缀
                timer.setSuffix(tempList.get(i) + " " + format2.format(date));
            }

        }, 0L, 20L);
    }

    public void turnOff() {
        isRun = false;
        task.cancel();
        player.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard());
    }
}