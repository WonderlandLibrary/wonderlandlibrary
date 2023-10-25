/*
 * Decompiled with CFR 0.150.
 */
package baritone.pathing.path;

import baritone.api.pathing.calc.IPath;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.movement.IMovement;
import baritone.api.utils.BetterBlockPos;
import baritone.utils.pathing.PathBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class SplicedPath
extends PathBase {
    private final List<BetterBlockPos> path;
    private final List<IMovement> movements;
    private final int numNodes;
    private final Goal goal;

    private SplicedPath(List<BetterBlockPos> path, List<IMovement> movements, int numNodesConsidered, Goal goal) {
        this.path = path;
        this.movements = movements;
        this.numNodes = numNodesConsidered;
        this.goal = goal;
        this.sanityCheck();
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

    @Override
    public int length() {
        return this.path.size();
    }

    public static Optional<SplicedPath> trySplice(IPath first, IPath second, boolean allowOverlapCutoff) {
        if (second == null || first == null) {
            return Optional.empty();
        }
        if (!first.getDest().equals(second.getSrc())) {
            return Optional.empty();
        }
        HashSet<BetterBlockPos> secondPos = new HashSet<BetterBlockPos>(second.positions());
        int firstPositionInSecond = -1;
        for (int i = 0; i < first.length() - 1; ++i) {
            if (!secondPos.contains(first.positions().get(i))) continue;
            firstPositionInSecond = i;
            break;
        }
        if (firstPositionInSecond != -1) {
            if (!allowOverlapCutoff) {
                return Optional.empty();
            }
        } else {
            firstPositionInSecond = first.length() - 1;
        }
        int positionInSecond = second.positions().indexOf(first.positions().get(firstPositionInSecond));
        if (!allowOverlapCutoff && positionInSecond != 0) {
            throw new IllegalStateException();
        }
        ArrayList<BetterBlockPos> positions = new ArrayList<BetterBlockPos>();
        ArrayList<IMovement> movements = new ArrayList<IMovement>();
        positions.addAll(first.positions().subList(0, firstPositionInSecond + 1));
        movements.addAll(first.movements().subList(0, firstPositionInSecond));
        positions.addAll(second.positions().subList(positionInSecond + 1, second.length()));
        movements.addAll(second.movements().subList(positionInSecond, second.length() - 1));
        return Optional.of(new SplicedPath(positions, movements, first.getNumNodesConsidered() + second.getNumNodesConsidered(), first.getGoal()));
    }
}

