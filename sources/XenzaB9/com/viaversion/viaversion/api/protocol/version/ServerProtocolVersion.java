// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.protocol.version;

import com.viaversion.viaversion.libs.fastutil.ints.IntSortedSet;

public interface ServerProtocolVersion
{
    int lowestSupportedVersion();
    
    int highestSupportedVersion();
    
    IntSortedSet supportedVersions();
    
    default boolean isKnown() {
        return this.lowestSupportedVersion() != -1 && this.highestSupportedVersion() != -1;
    }
}
