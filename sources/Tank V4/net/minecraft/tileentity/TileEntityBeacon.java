package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;

public class TileEntityBeacon extends TileEntityLockable implements ITickable, IInventory {
   private float field_146014_j;
   private long beamRenderCounter;
   private int primaryEffect;
   private ItemStack payment;
   private int secondaryEffect;
   private boolean isComplete;
   private String customName;
   public static final Potion[][] effectsList;
   private final List beamSegments = Lists.newArrayList();
   private int levels = -1;

   public boolean isUseableByPlayer(EntityPlayer var1) {
      return this.worldObj.getTileEntity(this.pos) != this ? false : var1.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
   }

   public double getMaxRenderDistanceSquared() {
      return 65536.0D;
   }

   public String getGuiID() {
      return "minecraft:beacon";
   }

   public void closeInventory(EntityPlayer var1) {
   }

   public void update() {
      if (this.worldObj.getTotalWorldTime() % 80L == 0L) {
         this.updateBeacon();
      }

   }

   public float shouldBeamRender() {
      if (!this.isComplete) {
         return 0.0F;
      } else {
         int var1 = (int)(this.worldObj.getTotalWorldTime() - this.beamRenderCounter);
         this.beamRenderCounter = this.worldObj.getTotalWorldTime();
         if (var1 > 1) {
            this.field_146014_j -= (float)var1 / 40.0F;
            if (this.field_146014_j < 0.0F) {
               this.field_146014_j = 0.0F;
            }
         }

         this.field_146014_j += 0.025F;
         if (this.field_146014_j > 1.0F) {
            this.field_146014_j = 1.0F;
         }

         return this.field_146014_j;
      }
   }

