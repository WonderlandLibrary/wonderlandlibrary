/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.process;

import baritone.api.pathing.goals.Goal;
import baritone.api.process.IBaritoneProcess;

public interface ICustomGoalProcess
extends IBaritoneProcess {
    public void setGoal(Goal var1);

    public void path();

    public Goal getGoal();

    default public void setGoalAndPath(Goal goal) {
        this.setGoal(goal);
        this.path();
    }
}

