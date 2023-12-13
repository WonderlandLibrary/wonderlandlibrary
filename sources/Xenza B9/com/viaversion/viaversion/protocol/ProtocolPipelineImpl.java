// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocol;

import com.viaversion.viaversion.api.platform.ViaPlatform;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import java.util.logging.Level;
import com.viaversion.viaversion.api.debug.DebugHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.packet.Direction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import com.viaversion.viaversion.api.Via;
import java.util.Map;
import com.google.common.collect.Sets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Set;
import com.viaversion.viaversion.api.protocol.Protocol;
import java.util.List;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.ProtocolPipeline;
import com.viaversion.viaversion.api.protocol.AbstractSimpleProtocol;

public class ProtocolPipelineImpl extends AbstractSimpleProtocol implements ProtocolPipeline
{
    private final UserConnection userConnection;
    private List<Protocol> protocolList;
    private Set<Class<? extends Protocol>> protocolSet;
    
    public ProtocolPipelineImpl(final UserConnection userConnection) {
        this.userConnection = userConnection;
        userConnection.getProtocolInfo().setPipeline(this);
        this.registerPackets();
    }
    
    @Override
    protected void registerPackets() {
        this.protocolList = new CopyOnWriteArrayList<Protocol>();
        this.protocolSet = Sets.newSetFromMap(new ConcurrentHashMap<Class<? extends Protocol>, Boolean>());
        final Protocol baseProtocol = Via.getManager().getProtocolManager().getBaseProtocol();
        this.protocolList.add(baseProtocol);
        this.protocolSet.add(baseProtocol.getClass());
    }
    
    @Override
    public void init(final UserConnection userConnection) {
        throw new UnsupportedOperationException("ProtocolPipeline can only be initialized once");
    }
    
    @Override
    public void add(final Protocol protocol) {
        this.protocolList.add(protocol);
        this.protocolSet.add(protocol.getClass());
        protocol.init(this.userConnection);
        if (!protocol.isBaseProtocol()) {
            this.moveBaseProtocolsToTail();
        }
    }
    
    @Override
    public void add(final Collection<Protocol> protocols) {
        this.protocolList.addAll(protocols);
        for (final Protocol protocol : protocols) {
            protocol.init(this.userConnection);
            this.protocolSet.add(protocol.getClass());
        }
        this.moveBaseProtocolsToTail();
    }
    
    private void moveBaseProtocolsToTail() {
        List<Protocol> baseProtocols = null;
        for (final Protocol protocol : this.protocolList) {
            if (protocol.isBaseProtocol()) {
                if (baseProtocols == null) {
                    baseProtocols = new ArrayList<Protocol>();
                }
                baseProtocols.add(protocol);
            }
        }
        if (baseProtocols != null) {
            this.protocolList.removeAll(baseProtocols);
            this.protocolList.addAll(baseProtocols);
        }
    }
    
    @Override
    public void transform(final Direction direction, final State state, final PacketWrapper packetWrapper) throws Exception {
        final int originalID = packetWrapper.getId();
        final DebugHandler debugHandler = Via.getManager().debugHandler();
        if (debugHandler.enabled() && !debugHandler.logPostPacketTransform() && debugHandler.shouldLog(packetWrapper, direction)) {
            this.logPacket(direction, state, packetWrapper, originalID);
        }
        packetWrapper.apply(direction, state, 0, this.protocolList, direction == Direction.CLIENTBOUND);
        super.transform(direction, state, packetWrapper);
        if (debugHandler.enabled() && debugHandler.logPostPacketTransform() && debugHandler.shouldLog(packetWrapper, direction)) {
            this.logPacket(direction, state, packetWrapper, originalID);
        }
    }
    
    private void logPacket(final Direction direction, final State state, final PacketWrapper packetWrapper, final int originalID) {
        final int clientProtocol = this.userConnection.getProtocolInfo().getProtocolVersion();
        final ViaPlatform<?> platform = Via.getPlatform();
        final String actualUsername = packetWrapper.user().getProtocolInfo().getUsername();
        final String username = (actualUsername != null) ? (actualUsername + " ") : "";
        platform.getLogger().log(Level.INFO, "{0}{1} {2}: {3} ({4}) -> {5} ({6}) [{7}] {8}", new Object[] { username, direction, state, originalID, AbstractProtocol.toNiceHex(originalID), packetWrapper.getId(), AbstractProtocol.toNiceHex(packetWrapper.getId()), Integer.toString(clientProtocol), packetWrapper });
    }
    
    @Override
    public boolean contains(final Class<? extends Protocol> protocolClass) {
        return this.protocolSet.contains(protocolClass);
    }
    
    @Override
    public <P extends Protocol> P getProtocol(final Class<P> pipeClass) {
        for (final Protocol protocol : this.protocolList) {
            if (protocol.getClass() == pipeClass) {
                return (P)protocol;
            }
        }
        return null;
    }
    
    @Override
    public List<Protocol> pipes() {
        return this.protocolList;
    }
    
    @Override
    public boolean hasNonBaseProtocols() {
        for (final Protocol protocol : this.protocolList) {
            if (!protocol.isBaseProtocol()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void cleanPipes() {
        this.registerPackets();
    }
}
