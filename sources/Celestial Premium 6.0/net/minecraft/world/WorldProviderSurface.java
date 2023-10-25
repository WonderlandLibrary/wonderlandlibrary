/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;

public class WorldProviderSurface
extends WorldProvider {
    @Override
    public DimensionType getDimensionType() {
        return DimensionType.OVERWORLD;
    }

    @Override
    public boolean canDropChunk(int x, int z) {
        return !this.worldObj.isSpawnChunk(x, z);
    }
}

