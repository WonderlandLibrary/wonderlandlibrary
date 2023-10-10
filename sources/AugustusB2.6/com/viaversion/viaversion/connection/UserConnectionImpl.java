// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.connection;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.exception.CancelException;
import com.viaversion.viaversion.protocol.packet.PacketWrapperImpl;
import com.viaversion.viaversion.api.protocol.packet.Direction;
import java.util.function.Function;
import io.netty.channel.ChannelHandlerContext;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.util.PipelineUtil;
import com.viaversion.viaversion.util.ChatColorUtil;
import io.netty.channel.ChannelFuture;
import com.viaversion.viaversion.api.Via;
import io.netty.buffer.ByteBuf;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import io.netty.channel.Channel;
import com.viaversion.viaversion.api.connection.ProtocolInfo;
import java.util.UUID;
import java.util.Set;
import com.viaversion.viaversion.api.protocol.packet.PacketTracker;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.connection.StorableObject;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import com.viaversion.viaversion.api.connection.UserConnection;

public class UserConnectionImpl implements UserConnection
{
    private static final AtomicLong IDS;
    private final long id;
    private final Map<Class<?>, StorableObject> storedObjects;
    private final Map<Class<? extends Protocol>, EntityTracker> entityTrackers;
    private final PacketTracker packetTracker;
    private final Set<UUID> passthroughTokens;
    private final ProtocolInfo protocolInfo;
    private final Channel channel;
    private final boolean clientSide;
    private boolean active;
    private boolean pendingDisconnect;
    private boolean packetLimiterEnabled;
    
