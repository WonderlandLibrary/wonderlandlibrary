// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.protocol;

import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.Direction;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public interface Protocol<C1 extends ClientboundPacketType, C2 extends ClientboundPacketType, S1 extends ServerboundPacketType, S2 extends ServerboundPacketType>
{
    default void registerServerbound(final State state, final int oldPacketID, final int newPacketID) {
        this.registerServerbound(state, oldPacketID, newPacketID, null);
    }
    
    default void registerServerbound(final State state, final int oldPacketID, final int newPacketID, final PacketRemapper packetRemapper) {
        this.registerServerbound(state, oldPacketID, newPacketID, packetRemapper, false);
    }
    
    void registerServerbound(final State p0, final int p1, final int p2, final PacketRemapper p3, final boolean p4);
    
    @Deprecated
    void cancelServerbound(final State p0, final int p1, final int p2);
    
    void cancelServerbound(final State p0, final int p1);
    
    default void registerClientbound(final State state, final int oldPacketID, final int newPacketID) {
        this.registerClientbound(state, oldPacketID, newPacketID, null);
    }
    
    default void registerClientbound(final State state, final int oldPacketID, final int newPacketID, final PacketRemapper packetRemapper) {
        this.registerClientbound(state, oldPacketID, newPacketID, packetRemapper, false);
    }
    
    @Deprecated
    void cancelClientbound(final State p0, final int p1, final int p2);
    
    void cancelClientbound(final State p0, final int p1);
    
    void registerClientbound(final State p0, final int p1, final int p2, final PacketRemapper p3, final boolean p4);
    
    void registerClientbound(final C1 p0, final PacketRemapper p1);
    
    default void registerClientbound(final C1 packetType, final C2 mappedPacketType) {
        this.registerClientbound(packetType, mappedPacketType, null);
    }
    
    default void registerClientbound(final C1 packetType, final C2 mappedPacketType, final PacketRemapper packetRemapper) {
        this.registerClientbound(packetType, mappedPacketType, packetRemapper, false);
    }
    
    void registerClientbound(final C1 p0, final C2 p1, final PacketRemapper p2, final boolean p3);
    
    void cancelClientbound(final C1 p0);
    
    default void registerServerbound(final S2 packetType, final S1 mappedPacketType) {
        this.registerServerbound(packetType, mappedPacketType, null);
    }
    
    void registerServerbound(final S2 p0, final PacketRemapper p1);
    
    default void registerServerbound(final S2 packetType, final S1 mappedPacketType, final PacketRemapper packetRemapper) {
        this.registerServerbound(packetType, mappedPacketType, packetRemapper, false);
    }
    
    void registerServerbound(final S2 p0, final S1 p1, final PacketRemapper p2, final boolean p3);
    
    void cancelServerbound(final S2 p0);
    
    boolean hasRegisteredClientbound(final C1 p0);
    
    boolean hasRegisteredServerbound(final S2 p0);
    
    boolean hasRegisteredClientbound(final State p0, final int p1);
    
    boolean hasRegisteredServerbound(final State p0, final int p1);
    
    void transform(final Direction p0, final State p1, final PacketWrapper p2) throws Exception;
    
     <T> T get(final Class<T> p0);
    
    void put(final Object p0);
    
    void initialize();
    
    boolean hasMappingDataToLoad();
    
    void loadMappingData();
    
    default void register(final ViaProviders providers) {
    }
    
    default void init(final UserConnection userConnection) {
    }
    
    default MappingData getMappingData() {
        return null;
    }
    
    default EntityRewriter getEntityRewriter() {
        return null;
    }
    
    default ItemRewriter getItemRewriter() {
        return null;
    }
    
    default boolean isBaseProtocol() {
        return false;
    }
}
