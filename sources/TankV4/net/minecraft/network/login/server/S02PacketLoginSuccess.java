package net.minecraft.network.login.server;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;

public class S02PacketLoginSuccess implements Packet {
   private GameProfile profile;

   public void readPacketData(PacketBuffer var1) throws IOException {
      String var2 = var1.readStringFromBuffer(36);
      String var3 = var1.readStringFromBuffer(16);
      UUID var4 = UUID.fromString(var2);
      this.profile = new GameProfile(var4, var3);
   }

   public void processPacket(INetHandlerLoginClient var1) {
      var1.handleLoginSuccess(this);
   }

   public S02PacketLoginSuccess() {
   }

   public GameProfile getProfile() {
      return this.profile;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      UUID var2 = this.profile.getId();
      var1.writeString(var2 == null ? "" : var2.toString());
      var1.writeString(this.profile.getName());
   }

   public S02PacketLoginSuccess(GameProfile var1) {
      this.profile = var1;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerLoginClient)var1);
   }
}
