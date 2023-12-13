package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class EntityPainting extends EntityHanging {
   public EntityPainting.EnumArt art;

   public EntityPainting(World var1, BlockPos var2, EnumFacing var3, String var4) {
      this(var1, var2, var3);
      EntityPainting.EnumArt[] var8;
      int var7 = (var8 = EntityPainting.EnumArt.values()).length;

      for(int var6 = 0; var6 < var7; ++var6) {
         EntityPainting.EnumArt var5 = var8[var6];
         if (var5.title.equals(var4)) {
            this.art = var5;
            break;
         }
      }

      this.updateFacingWithBoundingBox(var3);
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      var1.setString("Motive", this.art.title);
      super.writeEntityToNBT(var1);
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      String var2 = var1.getString("Motive");
      EntityPainting.EnumArt[] var6;
      int var5 = (var6 = EntityPainting.EnumArt.values()).length;

      for(int var4 = 0; var4 < var5; ++var4) {
         EntityPainting.EnumArt var3 = var6[var4];
         if (var3.title.equals(var2)) {
            this.art = var3;
         }
      }

      if (this.art == null) {
         this.art = EntityPainting.EnumArt.KEBAB;
      }

      super.readEntityFromNBT(var1);
   }

   public void setLocationAndAngles(double var1, double var3, double var5, float var7, float var8) {
      BlockPos var9 = this.hangingPosition.add(var1 - this.posX, var3 - this.posY, var5 - this.posZ);
      this.setPosition((double)var9.getX(), (double)var9.getY(), (double)var9.getZ());
   }

   public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9, boolean var10) {
      BlockPos var11 = this.hangingPosition.add(var1 - this.posX, var3 - this.posY, var5 - this.posZ);
      this.setPosition((double)var11.getX(), (double)var11.getY(), (double)var11.getZ());
   }

   public void onBroken(Entity var1) {
      if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
         if (var1 instanceof EntityPlayer) {
            EntityPlayer var2 = (EntityPlayer)var1;
            if (var2.capabilities.isCreativeMode) {
               return;
            }
         }

         this.entityDropItem(new ItemStack(Items.painting), 0.0F);
      }

   }

   public int getWidthPixels() {
      return this.art.sizeX;
   }

   public EntityPainting(World var1) {
      super(var1);
   }

   public EntityPainting(World var1, BlockPos var2, EnumFacing var3) {
      super(var1, var2);
      ArrayList var4 = Lists.newArrayList();
      EntityPainting.EnumArt[] var8;
      int var7 = (var8 = EntityPainting.EnumArt.values()).length;

      for(int var6 = 0; var6 < var7; ++var6) {
         EntityPainting.EnumArt var5 = var8[var6];
         this.art = var5;
         this.updateFacingWithBoundingBox(var3);
         if (this.onValidSurface()) {
            var4.add(var5);
         }
      }

      if (!var4.isEmpty()) {
         this.art = (EntityPainting.EnumArt)var4.get(this.rand.nextInt(var4.size()));
      }

      this.updateFacingWithBoundingBox(var3);
   }

   public int getHeightPixels() {
      return this.art.sizeY;
   }

   public static enum EnumArt {
      PIGSCENE("Pigscene", 64, 64, 64, 192),
      MATCH("Match", 32, 32, 0, 128),
      DONKEY_KONG("DonkeyKong", 64, 48, 192, 112),
      AZTEC("Aztec", 16, 16, 16, 0);

      private static final EntityPainting.EnumArt[] ENUM$VALUES = new EntityPainting.EnumArt[]{KEBAB, AZTEC, ALBAN, AZTEC_2, BOMB, PLANT, WASTELAND, POOL, COURBET, SEA, SUNSET, CREEBET, WANDERER, GRAHAM, MATCH, BUST, STAGE, VOID, SKULL_AND_ROSES, WITHER, FIGHTERS, POINTER, PIGSCENE, BURNING_SKULL, SKELETON, DONKEY_KONG};
      BOMB("Bomb", 16, 16, 64, 0),
      POOL("Pool", 32, 16, 0, 32),
      SUNSET("Sunset", 32, 16, 96, 32),
      POINTER("Pointer", 64, 64, 0, 192),
      GRAHAM("Graham", 16, 32, 16, 64),
      SEA("Sea", 32, 16, 64, 32),
      ALBAN("Alban", 16, 16, 32, 0),
      AZTEC_2("Aztec2", 16, 16, 48, 0),
      BUST("Bust", 32, 32, 32, 128),
      WASTELAND("Wasteland", 16, 16, 96, 0),
      CREEBET("Creebet", 32, 16, 128, 32),
      SKELETON("Skeleton", 64, 48, 192, 64);

      public final String title;
      WITHER("Wither", 32, 32, 160, 128);

      public static final int field_180001_A = "SkullAndRoses".length();
      KEBAB("Kebab", 16, 16, 0, 0);

      public final int sizeY;
      public final int offsetY;
      public final int sizeX;
      PLANT("Plant", 16, 16, 80, 0),
      WANDERER("Wanderer", 16, 32, 0, 64),
      VOID("Void", 32, 32, 96, 128),
      FIGHTERS("Fighters", 64, 32, 0, 96),
      BURNING_SKULL("BurningSkull", 64, 64, 128, 192),
      STAGE("Stage", 32, 32, 64, 128);

      public final int offsetX;
      SKULL_AND_ROSES("SkullAndRoses", 32, 32, 128, 128),
      COURBET("Courbet", 32, 16, 32, 32);

      private EnumArt(String var3, int var4, int var5, int var6, int var7) {
         this.title = var3;
         this.sizeX = var4;
         this.sizeY = var5;
         this.offsetX = var6;
         this.offsetY = var7;
      }
   }
}
