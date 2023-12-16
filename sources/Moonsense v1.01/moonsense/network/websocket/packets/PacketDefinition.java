// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.network.websocket.packets;

public class PacketDefinition<T extends Packet>
{
    private final int id;
    private final Class<T> packetClass;
    private final PacketFactory<T> factory;
    
    public PacketDefinition(final int id, final Class<T> packetClass, final PacketFactory<T> factory) {
        this.id = id;
        this.packetClass = packetClass;
        this.factory = factory;
    }
    
    public int getId() {
        return this.id;
    }
    
    public Class<T> getPacketClass() {
        return this.packetClass;
    }
    
    public PacketFactory<T> getFactory() {
        return this.factory;
    }
}
