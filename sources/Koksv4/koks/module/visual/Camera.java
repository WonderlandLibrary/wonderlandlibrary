package koks.module.visual;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.event.FOVEvent;
import koks.event.HurtCameraEvent;

@Module.Info(name = "Camera", description = "Modify your view", category = Module.Category.VISUAL)
public class Camera extends Module {

    @Value(name = "Static FOV")
    boolean staticFov = false;

    @Value(name = "FOV", minimum = 0.001, maximum = 2)
    double fov = 1;

    @Value(name = "HurtCam")
    boolean hurtCam = true;

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        switch (name) {
            case "FOV":
                return staticFov;
        }
        return super.isVisible(value, name);
    }

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof final FOVEvent fovEvent) {
            if (staticFov) {
                fovEvent.setFov((float) fov);
            }
        }
        if (event instanceof final HurtCameraEvent hurtCameraEvent) {
            if (!hurtCam)
                hurtCameraEvent.setCanceled(true);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
