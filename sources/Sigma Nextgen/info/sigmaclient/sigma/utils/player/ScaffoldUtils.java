package info.sigmaclient.sigma.utils.player;

import lombok.Setter;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import static info.sigmaclient.sigma.modules.Module.mc;

public class ScaffoldUtils {

    public static class BlockCache {

        private BlockPos position;
        @Setter
        private Direction facing;

        public BlockCache(final BlockPos position, final Direction facing) {
            this.position = position;
            this.facing = facing;
        }

        public BlockPos getPosition() {
            return position;
        }

        public Direction getFacing() {
            return facing;
        }
    }

    public static BlockCache check(BlockPos pos) {
        if (isOkBlock(pos.add(0, -0.5, 0))) {
            return new BlockCache(pos.add(0, -0.5, 0), Direction.UP);
        }
        if (isOkBlock(pos.add(-1, 0, 0))) {
            return new BlockCache(pos.add(-1, 0, 0), Direction.EAST);
        }
        if (isOkBlock(pos.add(1, 0, 0))) {
            return new BlockCache(pos.add(1, 0, 0), Direction.WEST);
        }
        if (isOkBlock(pos.add(0, 0, 1))) {
            return new BlockCache(pos.add(0, 0, 1), Direction.NORTH);
        }
        if (isOkBlock(pos.add(0, 0, -1))) {
            return new BlockCache(pos.add(0, 0, -1), Direction.SOUTH);
        }
        return null;
    }
    public static BlockCache check2(BlockPos pos) {
        if (isOkBlock(pos.add(0, -0.5, 0))) {
            return new BlockCache(pos.add(0, -0.5, 0), Direction.UP);
        }
        if (isOkBlock(pos.add(-1, 0, 0))) {
            return new BlockCache(pos.add(-1, 0, 0), Direction.EAST);
        }
        if (isOkBlock(pos.add(1, 0, 0))) {
            return new BlockCache(pos.add(1, 0, 0), Direction.WEST);
        }
        if (isOkBlock(pos.add(0, 0, 1))) {
            return new BlockCache(pos.add(0, 0, 1), Direction.NORTH);
        }
        if (isOkBlock(pos.add(0, 0, -1))) {
            return new BlockCache(pos.add(0, 0, -1), Direction.SOUTH);
        }
        return null;
    }

    static int[][] xzoff = new int[][]{
            {1, 0}, {0, 1}, {-1, 0}, {0, -1}
    };

    public static BlockCache getBlockCache(BlockPos pos, int range, boolean xz, boolean no) {
        BlockCache cache;
        if (no) {
            return check(pos);
        }
        cache = check(pos);
        if (cache != null)
            return cache;
        for (int[] ints : xzoff) {
            for (int extend = 1; extend <= range; extend++) {
                BlockPos pos1 = pos.add(ints[0] * extend, 0, ints[1] * extend);
                cache = check2(pos1);
                if (cache != null)
                    return cache;
            }
        }
        return null;
    }

    public static boolean isOkBlock(BlockItem blocks) {
        Block block = blocks.getBlock();
        return !(block instanceof FlowingFluidBlock) &&
                !(block instanceof AirBlock) &&
                !(block instanceof ChestBlock) &&
                !(block instanceof FurnaceBlock);
    }

    public static boolean isOkBlock(BlockPos blockPos) {
        Block block = mc.world.getBlockState(blockPos).getBlock();
        return !(block instanceof FlowingFluidBlock) &&
                !(block instanceof AirBlock) &&
                !(block instanceof ChestBlock) &&
                !(block instanceof FurnaceBlock);
    }
}
