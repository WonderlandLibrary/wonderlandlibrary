package io.github.raze.modules.collection.visual;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;

public class FullBright extends AbstractModule {

    private int gamma_before;

    public FullBright() {
        super("FullBright", "Makes everything bright.", ModuleCategory.VISUAL);
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        mc.gameSettings.gammaSetting = 5;
    }

    @Override
    public void onEnable() {
        gamma_before = (int) mc.gameSettings.gammaSetting;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = gamma_before;
    }

}
