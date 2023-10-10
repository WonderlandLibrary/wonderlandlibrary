// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.protocol;

import com.viaversion.viaversion.exception.CancelException;
import com.viaversion.viaversion.exception.InformativeException;
import com.viaversion.viaversion.api.protocol.packet.Direction;
import java.util.Arrays;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.logging.Level;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public abstract class AbstractProtocol<C1 extends ClientboundPacketType, C2 extends ClientboundPacketType, S1 extends ServerboundPacketType, S2 extends ServerboundPacketType> implements Protocol<C1, C2, S1, S2>
{
    private final Map<Packet, ProtocolPacket> serverbound;
    private final Map<Packet, ProtocolPacket> clientbound;
    private final Map<Class<?>, Object> storedObjects;
    protected final Class<C1> oldClientboundPacketEnum;
    protected final Class<C2> newClientboundPacketEnum;
    protected final Class<S1> oldServerboundPacketEnum;
    protected final Class<S2> newServerboundPacketEnum;
    private boolean initialized;
    
    protected AbstractProtocol() {
        this(null, null, null, null);
    }
    
    protected AbstractProtocol(final Class<C1> oldClientboundPacketEnum, final Class<C2> clientboundPacketEnum, final Class<S1> oldServerboundPacketEnum, final Class<S2> serverboundPacketEnum) {
        this.serverbound = new HashMap<Packet, ProtocolPacket>();
        this.clientbound = new HashMap<Packet, ProtocolPacket>();
        this.storedObjects = new HashMap<Class<?>, Object>();
        this.oldClientboundPacketEnum = oldClientboundPacketEnum;
        this.newClientboundPacketEnum = clientboundPacketEnum;
        this.oldServerboundPacketEnum = oldServerboundPacketEnum;
        this.newServerboundPacketEnum = serverboundPacketEnum;
    }
    
    @Override
    public final void initialize() {
        Preconditions.checkArgument(!this.initialized);
        this.initialized = true;
        this.registerPackets();
        if (this.oldClientboundPacketEnum != null && this.newClientboundPacketEnum != null && this.oldClientboundPacketEnum != this.newClientboundPacketEnum) {
            this.registerClientboundChannelIdChanges();
        }
        if (this.oldServerboundPacketEnum != null && this.newServerboundPacketEnum != null && this.oldServerboundPacketEnum != this.newServerboundPacketEnum) {
            this.registerServerboundChannelIdChanges();
        }
    }
    
    protected void registerClientboundChannelIdChanges() {
        final C2[] newConstants = this.newClientboundPacketEnum.getEnumConstants();
        final Map<String, C2> newClientboundPackets = new HashMap<String, C2>(newConstants.length);
        for (final C2 newConstant : newConstants) {
            newClientboundPackets.put(newConstant.getName(), newConstant);
        }
        for (final C1 packet : this.oldClientboundPacketEnum.getEnumConstants()) {
            final C2 mappedPacket = newClientboundPackets.get(packet.getName());
            if (mappedPacket == null) {
                Preconditions.checkArgument(this.hasRegisteredClientbound(packet), (Object)("Packet " + packet + " in " + this.getClass().getSimpleName() + " has no mapping - it needs to be manually cancelled or remapped!"));
            }
            else if (!this.hasRegisteredClientbound(packet)) {
                this.registerClientbound(packet, mappedPacket);
            }
        }
    }
    
    protected void registerServerboundChannelIdChanges() {
        final S1[] oldConstants = this.oldServerboundPacketEnum.getEnumConstants();
        final Map<String, S1> oldServerboundConstants = new HashMap<String, S1>(oldConstants.length);
        for (final S1 oldConstant : oldConstants) {
            oldServerboundConstants.put(oldConstant.getName(), oldConstant);
        }
        for (final S2 packet : this.newServerboundPacketEnum.getEnumConstants()) {
            final S1 mappedPacket = oldServerboundConstants.get(packet.getName());
            if (mappedPacket == null) {
                Preconditions.checkArgument(this.hasRegisteredServerbound(packet), (Object)("Packet " + packet + " in " + this.getClass().getSimpleName() + " has no mapping - it needs to be manually cancelled or remapped!"));
            }
            else if (!this.hasRegisteredServerbound(packet)) {
                this.registerServerbound(packet, mappedPacket);
            }
        }
    }
    
    protected void registerPackets() {
    }
    
    @Override
    public final void loadMappingData() {
        this.getMappingData().load();
        this.onMappingDataLoaded();
    }
    
    protected void onMappingDataLoaded() {
    }
    
    protected void addEntityTracker(final UserConnection connection, final EntityTracker tracker) {
        connection.addEntityTracker(this.getClass(), tracker);
    }
    
    @Override
    public void registerServerbound(final State state, final int oldPacketID, final int newPacketID, final PacketRemapper packetRemapper, final boolean override) {
        final ProtocolPacket protocolPacket = new ProtocolPacket(state, oldPacketID, newPacketID, packetRemapper);
        final Packet packet = new Packet(state, newPacketID);
        if (!override && this.serverbound.containsKey(packet)) {
            Via.getPlatform().getLogger().log(Level.WARNING, packet + " already registered! If this override is intentional, set override to true. Stacktrace: ", new Exception());
        }
        this.serverbound.put(packet, protocolPacket);
    }
    
    @Override
    public void cancelServerbound(final State state, final int oldPacketID, final int newPacketID) {
        this.registerServerbound(state, oldPacketID, newPacketID, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(PacketWrapper::cancel);
            }
        });
    }
    
    @Override
    public void cancelServerbound(final State state, final int newPacketID) {
        this.cancelServerbound(state, -1, newPacketID);
    }
    
    @Override
    public void cancelClientbound(final State state, final int oldPacketID, final int newPacketID) {
        this.registerClientbound(state, oldPacketID, newPacketID, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(PacketWrapper::cancel);
            }
        });
    }
    
    @Override
    public void cancelClientbound(final State state, final int oldPacketID) {
        this.cancelClientbound(state, oldPacketID, -1);
    }
    
    @Override
    public void registerClientbound(final State state, final int oldPacketID, final int newPacketID, final PacketRemapper packetRemapper, final boolean override) {
        final ProtocolPacket protocolPacket = new ProtocolPacket(state, oldPacketID, newPacketID, packetRemapper);
        final Packet packet = new Packet(state, oldPacketID);
        if (!override && this.clientbound.containsKey(packet)) {
            Via.getPlatform().getLogger().log(Level.WARNING, packet + " already registered! If override is intentional, set override to true. Stacktrace: ", new Exception());
        }
        this.clientbound.put(packet, protocolPacket);
    }
    
    @Override
    public void registerClientbound(final C1 packetType, final PacketRemapper packetRemapper) {
        this.checkPacketType(packetType, this.oldClientboundPacketEnum == null || packetType.getClass() == this.oldClientboundPacketEnum);
        final C2 mappedPacket = (C2)((this.oldClientboundPacketEnum == this.newClientboundPacketEnum) ? packetType : ((C2)Arrays.stream(this.newClientboundPacketEnum.getEnumConstants()).filter(en -> en.getName().equals(packetType.getName())).findAny().orElse(null)));
        Preconditions.checkNotNull(mappedPacket, (Object)("Packet type " + packetType + " in " + packetType.getClass().getSimpleName() + " could not be automatically mapped!"));
        this.registerClientbound(packetType, mappedPacket, packetRemapper);
    }
    
    @Override
    public void registerClientbound(final C1 packetType, final C2 mappedPacketType, final PacketRemapper packetRemapper, final boolean override) {
        this.register(this.clientbound, packetType, mappedPacketType, this.oldClientboundPacketEnum, this.newClientboundPacketEnum, packetRemapper, override);
    }
    
    @Override
    public void cancelClientbound(final C1 packetType) {
        this.registerClientbound(packetType, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(PacketWrapper::cancel);
            }
        });
    }
    
    @Override
    public void registerServerbound(final S2 packetType, final PacketRemapper packetRemapper) {
        this.checkPacketType(packetType, this.newServerboundPacketEnum == null || packetType.getClass() == this.newServerboundPacketEnum);
        final S1 mappedPacket = (S1)((this.oldServerboundPacketEnum == this.newServerboundPacketEnum) ? packetType : ((S1)Arrays.stream(this.oldServerboundPacketEnum.getEnumConstants()).filter(en -> en.getName().equals(packetType.getName())).findAny().orElse(null)));
        Preconditions.checkNotNull(mappedPacket, (Object)("Packet type " + packetType + " in " + packetType.getClass().getSimpleName() + " could not be automatically mapped!"));
        this.registerServerbound(packetType, mappedPacket, packetRemapper);
    }
    
    @Override
    public void registerServerbound(final S2 packetType, final S1 mappedPacketType, final PacketRemapper packetRemapper, final boolean override) {
        this.register(this.serverbound, packetType, mappedPacketType, this.newServerboundPacketEnum, this.oldServerboundPacketEnum, packetRemapper, override);
    }
    
    @Override
    public void cancelServerbound(final S2 packetType) {
        this.registerServerbound(packetType, null, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(PacketWrapper::cancel);
            }
        });
    }
    
    private void register(final Map<Packet, ProtocolPacket> packetMap, final PacketType packetType, final PacketType mappedPacketType, final Class<? extends PacketType> unmappedPacketEnum, final Class<? extends PacketType> mappedPacketEnum, final PacketRemapper remapper, final boolean override) {
        this.checkPacketType(packetType, unmappedPacketEnum == null || packetType.getClass() == unmappedPacketEnum);
        this.checkPacketType(mappedPacketType, mappedPacketType == null || mappedPacketEnum == null || mappedPacketType.getClass() == mappedPacketEnum);
        Preconditions.checkArgument(mappedPacketType == null || packetType.state() == mappedPacketType.state(), (Object)"Packet type state does not match mapped packet type state");
        final ProtocolPacket protocolPacket = new ProtocolPacket(packetType.state(), packetType, mappedPacketType, remapper);
        final Packet packet = new Packet(packetType.state(), packetType.getId());
        if (!override && packetMap.containsKey(packet)) {
            Via.getPlatform().getLogger().log(Level.WARNING, packet + " already registered! If override is intentional, set override to true. Stacktrace: ", new Exception());
        }
        packetMap.put(packet, protocolPacket);
    }
    
    @Override
    public boolean hasRegisteredClientbound(final C1 packetType) {
        return this.hasRegisteredClientbound(packetType.state(), packetType.getId());
    }
    
    @Override
    public boolean hasRegisteredServerbound(final S2 packetType) {
        return this.hasRegisteredServerbound(packetType.state(), packetType.getId());
    }
    
    @Override
    public boolean hasRegisteredClientbound(final State state, final int unmappedPacketId) {
        final Packet packet = new Packet(state, unmappedPacketId);
        return this.clientbound.containsKey(packet);
    }
    
    @Override
    public boolean hasRegisteredServerbound(final State state, final int unmappedPacketId) {
        final Packet packet = new Packet(state, unmappedPacketId);
        return this.serverbound.containsKey(packet);
    }
    
    @Override
    public void transform(final Direction direction, final State state, final PacketWrapper packetWrapper) throws Exception {
        final Packet statePacket = new Packet(state, packetWrapper.getId());
        final Map<Packet, ProtocolPacket> packetMap = (direction == Direction.CLIENTBOUND) ? this.clientbound : this.serverbound;
        final ProtocolPacket protocolPacket = packetMap.get(statePacket);
        if (protocolPacket == null) {
            return;
        }
        final int unmappedId = packetWrapper.getId();
        if (protocolPacket.isMappedOverTypes()) {
            packetWrapper.setPacketType(protocolPacket.getMappedPacketType());
        }
        else {
            final int mappedId = (direction == Direction.CLIENTBOUND) ? protocolPacket.getNewId() : protocolPacket.getOldId();
            if (unmappedId != mappedId) {
                packetWrapper.setId(mappedId);
            }
        }
        final PacketRemapper remapper = protocolPacket.getRemapper();
        if (remapper != null) {
            try {
                remapper.remap(packetWrapper);
            }
            catch (InformativeException e) {
                this.throwRemapError(direction, state, unmappedId, packetWrapper.getId(), e);
                return;
            }
            if (packetWrapper.isCancelled()) {
                throw CancelException.generate();
            }
        }
    }
    
    private void throwRemapError(final Direction direction, final State state, final int oldId, final int newId, final InformativeException e) throws InformativeException {
        if (state == State.HANDSHAKE) {
            throw e;
        }
        final Class<? extends PacketType> packetTypeClass = (Class<? extends PacketType>)((state == State.PLAY) ? ((direction == Direction.CLIENTBOUND) ? this.oldClientboundPacketEnum : this.newServerboundPacketEnum) : null);
        if (packetTypeClass != null) {
            final PacketType[] enumConstants = (PacketType[])packetTypeClass.getEnumConstants();
            final PacketType packetType = (oldId < enumConstants.length && oldId >= 0) ? enumConstants[oldId] : null;
            Via.getPlatform().getLogger().warning("ERROR IN " + this.getClass().getSimpleName() + " IN REMAP OF " + packetType + " (" + this.toNiceHex(oldId) + ")");
        }
        else {
            Via.getPlatform().getLogger().warning("ERROR IN " + this.getClass().getSimpleName() + " IN REMAP OF " + this.toNiceHex(oldId) + "->" + this.toNiceHex(newId));
        }
        throw e;
    }
    
    private String toNiceHex(final int id) {
        final String hex = Integer.toHexString(id).toUpperCase();
        return ((hex.length() == 1) ? "0x0" : "0x") + hex;
    }
    
    private void checkPacketType(final PacketType packetType, final boolean isValid) {
        if (!isValid) {
            throw new IllegalArgumentException("Packet type " + packetType + " in " + packetType.getClass().getSimpleName() + " is taken from the wrong enum");
        }
    }
    
    @Override
    public <T> T get(final Class<T> objectClass) {
        return (T)this.storedObjects.get(objectClass);
    }
    
    @Override
    public void put(final Object object) {
        this.storedObjects.put(object.getClass(), object);
    }
    
    @Override
    public boolean hasMappingDataToLoad() {
        return this.getMappingData() != null;
    }
    
    @Override
    public String toString() {
        return "Protocol:" + this.getClass().getSimpleName();
    }
    
    public static final class Packet
    {
        private final State state;
        private final int packetId;
        
        public Packet(final State state, final int packetId) {
            this.state = state;
            this.packetId = packetId;
        }
        
        public State getState() {
            return this.state;
        }
        
        public int getPacketId() {
            return this.packetId;
        }
        
        @Override
        public String toString() {
            return "Packet{state=" + this.state + ", packetId=" + this.packetId + '}';
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Packet that = (Packet)o;
            return this.packetId == that.packetId && this.state == that.state;
        }
        
        @Override
        public int hashCode() {
            int result = (this.state != null) ? this.state.hashCode() : 0;
            result = 31 * result + this.packetId;
            return result;
        }
    }
    
    public static final class ProtocolPacket
    {
        private final State state;
        private final int oldId;
        private final int newId;
        private final PacketType unmappedPacketType;
        private final PacketType mappedPacketType;
        private final PacketRemapper remapper;
        
        @Deprecated
        public ProtocolPacket(final State state, final int oldId, final int newId, final PacketRemapper remapper) {
            this.state = state;
            this.oldId = oldId;
            this.newId = newId;
            this.remapper = remapper;
            this.unmappedPacketType = null;
            this.mappedPacketType = null;
        }
        
        public ProtocolPacket(final State state, final PacketType unmappedPacketType, final PacketType mappedPacketType, final PacketRemapper remapper) {
            this.state = state;
            this.unmappedPacketType = unmappedPacketType;
            if (unmappedPacketType.direction() == Direction.CLIENTBOUND) {
                this.oldId = unmappedPacketType.getId();
                this.newId = ((mappedPacketType != null) ? mappedPacketType.getId() : -1);
            }
            else {
                this.oldId = ((mappedPacketType != null) ? mappedPacketType.getId() : -1);
                this.newId = unmappedPacketType.getId();
            }
            this.mappedPacketType = mappedPacketType;
            this.remapper = remapper;
        }
        
        public State getState() {
            return this.state;
        }
        
        @Deprecated
        public int getOldId() {
            return this.oldId;
        }
        
        @Deprecated
        public int getNewId() {
            return this.newId;
        }
        
        public PacketType getUnmappedPacketType() {
            return this.unmappedPacketType;
        }
        
        public PacketType getMappedPacketType() {
            return this.mappedPacketType;
        }
        
        public boolean isMappedOverTypes() {
            return this.unmappedPacketType != null;
        }
        
        public PacketRemapper getRemapper() {
            return this.remapper;
        }
    }
}
