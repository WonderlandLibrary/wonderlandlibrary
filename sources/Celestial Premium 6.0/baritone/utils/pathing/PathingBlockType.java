/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils.pathing;

public enum PathingBlockType {
    AIR(0),
    WATER(1),
    AVOID(2),
    SOLID(3);

    private final boolean[] bits;

    private PathingBlockType(int bits) {
        this.bits = new boolean[]{(bits & 2) != 0, (bits & 1) != 0};
    }

    public final boolean[] getBits() {
        return this.bits;
    }

    public static PathingBlockType fromBits(boolean b1, boolean b2) {
        return b1 ? (b2 ? SOLID : AVOID) : (b2 ? WATER : AIR);
    }
}

