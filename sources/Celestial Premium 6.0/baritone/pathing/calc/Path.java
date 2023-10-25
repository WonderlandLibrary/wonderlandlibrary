/*
 * Decompiled with CFR 0.150.
 */
package baritone.pathing.calc;

import baritone.api.pathing.calc.IPath;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.movement.IMovement;
import baritone.api.utils.BetterBlockPos;
import baritone.api.utils.Helper;
import baritone.pathing.calc.PathNode;
import baritone.pathing.movement.CalculationContext;
import baritone.pathing.movement.Movement;
import baritone.pathing.movement.Moves;
import baritone.pathing.path.CutoffPath;
import baritone.utils.pathing.PathBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class Path
extends PathBase {
    private final BetterBlockPos start;
    private final BetterBlockPos end;
    private final List<BetterBlockPos> path;
    private final List<Movement> movements;
    private final List<PathNode> nodes;
    private final Goal goal;
    private final int numNodes;
    private final CalculationContext context;
    private volatile boolean verified;

    Path(PathNode start, PathNode end, int numNodes, Goal goal, CalculationContext context) {
        this.start = new BetterBlockPos(start.x, start.y, start.z);
        this.end = new BetterBlockPos(end.x, end.y, end.z);
        this.numNodes = numNodes;
        this.movements = new ArrayList<Movement>();
        this.goal = goal;
        this.context = context;
        PathNode current = end;
        LinkedList<BetterBlockPos> tempPath = new LinkedList<BetterBlockPos>();
        LinkedList<PathNode> tempNodes = new LinkedList<PathNode>();
        while (current != null) {
            tempNodes.addFirst(current);
            tempPath.addFirst(new BetterBlockPos(current.x, current.y, current.z));
            current = current.previous;
        }
        this.path = new ArrayList<BetterBlockPos>(tempPath);
        this.nodes = new ArrayList<PathNode>(tempNodes);
    }

    @Override
    public Goal getGoal() {
        return this.goal;
    }

    private boolean assembleMovements() {
        if (this.path.isEmpty() || !this.movements.isEmpty()) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < this.path.size() - 1; ++i) {
            double cost = this.nodes.get((int)(i + 1)).cost - this.nodes.get((int)i).cost;
            Movement move = this.runBackwards(this.path.get(i), this.path.get(i + 1), cost);
            if (move == null) {
                return true;
            }
            this.movements.add(move);
        }
        return false;
    }

    private Movement runBackwards(BetterBlockPos src, BetterBlockPos dest, double cost) {
        for (Moves moves : Moves.values()) {
            Movement move = moves.apply0(this.context, src);
            if (!move.getDest().equals(dest)) continue;
            move.override(Math.min(move.calculateCost(this.context), cost));
            return move;
        }
        Helper.HELPER.logDebug("Movement became impossible during calculation " + src + " " + dest + " " + dest.subtract(src));
        return null;
    }

    @Override
    public IPath postProcess() {
        if (this.verified) {
            throw new IllegalStateException();
        }
        this.verified = true;
        boolean failed = this.assembleMovements();
        this.movements.forEach(m -> m.checkLoadedChunk(this.context));
        if (failed) {
            CutoffPath res = new CutoffPath(this, this.movements().size());
            if (res.movements().size() != this.movements.size()) {
                throw new IllegalStateException();
            }
            return res;
        }
        this.sanityCheck();
        return this;
    }

    @Override
    public List<IMovement> movements() {
        if (!this.verified) {
            throw new IllegalStateException();
        }
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
    public BetterBlockPos getSrc() {
        return this.start;
    }

    @Override
    public BetterBlockPos getDest() {
        return this.end;
    }
}

