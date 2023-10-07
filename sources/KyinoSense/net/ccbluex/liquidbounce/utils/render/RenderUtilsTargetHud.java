/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.WorldRenderer
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.EnumRarity
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.Timer
 *  net.minecraft.world.World
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.render.Animation;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtilsTargetHud;
import net.ccbluex.liquidbounce.utils.render.GLUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import net.minecraft.world.World;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public final class RenderUtilsTargetHud
extends MinecraftInstance {
    private static final Map<Integer, Boolean> glCapMap = new HashMap<Integer, Boolean>();
    public static final Pattern COLOR_PATTERN = Pattern.compile("(?i)");
    public static int deltaTime;
    public static Minecraft mc;
    public static float delta;
    private static final int[] DISPLAY_LISTS_2D;
    private static final Frustum frustrum;
    protected static float zLevel;
    private static final IntBuffer viewport;
    private static final FloatBuffer modelview;
    private static final FloatBuffer projections;

    public static void targetHudRect(double x, double y, double x1, double y1, double size) {
        RenderUtilsTargetHud.rectangleBordered(x, y + -4.0, x1 + size, y1 + size, 0.5, new Color(60, 60, 60).getRGB(), new Color(10, 10, 10).getRGB());
        RenderUtilsTargetHud.rectangleBordered(x + 1.0, y + -3.0, x1 + size - 1.0, y1 + size - 1.0, 1.0, new Color(40, 40, 40).getRGB(), new Color(40, 40, 40).getRGB());
        RenderUtilsTargetHud.rectangleBordered(x + 2.5, y - 1.5, x1 + size - 2.5, y1 + size - 2.5, 0.5, new Color(40, 40, 40).getRGB(), new Color(60, 60, 60).getRGB());
        RenderUtilsTargetHud.rectangleBordered(x + 2.5, y - 1.5, x1 + size - 2.5, y1 + size - 2.5, 0.5, new Color(22, 22, 22).getRGB(), new Color(255, 255, 255, 0).getRGB());
    }

    public static void targetHudRect1(double x, double y, double x1, double y1, double size) {
        RenderUtilsTargetHud.rectangleBordered(x + 4.35, y + 0.5, x1 + size - 84.5, y1 + size - 4.35, 0.5, new Color(48, 48, 48).getRGB(), new Color(10, 10, 10).getRGB());
        RenderUtilsTargetHud.rectangleBordered(x + 5.0, y + 1.0, x1 + size - 85.0, y1 + size - 5.0, 0.5, new Color(17, 17, 17).getRGB(), new Color(255, 255, 255, 0).getRGB());
    }

    public static void drawFastRoundedRect(float x0, float y0, float x1, float y1, float radius, int color) {
        float f11;
        int i;
        boolean Semicircle = true;
        float f = 5.0f;
        float f2 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f3 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f4 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f5 = (float)(color & 0xFF) / 255.0f;
        GL11.glDisable((int)2884);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GlStateManager.func_179147_l();
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179090_x();
        GL11.glColor4f((float)f3, (float)f4, (float)f5, (float)f2);
        GL11.glBegin((int)5);
        GL11.glVertex2f((float)(x0 + radius), (float)y0);
        GL11.glVertex2f((float)(x0 + radius), (float)y1);
        GL11.glVertex2f((float)(x1 - radius), (float)y0);
        GL11.glVertex2f((float)(x1 - radius), (float)y1);
        GL11.glEnd();
        GL11.glBegin((int)5);
        GL11.glVertex2f((float)x0, (float)(y0 + radius));
        GL11.glVertex2f((float)(x0 + radius), (float)(y0 + radius));
        GL11.glVertex2f((float)x0, (float)(y1 - radius));
        GL11.glVertex2f((float)(x0 + radius), (float)(y1 - radius));
        GL11.glEnd();
        GL11.glBegin((int)5);
        GL11.glVertex2f((float)x1, (float)(y0 + radius));
        GL11.glVertex2f((float)(x1 - radius), (float)(y0 + radius));
        GL11.glVertex2f((float)x1, (float)(y1 - radius));
        GL11.glVertex2f((float)(x1 - radius), (float)(y1 - radius));
        GL11.glEnd();
        GL11.glBegin((int)6);
        float f6 = x1 - radius;
        float f7 = y0 + radius;
        GL11.glVertex2f((float)f6, (float)f7);
        boolean j = false;
        for (i = 0; i <= 18; ++i) {
            f11 = (float)i * 5.0f;
            GL11.glVertex2f((float)((float)((double)f6 + (double)radius * Math.cos(Math.toRadians(f11)))), (float)((float)((double)f7 - (double)radius * Math.sin(Math.toRadians(f11)))));
        }
        GL11.glEnd();
        GL11.glBegin((int)6);
        f6 = x0 + radius;
        f7 = y0 + radius;
        GL11.glVertex2f((float)f6, (float)f7);
        for (i = 0; i <= 18; ++i) {
            f11 = (float)i * 5.0f;
            GL11.glVertex2f((float)((float)((double)f6 - (double)radius * Math.cos(Math.toRadians(f11)))), (float)((float)((double)f7 - (double)radius * Math.sin(Math.toRadians(f11)))));
        }
        GL11.glEnd();
        GL11.glBegin((int)6);
        f6 = x0 + radius;
        f7 = y1 - radius;
        GL11.glVertex2f((float)f6, (float)f7);
        for (i = 0; i <= 18; ++i) {
            f11 = (float)i * 5.0f;
            GL11.glVertex2f((float)((float)((double)f6 - (double)radius * Math.cos(Math.toRadians(f11)))), (float)((float)((double)f7 + (double)radius * Math.sin(Math.toRadians(f11)))));
        }
        GL11.glEnd();
        GL11.glBegin((int)6);
        f6 = x1 - radius;
        f7 = y1 - radius;
        GL11.glVertex2f((float)f6, (float)f7);
        for (i = 0; i <= 18; ++i) {
            f11 = (float)i * 5.0f;
            GL11.glVertex2f((float)((float)((double)f6 + (double)radius * Math.cos(Math.toRadians(f11)))), (float)((float)((double)f7 + (double)radius * Math.sin(Math.toRadians(f11)))));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2884);
        GL11.glDisable((int)3042);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawImage(ResourceLocation image2, double x, double y, double width, double height) {
        new ScaledResolution(Minecraft.func_71410_x());
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(image2);
        RenderUtilsTargetHud.drawModalRectWithCustomSizedTexture((float)x, (float)y, 0.0f, 0.0f, (float)width, (float)height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = 0.003921569f * (float)c.getRed();
        float g = 0.003921569f * (float)c.getGreen();
        float b = 0.003921569f * (float)c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }

    public static void drawDimRect(double x, double y, double x2, double y2, int col1) {
        RenderUtilsTargetHud.drawRect(x, y, x2, y2, col1);
        float f2 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(col1 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)0.2f);
        GL11.glLineWidth((float)2.0f);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static int width() {
        return new ScaledResolution(Minecraft.func_71410_x()).func_78326_a();
    }

    public static int height() {
        return new ScaledResolution(Minecraft.func_71410_x()).func_78328_b();
    }

    public static int rainbow(int delay) {
        double rainbow = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 10.0);
        return Color.getHSBColor((float)((rainbow %= 360.0) / 360.0), 0.5f, 1.0f).getRGB();
    }

    public static int rainbow(int delay, float slowspeed) {
        double rainbow = Math.ceil((float)(System.currentTimeMillis() + (long)delay) / slowspeed);
        return Color.getHSBColor((float)((rainbow %= 360.0) / 360.0), 0.5f, 1.0f).getRGB();
    }

    public static void disableSmoothLine() {
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3008);
        GL11.glDepthMask((boolean)true);
        GL11.glCullFace((int)1029);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    public static double getAnimationState(double animation, double finalState, double speed) {
        float add = (float)(0.01 * speed);
        animation = animation < finalState ? (animation + (double)add < finalState ? animation + (double)add : finalState) : (animation - (double)add > finalState ? animation - (double)add : finalState);
        return animation;
    }

    public static void drawImage(ResourceLocation image2, int x, int y, int width, int height2, Color color) {
        new ScaledResolution(Minecraft.func_71410_x());
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getRed() / 255.0f), (float)1.0f);
        Minecraft.func_71410_x().func_110434_K().func_110577_a(image2);
        Gui.func_146110_a((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height2, (float)width, (float)height2);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void doGlScissor(int x, int y, int width, int height2) {
        Minecraft mc = Minecraft.func_71410_x();
        int scaleFactor = 1;
        int k = mc.field_71474_y.field_74335_Z;
        if (k == 0) {
            k = 1000;
        }
        while (scaleFactor < k && mc.field_71443_c / (scaleFactor + 1) >= 320 && mc.field_71440_d / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        GL11.glScissor((int)(x * scaleFactor), (int)(mc.field_71440_d - (y + height2) * scaleFactor), (int)(width * scaleFactor), (int)(height2 * scaleFactor));
    }

    public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
        RenderUtilsTargetHud.drawRect(x + 0.5f, y, x1 - 0.5f, y + 0.5f, insideC);
        RenderUtilsTargetHud.drawRect(x + 0.5f, y1 - 0.5f, x1 - 0.5f, y1, insideC);
        RenderUtilsTargetHud.drawRect(x, y + 0.5f, x1, y1 - 0.5f, insideC);
    }

    public static void circle(float x, float y, float radius, int fill) {
        RenderUtilsTargetHud.arc(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void circle(float x, float y, float radius, Color fill) {
        RenderUtilsTargetHud.arc(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void arc(float x, float y, float start, float end, float radius, int color) {
        RenderUtilsTargetHud.arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void arc(float x, float y, float start, float end, float radius, Color color) {
        RenderUtilsTargetHud.arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void arcEllipse(float x, float y, float start, float end, float w, float h, int color) {
        float ldy;
        float ldx;
        float i;
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        float temp = 0.0f;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)f1, (float)f2, (float)f3, (float)f);
        if (f > 0.5f) {
            GL11.glEnable((int)2848);
            GL11.glLineWidth((float)2.0f);
            GL11.glBegin((int)3);
            for (i = end; i >= start; i -= 4.0f) {
                ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w * 1.001f;
                ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h * 1.001f;
                GL11.glVertex2f((float)(x + ldx), (float)(y + ldy));
            }
            GL11.glEnd();
            GL11.glDisable((int)2848);
        }
        GL11.glBegin((int)6);
        for (i = end; i >= start; i -= 4.0f) {
            ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w;
            ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h;
            GL11.glVertex2f((float)(x + ldx), (float)(y + ldy));
        }
        GL11.glEnd();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawBorderedRect(double x2, double d2, double x22, double e2, float l1, int col1, int col2) {
        RenderUtilsTargetHud.drawRect(x2, d2, x22, e2, col2);
        float f2 = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f22 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(col1 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glColor4f((float)f22, (float)f3, (float)f4, (float)f2);
        GL11.glLineWidth((float)l1);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x2, (double)d2);
        GL11.glVertex2d((double)x2, (double)e2);
        GL11.glVertex2d((double)x22, (double)e2);
        GL11.glVertex2d((double)x22, (double)d2);
        GL11.glVertex2d((double)x2, (double)d2);
        GL11.glVertex2d((double)x22, (double)d2);
        GL11.glVertex2d((double)x2, (double)e2);
        GL11.glVertex2d((double)x22, (double)e2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void pre() {
        GL11.glDisable((int)2929);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
    }

    public static void post() {
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glColor3d((double)1.0, (double)1.0, (double)1.0);
    }

    public static void enableRender3D(boolean disableDepth) {
        if (disableDepth) {
            GL11.glDepthMask((boolean)false);
            GL11.glDisable((int)2929);
        }
        GL11.glDisable((int)3008);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)1.0f);
    }

    public static void disableRender3D(boolean enableDepth) {
        if (enableDepth) {
            GL11.glDepthMask((boolean)true);
            GL11.glEnable((int)2929);
        }
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3008);
        GL11.glDisable((int)2848);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void setColor(int colorHex) {
        float alpha = (float)(colorHex >> 24 & 0xFF) / 255.0f;
        float red = (float)(colorHex >> 16 & 0xFF) / 255.0f;
        float green = (float)(colorHex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(colorHex & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static int getDisplayWidth() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int displayWidth = scaledResolution.func_78326_a();
        return displayWidth;
    }

    public static int getDisplayHeight() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int displayHeight = scaledResolution.func_78328_b();
        return displayHeight;
    }

    public static int darker(int hexColor, int factor) {
        float alpha = hexColor >> 24 & 0xFF;
        float red = Math.max((float)(hexColor >> 16 & 0xFF) - (float)(hexColor >> 16 & 0xFF) / (100.0f / (float)factor), 0.0f);
        float green = Math.max((float)(hexColor >> 8 & 0xFF) - (float)(hexColor >> 8 & 0xFF) / (100.0f / (float)factor), 0.0f);
        float blue = Math.max((float)(hexColor & 0xFF) - (float)(hexColor & 0xFF) / (100.0f / (float)factor), 0.0f);
        return (int)((float)(((int)alpha << 24) + ((int)red << 16) + ((int)green << 8)) + blue);
    }

    public static void drawBorderedRect(float x, float y, float width, float height, float borderWidth, Color rectColor, Color borderColor) {
        RenderUtilsTargetHud.drawBorderedRect(x, y, width, height, borderWidth, rectColor.getRGB(), borderColor.getRGB());
    }

    public static void drawRoundedRect3(float x, float y, float x2, float y2, float round, int color, int mode) {
        float rectX = x;
        float rectY = y;
        float rectX2 = x2;
        float rectY2 = y2;
        x += (float)((double)(round / 2.0f) + 0.5);
        y += (float)((double)(round / 2.0f) + 0.5);
        x2 -= (float)((double)(round / 2.0f) + 0.5);
        y2 -= (float)((double)(round / 2.0f) + 0.5);
        if (mode == 1) {
            RenderUtilsTargetHud.drawRect(x, rectY, rectX2, rectY2, color);
        } else {
            RenderUtilsTargetHud.drawRect(rectX, rectY, x2, rectY2, color);
        }
        RenderUtilsTargetHud.circle(x2 - round / 2.0f, y + round / 2.0f, round, color);
        RenderUtilsTargetHud.circle(x + round / 2.0f, y2 - round / 2.0f, round, color);
        RenderUtilsTargetHud.circle(x + round / 2.0f, y + round / 2.0f, round, color);
        RenderUtilsTargetHud.circle(x2 - round / 2.0f, y2 - round / 2.0f, round, color);
        RenderUtilsTargetHud.drawRect((float)((int)(x - round / 2.0f - 0.5f)), (float)((int)(y + round / 2.0f)), (float)((int)x2), (float)((int)(y2 - round / 2.0f)), color);
        RenderUtilsTargetHud.drawRect((float)((int)x), (float)((int)(y + round / 2.0f)), (float)((int)(x2 + round / 2.0f + 0.5f)), (float)((int)(y2 - round / 2.0f)), color);
        RenderUtilsTargetHud.drawRect((float)((int)(x + round / 2.0f)), (float)((int)(y - round / 2.0f - 0.5f)), (float)((int)(x2 - round / 2.0f)), (float)((int)(y2 - round / 2.0f)), color);
        RenderUtilsTargetHud.drawRect((float)((int)(x + round / 2.0f)), (float)((int)y), (float)((int)(x2 - round / 2.0f)), (float)((int)(y2 + round / 2.0f + 0.5f)), color);
    }

    public static void drawFullCircle(float x, float y, float radius, int color, int outSideColor) {
        int i;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        float outSideAlpha = (float)(outSideColor >> 24 & 0xFF) / 255.0f;
        float outSideRed = (float)(outSideColor >> 16 & 0xFF) / 255.0f;
        float outSideGreen = (float)(outSideColor >> 8 & 0xFF) / 255.0f;
        float outSideBlue = (float)(outSideColor & 0xFF) / 255.0f;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)outSideRed, (float)outSideGreen, (float)outSideBlue, (float)outSideAlpha);
        if (alpha > 0.5f) {
            GL11.glEnable((int)2881);
            GL11.glEnable((int)2848);
            RenderUtilsTargetHud.enableSmoothLine(2.0f);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glBegin((int)3);
            for (i = 0; i <= 360; ++i) {
                GL11.glVertex2d((double)((double)x + Math.sin((double)i * 3.141526 / 180.0) * (double)radius), (double)((double)y + Math.cos((double)i * 3.141526 / 180.0) * (double)radius));
            }
            GL11.glEnd();
            GL11.glDisable((int)2848);
            GL11.glDisable((int)2881);
            GlStateManager.func_179117_G();
        }
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)6);
        for (i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double)((double)x + Math.sin((double)i * 3.141526 / 180.0) * (double)radius), (double)((double)y + Math.cos((double)i * 3.141526 / 180.0) * (double)radius));
        }
        GL11.glEnd();
        RenderUtilsTargetHud.disableSmoothLine();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void enableSmoothLine(float width) {
        GL11.glDisable((int)3008);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glEnable((int)2884);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glLineWidth((float)width);
    }

    public static void startGlScissor(int x, int y, int width, int height) {
        int scaleFactor = new ScaledResolution(mc).func_78325_e();
        GL11.glPushMatrix();
        GL11.glEnable((int)3089);
        GL11.glScissor((int)(x * scaleFactor), (int)(RenderUtilsTargetHud.mc.field_71440_d - (y + height) * scaleFactor), (int)(width * scaleFactor), (int)((height += 14) * scaleFactor));
    }

    public static void stopGlScissor() {
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
    }

    public static void arcEllipse(float x, float y, float start, float end, float w, float h, Color color) {
        float ldy;
        float ldx;
        float i;
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        float temp = 0.0f;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        Tessellator var9 = Tessellator.func_178181_a();
        var9.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        if ((float)color.getAlpha() > 0.5f) {
            GL11.glEnable((int)2848);
            GL11.glLineWidth((float)2.0f);
            GL11.glBegin((int)3);
            for (i = end; i >= start; i -= 4.0f) {
                ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w * 1.001f;
                ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h * 1.001f;
                GL11.glVertex2f((float)(x + ldx), (float)(y + ldy));
            }
            GL11.glEnd();
            GL11.glDisable((int)2848);
        }
        GL11.glBegin((int)6);
        for (i = end; i >= start; i -= 4.0f) {
            ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w;
            ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h;
            GL11.glVertex2f((float)(x + ldx), (float)(y + ldy));
        }
        GL11.glEnd();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawNoFullCircle(float x, float y, float radius, int fill) {
        RenderUtilsTargetHud.arc233(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void arc233(float x, float y, float start, float end, float radius, int color) {
        RenderUtilsTargetHud.arcEllipse233(x, y, start, end, radius, radius, color);
    }

    public static void arcEllipse233(float x, float y, float start, float end, float w, float h, int color) {
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        if (start > end) {
            float temp = end;
            end = start;
            start = temp;
        }
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
        if (alpha > 0.5f) {
            GL11.glEnable((int)2881);
            GL11.glEnable((int)2848);
            GL11.glLineWidth((float)1.5f);
            GL11.glBegin((int)3);
            for (float i = end; i >= start; i -= 4.0f) {
                float ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w * 1.001f;
                float ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h * 1.001f;
                GL11.glVertex2f((float)(x + ldx), (float)(y + ldy));
            }
            GL11.glEnd();
            GL11.glDisable((int)2848);
            GL11.glDisable((int)2881);
        }
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawRectBlur(float x, float y, float x2, float y2, int color) {
        GLUtils.glEnable(3042);
        GLUtils.glDisable(3553);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtils.glEnable(2848);
        RenderUtilsTargetHud.glColor(color);
        RenderUtilsTargetHud.quickDrawRect(x, y, x2, y2);
        GLUtils.glEnable(3553);
        GLUtils.glDisable(3042);
        GLUtils.glDisable(2848);
    }

    public static void drawFilledCircle(int xx, int yy, float radius, int col) {
        float f = (float)(col >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col & 0xFF) / 255.0f;
        int sections = 50;
        double dAngle = Math.PI * 2 / (double)sections;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glBegin((int)6);
        for (int i = 0; i < sections; ++i) {
            float x = (float)((double)radius * Math.sin((double)i * dAngle));
            float y = (float)((double)radius * Math.cos((double)i * dAngle));
            GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
            GL11.glVertex2f((float)((float)xx + x), (float)((float)yy + y));
        }
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    }

    public static void drawFilledCircle(float xx, float yy, float radius, int col) {
        float f = (float)(col >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col & 0xFF) / 255.0f;
        int sections = 50;
        double dAngle = Math.PI * 2 / (double)sections;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)6);
        for (int i = 0; i < sections; ++i) {
            float x = (float)((double)radius * Math.sin((double)i * dAngle));
            float y = (float)((double)radius * Math.cos((double)i * dAngle));
            GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
            GL11.glVertex2f((float)(xx + x), (float)(yy + y));
        }
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    }

    public static void glColor(int hex, int alpha) {
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)((float)alpha / 255.0f));
    }

    public static void glColor(int hex, float alpha) {
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void glColor(Color color, float alpha) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, float zLevel) {
        float f = 0.00390625f;
        float f1 = 0.00390625f;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        worldrenderer.func_181662_b((double)(x + 0), (double)(y + height), (double)zLevel).func_181673_a((double)((float)(textureX + 0) * f), (double)((float)(textureY + height) * f1)).func_181675_d();
        worldrenderer.func_181662_b((double)(x + width), (double)(y + height), (double)zLevel).func_181673_a((double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f1)).func_181675_d();
        worldrenderer.func_181662_b((double)(x + width), (double)(y + 0), (double)zLevel).func_181673_a((double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f1)).func_181675_d();
        worldrenderer.func_181662_b((double)(x + 0), (double)(y + 0), (double)zLevel).func_181673_a((double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f1)).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void drawShadow(float x, float y, float width, float height) {
        RenderUtilsTargetHud.drawTexturedRect(x - 9.0f, y - 9.0f, 9.0f, 9.0f, "paneltopleft");
        RenderUtilsTargetHud.drawTexturedRect(x - 9.0f, y + height, 9.0f, 9.0f, "panelbottomleft");
        RenderUtilsTargetHud.drawTexturedRect(x + width, y + height, 9.0f, 9.0f, "panelbottomright");
        RenderUtilsTargetHud.drawTexturedRect(x + width, y - 9.0f, 9.0f, 9.0f, "paneltopright");
        RenderUtilsTargetHud.drawTexturedRect(x - 9.0f, y, 9.0f, height, "panelleft");
        RenderUtilsTargetHud.drawTexturedRect(x + width, y, 9.0f, height, "panelright");
        RenderUtilsTargetHud.drawTexturedRect(x, y - 9.0f, width, 9.0f, "paneltop");
        RenderUtilsTargetHud.drawTexturedRect(x, y + height, width, 9.0f, "panelbottom");
    }

    public static void renderShadowVertical(Color c, float lineWidth, double startAlpha, int size, double posX, double posY1, double posY2, boolean right, boolean edges) {
        GlStateManager.func_179117_G();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2884);
        RenderUtilsTargetHud.renderShadowVertical(lineWidth, startAlpha, size, posX, posY1, posY2, right, edges, (float)c.getRed() / 255.0f, (float)c.getGreen() / 255.0f, (float)c.getBlue() / 255.0f);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2884);
        GL11.glEnable((int)3008);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)3042);
    }

    public static void renderShadowVertical(float lineWidth, double startAlpha, int size, double posX, double posY1, double posY2, boolean right, boolean edges, float red, float green, float blue) {
        double alpha = startAlpha;
        GlStateManager.func_179092_a((int)516, (float)0.0f);
        GL11.glLineWidth((float)lineWidth);
        if (right) {
            for (double x = 0.5; x < (double)size; x += 0.5) {
                GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
                GL11.glBegin((int)1);
                GL11.glVertex2d((double)(posX + x), (double)(posY1 - (edges ? x : 0.0)));
                GL11.glVertex2d((double)(posX + x), (double)(posY2 + (edges ? x : 0.0)));
                GL11.glEnd();
                alpha = startAlpha - x / (double)size;
            }
        } else {
            for (double x = 0.0; x < (double)size; x += 0.5) {
                GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
                GL11.glBegin((int)1);
                GL11.glVertex2d((double)(posX - x), (double)(posY1 - (edges ? x : 0.0)));
                GL11.glVertex2d((double)(posX - x), (double)(posY2 + (edges ? x : 0.0)));
                GL11.glEnd();
                alpha = startAlpha - x / (double)size;
            }
        }
    }

    public static void drawModel(float yaw, float pitch, EntityLivingBase entityLivingBase) {
        GlStateManager.func_179117_G();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179142_g();
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)50.0f);
        GlStateManager.func_179152_a((float)-50.0f, (float)50.0f, (float)50.0f);
        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        float renderYawOffset = entityLivingBase.field_70761_aq;
        float rotationYaw = entityLivingBase.field_70177_z;
        float rotationPitch = entityLivingBase.field_70125_A;
        float prevRotationYawHead = entityLivingBase.field_70758_at;
        float rotationYawHead = entityLivingBase.field_70759_as;
        GlStateManager.func_179114_b((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.func_74519_b();
        GlStateManager.func_179114_b((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)(-Math.atan(pitch / 40.0f) * 20.0)), (float)1.0f, (float)0.0f, (float)0.0f);
        entityLivingBase.field_70761_aq = yaw - yaw / yaw * 0.4f;
        entityLivingBase.field_70177_z = yaw - yaw / yaw * 0.2f;
        entityLivingBase.field_70125_A = pitch;
        entityLivingBase.field_70759_as = entityLivingBase.field_70177_z;
        entityLivingBase.field_70758_at = entityLivingBase.field_70177_z;
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.0f);
        RenderManager renderManager = mc.func_175598_ae();
        renderManager.func_178631_a(180.0f);
        renderManager.func_178633_a(false);
        entityLivingBase.func_174833_aM();
        renderManager.func_147940_a((Entity)entityLivingBase, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        renderManager.func_178633_a(true);
        entityLivingBase.field_70761_aq = renderYawOffset;
        entityLivingBase.field_70177_z = rotationYaw;
        entityLivingBase.field_70125_A = rotationPitch;
        entityLivingBase.field_70758_at = prevRotationYawHead;
        entityLivingBase.field_70759_as = rotationYawHead;
        GlStateManager.func_179121_F();
        RenderHelper.func_74518_a();
        GlStateManager.func_179101_C();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77476_b);
        GlStateManager.func_179090_x();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77478_a);
        GlStateManager.func_179117_G();
    }

    public static void drawCheckeredBackground(float x, float y, float x2, float y2) {
        RenderUtilsTargetHud.drawRect(x, y, x2, y2, RenderUtilsTargetHud.getColor(0xFFFFFF));
        boolean offset = false;
        while (y < y2) {
            offset = !offset;
            for (float x3 = x + (float)(offset ? true : false); x3 < x2; x3 += 2.0f) {
                if (!(x3 <= x2 - 1.0f)) continue;
                RenderUtilsTargetHud.drawRect(x3, y, x3 + 1.0f, y + 1.0f, RenderUtilsTargetHud.getColor(0x808080));
            }
            y += 1.0f;
        }
    }

    public static int getColor(int color) {
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        int a = 255;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF | 0xFF000000;
    }

    public static int darker(int color, float factor) {
        int r = (int)((float)(color >> 16 & 0xFF) * factor);
        int g = (int)((float)(color >> 8 & 0xFF) * factor);
        int b = (int)((float)(color & 0xFF) * factor);
        int a = color >> 24 & 0xFF;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF | (a & 0xFF) << 24;
    }

    public static void drawGradientRect(double left, double top, double right, double bottom, boolean sideways, int startColor, int endColor) {
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)7);
        RenderUtilsTargetHud.color(startColor);
        if (sideways) {
            GL11.glVertex2d((double)left, (double)top);
            GL11.glVertex2d((double)left, (double)bottom);
            RenderUtilsTargetHud.color(endColor);
            GL11.glVertex2d((double)right, (double)bottom);
            GL11.glVertex2d((double)right, (double)top);
        } else {
            GL11.glVertex2d((double)left, (double)top);
            RenderUtilsTargetHud.color(endColor);
            GL11.glVertex2d((double)left, (double)bottom);
            GL11.glVertex2d((double)right, (double)bottom);
            RenderUtilsTargetHud.color(startColor);
            GL11.glVertex2d((double)right, (double)top);
        }
        GL11.glEnd();
        GL11.glDisable((int)3042);
        GL11.glShadeModel((int)7424);
        GL11.glEnable((int)3553);
    }

    public static void drawGradientRect(float left, float top, float right, float bottom, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(startColor & 0xFF) / 255.0f;
        float f5 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179103_j((int)7425);
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        worldrenderer.func_181662_b((double)right, (double)top, 0.0).func_181666_a(f2, f3, f4, f).func_181675_d();
        worldrenderer.func_181662_b((double)left, (double)top, 0.0).func_181666_a(f2, f3, f4, f).func_181675_d();
        worldrenderer.func_181662_b((double)left, (double)bottom, 0.0).func_181666_a(f6, f7, f8, f5).func_181675_d();
        worldrenderer.func_181662_b((double)right, (double)bottom, 0.0).func_181666_a(f6, f7, f8, f5).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179103_j((int)7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
    }

    public static void drawGradientSidewaysV(double left, double top, double right, double bottom, int col1, int col2) {
        GLUtils.glEnable(3042);
        GLUtils.glDisable(3553);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtils.glEnable(2848);
        GL11.glShadeModel((int)7425);
        RenderUtilsTargetHud.quickDrawGradientSidewaysV(left, top, right, bottom, col1, col2);
        GLUtils.glEnable(3553);
        GLUtils.glDisable(3042);
        GLUtils.glDisable(2848);
        GL11.glShadeModel((int)7424);
    }

    public static void quickDrawGradientSidewaysV(double left, double top, double right, double bottom, int col1, int col2) {
        GL11.glBegin((int)7);
        RenderUtilsTargetHud.glColor(col1);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)right, (double)top);
        RenderUtilsTargetHud.glColor(col2);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glEnd();
    }

    public static Color getGradientOffset(Color color1, Color color2, double offset) {
        int redPart;
        double inverse_percent;
        if (offset > 1.0) {
            inverse_percent = offset % 1.0;
            redPart = (int)offset;
            offset = redPart % 2 == 0 ? inverse_percent : 1.0 - inverse_percent;
        }
        inverse_percent = 1.0 - offset;
        redPart = (int)((double)color1.getRed() * inverse_percent + (double)color2.getRed() * offset);
        int greenPart = (int)((double)color1.getGreen() * inverse_percent + (double)color2.getGreen() * offset);
        int bluePart = (int)((double)color1.getBlue() * inverse_percent + (double)color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }

    public static void scissor(double x, double y, double width, double height) {
        int scaleFactor;
        for (scaleFactor = new ScaledResolution(Minecraft.func_71410_x()).func_78325_e(); scaleFactor < 2 && Minecraft.func_71410_x().field_71443_c / (scaleFactor + 1) >= 320 && Minecraft.func_71410_x().field_71440_d / (scaleFactor + 1) >= 240; ++scaleFactor) {
        }
        GL11.glScissor((int)((int)(x * (double)scaleFactor)), (int)((int)((double)Minecraft.func_71410_x().field_71440_d - (y + height) * (double)scaleFactor)), (int)((int)(width * (double)scaleFactor)), (int)((int)(height * (double)scaleFactor)));
    }

    public static void drawClickGuiArrow(float x, float y, float size, Animation animation, int color) {
        GL11.glTranslatef((float)x, (float)y, (float)0.0f);
        double[] interpolation = new double[1];
        RenderUtilsTargetHud.setup2DRendering(() -> RenderUtilsTargetHud.render(5, () -> {
            RenderUtilsTargetHud.color(color);
            interpolation[0] = RenderUtilsTargetHud.interpolate(0.0, (double)size / 2.0, animation.getOutput());
            if (animation.getOutput() >= 0.48) {
                GL11.glVertex2d((double)(size / 2.0f), (double)RenderUtilsTargetHud.interpolate((double)size / 2.0, 0.0, animation.getOutput()));
            }
            GL11.glVertex2d((double)0.0, (double)interpolation[0]);
            if (animation.getOutput() < 0.48) {
                GL11.glVertex2d((double)(size / 2.0f), (double)RenderUtilsTargetHud.interpolate((double)size / 2.0, 0.0, animation.getOutput()));
            }
            GL11.glVertex2d((double)size, (double)interpolation[0]);
        }));
        GL11.glTranslatef((float)(-x), (float)(-y), (float)0.0f);
    }

    public static void render(int mode, Runnable render) {
        GL11.glBegin((int)mode);
        render.run();
        GL11.glEnd();
    }

    public static void setup2DRendering(Runnable f) {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        f.run();
        GL11.glEnable((int)3553);
        GlStateManager.func_179084_k();
    }

    public static void drawTexturedRect(float x, float y, float width, float height, String image2) {
        boolean disableAlpha;
        GL11.glPushMatrix();
        boolean enableBlend = GL11.glIsEnabled((int)3042);
        boolean bl = disableAlpha = !GL11.glIsEnabled((int)3008);
        if (!enableBlend) {
            GLUtils.glEnable(3042);
        }
        if (!disableAlpha) {
            GLUtils.glDisable(3008);
        }
        mc.func_110434_K().func_110577_a(new ResourceLocation("liquidbounce+/ui/" + image2 + ".png"));
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        if (!enableBlend) {
            GLUtils.glDisable(3042);
        }
        if (!disableAlpha) {
            GLUtils.glEnable(3008);
        }
        GL11.glPopMatrix();
    }

    public static void drawModalRectWithCustomSizedTexture(float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        float f = 1.0f / textureWidth;
        float f1 = 1.0f / textureHeight;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        worldrenderer.func_181662_b((double)x, (double)(y + height), 0.0).func_181673_a((double)(u * f), (double)((v + height) * f1)).func_181675_d();
        worldrenderer.func_181662_b((double)(x + width), (double)(y + height), 0.0).func_181673_a((double)((u + width) * f), (double)((v + height) * f1)).func_181675_d();
        worldrenderer.func_181662_b((double)(x + width), (double)y, 0.0).func_181673_a((double)((u + width) * f), (double)(v * f1)).func_181675_d();
        worldrenderer.func_181662_b((double)x, (double)y, 0.0).func_181673_a((double)(u * f), (double)(v * f1)).func_181675_d();
        tessellator.func_78381_a();
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return RenderUtilsTargetHud.isInViewFrustrum(entity.func_174813_aQ()) || entity.field_70158_ak;
    }

    private static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = mc.func_175606_aa();
        frustrum.func_78547_a(current.field_70165_t, current.field_70163_u, current.field_70161_v);
        return frustrum.func_78546_a(bb);
    }

    public static float interpolate(float current, float old, float scale) {
        return old + (current - old) * scale;
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static int SkyRainbow(int var2, float st, float bright) {
        double d;
        double v1 = Math.ceil(System.currentTimeMillis() + (long)(var2 * 109)) / 5.0;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(v1 / 360.0)) : (float)((v1 %= 360.0) / 360.0), st, bright).getRGB();
    }

    public static Color skyRainbow(int var2, float st, float bright) {
        double d;
        double v1 = Math.ceil(System.currentTimeMillis() + (long)(var2 * 109)) / 5.0;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(v1 / 360.0)) : (float)((v1 %= 360.0) / 360.0), st, bright);
    }

    public static int getRainbowOpaque(int seconds, float saturation, float brightness, int index) {
        float hue = (float)((System.currentTimeMillis() + (long)index) % (long)(seconds * 1000)) / (float)(seconds * 1000);
        int color = Color.HSBtoRGB(hue, saturation, brightness);
        return color;
    }

    public static int getNormalRainbow(int delay, float sat, float brg) {
        double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 20.0);
        return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), sat, brg).getRGB();
    }

    public static void startSmooth() {
        GLUtils.glEnable(2848);
        GLUtils.glEnable(2881);
        GLUtils.glEnable(2832);
        GLUtils.glEnable(3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glHint((int)3153, (int)4354);
    }

    public static void endSmooth() {
        GLUtils.glDisable(2848);
        GLUtils.glDisable(2881);
        GLUtils.glEnable(2832);
    }

    public static void drawExhiRect(float x, float y, float x2, float y2) {
        RenderUtilsTargetHud.drawRect(x - 3.5f, y - 3.5f, x2 + 3.5f, y2 + 3.5f, Color.black.getRGB());
        RenderUtilsTargetHud.drawRect(x - 3.0f, y - 3.0f, x2 + 3.0f, y2 + 3.0f, new Color(50, 50, 50).getRGB());
        RenderUtilsTargetHud.drawRect(x - 2.5f, y - 2.5f, x2 + 2.5f, y2 + 2.5f, new Color(26, 26, 26).getRGB());
        RenderUtilsTargetHud.drawRect(x - 0.5f, y - 0.5f, x2 + 0.5f, y2 + 0.5f, new Color(50, 50, 50).getRGB());
        RenderUtilsTargetHud.drawRect(x, y, x2, y2, new Color(18, 18, 18).getRGB());
    }

    public static void drawOutlinedRect(float x, float y, float width, float height, float lineSize, int lineColor) {
        RenderUtils.drawRect(x, y, width, y + lineSize, lineColor);
        RenderUtils.drawRect(x, height - lineSize, width, height, lineColor);
        RenderUtils.drawRect(x, y + lineSize, x + lineSize, height - lineSize, lineColor);
        RenderUtils.drawRect(width - lineSize, y + lineSize, width, height - lineSize, lineColor);
    }

    public static void drawCircle(float x, float y, float radius, Color color) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        RenderUtilsTargetHud.glColor(Color.WHITE);
        GLUtils.glEnable(2848);
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)3);
        for (float i = 180.0f; i >= -180.0f; i -= 4.0f) {
            GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            GL11.glVertex2f((float)((float)((double)x + Math.cos((double)i * Math.PI / 180.0) * (double)(radius * 1.001f))), (float)((float)((double)y + Math.sin((double)i * Math.PI / 180.0) * (double)(radius * 1.001f))));
        }
        GL11.glEnd();
        GLUtils.glDisable(2848);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawExhiRect(float x, float y, float x2, float y2, float alpha) {
        RenderUtilsTargetHud.drawRect(x - 3.5f, y - 3.5f, x2 + 3.5f, y2 + 3.5f, new Color(0.0f, 0.0f, 0.0f, alpha).getRGB());
        RenderUtilsTargetHud.drawRect(x - 3.0f, y - 3.0f, x2 + 3.0f, y2 + 3.0f, new Color(0.19607843f, 0.19607843f, 0.19607843f, alpha).getRGB());
        RenderUtilsTargetHud.drawRect(x - 2.5f, y - 2.5f, x2 + 2.5f, y2 + 2.5f, new Color(0.101960786f, 0.101960786f, 0.101960786f, alpha).getRGB());
        RenderUtilsTargetHud.drawRect(x - 0.5f, y - 0.5f, x2 + 0.5f, y2 + 0.5f, new Color(0.19607843f, 0.19607843f, 0.19607843f, alpha).getRGB());
        RenderUtilsTargetHud.drawRect(x, y, x2, y2, new Color(0.07058824f, 0.07058824f, 0.07058824f, alpha).getRGB());
    }

    public static void drawRoundedRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float radius, int color) {
        RenderUtilsTargetHud.drawRoundedRect(paramXStart, paramYStart, paramXEnd, paramYEnd, radius, color, true);
    }

    public static void drawRDRect(float left, float top, float width, float height, int color) {
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f4 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f5 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f6 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)f4, (float)f5, (float)f6, (float)f3);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldrenderer.func_181662_b((double)left, (double)(top + height), 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)(left + width), (double)(top + height), 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)(left + width), (double)top, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)left, (double)top, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void enableRender2D() {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)2884);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glLineWidth((float)1.0f);
    }

    public static void color(int color, float alpha) {
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)r, (float)g, (float)b, (float)alpha);
    }

    public static void color(int color) {
        RenderUtilsTargetHud.color(color, (float)(color >> 24 & 0xFF) / 255.0f);
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float edgeRadius, int color, float borderWidth, int borderColor) {
        double angleRadians;
        int i;
        if (color == 0xFFFFFF) {
            color = -65794;
        }
        if (borderColor == 0xFFFFFF) {
            borderColor = -65794;
        }
        if (edgeRadius < 0.0f) {
            edgeRadius = 0.0f;
        }
        if (edgeRadius > width / 2.0f) {
            edgeRadius = width / 2.0f;
        }
        if (edgeRadius > height / 2.0f) {
            edgeRadius = height / 2.0f;
        }
        RenderUtilsTargetHud.drawRDRect(x + edgeRadius, y + edgeRadius, width - edgeRadius * 2.0f, height - edgeRadius * 2.0f, color);
        RenderUtilsTargetHud.drawRDRect(x + edgeRadius, y, width - edgeRadius * 2.0f, edgeRadius, color);
        RenderUtilsTargetHud.drawRDRect(x + edgeRadius, y + height - edgeRadius, width - edgeRadius * 2.0f, edgeRadius, color);
        RenderUtilsTargetHud.drawRDRect(x, y + edgeRadius, edgeRadius, height - edgeRadius * 2.0f, color);
        RenderUtilsTargetHud.drawRDRect(x + width - edgeRadius, y + edgeRadius, edgeRadius, height - edgeRadius * 2.0f, color);
        RenderUtilsTargetHud.enableRender2D();
        RenderUtilsTargetHud.color(color);
        GL11.glBegin((int)6);
        float centerX = x + edgeRadius;
        float centerY = y + edgeRadius;
        GL11.glVertex2d((double)centerX, (double)centerY);
        int vertices = (int)Math.min(Math.max(edgeRadius, 10.0f), 90.0f);
        for (i = 0; i < vertices + 1; ++i) {
            angleRadians = Math.PI * 2 * (double)(i + 180) / (double)(vertices * 4);
            GL11.glVertex2d((double)((double)centerX + Math.sin(angleRadians) * (double)edgeRadius), (double)((double)centerY + Math.cos(angleRadians) * (double)edgeRadius));
        }
        GL11.glEnd();
        GL11.glBegin((int)6);
        centerX = x + width - edgeRadius;
        centerY = y + edgeRadius;
        GL11.glVertex2d((double)centerX, (double)centerY);
        vertices = (int)Math.min(Math.max(edgeRadius, 10.0f), 90.0f);
        for (i = 0; i < vertices + 1; ++i) {
            angleRadians = Math.PI * 2 * (double)(i + 90) / (double)(vertices * 4);
            GL11.glVertex2d((double)((double)centerX + Math.sin(angleRadians) * (double)edgeRadius), (double)((double)centerY + Math.cos(angleRadians) * (double)edgeRadius));
        }
        GL11.glEnd();
        GL11.glBegin((int)6);
        centerX = x + edgeRadius;
        centerY = y + height - edgeRadius;
        GL11.glVertex2d((double)centerX, (double)centerY);
        vertices = (int)Math.min(Math.max(edgeRadius, 10.0f), 90.0f);
        for (i = 0; i < vertices + 1; ++i) {
            angleRadians = Math.PI * 2 * (double)(i + 270) / (double)(vertices * 4);
            GL11.glVertex2d((double)((double)centerX + Math.sin(angleRadians) * (double)edgeRadius), (double)((double)centerY + Math.cos(angleRadians) * (double)edgeRadius));
        }
        GL11.glEnd();
        GL11.glBegin((int)6);
        centerX = x + width - edgeRadius;
        centerY = y + height - edgeRadius;
        GL11.glVertex2d((double)centerX, (double)centerY);
        vertices = (int)Math.min(Math.max(edgeRadius, 10.0f), 90.0f);
        for (i = 0; i < vertices + 1; ++i) {
            angleRadians = Math.PI * 2 * (double)i / (double)(vertices * 4);
            GL11.glVertex2d((double)((double)centerX + Math.sin(angleRadians) * (double)edgeRadius), (double)((double)centerY + Math.cos(angleRadians) * (double)edgeRadius));
        }
        GL11.glEnd();
        RenderUtilsTargetHud.color(borderColor);
        GL11.glLineWidth((float)borderWidth);
        GL11.glBegin((int)3);
        centerX = x + edgeRadius;
        centerY = y + edgeRadius;
        vertices = i = (int)Math.min(Math.max(edgeRadius, 10.0f), 90.0f);
        while (i >= 0) {
            angleRadians = Math.PI * 2 * (double)(i + 180) / (double)(vertices * 4);
            GL11.glVertex2d((double)((double)centerX + Math.sin(angleRadians) * (double)edgeRadius), (double)((double)centerY + Math.cos(angleRadians) * (double)edgeRadius));
            --i;
        }
        GL11.glVertex2d((double)(x + edgeRadius), (double)y);
        GL11.glVertex2d((double)(x + width - edgeRadius), (double)y);
        centerX = x + width - edgeRadius;
        centerY = y + edgeRadius;
        for (i = vertices; i >= 0; --i) {
            angleRadians = Math.PI * 2 * (double)(i + 90) / (double)(vertices * 4);
            GL11.glVertex2d((double)((double)centerX + Math.sin(angleRadians) * (double)edgeRadius), (double)((double)centerY + Math.cos(angleRadians) * (double)edgeRadius));
        }
        GL11.glVertex2d((double)(x + width), (double)(y + edgeRadius));
        GL11.glVertex2d((double)(x + width), (double)(y + height - edgeRadius));
        centerX = x + width - edgeRadius;
        centerY = y + height - edgeRadius;
        for (i = vertices; i >= 0; --i) {
            angleRadians = Math.PI * 2 * (double)i / (double)(vertices * 4);
            GL11.glVertex2d((double)((double)centerX + Math.sin(angleRadians) * (double)edgeRadius), (double)((double)centerY + Math.cos(angleRadians) * (double)edgeRadius));
        }
        GL11.glVertex2d((double)(x + width - edgeRadius), (double)(y + height));
        GL11.glVertex2d((double)(x + edgeRadius), (double)(y + height));
        centerX = x + edgeRadius;
        centerY = y + height - edgeRadius;
        for (i = vertices; i >= 0; --i) {
            angleRadians = Math.PI * 2 * (double)(i + 270) / (double)(vertices * 4);
            GL11.glVertex2d((double)((double)centerX + Math.sin(angleRadians) * (double)edgeRadius), (double)((double)centerY + Math.cos(angleRadians) * (double)edgeRadius));
        }
        GL11.glVertex2d((double)x, (double)(y + height - edgeRadius));
        GL11.glVertex2d((double)x, (double)(y + edgeRadius));
        GL11.glEnd();
        RenderUtilsTargetHud.disableRender2D();
    }

    public static void disableRender2D() {
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2884);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179103_j((int)7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179098_w();
    }

    public static void resetColor() {
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void originalRoundedRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float radius, int color) {
        double i;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        float z = 0.0f;
        if (paramXStart > paramXEnd) {
            z = paramXStart;
            paramXStart = paramXEnd;
            paramXEnd = z;
        }
        if (paramYStart > paramYEnd) {
            z = paramYStart;
            paramYStart = paramYEnd;
            paramYEnd = z;
        }
        double x1 = paramXStart + radius;
        double y1 = paramYStart + radius;
        double x2 = paramXEnd - radius;
        double y2 = paramYEnd - radius;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
        worldrenderer.func_181668_a(9, DefaultVertexFormats.field_181705_e);
        double degree = Math.PI / 180;
        for (i = 0.0; i <= 90.0; i += 1.0) {
            worldrenderer.func_181662_b(x2 + Math.sin(i * degree) * (double)radius, y2 + Math.cos(i * degree) * (double)radius, 0.0).func_181675_d();
        }
        for (i = 90.0; i <= 180.0; i += 1.0) {
            worldrenderer.func_181662_b(x2 + Math.sin(i * degree) * (double)radius, y1 + Math.cos(i * degree) * (double)radius, 0.0).func_181675_d();
        }
        for (i = 180.0; i <= 270.0; i += 1.0) {
            worldrenderer.func_181662_b(x1 + Math.sin(i * degree) * (double)radius, y1 + Math.cos(i * degree) * (double)radius, 0.0).func_181675_d();
        }
        for (i = 270.0; i <= 360.0; i += 1.0) {
            worldrenderer.func_181662_b(x1 + Math.sin(i * degree) * (double)radius, y2 + Math.cos(i * degree) * (double)radius, 0.0).func_181675_d();
        }
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void newDrawRect(float left, float top, float right, float bottom, int color) {
        if (left < right) {
            float i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            float j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)f, (float)f1, (float)f2, (float)f3);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldrenderer.func_181662_b((double)left, (double)bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)right, (double)bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)right, (double)top, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)left, (double)top, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void newDrawRect(double left, double top, double right, double bottom, int color) {
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
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)f, (float)f1, (float)f2, (float)f3);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldrenderer.func_181662_b(left, bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b(right, bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b(right, top, 0.0).func_181675_d();
        worldrenderer.func_181662_b(left, top, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawRoundedRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float radius, int color, boolean popPush) {
        double i;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        float z = 0.0f;
        if (paramXStart > paramXEnd) {
            z = paramXStart;
            paramXStart = paramXEnd;
            paramXEnd = z;
        }
        if (paramYStart > paramYEnd) {
            z = paramYStart;
            paramYStart = paramYEnd;
            paramYEnd = z;
        }
        double x1 = paramXStart + radius;
        double y1 = paramYStart + radius;
        double x2 = paramXEnd - radius;
        double y2 = paramYEnd - radius;
        if (popPush) {
            GL11.glPushMatrix();
        }
        GLUtils.glEnable(3042);
        GLUtils.glDisable(3553);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtils.glEnable(2848);
        GL11.glLineWidth((float)1.0f);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)9);
        double degree = Math.PI / 180;
        for (i = 0.0; i <= 90.0; i += 1.0) {
            GL11.glVertex2d((double)(x2 + Math.sin(i * degree) * (double)radius), (double)(y2 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 90.0; i <= 180.0; i += 1.0) {
            GL11.glVertex2d((double)(x2 + Math.sin(i * degree) * (double)radius), (double)(y1 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 180.0; i <= 270.0; i += 1.0) {
            GL11.glVertex2d((double)(x1 + Math.sin(i * degree) * (double)radius), (double)(y1 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 270.0; i <= 360.0; i += 1.0) {
            GL11.glVertex2d((double)(x1 + Math.sin(i * degree) * (double)radius), (double)(y2 + Math.cos(i * degree) * (double)radius));
        }
        GL11.glEnd();
        GLUtils.glEnable(3553);
        GLUtils.glDisable(3042);
        GLUtils.glDisable(2848);
        if (popPush) {
            GL11.glPopMatrix();
        }
    }

    public static void drawGradientRound(float x, float y, float width, float height, float radius, Color bottomLeft, Color topLeft, Color bottomRight, Color topRight) {
        RenderUtilsTargetHud.resetColor();
        GlStateManager.func_179147_l();
        GlStateManager.func_179112_b((int)770, (int)771);
        GlStateManager.func_179084_k();
    }

    public static void customRounded(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float rTL, float rTR, float rBR, float rBL, int color) {
        double i;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        float z = 0.0f;
        if (paramXStart > paramXEnd) {
            z = paramXStart;
            paramXStart = paramXEnd;
            paramXEnd = z;
        }
        if (paramYStart > paramYEnd) {
            z = paramYStart;
            paramYStart = paramYEnd;
            paramYEnd = z;
        }
        double xTL = paramXStart + rTL;
        double yTL = paramYStart + rTL;
        double xTR = paramXEnd - rTR;
        double yTR = paramYStart + rTR;
        double xBR = paramXEnd - rBR;
        double yBR = paramYEnd - rBR;
        double xBL = paramXStart + rBL;
        double yBL = paramYEnd - rBL;
        GL11.glPushMatrix();
        GLUtils.glEnable(3042);
        GLUtils.glDisable(3553);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtils.glEnable(2848);
        GL11.glLineWidth((float)1.0f);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)9);
        double degree = Math.PI / 180;
        if (rBR <= 0.0f) {
            GL11.glVertex2d((double)xBR, (double)yBR);
        } else {
            for (i = 0.0; i <= 90.0; i += 1.0) {
                GL11.glVertex2d((double)(xBR + Math.sin(i * degree) * (double)rBR), (double)(yBR + Math.cos(i * degree) * (double)rBR));
            }
        }
        if (rTR <= 0.0f) {
            GL11.glVertex2d((double)xTR, (double)yTR);
        } else {
            for (i = 90.0; i <= 180.0; i += 1.0) {
                GL11.glVertex2d((double)(xTR + Math.sin(i * degree) * (double)rTR), (double)(yTR + Math.cos(i * degree) * (double)rTR));
            }
        }
        if (rTL <= 0.0f) {
            GL11.glVertex2d((double)xTL, (double)yTL);
        } else {
            for (i = 180.0; i <= 270.0; i += 1.0) {
                GL11.glVertex2d((double)(xTL + Math.sin(i * degree) * (double)rTL), (double)(yTL + Math.cos(i * degree) * (double)rTL));
            }
        }
        if (rBL <= 0.0f) {
            GL11.glVertex2d((double)xBL, (double)yBL);
        } else {
            for (i = 270.0; i <= 360.0; i += 1.0) {
                GL11.glVertex2d((double)(xBL + Math.sin(i * degree) * (double)rBL), (double)(yBL + Math.cos(i * degree) * (double)rBL));
            }
        }
        GL11.glEnd();
        GLUtils.glEnable(3553);
        GLUtils.glDisable(3042);
        GLUtils.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void fastRoundedRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float radius) {
        double i;
        float z = 0.0f;
        if (paramXStart > paramXEnd) {
            z = paramXStart;
            paramXStart = paramXEnd;
            paramXEnd = z;
        }
        if (paramYStart > paramYEnd) {
            z = paramYStart;
            paramYStart = paramYEnd;
            paramYEnd = z;
        }
        double x1 = paramXStart + radius;
        double y1 = paramYStart + radius;
        double x2 = paramXEnd - radius;
        double y2 = paramYEnd - radius;
        GLUtils.glEnable(2848);
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)9);
        double degree = Math.PI / 180;
        for (i = 0.0; i <= 90.0; i += 1.0) {
            GL11.glVertex2d((double)(x2 + Math.sin(i * degree) * (double)radius), (double)(y2 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 90.0; i <= 180.0; i += 1.0) {
            GL11.glVertex2d((double)(x2 + Math.sin(i * degree) * (double)radius), (double)(y1 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 180.0; i <= 270.0; i += 1.0) {
            GL11.glVertex2d((double)(x1 + Math.sin(i * degree) * (double)radius), (double)(y1 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 270.0; i <= 360.0; i += 1.0) {
            GL11.glVertex2d((double)(x1 + Math.sin(i * degree) * (double)radius), (double)(y2 + Math.cos(i * degree) * (double)radius));
        }
        GL11.glEnd();
        GLUtils.glDisable(2848);
    }

    public static void drawTriAngle(float cx, float cy, float r, float n, Color color, boolean polygon) {
        cx = (float)((double)cx * 2.0);
        cy = (float)((double)cy * 2.0);
        double b = 6.2831852 / (double)n;
        double p = Math.cos(b);
        double s = Math.sin(b);
        r = (float)((double)r * 2.0);
        double x = r;
        double y = 0.0;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        GL11.glLineWidth((float)1.0f);
        RenderUtilsTargetHud.enableGlCap(2848);
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179117_G();
        RenderUtilsTargetHud.glColor(color);
        GlStateManager.func_179152_a((float)0.5f, (float)0.5f, (float)0.5f);
        worldrenderer.func_181668_a(polygon ? 9 : 2, DefaultVertexFormats.field_181705_e);
        int ii = 0;
        while ((float)ii < n) {
            worldrenderer.func_181662_b(x + (double)cx, y + (double)cy, 0.0).func_181675_d();
            double t = x;
            x = p * x - s * y;
            y = s * t + p * y;
            ++ii;
        }
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179152_a((float)2.0f, (float)2.0f, (float)2.0f);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(col1 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(col2 & 0xFF) / 255.0f;
        GLUtils.glEnable(3042);
        GLUtils.glDisable(3553);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtils.glEnable(2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glColor4f((float)f6, (float)f7, (float)f8, (float)f5);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GLUtils.glEnable(3553);
        GLUtils.glDisable(3042);
        GLUtils.glDisable(2848);
        GL11.glShadeModel((int)7424);
    }

    public static void drawBlockBox(BlockPos blockPos, Color color, boolean outline) {
        RenderManager renderManager = mc.func_175598_ae();
        Timer timer = RenderUtilsTargetHud.mc.field_71428_T;
        double x = (double)blockPos.func_177958_n() - renderManager.field_78725_b;
        double y = (double)blockPos.func_177956_o() - renderManager.field_78726_c;
        double z = (double)blockPos.func_177952_p() - renderManager.field_78723_d;
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        Block block = BlockUtils.getBlock(blockPos);
        if (block != null) {
            EntityPlayerSP player = RenderUtilsTargetHud.mc.field_71439_g;
            double posX = player.field_70142_S + (player.field_70165_t - player.field_70142_S) * (double)timer.field_74281_c;
            double posY = player.field_70137_T + (player.field_70163_u - player.field_70137_T) * (double)timer.field_74281_c;
            double posZ = player.field_70136_U + (player.field_70161_v - player.field_70136_U) * (double)timer.field_74281_c;
            axisAlignedBB = block.func_180646_a((World)RenderUtilsTargetHud.mc.field_71441_e, blockPos).func_72314_b((double)0.002f, (double)0.002f, (double)0.002f).func_72317_d(-posX, -posY, -posZ);
        }
        GL11.glBlendFunc((int)770, (int)771);
        RenderUtilsTargetHud.enableGlCap(3042);
        RenderUtilsTargetHud.disableGlCap(3553, 2929);
        GL11.glDepthMask((boolean)false);
        RenderUtilsTargetHud.glColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() != 255 ? color.getAlpha() : (outline ? 26 : 35));
        RenderUtilsTargetHud.drawFilledBox(axisAlignedBB);
        if (outline) {
            GL11.glLineWidth((float)1.0f);
            RenderUtilsTargetHud.enableGlCap(2848);
            RenderUtilsTargetHud.glColor(color);
            RenderUtilsTargetHud.drawSelectionBoundingBox(axisAlignedBB);
        }
        GlStateManager.func_179117_G();
        GL11.glDepthMask((boolean)true);
        RenderUtilsTargetHud.resetCaps();
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        worldrenderer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
        worldrenderer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
        worldrenderer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
        worldrenderer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
        worldrenderer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
        worldrenderer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
        worldrenderer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
        worldrenderer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
        worldrenderer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
        worldrenderer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
        worldrenderer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
        worldrenderer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
        worldrenderer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
        worldrenderer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
        worldrenderer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
        worldrenderer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void drawEntityBox(Entity entity, Color color, boolean outline) {
        RenderManager renderManager = mc.func_175598_ae();
        Timer timer = RenderUtilsTargetHud.mc.field_71428_T;
        GL11.glBlendFunc((int)770, (int)771);
        RenderUtilsTargetHud.enableGlCap(3042);
        RenderUtilsTargetHud.disableGlCap(3553, 2929);
        GL11.glDepthMask((boolean)false);
        double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)timer.field_74281_c - renderManager.field_78725_b;
        double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)timer.field_74281_c - renderManager.field_78726_c;
        double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)timer.field_74281_c - renderManager.field_78723_d;
        AxisAlignedBB entityBox = entity.func_174813_aQ();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(entityBox.field_72340_a - entity.field_70165_t + x - 0.05, entityBox.field_72338_b - entity.field_70163_u + y, entityBox.field_72339_c - entity.field_70161_v + z - 0.05, entityBox.field_72336_d - entity.field_70165_t + x + 0.05, entityBox.field_72337_e - entity.field_70163_u + y + 0.15, entityBox.field_72334_f - entity.field_70161_v + z + 0.05);
        if (outline) {
            GL11.glLineWidth((float)1.0f);
            RenderUtilsTargetHud.enableGlCap(2848);
            RenderUtilsTargetHud.glColor(color.getRed(), color.getGreen(), color.getBlue(), 95);
            RenderUtilsTargetHud.drawSelectionBoundingBox(axisAlignedBB);
        }
        RenderUtilsTargetHud.glColor(color.getRed(), color.getGreen(), color.getBlue(), outline ? 26 : 35);
        RenderUtilsTargetHud.drawFilledBox(axisAlignedBB);
        GlStateManager.func_179117_G();
        GL11.glDepthMask((boolean)true);
        RenderUtilsTargetHud.resetCaps();
    }

    public static void drawAxisAlignedBB(AxisAlignedBB axisAlignedBB, Color color) {
        GL11.glBlendFunc((int)770, (int)771);
        GLUtils.glEnable(3042);
        GL11.glLineWidth((float)2.0f);
        GLUtils.glDisable(3553);
        GLUtils.glDisable(2929);
        GL11.glDepthMask((boolean)false);
        RenderUtilsTargetHud.glColor(color);
        RenderUtilsTargetHud.drawFilledBox(axisAlignedBB);
        GlStateManager.func_179117_G();
        GLUtils.glEnable(3553);
        GLUtils.glEnable(2929);
        GL11.glDepthMask((boolean)true);
        GLUtils.glDisable(3042);
    }

    public static void drawPlatform(double y, Color color, double size) {
        RenderManager renderManager = mc.func_175598_ae();
        double renderY = y - renderManager.field_78726_c;
        RenderUtilsTargetHud.drawAxisAlignedBB(new AxisAlignedBB(size, renderY + 0.02, size, -size, renderY, -size), color);
    }

    public static void drawFilledBox(AxisAlignedBB axisAlignedBB) {
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
    }

    public static ScaledResolution getResolution() {
        return new ScaledResolution(mc);
    }

    public static void Nametags1(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            int i = (int)left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            int j = (int)top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)f, (float)f1, (float)f2, (float)f3);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldrenderer.func_181662_b(left, bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b(right, bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b(right, top, 0.0).func_181675_d();
        worldrenderer.func_181662_b(left, top, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawEntityOnScreen(double posX, double posY, float scale, EntityLivingBase entity) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179142_g();
        GlStateManager.func_179137_b((double)posX, (double)posY, (double)50.0);
        GlStateManager.func_179152_a((float)(-scale), (float)scale, (float)scale);
        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.func_74519_b();
        GlStateManager.func_179114_b((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179137_b((double)0.0, (double)0.0, (double)0.0);
        RenderManager rendermanager = mc.func_175598_ae();
        rendermanager.func_178631_a(180.0f);
        rendermanager.func_178633_a(false);
        rendermanager.func_147940_a((Entity)entity, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        rendermanager.func_178633_a(true);
        GlStateManager.func_179121_F();
        RenderHelper.func_74518_a();
        GlStateManager.func_179101_C();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77476_b);
        GlStateManager.func_179090_x();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77478_a);
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, EntityLivingBase entity) {
        RenderUtilsTargetHud.drawEntityOnScreen((double)posX, (double)posY, (float)scale, entity);
    }

    public static void quickDrawRect(float x, float y, float x2, float y2) {
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
    }

    public static void drawRect(float x, float y, float x2, float y2, int color) {
        GL11.glPushMatrix();
        GLUtils.glEnable(3042);
        GLUtils.glDisable(3553);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtils.glEnable(2848);
        RenderUtilsTargetHud.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GLUtils.glEnable(3553);
        GLUtils.glDisable(3042);
        GLUtils.glDisable(2848);
        GL11.glPopMatrix();
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
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)f, (float)f1, (float)f2, (float)f3);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldrenderer.func_181662_b(left, bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b(right, bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b(right, top, 0.0).func_181675_d();
        worldrenderer.func_181662_b(left, top, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void quickDrawRect(float x, float y, float x2, float y2, int color) {
        RenderUtilsTargetHud.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
    }

    public static void drawRect(float x, float y, float x2, float y2, Color color) {
        RenderUtilsTargetHud.drawRect(x, y, x2, y2, color.getRGB());
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float width, int color1, int color2) {
        RenderUtilsTargetHud.drawRect(x, y, x2, y2, color2);
        RenderUtilsTargetHud.drawBorder(x, y, x2, y2, width, color1);
    }

    public static void drawBorder(float x, float y, float x2, float y2, float width, int color1) {
        GLUtils.glEnable(3042);
        GLUtils.glDisable(3553);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtils.glEnable(2848);
        RenderUtilsTargetHud.glColor(color1);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GLUtils.glEnable(3553);
        GLUtils.glDisable(3042);
        GLUtils.glDisable(2848);
    }

    public static void drawRectBasedBorder(float x, float y, float x2, float y2, float width, int color1) {
        RenderUtilsTargetHud.drawRect(x - width / 2.0f, y - width / 2.0f, x2 + width / 2.0f, y + width / 2.0f, color1);
        RenderUtilsTargetHud.drawRect(x - width / 2.0f, y + width / 2.0f, x + width / 2.0f, y2 + width / 2.0f, color1);
        RenderUtilsTargetHud.drawRect(x2 - width / 2.0f, y + width / 2.0f, x2 + width / 2.0f, y2 + width / 2.0f, color1);
        RenderUtilsTargetHud.drawRect(x + width / 2.0f, y2 - width / 2.0f, x2 - width / 2.0f, y2 + width / 2.0f, color1);
    }

    public static void quickDrawBorderedRect(float x, float y, float x2, float y2, float width, int color1, int color2) {
        RenderUtilsTargetHud.quickDrawRect(x, y, x2, y2, color2);
        RenderUtilsTargetHud.glColor(color1);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
    }

    public static void start2D() {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
    }

    public static void stop2D() {
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawCircle(float x, float y, float radius, float lineWidth, int start, int end, Color color) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        RenderUtilsTargetHud.glColor(color);
        GLUtils.glEnable(2848);
        GL11.glLineWidth((float)lineWidth);
        GL11.glBegin((int)3);
        for (float i = (float)end; i >= (float)start; i -= 4.0f) {
            GL11.glVertex2f((float)((float)((double)x + Math.cos((double)i * Math.PI / 180.0) * (double)(radius * 1.001f))), (float)((float)((double)y + Math.sin((double)i * Math.PI / 180.0) * (double)(radius * 1.001f))));
        }
        GL11.glEnd();
        GLUtils.glDisable(2848);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawOutlinedStringCock(FontRenderer fr, String s, float x, float y, int color, int outlineColor) {
        fr.func_78276_b(ColorUtils.stripColor(s), (int)(x - 1.0f), (int)y, outlineColor);
        fr.func_78276_b(ColorUtils.stripColor(s), (int)x, (int)(y - 1.0f), outlineColor);
        fr.func_78276_b(ColorUtils.stripColor(s), (int)(x + 1.0f), (int)y, outlineColor);
        fr.func_78276_b(ColorUtils.stripColor(s), (int)x, (int)(y + 1.0f), outlineColor);
        fr.func_78276_b(s, (int)x, (int)y, color);
    }

    private static void drawEnchantTag(String text, int x, float y) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179097_i();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        RenderUtilsTargetHud.drawOutlinedStringCock(Minecraft.func_71410_x().field_71466_p, text, x, y, -1, new Color(0, 0, 0, 220).darker().getRGB());
        GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179126_j();
        GlStateManager.func_179121_F();
    }

    public static String stripColor(String input) {
        return COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public static void renderEnchantText(ItemStack stack, int x, float y) {
        int unbreakingLevel;
        RenderHelper.func_74518_a();
        float enchantmentY = y + 24.0f;
        if (stack.func_77973_b() instanceof ItemArmor) {
            int protectionLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_180310_c.field_77352_x, (ItemStack)stack);
            int unbreakingLevel2 = EnchantmentHelper.func_77506_a((int)Enchantment.field_77347_r.field_77352_x, (ItemStack)stack);
            int thornLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_92091_k.field_77352_x, (ItemStack)stack);
            if (protectionLevel > 0) {
                RenderUtilsTargetHud.drawEnchantTag("P" + ColorUtilsTargetHud.getColor(protectionLevel) + protectionLevel, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
            if (unbreakingLevel2 > 0) {
                RenderUtilsTargetHud.drawEnchantTag("U" + ColorUtilsTargetHud.getColor(unbreakingLevel2) + unbreakingLevel2, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
            if (thornLevel > 0) {
                RenderUtilsTargetHud.drawEnchantTag("T" + ColorUtilsTargetHud.getColor(thornLevel) + thornLevel, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
        }
        if (stack.func_77973_b() instanceof ItemBow) {
            int powerLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77345_t.field_77352_x, (ItemStack)stack);
            int punchLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77344_u.field_77352_x, (ItemStack)stack);
            int flameLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77343_v.field_77352_x, (ItemStack)stack);
            unbreakingLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77347_r.field_77352_x, (ItemStack)stack);
            if (powerLevel > 0) {
                RenderUtilsTargetHud.drawEnchantTag("Pow" + ColorUtilsTargetHud.getColor(powerLevel) + powerLevel, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
            if (punchLevel > 0) {
                RenderUtilsTargetHud.drawEnchantTag("Pun" + ColorUtilsTargetHud.getColor(punchLevel) + punchLevel, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
            if (flameLevel > 0) {
                RenderUtilsTargetHud.drawEnchantTag("F" + ColorUtilsTargetHud.getColor(flameLevel) + flameLevel, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
            if (unbreakingLevel > 0) {
                RenderUtilsTargetHud.drawEnchantTag("U" + ColorUtilsTargetHud.getColor(unbreakingLevel) + unbreakingLevel, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
        }
        if (stack.func_77973_b() instanceof ItemSword) {
            int sharpnessLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_180314_l.field_77352_x, (ItemStack)stack);
            int knockbackLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_180313_o.field_77352_x, (ItemStack)stack);
            int fireAspectLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77334_n.field_77352_x, (ItemStack)stack);
            unbreakingLevel = EnchantmentHelper.func_77506_a((int)Enchantment.field_77347_r.field_77352_x, (ItemStack)stack);
            if (sharpnessLevel > 0) {
                RenderUtilsTargetHud.drawEnchantTag("S" + ColorUtilsTargetHud.getColor(sharpnessLevel) + sharpnessLevel, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
            if (knockbackLevel > 0) {
                RenderUtilsTargetHud.drawEnchantTag("K" + ColorUtilsTargetHud.getColor(knockbackLevel) + knockbackLevel, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
            if (fireAspectLevel > 0) {
                RenderUtilsTargetHud.drawEnchantTag("F" + ColorUtilsTargetHud.getColor(fireAspectLevel) + fireAspectLevel, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
            if (unbreakingLevel > 0) {
                RenderUtilsTargetHud.drawEnchantTag("U" + ColorUtilsTargetHud.getColor(unbreakingLevel) + unbreakingLevel, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
        }
        if (stack.func_77953_t() == EnumRarity.EPIC) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179097_i();
            GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
            RenderUtilsTargetHud.drawOutlinedStringCock(Minecraft.func_71410_x().field_71466_p, "God", x * 2, enchantmentY, new Color(255, 255, 0).getRGB(), new Color(100, 100, 0, 200).getRGB());
            GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.func_179126_j();
            GlStateManager.func_179121_F();
        }
    }

    public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        RenderUtilsTargetHud.rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtilsTargetHud.rectangle(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtilsTargetHud.rectangle(x, y, x + width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtilsTargetHud.rectangle(x1 - width, y, x1, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtilsTargetHud.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0f / tileWidth;
        float f1 = 1.0f / tileHeight;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        worldrenderer.func_181662_b((double)x, (double)(y + height), 0.0).func_181673_a((double)(u * f), (double)((v + (float)vHeight) * f1)).func_181675_d();
        worldrenderer.func_181662_b((double)(x + width), (double)(y + height), 0.0).func_181673_a((double)((u + (float)uWidth) * f), (double)((v + (float)vHeight) * f1)).func_181675_d();
        worldrenderer.func_181662_b((double)(x + width), (double)y, 0.0).func_181673_a((double)((u + (float)uWidth) * f), (double)(v * f1)).func_181675_d();
        worldrenderer.func_181662_b((double)x, (double)y, 0.0).func_181673_a((double)(u * f), (double)(v * f1)).func_181675_d();
        tessellator.func_78381_a();
    }

    public static double getAnimationStateSmooth(double target, double current, double speed) {
        boolean larger = target > current;
        boolean bl = larger;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        if (target == current) {
            return target;
        }
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = Math.max(dif * speed, 1.0);
        if (factor < 0.1) {
            factor = 0.1;
        }
        current = larger ? (current + factor > target ? target : (current += factor)) : (current - factor < target ? target : (current -= factor));
        return current;
    }

    public static void drawCircleFull(float x, float y, float radius, float Bord, Color color) {
        RenderUtilsTargetHud.drawCircle(x, y, radius + 0.15f, color);
        RenderUtilsTargetHud.drawCircleD(x, y, radius, color.getRGB());
    }

    public static void prepareScissorBox(float x, float y, float x2, float y2) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.func_78325_e();
        GL11.glScissor((int)((int)(x * (float)factor)), (int)((int)(((float)scale.func_78328_b() - y2) * (float)factor)), (int)((int)((x2 - x) * (float)factor)), (int)((int)((y2 - y) * (float)factor)));
    }

    public static int Astolfo(int var2) {
        double d;
        double v1 = Math.ceil(System.currentTimeMillis() + (long)(var2 * 109)) / 5.0;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(v1 / 360.0)) : (float)((v1 %= 360.0) / 360.0), 0.5f, 1.0f).getRGB();
    }

    public static void drawCircleD(float x, float y, float radius, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        boolean blend = GL11.glIsEnabled((int)3042);
        boolean line = GL11.glIsEnabled((int)2848);
        boolean texture = GL11.glIsEnabled((int)3553);
        if (!blend) {
            GL11.glEnable((int)3042);
        }
        if (!line) {
            GL11.glEnable((int)2848);
        }
        if (texture) {
            GL11.glDisable((int)3553);
        }
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double)((double)x + Math.sin((double)i * 3.141526 / 180.0) * (double)radius), (double)((double)y + Math.cos((double)i * 3.141526 / 180.0) * (double)radius));
        }
        GL11.glEnd();
        if (texture) {
            GL11.glEnable((int)3553);
        }
        if (!line) {
            GL11.glDisable((int)2848);
        }
        if (!blend) {
            GL11.glDisable((int)3042);
        }
    }

    public static void drawRectBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        RenderUtilsTargetHud.rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        RenderUtilsTargetHud.rectangle(x + width, y, x1 - width, y + width, borderColor);
        RenderUtilsTargetHud.rectangle(x, y, x + width, y1, borderColor);
        RenderUtilsTargetHud.rectangle(x1 - width, y, x1, y1, borderColor);
        RenderUtilsTargetHud.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
    }

    public static void rectangle(double x, double y, double x2, double y2, int color) {
        GLUtils.glEnable(3042);
        GLUtils.glDisable(3553);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtils.glEnable(2848);
        RenderUtilsTargetHud.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GLUtils.glEnable(3553);
        GLUtils.glDisable(3042);
        GLUtils.glDisable(2848);
    }

    public static void drawCircleRect(float x, float y, float x1, float y1, float radius, int color) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        RenderUtilsTargetHud.glColor(color);
        RenderUtilsTargetHud.quickRenderCircle(x1 - radius, y1 - radius, 0.0, 90.0, radius, radius);
        RenderUtilsTargetHud.quickRenderCircle(x + radius, y1 - radius, 90.0, 180.0, radius, radius);
        RenderUtilsTargetHud.quickRenderCircle(x + radius, y + radius, 180.0, 270.0, radius, radius);
        RenderUtilsTargetHud.quickRenderCircle(x1 - radius, y + radius, 270.0, 360.0, radius, radius);
        RenderUtilsTargetHud.quickDrawRect(x + radius, y + radius, x1 - radius, y1 - radius);
        RenderUtilsTargetHud.quickDrawRect(x, y + radius, x + radius, y1 - radius);
        RenderUtilsTargetHud.quickDrawRect(x1 - radius, y + radius, x1, y1 - radius);
        RenderUtilsTargetHud.quickDrawRect(x + radius, y, x1 - radius, y + radius);
        RenderUtilsTargetHud.quickDrawRect(x + radius, y1 - radius, x1 - radius, y1);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void quickRenderCircle(double x, double y, double start, double end, double w, double h) {
        if (start > end) {
            double temp = end;
            end = start;
            start = temp;
        }
        GL11.glBegin((int)6);
        GL11.glVertex2d((double)x, (double)y);
        for (double i = end; i >= start; i -= 4.0) {
            double ldx = Math.cos(i * Math.PI / 180.0) * w;
            double ldy = Math.sin(i * Math.PI / 180.0) * h;
            GL11.glVertex2d((double)(x + ldx), (double)(y + ldy));
        }
        GL11.glVertex2d((double)x, (double)y);
        GL11.glEnd();
    }

    public static void drawFilledCircle(int xx, int yy, float radius, Color color) {
        int sections = 50;
        double dAngle = Math.PI * 2 / (double)sections;
        GL11.glPushAttrib((int)8192);
        GLUtils.glEnable(3042);
        GLUtils.glDisable(3553);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtils.glEnable(2848);
        GL11.glBegin((int)6);
        for (int i = 0; i < sections; ++i) {
            float x = (float)((double)radius * Math.sin((double)i * dAngle));
            float y = (float)((double)radius * Math.cos((double)i * dAngle));
            GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            GL11.glVertex2f((float)((float)xx + x), (float)((float)yy + y));
        }
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glEnd();
        GL11.glPopAttrib();
    }

    public static void drawFilledCircle(double x, double y, double r, int c, int id) {
        float f = (float)(c >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(c >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(c >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(c & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glBegin((int)9);
        if (id == 1) {
            GL11.glVertex2d((double)x, (double)y);
            for (int i = 0; i <= 90; ++i) {
                double x2 = Math.sin((double)i * 3.141526 / 180.0) * r;
                double y2 = Math.cos((double)i * 3.141526 / 180.0) * r;
                GL11.glVertex2d((double)(x - x2), (double)(y - y2));
            }
        } else if (id == 2) {
            GL11.glVertex2d((double)x, (double)y);
            for (int i = 90; i <= 180; ++i) {
                double x2 = Math.sin((double)i * 3.141526 / 180.0) * r;
                double y2 = Math.cos((double)i * 3.141526 / 180.0) * r;
                GL11.glVertex2d((double)(x - x2), (double)(y - y2));
            }
        } else if (id == 3) {
            GL11.glVertex2d((double)x, (double)y);
            for (int i = 270; i <= 360; ++i) {
                double x2 = Math.sin((double)i * 3.141526 / 180.0) * r;
                double y2 = Math.cos((double)i * 3.141526 / 180.0) * r;
                GL11.glVertex2d((double)(x - x2), (double)(y - y2));
            }
        } else if (id == 4) {
            GL11.glVertex2d((double)x, (double)y);
            for (int i = 180; i <= 270; ++i) {
                double x2 = Math.sin((double)i * 3.141526 / 180.0) * r;
                double y2 = Math.cos((double)i * 3.141526 / 180.0) * r;
                GL11.glVertex2d((double)(x - x2), (double)(y - y2));
            }
        } else {
            for (int i = 0; i <= 360; ++i) {
                double x2 = Math.sin((double)i * 3.141526 / 180.0) * r;
                double y2 = Math.cos((double)i * 3.141526 / 180.0) * r;
                GL11.glVertex2f((float)((float)(x - x2)), (float)((float)(y - y2)));
            }
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
    }

    public static void drawFilledCircle(float xx, float yy, float radius, Color color) {
        int sections = 50;
        double dAngle = Math.PI * 2 / (double)sections;
        GL11.glPushAttrib((int)8192);
        GLUtils.glEnable(3042);
        GLUtils.glDisable(3553);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtils.glEnable(2848);
        GL11.glBegin((int)6);
        for (int i = 0; i < sections; ++i) {
            float x = (float)((double)radius * Math.sin((double)i * dAngle));
            float y = (float)((double)radius * Math.cos((double)i * dAngle));
            GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            GL11.glVertex2f((float)(xx + x), (float)(yy + y));
        }
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glEnd();
        GL11.glPopAttrib();
    }

    public static void drawImage(ResourceLocation image2, int x, int y, int width, int height) {
        GLUtils.glDisable(2929);
        GLUtils.glEnable(3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        mc.func_110434_K().func_110577_a(image2);
        Gui.func_146110_a((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GLUtils.glDisable(3042);
        GLUtils.glEnable(2929);
    }

    public static void drawImage(ResourceLocation image2, int x, int y, int width, int height, float alpha) {
        GLUtils.glDisable(2929);
        GLUtils.glEnable(3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)alpha);
        mc.func_110434_K().func_110577_a(image2);
        Gui.func_146110_a((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GLUtils.glDisable(3042);
        GLUtils.glEnable(2929);
    }

    public static void drawImage2(ResourceLocation image2, float x, float y, int width, int height) {
        GLUtils.glDisable(2929);
        GLUtils.glEnable(3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glTranslatef((float)x, (float)y, (float)x);
        mc.func_110434_K().func_110577_a(image2);
        Gui.func_146110_a((int)0, (int)0, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glTranslatef((float)(-x), (float)(-y), (float)(-x));
        GL11.glDepthMask((boolean)true);
        GLUtils.glDisable(3042);
        GLUtils.glEnable(2929);
    }

    public static void drawImage3(ResourceLocation image2, float x, float y, int width, int height, float r, float g, float b, float al) {
        GLUtils.glDisable(2929);
        GLUtils.glEnable(3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)r, (float)g, (float)b, (float)al);
        GL11.glTranslatef((float)x, (float)y, (float)x);
        mc.func_110434_K().func_110577_a(image2);
        Gui.func_146110_a((int)0, (int)0, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glTranslatef((float)(-x), (float)(-y), (float)(-x));
        GL11.glDepthMask((boolean)true);
        GLUtils.glDisable(3042);
        GLUtils.glEnable(2929);
    }

    public static void drawExhiEnchants(ItemStack stack, float x, float y) {
        int unb;
        RenderHelper.func_74518_a();
        GlStateManager.func_179097_i();
        GlStateManager.func_179084_k();
        GlStateManager.func_179117_G();
        int darkBorder = -16777216;
        if (stack.func_77973_b() instanceof ItemArmor) {
            int prot = EnchantmentHelper.func_77506_a((int)Enchantment.field_180310_c.field_77352_x, (ItemStack)stack);
            int unb2 = EnchantmentHelper.func_77506_a((int)Enchantment.field_77347_r.field_77352_x, (ItemStack)stack);
            int thorn = EnchantmentHelper.func_77506_a((int)Enchantment.field_92091_k.field_77352_x, (ItemStack)stack);
            if (prot > 0) {
                RenderUtilsTargetHud.drawExhiOutlined(prot + "", RenderUtilsTargetHud.drawExhiOutlined("P", x, y, 0.35f, -16777216, -1, true), y, 0.35f, RenderUtilsTargetHud.getBorderColor(prot), RenderUtilsTargetHud.getMainColor(prot), true);
                y += 4.0f;
            }
            if (unb2 > 0) {
                RenderUtilsTargetHud.drawExhiOutlined(unb2 + "", RenderUtilsTargetHud.drawExhiOutlined("U", x, y, 0.35f, -16777216, -1, true), y, 0.35f, RenderUtilsTargetHud.getBorderColor(unb2), RenderUtilsTargetHud.getMainColor(unb2), true);
                y += 4.0f;
            }
            if (thorn > 0) {
                RenderUtilsTargetHud.drawExhiOutlined(thorn + "", RenderUtilsTargetHud.drawExhiOutlined("T", x, y, 0.35f, -16777216, -1, true), y, 0.35f, RenderUtilsTargetHud.getBorderColor(thorn), RenderUtilsTargetHud.getMainColor(thorn), true);
                y += 4.0f;
            }
        }
        if (stack.func_77973_b() instanceof ItemBow) {
            int power = EnchantmentHelper.func_77506_a((int)Enchantment.field_77345_t.field_77352_x, (ItemStack)stack);
            int punch = EnchantmentHelper.func_77506_a((int)Enchantment.field_77344_u.field_77352_x, (ItemStack)stack);
            int flame = EnchantmentHelper.func_77506_a((int)Enchantment.field_77343_v.field_77352_x, (ItemStack)stack);
            unb = EnchantmentHelper.func_77506_a((int)Enchantment.field_77347_r.field_77352_x, (ItemStack)stack);
            if (power > 0) {
                RenderUtilsTargetHud.drawExhiOutlined(power + "", RenderUtilsTargetHud.drawExhiOutlined("Pow", x, y, 0.35f, -16777216, -1, true), y, 0.35f, RenderUtilsTargetHud.getBorderColor(power), RenderUtilsTargetHud.getMainColor(power), true);
                y += 4.0f;
            }
            if (punch > 0) {
                RenderUtilsTargetHud.drawExhiOutlined(punch + "", RenderUtilsTargetHud.drawExhiOutlined("Pun", x, y, 0.35f, -16777216, -1, true), y, 0.35f, RenderUtilsTargetHud.getBorderColor(punch), RenderUtilsTargetHud.getMainColor(punch), true);
                y += 4.0f;
            }
            if (flame > 0) {
                RenderUtilsTargetHud.drawExhiOutlined(flame + "", RenderUtilsTargetHud.drawExhiOutlined("F", x, y, 0.35f, -16777216, -1, true), y, 0.35f, RenderUtilsTargetHud.getBorderColor(flame), RenderUtilsTargetHud.getMainColor(flame), true);
                y += 4.0f;
            }
            if (unb > 0) {
                RenderUtilsTargetHud.drawExhiOutlined(unb + "", RenderUtilsTargetHud.drawExhiOutlined("U", x, y, 0.35f, -16777216, -1, true), y, 0.35f, RenderUtilsTargetHud.getBorderColor(unb), RenderUtilsTargetHud.getMainColor(unb), true);
                y += 4.0f;
            }
        }
        if (stack.func_77973_b() instanceof ItemSword) {
            int sharp = EnchantmentHelper.func_77506_a((int)Enchantment.field_180314_l.field_77352_x, (ItemStack)stack);
            int kb = EnchantmentHelper.func_77506_a((int)Enchantment.field_180313_o.field_77352_x, (ItemStack)stack);
            int fire = EnchantmentHelper.func_77506_a((int)Enchantment.field_77334_n.field_77352_x, (ItemStack)stack);
            unb = EnchantmentHelper.func_77506_a((int)Enchantment.field_77347_r.field_77352_x, (ItemStack)stack);
            if (sharp > 0) {
                RenderUtilsTargetHud.drawExhiOutlined(sharp + "", RenderUtilsTargetHud.drawExhiOutlined("S", x, y, 0.35f, -16777216, -1, true), y, 0.35f, RenderUtilsTargetHud.getBorderColor(sharp), RenderUtilsTargetHud.getMainColor(sharp), true);
                y += 4.0f;
            }
            if (kb > 0) {
                RenderUtilsTargetHud.drawExhiOutlined(kb + "", RenderUtilsTargetHud.drawExhiOutlined("K", x, y, 0.35f, -16777216, -1, true), y, 0.35f, RenderUtilsTargetHud.getBorderColor(kb), RenderUtilsTargetHud.getMainColor(kb), true);
                y += 4.0f;
            }
            if (fire > 0) {
                RenderUtilsTargetHud.drawExhiOutlined(fire + "", RenderUtilsTargetHud.drawExhiOutlined("F", x, y, 0.35f, -16777216, -1, true), y, 0.35f, RenderUtilsTargetHud.getBorderColor(fire), RenderUtilsTargetHud.getMainColor(fire), true);
                y += 4.0f;
            }
            if (unb > 0) {
                RenderUtilsTargetHud.drawExhiOutlined(unb + "", RenderUtilsTargetHud.drawExhiOutlined("U", x, y, 0.35f, -16777216, -1, true), y, 0.35f, RenderUtilsTargetHud.getBorderColor(unb), RenderUtilsTargetHud.getMainColor(unb), true);
                y += 4.0f;
            }
        }
        GlStateManager.func_179126_j();
        RenderHelper.func_74520_c();
    }

    private static float drawExhiOutlined(String text, float x, float y, float borderWidth, int borderColor, int mainColor, boolean drawText) {
        Fonts.fontTahomaSmall.drawString(text, x, y - borderWidth, borderColor);
        Fonts.fontTahomaSmall.drawString(text, x, y + borderWidth, borderColor);
        Fonts.fontTahomaSmall.drawString(text, x - borderWidth, y, borderColor);
        Fonts.fontTahomaSmall.drawString(text, x + borderWidth, y, borderColor);
        if (drawText) {
            Fonts.fontTahomaSmall.drawString(text, x, y, mainColor);
        }
        return x + Fonts.fontTahomaSmall.getWidth(text) - 2.0f;
    }

    private static int getMainColor(int level) {
        if (level == 4) {
            return -5636096;
        }
        return -1;
    }

    private static int getBorderColor(int level) {
        if (level == 2) {
            return 1884684117;
        }
        if (level == 3) {
            return 0x7000AAAA;
        }
        if (level == 4) {
            return 0x70AA0000;
        }
        if (level >= 5) {
            return 1895803392;
        }
        return 0x70FFFFFF;
    }

    public static void glColor(int red, int green, int blue, int alpha) {
        GlStateManager.func_179131_c((float)((float)red / 255.0f), (float)((float)green / 255.0f), (float)((float)blue / 255.0f), (float)((float)alpha / 255.0f));
    }

    public static void glColor(Color color) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void draw2D(EntityLivingBase entity, double posX, double posY, double posZ, int color, int backgroundColor) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)posX, (double)posY, (double)posZ);
        GlStateManager.func_179114_b((float)(-RenderUtilsTargetHud.mc.func_175598_ae().field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179139_a((double)-0.1, (double)-0.1, (double)0.1);
        GLUtils.glDisable(2929);
        GLUtils.glEnable(3042);
        GLUtils.glDisable(3553);
        GL11.glBlendFunc((int)770, (int)771);
        GlStateManager.func_179132_a((boolean)true);
        RenderUtilsTargetHud.glColor(color);
        GL11.glCallList((int)DISPLAY_LISTS_2D[0]);
        RenderUtilsTargetHud.glColor(backgroundColor);
        GL11.glCallList((int)DISPLAY_LISTS_2D[1]);
        GlStateManager.func_179137_b((double)0.0, (double)(21.0 + -(entity.func_174813_aQ().field_72337_e - entity.func_174813_aQ().field_72338_b) * 12.0), (double)0.0);
        RenderUtilsTargetHud.glColor(color);
        GL11.glCallList((int)DISPLAY_LISTS_2D[2]);
        RenderUtilsTargetHud.glColor(backgroundColor);
        GL11.glCallList((int)DISPLAY_LISTS_2D[3]);
        GLUtils.glEnable(2929);
        GLUtils.glEnable(3553);
        GLUtils.glDisable(3042);
        GlStateManager.func_179121_F();
    }

    public static void setColor(Color color) {
        float alpha = (float)(color.getRGB() >> 24 & 0xFF) / 255.0f;
        float red = (float)(color.getRGB() >> 16 & 0xFF) / 255.0f;
        float green = (float)(color.getRGB() >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color.getRGB() & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void draw2D(BlockPos blockPos, int color, int backgroundColor) {
        RenderManager renderManager = mc.func_175598_ae();
        double posX = (double)blockPos.func_177958_n() + 0.5 - renderManager.field_78725_b;
        double posY = (double)blockPos.func_177956_o() - renderManager.field_78726_c;
        double posZ = (double)blockPos.func_177952_p() + 0.5 - renderManager.field_78723_d;
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)posX, (double)posY, (double)posZ);
        GlStateManager.func_179114_b((float)(-RenderUtilsTargetHud.mc.func_175598_ae().field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179139_a((double)-0.1, (double)-0.1, (double)0.1);
        GLUtils.glDisable(2929);
        GLUtils.glEnable(3042);
        GLUtils.glDisable(3553);
        GL11.glBlendFunc((int)770, (int)771);
        GlStateManager.func_179132_a((boolean)true);
        RenderUtilsTargetHud.glColor(color);
        GL11.glCallList((int)DISPLAY_LISTS_2D[0]);
        RenderUtilsTargetHud.glColor(backgroundColor);
        GL11.glCallList((int)DISPLAY_LISTS_2D[1]);
        GlStateManager.func_179109_b((float)0.0f, (float)9.0f, (float)0.0f);
        RenderUtilsTargetHud.glColor(color);
        GL11.glCallList((int)DISPLAY_LISTS_2D[2]);
        RenderUtilsTargetHud.glColor(backgroundColor);
        GL11.glCallList((int)DISPLAY_LISTS_2D[3]);
        GLUtils.glEnable(2929);
        GLUtils.glEnable(3553);
        GLUtils.glDisable(3042);
        GlStateManager.func_179121_F();
    }

    public static void renderNameTag(String string, double x, double y, double z) {
        RenderManager renderManager = mc.func_175598_ae();
        GL11.glPushMatrix();
        GL11.glTranslated((double)(x - renderManager.field_78725_b), (double)(y - renderManager.field_78726_c), (double)(z - renderManager.field_78723_d));
        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)(-RenderUtilsTargetHud.mc.func_175598_ae().field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)RenderUtilsTargetHud.mc.func_175598_ae().field_78732_j, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glScalef((float)-0.05f, (float)-0.05f, (float)0.05f);
        RenderUtilsTargetHud.setGlCap(2896, false);
        RenderUtilsTargetHud.setGlCap(2929, false);
        RenderUtilsTargetHud.setGlCap(3042, true);
        GL11.glBlendFunc((int)770, (int)771);
        int width = Fonts.font35.func_78256_a(string) / 2;
        Gui.func_73734_a((int)(-width - 1), (int)-1, (int)(width + 1), (int)Fonts.font35.field_78288_b, (int)Integer.MIN_VALUE);
        Fonts.font35.func_175065_a(string, -width, 1.5f, Color.WHITE.getRGB(), true);
        RenderUtilsTargetHud.resetCaps();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
    }

    public static void drawLine(float x, float y, float x1, float y1, float width) {
        GLUtils.glDisable(3553);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)1);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glEnd();
        GLUtils.glEnable(3553);
    }

    public static void drawLine(double x, double y, double x1, double y1, float width) {
        GLUtils.glDisable(3553);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glEnd();
        GLUtils.glEnable(3553);
    }

    public static void makeScissorBox(float x, float y, float x2, float y2) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int factor = scaledResolution.func_78325_e();
        GL11.glScissor((int)((int)(x * (float)factor)), (int)((int)(((float)scaledResolution.func_78328_b() - y2) * (float)factor)), (int)((int)((x2 - x) * (float)factor)), (int)((int)((y2 - y) * (float)factor)));
    }

    public static void resetCaps() {
        glCapMap.forEach(RenderUtils::setGlState);
    }

    public static void enableGlCap(int cap) {
        RenderUtilsTargetHud.setGlCap(cap, true);
    }

    public static void enableGlCap(int ... caps) {
        for (int cap : caps) {
            RenderUtilsTargetHud.setGlCap(cap, true);
        }
    }

    public static void disableGlCap(int ... caps) {
        for (int cap : caps) {
            RenderUtilsTargetHud.setGlCap(cap, false);
        }
    }

    public static void setGlCap(int cap, boolean state) {
        glCapMap.put(cap, GL11.glGetBoolean((int)cap));
        RenderUtilsTargetHud.setGlState(cap, state);
    }

    public static void setGlState(int cap, boolean state) {
        if (state) {
            GLUtils.glEnable(cap);
        } else {
            GLUtils.glDisable(cap);
        }
    }

    static {
        mc = Minecraft.func_71410_x();
        DISPLAY_LISTS_2D = new int[4];
        for (int i = 0; i < DISPLAY_LISTS_2D.length; ++i) {
            RenderUtilsTargetHud.DISPLAY_LISTS_2D[i] = GL11.glGenLists((int)1);
        }
        GL11.glNewList((int)DISPLAY_LISTS_2D[0], (int)4864);
        RenderUtilsTargetHud.quickDrawRect(-7.0f, 2.0f, -4.0f, 3.0f);
        RenderUtilsTargetHud.quickDrawRect(4.0f, 2.0f, 7.0f, 3.0f);
        RenderUtilsTargetHud.quickDrawRect(-7.0f, 0.5f, -6.0f, 3.0f);
        RenderUtilsTargetHud.quickDrawRect(6.0f, 0.5f, 7.0f, 3.0f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[1], (int)4864);
        RenderUtilsTargetHud.quickDrawRect(-7.0f, 3.0f, -4.0f, 3.3f);
        RenderUtilsTargetHud.quickDrawRect(4.0f, 3.0f, 7.0f, 3.3f);
        RenderUtilsTargetHud.quickDrawRect(-7.3f, 0.5f, -7.0f, 3.3f);
        RenderUtilsTargetHud.quickDrawRect(7.0f, 0.5f, 7.3f, 3.3f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[2], (int)4864);
        RenderUtilsTargetHud.quickDrawRect(4.0f, -20.0f, 7.0f, -19.0f);
        RenderUtilsTargetHud.quickDrawRect(-7.0f, -20.0f, -4.0f, -19.0f);
        RenderUtilsTargetHud.quickDrawRect(6.0f, -20.0f, 7.0f, -17.5f);
        RenderUtilsTargetHud.quickDrawRect(-7.0f, -20.0f, -6.0f, -17.5f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[3], (int)4864);
        RenderUtilsTargetHud.quickDrawRect(7.0f, -20.0f, 7.3f, -17.5f);
        RenderUtilsTargetHud.quickDrawRect(-7.3f, -20.0f, -7.0f, -17.5f);
        RenderUtilsTargetHud.quickDrawRect(4.0f, -20.3f, 7.3f, -20.0f);
        RenderUtilsTargetHud.quickDrawRect(-7.3f, -20.3f, -4.0f, -20.0f);
        GL11.glEndList();
        frustrum = new Frustum();
        zLevel = 0.0f;
        viewport = BufferUtils.createIntBuffer((int)16);
        modelview = GLAllocation.func_74529_h((int)16);
        projections = GLAllocation.func_74529_h((int)16);
    }
}

