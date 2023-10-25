/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.pathing.goals;

import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.movement.ActionCosts;
import baritone.api.utils.SettingsUtil;

public class GoalYLevel
implements Goal,
ActionCosts {
    public final int level;

    public GoalYLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean isInGoal(int x, int y, int z) {
        return y == this.level;
    }

    @Override
    public double heuristic(int x, int y, int z) {
        return GoalYLevel.calculate(this.level, y);
    }

    public static double calculate(int goalY, int currentY) {
        if (currentY > goalY) {
            return FALL_N_BLOCKS_COST[2] / 2.0 * (double)(currentY - goalY);
        }
        if (currentY < goalY) {
            return (double)(goalY - currentY) * JUMP_ONE_BLOCK_COST;
        }
        return 0.0;
    }

    public String toString() {
        return String.format("GoalYLevel{y=%s}", SettingsUtil.maybeCensor(this.level));
    }
}

