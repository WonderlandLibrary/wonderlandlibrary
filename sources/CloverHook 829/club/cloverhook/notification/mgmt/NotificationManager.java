package club.cloverhook.notification.mgmt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import club.cloverhook.Cloverhook;
import club.cloverhook.event.minecraft.RenderOverlayEvent;
import club.cloverhook.notification.abs.Notification;
import club.cloverhook.notification.enums.NotificationType;
import club.cloverhook.utils.ColorCreator;
import club.cloverhook.utils.Draw;
import club.cloverhook.utils.Stopwatch;
import club.cloverhook.utils.font.Fonts;
import me.hippo.systems.lwjeb.annotation.Collect;

import java.util.ArrayList;

/**
 * @author antja03
 */
public class NotificationManager {

    /*
     * A list that contains all queued notifications
     */
    private final ArrayList<Notification> notificationQueue;

    /*
     * The position of the current notification
     */
    private int yOffset;

    /* Animation prop_mode */
    private int animMode;

    /*
     * Used to control the speed of animations
     */
    private final Stopwatch animationStopwatch;

    /*
     * Used to set the animation prop_mode
     */
    private final Stopwatch modeStopwatch;

    public NotificationManager() {
        notificationQueue = new ArrayList<>();
        yOffset = 0;
        animMode = 0;
        animationStopwatch = new Stopwatch();
        modeStopwatch = new Stopwatch();
        Cloverhook.eventBus.register(this);
    }

    public void postInfo(String title, String content) {
        notificationQueue.add(new Notification(NotificationType.INFO, title, content));
    }

    public void postWarning(String title, String content) {
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
            } else if (animMode == 2){
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
        }

        if (notificationQueue.size() - 1 > 0)
            Fonts.f12.drawStringWithShadow(notificationQueue.size() - 1 + " more...", resolution.getScaledWidth() - 4 - Fonts.f12.getStringWidth(notificationQueue.size() - 2 + " more..."), resolution.getScaledHeight() - yOffset - 7, 0xffffffff);

        Draw.drawBorderedRectangle(resolution.getScaledWidth() - 2 - notificationQueue.get(0).getWidth(), resolution.getScaledHeight() - yOffset, resolution.getScaledWidth() - 2, resolution.getScaledHeight() - yOffset + 24,
                0.5, ColorCreator.create(45, 45, 45, 255), ColorCreator.create(220, 220, 220, 20), true);

        Draw.drawRectangle(resolution.getScaledWidth() - 2.5 - notificationQueue.get(0).getWidth(), resolution.getScaledHeight() - yOffset - 0.5, resolution.getScaledWidth() - 1.5, resolution.getScaledHeight() - yOffset + 24.5, ColorCreator.create(0, 0, 0, 50));
        Draw.drawRectangle(resolution.getScaledWidth() - 3 - notificationQueue.get(0).getWidth(), resolution.getScaledHeight() - yOffset - 1.0, resolution.getScaledWidth() - 1.0, resolution.getScaledHeight() - yOffset + 25, ColorCreator.create(0, 0, 0, 50));
        Draw.drawRectangle(resolution.getScaledWidth() - 3.5 - notificationQueue.get(0).getWidth(), resolution.getScaledHeight() - yOffset - 1.5, resolution.getScaledWidth() - 0.5, resolution.getScaledHeight() - yOffset + 25.5, ColorCreator.create(0, 0, 0, 50));

        GL11.glPushMatrix();
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        Draw.drawImg(new ResourceLocation("client/gui/icon/info/info32_pink.png"), resolution.getScaledWidth() - 2 - (int) notificationQueue.get(0).getWidth(), resolution.getScaledHeight() - yOffset + 4, 16, 16);
        GL11.glPopMatrix();
        Fonts.bf18.drawString(notificationQueue.get(0).getTitle(), resolution.getScaledWidth() - 2 - notificationQueue.get(0).getWidth() + 20, resolution.getScaledHeight() - yOffset + 7, ColorCreator.create(220, 220, 220));
        Fonts.f12.drawString(notificationQueue.get(0).getContent(), resolution.getScaledWidth() - 2 - notificationQueue.get(0).getWidth() + 20.5, resolution.getScaledHeight() - yOffset + 16, ColorCreator.create(220, 220, 220));
    }

}
