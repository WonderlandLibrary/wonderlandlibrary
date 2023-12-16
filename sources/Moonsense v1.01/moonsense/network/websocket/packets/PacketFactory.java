// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.network.websocket.packets;

import java.io.IOException;
import java.io.DataInputStream;

@FunctionalInterface
public interface PacketFactory<T extends Packet>
{
    T construct(final DataInputStream p0) throws IOException;
}
