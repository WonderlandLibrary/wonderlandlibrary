package de.tired.util.render.notification;

import de.tired.util.animation.Animation;
import de.tired.util.animation.Easings;
import de.tired.util.math.TimerUtil;
import de.tired.base.font.FontManager;
import de.tired.util.render.ColorUtil;
import de.tired.base.interfaces.IHook;
import de.tired.util.render.RenderUtil;
import de.tired.util.render.StencilUtil;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.list.RoundedRectGradient;
import de.tired.util.render.shaderloader.list.RoundedRectShader;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class Notify implements IHook {

    private String notificationMessage, notificationTitle;

    private final TimerUtil clock;

    public double width, height, y, lastY;

    private final long stayTime;

    private final Animation animation = new Animation();

    private final Animation anim = new Animation();

    private final Animation animationY = new Animation();

    private final Animation scaleAnimation = new Animation();

    private final NotificationType notificationType;

    private int gettingY;

    public Notify(String title, String message, NotificationType notificationType) {
        this.notificationTitle = title;
        this.notificationMessage = message;
        (this.clock = new TimerUtil()).doReset();
        this.width = FontManager.futuraNormal.getStringWidth(title) > FontManager.futuraNormal.getStringWidth(message) ? FontManager.futuraNormal.getStringWidth(title) + 35 : FontManager.futuraNormal.getStringWidth(message) + 35;
        this.height = 25;
        this.stayTime = 2000;
        this.y = -1.0F;
        this.notificationType = notificationType;
    }

    public void doRenderNotification(double gettingY, double lastYAxis, boolean rectangle) {
        this.lastY = lastYAxis;
        this.animation.update();

        scaleAnimation.update();
        scaleAnimation.animate(finished() ? 0 : 1, .1, Easings.NONE);

        this.gettingY = (int) gettingY;
        //loat width = Atom.ATOM.getFontBridge().futura.getStringWidth(title) > Atom.ATOM.getFontBridge().futura.getStringWidth(message) ? Atom.ATOM.getFontBridge().futura.getStringWidth(title) + 35 : Atom.ATOM.getFontBridge().futuraSmall.getStringWidth(message) + 35;

        this.animation.animate(finished() ? -200 : this.width, .2, Easings.NONE);

        this.animationY.update();


        final ScaledResolution sr = new ScaledResolution(MC);


        final int x = (int) (sr.getScaledWidth() - this.animation.getValue());
        final int y = (int) this.y;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x - width / 2, y - height / 2, 0);
        GlStateManager.scale(scaleAnimation.getValue(), scaleAnimation.getValue(), 0);
        GlStateManager.translate(-(x - width / 2), -(y - height / 2), 0);


        this.y = gettingY;


        final Color color = new Color(notificationType.color.getRed(), notificationType.color.getGreen(), notificationType.color.getBlue(), 122);

        final float desiredWidth = (float) ((width / 2000) * Math.min(clock.getCurTime(), 2000));

        anim.update();
        anim.animate(desiredWidth, .1, Easings.BOUNCE_OUT);

     /*   if (notificationType != NotificationType.SUCCESS)
            ShaderManager.shaderBy(RoundedRectShader.class).drawRound((float) x, (float) y, (float) this.width, (float) height, 3, color);
        else {
            if (!rectangle)
            RenderUtil.instance.doRenderShadow(new Color(29, 210, 110), x - 2, y - 2, width, 30, 10);
            ShaderManager.shaderBy(RoundedRectShader.class).drawRound((float) x, (float) y, (float) this.width, (float) height, 3, new Color(38, 38, 38, 255));
        }*/

        if (!rectangle) {
            ShaderManager.shaderBy(RoundedRectShader.class).drawRound((float) x, (float) y, (float) width, (float)  height, 3, new Color(20, 20, 20, 255));
            StencilUtil.initStencilToWrite();
            ShaderManager.shaderBy(RoundedRectShader.class).drawRound((float) x, (float) y, (float) width, (float)  height, 3, new Color(20, 20, 20, 255));
            StencilUtil.readStencilBuffer(1);
            ShaderManager.shaderBy(RoundedRectShader.class).drawRound((float) x, y + 23, (float) width, (float)  4, 3, new Color(40, 40, 40, 255));
            ShaderManager.shaderBy(RoundedRectShader.class).drawRound(x, y + 23, anim.getValue() + 9, 4, 1, new Color(30, 30, 30));
            StencilUtil.uninitStencilBuffer();
        }
        FontManager.raleWay20.drawString(notificationTitle, x + 5, y + 6, -1);
        FontManager.interMedium14.drawString(notificationMessage, x + 5, y + 17, Integer.MAX_VALUE);

        if (notificationType == NotificationType.WARNING) {
            RenderUtil.instance.drawCircle(x + 10, y + 12, 8, color.brighter().getRGB());
            FontManager.futuraNormal.drawString("!", x + 8.5f, y + (float) 10.5, -1);
        }

        if (notificationType == NotificationType.ERROR) {
            RenderUtil.instance.drawCircle(x + 10, y + 12, 8, color.brighter().getRGB());
            FontManager.futuraNormal.drawString("/", (float) (x + 8), (float) ((float) y + 7.5), -1);
        }

        GlStateManager.popMatrix();

    }

    public boolean finished() {
        return clock.reachedTime(stayTime);
    }

    public boolean shouldDelete() {
        return finished() && Math.round(animation.getValue()) < -140;
    }

    public enum NotificationType {

        SUCCESS(new Color(58, 220, 120)), WARNING(new Color(229, 146, 54)), ERROR(new Color(231, 46, 46));

        public final Color color;

        NotificationType(java.awt.Color color) {
            this.color = color;
        }

    }

}