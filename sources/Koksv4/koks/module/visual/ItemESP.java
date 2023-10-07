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
import net.minecraft.entity.item.EntityItem;
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

@Module.Info(name = "ItemESP", description = "You can see items through walls", category = Module.Category.VISUAL)
public class ItemESP extends Module implements Module.Shader {

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

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        return switch (name) {
            case "CylinderCull" -> modeCylinder;
            case "Shader-Mode" -> modeShader;
            default -> super.isVisible(value, name);
        };
    }

    @Override
    @Event.Info
    public void onEvent(Event event) {
        final RenderUtil renderUtil = RenderUtil.getInstance();

        if(event instanceof Render3DEvent) {
            final Color customColor = new Color(color);
            if(modeCylinder) {
                for (Entity entity : getWorld().loadedEntityList) {
                    if (entity instanceof EntityItem) {
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
        }

        if (event instanceof Render2DEvent) {
            if (mode2D) {
                final Color customColor = new Color(color);
                for (Entity entity : getWorld().loadedEntityList) {
                    if (entity instanceof EntityItem) {
                        // ignore this and look at the method render2DOutline
                        ProjectionRenderer.projectionRenderer.render2DESP(entity, customColor);
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

    koks.module.visual.Shader shader;
    final Predicate<Entity> predicate = entity -> (entity instanceof EntityItem);


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
