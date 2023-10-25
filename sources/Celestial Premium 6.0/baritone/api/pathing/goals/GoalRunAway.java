/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.doubles.DoubleIterator
 *  it.unimi.dsi.fastutil.doubles.DoubleOpenHashSet
 */
package baritone.api.pathing.goals;

import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalXZ;
import baritone.api.pathing.goals.GoalYLevel;
import baritone.api.utils.SettingsUtil;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.DoubleOpenHashSet;
import java.util.Arrays;
import net.minecraft.util.math.BlockPos;

public class GoalRunAway
implements Goal {
    private final BlockPos[] from;
    private final int distanceSq;
    private final Integer maintainY;

    public GoalRunAway(double distance, BlockPos ... from) {
        this(distance, (Integer)null, from);
    }

    public GoalRunAway(double distance, Integer maintainY, BlockPos ... from) {
        if (from.length == 0) {
            throw new IllegalArgumentException();
        }
        this.from = from;
        this.distanceSq = (int)(distance * distance);
        this.maintainY = maintainY;
    }

    @Override
    public boolean isInGoal(int x, int y, int z) {
        if (this.maintainY != null && this.maintainY != y) {
            return false;
        }
        for (BlockPos p : this.from) {
            int diffZ;
            int diffX = x - p.getX();
            int distSq = diffX * diffX + (diffZ = z - p.getZ()) * diffZ;
            if (distSq >= this.distanceSq) continue;
            return false;
        }
        return true;
    }

    @Override
    public double heuristic(int x, int y, int z) {
        double min = Double.MAX_VALUE;
        for (BlockPos p : this.from) {
            double h = GoalXZ.calculate(p.getX() - x, p.getZ() - z);
            if (!(h < min)) continue;
            min = h;
        }
        min = -min;
        if (this.maintainY != null) {
            min = min * 0.6 + GoalYLevel.calculate(this.maintainY, y) * 1.5;
        }
        return min;
    }

    @Override
    public double heuristic() {
        int distance = (int)Math.ceil(Math.sqrt(this.distanceSq));
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        for (BlockPos p : this.from) {
            minX = Math.min(minX, p.getX() - distance);
            minY = Math.min(minY, p.getY() - distance);
            minZ = Math.min(minZ, p.getZ() - distance);
            maxX = Math.max(minX, p.getX() + distance);
            maxY = Math.max(minY, p.getY() + distance);
            maxZ = Math.max(minZ, p.getZ() + distance);
        }
        DoubleOpenHashSet maybeAlwaysInside = new DoubleOpenHashSet();
        double minOutside = Double.POSITIVE_INFINITY;
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    double h = this.heuristic(x, y, z);
                    if (h < minOutside && this.isInGoal(x, y, z)) {
                        maybeAlwaysInside.add(h);
                        continue;
                    }
                    minOutside = Math.min(minOutside, h);
                }
            }
        }
        double maxInside = Double.NEGATIVE_INFINITY;
        DoubleIterator it = maybeAlwaysInside.iterator();
        while (it.hasNext()) {
            double inside = it.nextDouble();
            if (!(inside < minOutside)) continue;
            maxInside = Math.max(maxInside, inside);
        }
        return maxInside;
    }

    public String toString() {
        if (this.maintainY != null) {
            return String.format("GoalRunAwayFromMaintainY y=%s, %s", SettingsUtil.maybeCensor(this.maintainY), Arrays.asList(this.from));
        }
        return "GoalRunAwayFrom" + Arrays.asList(this.from);
    }
}

