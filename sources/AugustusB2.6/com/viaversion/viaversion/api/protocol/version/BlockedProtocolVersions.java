// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.protocol.version;

import com.viaversion.viaversion.libs.fastutil.ints.IntSet;

public interface BlockedProtocolVersions
{
    boolean contains(final int p0);
    
    int blocksBelow();
    
    int blocksAbove();
    
    IntSet singleBlockedVersions();
}
