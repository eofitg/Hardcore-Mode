package com.eofitg.hardcore.configuration.settingsconfig;

import com.eofitg.hardcore.configuration.SettingsConfig;

public class KillConfig extends SettingsConfig {

    private static final String CONFIG_NAME = "kill";

    public KillConfig() {
        super(CONFIG_NAME);
    }

    public boolean allowPvp() {
        return this.getConfig().getBoolean(this.getConfigName() + ".pvp.", false);
    }

}
