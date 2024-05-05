package best.azura.client.impl.module.impl.other;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.value.NumberValue;

@ModuleInfo(name = "Timer", category = Category.OTHER, description = "Change the game speed")
public class Timer extends Module {
    private final NumberValue<Float> timerValue = new NumberValue<>("Timer", "Timer speed value", 1.0f, 0.1f, 0.1f, 10.0f);
    @EventHandler
    public final Listener<EventUpdate> eventUpdateListener = e -> {
        mc.timer.timerSpeed = timerValue.getObject();
    };
    @Override
    public void onDisable() {
        super.onDisable();
        mc.timer.timerSpeed = 1.0f;
    }
}