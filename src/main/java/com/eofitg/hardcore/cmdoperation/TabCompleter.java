package com.eofitg.hardcore.cmdoperation;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class TabCompleter extends CommandHandler implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (CommandChecker.conform(label, "hardcore")) {
            List<String> str = Arrays.asList("help", "on", "off", "reset");
            if (sender instanceof Player) {
                if (args.length > 1) {
                    return null;
                } else {
                    return str;
                }
            }
        }
        return null;
    }
}
