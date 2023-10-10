// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.debug;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;

public interface DebugHandler
{
    boolean enabled();
    
    void setEnabled(final boolean p0);
    
    void addPacketTypeNameToLog(final String p0);
    
    boolean removePacketTypeNameToLog(final String p0);
    
    void clearPacketTypesToLog();
    
    boolean logPostPacketTransform();
    
    void setLogPostPacketTransform(final boolean p0);
    
    boolean shouldLog(final PacketWrapper p0);
}
