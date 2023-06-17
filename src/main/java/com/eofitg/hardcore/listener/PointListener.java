package com.eofitg.hardcore.listener;

import com.eofitg.hardcore.listener.pointlistener.*;

public class PointListener extends AbstractListener {

    @Override
    public void register() {
        new DamageListener().register();
        new CraftListener().register();
        new ExpListener().register();
        new KillListener().register();
        new AdvancementListener().register();
        new StructureListener().register();
        new BiomesListener().register();
    }

}
