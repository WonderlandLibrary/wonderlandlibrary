/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.ui.Notification;

import java.awt.Color;
import me.Tengoku.Terror.ui.Notification.NotificationType;
import me.Tengoku.Terror.util.RoundedRect;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class Notification {
    private String title;
    private long fadeOut;
    private long end;
    private long fadedIn;
    private long start;
    private String messsage;
    private NotificationType type;

    public static void drawRect(int n, double d, double d2, double d3, double d4, int n2) {
        double d5;
        if (d < d3) {
            d5 = d;
            d = d3;
            d3 = d5;
        }
        if (d2 < d4) {
            d5 = d2;
            d2 = d4;
            d4 = d5;
        }
        float f = (float)(n2 >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n2 & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f2, f3, f4, f);
        worldRenderer.begin(n, DefaultVertexFormats.POSITION);
        worldRenderer.pos(d, d4, 0.0).endVertex();
        worldRenderer.pos(d3, d4, 0.0).endVertex();
        worldRenderer.pos(d3, d2, 0.0).endVertex();
        worldRenderer.pos(d, d2, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public Notification(NotificationType notificationType, String string, String string2, int n) {
        this.type = notificationType;
        this.title = string;
        this.messsage = string2;
        this.fadedIn = 200 * n;
        this.fadeOut = this.fadedIn + (long)(500 * n);
        this.end = this.fadeOut + this.fadedIn;
    }

    private long getTime() {
        return System.currentTimeMillis() - this.start;
    }

    public void show() {
        this.start = System.currentTimeMillis();
    }

    public void render() {
        Color color;
        double d = 0.0;
        int n = 120;
        int n2 = 30;
        long l = this.getTime();
        d = l < this.fadedIn ? Math.tanh((double)l / (double)this.fadedIn * 3.0) * (double)n : (l > this.fadeOut ? Math.tanh(3.0 - (double)(l - this.fadeOut) / (double)(this.end - this.fadeOut) * 3.0) * (double)n : (double)n);
        Color color2 = new Color(0, 0, 0, 220);
        if (this.type == NotificationType.INFO) {
            color = new Color(0, 26, 169);
        } else if (this.type == NotificationType.WARNING) {
            color = new Color(204, 193, 0);
        } else if (this.type == NotificationType.ONMODULE) {
            color = new Color(52, 140, 42);
            color2 = new Color(52, 140, 42);
        } else if (this.type == NotificationType.OFFMODULE) {
            color = new Color(128, 38, 34);
            color2 = new Color(128, 38, 34);
        } else {
            color = new Color(204, 0, 18);
            int n3 = Math.max(0, Math.min(255, (int)(Math.sin((double)l / 100.0) * 255.0 / 2.0 + 127.5)));
            color2 = new Color(n3, 0, 0, 220);
        }
        Minecraft.getMinecraft();
        FontRenderer fontRenderer = Minecraft.fontRendererObj;
        Gui.drawRect(0.0, 0.0, 0.0, 0.0, new Color(0, 0, 0).getRGB());
        RoundedRect.drawRoundedRect((double)GuiScreen.width - d, GuiScreen.height - 5 - n2, GuiScreen.width, GuiScreen.height - 5, 7.0, color2.getRGB());
        Gui.drawRect(0.0, 0.0, 0.0, 0.0, new Color(0, 0, 0).getRGB());
        RoundedRect.drawRoundedRect((double)GuiScreen.width - d, GuiScreen.height - 5 - n2, (double)GuiScreen.width - d + 4.0, GuiScreen.height - 5, 7.0, color.getRGB());
        Gui.drawRect(0.0, 0.0, 0.0, 0.0, new Color(0, 0, 0).getRGB());
        FontUtil.normal.drawString(this.title, (int)((double)GuiScreen.width - d + 6.0), GuiScreen.height - n2, -1);
        FontUtil.normal.drawString(this.messsage, (int)((double)GuiScreen.width - d + 6.0), GuiScreen.height - 18, -1);
    }

    public boolean isShown() {
        return this.getTime() <= this.end;
    }

    public static void drawRect(double d, double d2, double d3, double d4, int n) {
        double d5;
        if (d < d3) {
            d5 = d;
            d = d3;
            d3 = d5;
        }
        if (d2 < d4) {
            d5 = d2;
            d2 = d4;
            d4 = d5;
        }
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f2, f3, f4, f);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(d, d4, 0.0).endVertex();
        worldRenderer.pos(d3, d4, 0.0).endVertex();
        worldRenderer.pos(d3, d2, 0.0).endVertex();
        worldRenderer.pos(d, d2, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}

