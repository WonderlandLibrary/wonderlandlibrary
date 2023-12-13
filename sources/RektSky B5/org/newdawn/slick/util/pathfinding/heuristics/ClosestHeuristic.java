/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.util.pathfinding.heuristics;

import org.newdawn.slick.util.pathfinding.AStarHeuristic;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

public class ClosestHeuristic
implements AStarHeuristic {
    public float getCost(TileBasedMap map, Mover mover, int x2, int y2, int tx, int ty) {
        float dx = tx - x2;
        float dy = ty - y2;
        float result = (float)Math.sqrt(dx * dx + dy * dy);
        return result;
    }
}

