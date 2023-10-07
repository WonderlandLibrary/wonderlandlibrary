package best.azura.client.impl.module.impl.player;

import best.azura.client.impl.events.EventMotion;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;

@ModuleInfo(name = "Anti Hunger", description = "Prevent hungering", category = Category.PLAYER)
public class AntiHunger extends Module {

    @EventHandler
    public final Listener<EventMotion> eventMotionListener = e -> {
        e.sprinting = false;
    };

}