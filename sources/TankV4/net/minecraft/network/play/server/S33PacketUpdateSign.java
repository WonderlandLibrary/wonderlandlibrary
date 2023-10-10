package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class S33PacketUpdateSign implements Packet {
   private World world;
   private BlockPos blockPos;
   private IChatComponent[] lines;

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeBlockPos(this.blockPos);

      for(int var2 = 0; var2 < 4; ++var2) {
         var1.writeChatComponent(this.lines[var2]);
      }

   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public S33PacketUpdateSign() {
   }

   public S33PacketUpdateSign(World var1, BlockPos var2, IChatComponent[] var3) {
      this.world = var1;
      this.blockPos = var2;
      this.lines = new IChatComponent[]{var3[0], var3[1], var3[2], var3[3]};
   }

   public BlockPos getPos() {
      return this.blockPos;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.blockPos = var1.readBlockPos();
      this.lines = new IChatComponent[4];

      for(int var2 = 0; var2 < 4; ++var2) {
         this.lines[var2] = var1.readChatComponent();
      }

   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleUpdateSign(this);
   }

   public IChatComponent[] getLines() {
      return this.lines;
   }
}
