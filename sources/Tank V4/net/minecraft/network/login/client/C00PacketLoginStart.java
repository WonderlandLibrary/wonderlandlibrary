package net.minecraft.network.login.client;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginServer;

public class C00PacketLoginStart implements Packet {
   private GameProfile profile;

   public C00PacketLoginStart() {
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeString(this.profile.getName());
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerLoginServer)var1);
   }

   public GameProfile getProfile() {
      return this.profile;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.profile = new GameProfile((UUID)null, var1.readStringFromBuffer(16));
   }

   public void processPacket(INetHandlerLoginServer var1) {
      var1.processLoginStart(this);
   }

   public C00PacketLoginStart(GameProfile var1) {
      this.profile = var1;
   }
}
