/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.render;

import com.sun.javafx.geom.Vec3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.ReadableColor;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.RenderEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class ChestESP
extends Module {
    public ChestESP() {
        super("ChestESP", "ChestESP", Category.RENDER);
    }

    @Override
    public void onEvent(Event e2) {
        if (e2 instanceof RenderEvent && this.mc.thePlayer != null) {
            for (TileEntity tile : this.mc.theWorld.loadedTileEntityList) {
                if (!tile.getBlockType().equals(Blocks.chest)) continue;
                this.blockESPBox(tile.getPos(), ReadableColor.WHITE);
            }
        }
    }

    private void blockESPBox(BlockPos blockPos, ReadableColor c2) {
        double x2 = (double)blockPos.getX() - Minecraft.getMinecraft().getRenderManager().renderPosX;
        double y2 = (double)blockPos.getY() - Minecraft.getMinecraft().getRenderManager().renderPosY;
        double z2 = (double)blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().renderPosZ;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        RenderGlobal.drawBoundingBoxWithColor(new AxisAlignedBB(x2, y2, z2, x2 + 1.0, y2 + 1.0, z2 + 1.0), 255, 255, 255, 150);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }

    private void blockESPBox(Vec3d blockPos, double endX, double endY, double endZ, ReadableColor c2) {
        double x2 = blockPos.x - Minecraft.getMinecraft().getRenderManager().renderPosX;
        double y2 = blockPos.y - Minecraft.getMinecraft().getRenderManager().renderPosY;
        double z2 = blockPos.z - Minecraft.getMinecraft().getRenderManager().renderPosZ;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        RenderGlobal.drawBoundingBoxWithColor(new AxisAlignedBB(x2, y2, z2, x2 + 1.0, y2 + 1.0, z2 + 1.0), 255, 255, 255, 150);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
}

