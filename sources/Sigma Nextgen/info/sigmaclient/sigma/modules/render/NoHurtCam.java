package info.sigmaclient.sigma.modules.render;

import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class NoHurtCam extends Module {
    public NoHurtCam() {
        super("NoHurtCam", Category.Render, "No hurt camera render.");
    }

    @Override
    public void onRenderEvent(RenderEvent event) {
        super.onRenderEvent(event);
    }
}
