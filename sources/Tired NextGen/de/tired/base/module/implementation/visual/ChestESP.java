package de.tired.base.module.implementation.visual;

import de.tired.base.annotations.ModuleAnnotation;

import de.tired.util.render.RenderUtil;
import de.tired.base.guis.newclickgui.setting.impl.ColorPickerSetting;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.Render3DEvent2;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@ModuleAnnotation(name = "ChestESP", category = ModuleCategory.RENDER)
public class ChestESP extends Module {

    public ColorPickerSetting chestESPColor = new ColorPickerSetting("ChestESPColor", this, new Color(76, 204, 145, 255));

    @EventTarget
    public void onRender3D(Render3DEvent2 event) {
        for (TileEntity e : MC.theWorld.loadedTileEntityList) {
            if (e instanceof TileEntityChest) {

                final double x = (double) e.getPos().getX() - RenderManager.renderPosX;
                final double y = (double) e.getPos().getY() - RenderManager.renderPosY;
                final double z = (double) e.getPos().getZ() - RenderManager.renderPosZ;

                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                RenderUtil.glColor(new Color(chestESPColor.getColorPickerC().getRed(), chestESPColor.ColorPickerC.getGreen(), chestESPColor.getColorPickerC().getBlue(), 85).getRGB());
                RenderUtil.instance.drawBoundingBox(new AxisAlignedBB(x + e.getBlockType().getBlockBoundsMinX(), y + e.getBlockType().getBlockBoundsMinY(), z + e.getBlockType().getBlockBoundsMinZ(), x + e.getBlockType().getBlockBoundsMaxX(), y + e.getBlockType().getBlockBoundsMaxY(), z + e.getBlockType().getBlockBoundsMaxZ()));
                RenderUtil.instance.drawOutlinedBoundingBox(new AxisAlignedBB(x + e.getBlockType().getBlockBoundsMinX(), y + e.getBlockType().getBlockBoundsMinY(), z + e.getBlockType().getBlockBoundsMinZ(), x + e.getBlockType().getBlockBoundsMaxX(), y + e.getBlockType().getBlockBoundsMaxY(), z + e.getBlockType().getBlockBoundsMaxZ()));
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();

                GlStateManager.resetColor();

            }
        }
    }


    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
