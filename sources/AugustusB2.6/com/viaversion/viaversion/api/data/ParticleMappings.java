// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.data;

import com.viaversion.viaversion.libs.fastutil.ints.IntArrayList;
import com.viaversion.viaversion.libs.gson.JsonArray;
import com.viaversion.viaversion.libs.fastutil.ints.IntList;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;

public class ParticleMappings
{
    private final Object2IntMap<String> stringToId;
    private final Object2IntMap<String> mappedStringToId;
    private final Mappings mappings;
    private final IntList itemParticleIds;
    private final IntList blockParticleIds;
    
    public ParticleMappings(final JsonArray oldMappings, final JsonArray newMappings, final Mappings mappings) {
        this.itemParticleIds = new IntArrayList(2);
        this.blockParticleIds = new IntArrayList(4);
        this.mappings = mappings;
        this.stringToId = MappingDataLoader.arrayToMap(oldMappings);
        this.mappedStringToId = MappingDataLoader.arrayToMap(newMappings);
        this.stringToId.defaultReturnValue(-1);
        this.mappedStringToId.defaultReturnValue(-1);
        this.addBlockParticle("block");
        this.addBlockParticle("falling_dust");
        this.addBlockParticle("block_marker");
        this.addItemParticle("item");
    }
    
    public int id(final String identifier) {
        return this.stringToId.getInt(identifier);
    }
    
    public int mappedId(final String mappedIdentifier) {
        return this.mappedStringToId.getInt(mappedIdentifier);
    }
    
    public Mappings getMappings() {
        return this.mappings;
    }
    
    public boolean addItemParticle(final String identifier) {
        final int id = this.id(identifier);
        return id != -1 && this.itemParticleIds.add(id);
    }
    
    public boolean addBlockParticle(final String identifier) {
        final int id = this.id(identifier);
        return id != -1 && this.blockParticleIds.add(id);
    }
    
    public boolean isBlockParticle(final int id) {
        return this.blockParticleIds.contains(id);
    }
    
    public boolean isItemParticle(final int id) {
        return this.itemParticleIds.contains(id);
    }
    
    @Deprecated
    public int getBlockId() {
        return this.id("block");
    }
    
    @Deprecated
    public int getFallingDustId() {
        return this.id("falling_dust");
    }
    
    @Deprecated
    public int getItemId() {
        return this.id("item");
    }
}
