/*
 * Decompiled with CFR 0.150.
 */
package baritone.selection;

import baritone.api.selection.ISelection;
import baritone.api.utils.BetterBlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3i;

public class Selection
implements ISelection {
    private final BetterBlockPos pos1;
    private final BetterBlockPos pos2;
    private final BetterBlockPos min;
    private final BetterBlockPos max;
    private final Vec3i size;
    private final AxisAlignedBB aabb;

    public Selection(BetterBlockPos pos1, BetterBlockPos pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.min = new BetterBlockPos(Math.min(pos1.x, pos2.x), Math.min(pos1.y, pos2.y), Math.min(pos1.z, pos2.z));
        this.max = new BetterBlockPos(Math.max(pos1.x, pos2.x), Math.max(pos1.y, pos2.y), Math.max(pos1.z, pos2.z));
        this.size = new Vec3i(this.max.x - this.min.x + 1, this.max.y - this.min.y + 1, this.max.z - this.min.z + 1);
        this.aabb = new AxisAlignedBB(this.min, this.max.add(1, 1, 1));
    }

    @Override
    public BetterBlockPos pos1() {
        return this.pos1;
    }

    @Override
    public BetterBlockPos pos2() {
        return this.pos2;
    }

    @Override
    public BetterBlockPos min() {
        return this.min;
    }

    @Override
    public BetterBlockPos max() {
        return this.max;
    }

    @Override
    public Vec3i size() {
        return this.size;
    }

    @Override
    public AxisAlignedBB aabb() {
        return this.aabb;
    }

    public int hashCode() {
        return this.pos1.hashCode() ^ this.pos2.hashCode();
    }

    public String toString() {
        return String.format("Selection{pos1=%s,pos2=%s}", this.pos1, this.pos2);
    }

    private boolean isPos2(EnumFacing facing) {
        boolean negative = facing.getAxisDirection().getOffset() < 0;
        switch (facing.getAxis()) {
            case X: {
                return this.pos2.x > this.pos1.x ^ negative;
            }
            case Y: {
                return this.pos2.y > this.pos1.y ^ negative;
            }
            case Z: {
                return this.pos2.z > this.pos1.z ^ negative;
            }
        }
        throw new IllegalStateException("Bad EnumFacing.Axis");
    }

    @Override
    public ISelection expand(EnumFacing direction, int blocks) {
        if (this.isPos2(direction)) {
            return new Selection(this.pos1, this.pos2.offset(direction, blocks));
        }
        return new Selection(this.pos1.offset(direction, blocks), this.pos2);
    }

    @Override
    public ISelection contract(EnumFacing direction, int blocks) {
        if (this.isPos2(direction)) {
            return new Selection(this.pos1.offset(direction, blocks), this.pos2);
        }
        return new Selection(this.pos1, this.pos2.offset(direction, blocks));
    }

    @Override
    public ISelection shift(EnumFacing direction, int blocks) {
        return new Selection(this.pos1.offset(direction, blocks), this.pos2.offset(direction, blocks));
    }
}

