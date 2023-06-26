package com.eofitg.hardcore.configuration;

import com.eofitg.hardcore.Hardcore;

public class SettingsConfig extends AbstractConfig {

    private static final String PARENT_PATH = Hardcore.getInstance().getDataFolder() + "";
    private static final String CHILD_PATH = "settings.yml";
    private String configName;

    public SettingsConfig() {
        super(PARENT_PATH, CHILD_PATH);
    }
    public SettingsConfig(String configName) {
        this();
        this.configName = configName;
    }

    public String getConfigName() {
        return this.configName;
    }
    public boolean getState() {
        return this.getConfig().getBoolean(this.configName + ".enable", false);
    }
    public String getPoint() {
        return this.getConfig().getString(this.configName + ".point", "0.0");
    }
    public String getPoint(String type) {
        return this.getConfig().getString(this.configName + ".point." + type, "0.0");
    }
    public String getLimit() {
        return this.getConfig().getString(this.configName + ".limit", "1");
    }
    public String getLimit(String type) {
        return this.getConfig().getString(this.configName + ".limit." + type, "1");
    }

}
