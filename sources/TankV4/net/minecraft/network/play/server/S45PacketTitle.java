package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;

public class S45PacketTitle implements Packet {
   private int displayTime;
   private int fadeInTime;
   private int fadeOutTime;
   private IChatComponent message;
   private S45PacketTitle.Type type;

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeEnumValue(this.type);
      if (this.type == S45PacketTitle.Type.TITLE || this.type == S45PacketTitle.Type.SUBTITLE) {
         var1.writeChatComponent(this.message);
      }

      if (this.type == S45PacketTitle.Type.TIMES) {
         var1.writeInt(this.fadeInTime);
         var1.writeInt(this.displayTime);
         var1.writeInt(this.fadeOutTime);
      }

   }

   public S45PacketTitle(S45PacketTitle.Type var1, IChatComponent var2, int var3, int var4, int var5) {
      this.type = var1;
      this.message = var2;
      this.fadeInTime = var3;
      this.displayTime = var4;
      this.fadeOutTime = var5;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.type = (S45PacketTitle.Type)var1.readEnumValue(S45PacketTitle.Type.class);
      if (this.type == S45PacketTitle.Type.TITLE || this.type == S45PacketTitle.Type.SUBTITLE) {
         this.message = var1.readChatComponent();
      }

      if (this.type == S45PacketTitle.Type.TIMES) {
         this.fadeInTime = var1.readInt();
         this.displayTime = var1.readInt();
         this.fadeOutTime = var1.readInt();
      }

   }

   public int getFadeInTime() {
      return this.fadeInTime;
   }

   public int getDisplayTime() {
      return this.displayTime;
   }

   public S45PacketTitle() {
   }

   public S45PacketTitle.Type getType() {
      return this.type;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleTitle(this);
   }

   public IChatComponent getMessage() {
      return this.message;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public int getFadeOutTime() {
      return this.fadeOutTime;
   }

   public S45PacketTitle(S45PacketTitle.Type var1, IChatComponent var2) {
      this(var1, var2, -1, -1, -1);
   }

   public S45PacketTitle(int var1, int var2, int var3) {
      this(S45PacketTitle.Type.TIMES, (IChatComponent)null, var1, var2, var3);
   }

   public static enum Type {
      TITLE,
      RESET;

      private static final S45PacketTitle.Type[] ENUM$VALUES = new S45PacketTitle.Type[]{TITLE, SUBTITLE, TIMES, CLEAR, RESET};
      TIMES,
      SUBTITLE,
      CLEAR;

      public static String[] getNames() {
         String[] var0 = new String[values().length];
         int var1 = 0;
         S45PacketTitle.Type[] var5;
         int var4 = (var5 = values()).length;

         for(int var3 = 0; var3 < var4; ++var3) {
            S45PacketTitle.Type var2 = var5[var3];
            var0[var1++] = var2.name().toLowerCase();
         }

         return var0;
      }

      public static S45PacketTitle.Type byName(String var0) {
         S45PacketTitle.Type[] var4;
         int var3 = (var4 = values()).length;

         for(int var2 = 0; var2 < var3; ++var2) {
            S45PacketTitle.Type var1 = var4[var2];
            if (var1.name().equalsIgnoreCase(var0)) {
               return var1;
            }
         }

         return TITLE;
      }
   }
}
