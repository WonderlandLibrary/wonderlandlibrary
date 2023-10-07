/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.util.misc;

import me.arithmo.util.MinecraftUtil;
import me.arithmo.util.NetUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class BlockUtil
implements MinecraftUtil {
    public static float[] getRotationsNeeded(BlockPos pos) {
        double diffX = (double)pos.getX() + 0.5 - BlockUtil.mc.thePlayer.posX;
        double diffY = (double)pos.getY() + 0.5 - (BlockUtil.mc.thePlayer.posY + (double)BlockUtil.mc.thePlayer.height);
        double diffZ = (double)pos.getZ() + 0.5 - BlockUtil.mc.thePlayer.posZ;
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[]{BlockUtil.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - BlockUtil.mc.thePlayer.rotationYaw), BlockUtil.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - BlockUtil.mc.thePlayer.rotationPitch)};
    }

    public static float[] updateDirections(BlockPos pos) {
        float[] looks = BlockUtil.getRotationsNeeded(pos);
        if (BlockUtil.mc.thePlayer.isCollidedVertically) {
            NetUtil.sendPacketNoEvents(new C03PacketPlayer.C05PacketPlayerLook(looks[0], looks[1], BlockUtil.mc.thePlayer.onGround));
        }
        return looks;
    }

    public static void updateTool(BlockPos pos) {
        Block block = BlockUtil.mc.theWorld.getBlockState(pos).getBlock();
        float strength = 1.0f;
        int bestItemIndex = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = BlockUtil.mc.thePlayer.inventory.mainInventory[i];
            if (itemStack == null || itemStack.getStrVsBlock(block) <= strength) continue;
            strength = itemStack.getStrVsBlock(block);
            bestItemIndex = i;
        }
        if (bestItemIndex != -1) {
            BlockUtil.mc.thePlayer.inventory.currentItem = bestItemIndex;
        }
    }

    public static boolean isInLiquid() {
        if (BlockUtil.mc.thePlayer.isInWater()) {
            return true;
        }
        boolean inLiquid = false;
        int y = (int)BlockUtil.mc.thePlayer.getEntityBoundingBox().minY;
        for (int x = MathHelper.floor_double((double)BlockUtil.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double((double)BlockUtil.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                Block block = BlockUtil.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block == null || block.getMaterial() == Material.air) continue;
                if (!(block instanceof BlockLiquid)) {
                    return false;
                }
                inLiquid = true;
            }
        }
        return inLiquid;
    }

    public static boolean isOnLiquid() {
        if (BlockUtil.mc.thePlayer == null) {
            return false;
        }
        boolean onLiquid = false;
        int y = (int)BlockUtil.mc.thePlayer.getEntityBoundingBox().offset((double)0.0, (double)-0.01, (double)0.0).minY;
        for (int x = MathHelper.floor_double((double)BlockUtil.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double((double)BlockUtil.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                Block block = BlockUtil.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block == null || block.getMaterial() == Material.air) continue;
                if (!(block instanceof BlockLiquid)) {
                    return false;
                }
                onLiquid = true;
            }
        }
        return onLiquid;
    }
}

