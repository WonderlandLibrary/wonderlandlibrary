package net.minecraft.entity.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.IHopper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMinecartHopper extends EntityMinecartContainer implements IHopper {
   private int transferTicker = -1;
   private boolean isBlocked = true;
   private BlockPos field_174900_c;

   public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
      return new ContainerHopper(var1, this, var2);
   }

   public World getWorld() {
      return this.worldObj;
   }

   protected void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      this.transferTicker = var1.getInteger("TransferCooldown");
   }

   public int getSizeInventory() {
      return 5;
   }

   public IBlockState getDefaultDisplayTile() {
      return Blocks.hopper.getDefaultState();
   }

   public void setTransferTicker(int var1) {
      this.transferTicker = var1;
   }

   public EntityMinecartHopper(World var1) {
      super(var1);
      this.field_174900_c = BlockPos.ORIGIN;
   }

   public double getZPos() {
      return this.posZ;
   }

   public void killMinecart(DamageSource var1) {
      super.killMinecart(var1);
      if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
         this.dropItemWithOffset(Item.getItemFromBlock(Blocks.hopper), 1, 0.0F);
      }

   }

   public EntityMinecart.EnumMinecartType getMinecartType() {
      return EntityMinecart.EnumMinecartType.HOPPER;
   }

   public int getDefaultDisplayTileOffset() {
      return 1;
   }

   public boolean interactFirst(EntityPlayer var1) {
      if (!this.worldObj.isRemote) {
         var1.displayGUIChest(this);
      }

      return true;
   }

   public EntityMinecartHopper(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6);
      this.field_174900_c = BlockPos.ORIGIN;
   }

   public boolean getBlocked() {
      return this.isBlocked;
   }

   public double getYPos() {
      return this.posY + 0.5D;
   }

   public void onActivatorRailPass(int var1, int var2, int var3, boolean var4) {
      boolean var5 = !var4;
      if (var5 != this.getBlocked()) {
         this.setBlocked(var5);
      }

   }

   protected void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      var1.setInteger("TransferCooldown", this.transferTicker);
   }

   public void setBlocked(boolean var1) {
      this.isBlocked = var1;
   }

   public void onUpdate() {
      super.onUpdate();
      if (!this.worldObj.isRemote && this.isEntityAlive() && this.getBlocked()) {
         BlockPos var1 = new BlockPos(this);
         if (var1.equals(this.field_174900_c)) {
            --this.transferTicker;
         } else {
            this.setTransferTicker(0);
         }

         if (this > 0) {
            this.setTransferTicker(0);
            if (this != false) {
               this.setTransferTicker(4);
               this.markDirty();
            }
         }
      }

   }

   public String getGuiID() {
      return "minecraft:hopper";
   }

   public double getXPos() {
      return this.posX;
   }
}
