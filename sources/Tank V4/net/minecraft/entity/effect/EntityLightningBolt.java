package net.minecraft.entity.effect;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityLightningBolt extends EntityWeatherEffect {
   private int boltLivingTime;
   public long boltVertex;
   private int lightningState;

   protected void entityInit() {
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.lightningState == 2) {
         this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
         this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
      }

      --this.lightningState;
      if (this.lightningState < 0) {
         if (this.boltLivingTime == 0) {
            this.setDead();
         } else if (this.lightningState < -this.rand.nextInt(10)) {
            --this.boltLivingTime;
            this.lightningState = 1;
            this.boltVertex = this.rand.nextLong();
            BlockPos var1 = new BlockPos(this);
            if (!this.worldObj.isRemote && this.worldObj.getGameRules().getBoolean("doFireTick") && this.worldObj.isAreaLoaded(var1, 10) && this.worldObj.getBlockState(var1).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(this.worldObj, var1)) {
               this.worldObj.setBlockState(var1, Blocks.fire.getDefaultState());
            }
         }
      }

      if (this.lightningState >= 0) {
         if (this.worldObj.isRemote) {
            this.worldObj.setLastLightningBolt(2);
         } else {
            double var7 = 3.0D;
            List var3 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX - var7, this.posY - var7, this.posZ - var7, this.posX + var7, this.posY + 6.0D + var7, this.posZ + var7));

            for(int var4 = 0; var4 < var3.size(); ++var4) {
               Entity var5 = (Entity)var3.get(var4);
               var5.onStruckByLightning(this);
            }
         }
      }

   }

   protected void writeEntityToNBT(NBTTagCompound var1) {
   }

   public EntityLightningBolt(World var1, double var2, double var4, double var6) {
      super(var1);
      this.setLocationAndAngles(var2, var4, var6, 0.0F, 0.0F);
      this.lightningState = 2;
      this.boltVertex = this.rand.nextLong();
      this.boltLivingTime = this.rand.nextInt(3) + 1;
      BlockPos var8 = new BlockPos(this);
      if (!var1.isRemote && var1.getGameRules().getBoolean("doFireTick") && (var1.getDifficulty() == EnumDifficulty.NORMAL || var1.getDifficulty() == EnumDifficulty.HARD) && var1.isAreaLoaded(var8, 10)) {
         if (var1.getBlockState(var8).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(var1, var8)) {
            var1.setBlockState(var8, Blocks.fire.getDefaultState());
         }

         for(int var9 = 0; var9 < 4; ++var9) {
            BlockPos var10 = var8.add(this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1);
            if (var1.getBlockState(var10).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(var1, var10)) {
               var1.setBlockState(var10, Blocks.fire.getDefaultState());
            }
         }
      }

   }

   protected void readEntityFromNBT(NBTTagCompound var1) {
   }
}
