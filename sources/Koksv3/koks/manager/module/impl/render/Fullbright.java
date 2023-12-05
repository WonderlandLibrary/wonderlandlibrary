package koks.manager.module.impl.render;

import koks.manager.event.Event;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

/**
 * @author kroko
 * @created on 17.11.2020 : 04:02
 */

@ModuleInfo(category = Module.Category.RENDER, description = "You can see in the darkness", name = "Fullbright")
public class Fullbright extends Module {

    public float gamma;

    @Override
    public void onEvent(Event event) {

    }

    @Override
    public void onEnable() {
        gamma = getGameSettings().gammaSetting;
        getGameSettings().gammaSetting = 1000;
    }

    @Override
    public void onDisable() {
        getGameSettings().gammaSetting = gamma;
    }
}
