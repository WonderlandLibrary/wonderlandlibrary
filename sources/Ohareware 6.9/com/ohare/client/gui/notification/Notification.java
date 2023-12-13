package com.ohare.client.gui.notification;

import com.ohare.client.Client;
import com.ohare.client.module.modules.visuals.HUD;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.TimerUtil;
import com.ohare.client.utils.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class Notification {
    private TimerUtil timer;
    private static Minecraft mc = Minecraft.getMinecraft();
    private float x, oldX, y, oldY, width;
    private String text;
    private int stayTime;
    private boolean done;
    private float stayBar;
    public Notification(String text, int stayTime) {
        this.x = new ScaledResolution(mc).getScaledWidth() - 2;
        this.y = new ScaledResolution(mc).getScaledHeight() - 2;
        this.text = text;
        this.width = Fonts.hudfont.getStringWidth(text) + 8;
        this.stayTime = stayTime;
        timer = new TimerUtil();
        timer.reset();
        stayBar = stayTime;
        done = false;
    }

    public void draw(float prevY) {
        HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule("hud");
        if (done) {
            oldX = x;
            x++;
            y++;
        }
        if (!done() && !done) {
            timer.reset();
            oldX = x;
            x--;
        } else if (timer.reach(stayTime)) done = true;
        if (x < new ScaledResolution(mc).getScaledWidth() - 2 - width) {
            oldX = x;
            x++;
        }
        if (y != prevY) {
            if (y > prevY) {
                oldY = y;
                y--;
            } else if (y < prevY) {
                oldY = y;
                y++;
            }
        }
        if (done() && !done) {
            stayBar = stayTime - timer.time();
        }
        final float finishedX = oldX + (x - oldX);
        final float finishedY = oldY + (y - oldY);
        RenderUtil.drawBordered(finishedX, finishedY, finishedX + width, finishedY + 16, 0.5, new Color(0, 0, 0, 180).getRGB(), new Color(0, 0, 0, 255).getRGB());
        Gui.drawRect(finishedX + 0.5, finishedY + 0.5, finishedX + 3, finishedY + 15.5, hud.staticRainbow.isEnabled() ? color(2, 100) : new Color(0xFF4D4C).getRGB());
        Fonts.hudfont.drawStringWithShadow(text, finishedX + 5, finishedY + 5, -1);
        if (!done() && !done) {
            RenderUtil.drawRect(finishedX, finishedY + 15, width,1, hud.staticRainbow.isEnabled() ? color(2, 100) : new Color(0xFF4D4C).getRGB());
        } else {
            RenderUtil.drawRect(finishedX + 0.5, finishedY + 14.5, ((width - 1) / stayTime) * stayBar,1, hud.staticRainbow.isEnabled() ? color(2, 100) : new Color(0xFF4D4C).getRGB());
        }
        if (delete()) Client.INSTANCE.getNotificationManager().getNotifications().remove(this);
    }

    public boolean done() {
        return x <= new ScaledResolution(mc).getScaledWidth() - 2 - width;
    }

    public boolean delete() {
        return x >= new ScaledResolution(mc).getScaledWidth() - 2 && done;
    }

    public int color(int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(Color.getHSBColor(HUD.hue.getValue(), 1, 1).getRed(), Color.getHSBColor(HUD.hue.getValue(), 1, 1).getGreen(), Color.getHSBColor(HUD.hue.getValue(), 1, 1).getBlue(), hsb);
        float brightness = Math.abs(((getOffset() + (index / (float) count) * 2) % 2) - 1);
        brightness = 0.4f + (0.4f * brightness);
        hsb[2] = brightness % 1f;
        return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
    }

    private float getOffset() {
        return (System.currentTimeMillis() % 2000) / 1000f;
    }
}
