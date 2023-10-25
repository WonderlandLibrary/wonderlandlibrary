/*
 * Decompiled with CFR 0.150.
 */
package baritone.pathing.path;

import baritone.api.pathing.calc.IPath;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.movement.IMovement;
import baritone.api.utils.BetterBlockPos;
import baritone.utils.pathing.PathBase;
import java.util.Collections;
import java.util.List;

public class CutoffPath
extends PathBase {
    private final List<BetterBlockPos> path;
    private final List<IMovement> movements;
    private final int numNodes;
    private final Goal goal;

    public CutoffPath(IPath prev, int firstPositionToInclude, int lastPositionToInclude) {
        this.path = prev.positions().subList(firstPositionToInclude, lastPositionToInclude + 1);
        this.movements = prev.movements().subList(firstPositionToInclude, lastPositionToInclude);
        this.numNodes = prev.getNumNodesConsidered();
        this.goal = prev.getGoal();
        this.sanityCheck();
    }

    public CutoffPath(IPath prev, int lastPositionToInclude) {
        this(prev, 0, lastPositionToInclude);
    }

    @Override
    public Goal getGoal() {
        return this.goal;
    }

    @Override
    public List<IMovement> movements() {
        return Collections.unmodifiableList(this.movements);
    }

    @Override
    public List<BetterBlockPos> positions() {
        return Collections.unmodifiableList(this.path);
    }

    @Override
    public int getNumNodesConsidered() {
        return this.numNodes;
    }
}

