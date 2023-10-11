package com.initial.modules.impl.visual;

import com.initial.modules.*;
import com.initial.events.impl.*;
import net.minecraft.util.*;
import net.minecraft.tileentity.*;
import java.util.*;
import com.initial.events.*;
import org.lwjgl.opengl.*;
import com.initial.utils.render.*;

public class ChestESP extends Module
{
    public ChestESP() {
        super("ChestESP", 0, Category.VISUAL);
    }
    
    @EventTarget
    public void on3D(final Event3D event) {
        for (final TileEntity tile : this.mc.theWorld.loadedTileEntityList) {
            final double posX = tile.getPos().getX() - this.mc.getRenderManager().getRenderPosX();
            final double posY = tile.getPos().getY() - this.mc.getRenderManager().getRenderPosY();
            final double posZ = tile.getPos().getZ() - this.mc.getRenderManager().getRenderPosZ();
            if (tile instanceof TileEntityChest) {
                AxisAlignedBB bb = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.94, 0.875, 0.94).offset(posX, posY, posZ);
                TileEntityChest adjacent = null;
                if (((TileEntityChest)tile).adjacentChestXNeg != null) {
                    adjacent = ((TileEntityChest)tile).adjacentChestXNeg;
                }
                if (((TileEntityChest)tile).adjacentChestXPos != null) {
                    adjacent = ((TileEntityChest)tile).adjacentChestXPos;
                }
                if (((TileEntityChest)tile).adjacentChestZNeg != null) {
                    adjacent = ((TileEntityChest)tile).adjacentChestZNeg;
                }
                if (((TileEntityChest)tile).adjacentChestZPos != null) {
                    adjacent = ((TileEntityChest)tile).adjacentChestZPos;
                }
                if (adjacent != null) {
                    bb = bb.union(new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.94, 0.875, 0.94).offset(adjacent.getPos().getX() - this.mc.getRenderManager().getRenderPosX(), adjacent.getPos().getY() - this.mc.getRenderManager().getRenderPosY(), adjacent.getPos().getZ() - this.mc.getRenderManager().getRenderPosZ()));
                }
                if (((TileEntityChest)tile).getChestType() == 1) {
                    this.drawBlockESP(bb, 255.0f, 91.0f, 86.0f, 255.0f, 1.0f);
                }
                else {
                    this.drawBlockESP(bb, 255.0f, 227.0f, 0.0f, 255.0f, 1.0f);
                }
            }
            if (tile instanceof TileEntityEnderChest) {
                this.drawBlockESP(new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.94, 0.875, 0.94).offset(posX, posY, posZ), 78.0f, 197.0f, 255.0f, 255.0f, 1.0f);
            }
        }
    }
    
    private void drawBlockESP(final AxisAlignedBB bb, final float red, final float green, final float blue, final float alpha, final float width) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red / 255.0f, green / 255.0f, blue / 255.0f, 0.2f);
        RenderUtil.drawBoundingBox(bb);
        GL11.glLineWidth(width);
        GL11.glColor4f(red / 255.0f, green / 255.0f, blue / 255.0f, alpha / 255.0f);
        RenderUtil.drawOutlinedBoundingBox(bb);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
