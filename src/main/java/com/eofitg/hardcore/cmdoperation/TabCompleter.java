package com.eofitg.hardcore.cmdoperation;

import com.eofitg.hardcore.ConfigReader;
import com.eofitg.hardcore.Hardcore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class TabCompleter implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> str = Arrays.asList("on", "off", "reset");
        if (sender instanceof Player) {
            if (args.length > 1) {
                return null;
            } else {
                return str;
            }
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (CommandChecker.conform(label, "hardcore")) {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Empty parameters.");
                return true;
            }
            String childCmd = args[0].toLowerCase();
            switch (childCmd) {
                case "help" : {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &a/hardcore help &f- &7Get Help"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &a/hardcore on &f- &7Turn on the hardcore mode"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &a/hardcore off &f- &7Turn off the hardcore mode"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &a/hardcore reset &f- &7Reset players' survival states"));
                    break;
                }
                case "on" : {
                    ConfigReader.set("on", true);
                    Hardcore.getInstance().saveConfig();
                    sender.sendMessage(ChatColor.BLUE + "Hardcore mode is on.");
                    break;
                }
                case "off" : {
                    ConfigReader.set("on", false);
                    Hardcore.getInstance().saveConfig();
                    sender.sendMessage(ChatColor.BLUE + "Hardcore mode is off.");
                    break;
                }
                case "reset" : {
                    ConfigReader.reset();
                    Hardcore.getInstance().saveConfig();
                    sender.sendMessage("Player state has reset.");
                    break;
                }
            }
            return true;
        }
        return false;
    }
}
