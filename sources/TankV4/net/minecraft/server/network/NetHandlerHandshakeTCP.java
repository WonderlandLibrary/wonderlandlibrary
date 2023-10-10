package net.minecraft.server.network;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class NetHandlerHandshakeTCP implements INetHandlerHandshakeServer {
   private final NetworkManager networkManager;
   private final MinecraftServer server;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$network$EnumConnectionState;

   static int[] $SWITCH_TABLE$net$minecraft$network$EnumConnectionState() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$network$EnumConnectionState;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[EnumConnectionState.values().length];

         try {
            var0[EnumConnectionState.HANDSHAKING.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[EnumConnectionState.LOGIN.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[EnumConnectionState.PLAY.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[EnumConnectionState.STATUS.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$network$EnumConnectionState = var0;
         return var0;
      }
   }

   public NetHandlerHandshakeTCP(MinecraftServer var1, NetworkManager var2) {
      this.server = var1;
      this.networkManager = var2;
   }

   public void onDisconnect(IChatComponent var1) {
   }

   public void processHandshake(C00Handshake var1) {
      switch($SWITCH_TABLE$net$minecraft$network$EnumConnectionState()[var1.getRequestedState().ordinal()]) {
      case 3:
         this.networkManager.setConnectionState(EnumConnectionState.STATUS);
         this.networkManager.setNetHandler(new NetHandlerStatusServer(this.server, this.networkManager));
         break;
      case 4:
         this.networkManager.setConnectionState(EnumConnectionState.LOGIN);
         ChatComponentText var2;
         if (var1.getProtocolVersion() > 47) {
            var2 = new ChatComponentText("Outdated server! I'm still on 1.8.8");
            this.networkManager.sendPacket(new S00PacketDisconnect(var2));
            this.networkManager.closeChannel(var2);
         } else if (var1.getProtocolVersion() < 47) {
            var2 = new ChatComponentText("Outdated client! Please use 1.8.8");
            this.networkManager.sendPacket(new S00PacketDisconnect(var2));
            this.networkManager.closeChannel(var2);
         } else {
            this.networkManager.setNetHandler(new NetHandlerLoginServer(this.server, this.networkManager));
         }
         break;
      default:
         throw new UnsupportedOperationException("Invalid intention " + var1.getRequestedState());
      }

   }
}
