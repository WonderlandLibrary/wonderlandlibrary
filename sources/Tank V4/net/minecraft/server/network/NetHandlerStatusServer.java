package net.minecraft.server.network;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.status.INetHandlerStatusServer;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class NetHandlerStatusServer implements INetHandlerStatusServer {
   private final MinecraftServer server;
   private boolean field_183008_d;
   private static final IChatComponent field_183007_a = new ChatComponentText("Status request has been handled.");
   private final NetworkManager networkManager;

   public void processPing(C01PacketPing var1) {
      this.networkManager.sendPacket(new S01PacketPong(var1.getClientTime()));
      this.networkManager.closeChannel(field_183007_a);
   }

   public NetHandlerStatusServer(MinecraftServer var1, NetworkManager var2) {
      this.server = var1;
      this.networkManager = var2;
   }

   public void processServerQuery(C00PacketServerQuery var1) {
      if (this.field_183008_d) {
         this.networkManager.closeChannel(field_183007_a);
      } else {
         this.field_183008_d = true;
         this.networkManager.sendPacket(new S00PacketServerInfo(this.server.getServerStatusResponse()));
      }

   }

   public void onDisconnect(IChatComponent var1) {
   }
}
