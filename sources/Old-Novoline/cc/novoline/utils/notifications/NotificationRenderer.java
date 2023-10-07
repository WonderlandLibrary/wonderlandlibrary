package cc.novoline.utils.notifications;

import cc.novoline.Novoline;
import cc.novoline.modules.visual.HUD;
import cc.novoline.utils.OpenGLUtil;
import cc.novoline.utils.fonts.impl.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.OpenGLException;

import java.awt.*;

import static cc.novoline.utils.fonts.impl.Fonts.SF.SF_18.SF_18;

public final class NotificationRenderer {

    private static final int RED = new Color(255, 80, 80).getRGB();
    private static final int GREEN = new Color(135, 227, 49).getRGB();
    private static final int ORANGE = new Color(255, 215, 100).getRGB();
    private static final int WHITE = new Color(255, 255, 255).getRGB();

    private static final Minecraft mc = Minecraft.getInstance();
    private static final NotificationManager notificationManager = Novoline.getInstance().getNotificationManager();

    private static int displayHeight = 0, displayWidth = 0;

    public static void draw(ScaledResolution resolution) {
        if (!notificationManager.getNotifications().isEmpty()) {
            notificationManager.update();
        }

        for (Notification notification : notificationManager.getNotifications()) {

            double x = notification.getX();
            double y = resolution.getScaledHeightStatic(mc) - notification.getY();

            // region notification-rendering
            String callReason = notification.getCallReason() == null ? StringUtils.capitalize(notification.getType().toString()) :
                    notification.getCallReason();
            String message = notification.getMessage();
            String seconds = String.valueOf((notification.getDelay() - notification.getCount()) / 1000.0),
                    formatted = "(" + seconds.substring(0, seconds.indexOf(".") + 2) + "s) ";

            Gui.drawRect(
                    resolution.getScaledWidthStatic(mc) - x - (notification.getNotificationType() == NotificationType.WARNING || notification.getNotificationType() == NotificationType.SUCCESS || notification.getNotificationType() == NotificationType.INFO ? 2 : 0),
                    y,
                    resolution.getScaledWidthStatic(mc),
                    y + 24,
                    new Color(0, 0, 0,110).getRGB());

            mc.fontRendererObj.drawString(callReason,resolution.getScaledWidthStatic(mc) - (float)x + 25,(float)y + 2,0xffffffff,true);
            mc.fontRendererObj.drawString(message + " " + formatted,resolution.getScaledWidthStatic(mc) - (float)x + 25,(float)y + 12.5F,new Color(200,200,200,255).getRGB(),true);
            //endregion

            //region icon-rendering
            switch (notification.getType()) {
                case ERROR:
                    Fonts.ICONFONT.ICONFONT_50.ICONFONT_50.drawString("I", (float) (resolution.getScaledWidthStatic(mc) - x + 1.5), (float) y + 2,RED);
                    break;
                case WARNING:
                    Fonts.ICONFONT.ICONFONT_50.ICONFONT_50.drawString("J",(float) (resolution.getScaledWidthStatic(mc) - x - 0.5f), (float) y + 2,ORANGE);
                    break;
                case SUCCESS:
                    Fonts.ICONFONT.ICONFONT_50.ICONFONT_50.drawString("H",(float) (resolution.getScaledWidthStatic(mc) - x - 1), (float) y + 2,GREEN);
                    break;
                case INFO:
                    Fonts.ICONFONT.ICONFONT_50.ICONFONT_50.drawString("K",(int) (resolution.getScaledWidthStatic(mc) - x), (float) y + 2,WHITE);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + notification.getType());
            }
            //endregion

            //region timebar-rendering
            double width = notification.getX();
            double w1 = width/100;
            double a = notification.getDelay()/100;
            double perc = (float) (notification.getCount()/a);

            Gui.drawRect(
                    resolution.getScaledWidthStatic(mc) - x -
                            (notification.getNotificationType() == NotificationType.WARNING || notification.getNotificationType() == NotificationType.SUCCESS || notification.getNotificationType() == NotificationType.INFO ? 2 : 0),
                    y + 23,
                    resolution.getScaledWidthStatic(mc) - x + (perc * w1),
                    y + 24,
                    getColorForType(notification.getType()));
            //endregion
        }

    }


    private static int getColorForType(NotificationType type) {
        switch (type) { // @off
            case SUCCESS:
                return GREEN;
            case ERROR:
                return RED;
            case WARNING:
                return ORANGE;
            case INFO:
                return WHITE;
        } // @on

        return 0;
    }
}

