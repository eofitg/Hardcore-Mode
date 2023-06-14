package com.eofitg.hardcore.cmdoperation;

import com.eofitg.hardcore.Hardcore;
import com.eofitg.hardcore.configuration.MainConfig;

import java.util.List;

public class CommandChecker {
    public static boolean conform (String requestCommand, String commandName) {
        List<String> cmdList = MainConfig.getCmdList(commandName);
        String pluginName = Hardcore.getPluginName().toLowerCase();
        if (requestCommand.startsWith(pluginName + ":")) {
            requestCommand = requestCommand.substring(pluginName.length() + 1);
        }
        if (cmdList.contains(requestCommand)) {
            return true;
        }
        return false;
    }
}
