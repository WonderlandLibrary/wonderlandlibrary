/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils.pathing;

import net.minecraft.world.border.WorldBorder;

public class BetterWorldBorder {
    private final double minX;
    private final double maxX;
    private final double minZ;
    private final double maxZ;

    public BetterWorldBorder(WorldBorder border) {
        this.minX = border.minX();
        this.maxX = border.maxX();
        this.minZ = border.minZ();
        this.maxZ = border.maxZ();
    }

    public boolean entirelyContains(int x, int z) {
        return (double)(x + 1) > this.minX && (double)x < this.maxX && (double)(z + 1) > this.minZ && (double)z < this.maxZ;
    }

    public boolean canPlaceAt(int x, int z) {
        return (double)x > this.minX && (double)(x + 1) < this.maxX && (double)z > this.minZ && (double)(z + 1) < this.maxZ;
    }
}

