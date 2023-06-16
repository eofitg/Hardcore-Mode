package com.eofitg.hardcore.cmdoperation;

import org.bukkit.Bukkit;

import java.util.List;
import java.util.Objects;

public class TabCompleterRegister {

    public static void register(List<String> cmdNames) {
        for (String cmdName : cmdNames) {
            TabCompleterRegister.register(cmdName);
        }
    }
    public static void register(String cmdName) {
        Objects.requireNonNull(Bukkit.getPluginCommand(cmdName)).setTabCompleter(new TabCompleter());
    }

}
