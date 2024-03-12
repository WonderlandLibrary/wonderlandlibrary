// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.player;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import net.minecraft.util.MathHelper;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import dev.tenacity.utils.Utils;

public class BlockUtils implements Utils
{
    public static boolean isValidBlock(final BlockPos pos) {
        return isValidBlock(BlockUtils.mc.theWorld.getBlockState(pos).getBlock(), false);
    }
    
    public static Block getBlockAtPos(final BlockPos pos) {
        final IBlockState blockState = BlockUtils.mc.theWorld.getBlockState(pos);
        return blockState.getBlock();
    }
    
    public static boolean isValidBlock(final Block block, final boolean placing) {
        return !(block instanceof BlockCarpet) && !(block instanceof BlockSnow) && !(block instanceof BlockContainer) && !(block instanceof BlockBasePressurePlate) && !block.getMaterial().isLiquid() && (!placing || (!(block instanceof BlockSlab) && !(block instanceof BlockStairs) && !(block instanceof BlockLadder) && !(block instanceof BlockStainedGlassPane) && !(block instanceof BlockWall) && !(block instanceof BlockWeb) && !(block instanceof BlockCactus) && !(block instanceof BlockFalling) && block != Blocks.glass_pane && block != Blocks.iron_bars)) && (block.getMaterial().isSolid() || !block.isTranslucent() || block.isFullBlock());
    }
    
    public static boolean isInLiquid() {
        if (BlockUtils.mc.thePlayer == null) {
            return false;
        }
        for (int x = MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                final BlockPos pos = new BlockPos(x, (int)BlockUtils.mc.thePlayer.getEntityBoundingBox().minY, z);
                final Block block = BlockUtils.mc.theWorld.getBlockState(pos).getBlock();
                if (block != null && !(block instanceof BlockAir)) {
                    return block instanceof BlockLiquid;
                }
            }
        }
        return false;
    }
    
    public static boolean isOnLiquid() {
        if (BlockUtils.mc.thePlayer == null) {
            return false;
        }
        AxisAlignedBB boundingBox = BlockUtils.mc.thePlayer.getEntityBoundingBox();
        if (boundingBox != null) {
            boundingBox = boundingBox.contract(0.01, 0.0, 0.01).offset(0.0, -0.01, 0.0);
            boolean onLiquid = false;
            final int y = (int)boundingBox.minY;
            for (int x = MathHelper.floor_double(boundingBox.minX); x < MathHelper.floor_double(boundingBox.maxX + 1.0); ++x) {
                for (int z = MathHelper.floor_double(boundingBox.minZ); z < MathHelper.floor_double(boundingBox.maxZ + 1.0); ++z) {
                    final Block block = BlockUtils.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != Blocks.air) {
                        if (!(block instanceof BlockLiquid)) {
                            return false;
                        }
                        onLiquid = true;
                    }
                }
            }
            return onLiquid;
        }
        return false;
    }
}
