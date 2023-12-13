// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.debug;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.Direction;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import java.util.HashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import java.util.Set;
import com.viaversion.viaversion.api.debug.DebugHandler;

public final class DebugHandlerImpl implements DebugHandler
{
    private final Set<String> packetTypesToLog;
    private final IntSet clientboundPacketIdsToLog;
    private final IntSet serverboundPacketIdsToLog;
    private boolean logPostPacketTransform;
    private boolean enabled;
    
    public DebugHandlerImpl() {
        this.packetTypesToLog = new HashSet<String>();
        this.clientboundPacketIdsToLog = new IntOpenHashSet();
        this.serverboundPacketIdsToLog = new IntOpenHashSet();
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
    public void addPacketTypeToLog(final PacketType packetType) {
        ((packetType.direction() == Direction.SERVERBOUND) ? this.serverboundPacketIdsToLog : this.clientboundPacketIdsToLog).add(packetType.getId());
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
    public boolean shouldLog(final PacketWrapper wrapper, final Direction direction) {
        return (this.packetTypesToLog.isEmpty() && this.serverboundPacketIdsToLog.isEmpty() && this.clientboundPacketIdsToLog.isEmpty()) || (wrapper.getPacketType() != null && this.packetTypesToLog.contains(wrapper.getPacketType().getName())) || ((direction == Direction.SERVERBOUND) ? this.serverboundPacketIdsToLog : this.clientboundPacketIdsToLog).contains(wrapper.getId());
    }
}
