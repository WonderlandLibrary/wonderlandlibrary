package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.entity.DataWatcher;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S1CPacketEntityMetadata implements Packet {
   private List field_149378_b;
   private int entityId;

   public int getEntityId() {
      return this.entityId;
   }

   public S1CPacketEntityMetadata() {
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeVarIntToBuffer(this.entityId);
      DataWatcher.writeWatchedListToPacketBuffer(this.field_149378_b, var1);
   }

   public S1CPacketEntityMetadata(int var1, DataWatcher var2, boolean var3) {
      this.entityId = var1;
      if (var3) {
         this.field_149378_b = var2.getAllWatched();
      } else {
         this.field_149378_b = var2.getChanged();
      }

   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public List func_149376_c() {
      return this.field_149378_b;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = var1.readVarIntFromBuffer();
      this.field_149378_b = DataWatcher.readWatchedListFromPacketBuffer(var1);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleEntityMetadata(this);
   }
}
