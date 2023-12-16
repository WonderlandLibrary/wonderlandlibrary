// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.network.websocket.packets;

import java.io.IOException;
import java.io.DataInputStream;
import java.util.IdentityHashMap;
import java.util.HashMap;
import java.util.Map;

public abstract class PacketProtocol
{
    private final Map<Integer, PacketDefinition<? extends Packet>> serverbound;
    private final Map<Integer, PacketDefinition<? extends Packet>> clientbound;
    private final Map<Class<? extends Packet>, Integer> clientboundIds;
    private final Map<Class<? extends Packet>, Integer> serverboundIds;
    
    public PacketProtocol() {
        this.serverbound = new HashMap<Integer, PacketDefinition<? extends Packet>>();
        this.clientbound = new HashMap<Integer, PacketDefinition<? extends Packet>>();
        this.clientboundIds = new IdentityHashMap<Class<? extends Packet>, Integer>();
        this.serverboundIds = new IdentityHashMap<Class<? extends Packet>, Integer>();
    }
    
    public final void clearPackets() {
        this.serverbound.clear();
        this.clientbound.clear();
        this.clientboundIds.clear();
        this.serverboundIds.clear();
    }
    
    public final <T extends Packet> void register(final int id, final Class<T> packet, final PacketFactory<T> factory) {
        this.registerServerbound(id, (Class<Packet>)packet, (PacketFactory<Packet>)factory);
        this.registerClientbound(id, (Class<Packet>)packet, (PacketFactory<Packet>)factory);
    }
    
    public final void register(final PacketDefinition<? extends Packet> definition) {
        this.registerServerbound(definition);
        this.registerClientbound(definition);
    }
    
    public final <T extends Packet> void registerServerbound(final int id, final Class<T> packet, final PacketFactory<T> factory) {
        this.registerServerbound(new PacketDefinition<Packet>(id, packet, factory));
    }
    
    public final void registerServerbound(final PacketDefinition<? extends Packet> definition) {
        this.serverbound.put(definition.getId(), definition);
        this.serverboundIds.put(definition.getPacketClass(), definition.getId());
    }
    
    public final <T extends Packet> void registerClientbound(final int id, final Class<T> packet, final PacketFactory<T> factory) {
        this.registerClientbound(new PacketDefinition<Packet>(id, packet, factory));
    }
    
    public final void registerClientbound(final PacketDefinition<? extends Packet> definition) {
        this.clientbound.put(definition.getId(), definition);
        this.clientboundIds.put(definition.getPacketClass(), definition.getId());
    }
    
    public Packet createClientboundPacket(final int id, final DataInputStream in) throws IOException {
        final PacketDefinition<?> definition = this.clientbound.get(id);
        if (definition == null) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }
        return (Packet)definition.getFactory().construct(in);
    }
    
    public int getClientboundId(final Class<? extends Packet> packetClass) {
        final Integer packetId = this.clientboundIds.get(packetClass);
        if (packetId == null) {
            throw new IllegalArgumentException("Unregistered clientbound packet class: " + packetClass.getName());
        }
        return packetId;
    }
    
    public int getClientboundId(final Packet packet) {
        return this.getClientboundId(packet.getClass());
    }
    
    public Class<? extends Packet> getClientboundClass(final int id) {
        final PacketDefinition<?> definition = this.clientbound.get(id);
        if (definition == null) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }
        return (Class<? extends Packet>)definition.getPacketClass();
    }
    
    public Packet createServerboundPacket(final int id, final DataInputStream in) throws IOException {
        final PacketDefinition<?> definition = this.serverbound.get(id);
        if (definition == null) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }
        return (Packet)definition.getFactory().construct(in);
    }
    
    public int getServerboundId(final Class<? extends Packet> packetClass) {
        final Integer packetId = this.serverboundIds.get(packetClass);
        if (packetId == null) {
            throw new IllegalArgumentException("Unregistered serverbound packet class: " + packetClass.getName());
        }
        return packetId;
    }
    
    public int getServerboundId(final Packet packet) {
        return this.getServerboundId(packet.getClass());
    }
    
    public Class<? extends Packet> getServerboundClass(final int id) {
        final PacketDefinition<?> definition = this.serverbound.get(id);
        if (definition == null) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }
        return (Class<? extends Packet>)definition.getPacketClass();
    }
}
