/*
 * Decompiled with CFR 0.150.
 */
package baritone.pathing.calc.openset;

import baritone.pathing.calc.PathNode;

public interface IOpenSet {
    public void insert(PathNode var1);

    public boolean isEmpty();

    public PathNode removeLowest();

    public void update(PathNode var1);
}

