package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class S21PacketChunkData implements Packet {
   private int chunkZ;
   private boolean field_149279_g;
   private int chunkX;
   private S21PacketChunkData.Extracted extractedData;

   public S21PacketChunkData() {
   }

   public S21PacketChunkData(Chunk var1, boolean var2, int var3) {
      this.chunkX = var1.xPosition;
      this.chunkZ = var1.zPosition;
      this.field_149279_g = var2;
      this.extractedData = func_179756_a(var1, var2, !var1.getWorld().provider.getHasNoSky(), var3);
   }

   public void processPacket(INetHandlerPlayClient var1) {
      var1.handleChunkData(this);
   }

   protected static int func_180737_a(int var0, boolean var1, boolean var2) {
      int var3 = var0 * 2 * 16 * 16 * 16;
      int var4 = var0 * 16 * 16 * 16 / 2;
      int var5 = var1 ? var0 * 16 * 16 * 16 / 2 : 0;
      int var6 = var2 ? 256 : 0;
      return var3 + var4 + var5 + var6;
   }

   public void writePacketData(PacketBuffer var1) throws IOException {
      var1.writeInt(this.chunkX);
      var1.writeInt(this.chunkZ);
      var1.writeBoolean(this.field_149279_g);
      var1.writeShort((short)(this.extractedData.dataSize & '\uffff'));
      var1.writeByteArray(this.extractedData.data);
   }

   public byte[] func_149272_d() {
      return this.extractedData.data;
   }

   public boolean func_149274_i() {
      return this.field_149279_g;
   }

   public void processPacket(INetHandler var1) {
      this.processPacket((INetHandlerPlayClient)var1);
   }

   public int getChunkX() {
      return this.chunkX;
   }

   public void readPacketData(PacketBuffer var1) throws IOException {
      this.chunkX = var1.readInt();
      this.chunkZ = var1.readInt();
      this.field_149279_g = var1.readBoolean();
      this.extractedData = new S21PacketChunkData.Extracted();
      this.extractedData.dataSize = var1.readShort();
      this.extractedData.data = var1.readByteArray();
   }

   private static int func_179757_a(byte[] var0, byte[] var1, int var2) {
      System.arraycopy(var0, 0, var1, var2, var0.length);
      return var2 + var0.length;
   }

   public int getExtractedSize() {
      return this.extractedData.dataSize;
   }

   public int getChunkZ() {
      return this.chunkZ;
   }

   public static S21PacketChunkData.Extracted func_179756_a(Chunk var0, boolean var1, boolean var2, int var3) {
      ExtendedBlockStorage[] var4 = var0.getBlockStorageArray();
      S21PacketChunkData.Extracted var5 = new S21PacketChunkData.Extracted();
      ArrayList var6 = Lists.newArrayList();

      int var7;
      ExtendedBlockStorage var8;
      for(var7 = 0; var7 < var4.length; ++var7) {
         var8 = var4[var7];
         if (var8 != null && (!var1 || !var8.isEmpty()) && (var3 & 1 << var7) != 0) {
            var5.dataSize |= 1 << var7;
            var6.add(var8);
         }
      }

      var5.data = new byte[func_180737_a(Integer.bitCount(var5.dataSize), var2, var1)];
      var7 = 0;
      Iterator var9 = var6.iterator();

      while(var9.hasNext()) {
         var8 = (ExtendedBlockStorage)var9.next();
         char[] var10 = var8.getData();
         char[] var14 = var10;
         int var13 = var10.length;

         for(int var12 = 0; var12 < var13; ++var12) {
            char var11 = var14[var12];
            var5.data[var7++] = (byte)(var11 & 255);
            var5.data[var7++] = (byte)(var11 >> 8 & 255);
         }
      }

      for(var9 = var6.iterator(); var9.hasNext(); var7 = func_179757_a(var8.getBlocklightArray().getData(), var5.data, var7)) {
         var8 = (ExtendedBlockStorage)var9.next();
      }

      if (var2) {
         for(var9 = var6.iterator(); var9.hasNext(); var7 = func_179757_a(var8.getSkylightArray().getData(), var5.data, var7)) {
            var8 = (ExtendedBlockStorage)var9.next();
         }
      }

      if (var1) {
         func_179757_a(var0.getBiomeArray(), var5.data, var7);
      }

      return var5;
   }

   public static class Extracted {
      public byte[] data;
      public int dataSize;
   }
}
