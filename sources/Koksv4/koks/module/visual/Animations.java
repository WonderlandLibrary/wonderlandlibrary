package koks.module.visual;

import koks.api.event.Event;
import koks.api.registry.module.Module;

/**
 * @author kroko
 * @created on 05.02.2021 : 05:43
 */

@Module.Info(name = "Animations", description = "You have animations", category = Module.Category.VISUAL)
public class Animations extends Module {

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
