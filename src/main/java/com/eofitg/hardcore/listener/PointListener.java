package com.eofitg.hardcore.listener;

import com.eofitg.hardcore.Hardcore;
import com.eofitg.hardcore.listener.pointlistener.*;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

import static com.eofitg.hardcore.util.MathUtil.*;

public class PointListener extends AbstractListener {

    public void register(Plugin plugin) {
        new DamageListener().register();
        new CraftListener().register();
        new ExpListener().register();
        new KillListener().register();
        new AdvancementListener().register();
        new StructureListener().register();
        new BiomesListener().register();
    }

    public static double parsePoint(String str) {

        // a,b,...
        if (str.contains(",")) {

            String[] strList = str.split(",");
            List<Double> pointList = new ArrayList<>();

            for (String s : strList) {
                if (isDouble(s)) {
                    // Keep only valid numbers
                    double n = round_down(Double.parseDouble(s), 1);
                    if (!pointList.contains(n)) {
                        // De-duplication
                        pointList.add(n);
                    }
                }
            }

            if (!pointList.isEmpty()) {
                return pointList.get(randomInt(0, pointList.size() - 1));
            }

        }

        // a~b
        if (str.contains("~")) {

            String[] strList = str.split("~");
            List<Double> pointList = new ArrayList<>();

            for (String s : strList) {
                // Read only the first two valid numbers
                if (pointList.size() > 1) break;
                if (isDouble(s)) {
                    // Keep only valid numbers
                    pointList.add(round_down(Double.parseDouble(s), 1));
                }
            }

            // Check if there are enough numbers in pointList
            if (pointList.size() > 1) {
                int MIN = Math.min((int) (pointList.get(0) * 10), (int) (pointList.get(1) * 10));
                int MAX = Math.max((int) (pointList.get(0) * 10), (int) (pointList.get(1) * 10));
                return randomInt(MIN, MAX) / 10.0;
            }

        }

        // a
        if (isDouble(str)) {
            return round_down(Double.parseDouble(str), 1);
        }

        Hardcore.getInstance().getLogger().info("ERROR: Invalid point value! Please check the configuration file named \"settings.yml\"!");
        // Return default point value
        return 0.0;

    }

    public static int parseLimit(String str) {

        // a,b,...
        if (str.contains(",")) {

            String[] strList = str.split(",");
            List<Integer> pointList = new ArrayList<>();

            for (String s : strList) {
                if (isInt(s)) {
                    int n = Integer.parseInt(s);
                    // The normal limit shouldn't be less than 1
                    if (n < 1) continue;
                    if (!pointList.contains(n)) {
                        // De-duplication
                        pointList.add(n);
                    }
                }
            }

            if (!pointList.isEmpty()) {
                return pointList.get(randomInt(0, pointList.size() - 1));
            }

        }

        // a~b
        if (str.contains("~")) {

            String[] strList = str.split("~");
            List<Integer> pointList = new ArrayList<>();

            for (String s : strList) {
                // Read only the first two valid numbers
                if (pointList.size() > 1) break;
                if (isInt(s)) {
                    // The normal limit shouldn't be less than 1
                    if (Integer.parseInt(s) >= 1) {
                        pointList.add(Integer.parseInt(s));
                    }
                }
            }

            // Check if there are enough numbers in pointList
            if (pointList.size() > 1) {
                int MIN = Math.min(pointList.get(0), pointList.get(1));
                int MAX = Math.max(pointList.get(0), pointList.get(1));
                return randomInt(MIN, MAX);
            }

        }

        // a
        if (isInt(str)) {
            // The minimum valid value of limit is -1
            if (Integer.parseInt(str) >= -1) {
                return Integer.parseInt(str);
            }
        }

        Hardcore.getInstance().getLogger().info("ERROR: Invalid limit value! Please check the configuration file named \"settings.yml\"!");
        // Return default limit value
        return 1;

    }

}
