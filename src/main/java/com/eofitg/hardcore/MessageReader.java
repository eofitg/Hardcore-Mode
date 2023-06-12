package com.eofitg.hardcore;

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
    public static ConfigurationSection getMessages() {
        if (messages == null) {
            messages = YamlConfiguration.loadConfiguration(languageFile);
        }
        return messages.getConfigurationSection("messages." + ConfigReader.getLanguage());
    }
}
