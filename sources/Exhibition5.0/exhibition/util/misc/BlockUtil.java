// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.util.misc;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.network.Packet;
import exhibition.util.NetUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos;
import exhibition.util.MinecraftUtil;

public class BlockUtil implements MinecraftUtil
{
    public static float[] getRotationsNeeded(final BlockPos pos) {
        final double diffX = pos.getX() + 0.5 - BlockUtil.mc.thePlayer.posX;
        final double diffY = pos.getY() + 0.5 - (BlockUtil.mc.thePlayer.posY + BlockUtil.mc.thePlayer.height);
        final double diffZ = pos.getZ() + 0.5 - BlockUtil.mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { BlockUtil.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - BlockUtil.mc.thePlayer.rotationYaw), BlockUtil.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - BlockUtil.mc.thePlayer.rotationPitch) };
    }
    
    public static float[] updateDirections(final BlockPos pos) {
        final float[] looks = getRotationsNeeded(pos);
        if (BlockUtil.mc.thePlayer.isCollidedVertically) {
            NetUtil.sendPacketNoEvents(new C03PacketPlayer.C05PacketPlayerLook(looks[0], looks[1], BlockUtil.mc.thePlayer.onGround));
        }
        return looks;
    }
    
    public static void updateTool(final BlockPos pos) {
        final Block block = BlockUtil.mc.theWorld.getBlockState(pos).getBlock();
        float strength = 1.0f;
        int bestItemIndex = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack itemStack = BlockUtil.mc.thePlayer.inventory.mainInventory[i];
            if (itemStack != null) {
                if (itemStack.getStrVsBlock(block) > strength) {
                    strength = itemStack.getStrVsBlock(block);
                    bestItemIndex = i;
                }
            }
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
        final int y = (int)BlockUtil.mc.thePlayer.getEntityBoundingBox().minY;
        for (int x = MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                final Block block = BlockUtil.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && block.getMaterial() != Material.air) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    inLiquid = true;
                }
            }
        }
        return inLiquid;
    }
    
    public static boolean isOnLiquid() {
        if (BlockUtil.mc.thePlayer == null) {
            return false;
        }
        boolean onLiquid = false;
        final int y = (int)BlockUtil.mc.thePlayer.getEntityBoundingBox().offset(0.0, -0.01, 0.0).minY;
        for (int x = MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(BlockUtil.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                final Block block = BlockUtil.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && block.getMaterial() != Material.air) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }
}
