package club.async.module.impl.visual;

import club.async.event.impl.EventUpdate;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import rip.hippo.lwjeb.annotation.Handler;

@ModuleInfo(name = "NoBob", description = "No bob effect", category = Category.VISUAL)
public class NoBob extends Module {

    @Handler
    public void update(EventUpdate eventUpdate) {
        if (mc.gameSettings.viewBobbing)
        mc.gameSettings.viewBobbing = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.viewBobbing = true;
    }
}