    public UserConnectionImpl(final Channel channel, final boolean clientSide) {
        this.id = UserConnectionImpl.IDS.incrementAndGet();
        this.storedObjects = new ConcurrentHashMap<Class<?>, StorableObject>();
        this.entityTrackers = new HashMap<Class<? extends Protocol>, EntityTracker>();
        this.packetTracker = new PacketTracker(this);
        this.passthroughTokens = Collections.newSetFromMap((Map<UUID, Boolean>)CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.SECONDS).build().asMap());
        this.protocolInfo = new ProtocolInfoImpl(this);
        this.active = true;
        this.packetLimiterEnabled = true;
        this.channel = channel;
        this.clientSide = clientSide;
    }
    
    public UserConnectionImpl(final Channel channel) {
        this(channel, false);
    }
    
    @Override
    public <T extends StorableObject> T get(final Class<T> objectClass) {
        return (T)this.storedObjects.get(objectClass);
    }
    
    @Override
    public boolean has(final Class<? extends StorableObject> objectClass) {
        return this.storedObjects.containsKey(objectClass);
    }
    
    @Override
    public void put(final StorableObject object) {
        this.storedObjects.put(object.getClass(), object);
    }
    
    @Override
    public Collection<EntityTracker> getEntityTrackers() {
        return this.entityTrackers.values();
    }
    
    @Override
    public <T extends EntityTracker> T getEntityTracker(final Class<? extends Protocol> protocolClass) {
        return (T)this.entityTrackers.get(protocolClass);
    }
    
    @Override
    public void addEntityTracker(final Class<? extends Protocol> protocolClass, final EntityTracker tracker) {
        this.entityTrackers.put(protocolClass, tracker);
    }
    
    @Override
    public void clearStoredObjects() {
        this.storedObjects.clear();
        this.entityTrackers.clear();
    }
    
    @Override
    public void sendRawPacket(final ByteBuf packet) {
        this.sendRawPacket(packet, true);
    }
    
    @Override
    public void scheduleSendRawPacket(final ByteBuf packet) {
        this.sendRawPacket(packet, false);
    }
    
    private void sendRawPacket(final ByteBuf packet, final boolean currentThread) {
        Runnable act;
        if (this.clientSide) {
            act = (() -> this.getChannel().pipeline().context(Via.getManager().getInjector().getDecoderName()).fireChannelRead(packet));
        }
        else {
            act = (() -> this.channel.pipeline().context(Via.getManager().getInjector().getEncoderName()).writeAndFlush(packet));
        }
        if (currentThread) {
            act.run();
        }
        else {
            try {
                this.channel.eventLoop().submit(act);
            }
            catch (Throwable e) {
                packet.release();
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public ChannelFuture sendRawPacketFuture(final ByteBuf packet) {
        if (this.clientSide) {
            this.getChannel().pipeline().context(Via.getManager().getInjector().getDecoderName()).fireChannelRead(packet);
            return this.getChannel().newSucceededFuture();
        }
        return this.channel.pipeline().context(Via.getManager().getInjector().getEncoderName()).writeAndFlush(packet);
    }
    
    @Override
    public PacketTracker getPacketTracker() {
        return this.packetTracker;
    }
    
    @Override
    public void disconnect(final String reason) {
        if (!this.channel.isOpen() || this.pendingDisconnect) {
            return;
        }
        this.pendingDisconnect = true;
        Via.getPlatform().runSync(() -> {
            if (!Via.getPlatform().disconnect(this, ChatColorUtil.translateAlternateColorCodes(reason))) {
                this.channel.close();
            }
        });
    }
    
    @Override
    public void sendRawPacketToServer(final ByteBuf packet) {
        if (this.clientSide) {
            this.sendRawPacketToServerClientSide(packet, true);
        }
        else {
            this.sendRawPacketToServerServerSide(packet, true);
        }
    }
    
    @Override
    public void scheduleSendRawPacketToServer(final ByteBuf packet) {
        if (this.clientSide) {
            this.sendRawPacketToServerClientSide(packet, false);
        }
        else {
            this.sendRawPacketToServerServerSide(packet, false);
        }
    }
    
    private void sendRawPacketToServerServerSide(final ByteBuf packet, final boolean currentThread) {
        final ByteBuf buf = packet.alloc().buffer();
        try {
            final ChannelHandlerContext context = PipelineUtil.getPreviousContext(Via.getManager().getInjector().getDecoderName(), this.channel.pipeline());
            if (this.shouldTransformPacket()) {
                try {
                    Type.VAR_INT.writePrimitive(buf, 1000);
                    Type.UUID.write(buf, this.generatePassthroughToken());
                }
                catch (Exception shouldNotHappen) {
                    throw new RuntimeException(shouldNotHappen);
                }
            }
            buf.writeBytes(packet);
            final ChannelHandlerContext channelHandlerContext;
            final Object o;
            final Runnable act = () -> {
                if (channelHandlerContext != null) {
                    channelHandlerContext.fireChannelRead(o);
                }
                else {
                    this.channel.pipeline().fireChannelRead(o);
                }
                return;
            };
            if (currentThread) {
                act.run();
            }
            else {
                try {
                    this.channel.eventLoop().submit(act);
                }
                catch (Throwable t) {
                    buf.release();
                    throw t;
                }
            }
        }
        finally {
            packet.release();
        }
    }
    
    private void sendRawPacketToServerClientSide(final ByteBuf packet, final boolean currentThread) {
        final Runnable act = () -> this.getChannel().pipeline().context(Via.getManager().getInjector().getEncoderName()).writeAndFlush(packet);
        if (currentThread) {
            act.run();
        }
        else {
            try {
                this.getChannel().eventLoop().submit(act);
            }
            catch (Throwable e) {
                e.printStackTrace();
                packet.release();
            }
        }
    }
    
    @Override
    public boolean checkServerboundPacket() {
        return !this.pendingDisconnect && (!this.packetLimiterEnabled || !this.packetTracker.incrementReceived() || !this.packetTracker.exceedsMaxPPS());
    }
    
    @Override
    public boolean checkClientboundPacket() {
        this.packetTracker.incrementSent();
        return true;
    }
    
    @Override
    public boolean shouldTransformPacket() {
        return this.active;
    }
    
    @Override
    public void transformClientbound(final ByteBuf buf, final Function<Throwable, Exception> cancelSupplier) throws Exception {
        this.transform(buf, Direction.CLIENTBOUND, cancelSupplier);
    }
    
    @Override
    public void transformServerbound(final ByteBuf buf, final Function<Throwable, Exception> cancelSupplier) throws Exception {
        this.transform(buf, Direction.SERVERBOUND, cancelSupplier);
    }
    
    private void transform(final ByteBuf buf, final Direction direction, final Function<Throwable, Exception> cancelSupplier) throws Exception {
        if (!buf.isReadable()) {
            return;
        }
        final int id = Type.VAR_INT.readPrimitive(buf);
        if (id != 1000) {
            final PacketWrapper wrapper = new PacketWrapperImpl(id, buf, this);
            try {
                this.protocolInfo.getPipeline().transform(direction, this.protocolInfo.getState(), wrapper);
            }
            catch (CancelException ex) {
                throw cancelSupplier.apply(ex);
            }
            final ByteBuf transformed = buf.alloc().buffer();
            try {
                wrapper.writeToBuffer(transformed);
                buf.clear().writeBytes(transformed);
            }
            finally {
                transformed.release();
            }
            return;
        }
        if (!this.passthroughTokens.remove(Type.UUID.read(buf))) {
            throw new IllegalArgumentException("Invalid token");
        }
    }
    
    @Override
    public long getId() {
        return this.id;
    }
    
    @Override
    public Channel getChannel() {
        return this.channel;
    }
    
    @Override
    public ProtocolInfo getProtocolInfo() {
        return this.protocolInfo;
    }
    
    @Override
    public Map<Class<?>, StorableObject> getStoredObjects() {
        return this.storedObjects;
    }
    
    @Override
    public boolean isActive() {
        return this.active;
    }
    
    @Override
    public void setActive(final boolean active) {
        this.active = active;
    }
    
    @Override
    public boolean isPendingDisconnect() {
        return this.pendingDisconnect;
    }
    
    @Override
    public void setPendingDisconnect(final boolean pendingDisconnect) {
        this.pendingDisconnect = pendingDisconnect;
    }
    
    @Override
    public boolean isClientSide() {
        return this.clientSide;
    }
    
    @Override
    public boolean shouldApplyBlockProtocol() {
        return !this.clientSide;
    }
    
    @Override
    public boolean isPacketLimiterEnabled() {
        return this.packetLimiterEnabled;
    }
    
    @Override
    public void setPacketLimiterEnabled(final boolean packetLimiterEnabled) {
        this.packetLimiterEnabled = packetLimiterEnabled;
    }
    
    @Override
    public UUID generatePassthroughToken() {
        final UUID token = UUID.randomUUID();
        this.passthroughTokens.add(token);
        return token;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final UserConnectionImpl that = (UserConnectionImpl)o;
        return this.id == that.id;
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(this.id);
    }
    
    static {
        IDS = new AtomicLong();
    }
}
