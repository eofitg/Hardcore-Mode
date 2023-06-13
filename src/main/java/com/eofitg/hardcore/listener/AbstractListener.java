package com.eofitg.hardcore.listener;

import com.eofitg.hardcore.ConfigReader;

public class AbstractListener {
    protected static boolean state = ConfigReader.getState();

    protected void reload() {
        state = ConfigReader.getState();
    }

}
