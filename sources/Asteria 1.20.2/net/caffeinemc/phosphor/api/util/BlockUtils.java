package net.caffeinemc.phosphor.api.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.util.math.BlockPos;

import static net.caffeinemc.phosphor.common.Phosphor.mc;

public class BlockUtils {
    public static BlockState getBlockState(BlockPos pos) {
        return mc.world.getBlockState(pos);
    }

    public static boolean isBlock(Block block, BlockPos pos) {
        return getBlockState(pos).getBlock() == block;
    }

    public static boolean isAnchorCharged(BlockPos anchor) {
        try {
            if (!isBlock(Blocks.RESPAWN_ANCHOR, anchor))
                return false;

            return getBlockState(anchor).get(RespawnAnchorBlock.CHARGES) != 0;
        }
        catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isAnchorUncharged(BlockPos anchor) {
        try {
            if (!isBlock(Blocks.RESPAWN_ANCHOR, anchor))
                return false;

            return getBlockState(anchor).get(RespawnAnchorBlock.CHARGES) == 0;
        } catch (IllegalArgumentException var2) {
            return false;
        }
    }
}
