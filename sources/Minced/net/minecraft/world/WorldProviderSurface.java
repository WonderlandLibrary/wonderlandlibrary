// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

public class WorldProviderSurface extends WorldProvider
{
    @Override
    public DimensionType getDimensionType() {
        return DimensionType.OVERWORLD;
    }
    
    @Override
    public boolean canDropChunk(final int x, final int z) {
        return !this.world.isSpawnChunk(x, z);
    }
}
