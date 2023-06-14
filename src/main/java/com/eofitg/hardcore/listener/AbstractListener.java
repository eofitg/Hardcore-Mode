package com.eofitg.hardcore.listener;

import com.eofitg.hardcore.configuration.MainConfig;

public class AbstractListener {
    protected static boolean state = MainConfig.getState();

    protected void reload() {
        state = MainConfig.getState();
    }

}
