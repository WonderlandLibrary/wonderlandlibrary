package koks.api.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

import static java.awt.Color.*;
import static net.minecraft.client.gui.Gui.*;
import static net.minecraft.client.gui.GuiScreen.*;
import static net.minecraft.client.renderer.GlStateManager.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

public class RenderUtil {

    private static RenderUtil renderUtil;

    final Minecraft mc = Minecraft.getMinecraft();
    final RenderManager renderManager = mc.getRenderManager();

    /**
     * {@see net.minecraft.client.renderer.entity.Render#renderLivingLabel(Entity, String, double, double, double, int)}
     * Modified by Phantom
     */
    public void renderNameTag(Entity entityIn, double x, double y, double z, Color color, Color colorSneaking) {
        if (!(entityIn instanceof EntityCreature || entityIn instanceof EntitySquid || entityIn instanceof EntityBat || entityIn instanceof EntityGhast || entityIn instanceof EntitySlime) &&
                entityIn instanceof EntityLivingBase && entityIn != Minecraft.getMinecraft().thePlayer) {
            if (!entityIn.getName().equals("Armor Stand")) {
                final FontRenderer fontRenderer = mc.fontRendererObj;
                double distance = mc.thePlayer.getDistanceToEntity(entityIn) / 4F;
                if (distance <= 3) {
                    distance = 3;
                }
                final float scale = (float) (distance / 70F);
                pushMatrix();
                translate((float) x + 0.0F, (float) y + (((EntityLivingBase) entityIn).isChild() ? entityIn.height / 2.0F : entityIn.height + 0.5F), (float) z);
                glNormal3f(0.0F, 1.0F, 0.0F);
                rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                glScalef(-scale, -scale, scale);
                disableLighting();
                depthMask(false);
                disableDepth();
                enableBlend();
                tryBlendFuncSeparate(770, 771, 1, 0);
                byte b0 = 0;
                if (entityIn.getName().equals("deadmau5")) {
                    b0 = -10;
                }
                final int width = fontRenderer.getStringWidth(entityIn.getName()) / 2;
                disableTexture2D();
                drawRect(-width - 1, b0 - 2, width, b0 + fontRenderer.FONT_HEIGHT - 2, new Color(0, 0, 0, 180).getRGB());
                enableTexture2D();
                final int playerColor = entityIn.isSneaking() ? colorSneaking.getRGB() : color.getRGB();
                fontRenderer.drawString(entityIn.getName(), -width, b0 - 1, playerColor);
                enableDepth();
                depthMask(true);
                fontRenderer.drawString(entityIn.getName(), -width, b0 - 1, playerColor);
                enableLighting();
                disableBlend();
                color(1.0F, 1.0F, 1.0F, 1.0F);
                popMatrix();
            }
        }
    }

