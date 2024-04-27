/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.MathHelper
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL14
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.WDefaultVertexFormats;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.render.ITessellator;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import net.ccbluex.liquidbounce.api.minecraft.renderer.entity.IRenderManager;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.api.minecraft.util.ITimer;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.features.module.modules.render.CustomColor;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.features.module.modules.render.PictureColor;
import net.ccbluex.liquidbounce.features.module.modules.render.PictureColor2;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.misc.MathUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.Colors;
import net.ccbluex.liquidbounce.utils.render.ImageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

public final class RenderUtils
extends MinecraftInstance {
    public static int deltaTime;
    public static Framebuffer bloomFramebuffer;
    private static final Map glCapMap;
    private static final int[] DISPLAY_LISTS_2D;

    public static void quickDrawRect(float f, float f2, float f3, float f4) {
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)f3, (double)f2);
        GL11.glVertex2d((double)f, (double)f2);
        GL11.glVertex2d((double)f, (double)f4);
        GL11.glVertex2d((double)f3, (double)f4);
        GL11.glEnd();
    }

    public static void ArrayListBGGradient(double d, double d2, double d3, double d4, int n, int n2) {
        float f = (float)(n >> 24 & 0xFF) / 230.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 230.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 230.0f;
        float f4 = (float)(n & 0xFF) / 230.0f;
        float f5 = (float)(n2 >> 24 & 0xFF) / 230.0f;
        float f6 = (float)(n2 >> 16 & 0xFF) / 230.0f;
        float f7 = (float)(n2 >> 8 & 0xFF) / 230.0f;
        float f8 = (float)(n2 & 0xFF) / 230.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glVertex2d((double)d, (double)d4);
        GL11.glColor4f((float)f6, (float)f7, (float)f8, (float)f5);
        GL11.glVertex2d((double)d3, (double)d4);
        GL11.glVertex2d((double)d3, (double)d2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static boolean glEnableBlend() {
        boolean bl = GL11.glIsEnabled((int)3042);
        if (!bl) {
            GL11.glEnable((int)3042);
            GL14.glBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        }
        return bl;
    }

    public static void color(Color color) {
        RenderUtils.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static void drawArc(float f, float f2, double d, int n, int n2, double d2, int n3) {
        d *= 2.0;
        f *= 2.0f;
        f2 *= 2.0f;
        float f3 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f4 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f5 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f6 = (float)(n & 0xFF) / 255.0f;
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glLineWidth((float)n3);
        GL11.glEnable((int)2848);
        GL11.glColor4f((float)f4, (float)f5, (float)f6, (float)f3);
        GL11.glBegin((int)3);
        int n4 = n2;
        while ((double)n4 <= d2) {
            GL11.glVertex2d((double)((double)f + Math.sin((double)n4 * Math.PI / 180.0) * d), (double)((double)f2 + Math.cos((double)n4 * Math.PI / 180.0) * d));
            ++n4;
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    public static Color getGradientOffset2(Color color, Color color2, double d) {
        int n;
        double d2;
        if (d > 1.0) {
            d2 = d % 1.0;
            n = (int)d;
            d = n % 2 == 0 ? d2 : 1.0 - d2;
        }
        d2 = 1.0 - d;
        n = (int)((double)color.getRed() * d2 + (double)color2.getRed() * d);
        int n2 = (int)((double)color.getGreen() * d2 + (double)color2.getGreen() * d);
        int n3 = (int)((double)color.getBlue() * d2 + (double)color2.getBlue() * d);
        return new Color(n, n2, n3);
    }

    public static void disableGlCap(int ... nArray) {
        for (int n : nArray) {
            RenderUtils.setGlCap(n, false);
        }
    }

    public static void end() {
        GL11.glEnd();
    }

    static {
        glCapMap = new HashMap();
        DISPLAY_LISTS_2D = new int[4];
        bloomFramebuffer = new Framebuffer(1, 1, false);
        for (int i = 0; i < DISPLAY_LISTS_2D.length; ++i) {
            RenderUtils.DISPLAY_LISTS_2D[i] = GL11.glGenLists((int)1);
        }
        GL11.glNewList((int)DISPLAY_LISTS_2D[0], (int)4864);
        RenderUtils.quickDrawRect(-7.0f, 2.0f, -4.0f, 3.0f);
        RenderUtils.quickDrawRect(4.0f, 2.0f, 7.0f, 3.0f);
        RenderUtils.quickDrawRect(-7.0f, 0.5f, -6.0f, 3.0f);
        RenderUtils.quickDrawRect(6.0f, 0.5f, 7.0f, 3.0f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[1], (int)4864);
        RenderUtils.quickDrawRect(-7.0f, 3.0f, -4.0f, 3.3f);
        RenderUtils.quickDrawRect(4.0f, 3.0f, 7.0f, 3.3f);
        RenderUtils.quickDrawRect(-7.3f, 0.5f, -7.0f, 3.3f);
        RenderUtils.quickDrawRect(7.0f, 0.5f, 7.3f, 3.3f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[2], (int)4864);
        RenderUtils.quickDrawRect(4.0f, -20.0f, 7.0f, -19.0f);
        RenderUtils.quickDrawRect(-7.0f, -20.0f, -4.0f, -19.0f);
        RenderUtils.quickDrawRect(6.0f, -20.0f, 7.0f, -17.5f);
        RenderUtils.quickDrawRect(-7.0f, -20.0f, -6.0f, -17.5f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[3], (int)4864);
        RenderUtils.quickDrawRect(7.0f, -20.0f, 7.3f, -17.5f);
        RenderUtils.quickDrawRect(-7.3f, -20.0f, -7.0f, -17.5f);
        RenderUtils.quickDrawRect(4.0f, -20.3f, 7.3f, -20.0f);
        RenderUtils.quickDrawRect(-7.3f, -20.3f, -4.0f, -20.0f);
        GL11.glEndList();
    }

    public static void setAlphaLimit(float f) {
        GlStateManager.func_179141_d();
        GlStateManager.func_179092_a((int)516, (float)((float)((double)f * 0.01)));
    }

    public static void drawRoundedCornerRect(float f, float f2, float f3, float f4, float f5, int n) {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        boolean bl = GL11.glIsEnabled((int)2884);
        GL11.glDisable((int)2884);
        RenderUtils.glColor(n);
        RenderUtils.drawRoundedCornerRect(f, f2, f3, f4, f5);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        RenderUtils.setGlState(2884, bl);
    }

    public static void drawmark(IEntity iEntity, double d, boolean bl) {
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glEnable((int)2832);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glHint((int)3153, (int)4354);
        GL11.glDepthMask((boolean)false);
        GlStateManager.func_179092_a((int)516, (float)0.0f);
        if (bl) {
            GL11.glShadeModel((int)7425);
        }
        GlStateManager.func_179129_p();
        GL11.glBegin((int)5);
        double d2 = iEntity.getLastTickPosX() + (iEntity.getPosX() - iEntity.getLastTickPosX()) * (double)mc.getTimer().getRenderPartialTicks() - mc.getRenderManager().getRenderPosX();
        double d3 = iEntity.getLastTickPosY() + (iEntity.getPosY() - iEntity.getLastTickPosY()) * (double)mc.getTimer().getRenderPartialTicks() - mc.getRenderManager().getRenderPosY() + Math.sin((double)System.currentTimeMillis() / 200.0) + 1.0;
        double d4 = iEntity.getLastTickPosZ() + (iEntity.getPosZ() - iEntity.getLastTickPosZ()) * (double)mc.getTimer().getRenderPartialTicks() - mc.getRenderManager().getRenderPosZ();
        PictureColor pictureColor = (PictureColor)LiquidBounce.moduleManager.getModule(PictureColor.class);
        PictureColor2 pictureColor2 = (PictureColor2)LiquidBounce.moduleManager.getModule(PictureColor2.class);
        float f = 0.0f;
        while (f < 6.2832f) {
            double d5 = d2 + d * Math.cos(f);
            double d6 = d4 + d * Math.sin(f);
            Color color = ColorUtils.mixColors(new Color((Integer)pictureColor.getColorRedValue().get(), (Integer)pictureColor.getColorGreenValue().get(), (Integer)pictureColor.getColorBlueValue().get(), 255), new Color((Integer)pictureColor2.getColorRedValue().get(), (Integer)pictureColor2.getColorGreenValue().get(), (Integer)pictureColor2.getColorBlueValue().get(), 255), (Math.sin((double)System.currentTimeMillis() / 1.0E8 * 1.0 * 400000.0 + (double)(f * 2.0f)) + 1.0) * 0.2);
            if (bl) {
                GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)0.0f);
                GL11.glVertex3d((double)d5, (double)(d3 - Math.cos((double)System.currentTimeMillis() / 200.0) / 2.0), (double)d6);
                GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)0.85f);
            }
            GL11.glVertex3d((double)d5, (double)d3, (double)d6);
            f = (float)((double)f + 0.09817477042468103);
        }
        GL11.glEnd();
        if (bl) {
            GL11.glShadeModel((int)7424);
        }
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        GL11.glLineWidth((float)10.0f);
        GlStateManager.func_179092_a((int)516, (float)0.1f);
        GlStateManager.func_179089_o();
        GL11.glDisable((int)2848);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2832);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
        GL11.glColor3f((float)255.0f, (float)255.0f, (float)255.0f);
    }

    public static void setColor(int n) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
    }

    public static Color getGradientOffset(Color color, Color color2, double d) {
        int n;
        double d2;
        if (d > 1.0) {
            d2 = d % 1.0;
            n = (int)d;
            d = n % 2 == 0 ? d2 : 1.0 - d2;
        }
        d2 = 1.0 - d;
        n = (int)((double)color.getRed() * d2 + (double)color2.getRed() * d);
        int n2 = (int)((double)color.getGreen() * d2 + (double)color2.getGreen() * d);
        int n3 = (int)((double)color.getBlue() * d2 + (double)color2.getBlue() * d);
        return new Color(n, n2, n3);
    }

    public static void glColor(Color color) {
        RenderUtils.glColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static void drawFilledCircle(int n, int n2, float f, Color color) {
        int n3 = 50;
        double d = Math.PI * 2 / (double)n3;
        GL11.glPushAttrib((int)8192);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)6);
        for (int i = 0; i < n3; ++i) {
            float f2 = (float)((double)f * Math.sin((double)i * d));
            float f3 = (float)((double)f * Math.cos((double)i * d));
            GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            GL11.glVertex2f((float)((float)n + f2), (float)((float)n2 + f3));
        }
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glEnd();
        GL11.glPopAttrib();
    }

    public static void glColor(int n) {
        RenderUtils.glColor(n >> 16 & 0xFF, n >> 8 & 0xFF, n & 0xFF, n >> 24 & 0xFF);
    }

    public static void quickDrawGradientSidewaysH(double d, double d2, double d3, double d4, int n, int n2) {
        GL11.glBegin((int)7);
        RenderUtils.glColor(n);
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glVertex2d((double)d, (double)d4);
        RenderUtils.glColor(n2);
        GL11.glVertex2d((double)d3, (double)d4);
        GL11.glVertex2d((double)d3, (double)d2);
        GL11.glEnd();
    }

    public static int interpolateInt(int n, int n2, double d) {
        return RenderUtils.interpolate(n, n2, (float)d).intValue();
    }

    public static void drawRoundedCornerRect(float f, float f2, float f3, float f4, float f5) {
        GL11.glBegin((int)9);
        float f6 = (float)Math.min((double)(f3 - f) * 0.5, (double)f5);
        float f7 = (float)Math.min((double)(f4 - f2) * 0.5, (double)f5);
        RenderUtils.quickPolygonCircle(f + f6, f2 + f7, f6, f7, 180, 270, 4);
        RenderUtils.quickPolygonCircle(f3 - f6, f2 + f7, f6, f7, 90, 180, 4);
        RenderUtils.quickPolygonCircle(f3 - f6, f4 - f7, f6, f7, 0, 90, 4);
        RenderUtils.quickPolygonCircle(f + f6, f4 - f7, f6, f7, 270, 360, 4);
        GL11.glEnd();
    }

    public static void enableSmoothLine(float f) {
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
        GL11.glLineWidth((float)f);
    }

    private static void quickPolygonCircle(float f, float f2, float f3, float f4, int n, int n2, int n3) {
        for (int i = n2; i >= n; i -= n3) {
            GL11.glVertex2d((double)((double)f + Math.sin((double)i * Math.PI / 180.0) * (double)f3), (double)((double)f2 + Math.cos((double)i * Math.PI / 180.0) * (double)f4));
        }
    }

    public static void color(float f, float f2, float f3, float f4) {
        GL11.glColor4f((float)(f / 255.0f), (float)(f2 / 255.0f), (float)(f3 / 255.0f), (float)(f4 / 255.0f));
    }

    public static void customRounded(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int n) {
        double d;
        float f9;
        float f10 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f11 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f12 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f13 = (float)(n & 0xFF) / 255.0f;
        if (f > f3) {
            f9 = f;
            f = f3;
            f3 = f9;
        }
        if (f2 > f4) {
            f9 = f2;
            f2 = f4;
            f4 = f9;
        }
        double d2 = f + f5;
        double d3 = f2 + f5;
        double d4 = f3 - f6;
        double d5 = f2 + f6;
        double d6 = f3 - f7;
        double d7 = f4 - f7;
        double d8 = f + f8;
        double d9 = f4 - f8;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)1.0f);
        GL11.glColor4f((float)f11, (float)f12, (float)f13, (float)f10);
        GL11.glBegin((int)9);
        double d10 = Math.PI / 180;
        for (d = 0.0; d <= 90.0; d += 0.25) {
            GL11.glVertex2d((double)(d6 + Math.sin(d * d10) * (double)f7), (double)(d7 + Math.cos(d * d10) * (double)f7));
        }
        for (d = 90.0; d <= 180.0; d += 0.25) {
            GL11.glVertex2d((double)(d4 + Math.sin(d * d10) * (double)f6), (double)(d5 + Math.cos(d * d10) * (double)f6));
        }
        for (d = 180.0; d <= 270.0; d += 0.25) {
            GL11.glVertex2d((double)(d2 + Math.sin(d * d10) * (double)f5), (double)(d3 + Math.cos(d * d10) * (double)f5));
        }
        for (d = 270.0; d <= 360.0; d += 0.25) {
            GL11.glVertex2d((double)(d8 + Math.sin(d * d10) * (double)f8), (double)(d9 + Math.cos(d * d10) * (double)f8));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    }

    public static void drawTexturedRectWithCustomAlpha(float f, float f2, float f3, float f4, String string, float f5) {
        boolean bl;
        GL11.glPushMatrix();
        boolean bl2 = GL11.glIsEnabled((int)3042);
        boolean bl3 = bl = !GL11.glIsEnabled((int)3008);
        if (!bl2) {
            GL11.glEnable((int)3042);
        }
        if (!bl) {
            GL11.glDisable((int)3008);
        }
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)f5);
        minecraft.func_110434_K().func_110577_a(new ResourceLocation("shaders/" + string + ".png"));
        RenderUtils.drawModalRectWithCustomSizedTexture(f, f2, 0.0f, 0.0f, f3, f4, f3, f4);
        if (!bl2) {
            GL11.glDisable((int)3042);
        }
        if (!bl) {
            GL11.glEnable((int)3008);
        }
        GlStateManager.func_179117_G();
        GL11.glPopMatrix();
    }

    public static void drawImage4(ResourceLocation resourceLocation, int n, int n2, int n3, int n4) {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        GL14.glBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        mc.getTextureManager().bindTexture2(resourceLocation);
        RenderUtils.drawModalRectWithCustomSizedTexture(n, n2, 0.0f, 0.0f, n3, n4, (float)n3, (float)n4);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void translate(double d, double d2) {
        GL11.glTranslated((double)d, (double)d2, (double)0.0);
    }

    public static void glDrawFramebuffer(int n, int n2, int n3) {
        GL11.glBindTexture((int)3553, (int)n);
        GL11.glDisable((int)3008);
        boolean bl = RenderUtils.glEnableBlend();
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex2f((float)0.0f, (float)0.0f);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex2f((float)0.0f, (float)n3);
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex2f((float)n2, (float)n3);
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex2f((float)n2, (float)0.0f);
        GL11.glEnd();
        RenderUtils.glRestoreBlend(bl);
        GL11.glEnable((int)3008);
    }

    public static void drawModalRectWithCustomSizedTexture(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = 1.0f / f7;
        float f10 = 1.0f / f8;
        ITessellator iTessellator = classProvider.getTessellatorInstance();
        IWorldRenderer iWorldRenderer = iTessellator.getWorldRenderer();
        iWorldRenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION_TEX));
        iWorldRenderer.pos(f, f2 + f6, 0.0).tex(f3 * f9, (f4 + f6) * f10).endVertex();
        iWorldRenderer.pos(f + f5, f2 + f6, 0.0).tex((f3 + f5) * f9, (f4 + f6) * f10).endVertex();
        iWorldRenderer.pos(f + f5, f2, 0.0).tex((f3 + f5) * f9, f4 * f10).endVertex();
        iWorldRenderer.pos(f, f2, 0.0).tex(f3 * f9, f4 * f10).endVertex();
        iTessellator.draw();
    }

    public static void rectangleBordered(double d, double d2, double d3, double d4, double d5, int n, int n2) {
        RenderUtils.rectangle(d + d5, d2 + d5, d3 - d5, d4 - d5, n);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.rectangle(d + d5, d2, d3 - d5, d2 + d5, n2);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.rectangle(d, d2, d + d5, d4, n2);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.rectangle(d3 - d5, d2, d3, d4, n2);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.rectangle(d + d5, d4 - d5, d3 - d5, d4, n2);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static Color interpolateColorC(Color color, Color color2, float f) {
        f = Math.min(1.0f, Math.max(0.0f, f));
        return new Color(RenderUtils.interpolateInt(color.getRed(), color2.getRed(), f), RenderUtils.interpolateInt(color.getGreen(), color2.getGreen(), f), RenderUtils.interpolateInt(color.getBlue(), color2.getBlue(), f), RenderUtils.interpolateInt(color.getAlpha(), color2.getAlpha(), f));
    }

    public static void resetColor() {
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static int reAlpha(int n, float f) {
        Color color = new Color(n);
        float f2 = 0.003921569f * (float)color.getRed();
        float f3 = 0.003921569f * (float)color.getGreen();
        float f4 = 0.003921569f * (float)color.getBlue();
        return new Color(f2, f3, f4, f).getRGB();
    }

    public static void quickDrawBorderedRect(float f, float f2, float f3, float f4, float f5, int n, int n2) {
        RenderUtils.quickDrawRect(f, f2, f3, f4, n2);
        RenderUtils.glColor(n);
        GL11.glLineWidth((float)f5);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)f3, (double)f2);
        GL11.glVertex2d((double)f, (double)f2);
        GL11.glVertex2d((double)f, (double)f4);
        GL11.glVertex2d((double)f3, (double)f4);
        GL11.glEnd();
    }

    public static void rotate(double d, double d2, double d3, double d4) {
        GL11.glRotated((double)d4, (double)d, (double)d2, (double)d3);
    }

    public static double getAnimationStateSmooth(double d, double d2, double d3) {
        boolean bl;
        boolean bl2 = bl = d > d2;
        if (d3 < 0.0) {
            d3 = 0.0;
        } else if (d3 > 1.0) {
            d3 = 1.0;
        }
        if (d == d2) {
            return d;
        }
        double d4 = Math.max(d, d2) - Math.min(d, d2);
        double d5 = d4 * d3;
        if (d5 < 0.1) {
            d5 = 0.1;
        }
        d2 = bl ? (d2 + d5 > d ? d : (d2 += d5)) : (d2 - d5 < d ? d : (d2 -= d5));
        return d2;
    }

    public static void drawLoadingCircle(float f, float f2) {
        for (int i = 0; i < 4; ++i) {
            int n = (int)(System.nanoTime() / 5000000L * (long)i % 360L);
            RenderUtils.drawCircle(f, f2, i * 10, n - 180, n);
        }
    }

    public static void stop() {
        GlStateManager.func_179141_d();
        GlStateManager.func_179126_j();
        RenderUtils.enable(2884);
        RenderUtils.enable(3553);
        RenderUtils.disable(3042);
        RenderUtils.color(Color.white);
    }

    public static void disable(int n) {
        GL11.glDisable((int)n);
    }

    public static void autoExhibition(double d, double d2, double d3, double d4, double d5) {
        RenderUtils.rectangleBordered(d, d2, d3 + d5, d4 + d5, 0.5, Colors.getColor(90), Colors.getColor(0));
        RenderUtils.rectangleBordered(d + 1.0, d2 + 1.0, d3 + d5 - 1.0, d4 + d5 - 1.0, 1.0, Colors.getColor(90), Colors.getColor(61));
        RenderUtils.rectangleBordered(d + 2.5, d2 + 2.5, d3 + d5 - 2.5, d4 + d5 - 2.5, 0.5, Colors.getColor(61), Colors.getColor(0));
    }

    public static void stopDrawing() {
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void startSmooth() {
        RenderUtils.enable(2881);
        RenderUtils.enable(2848);
        RenderUtils.enable(2832);
    }

    public static int SkyRainbow(int n, float f, float f2) {
        double d;
        double d2 = Math.ceil(System.currentTimeMillis() + (long)(n * 109)) / 5.0;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(d2 / 360.0)) : (float)((d2 %= 360.0) / 360.0), f, f2).getRGB();
    }

    public static void enableGlCap(int ... nArray) {
        for (int n : nArray) {
            RenderUtils.setGlCap(n, true);
        }
    }

    public static void drawRoundedRect2(float f, float f2, float f3, float f4, float f5, int n) {
        int n2;
        float f6 = f + f3;
        float f7 = f2 + f4;
        float f8 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f9 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f10 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f11 = (float)(n & 0xFF) / 255.0f;
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        f *= 2.0f;
        f2 *= 2.0f;
        f6 *= 2.0f;
        f7 *= 2.0f;
        GL11.glDisable((int)3553);
        GL11.glColor4f((float)f9, (float)f10, (float)f11, (float)f8);
        GlStateManager.func_179147_l();
        GL11.glEnable((int)2848);
        GL11.glBegin((int)9);
        double d = Math.PI / 180;
        for (n2 = 0; n2 <= 90; n2 += 3) {
            GL11.glVertex2d((double)(f + f5 + MathHelper.func_76126_a((float)((float)((double)n2 * (Math.PI / 180)))) * (f5 * -1.0f)), (double)(f2 + f5 + MathHelper.func_76134_b((float)((float)((double)n2 * (Math.PI / 180)))) * (f5 * -1.0f)));
        }
        for (n2 = 90; n2 <= 180; n2 += 3) {
            GL11.glVertex2d((double)(f + f5 + MathHelper.func_76126_a((float)((float)((double)n2 * (Math.PI / 180)))) * (f5 * -1.0f)), (double)(f7 - f5 + MathHelper.func_76134_b((float)((float)((double)n2 * (Math.PI / 180)))) * (f5 * -1.0f)));
        }
        for (n2 = 0; n2 <= 90; n2 += 3) {
            GL11.glVertex2d((double)(f6 - f5 + MathHelper.func_76126_a((float)((float)((double)n2 * (Math.PI / 180)))) * f5), (double)(f7 - f5 + MathHelper.func_76134_b((float)((float)((double)n2 * (Math.PI / 180)))) * f5));
        }
        for (n2 = 90; n2 <= 180; n2 += 3) {
            GL11.glVertex2d((double)(f6 - f5 + MathHelper.func_76126_a((float)((float)((double)n2 * (Math.PI / 180)))) * f5), (double)(f2 + f5 + MathHelper.func_76134_b((float)((float)((double)n2 * (Math.PI / 180)))) * f5));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static float getAnimationState(float f, float f2, float f3) {
        float f4 = (float)deltaTime * f3;
        f = f < f2 ? (f + f4 < f2 ? (f += f4) : f2) : (f - f4 > f2 ? (f -= f4) : f2);
        return f;
    }

    public static void drawScaledCustomSizeModalRect(int n, int n2, float f, float f2, int n3, int n4, int n5, int n6, float f3, float f4) {
        float f5 = 1.0f / f3;
        float f6 = 1.0f / f4;
        ITessellator iTessellator = classProvider.getTessellatorInstance();
        IWorldRenderer iWorldRenderer = iTessellator.getWorldRenderer();
        iWorldRenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION_TEX));
        iWorldRenderer.pos(n, n2 + n6, 0.0).tex(f * f5, (f2 + (float)n4) * f6).endVertex();
        iWorldRenderer.pos(n + n5, n2 + n6, 0.0).tex((f + (float)n3) * f5, (f2 + (float)n4) * f6).endVertex();
        iWorldRenderer.pos(n + n5, n2, 0.0).tex((f + (float)n3) * f5, f2 * f6).endVertex();
        iWorldRenderer.pos(n, n2, 0.0).tex(f * f5, f2 * f6).endVertex();
        iTessellator.draw();
    }

    public static void draw2D(WBlockPos wBlockPos, int n, int n2) {
        IRenderManager iRenderManager = mc.getRenderManager();
        double d = (double)wBlockPos.getX() + 0.5 - iRenderManager.getRenderPosX();
        double d2 = (double)wBlockPos.getY() - iRenderManager.getRenderPosY();
        double d3 = (double)wBlockPos.getZ() + 0.5 - iRenderManager.getRenderPosZ();
        GL11.glPushMatrix();
        GL11.glTranslated((double)d, (double)d2, (double)d3);
        GL11.glRotated((double)(-mc.getRenderManager().getPlayerViewY()), (double)0.0, (double)1.0, (double)0.0);
        GL11.glScaled((double)-0.1, (double)-0.1, (double)0.1);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)true);
        RenderUtils.glColor(n);
        GL11.glCallList((int)DISPLAY_LISTS_2D[0]);
        RenderUtils.glColor(n2);
        GL11.glCallList((int)DISPLAY_LISTS_2D[1]);
        GL11.glTranslated((double)0.0, (double)9.0, (double)0.0);
        RenderUtils.glColor(n);
        GL11.glCallList((int)DISPLAY_LISTS_2D[2]);
        RenderUtils.glColor(n2);
        GL11.glCallList((int)DISPLAY_LISTS_2D[3]);
        GL11.glEnable((int)2929);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static Double interpolate(double d, double d2, double d3) {
        return d + (d2 - d) * d3;
    }

    public static void enable(int n) {
        GL11.glEnable((int)n);
    }

    public static void scale(double d, double d2) {
        GL11.glScaled((double)d, (double)d2, (double)1.0);
    }

    public static Color interpolateColorHue(Color color, Color color2, float f) {
        f = Math.min(1.0f, Math.max(0.0f, f));
        float[] fArray = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float[] fArray2 = Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(), null);
        Color color3 = Color.getHSBColor(MathUtils.interpolateFloat(fArray[0], fArray2[0], f), MathUtils.interpolateFloat(fArray[1], fArray2[1], f), MathUtils.interpolateFloat(fArray[2], fArray2[2], f));
        return new Color(color3.getRed(), color3.getGreen(), color3.getBlue(), RenderUtils.interpolateInt(color.getAlpha(), color2.getAlpha(), f));
    }

    public static void drawBorder(float f, float f2, float f3, float f4, float f5, int n) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        RenderUtils.glColor(n);
        GL11.glLineWidth((float)f5);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)f3, (double)f2);
        GL11.glVertex2d((double)f, (double)f2);
        GL11.glVertex2d((double)f, (double)f4);
        GL11.glVertex2d((double)f3, (double)f4);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawCircle(double d, double d2, double d3, float f, float f2, int n, float f3) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179118_c();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)f3);
        GL11.glBegin((int)3);
        for (int i = (int)((double)f / 360.0 * 100.0); i <= (int)((double)f2 / 360.0 * 100.0); ++i) {
            double d4 = Math.PI * 2 * (double)i / 100.0 + Math.toRadians(180.0);
            RenderUtils.color(n);
            GL11.glVertex2d((double)(d + Math.sin(d4) * d3), (double)(d2 + Math.cos(d4) * d3));
        }
        GL11.glEnd();
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
        GL11.glDisable((int)2848);
        GlStateManager.func_179121_F();
        GlStateManager.func_179117_G();
    }

    public static void color(int n, float f) {
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)f2, (float)f3, (float)f4, (float)f);
    }

    public static void drawRoundedRect(float f, float f2, float f3, float f4, float f5, int n, boolean bl) {
        double d;
        float f6 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f7 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f8 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f9 = (float)(n & 0xFF) / 255.0f;
        float f10 = 0.0f;
        if (f > f3) {
            f10 = f;
            f = f3;
            f3 = f10;
        }
        if (f2 > f4) {
            f10 = f2;
            f2 = f4;
            f4 = f10;
        }
        double d2 = f + f5;
        double d3 = f2 + f5;
        double d4 = f3 - f5;
        double d5 = f4 - f5;
        if (bl) {
            GL11.glPushMatrix();
        }
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)1.0f);
        GL11.glColor4f((float)f7, (float)f8, (float)f9, (float)f6);
        GL11.glBegin((int)9);
        double d6 = Math.PI / 180;
        for (d = 0.0; d <= 90.0; d += 1.0) {
            GL11.glVertex2d((double)(d4 + Math.sin(d * d6) * (double)f5), (double)(d5 + Math.cos(d * d6) * (double)f5));
        }
        for (d = 90.0; d <= 180.0; d += 1.0) {
            GL11.glVertex2d((double)(d4 + Math.sin(d * d6) * (double)f5), (double)(d3 + Math.cos(d * d6) * (double)f5));
        }
        for (d = 180.0; d <= 270.0; d += 1.0) {
            GL11.glVertex2d((double)(d2 + Math.sin(d * d6) * (double)f5), (double)(d3 + Math.cos(d * d6) * (double)f5));
        }
        for (d = 270.0; d <= 360.0; d += 1.0) {
            GL11.glVertex2d((double)(d2 + Math.sin(d * d6) * (double)f5), (double)(d5 + Math.cos(d * d6) * (double)f5));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        if (bl) {
            GL11.glPopMatrix();
        }
    }

    public static void drawSmoothRect(double d, double d2, double d3, double d4, int n) {
        GlStateManager.func_179117_G();
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2848);
        RenderUtils.drawRect(d, d2, d3, d4, n);
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        RenderUtils.drawRect(d * 2.0 - 1.0, d2 * 2.0, d * 2.0, d4 * 2.0 - 1.0, n);
        RenderUtils.drawRect(d * 2.0, d2 * 2.0 - 1.0, d3 * 2.0, d2 * 2.0, n);
        RenderUtils.drawRect(d3 * 2.0, d2 * 2.0, d3 * 2.0 + 1.0, d4 * 2.0 - 1.0, n);
        GL11.glDisable((int)3042);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
    }

    public static void drawGradientSideways(double d, double d2, double d3, double d4, int n, int n2) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        float f5 = (float)(n2 >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(n2 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glVertex2d((double)d, (double)d4);
        GL11.glColor4f((float)f6, (float)f7, (float)f8, (float)f5);
        GL11.glVertex2d((double)d3, (double)d4);
        GL11.glVertex2d((double)d3, (double)d2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static void newDrawRect(float f, float f2, float f3, float f4, int n) {
        float f5;
        if (f < f3) {
            f5 = f;
            f = f3;
            f3 = f5;
        }
        if (f2 < f4) {
            f5 = f2;
            f2 = f4;
            f4 = f5;
        }
        f5 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(n & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferBuilder = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)f6, (float)f7, (float)f8, (float)f5);
        bufferBuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferBuilder.func_181662_b((double)f, (double)f4, 0.0).func_181675_d();
        bufferBuilder.func_181662_b((double)f3, (double)f4, 0.0).func_181675_d();
        bufferBuilder.func_181662_b((double)f3, (double)f2, 0.0).func_181675_d();
        bufferBuilder.func_181662_b((double)f, (double)f2, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void gradientSideways(double d, double d2, double d3, double d4, boolean bl, Color color, Color color2) {
        RenderUtils.start();
        GL11.glShadeModel((int)7425);
        GlStateManager.func_179118_c();
        if (color != null) {
            RenderUtils.color(color);
        }
        RenderUtils.begin(bl ? 6 : 1);
        RenderUtils.vertex(d, d2);
        RenderUtils.vertex(d, d2 + d4);
        if (color2 != null) {
            RenderUtils.color(color2);
        }
        RenderUtils.vertex(d + d3, d2 + d4);
        RenderUtils.vertex(d + d3, d2);
        RenderUtils.end();
        GlStateManager.func_179141_d();
        GL11.glShadeModel((int)7424);
        RenderUtils.stop();
    }

    public static void resetCaps() {
        glCapMap.forEach(RenderUtils::setGlState);
    }

    public static void drawRoundRect(float f, float f2, float f3, float f4, int n) {
        RenderUtils.drawRect(f + 1.0f, f2, f3, f4, n);
        RenderUtils.drawRect((double)f, (double)f2 + 0.75, (double)f, (double)f4, n);
        RenderUtils.drawRect((double)f, (double)(f2 + 1.0f), (double)(f + 1.0f), (double)f4 - 0.5, n);
        RenderUtils.drawRect((double)f - 0.75, (double)f2 + 1.5, (double)f, (double)f4 - 1.25, n);
    }

    public static void drawCircle2(float f, float f2, float f3, int n) {
        RenderUtils.glColor(n);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double)((double)f + Math.sin((double)i * Math.PI / 180.0) * (double)f3), (double)((double)f2 + Math.cos((double)i * Math.PI / 180.0) * (double)f3));
        }
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void renderNameTag(String string, double d, double d2, double d3) {
        IRenderManager iRenderManager = mc.getRenderManager();
        GL11.glPushMatrix();
        GL11.glTranslated((double)(d - iRenderManager.getRenderPosX()), (double)(d2 - iRenderManager.getRenderPosY()), (double)(d3 - iRenderManager.getRenderPosZ()));
        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)(-mc.getRenderManager().getPlayerViewY()), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)mc.getRenderManager().getPlayerViewX(), (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glScalef((float)-0.05f, (float)-0.05f, (float)0.05f);
        RenderUtils.setGlCap(2896, false);
        RenderUtils.setGlCap(2929, false);
        RenderUtils.setGlCap(3042, true);
        GL11.glBlendFunc((int)770, (int)771);
        int n = Fonts.roboto35.getStringWidth(string) / 2;
        RenderUtils.drawRect(-n - 1, -1, n + 1, Fonts.roboto35.getFontHeight(), Integer.MIN_VALUE);
        Fonts.roboto35.drawString(string, -n, 1.5f, Color.WHITE.getRGB(), true);
        RenderUtils.resetCaps();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
    }

    public static void drawRect(float f, float f2, float f3, float f4, Color color) {
        RenderUtils.drawRect(f, f2, f3, f4, color.getRGB());
    }

    public static float animate(float f, float f2, float f3) {
        boolean bl;
        boolean bl2 = bl = f > f2;
        if (f3 < 0.0f) {
            f3 = 0.0f;
        } else if (f3 > 1.0f) {
            f3 = 1.0f;
        }
        float f4 = Math.abs(f2 - f);
        float f5 = f4 * f3;
        f2 = bl ? (f2 += f5) : (f2 -= f5);
        return f2;
    }

    public static void disableGlCap(int n) {
        RenderUtils.setGlCap(n, true);
    }

    public static void drawSelectionBoundingBox(IAxisAlignedBB iAxisAlignedBB) {
        ITessellator iTessellator = classProvider.getTessellatorInstance();
        IWorldRenderer iWorldRenderer = iTessellator.getWorldRenderer();
        iWorldRenderer.begin(3, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMinZ()).endVertex();
        iTessellator.draw();
    }

    public static void quickRenderCircle(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7;
        if (d3 > d4) {
            d7 = d4;
            d4 = d3;
            d3 = d7;
        }
        GL11.glBegin((int)6);
        GL11.glVertex2d((double)d, (double)d2);
        for (d7 = d4; d7 >= d3; d7 -= 4.0) {
            double d8 = Math.cos(d7 * Math.PI / 180.0) * d5;
            double d9 = Math.sin(d7 * Math.PI / 180.0) * d6;
            GL11.glVertex2d((double)(d + d8), (double)(d2 + d9));
        }
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glEnd();
    }

    public static void setGlCap(int n, boolean bl) {
        glCapMap.put(n, GL11.glGetBoolean((int)n));
        RenderUtils.setGlState(n, bl);
    }

    public static void drawBlockBox2(WBlockPos wBlockPos, Color color, boolean bl, boolean bl2, float f) {
        IRenderManager iRenderManager = mc.getRenderManager();
        ITimer iTimer = mc.getTimer();
        double d = (double)wBlockPos.getX() - iRenderManager.getRenderPosX();
        double d2 = (double)wBlockPos.getY() - iRenderManager.getRenderPosY();
        double d3 = (double)wBlockPos.getZ() - iRenderManager.getRenderPosZ();
        IAxisAlignedBB iAxisAlignedBB = classProvider.createAxisAlignedBB(d, d2, d3, d + 1.0, d2 + 1.0, d3 + 1.0);
        IBlock iBlock = BlockUtils.getBlock(wBlockPos);
        if (iBlock != null) {
            IEntityPlayerSP iEntityPlayerSP = mc.getThePlayer();
            double d4 = iEntityPlayerSP.getLastTickPosX() + (iEntityPlayerSP.getPosX() - iEntityPlayerSP.getLastTickPosX()) * (double)iTimer.getRenderPartialTicks();
            double d5 = iEntityPlayerSP.getLastTickPosY() + (iEntityPlayerSP.getPosY() - iEntityPlayerSP.getLastTickPosY()) * (double)iTimer.getRenderPartialTicks();
            double d6 = iEntityPlayerSP.getLastTickPosZ() + (iEntityPlayerSP.getPosZ() - iEntityPlayerSP.getLastTickPosZ()) * (double)iTimer.getRenderPartialTicks();
            iAxisAlignedBB = iBlock.getSelectedBoundingBox(mc.getTheWorld(), mc.getTheWorld().getBlockState(wBlockPos), wBlockPos).expand(0.002f, 0.002f, 0.002f).offset(-d4, -d5, -d6);
        }
        GL11.glBlendFunc((int)770, (int)771);
        RenderUtils.enableGlCap(3042);
        RenderUtils.disableGlCap(3553, 2929);
        GL11.glDepthMask((boolean)false);
        if (bl2) {
            RenderUtils.glColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() != 255 ? color.getAlpha() : (bl ? 26 : 35));
            RenderUtils.drawFilledBox(iAxisAlignedBB);
        }
        if (bl) {
            GL11.glLineWidth((float)f);
            RenderUtils.enableGlCap(2848);
            RenderUtils.glColor(color);
            RenderUtils.drawSelectionBoundingBox(iAxisAlignedBB);
        }
        GlStateManager.func_179117_G();
        GL11.glDepthMask((boolean)true);
        RenderUtils.resetCaps();
    }

    public static void drawRect(float f, float f2, float f3, float f4, int n) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        RenderUtils.glColor(n);
        GL11.glBegin((int)7);
        GL11.glVertex2f((float)f3, (float)f2);
        GL11.glVertex2f((float)f, (float)f2);
        GL11.glVertex2f((float)f, (float)f4);
        GL11.glVertex2f((float)f3, (float)f4);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawRoundedRect(float f, float f2, float f3, float f4, float f5, int n) {
        RenderUtils.drawRoundedRect(f, f2, f3, f4, f5, n, true);
    }

    public static void enableGlCap(int n) {
        RenderUtils.setGlCap(n, true);
    }

    public static void vertex(double d, double d2) {
        GL11.glVertex2d((double)d, (double)d2);
    }

    public static void drawRect(int n, int n2, int n3, int n4, int n5) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        RenderUtils.glColor(n5);
        GL11.glBegin((int)7);
        GL11.glVertex2i((int)n3, (int)n2);
        GL11.glVertex2i((int)n, (int)n2);
        GL11.glVertex2i((int)n, (int)n4);
        GL11.glVertex2i((int)n3, (int)n4);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawCircleRect(float f, float f2, float f3, float f4, float f5, int n) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        RenderUtils.glColor(n);
        RenderUtils.quickRenderCircle(f3 - f5, f4 - f5, 0.0, 90.0, f5, f5);
        RenderUtils.quickRenderCircle(f + f5, f4 - f5, 90.0, 180.0, f5, f5);
        RenderUtils.quickRenderCircle(f + f5, f2 + f5, 180.0, 270.0, f5, f5);
        RenderUtils.quickRenderCircle(f3 - f5, f2 + f5, 270.0, 360.0, f5, f5);
        RenderUtils.quickDrawRect(f + f5, f2 + f5, f3 - f5, f4 - f5);
        RenderUtils.quickDrawRect(f, f2 + f5, f + f5, f4 - f5);
        RenderUtils.quickDrawRect(f3 - f5, f2 + f5, f3, f4 - f5);
        RenderUtils.quickDrawRect(f + f5, f2, f3 - f5, f2 + f5);
        RenderUtils.quickDrawRect(f + f5, f4 - f5, f3 - f5, f4);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawRoundedRect2(float f, float f2, float f3, float f4, float f5, int n, int n2) {
        float f6 = (float)(n2 >> 24 & 0xFF) / 255.0f;
        float f7 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f8 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f9 = (float)(n2 & 0xFF) / 255.0f;
        if (f < f3) {
            float f10 = f + f3;
            f3 = f;
            f = f10 - f3;
        }
        if (f2 < f4) {
            float f11 = f2 + f4;
            f4 = f2;
            f2 = f11 - f4;
        }
        float[][] fArrayArray = new float[][]{{f3 + f5, f2 - f5, 270.0f}, {f - f5, f2 - f5, 360.0f}, {f - f5, f4 + f5, 90.0f}, {f3 + f5, f4 + f5, 180.0f}};
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179092_a((int)516, (float)0.003921569f);
        GlStateManager.func_179131_c((float)f7, (float)f8, (float)f9, (float)f6);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferBuilder = tessellator.func_178180_c();
        bufferBuilder.func_181668_a(9, DefaultVertexFormats.field_181705_e);
        for (float[] fArray : fArrayArray) {
            for (int i = 0; i <= n; ++i) {
                double d = Math.PI * (double)(fArray[2] + (float)i * 90.0f / (float)n) / 180.0;
                bufferBuilder.func_181662_b((double)fArray[0] + Math.sin(d) * (double)f5, (double)fArray[1] + Math.cos(d) * (double)f5, 0.0).func_181675_d();
            }
        }
        tessellator.func_78381_a();
        GlStateManager.func_179084_k();
        GlStateManager.func_179098_w();
    }

    public static float smoothAnimation(float f, float f2, float f3, float f4) {
        return RenderUtils.getAnimationState(f, f2, Math.max(10.0f, Math.abs(f - f2) * f3) * f4);
    }

    public static void drawBlockBox(WBlockPos wBlockPos, Color color, boolean bl) {
        IRenderManager iRenderManager = mc.getRenderManager();
        ITimer iTimer = mc.getTimer();
        double d = (double)wBlockPos.getX() - iRenderManager.getRenderPosX();
        double d2 = (double)wBlockPos.getY() - iRenderManager.getRenderPosY();
        double d3 = (double)wBlockPos.getZ() - iRenderManager.getRenderPosZ();
        IAxisAlignedBB iAxisAlignedBB = classProvider.createAxisAlignedBB(d, d2, d3, d + 1.0, d2 + 1.0, d3 + 1.0);
        IBlock iBlock = BlockUtils.getBlock(wBlockPos);
        if (iBlock != null) {
            IEntityPlayerSP iEntityPlayerSP = mc.getThePlayer();
            double d4 = iEntityPlayerSP.getLastTickPosX() + (iEntityPlayerSP.getPosX() - iEntityPlayerSP.getLastTickPosX()) * (double)iTimer.getRenderPartialTicks();
            double d5 = iEntityPlayerSP.getLastTickPosY() + (iEntityPlayerSP.getPosY() - iEntityPlayerSP.getLastTickPosY()) * (double)iTimer.getRenderPartialTicks();
            double d6 = iEntityPlayerSP.getLastTickPosZ() + (iEntityPlayerSP.getPosZ() - iEntityPlayerSP.getLastTickPosZ()) * (double)iTimer.getRenderPartialTicks();
            iAxisAlignedBB = iBlock.getSelectedBoundingBox(mc.getTheWorld(), mc.getTheWorld().getBlockState(wBlockPos), wBlockPos).expand(0.002f, 0.002f, 0.002f).offset(-d4, -d5, -d6);
        }
        GL11.glBlendFunc((int)770, (int)771);
        RenderUtils.enableGlCap(3042);
        RenderUtils.disableGlCap(3553, 2929);
        GL11.glDepthMask((boolean)false);
        RenderUtils.glColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() != 255 ? color.getAlpha() : (bl ? 26 : 35));
        RenderUtils.drawFilledBox(iAxisAlignedBB);
        if (bl) {
            GL11.glLineWidth((float)1.0f);
            RenderUtils.enableGlCap(2848);
            RenderUtils.glColor(color);
            RenderUtils.drawSelectionBoundingBox(iAxisAlignedBB);
        }
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDepthMask((boolean)true);
        RenderUtils.resetCaps();
    }

    public static void bindTexture(int n) {
        GL11.glBindTexture((int)3553, (int)n);
    }

    public static int loadGlTexture(BufferedImage bufferedImage) {
        int n = GL11.glGenTextures();
        GL11.glBindTexture((int)3553, (int)n);
        GL11.glTexParameteri((int)3553, (int)10242, (int)10497);
        GL11.glTexParameteri((int)3553, (int)10243, (int)10497);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        GL11.glTexImage2D((int)3553, (int)0, (int)6408, (int)bufferedImage.getWidth(), (int)bufferedImage.getHeight(), (int)0, (int)6408, (int)5121, (ByteBuffer)ImageUtils.readImageToBuffer(bufferedImage));
        GL11.glBindTexture((int)3553, (int)0);
        return n;
    }

    public static void drawLine(double d, double d2, double d3, double d4, float f) {
        GL11.glDisable((int)3553);
        GL11.glLineWidth((float)f);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glVertex2d((double)d3, (double)d4);
        GL11.glEnd();
        GL11.glEnable((int)3553);
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.field_147621_c != mc.getDisplayWidth() || framebuffer.field_147618_d != mc.getDisplayHeight()) {
            if (framebuffer != null) {
                framebuffer.func_147608_a();
            }
            return new Framebuffer(mc.getDisplayWidth(), mc.getDisplayHeight(), true);
        }
        return framebuffer;
    }

    public static boolean isHovering(int n, int n2, float f, float f2, float f3, float f4) {
        return (float)n > f && (float)n < f3 && (float)n2 > f2 && (float)n2 < f4;
    }

    public static void drawCircle(IEntity iEntity, double d, int n, boolean bl) {
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glEnable((int)2832);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glHint((int)3153, (int)4354);
        GL11.glDepthMask((boolean)false);
        GlStateManager.func_179092_a((int)516, (float)0.0f);
        if (bl) {
            GL11.glShadeModel((int)7425);
        }
        GlStateManager.func_179129_p();
        GL11.glBegin((int)5);
        double d2 = iEntity.getLastTickPosX() + (iEntity.getPosX() - iEntity.getLastTickPosX()) * (double)mc.getTimer().getRenderPartialTicks() - mc.getRenderManager().getRenderPosX();
        double d3 = iEntity.getLastTickPosY() + (iEntity.getPosY() - iEntity.getLastTickPosY()) * (double)mc.getTimer().getRenderPartialTicks() - mc.getRenderManager().getRenderPosY() + Math.sin((double)System.currentTimeMillis() / 200.0) + 1.0;
        double d4 = iEntity.getLastTickPosZ() + (iEntity.getPosZ() - iEntity.getLastTickPosZ()) * (double)mc.getTimer().getRenderPartialTicks() - mc.getRenderManager().getRenderPosZ();
        float f = 0.0f;
        while ((double)f < Math.PI * 2) {
            double d5 = d2 + d * Math.cos(f);
            double d6 = d4 + d * Math.sin(f);
            HUD hUD = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
            Color color = RenderUtils.getGradientOffset(new Color((Integer)CustomColor.r.get(), (Integer)CustomColor.g.get(), (Integer)CustomColor.b.get()), new Color((Integer)CustomColor.r2.get(), (Integer)CustomColor.g2.get(), (Integer)CustomColor.b2.get()), f);
            if (bl) {
                GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)0.0f);
                GL11.glVertex3d((double)d5, (double)(d3 - Math.cos((double)System.currentTimeMillis() / 200.0) / 2.0), (double)d6);
                GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)0.85f);
            }
            GL11.glVertex3d((double)d5, (double)d3, (double)d6);
            f = (float)((double)f + 0.09817477042468103);
        }
        GL11.glEnd();
        if (bl) {
            GL11.glShadeModel((int)7424);
        }
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        GlStateManager.func_179092_a((int)516, (float)0.1f);
        GlStateManager.func_179089_o();
        GL11.glDisable((int)2848);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2832);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
        GL11.glColor3f((float)255.0f, (float)255.0f, (float)255.0f);
    }

    public static void glColor1(int n) {
    }

    public static void makeScissorBox(float f, float f2, float f3, float f4) {
        IScaledResolution iScaledResolution = classProvider.createScaledResolution(mc);
        int n = iScaledResolution.getScaleFactor();
        GL11.glScissor((int)((int)(f * (float)n)), (int)((int)(((float)iScaledResolution.getScaledHeight() - f4) * (float)n)), (int)((int)((f3 - f) * (float)n)), (int)((int)((f4 - f2) * (float)n)));
    }

    public static int getRainbow(int n, int n2, float f, float f2) {
        float f3 = (System.currentTimeMillis() + (long)n2 * (long)n) % 2000L;
        return Color.getHSBColor(f3 /= 2000.0f, f2, f).getRGB();
    }

    public static void drawPlatform(IEntity iEntity, Color color) {
        IRenderManager iRenderManager = mc.getRenderManager();
        ITimer iTimer = mc.getTimer();
        double d = iEntity.getLastTickPosX() + (iEntity.getPosX() - iEntity.getLastTickPosX()) * (double)iTimer.getRenderPartialTicks() - iRenderManager.getRenderPosX();
        double d2 = iEntity.getLastTickPosY() + (iEntity.getPosY() - iEntity.getLastTickPosY()) * (double)iTimer.getRenderPartialTicks() - iRenderManager.getRenderPosY();
        double d3 = iEntity.getLastTickPosZ() + (iEntity.getPosZ() - iEntity.getLastTickPosZ()) * (double)iTimer.getRenderPartialTicks() - iRenderManager.getRenderPosZ();
        IAxisAlignedBB iAxisAlignedBB = iEntity.getEntityBoundingBox().offset(-iEntity.getPosX(), -iEntity.getPosY(), -iEntity.getPosZ()).offset(d, d2, d3);
        RenderUtils.drawAxisAlignedBB(classProvider.createAxisAlignedBB(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMaxY() + 0.2, iAxisAlignedBB.getMinZ(), iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMaxY() + 0.26, iAxisAlignedBB.getMaxZ()), color);
    }

    public static void drawCircleESP(IEntity iEntity, double d, int n, boolean bl) {
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glEnable((int)2832);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glHint((int)3153, (int)4354);
        GL11.glDepthMask((boolean)false);
        GlStateManager.func_179092_a((int)516, (float)0.0f);
        if (bl) {
            GL11.glShadeModel((int)7425);
        }
        GlStateManager.func_179129_p();
        GL11.glBegin((int)5);
        double d2 = iEntity.getLastTickPosX() + (iEntity.getPosX() - iEntity.getLastTickPosX()) * (double)mc.getTimer().getRenderPartialTicks() - mc.getRenderManager().getRenderPosX();
        double d3 = iEntity.getLastTickPosY() + (iEntity.getPosY() - iEntity.getLastTickPosY()) * (double)mc.getTimer().getRenderPartialTicks() - mc.getRenderManager().getRenderPosY() + Math.sin((double)System.currentTimeMillis() / 200.0) + 1.0;
        double d4 = iEntity.getLastTickPosZ() + (iEntity.getPosZ() - iEntity.getLastTickPosZ()) * (double)mc.getTimer().getRenderPartialTicks() - mc.getRenderManager().getRenderPosZ();
        float f = 0.0f;
        while ((double)f < Math.PI * 2) {
            double d5 = d2 + d * Math.cos(f);
            double d6 = d4 + d * Math.sin(f);
            Color color = ColorUtils.INSTANCE.rainbow();
            if (bl) {
                GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)0.0f);
                GL11.glVertex3d((double)d5, (double)(d3 - Math.cos((double)System.currentTimeMillis() / 200.0) / 2.0), (double)d6);
                GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)0.85f);
            }
            GL11.glVertex3d((double)d5, (double)d3, (double)d6);
            f = (float)((double)f + 0.09817477042468103);
        }
        GL11.glEnd();
        if (bl) {
            GL11.glShadeModel((int)7424);
        }
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        GlStateManager.func_179092_a((int)516, (float)0.1f);
        GlStateManager.func_179089_o();
        GL11.glDisable((int)2848);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2832);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
        GL11.glColor3f((float)255.0f, (float)255.0f, (float)255.0f);
    }

    public static void drawShadow(float f, float f2, float f3, float f4) {
        RenderUtils.drawTexturedRect(f - 9.0f, f2 - 9.0f, 9.0f, 9.0f, "paneltopleft");
        RenderUtils.drawTexturedRect(f - 9.0f, f2 + f4, 9.0f, 9.0f, "panelbottomleft");
        RenderUtils.drawTexturedRect(f + f3, f2 + f4, 9.0f, 9.0f, "panelbottomright");
        RenderUtils.drawTexturedRect(f + f3, f2 - 9.0f, 9.0f, 9.0f, "paneltopright");
        RenderUtils.drawTexturedRect(f - 9.0f, f2, 9.0f, f4, "panelleft");
        RenderUtils.drawTexturedRect(f + f3, f2, 9.0f, f4, "panelright");
        RenderUtils.drawTexturedRect(f, f2 - 9.0f, f3, 9.0f, "paneltop");
        RenderUtils.drawTexturedRect(f, f2 + f4, f3, 9.0f, "panelbottom");
    }

    public static double getAnimationState2(double d, double d2, double d3) {
        float f = (float)(0.01 * d3);
        d = d < d2 ? (d + (double)f < d2 ? (d += (double)f) : d2) : (d - (double)f > d2 ? (d -= (double)f) : d2);
        return d;
    }

    public static void drawTexturedRect(float f, float f2, float f3, float f4, String string) {
        boolean bl;
        GL11.glPushMatrix();
        boolean bl2 = GL11.glIsEnabled((int)3042);
        boolean bl3 = bl = !GL11.glIsEnabled((int)3008);
        if (!bl2) {
            GL11.glEnable((int)3042);
        }
        if (!bl) {
            GL11.glDisable((int)3008);
        }
        minecraft.func_110434_K().func_110577_a(new ResourceLocation("shaders/" + string + ".png"));
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawModalRectWithCustomSizedTexture(f, f2, 0.0f, 0.0f, f3, f4, f3, f4);
        if (!bl2) {
            GL11.glDisable((int)3042);
        }
        if (!bl) {
            GL11.glEnable((int)3008);
        }
        GL11.glPopMatrix();
    }

    public static Color skyRainbow(int n, float f, float f2) {
        double d;
        double d2 = Math.ceil(System.currentTimeMillis() + (long)(n * 109)) / 5.0;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(d2 / 360.0)) : (float)((d2 %= 360.0) / 360.0), f, f2);
    }

    public static void color(int n) {
        RenderUtils.color(n, (float)(n >> 24 & 0xFF) / 255.0f);
    }

    public static void drawAxisAlignedBB(IAxisAlignedBB iAxisAlignedBB, Color color) {
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        RenderUtils.glColor(color);
        RenderUtils.drawFilledBox(iAxisAlignedBB);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static Color skyRainbow(int n, float f, float f2, double d) {
        double d2;
        double d3 = Math.ceil((double)System.currentTimeMillis() / d + (double)((long)n * 109L)) / 5.0;
        return Color.getHSBColor((double)((float)(d2 / 360.0)) < 0.5 ? -((float)(d3 / 360.0)) : (float)((d3 %= 360.0) / 360.0), f2, f);
    }

    public static void endSmooth() {
        RenderUtils.disable(2832);
        RenderUtils.disable(2848);
        RenderUtils.disable(2881);
    }

    public static void drawRoundedRect3(float f, float f2, float f3, float f4, float f5, int n, int n2) {
        float f6 = (float)(n2 >> 24 & 0xFF) / 255.0f;
        float f7 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f8 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f9 = (float)(n2 & 0xFF) / 255.0f;
        if (f < f3) {
            float f10 = f + f3;
            f3 = f;
            f = f10 - f3;
        }
        if (f2 < f4) {
            float f11 = f2 + f4;
            f4 = f2;
            f2 = f11 - f4;
        }
        float[][] fArrayArray = new float[][]{{f3 + f5, f2 - f5, 270.0f}, {f - f5, f2 - f5, 360.0f}, {f - f5, f4 + f5, 90.0f}, {f3 + f5, f4 + f5, 180.0f}};
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179092_a((int)516, (float)0.003921569f);
        GlStateManager.func_179131_c((float)f7, (float)f8, (float)f9, (float)f6);
        ITessellator iTessellator = classProvider.getTessellatorInstance();
        IWorldRenderer iWorldRenderer = iTessellator.getWorldRenderer();
        iWorldRenderer.begin(9, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION_TEX));
        for (float[] fArray : fArrayArray) {
            for (int i = 0; i <= n; ++i) {
                double d = Math.PI * (double)(fArray[2] + (float)i * 90.0f / (float)n) / 180.0;
                iWorldRenderer.pos((double)fArray[0] + Math.sin(d) * (double)f5, (double)fArray[1] + Math.cos(d) * (double)f5, 0.0).endVertex();
            }
        }
        iTessellator.draw();
        GlStateManager.func_179084_k();
        GlStateManager.func_179098_w();
    }

    public static void draw2D(IEntityLivingBase iEntityLivingBase, double d, double d2, double d3, int n, int n2) {
        GL11.glPushMatrix();
        GL11.glTranslated((double)d, (double)d2, (double)d3);
        GL11.glRotated((double)(-mc.getRenderManager().getPlayerViewY()), (double)0.0, (double)1.0, (double)0.0);
        GL11.glScaled((double)-0.1, (double)-0.1, (double)0.1);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)true);
        RenderUtils.glColor(n);
        GL11.glCallList((int)DISPLAY_LISTS_2D[0]);
        RenderUtils.glColor(n2);
        GL11.glCallList((int)DISPLAY_LISTS_2D[1]);
        GL11.glTranslated((double)0.0, (double)(21.0 + -(iEntityLivingBase.getEntityBoundingBox().getMaxY() - iEntityLivingBase.getEntityBoundingBox().getMinY()) * 12.0), (double)0.0);
        RenderUtils.glColor(n);
        GL11.glCallList((int)DISPLAY_LISTS_2D[2]);
        RenderUtils.glColor(n2);
        GL11.glCallList((int)DISPLAY_LISTS_2D[3]);
        GL11.glEnable((int)2929);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void begin(int n) {
        GL11.glBegin((int)n);
    }

    public static void drawShadowWithCustomAlpha(double d, double d2, double d3, double d4, double d5) {
        RenderUtils.drawShadowWithCustomAlpha((float)d, (float)d2, (float)d3, (float)d4, (float)d5);
    }

    public static int Astolfo(int n, float f, float f2, int n2, int n3, float f3) {
        double d;
        double d2 = Math.ceil(System.currentTimeMillis() + (long)(n * n2)) / (double)n3;
        return Color.getHSBColor((double)((float)(d / (double)f3)) < 0.5 ? -((float)(d2 / (double)f3)) : (float)((d2 %= (double)f3) / (double)f3), f2, f).getRGB();
    }

    public static void outlineRect(double d, double d2, double d3, double d4, double d5, int n, int n2) {
        RenderUtils.drawRect(d + d5, d2 + d5, d3 - d5, d4 - d5, n);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawRect(d + d5, d2, d3 - d5, d2 + d5, n2);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawRect(d, d2, d + d5, d4, n2);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawRect(d3 - d5, d2, d3, d4, n2);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.drawRect(d + d5, d4 - d5, d3 - d5, d4, n2);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void setGlState(int n, boolean bl) {
        if (bl) {
            GL11.glEnable((int)n);
        } else {
            GL11.glDisable((int)n);
        }
    }

    public static void startDrawing() {
        GL11.glEnable((int)3042);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        Minecraft.func_71410_x().field_71460_t.func_78479_a(Minecraft.func_71410_x().field_71428_T.field_194147_b, 0);
    }

    public static int getRainbowOpaque(int n, float f, float f2, int n2) {
        float f3 = (float)((System.currentTimeMillis() + (long)n2) % (long)(n * 1000)) / (float)(n * 1000);
        int n3 = Color.HSBtoRGB(f3, f, f2);
        return n3;
    }

    public static void quickDrawRect(float f, float f2, float f3, float f4, int n) {
        RenderUtils.glColor(n);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)f3, (double)f2);
        GL11.glVertex2d((double)f, (double)f2);
        GL11.glVertex2d((double)f, (double)f4);
        GL11.glVertex2d((double)f3, (double)f4);
        GL11.glEnd();
    }

    public static void doGlScissor(int n, int n2, float f, float f2) {
        int n3 = 1;
        float f3 = 2.0f;
        while ((float)n3 < f3 && mc.getDisplayWidth() / (n3 + 1) >= 320 && mc.getDisplayHeight() / (n3 + 1) >= 240) {
            ++n3;
        }
        GL11.glScissor((int)(n * n3), (int)((int)((float)mc.getDisplayHeight() - ((float)n2 + f2) * (float)n3)), (int)((int)(f * (float)n3)), (int)((int)(f2 * (float)n3)));
    }

    public static void drawCircle(float f, float f2, float f3, int n, int n2) {
        classProvider.getGlStateManager().enableBlend();
        classProvider.getGlStateManager().disableTexture2D();
        classProvider.getGlStateManager().tryBlendFuncSeparate(770, 771, 1, 0);
        RenderUtils.glColor(Color.WHITE);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)2.0f);
        GL11.glBegin((int)3);
        for (float f4 = (float)n2; f4 >= (float)n; f4 -= 4.0f) {
            GL11.glVertex2f((float)((float)((double)f + Math.cos((double)f4 * Math.PI / 180.0) * (double)(f3 * 1.001f))), (float)((float)((double)f2 + Math.sin((double)f4 * Math.PI / 180.0) * (double)(f3 * 1.001f))));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        classProvider.getGlStateManager().enableTexture2D();
        classProvider.getGlStateManager().disableBlend();
    }

    public static void glColor(int n, int n2, int n3, int n4) {
        GL11.glColor4f((float)((float)n / 255.0f), (float)((float)n2 / 255.0f), (float)((float)n3 / 255.0f), (float)((float)n4 / 255.0f));
    }

    public static void drawShadowWithCustomAlpha(float f, float f2, float f3, float f4, float f5) {
        RenderUtils.drawTexturedRectWithCustomAlpha(f - 9.0f, f2 - 9.0f, 9.0f, 9.0f, "paneltopleft", f5);
        RenderUtils.drawTexturedRectWithCustomAlpha(f - 9.0f, f2 + f4, 9.0f, 9.0f, "panelbottomleft", f5);
        RenderUtils.drawTexturedRectWithCustomAlpha(f + f3, f2 + f4, 9.0f, 9.0f, "panelbottomright", f5);
        RenderUtils.drawTexturedRectWithCustomAlpha(f + f3, f2 - 9.0f, 9.0f, 9.0f, "paneltopright", f5);
        RenderUtils.drawTexturedRectWithCustomAlpha(f - 9.0f, f2, 9.0f, f4, "panelleft", f5);
        RenderUtils.drawTexturedRectWithCustomAlpha(f + f3, f2, 9.0f, f4, "panelright", f5);
        RenderUtils.drawTexturedRectWithCustomAlpha(f, f2 - 9.0f, f3, 9.0f, "paneltop", f5);
        RenderUtils.drawTexturedRectWithCustomAlpha(f, f2 + f4, f3, 9.0f, "panelbottom", f5);
    }

    public static void glRestoreBlend(boolean bl) {
        if (!bl) {
            GL11.glDisable((int)3042);
        }
    }

    public static void drawEntityBox(IEntity iEntity, Color color, boolean bl) {
        IRenderManager iRenderManager = mc.getRenderManager();
        ITimer iTimer = mc.getTimer();
        GL11.glBlendFunc((int)770, (int)771);
        RenderUtils.enableGlCap(3042);
        RenderUtils.disableGlCap(3553, 2929);
        GL11.glDepthMask((boolean)false);
        double d = iEntity.getLastTickPosX() + (iEntity.getPosX() - iEntity.getLastTickPosX()) * (double)iTimer.getRenderPartialTicks() - iRenderManager.getRenderPosX();
        double d2 = iEntity.getLastTickPosY() + (iEntity.getPosY() - iEntity.getLastTickPosY()) * (double)iTimer.getRenderPartialTicks() - iRenderManager.getRenderPosY();
        double d3 = iEntity.getLastTickPosZ() + (iEntity.getPosZ() - iEntity.getLastTickPosZ()) * (double)iTimer.getRenderPartialTicks() - iRenderManager.getRenderPosZ();
        IAxisAlignedBB iAxisAlignedBB = iEntity.getEntityBoundingBox();
        IAxisAlignedBB iAxisAlignedBB2 = classProvider.createAxisAlignedBB(iAxisAlignedBB.getMinX() - iEntity.getPosX() + d - 0.05, iAxisAlignedBB.getMinY() - iEntity.getPosY() + d2, iAxisAlignedBB.getMinZ() - iEntity.getPosZ() + d3 - 0.05, iAxisAlignedBB.getMaxX() - iEntity.getPosX() + d + 0.05, iAxisAlignedBB.getMaxY() - iEntity.getPosY() + d2 + 0.15, iAxisAlignedBB.getMaxZ() - iEntity.getPosZ() + d3 + 0.05);
        if (bl) {
            GL11.glLineWidth((float)1.0f);
            RenderUtils.enableGlCap(2848);
            RenderUtils.glColor(color.getRed(), color.getGreen(), color.getBlue(), 95);
            RenderUtils.drawSelectionBoundingBox(iAxisAlignedBB2);
        }
        RenderUtils.glColor(color.getRed(), color.getGreen(), color.getBlue(), bl ? 26 : 35);
        RenderUtils.drawFilledBox(iAxisAlignedBB2);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDepthMask((boolean)true);
        RenderUtils.resetCaps();
    }

    public static void start() {
        RenderUtils.enable(3042);
        GL11.glBlendFunc((int)770, (int)771);
        RenderUtils.disable(3553);
        RenderUtils.disable(2884);
        GlStateManager.func_179118_c();
        GlStateManager.func_179097_i();
    }

    public static void drawBorderedRect(float f, float f2, float f3, float f4, float f5, int n, int n2) {
        RenderUtils.drawRect(f, f2, f3, f4, n2);
        RenderUtils.drawBorder(f, f2, f3, f4, f5, n);
    }

    public static void gradientSideways(double d, double d2, double d3, double d4, Color color, Color color2) {
        RenderUtils.gradientSideways(d, d2, d3, d4, true, color, color2);
    }

    public static void drawOutlinedString(String string, int n, int n2, int n3, int n4) {
        mc.getFontRendererObj().drawString(string, (int)((float)n - 1.0f), n2, n4);
        mc.getFontRendererObj().drawString(string, (int)((float)n + 1.0f), n2, n4);
        mc.getFontRendererObj().drawString(string, n, (int)((float)n2 + 1.0f), n4);
        mc.getFontRendererObj().drawString(string, n, (int)((float)n2 - 1.0f), n4);
        mc.getFontRendererObj().drawString(string, n, n2, n3);
    }

    public static void drawImage(IResourceLocation iResourceLocation, int n, int n2, int n3, int n4) {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        GL14.glBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        mc.getTextureManager().bindTexture(iResourceLocation);
        RenderUtils.drawModalRectWithCustomSizedTexture(n, n2, 0.0f, 0.0f, n3, n4, (float)n3, (float)n4);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void drawGradientSidewaysH(double d, double d2, double d3, double d4, int n, int n2) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        RenderUtils.quickDrawGradientSidewaysH(d, d2, d3, d4, n, n2);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static void color(double d, double d2, double d3, double d4) {
        GL11.glColor4d((double)d, (double)d2, (double)d3, (double)d4);
    }

    public static void drawPlatform(double d, Color color, double d2) {
        IRenderManager iRenderManager = mc.getRenderManager();
        double d3 = d - iRenderManager.getRenderPosY();
        RenderUtils.drawAxisAlignedBB(classProvider.createAxisAlignedBB(d2, d3 + 0.02, d2, -d2, d3, -d2), color);
    }

    public static boolean isHovered(float f, float f2, float f3, float f4, int n, int n2) {
        return (float)n >= f && (float)n <= f + f3 && (float)n2 >= f2 && (float)n2 <= f2 + f4;
    }

    public static void drawModalRectWithCustomSizedTexture(int n, int n2, float f, float f2, int n3, int n4, float f3, float f4) {
        float f5 = 1.0f / f3;
        float f6 = 1.0f / f4;
        ITessellator iTessellator = classProvider.getTessellatorInstance();
        IWorldRenderer iWorldRenderer = iTessellator.getWorldRenderer();
        iWorldRenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION_TEX));
        iWorldRenderer.pos(n, n2 + n4, 0.0).tex(f * f5, (f2 + (float)n4) * f6).endVertex();
        iWorldRenderer.pos(n + n3, n2 + n4, 0.0).tex((f + (float)n3) * f5, (f2 + (float)n4) * f6).endVertex();
        iWorldRenderer.pos(n + n3, n2, 0.0).tex((f + (float)n3) * f5, f2 * f6).endVertex();
        iWorldRenderer.pos(n, n2, 0.0).tex(f * f5, f2 * f6).endVertex();
        iTessellator.draw();
    }

    public static void rectangle(double d, double d2, double d3, double d4, int n) {
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
        ITessellator iTessellator = classProvider.getTessellatorInstance();
        IWorldRenderer iWorldRenderer = iTessellator.getWorldRenderer();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)f2, (float)f3, (float)f4, (float)f);
        iWorldRenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        iWorldRenderer.pos(d, d4, 0.0).endVertex();
        iWorldRenderer.pos(d3, d4, 0.0).endVertex();
        iWorldRenderer.pos(d3, d2, 0.0).endVertex();
        iWorldRenderer.pos(d, d2, 0.0).endVertex();
        Tessellator.func_178181_a().func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
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

    public static void drawFilledBox(IAxisAlignedBB iAxisAlignedBB) {
        ITessellator iTessellator = classProvider.getTessellatorInstance();
        IWorldRenderer iWorldRenderer = iTessellator.getWorldRenderer();
        iWorldRenderer.begin(7, classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION));
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMinX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMinZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMaxY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iWorldRenderer.pos(iAxisAlignedBB.getMaxX(), iAxisAlignedBB.getMinY(), iAxisAlignedBB.getMaxZ()).endVertex();
        iTessellator.draw();
    }

    public static void drawRect(double d, double d2, double d3, double d4, int n) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        RenderUtils.glColor(new Color(n));
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)d3, (double)d2);
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glVertex2d((double)d, (double)d4);
        GL11.glVertex2d((double)d3, (double)d4);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void fastRoundedRect(float f, float f2, float f3, float f4, float f5) {
        double d;
        float f6 = 0.0f;
        if (f > f3) {
            f6 = f;
            f = f3;
            f3 = f6;
        }
        if (f2 > f4) {
            f6 = f2;
            f2 = f4;
            f4 = f6;
        }
        double d2 = f + f5;
        double d3 = f2 + f5;
        double d4 = f3 - f5;
        double d5 = f4 - f5;
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)9);
        double d6 = Math.PI / 180;
        for (d = 0.0; d <= 90.0; d += 1.0) {
            GL11.glVertex2d((double)(d4 + Math.sin(d * d6) * (double)f5), (double)(d5 + Math.cos(d * d6) * (double)f5));
        }
        for (d = 90.0; d <= 180.0; d += 1.0) {
            GL11.glVertex2d((double)(d4 + Math.sin(d * d6) * (double)f5), (double)(d3 + Math.cos(d * d6) * (double)f5));
        }
        for (d = 180.0; d <= 270.0; d += 1.0) {
            GL11.glVertex2d((double)(d2 + Math.sin(d * d6) * (double)f5), (double)(d3 + Math.cos(d * d6) * (double)f5));
        }
        for (d = 270.0; d <= 360.0; d += 1.0) {
            GL11.glVertex2d((double)(d2 + Math.sin(d * d6) * (double)f5), (double)(d5 + Math.cos(d * d6) * (double)f5));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
    }
}

