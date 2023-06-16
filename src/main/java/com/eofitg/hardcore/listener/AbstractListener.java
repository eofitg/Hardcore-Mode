package com.eofitg.hardcore.listener;

import com.eofitg.hardcore.configuration.MainConfig;

public abstract class AbstractListener {
    protected static boolean state = MainConfig.getState();

    protected static void reload() {
        state = MainConfig.getState();
    }

}
