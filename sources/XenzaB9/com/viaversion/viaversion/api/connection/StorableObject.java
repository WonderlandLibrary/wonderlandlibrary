// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.connection;

public interface StorableObject
{
    default boolean clearOnServerSwitch() {
        return true;
    }
}
