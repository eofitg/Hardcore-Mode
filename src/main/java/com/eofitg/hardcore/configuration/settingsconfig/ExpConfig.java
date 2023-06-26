package com.eofitg.hardcore.configuration.settingsconfig;

import com.eofitg.hardcore.configuration.SettingsConfig;

public class ExpConfig extends SettingsConfig {

    private static final String CONFIG_NAME = "exp";

    public ExpConfig() {
        super(CONFIG_NAME);
    }

    public boolean byLevel(String type) {
        return this.getConfig().getBoolean(this.getConfigName() + ".by-level." + type, true);
    }

}
