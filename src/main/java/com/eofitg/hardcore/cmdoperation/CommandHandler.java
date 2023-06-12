package com.eofitg.hardcore.cmdoperation;

import com.eofitg.hardcore.ConfigReader;
import com.eofitg.hardcore.Hardcore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static com.eofitg.hardcore.Hardcore.message;

public class CommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
        if (CommandChecker.conform(label, "hardcore")) {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + message("noperm"));
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + message("emptyparams"));
                return true;
            }
            String childCmd = args[0].toLowerCase();
            switch (childCmd) {
                case "help" : {
                    sender.sendMessage(message("help"));
                    break;
                }
                case "on" : {
                    ConfigReader.set("enable", true);
                    Hardcore.getInstance().saveConfig();
                    sender.sendMessage(message("enable"));
                    break;
                }
                case "off" : {
                    ConfigReader.set("enable", false);
                    Hardcore.getInstance().saveConfig();
                    sender.sendMessage(message("disable"));
                    break;
                }
                case "reset" : {
                    ConfigReader.reset();
                    Hardcore.getInstance().saveConfig();
                    sender.sendMessage(ChatColor.BLUE + message("reset"));
                    break;
                }
            }
            return true;
        }
        return false;
    }
}
