package com.eofitg.hardcore.listener;

import com.eofitg.hardcore.listener.pointlistener.*;
import org.bukkit.plugin.Plugin;

public class PointListener extends AbstractListener {

    public void register(Plugin plugin) {
        new DamageListener().register();
        new CraftListener().register();
        new ExpListener().register();
        new KillListener().register();
        new AdvancementListener().register();
        new StructureListener().register();
        new BiomesListener().register();
    }


}
