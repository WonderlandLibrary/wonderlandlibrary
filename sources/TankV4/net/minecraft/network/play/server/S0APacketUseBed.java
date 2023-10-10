package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class S0APacketUseBed implements Packet {
   private BlockPos bedPos;
   private int playerID;

   public BlockPos getBedPosition() {
      return this.bedPos;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleUseBed(this);
   }

   public S0APacketUseBed() {
   }

   public S0APacketUseBed(EntityPlayer var1, BlockPos var2) {
      this.playerID = var1.getEntityId();
      this.bedPos = var2;
   }

   public EntityPlayer getPlayer(World var1) {
      return (EntityPlayer)var1.getEntityByID(this.playerID);
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeVarIntToBuffer(this.playerID);
      var1.writeBlockPos(this.bedPos);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.playerID = var1.readVarIntFromBuffer();
      this.bedPos = var1.readBlockPos();
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }
}
