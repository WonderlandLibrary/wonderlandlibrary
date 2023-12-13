package me.nyan.flush.module.impl.render;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventRenderFire;
import me.nyan.flush.module.Module;

public class NoFire extends Module {
    public NoFire() {
        super("NoFire", Category.RENDER);
    }

    @SubscribeEvent
    public void onRenderFire(EventRenderFire e) {
        e.cancel();
    }
}
