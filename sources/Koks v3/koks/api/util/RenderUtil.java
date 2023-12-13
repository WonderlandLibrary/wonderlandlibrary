package koks.api.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.UUID;

import static net.minecraft.client.renderer.GlStateManager.*;
import static net.minecraft.client.renderer.GlStateManager.rotate;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glScalef;

public class RenderUtil {
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
                GlStateManager.disableLighting();
                GlStateManager.depthMask(false);
                GlStateManager.disableDepth();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                byte b0 = 0;
                if (entityIn.getName().equals("deadmau5")) {
                    b0 = -10;
                }
                final int width = fontRenderer.getStringWidth(entityIn.getName()) / 2;
                GlStateManager.disableTexture2D();
                Gui.drawRect(-width - 1, b0 - 2, width, b0 + fontRenderer.FONT_HEIGHT - 2, new Color(0, 0, 0, 180).getRGB());
                GlStateManager.enableTexture2D();
                final int playerColor = entityIn.isSneaking() ? colorSneaking.getRGB() : color.getRGB();
                fontRenderer.drawString(entityIn.getName(), -width, b0 - 1, playerColor);
                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                fontRenderer.drawString(entityIn.getName(), -width, b0 - 1, playerColor);
                GlStateManager.enableLighting();
                GlStateManager.disableBlend();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.popMatrix();
            }
        }
    }

    /* GuiPlayerTabOverlay:179*/
    public void drawPlayerHead(GameProfile gameProfile, int x, int y, int width, int height) {
        GL11.glPushMatrix();
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
            Gui.drawScaledCustomSizeModalRect(x, y, 8.0F, (float) l2, 8, i3, width, height, 64.0F, 64.0F);

            if (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
                int j3 = 8 + (flag1 ? 8 : 0);
                int k3 = 8 * (flag1 ? -1 : 1);
                Gui.drawScaledCustomSizeModalRect(x, y, 40.0F, (float) j3, 8, k3, width, height, 64.0F, 64.0F);
            }
        } else {
            EntityPlayerSP playerMP = (EntityPlayerSP) entityplayer;
            boolean flag1 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && (gameProfile.getName().equals("Dinnerbone") || gameProfile.getName().equals("Grumm"));
            assert playerMP != null;
            NetworkPlayerInfo networkplayerinfo = playerMP.getPlayerInfo();
            if (networkplayerinfo != null)
                this.mc.getTextureManager().bindTexture(playerMP.getLocationSkin());
            else
                this.mc.getTextureManager().bindTexture(DefaultPlayerSkin.getDefaultSkinLegacy());
            int l2 = 8 + (flag1 ? 8 : 0);
            int i3 = 8 * (flag1 ? -1 : 1);
            Gui.drawScaledCustomSizeModalRect(x, y, 8.0F, (float) l2, 8, i3, width, height, 64.0F, 64.0F);

            if (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
                int j3 = 8 + (flag1 ? 8 : 0);
                int k3 = 8 * (flag1 ? -1 : 1);
                Gui.drawScaledCustomSizeModalRect(x, y, 40.0F, (float) j3, 8, k3, width, height, 64.0F, 64.0F);
            }
        }
        GL11.glPopMatrix();
    }

    public void scissor(int x, int y, int x2, int y2) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        double factor = scaledResolution.getScaleFactor();
        GL11.glScissor((int) (x * factor), (int) ((scaledResolution.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));
    }

    public void drawPicture(int x, int y, int width, int height, ResourceLocation resourceLocation) {
        mc.getTextureManager().bindTexture(resourceLocation);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
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
        GL11.glPushMatrix();
        Color c = new Color(color);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4d(c.getRed() / 255D, c.getGreen() / 255D, c.getBlue() / 255D, c.getAlpha() / 255D);
        GL11.glBegin(GL11.GL_POLYGON);
        for(int i = 0; i < 360; i++) {
            GL11.glVertex2d(x + Math.sin(i * Math.PI / 180) * radius, y + Math.cos(i * Math.PI / 180) * radius);
        }
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(1, 1, 1);
        GL11.glPopMatrix();
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
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double) left, (double) bottom, 0.0D).endVertex();
        worldrenderer.pos((double) right, (double) bottom, 0.0D).endVertex();
        worldrenderer.pos((double) right, (double) top, 0.0D).endVertex();
        worldrenderer.pos((double) left, (double) top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public Color getRainbow(int offset, int speed, float saturation, float brightness) {
        float hue = ((System.currentTimeMillis() + offset) % speed) / (float) speed;
        return Color.getHSBColor(hue, saturation, brightness);
    }

    public Color getAlphaColor(Color color, int alpha) {
        if (alpha > 255) {
            alpha = 255;
        }
        if (alpha < 0) {
            alpha = 1;
        }
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public void setColor(Color color) {
        color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }

}
