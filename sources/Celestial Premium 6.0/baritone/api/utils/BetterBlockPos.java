/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.utils;

import baritone.api.utils.SettingsUtil;
import javax.annotation.Nonnull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

public final class BetterBlockPos
extends BlockPos {
    public static final BetterBlockPos ORIGIN = new BetterBlockPos(0, 0, 0);
    public final int x;
    public final int y;
    public final int z;

    public BetterBlockPos(int x, int y, int z) {
        super(x, y, z);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BetterBlockPos(double x, double y, double z) {
        this(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
    }

    public BetterBlockPos(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public static BetterBlockPos from(BlockPos pos) {
        if (pos == null) {
            return null;
        }
        return new BetterBlockPos(pos);
    }

    @Override
    public int hashCode() {
        return (int)BetterBlockPos.longHash(this.x, this.y, this.z);
    }

    public static long longHash(BetterBlockPos pos) {
        return BetterBlockPos.longHash(pos.x, pos.y, pos.z);
    }

    public static long longHash(int x, int y, int z) {
        long hash = 3241L;
        hash = 3457689L * hash + (long)x;
        hash = 8734625L * hash + (long)y;
        hash = 2873465L * hash + (long)z;
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof BetterBlockPos) {
            BetterBlockPos oth = (BetterBlockPos)o;
            return oth.x == this.x && oth.y == this.y && oth.z == this.z;
        }
        BlockPos oth = (BlockPos)o;
        return oth.getX() == this.x && oth.getY() == this.y && oth.getZ() == this.z;
    }

    @Override
    public BetterBlockPos up() {
        return new BetterBlockPos(this.x, this.y + 1, this.z);
    }

    @Override
    public BetterBlockPos up(int amt) {
        return amt == 0 ? this : new BetterBlockPos(this.x, this.y + amt, this.z);
    }

    @Override
    public BetterBlockPos down() {
        return new BetterBlockPos(this.x, this.y - 1, this.z);
    }

    @Override
    public BetterBlockPos down(int amt) {
        return amt == 0 ? this : new BetterBlockPos(this.x, this.y - amt, this.z);
    }

    @Override
    public BetterBlockPos offset(EnumFacing dir) {
        Vec3i vec = dir.getDirectionVec();
        return new BetterBlockPos(this.x + vec.getX(), this.y + vec.getY(), this.z + vec.getZ());
    }

    @Override
    public BetterBlockPos offset(EnumFacing dir, int dist) {
        if (dist == 0) {
            return this;
        }
        Vec3i vec = dir.getDirectionVec();
        return new BetterBlockPos(this.x + vec.getX() * dist, this.y + vec.getY() * dist, this.z + vec.getZ() * dist);
    }

    @Override
    public BetterBlockPos north() {
        return new BetterBlockPos(this.x, this.y, this.z - 1);
    }

    @Override
    public BetterBlockPos north(int amt) {
        return amt == 0 ? this : new BetterBlockPos(this.x, this.y, this.z - amt);
    }

    @Override
    public BetterBlockPos south() {
        return new BetterBlockPos(this.x, this.y, this.z + 1);
    }

    @Override
    public BetterBlockPos south(int amt) {
        return amt == 0 ? this : new BetterBlockPos(this.x, this.y, this.z + amt);
    }

    @Override
    public BetterBlockPos east() {
        return new BetterBlockPos(this.x + 1, this.y, this.z);
    }

    @Override
    public BetterBlockPos east(int amt) {
        return amt == 0 ? this : new BetterBlockPos(this.x + amt, this.y, this.z);
    }

    @Override
    public BetterBlockPos west() {
        return new BetterBlockPos(this.x - 1, this.y, this.z);
    }

    @Override
    public BetterBlockPos west(int amt) {
        return amt == 0 ? this : new BetterBlockPos(this.x - amt, this.y, this.z);
    }

    @Override
    @Nonnull
    public String toString() {
        return String.format("BetterBlockPos{x=%s,y=%s,z=%s}", SettingsUtil.maybeCensor(this.x), SettingsUtil.maybeCensor(this.y), SettingsUtil.maybeCensor(this.z));
    }
}

