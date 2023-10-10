// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocol.packet;

import java.util.Iterator;
import java.util.List;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.ProtocolPathEntry;
import com.viaversion.viaversion.api.protocol.Protocol;
import java.util.ArrayList;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.packet.Direction;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import java.util.function.Consumer;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.protocol.packet.VersionedPacketTransformer;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public class VersionedPacketTransformerImpl<C extends ClientboundPacketType, S extends ServerboundPacketType> implements VersionedPacketTransformer<C, S>
{
    private final int inputProtocolVersion;
    private final Class<C> clientboundPacketsClass;
    private final Class<S> serverboundPacketsClass;
    
    public VersionedPacketTransformerImpl(final ProtocolVersion inputVersion, final Class<C> clientboundPacketsClass, final Class<S> serverboundPacketsClass) {
        Preconditions.checkNotNull(inputVersion);
        Preconditions.checkArgument(clientboundPacketsClass != null || serverboundPacketsClass != null, (Object)"Either the clientbound or serverbound packets class has to be non-null");
        this.inputProtocolVersion = inputVersion.getVersion();
        this.clientboundPacketsClass = clientboundPacketsClass;
        this.serverboundPacketsClass = serverboundPacketsClass;
    }
    
    @Override
    public boolean send(final PacketWrapper packet) throws Exception {
        this.validatePacket(packet);
        return this.transformAndSendPacket(packet, true);
    }
    
    @Override
    public boolean send(final UserConnection connection, final C packetType, final Consumer<PacketWrapper> packetWriter) throws Exception {
        return this.createAndSend(connection, packetType, packetWriter);
    }
    
    @Override
    public boolean send(final UserConnection connection, final S packetType, final Consumer<PacketWrapper> packetWriter) throws Exception {
        return this.createAndSend(connection, packetType, packetWriter);
    }
    
    @Override
    public boolean scheduleSend(final PacketWrapper packet) throws Exception {
        this.validatePacket(packet);
        return this.transformAndSendPacket(packet, false);
    }
    
    @Override
    public boolean scheduleSend(final UserConnection connection, final C packetType, final Consumer<PacketWrapper> packetWriter) throws Exception {
        return this.scheduleCreateAndSend(connection, packetType, packetWriter);
    }
    
    @Override
    public boolean scheduleSend(final UserConnection connection, final S packetType, final Consumer<PacketWrapper> packetWriter) throws Exception {
        return this.scheduleCreateAndSend(connection, packetType, packetWriter);
    }
    
    @Override
    public PacketWrapper transform(final PacketWrapper packet) throws Exception {
        this.validatePacket(packet);
        this.transformPacket(packet);
        return packet.isCancelled() ? null : packet;
    }
    
    @Override
    public PacketWrapper transform(final UserConnection connection, final C packetType, final Consumer<PacketWrapper> packetWriter) throws Exception {
        return this.createAndTransform(connection, packetType, packetWriter);
    }
    
    @Override
    public PacketWrapper transform(final UserConnection connection, final S packetType, final Consumer<PacketWrapper> packetWriter) throws Exception {
        return this.createAndTransform(connection, packetType, packetWriter);
    }
    
    private void validatePacket(final PacketWrapper packet) {
        if (packet.user() == null) {
            throw new IllegalArgumentException("PacketWrapper does not have a targetted UserConnection");
        }
        if (packet.getPacketType() == null) {
            throw new IllegalArgumentException("PacketWrapper does not have a valid packet type");
        }
        final Class<? extends PacketType> expectedPacketClass = (Class<? extends PacketType>)((packet.getPacketType().direction() == Direction.CLIENTBOUND) ? this.clientboundPacketsClass : this.serverboundPacketsClass);
        if (packet.getPacketType().getClass() != expectedPacketClass) {
            throw new IllegalArgumentException("PacketWrapper packet type is of the wrong packet class");
        }
    }
    
    private boolean transformAndSendPacket(final PacketWrapper packet, final boolean currentThread) throws Exception {
        this.transformPacket(packet);
        if (packet.isCancelled()) {
            return false;
        }
        if (currentThread) {
            if (packet.getPacketType().direction() == Direction.CLIENTBOUND) {
                packet.sendRaw();
            }
            else {
                packet.sendToServerRaw();
            }
        }
        else if (packet.getPacketType().direction() == Direction.CLIENTBOUND) {
            packet.scheduleSendRaw();
        }
        else {
            packet.scheduleSendToServerRaw();
        }
        return true;
    }
    
    private void transformPacket(final PacketWrapper packet) throws Exception {
        final PacketType packetType = packet.getPacketType();
        final UserConnection connection = packet.user();
        final boolean clientbound = packetType.direction() == Direction.CLIENTBOUND;
        final int serverProtocolVersion = clientbound ? this.inputProtocolVersion : connection.getProtocolInfo().getServerProtocolVersion();
        final int clientProtocolVersion = clientbound ? connection.getProtocolInfo().getProtocolVersion() : this.inputProtocolVersion;
        final List<ProtocolPathEntry> path = Via.getManager().getProtocolManager().getProtocolPath(clientProtocolVersion, serverProtocolVersion);
        List<Protocol> protocolList = null;
        if (path != null) {
            protocolList = new ArrayList<Protocol>(path.size());
            for (final ProtocolPathEntry entry : path) {
                protocolList.add(entry.protocol());
            }
        }
        else if (serverProtocolVersion != clientProtocolVersion) {
            throw new RuntimeException("No protocol path between client version " + clientProtocolVersion + " and server version " + serverProtocolVersion);
        }
        if (protocolList != null) {
            packet.resetReader();
            try {
                packet.apply(packetType.direction(), State.PLAY, 0, protocolList, clientbound);
            }
            catch (Exception e) {
                throw new Exception("Exception trying to transform packet between client version " + clientProtocolVersion + " and server version " + serverProtocolVersion + ". Are you sure you used the correct input version and packet write types?", e);
            }
        }
    }
    
    private boolean createAndSend(final UserConnection connection, final PacketType packetType, final Consumer<PacketWrapper> packetWriter) throws Exception {
        final PacketWrapper packet = PacketWrapper.create(packetType, connection);
        packetWriter.accept(packet);
        return this.send(packet);
    }
    
    private boolean scheduleCreateAndSend(final UserConnection connection, final PacketType packetType, final Consumer<PacketWrapper> packetWriter) throws Exception {
        final PacketWrapper packet = PacketWrapper.create(packetType, connection);
        packetWriter.accept(packet);
        return this.scheduleSend(packet);
    }
    
    private PacketWrapper createAndTransform(final UserConnection connection, final PacketType packetType, final Consumer<PacketWrapper> packetWriter) throws Exception {
        final PacketWrapper packet = PacketWrapper.create(packetType, connection);
        packetWriter.accept(packet);
        return this.transform(packet);
    }
}
