// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.debug;

import com.viaversion.viaversion.api.protocol.packet.Direction;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.google.common.annotations.Beta;

@Beta
public interface DebugHandler
{
    boolean enabled();
    
    void setEnabled(final boolean p0);
    
    void addPacketTypeNameToLog(final String p0);
    
    void addPacketTypeToLog(final PacketType p0);
    
    boolean removePacketTypeNameToLog(final String p0);
    
    void clearPacketTypesToLog();
    
    boolean logPostPacketTransform();
    
    void setLogPostPacketTransform(final boolean p0);
    
    boolean shouldLog(final PacketWrapper p0, final Direction p1);
    
    default void enableAndLogIds(final PacketType... packetTypes) {
        this.setEnabled(true);
        for (final PacketType packetType : packetTypes) {
            this.addPacketTypeToLog(packetType);
        }
    }
}
