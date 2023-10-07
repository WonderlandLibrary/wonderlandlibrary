// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net;

public enum Protocol
{
    TCP, 
    SSL, 
    UDP;
    
    public boolean isEqual(final String name) {
        return this.name().equalsIgnoreCase(name);
    }
}
