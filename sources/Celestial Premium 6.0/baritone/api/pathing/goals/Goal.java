/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.pathing.goals;

import net.minecraft.util.math.BlockPos;

public interface Goal {
    public boolean isInGoal(int var1, int var2, int var3);

    public double heuristic(int var1, int var2, int var3);

    default public boolean isInGoal(BlockPos pos) {
        return this.isInGoal(pos.getX(), pos.getY(), pos.getZ());
    }

    default public double heuristic(BlockPos pos) {
        return this.heuristic(pos.getX(), pos.getY(), pos.getZ());
    }

    default public double heuristic() {
        return 0.0;
    }
}

