package dev.rise.util.world;

import lombok.experimental.UtilityClass;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public final class BlockUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static final List<Block> BLOCK_BLACKLIST = Arrays.asList(Blocks.enchanting_table, Blocks.chest, Blocks.ender_chest,
            Blocks.trapped_chest, Blocks.anvil, Blocks.sand, Blocks.web, Blocks.torch,
            Blocks.crafting_table, Blocks.furnace, Blocks.waterlily, Blocks.dispenser,
            Blocks.stone_pressure_plate, Blocks.wooden_pressure_plate, Blocks.noteblock,
            Blocks.dropper, Blocks.tnt, Blocks.standing_banner, Blocks.wall_banner, Blocks.redstone_torch);

    public static float[] getDirectionToBlock(final double x, final double y, final double z, final EnumFacing enumfacing) {
        final EntityEgg var4 = new EntityEgg(mc.theWorld);
        var4.posX = x + 0.5D;
        var4.posY = y + 0.5D;
        var4.posZ = z + 0.5D;
        var4.posX += (double) enumfacing.getDirectionVec().getX() * 0.5D;
        var4.posY += (double) enumfacing.getDirectionVec().getY() * 0.5D;
        var4.posZ += (double) enumfacing.getDirectionVec().getZ() * 0.5D;
        return getRotations(var4.posX, var4.posY, var4.posZ);
    }

    public static float[] getRotations(final double posX, final double posY, final double posZ) {
        final EntityPlayerSP player = mc.thePlayer;
        final double x = posX - player.posX;
        final double y = posY - (player.posY + (double) player.getEyeHeight());
        final double z = posZ - player.posZ;
        final double dist = MathHelper.sqrt_double(x * x + z * z);
        final float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        final float pitch = (float) (-(Math.atan2(y, dist) * 180.0D / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static boolean lookingAtBlock(final BlockPos blockFace, final float yaw, final float pitch, final EnumFacing enumFacing, final boolean strict) {
        final MovingObjectPosition movingObjectPosition = mc.thePlayer.rayTraceCustom(mc.playerController.getBlockReachDistance(), mc.timer.renderPartialTicks, yaw, pitch);
        if (movingObjectPosition == null) return false;
        final Vec3 hitVec = movingObjectPosition.hitVec;
        if (hitVec == null) return false;
        if ((hitVec.xCoord - blockFace.getX()) > 1.0 || (hitVec.xCoord - blockFace.getX()) < 0.0) return false;
        if ((hitVec.yCoord - blockFace.getY()) > 1.0 || (hitVec.yCoord - blockFace.getY()) < 0.0) return false;
        return !((hitVec.zCoord - blockFace.getZ()) > 1.0) && !((hitVec.zCoord - blockFace.getZ()) < 0.0) && (movingObjectPosition.sideHit == enumFacing || !strict);
    }

    public static int findBlock() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock && itemStack.stackSize > 0) {
                final ItemBlock itemBlock = (ItemBlock) itemStack.getItem();
                final Block block = itemBlock.getBlock();
                if (block.isFullCube() && !BLOCK_BLACKLIST.contains(block)) {
                    return i;
                }
            }
        }

        return -1;
    }
}
