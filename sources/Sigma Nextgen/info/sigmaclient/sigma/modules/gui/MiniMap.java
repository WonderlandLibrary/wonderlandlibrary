package info.sigmaclient.sigma.modules.gui;

import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.gui.hud.notification.NotificationManager;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.minimap.events.Events;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class MiniMap extends Module {
    public MiniMap() {
        super("MiniMap", Category.Gui, "Show a minimap in your GUI.");
    }
    public static int offsetY = 0;
    @Override
    public void onEnable() {
        this.enabled = false;
//        NotificationManager.notify("MiniMap", "itz WIP, so I disable it.", 4000);
        NotificationManager.notify("MiniMap", "this module is unstable", 4000);
        super.onEnable();
    }

    @Override
    public void onRenderEvent(RenderEvent event) {
        Events.drawMiniMap();
        super.onRenderEvent(event);
    }
}
