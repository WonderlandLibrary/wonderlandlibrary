/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.pathing.goals;

import baritone.api.pathing.goals.Goal;
import java.util.Arrays;

public class GoalComposite
implements Goal {
    private final Goal[] goals;

    public GoalComposite(Goal ... goals) {
        this.goals = goals;
    }

    @Override
    public boolean isInGoal(int x, int y, int z) {
        for (Goal goal : this.goals) {
            if (!goal.isInGoal(x, y, z)) continue;
            return true;
        }
        return false;
    }

    @Override
    public double heuristic(int x, int y, int z) {
        double min = Double.MAX_VALUE;
        for (Goal g : this.goals) {
            min = Math.min(min, g.heuristic(x, y, z));
        }
        return min;
    }

    @Override
    public double heuristic() {
        double min = Double.MAX_VALUE;
        for (Goal g : this.goals) {
            min = Math.min(min, g.heuristic());
        }
        return min;
    }

    public String toString() {
        return "GoalComposite" + Arrays.toString(this.goals);
    }

    public Goal[] goals() {
        return this.goals;
    }
}

