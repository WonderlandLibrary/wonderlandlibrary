package info.sigmaclient.sigma.modules.render;

import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class CameraNoClip extends Module {
    public CameraNoClip() {
        super("CameraNoClip", Category.Render, "No clip camera");
    }

    @Override
    public void onRenderEvent(RenderEvent event) {
        super.onRenderEvent(event);
    }
}
