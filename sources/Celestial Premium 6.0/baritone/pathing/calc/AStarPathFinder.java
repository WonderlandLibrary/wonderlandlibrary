/*
 * Decompiled with CFR 0.150.
 */
package baritone.pathing.calc;

import baritone.Baritone;
import baritone.api.pathing.calc.IPath;
import baritone.api.pathing.goals.Goal;
import baritone.api.utils.BetterBlockPos;
import baritone.pathing.calc.AbstractNodeCostSearch;
import baritone.pathing.calc.Path;
import baritone.pathing.calc.PathNode;
import baritone.pathing.calc.openset.BinaryHeapOpenSet;
import baritone.pathing.movement.CalculationContext;
import baritone.pathing.movement.Moves;
import baritone.utils.pathing.BetterWorldBorder;
import baritone.utils.pathing.Favoring;
import baritone.utils.pathing.MutableMoveResult;
import java.util.Optional;

public final class AStarPathFinder
extends AbstractNodeCostSearch {
    private final Favoring favoring;
    private final CalculationContext calcContext;

    public AStarPathFinder(int startX, int startY, int startZ, Goal goal, Favoring favoring, CalculationContext context) {
        super(startX, startY, startZ, goal, context);
        this.favoring = favoring;
        this.calcContext = context;
    }

    @Override
    protected Optional<IPath> calculate0(long primaryTimeout, long failureTimeout) {
        long now2;
        this.startNode = this.getNodeAtPosition(this.startX, this.startY, this.startZ, BetterBlockPos.longHash(this.startX, this.startY, this.startZ));
        this.startNode.cost = 0.0;
        this.startNode.combinedCost = this.startNode.estimatedCostToGoal;
        BinaryHeapOpenSet openSet = new BinaryHeapOpenSet();
        openSet.insert(this.startNode);
        double[] bestHeuristicSoFar = new double[COEFFICIENTS.length];
        for (int i = 0; i < bestHeuristicSoFar.length; ++i) {
            bestHeuristicSoFar[i] = this.startNode.estimatedCostToGoal;
            this.bestSoFar[i] = this.startNode;
        }
        MutableMoveResult res = new MutableMoveResult();
        BetterWorldBorder worldBorder = new BetterWorldBorder(this.calcContext.world.getWorldBorder());
        long startTime = System.currentTimeMillis();
        boolean slowPath = (Boolean)Baritone.settings().slowPath.value;
        if (slowPath) {
            this.logDebug("slowPath is on, path timeout will be " + Baritone.settings().slowPathTimeoutMS.value + "ms instead of " + primaryTimeout + "ms");
        }
        long primaryTimeoutTime = startTime + (slowPath ? (Long)Baritone.settings().slowPathTimeoutMS.value : primaryTimeout);
        long failureTimeoutTime = startTime + (slowPath ? (Long)Baritone.settings().slowPathTimeoutMS.value : failureTimeout);
        boolean failing = true;
        int numNodes = 0;
        int numMovementsConsidered = 0;
        int numEmptyChunk = 0;
        boolean isFavoring = !this.favoring.isEmpty();
        int timeCheckInterval = 64;
        int pathingMaxChunkBorderFetch = (Integer)Baritone.settings().pathingMaxChunkBorderFetch.value;
        double minimumImprovement = (Boolean)Baritone.settings().minimumImprovementRepropagation.value != false ? 0.01 : 0.0;
        Moves[] allMoves = Moves.values();
        while (!(openSet.isEmpty() || numEmptyChunk >= pathingMaxChunkBorderFetch || this.cancelRequested || (numNodes & timeCheckInterval - 1) == 0 && ((now2 = System.currentTimeMillis()) - failureTimeoutTime >= 0L || !failing && now2 - primaryTimeoutTime >= 0L))) {
            PathNode currentNode;
            if (slowPath) {
                try {
                    Thread.sleep((Long)Baritone.settings().slowPathTimeDelayMS.value);
                }
                catch (InterruptedException now2) {
                    // empty catch block
                }
            }
            this.mostRecentConsidered = currentNode = openSet.removeLowest();
            ++numNodes;
            if (this.goal.isInGoal(currentNode.x, currentNode.y, currentNode.z)) {
                this.logDebug("Took " + (System.currentTimeMillis() - startTime) + "ms, " + numMovementsConsidered + " movements considered");
                return Optional.of(new Path(this.startNode, currentNode, numNodes, this.goal, this.calcContext));
            }
            for (Moves moves : allMoves) {
                int newX = currentNode.x + moves.xOffset;
                int newZ = currentNode.z + moves.zOffset;
                if (!(newX >> 4 == currentNode.x >> 4 && newZ >> 4 == currentNode.z >> 4 || this.calcContext.isLoaded(newX, newZ))) {
                    if (moves.dynamicXZ) continue;
                    ++numEmptyChunk;
                    continue;
                }
                if (!moves.dynamicXZ && !worldBorder.entirelyContains(newX, newZ) || currentNode.y + moves.yOffset > 256 || currentNode.y + moves.yOffset < 0) continue;
                res.reset();
                moves.apply(this.calcContext, currentNode.x, currentNode.y, currentNode.z, res);
                ++numMovementsConsidered;
                double actionCost = res.cost;
                if (actionCost >= 1000000.0) continue;
                if (actionCost <= 0.0 || Double.isNaN(actionCost)) {
                    throw new IllegalStateException((Object)((Object)moves) + " calculated implausible cost " + actionCost);
                }
                if (moves.dynamicXZ && !worldBorder.entirelyContains(res.x, res.z)) continue;
                if (!(moves.dynamicXZ || res.x == newX && res.z == newZ)) {
                    throw new IllegalStateException((Object)((Object)moves) + " " + res.x + " " + newX + " " + res.z + " " + newZ);
                }
                if (!moves.dynamicY && res.y != currentNode.y + moves.yOffset) {
                    throw new IllegalStateException((Object)((Object)moves) + " " + res.y + " " + (currentNode.y + moves.yOffset));
                }
                long hashCode = BetterBlockPos.longHash(res.x, res.y, res.z);
                if (isFavoring) {
                    actionCost *= this.favoring.calculate(hashCode);
                }
                PathNode neighbor = this.getNodeAtPosition(res.x, res.y, res.z, hashCode);
                double tentativeCost = currentNode.cost + actionCost;
                if (!(neighbor.cost - tentativeCost > minimumImprovement)) continue;
                neighbor.previous = currentNode;
                neighbor.cost = tentativeCost;
                neighbor.combinedCost = tentativeCost + neighbor.estimatedCostToGoal;
                if (neighbor.isOpen()) {
                    openSet.update(neighbor);
                } else {
                    openSet.insert(neighbor);
                }
                for (int i = 0; i < COEFFICIENTS.length; ++i) {
                    double heuristic = neighbor.estimatedCostToGoal + neighbor.cost / COEFFICIENTS[i];
                    if (!(bestHeuristicSoFar[i] - heuristic > minimumImprovement)) continue;
                    bestHeuristicSoFar[i] = heuristic;
                    this.bestSoFar[i] = neighbor;
                    if (!failing || !(this.getDistFromStartSq(neighbor) > 25.0)) continue;
                    failing = false;
                }
            }
        }
        if (this.cancelRequested) {
            return Optional.empty();
        }
        System.out.println(numMovementsConsidered + " movements considered");
        System.out.println("Open set size: " + openSet.size());
        System.out.println("PathNode map size: " + this.mapSize());
        System.out.println((int)((double)numNodes * 1.0 / (double)((float)(System.currentTimeMillis() - startTime) / 1000.0f)) + " nodes per second");
        Optional<IPath> result = this.bestSoFar(true, numNodes);
        if (result.isPresent()) {
            this.logDebug("Took " + (System.currentTimeMillis() - startTime) + "ms, " + numMovementsConsidered + " movements considered");
        }
        return result;
    }
}

