/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.api.minecraft;

import java.util.HashMap;
import java.util.Map;

public enum BlockFace {
    NORTH(0, 0, -1, EnumAxis.Z),
    SOUTH(0, 0, 1, EnumAxis.Z),
    EAST(1, 0, 0, EnumAxis.X),
    WEST(-1, 0, 0, EnumAxis.X),
    TOP(0, 1, 0, EnumAxis.Y),
    BOTTOM(0, -1, 0, EnumAxis.Y);

    private static final Map<BlockFace, BlockFace> opposites;
    private final byte modX;
    private final byte modY;
    private final byte modZ;
    private final EnumAxis axis;

    private BlockFace(byte modX, byte modY, byte modZ, EnumAxis axis) {
        this.modX = modX;
        this.modY = modY;
        this.modZ = modZ;
        this.axis = axis;
    }

    public BlockFace opposite() {
        return opposites.get((Object)this);
    }

    public byte getModX() {
        return this.modX;
    }

    public byte getModY() {
        return this.modY;
    }

    public byte getModZ() {
        return this.modZ;
    }

    public EnumAxis getAxis() {
        return this.axis;
    }

    static {
        opposites = new HashMap<BlockFace, BlockFace>();
        opposites.put(NORTH, SOUTH);
        opposites.put(SOUTH, NORTH);
        opposites.put(EAST, WEST);
        opposites.put(WEST, EAST);
        opposites.put(TOP, BOTTOM);
        opposites.put(BOTTOM, TOP);
    }

    public static enum EnumAxis {
        X,
        Y,
        Z;

    }
}

