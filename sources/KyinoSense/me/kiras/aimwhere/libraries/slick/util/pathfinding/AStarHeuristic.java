/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.util.pathfinding;

import me.kiras.aimwhere.libraries.slick.util.pathfinding.Mover;
import me.kiras.aimwhere.libraries.slick.util.pathfinding.TileBasedMap;

public interface AStarHeuristic {
    public float getCost(TileBasedMap var1, Mover var2, int var3, int var4, int var5, int var6);
}

