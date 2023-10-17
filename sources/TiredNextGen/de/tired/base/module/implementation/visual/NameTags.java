package de.tired.base.module.implementation.visual;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.util.render.ESPUtil;
import de.tired.base.font.FontManager;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.Render2DEvent;
import de.tired.base.event.events.Render3DEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.base.module.ModuleManager;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.list.RoundedRectShader;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector4f;
import java.awt.*;

@ModuleAnnotation(name = "NameTags", category = ModuleCategory.RENDER, clickG = "Render big name over players")
public class NameTags extends Module {

    public float partialTicks;

    private double animationX = 0;

    @EventTarget
    public void onRender(Render2DEvent e) {
        partialTicks = e.getPartialTicks();

    }

    public void onRender2(Render3DEvent e) {

    }

    public static NameTags getInstance() {
        return ModuleManager.getInstance(NameTags.class);
    }

    public void doRenderFinal(boolean rect) {
        for (final Entity entity : MC.theWorld.loadedEntityList) {
            if (shouldRender(entity) && ESPUtil.isInView(entity)) {
                if (entity instanceof EntityPlayer) {
                    renderReal2D(entity, rect);
                }
            }
        }

    }

    public final void renderReal2D(final Entity entity, boolean rect) {
        Entity camera = MC.getRenderViewEntity();
        assert (camera != null);
        MC.entityRenderer.setupCameraTransform(MC.timer.renderPartialTicks, 0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Vector4f pos = ESPUtil.getEntityPositionsOn2D(entity);
        float x = pos.getX(), y = pos.getY(), right = pos.getZ();

        if (entity instanceof EntityPlayer) {
            MC.entityRenderer.setupCameraTransform(MC.timer.renderPartialTicks, 0);
            EntityLivingBase renderingEntity = (EntityLivingBase) entity;

            String text = renderingEntity.getName();

            float finalHealth = Float.isNaN(((EntityLivingBase) entity).getHealth()) ? -1 : Math.round(((EntityLivingBase) entity).getHealth() * 5);
            final String colorPrefix = finalHealth == -1 ? "" : finalHealth >= 80 ? "§a" : finalHealth >= 60 ? "§e" : finalHealth >= 40 ? "§6" : finalHealth >= 20 ? "§c" : "§4";
            final String health = finalHealth == -1 ? " §cNaN" : " " + colorPrefix + Math.round(finalHealth) + "%";
            final String tagText = text + " " + health;
            double fontScale = 1.0F;
            float middle = x + ((right - x) / 2);
            float textWidth;
            double fontHeight;
            textWidth = FontManager.futuraNormal.getStringWidth(tagText);
            middle -= (textWidth * fontScale) / 2f;
            fontHeight = FontManager.futuraNormal.getHeight() * fontScale;
            MC.entityRenderer.setupOverlayRendering();


            GL11.glPushMatrix();
            GL11.glTranslated(middle, y - (fontHeight + 2), 0);
            GL11.glScaled(fontScale, fontScale, 1);
            GL11.glTranslated(-middle, -(y - (fontHeight + 2)), 0);

            ShaderManager.shaderBy(RoundedRectShader.class).drawRound(middle - 3, (float) (y - fontHeight - 10.5), textWidth + 6, 13, 0, new Color(30, 30, 30, 122));


            if (!rect) {
                drawPlayerHead((EntityPlayer) entity, middle - 23, y - fontHeight - 11, 15, 15);
            }

            GlStateManager.resetColor();

            if (!rect) {
             FontManager.futuraNormal.drawString(tagText, middle, (float) (y - (fontHeight + 6)), -1);
            }
            GlStateManager.popMatrix();

        }
    }

    public void drawPlayerHead(EntityPlayer player, double x, double y, int width, int height) {

        AbstractClientPlayer clientPlayer = (AbstractClientPlayer) player;
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.color(1, 1, 1, 1);
        MC.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
        Gui.drawScaledCustomSizeModalRect((int) x, (int) y, 8.0f, 8.0f, 8, 8, width, height, 64.0f, 64.0f);
    }

    public boolean shouldRender(Entity entity) {
        if (entity.isDead || entity.isInvisible()) {
            return false;
        }
        if (entity instanceof EntityPlayer) {
            if (entity == MC.thePlayer) {
                return MC.gameSettings.thirdPersonView;
            }
            return true;
        }
        if (entity instanceof EntityAnimal) {
            return false;
        }

        if (entity instanceof EntityArmorStand) {
            return true;
        }

        return entity instanceof EntityMob;
    }

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
