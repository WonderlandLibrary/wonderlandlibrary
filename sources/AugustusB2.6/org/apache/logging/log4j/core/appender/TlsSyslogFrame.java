// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import java.util.Objects;
import java.nio.charset.StandardCharsets;

public class TlsSyslogFrame
{
    private final String message;
    private final int byteLength;
    
    public TlsSyslogFrame(final String message) {
        this.message = message;
        final byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        this.byteLength = messageBytes.length;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    @Override
    public String toString() {
        return Integer.toString(this.byteLength) + ' ' + this.message;
    }
    
    @Override
    public int hashCode() {
        return 31 + Objects.hashCode(this.message);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TlsSyslogFrame)) {
            return false;
        }
        final TlsSyslogFrame other = (TlsSyslogFrame)obj;
        return Objects.equals(this.message, other.message);
    }
}
