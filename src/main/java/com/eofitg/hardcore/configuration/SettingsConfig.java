package com.eofitg.hardcore.configuration;

import com.eofitg.hardcore.Hardcore;

public class SettingsConfig extends AbstractConfig {

    private static final String PARENT_PATH = Hardcore.getInstance().getDataFolder() + "";
    private static final String CHILD_PATH = "settings.yml";

    public SettingsConfig() {
        super(PARENT_PATH, CHILD_PATH);
    }

}
