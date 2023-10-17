package de.tired.base.module.implementation.visual;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.util.render.RenderUtil;
import de.tired.base.font.FontManager;
import de.tired.util.render.ColorUtil;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.Render3DEvent2;
import de.tired.base.event.events.UpdateEvent;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@ModuleAnnotation(name = "DamageMarkers", category = ModuleCategory.RENDER)
public class AttackMarkers extends Module {

    private final HashMap<Integer, Float> healthMap = new HashMap<>();
    private final ArrayList<Marker> particles = new ArrayList<>();

    @EventTarget
    public void onRender3D(Render3DEvent2 event2) {
        synchronized (this.particles) {
            for (Marker marker : this.particles) {
                RenderManager renderManager = MC.getRenderManager();
                double size =1.0;
                size = MathHelper.clamp_double(size, 0.03, .07);
                double x = marker.x - RenderManager.renderPosX;
                double y = marker.y - RenderManager.renderPosY;
                double z = marker.z - RenderManager.renderPosZ;
                GlStateManager.pushMatrix();
                GlStateManager.enablePolygonOffset();
                GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
                GlStateManager.translate(x, y, z);
                GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                double textY = MC.gameSettings.thirdPersonView ? -1.0 : 1.0;
                GlStateManager.rotate(renderManager.playerViewX, (float) textY, 0.0f, 0.0f);
                GlStateManager.scale(-size, -size, size);
                GL11.glDepthMask(false);
                int color = -1;

                Color firstColor = ColorUtil.interpolateColorsBackAndForth(3, 0, new Color(84, 51, 158).brighter(), new Color(104, 127, 203), true);
                RenderUtil.instance.doRenderShadow(firstColor,  -((float) FontManager.raleWay15.getStringWidth(marker.getText()) / 2.0f), -(FontManager.raleWay15.FONT_HEIGHT - 1), 10, 5, 12);
                FontManager.raleWay15.drawString(marker.getText(), -((float) FontManager.raleWay15.getStringWidth(marker.getText()) / 2.0f), -(FontManager.raleWay15.FONT_HEIGHT - 1), color);

                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glDepthMask(true);
                GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
                GlStateManager.disablePolygonOffset();
                GlStateManager.popMatrix();
            }
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        synchronized (this.particles) {
            for (Entity entity : MC.theWorld.loadedEntityList) {
                if (entity == null || MC.thePlayer.getDistanceToEntity(entity) > 10.0f || entity.isDead || entity == MC.thePlayer || !(entity instanceof EntityLivingBase))
                    continue;
                final float lastHealth = this.healthMap.getOrDefault(entity.getEntityId(), ((EntityLivingBase) entity).getMaxHealth());
                this.healthMap.put(entity.getEntityId(), ((EntityLivingBase) entity).getHealth());
                if (lastHealth == ((EntityLivingBase) entity).getHealth()) continue;
                this.particles.add(new Marker(entity, "" + BigDecimal.valueOf(Math.abs(lastHealth - ((EntityLivingBase) entity).getHealth())).setScale(1, 4), entity.posX - 0.5 + (double) new Random(System.currentTimeMillis()).nextInt(5) * 0.1, entity.getEntityBoundingBox().minY + (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) / 2.0, entity.posZ - 0.5 + (double) new Random(System.currentTimeMillis() + 1L).nextInt(5) * 0.1));
            }
            final ArrayList<Marker> needRemove = new ArrayList<>();
            for (Marker marker : this.particles) {
                marker.ticks++;
                if (!((float) marker.ticks >= 40) && !marker.getTarget().isDead)
                    continue;
                needRemove.add(marker);
            }
            for (Marker marker : needRemove)
                this.particles.remove(marker);
        }
    }

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {
        this.particles.clear();
        this.healthMap.clear();
    }

    @Getter
    @Setter
    private static class Marker {

        private final Entity target;
        private final String text;
        private final double x, y, z;
        private int ticks = 0;

        public Marker(Entity entity, String text, double x, double y, double z) {
            this.target = entity;
            this.text = text;
            this.x = x;
            this.y = y;
            this.z = z;
        }

    }

}
