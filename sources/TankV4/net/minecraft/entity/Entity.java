package net.minecraft.entity;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.HoverEvent;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class Entity implements ICommandSender {
   public Entity ridingEntity;
   public float prevRotationPitch;
   public float distanceWalkedOnStepModified;
   private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
   public boolean isCollidedVertically;
   protected boolean firstUpdate;
   public float prevDistanceWalkedModified;
   public int serverPosZ;
   public double motionX;
   public boolean ignoreFrustumCheck;
   public boolean isDead;
   protected Random rand;
   private boolean invulnerable;
   public int chunkCoordZ;
   public float height;
   public double motionY;
   protected EnumFacing field_181018_ap;
   public boolean onGround;
   public int serverPosX;
   public int serverPosY;
   public float prevRotationYaw;
   public int chunkCoordX;
   private int fire;
   public double prevPosX;
   private double entityRiderYawDelta;
   protected UUID entityUniqueID;
   protected Vec3 field_181017_ao;
   public boolean isCollided;
   public float rotationYaw;
   public float stepHeight;
   public double renderDistanceWeight;
   public double prevPosY;
   public boolean velocityChanged;
   private final CommandResultStats cmdResultStats;
   public float distanceWalkedModified;
   public double posX;
   public int chunkCoordY;
   public boolean isCollidedHorizontally;
   public boolean noClip;
   public double lastTickPosZ;
   public World worldObj;
   public boolean preventEntitySpawning;
   public int ticksExisted;
   protected BlockPos field_181016_an;
   public double posY;
   public int dimension;
   public int fireResistance;
   public double prevPosZ;
   private boolean isOutsideBorder;
   public boolean addedToChunk;
   public boolean forceSpawn;
   protected boolean inPortal;
   public float entityCollisionReduction;
   private static int nextEntityID;
   public boolean inWater;
   private int entityId;
   public float fallDistance;
   protected boolean isInWeb;
   protected DataWatcher dataWatcher;
   public AxisAlignedBB boundingBox;
   public boolean isAirBorne;
   protected boolean isImmuneToFire;
   private int nextStepDistance;
   protected int portalCounter;
   public double posZ;
   public float width;
   public int hurtResistantTime;
   public float rotationPitch;
   public int timeUntilPortal;
   public double lastTickPosY;
   private double entityRiderPitchDelta;
   public Entity riddenByEntity;
   public double motionZ;
   public double lastTickPosX;

   public boolean getAlwaysRenderNameTagForRender() {
      return this.getAlwaysRenderNameTag();
   }

   public float getEyeHeight() {
      return this.height * 0.85F;
   }

   public boolean sendCommandFeedback() {
      return false;
   }

   public void readFromNBT(NBTTagCompound var1) {
      try {
         NBTTagList var2 = var1.getTagList("Pos", 6);
         NBTTagList var7 = var1.getTagList("Motion", 6);
         NBTTagList var8 = var1.getTagList("Rotation", 5);
         this.motionX = var7.getDoubleAt(0);
         this.motionY = var7.getDoubleAt(1);
         this.motionZ = var7.getDoubleAt(2);
         if (Math.abs(this.motionX) > 10.0D) {
            this.motionX = 0.0D;
         }

         if (Math.abs(this.motionY) > 10.0D) {
            this.motionY = 0.0D;
         }

         if (Math.abs(this.motionZ) > 10.0D) {
            this.motionZ = 0.0D;
         }

         this.prevPosX = this.lastTickPosX = this.posX = var2.getDoubleAt(0);
         this.prevPosY = this.lastTickPosY = this.posY = var2.getDoubleAt(1);
         this.prevPosZ = this.lastTickPosZ = this.posZ = var2.getDoubleAt(2);
         this.prevRotationYaw = this.rotationYaw = var8.getFloatAt(0);
         this.prevRotationPitch = this.rotationPitch = var8.getFloatAt(1);
         this.setRotationYawHead(this.rotationYaw);
         this.func_181013_g(this.rotationYaw);
         this.fallDistance = var1.getFloat("FallDistance");
         this.fire = var1.getShort("Fire");
         this.setAir(var1.getShort("Air"));
         this.onGround = var1.getBoolean("OnGround");
         this.dimension = var1.getInteger("Dimension");
         this.invulnerable = var1.getBoolean("Invulnerable");
         this.timeUntilPortal = var1.getInteger("PortalCooldown");
         if (var1.hasKey("UUIDMost", 4) && var1.hasKey("UUIDLeast", 4)) {
            this.entityUniqueID = new UUID(var1.getLong("UUIDMost"), var1.getLong("UUIDLeast"));
         } else if (var1.hasKey("UUID", 8)) {
            this.entityUniqueID = UUID.fromString(var1.getString("UUID"));
         }

         this.setPosition(this.posX, this.posY, this.posZ);
         this.setRotation(this.rotationYaw, this.rotationPitch);
         if (var1.hasKey("CustomName", 8) && var1.getString("CustomName").length() > 0) {
            this.setCustomNameTag(var1.getString("CustomName"));
         }

         this.setAlwaysRenderNameTag(var1.getBoolean("CustomNameVisible"));
         this.cmdResultStats.readStatsFromNBT(var1);
         this.setSilent(var1.getBoolean("Silent"));
         this.readEntityFromNBT(var1);
         if (this.shouldSetPosAfterLoading()) {
            this.setPosition(this.posX, this.posY, this.posZ);
         }

      } catch (Throwable var6) {
         CrashReport var3 = CrashReport.makeCrashReport(var6, "Loading entity NBT");
         CrashReportCategory var4 = var3.makeCategory("Entity being loaded");
         this.addEntityCrashInfo(var4);
         throw new ReportedException(var3);
      }
   }

   protected void setOnFireFromLava() {
      if (!this.isImmuneToFire) {
         this.attackEntityFrom(DamageSource.lava, 4.0F);
         this.setFire(15);
      }

   }

   public void setAir(int var1) {
      this.dataWatcher.updateObject(1, (short)var1);
   }

   public void moveToBlockPosAndAngles(BlockPos var1, float var2, float var3) {
      this.setLocationAndAngles((double)var1.getX() + 0.5D, (double)var1.getY(), (double)var1.getZ() + 0.5D, var2, var3);
   }

   public void setAngles(float var1, float var2) {
      float var3 = this.rotationPitch;
      float var4 = this.rotationYaw;
      this.rotationYaw = (float)((double)this.rotationYaw + (double)var1 * 0.15D);
      this.rotationPitch = (float)((double)this.rotationPitch - (double)var2 * 0.15D);
      this.rotationPitch = MathHelper.clamp_float(this.rotationPitch, -90.0F, 90.0F);
      this.prevRotationPitch += this.rotationPitch - var3;
      this.prevRotationYaw += this.rotationYaw - var4;
   }

   public boolean isInsideOfMaterial(Material var1) {
      double var2 = this.posY + (double)this.getEyeHeight();
      BlockPos var4 = new BlockPos(this.posX, var2, this.posZ);
      IBlockState var5 = this.worldObj.getBlockState(var4);
      Block var6 = var5.getBlock();
      if (var6.getMaterial() == var1) {
         float var7 = BlockLiquid.getLiquidHeightPercent(var5.getBlock().getMetaFromState(var5)) - 0.11111111F;
         float var8 = (float)(var4.getY() + 1) - var7;
         boolean var9 = var2 < (double)var8;
         return !var9 && this instanceof EntityPlayer ? false : var9;
      } else {
         return false;
      }
   }

   public boolean isBurning() {
      boolean var1 = this.worldObj != null && this.worldObj.isRemote;
      if (!this.isImmuneToFire) {
         if (this.fire > 0) {
            return true;
         }

         if (var1) {
            ;
         }
      }

      return false;
   }

   public Entity[] getParts() {
      return null;
   }

   public boolean hitByEntity(Entity var1) {
      return false;
   }

   public void setCurrentItemOrArmor(int var1, ItemStack var2) {
   }

   public void setSprinting(boolean var1) {
      this.setFlag(3, var1);
   }

   public EntityItem entityDropItem(ItemStack var1, float var2) {
      if (var1.stackSize != 0 && var1.getItem() != null) {
         EntityItem var3 = new EntityItem(this.worldObj, this.posX, this.posY + (double)var2, this.posZ, var1);
         var3.setDefaultPickupDelay();
         this.worldObj.spawnEntityInWorld(var3);
         return var3;
      } else {
         return null;
      }
   }

   public boolean isInLava() {
      return this.worldObj.isMaterialInBB(this.getEntityBoundingBox().expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.lava);
   }

   public float getBrightness(float var1) {
      BlockPos var2 = new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
      return this.worldObj.isBlockLoaded(var2) ? this.worldObj.getLightBrightness(var2) : 0.0F;
   }

   public String toString() {
      return String.format("%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]", this.getClass().getSimpleName(), this.getName(), this.entityId, this.worldObj == null ? "~NULL~" : this.worldObj.getWorldInfo().getWorldName(), this.posX, this.posY, this.posZ);
   }

   public int getAir() {
      return this.dataWatcher.getWatchableObjectShort(1);
   }

   public void setOutsideBorder(boolean var1) {
      this.isOutsideBorder = var1;
   }

   public void moveFlying(float var1, float var2, float var3) {
      float var4 = var1 * var1 + var2 * var2;
      if (var4 >= 1.0E-4F) {
         var4 = MathHelper.sqrt_float(var4);
         if (var4 < 1.0F) {
            var4 = 1.0F;
         }

         var4 = var3 / var4;
         var1 *= var4;
         var2 *= var4;
         float var5 = MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F);
         float var6 = MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F);
         this.motionX += (double)(var1 * var6 - var2 * var5);
         this.motionZ += (double)(var2 * var6 + var1 * var5);
      }

   }

   public boolean canAttackWithItem() {
      return true;
   }

   public boolean isWet() {
      return this.inWater || this.worldObj.canLightningStrike(new BlockPos(this.posX, this.posY, this.posZ)) || this.worldObj.canLightningStrike(new BlockPos(this.posX, this.posY + (double)this.height, this.posZ));
   }

   public void setDead() {
      this.isDead = true;
   }

   public void onChunkLoad() {
   }

   protected abstract void readEntityFromNBT(NBTTagCompound var1);

   public boolean isEntityEqual(Entity var1) {
      return this == var1;
   }

   public void clientUpdateEntityNBT(NBTTagCompound var1) {
   }

   public boolean isInWater() {
      return this.inWater;
   }

   protected boolean shouldSetPosAfterLoading() {
      return true;
   }

   protected void setBeenAttacked() {
      this.velocityChanged = true;
   }

   public void addToPlayerScore(Entity var1, int var2) {
   }

   public void setEating(boolean var1) {
      this.setFlag(4, var1);
   }

   public void onCollideWithPlayer(EntityPlayer var1) {
   }

   public void setVelocity(double var1, double var3, double var5) {
      this.motionX = var1;
      this.motionY = var3;
      this.motionZ = var5;
   }

   public void onEntityUpdate() {
      this.worldObj.theProfiler.startSection("entityBaseTick");
      if (this.ridingEntity != null && this.ridingEntity.isDead) {
         this.ridingEntity = null;
      }

      this.prevDistanceWalkedModified = this.distanceWalkedModified;
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.prevRotationPitch = this.rotationPitch;
      this.prevRotationYaw = this.rotationYaw;
      if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer) {
         this.worldObj.theProfiler.startSection("portal");
         MinecraftServer var1 = ((WorldServer)this.worldObj).getMinecraftServer();
         int var2 = this.getMaxInPortalTime();
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

      this.spawnRunningParticles();
      this.handleWaterMovement();
      if (this.worldObj.isRemote) {
         this.fire = 0;
      } else if (this.fire > 0) {
         if (this.isImmuneToFire) {
            this.fire -= 4;
            if (this.fire < 0) {
               this.fire = 0;
            }
         } else {
            if (this.fire % 20 == 0) {
               this.attackEntityFrom(DamageSource.onFire, 1.0F);
            }

            --this.fire;
         }
      }

      if (this.isInLava()) {
         this.setOnFireFromLava();
         this.fallDistance *= 0.5F;
      }

      if (this.posY < -64.0D) {
         this.kill();
      }

      if (!this.worldObj.isRemote) {
         this.setFlag(0, this.fire > 0);
      }

      this.firstUpdate = false;
      this.worldObj.theProfiler.endSection();
   }

   protected boolean pushOutOfBlocks(double var1, double var3, double var5) {
      BlockPos var7 = new BlockPos(var1, var3, var5);
      double var8 = var1 - (double)var7.getX();
      double var10 = var3 - (double)var7.getY();
      double var12 = var5 - (double)var7.getZ();
      List var14 = this.worldObj.func_147461_a(this.getEntityBoundingBox());
      if (var14.isEmpty() && !this.worldObj.isBlockFullCube(var7)) {
         return false;
      } else {
         byte var15 = 3;
         double var16 = 9999.0D;
         if (!this.worldObj.isBlockFullCube(var7.west()) && var8 < var16) {
            var16 = var8;
            var15 = 0;
         }

         if (!this.worldObj.isBlockFullCube(var7.east()) && 1.0D - var8 < var16) {
            var16 = 1.0D - var8;
            var15 = 1;
         }

         if (!this.worldObj.isBlockFullCube(var7.up()) && 1.0D - var10 < var16) {
            var16 = 1.0D - var10;
            var15 = 3;
         }

         if (!this.worldObj.isBlockFullCube(var7.north()) && var12 < var16) {
            var16 = var12;
            var15 = 4;
         }

         if (!this.worldObj.isBlockFullCube(var7.south()) && 1.0D - var12 < var16) {
            var16 = 1.0D - var12;
            var15 = 5;
         }

         float var18 = this.rand.nextFloat() * 0.2F + 0.1F;
         if (var15 == 0) {
            this.motionX = (double)(-var18);
         }

         if (var15 == 1) {
            this.motionX = (double)var18;
         }

         if (var15 == 3) {
            this.motionY = (double)var18;
         }

         if (var15 == 4) {
            this.motionZ = (double)(-var18);
         }

         if (var15 == 5) {
            this.motionZ = (double)var18;
         }

         return true;
      }
   }

   public void onKillEntity(EntityLivingBase var1) {
   }

   public EnumFacing getHorizontalFacing() {
      return EnumFacing.getHorizontal(MathHelper.floor_double((double)(this.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
   }

   protected void playStepSound(BlockPos var1, Block var2) {
      Block.SoundType var3 = var2.stepSound;
      if (this.worldObj.getBlockState(var1.up()).getBlock() == Blocks.snow_layer) {
         var3 = Blocks.snow_layer.stepSound;
         this.playSound(var3.getStepSound(), var3.getVolume() * 0.15F, var3.getFrequency());
      } else if (!var2.getMaterial().isLiquid()) {
         this.playSound(var3.getStepSound(), var3.getVolume() * 0.15F, var3.getFrequency());
      }

   }

   public boolean doesEntityNotTriggerPressurePlate() {
      return false;
   }

   public double getMountedYOffset() {
      return (double)this.height * 0.75D;
   }

   public void setPositionAndRotation(double var1, double var3, double var5, float var7, float var8) {
      this.prevPosX = this.posX = var1;
      this.prevPosY = this.posY = var3;
      this.prevPosZ = this.posZ = var5;
      this.prevRotationYaw = this.rotationYaw = var7;
      this.prevRotationPitch = this.rotationPitch = var8;
      double var9 = (double)(this.prevRotationYaw - var7);
      if (var9 < -180.0D) {
         this.prevRotationYaw += 360.0F;
      }

      if (var9 >= 180.0D) {
         this.prevRotationYaw -= 360.0F;
      }

      this.setPosition(this.posX, this.posY, this.posZ);
      this.setRotation(var7, var8);
   }

   public void addEntityCrashInfo(CrashReportCategory var1) {
      var1.addCrashSectionCallable("Entity Type", new Callable(this) {
         final Entity this$0;

         public String call() throws Exception {
            return EntityList.getEntityString(this.this$0) + " (" + this.this$0.getClass().getCanonicalName() + ")";
         }

         {
            this.this$0 = var1;
         }

         public Object call() throws Exception {
            return this.call();
         }
      });
      var1.addCrashSection("Entity ID", this.entityId);
      var1.addCrashSectionCallable("Entity Name", new Callable(this) {
         final Entity this$0;

         public Object call() throws Exception {
            return this.call();
         }

         public String call() throws Exception {
            return this.this$0.getName();
         }

         {
            this.this$0 = var1;
         }
      });
      var1.addCrashSection("Entity's Exact location", String.format("%.2f, %.2f, %.2f", this.posX, this.posY, this.posZ));
      var1.addCrashSection("Entity's Block location", CrashReportCategory.getCoordinateInfo((double)MathHelper.floor_double(this.posX), (double)MathHelper.floor_double(this.posY), (double)MathHelper.floor_double(this.posZ)));
      var1.addCrashSection("Entity's Momentum", String.format("%.2f, %.2f, %.2f", this.motionX, this.motionY, this.motionZ));
      var1.addCrashSectionCallable("Entity's Rider", new Callable(this) {
         final Entity this$0;

         public String call() throws Exception {
            return this.this$0.riddenByEntity.toString();
         }

         {
            this.this$0 = var1;
         }

         public Object call() throws Exception {
            return this.call();
         }
      });
      var1.addCrashSectionCallable("Entity's Vehicle", new Callable(this) {
         final Entity this$0;

         public Object call() throws Exception {
            return this.call();
         }

         public String call() throws Exception {
            return this.this$0.ridingEntity.toString();
         }

         {
            this.this$0 = var1;
         }
      });
   }

   public double getDistance(double var1, double var3, double var5) {
      double var7 = this.posX - var1;
      double var9 = this.posY - var3;
      double var11 = this.posZ - var5;
      return (double)MathHelper.sqrt_double(var7 * var7 + var9 * var9 + var11 * var11);
   }

   public BlockPos getPosition() {
      return new BlockPos(this.posX, this.posY + 0.5D, this.posZ);
   }

   protected void setRotation(float var1, float var2) {
      this.rotationYaw = var1 % 360.0F;
      this.rotationPitch = var2 % 360.0F;
   }

   public boolean isEntityAlive() {
      return !this.isDead;
   }

   protected NBTTagList newDoubleNBTList(double... var1) {
      NBTTagList var2 = new NBTTagList();
      double[] var7 = var1;
      int var6 = var1.length;

      for(int var5 = 0; var5 < var6; ++var5) {
         double var3 = var7[var5];
         var2.appendTag(new NBTTagDouble(var3));
      }

      return var2;
   }

   public boolean isInvisibleToPlayer(EntityPlayer var1) {
      return var1.isSpectator() ? false : this.isInvisible();
   }

   public World getEntityWorld() {
      return this.worldObj;
   }

   public Vec3 getLookVec() {
      return null;
   }

   public AxisAlignedBB getCollisionBox(Entity var1) {
      return null;
   }

   public void onKillCommand() {
      this.setDead();
   }

   public int getPortalCooldown() {
      return 300;
   }

   protected void setSize(float var1, float var2) {
      if (var1 != this.width || var2 != this.height) {
         float var3 = this.width;
         this.width = var1;
         this.height = var2;
         this.setEntityBoundingBox(new AxisAlignedBB(this.getEntityBoundingBox().minX, this.getEntityBoundingBox().minY, this.getEntityBoundingBox().minZ, this.getEntityBoundingBox().minX + (double)this.width, this.getEntityBoundingBox().minY + (double)this.height, this.getEntityBoundingBox().minZ + (double)this.width));
         if (this.width > var3 && !this.firstUpdate && !this.worldObj.isRemote) {
            this.moveEntity((double)(var3 - this.width), 0.0D, (double)(var3 - this.width));
         }
      }

   }

   public NBTTagCompound getNBTTagCompound() {
      return null;
   }

   public CommandResultStats getCommandStats() {
      return this.cmdResultStats;
   }

   public void setCustomNameTag(String var1) {
      this.dataWatcher.updateObject(2, var1);
   }

   protected void dealFireDamage(int var1) {
      if (!this.isImmuneToFire) {
         this.attackEntityFrom(DamageSource.inFire, (float)var1);
      }

   }

   public void onUpdate() {
      this.onEntityUpdate();
   }

   public boolean equals(Object var1) {
      return var1 instanceof Entity ? ((Entity)var1).entityId == this.entityId : false;
   }

   public boolean verifyExplosion(Explosion var1, World var2, BlockPos var3, IBlockState var4, float var5) {
      return true;
   }

   protected void applyEnchantments(EntityLivingBase var1, Entity var2) {
      if (var2 instanceof EntityLivingBase) {
         EnchantmentHelper.applyThornEnchantments((EntityLivingBase)var2, var1);
      }

      EnchantmentHelper.applyArthropodEnchantments(var1, var2);
   }

   public void writeToNBT(NBTTagCompound param1) {
      // $FF: Couldn't be decompiled
   }

   public void playSound(String param1, float param2, float param3) {
      // $FF: Couldn't be decompiled
   }

   public UUID getUniqueID() {
      return this.entityUniqueID;
   }

   protected void doBlockCollisions() {
      BlockPos var1 = new BlockPos(this.getEntityBoundingBox().minX + 0.001D, this.getEntityBoundingBox().minY + 0.001D, this.getEntityBoundingBox().minZ + 0.001D);
      BlockPos var2 = new BlockPos(this.getEntityBoundingBox().maxX - 0.001D, this.getEntityBoundingBox().maxY - 0.001D, this.getEntityBoundingBox().maxZ - 0.001D);
      if (this.worldObj.isAreaLoaded(var1, var2)) {
         for(int var3 = var1.getX(); var3 <= var2.getX(); ++var3) {
            for(int var4 = var1.getY(); var4 <= var2.getY(); ++var4) {
               for(int var5 = var1.getZ(); var5 <= var2.getZ(); ++var5) {
                  BlockPos var6 = new BlockPos(var3, var4, var5);
                  IBlockState var7 = this.worldObj.getBlockState(var6);

                  try {
                     var7.getBlock().onEntityCollidedWithBlock(this.worldObj, var6, var7, this);
                  } catch (Throwable var12) {
                     CrashReport var9 = CrashReport.makeCrashReport(var12, "Colliding entity with block");
                     CrashReportCategory var10 = var9.makeCategory("Block being collided with");
                     CrashReportCategory.addBlockInfo(var10, var6, var7);
                     throw new ReportedException(var9);
                  }
               }
            }
         }
      }

   }

   public AxisAlignedBB getCollisionBoundingBox() {
      return null;
   }

   public void setEntityId(int var1) {
      this.entityId = var1;
   }

   public void setWorld(World var1) {
      this.worldObj = var1;
   }

   public void setLocationAndAngles(double var1, double var3, double var5, float var7, float var8) {
      this.lastTickPosX = this.prevPosX = this.posX = var1;
      this.lastTickPosY = this.prevPosY = this.posY = var3;
      this.lastTickPosZ = this.prevPosZ = this.posZ = var5;
      this.rotationYaw = var7;
      this.rotationPitch = var8;
      this.setPosition(this.posX, this.posY, this.posZ);
   }

   public void onStruckByLightning(EntityLightningBolt var1) {
      this.attackEntityFrom(DamageSource.lightningBolt, 5.0F);
      ++this.fire;
      if (this.fire == 0) {
         this.setFire(8);
      }

   }

   public void setRotationYawHead(float var1) {
   }

   protected NBTTagList newFloatNBTList(float... var1) {
      NBTTagList var2 = new NBTTagList();
      float[] var6 = var1;
      int var5 = var1.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         float var3 = var6[var4];
         var2.appendTag(new NBTTagFloat(var3));
      }

      return var2;
   }

   public boolean getAlwaysRenderNameTag() {
      return this.dataWatcher.getWatchableObjectByte(3) == 1;
   }

   public double getDistanceSqToEntity(Entity var1) {
      double var2 = this.posX - var1.posX;
      double var4 = this.posY - var1.posY;
      double var6 = this.posZ - var1.posZ;
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   public void performHurtAnimation() {
   }

   protected void createRunningParticles() {
      int var1 = MathHelper.floor_double(this.posX);
      int var2 = MathHelper.floor_double(this.posY - 0.20000000298023224D);
      int var3 = MathHelper.floor_double(this.posZ);
      BlockPos var4 = new BlockPos(var1, var2, var3);
      IBlockState var5 = this.worldObj.getBlockState(var4);
      Block var6 = var5.getBlock();
      if (var6.getRenderType() != -1) {
         this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, this.getEntityBoundingBox().minY + 0.1D, this.posZ + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, -this.motionX * 4.0D, 1.5D, -this.motionZ * 4.0D, Block.getStateId(var5));
      }

   }

   public boolean isInRangeToRender3d(double var1, double var3, double var5) {
      double var7 = this.posX - var1;
      double var9 = this.posY - var3;
      double var11 = this.posZ - var5;
      double var13 = var7 * var7 + var9 * var9 + var11 * var11;
      return this.isInRangeToRenderDist(var13);
   }

   public void func_181013_g(float var1) {
   }

   public void setEntityBoundingBox(AxisAlignedBB var1) {
      this.boundingBox = var1;
   }

   protected String getSplashSound() {
      return "game.neutral.swim.splash";
   }

   public boolean isSneaking() {
      return this.getFlag(1);
   }

   public MovingObjectPosition rayTrace(double var1, float var3) {
      Vec3 var4 = this.getPositionEyes(var3);
      Vec3 var5 = this.getLook(var3);
      Vec3 var6 = var4.addVector(var5.xCoord * var1, var5.yCoord * var1, var5.zCoord * var1);
      return this.worldObj.rayTraceBlocks(var4, var6, false, false, true);
   }

   public boolean isOffsetPositionInLiquid(double var1, double var3, double var5) {
      AxisAlignedBB var7 = this.getEntityBoundingBox().offset(var1, var3, var5);
      return this.isLiquidPresentInAABB(var7);
   }

   public boolean canCommandSenderUseCommand(int var1, String var2) {
      return true;
   }

   public void func_174817_o(Entity var1) {
      this.cmdResultStats.func_179671_a(var1.getCommandStats());
   }

   public int getMaxInPortalTime() {
      return 0;
   }

   public boolean canRenderOnFire() {
      return this.isBurning();
   }

   public double getDistanceSqToCenter(BlockPos var1) {
      return var1.distanceSqToCenter(this.posX, this.posY, this.posZ);
   }

   public AxisAlignedBB getEntityBoundingBox() {
      return this.boundingBox;
   }

   public float getExplosionResistance(Explosion var1, World var2, BlockPos var3, IBlockState var4) {
      return var4.getBlock().getExplosionResistance(this);
   }

   public DataWatcher getDataWatcher() {
      return this.dataWatcher;
   }

   protected void setFlag(int var1, boolean var2) {
      byte var3 = this.dataWatcher.getWatchableObjectByte(0);
      if (var2) {
         this.dataWatcher.updateObject(0, (byte)(var3 | 1 << var1));
      } else {
         this.dataWatcher.updateObject(0, (byte)(var3 & ~(1 << var1)));
      }

   }

   public boolean canBeCollidedWith() {
      return false;
   }

   protected void resetHeight() {
      float var1 = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.2F;
      if (var1 > 1.0F) {
         var1 = 1.0F;
      }

      this.playSound(this.getSplashSound(), var1, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
      float var2 = (float)MathHelper.floor_double(this.getEntityBoundingBox().minY);

      int var3;
      float var4;
      float var5;
      for(var3 = 0; (float)var3 < 1.0F + this.width * 20.0F; ++var3) {
         var4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
         var5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
         this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + (double)var4, (double)(var2 + 1.0F), this.posZ + (double)var5, this.motionX, this.motionY - (double)(this.rand.nextFloat() * 0.2F), this.motionZ);
      }

      for(var3 = 0; (float)var3 < 1.0F + this.width * 20.0F; ++var3) {
         var4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
         var5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
         this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + (double)var4, (double)(var2 + 1.0F), this.posZ + (double)var5, this.motionX, this.motionY, this.motionZ);
      }

   }

   public float getDistanceToEntity(Entity var1) {
      float var2 = (float)(this.posX - var1.posX);
      float var3 = (float)(this.posY - var1.posY);
      float var4 = (float)(this.posZ - var1.posZ);
      return MathHelper.sqrt_float(var2 * var2 + var3 * var3 + var4 * var4);
   }

   public int getMaxFallHeight() {
      return 3;
   }

   protected final Vec3 getVectorForRotation(float var1, float var2) {
      float var3 = MathHelper.cos(-var2 * 0.017453292F - 3.1415927F);
      float var4 = MathHelper.sin(-var2 * 0.017453292F - 3.1415927F);
      float var5 = -MathHelper.cos(-var1 * 0.017453292F);
      float var6 = MathHelper.sin(-var1 * 0.017453292F);
      return new Vec3((double)(var4 * var5), (double)var6, (double)(var3 * var5));
   }

   public void setPosition(double var1, double var3, double var5) {
      this.posX = var1;
      this.posY = var3;
      this.posZ = var5;
      float var7 = this.width / 2.0F;
      float var8 = this.height;
      this.setEntityBoundingBox(new AxisAlignedBB(var1 - (double)var7, var3, var5 - (double)var7, var1 + (double)var7, var3 + (double)var8, var5 + (double)var7));
   }

   public void setSilent(boolean var1) {
      this.dataWatcher.updateObject(4, (byte)(var1 ? 1 : 0));
   }

   public double getYOffset() {
      return 0.0D;
   }

   public int hashCode() {
      return this.entityId;
   }

   public final boolean isImmuneToFire() {
      return this.isImmuneToFire;
   }

   public boolean isEating() {
      return this.getFlag(4);
   }

   public ItemStack[] getInventory() {
      return null;
   }

   protected abstract void entityInit();

   public int getBrightnessForRender(float var1) {
      BlockPos var2 = new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
      return this.worldObj.isBlockLoaded(var2) ? this.worldObj.getCombinedLight(var2, 0) : 0;
   }

   public void setCommandStat(CommandResultStats.Type var1, int var2) {
      this.cmdResultStats.func_179672_a(this, var1, var2);
   }

   public Entity(World var1) {
      this.entityId = nextEntityID++;
      this.renderDistanceWeight = 1.0D;
      this.boundingBox = ZERO_AABB;
      this.width = 0.6F;
      this.height = 1.8F;
      this.nextStepDistance = 1;
      this.rand = new Random();
      this.fireResistance = 1;
      this.firstUpdate = true;
      this.entityUniqueID = MathHelper.getRandomUuid(this.rand);
      this.cmdResultStats = new CommandResultStats();
      this.worldObj = var1;
      this.setPosition(0.0D, 0.0D, 0.0D);
      if (var1 != null) {
         this.dimension = var1.provider.getDimensionId();
      }

      this.dataWatcher = new DataWatcher(this);
      this.dataWatcher.addObject(0, (byte)0);
      this.dataWatcher.addObject(1, (short)300);
      this.dataWatcher.addObject(3, (byte)0);
      this.dataWatcher.addObject(2, "");
      this.dataWatcher.addObject(4, (byte)0);
      this.entityInit();
   }

   public boolean isInRangeToRenderDist(double var1) {
      double var3 = this.getEntityBoundingBox().getAverageEdgeLength();
      if (Double.isNaN(var3)) {
         var3 = 1.0D;
      }

      var3 = var3 * 64.0D * this.renderDistanceWeight;
      return var1 < var3 * var3;
   }

   public boolean isEntityInsideOpaqueBlock() {
      if (this.noClip) {
         return false;
      } else {
         BlockPos.MutableBlockPos var1 = new BlockPos.MutableBlockPos(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

         for(int var2 = 0; var2 < 8; ++var2) {
            int var3 = MathHelper.floor_double(this.posY + (double)(((float)((var2 >> 0) % 2) - 0.5F) * 0.1F) + (double)this.getEyeHeight());
            int var4 = MathHelper.floor_double(this.posX + (double)(((float)((var2 >> 1) % 2) - 0.5F) * this.width * 0.8F));
            int var5 = MathHelper.floor_double(this.posZ + (double)(((float)((var2 >> 2) % 2) - 0.5F) * this.width * 0.8F));
            if (var1.getX() != var4 || var1.getY() != var3 || var1.getZ() != var5) {
               var1.func_181079_c(var4, var3, var5);
               if (this.worldObj.getBlockState(var1).getBlock().isVisuallyOpaque()) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public void setPositionAndUpdate(double var1, double var3, double var5) {
      this.setLocationAndAngles(var1, var3, var5, this.rotationYaw, this.rotationPitch);
   }

   protected void preparePlayerToSpawn() {
      if (this.worldObj != null) {
         while(true) {
            if (this.posY > 0.0D && this.posY < 256.0D) {
               this.setPosition(this.posX, this.posY, this.posZ);
               if (!this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty()) {
                  ++this.posY;
                  continue;
               }
            }

            this.motionX = this.motionY = this.motionZ = 0.0D;
            this.rotationPitch = 0.0F;
            break;
         }
      }

   }

   public boolean isSprinting() {
      return this.getFlag(3);
   }

   public void travelToDimension(int var1) {
      if (!this.worldObj.isRemote && !this.isDead) {
         this.worldObj.theProfiler.startSection("changeDimension");
         MinecraftServer var2 = MinecraftServer.getServer();
         int var3 = this.dimension;
         WorldServer var4 = var2.worldServerForDimension(var3);
         WorldServer var5 = var2.worldServerForDimension(var1);
         this.dimension = var1;
         if (var3 == 1 && var1 == 1) {
            var5 = var2.worldServerForDimension(0);
            this.dimension = 0;
         }

         this.worldObj.removeEntity(this);
         this.isDead = false;
         this.worldObj.theProfiler.startSection("reposition");
         var2.getConfigurationManager().transferEntityToWorld(this, var3, var4, var5);
         this.worldObj.theProfiler.endStartSection("reloading");
         Entity var6 = EntityList.createEntityByName(EntityList.getEntityString(this), var5);
         if (var6 != null) {
            var6.copyDataFromOld(this);
            if (var3 == 1 && var1 == 1) {
               BlockPos var7 = this.worldObj.getTopSolidOrLiquidBlock(var5.getSpawnPoint());
               var6.moveToBlockPosAndAngles(var7, var6.rotationYaw, var6.rotationPitch);
            }

            var5.spawnEntityInWorld(var6);
         }

         this.isDead = true;
         this.worldObj.theProfiler.endSection();
         var4.resetUpdateEntityTick();
         var5.resetUpdateEntityTick();
         this.worldObj.theProfiler.endSection();
      }

   }

   public void setFire(int var1) {
      int var2 = var1 * 20;
      var2 = EnchantmentProtection.getFireTimeForEntity(this, var2);
      if (this.fire < var2) {
         this.fire = var2;
      }

   }

   public int getEntityId() {
      return this.entityId;
   }

   public boolean isImmuneToExplosions() {
      return false;
   }

   public EnumFacing func_181012_aH() {
      return this.field_181018_ap;
   }

   protected abstract void writeEntityToNBT(NBTTagCompound var1);

   public boolean isInvisible() {
      return this.getFlag(5);
   }

   public boolean isRiding() {
      return this.ridingEntity != null;
   }

   public void handleStatusUpdate(byte var1) {
   }

   public Vec3 func_181014_aG() {
      return this.field_181017_ao;
   }

   public void setSneaking(boolean var1) {
      this.setFlag(1, var1);
   }

   public boolean replaceItemInInventory(int var1, ItemStack var2) {
      return false;
   }

   public Vec3 getPositionEyes(float var1) {
      if (var1 == 1.0F) {
         return new Vec3(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
      } else {
         double var2 = this.prevPosX + (this.posX - this.prevPosX) * (double)var1;
         double var4 = this.prevPosY + (this.posY - this.prevPosY) * (double)var1 + (double)this.getEyeHeight();
         double var6 = this.prevPosZ + (this.posZ - this.prevPosZ) * (double)var1;
         return new Vec3(var2, var4, var6);
      }
   }

   public double getDistanceSq(double var1, double var3, double var5) {
      double var7 = this.posX - var1;
      double var9 = this.posY - var3;
      double var11 = this.posZ - var5;
      return var7 * var7 + var9 * var9 + var11 * var11;
   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (var1 != false) {
         return false;
      } else {
         this.setBeenAttacked();
         return false;
      }
   }

   protected boolean canTriggerWalking() {
      return true;
   }

   public void extinguish() {
      this.fire = 0;
   }

   public boolean handleWaterMovement() {
      if (this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox().expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, this)) {
         if (!this.inWater && !this.firstUpdate) {
            this.resetHeight();
         }

         this.fallDistance = 0.0F;
         this.inWater = true;
         this.fire = 0;
      } else {
         this.inWater = false;
      }

      return this.inWater;
   }

   public float getRotationYawHead() {
      return 0.0F;
   }

   public void onDataWatcherUpdate(int var1) {
   }

   protected String getSwimSound() {
      return "game.neutral.swim";
   }

   public Vec3 getPositionVector() {
      return new Vec3(this.posX, this.posY, this.posZ);
   }

   protected HoverEvent getHoverEvent() {
      NBTTagCompound var1 = new NBTTagCompound();
      String var2 = EntityList.getEntityString(this);
      var1.setString("id", this.getUniqueID().toString());
      if (var2 != null) {
         var1.setString("type", var2);
      }

      var1.setString("name", this.getName());
      return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new ChatComponentText(var1.toString()));
   }

   public EntityItem dropItem(Item var1, int var2) {
      return this.dropItemWithOffset(var1, var2, 0.0F);
   }

   public double getDistanceSq(BlockPos var1) {
      return var1.distanceSq(this.posX, this.posY, this.posZ);
   }

   public void updateRiderPosition() {
      if (this.riddenByEntity != null) {
         this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
      }

   }

   public EntityItem dropItemWithOffset(Item var1, int var2, float var3) {
      return this.entityDropItem(new ItemStack(var1, var2, 0), var3);
   }

   public boolean interactAt(EntityPlayer var1, Vec3 var2) {
      return false;
   }

   public void moveEntity(double var1, double var3, double var5) {
      if (this.noClip) {
         this.setEntityBoundingBox(this.getEntityBoundingBox().offset(var1, var3, var5));
         this.resetPositionToBB();
      } else {
         this.worldObj.theProfiler.startSection("move");
         double var7 = this.posX;
         double var9 = this.posY;
         double var11 = this.posZ;
         if (this.isInWeb) {
            this.isInWeb = false;
            var1 *= 0.25D;
            var3 *= 0.05000000074505806D;
            var5 *= 0.25D;
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
         }

         double var13 = var1;
         double var15 = var3;
         double var17 = var5;
         boolean var19 = this.onGround && this.isSneaking() && this instanceof EntityPlayer;
         if (var19 && this instanceof EntityPlayer) {
            double var20;
            for(var20 = 0.05D; var1 != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(var1, -1.0D, 0.0D)).isEmpty(); var13 = var1) {
               if (var1 < var20 && var1 >= -var20) {
                  var1 = 0.0D;
               } else if (var1 > 0.0D) {
                  var1 -= var20;
               } else {
                  var1 += var20;
               }
            }

            for(; var5 != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(0.0D, -1.0D, var5)).isEmpty(); var17 = var5) {
               if (var5 < var20 && var5 >= -var20) {
                  var5 = 0.0D;
               } else if (var5 > 0.0D) {
                  var5 -= var20;
               } else {
                  var5 += var20;
               }
            }

            for(; var1 != 0.0D && var5 != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(var1, -1.0D, var5)).isEmpty(); var17 = var5) {
               if (var1 < var20 && var1 >= -var20) {
                  var1 = 0.0D;
               } else if (var1 > 0.0D) {
                  var1 -= var20;
               } else {
                  var1 += var20;
               }

               var13 = var1;
               if (var5 < var20 && var5 >= -var20) {
                  var5 = 0.0D;
               } else if (var5 > 0.0D) {
                  var5 -= var20;
               } else {
                  var5 += var20;
               }
            }
         }

         List var53 = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(var1, var3, var5));
         AxisAlignedBB var21 = this.getEntityBoundingBox();

         AxisAlignedBB var22;
         for(Iterator var23 = var53.iterator(); var23.hasNext(); var3 = var22.calculateYOffset(this.getEntityBoundingBox(), var3)) {
            var22 = (AxisAlignedBB)var23.next();
         }

         this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, var3, 0.0D));
         boolean var54 = this.onGround || var15 != var3 && var15 < 0.0D;

         Iterator var24;
         AxisAlignedBB var55;
         for(var24 = var53.iterator(); var24.hasNext(); var1 = var55.calculateXOffset(this.getEntityBoundingBox(), var1)) {
            var55 = (AxisAlignedBB)var24.next();
         }

         this.setEntityBoundingBox(this.getEntityBoundingBox().offset(var1, 0.0D, 0.0D));

         for(var24 = var53.iterator(); var24.hasNext(); var5 = var55.calculateZOffset(this.getEntityBoundingBox(), var5)) {
            var55 = (AxisAlignedBB)var24.next();
         }

         this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, 0.0D, var5));
         if (this.stepHeight > 0.0F && var54 && (var13 != var1 || var17 != var5)) {
            double var56 = var1;
            double var25 = var3;
            double var27 = var5;
            AxisAlignedBB var29 = this.getEntityBoundingBox();
            this.setEntityBoundingBox(var21);
            var3 = (double)this.stepHeight;
            List var30 = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(var13, var3, var17));
            AxisAlignedBB var31 = this.getEntityBoundingBox();
            AxisAlignedBB var32 = var31.addCoord(var13, 0.0D, var17);
            double var33 = var3;

            AxisAlignedBB var35;
            for(Iterator var36 = var30.iterator(); var36.hasNext(); var33 = var35.calculateYOffset(var32, var33)) {
               var35 = (AxisAlignedBB)var36.next();
            }

            var31 = var31.offset(0.0D, var33, 0.0D);
            double var67 = var13;

            AxisAlignedBB var37;
            for(Iterator var38 = var30.iterator(); var38.hasNext(); var67 = var37.calculateXOffset(var31, var67)) {
               var37 = (AxisAlignedBB)var38.next();
            }

            var31 = var31.offset(var67, 0.0D, 0.0D);
            double var68 = var17;

            AxisAlignedBB var39;
            for(Iterator var40 = var30.iterator(); var40.hasNext(); var68 = var39.calculateZOffset(var31, var68)) {
               var39 = (AxisAlignedBB)var40.next();
            }

            var31 = var31.offset(0.0D, 0.0D, var68);
            var39 = this.getEntityBoundingBox();
            double var69 = var3;

            AxisAlignedBB var42;
            for(Iterator var43 = var30.iterator(); var43.hasNext(); var69 = var42.calculateYOffset(var39, var69)) {
               var42 = (AxisAlignedBB)var43.next();
            }

            var39 = var39.offset(0.0D, var69, 0.0D);
            double var70 = var13;

            AxisAlignedBB var44;
            for(Iterator var45 = var30.iterator(); var45.hasNext(); var70 = var44.calculateXOffset(var39, var70)) {
               var44 = (AxisAlignedBB)var45.next();
            }

            var39 = var39.offset(var70, 0.0D, 0.0D);
            double var71 = var17;

            AxisAlignedBB var46;
            for(Iterator var47 = var30.iterator(); var47.hasNext(); var71 = var46.calculateZOffset(var39, var71)) {
               var46 = (AxisAlignedBB)var47.next();
            }

            var39 = var39.offset(0.0D, 0.0D, var71);
            double var72 = var67 * var67 + var68 * var68;
            double var48 = var70 * var70 + var71 * var71;
            if (var72 > var48) {
               var1 = var67;
               var5 = var68;
               var3 = -var33;
               this.setEntityBoundingBox(var31);
            } else {
               var1 = var70;
               var5 = var71;
               var3 = -var69;
               this.setEntityBoundingBox(var39);
            }

            AxisAlignedBB var50;
            for(Iterator var51 = var30.iterator(); var51.hasNext(); var3 = var50.calculateYOffset(this.getEntityBoundingBox(), var3)) {
               var50 = (AxisAlignedBB)var51.next();
            }

            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, var3, 0.0D));
            if (var56 * var56 + var27 * var27 >= var1 * var1 + var5 * var5) {
               var1 = var56;
               var3 = var25;
               var5 = var27;
               this.setEntityBoundingBox(var29);
            }
         }

         this.worldObj.theProfiler.endSection();
         this.worldObj.theProfiler.startSection("rest");
         this.resetPositionToBB();
         this.isCollidedHorizontally = var13 != var1 || var17 != var5;
         this.isCollidedVertically = var15 != var3;
         this.onGround = this.isCollidedVertically && var15 < 0.0D;
         this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
         int var57 = MathHelper.floor_double(this.posX);
         int var58 = MathHelper.floor_double(this.posY - 0.20000000298023224D);
         int var59 = MathHelper.floor_double(this.posZ);
         BlockPos var26 = new BlockPos(var57, var58, var59);
         Block var60 = this.worldObj.getBlockState(var26).getBlock();
         if (var60.getMaterial() == Material.air) {
            Block var28 = this.worldObj.getBlockState(var26.down()).getBlock();
            if (var28 instanceof BlockFence || var28 instanceof BlockWall || var28 instanceof BlockFenceGate) {
               var60 = var28;
               var26 = var26.down();
            }
         }

         this.updateFallState(var3, this.onGround, var60, var26);
         if (var13 != var1) {
            this.motionX = 0.0D;
         }

         if (var17 != var5) {
            this.motionZ = 0.0D;
         }

         if (var15 != var3) {
            var60.onLanded(this.worldObj, this);
         }

         if (this.canTriggerWalking() && !var19 && this.ridingEntity == null) {
            double var61 = this.posX - var7;
            double var64 = this.posY - var9;
            double var66 = this.posZ - var11;
            if (var60 != Blocks.ladder) {
               var64 = 0.0D;
            }

            if (var60 != null && this.onGround) {
               var60.onEntityCollidedWithBlock(this.worldObj, var26, this);
            }

            this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_double(var61 * var61 + var66 * var66) * 0.6D);
            this.distanceWalkedOnStepModified = (float)((double)this.distanceWalkedOnStepModified + (double)MathHelper.sqrt_double(var61 * var61 + var64 * var64 + var66 * var66) * 0.6D);
            if (this.distanceWalkedOnStepModified > (float)this.nextStepDistance && var60.getMaterial() != Material.air) {
               this.nextStepDistance = (int)this.distanceWalkedOnStepModified + 1;
               if (this.isInWater()) {
                  float var34 = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.35F;
                  if (var34 > 1.0F) {
                     var34 = 1.0F;
                  }

                  this.playSound(this.getSwimSound(), var34, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
               }

               this.playStepSound(var26, var60);
            }
         }

         try {
            this.doBlockCollisions();
         } catch (Throwable var52) {
            CrashReport var63 = CrashReport.makeCrashReport(var52, "Checking entity block collision");
            CrashReportCategory var65 = var63.makeCategory("Entity being checked for collision");
            this.addEntityCrashInfo(var65);
            throw new ReportedException(var63);
         }

         boolean var62 = this.isWet();
         if (this.worldObj.isFlammableWithin(this.getEntityBoundingBox().contract(0.001D, 0.001D, 0.001D))) {
            this.dealFireDamage(1);
            if (!var62) {
               ++this.fire;
               if (this.fire == 0) {
                  this.setFire(8);
               }
            }
         } else if (this.fire <= 0) {
            this.fire = -this.fireResistance;
         }

         if (var62 && this.fire > 0) {
            this.playSound("random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
            this.fire = -this.fireResistance;
         }

         this.worldObj.theProfiler.endSection();
      }

   }

   public boolean isSpectatedByPlayer(EntityPlayerMP var1) {
      return true;
   }

   public void setAlwaysRenderNameTag(boolean var1) {
      this.dataWatcher.updateObject(3, (byte)(var1 ? 1 : 0));
   }

   public void spawnRunningParticles() {
      if (this.isSprinting() && !this.isInWater()) {
         this.createRunningParticles();
      }

   }

   public void applyEntityCollision(Entity var1) {
      if (var1.riddenByEntity != this && var1.ridingEntity != this && !var1.noClip && !this.noClip) {
         double var2 = var1.posX - this.posX;
         double var4 = var1.posZ - this.posZ;
         double var6 = MathHelper.abs_max(var2, var4);
         if (var6 >= 0.009999999776482582D) {
            var6 = (double)MathHelper.sqrt_double(var6);
            var2 /= var6;
            var4 /= var6;
            double var8 = 1.0D / var6;
            if (var8 > 1.0D) {
               var8 = 1.0D;
            }

            var2 *= var8;
            var4 *= var8;
            var2 *= 0.05000000074505806D;
            var4 *= 0.05000000074505806D;
            var2 *= (double)(1.0F - this.entityCollisionReduction);
            var4 *= (double)(1.0F - this.entityCollisionReduction);
            if (this.riddenByEntity == null) {
               this.addVelocity(-var2, 0.0D, -var4);
            }

            if (var1.riddenByEntity == null) {
               var1.addVelocity(var2, 0.0D, var4);
            }
         }
      }

   }

   protected void kill() {
      this.setDead();
   }

   protected final String getEntityString() {
      return EntityList.getEntityString(this);
   }

   private boolean isLiquidPresentInAABB(AxisAlignedBB var1) {
      return this.worldObj.getCollidingBoundingBoxes(this, var1).isEmpty() && !this.worldObj.isAnyLiquid(var1);
   }

   public boolean writeToNBTOptional(NBTTagCompound var1) {
      String var2 = this.getEntityString();
      if (!this.isDead && var2 != null && this.riddenByEntity == null) {
         var1.setString("id", var2);
         this.writeToNBT(var1);
         return true;
      } else {
         return false;
      }
   }

   public String getCustomNameTag() {
      return this.dataWatcher.getWatchableObjectString(2);
   }

   public void addChatMessage(IChatComponent var1) {
   }

   public void func_181015_d(BlockPos var1) {
      if (this.timeUntilPortal > 0) {
         this.timeUntilPortal = this.getPortalCooldown();
      } else {
         if (!this.worldObj.isRemote && !var1.equals(this.field_181016_an)) {
            this.field_181016_an = var1;
            BlockPattern.PatternHelper var2 = Blocks.portal.func_181089_f(this.worldObj, var1);
            double var3 = var2.getFinger().getAxis() == EnumFacing.Axis.X ? (double)var2.func_181117_a().getZ() : (double)var2.func_181117_a().getX();
            double var5 = var2.getFinger().getAxis() == EnumFacing.Axis.X ? this.posZ : this.posX;
            var5 = Math.abs(MathHelper.func_181160_c(var5 - (double)(var2.getFinger().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE ? 1 : 0), var3, var3 - (double)var2.func_181118_d()));
            double var7 = MathHelper.func_181160_c(this.posY - 1.0D, (double)var2.func_181117_a().getY(), (double)(var2.func_181117_a().getY() - var2.func_181119_e()));
            this.field_181017_ao = new Vec3(var5, var7, 0.0D);
            this.field_181018_ap = var2.getFinger();
         }

         this.inPortal = true;
      }

   }

   public boolean isOutsideBorder() {
      return this.isOutsideBorder;
   }

   public Entity getCommandSenderEntity() {
      return this;
   }

   public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9, boolean var10) {
      this.setPosition(var1, var3, var5);
      this.setRotation(var7, var8);
      List var11 = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().contract(0.03125D, 0.0D, 0.03125D));
      if (!var11.isEmpty()) {
         double var12 = 0.0D;
         Iterator var15 = var11.iterator();

         while(var15.hasNext()) {
            AxisAlignedBB var14 = (AxisAlignedBB)var15.next();
            if (var14.maxY > var12) {
               var12 = var14.maxY;
            }
         }

         var3 += var12 - this.getEntityBoundingBox().minY;
         this.setPosition(var1, var3, var5);
      }

   }

   public void fall(float var1, float var2) {
      if (this.riddenByEntity != null) {
         this.riddenByEntity.fall(var1, var2);
      }

   }

   public boolean isPushedByWater() {
      return true;
   }

   public void updateRidden() {
      if (this.ridingEntity.isDead) {
         this.ridingEntity = null;
      } else {
         this.motionX = 0.0D;
         this.motionY = 0.0D;
         this.motionZ = 0.0D;
         this.onUpdate();
         if (this.ridingEntity != null) {
            this.ridingEntity.updateRiderPosition();
            this.entityRiderYawDelta += (double)(this.ridingEntity.rotationYaw - this.ridingEntity.prevRotationYaw);

            for(this.entityRiderPitchDelta += (double)(this.ridingEntity.rotationPitch - this.ridingEntity.prevRotationPitch); this.entityRiderYawDelta >= 180.0D; this.entityRiderYawDelta -= 360.0D) {
            }

            while(this.entityRiderYawDelta < -180.0D) {
               this.entityRiderYawDelta += 360.0D;
            }

            while(this.entityRiderPitchDelta >= 180.0D) {
               this.entityRiderPitchDelta -= 360.0D;
            }

            while(this.entityRiderPitchDelta < -180.0D) {
               this.entityRiderPitchDelta += 360.0D;
            }

            double var1 = this.entityRiderYawDelta * 0.5D;
            double var3 = this.entityRiderPitchDelta * 0.5D;
            float var5 = 10.0F;
            if (var1 > (double)var5) {
               var1 = (double)var5;
            }

            if (var1 < (double)(-var5)) {
               var1 = (double)(-var5);
            }

            if (var3 > (double)var5) {
               var3 = (double)var5;
            }

            if (var3 < (double)(-var5)) {
               var3 = (double)(-var5);
            }

            this.entityRiderYawDelta -= var1;
            this.entityRiderPitchDelta -= var3;
         }
      }

   }

   public float getCollisionBorderSize() {
      return 0.1F;
   }

   public void addVelocity(double var1, double var3, double var5) {
      this.motionX += var1;
      this.motionY += var3;
      this.motionZ += var5;
      this.isAirBorne = true;
   }

   public String getName() {
      if (this > 0) {
         return this.getCustomNameTag();
      } else {
         String var1 = EntityList.getEntityString(this);
         if (var1 == null) {
            var1 = "generic";
         }

         return StatCollector.translateToLocal("entity." + var1 + ".name");
      }
   }

   protected void updateFallState(double var1, boolean var3, Block var4, BlockPos var5) {
      if (var3) {
         if (this.fallDistance > 0.0F) {
            if (var4 != null) {
               var4.onFallenUpon(this.worldObj, var5, this, this.fallDistance);
            } else {
               this.fall(this.fallDistance, 1.0F);
            }

            this.fallDistance = 0.0F;
         }
      } else if (var1 < 0.0D) {
         this.fallDistance = (float)((double)this.fallDistance - var1);
      }

   }

   public void setInWeb() {
      this.isInWeb = true;
      this.fallDistance = 0.0F;
   }

   public void copyDataFromOld(Entity var1) {
      NBTTagCompound var2 = new NBTTagCompound();
      var1.writeToNBT(var2);
      this.readFromNBT(var2);
      this.timeUntilPortal = var1.timeUntilPortal;
      this.field_181016_an = var1.field_181016_an;
      this.field_181017_ao = var1.field_181017_ao;
      this.field_181018_ap = var1.field_181018_ap;
   }

   private void resetPositionToBB() {
      this.posX = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0D;
      this.posY = this.getEntityBoundingBox().minY;
      this.posZ = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0D;
   }

   public void copyLocationAndAnglesFrom(Entity var1) {
      this.setLocationAndAngles(var1.posX, var1.posY, var1.posZ, var1.rotationYaw, var1.rotationPitch);
   }

   public Vec3 getLook(float var1) {
      if (var1 == 1.0F) {
         return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
      } else {
         float var2 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * var1;
         float var3 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * var1;
         return this.getVectorForRotation(var2, var3);
      }
   }

   public IChatComponent getDisplayName() {
      ChatComponentText var1 = new ChatComponentText(this.getName());
      var1.getChatStyle().setChatHoverEvent(this.getHoverEvent());
      var1.getChatStyle().setInsertion(this.getUniqueID().toString());
      return var1;
   }

   public void mountEntity(Entity var1) {
      this.entityRiderPitchDelta = 0.0D;
      this.entityRiderYawDelta = 0.0D;
      if (var1 == null) {
         if (this.ridingEntity != null) {
            this.setLocationAndAngles(this.ridingEntity.posX, this.ridingEntity.getEntityBoundingBox().minY + (double)this.ridingEntity.height, this.ridingEntity.posZ, this.rotationYaw, this.rotationPitch);
            this.ridingEntity.riddenByEntity = null;
         }

         this.ridingEntity = null;
      } else {
         if (this.ridingEntity != null) {
            this.ridingEntity.riddenByEntity = null;
         }

         if (var1 != null) {
            for(Entity var2 = var1.ridingEntity; var2 != null; var2 = var2.ridingEntity) {
               if (var2 == this) {
                  return;
               }
            }
         }

         this.ridingEntity = var1;
         var1.riddenByEntity = this;
      }

   }

   public boolean canBePushed() {
      return false;
   }

   public void setInvisible(boolean var1) {
      this.setFlag(5, var1);
   }

   public boolean interactFirst(EntityPlayer var1) {
      return false;
   }
}
