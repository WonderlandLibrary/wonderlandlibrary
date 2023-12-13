package koks.module.visual;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;

@Module.Info(name = "PortalScreen", description = "Modify the portal system", category = Module.Category.VISUAL)
public class PortalScreen extends Module {

    @Value(name = "GuiCheck")
    boolean guiCheck = false;

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
