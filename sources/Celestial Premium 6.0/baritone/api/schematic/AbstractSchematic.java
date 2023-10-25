/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.schematic;

import baritone.api.schematic.ISchematic;

public abstract class AbstractSchematic
implements ISchematic {
    protected int x;
    protected int y;
    protected int z;

    public AbstractSchematic() {
        this(0, 0, 0);
    }

    public AbstractSchematic(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int widthX() {
        return this.x;
    }

    @Override
    public int heightY() {
        return this.y;
    }

    @Override
    public int lengthZ() {
        return this.z;
    }
}

