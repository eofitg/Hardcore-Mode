package com.eofitg.hardcore.listener;

import com.eofitg.hardcore.configuration.DefaultConfig;

public class AbstractListener {
    protected static boolean state = DefaultConfig.getState();

    protected void reload() {
        state = DefaultConfig.getState();
    }

}
