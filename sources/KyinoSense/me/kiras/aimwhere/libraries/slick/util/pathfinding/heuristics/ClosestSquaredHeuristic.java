/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.util.pathfinding.heuristics;

import me.kiras.aimwhere.libraries.slick.util.pathfinding.AStarHeuristic;
import me.kiras.aimwhere.libraries.slick.util.pathfinding.Mover;
import me.kiras.aimwhere.libraries.slick.util.pathfinding.TileBasedMap;

public class ClosestSquaredHeuristic
implements AStarHeuristic {
    @Override
    public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty) {
        float dx = tx - x;
        float dy = ty - y;
        return dx * dx + dy * dy;
    }
}

