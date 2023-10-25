/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.pathing.goals;

import baritone.api.pathing.goals.Goal;
import baritone.api.utils.SettingsUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class GoalStrictDirection
implements Goal {
    public final int x;
    public final int y;
    public final int z;
    public final int dx;
    public final int dz;

    public GoalStrictDirection(BlockPos origin, EnumFacing direction) {
        this.x = origin.getX();
        this.y = origin.getY();
        this.z = origin.getZ();
        this.dx = direction.getXOffset();
        this.dz = direction.getZOffset();
        if (this.dx == 0 && this.dz == 0) {
            throw new IllegalArgumentException(direction + "");
        }
    }

    @Override
    public boolean isInGoal(int x, int y, int z) {
        return false;
    }

    @Override
    public double heuristic(int x, int y, int z) {
        int distanceFromStartInDesiredDirection = (x - this.x) * this.dx + (z - this.z) * this.dz;
        int distanceFromStartInIncorrectDirection = Math.abs((x - this.x) * this.dz) + Math.abs((z - this.z) * this.dx);
        int verticalDistanceFromStart = Math.abs(y - this.y);
        double heuristic = -distanceFromStartInDesiredDirection * 100;
        heuristic += (double)(distanceFromStartInIncorrectDirection * 1000);
        return heuristic += (double)(verticalDistanceFromStart * 1000);
    }

    @Override
    public double heuristic() {
        return Double.NEGATIVE_INFINITY;
    }

    public String toString() {
        return String.format("GoalStrictDirection{x=%s, y=%s, z=%s, dx=%s, dz=%s}", SettingsUtil.maybeCensor(this.x), SettingsUtil.maybeCensor(this.y), SettingsUtil.maybeCensor(this.z), SettingsUtil.maybeCensor(this.dx), SettingsUtil.maybeCensor(this.dz));
    }
}

