package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class S14PacketEntity implements Packet {
   protected byte posX;
   protected byte yaw;
   protected int entityId;
   protected boolean field_149069_g;
   protected byte posY;
   protected boolean onGround;
   protected byte posZ;
   protected byte pitch;

   public String toString() {
      return "Entity_" + super.toString();
   }

   public Entity getEntity(World var1) {
      return var1.getEntityByID(this.entityId);
   }

   public boolean getOnGround() {
      return this.onGround;
   }

   public byte func_149062_c() {
      return this.posX;
   }

   public byte func_149061_d() {
      return this.posY;
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleEntityMovement(this);
   }

   public byte func_149063_g() {
      return this.pitch;
   }

   public byte func_149066_f() {
      return this.yaw;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.entityId = var1.readVarIntFromBuffer();
   }

   public S14PacketEntity() {
   }

   public boolean func_149060_h() {
      return this.field_149069_g;
   }

   public byte func_149064_e() {
      return this.posZ;
   }

   public S14PacketEntity(int var1) {
      this.entityId = var1;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeVarIntToBuffer(this.entityId);
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public static class S15PacketEntityRelMove extends S14PacketEntity {
      public S15PacketEntityRelMove() {
      }

      public void writePacketData(PacketBuffer var1) throws IOException {
         super.writePacketData(var1);
         var1.writeByte(this.posX);
         var1.writeByte(this.posY);
         var1.writeByte(this.posZ);
         var1.writeBoolean(this.onGround);
      }

      public S15PacketEntityRelMove(int var1, byte var2, byte var3, byte var4, boolean var5) {
         super(var1);
         this.posX = var2;
         this.posY = var3;
         this.posZ = var4;
         this.onGround = var5;
      }

      public void readPacketData(PacketBuffer var1) throws IOException {
         super.readPacketData(var1);
         this.posX = var1.readByte();
         this.posY = var1.readByte();
         this.posZ = var1.readByte();
         this.onGround = var1.readBoolean();
      }
   }

   public static class S16PacketEntityLook extends S14PacketEntity {
      public S16PacketEntityLook(int var1, byte var2, byte var3, boolean var4) {
         super(var1);
         this.yaw = var2;
         this.pitch = var3;
         this.field_149069_g = true;
         this.onGround = var4;
      }

      public S16PacketEntityLook() {
         this.field_149069_g = true;
      }

      public void readPacketData(PacketBuffer var1) throws IOException {
         super.readPacketData(var1);
         this.yaw = var1.readByte();
         this.pitch = var1.readByte();
         this.onGround = var1.readBoolean();
      }

      public void writePacketData(PacketBuffer var1) throws IOException {
         super.writePacketData(var1);
         var1.writeByte(this.yaw);
         var1.writeByte(this.pitch);
         var1.writeBoolean(this.onGround);
      }
   }

   public static class S17PacketEntityLookMove extends S14PacketEntity {
      public S17PacketEntityLookMove(int var1, byte var2, byte var3, byte var4, byte var5, byte var6, boolean var7) {
         super(var1);
         this.posX = var2;
         this.posY = var3;
         this.posZ = var4;
         this.yaw = var5;
         this.pitch = var6;
         this.onGround = var7;
         this.field_149069_g = true;
      }

      public S17PacketEntityLookMove() {
         this.field_149069_g = true;
      }

      public void writePacketData(PacketBuffer var1) throws IOException {
         super.writePacketData(var1);
         var1.writeByte(this.posX);
         var1.writeByte(this.posY);
         var1.writeByte(this.posZ);
         var1.writeByte(this.yaw);
         var1.writeByte(this.pitch);
         var1.writeBoolean(this.onGround);
      }

      public void readPacketData(PacketBuffer var1) throws IOException {
         super.readPacketData(var1);
         this.posX = var1.readByte();
         this.posY = var1.readByte();
         this.posZ = var1.readByte();
         this.yaw = var1.readByte();
         this.pitch = var1.readByte();
         this.onGround = var1.readBoolean();
      }
   }
}
