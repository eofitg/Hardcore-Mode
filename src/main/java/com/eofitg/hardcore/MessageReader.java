package com.eofitg.hardcore;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageReader {
    private static File languageFile = new File(Hardcore.getInstance().getDataFolder(), "language/" + ConfigReader.getLanguage());
    private static YamlConfiguration messages = YamlConfiguration.loadConfiguration(languageFile);
    public static void saveLanguageFiles() {
        File enlang = new File(Hardcore.getInstance().getDataFolder() + "/language/en.yml");
        File cnlang = new File(Hardcore.getInstance().getDataFolder() + "/language/zh.yml");
        if (!enlang.exists()) {
            Hardcore.getInstance().saveResource("language/en.yml", false);
        }
        if (!cnlang.exists()) {
            Hardcore.getInstance().saveResource("language/zh.yml", false);
        }
    }
    public static FileConfiguration getMessages() {
        if (messages == null) {
            messages = YamlConfiguration.loadConfiguration(languageFile);
        }
        return messages;
    }
}
