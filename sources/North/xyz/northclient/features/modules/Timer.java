package xyz.northclient.features.modules;

import xyz.northclient.NorthSingleton;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Category;
import xyz.northclient.features.EventLink;
import xyz.northclient.features.ModuleInfo;
import xyz.northclient.features.events.TickEvent;
import xyz.northclient.features.values.DoubleValue;
import xyz.northclient.util.MoveUtil;

@ModuleInfo(name = "Timer", description = "Speeds up game ticks", category = Category.MOVEMENT)
public class Timer extends AbstractModule {
    public DoubleValue timerSpeed = new DoubleValue("Timer speed", this)
            .setDefault(1)
            .setMin(0.01)
            .setIncrement(0.01)
            .setMax(10);

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
    }

    @EventLink
    public void onUpdate(TickEvent event) {
        mc.timer.timerSpeed = timerSpeed.get().floatValue();
    }
}
