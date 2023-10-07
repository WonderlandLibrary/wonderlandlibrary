package dev.monsoon.notification;

import dev.monsoon.Monsoon;
import dev.monsoon.util.font.GloriFontRenderer;
import dev.monsoon.util.misc.PacketUtil;
import dev.monsoon.util.misc.Timer;
import dev.monsoon.util.render.AnimationUtil;
import dev.monsoon.util.render.DrawUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.Sys;

import java.awt.Color;

import static org.lwjgl.opengl.GL11.glColor4f;

public class Notification {
    private NotificationType type;
    private String title;
    private String messsage;
    private long start;

    private long fadedIn;
    private long fadeOut;
    public long end;

    public Timer timer = new Timer();


    public Notification(NotificationType type, String title, String messsage, int length) {
        this.type = type;
        this.title = title;
        this.messsage = messsage;

        timer.reset();
        end = (long) (Monsoon.manager.notifs.time.getValue() * 1000);
    }

    public void show() {
        start = System.currentTimeMillis();
    }

    public boolean isShown() {
        return getTime() <= end;
    }

    long getTime() {
        return System.currentTimeMillis() - start;
    }

    public void render(int count) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        FontRenderer fr = Monsoon.getFont();
        int width = 120;
        double offset = width;
        int height = 0;
        long time = getTime();
        float barThickness = 3;

        Color color = new Color(0, 0, 0, 170);
        Color color1 = new Color(0, 140, 255);

        if (type == NotificationType.INFO) {
            color1 = new Color(255, 255, 255);
        }
        else if (type == NotificationType.WARNING)
            color1 = new Color(255, 255, 0);
        else if (type == NotificationType.ERROR) {
        	color1 = new Color(204, 0, 18);
            int i = Math.max(0, Math.min(255, (int) (Math.sin(time / 100.0) * 255.0 / 2 + 127.5)));
            color = new Color(i, 0, 0, 220);
        }
        else if (type == NotificationType.SUCCESS) {
            color1 = new Color(0, 255, 0);
        }
        else if (type == NotificationType.FAIL) {
            color1 = new Color(255, 0, 0);
        }

        double x = sr.getScaledWidth() - 45 - fr.getStringWidth(messsage),
                y = sr.getScaledHeight() - 54 * count + 2,
                w = sr.getScaledWidth() - 5,
                h = 30;

        float health = timer.getTime();
        double hpPercentage = health / 2570;
        hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
        final double hpWidth = (45 + fr.getStringWidth(messsage)) * hpPercentage;
        double progress = AnimationUtil.INSTANCE.animate(hpWidth, 5, end / 1000);


        Gui.drawRect(x, y, w, y + h, color.getRGB());

        //Gui.drawRect(x + 2, y + 3, x + 24, y + 24, -1);

        Gui.drawRect(x, y + 30, x + progress, y + 28, color1.getRGB());

        fr.drawStringWithShadow(title, x + 28, y + 5, -1);
        fr.drawStringWithShadow(messsage, x + 28, y + 15, -1);

        if (type == NotificationType.INFO) {

            DrawUtil.drawBorderedRoundedRect((float) x +4, (float) y +5, (float) x + 22, (float) y + 22, 19, 2, -1, new Color(0,0,0,0).getRGB());
            fr.drawString("i", x + 12.5f, y + 9.5f, new Color(0,0,0,255).getRGB());

        } else if (type == NotificationType.WARNING) {

           // DrawUtil.drawRoundedRect((float) x + 2, (float) y + 3, (float) x + 24, (float) y + 24, 19, new Color(255,255,0,255).getRGB());
            fr.drawString("\u26A0", x + 12.5f, y + 9.5f, new Color(255,255,0,255).getRGB());

        } else if (type == NotificationType.ERROR) {

            fr.drawString("\u26A0", x + 10f, y + 9.5f, new Color(204,0,18,255).getRGB());

        } else if (type == NotificationType.SUCCESS) {

            //DrawUtil.drawRoundedRect((float) x + 2, (float) y + 3, (float) x + 24, (float) y + 24, 19, new Color(0,255,0,255).getRGB());
            fr.drawString("\u2714", x + 9.5f, y + 9.5f, new Color(0,255,0,255).getRGB());

        } else if (type == NotificationType.FAIL) {
           // DrawUtil.drawRoundedRect((float) x + 2, (float) y + 3, (float) x + 24, (float) y + 24, 19, new Color(255,0,0,255).getRGB());
            fr.drawString("\u2716", x + 9.5f, y + 9.5f, new Color(255,0,0,255).getRGB());
        }
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.func_179090_x();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.startDrawing(7);
        worldrenderer.addVertex(left, bottom, 0.0D);
        worldrenderer.addVertex(right, bottom, 0.0D);
        worldrenderer.addVertex(right, top, 0.0D);
        worldrenderer.addVertex(left, top, 0.0D);
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(int mode, double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.func_179090_x();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.startDrawing(mode);
        worldrenderer.addVertex(left, bottom, 0.0D);
        worldrenderer.addVertex(right, bottom, 0.0D);
        worldrenderer.addVertex(right, top, 0.0D);
        worldrenderer.addVertex(left, top, 0.0D);
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }


}
