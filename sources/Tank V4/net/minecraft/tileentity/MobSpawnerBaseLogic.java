package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StringUtils;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

public abstract class MobSpawnerBaseLogic {
   private String mobID = "Pig";
   private int minSpawnDelay = 200;
   private int maxNearbyEntities = 6;
   private final List minecartToSpawn = Lists.newArrayList();
   private Entity cachedEntity;
   private MobSpawnerBaseLogic.WeightedRandomMinecart randomEntity;
   private double mobRotation;
   private int maxSpawnDelay = 800;
   private int activatingRangeFromPlayer = 16;
   private double prevMobRotation;
   private int spawnRange = 4;
   private int spawnDelay = 20;
   private int spawnCount = 4;

   public abstract void func_98267_a(int var1);

   public void setEntityName(String var1) {
      this.mobID = var1;
   }

   private boolean isActivated() {
      BlockPos var1 = this.getSpawnerPosition();
      return this.getSpawnerWorld().isAnyPlayerWithinRangeAt((double)var1.getX() + 0.5D, (double)var1.getY() + 0.5D, (double)var1.getZ() + 0.5D, (double)this.activatingRangeFromPlayer);
   }

   public void updateSpawner() {
      if (this.isActivated()) {
         BlockPos var1 = this.getSpawnerPosition();
         double var6;
         if (this.getSpawnerWorld().isRemote) {
            double var13 = (double)((float)var1.getX() + this.getSpawnerWorld().rand.nextFloat());
            double var14 = (double)((float)var1.getY() + this.getSpawnerWorld().rand.nextFloat());
            var6 = (double)((float)var1.getZ() + this.getSpawnerWorld().rand.nextFloat());
            this.getSpawnerWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var13, var14, var6, 0.0D, 0.0D, 0.0D);
            this.getSpawnerWorld().spawnParticle(EnumParticleTypes.FLAME, var13, var14, var6, 0.0D, 0.0D, 0.0D);
            if (this.spawnDelay > 0) {
               --this.spawnDelay;
            }

            this.prevMobRotation = this.mobRotation;
            this.mobRotation = (this.mobRotation + (double)(1000.0F / ((float)this.spawnDelay + 200.0F))) % 360.0D;
         } else {
            if (this.spawnDelay == -1) {
               this.resetTimer();
            }

            if (this.spawnDelay > 0) {
               --this.spawnDelay;
               return;
            }

            boolean var2 = false;
            int var3 = 0;

            while(true) {
               if (var3 >= this.spawnCount) {
                  if (var2) {
                     this.resetTimer();
                  }
                  break;
               }

               Entity var4 = EntityList.createEntityByName(this.getEntityNameToSpawn(), this.getSpawnerWorld());
               if (var4 == null) {
                  return;
               }

               int var5 = this.getSpawnerWorld().getEntitiesWithinAABB(var4.getClass(), (new AxisAlignedBB((double)var1.getX(), (double)var1.getY(), (double)var1.getZ(), (double)(var1.getX() + 1), (double)(var1.getY() + 1), (double)(var1.getZ() + 1))).expand((double)this.spawnRange, (double)this.spawnRange, (double)this.spawnRange)).size();
               if (var5 >= this.maxNearbyEntities) {
                  this.resetTimer();
                  return;
               }

               var6 = (double)var1.getX() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * (double)this.spawnRange + 0.5D;
               double var8 = (double)(var1.getY() + this.getSpawnerWorld().rand.nextInt(3) - 1);
               double var10 = (double)var1.getZ() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * (double)this.spawnRange + 0.5D;
               EntityLiving var12 = var4 instanceof EntityLiving ? (EntityLiving)var4 : null;
               var4.setLocationAndAngles(var6, var8, var10, this.getSpawnerWorld().rand.nextFloat() * 360.0F, 0.0F);
               if (var12 == null || var12.getCanSpawnHere() && var12.isNotColliding()) {
                  this.spawnNewEntity(var4, true);
                  this.getSpawnerWorld().playAuxSFX(2004, var1, 0);
                  if (var12 != null) {
                     var12.spawnExplosionParticle();
                  }

                  var2 = true;
               }

               ++var3;
            }
         }
      }

   }

   public boolean setDelayToMin(int var1) {
      if (var1 == 1 && this.getSpawnerWorld().isRemote) {
         this.spawnDelay = this.minSpawnDelay;
         return true;
      } else {
         return false;
      }
   }

   public void readFromNBT(NBTTagCompound var1) {
      this.mobID = var1.getString("EntityId");
      this.spawnDelay = var1.getShort("Delay");
      this.minecartToSpawn.clear();
      if (var1.hasKey("SpawnPotentials", 9)) {
         NBTTagList var2 = var1.getTagList("SpawnPotentials", 10);

         for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
            this.minecartToSpawn.add(new MobSpawnerBaseLogic.WeightedRandomMinecart(this, var2.getCompoundTagAt(var3)));
         }
      }

      if (var1.hasKey("SpawnData", 10)) {
         this.setRandomEntity(new MobSpawnerBaseLogic.WeightedRandomMinecart(this, var1.getCompoundTag("SpawnData"), this.mobID));
      } else {
         this.setRandomEntity((MobSpawnerBaseLogic.WeightedRandomMinecart)null);
      }

      if (var1.hasKey("MinSpawnDelay", 99)) {
         this.minSpawnDelay = var1.getShort("MinSpawnDelay");
         this.maxSpawnDelay = var1.getShort("MaxSpawnDelay");
         this.spawnCount = var1.getShort("SpawnCount");
      }

      if (var1.hasKey("MaxNearbyEntities", 99)) {
         this.maxNearbyEntities = var1.getShort("MaxNearbyEntities");
         this.activatingRangeFromPlayer = var1.getShort("RequiredPlayerRange");
      }

      if (var1.hasKey("SpawnRange", 99)) {
         this.spawnRange = var1.getShort("SpawnRange");
      }

      if (this.getSpawnerWorld() != null) {
         this.cachedEntity = null;
      }

   }

   public double getMobRotation() {
      return this.mobRotation;
   }

   private String getEntityNameToSpawn() {
      if (this.getRandomEntity() == null) {
         if (this.mobID != null && this.mobID.equals("Minecart")) {
            this.mobID = "MinecartRideable";
         }

         return this.mobID;
      } else {
         return MobSpawnerBaseLogic.WeightedRandomMinecart.access$0(this.getRandomEntity());
      }
   }

   private Entity spawnNewEntity(Entity var1, boolean var2) {
      if (this.getRandomEntity() != null) {
         NBTTagCompound var3 = new NBTTagCompound();
         var1.writeToNBTOptional(var3);
         Iterator var5 = MobSpawnerBaseLogic.WeightedRandomMinecart.access$1(this.getRandomEntity()).getKeySet().iterator();

         while(var5.hasNext()) {
            String var4 = (String)var5.next();
            NBTBase var6 = MobSpawnerBaseLogic.WeightedRandomMinecart.access$1(this.getRandomEntity()).getTag(var4);
            var3.setTag(var4, var6.copy());
         }

         var1.readFromNBT(var3);
         if (var1.worldObj != null && var2) {
            var1.worldObj.spawnEntityInWorld(var1);
         }

         NBTTagCompound var11;
         for(Entity var12 = var1; var3.hasKey("Riding", 10); var3 = var11) {
            var11 = var3.getCompoundTag("Riding");
            Entity var13 = EntityList.createEntityByName(var11.getString("id"), var1.worldObj);
            if (var13 != null) {
               NBTTagCompound var7 = new NBTTagCompound();
               var13.writeToNBTOptional(var7);
               Iterator var9 = var11.getKeySet().iterator();

               while(var9.hasNext()) {
                  String var8 = (String)var9.next();
                  NBTBase var10 = var11.getTag(var8);
                  var7.setTag(var8, var10.copy());
               }

               var13.readFromNBT(var7);
               var13.setLocationAndAngles(var12.posX, var12.posY, var12.posZ, var12.rotationYaw, var12.rotationPitch);
               if (var1.worldObj != null && var2) {
                  var1.worldObj.spawnEntityInWorld(var13);
               }

               var12.mountEntity(var13);
            }

            var12 = var13;
         }
      } else if (var1 instanceof EntityLivingBase && var1.worldObj != null && var2) {
         if (var1 instanceof EntityLiving) {
            ((EntityLiving)var1).onInitialSpawn(var1.worldObj.getDifficultyForLocation(new BlockPos(var1)), (IEntityLivingData)null);
         }

         var1.worldObj.spawnEntityInWorld(var1);
      }

      return var1;
   }

   private MobSpawnerBaseLogic.WeightedRandomMinecart getRandomEntity() {
      return this.randomEntity;
   }

   public abstract BlockPos getSpawnerPosition();

   public abstract World getSpawnerWorld();

   public void setRandomEntity(MobSpawnerBaseLogic.WeightedRandomMinecart var1) {
      this.randomEntity = var1;
   }

   public double getPrevMobRotation() {
      return this.prevMobRotation;
   }

   public Entity func_180612_a(World var1) {
      if (this.cachedEntity == null) {
         Entity var2 = EntityList.createEntityByName(this.getEntityNameToSpawn(), var1);
         if (var2 != null) {
            var2 = this.spawnNewEntity(var2, false);
            this.cachedEntity = var2;
         }
      }

      return this.cachedEntity;
   }

   public void writeToNBT(NBTTagCompound var1) {
      String var2 = this.getEntityNameToSpawn();
      if (!StringUtils.isNullOrEmpty(var2)) {
         var1.setString("EntityId", var2);
         var1.setShort("Delay", (short)this.spawnDelay);
         var1.setShort("MinSpawnDelay", (short)this.minSpawnDelay);
         var1.setShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
         var1.setShort("SpawnCount", (short)this.spawnCount);
         var1.setShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
         var1.setShort("RequiredPlayerRange", (short)this.activatingRangeFromPlayer);
         var1.setShort("SpawnRange", (short)this.spawnRange);
         if (this.getRandomEntity() != null) {
            var1.setTag("SpawnData", MobSpawnerBaseLogic.WeightedRandomMinecart.access$1(this.getRandomEntity()).copy());
         }

         if (this.getRandomEntity() != null || this.minecartToSpawn.size() > 0) {
            NBTTagList var3 = new NBTTagList();
            if (this.minecartToSpawn.size() > 0) {
               Iterator var5 = this.minecartToSpawn.iterator();

               while(var5.hasNext()) {
                  MobSpawnerBaseLogic.WeightedRandomMinecart var4 = (MobSpawnerBaseLogic.WeightedRandomMinecart)var5.next();
                  var3.appendTag(var4.toNBT());
               }
            } else {
               var3.appendTag(this.getRandomEntity().toNBT());
            }

            var1.setTag("SpawnPotentials", var3);
         }
      }

   }

   private void resetTimer() {
      if (this.maxSpawnDelay <= this.minSpawnDelay) {
         this.spawnDelay = this.minSpawnDelay;
      } else {
         int var1 = this.maxSpawnDelay - this.minSpawnDelay;
         this.spawnDelay = this.minSpawnDelay + this.getSpawnerWorld().rand.nextInt(var1);
      }

      if (this.minecartToSpawn.size() > 0) {
         this.setRandomEntity((MobSpawnerBaseLogic.WeightedRandomMinecart)WeightedRandom.getRandomItem(this.getSpawnerWorld().rand, this.minecartToSpawn));
      }

      this.func_98267_a(1);
   }

   public class WeightedRandomMinecart extends WeightedRandom.Item {
      private final String entityType;
      final MobSpawnerBaseLogic this$0;
      private final NBTTagCompound nbtData;

      public NBTTagCompound toNBT() {
         NBTTagCompound var1 = new NBTTagCompound();
         var1.setTag("Properties", this.nbtData);
         var1.setString("Type", this.entityType);
         var1.setInteger("Weight", this.itemWeight);
         return var1;
      }

      static NBTTagCompound access$1(MobSpawnerBaseLogic.WeightedRandomMinecart var0) {
         return var0.nbtData;
      }

      public WeightedRandomMinecart(MobSpawnerBaseLogic var1, NBTTagCompound var2) {
         this(var1, var2.getCompoundTag("Properties"), var2.getString("Type"), var2.getInteger("Weight"));
      }

      private WeightedRandomMinecart(MobSpawnerBaseLogic var1, NBTTagCompound var2, String var3, int var4) {
         super(var4);
         this.this$0 = var1;
         if (var3.equals("Minecart")) {
            if (var2 != null) {
               var3 = EntityMinecart.EnumMinecartType.byNetworkID(var2.getInteger("Type")).getName();
            } else {
               var3 = "MinecartRideable";
            }
         }

         this.nbtData = var2;
         this.entityType = var3;
      }

      static String access$0(MobSpawnerBaseLogic.WeightedRandomMinecart var0) {
         return var0.entityType;
      }

      public WeightedRandomMinecart(MobSpawnerBaseLogic var1, NBTTagCompound var2, String var3) {
         this(var1, var2, var3, 1);
      }
   }
}
