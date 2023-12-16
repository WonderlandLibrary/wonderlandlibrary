// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.network.websocket.implementation.protocol;

import moonsense.network.websocket.implementation.packet.IsUserOnlinePacket;
import moonsense.network.websocket.implementation.packet.PingServerPacket;
import moonsense.network.websocket.implementation.packet.UpdateUserPacket;
import moonsense.network.websocket.packets.PacketProtocol;

public class ClientProtocol extends PacketProtocol
{
    public ClientProtocol() {
        this.register(1, UpdateUserPacket.class, UpdateUserPacket::new);
        this.register(2, PingServerPacket.class, PingServerPacket::new);
        this.register(3, IsUserOnlinePacket.class, IsUserOnlinePacket::new);
    }
}
