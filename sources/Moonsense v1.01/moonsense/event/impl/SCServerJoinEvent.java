// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event.impl;

import moonsense.event.SCEvent;

public class SCServerJoinEvent extends SCEvent
{
    public final String ip;
    public final int port;
    
    public SCServerJoinEvent(final String ip, final int port) {
        this.ip = ip;
        this.port = port;
    }
    
    public String getStringifiedPort() {
        return new StringBuilder().append(this.port).toString();
    }
    
    public String getServerRepresentation() {
        return String.valueOf(this.ip) + ":" + this.port;
    }
}
