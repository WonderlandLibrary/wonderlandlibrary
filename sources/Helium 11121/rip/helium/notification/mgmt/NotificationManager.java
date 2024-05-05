package rip.helium.notification.mgmt;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import rip.helium.Helium;
import rip.helium.event.minecraft.RenderOverlayEvent;
import rip.helium.notification.abs.Notification;
import rip.helium.notification.enums.NotificationType;
import rip.helium.utils.ColorCreator;
import rip.helium.utils.Draw;
import rip.helium.utils.RenderUtil;
import rip.helium.utils.Stopwatch;
import rip.helium.utils.font.Fonts;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author antja03
 */
public class NotificationManager {

    /*
     * A list that contains all queued notifications
     */
    private static ArrayList<Notification> notificationQueue;
    /*
     * Used to control the speed of animations
     */
    private final Stopwatch animationStopwatch;
    /*
     * Used to set the animation prop_mode
     */
    private final Stopwatch modeStopwatch;
    double i;
    int color = new Color(255, 0, 225, 116).getRGB();
    int y;
    /*
     * The position of the current notification
     */
    private int yOffset;
    /* Animation prop_mode */
    private int animMode;

    public NotificationManager() {
        notificationQueue = new ArrayList<>();
        yOffset = 0;
        animMode = 0;
        animationStopwatch = new Stopwatch();
        modeStopwatch = new Stopwatch();
        Helium.eventBus.register(this);
    }

    public static void postInfo(String title, String content) {
        notificationQueue.add(new Notification(NotificationType.INFO, title, content));
    }

    public static void postWarning(String title, String content) {
        notificationQueue.add(new Notification(NotificationType.WARNING, title, content));
    }

    public void postError(String title, String content) {
        notificationQueue.add(new Notification(NotificationType.ERROR, title, content));
    }

    @Collect
    public void onRenderOverlay(RenderOverlayEvent renderOverlayEvent) {
        if (notificationQueue.isEmpty()) {
            return;
        }

        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

        if (animationStopwatch.hasPassed(1000 / 100)) {
            if (animMode == 1) {
                if (yOffset < 26) {
                    if (yOffset < 20) {
                        yOffset += 5;
                        modeStopwatch.reset();
                    } else {
                        yOffset += 3;
                        modeStopwatch.reset();
                    }
                }
            } else if (animMode == 2) {
                if (yOffset > 0) {
                    if (yOffset < 6) {
                        yOffset -= 5;
                        modeStopwatch.reset();
                    } else {
                        yOffset -= 3;
                        modeStopwatch.reset();
                    }
                } else {
                    notificationQueue.remove(notificationQueue.get(0));
                    modeStopwatch.reset();
                    animMode = 1;
                    return;
                }
            }
            animationStopwatch.reset();
        }

        if (modeStopwatch.hasPassed(1000)) {
            animMode = 2;
            modeStopwatch.reset();
            color = new Color(0, 0, 0, 0).getRGB();
            i = 0;
        }


        if (i < 2500) {
            i = i + 0.33;
            color = new Color(255, 255, 255).getRGB();
        }

        Draw.drawRectangle(ScaledResolution.getScaledWidth() - 3.5 - notificationQueue.get(0).getWidth(), ScaledResolution.getScaledHeight() - yOffset - 1.5 - 15 - y, ScaledResolution.getScaledWidth() - 0.5, ScaledResolution.getScaledHeight() - yOffset + 25.5 + y, RenderUtil.withTransparency(0, 0.5f));
        Fonts.f20.drawStringWithShadow(notificationQueue.get(0).getTitle(), ScaledResolution.getScaledWidth() - 2 - notificationQueue.get(0).getWidth() + 20, ScaledResolution.getScaledHeight() - y - yOffset + 10 - 18, ColorCreator.create(220, 220, 220));
        Fonts.f16.drawStringWithShadow(notificationQueue.get(0).getContent(), ScaledResolution.getScaledWidth() - 2 - notificationQueue.get(0).getWidth() + 20, ScaledResolution.getScaledHeight() - y - yOffset + 10 - 8, ColorCreator.create(220, 220, 220));
        //ChatUtil.chat(y + " yes");

    }

}
