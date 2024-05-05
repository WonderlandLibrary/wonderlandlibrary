/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.wallhacks.losebypass.utils;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.gui.ClickGui;
import com.wallhacks.losebypass.gui.components.SettingGroupComponent;
import com.wallhacks.losebypass.gui.components.settings.BooleanComponent;
import com.wallhacks.losebypass.gui.components.settings.ColorPicker;
import com.wallhacks.losebypass.gui.components.settings.DoubleSlider;
import com.wallhacks.losebypass.gui.components.settings.IntSlider;
import com.wallhacks.losebypass.gui.components.settings.ModeSelector;
import com.wallhacks.losebypass.gui.components.settings.SettingComponent;
import com.wallhacks.losebypass.systems.SettingsHolder;
import com.wallhacks.losebypass.systems.setting.Setting;
import com.wallhacks.losebypass.systems.setting.SettingGroup;
import com.wallhacks.losebypass.systems.setting.settings.BooleanSetting;
import com.wallhacks.losebypass.systems.setting.settings.ColorSetting;
import com.wallhacks.losebypass.systems.setting.settings.DoubleSetting;
import com.wallhacks.losebypass.systems.setting.settings.IntSetting;
import com.wallhacks.losebypass.systems.setting.settings.ModeSetting;
import com.wallhacks.losebypass.utils.ColorUtil;
import com.wallhacks.losebypass.utils.MC;
import com.wallhacks.losebypass.utils.MathUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiUtil
implements MC {
    public static void drawCompleteImage(double posX, double posY, double width, double height) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glTranslated((double)posX, (double)posY, (double)0.0);
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex3d((double)0.0, (double)height, (double)0.0);
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex3d((double)width, (double)height, (double)0.0);
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex3d((double)width, (double)0.0, (double)0.0);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static ArrayList<SettingComponent> getComponents(SettingsHolder holder) {
        ArrayList<SettingComponent> components = new ArrayList<SettingComponent>();
        ArrayList<SettingGroup> groups = new ArrayList<SettingGroup>();
        Iterator<Setting<?>> iterator = holder.getSettings().iterator();
        while (iterator.hasNext()) {
            Setting<?> setting = iterator.next();
            if (setting.getSettingsHolder() instanceof SettingGroup) {
                if (groups.contains(setting.getsettingsHolder())) continue;
                groups.add((SettingGroup)setting.getsettingsHolder());
                components.add(new SettingGroupComponent((SettingGroup)setting.getsettingsHolder()));
                continue;
            }
            SettingComponent component = GuiUtil.getComponent(setting, false);
            if (component == null) continue;
            components.add(component);
        }
        return components;
    }

    public static SettingComponent getComponent(Setting<?> setting, boolean alwaysOpen) {
        if (setting instanceof IntSetting) {
            return new IntSlider((IntSetting)setting);
        }
        if (setting instanceof DoubleSetting) {
            return new DoubleSlider((DoubleSetting)setting);
        }
        if (setting instanceof BooleanSetting) {
            return new BooleanComponent((BooleanSetting)setting);
        }
        if (setting instanceof ModeSetting) {
            return new ModeSelector((ModeSetting)setting);
        }
        if (!(setting instanceof ColorSetting)) return null;
        return new ColorPicker((ColorSetting)setting, alwaysOpen);
    }

    public static void setup(int color, boolean blend) {
        GlStateManager.pushMatrix();
        if (blend) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        } else {
            GlStateManager.disableBlend();
        }
        GlStateManager.disableTexture2D();
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glColor4f((float)((float)(color >> 16 & 0xFF) / 255.0f), (float)((float)(color >> 8 & 0xFF) / 255.0f), (float)((float)(color & 0xFF) / 255.0f), (float)((float)(color >> 24 & 0xFF) / 255.0f));
        GL11.glBegin((int)6);
    }

    public static void setup(int color) {
        GuiUtil.setup(color, true);
    }

    public static void finish() {
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void rounded(int x, int y, int right, int bottom, int color, int radius, boolean blend) {
        GuiUtil.setup(color, blend);
        GuiUtil.corner(right - radius, bottom - radius, (double)radius, 0, 90);
        GuiUtil.corner(right - radius, y + radius, (double)radius, 90, 180);
        GuiUtil.corner(x + radius, y + radius, (double)radius, 180, 270);
        GuiUtil.corner(x + radius, bottom - radius, (double)radius, 270, 360);
        GuiUtil.finish();
    }

    public static void rounded(int x, int y, int right, int bottom, int color, int radius) {
        GuiUtil.rounded(x, y, right, bottom, color, radius, true);
    }

    public static void corner(float x, float y, double radius, int start, int stop) {
        int i = start;
        do {
            i = Math.min(stop, i + 5);
            GL11.glVertex2d((double)((double)x + Math.sin((double)i * Math.PI / 180.0) * radius), (double)((double)y + Math.cos((double)i * Math.PI / 180.0) * radius));
        } while (i != stop);
    }

    public static void corner(int x, int y, double radius, int start, int stop) {
        GuiUtil.corner((float)x, (float)y, radius, start, stop);
    }

    public static String getLoadingText(boolean text) {
        String string;
        long time = System.currentTimeMillis() % 600L;
        String dot = ".";
        if (time > 200L) {
            dot = dot + ".";
        }
        if (time > 400L) {
            dot = dot + ".";
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (text) {
            string = "Loading";
            return stringBuilder.append(string).append(dot).toString();
        }
        string = "";
        return stringBuilder.append(string).append(dot).toString();
    }

    public static void drawPickerBase(int posX, int posY, int right, int bottem, float red, float green, float blue) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)9);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glVertex2f((float)posX, (float)posY);
        GL11.glVertex2f((float)posX, (float)bottem);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)1.0f);
        GL11.glVertex2f((float)right, (float)bottem);
        GL11.glVertex2f((float)right, (float)posY);
        GL11.glEnd();
        GL11.glDisable((int)3008);
        GL11.glBegin((int)9);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glVertex2f((float)posX, (float)posY);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glVertex2f((float)posX, (float)bottem);
        GL11.glVertex2f((float)right, (float)bottem);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glVertex2f((float)right, (float)posY);
        GL11.glEnd();
        GL11.glEnable((int)3008);
        GL11.glShadeModel((int)7424);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static boolean button(int posX, int posY, int right, int bottom, int mouseX, int mouseY, String text, boolean disabled) {
        boolean hover = mouseX > posX && mouseX < right && mouseY > posY && mouseY < bottom;
        int mid = (right - posX) / 2 + posX;
        if (disabled) {
            GuiUtil.rounded(posX, posY, right, bottom, ClickGui.mainColor2(), 3);
        } else if (!hover) {
            GuiUtil.rounded(posX, posY, right, bottom, ClickGui.mainColor(), 3);
        } else {
            GuiUtil.rounded(posX + 1, posY + 1, right - 1, bottom - 1, ClickGui.mainColor(), 3);
        }
        LoseBypass.fontManager.drawString(text, mid - LoseBypass.fontManager.getTextWidth(text) / 2, posY + LoseBypass.fontManager.getTextHeight() / 2 + 2, disabled ? Color.GRAY.getRGB() : -1);
        if (!hover) return false;
        if (disabled) return false;
        return true;
    }

    public static void renderPlayerHead(NetworkPlayerInfo networkPlayerInfo, int x, int y, int size) {
        GlStateManager.pushAttrib();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(networkPlayerInfo.getLocationSkin());
        int l5 = 8;
        int i3 = 8;
        Gui.drawScaledCustomSizeModalRect(x, y, 8.0f, l5, 8, i3, size, size, 64.0f, 64.0f);
        int j3 = 8;
        int k3 = 8;
        Gui.drawScaledCustomSizeModalRect(x, y, 40.0f, j3, 8, k3, size, size, 64.0f, 64.0f);
        GlStateManager.disableBlend();
        GlStateManager.popAttrib();
    }

    public static void drawCompleteImage(double posX, double posY, double width, double height, ResourceLocation image, Color c) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        ColorUtil.glColor(c);
        GuiUtil.drawCompleteImage(posX, posY, width, height);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void drawCompleteImageRotated(float posX, float posY, float width, float height, float rotation, ResourceLocation image, Color c) {
        GL11.glPushMatrix();
        GlStateManager.translate(posX + width / 2.0f, posY + height / 2.0f, 0.0f);
        GlStateManager.rotate(rotation, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(-width / 2.0f, -height / 2.0f, 0.0f);
        GuiUtil.drawCompleteImage(0.0, 0.0, width, height, image, c);
        GL11.glPopMatrix();
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent) {
        mouseX = (float)posX - mouseX;
        mouseY = MathUtil.clamp(mouseY, (float)(posY - 200), (float)posY);
        mouseY = (float)posY - mouseY - (float)scale / 7.0f * 6.0f;
        ColorUtil.glColor(Color.white);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, 50.0f);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-((float)Math.atan(mouseY / 40.0f)) * 20.0f, 1.0f, 0.0f, 0.0f);
        ent.renderYawOffset = (float)Math.atan(mouseX / 40.0f) * 20.0f;
        ent.rotationYaw = (float)Math.atan(mouseX / 40.0f) * 40.0f;
        ent.rotationPitch = -((float)Math.atan(mouseY / 40.0f)) * 20.0f;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(ent, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static void drawGradientRect(int x, int y, int width, int height, int startColor, int endColor, boolean horizontal) {
        float s = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float s1 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float s2 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float s3 = (float)(startColor & 0xFF) / 255.0f;
        float e = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float e1 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float e2 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float e3 = (float)(endColor & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer bufferbuilder = tessellator.getWorldRenderer();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        if (horizontal) {
            bufferbuilder.pos(width, y, 0.0).color(e1, e2, e3, e).endVertex();
            bufferbuilder.pos(x, y, 0.0).color(s1, s2, s3, s).endVertex();
            bufferbuilder.pos(x, height, 0.0).color(s1, s2, s3, s).endVertex();
            bufferbuilder.pos(width, height, 0.0).color(e1, e2, e3, e).endVertex();
        } else {
            bufferbuilder.pos(width, y, 0.0).color(s1, s2, s3, s).endVertex();
            bufferbuilder.pos(x, y, 0.0).color(s1, s2, s3, s).endVertex();
            bufferbuilder.pos(x, height, 0.0).color(e1, e2, e3, e).endVertex();
            bufferbuilder.pos(width, height, 0.0).color(e1, e2, e3, e).endVertex();
        }
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(startColor & 0xFF) / 255.0f;
        float f4 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(endColor & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer bufferbuilder = tessellator.getWorldRenderer();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 0.0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, top, 0.0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, bottom, 0.0).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawGradientRect(int left, int top, int right, int bottom, int coltl, int colbl, int coltr, int colbr, int zLevel) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer buffer = tessellator.getWorldRenderer();
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(right, top, zLevel).color((coltr & 0xFF0000) >> 16, (coltr & 0xFF00) >> 8, coltr & 0xFF, (coltr & 0xFF000000) >>> 24).endVertex();
        buffer.pos(left, top, zLevel).color((coltl & 0xFF0000) >> 16, (coltl & 0xFF00) >> 8, coltl & 0xFF, (coltl & 0xFF000000) >>> 24).endVertex();
        buffer.pos(left, bottom, zLevel).color((colbl & 0xFF0000) >> 16, (colbl & 0xFF00) >> 8, colbl & 0xFF, (colbl & 0xFF000000) >>> 24).endVertex();
        buffer.pos(right, bottom, zLevel).color((colbr & 0xFF0000) >> 16, (colbr & 0xFF00) >> 8, colbr & 0xFF, (colbr & 0xFF000000) >>> 24).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawQuad(float left, float top, float width, float height, int color) {
        GuiUtil.drawRect(left, top, left + width, top + height, color);
    }

    public static void drawRect(float left, float top, float right, float bottom, int color) {
        float j;
        if (left < right) {
            j = left;
            left = right;
            right = j;
        }
        if (top < bottom) {
            j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer bufferbuilder = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, top, 0.0).endVertex();
        bufferbuilder.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void glScissor(int x, int y, int width, int height) {
        ScaledResolution scr = new ScaledResolution(mc);
        GL11.glScissor((int)(x * scr.getScaleFactor()), (int)((scr.getScaledHeight() - y - height) * scr.getScaleFactor()), (int)(width * scr.getScaleFactor()), (int)(height * scr.getScaleFactor()));
    }

    public static void drawHorizontalLine(int startX, int endX, int y, int color) {
        if (endX < startX) {
            int i = startX;
            startX = endX;
            endX = i;
        }
        Gui.drawRect(startX, y, endX + 1, y + 1, color);
    }

    public static void drawVerticalLine(int x, int startY, int endY, int color) {
        if (endY < startY) {
            int i = startY;
            startY = endY;
            endY = i;
        }
        Gui.drawRect(x, startY + 1, x + 1, endY, color);
    }

    public static void linePre(Color color, float width) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        GL11.glLineWidth((float)width);
    }

    public static void linePost() {
        GL11.glDisable((int)3042);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
    }
}

