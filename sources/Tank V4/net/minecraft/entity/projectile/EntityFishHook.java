package net.minecraft.entity.projectile;

import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityFishHook extends Entity {
   private int ticksCaughtDelay;
   public Entity caughtEntity;
   private int xTile;
   private double clientMotionY;
   private boolean inGround;
   private static final List TREASURE;
   private int yTile;
   public int shake;
   private static final List JUNK;
   private Block inTile;
   private int ticksCatchable;
   private double fishYaw;
   private double fishY;
   private float fishApproachAngle;
   private static final List FISH;
   private double clientMotionX;
   private int ticksCatchableDelay;
   private double fishX;
   private int fishPosRotationIncrements;
   private double fishZ;
   private int ticksInGround;
   private double fishPitch;
   private double clientMotionZ;
   private int ticksInAir;
   private int zTile;
   public EntityPlayer angler;

   public void writeEntityToNBT(NBTTagCompound var1) {
      var1.setShort("xTile", (short)this.xTile);
      var1.setShort("yTile", (short)this.yTile);
      var1.setShort("zTile", (short)this.zTile);
      ResourceLocation var2 = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
      var1.setString("inTile", var2 == null ? "" : var2.toString());
      var1.setByte("shake", (byte)this.shake);
      var1.setByte("inGround", (byte)(this.inGround ? 1 : 0));
   }

   public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9, boolean var10) {
      this.fishX = var1;
      this.fishY = var3;
      this.fishZ = var5;
      this.fishYaw = (double)var7;
      this.fishPitch = (double)var8;
      this.fishPosRotationIncrements = var9;
      this.motionX = this.clientMotionX;
      this.motionY = this.clientMotionY;
      this.motionZ = this.clientMotionZ;
   }

   public void setDead() {
      super.setDead();
      if (this.angler != null) {
         this.angler.fishEntity = null;
      }

   }

   public void setVelocity(double var1, double var3, double var5) {
      this.clientMotionX = this.motionX = var1;
      this.clientMotionY = this.motionY = var3;
      this.clientMotionZ = this.motionZ = var5;
   }

   public int handleHookRetraction() {
      if (this.worldObj.isRemote) {
         return 0;
      } else {
         byte var1 = 0;
         if (this.caughtEntity != null) {
            double var2 = this.angler.posX - this.posX;
            double var4 = this.angler.posY - this.posY;
            double var6 = this.angler.posZ - this.posZ;
            double var8 = (double)MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
            double var10 = 0.1D;
            Entity var10000 = this.caughtEntity;
            var10000.motionX += var2 * var10;
            var10000 = this.caughtEntity;
            var10000.motionY += var4 * var10 + (double)MathHelper.sqrt_double(var8) * 0.08D;
            var10000 = this.caughtEntity;
            var10000.motionZ += var6 * var10;
            var1 = 3;
         } else if (this.ticksCatchable > 0) {
            EntityItem var13 = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, this.getFishingResult());
            double var3 = this.angler.posX - this.posX;
            double var5 = this.angler.posY - this.posY;
            double var7 = this.angler.posZ - this.posZ;
            double var9 = (double)MathHelper.sqrt_double(var3 * var3 + var5 * var5 + var7 * var7);
            double var11 = 0.1D;
            var13.motionX = var3 * var11;
            var13.motionY = var5 * var11 + (double)MathHelper.sqrt_double(var9) * 0.08D;
            var13.motionZ = var7 * var11;
            this.worldObj.spawnEntityInWorld(var13);
            this.angler.worldObj.spawnEntityInWorld(new EntityXPOrb(this.angler.worldObj, this.angler.posX, this.angler.posY + 0.5D, this.angler.posZ + 0.5D, this.rand.nextInt(6) + 1));
            var1 = 1;
         }

         if (this.inGround) {
            var1 = 2;
         }

         this.setDead();
         this.angler.fishEntity = null;
         return var1;
      }
   }

   public boolean isInRangeToRenderDist(double var1) {
      double var3 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
      if (Double.isNaN(var3)) {
         var3 = 4.0D;
      }

      var3 *= 64.0D;
      return var1 < var3 * var3;
   }

   static {
      JUNK = Arrays.asList((new WeightedRandomFishable(new ItemStack(Items.leather_boots), 10)).setMaxDamagePercent(0.9F), new WeightedRandomFishable(new ItemStack(Items.leather), 10), new WeightedRandomFishable(new ItemStack(Items.bone), 10), new WeightedRandomFishable(new ItemStack(Items.potionitem), 10), new WeightedRandomFishable(new ItemStack(Items.string), 5), (new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 2)).setMaxDamagePercent(0.9F), new WeightedRandomFishable(new ItemStack(Items.bowl), 10), new WeightedRandomFishable(new ItemStack(Items.stick), 5), new WeightedRandomFishable(new ItemStack(Items.dye, 10, EnumDyeColor.BLACK.getDyeDamage()), 1), new WeightedRandomFishable(new ItemStack(Blocks.tripwire_hook), 10), new WeightedRandomFishable(new ItemStack(Items.rotten_flesh), 10));
      TREASURE = Arrays.asList(new WeightedRandomFishable(new ItemStack(Blocks.waterlily), 1), new WeightedRandomFishable(new ItemStack(Items.name_tag), 1), new WeightedRandomFishable(new ItemStack(Items.saddle), 1), (new WeightedRandomFishable(new ItemStack(Items.bow), 1)).setMaxDamagePercent(0.25F).setEnchantable(), (new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 1)).setMaxDamagePercent(0.25F).setEnchantable(), (new WeightedRandomFishable(new ItemStack(Items.book), 1)).setEnchantable());
      FISH = Arrays.asList(new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getMetadata()), 60), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.SALMON.getMetadata()), 25), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()), 2), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()), 13));
   }

   public EntityFishHook(World var1, EntityPlayer var2) {
      super(var1);
      this.xTile = -1;
      this.yTile = -1;
      this.zTile = -1;
      this.ignoreFrustumCheck = true;
      this.angler = var2;
      this.angler.fishEntity = this;
      this.setSize(0.25F, 0.25F);
      this.setLocationAndAngles(var2.posX, var2.posY + (double)var2.getEyeHeight(), var2.posZ, var2.rotationYaw, var2.rotationPitch);
      this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
      this.posY -= 0.10000000149011612D;
      this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
      this.setPosition(this.posX, this.posY, this.posZ);
      float var3 = 0.4F;
      this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * var3);
      this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * var3);
      this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F) * var3);
      this.handleHookCasting(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
   }

   private ItemStack getFishingResult() {
      float var1 = this.worldObj.rand.nextFloat();
      int var2 = EnchantmentHelper.getLuckOfSeaModifier(this.angler);
      int var3 = EnchantmentHelper.getLureModifier(this.angler);
      float var4 = 0.1F - (float)var2 * 0.025F - (float)var3 * 0.01F;
      float var5 = 0.05F + (float)var2 * 0.01F - (float)var3 * 0.01F;
      var4 = MathHelper.clamp_float(var4, 0.0F, 1.0F);
      var5 = MathHelper.clamp_float(var5, 0.0F, 1.0F);
      if (var1 < var4) {
         this.angler.triggerAchievement(StatList.junkFishedStat);
         return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, JUNK)).getItemStack(this.rand);
      } else {
         var1 -= var4;
         if (var1 < var5) {
            this.angler.triggerAchievement(StatList.treasureFishedStat);
            return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, TREASURE)).getItemStack(this.rand);
         } else {
            float var10000 = var1 - var5;
            this.angler.triggerAchievement(StatList.fishCaughtStat);
            return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, FISH)).getItemStack(this.rand);
         }
      }
   }

   public static List func_174855_j() {
      return FISH;
   }

   public void handleHookCasting(double var1, double var3, double var5, float var7, float var8) {
      float var9 = MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
      var1 /= (double)var9;
      var3 /= (double)var9;
      var5 /= (double)var9;
      var1 += this.rand.nextGaussian() * 0.007499999832361937D * (double)var8;
      var3 += this.rand.nextGaussian() * 0.007499999832361937D * (double)var8;
      var5 += this.rand.nextGaussian() * 0.007499999832361937D * (double)var8;
      var1 *= (double)var7;
      var3 *= (double)var7;
      var5 *= (double)var7;
      this.motionX = var1;
      this.motionY = var3;
      this.motionZ = var5;
      float var10 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
      this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.func_181159_b(var1, var5) * 180.0D / 3.141592653589793D);
      this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.func_181159_b(var3, (double)var10) * 180.0D / 3.141592653589793D);
      this.ticksInGround = 0;
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      this.xTile = var1.getShort("xTile");
      this.yTile = var1.getShort("yTile");
      this.zTile = var1.getShort("zTile");
      if (var1.hasKey("inTile", 8)) {
         this.inTile = Block.getBlockFromName(var1.getString("inTile"));
      } else {
         this.inTile = Block.getBlockById(var1.getByte("inTile") & 255);
      }

      this.shake = var1.getByte("shake") & 255;
      this.inGround = var1.getByte("inGround") == 1;
   }

   protected void entityInit() {
   }

   public EntityFishHook(World var1) {
      super(var1);
      this.xTile = -1;
      this.yTile = -1;
      this.zTile = -1;
      this.setSize(0.25F, 0.25F);
      this.ignoreFrustumCheck = true;
   }

   public EntityFishHook(World var1, double var2, double var4, double var6, EntityPlayer var8) {
      this(var1);
      this.setPosition(var2, var4, var6);
      this.ignoreFrustumCheck = true;
      this.angler = var8;
      var8.fishEntity = this;
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.fishPosRotationIncrements > 0) {
         double var29 = this.posX + (this.fishX - this.posX) / (double)this.fishPosRotationIncrements;
         double var31 = this.posY + (this.fishY - this.posY) / (double)this.fishPosRotationIncrements;
         double var32 = this.posZ + (this.fishZ - this.posZ) / (double)this.fishPosRotationIncrements;
         double var7 = MathHelper.wrapAngleTo180_double(this.fishYaw - (double)this.rotationYaw);
         this.rotationYaw = (float)((double)this.rotationYaw + var7 / (double)this.fishPosRotationIncrements);
         this.rotationPitch = (float)((double)this.rotationPitch + (this.fishPitch - (double)this.rotationPitch) / (double)this.fishPosRotationIncrements);
         --this.fishPosRotationIncrements;
         this.setPosition(var29, var31, var32);
         this.setRotation(this.rotationYaw, this.rotationPitch);
      } else {
         if (!this.worldObj.isRemote) {
            ItemStack var1 = this.angler.getCurrentEquippedItem();
            if (this.angler.isDead || !this.angler.isEntityAlive() || var1 == null || var1.getItem() != Items.fishing_rod || this.getDistanceSqToEntity(this.angler) > 1024.0D) {
               this.setDead();
               this.angler.fishEntity = null;
               return;
            }

            if (this.caughtEntity != null) {
               if (!this.caughtEntity.isDead) {
                  this.posX = this.caughtEntity.posX;
                  double var30 = (double)this.caughtEntity.height;
                  this.posY = this.caughtEntity.getEntityBoundingBox().minY + var30 * 0.8D;
                  this.posZ = this.caughtEntity.posZ;
                  return;
               }

               this.caughtEntity = null;
            }
         }

         if (this.shake > 0) {
            --this.shake;
         }

         if (this.inGround) {
            if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile) {
               ++this.ticksInGround;
               if (this.ticksInGround == 1200) {
                  this.setDead();
               }

               return;
            }

            this.inGround = false;
            this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
            this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
            this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
            this.ticksInGround = 0;
            this.ticksInAir = 0;
         } else {
            ++this.ticksInAir;
         }

         Vec3 var28 = new Vec3(this.posX, this.posY, this.posZ);
         Vec3 var2 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
         MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var28, var2);
         var28 = new Vec3(this.posX, this.posY, this.posZ);
         var2 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
         if (var3 != null) {
            var2 = new Vec3(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
         }

         Entity var4 = null;
         List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
         double var6 = 0.0D;

         double var13;
         for(int var8 = 0; var8 < var5.size(); ++var8) {
            Entity var9 = (Entity)var5.get(var8);
            if (var9.canBeCollidedWith() && (var9 != this.angler || this.ticksInAir >= 5)) {
               float var10 = 0.3F;
               AxisAlignedBB var11 = var9.getEntityBoundingBox().expand((double)var10, (double)var10, (double)var10);
               MovingObjectPosition var12 = var11.calculateIntercept(var28, var2);
               if (var12 != null) {
                  var13 = var28.squareDistanceTo(var12.hitVec);
                  if (var13 < var6 || var6 == 0.0D) {
                     var4 = var9;
                     var6 = var13;
                  }
               }
            }
         }

         if (var4 != null) {
            var3 = new MovingObjectPosition(var4);
         }

         if (var3 != null) {
            if (var3.entityHit != null) {
               if (var3.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.angler), 0.0F)) {
                  this.caughtEntity = var3.entityHit;
               }
            } else {
               this.inGround = true;
            }
         }

         if (!this.inGround) {
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            float var33 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0D / 3.141592653589793D);

            for(this.rotationPitch = (float)(MathHelper.func_181159_b(this.motionY, (double)var33) * 180.0D / 3.141592653589793D); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            }

            while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
               this.prevRotationPitch += 360.0F;
            }

            while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
               this.prevRotationYaw -= 360.0F;
            }

            while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
               this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            float var34 = 0.92F;
            if (this.onGround || this.isCollidedHorizontally) {
               var34 = 0.5F;
            }

            byte var35 = 5;
            double var36 = 0.0D;

            double var19;
            for(int var37 = 0; var37 < var35; ++var37) {
               AxisAlignedBB var14 = this.getEntityBoundingBox();
               double var15 = var14.maxY - var14.minY;
               double var17 = var14.minY + var15 * (double)var37 / (double)var35;
               var19 = var14.minY + var15 * (double)(var37 + 1) / (double)var35;
               AxisAlignedBB var21 = new AxisAlignedBB(var14.minX, var17, var14.minZ, var14.maxX, var19, var14.maxZ);
               if (this.worldObj.isAABBInMaterial(var21, Material.water)) {
                  var36 += 1.0D / (double)var35;
               }
            }

            if (!this.worldObj.isRemote && var36 > 0.0D) {
               WorldServer var38 = (WorldServer)this.worldObj;
               int var39 = 1;
               BlockPos var40 = (new BlockPos(this)).up();
               if (this.rand.nextFloat() < 0.25F && this.worldObj.canLightningStrike(var40)) {
                  var39 = 2;
               }

               if (this.rand.nextFloat() < 0.5F && !this.worldObj.canSeeSky(var40)) {
                  --var39;
               }

               if (this.ticksCatchable > 0) {
                  --this.ticksCatchable;
                  if (this.ticksCatchable <= 0) {
                     this.ticksCaughtDelay = 0;
                     this.ticksCatchableDelay = 0;
                  }
               } else {
                  float var16;
                  float var18;
                  double var23;
                  Block var25;
                  float var41;
                  double var42;
                  if (this.ticksCatchableDelay > 0) {
                     this.ticksCatchableDelay -= var39;
                     if (this.ticksCatchableDelay <= 0) {
                        this.motionY -= 0.20000000298023224D;
                        this.playSound("random.splash", 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                        var16 = (float)MathHelper.floor_double(this.getEntityBoundingBox().minY);
                        var38.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX, (double)(var16 + 1.0F), this.posZ, (int)(1.0F + this.width * 20.0F), (double)this.width, 0.0D, (double)this.width, 0.20000000298023224D);
                        var38.spawnParticle(EnumParticleTypes.WATER_WAKE, this.posX, (double)(var16 + 1.0F), this.posZ, (int)(1.0F + this.width * 20.0F), (double)this.width, 0.0D, (double)this.width, 0.20000000298023224D);
                        this.ticksCatchable = MathHelper.getRandomIntegerInRange(this.rand, 10, 30);
                     } else {
                        this.fishApproachAngle = (float)((double)this.fishApproachAngle + this.rand.nextGaussian() * 4.0D);
                        var16 = this.fishApproachAngle * 0.017453292F;
                        var41 = MathHelper.sin(var16);
                        var18 = MathHelper.cos(var16);
                        var19 = this.posX + (double)(var41 * (float)this.ticksCatchableDelay * 0.1F);
                        var42 = (double)((float)MathHelper.floor_double(this.getEntityBoundingBox().minY) + 1.0F);
                        var23 = this.posZ + (double)(var18 * (float)this.ticksCatchableDelay * 0.1F);
                        var25 = var38.getBlockState(new BlockPos((int)var19, (int)var42 - 1, (int)var23)).getBlock();
                        if (var25 == Blocks.water || var25 == Blocks.flowing_water) {
                           if (this.rand.nextFloat() < 0.15F) {
                              var38.spawnParticle(EnumParticleTypes.WATER_BUBBLE, var19, var42 - 0.10000000149011612D, var23, 1, (double)var41, 0.1D, (double)var18, 0.0D);
                           }

                           float var26 = var41 * 0.04F;
                           float var27 = var18 * 0.04F;
                           var38.spawnParticle(EnumParticleTypes.WATER_WAKE, var19, var42, var23, 0, (double)var27, 0.01D, (double)(-var26), 1.0D);
                           var38.spawnParticle(EnumParticleTypes.WATER_WAKE, var19, var42, var23, 0, (double)(-var27), 0.01D, (double)var26, 1.0D);
                        }
                     }
                  } else if (this.ticksCaughtDelay > 0) {
                     this.ticksCaughtDelay -= var39;
                     var16 = 0.15F;
                     if (this.ticksCaughtDelay < 20) {
                        var16 = (float)((double)var16 + (double)(20 - this.ticksCaughtDelay) * 0.05D);
                     } else if (this.ticksCaughtDelay < 40) {
                        var16 = (float)((double)var16 + (double)(40 - this.ticksCaughtDelay) * 0.02D);
                     } else if (this.ticksCaughtDelay < 60) {
                        var16 = (float)((double)var16 + (double)(60 - this.ticksCaughtDelay) * 0.01D);
                     }

                     if (this.rand.nextFloat() < var16) {
                        var41 = MathHelper.randomFloatClamp(this.rand, 0.0F, 360.0F) * 0.017453292F;
                        var18 = MathHelper.randomFloatClamp(this.rand, 25.0F, 60.0F);
                        var19 = this.posX + (double)(MathHelper.sin(var41) * var18 * 0.1F);
                        var42 = (double)((float)MathHelper.floor_double(this.getEntityBoundingBox().minY) + 1.0F);
                        var23 = this.posZ + (double)(MathHelper.cos(var41) * var18 * 0.1F);
                        var25 = var38.getBlockState(new BlockPos((int)var19, (int)var42 - 1, (int)var23)).getBlock();
                        if (var25 == Blocks.water || var25 == Blocks.flowing_water) {
                           var38.spawnParticle(EnumParticleTypes.WATER_SPLASH, var19, var42, var23, 2 + this.rand.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
                        }
                     }

                     if (this.ticksCaughtDelay <= 0) {
                        this.fishApproachAngle = MathHelper.randomFloatClamp(this.rand, 0.0F, 360.0F);
                        this.ticksCatchableDelay = MathHelper.getRandomIntegerInRange(this.rand, 20, 80);
                     }
                  } else {
                     this.ticksCaughtDelay = MathHelper.getRandomIntegerInRange(this.rand, 100, 900);
                     this.ticksCaughtDelay -= EnchantmentHelper.getLureModifier(this.angler) * 20 * 5;
                  }
               }

               if (this.ticksCatchable > 0) {
                  this.motionY -= (double)(this.rand.nextFloat() * this.rand.nextFloat() * this.rand.nextFloat()) * 0.2D;
               }
            }

            var13 = var36 * 2.0D - 1.0D;
            this.motionY += 0.03999999910593033D * var13;
            if (var36 > 0.0D) {
               var34 = (float)((double)var34 * 0.9D);
               this.motionY *= 0.8D;
            }

            this.motionX *= (double)var34;
            this.motionY *= (double)var34;
            this.motionZ *= (double)var34;
            this.setPosition(this.posX, this.posY, this.posZ);
         }
      }

   }
}
