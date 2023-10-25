/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.pathing.calc;

import baritone.api.pathing.calc.IPath;
import baritone.api.pathing.goals.Goal;
import baritone.api.utils.PathCalculationResult;
import java.util.Optional;

public interface IPathFinder {
    public Goal getGoal();

    public PathCalculationResult calculate(long var1, long var3);

    public boolean isFinished();

    public Optional<IPath> pathToMostRecentNodeConsidered();

    public Optional<IPath> bestPathSoFar();
}

