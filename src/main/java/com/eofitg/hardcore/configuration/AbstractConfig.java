package com.eofitg.hardcore.configuration;

import com.eofitg.hardcore.Hardcore;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class AbstractConfig {

    private final String parentPath;
    private final String childPath;
    private final File file;
    private final FileConfiguration config;
    private final boolean exists;

    public AbstractConfig(String parentPath, String childPath) {
        this.parentPath = parentPath;
        this.childPath = childPath;
        this.file = new File(parentPath, childPath);
        this.config = new YamlConfiguration();
        this.exists = file.exists();
        if (!exists) {
            this.file.getParentFile().mkdirs();
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            this.config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public String getParentPath() {
        return this.parentPath;
    }
    public String getChildPath() {
        return this.childPath;
    }
    public File getFile() {
        return this.file;
    }
    public FileConfiguration getConfig() {
        return this.config;
    }
    public boolean exists() {
        return this.exists;
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void saveDefault() {
        Hardcore.getInstance().saveResource(this.childPath, !this.exists);
    }

}
