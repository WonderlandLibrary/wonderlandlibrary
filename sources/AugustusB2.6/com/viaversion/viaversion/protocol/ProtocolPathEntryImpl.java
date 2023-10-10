// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocol;

import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.ProtocolPathEntry;

public class ProtocolPathEntryImpl implements ProtocolPathEntry
{
    private final int outputProtocolVersion;
    private final Protocol<?, ?, ?, ?> protocol;
    
    public ProtocolPathEntryImpl(final int outputProtocolVersion, final Protocol<?, ?, ?, ?> protocol) {
        this.outputProtocolVersion = outputProtocolVersion;
        this.protocol = protocol;
    }
    
    @Override
    public int outputProtocolVersion() {
        return this.outputProtocolVersion;
    }
    
    @Override
    public Protocol<?, ?, ?, ?> protocol() {
        return this.protocol;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ProtocolPathEntryImpl that = (ProtocolPathEntryImpl)o;
        return this.outputProtocolVersion == that.outputProtocolVersion && this.protocol.equals(that.protocol);
    }
    
    @Override
    public int hashCode() {
        int result = this.outputProtocolVersion;
        result = 31 * result + this.protocol.hashCode();
        return result;
    }
}
