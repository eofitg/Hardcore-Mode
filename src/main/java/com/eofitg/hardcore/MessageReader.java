package com.eofitg.hardcore;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageReader {
    private static File languageFile = new File(Hardcore.getInstance().getDataFolder(), "language.yml");
    private static YamlConfiguration messages = YamlConfiguration.loadConfiguration(languageFile);
    public static void saveLanguageFiles() {
        if (!languageFile.exists()) {
            Hardcore.getInstance().saveResource("language.yml", false);
        }
    }
    private static ConfigurationSection getMessages() {
        return messages.getConfigurationSection("messages." + ConfigReader.getLanguage());
    }
    public static String message(String name) {
        String msg = MessageReader.getMessages().getString(name);
        if (msg == null) {
            if (ConfigReader.getLanguage() == "zh_CN") {
                return "没有找到语言文件!";
            } else {
                return "No language file found!";
            }
        }
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
