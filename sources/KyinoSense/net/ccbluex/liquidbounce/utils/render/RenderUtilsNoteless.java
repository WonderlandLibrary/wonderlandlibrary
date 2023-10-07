/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3d
 *  net.minecraft.block.Block
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
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
 *  net.minecraft.client.renderer.entity.RenderItem
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.Timer
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL14
 *  org.lwjgl.util.glu.GLU
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import javax.vecmath.Vector3d;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtil;
import net.ccbluex.liquidbounce.utils.render.Colors;
import net.ccbluex.liquidbounce.utils.render.Colors2;
import net.ccbluex.liquidbounce.utils.render.GLUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
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
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.glu.GLU;

@SideOnly(value=Side.CLIENT)
public final class RenderUtilsNoteless
extends MinecraftInstance {
    private static final Map<Integer, Boolean> glCapMap = new HashMap<Integer, Boolean>();
    private static final IntBuffer viewport = BufferUtils.createIntBuffer((int)16);
    private static final FloatBuffer modelview = GLAllocation.func_74529_h((int)16);
    private static final FloatBuffer projections = GLAllocation.func_74529_h((int)16);
    private static final Frustum frustrum = new Frustum();
    public static int deltaTime;
    private static final int[] DISPLAY_LISTS_2D;

    public static ScaledResolution getResolution() {
        return new ScaledResolution(mc);
    }

    public static int width() {
        return new ScaledResolution(Minecraft.func_71410_x()).func_78326_a();
    }

    public static void post() {
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glColor3d((double)1.0, (double)1.0, (double)1.0);
    }

    public static void prepareScissorBox(float x, float y, float x2, float y2) {
        ScaledResolution scale = new ScaledResolution(Minecraft.func_71410_x());
        int factor = scale.func_78325_e();
        GL11.glScissor((int)((int)(x * (float)factor)), (int)((int)(((float)scale.func_78328_b() - y2) * (float)factor)), (int)((int)((x2 - x) * (float)factor)), (int)((int)((y2 - y) * (float)factor)));
    }

    public static double getAnimationStateSmooth(double target, double current, double speed) {
        boolean larger;
        boolean bl = larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        if (target == current) {
            return target;
        }
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        current = larger ? (current + factor > target ? target : (current += factor)) : (current - factor < target ? target : (current -= factor));
        return current;
    }

    public static void glDrawFramebuffer(int framebufferTexture, int width, int height) {
        GL11.glBindTexture((int)3553, (int)framebufferTexture);
        GL11.glDisable((int)3008);
        boolean restore = RenderUtilsNoteless.glEnableBlend();
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex2f((float)0.0f, (float)0.0f);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex2f((float)0.0f, (float)height);
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex2f((float)width, (float)height);
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex2f((float)width, (float)0.0f);
        GL11.glEnd();
        RenderUtilsNoteless.glRestoreBlend(restore);
        GL11.glEnable((int)3008);
    }

    public static void glDrawSidewaysGradientRect(double x, double y, double width, double height, int startColour, int endColour) {
        boolean restore = RenderUtilsNoteless.glEnableBlend();
        GL11.glDisable((int)3553);
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)7);
        RenderUtilsNoteless.glColour(startColour);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)(y + height));
        RenderUtilsNoteless.glColour(endColour);
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glEnd();
        GL11.glShadeModel((int)7424);
        GL11.glEnable((int)3553);
        RenderUtilsNoteless.glRestoreBlend(restore);
    }

    public static double animateProgress(double current, double target, double speed) {
        if (current < target) {
            double inc = 1.0 / (double)Minecraft.func_175610_ah() * speed;
            if (target - current < inc) {
                return target;
            }
            return current + inc;
        }
        if (current > target) {
            double inc = 1.0 / (double)Minecraft.func_175610_ah() * speed;
            if (current - target < inc) {
                return target;
            }
            return current - inc;
        }
        return current;
    }

    public static int Astolfo(int var2, float bright, float st, int index, int offset, float client) {
        double d;
        double rainbowDelay = Math.ceil(System.currentTimeMillis() + (long)(var2 * index)) / (double)offset;
        return Color.getHSBColor((double)((float)(d / (double)client)) < 0.5 ? -((float)(rainbowDelay / (double)client)) : (float)((rainbowDelay %= (double)client) / (double)client), st, bright).getRGB();
    }

    public static void drawScaledCustomSizeModalRect(double x, double y, float u, float v, int uWidth, int vHeight, double width, double height, float tileWidth, float tileHeight) {
        float f = 1.0f / tileWidth;
        float f1 = 1.0f / tileHeight;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        worldrenderer.func_181662_b(x, y + height, 0.0).func_181673_a((double)(u * f), (double)((v + (float)vHeight) * f1)).func_181675_d();
        worldrenderer.func_181662_b(x + width, y + height, 0.0).func_181673_a((double)((u + (float)uWidth) * f), (double)((v + (float)vHeight) * f1)).func_181675_d();
        worldrenderer.func_181662_b(x + width, y, 0.0).func_181673_a((double)((u + (float)uWidth) * f), (double)(v * f1)).func_181675_d();
        worldrenderer.func_181662_b(x, y, 0.0).func_181673_a((double)(u * f), (double)(v * f1)).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void glColour(int color) {
        GL11.glColor4ub((byte)((byte)(color >> 16 & 0xFF)), (byte)((byte)(color >> 8 & 0xFF)), (byte)((byte)(color & 0xFF)), (byte)((byte)(color >> 24 & 0xFF)));
    }

    public static Color getGradientOffset(Color color1, Color color2, double gident) {
        if (gident > 1.0) {
            double f1 = gident % 1.0;
            int f2 = (int)gident;
            gident = f2 % 2 == 0 ? f1 : 1.0 - f1;
        }
        double f3 = 1.0 - gident;
        int f4 = (int)((double)color1.getRed() * f3 + (double)color2.getRed() * gident);
        int f5 = (int)((double)color1.getGreen() * f3 + (double)color2.getGreen() * gident);
        int f6 = (int)((double)color1.getBlue() * f3 + (double)color2.getBlue() * gident);
        return new Color(f4, f5, f6);
    }

    public static int getRainbow(int index, int offset, float bright, float st) {
        float hue = (System.currentTimeMillis() + (long)offset * (long)index) % 2000L;
        return Color.getHSBColor(hue /= 2000.0f, st, bright).getRGB();
    }

    public static void skeetRect(double x, double y, double x1, double y1, double size) {
        RenderUtils.rectangleBordered(x, y + -4.0, x1 + size, y1 + size, 0.5, new Color(60, 60, 60).getRGB(), new Color(10, 10, 10).getRGB());
        RenderUtils.rectangleBordered(x + 1.0, y + -3.0, x1 + size - 1.0, y1 + size - 1.0, 1.0, new Color(40, 40, 40).getRGB(), new Color(40, 40, 40).getRGB());
        RenderUtils.rectangleBordered(x + 2.5, y + -1.5, x1 + size - 2.5, y1 + size - 2.5, 0.5, new Color(40, 40, 40).getRGB(), new Color(60, 60, 60).getRGB());
        RenderUtils.rectangleBordered(x + 2.5, y + -1.5, x1 + size - 2.5, y1 + size - 2.5, 0.5, new Color(22, 22, 22).getRGB(), new Color(255, 255, 255, 0).getRGB());
    }

    public static void skeetRectSmall(double x, double y, double x1, double y1, double size) {
        RenderUtils.rectangleBordered(x + 4.35, y + 0.5, x1 + size - 84.5, y1 + size - 4.35, 0.5, new Color(48, 48, 48).getRGB(), new Color(10, 10, 10).getRGB());
        RenderUtils.rectangleBordered(x + 5.0, y + 1.0, x1 + size - 85.0, y1 + size - 5.0, 0.5, new Color(17, 17, 17).getRGB(), new Color(255, 255, 255, 0).getRGB());
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

    public static void drawOutlinedStringCock(FontRenderer fr, String s, float x, float y, int color, int outlineColor) {
        fr.func_78276_b(ColorUtil.stripColor(s), (int)(x - 1.0f), (int)y, outlineColor);
        fr.func_78276_b(ColorUtil.stripColor(s), (int)x, (int)(y - 1.0f), outlineColor);
        fr.func_78276_b(ColorUtil.stripColor(s), (int)(x + 1.0f), (int)y, outlineColor);
        fr.func_78276_b(ColorUtil.stripColor(s), (int)x, (int)(y + 1.0f), outlineColor);
        fr.func_78276_b(s, (int)x, (int)y, color);
    }

    private static void drawEnchantTag(String text, int x, float y) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179097_i();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        RenderUtilsNoteless.drawOutlinedStringCock(Minecraft.func_71410_x().field_71466_p, text, x, y, -1, new Color(0, 0, 0, 220).darker().getRGB());
        GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179126_j();
        GlStateManager.func_179121_F();
    }

    public static void imageCentered(ResourceLocation imageLocation, float x, float y, float width, float height) {
        RenderUtilsNoteless.image(imageLocation, x -= width / 2.0f, y -= height / 2.0f, width, height);
    }

    public static void bindTexture(int texture) {
        GL11.glBindTexture((int)3553, (int)texture);
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.field_147621_c != RenderUtilsNoteless.mc.field_71443_c || framebuffer.field_147618_d != RenderUtilsNoteless.mc.field_71440_d) {
            if (framebuffer != null) {
                framebuffer.func_147608_a();
            }
            return new Framebuffer(RenderUtilsNoteless.mc.field_71443_c, RenderUtilsNoteless.mc.field_71440_d, true);
        }
        return framebuffer;
    }

    public static double bezierBlendAnimation(double t) {
        return t * t * (3.0 - 2.0 * t);
    }

    public static int darker(int color, float factor) {
        int r = (int)((float)(color >> 16 & 0xFF) * factor);
        int g = (int)((float)(color >> 8 & 0xFF) * factor);
        int b = (int)((float)(color & 0xFF) * factor);
        int a = color >> 24 & 0xFF;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF | (a & 0xFF) << 24;
    }

    public static int darker(int color) {
        return RenderUtilsNoteless.darker(color, 0.6f);
    }

    public static boolean glEnableBlend() {
        boolean wasEnabled = GL11.glIsEnabled((int)3042);
        if (!wasEnabled) {
            GL11.glEnable((int)3042);
            GL14.glBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        }
        return wasEnabled;
    }

    public static void glRestoreBlend(boolean wasEnabled) {
        if (!wasEnabled) {
            GL11.glDisable((int)3042);
        }
    }

    public static void glDrawFilledQuad(double x, double y, double width, double height, int colour) {
        boolean restore = RenderUtilsNoteless.glEnableBlend();
        GL11.glDisable((int)3553);
        RenderUtilsNoteless.glColor(colour);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glEnd();
        RenderUtilsNoteless.glRestoreBlend(restore);
        GL11.glEnable((int)3553);
        GlStateManager.func_179084_k();
    }

    public static void drawFilledCircleNoGL(int x, int y, double r, int c, int quality) {
        float f = (float)(c >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(c >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(c >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(c & 0xFF) / 255.0f;
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glBegin((int)6);
        for (int i = 0; i <= 360 / quality; ++i) {
            double x2 = Math.sin((double)(i * quality) * Math.PI / 180.0) * r;
            double y2 = Math.cos((double)(i * quality) * Math.PI / 180.0) * r;
            GL11.glVertex2d((double)((double)x + x2), (double)((double)y + y2));
        }
        GL11.glEnd();
    }

    public static void image(ResourceLocation imageLocation, float x, float y, float width, float height) {
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        RenderUtilsNoteless.enable(3042);
        GlStateManager.func_179118_c();
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        mc.func_110434_K().func_110577_a(imageLocation);
        RenderUtilsNoteless.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GlStateManager.func_179141_d();
        RenderUtilsNoteless.disable(3042);
    }

    public static void enableRender2D() {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)2884);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glLineWidth((float)1.0f);
    }

    public static void start() {
        RenderUtilsNoteless.enable(3042);
        GL11.glBlendFunc((int)770, (int)771);
        RenderUtilsNoteless.disable(3553);
        RenderUtilsNoteless.disable(2884);
        GlStateManager.func_179118_c();
        GlStateManager.func_179097_i();
    }

    public static void enable(int glTarget) {
        GL11.glEnable((int)glTarget);
    }

    public static void color(Color color) {
        if (color == null) {
            color = Color.white;
        }
        RenderUtilsNoteless.color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    public static void color(double red, double green, double blue, double alpha) {
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
    }

    public static void lineWidth(float width) {
        GL11.glLineWidth((float)width);
    }

    public static void begin(int glMode) {
        GL11.glBegin((int)glMode);
    }

    public static void vertex(double x, double y) {
        GL11.glVertex2d((double)x, (double)y);
    }

    public static void end() {
        GL11.glEnd();
    }

    public static void stop() {
        GlStateManager.func_179141_d();
        GlStateManager.func_179126_j();
        RenderUtilsNoteless.enable(2884);
        RenderUtilsNoteless.enable(3553);
        RenderUtilsNoteless.disable(3042);
        RenderUtilsNoteless.color(Color.white);
    }

    public static void disable(int glTarget) {
        GL11.glDisable((int)glTarget);
    }

    public static void lineNoGl(double firstX, double firstY, double secondX, double secondY, Color color, Color secondColor) {
        RenderUtilsNoteless.start();
        if (color != null) {
            RenderUtilsNoteless.color(color);
        }
        RenderUtilsNoteless.lineWidth(1.0f);
        GL11.glEnable((int)2848);
        RenderUtilsNoteless.begin(1);
        RenderUtilsNoteless.vertex(firstX, firstY);
        RenderUtilsNoteless.vertex(secondX, secondY);
        RenderUtilsNoteless.end();
        GL11.glDisable((int)2848);
        RenderUtilsNoteless.stop();
    }

    public static void rect(double x, double y, double width, double height, Color color) {
        RenderUtilsNoteless.rect(x, y, width, height, true, color);
    }

    public static void rect(double x, double y, double width, double height, boolean filled, Color color) {
        RenderUtilsNoteless.start();
        if (color != null) {
            RenderUtilsNoteless.color(color);
        }
        RenderUtilsNoteless.begin(filled ? 6 : 1);
        RenderUtilsNoteless.vertex(x, y);
        RenderUtilsNoteless.vertex(x + width, y);
        RenderUtilsNoteless.vertex(x + width, y + height);
        RenderUtilsNoteless.vertex(x, y + height);
        if (!filled) {
            RenderUtilsNoteless.vertex(x, y);
            RenderUtilsNoteless.vertex(x, y + height);
            RenderUtilsNoteless.vertex(x + width, y);
            RenderUtilsNoteless.vertex(x + width, y + height);
        }
        RenderUtilsNoteless.end();
        RenderUtilsNoteless.stop();
    }

    public static void roundedRect(double x, double y, double width, double height, double edgeRadius, Color color) {
        double angle;
        double i;
        double halfRadius = edgeRadius / 2.0;
        width -= halfRadius;
        height -= halfRadius;
        float sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        RenderUtilsNoteless.start();
        if (color != null) {
            RenderUtilsNoteless.color(color);
        }
        RenderUtilsNoteless.begin(6);
        for (i = 180.0; i <= 270.0; i += 1.0) {
            angle = i * (Math.PI * 2) / 360.0;
            RenderUtilsNoteless.vertex(x + (double)sideLength * Math.cos(angle) + (double)sideLength, y + (double)sideLength * Math.sin(angle) + (double)sideLength);
        }
        RenderUtilsNoteless.vertex(x + (double)sideLength, y + (double)sideLength);
        RenderUtilsNoteless.end();
        RenderUtilsNoteless.stop();
        sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        RenderUtilsNoteless.start();
        if (color != null) {
            RenderUtilsNoteless.color(color);
        }
        GL11.glEnable((int)2848);
        RenderUtilsNoteless.begin(6);
        for (i = 0.0; i <= 90.0; i += 1.0) {
            angle = i * (Math.PI * 2) / 360.0;
            RenderUtilsNoteless.vertex(x + width + (double)sideLength * Math.cos(angle), y + height + (double)sideLength * Math.sin(angle));
        }
        RenderUtilsNoteless.vertex(x + width, y + height);
        RenderUtilsNoteless.end();
        GL11.glDisable((int)2848);
        RenderUtilsNoteless.stop();
        sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        RenderUtilsNoteless.start();
        if (color != null) {
            RenderUtilsNoteless.color(color);
        }
        GL11.glEnable((int)2848);
        RenderUtilsNoteless.begin(6);
        for (i = 270.0; i <= 360.0; i += 1.0) {
            angle = i * (Math.PI * 2) / 360.0;
            RenderUtilsNoteless.vertex(x + width + (double)sideLength * Math.cos(angle), y + (double)sideLength * Math.sin(angle) + (double)sideLength);
        }
        RenderUtilsNoteless.vertex(x + width, y + (double)sideLength);
        RenderUtilsNoteless.end();
        GL11.glDisable((int)2848);
        RenderUtilsNoteless.stop();
        sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        RenderUtilsNoteless.start();
        if (color != null) {
            RenderUtilsNoteless.color(color);
        }
        GL11.glEnable((int)2848);
        RenderUtilsNoteless.begin(6);
        for (i = 90.0; i <= 180.0; i += 1.0) {
            angle = i * (Math.PI * 2) / 360.0;
            RenderUtilsNoteless.vertex(x + (double)sideLength * Math.cos(angle) + (double)sideLength, y + height + (double)sideLength * Math.sin(angle));
        }
        RenderUtilsNoteless.vertex(x + (double)sideLength, y + height);
        RenderUtilsNoteless.end();
        GL11.glDisable((int)2848);
        RenderUtilsNoteless.stop();
        RenderUtilsNoteless.rect(x + halfRadius, y + halfRadius, width - halfRadius, height - halfRadius, color);
        RenderUtilsNoteless.rect(x, y + halfRadius, edgeRadius / 2.0, height - halfRadius, color);
        RenderUtilsNoteless.rect(x + width, y + halfRadius, edgeRadius / 2.0, height - halfRadius, color);
        RenderUtilsNoteless.rect(x + halfRadius, y, width - halfRadius, halfRadius, color);
        RenderUtilsNoteless.rect(x + halfRadius, y + height, width - halfRadius, halfRadius, color);
    }

    public static void setAlphaLimit(float limit) {
        GlStateManager.func_179141_d();
        GlStateManager.func_179092_a((int)516, (float)((float)((double)limit * 0.01)));
    }

    public static void setColor(Color color) {
        float alpha = (float)(color.getRGB() >> 24 & 0xFF) / 255.0f;
        float red = (float)(color.getRGB() >> 16 & 0xFF) / 255.0f;
        float green = (float)(color.getRGB() >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color.getRGB() & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void start2D() {
        GLUtils.glEnable(3042);
        GLUtils.glDisable(3553);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtils.glEnable(2848);
    }

    public static void stop2D() {
        GLUtils.glEnable(3553);
        GLUtils.glDisable(3042);
        GLUtils.glDisable(2848);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawArrow(double x, double y, int lineWidth, int color, double length) {
        RenderUtilsNoteless.start2D();
        GL11.glPushMatrix();
        GL11.glLineWidth((float)lineWidth);
        RenderUtilsNoteless.setColor(new Color(color));
        GL11.glBegin((int)3);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)(x + 3.0), (double)(y + length));
        GL11.glVertex2d((double)(x + 6.0), (double)y);
        GL11.glEnd();
        GL11.glPopMatrix();
        RenderUtilsNoteless.stop2D();
    }

    public static void drawCheck(double x, double y, int lineWidth, int color) {
        RenderUtilsNoteless.start2D();
        GL11.glPushMatrix();
        GL11.glLineWidth((float)lineWidth);
        RenderUtilsNoteless.setColor(new Color(color));
        GL11.glBegin((int)3);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)(x + 2.0), (double)(y + 3.0));
        GL11.glVertex2d((double)(x + 6.0), (double)(y - 2.0));
        GL11.glEnd();
        GL11.glPopMatrix();
        RenderUtilsNoteless.stop2D();
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

    public static void drawRoundedRect(float x, float y, float width, float height, float edgeRadius, int color, float borderWidth, int borderColor) {
        double angleRadians;
        int i;
        if (color == 0xFFFFFF) {
            color = Colors.WHITE.c;
        }
        if (borderColor == 0xFFFFFF) {
            borderColor = Colors.WHITE.c;
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
        RenderUtilsNoteless.drawRect(x + edgeRadius, y + edgeRadius, width - edgeRadius * 2.0f, height - edgeRadius * 2.0f, color);
        RenderUtilsNoteless.drawRect(x + edgeRadius, y, width - edgeRadius * 2.0f, edgeRadius, color);
        RenderUtilsNoteless.drawRect(x + edgeRadius, y + height - edgeRadius, width - edgeRadius * 2.0f, edgeRadius, color);
        RenderUtilsNoteless.drawRect(x, y + edgeRadius, edgeRadius, height - edgeRadius * 2.0f, color);
        RenderUtilsNoteless.drawRect(x + width - edgeRadius, y + edgeRadius, edgeRadius, height - edgeRadius * 2.0f, color);
        RenderUtilsNoteless.enableRender2D();
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
        GL11.glLineWidth((float)borderWidth);
        GL11.glBegin((int)3);
        centerX = x + edgeRadius;
        centerY = y + edgeRadius;
        for (i = vertices = (int)Math.min(Math.max(edgeRadius, 10.0f), 90.0f); i >= 0; --i) {
            angleRadians = Math.PI * 2 * (double)(i + 180) / (double)(vertices * 4);
            GL11.glVertex2d((double)((double)centerX + Math.sin(angleRadians) * (double)edgeRadius), (double)((double)centerY + Math.cos(angleRadians) * (double)edgeRadius));
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
        RenderUtilsNoteless.disableRender2D();
    }

    public static void drawHead(ResourceLocation skin, int width, int height) {
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        mc.func_110434_K().func_110577_a(skin);
        Gui.func_152125_a((int)2, (int)2, (float)8.0f, (float)8.0f, (int)8, (int)8, (int)width, (int)height, (float)64.0f, (float)64.0f);
    }

    public static void drawExhiRect(float x, float y, float x2, float y2) {
        RenderUtilsNoteless.drawRect(x - 3.5f, y - 3.5f, x2 + 3.5f, y2 + 3.5f, Color.black.getRGB());
        RenderUtilsNoteless.drawRect(x - 3.0f, y - 3.0f, x2 + 3.0f, y2 + 3.0f, new Color(50, 50, 50).getRGB());
        RenderUtilsNoteless.drawRect(x - 2.5f, y - 2.5f, x2 + 2.5f, y2 + 2.5f, new Color(26, 26, 26).getRGB());
        RenderUtilsNoteless.drawRect(x - 0.5f, y - 0.5f, x2 + 0.5f, y2 + 0.5f, new Color(50, 50, 50).getRGB());
        RenderUtilsNoteless.drawRect(x, y, x2, y2, new Color(18, 18, 18).getRGB());
    }

    public static void drawRoundedRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float radius, int color) {
        RenderUtilsNoteless.drawRoundedRect(paramXStart, paramYStart, paramXEnd, paramYEnd, radius, color, true);
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
        for (i = 0.0; i <= 90.0; i += 0.25) {
            GL11.glVertex2d((double)(x2 + Math.sin(i * degree) * (double)radius), (double)(y2 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 90.0; i <= 180.0; i += 0.25) {
            GL11.glVertex2d((double)(x2 + Math.sin(i * degree) * (double)radius), (double)(y1 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 180.0; i <= 270.0; i += 0.25) {
            GL11.glVertex2d((double)(x1 + Math.sin(i * degree) * (double)radius), (double)(y1 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 270.0; i <= 360.0; i += 0.25) {
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

    public static void drawRoundedCornerRect(float x, float y, float x1, float y1, float radius, int color) {
        GLUtils.glEnable(3042);
        GLUtils.glDisable(3553);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtils.glEnable(2848);
        RenderUtilsNoteless.glColor(color);
        RenderUtilsNoteless.drawRoundedCornerRect(x, y, x1, y1, radius);
        GLUtils.glEnable(3553);
        GLUtils.glDisable(3042);
        GLUtils.glDisable(2848);
    }

    private static void quickPolygonCircle(float x, float y, float xRadius, float yRadius, int start, int end, int split) {
        for (int i = end; i >= start; i -= split) {
            GL11.glVertex2d((double)((double)x + Math.sin((double)i * Math.PI / 180.0) * (double)xRadius), (double)((double)y + Math.cos((double)i * Math.PI / 180.0) * (double)yRadius));
        }
    }

    public static void drawCircle(float x, float y, float radius, int color) {
        RenderUtilsNoteless.glColor(color);
        GLUtils.glEnable(3042);
        GLUtils.glDisable(3553);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtils.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double)((double)x + Math.sin((double)i * Math.PI / 180.0) * (double)radius), (double)((double)y + Math.cos((double)i * Math.PI / 180.0) * (double)radius));
        }
        GL11.glEnd();
        GL11.glPopMatrix();
        GLUtils.glEnable(3553);
        GLUtils.glDisable(2848);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void quickDrawGradientSidewaysH(double left, double top, double right, double bottom, int col1, int col2) {
        GL11.glBegin((int)7);
        RenderUtilsNoteless.glColor(col1);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)left, (double)bottom);
        RenderUtilsNoteless.glColor(col2);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glEnd();
    }

    public static void drawRoundedCornerRect(float x, float y, float x1, float y1, float radius) {
        GL11.glBegin((int)9);
        float xRadius = (float)Math.min((double)(x1 - x) * 0.5, (double)radius);
        float yRadius = (float)Math.min((double)(y1 - y) * 0.5, (double)radius);
        RenderUtilsNoteless.quickPolygonCircle(x + xRadius, y + yRadius, xRadius, yRadius, 180, 270, 4);
        RenderUtilsNoteless.quickPolygonCircle(x1 - xRadius, y + yRadius, xRadius, yRadius, 90, 180, 4);
        RenderUtilsNoteless.quickPolygonCircle(x1 - xRadius, y1 - yRadius, xRadius, yRadius, 0, 90, 4);
        RenderUtilsNoteless.quickPolygonCircle(x + xRadius, y1 - yRadius, xRadius, yRadius, 270, 360, 4);
        GL11.glEnd();
    }

    public static void drawCircleFull(float x, float y, float radius, float Bord, Color color) {
        RenderUtilsNoteless.drawCircle(x, y, radius + 0.15f, color);
        RenderUtilsNoteless.drawCircleD(x, y, radius, color.getRGB());
    }

    public static void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDisable((int)2896);
        GL11.glDepthMask((boolean)false);
        GL11.glHint((int)3154, (int)4354);
    }

    public static void post3D() {
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static int getNormalRainbow(int delay, float sat, float brg) {
        double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 20.0);
        return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), sat, brg).getRGB();
    }

    public static void drawCircle(float x, float y, float radius, Color color) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        RenderUtilsNoteless.glColor(Color.WHITE);
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

    public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        RenderUtilsNoteless.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glLineWidth((float)lineWdith);
        GL11.glColor4f((float)lineRed, (float)lineGreen, (float)lineBlue, (float)lineAlpha);
        RenderUtilsNoteless.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void stopDrawing() {
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
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
        RenderUtilsNoteless.drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        GL11.glLineWidth((float)a6);
        GL11.glColor4f((float)a12, (float)a13, (float)a14, (float)a11);
        RenderUtilsNoteless.drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
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

    public static void drawBordered(double x2, double y2, double width, double height, double length, int innerColor, int outerColor) {
        RenderUtilsNoteless.drawRect(x2, y2, x2 + width, y2 + height, innerColor);
        RenderUtilsNoteless.drawRect(x2 - length, y2, x2, y2 + height, outerColor);
        RenderUtilsNoteless.drawRect(x2 - length, y2 - length, x2 + width, y2, outerColor);
        RenderUtilsNoteless.drawRect(x2 + width, y2 - length, x2 + width + length, y2 + height + length, outerColor);
        RenderUtilsNoteless.drawRect(x2 - length, y2 + height, x2 + width, y2 + height + length, outerColor);
    }

    public static void quickDrawHead(ResourceLocation skin, int x, int y, int width, int height) {
        mc.func_110434_K().func_110577_a(skin);
        RenderUtils.drawScaledCustomSizeModalRect(x, y, 8.0f, 8.0f, 8, 8, width, height, 64.0f, 64.0f);
        RenderUtils.drawScaledCustomSizeModalRect(x, y, 40.0f, 8.0f, 8, 8, width, height, 64.0f, 64.0f);
    }

    public static void drawScaledCustomSizeModalCircle(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0f / tileWidth;
        float f1 = 1.0f / tileHeight;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(9, DefaultVertexFormats.field_181707_g);
        float xRadius = (float)width / 2.0f;
        float yRadius = (float)height / 2.0f;
        float uRadius = ((u + (float)uWidth) * f - u * f) / 2.0f;
        float vRadius = ((v + (float)vHeight) * f1 - v * f1) / 2.0f;
        for (int i = 0; i <= 360; i += 10) {
            double xPosOffset = Math.sin((double)i * Math.PI / 180.0);
            double yPosOffset = Math.cos((double)i * Math.PI / 180.0);
            worldrenderer.func_181662_b((double)((float)x + xRadius) + xPosOffset * (double)xRadius, (double)((float)y + yRadius) + yPosOffset * (double)yRadius, 0.0).func_181673_a((double)(u * f + uRadius) + xPosOffset * (double)uRadius, (double)(v * f1 + vRadius) + yPosOffset * (double)vRadius).func_181675_d();
        }
        tessellator.func_78381_a();
    }

    public static void drawFace(int x, int y, float scale, AbstractClientPlayer target) {
        try {
            ResourceLocation skin = target.func_110306_p();
            Minecraft.func_71410_x().func_110434_K().func_110577_a(skin);
            GL11.glEnable((int)3042);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            Gui.func_152125_a((int)x, (int)y, (float)8.0f, (float)8.0f, (int)8, (int)8, (int)((int)scale), (int)((int)scale), (float)64.0f, (float)64.0f);
            GL11.glDisable((int)3042);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void drawEntityBox(Entity entity, Color color, boolean outline, boolean box, float outlineWidth) {
        RenderManager renderManager = mc.func_175598_ae();
        Timer timer = RenderUtilsNoteless.mc.field_71428_T;
        GL11.glBlendFunc((int)770, (int)771);
        RenderUtilsNoteless.enableGlCap(3042);
        RenderUtilsNoteless.disableGlCap(3553, 2929);
        GL11.glDepthMask((boolean)false);
        double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)timer.field_74281_c - renderManager.field_78725_b;
        double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)timer.field_74281_c - renderManager.field_78726_c;
        double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)timer.field_74281_c - renderManager.field_78723_d;
        AxisAlignedBB entityBox = entity.func_174813_aQ();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(entityBox.field_72340_a - entity.field_70165_t + x - 0.05, entityBox.field_72338_b - entity.field_70163_u + y, entityBox.field_72339_c - entity.field_70161_v + z - 0.05, entityBox.field_72336_d - entity.field_70165_t + x + 0.05, entityBox.field_72337_e - entity.field_70163_u + y + 0.15, entityBox.field_72334_f - entity.field_70161_v + z + 0.05);
        if (outline) {
            GL11.glLineWidth((float)outlineWidth);
            RenderUtilsNoteless.enableGlCap(2848);
            RenderUtilsNoteless.glColor(color.getRed(), color.getGreen(), color.getBlue(), box ? 170 : 255);
            RenderUtilsNoteless.drawSelectionBoundingBox(axisAlignedBB);
        }
        if (box) {
            RenderUtilsNoteless.glColor(color.getRed(), color.getGreen(), color.getBlue(), outline ? 26 : 35);
            RenderUtilsNoteless.drawFilledBox(axisAlignedBB);
        }
        GlStateManager.func_179117_G();
        GL11.glDepthMask((boolean)true);
        RenderUtilsNoteless.resetCaps();
    }

    public static void glColor(Color color, int alpha) {
        RenderUtilsNoteless.glColor(color, (float)alpha / 255.0f);
    }

    public static void glColor(Color color, float alpha) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
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

    public static void drawImage(ResourceLocation image2, double x, double y, double width, double height, int color) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179117_G();
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        Color color1 = new Color(color);
        GL11.glColor4f((float)((float)color1.getRed() / 255.0f), (float)((float)color1.getGreen() / 255.0f), (float)((float)color1.getBlue() / 255.0f), (float)((float)color1.getAlpha() / 255.0f));
        Minecraft.func_71410_x().func_110434_K().func_110577_a(image2);
        RenderUtils.drawModalRectWithCustomSizedTexture((float)x, (float)y, 0.0f, 0.0f, (float)width, (float)height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GlStateManager.func_179121_F();
    }

    public static void pre() {
        GL11.glDisable((int)2929);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
    }

    public static void drawFace(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight, AbstractClientPlayer target) {
        try {
            ResourceLocation e = target.func_110306_p();
            mc.func_110434_K().func_110577_a(e);
            GL11.glEnable((int)3042);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            Gui.func_152125_a((int)x, (int)y, (float)u, (float)v, (int)uWidth, (int)vHeight, (int)width, (int)height, (float)tileWidth, (float)tileHeight);
            GL11.glDisable((int)3042);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void drawRectBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        RenderUtilsNoteless.rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        RenderUtilsNoteless.rectangle(x + width, y, x1 - width, y + width, borderColor);
        RenderUtilsNoteless.rectangle(x, y, x + width, y1, borderColor);
        RenderUtilsNoteless.rectangle(x1 - width, y, x1, y1, borderColor);
        RenderUtilsNoteless.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
    }

    public static Vector3d project(double x, double y, double z) {
        FloatBuffer vector = GLAllocation.func_74529_h((int)4);
        GL11.glGetFloat((int)2982, (FloatBuffer)modelview);
        GL11.glGetFloat((int)2983, (FloatBuffer)projections);
        GL11.glGetInteger((int)2978, (IntBuffer)viewport);
        if (GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)modelview, (FloatBuffer)projections, (IntBuffer)viewport, (FloatBuffer)vector)) {
            return new Vector3d((double)(vector.get(0) / (float)RenderUtilsNoteless.getResolution().func_78325_e()), (double)(((float)Display.getHeight() - vector.get(1)) / (float)RenderUtilsNoteless.getResolution().func_78325_e()), (double)vector.get(2));
        }
        return null;
    }

    public static void MoondrawRect(double x, double y, double width, double height, int color) {
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        RenderUtilsNoteless.MoondrawRect2(x, y, x + width, y + height, color);
    }

    public static void MoondrawRect2(double left, double top, double right, double bottom, int color) {
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

    public static void drawShadow(float x, float y, float width, float height) {
        RenderUtilsNoteless.drawTexturedRect(x - 9.0f, y - 9.0f, 9.0f, 9.0f, "paneltopleft");
        RenderUtilsNoteless.drawTexturedRect(x - 9.0f, y + height, 9.0f, 9.0f, "panelbottomleft");
        RenderUtilsNoteless.drawTexturedRect(x + width, y + height, 9.0f, 9.0f, "panelbottomright");
        RenderUtilsNoteless.drawTexturedRect(x + width, y - 9.0f, 9.0f, 9.0f, "paneltopright");
        RenderUtilsNoteless.drawTexturedRect(x - 9.0f, y, 9.0f, height, "panelleft");
        RenderUtilsNoteless.drawTexturedRect(x + width, y, 9.0f, height, "panelright");
        RenderUtilsNoteless.drawTexturedRect(x, y - 9.0f, width, 9.0f, "paneltop");
        RenderUtilsNoteless.drawTexturedRect(x, y + height, width, 9.0f, "panelbottom");
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
        mc.func_110434_K().func_110577_a(new ResourceLocation("Insane/" + image2 + ".png"));
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

    public static void drawTexturedRect(float x, float y, float width, float height, String image2, ScaledResolution sr) {
        GL11.glPushMatrix();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        mc.func_110434_K().func_110577_a(new ResourceLocation("liquidbounce/" + image2 + ".png"));
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Gui.func_146110_a((int)((int)x), (int)((int)y), (float)0.0f, (float)0.0f, (int)((int)width), (int)((int)height), (float)((int)width), (float)((int)height));
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
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
        for (i = 0.0; i <= 90.0; i += 0.25) {
            GL11.glVertex2d((double)(xBR + Math.sin(i * degree) * (double)rBR), (double)(yBR + Math.cos(i * degree) * (double)rBR));
        }
        for (i = 90.0; i <= 180.0; i += 0.25) {
            GL11.glVertex2d((double)(xTR + Math.sin(i * degree) * (double)rTR), (double)(yTR + Math.cos(i * degree) * (double)rTR));
        }
        for (i = 180.0; i <= 270.0; i += 0.25) {
            GL11.glVertex2d((double)(xTL + Math.sin(i * degree) * (double)rTL), (double)(yTL + Math.cos(i * degree) * (double)rTL));
        }
        for (i = 270.0; i <= 360.0; i += 0.25) {
            GL11.glVertex2d((double)(xBL + Math.sin(i * degree) * (double)rBL), (double)(yBL + Math.cos(i * degree) * (double)rBL));
        }
        GL11.glEnd();
        GLUtils.glEnable(3553);
        GLUtils.glDisable(3042);
        GLUtils.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return RenderUtilsNoteless.isInViewFrustrum(entity.func_174813_aQ()) || entity.field_70158_ak;
    }

    private static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = mc.func_175606_aa();
        frustrum.func_78547_a(current.field_70165_t, current.field_70163_u, current.field_70161_v);
        return frustrum.func_78546_a(bb);
    }

    public static void drawBlockBox(BlockPos blockPos, Color color, boolean outline, boolean box, float outlineWidth) {
        RenderManager renderManager = mc.func_175598_ae();
        Timer timer = RenderUtilsNoteless.mc.field_71428_T;
        double x = (double)blockPos.func_177958_n() - renderManager.field_78725_b;
        double y = (double)blockPos.func_177956_o() - renderManager.field_78726_c;
        double z = (double)blockPos.func_177952_p() - renderManager.field_78723_d;
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        Block block = BlockUtils.getBlock(blockPos);
        if (block != null) {
            EntityPlayerSP player = RenderUtilsNoteless.mc.field_71439_g;
            double posX = player.field_70142_S + (player.field_70165_t - player.field_70142_S) * (double)timer.field_74281_c;
            double posY = player.field_70137_T + (player.field_70163_u - player.field_70137_T) * (double)timer.field_74281_c;
            double posZ = player.field_70136_U + (player.field_70161_v - player.field_70136_U) * (double)timer.field_74281_c;
            axisAlignedBB = block.func_180646_a((World)RenderUtilsNoteless.mc.field_71441_e, blockPos).func_72314_b((double)0.002f, (double)0.002f, (double)0.002f).func_72317_d(-posX, -posY, -posZ);
        }
        GL11.glBlendFunc((int)770, (int)771);
        RenderUtilsNoteless.enableGlCap(3042);
        RenderUtilsNoteless.disableGlCap(3553, 2929);
        GL11.glDepthMask((boolean)false);
        if (box) {
            RenderUtilsNoteless.glColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() != 255 ? color.getAlpha() : (outline ? 26 : 35));
            RenderUtilsNoteless.drawFilledBox(axisAlignedBB);
        }
        if (outline) {
            GL11.glLineWidth((float)outlineWidth);
            RenderUtilsNoteless.enableGlCap(2848);
            RenderUtilsNoteless.glColor(color);
            RenderUtilsNoteless.drawSelectionBoundingBox(axisAlignedBB);
        }
        GlStateManager.func_179117_G();
        GL11.glDepthMask((boolean)true);
        RenderUtilsNoteless.resetCaps();
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
        for (i = 0.0; i <= 90.0; i += 0.25) {
            GL11.glVertex2d((double)(x2 + Math.sin(i * degree) * (double)radius), (double)(y2 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 90.0; i <= 180.0; i += 0.25) {
            GL11.glVertex2d((double)(x2 + Math.sin(i * degree) * (double)radius), (double)(y1 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 180.0; i <= 270.0; i += 0.25) {
            GL11.glVertex2d((double)(x1 + Math.sin(i * degree) * (double)radius), (double)(y1 + Math.cos(i * degree) * (double)radius));
        }
        for (i = 270.0; i <= 360.0; i += 0.25) {
            GL11.glVertex2d((double)(x1 + Math.sin(i * degree) * (double)radius), (double)(y2 + Math.cos(i * degree) * (double)radius));
        }
        GL11.glEnd();
        GLUtils.glDisable(2848);
    }

    public static int SkyRainbow(int var2, float st, float bright) {
        double d;
        double v1 = Math.ceil(System.currentTimeMillis() + (long)(var2 * 109)) / 5.0;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(v1 / 360.0)) : (float)((v1 %= 360.0) / 360.0), st, bright).getRGB();
    }

    public static int getRainbowOpaque(int seconds, float saturation, float brightness, int index) {
        float hue = (float)((System.currentTimeMillis() + (long)index) % (long)(seconds * 1000)) / (float)(seconds * 1000);
        int color = Color.HSBtoRGB(hue, saturation, brightness);
        return color;
    }

    public static Color skyRainbow(int var2, float st, float bright) {
        double d;
        double v1 = Math.ceil(System.currentTimeMillis() + (long)(var2 * 109)) / 5.0;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(v1 / 360.0)) : (float)((v1 %= 360.0) / 360.0), st, bright);
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

    public static int height() {
        return new ScaledResolution(Minecraft.func_71410_x()).func_78328_b();
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
        RenderItem renderItem = Minecraft.func_71410_x().func_175599_af();
        Minecraft.func_71410_x();
        renderItem.func_175030_a(RenderUtilsNoteless.mc.field_71466_p, stack, x, y);
        Minecraft.func_71410_x().func_175599_af().field_77023_b = 0.0f;
        RenderHelper.func_74518_a();
        GlStateManager.func_179129_p();
        GlStateManager.func_179141_d();
        GlStateManager.func_179084_k();
        GlStateManager.func_179140_f();
        double s = 0.5;
        GlStateManager.func_179139_a((double)0.5, (double)0.5, (double)0.5);
        GlStateManager.func_179097_i();
        GlStateManager.func_179126_j();
        GlStateManager.func_179152_a((float)2.0f, (float)2.0f, (float)2.0f);
        GlStateManager.func_179121_F();
    }

    public static void quickDrawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        float f4 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(col2 & 0xFF) / 255.0f;
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
    }

    public static boolean isHovering(int n, int n2, float n3, float n4, float n5, float n6) {
        boolean b = (float)n > n3 && (float)n < n5 && (float)n2 > n4 && (float)n2 < n6;
        return b;
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

    public static void drawblock2(double a, double a2, double a3, int a4, int a5, float a6) {
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
        RenderUtilsNoteless.drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        GL11.glLineWidth((float)a6);
        GL11.glColor4f((float)a12, (float)a13, (float)a14, (float)a11);
        RenderUtilsNoteless.drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawCircleRect(float x, float y, float x1, float y1, float radius, int color) {
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        float var11 = (float)(color >> 24 & 0xFF) / 255.0f;
        float var6 = (float)(color >> 16 & 0xFF) / 255.0f;
        float var7 = (float)(color >> 8 & 0xFF) / 255.0f;
        float var8 = (float)(color & 0xFF) / 255.0f;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)var6, (float)var7, (float)var8, (float)var11);
        RenderUtilsNoteless.quickRenderCircle(x1 - radius, y1 - radius, 0.0, 90.0, radius, radius);
        RenderUtilsNoteless.quickRenderCircle(x + radius, y1 - radius, 90.0, 180.0, radius, radius);
        RenderUtilsNoteless.quickRenderCircle(x + radius, y + radius, 180.0, 270.0, radius, radius);
        RenderUtilsNoteless.quickRenderCircle(x1 - radius, y + radius, 270.0, 360.0, radius, radius);
        RenderUtilsNoteless.quickDrawRect(x + radius, y + radius, x1 - radius, y1 - radius);
        RenderUtilsNoteless.quickDrawRect(x, y + radius, x + radius, y1 - radius);
        RenderUtilsNoteless.quickDrawRect(x1 - radius, y + radius, x1, y1 - radius);
        RenderUtilsNoteless.quickDrawRect(x + radius, y, x1 - radius, y + radius);
        RenderUtilsNoteless.quickDrawRect(x + radius, y1 - radius, x1 - radius, y1);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
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
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)var6, (float)var7, (float)var8, (float)var11);
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(left, bottom, 0.0).func_181675_d();
        worldRenderer.func_181662_b(right, bottom, 0.0).func_181675_d();
        worldRenderer.func_181662_b(right, top, 0.0).func_181675_d();
        worldRenderer.func_181662_b(left, top, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = 0.003921569f * (float)c.getRed();
        float g = 0.003921569f * (float)c.getGreen();
        float b = 0.003921569f * (float)c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }

    public static Color reAlpha(Color cIn, float alpha) {
        return new Color((float)cIn.getRed() / 255.0f, (float)cIn.getGreen() / 255.0f, (float)cIn.getBlue() / 255.0f, (float)cIn.getAlpha() / 255.0f * alpha);
    }

    public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        RenderUtils.rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.rectangle(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.rectangle(x, y, x + width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.rectangle(x1 - width, y, x1, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtils.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void Gamesense(double x, double y, double x1, double y1, double size, float color1, float color2, float color3) {
        RenderUtils.rectangleBordered(x, y, x1 + size, y1 + size, 0.5, Colors2.getColor(90), Colors2.getColor(0));
        RenderUtils.rectangleBordered(x + 1.0, y + 1.0, x1 + size - 1.0, y1 + size - 1.0, 1.0, Colors2.getColor(90), Colors2.getColor(61));
        RenderUtils.rectangleBordered(x + 2.5, y + 2.5, x1 + size - 2.5, y1 + size - 2.5, 0.5, Colors2.getColor(61), Colors2.getColor(0));
        RenderUtils.drawGradientSideways(x + size * 3.0, y + 3.0, x1 - size * 2.0, y + 4.0, (int)color2, (int)color3);
    }

    public static void drawLimitedCircle(float lx, float ly, float x2, float y2, int xx, int yy, float radius, Color color) {
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
            GL11.glVertex2f((float)Math.min(x2, Math.max((float)xx + x, lx)), (float)Math.min(y2, Math.max((float)yy + y, ly)));
        }
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glEnd();
        GL11.glPopAttrib();
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

    public static void drawEntityKillAuraESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        RenderUtilsNoteless.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glLineWidth((float)lineWdith);
        GL11.glColor4f((float)lineRed, (float)lineGreen, (float)lineBlue, (float)lineAlpha);
        RenderUtilsNoteless.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
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

    public static void drawHead(ResourceLocation skin, int x, int y, int width, int height) {
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        mc.func_110434_K().func_110577_a(skin);
        RenderUtils.drawScaledCustomSizeModalRect(x, y, 8.0f, 8.0f, 8, 8, width, height, 64.0f, 64.0f);
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

    public static Color skyRainbow(int var2, float bright, float st, double speed) {
        double d;
        double v1 = Math.ceil((double)System.currentTimeMillis() / speed + (double)((long)var2 * 109L)) / 5.0;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(v1 / 360.0)) : (float)((v1 %= 360.0) / 360.0), st, bright);
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

    public static void glColor1(int hex) {
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
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

    public static void drawBlockBox(BlockPos blockPos, Color color, boolean outline) {
        RenderManager renderManager = mc.func_175598_ae();
        Timer timer = RenderUtilsNoteless.mc.field_71428_T;
        double x = (double)blockPos.func_177958_n() - renderManager.field_78725_b;
        double y = (double)blockPos.func_177956_o() - renderManager.field_78726_c;
        double z = (double)blockPos.func_177952_p() - renderManager.field_78723_d;
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        Block block = BlockUtils.getBlock(blockPos);
        if (block != null) {
            EntityPlayerSP player = RenderUtilsNoteless.mc.field_71439_g;
            double posX = player.field_70142_S + (player.field_70165_t - player.field_70142_S) * (double)timer.field_74281_c;
            double posY = player.field_70137_T + (player.field_70163_u - player.field_70137_T) * (double)timer.field_74281_c;
            double posZ = player.field_70136_U + (player.field_70161_v - player.field_70136_U) * (double)timer.field_74281_c;
            axisAlignedBB = block.func_180646_a((World)RenderUtilsNoteless.mc.field_71441_e, blockPos).func_72314_b((double)0.002f, (double)0.002f, (double)0.002f).func_72317_d(-posX, -posY, -posZ);
        }
        GL11.glBlendFunc((int)770, (int)771);
        RenderUtilsNoteless.enableGlCap(3042);
        RenderUtilsNoteless.disableGlCap(3553, 2929);
        GL11.glDepthMask((boolean)false);
        RenderUtilsNoteless.glColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() != 255 ? color.getAlpha() : (outline ? 26 : 35));
        RenderUtilsNoteless.drawFilledBox(axisAlignedBB);
        if (outline) {
            GL11.glLineWidth((float)1.0f);
            RenderUtilsNoteless.enableGlCap(2848);
            RenderUtilsNoteless.glColor(color);
            RenderUtilsNoteless.drawSelectionBoundingBox(axisAlignedBB);
        }
        GlStateManager.func_179117_G();
        GL11.glDepthMask((boolean)true);
        RenderUtilsNoteless.resetCaps();
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
        Timer timer = RenderUtilsNoteless.mc.field_71428_T;
        GL11.glBlendFunc((int)770, (int)771);
        RenderUtilsNoteless.enableGlCap(3042);
        RenderUtilsNoteless.disableGlCap(3553, 2929);
        GL11.glDepthMask((boolean)false);
        double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)timer.field_74281_c - renderManager.field_78725_b;
        double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)timer.field_74281_c - renderManager.field_78726_c;
        double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)timer.field_74281_c - renderManager.field_78723_d;
        AxisAlignedBB entityBox = entity.func_174813_aQ();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(entityBox.field_72340_a - entity.field_70165_t + x - 0.05, entityBox.field_72338_b - entity.field_70163_u + y, entityBox.field_72339_c - entity.field_70161_v + z - 0.05, entityBox.field_72336_d - entity.field_70165_t + x + 0.05, entityBox.field_72337_e - entity.field_70163_u + y + 0.15, entityBox.field_72334_f - entity.field_70161_v + z + 0.05);
        if (outline) {
            GL11.glLineWidth((float)1.0f);
            RenderUtilsNoteless.enableGlCap(2848);
            RenderUtilsNoteless.glColor(color.getRed(), color.getGreen(), color.getBlue(), 95);
            RenderUtilsNoteless.drawSelectionBoundingBox(axisAlignedBB);
        }
        RenderUtilsNoteless.glColor(color.getRed(), color.getGreen(), color.getBlue(), outline ? 26 : 35);
        RenderUtilsNoteless.drawFilledBox(axisAlignedBB);
        GlStateManager.func_179117_G();
        GL11.glDepthMask((boolean)true);
        RenderUtilsNoteless.resetCaps();
    }

    public static void drawAxisAlignedBB(AxisAlignedBB axisAlignedBB, Color color) {
        GL11.glBlendFunc((int)770, (int)771);
        GLUtils.glEnable(3042);
        GL11.glLineWidth((float)2.0f);
        GLUtils.glDisable(3553);
        GLUtils.glDisable(2929);
        GL11.glDepthMask((boolean)false);
        RenderUtilsNoteless.glColor(color);
        RenderUtilsNoteless.drawFilledBox(axisAlignedBB);
        GlStateManager.func_179117_G();
        GLUtils.glEnable(3553);
        GLUtils.glEnable(2929);
        GL11.glDepthMask((boolean)true);
        GLUtils.glDisable(3042);
    }

    public static void drawPlatform(double y, Color color, double size) {
        RenderManager renderManager = mc.func_175598_ae();
        double renderY = y - renderManager.field_78726_c;
        RenderUtilsNoteless.drawAxisAlignedBB(new AxisAlignedBB(size, renderY + 0.02, size, -size, renderY, -size), color);
    }

    public static void drawPlatform(Entity entity, Color color) {
        RenderManager renderManager = mc.func_175598_ae();
        Timer timer = RenderUtilsNoteless.mc.field_71428_T;
        double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)timer.field_74281_c - renderManager.field_78725_b;
        double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)timer.field_74281_c - renderManager.field_78726_c;
        double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)timer.field_74281_c - renderManager.field_78723_d;
        AxisAlignedBB axisAlignedBB = entity.func_174813_aQ().func_72317_d(-entity.field_70165_t, -entity.field_70163_u, -entity.field_70161_v).func_72317_d(x, y, z);
        RenderUtilsNoteless.drawAxisAlignedBB(new AxisAlignedBB(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e + 0.2, axisAlignedBB.field_72339_c, axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e + 0.26, axisAlignedBB.field_72334_f), color);
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

    public static void quickDrawRect(float x, float y, float x2, float y2) {
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
    }

    public static void drawRect(float left, float top, float right, float bottom, int color) {
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

    public static void drawRect(double x, double y, double x2, double y2, int color) {
        GLUtils.glEnable(3042);
        GLUtils.glDisable(3553);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtils.glEnable(2848);
        RenderUtilsNoteless.glColor(color);
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

    public static void drawRect(float x, float y, float x2, float y2, Color color) {
        RenderUtilsNoteless.drawRect(x, y, x2, y2, color.getRGB());
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float width, int color1, int color2) {
        RenderUtilsNoteless.drawRect(x, y, x2, y2, color2);
        GLUtils.glEnable(3042);
        GLUtils.glDisable(3553);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtils.glEnable(2848);
        RenderUtilsNoteless.glColor(color1);
        GL11.glLineWidth((float)width);
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
        GLUtils.glEnable(3553);
        GLUtils.glDisable(3042);
        GLUtils.glDisable(2848);
    }

    public static void drawLoadingCircle(float x, float y) {
        for (int i = 0; i < 4; ++i) {
            int rot = (int)(System.nanoTime() / 5000000L * (long)i % 360L);
            RenderUtilsNoteless.drawCircle(x, y, i * 10, rot - 180, rot);
        }
    }

    public static void drawCircle(float x, float y, float radius, int start, int end) {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        RenderUtilsNoteless.glColor(Color.WHITE);
        GLUtils.glEnable(2848);
        GL11.glLineWidth((float)2.0f);
        GL11.glBegin((int)3);
        for (float i = (float)end; i >= (float)start; i -= 4.0f) {
            GL11.glVertex2f((float)((float)((double)x + Math.cos((double)i * Math.PI / 180.0) * (double)(radius * 1.001f))), (float)((float)((double)y + Math.sin((double)i * Math.PI / 180.0) * (double)(radius * 1.001f))));
        }
        GL11.glEnd();
        GLUtils.glDisable(2848);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
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

    public static void autoExhibition(double x, double y, double x1, double y1, double size) {
        RenderUtilsNoteless.rectangleBordered(x, y, x1 + size, y1 + size, 0.5, Colors.getColor(90), Colors.getColor(0));
        RenderUtilsNoteless.rectangleBordered(x + 1.0, y + 1.0, x1 + size - 1.0, y1 + size - 1.0, 1.0, Colors.getColor(90), Colors.getColor(61));
        RenderUtilsNoteless.rectangleBordered(x + 2.5, y + 2.5, x1 + size - 2.5, y1 + size - 2.5, 0.5, Colors.getColor(61), Colors.getColor(0));
    }

    public static void draw2D(EntityLivingBase entity, double posX, double posY, double posZ, int color, int backgroundColor) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)posX, (double)posY, (double)posZ);
        GlStateManager.func_179114_b((float)(-RenderUtilsNoteless.mc.func_175598_ae().field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179139_a((double)-0.1, (double)-0.1, (double)0.1);
        GLUtils.glDisable(2929);
        GLUtils.glEnable(3042);
        GLUtils.glDisable(3553);
        GL11.glBlendFunc((int)770, (int)771);
        GlStateManager.func_179132_a((boolean)true);
        RenderUtilsNoteless.glColor(color);
        GL11.glCallList((int)DISPLAY_LISTS_2D[0]);
        RenderUtilsNoteless.glColor(backgroundColor);
        GL11.glCallList((int)DISPLAY_LISTS_2D[1]);
        GlStateManager.func_179137_b((double)0.0, (double)(21.0 + -(entity.func_174813_aQ().field_72337_e - entity.func_174813_aQ().field_72338_b) * 12.0), (double)0.0);
        RenderUtilsNoteless.glColor(color);
        GL11.glCallList((int)DISPLAY_LISTS_2D[2]);
        RenderUtilsNoteless.glColor(backgroundColor);
        GL11.glCallList((int)DISPLAY_LISTS_2D[3]);
        GLUtils.glEnable(2929);
        GLUtils.glEnable(3553);
        GLUtils.glDisable(3042);
        GlStateManager.func_179121_F();
    }

    public static void draw2D(BlockPos blockPos, int color, int backgroundColor) {
        RenderManager renderManager = mc.func_175598_ae();
        double posX = (double)blockPos.func_177958_n() + 0.5 - renderManager.field_78725_b;
        double posY = (double)blockPos.func_177956_o() - renderManager.field_78726_c;
        double posZ = (double)blockPos.func_177952_p() + 0.5 - renderManager.field_78723_d;
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)posX, (double)posY, (double)posZ);
        GlStateManager.func_179114_b((float)(-RenderUtilsNoteless.mc.func_175598_ae().field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179139_a((double)-0.1, (double)-0.1, (double)0.1);
        GLUtils.glDisable(2929);
        GLUtils.glEnable(3042);
        GLUtils.glDisable(3553);
        GL11.glBlendFunc((int)770, (int)771);
        GlStateManager.func_179132_a((boolean)true);
        RenderUtilsNoteless.glColor(color);
        GL11.glCallList((int)DISPLAY_LISTS_2D[0]);
        RenderUtilsNoteless.glColor(backgroundColor);
        GL11.glCallList((int)DISPLAY_LISTS_2D[1]);
        GlStateManager.func_179109_b((float)0.0f, (float)9.0f, (float)0.0f);
        RenderUtilsNoteless.glColor(color);
        GL11.glCallList((int)DISPLAY_LISTS_2D[2]);
        RenderUtilsNoteless.glColor(backgroundColor);
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
        GL11.glRotatef((float)(-RenderUtilsNoteless.mc.func_175598_ae().field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)RenderUtilsNoteless.mc.func_175598_ae().field_78732_j, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glScalef((float)-0.05f, (float)-0.05f, (float)0.05f);
        RenderUtilsNoteless.setGlCap(2896, false);
        RenderUtilsNoteless.setGlCap(2929, false);
        RenderUtilsNoteless.setGlCap(3042, true);
        GL11.glBlendFunc((int)770, (int)771);
        int width = Fonts.font35.func_78256_a(string) / 2;
        Gui.func_73734_a((int)(-width - 1), (int)-1, (int)(width + 1), (int)Fonts.font35.field_78288_b, (int)Integer.MIN_VALUE);
        Fonts.font35.func_175065_a(string, -width, 1.5f, Color.WHITE.getRGB(), true);
        RenderUtilsNoteless.resetCaps();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
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

    public static double[] convertTo2D(double x, double y, double z) {
        double[] dArray;
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer((int)3);
        IntBuffer viewport = BufferUtils.createIntBuffer((int)16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer((int)16);
        FloatBuffer projection = BufferUtils.createFloatBuffer((int)16);
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)projection);
        GL11.glGetInteger((int)2978, (IntBuffer)viewport);
        boolean result = GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)modelView, (FloatBuffer)projection, (IntBuffer)viewport, (FloatBuffer)screenCoords);
        if (result) {
            double[] dArray2 = new double[3];
            dArray2[0] = screenCoords.get(0);
            dArray2[1] = (float)Display.getHeight() - screenCoords.get(1);
            dArray = dArray2;
            dArray2[2] = screenCoords.get(2);
        } else {
            dArray = null;
        }
        return dArray;
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
        RenderUtilsNoteless.setGlCap(cap, true);
    }

    public static void enableGlCap(int ... caps) {
        for (int cap : caps) {
            RenderUtilsNoteless.setGlCap(cap, true);
        }
    }

    public static void disableGlCap(int cap) {
        RenderUtilsNoteless.setGlCap(cap, true);
    }

    public static void disableGlCap(int ... caps) {
        for (int cap : caps) {
            RenderUtilsNoteless.setGlCap(cap, false);
        }
    }

    public static void setGlCap(int cap, boolean state) {
        glCapMap.put(cap, GL11.glGetBoolean((int)cap));
        RenderUtilsNoteless.setGlState(cap, state);
    }

    public static void setGlState(int cap, boolean state) {
        if (state) {
            GLUtils.glEnable(cap);
        } else {
            GLUtils.glDisable(cap);
        }
    }

    static {
        DISPLAY_LISTS_2D = new int[4];
        for (int i = 0; i < DISPLAY_LISTS_2D.length; ++i) {
            RenderUtilsNoteless.DISPLAY_LISTS_2D[i] = GL11.glGenLists((int)1);
        }
        GL11.glNewList((int)DISPLAY_LISTS_2D[0], (int)4864);
        RenderUtilsNoteless.quickDrawRect(-7.0f, 2.0f, -4.0f, 3.0f);
        RenderUtilsNoteless.quickDrawRect(4.0f, 2.0f, 7.0f, 3.0f);
        RenderUtilsNoteless.quickDrawRect(-7.0f, 0.5f, -6.0f, 3.0f);
        RenderUtilsNoteless.quickDrawRect(6.0f, 0.5f, 7.0f, 3.0f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[1], (int)4864);
        RenderUtilsNoteless.quickDrawRect(-7.0f, 3.0f, -4.0f, 3.3f);
        RenderUtilsNoteless.quickDrawRect(4.0f, 3.0f, 7.0f, 3.3f);
        RenderUtilsNoteless.quickDrawRect(-7.3f, 0.5f, -7.0f, 3.3f);
        RenderUtilsNoteless.quickDrawRect(7.0f, 0.5f, 7.3f, 3.3f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[2], (int)4864);
        RenderUtilsNoteless.quickDrawRect(4.0f, -20.0f, 7.0f, -19.0f);
        RenderUtilsNoteless.quickDrawRect(-7.0f, -20.0f, -4.0f, -19.0f);
        RenderUtilsNoteless.quickDrawRect(6.0f, -20.0f, 7.0f, -17.5f);
        RenderUtilsNoteless.quickDrawRect(-7.0f, -20.0f, -6.0f, -17.5f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[3], (int)4864);
        RenderUtilsNoteless.quickDrawRect(7.0f, -20.0f, 7.3f, -17.5f);
        RenderUtilsNoteless.quickDrawRect(-7.3f, -20.0f, -7.0f, -17.5f);
        RenderUtilsNoteless.quickDrawRect(4.0f, -20.3f, 7.3f, -20.0f);
        RenderUtilsNoteless.quickDrawRect(-7.3f, -20.3f, -4.0f, -20.0f);
        GL11.glEndList();
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