    public void renderItem(ItemStack itemStack, int xPos, int yPos) {
        if (itemStack != null) {
            mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, xPos, yPos);

            mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, itemStack, xPos, yPos);
        }
    }

    /* GuiPlayerTabOverlay:179*/
    public void drawPlayerHead(GameProfile gameProfile, int x, int y, int width, int height) {
        glPushMatrix();
        EntityPlayer entityplayer = this.mc.theWorld.getPlayerEntityByUUID(gameProfile.getId());
        if (!(entityplayer instanceof EntityPlayerSP)) {
            EntityOtherPlayerMP playerMP = (EntityOtherPlayerMP) entityplayer;
            boolean flag1 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && (gameProfile.getName().equals("Dinnerbone") || gameProfile.getName().equals("Grumm"));
            assert playerMP != null;
            NetworkPlayerInfo networkplayerinfo = playerMP.getPlayerInfo();
            if (networkplayerinfo != null)
                this.mc.getTextureManager().bindTexture(playerMP.getLocationSkin());
            else
                this.mc.getTextureManager().bindTexture(DefaultPlayerSkin.getDefaultSkinLegacy());
            int l2 = 8 + (flag1 ? 8 : 0);
            int i3 = 8 * (flag1 ? -1 : 1);
            drawScaledCustomSizeModalRect(x, y, 8.0F, (float) l2, 8, i3, width, height, 64.0F, 64.0F);

            if (entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
                int j3 = 8 + (flag1 ? 8 : 0);
                int k3 = 8 * (flag1 ? -1 : 1);
                drawScaledCustomSizeModalRect(x, y, 40.0F, (float) j3, 8, k3, width, height, 64.0F, 64.0F);
            }
        } else {
            EntityPlayerSP playerMP = (EntityPlayerSP) entityplayer;
            boolean flag1 = entityplayer.isWearing(EnumPlayerModelParts.CAPE) && (gameProfile.getName().equals("Dinnerbone") || gameProfile.getName().equals("Grumm"));
            NetworkPlayerInfo networkplayerinfo = playerMP.getPlayerInfo();
            if (networkplayerinfo != null)
                this.mc.getTextureManager().bindTexture(playerMP.getLocationSkin());
            else
                this.mc.getTextureManager().bindTexture(DefaultPlayerSkin.getDefaultSkinLegacy());
            int l2 = 8 + (flag1 ? 8 : 0);
            int i3 = 8 * (flag1 ? -1 : 1);
            drawScaledCustomSizeModalRect(x, y, 8.0F, (float) l2, 8, i3, width, height, 64.0F, 64.0F);

            if (entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
                int j3 = 8 + (flag1 ? 8 : 0);
                int k3 = 8 * (flag1 ? -1 : 1);
                drawScaledCustomSizeModalRect(x, y, 40.0F, (float) j3, 8, k3, width, height, 64.0F, 64.0F);
            }
        }
        glPopMatrix();
    }

    public void scissor(int x, int y, int x2, int y2) {
        final Resolution resolution = Resolution.getResolution();
        final double factor = resolution.getScale();
        glScissor((int) (x * factor), (int) ((resolution.getHeight() - y2) * factor), (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));
    }

    public void drawPicture(int x, int y, int width, int height, ResourceLocation resourceLocation) {
        mc.getTextureManager().bindTexture(resourceLocation);
        color(1.0F, 1.0F, 1.0F, 1.0F);
        drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void drawRoundedRect(double x, double y, double width, double height, double radius, Color color) {
        drawRoundedRect((int) x, (int) y, (int) width, (int) height, radius, color.getRGB());
    }

    public void drawRoundedRect(double x, double y, double width, double height, double radius, int color) {
        drawRoundedRect((int) x, (int) y, (int) width, (int) height, radius, color);
    }

    public void drawRoundedRect(int x, int y, int width, int height, double radius, int color) {
        drawCircle(x,y, radius, 180, 270, color);
        drawCircle(x + width, y, radius, 90, 180, color);
        drawCircle(x + width, y + height, radius,0, 91, color);
        drawCircle(x, y + height, radius,270, 360, color);
        drawRect(x - radius, y, x + width + radius, y + height, color);
        drawRect(x, y - radius, x + width, y, color);
        drawRect(x, y + height, x + width, y + height + radius, color);
    }

    public void drawOutlineRect(double left, double top, double right, double bottom, float size, int colorOutline, int color) {
        drawRect(left - size, top, left, bottom, colorOutline);
        drawRect(right, top, right + size, bottom, colorOutline);
        drawRect(left - size, top - size, right + size, top, colorOutline);
        drawRect(left - size, bottom, right + size, bottom + size, colorOutline);
        drawRect(left, top, right, bottom, color);
    }

    public void drawOutline(double left, double top, double right, double bottom, float size, int colorOutline) {
        drawRect(left - size, top, left, bottom, colorOutline);
        drawRect(right, top, right + size, bottom, colorOutline);
        drawRect(left - size, top - size, right + size, top, colorOutline);
        drawRect(left - size, bottom, right + size, bottom + size, colorOutline);
    }

    public void drawCircle(double x, double y, double radius, int color) {
        drawCircle(x, y, radius, 0, 360, color);
    }

    public void drawCircle(double x, double y, double radius, int angleBegin, int angleEnd, int color) {
        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        enableBlend();
        disableTexture2D();
        tryBlendFuncSeparate(770, 771, 1, 0);
        color(f, f1, f2, f3);

        GL11.glBegin(GL_POLYGON);
        if (Math.abs(angleBegin - angleEnd) != 360)
            glVertex2d(x, y);
        for (int i = angleBegin; i < angleEnd; i++) {
            glVertex2d((x + Math.sin(i * Math.PI / 180) * radius), (y + Math.cos(i * Math.PI / 180) * radius));
        }
        GL11.glEnd();
        disableBlend();
        enableTexture2D();
        resetColor();
    }

    public void drawRect(double left, double top, double right, double bottom, int color) {
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
        enableBlend();
        disableTexture2D();
        tryBlendFuncSeparate(770, 771, 1, 0);
        color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double) left, (double) bottom, 0.0D).endVertex();
        worldrenderer.pos((double) right, (double) bottom, 0.0D).endVertex();
        worldrenderer.pos((double) right, (double) top, 0.0D).endVertex();
        worldrenderer.pos((double) left, (double) top, 0.0D).endVertex();
        tessellator.draw();
        enableTexture2D();
        disableBlend();
        resetColor();
    }

    public Color getAlphaColor(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), MathHelper.clamp_int(alpha, 0, 255));
    }

    public void setColor(Color color) {
        color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }

    public static RenderUtil getInstance() {
        if (renderUtil == null) {
            renderUtil = new RenderUtil();
        }
        return renderUtil;
    }
}
