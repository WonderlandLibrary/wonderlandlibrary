/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.WorldRenderer
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.Timer
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Cylinder
 *  org.lwjgl.util.glu.GLU
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.Color.Colors;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.UiUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

@SideOnly(value=Side.CLIENT)
public final class RenderUtils3
extends MinecraftInstance {
    private static int counts = 0;
    private static Frustum frustrum = new Frustum();
    private static final Map<Integer, Boolean> glCapMap = new HashMap<Integer, Boolean>();
    public static int deltaTime;
    private static FloatBuffer modelView;
    private static FloatBuffer projection;
    private static IntBuffer viewport;

    public static void drawWolframEntityESP(EntityLivingBase entity, int rgb, double posX, double posY, double posZ) {
        GL11.glPushMatrix();
        GL11.glTranslated((double)posX, (double)posY, (double)posZ);
        GL11.glRotatef((float)(-entity.field_70177_z), (float)0.0f, (float)1.0f, (float)0.0f);
        RenderUtils3.setColor(rgb);
        UiUtils.enableGL3D(1.0f);
        Cylinder c = new Cylinder();
        GL11.glRotatef((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        c.setDrawStyle(100011);
        c.draw(0.5f, 0.5f, entity.field_70131_O + 0.1f, 18, 1);
        RenderUtils3.disableGL3D();
        GL11.glPopMatrix();
    }

    private static void disableGL3D() {
    }

    private static void setColor(int rgb) {
    }

    public static int Astolfo(int var2) {
        double d;
        double v1 = Math.ceil(System.currentTimeMillis() + (long)(var2 * 109)) / 5.0;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(v1 / 360.0)) : (float)((v1 %= 360.0) / 360.0), 0.5f, 1.0f).getRGB();
    }

    public static void chamsColor(Color color) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
    }

    public static void drawblock(double a, double a2, double a3, int a4, int a5, float a6) {
        float a7 = (float)(a4 >> 24 & 0xFF) / 255.0f;
        float a8 = (float)(a4 >> 16 & 0xFF) / 255.0f;
        float a9 = (float)(a4 >> 8 & 0xFF) / 255.0f;
        float a10 = (float)(a4 & 0xFF) / 255.0f;
        float a11 = (float)(a5 >> 24 & 0xFF) / 255.0f;
        float a12 = (float)(a5 >> 16 & 0xFF) / 255.0f;
        float a13 = (float)(a5 >> 8 & 0xFF) / 255.0f;
        float a14 = (float)(a5 & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4f((float)a8, (float)a9, (float)a10, (float)a7);
        RenderUtils3.drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        GL11.glLineWidth((float)a6);
        GL11.glColor4f((float)a12, (float)a13, (float)a14, (float)a11);
        RenderUtils3.drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawGradientSidewaysV(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        float f4 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(col2 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f4);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
        Gui.func_73734_a((int)0, (int)0, (int)0, (int)0, (int)0);
    }

    public static int width() {
        return new ScaledResolution(Minecraft.func_71410_x()).func_78326_a();
    }

    public static int height() {
        return new ScaledResolution(Minecraft.func_71410_x()).func_78328_b();
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        worldRenderer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(1, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
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

    public static void circle(float x, float y, float radius, int fill) {
        RenderUtils3.arc(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void arc(float x, float y, float start, float end, float radius, int color) {
        RenderUtils3.arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void drawEntityKillAuraESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        RenderUtils3.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glLineWidth((float)lineWdith);
        GL11.glColor4f((float)lineRed, (float)lineGreen, (float)lineBlue, (float)lineAlpha);
        RenderUtils3.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
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
        float var11 = (float)(color >> 24 & 0xFF) / 255.0f;
        float var12 = (float)(color >> 16 & 0xFF) / 255.0f;
        float var13 = (float)(color >> 8 & 0xFF) / 255.0f;
        float var14 = (float)(color & 0xFF) / 255.0f;
        Tessellator var15 = Tessellator.func_178181_a();
        WorldRenderer var16 = var15.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)var12, (float)var13, (float)var14, (float)var11);
        if (var11 > 0.5f) {
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

    public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
        R2DUtils.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        R2DUtils.drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
        R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
        R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
        R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
        R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
        R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
        R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
        R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
        R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        R2DUtils.disableGL2D();
        Gui.func_73734_a((int)0, (int)0, (int)0, (int)0, (int)0);
    }

    public static void updateView() {
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)projection);
        GL11.glGetInteger((int)2978, (IntBuffer)viewport);
    }

    public static Vec3 WorldToScreen(Vec3 position) {
        FloatBuffer screenPositions = BufferUtils.createFloatBuffer((int)3);
        boolean result = GLU.gluProject((float)((float)position.field_72450_a), (float)((float)position.field_72448_b), (float)((float)position.field_72449_c), (FloatBuffer)modelView, (FloatBuffer)projection, (IntBuffer)viewport, (FloatBuffer)screenPositions);
        if (result) {
            return new Vec3((double)screenPositions.get(0), (double)((float)Display.getHeight() - screenPositions.get(1)), (double)screenPositions.get(2));
        }
        return null;
    }

    public static void drawFastRoundedRect(float x0, float y0, float x1, float y1, float radius, int color) {
        int Semicircle = 18;
        float f = 5.0f;
        float f2 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f3 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f4 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f5 = (float)(color & 0xFF) / 255.0f;
        GL11.glDisable((int)2884);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
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
        int j = 0;
        for (j = 0; j <= 18; ++j) {
            float f8 = (float)j * 5.0f;
            GL11.glVertex2f((float)((float)((double)f6 + (double)radius * Math.cos(Math.toRadians(f8)))), (float)((float)((double)f7 - (double)radius * Math.sin(Math.toRadians(f8)))));
        }
        GL11.glEnd();
        GL11.glBegin((int)6);
        f6 = x0 + radius;
        f7 = y0 + radius;
        GL11.glVertex2f((float)f6, (float)f7);
        for (j = 0; j <= 18; ++j) {
            float f9 = (float)j * 5.0f;
            GL11.glVertex2f((float)((float)((double)f6 - (double)radius * Math.cos(Math.toRadians(f9)))), (float)((float)((double)f7 - (double)radius * Math.sin(Math.toRadians(f9)))));
        }
        GL11.glEnd();
        GL11.glBegin((int)6);
        f6 = x0 + radius;
        f7 = y1 - radius;
        GL11.glVertex2f((float)f6, (float)f7);
        for (j = 0; j <= 18; ++j) {
            float f10 = (float)j * 5.0f;
            GL11.glVertex2f((float)((float)((double)f6 - (double)radius * Math.cos(Math.toRadians(f10)))), (float)((float)((double)f7 + (double)radius * Math.sin(Math.toRadians(f10)))));
        }
        GL11.glEnd();
        GL11.glBegin((int)6);
        f6 = x1 - radius;
        f7 = y1 - radius;
        GL11.glVertex2f((float)f6, (float)f7);
        for (j = 0; j <= 18; ++j) {
            float f11 = (float)j * 5.0f;
            GL11.glVertex2f((float)((float)((double)f6 + (double)radius * Math.cos(Math.toRadians(f11)))), (float)((float)((double)f7 + (double)radius * Math.sin(Math.toRadians(f11)))));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2884);
        GL11.glDisable((int)3042);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawnewrect(float left, float top, float right, float bottom, int color) {
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

    public static Vec3 getEntityRenderPosition(Entity entity) {
        return new Vec3(RenderUtils3.getEntityRenderX(entity), RenderUtils3.getEntityRenderY(entity), RenderUtils3.getEntityRenderZ(entity));
    }

    public static double getEntityRenderX(Entity entity) {
        return entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)Minecraft.func_71410_x().field_71428_T.field_74281_c - RenderUtils3.mc.func_175598_ae().field_78725_b;
    }

    public static double getEntityRenderY(Entity entity) {
        return entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)Minecraft.func_71410_x().field_71428_T.field_74281_c - RenderUtils3.mc.func_175598_ae().field_78726_c;
    }

    public static double getEntityRenderZ(Entity entity) {
        return entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)Minecraft.func_71410_x().field_71428_T.field_74281_c - RenderUtils3.mc.func_175598_ae().field_78723_d;
    }

    public static int RedRainbow() {
        int RainbowColor;
        int c = RainbowColor = RenderUtils3.rainbow(System.nanoTime(), ++counts, 0.5f).getRGB();
        Color col = new Color(c);
        return new Color((float)col.getRed() / 150.0f, 0.09803922f, 0.09803922f).getRGB();
    }

    public static void drawEntityBox(Entity entity, Color color) {
        RenderManager renderManager = mc.func_175598_ae();
        Timer timer = RenderUtils3.mc.field_71428_T;
        GlStateManager.func_179094_E();
        GL11.glBlendFunc((int)770, (int)771);
        RenderUtils3.enableGlCap(3042);
        RenderUtils3.disableGlCap(3553, 2929);
        GL11.glDepthMask((boolean)false);
        double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)timer.field_74281_c - renderManager.field_78725_b;
        double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)timer.field_74281_c - renderManager.field_78726_c;
        double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)timer.field_74281_c - renderManager.field_78723_d;
        AxisAlignedBB entityBox = entity.func_174813_aQ();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(entityBox.field_72340_a - entity.field_70165_t + x - 0.05, entityBox.field_72338_b - entity.field_70163_u + y, entityBox.field_72339_c - entity.field_70161_v + z - 0.05, entityBox.field_72336_d - entity.field_70165_t + x + 0.05, entityBox.field_72337_e - entity.field_70163_u + y + 0.15, entityBox.field_72334_f - entity.field_70161_v + z + 0.05);
        GL11.glTranslated((double)x, (double)y, (double)z);
        GL11.glRotated((double)(-entity.func_70079_am()), (double)0.0, (double)1.0, (double)0.0);
        GL11.glTranslated((double)(-x), (double)(-y), (double)(-z));
        GL11.glLineWidth((float)3.0f);
        RenderUtils3.enableGlCap(2848);
        RenderUtils3.glColor(0, 0, 0, 255);
        RenderUtils3.drawSelectionBoundingBox(axisAlignedBB);
        GL11.glLineWidth((float)1.0f);
        RenderUtils3.enableGlCap(2848);
        RenderUtils3.glColor(color.getRed(), color.getGreen(), color.getBlue(), 255);
        RenderUtils3.drawSelectionBoundingBox(axisAlignedBB);
        GlStateManager.func_179117_G();
        GL11.glDepthMask((boolean)true);
        RenderUtils3.resetCaps();
        GlStateManager.func_179121_F();
    }

    public static void renderItemStack(ItemStack stack, int x, int y) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179086_m((int)256);
        RenderHelper.func_74519_b();
        Minecraft.func_71410_x().func_175599_af().field_77023_b = -150.0f;
        GlStateManager.func_179097_i();
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
        GlStateManager.func_179145_e();
        GlStateManager.func_179126_j();
        Minecraft.func_71410_x().func_175599_af().func_180450_b(stack, x, y);
        Minecraft.func_71410_x().func_175599_af().func_175030_a(Minecraft.func_71410_x().field_71466_p, stack, x, y);
        Minecraft.func_71410_x().func_175599_af().field_77023_b = 0.0f;
        RenderHelper.func_74518_a();
        GlStateManager.func_179129_p();
        GlStateManager.func_179141_d();
        GlStateManager.func_179084_k();
        GlStateManager.func_179140_f();
        GlStateManager.func_179097_i();
        GlStateManager.func_179126_j();
        GlStateManager.func_179121_F();
    }

    public static void shadow(Entity player, double x, double y, double z, double range, int s, int color) {
        GL11.glPushMatrix();
        GL11.glDisable((int)2896);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)2848);
        GL11.glDepthMask((boolean)true);
        GlStateManager.func_179137_b((double)x, (double)y, (double)z);
        GlStateManager.func_179131_c((float)0.1f, (float)0.1f, (float)0.1f, (float)0.75f);
        GlStateManager.func_179114_b((float)180.0f, (float)90.0f, (float)0.0f, (float)2.0f);
        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)90.0f, (float)90.0f);
        GlStateManager.func_179117_G();
        RenderUtils3.glColor(color);
        GL11.glBegin((int)2);
        Cylinder c = new Cylinder();
        c.setDrawStyle(100011);
        c.draw((float)(range - 0.45), (float)(range - 0.5), 0.0f, s, 0);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }

    public static void cylinder(Entity player, double x, double y, double z, double range, int s, int color) {
        GL11.glPushMatrix();
        GL11.glDisable((int)2896);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)2848);
        GL11.glDepthMask((boolean)true);
        GlStateManager.func_179137_b((double)x, (double)y, (double)z);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)0.75f);
        GlStateManager.func_179114_b((float)180.0f, (float)90.0f, (float)0.0f, (float)2.0f);
        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)90.0f, (float)90.0f);
        GlStateManager.func_179117_G();
        RenderUtils3.glColor(color);
        GL11.glBegin((int)2);
        Cylinder c = new Cylinder();
        c.setDrawStyle(100011);
        c.draw((float)(range - 0.5), (float)(range - 0.5), 0.0f, s, 0);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }

    public static void NdrawCircle(float cx, float cy, float r, int num_segments, float width, int color) {
        GL11.glPushMatrix();
        cx *= 2.0f;
        cy *= 2.0f;
        float theta = (float)(6.2831852 / (double)num_segments);
        float p = (float)Math.cos(theta);
        float s = (float)Math.sin(theta);
        float x = r *= 2.0f;
        float y = 0.0f;
        RenderUtils3.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glLineWidth((float)width);
        GlStateManager.func_179117_G();
        RenderUtils3.glColor(color);
        GL11.glBegin((int)2);
        for (int ii = 0; ii < num_segments; ++ii) {
            GL11.glVertex2f((float)(x + cx), (float)(y + cy));
            float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
        }
        RenderUtils3.glColor(color);
        GL11.glEnd();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderUtils3.disableGL2D();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
    }

    public static void drawESPCircle(float cx, float cy, float r, int num_segments, int color) {
        GL11.glPushMatrix();
        cx *= 2.0f;
        cy *= 2.0f;
        float theta = (float)(6.2831852 / (double)num_segments);
        float p = (float)Math.cos(theta);
        float s = (float)Math.sin(theta);
        float x = r *= 2.0f;
        float y = 0.0f;
        RenderUtils3.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179117_G();
        RenderUtils3.glColor(color);
        GL11.glBegin((int)2);
        for (int ii = 0; ii < num_segments; ++ii) {
            GL11.glVertex2f((float)(x + cx), (float)(y + cy));
            float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
        }
        Colors.getColor(-1);
        GL11.glEnd();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderUtils3.disableGL2D();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
    }

    public static void startDrawing() {
        GL11.glEnable((int)3042);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        Minecraft.func_71410_x().field_71460_t.func_78479_a(Minecraft.func_71410_x().field_71428_T.field_74281_c, 0);
    }

    public static void stopDrawing() {
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void enableGL2D() {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
    }

    public static void disableGL2D() {
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    public static void drawtargethudRect(double d, double e, double g, double h, int color, int i) {
        RenderUtils3.drawRect(d + 1.0, e, g - 1.0, h, color);
        RenderUtils3.drawRect(d, e + 1.0, d + 1.0, h - 1.0, color);
        RenderUtils3.drawRect(d + 1.0, e + 1.0, d + 0.5, e + 0.5, color);
        RenderUtils3.drawRect(d + 1.0, e + 1.0, d + 0.5, e + 0.5, color);
        RenderUtils3.drawRect(g - 1.0, e + 1.0, g - 0.5, e + 0.5, color);
        RenderUtils3.drawRect(g - 1.0, e + 1.0, g, h - 1.0, color);
        RenderUtils3.drawRect(d + 1.0, h - 1.0, d + 0.5, h - 0.5, color);
        RenderUtils3.drawRect(g - 1.0, h - 1.0, g - 0.5, h - 0.5, color);
    }

    public static void drawBorder(float x, float y, float x2, float y2, float width, int color1) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        RenderUtils3.glColor(color1);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void quickDrawRect(float x, float y, float x2, float y2, int color) {
        RenderUtils3.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
    }

    public static void quickDrawBorderedRect(float x, float y, float x2, float y2, float width, int color1, int color2) {
        RenderUtils3.quickDrawRect(x, y, x2, y2, color2);
        RenderUtils3.glColor(color1);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
    }

    public static void drawArc(float n, float n2, double n3, int n4, int n5, double n6, int n7) {
        n3 *= 2.0;
        n *= 2.0f;
        n2 *= 2.0f;
        float n8 = (float)(n4 >> 24 & 0xFF) / 255.0f;
        float n9 = (float)(n4 >> 16 & 0xFF) / 255.0f;
        float n10 = (float)(n4 >> 8 & 0xFF) / 255.0f;
        float n11 = (float)(n4 & 0xFF) / 255.0f;
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glLineWidth((float)n7);
        GL11.glEnable((int)2848);
        GL11.glColor4f((float)n9, (float)n10, (float)n11, (float)n8);
        GL11.glBegin((int)3);
        int n12 = n5;
        while ((double)n12 <= n6) {
            GL11.glVertex2d((double)((double)n + Math.sin((double)n12 * Math.PI / 180.0) * n3), (double)((double)n2 + Math.cos((double)n12 * Math.PI / 180.0) * n3));
            ++n12;
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

    public static double getAnimationState(double n, double n2, double n3) {
        float n4 = (float)((double)RenderUtils.deltaTime * n3);
        n = n < n2 ? (n + (double)n4 < n2 ? (n += (double)n4) : n2) : (n - (double)n4 > n2 ? (n -= (double)n4) : n2);
        return n;
    }

    public static void drawHorizontalLine(float x, float y, float x1, float thickness, int color) {
        RenderUtils3.rectangle(x, y, x1, y + thickness, color);
    }

    public static void drawVerticalLine(float x, float y, float y1, float thickness, int color) {
        RenderUtils3.rectangle(x, y, x + thickness, y1, color);
    }

    public static Color rainbow(long time, float count, float fade) {
        float hue = ((float)time + (1.0f + count) * 2.0E8f) / 1.0E10f % 1.0f;
        long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 0.8f, 1.0f)), 16);
        Color c = new Color((int)color);
        return new Color((float)c.getRed() / 255.0f * fade, (float)c.getGreen() / 255.0f * fade, (float)c.getBlue() / 255.0f * fade, (float)c.getAlpha() / 255.0f);
    }

    public static void drawEntityOnScreen(int p_147046_0_, int p_147046_1_, float targetHeight, float p_147046_3_, float p_147046_4_, EntityLivingBase p_147046_5_) {
        GlStateManager.func_179142_g();
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)p_147046_0_, (float)p_147046_1_, (float)40.0f);
        GlStateManager.func_179152_a((float)(-targetHeight), (float)targetHeight, (float)targetHeight);
        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        float var6 = p_147046_5_.field_70761_aq;
        float var7 = p_147046_5_.field_70177_z;
        float var8 = p_147046_5_.field_70125_A;
        float var9 = p_147046_5_.field_70758_at;
        float var10 = p_147046_5_.field_70759_as;
        GlStateManager.func_179114_b((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.func_74519_b();
        GlStateManager.func_179114_b((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(-((float)Math.atan(p_147046_4_ / 40.0f)) * 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        p_147046_5_.field_70761_aq = (float)Math.atan(p_147046_3_ / 40.0f) * -14.0f;
        p_147046_5_.field_70177_z = (float)Math.atan(p_147046_3_ / 40.0f) * -14.0f;
        p_147046_5_.field_70125_A = -((float)Math.atan(p_147046_4_ / 40.0f)) * 15.0f;
        p_147046_5_.field_70759_as = p_147046_5_.field_70177_z;
        p_147046_5_.field_70758_at = p_147046_5_.field_70177_z;
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.0f);
        RenderManager var11 = Minecraft.func_71410_x().func_175598_ae();
        var11.func_178631_a(180.0f);
        var11.func_178633_a(false);
        var11.func_147940_a((Entity)p_147046_5_, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        var11.func_178633_a(true);
        p_147046_5_.field_70761_aq = var6;
        p_147046_5_.field_70177_z = var7;
        p_147046_5_.field_70125_A = var8;
        p_147046_5_.field_70758_at = var9;
        p_147046_5_.field_70759_as = var10;
        GlStateManager.func_179121_F();
        RenderHelper.func_74518_a();
        GlStateManager.func_179101_C();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77476_b);
        GlStateManager.func_179090_x();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77478_a);
    }

    public static int reAlpha(int n, float n2) {
        Color color = new Color(n);
        return new Color(0.003921569f * (float)color.getRed(), 0.003921569f * (float)color.getGreen(), 0.003921569f * (float)color.getBlue(), n2).getRGB();
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        float f4 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(col2 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f4);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static void drawRoundedRect2(float x, float y, float x2, float y2, float round, int color) {
        Gui.func_73734_a((int)((int)(x += (float)((double)(round / 2.0f) + 0.5))), (int)((int)(y += (float)((double)(round / 2.0f) + 0.5))), (int)((int)(x2 -= (float)((double)(round / 2.0f) + 0.5))), (int)((int)(y2 -= (float)((double)(round / 2.0f) + 0.5))), (int)color);
        RenderUtils3.circle(x2 - round / 2.0f, y + round / 2.0f, round, color);
        RenderUtils3.circle(x + round / 2.0f, y2 - round / 2.0f, round, color);
        RenderUtils3.circle(x + round / 2.0f, y + round / 2.0f, round, color);
        RenderUtils3.circle(x2 - round / 2.0f, y2 - round / 2.0f, round, color);
        Gui.func_73734_a((int)((int)(x - round / 2.0f - 0.5f)), (int)((int)(y + round / 2.0f)), (int)((int)x2), (int)((int)(y2 - round / 2.0f)), (int)color);
        Gui.func_73734_a((int)((int)x), (int)((int)(y + round / 2.0f)), (int)((int)(x2 + round / 2.0f + 0.5f)), (int)((int)(y2 - round / 2.0f)), (int)color);
        Gui.func_73734_a((int)((int)(x + round / 2.0f)), (int)((int)(y - round / 2.0f - 0.5f)), (int)((int)(x2 - round / 2.0f)), (int)((int)(y2 - round / 2.0f)), (int)color);
        Gui.func_73734_a((int)((int)(x + round / 2.0f)), (int)((int)y), (int)((int)(x2 - round / 2.0f)), (int)((int)(y2 + round / 2.0f + 0.5f)), (int)color);
    }

    public static void drawBorderRect(float x, float y, float x2, float y2, float round, int color) {
        RenderUtils3.drawRect(x += round / 2.0f + 0.5f, y += round / 2.0f + 0.5f, x2 -= round / 2.0f + 0.5f, y2 -= round / 2.0f + 0.5f, color);
        RenderUtils3.circle(x2 - round / 2.0f, y + round / 2.0f, round, color);
        RenderUtils3.circle(x + round / 2.0f, y2 - round / 2.0f - 0.2f, round, color);
        RenderUtils3.circle(x + round / 2.0f, y + round / 2.0f, round, color);
        RenderUtils3.circle(x2 - round / 2.0f, y2 - round / 2.0f - 0.2f, round, color);
        RenderUtils3.drawRect(x - round / 2.0f - 0.5f, y + round / 2.0f, x2, y2 - round / 2.0f, color);
        RenderUtils3.drawRect(x, y + round / 2.0f, x2 + round / 2.0f + 0.5f, y2 - round / 2.0f, color);
        RenderUtils3.drawRect(x + round / 2.0f, y - round / 2.0f - 0.5f, x2 - round / 2.0f, y2 - round / 2.0f, color);
        RenderUtils3.drawRect(x + round / 2.0f, y, x2 - round / 2.0f, y2 + round / 2.0f + 0.5f, color);
    }

    public static void glColor1(int color) {
    }

    public static void enableSmoothLine(float fl) {
    }

    public static void disableSmoothLine() {
    }

    public static void rectangle(double left, double top, double right, double bottom, int color) {
        double var5;
        if (left < right) {
            var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            var5 = top;
            top = bottom;
            bottom = var5;
        }
        float var11 = (float)(color >> 24 & 0xFF) / 255.0f;
        float var6 = (float)(color >> 16 & 0xFF) / 255.0f;
        float var7 = (float)(color >> 8 & 0xFF) / 255.0f;
        float var8 = (float)(color & 0xFF) / 255.0f;
        WorldRenderer worldRenderer = Tessellator.func_178181_a().func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)var6, (float)var7, (float)var8, (float)var11);
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(left, bottom, 0.0).func_181675_d();
        worldRenderer.func_181662_b(right, bottom, 0.0).func_181675_d();
        worldRenderer.func_181662_b(right, top, 0.0).func_181675_d();
        worldRenderer.func_181662_b(left, top, 0.0).func_181675_d();
        Tessellator.func_178181_a().func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawOutlinedString(String str, int x, int y, int color, int color2) {
        Minecraft mc = Minecraft.func_71410_x();
        mc.field_71466_p.func_78276_b(str, (int)((float)x - 1.0f), y, color2);
        mc.field_71466_p.func_78276_b(str, (int)((float)x + 1.0f), y, color2);
        mc.field_71466_p.func_78276_b(str, x, (int)((float)y + 1.0f), color2);
        mc.field_71466_p.func_78276_b(str, x, (int)((float)y - 1.0f), color2);
        mc.field_71466_p.func_78276_b(str, x, y, color);
    }

    public static void drawBlockBox(BlockPos blockPos, Color color, boolean outline) {
        RenderManager renderManager = mc.func_175598_ae();
        Timer timer = RenderUtils3.mc.field_71428_T;
        double x = (double)blockPos.func_177958_n() - renderManager.field_78725_b;
        double y = (double)blockPos.func_177956_o() - renderManager.field_78726_c;
        double z = (double)blockPos.func_177952_p() - renderManager.field_78723_d;
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        Block block = BlockUtils.getBlock(blockPos);
        if (block != null) {
            EntityPlayerSP player = RenderUtils3.mc.field_71439_g;
            double posX = player.field_70142_S + (player.field_70165_t - player.field_70142_S) * (double)timer.field_74281_c;
            double posY = player.field_70137_T + (player.field_70163_u - player.field_70137_T) * (double)timer.field_74281_c;
            double posZ = player.field_70136_U + (player.field_70161_v - player.field_70136_U) * (double)timer.field_74281_c;
            axisAlignedBB = block.func_180646_a((World)RenderUtils3.mc.field_71441_e, blockPos).func_72314_b((double)0.002f, (double)0.002f, (double)0.002f).func_72317_d(-posX, -posY, -posZ);
        }
        GL11.glBlendFunc((int)770, (int)771);
        RenderUtils3.enableGlCap(3042);
        RenderUtils3.disableGlCap(3553, 2929);
        GL11.glDepthMask((boolean)false);
        RenderUtils3.glColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() != 255 ? color.getAlpha() : (outline ? 26 : 35));
        RenderUtils3.drawFilledBox(axisAlignedBB);
        if (outline) {
            GL11.glLineWidth((float)1.0f);
            RenderUtils3.enableGlCap(2848);
            RenderUtils3.glColor(color);
            RenderUtils3.drawSelectionBoundingBox(axisAlignedBB);
        }
        GlStateManager.func_179117_G();
        GL11.glDepthMask((boolean)true);
        RenderUtils3.resetCaps();
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
        Timer timer = RenderUtils3.mc.field_71428_T;
        GL11.glBlendFunc((int)770, (int)771);
        RenderUtils3.enableGlCap(3042);
        RenderUtils3.disableGlCap(3553, 2929);
        GL11.glDepthMask((boolean)false);
        double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)timer.field_74281_c - renderManager.field_78725_b;
        double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)timer.field_74281_c - renderManager.field_78726_c;
        double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)timer.field_74281_c - renderManager.field_78723_d;
        AxisAlignedBB entityBox = entity.func_174813_aQ();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(entityBox.field_72340_a - entity.field_70165_t + x - 0.05, entityBox.field_72338_b - entity.field_70163_u + y + 0.1, entityBox.field_72339_c - entity.field_70161_v + z - 0.05, entityBox.field_72336_d - entity.field_70165_t + x + 0.05, entityBox.field_72337_e - entity.field_70163_u + y - 0.05, entityBox.field_72334_f - entity.field_70161_v + z + 0.05);
        if (outline) {
            GL11.glLineWidth((float)0.5f);
            RenderUtils3.enableGlCap(2848);
            RenderUtils3.glColor(color.getRed(), color.getGreen(), color.getBlue(), 150);
            RenderUtils3.drawSelectionBoundingBox(axisAlignedBB);
        }
        RenderUtils3.glColor(color.getRed(), color.getGreen(), color.getBlue(), outline ? 2 : 1);
        RenderUtils3.drawFilledBox(axisAlignedBB);
        GlStateManager.func_179117_G();
        GL11.glDepthMask((boolean)true);
        RenderUtils3.resetCaps();
    }

    public static void drawEntityOtherBox(Entity entity, Color color, boolean outline) {
        RenderManager renderManager = mc.func_175598_ae();
        Timer timer = RenderUtils3.mc.field_71428_T;
        GL11.glBlendFunc((int)770, (int)771);
        RenderUtils3.enableGlCap(3042);
        RenderUtils3.disableGlCap(3553, 2929);
        GL11.glDepthMask((boolean)false);
        double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)timer.field_74281_c - renderManager.field_78725_b;
        double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)timer.field_74281_c - renderManager.field_78726_c;
        double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)timer.field_74281_c - renderManager.field_78723_d;
        AxisAlignedBB entityBox = entity.func_174813_aQ();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(entityBox.field_72340_a - entity.field_70165_t + x - 0.05, entityBox.field_72338_b - entity.field_70163_u + y, entityBox.field_72339_c - entity.field_70161_v + z - 0.05, entityBox.field_72336_d - entity.field_70165_t + x + 0.05, entityBox.field_72337_e - entity.field_70163_u + y + 0.15, entityBox.field_72334_f - entity.field_70161_v + z + 0.05);
        if (outline) {
            GL11.glLineWidth((float)1.0f);
            RenderUtils3.enableGlCap(2848);
            RenderUtils3.glColor(color.getRed(), color.getGreen(), color.getBlue(), 100);
            RenderUtils3.drawSelectionBoundingBox(axisAlignedBB);
        }
        RenderUtils3.glColor(color.getRed(), color.getGreen(), color.getBlue(), outline ? 26 : 35);
        RenderUtils3.drawFilledBox(axisAlignedBB);
        GlStateManager.func_179117_G();
        GL11.glDepthMask((boolean)true);
        RenderUtils3.resetCaps();
    }

    public static void drawAxisAlignedBB(AxisAlignedBB axisAlignedBB, Color color) {
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        RenderUtils3.glColor(color);
        RenderUtils3.drawFilledBox(axisAlignedBB);
        GlStateManager.func_179117_G();
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static void drawPlatform(double y, Color color, double size) {
        RenderManager renderManager = mc.func_175598_ae();
        double renderY = y - renderManager.field_78726_c;
        RenderUtils3.drawAxisAlignedBB(new AxisAlignedBB(size, renderY + 0.02, size, -size, renderY, -size), color);
    }

    public static void drawPlatform(Entity entity, Color color) {
        RenderManager renderManager = mc.func_175598_ae();
        Timer timer = RenderUtils3.mc.field_71428_T;
        double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)timer.field_74281_c - renderManager.field_78725_b;
        double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)timer.field_74281_c - renderManager.field_78726_c;
        double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)timer.field_74281_c - renderManager.field_78723_d;
        AxisAlignedBB axisAlignedBB = entity.func_174813_aQ().func_72317_d(-entity.field_70165_t, -entity.field_70163_u, -entity.field_70161_v).func_72317_d(x, y, z);
        RenderUtils3.drawAxisAlignedBB(new AxisAlignedBB(axisAlignedBB.field_72340_a - 0.1, axisAlignedBB.field_72338_b - 0.1, axisAlignedBB.field_72339_c - 0.1, axisAlignedBB.field_72336_d + 0.1, axisAlignedBB.field_72337_e + 0.2, axisAlignedBB.field_72334_f + 0.1), color);
    }

    public static void kaMark(Entity entity, Color color) {
        RenderManager renderManager = mc.func_175598_ae();
        Timer timer = RenderUtils3.mc.field_71428_T;
        double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)timer.field_74281_c - renderManager.field_78725_b;
        double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)timer.field_74281_c - renderManager.field_78726_c;
        double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)timer.field_74281_c - renderManager.field_78723_d;
        AxisAlignedBB axisAlignedBB = entity.func_174813_aQ().func_72317_d(-entity.field_70165_t, -entity.field_70163_u, -entity.field_70161_v).func_72317_d(x, y, z);
        RenderUtils3.drawAxisAlignedBB(new AxisAlignedBB(axisAlignedBB.field_72340_a - 0.025, axisAlignedBB.field_72337_e - 0.35, axisAlignedBB.field_72339_c - 0.025, axisAlignedBB.field_72336_d + 0.025, axisAlignedBB.field_72337_e - 0.275, axisAlignedBB.field_72334_f + 0.025), color);
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
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
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

    public static void drawRect(float x, float y, float x2, float y2, int color) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        RenderUtils3.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawRect(double x, double y, double x2, double y2, int color) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        RenderUtils3.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawRectBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        RenderUtils3.rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        RenderUtils3.rectangle(x + width, y, x1 - width, y + width, borderColor);
        RenderUtils3.rectangle(x, y, x + width, y1, borderColor);
        RenderUtils3.rectangle(x1 - width, y, x1, y1, borderColor);
        RenderUtils3.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
    }

    public static void drawRect(float x, float y, float x2, float y2, Color color) {
        RenderUtils3.drawRect(x, y, x2, y2, color.getRGB());
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float width, int color1, int color2) {
        RenderUtils3.drawRect(x, y, x2, y2, color2);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        RenderUtils3.glColor(color1);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawLoadingCircle(float x, float y) {
        for (int i = 0; i < 4; ++i) {
            int rot = (int)(System.nanoTime() / 5000000L * (long)i % 360L);
            RenderUtils3.drawCircle(x, y, i * 10, rot - 180, rot);
        }
    }

    public static void drawCircle(float x, float y, float radius, int start, int end) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        RenderUtils3.glColor(Color.WHITE);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)2.0f);
        GL11.glBegin((int)3);
        for (float i = (float)end; i >= (float)start; i -= 4.0f) {
            GL11.glVertex2f((float)((float)((double)x + Math.cos((double)i * Math.PI / 180.0) * (double)(radius * 1.001f))), (float)((float)((double)y + Math.sin((double)i * Math.PI / 180.0) * (double)(radius * 1.001f))));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawFilledCircle(int xx, int yy, float radius, Color color) {
        int sections = 50;
        double dAngle = Math.PI * 2 / (double)sections;
        GL11.glPushAttrib((int)8192);
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
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

    public static void drawImage(ResourceLocation image2, int x, int y, int width, int height) {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        mc.func_110434_K().func_110577_a(image2);
        Gui.func_146110_a((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
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

    private static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void draw2D(EntityLivingBase entity, double posX, double posY, double posZ, int color, int backgroundColor) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)posX, (double)posY, (double)posZ);
        GL11.glNormal3f((float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(-RenderUtils3.mc.func_175598_ae().field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179139_a((double)-0.1, (double)-0.1, (double)0.1);
        GL11.glDisable((int)2929);
        GL11.glBlendFunc((int)770, (int)771);
        GlStateManager.func_179098_w();
        GlStateManager.func_179132_a((boolean)true);
        RenderUtils3.drawRect(-7.0f, 2.0f, -4.0f, 3.0f, color);
        RenderUtils3.drawRect(4.0f, 2.0f, 7.0f, 3.0f, color);
        RenderUtils3.drawRect(-7.0f, 0.5f, -6.0f, 3.0f, color);
        RenderUtils3.drawRect(6.0f, 0.5f, 7.0f, 3.0f, color);
        RenderUtils3.drawRect(-7.0f, 3.0f, -4.0f, 3.3f, backgroundColor);
        RenderUtils3.drawRect(4.0f, 3.0f, 7.0f, 3.3f, backgroundColor);
        RenderUtils3.drawRect(-7.3f, 0.5f, -7.0f, 3.3f, backgroundColor);
        RenderUtils3.drawRect(7.0f, 0.5f, 7.3f, 3.3f, backgroundColor);
        GlStateManager.func_179137_b((double)0.0, (double)(21.0 + -(entity.func_174813_aQ().field_72337_e - entity.func_174813_aQ().field_72338_b) * 12.0), (double)0.0);
        RenderUtils3.drawRect(4.0f, -20.0f, 7.0f, -19.0f, color);
        RenderUtils3.drawRect(-7.0f, -20.0f, -4.0f, -19.0f, color);
        RenderUtils3.drawRect(6.0f, -20.0f, 7.0f, -17.5f, color);
        RenderUtils3.drawRect(-7.0f, -20.0f, -6.0f, -17.5f, color);
        RenderUtils3.drawRect(7.0f, -20.0f, 7.3f, -17.5f, backgroundColor);
        RenderUtils3.drawRect(-7.3f, -20.0f, -7.0f, -17.5f, backgroundColor);
        RenderUtils3.drawRect(4.0f, -20.5f, 7.3f, -20.0f, backgroundColor);
        RenderUtils3.drawRect(-7.3f, -20.5f, -4.0f, -20.0f, backgroundColor);
        GL11.glEnable((int)2929);
        GlStateManager.func_179121_F();
    }

    public static void draw2D(BlockPos blockPos, int color, int backgroundColor) {
        RenderManager renderManager = mc.func_175598_ae();
        double posX = (double)blockPos.func_177958_n() + 0.5 - renderManager.field_78725_b;
        double posY = (double)blockPos.func_177956_o() - renderManager.field_78726_c;
        double posZ = (double)blockPos.func_177952_p() + 0.5 - renderManager.field_78723_d;
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)posX, (double)posY, (double)posZ);
        GL11.glNormal3f((float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(-RenderUtils3.mc.func_175598_ae().field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179139_a((double)-0.1, (double)-0.1, (double)0.1);
        RenderUtils3.setGlCap(2929, false);
        GL11.glBlendFunc((int)770, (int)771);
        GlStateManager.func_179098_w();
        GlStateManager.func_179132_a((boolean)true);
        RenderUtils3.drawRect(-7.0f, 2.0f, -4.0f, 3.0f, color);
        RenderUtils3.drawRect(4.0f, 2.0f, 7.0f, 3.0f, color);
        RenderUtils3.drawRect(-7.0f, 0.5f, -6.0f, 3.0f, color);
        RenderUtils3.drawRect(6.0f, 0.5f, 7.0f, 3.0f, color);
        RenderUtils3.drawRect(-7.0f, 3.0f, -4.0f, 3.3f, backgroundColor);
        RenderUtils3.drawRect(4.0f, 3.0f, 7.0f, 3.3f, backgroundColor);
        RenderUtils3.drawRect(-7.3f, 0.5f, -7.0f, 3.3f, backgroundColor);
        RenderUtils3.drawRect(7.0f, 0.5f, 7.3f, 3.3f, backgroundColor);
        GlStateManager.func_179109_b((float)0.0f, (float)9.0f, (float)0.0f);
        RenderUtils3.drawRect(4.0f, -20.0f, 7.0f, -19.0f, color);
        RenderUtils3.drawRect(-7.0f, -20.0f, -4.0f, -19.0f, color);
        RenderUtils3.drawRect(6.0f, -20.0f, 7.0f, -17.5f, color);
        RenderUtils3.drawRect(-7.0f, -20.0f, -6.0f, -17.5f, color);
        RenderUtils3.drawRect(7.0f, -20.0f, 7.3f, -17.5f, backgroundColor);
        RenderUtils3.drawRect(-7.3f, -20.0f, -7.0f, -17.5f, backgroundColor);
        RenderUtils3.drawRect(4.0f, -20.5f, 7.3f, -20.0f, backgroundColor);
        RenderUtils3.drawRect(-7.3f, -20.5f, -4.0f, -20.0f, backgroundColor);
        RenderUtils3.resetCaps();
        GlStateManager.func_179121_F();
    }

    public static void drawHLine(float x, float y, float x1, int y1) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }
        R2DUtils.drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
    }

    public static void drawHLine(double x, double y, double x1, double y1, float width, int color) {
        float var11 = (float)(color >> 24 & 0xFF) / 255.0f;
        float var6 = (float)(color >> 16 & 0xFF) / 255.0f;
        float var7 = (float)(color >> 8 & 0xFF) / 255.0f;
        float var8 = (float)(color & 0xFF) / 255.0f;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)var6, (float)var7, (float)var8, (float)var11);
        GL11.glPushMatrix();
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)3);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glEnd();
        GL11.glLineWidth((float)1.0f);
        GL11.glPopMatrix();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void renderNameTag(String string, double x, double y, double z) {
        RenderManager renderManager = mc.func_175598_ae();
        GL11.glPushMatrix();
        GL11.glTranslated((double)(x - renderManager.field_78725_b), (double)(y - renderManager.field_78726_c), (double)(z - renderManager.field_78723_d));
        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)(-RenderUtils3.mc.func_175598_ae().field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)RenderUtils3.mc.func_175598_ae().field_78732_j, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glScalef((float)-0.05f, (float)-0.05f, (float)0.05f);
        RenderUtils3.setGlCap(2896, false);
        RenderUtils3.setGlCap(2929, false);
        RenderUtils3.setGlCap(3042, true);
        GL11.glBlendFunc((int)770, (int)771);
        int width = Fonts.font35.func_78256_a(string) / 2;
        Gui.func_73734_a((int)(-width - 1), (int)-1, (int)(width + 1), (int)Fonts.font35.field_78288_b, (int)Integer.MIN_VALUE);
        Fonts.font35.func_175065_a(string, -width, 1.5f, Color.WHITE.getRGB(), true);
        RenderUtils3.resetCaps();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
    }

    public static void drawLine(double x, double y, double x1, double y1, float width) {
        GL11.glDisable((int)3553);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glEnd();
        GL11.glEnable((int)3553);
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
        RenderUtils3.setGlCap(cap, true);
    }

    public static void enableGlCap(int ... caps) {
        for (int cap : caps) {
            RenderUtils3.setGlCap(cap, true);
        }
    }

    public static void disableGlCap(int cap) {
        RenderUtils3.setGlCap(cap, true);
    }

    public static void disableGlCap(int ... caps) {
        for (int cap : caps) {
            RenderUtils3.setGlCap(cap, false);
        }
    }

    public static void setGlCap(int cap, boolean state) {
        glCapMap.put(cap, GL11.glGetBoolean((int)cap));
        RenderUtils3.setGlState(cap, state);
    }

    public static void setGlState(int cap, boolean state) {
        if (state) {
            GL11.glEnable((int)cap);
        } else {
            GL11.glDisable((int)cap);
        }
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return RenderUtils3.isInViewFrustrum(entity.func_174813_aQ()) || entity.field_70158_ak;
    }

    public static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.func_71410_x().func_175606_aa();
        frustrum.func_78547_a(current.field_70165_t, current.field_70163_u, current.field_70161_v);
        return frustrum.func_78546_a(bb);
    }

    public static void drawRoundRect2(float u, float d, float l, float r, int color) {
    }

    public static void drawRoundRect(float d, float e, float g, float h, int color) {
        RenderUtils3.drawRect(d + 1.0f, e, g, h, color);
        RenderUtils3.drawRect((double)d, (double)e + 0.75, (double)d, (double)h, color);
        RenderUtils3.drawRect((double)d, (double)(e + 1.0f), (double)(d + 1.0f), (double)h - 0.5, color);
        RenderUtils3.drawRect((double)d - 0.75, (double)e + 1.5, (double)d, (double)h - 1.25, color);
    }

    public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        RenderUtils3.rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils3.rectangle(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils3.rectangle(x, y, x + width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils3.rectangle(x1 - width, y, x1, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils3.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawFace(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight, AbstractClientPlayer target) {
        try {
            ResourceLocation skin = target.func_110306_p();
            Minecraft.func_71410_x().func_110434_K().func_110577_a(skin);
            GL11.glEnable((int)3042);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            Gui.func_152125_a((int)x, (int)y, (float)u, (float)v, (int)uWidth, (int)vHeight, (int)width, (int)height, (float)tileWidth, (float)tileHeight);
            GL11.glDisable((int)3042);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, EntityLivingBase ent) {
        GlStateManager.func_179142_g();
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)posX, (float)posY, (float)50.0f);
        GlStateManager.func_179152_a((float)(-scale), (float)scale, (float)scale);
        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        float f = ent.field_70761_aq;
        float f1 = ent.field_70177_z;
        float f2 = ent.field_70125_A;
        float f3 = ent.field_70758_at;
        float f4 = ent.field_70759_as;
        GlStateManager.func_179114_b((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.func_74519_b();
        GlStateManager.func_179114_b((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)0.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        ent.field_70761_aq = 10 * ent.field_70173_aa % 360;
        ent.field_70177_z = 10 * ent.field_70173_aa % 360;
        ent.field_70125_A = 0.0f;
        ent.field_70759_as = ent.field_70177_z;
        ent.field_70758_at = ent.field_70177_z;
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.0f);
        RenderManager rendermanager = Minecraft.func_71410_x().func_175598_ae();
        rendermanager.func_178631_a(180.0f);
        rendermanager.func_178633_a(false);
        rendermanager.func_147940_a((Entity)ent, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        rendermanager.func_178633_a(true);
        ent.field_70761_aq = f;
        ent.field_70177_z = f1;
        ent.field_70125_A = f2;
        ent.field_70758_at = f3;
        ent.field_70759_as = f4;
        GlStateManager.func_179121_F();
        RenderHelper.func_74518_a();
        GlStateManager.func_179101_C();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77476_b);
        GlStateManager.func_179090_x();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77478_a);
    }

    public static void drawStrokeRect(float x, float y, float x2, float y2, float size, int color, int color2) {
        RenderUtils3.drawRect(x, y, x2, y2, color);
        RenderUtils3.drawRect(x, y, x2, y + size, color2);
        RenderUtils3.drawRect(x, y2 - size, x2, y2, color2);
        RenderUtils3.drawRect(x, y, x + size, y2, color2);
        RenderUtils3.drawRect(x2 - size, y, x2, y2, color2);
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

    public static void drawPoses(Color color, List<Vec3> positions) {
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)2929);
        RenderUtils3.mc.field_71460_t.func_175072_h();
        GL11.glBegin((int)3);
        RenderUtils.glColor(color);
        double renderPosX = RenderUtils3.mc.func_175598_ae().field_78730_l;
        double renderPosY = RenderUtils3.mc.func_175598_ae().field_78731_m;
        double renderPosZ = RenderUtils3.mc.func_175598_ae().field_78728_n;
        for (Vec3 pos : positions) {
            GL11.glVertex3d((double)(pos.field_72450_a - renderPosX), (double)(pos.field_72448_b - renderPosY), (double)(pos.field_72449_c - renderPosZ));
        }
        GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
        GL11.glEnd();
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
        RenderUtils.glColor(0, 0, 0, 0);
    }

    static {
        modelView = BufferUtils.createFloatBuffer((int)16);
        projection = BufferUtils.createFloatBuffer((int)16);
        viewport = BufferUtils.createIntBuffer((int)16);
    }

    public static class R2DUtils {
        public static void enableGL2D() {
            GL11.glDisable((int)2929);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glDepthMask((boolean)true);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            GL11.glHint((int)3155, (int)4354);
        }

        public static void disableGL2D() {
            GL11.glEnable((int)3553);
            GL11.glDisable((int)3042);
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glHint((int)3154, (int)4352);
            GL11.glHint((int)3155, (int)4352);
        }

        public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
            R2DUtils.enableGL2D();
            GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
            R2DUtils.drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
            R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
            R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
            R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
            R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
            R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
            R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
            R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
            R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
            GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
            R2DUtils.disableGL2D();
            Gui.func_73734_a((int)0, (int)0, (int)0, (int)0, (int)0);
        }

        public static void drawRect(double x2, double y2, double x1, double y1, int color) {
            R2DUtils.enableGL2D();
            R2DUtils.glColor(color);
            R2DUtils.drawRect(x2, y2, x1, y1);
            R2DUtils.disableGL2D();
        }

        private static void drawRect(double x2, double y2, double x1, double y1) {
            GL11.glBegin((int)7);
            GL11.glVertex2d((double)x2, (double)y1);
            GL11.glVertex2d((double)x1, (double)y1);
            GL11.glVertex2d((double)x1, (double)y2);
            GL11.glVertex2d((double)x2, (double)y2);
            GL11.glEnd();
        }

        public static void glColor(int hex) {
            float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
            float red = (float)(hex >> 16 & 0xFF) / 255.0f;
            float green = (float)(hex >> 8 & 0xFF) / 255.0f;
            float blue = (float)(hex & 0xFF) / 255.0f;
            GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        }

        public static void drawRect(float x, float y, float x1, float y1, int color) {
            R2DUtils.enableGL2D();
            R2DUtils.glColor(color);
            R2DUtils.drawRect(x, y, x1, y1);
            R2DUtils.disableGL2D();
        }

        public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int borderColor) {
            R2DUtils.enableGL2D();
            R2DUtils.glColor(borderColor);
            R2DUtils.drawRect(x + width, y, x1 - width, y + width);
            R2DUtils.drawRect(x, y, x + width, y1);
            R2DUtils.drawRect(x1 - width, y, x1, y1);
            R2DUtils.drawRect(x + width, y1 - width, x1 - width, y1);
            R2DUtils.disableGL2D();
        }

        public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
            R2DUtils.enableGL2D();
            GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
            R2DUtils.drawVLine(x *= 2.0f, y *= 2.0f, y1 *= 2.0f, borderC);
            R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y, y1, borderC);
            R2DUtils.drawHLine(x, x1 - 1.0f, y, borderC);
            R2DUtils.drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
            R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
            GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
            R2DUtils.disableGL2D();
        }

        public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
            R2DUtils.enableGL2D();
            GL11.glShadeModel((int)7425);
            GL11.glBegin((int)7);
            R2DUtils.glColor(topColor);
            GL11.glVertex2f((float)x, (float)y1);
            GL11.glVertex2f((float)x1, (float)y1);
            R2DUtils.glColor(bottomColor);
            GL11.glVertex2f((float)x1, (float)y);
            GL11.glVertex2f((float)x, (float)y);
            GL11.glEnd();
            GL11.glShadeModel((int)7424);
            R2DUtils.disableGL2D();
        }

        public static void drawHLine(float x, float y, float x1, int y1) {
            if (y < x) {
                float var5 = x;
                x = y;
                y = var5;
            }
            R2DUtils.drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
        }

        public static void drawVLine(float x, float y, float x1, int y1) {
            if (x1 < y) {
                float var5 = y;
                y = x1;
                x1 = var5;
            }
            R2DUtils.drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
        }

        public static void drawHLine(float x, float y, float x1, int y1, int y2) {
            if (y < x) {
                float var5 = x;
                x = y;
                y = var5;
            }
            R2DUtils.drawGradientRect(x, x1, y + 1.0f, x1 + 1.0f, y1, y2);
        }

        public static void drawRect(float x, float y, float x1, float y1) {
            GL11.glBegin((int)7);
            GL11.glVertex2f((float)x, (float)y1);
            GL11.glVertex2f((float)x1, (float)y1);
            GL11.glVertex2f((float)x1, (float)y);
            GL11.glVertex2f((float)x, (float)y);
            GL11.glEnd();
        }
    }
}

