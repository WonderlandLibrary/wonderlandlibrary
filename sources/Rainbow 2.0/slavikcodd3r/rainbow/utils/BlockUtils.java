package slavikcodd3r.rainbow.utils;

import java.util.Arrays;
import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import java.util.List;
import net.minecraft.client.Minecraft;

public class BlockUtils
{
    private static Minecraft mc;
    private static List<Block> blacklistedBlocks;
    
    public BlockUtils() {
        super();
    }
    
    public static float[] getRotationsNeeded(final BlockPos pos) {
        final double diffX = pos.getX() + 0.5 - BlockUtils.mc.thePlayer.posX;
        final double diffY = pos.getY() + 0.5 - (BlockUtils.mc.thePlayer.posY + BlockUtils.mc.thePlayer.height);
        final double diffZ = pos.getZ() + 0.5 - BlockUtils.mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { BlockUtils.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - BlockUtils.mc.thePlayer.rotationYaw), BlockUtils.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - BlockUtils.mc.thePlayer.rotationPitch) };
    }
    
    public static float[] updateDirections(final BlockPos pos) {
        final float[] looks = getRotationsNeeded(pos);
        if (BlockUtils.mc.thePlayer.isCollidedVertically) {
            NetUtil.sendPacketNoEvents(new C03PacketPlayer.C05PacketPlayerLook(looks[0], looks[1], BlockUtils.mc.thePlayer.onGround));
        }
        return looks;
    }
    
    public static void updateTool(final BlockPos pos) {
        final Block block = BlockUtils.mc.theWorld.getBlockState(pos).getBlock();
        float strength = 1.0f;
        int bestItemIndex = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack itemStack = BlockUtils.mc.thePlayer.inventory.mainInventory[i];
            if (itemStack != null) {
                if (itemStack.getStrVsBlock(block) > strength) {
                    strength = itemStack.getStrVsBlock(block);
                    bestItemIndex = i;
                }
            }
        }
        if (bestItemIndex != -1) {
            BlockUtils.mc.thePlayer.inventory.currentItem = bestItemIndex;
        }
    }
    
    public static boolean isInLiquid() {
        if (BlockUtils.mc.thePlayer.isInWater()) {
            return true;
        }
        boolean inLiquid = false;
        final int y = (int)BlockUtils.mc.thePlayer.getEntityBoundingBox().minY;
        for (int x = MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                final Block block = BlockUtils.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
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
        if (BlockUtils.mc.thePlayer == null) {
            return false;
        }
        boolean onLiquid = false;
        final int y = (int)BlockUtils.mc.thePlayer.getEntityBoundingBox().offset(0.0, -0.01, 0.0).minY;
        for (int x = MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                final Block block = BlockUtils.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
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
    
    public static boolean isOnLiquid(final double profondeur) {
        boolean onLiquid = false;
        if (BlockUtils.mc.theWorld.getBlockState(new BlockPos(BlockUtils.mc.thePlayer.posX, BlockUtils.mc.thePlayer.posY - profondeur, BlockUtils.mc.thePlayer.posZ)).getBlock().getMaterial().isLiquid()) {
            onLiquid = true;
        }
        return onLiquid;
    }
    
    public static boolean isTotalOnLiquid(final double profondeur) {
        for (double x = BlockUtils.mc.thePlayer.boundingBox.minX; x < BlockUtils.mc.thePlayer.boundingBox.maxX; x += 0.009999999776482582) {
            for (double z = BlockUtils.mc.thePlayer.boundingBox.minZ; z < BlockUtils.mc.thePlayer.boundingBox.maxZ; z += 0.009999999776482582) {
                final Block block = BlockUtils.mc.theWorld.getBlockState(new BlockPos(x, BlockUtils.mc.thePlayer.posY - profondeur, z)).getBlock();
                if (!(block instanceof BlockLiquid) && !(block instanceof BlockAir)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean isOnGround(final double height) {
        return !BlockUtils.mc.theWorld.getCollidingBoundingBoxes(BlockUtils.mc.thePlayer, BlockUtils.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
    
    public static List<Block> getBlacklistedBlocks() {
        return BlockUtils.blacklistedBlocks;
    }
    
    static {
        BlockUtils.mc = Minecraft.getMinecraft();
        BlockUtils.blacklistedBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever);
    }
}
