// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.platform;

import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;

public interface ProtocolDetectorService
{
    int serverProtocolVersion(final String p0);
    
    void probeAllServers();
    
    void setProtocolVersion(final String p0, final int p1);
    
    int uncacheProtocolVersion(final String p0);
    
    Object2IntMap<String> detectedProtocolVersions();
}
