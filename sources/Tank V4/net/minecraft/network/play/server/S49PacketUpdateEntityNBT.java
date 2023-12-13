package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class S49PacketUpdateEntityNBT implements Packet {
   private int entityId;
   private NBTTagCompound tagCompound;

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeVarIntToBuffer(this.entityId);
      var1.writeNBTTagCompoundToBuffer(this.tagCompound);
   }

   public Entity getEntity(World var1) {
      return var1.getEntityByID(this.entityId);
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleEntityNBT(this);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = var1.readVarIntFromBuffer();
      this.tagCompound = var1.readNBTTagCompoundFromBuffer();
   }

   public S49PacketUpdateEntityNBT(int var1, NBTTagCompound var2) {
      this.entityId = var1;
      this.tagCompound = var2;
   }

   public NBTTagCompound getTagCompound() {
      return this.tagCompound;
   }

   public S49PacketUpdateEntityNBT() {
   }
}
