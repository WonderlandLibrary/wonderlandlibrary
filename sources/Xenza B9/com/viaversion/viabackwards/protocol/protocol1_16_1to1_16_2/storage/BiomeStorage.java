// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_16_1to1_16_2.storage;

import com.viaversion.viabackwards.protocol.protocol1_16_1to1_16_2.data.BiomeMappings;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;
import com.viaversion.viaversion.api.connection.StorableObject;

public final class BiomeStorage implements StorableObject
{
    private final Int2IntMap modernToLegacyBiomes;
    
    public BiomeStorage() {
        (this.modernToLegacyBiomes = new Int2IntOpenHashMap()).defaultReturnValue(-1);
    }
    
    public void addBiome(final String biome, final int id) {
        this.modernToLegacyBiomes.put(id, BiomeMappings.toLegacyBiome(biome));
    }
    
    public int legacyBiome(final int biome) {
        return this.modernToLegacyBiomes.get(biome);
    }
    
    public void clear() {
        this.modernToLegacyBiomes.clear();
    }
}
