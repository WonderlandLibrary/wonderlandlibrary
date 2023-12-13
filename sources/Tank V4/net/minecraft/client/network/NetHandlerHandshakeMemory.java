package net.minecraft.client.network;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.util.IChatComponent;

public class NetHandlerHandshakeMemory implements INetHandlerHandshakeServer {
   private final NetworkManager networkManager;
   private final MinecraftServer mcServer;

   public NetHandlerHandshakeMemory(MinecraftServer var1, NetworkManager var2) {
      this.mcServer = var1;
      this.networkManager = var2;
   }

   public void onDisconnect(IChatComponent var1) {
   }

   public void processHandshake(C00Handshake var1) {
      this.networkManager.setConnectionState(var1.getRequestedState());
      this.networkManager.setNetHandler(new NetHandlerLoginServer(this.mcServer, this.networkManager));
   }
}
