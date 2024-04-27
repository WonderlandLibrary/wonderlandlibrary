package me.aquavit.liquidsense.utils.block;

import me.aquavit.liquidsense.utils.mc.MinecraftInstance;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

import java.util.HashMap;
import java.util.Map;

public class BlockUtils extends MinecraftInstance {

    /**
     * Get block from [blockPos]
     */
    public static Block getBlock(BlockPos blockPos) {
        return mc.theWorld.getBlockState(blockPos).getBlock();
    }

    /**
     * Get material from [blockPos]
     */
    public static Material getMaterial(BlockPos blockPos) {
        return getBlock(blockPos).getMaterial();
    }

    /**
     * Check [blockPos] is replaceable
     */
    public static boolean isReplaceable(BlockPos blockPos) {
        return getMaterial(blockPos) != null && getMaterial(blockPos).isReplaceable();
    }

    /**
     * Get state from [blockPos]
     */
    public static IBlockState getState(BlockPos blockPos) {
        return mc.theWorld.getBlockState(blockPos);
    }

    /**
     * Check if [blockPos] is clickable
     */
    public static boolean canBeClicked(BlockPos blockPos) {
        if (getBlock(blockPos) != null && getBlock(blockPos).canCollideCheck(getState(blockPos), false)) {
            return mc.theWorld.getWorldBorder().contains(blockPos);
        }
        return false;
    }

    /**
     * Get block name by [id]
     */
    public static String getBlockName(int id) {
        return Block.getBlockById(id).getLocalizedName();
    }

    /**
     * Check if block is full block
     */
    public static boolean isFullBlock(BlockPos blockPos) {
        Block block = getBlock(blockPos);
        if (block != null) {
            AxisAlignedBB axisAlignedBB = block.getCollisionBoundingBox(mc.theWorld, blockPos, getState(blockPos));
            if (axisAlignedBB != null) {
                return axisAlignedBB.maxX - axisAlignedBB.minX == 1.0D && axisAlignedBB.maxY - axisAlignedBB.minY == 1.0D && axisAlignedBB.maxZ - axisAlignedBB.minZ == 1.0D;
            }
        }

        return false;
    }

    /**
     * Get distance to center of [blockPos]
     */
    public static double getCenterDistance(BlockPos blockPos) {
        return mc.thePlayer.getDistance(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
    }

    /**
     * Search blocks around the player in a specific [radius]
     */
    public static Map<BlockPos, Block> searchBlocks(int radius) {
        HashMap<BlockPos, Block> blocks = new HashMap<>();

        for (int x = radius; x >= -radius + 1; x--) {
            for (int y = radius; y >= -radius + 1; y--) {
                for (int z = radius; z >= -radius + 1; z--) {
                    BlockPos blockPos = new BlockPos((int) mc.thePlayer.posX + x, (int) mc.thePlayer.posY + y,
                            (int) mc.thePlayer.posZ + z);
                    Block block = getBlock(blockPos);
                    if (block != null) {
                        blocks.put(blockPos, block);
                    }

                }
            }
        }

        return blocks;
    }

    /**
     * Check if [axisAlignedBB] has collidable blocks using custom [collide] check
     */
    public static boolean collideBlock(AxisAlignedBB axisAlignedBB, Collidable collide) {
        for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                Block block = getBlock(new BlockPos(x, axisAlignedBB.minY, z));

                if (!collide.collideBlock(block))
                    return false;
            }
        }

        return true;
    }

    /**
     * Check if [axisAlignedBB] has collidable blocks using custom [collide] check
     */
    public static boolean collideBlockIntersects(AxisAlignedBB axisAlignedBB, Collidable collide) {
        for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
            for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
                BlockPos blockPos = new BlockPos(x, axisAlignedBB.minY, z);
                Block block = getBlock(blockPos);

                if (block != null && collide.collideBlock(block)) {
                    AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, blockPos, getState(blockPos));

                    if (boundingBox != null && mc.thePlayer.getEntityBoundingBox().intersectsWith(boundingBox))
                        return true;
                }
            }
        }

        return false;
    }
}
