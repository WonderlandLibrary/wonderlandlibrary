package koks.module.visual;

import koks.api.event.Event;
import koks.api.registry.module.Module;

/**
 * @author kroko
 * @created on 27.02.2021 : 05:33
 */
@Module.Info(name = "AntiStrike", description = "You cant anymore striked because text", category = Module.Category.VISUAL)
public class AntiStrike extends Module {

    @Override
    @Event.Info
    public void onEvent(Event event) {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
