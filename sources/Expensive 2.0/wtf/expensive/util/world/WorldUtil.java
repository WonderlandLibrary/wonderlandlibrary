package wtf.expensive.util.world;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import wtf.expensive.util.IMinecraft;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WorldUtil implements IMinecraft {

    public static class TotemUtil {
        public static BlockPos getBlock(float distance, Block block) {
            return getSphere(getPlayerPosLocal(), distance, 6, false, true, 0)
                    .stream()
                    .filter(position -> mc.world.getBlockState(position).getBlock() == block)
                    .min(Comparator.comparing(blockPos -> getDistanceOfEntityToBlock(mc.player, blockPos)))
                    .orElse(null);
        }

        public static BlockPos getBlock(float distance) {
            return getSphere(getPlayerPosLocal(), distance, 6, false, true, 0)
                    .stream()
                    .filter(position -> mc.world.getBlockState(position).getBlock() != net.minecraft.block.Blocks.AIR)
                    .min(Comparator.comparing(blockPos -> getDistanceOfEntityToBlock(mc.player, blockPos)))
                    .orElse(null);
        }

        public static BlockPos getBlockFlat(int distance) {

            BlockPos vec = getPlayerPosLocal().add(0,-1,0);

            for (int x = vec.getX() - distance; x <= vec.getX() + distance; x++) {
                for (int z = vec.getX() - distance; z <= vec.getZ() + distance; z++) {
                    if (mc.world.getBlockState(new BlockPos(x, vec.getY(), z)).getBlock() != net.minecraft.block.Blocks.AIR) {
                        return new BlockPos(x, vec.getY(), z);
                    }
                }
            }
            return vec;
        }

        public static List<BlockPos> getSphere(final BlockPos blockPos, final float n, final int n2, final boolean b,
                                               final boolean b2, final int n3) {
            final ArrayList<BlockPos> list = new ArrayList<BlockPos>();
            final int x = blockPos.getX();
            final int y = blockPos.getY();
            final int z = blockPos.getZ();
            for (int n4 = x - (int) n; n4 <= x + n; ++n4) {
                for (int n5 = z - (int) n; n5 <= z + n; ++n5) {
                    for (int n6 = b2 ? (y - (int) n) : y; n6 < (b2 ? (y + n) : ((float) (y + n2))); ++n6) {
                        final double n7 = (x - n4) * (x - n4) + (z - n5) * (z - n5) + (b2 ? ((y - n6) * (y - n6)) : 0);
                        if (n7 < n * n && (!b || n7 >= (n - 1.0f) * (n - 1.0f))) {
                            list.add(new BlockPos(n4, n6 + n3, n5));
                        }
                    }
                }
            }
            return list;
        }

        public static BlockPos getPlayerPosLocal() {
            if (mc.player == null) {
                return BlockPos.ZERO;
            }
            return new BlockPos(Math.floor(mc.player.getPosX()), Math.floor(mc.player.getPosY()), Math.floor(mc.player.getPosZ()));
        }

        public static double getDistanceOfEntityToBlock(final Entity entity, final BlockPos blockPos) {
            return getDistance(entity.getPosX(), entity.getPosY(), entity.getPosZ(), blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }


        public static double getDistance(final double n, final double n2, final double n3, final double n4,
                                         final double n5, final double n6) {
            final double n7 = n - n4;
            final double n8 = n2 - n5;
            final double n9 = n3 - n6;
            return MathHelper.sqrt(n7 * n7 + n8 * n8 + n9 * n9);
        }
    }

    public static class Blocks {
        public static ArrayList<BlockPos> getAllInBox(BlockPos from, BlockPos to) {
            ArrayList<BlockPos> blocks = new ArrayList<BlockPos>();
            BlockPos min = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
            BlockPos max = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
            for (int x = min.getX(); x <= max.getX(); ++x) {
                for (int y = min.getY(); y <= max.getY(); ++y) {
                    for (int z = min.getZ(); z <= max.getZ(); ++z) {
                        blocks.add(new BlockPos(x, y, z));
                    }
                }
            }
            return blocks;
        }

        public static CopyOnWriteArrayList<BlockPos> getAllInBoxA(BlockPos from, BlockPos to) {
            CopyOnWriteArrayList<BlockPos> blocks = new CopyOnWriteArrayList<BlockPos>();
            BlockPos min = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
            BlockPos max = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
            for (int x = min.getX(); x <= max.getX(); ++x) {
                for (int y = min.getY(); y <= max.getY(); ++y) {
                    for (int z = min.getZ(); z <= max.getZ(); ++z) {
                        if (mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() == net.minecraft.block.Blocks.AIR) continue;
                            blocks.add(new BlockPos(x, y, z));
                    }
                }
            }
            return blocks;
        }

    }
}
