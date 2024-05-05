/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.RENDER;

import com.darkmagician6.eventapi.EventTarget;
import java.util.List;
import me.AveReborn.events.EventRender;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

public class ChestESP
extends Mod {
    public ChestESP() {
        super("ChestESP", Category.RENDER);
    }

    @EventTarget
    public void onRender(EventRender event) {
        for (TileEntity tileentity : this.mc.theWorld.loadedTileEntityList) {
            if (tileentity instanceof TileEntityChest) {
                ChestESP.renderChest(tileentity.getPos());
            }
            if (!(tileentity instanceof TileEntityEnderChest)) continue;
            ChestESP.renderEnderChest(tileentity.getPos());
        }
    }

    public static void renderChest(BlockPos blockPos) {
        double d0 = (double)blockPos.getX() - Minecraft.getMinecraft().getRenderManager().viewerPosX;
        double d1 = (double)blockPos.getY() - Minecraft.getMinecraft().getRenderManager().viewerPosY;
        double d2 = (double)blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(true);
        GL11.glColor4d(255.0, 170.0, 0.0, 1.0);
        RenderGlobal.func_181561_a(new AxisAlignedBB(d0, d1, d2, d0 + 1.0, d1 + 1.0, d2 + 1.0));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void renderEnderChest(BlockPos blockPos) {
        double d0 = (double)blockPos.getX() - Minecraft.getMinecraft().getRenderManager().viewerPosX;
        double d1 = (double)blockPos.getY() - Minecraft.getMinecraft().getRenderManager().viewerPosY;
        double d2 = (double)blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(true);
        GL11.glColor4d(170.0, 0.0, 170.0, 1.0);
        RenderGlobal.func_181561_a(new AxisAlignedBB(d0, d1, d2, d0 + 1.0, d1 + 1.0, d2 + 1.0));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
}

