package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;

public class C12PacketUpdateSign implements Packet {
   private IChatComponent[] lines;
   private BlockPos pos;

   public C12PacketUpdateSign() {
   }

   public void processPacket(INetHandlerPlayServer var1) {
      var1.processUpdateSign(this);
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayServer)var1);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.pos = var1.readBlockPos();
      this.lines = new IChatComponent[4];

      for(int var2 = 0; var2 < 4; ++var2) {
         String var3 = var1.readStringFromBuffer(384);
         IChatComponent var4 = IChatComponent.Serializer.jsonToComponent(var3);
         this.lines[var2] = var4;
      }

   }

   public IChatComponent[] getLines() {
      return this.lines;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeBlockPos(this.pos);

      for(int var2 = 0; var2 < 4; ++var2) {
         IChatComponent var3 = this.lines[var2];
         String var4 = IChatComponent.Serializer.componentToJson(var3);
         var1.writeString(var4);
      }

   }

   public C12PacketUpdateSign(BlockPos var1, IChatComponent[] var2) {
      this.pos = var1;
      this.lines = new IChatComponent[]{var2[0], var2[1], var2[2], var2[3]};
   }

   public BlockPos getPosition() {
      return this.pos;
   }
}
