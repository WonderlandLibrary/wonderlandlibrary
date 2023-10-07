package koks.module.player;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.event.PotionUpdateEvent;

@Module.Info(name = "PotionSaver", description = "Save your potions", category = Module.Category.PLAYER)
public class PotionSaver extends Module {

    @Override
    @Event.Info(priority = Event.Priority.EXTREME)
    public void onEvent(Event event) {
        if(event instanceof PotionUpdateEvent) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
