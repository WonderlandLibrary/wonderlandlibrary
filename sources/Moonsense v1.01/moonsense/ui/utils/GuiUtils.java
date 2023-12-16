// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.utils;

import net.minecraft.util.AxisAlignedBB;
import moonsense.MoonsenseClient;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import moonsense.features.SCModule;
import moonsense.utils.ColorObject;
import moonsense.utils.MathUtil;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.IImageBuffer;
import java.io.File;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.GlStateManager;
import java.util.HashMap;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GuiUtils extends Gui
{
    public static final Minecraft mc;
    public static final GuiUtils INSTANCE;
    private static final Map<String, ResourceLocation> playerSkins;
    
    static {
        playerSkins = new HashMap<String, ResourceLocation>();
        INSTANCE = new GuiUtils();
        mc = Minecraft.getMinecraft();
    }
    
    public static void drawHead(final String name, final int x, final int y, final int size) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final ResourceLocation headLocation = getHeadLocation(name);
        Minecraft.getMinecraft().getTextureManager().bindTexture(headLocation);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, size, size, (float)size, (float)size);
    }
    
    public static ResourceLocation getHeadLocation(final String displayName) {
        final ResourceLocation playerSkin = GuiUtils.playerSkins.getOrDefault(displayName, new ResourceLocation("streamlined/heads/" + displayName + ".png"));
        if (!GuiUtils.playerSkins.containsKey(displayName)) {
            final ThreadDownloadImageData skinData = new ThreadDownloadImageData(null, "https://minotar.net/helm/" + displayName + "/32.png", new ResourceLocation("streamlined/heads/steve.png"), null);
            Minecraft.getMinecraft().getTextureManager().loadTexture(playerSkin, skinData);
            GuiUtils.playerSkins.put(displayName, playerSkin);
        }
        return playerSkin;
    }
    
    public static int drawString(final Object text, final int x, final int y) {
        return drawString(text, x, y, 16777215, false);
    }
    
    public static int drawString(final Object text, final int x, final int y, final int color) {
        return drawString(text, x, y, color, false);
    }
    
    public static int drawString(final Object text, final int x, final int y, final boolean shadow) {
        return drawString(text, x, y, 16777215, shadow);
    }
    
    public static int drawString(final Object text, final int x, final int y, final int color, final boolean shadow) {
        return GuiUtils.mc.fontRendererObj.drawString(String.valueOf(text), (float)x, (float)y, color, shadow);
    }
    
    public static int drawString(final Object text, final float x, final float y) {
        return drawString(text, x, y, 16777215, false);
    }
    
    public static int drawString(final Object text, final float x, final float y, final int color) {
        return drawString(text, x, y, color, false);
    }
    
    public static int drawString(final Object text, final float x, final float y, final boolean shadow) {
        return drawString(text, x, y, 16777215, shadow);
    }
    
    public static int drawString(final Object text, final float x, final float y, final int color, final boolean shadow) {
        return GuiUtils.mc.fontRendererObj.drawString(String.valueOf(text), x, y, color, shadow);
    }
    
    public static int drawCenteredString(final Object text, final int x, final int y, final int color) {
        return drawCenteredString(text, x, y, color, false);
    }
    
    public static int drawCenteredString(final Object text, final float x, final float y, final int color) {
        return GuiUtils.mc.fontRendererObj.drawString(String.valueOf(text), x - GuiUtils.mc.fontRendererObj.getStringWidth(String.valueOf(text)) / 2.0f, y, color, false);
    }
    
    public static int drawCenteredString(final Object text, final int x, final int y, final int color, final boolean shadow) {
        return GuiUtils.mc.fontRendererObj.drawString(String.valueOf(text), x - GuiUtils.mc.fontRendererObj.getStringWidth(String.valueOf(text)) / 2.0f, (float)y, color, shadow);
    }
    
    public static int drawCenteredString(final Object text, final float x, final float y, final int color, final boolean shadow) {
        return GuiUtils.mc.fontRendererObj.drawString(String.valueOf(text), x - GuiUtils.mc.fontRendererObj.getStringWidth(String.valueOf(text)) / 2.0f, y, color, shadow);
    }
    
    public static int drawCenteredString(final String text, final float x, final float y, final int color, final boolean shadow) {
        return GuiUtils.mc.fontRendererObj.drawString(String.valueOf(text), x - GuiUtils.mc.fontRendererObj.getStringWidth(String.valueOf(text)) / 2.0f, y, color, shadow);
    }
    
    public static void drawRect(float left, float top, float right, float bottom, final int color) {
        if (left < right) {
            final float i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final float j = top;
            top = bottom;
            bottom = j;
        }
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        setGlColor(color);
        worldrenderer.startDrawing(7);
        worldrenderer.addVertex(left, bottom, 0.0);
        worldrenderer.addVertex(right, bottom, 0.0);
        worldrenderer.addVertex(right, top, 0.0);
        worldrenderer.addVertex(left, top, 0.0);
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawColoredRect(final float x, final float y, final float x2, final float y2, final int color, final int color2) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.startDrawing(7);
        setGlColor(color);
        worldRenderer.addVertex(x, y2, 0.0);
        setGlColor(color2);
        worldRenderer.addVertex(x2, y2, 0.0);
        worldRenderer.addVertex(x2, y, 0.0);
        setGlColor(color);
        worldRenderer.addVertex(x, y, 0.0);
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawGradientRect(final int left, final int top, final int right, final int bottom, final int coltl, final int coltr, final int colbl, final int colbr, final int zLevel) {
        drawGradientRect((float)left, (float)top, (float)right, (float)bottom, coltl, coltr, colbl, colbr, zLevel);
    }
    
    public static void drawGradientRect(final float left, final float top, final float right, final float bottom, final int coltl, final int coltr, final int colbl, final int colbr, final int zLevel) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer buffer = tessellator.getWorldRenderer();
        buffer.startDrawing(7);
        buffer.color((coltr & 0xFF0000) >> 16, (coltr & 0xFF00) >> 8, coltr & 0xFF, (coltr & 0xFF000000) >>> 24);
        buffer.addVertex(right, top, zLevel);
        buffer.color((coltl & 0xFF0000) >> 16, (coltl & 0xFF00) >> 8, coltl & 0xFF, (coltl & 0xFF000000) >>> 24);
        buffer.addVertex(left, top, zLevel);
        buffer.color((colbl & 0xFF0000) >> 16, (colbl & 0xFF00) >> 8, colbl & 0xFF, (colbl & 0xFF000000) >>> 24);
        buffer.addVertex(left, bottom, zLevel);
        buffer.color((colbr & 0xFF0000) >> 16, (colbr & 0xFF00) >> 8, colbr & 0xFF, (colbr & 0xFF000000) >>> 24);
        buffer.addVertex(right, bottom, zLevel);
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawRectOutline(final float left, final float top, final float right, final float bottom, final int color) {
        final float width = 0.55f;
        drawRect(left - 0.55f, top - 0.55f, right + 0.55f, top, color);
        drawRect(right, top, right + 0.55f, bottom, color);
        drawRect(left - 0.55f, bottom, right + 0.55f, bottom + 0.55f, color);
        drawRect(left - 0.55f, top, left, bottom, color);
    }
    
    public static void drawRoundedRect(final float nameInt1, final float nameInt2, final float nameInt3, final float nameInt4, final float radius, final int color) {
        final float f1 = (color >> 24 & 0xFF) / 255.0f;
        final float f2 = (color >> 16 & 0xFF) / 255.0f;
        final float f3 = (color >> 8 & 0xFF) / 255.0f;
        final float f4 = (color & 0xFF) / 255.0f;
        GlStateManager.color(f2, f3, f4, f1);
        drawRoundedRect(nameInt1, nameInt2, nameInt3, nameInt4, radius);
    }
    
    private static void drawRoundedRect(final float nameFloat1, final float nameFloat2, final float nameFloat3, final float nameFloat4, final float nameFloat5) {
        final int i = 18;
        final float f1 = 5.0f;
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.enableColorMaterial();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glEnable(2848);
        GL11.glBegin(5);
        GL11.glVertex2f(nameFloat1 + nameFloat5, nameFloat2);
        GL11.glVertex2f(nameFloat1 + nameFloat5, nameFloat4);
        GL11.glVertex2f(nameFloat3 - nameFloat5, nameFloat2);
        GL11.glVertex2f(nameFloat3 - nameFloat5, nameFloat4);
        GL11.glEnd();
        GL11.glBegin(5);
        GL11.glVertex2f(nameFloat1, nameFloat2 + nameFloat5);
        GL11.glVertex2f(nameFloat1 + nameFloat5, nameFloat2 + nameFloat5);
        GL11.glVertex2f(nameFloat1, nameFloat4 - nameFloat5);
        GL11.glVertex2f(nameFloat1 + nameFloat5, nameFloat4 - nameFloat5);
        GL11.glEnd();
        GL11.glBegin(5);
        GL11.glVertex2f(nameFloat3, nameFloat2 + nameFloat5);
        GL11.glVertex2f(nameFloat3 - nameFloat5, nameFloat2 + nameFloat5);
        GL11.glVertex2f(nameFloat3, nameFloat4 - nameFloat5);
        GL11.glVertex2f(nameFloat3 - nameFloat5, nameFloat4 - nameFloat5);
        GL11.glEnd();
        GL11.glBegin(6);
        float f2 = nameFloat3 - nameFloat5;
        float f3 = nameFloat2 + nameFloat5;
        GL11.glVertex2f(f2, f3);
        for (int j = 0; j <= 18; ++j) {
            final float f4 = j * 5.0f;
            GL11.glVertex2f((float)(f2 + nameFloat5 * Math.cos(Math.toRadians(f4))), (float)(f3 - nameFloat5 * Math.sin(Math.toRadians(f4))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f2 = nameFloat1 + nameFloat5;
        f3 = nameFloat2 + nameFloat5;
        GL11.glVertex2f(f2, f3);
        for (int j = 0; j <= 18; ++j) {
            final float f4 = j * 5.0f;
            GL11.glVertex2f((float)(f2 - nameFloat5 * Math.cos(Math.toRadians(f4))), (float)(f3 - nameFloat5 * Math.sin(Math.toRadians(f4))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f2 = nameFloat1 + nameFloat5;
        f3 = nameFloat4 - nameFloat5;
        GL11.glVertex2f(f2, f3);
        for (int j = 0; j <= 18; ++j) {
            final float f4 = j * 5.0f;
            GL11.glVertex2f((float)(f2 - nameFloat5 * Math.cos(Math.toRadians(f4))), (float)(f3 + nameFloat5 * Math.sin(Math.toRadians(f4))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f2 = nameFloat3 - nameFloat5;
        f3 = nameFloat4 - nameFloat5;
        GL11.glVertex2f(f2, f3);
        for (int j = 0; j <= 18; ++j) {
            final float f4 = j * 5.0f;
            GL11.glVertex2f((float)(f2 + nameFloat5 * Math.cos(Math.toRadians(f4))), (float)(f3 + nameFloat5 * Math.sin(Math.toRadians(f4))));
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableColorMaterial();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
    
    public static void drawSelectRoundedRect(float x, float y, float x1, float y1, final float topLeft, final float topRight, final float bottomRight, final float bottomLeft, final int color) {
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);
        x *= 2.0;
        y *= 2.0;
        x1 *= 2.0;
        y1 *= 2.0;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        setGlColor(color);
        GL11.glEnable(2848);
        GL11.glBegin(9);
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + topLeft + Math.sin(i * 3.141592653589793 / 180.0) * topLeft * -1.0, y + topLeft + Math.cos(i * 3.141592653589793 / 180.0) * topLeft * -1.0);
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + bottomLeft + Math.sin(i * 3.141592653589793 / 180.0) * bottomLeft * -1.0, y1 - bottomLeft + Math.cos(i * 3.141592653589793 / 180.0) * bottomLeft * -1.0);
        }
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x1 - bottomRight + Math.sin(i * 3.141592653589793 / 180.0) * bottomRight, y1 - bottomRight + Math.cos(i * 3.141592653589793 / 180.0) * bottomRight);
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x1 - topRight + Math.sin(i * 3.141592653589793 / 180.0) * topRight, y + topRight + Math.cos(i * 3.141592653589793 / 180.0) * topRight);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glPopAttrib();
    }
    
    public static void drawSelectRoundedOutline(float x, float y, float x1, float y1, final float topLeft, final float topRight, final float bottomRight, final float bottomLeft, final float lineWidth, final int color) {
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);
        x *= 2.0;
        y *= 2.0;
        x1 *= 2.0;
        y1 *= 2.0;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        setGlColor(color);
        GL11.glEnable(2848);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(2);
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + topLeft + Math.sin(i * 3.141592653589793 / 180.0) * topLeft * -1.0, y + topLeft + Math.cos(i * 3.141592653589793 / 180.0) * topLeft * -1.0);
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + topRight + Math.sin(i * 3.141592653589793 / 180.0) * topRight * -1.0, y1 - topRight + Math.cos(i * 3.141592653589793 / 180.0) * topRight * -1.0);
        }
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x1 - bottomLeft + Math.sin(i * 3.141592653589793 / 180.0) * bottomLeft, y1 - bottomLeft + Math.cos(i * 3.141592653589793 / 180.0) * bottomLeft);
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x1 - bottomRight + Math.sin(i * 3.141592653589793 / 180.0) * bottomRight, y + bottomRight + Math.cos(i * 3.141592653589793 / 180.0) * bottomRight);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glPopAttrib();
    }
    
    public static void drawRoundedOutline(final int x, final int y, final int x2, final int y2, final float radius, final float width, final int color) {
        setGlColor(color);
        drawRoundedOutline((float)x, (float)y, (float)x2, (float)y2, radius, (width == 1.0f) ? 1.001f : width);
    }
    
    public static void drawRoundedOutline(final float x, final float y, final float x2, final float y2, final float radius, final float width, final int color) {
        setGlColor(color);
        drawRoundedOutline(x, y, x2, y2, radius, (width == 1.0f) ? 1.001f : width);
    }
    
    private static void drawRoundedOutline(final float x, final float y, final float x2, final float y2, final float radius, float width) {
        final int i = 18;
        final int j = 5;
        if (width == 1.0f) {
            width = 1.001f;
        }
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.enableColorMaterial();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glEnable(2848);
        if (width != 1.0f) {
            GL11.glLineWidth(width);
        }
        GL11.glBegin(3);
        GL11.glVertex2f(x + radius, y);
        GL11.glVertex2f(x2 - radius, y);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex2f(x2, y + radius);
        GL11.glVertex2f(x2, y2 - radius);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex2f(x2 - radius, y2 - 0.1f);
        GL11.glVertex2f(x + radius, y2 - 0.1f);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex2f(x + 0.1f, y2 - radius);
        GL11.glVertex2f(x + 0.1f, y + radius);
        GL11.glEnd();
        float f1 = x2 - radius;
        float f2 = y + radius;
        GL11.glBegin(3);
        for (int k = 0; k <= 18; ++k) {
            final int m = 90 - k * 5;
            GL11.glVertex2f((float)(f1 + radius * MathUtil.getRightAngle(m)), (float)(f2 - radius * MathUtil.getAngle(m)));
        }
        GL11.glEnd();
        f1 = x2 - radius;
        f2 = y2 - radius;
        GL11.glBegin(3);
        for (int k = 0; k <= 18; ++k) {
            final int m = k * 5 + 270;
            GL11.glVertex2f((float)(f1 + radius * MathUtil.getRightAngle(m)), (float)(f2 - radius * MathUtil.getAngle(m)));
        }
        GL11.glEnd();
        GL11.glBegin(3);
        f1 = x + radius;
        f2 = y2 - radius;
        for (int k = 0; k <= 18; ++k) {
            final int m = k * 5 + 90;
            GL11.glVertex2f((float)(f1 + radius * MathUtil.getRightAngle(m)), (float)(f2 + radius * MathUtil.getAngle(m)));
        }
        GL11.glEnd();
        GL11.glBegin(3);
        f1 = x + radius;
        f2 = y + radius;
        for (int k = 0; k <= 18; ++k) {
            final int m = 270 - k * 5;
            GL11.glVertex2f((float)(f1 + radius * MathUtil.getRightAngle(m)), (float)(f2 + radius * MathUtil.getAngle(m)));
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        if (width != 1.0f) {
            GL11.glLineWidth(1.0f);
        }
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableColorMaterial();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawRoundedOutlineGradient(final float x, final float y, final float x2, final float y2, final float radius, float width, final int color, final int color2) {
        final int i = 18;
        final int j = 5;
        if (width == 1.0f) {
            width = 1.001f;
        }
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.enableColorMaterial();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        if (width != 1.0f) {
            GL11.glLineWidth(width);
        }
        setGlColor(color);
        GL11.glShadeModel(7425);
        GL11.glBegin(3);
        GL11.glVertex2f(x + radius, y);
        GL11.glVertex2f(x2 - radius, y);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex2f(x2, y + radius);
        setGlColor(color2);
        GL11.glVertex2f(x2, y2 - radius);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex2f(x2 - radius, y2 - 0.1f);
        GL11.glVertex2f(x + radius, y2 - 0.1f);
        GL11.glEnd();
        GL11.glBegin(3);
        setGlColor(color2);
        GL11.glVertex2f(x + 0.1f, y2 - radius);
        setGlColor(color);
        GL11.glVertex2f(x + 0.1f, y + radius);
        GL11.glEnd();
        float f1 = x2 - radius;
        float f2 = y + radius;
        setGlColor(color);
        GL11.glBegin(3);
        for (int k = 0; k <= 18; ++k) {
            final int m = 90 - k * 5;
            GL11.glVertex2f((float)(f1 + radius * MathUtil.getRightAngle(m)), (float)(f2 - radius * MathUtil.getAngle(m)));
        }
        GL11.glEnd();
        f1 = x2 - radius;
        f2 = y2 - radius;
        setGlColor(color2);
        GL11.glBegin(3);
        for (int k = 0; k <= 18; ++k) {
            final int m = k * 5 + 270;
            GL11.glVertex2f((float)(f1 + radius * MathUtil.getRightAngle(m)), (float)(f2 - radius * MathUtil.getAngle(m)));
        }
        GL11.glEnd();
        setGlColor(color2);
        GL11.glBegin(3);
        f1 = x + radius;
        f2 = y2 - radius;
        for (int k = 0; k <= 18; ++k) {
            final int m = k * 5 + 90;
            GL11.glVertex2f((float)(f1 + radius * MathUtil.getRightAngle(m)), (float)(f2 + radius * MathUtil.getAngle(m)));
        }
        GL11.glEnd();
        setGlColor(color);
        GL11.glBegin(3);
        f1 = x + radius;
        f2 = y + radius;
        for (int k = 0; k <= 18; ++k) {
            final int m = 270 - k * 5;
            GL11.glVertex2f((float)(f1 + radius * MathUtil.getRightAngle(m)), (float)(f2 + radius * MathUtil.getAngle(m)));
        }
        GL11.glEnd();
        if (width != 1.0f) {
            GL11.glLineWidth(1.0f);
        }
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableColorMaterial();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawRect(float left, float top, float right, float bottom, final ColorObject clr) {
        final int color = SCModule.getColor(clr);
        if (left < right) {
            final float i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final float j = top;
            top = bottom;
            bottom = j;
        }
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        setGlColor(color);
        worldrenderer.startDrawing(7);
        worldrenderer.addVertex(left, bottom, 0.0);
        worldrenderer.addVertex(right, bottom, 0.0);
        worldrenderer.addVertex(right, top, 0.0);
        worldrenderer.addVertex(left, top, 0.0);
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawColoredRect(final float x, final float y, final float x2, final float y2, final ColorObject clr1, final ColorObject clr2) {
        final int color = SCModule.getColor(clr1);
        final int color2 = SCModule.getColor(clr2);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.startDrawing(7);
        setGlColor(color);
        worldRenderer.addVertex(x, y2, 0.0);
        setGlColor(color2);
        worldRenderer.addVertex(x2, y2, 0.0);
        worldRenderer.addVertex(x2, y, 0.0);
        setGlColor(color);
        worldRenderer.addVertex(x, y, 0.0);
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawGradientRect(final int left, final int top, final int right, final int bottom, final ColorObject ctl, final ColorObject ctr, final ColorObject cbl, final ColorObject cbr, final int zLevel) {
        final int coltl = SCModule.getColor(ctl);
        final int coltr = SCModule.getColor(ctr);
        final int colbl = SCModule.getColor(cbl);
        final int colbr = SCModule.getColor(cbr);
        drawGradientRect((float)left, (float)top, (float)right, (float)bottom, coltl, coltr, colbl, colbr, zLevel);
    }
    
    public static void drawGradientRect(final float left, final float top, final float right, final float bottom, final ColorObject ctl, final ColorObject ctr, final ColorObject cbl, final ColorObject cbr, final int zLevel) {
        final int coltl = SCModule.getColor(ctl);
        final int coltr = SCModule.getColor(ctr);
        final int colbl = SCModule.getColor(cbl);
        final int colbr = SCModule.getColor(cbr);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer buffer = tessellator.getWorldRenderer();
        buffer.startDrawing(7);
        buffer.color((coltr & 0xFF0000) >> 16, (coltr & 0xFF00) >> 8, coltr & 0xFF, (coltr & 0xFF000000) >>> 24);
        buffer.addVertex(right, top, zLevel);
        buffer.color((coltl & 0xFF0000) >> 16, (coltl & 0xFF00) >> 8, coltl & 0xFF, (coltl & 0xFF000000) >>> 24);
        buffer.addVertex(left, top, zLevel);
        buffer.color((colbl & 0xFF0000) >> 16, (colbl & 0xFF00) >> 8, colbl & 0xFF, (colbl & 0xFF000000) >>> 24);
        buffer.addVertex(left, bottom, zLevel);
        buffer.color((colbr & 0xFF0000) >> 16, (colbr & 0xFF00) >> 8, colbr & 0xFF, (colbr & 0xFF000000) >>> 24);
        buffer.addVertex(right, bottom, zLevel);
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawRectOutline(final float left, final float top, final float right, final float bottom, final ColorObject clr) {
        final int color = SCModule.getColor(clr);
        final float width = 0.55f;
        drawRect(left - 0.55f, top - 0.55f, right + 0.55f, top, color);
        drawRect(right, top, right + 0.55f, bottom, color);
        drawRect(left - 0.55f, bottom, right + 0.55f, bottom + 0.55f, color);
        drawRect(left - 0.55f, top, left, bottom, color);
    }
    
    public static void drawRoundedRect(final float nameInt1, final float nameInt2, final float nameInt3, final float nameInt4, final float radius, final ColorObject clr) {
        final int color = SCModule.getColor(clr);
        final float f1 = (color >> 24 & 0xFF) / 255.0f;
        final float f2 = (color >> 16 & 0xFF) / 255.0f;
        final float f3 = (color >> 8 & 0xFF) / 255.0f;
        final float f4 = (color & 0xFF) / 255.0f;
        GlStateManager.color(f2, f3, f4, f1);
        drawRoundedRect(nameInt1, nameInt2, nameInt3, nameInt4, radius);
    }
    
    public static void drawRoundedOutline(final int x, final int y, final int x2, final int y2, final float radius, final float width, final ColorObject clr) {
        final int color = SCModule.getColor(clr);
        setGlColor(color);
        drawRoundedOutline((float)x, (float)y, (float)x2, (float)y2, radius, (width == 1.0f) ? 1.001f : width);
    }
    
    public static void drawRoundedOutline(final float x, final float y, final float x2, final float y2, final float radius, final float width, final ColorObject clr) {
        final int color = SCModule.getColor(clr);
        setGlColor(color);
        drawRoundedOutline(x, y, x2, y2, radius, (width == 1.0f) ? 1.001f : width);
    }
    
    public static void drawRoundedOutlineGradient(final float x, final float y, final float x2, final float y2, final float radius, float width, final ColorObject clr1, final ColorObject clr2) {
        final int color = SCModule.getColor(clr1);
        final int color2 = SCModule.getColor(clr2);
        if (width == 1.0f) {
            width = 1.001f;
        }
        final int i = 18;
        final int j = 5;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.enableColorMaterial();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        if (width != 1.0f) {
            GL11.glLineWidth(width);
        }
        setGlColor(color);
        GL11.glShadeModel(7425);
        GL11.glBegin(3);
        GL11.glVertex2f(x + radius, y);
        GL11.glVertex2f(x2 - radius, y);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex2f(x2, y + radius);
        setGlColor(color2);
        GL11.glVertex2f(x2, y2 - radius);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex2f(x2 - radius, y2 - 0.1f);
        GL11.glVertex2f(x + radius, y2 - 0.1f);
        GL11.glEnd();
        GL11.glBegin(3);
        setGlColor(color2);
        GL11.glVertex2f(x + 0.1f, y2 - radius);
        setGlColor(color);
        GL11.glVertex2f(x + 0.1f, y + radius);
        GL11.glEnd();
        float f1 = x2 - radius;
        float f2 = y + radius;
        setGlColor(color);
        GL11.glBegin(3);
        for (int k = 0; k <= 18; ++k) {
            final int m = 90 - k * 5;
            GL11.glVertex2f((float)(f1 + radius * MathUtil.getRightAngle(m)), (float)(f2 - radius * MathUtil.getAngle(m)));
        }
        GL11.glEnd();
        f1 = x2 - radius;
        f2 = y2 - radius;
        setGlColor(color2);
        GL11.glBegin(3);
        for (int k = 0; k <= 18; ++k) {
            final int m = k * 5 + 270;
            GL11.glVertex2f((float)(f1 + radius * MathUtil.getRightAngle(m)), (float)(f2 - radius * MathUtil.getAngle(m)));
        }
        GL11.glEnd();
        setGlColor(color2);
        GL11.glBegin(3);
        f1 = x + radius;
        f2 = y2 - radius;
        for (int k = 0; k <= 18; ++k) {
            final int m = k * 5 + 90;
            GL11.glVertex2f((float)(f1 + radius * MathUtil.getRightAngle(m)), (float)(f2 + radius * MathUtil.getAngle(m)));
        }
        GL11.glEnd();
        setGlColor(color);
        GL11.glBegin(3);
        f1 = x + radius;
        f2 = y + radius;
        for (int k = 0; k <= 18; ++k) {
            final int m = 270 - k * 5;
            GL11.glVertex2f((float)(f1 + radius * MathUtil.getRightAngle(m)), (float)(f2 + radius * MathUtil.getAngle(m)));
        }
        GL11.glEnd();
        if (width != 1.0f) {
            GL11.glLineWidth(1.0f);
        }
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableColorMaterial();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawDownwardsTriangle(float f, final float f2, final float f3, final int n) {
        color(n);
        final float f4 = (f -= f3 / 2.0f) + f3;
        final float f5 = f2 + f3 / 1.5f;
        drawTriangle(f + f3 / 2.0f, f4, f, f5, f2, f2, f3, n);
        reset();
    }
    
    public static void drawUpwardsTriangle(float f, final float f2, final float f3, final int n) {
        color(n);
        final float f4 = (f -= f3 / 2.0f) + f3;
        final float f5 = f2 + f3 / 1.5f;
        drawTriangle(f + f3 / 2.0f, f4, f, f5, f2, f2, f3, n);
        reset();
    }
    
    public static void drawLeftFacingTriangle(float f, final float f2, final float f3, final int n) {
        color(n);
        final float f4 = (f -= f3 / 2.0f) + f3;
        final float f5 = f2 + f3;
        drawTriangle(f4, f4, f, f5, f2, f2 + (f5 - f2) / 2.0f, f3, n);
        reset();
    }
    
    public static void drawRightFacingTriangle(float f, final float f2, final float f3, final int n) {
        color(n);
        final float f4 = (f -= f3 / 2.0f) + f3;
        final float f5 = f2 + f3;
        drawTriangle(f, f, f4, f2, f5, f2 + (f5 - f2) / 2.0f, f3, n);
        reset();
    }
    
    public static void drawTriangle(final float x1, final float x2, final float x3, final float y1, final float y2, final float y3, final float var7, final int var8) {
        try {
            final Tessellator t = Tessellator.getInstance();
            final WorldRenderer wr = t.getWorldRenderer();
            wr.startDrawing(4);
            wr.addVertex(x1, y1, 0.0);
            wr.addVertex(x2, y2, 0.0);
            wr.addVertex(x3, y3, 0.0);
            t.draw();
        }
        catch (Throwable var9) {
            throw var9;
        }
    }
    
    private static void color(final int n) {
        final float f = (n >> 24 & 0xFF) / 255.0f;
        final float f2 = (n >> 16 & 0xFF) / 255.0f;
        final float f3 = (n >> 8 & 0xFF) / 255.0f;
        final float f4 = (n & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.color(f2, f3, f4, f);
    }
    
    private static void reset() {
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static ScaledResolution getScaledResolution() {
        return new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
    }
    
    public static void drawDot(final float x, final float y, final float size, final int color) {
        GL11.glEnable(2848);
        GL11.glEnable(2832);
        setGlColor(color);
        GL11.glPointSize(size);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(770, 771);
        GL11.glBegin(0);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawPartialCircle(final float x, final float y, final float radius, int startAngle, int endAngle, final float thickness, final int color, final boolean smooth) {
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        if (startAngle > endAngle) {
            final int temp = startAngle;
            startAngle = endAngle;
            endAngle = temp;
        }
        if (startAngle < 0) {
            startAngle = 0;
        }
        if (endAngle > 360) {
            endAngle = 360;
        }
        if (smooth) {
            GL11.glEnable(2848);
        }
        else {
            GL11.glDisable(2848);
        }
        GL11.glLineWidth(thickness);
        setGlColor(color);
        GL11.glBegin(3);
        final float ratio = 0.017453292f;
        for (int i = startAngle; i <= endAngle; ++i) {
            final float radians = (i - 90) * 0.017453292f;
            GL11.glVertex2f(x + (float)Math.cos(radians) * radius, y + (float)Math.sin(radians) * radius);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawTorus(final int x, final int y, final float innerRadius, final float outerRadius, final int color, final boolean smooth) {
        if (smooth) {
            GL11.glEnable(2848);
        }
        else {
            GL11.glDisable(2848);
        }
        final float ratio = 0.017453292f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer bufferBuilder = tessellator.getWorldRenderer();
        bufferBuilder.startDrawing(1);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(770, 771);
        setGlColor(color);
        for (int i = 0; i <= 360; ++i) {
            final float radians = (i - 90) * 0.017453292f;
            bufferBuilder.addVertex(x + (float)Math.cos(radians) * innerRadius, y + (float)Math.sin(radians) * innerRadius, 0.0);
            bufferBuilder.addVertex(x + (float)Math.cos(radians) * outerRadius, y + (float)Math.sin(radians) * outerRadius, 0.0);
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawTorus(final float x, final float y, final float innerRadius, final float outerRadius, final int color, final boolean smooth) {
        if (smooth) {
            GL11.glEnable(2848);
        }
        else {
            GL11.glDisable(2848);
        }
        final float ratio = 0.017453292f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer bufferBuilder = tessellator.getWorldRenderer();
        bufferBuilder.startDrawing(1);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(770, 771);
        setGlColor(color);
        for (int i = 0; i <= 360; ++i) {
            final float radians = (i - 90) * 0.017453292f;
            bufferBuilder.addVertex(x + (float)Math.cos(radians) * innerRadius, y + (float)Math.sin(radians) * innerRadius, 0.0);
            bufferBuilder.addVertex(x + (float)Math.cos(radians) * outerRadius, y + (float)Math.sin(radians) * outerRadius, 0.0);
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawPartialTorus(final int x, final int y, final float innerRadius, final float outerRadius, final int startAngle, final int endAngle, final int color, final boolean smooth) {
        if (smooth) {
            GL11.glEnable(2848);
        }
        else {
            GL11.glDisable(2848);
        }
        final float ratio = 0.017453292f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer bufferBuilder = tessellator.getWorldRenderer();
        bufferBuilder.startDrawing(1);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(770, 771);
        setGlColor(color);
        for (int i = startAngle; i <= endAngle; ++i) {
            final float radians = (i - 90) * 0.017453292f;
            bufferBuilder.addVertex(x + (float)Math.cos(radians) * innerRadius, y + (float)Math.sin(radians) * innerRadius, 0.0);
            bufferBuilder.addVertex(x + (float)Math.cos(radians) * outerRadius, y + (float)Math.sin(radians) * outerRadius, 0.0);
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public void drawLine(final float x, final float x1, final float y, final float thickness, final int colour, final boolean smooth) {
        drawLines(new float[] { x, y, x1, y }, thickness, colour, smooth);
    }
    
    public void drawVerticalLine(final float x, final float y, final float y1, final float thickness, final int colour, final boolean smooth) {
        drawLines(new float[] { x, y, x, y1 }, thickness, colour, smooth);
    }
    
    public static void drawLines(final float[] points, final float thickness, final int color, final boolean smooth) {
        if (smooth) {
            GL11.glEnable(2848);
        }
        else {
            GL11.glDisable(2848);
        }
        GL11.glLineWidth(thickness);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer bufferBuilder = tessellator.getWorldRenderer();
        bufferBuilder.startDrawing(1);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(770, 771);
        setGlColor(color);
        for (int i = 0; i < points.length; i += 2) {
            bufferBuilder.addVertex(points[i], points[i + 1], 0.0);
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawFilledShape(final float[] points, final int color, final boolean smooth) {
        if (smooth) {
            GL11.glEnable(2848);
        }
        else {
            GL11.glDisable(2848);
        }
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer bufferBuilder = tessellator.getWorldRenderer();
        bufferBuilder.startDrawing(9);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(770, 771);
        setGlColor(color);
        for (int i = 0; i < points.length; i += 2) {
            bufferBuilder.addVertex(points[i], points[i + 1], 0.0);
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawFilledRectangle(final float x1, final float y1, final float x2, final float y2, final int color, final boolean smooth) {
        final float[] points = { x1, y1, x1, y2, x2, y2, x2, y1 };
        drawFilledShape(points, color, smooth);
    }
    
    public static void scissorHelper(final int x1, final int y1, int x2, int y2) {
        x2 -= x1;
        y2 -= y1;
        final Minecraft mc = Minecraft.getMinecraft();
        final ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        GL11.glScissor(x1 * resolution.getScaleFactor(), mc.displayHeight - y1 * resolution.getScaleFactor() - y2 * resolution.getScaleFactor(), x2 * resolution.getScaleFactor(), y2 * resolution.getScaleFactor());
    }
    
    public static void drawModalRectWithCustomSizedTexture(final float x, final float y, final float u, final float v, final int width, final int height, final float textureWidth, final float textureHeight) {
        final float f = 1.0f / textureWidth;
        final float f2 = 1.0f / textureHeight;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawing(7);
        worldrenderer.addVertexWithUV(x, y + height, 0.0, u * f, (v + height) * f2);
        worldrenderer.addVertexWithUV(x + width, y + height, 0.0, (u + width) * f, (v + height) * f2);
        worldrenderer.addVertexWithUV(x + width, y, 0.0, (u + width) * f, v * f2);
        worldrenderer.addVertexWithUV(x, y, 0.0, u * f, v * f2);
        tessellator.draw();
    }
    
    public static void drawScaledCustomSizeModalRect(final float x, final float y, final float u, final float v, final int uWidth, final int vHeight, final int width, final int height, final float tileWidth, final float tileHeight) {
        final float f = 1.0f / tileWidth;
        final float f2 = 1.0f / tileHeight;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawing(7);
        worldrenderer.addVertexWithUV(x, y + height, 0.0, u * f, (v + vHeight) * f2);
        worldrenderer.addVertexWithUV(x + width, y + height, 0.0, (u + uWidth) * f, (v + vHeight) * f2);
        worldrenderer.addVertexWithUV(x + width, y, 0.0, (u + uWidth) * f, v * f2);
        worldrenderer.addVertexWithUV(x, y, 0.0, u * f, v * f2);
        tessellator.draw();
    }
    
    public static void drawImage(final ResourceLocation res, final int x, final int y, final int width, final int height) {
        drawImage(res, (float)x, (float)y, (float)width, (float)height);
    }
    
    public static void drawImage(final ResourceLocation res, final float x, final float y, final float width, final float height) {
        bindTexture(res);
        drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, (int)width, (int)height, width, height);
    }
    
    public static void bindTexture(final ResourceLocation res) {
        GuiUtils.mc.getTextureManager().bindTexture(res);
    }
    
    public static int glToRGB(final float red, final float green, final float blue, final float alpha) {
        return new Color((int)red * 255, (int)green * 255, (int)blue * 255, (int)alpha * 255).getRGB();
    }
    
    public static float rgbToGl(final int rgb) {
        return rgb / 255.0f;
    }
    
    public static void setGlColor(final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }
    
    public static void setGlColor(final int color, final float alpha) {
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }
    
    public static int getRGB(final int color, final int alpha) {
        return new Color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, alpha).getRGB();
    }
    
    public static Color getColor(final int color) {
        return new Color(color, true);
    }
    
    public static int getAlpha(final int color) {
        return color >> 24 & 0xFF;
    }
    
    public static int hsvToRgb(int hue, final int saturation, final int value) {
        hue %= 360;
        final float s = saturation / 100.0f;
        final float v = value / 100.0f;
        final float c = v * s;
        final float h = hue / 60.0f;
        final float x = c * (1.0f - Math.abs(h % 2.0f - 1.0f));
        float r = 0.0f;
        float g = 0.0f;
        float b = 0.0f;
        switch (hue / 60) {
            case 0: {
                r = c;
                g = x;
                b = 0.0f;
                break;
            }
            case 1: {
                r = x;
                g = c;
                b = 0.0f;
                break;
            }
            case 2: {
                r = 0.0f;
                g = c;
                b = x;
                break;
            }
            case 3: {
                r = 0.0f;
                g = x;
                b = c;
                break;
            }
            case 4: {
                r = x;
                g = 0.0f;
                b = c;
                break;
            }
            case 5: {
                r = c;
                g = 0.0f;
                b = x;
                break;
            }
            default: {
                return 0;
            }
        }
        final float m = v - c;
        return (int)((r + m) * 255.0f) << 16 | (int)((g + m) * 255.0f) << 8 | (int)((b + m) * 255.0f);
    }
    
    public static int[] rgbToHsv(final int rgb) {
        final float r = ((rgb & 0xFF0000) >> 16) / 255.0f;
        final float g = ((rgb & 0xFF00) >> 8) / 255.0f;
        final float b = (rgb & 0xFF) / 255.0f;
        final float M = (r > g) ? Math.max(r, b) : Math.max(g, b);
        final float m = (r < g) ? Math.min(r, b) : Math.min(g, b);
        final float c = M - m;
        float h;
        if (M == r) {
            for (h = (g - b) / c; h < 0.0f; h += 6.0f) {}
            h %= 6.0f;
        }
        else if (M == g) {
            h = (b - r) / c + 2.0f;
        }
        else {
            h = (r - g) / c + 4.0f;
        }
        h *= 60.0f;
        final float s = c / M;
        return new int[] { (c == 0.0f) ? -1 : ((int)h), (int)(s * 100.0f), (int)(M * 100.0f) };
    }
    
    public static int getIntermediateColor(final int a, final int b, final float percent) {
        final float avgRed = (a >> 16 & 0xFF) * percent + (b >> 16 & 0xFF) * (1.0f - percent);
        final float avgGreen = (a >> 8 & 0xFF) * percent + (b >> 8 & 0xFF) * (1.0f - percent);
        final float avgBlue = (a >> 0 & 0xFF) * percent + (b >> 0 & 0xFF) * (1.0f - percent);
        final float avgAlpha = (a >> 24 & 0xFF) * percent + (b >> 24 & 0xFF) * (1.0f - percent);
        try {
            return new Color(avgRed / 255.0f, avgGreen / 255.0f, avgBlue / 255.0f, avgAlpha / 255.0f).getRGB();
        }
        catch (IllegalArgumentException e) {
            MoonsenseClient.error("Color nameeter outside of expected range!", new Object[0]);
            return Integer.MIN_VALUE;
        }
    }
    
    public static int convertPercentToValue(final float percent) {
        return (int)(percent * 255.0f);
    }
    
    public static float[] getColorComponents(final int color) {
        return new Color(color).getRGBColorComponents(null);
    }
    
    public static float getRedComponent(final int color) {
        return getColorComponents(color)[0];
    }
    
    public static float getGreenComponent(final int color) {
        return getColorComponents(color)[1];
    }
    
    public static float getBlueComponent(final int color) {
        return getColorComponents(color)[2];
    }
    
    public static float getAlphaComponent(final int color) {
        try {
            return getColorComponents(color)[3];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return 1.0f;
        }
    }
    
    public static float getAlphaComponentForRenderingEntityHit(final int color) {
        try {
            return getColorComponents(color)[3];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return 0.3f;
        }
    }
    
    public static void draw3DLine(final AxisAlignedBB aabb, final double offsetX, final double offsetY, final double offsetZ) {
        double x1 = aabb.minX;
        double y1 = aabb.minY;
        double z1 = aabb.minZ;
        double x2 = aabb.maxX;
        double y2 = aabb.maxY;
        double z2 = aabb.maxZ;
        x1 += offsetX;
        x2 += offsetX;
        y1 += offsetY;
        y2 += offsetY;
        z1 += offsetZ;
        z2 += offsetZ;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.4f);
        GL11.glLineWidth(2.0f);
        GlStateManager.func_179090_x();
        final Tessellator var2 = Tessellator.getInstance();
        final WorldRenderer var3 = var2.getWorldRenderer();
        var3.startDrawing(1);
        var3.func_178991_c(-1);
        var3.addVertex(x1, y1, z1);
        var3.addVertex(x2, y2, z2);
        var2.draw();
        GlStateManager.func_179098_w();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