   private void updateSegmentColors() {
      int var1 = this.levels;
      int var2 = this.pos.getX();
      int var3 = this.pos.getY();
      int var4 = this.pos.getZ();
      this.levels = 0;
      this.beamSegments.clear();
      this.isComplete = true;
      TileEntityBeacon.BeamSegment var5 = new TileEntityBeacon.BeamSegment(EntitySheep.func_175513_a(EnumDyeColor.WHITE));
      this.beamSegments.add(var5);
      boolean var6 = true;
      BlockPos.MutableBlockPos var7 = new BlockPos.MutableBlockPos();

      int var8;
      for(var8 = var3 + 1; var8 < 256; ++var8) {
         IBlockState var9 = this.worldObj.getBlockState(var7.func_181079_c(var2, var8, var4));
         float[] var10;
         if (var9.getBlock() == Blocks.stained_glass) {
            var10 = EntitySheep.func_175513_a((EnumDyeColor)var9.getValue(BlockStainedGlass.COLOR));
         } else {
            if (var9.getBlock() != Blocks.stained_glass_pane) {
               if (var9.getBlock().getLightOpacity() >= 15 && var9.getBlock() != Blocks.bedrock) {
                  this.isComplete = false;
                  this.beamSegments.clear();
                  break;
               }

               var5.incrementHeight();
               continue;
            }

            var10 = EntitySheep.func_175513_a((EnumDyeColor)var9.getValue(BlockStainedGlassPane.COLOR));
         }

         if (!var6) {
            var10 = new float[]{(var5.getColors()[0] + var10[0]) / 2.0F, (var5.getColors()[1] + var10[1]) / 2.0F, (var5.getColors()[2] + var10[2]) / 2.0F};
         }

         if (Arrays.equals(var10, var5.getColors())) {
            var5.incrementHeight();
         } else {
            var5 = new TileEntityBeacon.BeamSegment(var10);
            this.beamSegments.add(var5);
         }

         var6 = false;
      }

      if (this.isComplete) {
         for(var8 = 1; var8 <= 4; this.levels = var8++) {
            int var14 = var3 - var8;
            if (var14 < 0) {
               break;
            }

            boolean var17 = true;

            for(int var11 = var2 - var8; var11 <= var2 + var8 && var17; ++var11) {
               for(int var12 = var4 - var8; var12 <= var4 + var8; ++var12) {
                  Block var13 = this.worldObj.getBlockState(new BlockPos(var11, var14, var12)).getBlock();
                  if (var13 != Blocks.emerald_block && var13 != Blocks.gold_block && var13 != Blocks.diamond_block && var13 != Blocks.iron_block) {
                     var17 = false;
                     break;
                  }
               }
            }

            if (!var17) {
               break;
            }
         }

         if (this.levels == 0) {
            this.isComplete = false;
         }
      }

      if (!this.worldObj.isRemote && this.levels == 4 && var1 < this.levels) {
         Iterator var16 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, (new AxisAlignedBB((double)var2, (double)var3, (double)var4, (double)var2, (double)(var3 - 4), (double)var4)).expand(10.0D, 5.0D, 10.0D)).iterator();

         while(var16.hasNext()) {
            EntityPlayer var15 = (EntityPlayer)var16.next();
            var15.triggerAchievement(AchievementList.fullBeacon);
         }
      }

   }

   public ItemStack removeStackFromSlot(int var1) {
      if (var1 == 0 && this.payment != null) {
         ItemStack var2 = this.payment;
         this.payment = null;
         return var2;
      } else {
         return null;
      }
   }

   public void clear() {
      this.payment = null;
   }

   private int func_183001_h(int var1) {
      if (var1 >= 0 && var1 < Potion.potionTypes.length && Potion.potionTypes[var1] != null) {
         Potion var2 = Potion.potionTypes[var1];
         return var2 != Potion.moveSpeed && var2 != Potion.digSpeed && var2 != Potion.resistance && var2 != Potion.jump && var2 != Potion.damageBoost && var2 != Potion.regeneration ? 0 : var1;
      } else {
         return 0;
      }
   }

   public String getName() {
      return this != null ? this.customName : "container.beacon";
   }

   public int getInventoryStackLimit() {
      return 1;
   }

   public void setInventorySlotContents(int var1, ItemStack var2) {
      if (var1 == 0) {
         this.payment = var2;
      }

   }

   public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
      return new ContainerBeacon(var1, this);
   }

   public void setName(String var1) {
      this.customName = var1;
   }

   public ItemStack decrStackSize(int var1, int var2) {
      if (var1 == 0 && this.payment != null) {
         if (var2 >= this.payment.stackSize) {
            ItemStack var3 = this.payment;
            this.payment = null;
            return var3;
         } else {
            ItemStack var10000 = this.payment;
            var10000.stackSize -= var2;
            return new ItemStack(this.payment.getItem(), var2, this.payment.getMetadata());
         }
      } else {
         return null;
      }
   }

   public void openInventory(EntityPlayer var1) {
   }

   public boolean receiveClientEvent(int var1, int var2) {
      if (var1 == 1) {
         this.updateBeacon();
         return true;
      } else {
         return super.receiveClientEvent(var1, var2);
      }
   }

   public int getFieldCount() {
      return 3;
   }

   public ItemStack getStackInSlot(int var1) {
      return var1 == 0 ? this.payment : null;
   }

   public void setField(int var1, int var2) {
      switch(var1) {
      case 0:
         this.levels = var2;
         break;
      case 1:
         this.primaryEffect = this.func_183001_h(var2);
         break;
      case 2:
         this.secondaryEffect = this.func_183001_h(var2);
      }

   }

   static {
      effectsList = new Potion[][]{{Potion.moveSpeed, Potion.digSpeed}, {Potion.resistance, Potion.jump}, {Potion.damageBoost}, {Potion.regeneration}};
   }

   public void updateBeacon() {
      this.updateSegmentColors();
      this.addEffectsToPlayers();
   }

   public void writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(var1);
      var1.setInteger("Primary", this.primaryEffect);
      var1.setInteger("Secondary", this.secondaryEffect);
      var1.setInteger("Levels", this.levels);
   }

   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(var1);
      this.primaryEffect = this.func_183001_h(var1.getInteger("Primary"));
      this.secondaryEffect = this.func_183001_h(var1.getInteger("Secondary"));
      this.levels = var1.getInteger("Levels");
   }

   public boolean isItemValidForSlot(int var1, ItemStack var2) {
      return var2.getItem() == Items.emerald || var2.getItem() == Items.diamond || var2.getItem() == Items.gold_ingot || var2.getItem() == Items.iron_ingot;
   }

   public int getField(int var1) {
      switch(var1) {
      case 0:
         return this.levels;
      case 1:
         return this.primaryEffect;
      case 2:
         return this.secondaryEffect;
      default:
         return 0;
      }
   }

   private void addEffectsToPlayers() {
      if (this.isComplete && this.levels > 0 && !this.worldObj.isRemote && this.primaryEffect > 0) {
         double var1 = (double)(this.levels * 10 + 10);
         byte var3 = 0;
         if (this.levels >= 4 && this.primaryEffect == this.secondaryEffect) {
            var3 = 1;
         }

         int var4 = this.pos.getX();
         int var5 = this.pos.getY();
         int var6 = this.pos.getZ();
         AxisAlignedBB var7 = (new AxisAlignedBB((double)var4, (double)var5, (double)var6, (double)(var4 + 1), (double)(var5 + 1), (double)(var6 + 1))).expand(var1, var1, var1).addCoord(0.0D, (double)this.worldObj.getHeight(), 0.0D);
         List var8 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, var7);
         Iterator var10 = var8.iterator();

         EntityPlayer var9;
         while(var10.hasNext()) {
            var9 = (EntityPlayer)var10.next();
            var9.addPotionEffect(new PotionEffect(this.primaryEffect, 180, var3, true, true));
         }

         if (this.levels >= 4 && this.primaryEffect != this.secondaryEffect && this.secondaryEffect > 0) {
            var10 = var8.iterator();

            while(var10.hasNext()) {
               var9 = (EntityPlayer)var10.next();
               var9.addPotionEffect(new PotionEffect(this.secondaryEffect, 180, 0, true, true));
            }
         }
      }

   }

   public int getSizeInventory() {
      return 1;
   }

   public List getBeamSegments() {
      return this.beamSegments;
   }

   public Packet getDescriptionPacket() {
      NBTTagCompound var1 = new NBTTagCompound();
      this.writeToNBT(var1);
      return new S35PacketUpdateTileEntity(this.pos, 3, var1);
   }

   public static class BeamSegment {
      private int height;
      private final float[] colors;

      protected void incrementHeight() {
         ++this.height;
      }

      public BeamSegment(float[] var1) {
         this.colors = var1;
         this.height = 1;
      }

      public int getHeight() {
         return this.height;
      }

      public float[] getColors() {
         return this.colors;
      }
   }
}
