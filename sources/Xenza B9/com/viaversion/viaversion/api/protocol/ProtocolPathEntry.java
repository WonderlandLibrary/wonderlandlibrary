// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.protocol;

public interface ProtocolPathEntry
{
    int outputProtocolVersion();
    
    Protocol<?, ?, ?, ?> protocol();
    
    @Deprecated
    default int getOutputProtocolVersion() {
        return this.outputProtocolVersion();
    }
    
    @Deprecated
    default Protocol getProtocol() {
        return this.protocol();
    }
}
