package info.sigmaclient.sigma.modules.gui;

import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.gui.hud.notification.NotificationManager;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class RearView extends Module {
    public RearView() {
        super("RearView", Category.Gui, "View rear your body.");
    }

    @Override
    public void onEnable() {
        this.enabled = false;
        NotificationManager.notify("RearView", "itz WIP, so I disable it.", 4000);
        super.onEnable();
    }

    @Override
    public void onRenderEvent(RenderEvent event) {
        super.onRenderEvent(event);
    }
}
