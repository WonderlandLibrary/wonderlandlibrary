package info.sigmaclient.sigma.modules.gui;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.event.player.WindowUpdateEvent;
import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.gui.hud.notification.Notification;
import info.sigmaclient.sigma.gui.notifications.NotificationManager;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.font.FontUtil;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;

import java.awt.*;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Notifications extends Module {

    public Notifications() {
        super("Notifications", Category.Gui, "new Notifications", true);
    }

    @Override
    public void onRenderEvent(RenderEvent event) {
//        NotificationManager.onRender2D();
        SigmaNG.getSigmaNG().notificationManager.onRender();
    }
    @Override
    public void onWindowUpdateEvent(WindowUpdateEvent event) {
    }
}
