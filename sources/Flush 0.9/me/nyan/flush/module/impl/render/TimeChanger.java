package me.nyan.flush.module.impl.render;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventSetWorldTime;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.NumberSetting;

public class TimeChanger extends Module {
    private final NumberSetting time = new NumberSetting("Time", this, 23999, 0, 23999);

    public TimeChanger() {
        super("TimeChanger", Category.RENDER);
    }

    @SubscribeEvent
    public void onSetWorldTime(EventSetWorldTime e) {
        e.setTime(time.getValueInt());
    }
}
