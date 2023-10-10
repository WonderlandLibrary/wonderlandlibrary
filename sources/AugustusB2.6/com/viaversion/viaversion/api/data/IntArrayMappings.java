// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.data;

import com.viaversion.viaversion.libs.gson.JsonArray;
import java.util.Arrays;
import com.viaversion.viaversion.libs.gson.JsonObject;

public class IntArrayMappings implements Mappings
{
    private final int[] oldToNew;
    private final int mappedIds;
    
    protected IntArrayMappings(final int[] oldToNew, final int mappedIds) {
        this.oldToNew = oldToNew;
        this.mappedIds = mappedIds;
    }
    
    public static IntArrayMappings of(final int[] oldToNew, final int mappedIds) {
        return new IntArrayMappings(oldToNew, mappedIds);
    }
    
    public static Builder<IntArrayMappings> builder() {
        return Mappings.builder(IntArrayMappings::new);
    }
    
    @Deprecated
    public IntArrayMappings(final int[] oldToNew) {
        this(oldToNew, -1);
    }
    
    @Deprecated
    public IntArrayMappings(final int size, final JsonObject oldMapping, final JsonObject newMapping, final JsonObject diffMapping) {
        Arrays.fill(this.oldToNew = new int[size], -1);
        this.mappedIds = newMapping.size();
        MappingDataLoader.mapIdentifiers(this.oldToNew, oldMapping, newMapping, diffMapping);
    }
    
    @Deprecated
    public IntArrayMappings(final JsonObject oldMapping, final JsonObject newMapping, final JsonObject diffMapping) {
        this(oldMapping.entrySet().size(), oldMapping, newMapping, diffMapping);
    }
    
    @Deprecated
    public IntArrayMappings(final int size, final JsonObject oldMapping, final JsonObject newMapping) {
        Arrays.fill(this.oldToNew = new int[size], -1);
        this.mappedIds = -1;
        MappingDataLoader.mapIdentifiers(this.oldToNew, oldMapping, newMapping);
    }
    
    @Deprecated
    public IntArrayMappings(final JsonObject oldMapping, final JsonObject newMapping) {
        this(oldMapping.entrySet().size(), oldMapping, newMapping);
    }
    
    @Deprecated
    public IntArrayMappings(final int size, final JsonArray oldMapping, final JsonArray newMapping, final JsonObject diffMapping, final boolean warnOnMissing) {
        Arrays.fill(this.oldToNew = new int[size], -1);
        this.mappedIds = -1;
        MappingDataLoader.mapIdentifiers(this.oldToNew, oldMapping, newMapping, diffMapping, warnOnMissing);
    }
    
    @Deprecated
    public IntArrayMappings(final int size, final JsonArray oldMapping, final JsonArray newMapping, final boolean warnOnMissing) {
        this(size, oldMapping, newMapping, null, warnOnMissing);
    }
    
    @Deprecated
    public IntArrayMappings(final JsonArray oldMapping, final JsonArray newMapping, final boolean warnOnMissing) {
        this(oldMapping.size(), oldMapping, newMapping, warnOnMissing);
    }
    
    @Deprecated
    public IntArrayMappings(final int size, final JsonArray oldMapping, final JsonArray newMapping) {
        this(size, oldMapping, newMapping, true);
    }
    
    @Deprecated
    public IntArrayMappings(final JsonArray oldMapping, final JsonArray newMapping, final JsonObject diffMapping) {
        this(oldMapping.size(), oldMapping, newMapping, diffMapping, true);
    }
    
    @Deprecated
    public IntArrayMappings(final JsonArray oldMapping, final JsonArray newMapping) {
        this(oldMapping.size(), oldMapping, newMapping, true);
    }
    
    @Override
    public int getNewId(final int id) {
        return (id >= 0 && id < this.oldToNew.length) ? this.oldToNew[id] : -1;
    }
    
    @Override
    public void setNewId(final int id, final int newId) {
        this.oldToNew[id] = newId;
    }
    
    @Override
    public int size() {
        return this.oldToNew.length;
    }
    
    @Override
    public int mappedSize() {
        return this.mappedIds;
    }
    
    public int[] getOldToNew() {
        return this.oldToNew;
    }
}
