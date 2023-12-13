package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S0EPacketSpawnObject implements Packet {
   private int field_149020_k;
   private int speedX;
   private int speedZ;
   private int y;
   private int pitch;
   private int z;
   private int x;
   private int type;
   private int speedY;
   private int yaw;
   private int entityId;

   public void setX(int var1) {
      this.x = var1;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = var1.readVarIntFromBuffer();
      this.type = var1.readByte();
      this.x = var1.readInt();
      this.y = var1.readInt();
      this.z = var1.readInt();
      this.pitch = var1.readByte();
      this.yaw = var1.readByte();
      this.field_149020_k = var1.readInt();
      if (this.field_149020_k > 0) {
         this.speedX = var1.readShort();
         this.speedY = var1.readShort();
         this.speedZ = var1.readShort();
      }

   }

   public int getX() {
      return this.x;
   }

   public S0EPacketSpawnObject(Entity var1, int var2, int var3) {
      this.entityId = var1.getEntityId();
      this.x = MathHelper.floor_double(var1.posX * 32.0D);
      this.y = MathHelper.floor_double(var1.posY * 32.0D);
      this.z = MathHelper.floor_double(var1.posZ * 32.0D);
      this.pitch = MathHelper.floor_float(var1.rotationPitch * 256.0F / 360.0F);
      this.yaw = MathHelper.floor_float(var1.rotationYaw * 256.0F / 360.0F);
      this.type = var2;
      this.field_149020_k = var3;
      if (var3 > 0) {
         double var4 = var1.motionX;
         double var6 = var1.motionY;
         double var8 = var1.motionZ;
         double var10 = 3.9D;
         if (var4 < -var10) {
            var4 = -var10;
         }

         if (var6 < -var10) {
            var6 = -var10;
         }

         if (var8 < -var10) {
            var8 = -var10;
         }

         if (var4 > var10) {
            var4 = var10;
         }

         if (var6 > var10) {
            var6 = var10;
         }

         if (var8 > var10) {
            var8 = var10;
         }

         this.speedX = (int)(var4 * 8000.0D);
         this.speedY = (int)(var6 * 8000.0D);
         this.speedZ = (int)(var8 * 8000.0D);
      }

   }

   public int getEntityID() {
      return this.entityId;
   }

   public void setZ(int var1) {
      this.z = var1;
   }

   public S0EPacketSpawnObject() {
   }

   public void func_149002_g(int var1) {
      this.field_149020_k = var1;
   }

   public void setSpeedZ(int var1) {
      this.speedZ = var1;
   }

   public int getSpeedY() {
      return this.speedY;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeVarIntToBuffer(this.entityId);
      var1.writeByte(this.type);
      var1.writeInt(this.x);
      var1.writeInt(this.y);
      var1.writeInt(this.z);
      var1.writeByte(this.pitch);
      var1.writeByte(this.yaw);
      var1.writeInt(this.field_149020_k);
      if (this.field_149020_k > 0) {
         var1.writeShort(this.speedX);
         var1.writeShort(this.speedY);
         var1.writeShort(this.speedZ);
      }

   }

   public int getYaw() {
      return this.yaw;
   }

   public void setSpeedY(int var1) {
      this.speedY = var1;
   }

   public int getY() {
      return this.y;
   }

   public void setSpeedX(int var1) {
      this.speedX = var1;
   }

   public int getType() {
      return this.type;
   }

   public void setY(int var1) {
      this.y = var1;
   }

   public int getSpeedZ() {
      return this.speedZ;
   }

   public int getPitch() {
      return this.pitch;
   }

   public int getSpeedX() {
      return this.speedX;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleSpawnObject(this);
   }

   public int getZ() {
      return this.z;
   }

   public int func_149009_m() {
      return this.field_149020_k;
   }

   public S0EPacketSpawnObject(Entity var1, int var2) {
      this(var1, var2, 0);
   }
}
