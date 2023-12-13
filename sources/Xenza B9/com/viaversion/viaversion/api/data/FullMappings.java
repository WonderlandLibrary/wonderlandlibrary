// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.data;

public interface FullMappings
{
    Mappings mappings();
    
    int id(final String p0);
    
    int mappedId(final String p0);
    
    String identifier(final int p0);
    
    String mappedIdentifier(final int p0);
    
    String mappedIdentifier(final String p0);
}
