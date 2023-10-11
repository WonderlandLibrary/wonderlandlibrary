package com.initial.modules.impl.visual;

import com.initial.modules.*;
import com.initial.events.impl.*;
import org.lwjgl.opengl.*;
import java.awt.*;
import net.minecraft.block.*;
import com.initial.utils.render.*;
import net.minecraft.util.*;
import net.minecraft.client.renderer.entity.*;
import com.initial.events.*;

public class BlockOverlay extends Module
{
    public BlockOverlay() {
        super("BlockOverlay", 0, Category.VISUAL);
    }
    
    @EventTarget
    public void on3D(final EventRender3D e) {
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            final BlockPos pos = this.mc.objectMouseOver.getBlockPos();
            final Block block = this.mc.theWorld.getBlockState(pos).getBlock();
            final RenderManager renderManager = this.mc.getRenderManager();
            final String s = block.getLocalizedName();
            this.mc.getRenderManager();
            final double x = pos.getX() - renderManager.getRenderPosX();
            this.mc.getRenderManager();
            final double y = pos.getY() - renderManager.getRenderPosY();
            this.mc.getRenderManager();
            final double z = pos.getZ() - renderManager.getRenderPosZ();
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            final Color c = new Color(192, 75, 255);
            final int r = c.getRed();
            final int g = c.getGreen();
            final int b = c.getBlue();
            Render2DUtils.glColor(new Color(r, g, b, 50).getRGB());
            final double minX = (block instanceof BlockStairs || Block.getIdFromBlock(block) == 134) ? 0.0 : block.getBlockBoundsMinX();
            final double minY = (block instanceof BlockStairs || Block.getIdFromBlock(block) == 134) ? 0.0 : block.getBlockBoundsMinY();
            final double minZ = (block instanceof BlockStairs || Block.getIdFromBlock(block) == 134) ? 0.0 : block.getBlockBoundsMinZ();
            RenderUtil.drawBoundingBox(new AxisAlignedBB(x + minX, y + minY, z + minZ, x + block.getBlockBoundsMaxX(), y + block.getBlockBoundsMaxY(), z + block.getBlockBoundsMaxZ()));
            Render2DUtils.glColor(new Color(130, 60, 255).getRGB());
            GL11.glLineWidth(0.5f);
            GL11.glDisable(2848);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
