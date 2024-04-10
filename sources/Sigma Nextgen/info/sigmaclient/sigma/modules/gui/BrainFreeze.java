package info.sigmaclient.sigma.modules.gui;

import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class BrainFreeze extends Module {
    public BrainFreeze() {
        super("BrainFreeze", Category.Gui, "Freeze your brain");
    }

    @Override
    public void onRenderEvent(RenderEvent event) {
        super.onRenderEvent(event);
    }
}
