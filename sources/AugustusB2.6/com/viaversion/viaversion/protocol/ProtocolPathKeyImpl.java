// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocol;

import com.viaversion.viaversion.api.protocol.ProtocolPathKey;

public class ProtocolPathKeyImpl implements ProtocolPathKey
{
    private final int clientProtocolVersion;
    private final int serverProtocolVersion;
    
    public ProtocolPathKeyImpl(final int clientProtocolVersion, final int serverProtocolVersion) {
        this.clientProtocolVersion = clientProtocolVersion;
        this.serverProtocolVersion = serverProtocolVersion;
    }
    
    @Override
    public int clientProtocolVersion() {
        return this.clientProtocolVersion;
    }
    
    @Override
    public int serverProtocolVersion() {
        return this.serverProtocolVersion;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ProtocolPathKeyImpl that = (ProtocolPathKeyImpl)o;
        return this.clientProtocolVersion == that.clientProtocolVersion && this.serverProtocolVersion == that.serverProtocolVersion;
    }
    
    @Override
    public int hashCode() {
        int result = this.clientProtocolVersion;
        result = 31 * result + this.serverProtocolVersion;
        return result;
    }
}
