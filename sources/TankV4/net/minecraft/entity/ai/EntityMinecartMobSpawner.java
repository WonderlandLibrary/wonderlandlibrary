package net.minecraft.entity.ai;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityMinecartMobSpawner extends EntityMinecart {
   private final MobSpawnerBaseLogic mobSpawnerLogic = new MobSpawnerBaseLogic(this) {
      final EntityMinecartMobSpawner this$0;

      public BlockPos getSpawnerPosition() {
         return new BlockPos(this.this$0);
      }

      {
         this.this$0 = var1;
      }

      public World getSpawnerWorld() {
         return this.this$0.worldObj;
      }

      public void func_98267_a(int var1) {
         this.this$0.worldObj.setEntityState(this.this$0, (byte)var1);
      }
   };

   public EntityMinecartMobSpawner(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6);
   }

   public void handleStatusUpdate(byte var1) {
      this.mobSpawnerLogic.setDelayToMin(var1);
   }

   protected void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      this.mobSpawnerLogic.writeToNBT(var1);
   }

   public IBlockState getDefaultDisplayTile() {
      return Blocks.mob_spawner.getDefaultState();
   }

   public MobSpawnerBaseLogic func_98039_d() {
      return this.mobSpawnerLogic;
   }

   public EntityMinecartMobSpawner(World var1) {
      super(var1);
   }

   protected void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      this.mobSpawnerLogic.readFromNBT(var1);
   }

   public void onUpdate() {
      super.onUpdate();
      this.mobSpawnerLogic.updateSpawner();
   }

   public EntityMinecart.EnumMinecartType getMinecartType() {
      return EntityMinecart.EnumMinecartType.SPAWNER;
   }
}
