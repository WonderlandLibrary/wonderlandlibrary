package client.module.impl.other;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.TickEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.value.impl.NumberValue;

@ModuleInfo(name = "Timer", description = "Changes the speed that Minecraft runs at", category = Category.OTHER)
public class Timer extends Module {

    private final NumberValue speed = new NumberValue("Speed", this, 1, 0.05, 10, 0.05);

    @Override
    protected void onDisable() {
        mc.timer.timerSpeed = 1.0F;
    }

    @EventLink
    public final Listener<TickEvent> onTick = event -> {
        mc.timer.timerSpeed = speed.getValue().floatValue();
    };
}
