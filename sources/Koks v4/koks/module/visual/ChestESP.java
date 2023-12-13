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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.util.List;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "ChestESP", description = "Marks all chests in the world.", category = Module.Category.VISUAL)
public class ChestESP extends Module implements Module.Shader {


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

    @Override
    @Event.Info
    public void onEvent(Event event) {
        final RenderUtil renderUtil = RenderUtil.getInstance();

        if (event instanceof Render3DEvent) {
            final Color customColor = new Color(color);
            for (TileEntity e : getWorld().loadedTileEntityList) {
                if (e instanceof TileEntityChest || e instanceof TileEntityEnderChest) {
                    getWorld().getBlockState(e.getPos()).getBlock();
                    final double x = (e.getPos().getX() - mc.getRenderManager().renderPosX);
                    final double y = (e.getPos().getY() - mc.getRenderManager().renderPosY);
                    final double z = (e.getPos().getZ() - mc.getRenderManager().renderPosZ);
                    if (modeCylinder) {
                        GL11.glPushMatrix();
                        GL11.glTranslated(x + 0.5, y + 0.9, z + 0.5);
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
                        cylinder.draw(0.62f, 0.62f, 0.9f, 8, 1);

                        renderUtil.setColor(renderUtil.getAlphaColor(customColor, 150));
                        cylinder.setDrawStyle(GLU.GLU_FILL);
                        cylinder.setOrientation(GLU.GLU_INSIDE);
                        cylinder.draw(0.62f, 0.65f, 0.9f, 8, 1);

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
                for (TileEntity e : getWorld().loadedTileEntityList) {
                    if (e instanceof TileEntityChest || e instanceof TileEntityEnderChest) {
                        // ignore this and look at the method render2DOutline
                        ProjectionRenderer.projectionRenderer.render2DESP(e, customColor);
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

    @Override
    public void drawShaderESP(List list, boolean flag, RenderManager renderManager, float partialTicks, Entity renderViewEntity, ICamera camera, int i) {
        if (modeShader) {
            if (shader.isToggled())
                shader.drawChestShader(list, flag, renderManager, partialTicks, renderViewEntity, camera, i);
            else {
                sendMessage("§cPlease toggle the Shader module!");
                setToggled(false);
            }
        }
    }
}