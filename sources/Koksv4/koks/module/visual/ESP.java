package koks.module.visual;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import koks.api.utils.ProjectionRenderer;
import koks.api.utils.RenderUtil;
import koks.api.manager.value.annotation.Value;
import koks.event.Render2DEvent;
import koks.event.Render3DEvent;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.util.List;
import java.util.function.Predicate;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "ESP", description = "You can see Entities through Walls", category = Module.Category.VISUAL)
public class ESP extends Module implements Module.Shader {

    @Value(name = "Player")
    boolean player = true;

    @Value(name = "Animal")
    boolean animal = false;

    @Value(name = "Mob")
    boolean mob = false;

    @Value(name = "Villager")
    boolean villager = false;

    @Value(name = "ArmorStand")
    boolean armorStand = false;

    @Value(name = "2D")
    boolean mode2D = false;

    @Value(name = "Cylinder")
    boolean modeCylinder = false;

    @Value(name = "CylinderCull")
    boolean cylinderCull = false;

    @Value(name = "Shader")
    boolean modeShader = false;

    @Value(name = "Color", colorPicker = true)
    int color = Color.green.getRGB();

    koks.module.visual.Shader shader;

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        return switch (name) {
            case "CylinderCull" -> modeCylinder;
            case "Shader-Mode" -> modeShader;
            default -> super.isVisible(value, name);
        };
    }

    final Predicate<Entity> predicate = entity -> (this.player && entity instanceof EntityPlayer) ||
            (this.villager && entity instanceof EntityVillager) ||
            (this.armorStand && entity instanceof EntityArmorStand) ||
            (this.mob && entity instanceof EntityMob) ||
            (this.animal && entity instanceof EntityAnimal);

    @Override
    @Event.Info
    public void onEvent(Event event) {
        final RenderUtil renderUtil = RenderUtil.getInstance();

        if (event instanceof Render3DEvent) {
            final Color customColor = new Color(color);
            if (modeCylinder) {
                for (Entity entity : getWorld().loadedEntityList) {
                    if (predicate.test(entity) && entity != getPlayer() && !entity.isInvisible() && entity instanceof EntityLivingBase) {
                        final double x = ((entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX);
                        final double y = ((entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY);
                        final double z = ((entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ);

                        GL11.glPushMatrix();
                        GL11.glTranslated(x, y + entity.height, z);
                        GL11.glNormal3d(0.0, 1.0, 0.0);
                        GL11.glRotated(90, 1, 0, 0);

                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                        GL11.glEnable(GL11.GL_BLEND);
                        if (cylinderCull) {
                            GL11.glDisable(GL11.GL_CULL_FACE);
                        }
                        GL11.glDisable(GL11.GL_DEPTH_TEST);

                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        GL11.glLineWidth(1);
                        renderUtil.setColor(customColor);
                        final Cylinder cylinder = new Cylinder();

                        cylinder.setDrawStyle(GLU.GLU_LINE);
                        cylinder.setOrientation(GLU.GLU_INSIDE);
                        cylinder.draw(0.62f, 0.62f, entity.height, 8, 1);

                        renderUtil.setColor(renderUtil.getAlphaColor(customColor, 150));
                        cylinder.setDrawStyle(GLU.GLU_FILL);
                        cylinder.setOrientation(GLU.GLU_INSIDE);
                        cylinder.draw(0.62f, 0.65f, entity.height, 8, 1);

                        GL11.glDisable(GL11.GL_BLEND);
                        if (cylinderCull) {
                            GL11.glEnable(GL11.GL_CULL_FACE);
                        }
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                        GL11.glPopMatrix();
                    }
                }
            }
        } else if (event instanceof Render2DEvent) {
            if (mode2D) {
                for (Entity entity : getWorld().loadedEntityList) {
                    if (predicate.test(entity) && entity != getPlayer() && !entity.isInvisible() && entity instanceof EntityLivingBase) {
                        // ignore this and look at the method render2DOutline
                        ProjectionRenderer.projectionRenderer.render2DESP(entity, new Color(color));
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {
        shader = ModuleRegistry.getModule(koks.module.visual.Shader.class);
    }

    @Override
    public void onDisable() {

    }

    private void translateRotate(Entity entity) {
        final double x = ((entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX);
        final double y = ((entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY);
        final double z = ((entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ);
        GL11.glTranslated(x, y, z);
        GL11.glNormal3d(0.0, 1.0, 0.0);
        GL11.glRotated(-mc.getRenderManager().playerViewY, 0.0, 1.0, 0.0);
    }

    @Override
    public void drawShaderESP(List list, boolean flag, RenderManager renderManager, float partialTicks, Entity renderViewEntity, ICamera camera, int i) {
        if (modeShader) {
            if (shader.isToggled())
                shader.drawShader(predicate, list, flag, renderManager, partialTicks, renderViewEntity, camera, i);
            else {
                sendMessage("§cPlease toggle the Shader module!");
                setToggled(false);
            }
        }
    }
}
