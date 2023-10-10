package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;

public class S05PacketSpawnPosition implements Packet {
   private BlockPos spawnBlockPos;

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public S05PacketSpawnPosition() {
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleSpawnPosition(this);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.spawnBlockPos = var1.readBlockPos();
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeBlockPos(this.spawnBlockPos);
   }

   public BlockPos getSpawnPos() {
      return this.spawnBlockPos;
   }

   public S05PacketSpawnPosition(BlockPos var1) {
      this.spawnBlockPos = var1;
   }
}
