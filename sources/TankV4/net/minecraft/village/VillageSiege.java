package net.minecraft.village;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;

public class VillageSiege {
   private boolean field_75535_b;
   private int field_75533_d;
   private int field_75539_i;
   private int field_75534_e;
   private World worldObj;
   private int field_75536_c = -1;
   private int field_75538_h;
   private int field_75532_g;
   private Village theVillage;

   public VillageSiege(World var1) {
      this.worldObj = var1;
   }

   private boolean spawnZombie() {
      Vec3 var1 = this.func_179867_a(new BlockPos(this.field_75532_g, this.field_75538_h, this.field_75539_i));
      if (var1 == null) {
         return false;
      } else {
         EntityZombie var2;
         try {
            var2 = new EntityZombie(this.worldObj);
            var2.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(var2)), (IEntityLivingData)null);
            var2.setVillager(false);
         } catch (Exception var5) {
            var5.printStackTrace();
            return false;
         }

         var2.setLocationAndAngles(var1.xCoord, var1.yCoord, var1.zCoord, this.worldObj.rand.nextFloat() * 360.0F, 0.0F);
         this.worldObj.spawnEntityInWorld(var2);
         BlockPos var3 = this.theVillage.getCenter();
         var2.setHomePosAndDistance(var3, this.theVillage.getVillageRadius());
         return true;
      }
   }

   private Vec3 func_179867_a(BlockPos var1) {
      for(int var2 = 0; var2 < 10; ++var2) {
         BlockPos var3 = var1.add(this.worldObj.rand.nextInt(16) - 8, this.worldObj.rand.nextInt(6) - 3, this.worldObj.rand.nextInt(16) - 8);
         if (this.theVillage.func_179866_a(var3) && SpawnerAnimals.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, this.worldObj, var3)) {
            return new Vec3((double)var3.getX(), (double)var3.getY(), (double)var3.getZ());
         }
      }

      return null;
   }

   public void tick() {
      if (this.worldObj.isDaytime()) {
         this.field_75536_c = 0;
      } else if (this.field_75536_c != 2) {
         if (this.field_75536_c == 0) {
            float var1 = this.worldObj.getCelestialAngle(0.0F);
            if ((double)var1 < 0.5D || (double)var1 > 0.501D) {
               return;
            }

            this.field_75536_c = this.worldObj.rand.nextInt(10) == 0 ? 1 : 2;
            this.field_75535_b = false;
            if (this.field_75536_c == 2) {
               return;
            }
         }

         if (this.field_75536_c != -1) {
            if (!this.field_75535_b) {
               if (this == false) {
                  return;
               }

               this.field_75535_b = true;
            }

            if (this.field_75534_e > 0) {
               --this.field_75534_e;
            } else {
               this.field_75534_e = 2;
               if (this.field_75533_d > 0) {
                  this.spawnZombie();
                  --this.field_75533_d;
               } else {
                  this.field_75536_c = 2;
               }
            }
         }
      }

   }
}
