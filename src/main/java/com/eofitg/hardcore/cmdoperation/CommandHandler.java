package com.eofitg.hardcore.cmdoperation;

import com.eofitg.hardcore.ConfigReader;
import com.eofitg.hardcore.Hardcore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
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
                    ConfigReader.set("enable", true);
                    Hardcore.getInstance().saveConfig();
                    sender.sendMessage("Hardcore mode is on.");
                    break;
                }
                case "off" : {
                    ConfigReader.set("enable", false);
                    Hardcore.getInstance().saveConfig();
                    sender.sendMessage("Hardcore mode is off.");
                    break;
                }
                case "reset" : {
                    ConfigReader.reset();
                    Hardcore.getInstance().saveConfig();
                    sender.sendMessage(ChatColor.BLUE + "Player state has reset.");
                    break;
                }
            }
            return true;
        }
        return false;
    }
}
