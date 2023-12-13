/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.util.pathfinding.heuristics;

import org.newdawn.slick.util.pathfinding.AStarHeuristic;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

public class ManhattanHeuristic
implements AStarHeuristic {
    private int minimumCost;

    public ManhattanHeuristic(int minimumCost) {
        this.minimumCost = minimumCost;
    }

    public float getCost(TileBasedMap map, Mover mover, int x2, int y2, int tx, int ty) {
        return this.minimumCost * (Math.abs(x2 - tx) + Math.abs(y2 - ty));
    }
}

