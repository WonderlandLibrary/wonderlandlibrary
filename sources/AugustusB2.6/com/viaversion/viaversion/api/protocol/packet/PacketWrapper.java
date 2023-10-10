// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.protocol.packet;

import java.util.List;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import io.netty.channel.ChannelFuture;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.Via;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.connection.UserConnection;

public interface PacketWrapper
{
    public static final int PASSTHROUGH_ID = 1000;
    
    default PacketWrapper create(final PacketType packetType, final UserConnection connection) {
        return create(packetType, null, connection);
    }
    
    default PacketWrapper create(final PacketType packetType, final ByteBuf inputBuffer, final UserConnection connection) {
        return Via.getManager().getProtocolManager().createPacketWrapper(packetType, inputBuffer, connection);
    }
    
    @Deprecated
    default PacketWrapper create(final int packetId, final ByteBuf inputBuffer, final UserConnection connection) {
        return Via.getManager().getProtocolManager().createPacketWrapper(packetId, inputBuffer, connection);
    }
    
     <T> T get(final Type<T> p0, final int p1) throws Exception;
    
    boolean is(final Type p0, final int p1);
    
    boolean isReadable(final Type p0, final int p1);
    
     <T> void set(final Type<T> p0, final int p1, final T p2) throws Exception;
    
     <T> T read(final Type<T> p0) throws Exception;
    
     <T> void write(final Type<T> p0, final T p1);
    
     <T> T passthrough(final Type<T> p0) throws Exception;
    
    void passthroughAll() throws Exception;
    
    void writeToBuffer(final ByteBuf p0) throws Exception;
    
    void clearInputBuffer();
    
    void clearPacket();
    
    default void send(final Class<? extends Protocol> protocol) throws Exception {
        this.send(protocol, true);
    }
    
    void send(final Class<? extends Protocol> p0, final boolean p1) throws Exception;
    
    default void scheduleSend(final Class<? extends Protocol> protocol) throws Exception {
        this.scheduleSend(protocol, true);
    }
    
    void scheduleSend(final Class<? extends Protocol> p0, final boolean p1) throws Exception;
    
    ChannelFuture sendFuture(final Class<? extends Protocol> p0) throws Exception;
    
    @Deprecated
    default void send() throws Exception {
        this.sendRaw();
    }
    
    void sendRaw() throws Exception;
    
    void scheduleSendRaw() throws Exception;
    
    default PacketWrapper create(final PacketType packetType) {
        return this.create(packetType.getId());
    }
    
    default PacketWrapper create(final PacketType packetType, final PacketHandler handler) throws Exception {
        return this.create(packetType.getId(), handler);
    }
    
    PacketWrapper create(final int p0);
    
    PacketWrapper create(final int p0, final PacketHandler p1) throws Exception;
    
    PacketWrapper apply(final Direction p0, final State p1, final int p2, final List<Protocol> p3, final boolean p4) throws Exception;
    
    PacketWrapper apply(final Direction p0, final State p1, final int p2, final List<Protocol> p3) throws Exception;
    
    void cancel();
    
    boolean isCancelled();
    
    UserConnection user();
    
    void resetReader();
    
    @Deprecated
    default void sendToServer() throws Exception {
        this.sendToServerRaw();
    }
    
    void sendToServerRaw() throws Exception;
    
    void scheduleSendToServerRaw() throws Exception;
    
    default void sendToServer(final Class<? extends Protocol> protocol) throws Exception {
        this.sendToServer(protocol, true);
    }
    
    void sendToServer(final Class<? extends Protocol> p0, final boolean p1) throws Exception;
    
    default void scheduleSendToServer(final Class<? extends Protocol> protocol) throws Exception {
        this.scheduleSendToServer(protocol, true);
    }
    
    void scheduleSendToServer(final Class<? extends Protocol> p0, final boolean p1) throws Exception;
    
    PacketType getPacketType();
    
    void setPacketType(final PacketType p0);
    
    int getId();
    
    @Deprecated
    default void setId(final PacketType packetType) {
        this.setPacketType(packetType);
    }
    
    @Deprecated
    void setId(final int p0);
}
