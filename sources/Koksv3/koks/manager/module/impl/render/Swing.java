package koks.manager.module.impl.render;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

@ModuleInfo(name = "Swing", category = Module.Category.RENDER, description = "")
public class Swing extends Module {

    public Setting mode = new Setting("Mode", new String[]{"Animation1" , "Animation2", "Animation3", "Animation4", "Animation5", "Spin", "1.7 Animation"}, "1.7 Animation", this);

    @Override
    public void onEvent(Event event) {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
