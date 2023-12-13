package me.nyan.flush.module.impl.render;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventCameraClip;
import me.nyan.flush.module.Module;

public class CameraNoClip extends Module {
    public CameraNoClip() {
        super("CameraNoClip", Category.RENDER);
    }

    @SubscribeEvent
    public void onCameraClip(EventCameraClip e) {
        e.cancel();
    }
}
