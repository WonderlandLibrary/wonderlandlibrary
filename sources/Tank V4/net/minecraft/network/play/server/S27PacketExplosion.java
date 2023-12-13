package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class S27PacketExplosion implements Packet {
   private float strength;
   public float field_149159_h;
   public float field_149152_f;
   private double posY;
   private double posZ;
   public float field_149153_g;
   private double posX;
   private List affectedBlockPositions;

   public double getY() {
      return this.posY;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeFloat((float)this.posX);
      var1.writeFloat((float)this.posY);
      var1.writeFloat((float)this.posZ);
      var1.writeFloat(this.strength);
      var1.writeInt(this.affectedBlockPositions.size());
      int var2 = (int)this.posX;
      int var3 = (int)this.posY;
      int var4 = (int)this.posZ;
      Iterator var6 = this.affectedBlockPositions.iterator();

      while(var6.hasNext()) {
         BlockPos var5 = (BlockPos)var6.next();
         int var7 = var5.getX() - var2;
         int var8 = var5.getY() - var3;
         int var9 = var5.getZ() - var4;
         var1.writeByte(var7);
         var1.writeByte(var8);
         var1.writeByte(var9);
      }

      var1.writeFloat(this.field_149152_f);
      var1.writeFloat(this.field_149153_g);
      var1.writeFloat(this.field_149159_h);
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.posX = (double)var1.readFloat();
      this.posY = (double)var1.readFloat();
      this.posZ = (double)var1.readFloat();
      this.strength = var1.readFloat();
      int var2 = var1.readInt();
      this.affectedBlockPositions = Lists.newArrayListWithCapacity(var2);
      int var3 = (int)this.posX;
      int var4 = (int)this.posY;
      int var5 = (int)this.posZ;

      for(int var6 = 0; var6 < var2; ++var6) {
         int var7 = var1.readByte() + var3;
         int var8 = var1.readByte() + var4;
         int var9 = var1.readByte() + var5;
         this.affectedBlockPositions.add(new BlockPos(var7, var8, var9));
      }

      this.field_149152_f = var1.readFloat();
      this.field_149153_g = var1.readFloat();
      this.field_149159_h = var1.readFloat();
   }

   public float func_149149_c() {
      return this.field_149152_f;
   }

   public float func_149147_e() {
      return this.field_149159_h;
   }

   public float getStrength() {
      return this.strength;
   }

   public S27PacketExplosion(double var1, double var3, double var5, float var7, List var8, Vec3 var9) {
      this.posX = var1;
      this.posY = var3;
      this.posZ = var5;
      this.strength = var7;
      this.affectedBlockPositions = Lists.newArrayList((Iterable)var8);
      if (var9 != null) {
         this.field_149152_f = (float)var9.xCoord;
         this.field_149153_g = (float)var9.yCoord;
         this.field_149159_h = (float)var9.zCoord;
      }

   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleExplosion(this);
   }

   public double getX() {
      return this.posX;
   }

   public float func_149144_d() {
      return this.field_149153_g;
   }

   public S27PacketExplosion() {
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public List getAffectedBlockPositions() {
      return this.affectedBlockPositions;
   }

   public double getZ() {
      return this.posZ;
   }
}
