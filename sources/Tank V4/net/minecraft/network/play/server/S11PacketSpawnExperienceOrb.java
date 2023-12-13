package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S11PacketSpawnExperienceOrb implements Packet {
   private int xpValue;
   private int posY;
   private int posX;
   private int posZ;
   private int entityID;

   public int getXPValue() {
      return this.xpValue;
   }

   public int getY() {
      return this.posY;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeVarIntToBuffer(this.entityID);
      var1.writeInt(this.posX);
      var1.writeInt(this.posY);
      var1.writeInt(this.posZ);
      var1.writeShort(this.xpValue);
   }

   public int getZ() {
      return this.posZ;
   }

   public int getEntityID() {
      return this.entityID;
   }

   public S11PacketSpawnExperienceOrb() {
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleSpawnExperienceOrb(this);
   }

   public S11PacketSpawnExperienceOrb(EntityXPOrb var1) {
      this.entityID = var1.getEntityId();
      this.posX = MathHelper.floor_double(var1.posX * 32.0D);
      this.posY = MathHelper.floor_double(var1.posY * 32.0D);
      this.posZ = MathHelper.floor_double(var1.posZ * 32.0D);
      this.xpValue = var1.getXpValue();
   }

   public int getX() {
      return this.posX;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityID = var1.readVarIntFromBuffer();
      this.posX = var1.readInt();
      this.posY = var1.readInt();
      this.posZ = var1.readInt();
      this.xpValue = var1.readShort();
   }
}
