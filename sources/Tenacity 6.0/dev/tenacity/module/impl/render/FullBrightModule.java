package dev.tenacity.module.impl.render;

import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;

public final class FullBrightModule extends Module {

    public FullBrightModule() {
        super("FullBright", "Makes your game brighter", ModuleCategory.RENDER);
    }

    @Override
    public void onEnable() {
        mc.gameSettings.gammaSetting = 1000000;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = 1;
        super.onDisable();
    }
}
