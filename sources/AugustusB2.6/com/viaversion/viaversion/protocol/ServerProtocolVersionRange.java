// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocol;

import com.viaversion.viaversion.libs.fastutil.ints.IntSortedSet;
import com.viaversion.viaversion.api.protocol.version.ServerProtocolVersion;

public class ServerProtocolVersionRange implements ServerProtocolVersion
{
    private final int lowestSupportedVersion;
    private final int highestSupportedVersion;
    private final IntSortedSet supportedVersions;
    
    public ServerProtocolVersionRange(final int lowestSupportedVersion, final int highestSupportedVersion, final IntSortedSet supportedVersions) {
        this.lowestSupportedVersion = lowestSupportedVersion;
        this.highestSupportedVersion = highestSupportedVersion;
        this.supportedVersions = supportedVersions;
    }
    
    @Override
    public int lowestSupportedVersion() {
        return this.lowestSupportedVersion;
    }
    
    @Override
    public int highestSupportedVersion() {
        return this.highestSupportedVersion;
    }
    
    @Override
    public IntSortedSet supportedVersions() {
        return this.supportedVersions;
    }
}
