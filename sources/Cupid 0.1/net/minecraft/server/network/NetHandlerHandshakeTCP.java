package net.minecraft.server.network;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class NetHandlerHandshakeTCP implements INetHandlerHandshakeServer {
  private final MinecraftServer server;
  
  private final NetworkManager networkManager;
  
  public NetHandlerHandshakeTCP(MinecraftServer serverIn, NetworkManager netManager) {
    this.server = serverIn;
    this.networkManager = netManager;
  }
  
  public void processHandshake(C00Handshake packetIn) {
    switch (packetIn.getRequestedState()) {
      case LOGIN:
        this.networkManager.setConnectionState(EnumConnectionState.LOGIN);
        if (packetIn.getProtocolVersion() > 47) {
          ChatComponentText chatcomponenttext = new ChatComponentText("Outdated server! I'm still on 1.8.8");
          this.networkManager.sendPacket((Packet)new S00PacketDisconnect((IChatComponent)chatcomponenttext));
          this.networkManager.closeChannel((IChatComponent)chatcomponenttext);
        } else if (packetIn.getProtocolVersion() < 47) {
          ChatComponentText chatcomponenttext1 = new ChatComponentText("Outdated client! Please use 1.8.8");
          this.networkManager.sendPacket((Packet)new S00PacketDisconnect((IChatComponent)chatcomponenttext1));
          this.networkManager.closeChannel((IChatComponent)chatcomponenttext1);
        } else {
          this.networkManager.setNetHandler((INetHandler)new NetHandlerLoginServer(this.server, this.networkManager));
        } 
        return;
      case STATUS:
        this.networkManager.setConnectionState(EnumConnectionState.STATUS);
        this.networkManager.setNetHandler((INetHandler)new NetHandlerStatusServer(this.server, this.networkManager));
        return;
    } 
    throw new UnsupportedOperationException("Invalid intention " + packetIn.getRequestedState());
  }
  
  public void onDisconnect(IChatComponent reason) {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\server\network\NetHandlerHandshakeTCP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */