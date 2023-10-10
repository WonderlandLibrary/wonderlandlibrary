// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocol;

import com.viaversion.viaversion.libs.fastutil.ints.IntSortedSets;
import com.viaversion.viaversion.libs.fastutil.ints.IntSortedSet;
import com.viaversion.viaversion.api.protocol.version.ServerProtocolVersion;

public class ServerProtocolVersionSingleton implements ServerProtocolVersion
{
    private final int protocolVersion;
    
    public ServerProtocolVersionSingleton(final int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
    
    @Override
    public int lowestSupportedVersion() {
        return this.protocolVersion;
    }
    
    @Override
    public int highestSupportedVersion() {
        return this.protocolVersion;
    }
    
    @Override
    public IntSortedSet supportedVersions() {
        return IntSortedSets.singleton(this.protocolVersion);
    }
}
