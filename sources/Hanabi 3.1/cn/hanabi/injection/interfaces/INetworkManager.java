package cn.hanabi.injection.interfaces;

import java.net.InetAddress;
import net.minecraft.network.Packet;

public interface INetworkManager {
   void sendPacketNoEvent(Packet var1);

   void createNetworkManagerAndConnect(InetAddress var1, int var2, boolean var3);
}
