// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.data;

import com.viaversion.viaversion.libs.gson.JsonArray;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;

public class FullMappingsBase implements FullMappings
{
    private final Object2IntMap<String> stringToId;
    private final Object2IntMap<String> mappedStringToId;
    private final String[] idToString;
    private final String[] mappedIdToString;
    private final Mappings mappings;
    
    public FullMappingsBase(final JsonArray oldMappings, final JsonArray newMappings, final Mappings mappings) {
        this.mappings = mappings;
        this.stringToId = MappingDataLoader.arrayToMap(oldMappings);
        this.mappedStringToId = MappingDataLoader.arrayToMap(newMappings);
        this.stringToId.defaultReturnValue(-1);
        this.mappedStringToId.defaultReturnValue(-1);
        this.idToString = new String[oldMappings.size()];
        for (int i = 0; i < oldMappings.size(); ++i) {
            this.idToString[i] = oldMappings.get(i).getAsString();
        }
        this.mappedIdToString = new String[newMappings.size()];
        for (int i = 0; i < newMappings.size(); ++i) {
            this.mappedIdToString[i] = newMappings.get(i).getAsString();
        }
    }
    
    @Override
    public Mappings mappings() {
        return this.mappings;
    }
    
    @Override
    public int id(final String identifier) {
        return this.stringToId.getInt(identifier);
    }
    
    @Override
    public int mappedId(final String mappedIdentifier) {
        return this.mappedStringToId.getInt(mappedIdentifier);
    }
    
    @Override
    public String identifier(final int id) {
        return this.idToString[id];
    }
    
    @Override
    public String mappedIdentifier(final int mappedId) {
        return this.mappedIdToString[mappedId];
    }
    
    @Override
    public String mappedIdentifier(final String identifier) {
        final int id = this.id(identifier);
        if (id == -1) {
            return null;
        }
        final int mappedId = this.mappings.getNewId(id);
        return (mappedId != -1) ? this.mappedIdentifier(mappedId) : null;
    }
}
