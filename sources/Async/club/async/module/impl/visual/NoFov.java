package club.async.module.impl.visual;

import club.async.event.impl.EventUpdate;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import rip.hippo.lwjeb.annotation.Handler;

@ModuleInfo(name = "NoFov", description = "No fov effect", category = Category.VISUAL)
public class NoFov extends Module {

    @Handler
    public void update(EventUpdate eventUpdate) {
        if (mc.gameSettings.ofDynamicFov)
            mc.gameSettings.ofDynamicFov = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.ofDynamicFov = true;
    }
}
