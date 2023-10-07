/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.util.pathfinding;

import me.kiras.aimwhere.libraries.slick.util.pathfinding.Mover;

public interface PathFindingContext {
    public Mover getMover();

    public int getSourceX();

    public int getSourceY();

    public int getSearchDistance();
}

