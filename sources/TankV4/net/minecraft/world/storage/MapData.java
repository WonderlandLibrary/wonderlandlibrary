package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S34PacketMaps;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec4b;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class MapData extends WorldSavedData {
   public Map mapDecorations = Maps.newLinkedHashMap();
   public List playersArrayList = Lists.newArrayList();
   public byte scale;
   public int zCenter;
   public byte dimension;
   private Map playersHashMap = Maps.newHashMap();
   public int xCenter;
   public byte[] colors = new byte[16384];

   private void updateDecorations(int var1, World var2, String var3, double var4, double var6, double var8) {
      int var10 = 1 << this.scale;
      float var11 = (float)(var4 - (double)this.xCenter) / (float)var10;
      float var12 = (float)(var6 - (double)this.zCenter) / (float)var10;
      byte var13 = (byte)((int)((double)(var11 * 2.0F) + 0.5D));
      byte var14 = (byte)((int)((double)(var12 * 2.0F) + 0.5D));
      byte var15 = 63;
      byte var16;
      if (var11 >= (float)(-var15) && var12 >= (float)(-var15) && var11 <= (float)var15 && var12 <= (float)var15) {
         var8 += var8 < 0.0D ? -8.0D : 8.0D;
         var16 = (byte)((int)(var8 * 16.0D / 360.0D));
         if (this.dimension < 0) {
            int var17 = (int)(var2.getWorldInfo().getWorldTime() / 10L);
            var16 = (byte)(var17 * var17 * 34187121 + var17 * 121 >> 15 & 15);
         }
      } else {
         if (Math.abs(var11) >= 320.0F || Math.abs(var12) >= 320.0F) {
            this.mapDecorations.remove(var3);
            return;
         }

         var1 = 6;
         var16 = 0;
         if (var11 <= (float)(-var15)) {
            var13 = (byte)((int)((double)(var15 * 2) + 2.5D));
         }

         if (var12 <= (float)(-var15)) {
            var14 = (byte)((int)((double)(var15 * 2) + 2.5D));
         }

         if (var11 >= (float)var15) {
            var13 = (byte)(var15 * 2 + 1);
         }

         if (var12 >= (float)var15) {
            var14 = (byte)(var15 * 2 + 1);
         }
      }

      this.mapDecorations.put(var3, new Vec4b((byte)var1, var13, var14, var16));
   }

   public Packet getMapPacket(ItemStack var1, World var2, EntityPlayer var3) {
      MapData.MapInfo var4 = (MapData.MapInfo)this.playersHashMap.get(var3);
      return var4 == null ? null : var4.getPacket(var1);
   }

   public MapData.MapInfo getMapInfo(EntityPlayer var1) {
      MapData.MapInfo var2 = (MapData.MapInfo)this.playersHashMap.get(var1);
      if (var2 == null) {
         var2 = new MapData.MapInfo(this, var1);
         this.playersHashMap.put(var1, var2);
         this.playersArrayList.add(var2);
      }

      return var2;
   }

   public void writeToNBT(NBTTagCompound var1) {
      var1.setByte("dimension", this.dimension);
      var1.setInteger("xCenter", this.xCenter);
      var1.setInteger("zCenter", this.zCenter);
      var1.setByte("scale", this.scale);
      var1.setShort("width", (short)128);
      var1.setShort("height", (short)128);
      var1.setByteArray("colors", this.colors);
   }

   public void readFromNBT(NBTTagCompound var1) {
      this.dimension = var1.getByte("dimension");
      this.xCenter = var1.getInteger("xCenter");
      this.zCenter = var1.getInteger("zCenter");
      this.scale = var1.getByte("scale");
      this.scale = (byte)MathHelper.clamp_int(this.scale, 0, 4);
      short var2 = var1.getShort("width");
      short var3 = var1.getShort("height");
      if (var2 == 128 && var3 == 128) {
         this.colors = var1.getByteArray("colors");
      } else {
         byte[] var4 = var1.getByteArray("colors");
         this.colors = new byte[16384];
         int var5 = (128 - var2) / 2;
         int var6 = (128 - var3) / 2;

         for(int var7 = 0; var7 < var3; ++var7) {
            int var8 = var7 + var6;
            if (var8 >= 0 || var8 < 128) {
               for(int var9 = 0; var9 < var2; ++var9) {
                  int var10 = var9 + var5;
                  if (var10 >= 0 || var10 < 128) {
                     this.colors[var10 + var8 * 128] = var4[var9 + var7 * var2];
                  }
               }
            }
         }
      }

   }

   public void calculateMapCenter(double var1, double var3, int var5) {
      int var6 = 128 * (1 << var5);
      int var7 = MathHelper.floor_double((var1 + 64.0D) / (double)var6);
      int var8 = MathHelper.floor_double((var3 + 64.0D) / (double)var6);
      this.xCenter = var7 * var6 + var6 / 2 - 64;
      this.zCenter = var8 * var6 + var6 / 2 - 64;
   }

   public void updateMapData(int var1, int var2) {
      super.markDirty();
      Iterator var4 = this.playersArrayList.iterator();

      while(var4.hasNext()) {
         MapData.MapInfo var3 = (MapData.MapInfo)var4.next();
         var3.update(var1, var2);
      }

   }

   public void updateVisiblePlayers(EntityPlayer var1, ItemStack var2) {
      if (!this.playersHashMap.containsKey(var1)) {
         MapData.MapInfo var3 = new MapData.MapInfo(this, var1);
         this.playersHashMap.put(var1, var3);
         this.playersArrayList.add(var3);
      }

      if (!var1.inventory.hasItemStack(var2)) {
         this.mapDecorations.remove(var1.getName());
      }

      for(int var6 = 0; var6 < this.playersArrayList.size(); ++var6) {
         MapData.MapInfo var4 = (MapData.MapInfo)this.playersArrayList.get(var6);
         if (var4.entityplayerObj.isDead || !var4.entityplayerObj.inventory.hasItemStack(var2) && !var2.isOnItemFrame()) {
            this.playersHashMap.remove(var4.entityplayerObj);
            this.playersArrayList.remove(var4);
         } else if (!var2.isOnItemFrame() && var4.entityplayerObj.dimension == this.dimension) {
            this.updateDecorations(0, var4.entityplayerObj.worldObj, var4.entityplayerObj.getName(), var4.entityplayerObj.posX, var4.entityplayerObj.posZ, (double)var4.entityplayerObj.rotationYaw);
         }
      }

      if (var2.isOnItemFrame()) {
         EntityItemFrame var7 = var2.getItemFrame();
         BlockPos var8 = var7.getHangingPosition();
         this.updateDecorations(1, var1.worldObj, "frame-" + var7.getEntityId(), (double)var8.getX(), (double)var8.getZ(), (double)(var7.facingDirection.getHorizontalIndex() * 90));
      }

      if (var2.hasTagCompound() && var2.getTagCompound().hasKey("Decorations", 9)) {
         NBTTagList var9 = var2.getTagCompound().getTagList("Decorations", 10);

         for(int var10 = 0; var10 < var9.tagCount(); ++var10) {
            NBTTagCompound var5 = var9.getCompoundTagAt(var10);
            if (!this.mapDecorations.containsKey(var5.getString("id"))) {
               this.updateDecorations(var5.getByte("type"), var1.worldObj, var5.getString("id"), var5.getDouble("x"), var5.getDouble("z"), var5.getDouble("rot"));
            }
         }
      }

   }

   public MapData(String var1) {
      super(var1);
   }

   public class MapInfo {
      private boolean field_176105_d;
      private int field_176109_i;
      private int minX;
      private int minY;
      private int maxY;
      private int maxX;
      final MapData this$0;
      public final EntityPlayer entityplayerObj;
      public int field_82569_d;

      public MapInfo(MapData var1, EntityPlayer var2) {
         this.this$0 = var1;
         this.field_176105_d = true;
         this.minX = 0;
         this.minY = 0;
         this.maxX = 127;
         this.maxY = 127;
         this.entityplayerObj = var2;
      }

      public Packet getPacket(ItemStack var1) {
         if (this.field_176105_d) {
            this.field_176105_d = false;
            return new S34PacketMaps(var1.getMetadata(), this.this$0.scale, this.this$0.mapDecorations.values(), this.this$0.colors, this.minX, this.minY, this.maxX + 1 - this.minX, this.maxY + 1 - this.minY);
         } else {
            return this.field_176109_i++ % 5 == 0 ? new S34PacketMaps(var1.getMetadata(), this.this$0.scale, this.this$0.mapDecorations.values(), this.this$0.colors, 0, 0, 0, 0) : null;
         }
      }

      public void update(int var1, int var2) {
         if (this.field_176105_d) {
            this.minX = Math.min(this.minX, var1);
            this.minY = Math.min(this.minY, var2);
            this.maxX = Math.max(this.maxX, var1);
            this.maxY = Math.max(this.maxY, var2);
         } else {
            this.field_176105_d = true;
            this.minX = var1;
            this.minY = var2;
            this.maxX = var1;
            this.maxY = var2;
         }

      }
   }
}
