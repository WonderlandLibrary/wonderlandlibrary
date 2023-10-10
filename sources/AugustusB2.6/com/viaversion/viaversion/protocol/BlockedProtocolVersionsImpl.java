// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocol;

import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import com.viaversion.viaversion.api.protocol.version.BlockedProtocolVersions;

public class BlockedProtocolVersionsImpl implements BlockedProtocolVersions
{
    private final IntSet singleBlockedVersions;
    private final int blocksBelow;
    private final int blocksAbove;
    
    public BlockedProtocolVersionsImpl(final IntSet singleBlockedVersions, final int blocksBelow, final int blocksAbove) {
        this.singleBlockedVersions = singleBlockedVersions;
        this.blocksBelow = blocksBelow;
        this.blocksAbove = blocksAbove;
    }
    
    @Override
    public boolean contains(final int protocolVersion) {
        return (this.blocksBelow != -1 && protocolVersion < this.blocksBelow) || (this.blocksAbove != -1 && protocolVersion > this.blocksAbove) || this.singleBlockedVersions.contains(protocolVersion);
    }
    
    @Override
    public int blocksBelow() {
        return this.blocksBelow;
    }
    
    @Override
    public int blocksAbove() {
        return this.blocksAbove;
    }
    
    @Override
    public IntSet singleBlockedVersions() {
        return this.singleBlockedVersions;
    }
}
