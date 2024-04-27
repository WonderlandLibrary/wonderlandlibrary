package net.ccbluex.liquidbounce.Gui.Notifications;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.ccbluex.liquidbounce.Gui.Notifications.Utils.TimeHelper;
import net.ccbluex.liquidbounce.Gui.cfont.CFontRenderer;
import net.ccbluex.liquidbounce.Gui.cfont.FontLoaders;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class Notificationsn {
    private String message;
    private TimeHelper timer;
    double animationx;
    private double lastY, posY, width, height, animationX;
    private int color, imageWidth;
    private ResourceLocation image;
    private long stayTime;
    Minecraft mc = Minecraft.getMinecraft();
    private double delay;
    public static double getAnimationState(double animation, double finalState, double speed) {
        float add = (float) (0.01 * speed);
        if (animation < finalState) {
            if (animation + add < finalState)
                animation += add;
            else
                animation = finalState;
        } else {
            if (animation - add > finalState)
                animation -= add;
            else
                animation = finalState;
        }
        return animation;
    }
    public Notificationsn(final String message, final Type type) {
        this.timer = new TimeHelper();
        timer.reset();
        GameFontRenderer font = Fonts.font35;
        this.width = font.getStringWidth(message) + 35;
        height = 18;
        animationX = width;
        stayTime = 100;
        imageWidth = 8;
        this.delay = this.animationX;
        posY = -1;
        this.image = new ResourceLocation("liquidbounce/notification/" + type.name().toLowerCase() + ".png");
        if (type.equals(Type.INFO)) {
            this.color = new Color(255, 255, 255, 255).getRGB();
        } else if (type.equals(Type.ERROR)) {
            this.color = new Color(255, 255, 255, 255).getRGB();
        } else if (type.equals(Type.SUCCESS)) {
            this.color = new Color(255, 255, 255, 255).getRGB();
        } else if (type.equals(Type.WARNING)) {
            this.color = new Color(255, 255, 255, 255).getRGB();
        }
        if (type == Type.INFO) {
            this.message = EnumChatFormatting.WHITE + message;
        }
        if (type == Type.ERROR) {
            this.message = EnumChatFormatting.WHITE + message;
        }
        if (type == Type.SUCCESS) {
            this.message = EnumChatFormatting.WHITE + message;
        }
        if (type == Type.WARNING) {
            this.message = EnumChatFormatting.WHITE + message;
        }
    }

    public void draw(final double getY, final double lastY) {
        this.lastY = lastY;
        this.animationX = getAnimationState(this.animationX, this.isFinished() ? this.width : 1.0, Math.max(
                this.isFinished() ? 1 : 1, Math.abs(this.animationX - (this.isFinished() ? this.width : 0.0)) * 5.6));
        this.delay = getAnimationState(this.delay, 0.0, 50);
        if (this.posY == -1.0) {
            this.posY = getY;
        } else {
            this.posY = getAnimationState(this.posY, getY, 200.0);
        }
        final ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        final int x1 = (int) (res.getScaledWidth() - this.width + this.animationX);
        final int x2 = (int) (res.getScaledWidth() + this.animationX);
        final int y1 = (int) this.posY;
        final int y2 = (int) (y1 + this.height);
        Gui.drawRect(x1 + 15, y1, x2, y2, new Color(40, 40,40, 220).getRGB());
        RenderUtils.drawRect(x1 + 15, y1 + 17, (float) (x2 - delay), y2, color);
        GameFontRenderer font = Fonts.font35;
        RenderUtils.drawImage(this.image, (int) (x1 + (this.height - this.imageWidth) / 2.0 + 14),
                y1 + (int) ((this.height - this.imageWidth) / 2.0), this.imageWidth, this.imageWidth);
        font.drawCenteredString(this.message, (float) (x1 + this.width / 2.0) + 12.0f,
                (float) (y1 + this.height / 3.5) + 1, -1);
    }

    public static int reAlpha(final int n, final float n2) {
        final Color color = new Color(n);
        return new Color(0.003921569f * color.getRed(), 0.003921569f * color.getGreen(), 0.003921569f * color.getBlue(),
                n2).getRGB();
    }

    public boolean shouldDelete() {
        return this.isFinished() && this.animationX >= this.width;
    }

    private boolean isFinished() {
        return this.delay == 0;
    }

    public double getHeight() {
        return this.height;
    }

    public enum Type {
        SUCCESS("SUCCESS", 0), INFO("INFO", 1), WARNING("WARNING", 2), ERROR("ERROR", 3);

        private Type(final String s, final int n) {
        }
    }
}

