package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C19PacketResourcePackStatus implements Packet {
   private String hash;
   private C19PacketResourcePackStatus.Action status;

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayServer)var1);
   }

   public C19PacketResourcePackStatus(String var1, C19PacketResourcePackStatus.Action var2) {
      if (var1.length() > 40) {
         var1 = var1.substring(0, 40);
      }

      this.hash = var1;
      this.status = var2;
   }

   public void processPacket(INetHandlerPlayServer var1) {
      var1.handleResourcePackStatus(this);
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeString(this.hash);
      var1.writeEnumValue(this.status);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.hash = var1.readStringFromBuffer(40);
      this.status = (C19PacketResourcePackStatus.Action)var1.readEnumValue(C19PacketResourcePackStatus.Action.class);
   }

   public C19PacketResourcePackStatus() {
   }

   public static enum Action {
      ACCEPTED,
      DECLINED;

      private static final C19PacketResourcePackStatus.Action[] ENUM$VALUES = new C19PacketResourcePackStatus.Action[]{SUCCESSFULLY_LOADED, DECLINED, FAILED_DOWNLOAD, ACCEPTED};
      SUCCESSFULLY_LOADED,
      FAILED_DOWNLOAD;
   }
}
