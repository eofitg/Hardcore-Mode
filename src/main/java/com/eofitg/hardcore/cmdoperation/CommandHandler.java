package com.eofitg.hardcore.cmdoperation;

import com.eofitg.hardcore.configuration.MainConfig;
import com.eofitg.hardcore.configuration.UserDataConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
        if (CommandChecker.conform(label, "hardcore")) {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }
            if (args.length > 2) {
                sender.sendMessage(ChatColor.RED + "Invalid command.");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Empty parameters.");
                return true;
            }

            String childCmd = args[0].toLowerCase();
            if (args.length == 2) {
                // /hardcore reset <online-player-name>
                if (childCmd.equals("reset")) {
                    String name = args[1];
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        String playerName = player.getName();
                        if (name.equalsIgnoreCase(playerName)) {
                            UserDataConfig userDataConfig = new UserDataConfig(player, player.getUniqueId().toString(), playerName);
                            userDataConfig.reset();
                            userDataConfig.save();
                        }
                    }
                    sender.sendMessage(ChatColor.BLUE + "Player " + args[1] + "'s state has reset.");
                } else {
                    sender.sendMessage(ChatColor.RED + "Invalid command.");
                }
                return true;
            }

            switch (childCmd) {
                case "help" : {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &a/hardcore help &f- &7Get Help"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &a/hardcore on &f- &7Turn on the hardcore mode"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &a/hardcore off &f- &7Turn off the hardcore mode"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &a/hardcore reset &f- &7Reset players' survival states"));
                    break;
                }
                case "on" : {
                    MainConfig.setState(true);
                    MainConfig.save();
                    sender.sendMessage(ChatColor.BLUE + "Hardcore mode is on.");
                    break;
                }
                case "off" : {
                    MainConfig.setState(false);
                    MainConfig.save();
                    sender.sendMessage(ChatColor.BLUE + "Hardcore mode is off.");
                    break;
                }
                case "reset" : {
                    UserDataConfig.reset_all();
                    sender.sendMessage(ChatColor.BLUE + "Player state has reset.");
                    break;
                }
            }
            return true;
        }
        return false;
    }

}
