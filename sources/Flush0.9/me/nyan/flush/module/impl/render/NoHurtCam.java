package me.nyan.flush.module.impl.render;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventHurtCameraEffect;
import me.nyan.flush.module.Module;

public class NoHurtCam extends Module {
    public NoHurtCam() {
        super("NoHurtCam", Category.RENDER);
    }

    @SubscribeEvent
    public void onHurtCameraEffect(EventHurtCameraEffect e) {
        e.cancel();
    }
}
