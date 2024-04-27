package me.aquavit.liquidsense.utils.extensions;

import me.aquavit.liquidsense.utils.block.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class BlockExtensionUtils {

    public static Block getBlock(BlockPos blockPos) {
        return BlockUtils.getBlock(blockPos);
    }

    public static Vec3 getVec(BlockPos blockPos) {
        return new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
    }

}
