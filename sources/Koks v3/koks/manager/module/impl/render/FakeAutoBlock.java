package koks.manager.module.impl.render;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

@ModuleInfo(name = "FakeAutoBlock", description = "", category = Module.Category.RENDER)
public class FakeAutoBlock extends Module {

    public Setting onlySword = new Setting("OnlySword", true, this);

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
