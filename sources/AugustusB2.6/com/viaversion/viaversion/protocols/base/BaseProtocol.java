// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.base;

import com.viaversion.viaversion.api.protocol.packet.Direction;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.Iterator;
import com.viaversion.viaversion.api.protocol.ProtocolPipeline;
import java.util.List;
import com.viaversion.viaversion.api.connection.ProtocolInfo;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import java.util.Collection;
import com.viaversion.viaversion.api.protocol.ProtocolPathEntry;
import com.viaversion.viaversion.api.protocol.Protocol;
import java.util.ArrayList;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public class BaseProtocol extends AbstractProtocol
{
    @Override
    protected void registerPackets() {
        this.registerServerbound(ServerboundHandshakePackets.CLIENT_INTENTION, new PacketRemapper() {
            @Override
            public void registerMap() {
                final int protocolVersion;
                final int state;
                final ProtocolInfo info;
                final VersionProvider versionProvider;
                int serverProtocol;
                List<ProtocolPathEntry> protocolPath;
                ProtocolPipeline pipeline;
                ArrayList protocols;
                final Iterator<ProtocolPathEntry> iterator;
                ProtocolPathEntry entry;
                ProtocolVersion protocol;
                this.handler(wrapper -> {
                    protocolVersion = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    wrapper.passthrough(Type.STRING);
                    wrapper.passthrough((Type<Object>)Type.UNSIGNED_SHORT);
                    state = wrapper.passthrough((Type<Integer>)Type.VAR_INT);
                    info = wrapper.user().getProtocolInfo();
                    info.setProtocolVersion(protocolVersion);
                    versionProvider = Via.getManager().getProviders().get(VersionProvider.class);
                    if (versionProvider == null) {
                        wrapper.user().setActive(false);
                    }
                    else {
                        serverProtocol = versionProvider.getClosestServerProtocol(wrapper.user());
                        info.setServerProtocolVersion(serverProtocol);
                        protocolPath = null;
                        if (info.getProtocolVersion() >= serverProtocol || Via.getPlatform().isOldClientsAllowed()) {
                            protocolPath = Via.getManager().getProtocolManager().getProtocolPath(info.getProtocolVersion(), serverProtocol);
                        }
                        pipeline = wrapper.user().getProtocolInfo().getPipeline();
                        if (protocolPath != null) {
                            protocols = new ArrayList<Protocol>(protocolPath.size());
                            protocolPath.iterator();
                            while (iterator.hasNext()) {
                                entry = iterator.next();
                                protocols.add(entry.protocol());
                                Via.getManager().getProtocolManager().completeMappingDataLoading(entry.protocol().getClass());
                            }
                            pipeline.add((Collection<Protocol>)protocols);
                            protocol = ProtocolVersion.getProtocol(serverProtocol);
                            wrapper.set(Type.VAR_INT, 0, protocol.getOriginalVersion());
                        }
                        pipeline.add(Via.getManager().getProtocolManager().getBaseProtocol(serverProtocol));
                        if (state == 1) {
                            info.setState(State.STATUS);
                        }
                        else if (state == 2) {
                            info.setState(State.LOGIN);
                        }
                    }
                });
            }
        });
    }
    
    @Override
    public boolean isBaseProtocol() {
        return true;
    }
    
    @Override
    public void register(final ViaProviders providers) {
        providers.register((Class<BaseVersionProvider>)VersionProvider.class, new BaseVersionProvider());
    }
    
    @Override
    public void transform(final Direction direction, final State state, final PacketWrapper packetWrapper) throws Exception {
        super.transform(direction, state, packetWrapper);
        if (direction == Direction.SERVERBOUND && state == State.HANDSHAKE && packetWrapper.getId() != 0) {
            packetWrapper.user().setActive(false);
        }
    }
}
