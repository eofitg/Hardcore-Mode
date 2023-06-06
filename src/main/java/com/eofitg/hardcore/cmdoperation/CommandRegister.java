package com.eofitg.hardcore.cmdoperation;

import com.eofitg.hardcore.ConfigReader;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Objects;

public class CommandRegister {
    public static void register (List<String> cmdNames) {
        for (String cmdName : cmdNames) {
            CommandRegister.register(cmdName);
        }
    }
    public static void register (String cmdName) {
        List<String> cmdList = ConfigReader.getCmdList(cmdName);
        for (String s : cmdList) {
            Objects.requireNonNull(Bukkit.getPluginCommand(s)).setExecutor(new CommandHandler());
        }
    }
}
