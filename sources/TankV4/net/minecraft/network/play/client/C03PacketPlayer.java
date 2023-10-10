package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C03PacketPlayer implements Packet {
   public double y;
   public double x;
   public float pitch;
   public boolean rotating;
   public boolean onGround;
   public double z;
   public float yaw;
   public boolean moving;

   public float getYaw() {
      return this.yaw;
   }

   public double getPositionY() {
      return this.y;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayServer)var1);
   }

   public float getPitch() {
      return this.pitch;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.onGround = var1.readUnsignedByte() != 0;
   }

   public void setMoving(boolean var1) {
      this.moving = var1;
   }

   public double getPositionZ() {
      return this.z;
   }

   public boolean isOnGround() {
      return this.onGround;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeByte(this.onGround ? 1 : 0);
   }

   public double getPositionX() {
      return this.x;
   }

   public boolean getRotating() {
      return this.rotating;
   }

   public boolean isMoving() {
      return this.moving;
   }

   public void processPacket(INetHandlerPlayServer var1) {
      var1.processPlayer(this);
   }

   public C03PacketPlayer(boolean var1) {
      this.onGround = var1;
   }

   public C03PacketPlayer() {
   }

   public static class C05PacketPlayerLook extends C03PacketPlayer {
      public C05PacketPlayerLook(float var1, float var2, boolean var3) {
         this.yaw = var1;
         this.pitch = var2;
         this.onGround = var3;
         this.rotating = true;
      }

      public C05PacketPlayerLook() {
         this.rotating = true;
      }

      public void readPacketData(PacketBuffer var1) throws IOException {
         this.yaw = var1.readFloat();
         this.pitch = var1.readFloat();
         super.readPacketData(var1);
      }

      public void writePacketData(PacketBuffer var1) throws IOException {
         var1.writeFloat(this.yaw);
         var1.writeFloat(this.pitch);
         super.writePacketData(var1);
      }
   }

   public static class C06PacketPlayerPosLook extends C03PacketPlayer {
      public void writePacketData(PacketBuffer var1) throws IOException {
         var1.writeDouble(this.x);
         var1.writeDouble(this.y);
         var1.writeDouble(this.z);
         var1.writeFloat(this.yaw);
         var1.writeFloat(this.pitch);
         super.writePacketData(var1);
      }

      public void readPacketData(PacketBuffer var1) throws IOException {
         this.x = var1.readDouble();
         this.y = var1.readDouble();
         this.z = var1.readDouble();
         this.yaw = var1.readFloat();
         this.pitch = var1.readFloat();
         super.readPacketData(var1);
      }

      public C06PacketPlayerPosLook() {
         this.moving = true;
         this.rotating = true;
      }

      public C06PacketPlayerPosLook(double var1, double var3, double var5, float var7, float var8, boolean var9) {
         this.x = var1;
         this.y = var3;
         this.z = var5;
         this.yaw = var7;
         this.pitch = var8;
         this.onGround = var9;
         this.rotating = true;
         this.moving = true;
      }
   }

   public static class C04PacketPlayerPosition extends C03PacketPlayer {
      public C04PacketPlayerPosition() {
         this.moving = true;
      }

      public void readPacketData(PacketBuffer var1) throws IOException {
         this.x = var1.readDouble();
         this.y = var1.readDouble();
         this.z = var1.readDouble();
         super.readPacketData(var1);
      }

      public void writePacketData(PacketBuffer var1) throws IOException {
         var1.writeDouble(this.x);
         var1.writeDouble(this.y);
         var1.writeDouble(this.z);
         super.writePacketData(var1);
      }

      public C04PacketPlayerPosition(double var1, double var3, double var5, boolean var7) {
         this.x = var1;
         this.y = var3;
         this.z = var5;
         this.onGround = var7;
         this.moving = true;
      }
   }
}
