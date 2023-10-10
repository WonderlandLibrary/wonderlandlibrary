// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft;

import java.util.HashMap;
import java.util.Map;

public enum RegistryType
{
    BLOCK("block"), 
    ITEM("item"), 
    FLUID("fluid"), 
    ENTITY("entity_type"), 
    GAME_EVENT("game_event");
    
    private static final Map<String, RegistryType> MAP;
    private static final RegistryType[] VALUES;
    private final String resourceLocation;
    
    public static RegistryType[] getValues() {
        return RegistryType.VALUES;
    }
    
    public static RegistryType getByKey(final String resourceKey) {
        return RegistryType.MAP.get(resourceKey);
    }
    
    private RegistryType(final String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }
    
    @Deprecated
    public String getResourceLocation() {
        return this.resourceLocation;
    }
    
    public String resourceLocation() {
        return this.resourceLocation;
    }
    
    static {
        MAP = new HashMap<String, RegistryType>();
        VALUES = values();
        for (final RegistryType type : getValues()) {
            RegistryType.MAP.put(type.resourceLocation, type);
        }
    }
}
