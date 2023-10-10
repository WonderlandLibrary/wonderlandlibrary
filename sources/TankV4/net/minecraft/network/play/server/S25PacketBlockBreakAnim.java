package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;

public class S25PacketBlockBreakAnim implements Packet {
   private int progress;
   private int breakerId;
   private BlockPos position;

   public S25PacketBlockBreakAnim() {
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleBlockBreakAnim(this);
   }

   public int getProgress() {
      return this.progress;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeVarIntToBuffer(this.breakerId);
      var1.writeBlockPos(this.position);
      var1.writeByte(this.progress);
   }

   public int getBreakerId() {
      return this.breakerId;
   }

   public BlockPos getPosition() {
      return this.position;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.breakerId = var1.readVarIntFromBuffer();
      this.position = var1.readBlockPos();
      this.progress = var1.readUnsignedByte();
   }

   public S25PacketBlockBreakAnim(int var1, BlockPos var2, int var3) {
      this.breakerId = var1;
      this.position = var2;
      this.progress = var3;
   }
}
