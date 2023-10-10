// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.debug;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.HashSet;
import java.util.Set;
import com.viaversion.viaversion.api.debug.DebugHandler;

public final class DebugHandlerImpl implements DebugHandler
{
    private final Set<String> packetTypesToLog;
    private boolean logPostPacketTransform;
    private boolean enabled;
    
    public DebugHandlerImpl() {
        this.packetTypesToLog = new HashSet<String>();
        this.logPostPacketTransform = true;
    }
    
    @Override
    public boolean enabled() {
        return this.enabled;
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    @Override
    public void addPacketTypeNameToLog(final String packetTypeName) {
        this.packetTypesToLog.add(packetTypeName);
    }
    
    @Override
    public boolean removePacketTypeNameToLog(final String packetTypeName) {
        return this.packetTypesToLog.remove(packetTypeName);
    }
    
    @Override
    public void clearPacketTypesToLog() {
        this.packetTypesToLog.clear();
    }
    
    @Override
    public boolean logPostPacketTransform() {
        return this.logPostPacketTransform;
    }
    
    @Override
    public void setLogPostPacketTransform(final boolean logPostPacketTransform) {
        this.logPostPacketTransform = logPostPacketTransform;
    }
    
    @Override
    public boolean shouldLog(final PacketWrapper wrapper) {
        return this.packetTypesToLog.isEmpty() || (wrapper.getPacketType() != null && this.packetTypesToLog.contains(wrapper.getPacketType().getName()));
    }
}
