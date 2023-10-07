package best.azura.client.impl.module.impl.movement;

import best.azura.client.impl.events.EventMove;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;

@SuppressWarnings("unused")
@ModuleInfo(name = "No Clip", description = "Clip through blocks", category = Category.MOVEMENT)
public class NoClip extends Module {

    @EventHandler
    public final Listener<EventMove> eventMotionListener = eventMove -> {
        eventMove.setNoClip(true);
    };

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
