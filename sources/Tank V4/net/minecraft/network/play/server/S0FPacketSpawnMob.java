package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S0FPacketSpawnMob implements Packet {
   private byte headPitch;
   private int type;
   private int velocityZ;
   private int z;
   private List watcher;
   private byte yaw;
   private int velocityX;
   private int entityId;
   private int velocityY;
   private int y;
   private int x;
   private byte pitch;
   private DataWatcher field_149043_l;

   public S0FPacketSpawnMob() {
   }

   public S0FPacketSpawnMob(EntityLivingBase var1) {
      this.entityId = var1.getEntityId();
      this.type = (byte)EntityList.getEntityID(var1);
      this.x = MathHelper.floor_double(var1.posX * 32.0D);
      this.y = MathHelper.floor_double(var1.posY * 32.0D);
      this.z = MathHelper.floor_double(var1.posZ * 32.0D);
      this.yaw = (byte)((int)(var1.rotationYaw * 256.0F / 360.0F));
      this.pitch = (byte)((int)(var1.rotationPitch * 256.0F / 360.0F));
      this.headPitch = (byte)((int)(var1.rotationYawHead * 256.0F / 360.0F));
      double var2 = 3.9D;
      double var4 = var1.motionX;
      double var6 = var1.motionY;
      double var8 = var1.motionZ;
      if (var4 < -var2) {
         var4 = -var2;
      }

      if (var6 < -var2) {
         var6 = -var2;
      }

      if (var8 < -var2) {
         var8 = -var2;
      }

      if (var4 > var2) {
         var4 = var2;
      }

      if (var6 > var2) {
         var6 = var2;
      }

      if (var8 > var2) {
         var8 = var2;
      }

      this.velocityX = (int)(var4 * 8000.0D);
      this.velocityY = (int)(var6 * 8000.0D);
      this.velocityZ = (int)(var8 * 8000.0D);
      this.field_149043_l = var1.getDataWatcher();
   }

   public int getZ() {
      return this.z;
   }

   public int getEntityID() {
      return this.entityId;
   }

   public int getVelocityX() {
      return this.velocityX;
   }

   public int getVelocityZ() {
      return this.velocityZ;
   }

   public byte getYaw() {
      return this.yaw;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleSpawnMob(this);
   }

   public int getY() {
      return this.y;
   }

   public List func_149027_c() {
      if (this.watcher == null) {
         this.watcher = this.field_149043_l.getAllWatched();
      }

      return this.watcher;
   }

   public byte getHeadPitch() {
      return this.headPitch;
   }

   public int getVelocityY() {
      return this.velocityY;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeVarIntToBuffer(this.entityId);
      var1.writeByte(this.type & 255);
      var1.writeInt(this.x);
      var1.writeInt(this.y);
      var1.writeInt(this.z);
      var1.writeByte(this.yaw);
      var1.writeByte(this.pitch);
      var1.writeByte(this.headPitch);
      var1.writeShort(this.velocityX);
      var1.writeShort(this.velocityY);
      var1.writeShort(this.velocityZ);
      this.field_149043_l.writeTo(var1);
   }

   public int getEntityType() {
      return this.type;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = var1.readVarIntFromBuffer();
      this.type = var1.readByte() & 255;
      this.x = var1.readInt();
      this.y = var1.readInt();
      this.z = var1.readInt();
      this.yaw = var1.readByte();
      this.pitch = var1.readByte();
      this.headPitch = var1.readByte();
      this.velocityX = var1.readShort();
      this.velocityY = var1.readShort();
      this.velocityZ = var1.readShort();
      this.watcher = DataWatcher.readWatchedListFromPacketBuffer(var1);
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public byte getPitch() {
      return this.pitch;
   }

   public int getX() {
      return this.x;
   }
}
