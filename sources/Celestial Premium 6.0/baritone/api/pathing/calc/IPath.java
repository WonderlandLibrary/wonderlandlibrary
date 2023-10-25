/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.pathing.calc;

import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.movement.IMovement;
import baritone.api.utils.BetterBlockPos;
import java.util.HashSet;
import java.util.List;

public interface IPath {
    public List<IMovement> movements();

    public List<BetterBlockPos> positions();

    default public IPath postProcess() {
        throw new UnsupportedOperationException();
    }

    default public int length() {
        return this.positions().size();
    }

    public Goal getGoal();

    public int getNumNodesConsidered();

    default public BetterBlockPos getSrc() {
        return this.positions().get(0);
    }

    default public BetterBlockPos getDest() {
        List<BetterBlockPos> pos = this.positions();
        return pos.get(pos.size() - 1);
    }

    default public double ticksRemainingFrom(int pathPosition) {
        double sum = 0.0;
        List<IMovement> movements = this.movements();
        for (int i = pathPosition; i < movements.size(); ++i) {
            sum += movements.get(i).getCost();
        }
        return sum;
    }

    default public IPath cutoffAtLoadedChunks(Object bsi) {
        throw new UnsupportedOperationException();
    }

    default public IPath staticCutoff(Goal destination) {
        throw new UnsupportedOperationException();
    }

    default public void sanityCheck() {
        List<BetterBlockPos> path = this.positions();
        List<IMovement> movements = this.movements();
        if (!this.getSrc().equals(path.get(0))) {
            throw new IllegalStateException("Start node does not equal first path element");
        }
        if (!this.getDest().equals(path.get(path.size() - 1))) {
            throw new IllegalStateException("End node does not equal last path element");
        }
        if (path.size() != movements.size() + 1) {
            throw new IllegalStateException("Size of path array is unexpected");
        }
        HashSet<BetterBlockPos> seenSoFar = new HashSet<BetterBlockPos>();
        for (int i = 0; i < path.size() - 1; ++i) {
            BetterBlockPos src = path.get(i);
            BetterBlockPos dest = path.get(i + 1);
            IMovement movement = movements.get(i);
            if (!src.equals(movement.getSrc())) {
                throw new IllegalStateException("Path source is not equal to the movement source");
            }
            if (!dest.equals(movement.getDest())) {
                throw new IllegalStateException("Path destination is not equal to the movement destination");
            }
            if (seenSoFar.contains(src)) {
                throw new IllegalStateException("Path doubles back on itself, making a loop");
            }
            seenSoFar.add(src);
        }
    }
}

