/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.util.pathfinding.heuristics;

import me.kiras.aimwhere.libraries.slick.util.pathfinding.AStarHeuristic;
import me.kiras.aimwhere.libraries.slick.util.pathfinding.Mover;
import me.kiras.aimwhere.libraries.slick.util.pathfinding.TileBasedMap;

public class ManhattanHeuristic
implements AStarHeuristic {
    private int minimumCost;

    public ManhattanHeuristic(int minimumCost) {
        this.minimumCost = minimumCost;
    }

    @Override
    public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty) {
        return this.minimumCost * (Math.abs(x - tx) + Math.abs(y - ty));
    }
}

