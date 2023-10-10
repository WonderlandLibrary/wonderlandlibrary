package net.minecraft.entity.item;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityMinecart extends Entity implements IWorldNameable {
   private double minecartX;
   private double velocityZ;
   private int turnProgress;
   private String entityName;
   private boolean isInReverse;
   private double velocityX;
   private static final int[][][] matrix = new int[][][]{{{0, 0, -1}, {0, 0, 1}}, {{-1, 0, 0}, {1, 0, 0}}, {{-1, -1, 0}, {1, 0, 0}}, {{-1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, {-1, 0, 0}}, {{0, 0, -1}, {-1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};
   private double minecartZ;
   private double minecartYaw;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$block$BlockRailBase$EnumRailDirection;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$entity$item$EntityMinecart$EnumMinecartType;
   private double minecartY;
   private double minecartPitch;
   private double velocityY;

   public int getRollingDirection() {
      return this.dataWatcher.getWatchableObjectInt(18);
   }

   public Vec3 func_70489_a(double var1, double var3, double var5) {
      int var7 = MathHelper.floor_double(var1);
      int var8 = MathHelper.floor_double(var3);
      int var9 = MathHelper.floor_double(var5);
      if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(var7, var8 - 1, var9))) {
         --var8;
      }

      IBlockState var10 = this.worldObj.getBlockState(new BlockPos(var7, var8, var9));
      if (BlockRailBase.isRailBlock(var10)) {
         BlockRailBase.EnumRailDirection var11 = (BlockRailBase.EnumRailDirection)var10.getValue(((BlockRailBase)var10.getBlock()).getShapeProperty());
         int[][] var12 = matrix[var11.getMetadata()];
         double var13 = 0.0D;
         double var15 = (double)var7 + 0.5D + (double)var12[0][0] * 0.5D;
         double var17 = (double)var8 + 0.0625D + (double)var12[0][1] * 0.5D;
         double var19 = (double)var9 + 0.5D + (double)var12[0][2] * 0.5D;
         double var21 = (double)var7 + 0.5D + (double)var12[1][0] * 0.5D;
         double var23 = (double)var8 + 0.0625D + (double)var12[1][1] * 0.5D;
         double var25 = (double)var9 + 0.5D + (double)var12[1][2] * 0.5D;
         double var27 = var21 - var15;
         double var29 = (var23 - var17) * 2.0D;
         double var31 = var25 - var19;
         if (var27 == 0.0D) {
            var1 = (double)var7 + 0.5D;
            var13 = var5 - (double)var9;
         } else if (var31 == 0.0D) {
            var5 = (double)var9 + 0.5D;
            var13 = var1 - (double)var7;
         } else {
            double var33 = var1 - var15;
            double var35 = var5 - var19;
            var13 = (var33 * var27 + var35 * var31) * 2.0D;
         }

         var1 = var15 + var27 * var13;
         var3 = var17 + var29 * var13;
         var5 = var19 + var31 * var13;
         if (var29 < 0.0D) {
            ++var3;
         }

         if (var29 > 0.0D) {
            var3 += 0.5D;
         }

         return new Vec3(var1, var3, var5);
      } else {
         return null;
      }
   }

   public int getRollingAmplitude() {
      return this.dataWatcher.getWatchableObjectInt(17);
   }

   public int getDefaultDisplayTileOffset() {
      return 6;
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   protected double getMaximumSpeed() {
      return 0.4D;
   }

   public IBlockState getDefaultDisplayTile() {
      return Blocks.air.getDefaultState();
   }

   public void setPosition(double var1, double var3, double var5) {
      this.posX = var1;
      this.posY = var3;
      this.posZ = var5;
      float var7 = this.width / 2.0F;
      float var8 = this.height;
      this.setEntityBoundingBox(new AxisAlignedBB(var1 - (double)var7, var3, var5 - (double)var7, var1 + (double)var7, var3 + (double)var8, var5 + (double)var7));
   }

   public String getName() {
      return this.entityName != null ? this.entityName : super.getName();
   }

   public float getDamage() {
      return this.dataWatcher.getWatchableObjectFloat(19);
   }

   static int[] $SWITCH_TABLE$net$minecraft$entity$item$EntityMinecart$EnumMinecartType() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$entity$item$EntityMinecart$EnumMinecartType;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[EntityMinecart.EnumMinecartType.values().length];

         try {
            var0[EntityMinecart.EnumMinecartType.CHEST.ordinal()] = 2;
         } catch (NoSuchFieldError var7) {
         }

         try {
            var0[EntityMinecart.EnumMinecartType.COMMAND_BLOCK.ordinal()] = 7;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[EntityMinecart.EnumMinecartType.FURNACE.ordinal()] = 3;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[EntityMinecart.EnumMinecartType.HOPPER.ordinal()] = 6;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[EntityMinecart.EnumMinecartType.RIDEABLE.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[EntityMinecart.EnumMinecartType.SPAWNER.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[EntityMinecart.EnumMinecartType.TNT.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$entity$item$EntityMinecart$EnumMinecartType = var0;
         return var0;
      }
   }

   public void performHurtAnimation() {
      this.setRollingDirection(-this.getRollingDirection());
      this.setRollingAmplitude(10);
      this.setDamage(this.getDamage() + this.getDamage() * 10.0F);
   }

   protected void func_180460_a(BlockPos var1, IBlockState var2) {
      this.fallDistance = 0.0F;
      Vec3 var3 = this.func_70489_a(this.posX, this.posY, this.posZ);
      this.posY = (double)var1.getY();
      boolean var4 = false;
      boolean var5 = false;
      BlockRailBase var6 = (BlockRailBase)var2.getBlock();
      if (var6 == Blocks.golden_rail) {
         var4 = (Boolean)var2.getValue(BlockRailPowered.POWERED);
         var5 = !var4;
      }

      double var7 = 0.0078125D;
      BlockRailBase.EnumRailDirection var9 = (BlockRailBase.EnumRailDirection)var2.getValue(var6.getShapeProperty());
      switch($SWITCH_TABLE$net$minecraft$block$BlockRailBase$EnumRailDirection()[var9.ordinal()]) {
      case 3:
         this.motionX -= 0.0078125D;
         ++this.posY;
         break;
      case 4:
         this.motionX += 0.0078125D;
         ++this.posY;
         break;
      case 5:
         this.motionZ += 0.0078125D;
         ++this.posY;
         break;
      case 6:
         this.motionZ -= 0.0078125D;
         ++this.posY;
      }

      int[][] var10 = matrix[var9.getMetadata()];
      double var11 = (double)(var10[1][0] - var10[0][0]);
      double var13 = (double)(var10[1][2] - var10[0][2]);
      double var15 = Math.sqrt(var11 * var11 + var13 * var13);
      double var17 = this.motionX * var11 + this.motionZ * var13;
      if (var17 < 0.0D) {
         var11 = -var11;
         var13 = -var13;
      }

      double var19 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
      if (var19 > 2.0D) {
         var19 = 2.0D;
      }

      this.motionX = var19 * var11 / var15;
      this.motionZ = var19 * var13 / var15;
      double var21;
      double var23;
      double var25;
      double var27;
      if (this.riddenByEntity instanceof EntityLivingBase) {
         var21 = (double)((EntityLivingBase)this.riddenByEntity).moveForward;
         if (var21 > 0.0D) {
            var23 = -Math.sin((double)(this.riddenByEntity.rotationYaw * 3.1415927F / 180.0F));
            var25 = Math.cos((double)(this.riddenByEntity.rotationYaw * 3.1415927F / 180.0F));
            var27 = this.motionX * this.motionX + this.motionZ * this.motionZ;
            if (var27 < 0.01D) {
               this.motionX += var23 * 0.1D;
               this.motionZ += var25 * 0.1D;
               var5 = false;
            }
         }
      }

      if (var5) {
         var21 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
         if (var21 < 0.03D) {
            this.motionX *= 0.0D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.0D;
         } else {
            this.motionX *= 0.5D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.5D;
         }
      }

      var21 = 0.0D;
      var23 = (double)var1.getX() + 0.5D + (double)var10[0][0] * 0.5D;
      var25 = (double)var1.getZ() + 0.5D + (double)var10[0][2] * 0.5D;
      var27 = (double)var1.getX() + 0.5D + (double)var10[1][0] * 0.5D;
      double var29 = (double)var1.getZ() + 0.5D + (double)var10[1][2] * 0.5D;
      var11 = var27 - var23;
      var13 = var29 - var25;
      double var31;
      double var33;
      if (var11 == 0.0D) {
         this.posX = (double)var1.getX() + 0.5D;
         var21 = this.posZ - (double)var1.getZ();
      } else if (var13 == 0.0D) {
         this.posZ = (double)var1.getZ() + 0.5D;
         var21 = this.posX - (double)var1.getX();
      } else {
         var31 = this.posX - var23;
         var33 = this.posZ - var25;
         var21 = (var31 * var11 + var33 * var13) * 2.0D;
      }

      this.posX = var23 + var11 * var21;
      this.posZ = var25 + var13 * var21;
      this.setPosition(this.posX, this.posY, this.posZ);
      var31 = this.motionX;
      var33 = this.motionZ;
      if (this.riddenByEntity != null) {
         var31 *= 0.75D;
         var33 *= 0.75D;
      }

      double var35 = this.getMaximumSpeed();
      var31 = MathHelper.clamp_double(var31, -var35, var35);
      var33 = MathHelper.clamp_double(var33, -var35, var35);
      this.moveEntity(var31, 0.0D, var33);
      if (var10[0][1] != 0 && MathHelper.floor_double(this.posX) - var1.getX() == var10[0][0] && MathHelper.floor_double(this.posZ) - var1.getZ() == var10[0][2]) {
         this.setPosition(this.posX, this.posY + (double)var10[0][1], this.posZ);
      } else if (var10[1][1] != 0 && MathHelper.floor_double(this.posX) - var1.getX() == var10[1][0] && MathHelper.floor_double(this.posZ) - var1.getZ() == var10[1][2]) {
         this.setPosition(this.posX, this.posY + (double)var10[1][1], this.posZ);
      }

      this.applyDrag();
      Vec3 var37 = this.func_70489_a(this.posX, this.posY, this.posZ);
      if (var37 != null && var3 != null) {
         double var38 = (var3.yCoord - var37.yCoord) * 0.05D;
         var19 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
         if (var19 > 0.0D) {
            this.motionX = this.motionX / var19 * (var19 + var38);
            this.motionZ = this.motionZ / var19 * (var19 + var38);
         }

         this.setPosition(this.posX, var37.yCoord, this.posZ);
      }

      int var44 = MathHelper.floor_double(this.posX);
      int var39 = MathHelper.floor_double(this.posZ);
      if (var44 != var1.getX() || var39 != var1.getZ()) {
         var19 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
         this.motionX = var19 * (double)(var44 - var1.getX());
         this.motionZ = var19 * (double)(var39 - var1.getZ());
      }

      if (var4) {
         double var40 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
         if (var40 > 0.01D) {
            double var42 = 0.06D;
            this.motionX += this.motionX / var40 * var42;
            this.motionZ += this.motionZ / var40 * var42;
         } else if (var9 == BlockRailBase.EnumRailDirection.EAST_WEST) {
            if (this.worldObj.getBlockState(var1.west()).getBlock().isNormalCube()) {
               this.motionX = 0.02D;
            } else if (this.worldObj.getBlockState(var1.east()).getBlock().isNormalCube()) {
               this.motionX = -0.02D;
            }
         } else if (var9 == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
            if (this.worldObj.getBlockState(var1.north()).getBlock().isNormalCube()) {
               this.motionZ = 0.02D;
            } else if (this.worldObj.getBlockState(var1.south()).getBlock().isNormalCube()) {
               this.motionZ = -0.02D;
            }
         }
      }

   }

   public static EntityMinecart func_180458_a(World var0, double var1, double var3, double var5, EntityMinecart.EnumMinecartType var7) {
      switch($SWITCH_TABLE$net$minecraft$entity$item$EntityMinecart$EnumMinecartType()[var7.ordinal()]) {
      case 2:
         return new EntityMinecartChest(var0, var1, var3, var5);
      case 3:
         return new EntityMinecartFurnace(var0, var1, var3, var5);
      case 4:
         return new EntityMinecartTNT(var0, var1, var3, var5);
      case 5:
         return new EntityMinecartMobSpawner(var0, var1, var3, var5);
      case 6:
         return new EntityMinecartHopper(var0, var1, var3, var5);
      case 7:
         return new EntityMinecartCommandBlock(var0, var1, var3, var5);
      default:
         return new EntityMinecartEmpty(var0, var1, var3, var5);
      }
   }

   public void setDamage(float var1) {
      this.dataWatcher.updateObject(19, var1);
   }

   protected void applyDrag() {
      if (this.riddenByEntity != null) {
         this.motionX *= 0.996999979019165D;
         this.motionY *= 0.0D;
         this.motionZ *= 0.996999979019165D;
      } else {
         this.motionX *= 0.9599999785423279D;
         this.motionY *= 0.0D;
         this.motionZ *= 0.9599999785423279D;
      }

   }

   public void setVelocity(double var1, double var3, double var5) {
      this.velocityX = this.motionX = var1;
      this.velocityY = this.motionY = var3;
      this.velocityZ = this.motionZ = var5;
   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (!this.worldObj.isRemote && !this.isDead) {
         if (this.isEntityInvulnerable(var1)) {
            return false;
         } else {
            this.setRollingDirection(-this.getRollingDirection());
            this.setRollingAmplitude(10);
            this.setBeenAttacked();
            this.setDamage(this.getDamage() + var2 * 10.0F);
            boolean var3 = var1.getEntity() instanceof EntityPlayer && ((EntityPlayer)var1.getEntity()).capabilities.isCreativeMode;
            if (var3 || this.getDamage() > 40.0F) {
               if (this.riddenByEntity != null) {
                  this.riddenByEntity.mountEntity((Entity)null);
               }

               if (var3 && this != false) {
                  this.setDead();
               } else {
                  this.killMinecart(var1);
               }
            }

            return true;
         }
      } else {
         return true;
      }
   }

   public boolean canBePushed() {
      return true;
   }

   public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9, boolean var10) {
      this.minecartX = var1;
      this.minecartY = var3;
      this.minecartZ = var5;
      this.minecartYaw = (double)var7;
      this.minecartPitch = (double)var8;
      this.turnProgress = var9 + 2;
      this.motionX = this.velocityX;
      this.motionY = this.velocityY;
      this.motionZ = this.velocityZ;
   }

   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   protected void readEntityFromNBT(NBTTagCompound var1) {
      if (var1.getBoolean("CustomDisplayTile")) {
         int var2 = var1.getInteger("DisplayData");
         Block var3;
         if (var1.hasKey("DisplayTile", 8)) {
            var3 = Block.getBlockFromName(var1.getString("DisplayTile"));
            if (var3 == null) {
               this.func_174899_a(Blocks.air.getDefaultState());
            } else {
               this.func_174899_a(var3.getStateFromMeta(var2));
            }
         } else {
            var3 = Block.getBlockById(var1.getInteger("DisplayTile"));
            if (var3 == null) {
               this.func_174899_a(Blocks.air.getDefaultState());
            } else {
               this.func_174899_a(var3.getStateFromMeta(var2));
            }
         }

         this.setDisplayTileOffset(var1.getInteger("DisplayOffset"));
      }

      if (var1.hasKey("CustomName", 8) && var1.getString("CustomName").length() > 0) {
         this.entityName = var1.getString("CustomName");
      }

   }

   protected void entityInit() {
      this.dataWatcher.addObject(17, new Integer(0));
      this.dataWatcher.addObject(18, new Integer(1));
      this.dataWatcher.addObject(19, new Float(0.0F));
      this.dataWatcher.addObject(20, new Integer(0));
      this.dataWatcher.addObject(21, new Integer(6));
      this.dataWatcher.addObject(22, (byte)0);
   }

   public String getCustomNameTag() {
      return this.entityName;
   }

   public EntityMinecart(World var1, double var2, double var4, double var6) {
      this(var1);
      this.setPosition(var2, var4, var6);
      this.motionX = 0.0D;
      this.motionY = 0.0D;
      this.motionZ = 0.0D;
      this.prevPosX = var2;
      this.prevPosY = var4;
      this.prevPosZ = var6;
   }

   public abstract EntityMinecart.EnumMinecartType getMinecartType();

   protected void writeEntityToNBT(NBTTagCompound var1) {
      if (this != false) {
         var1.setBoolean("CustomDisplayTile", true);
         IBlockState var2 = this.getDisplayTile();
         ResourceLocation var3 = (ResourceLocation)Block.blockRegistry.getNameForObject(var2.getBlock());
         var1.setString("DisplayTile", var3 == null ? "" : var3.toString());
         var1.setInteger("DisplayData", var2.getBlock().getMetaFromState(var2));
         var1.setInteger("DisplayOffset", this.getDisplayTileOffset());
      }

      if (this.entityName != null && this.entityName.length() > 0) {
         var1.setString("CustomName", this.entityName);
      }

   }

   public void killMinecart(DamageSource var1) {
      this.setDead();
      if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
         ItemStack var2 = new ItemStack(Items.minecart, 1);
         if (this.entityName != null) {
            var2.setStackDisplayName(this.entityName);
         }

         this.entityDropItem(var2, 0.0F);
      }

   }

   public void applyEntityCollision(Entity var1) {
      if (!this.worldObj.isRemote && !var1.noClip && !this.noClip && var1 != this.riddenByEntity) {
         if (var1 instanceof EntityLivingBase && !(var1 instanceof EntityPlayer) && !(var1 instanceof EntityIronGolem) && this.getMinecartType() == EntityMinecart.EnumMinecartType.RIDEABLE && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01D && this.riddenByEntity == null && var1.ridingEntity == null) {
            var1.mountEntity(this);
         }

         double var2 = var1.posX - this.posX;
         double var4 = var1.posZ - this.posZ;
         double var6 = var2 * var2 + var4 * var4;
         if (var6 >= 9.999999747378752E-5D) {
            var6 = (double)MathHelper.sqrt_double(var6);
            var2 /= var6;
            var4 /= var6;
            double var8 = 1.0D / var6;
            if (var8 > 1.0D) {
               var8 = 1.0D;
            }

            var2 *= var8;
            var4 *= var8;
            var2 *= 0.10000000149011612D;
            var4 *= 0.10000000149011612D;
            var2 *= (double)(1.0F - this.entityCollisionReduction);
            var4 *= (double)(1.0F - this.entityCollisionReduction);
            var2 *= 0.5D;
            var4 *= 0.5D;
            if (var1 instanceof EntityMinecart) {
               double var10 = var1.posX - this.posX;
               double var12 = var1.posZ - this.posZ;
               Vec3 var14 = (new Vec3(var10, 0.0D, var12)).normalize();
               Vec3 var15 = (new Vec3((double)MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F), 0.0D, (double)MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F))).normalize();
               double var16 = Math.abs(var14.dotProduct(var15));
               if (var16 < 0.800000011920929D) {
                  return;
               }

               double var18 = var1.motionX + this.motionX;
               double var20 = var1.motionZ + this.motionZ;
               if (((EntityMinecart)var1).getMinecartType() == EntityMinecart.EnumMinecartType.FURNACE && this.getMinecartType() != EntityMinecart.EnumMinecartType.FURNACE) {
                  this.motionX *= 0.20000000298023224D;
                  this.motionZ *= 0.20000000298023224D;
                  this.addVelocity(var1.motionX - var2, 0.0D, var1.motionZ - var4);
                  var1.motionX *= 0.949999988079071D;
                  var1.motionZ *= 0.949999988079071D;
               } else if (((EntityMinecart)var1).getMinecartType() != EntityMinecart.EnumMinecartType.FURNACE && this.getMinecartType() == EntityMinecart.EnumMinecartType.FURNACE) {
                  var1.motionX *= 0.20000000298023224D;
                  var1.motionZ *= 0.20000000298023224D;
                  var1.addVelocity(this.motionX + var2, 0.0D, this.motionZ + var4);
                  this.motionX *= 0.949999988079071D;
                  this.motionZ *= 0.949999988079071D;
               } else {
                  var18 /= 2.0D;
                  var20 /= 2.0D;
                  this.motionX *= 0.20000000298023224D;
                  this.motionZ *= 0.20000000298023224D;
                  this.addVelocity(var18 - var2, 0.0D, var20 - var4);
                  var1.motionX *= 0.20000000298023224D;
                  var1.motionZ *= 0.20000000298023224D;
                  var1.addVelocity(var18 + var2, 0.0D, var20 + var4);
               }
            } else {
               this.addVelocity(-var2, 0.0D, -var4);
               var1.addVelocity(var2 / 4.0D, 0.0D, var4 / 4.0D);
            }
         }
      }

   }

   public IBlockState getDisplayTile() {
      return this != false ? this.getDefaultDisplayTile() : Block.getStateById(this.getDataWatcher().getWatchableObjectInt(20));
   }

   public void setRollingAmplitude(int var1) {
      this.dataWatcher.updateObject(17, var1);
   }

   public Vec3 func_70495_a(double var1, double var3, double var5, double var7) {
      int var9 = MathHelper.floor_double(var1);
      int var10 = MathHelper.floor_double(var3);
      int var11 = MathHelper.floor_double(var5);
      if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(var9, var10 - 1, var11))) {
         --var10;
      }

      IBlockState var12 = this.worldObj.getBlockState(new BlockPos(var9, var10, var11));
      if (BlockRailBase.isRailBlock(var12)) {
         BlockRailBase.EnumRailDirection var13 = (BlockRailBase.EnumRailDirection)var12.getValue(((BlockRailBase)var12.getBlock()).getShapeProperty());
         var3 = (double)var10;
         if (var13.isAscending()) {
            var3 = (double)(var10 + 1);
         }

         int[][] var14 = matrix[var13.getMetadata()];
         double var15 = (double)(var14[1][0] - var14[0][0]);
         double var17 = (double)(var14[1][2] - var14[0][2]);
         double var19 = Math.sqrt(var15 * var15 + var17 * var17);
         var15 /= var19;
         var17 /= var19;
         var1 += var15 * var7;
         var5 += var17 * var7;
         if (var14[0][1] != 0 && MathHelper.floor_double(var1) - var9 == var14[0][0] && MathHelper.floor_double(var5) - var11 == var14[0][2]) {
            var3 += (double)var14[0][1];
         } else if (var14[1][1] != 0 && MathHelper.floor_double(var1) - var9 == var14[1][0] && MathHelper.floor_double(var5) - var11 == var14[1][2]) {
            var3 += (double)var14[1][1];
         }

         return this.func_70489_a(var1, var3, var5);
      } else {
         return null;
      }
   }

   protected void moveDerailedMinecart() {
      double var1 = this.getMaximumSpeed();
      this.motionX = MathHelper.clamp_double(this.motionX, -var1, var1);
      this.motionZ = MathHelper.clamp_double(this.motionZ, -var1, var1);
      if (this.onGround) {
         this.motionX *= 0.5D;
         this.motionY *= 0.5D;
         this.motionZ *= 0.5D;
      }

      this.moveEntity(this.motionX, this.motionY, this.motionZ);
      if (!this.onGround) {
         this.motionX *= 0.949999988079071D;
         this.motionY *= 0.949999988079071D;
         this.motionZ *= 0.949999988079071D;
      }

   }

   public void setHasDisplayTile(boolean var1) {
      this.getDataWatcher().updateObject(22, (byte)(var1 ? 1 : 0));
   }

   public int getDisplayTileOffset() {
      return this != false ? this.getDefaultDisplayTileOffset() : this.getDataWatcher().getWatchableObjectInt(21);
   }

   public void setDead() {
      super.setDead();
   }

   public void func_174899_a(IBlockState var1) {
      this.getDataWatcher().updateObject(20, Block.getStateId(var1));
      this.setHasDisplayTile(true);
   }

   static int[] $SWITCH_TABLE$net$minecraft$block$BlockRailBase$EnumRailDirection() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$block$BlockRailBase$EnumRailDirection;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[BlockRailBase.EnumRailDirection.values().length];

         try {
            var0[BlockRailBase.EnumRailDirection.ASCENDING_EAST.ordinal()] = 3;
         } catch (NoSuchFieldError var10) {
         }

         try {
            var0[BlockRailBase.EnumRailDirection.ASCENDING_NORTH.ordinal()] = 5;
         } catch (NoSuchFieldError var9) {
         }

         try {
            var0[BlockRailBase.EnumRailDirection.ASCENDING_SOUTH.ordinal()] = 6;
         } catch (NoSuchFieldError var8) {
         }

         try {
            var0[BlockRailBase.EnumRailDirection.ASCENDING_WEST.ordinal()] = 4;
         } catch (NoSuchFieldError var7) {
         }

         try {
            var0[BlockRailBase.EnumRailDirection.EAST_WEST.ordinal()] = 2;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[BlockRailBase.EnumRailDirection.NORTH_EAST.ordinal()] = 10;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[BlockRailBase.EnumRailDirection.NORTH_SOUTH.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[BlockRailBase.EnumRailDirection.NORTH_WEST.ordinal()] = 9;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[BlockRailBase.EnumRailDirection.SOUTH_EAST.ordinal()] = 7;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[BlockRailBase.EnumRailDirection.SOUTH_WEST.ordinal()] = 8;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$block$BlockRailBase$EnumRailDirection = var0;
         return var0;
      }
   }

   public AxisAlignedBB getCollisionBox(Entity var1) {
      return var1.canBePushed() ? var1.getEntityBoundingBox() : null;
   }

   public double getMountedYOffset() {
      return 0.0D;
   }

   public IChatComponent getDisplayName() {
      if (this != null) {
         ChatComponentText var3 = new ChatComponentText(this.entityName);
         var3.getChatStyle().setChatHoverEvent(this.getHoverEvent());
         var3.getChatStyle().setInsertion(this.getUniqueID().toString());
         return var3;
      } else {
         ChatComponentTranslation var1 = new ChatComponentTranslation(this.getName(), new Object[0]);
         var1.getChatStyle().setChatHoverEvent(this.getHoverEvent());
         var1.getChatStyle().setInsertion(this.getUniqueID().toString());
         return var1;
      }
   }

   public void onActivatorRailPass(int var1, int var2, int var3, boolean var4) {
   }

   public AxisAlignedBB getCollisionBoundingBox() {
      return null;
   }

   public void setRollingDirection(int var1) {
      this.dataWatcher.updateObject(18, var1);
   }

   public EntityMinecart(World var1) {
      super(var1);
      this.preventEntitySpawning = true;
      this.setSize(0.98F, 0.7F);
   }

   public void onUpdate() {
      if (this.getRollingAmplitude() > 0) {
         this.setRollingAmplitude(this.getRollingAmplitude() - 1);
      }

      if (this.getDamage() > 0.0F) {
         this.setDamage(this.getDamage() - 1.0F);
      }

      if (this.posY < -64.0D) {
         this.kill();
      }

      int var2;
      if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer) {
         this.worldObj.theProfiler.startSection("portal");
         MinecraftServer var1 = ((WorldServer)this.worldObj).getMinecraftServer();
         var2 = this.getMaxInPortalTime();
         if (this.inPortal) {
            if (var1.getAllowNether()) {
               if (this.ridingEntity == null && this.portalCounter++ >= var2) {
                  this.portalCounter = var2;
                  this.timeUntilPortal = this.getPortalCooldown();
                  byte var3;
                  if (this.worldObj.provider.getDimensionId() == -1) {
                     var3 = 0;
                  } else {
                     var3 = -1;
                  }

                  this.travelToDimension(var3);
               }

               this.inPortal = false;
            }
         } else {
            if (this.portalCounter > 0) {
               this.portalCounter -= 4;
            }

            if (this.portalCounter < 0) {
               this.portalCounter = 0;
            }
         }

         if (this.timeUntilPortal > 0) {
            --this.timeUntilPortal;
         }

         this.worldObj.theProfiler.endSection();
      }

      if (this.worldObj.isRemote) {
         if (this.turnProgress > 0) {
            double var14 = this.posX + (this.minecartX - this.posX) / (double)this.turnProgress;
            double var16 = this.posY + (this.minecartY - this.posY) / (double)this.turnProgress;
            double var5 = this.posZ + (this.minecartZ - this.posZ) / (double)this.turnProgress;
            double var7 = MathHelper.wrapAngleTo180_double(this.minecartYaw - (double)this.rotationYaw);
            this.rotationYaw = (float)((double)this.rotationYaw + var7 / (double)this.turnProgress);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.minecartPitch - (double)this.rotationPitch) / (double)this.turnProgress);
            --this.turnProgress;
            this.setPosition(var14, var16, var5);
            this.setRotation(this.rotationYaw, this.rotationPitch);
         } else {
            this.setPosition(this.posX, this.posY, this.posZ);
            this.setRotation(this.rotationYaw, this.rotationPitch);
         }
      } else {
         this.prevPosX = this.posX;
         this.prevPosY = this.posY;
         this.prevPosZ = this.posZ;
         this.motionY -= 0.03999999910593033D;
         int var15 = MathHelper.floor_double(this.posX);
         var2 = MathHelper.floor_double(this.posY);
         int var17 = MathHelper.floor_double(this.posZ);
         if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(var15, var2 - 1, var17))) {
            --var2;
         }

         BlockPos var4 = new BlockPos(var15, var2, var17);
         IBlockState var18 = this.worldObj.getBlockState(var4);
         if (BlockRailBase.isRailBlock(var18)) {
            this.func_180460_a(var4, var18);
            if (var18.getBlock() == Blocks.activator_rail) {
               this.onActivatorRailPass(var15, var2, var17, (Boolean)var18.getValue(BlockRailPowered.POWERED));
            }
         } else {
            this.moveDerailedMinecart();
         }

         this.doBlockCollisions();
         this.rotationPitch = 0.0F;
         double var6 = this.prevPosX - this.posX;
         double var8 = this.prevPosZ - this.posZ;
         if (var6 * var6 + var8 * var8 > 0.001D) {
            this.rotationYaw = (float)(MathHelper.func_181159_b(var8, var6) * 180.0D / 3.141592653589793D);
            if (this.isInReverse) {
               this.rotationYaw += 180.0F;
            }
         }

         double var10 = (double)MathHelper.wrapAngleTo180_float(this.rotationYaw - this.prevRotationYaw);
         if (var10 < -170.0D || var10 >= 170.0D) {
            this.rotationYaw += 180.0F;
            this.isInReverse = !this.isInReverse;
         }

         this.setRotation(this.rotationYaw, this.rotationPitch);
         Iterator var13 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D)).iterator();

         while(var13.hasNext()) {
            Entity var12 = (Entity)var13.next();
            if (var12 != this.riddenByEntity && var12.canBePushed() && var12 instanceof EntityMinecart) {
               var12.applyEntityCollision(this);
            }
         }

         if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
            if (this.riddenByEntity.ridingEntity == this) {
               this.riddenByEntity.ridingEntity = null;
            }

            this.riddenByEntity = null;
         }

         this.handleWaterMovement();
      }

   }

   public void setDisplayTileOffset(int var1) {
      this.getDataWatcher().updateObject(21, var1);
      this.setHasDisplayTile(true);
   }

   public void setCustomNameTag(String var1) {
      this.entityName = var1;
   }

   public static enum EnumMinecartType {
      private final String name;
      FURNACE(2, "MinecartFurnace"),
      TNT(3, "MinecartTNT");

      private final int networkID;
      SPAWNER(4, "MinecartSpawner");

      private static final EntityMinecart.EnumMinecartType[] ENUM$VALUES = new EntityMinecart.EnumMinecartType[]{RIDEABLE, CHEST, FURNACE, TNT, SPAWNER, HOPPER, COMMAND_BLOCK};
      RIDEABLE(0, "MinecartRideable"),
      COMMAND_BLOCK(6, "MinecartCommandBlock"),
      CHEST(1, "MinecartChest"),
      HOPPER(5, "MinecartHopper");

      private static final Map ID_LOOKUP = Maps.newHashMap();

      public static EntityMinecart.EnumMinecartType byNetworkID(int var0) {
         EntityMinecart.EnumMinecartType var1 = (EntityMinecart.EnumMinecartType)ID_LOOKUP.get(var0);
         return var1 == null ? RIDEABLE : var1;
      }

      public int getNetworkID() {
         return this.networkID;
      }

      private EnumMinecartType(int var3, String var4) {
         this.networkID = var3;
         this.name = var4;
      }

      public String getName() {
         return this.name;
      }

      static {
         EntityMinecart.EnumMinecartType[] var3;
         int var2 = (var3 = values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            EntityMinecart.EnumMinecartType var0 = var3[var1];
            ID_LOOKUP.put(var0.getNetworkID(), var0);
         }

      }
   }
}
