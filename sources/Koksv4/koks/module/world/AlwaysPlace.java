package koks.module.world;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import koks.event.CanBlockPlaceEvent;

@Module.Info(name = "AlwaysPlace", description = "You can place inside you", category = Module.Category.WORLD)
public class AlwaysPlace extends Module {

    @Override
    @Event.Info(priority = Event.Priority.HIGH)
    public void onEvent(Event event) {
        if (event instanceof final CanBlockPlaceEvent canBlockPlaceEvent) {
            if (!ModuleRegistry.getModule(Scaffold.class).isToggled())
                canBlockPlaceEvent.setAllowedToPlace(true);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
