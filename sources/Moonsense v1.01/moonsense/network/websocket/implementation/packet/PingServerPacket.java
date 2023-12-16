// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.network.websocket.implementation.packet;

import java.io.IOException;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import moonsense.network.websocket.packets.Packet;

public class PingServerPacket implements Packet
{
    public PingServerPacket(final DataInputStream in) {
    }
    
    public PingServerPacket() {
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
    }
}
