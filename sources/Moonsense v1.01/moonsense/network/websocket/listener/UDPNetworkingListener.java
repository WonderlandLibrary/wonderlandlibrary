// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.network.websocket.listener;

import java.net.InetAddress;
import moonsense.network.websocket.packets.Packet;

public interface UDPNetworkingListener
{
    void onPacketReceived(final Packet p0, final InetAddress p1, final int p2);
}
