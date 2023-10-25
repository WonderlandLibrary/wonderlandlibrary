/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.utils;

import net.minecraft.block.BlockFire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class VecUtils {
    private VecUtils() {
    }

    public static Vec3d calculateBlockCenter(World world, BlockPos pos) {
        IBlockState b = world.getBlockState(pos);
        AxisAlignedBB bbox = b.getBoundingBox(world, pos);
        double xDiff = (bbox.minX + bbox.maxX) / 2.0;
        double yDiff = (bbox.minY + bbox.maxY) / 2.0;
        double zDiff = (bbox.minZ + bbox.maxZ) / 2.0;
        if (b.getBlock() instanceof BlockFire) {
            yDiff = 0.0;
        }
        return new Vec3d((double)pos.getX() + xDiff, (double)pos.getY() + yDiff, (double)pos.getZ() + zDiff);
    }

    public static Vec3d getBlockPosCenter(BlockPos pos) {
        return new Vec3d((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5);
    }

    public static double distanceToCenter(BlockPos pos, double x, double y, double z) {
        double xdiff = (double)pos.getX() + 0.5 - x;
        double ydiff = (double)pos.getY() + 0.5 - y;
        double zdiff = (double)pos.getZ() + 0.5 - z;
        return Math.sqrt(xdiff * xdiff + ydiff * ydiff + zdiff * zdiff);
    }

    public static double entityDistanceToCenter(Entity entity, BlockPos pos) {
        return VecUtils.distanceToCenter(pos, entity.posX, entity.posY, entity.posZ);
    }

    public static double entityFlatDistanceToCenter(Entity entity, BlockPos pos) {
        return VecUtils.distanceToCenter(pos, entity.posX, (double)pos.getY() + 0.5, entity.posZ);
    }
}

