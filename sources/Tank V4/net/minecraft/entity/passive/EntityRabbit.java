package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityRabbit extends EntityAnimal {
   private int carrotTicks;
   private EntityRabbit.EnumMoveType moveType;
   private EntityPlayer field_175543_bt;
   private int field_175540_bm = 0;
   private int currentMoveTypeDuration = 0;
   private int field_175535_bn = 0;
   private EntityRabbit.AIAvoidEntity aiAvoidWolves;
   private boolean field_175536_bo = false;
   private boolean field_175537_bp = false;

   public EntityAgeable createChild(EntityAgeable var1) {
      return this.createChild(var1);
   }

   public void setMoveType(EntityRabbit.EnumMoveType var1) {
      this.moveType = var1;
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(18, (byte)0);
   }

   protected String getHurtSound() {
      return "mob.rabbit.hurt";
   }

   public EntityRabbit(World var1) {
      super(var1);
      this.moveType = EntityRabbit.EnumMoveType.HOP;
      this.carrotTicks = 0;
      this.field_175543_bt = null;
      this.setSize(0.6F, 0.7F);
      this.jumpHelper = new EntityRabbit.RabbitJumpHelper(this, this);
      this.moveHelper = new EntityRabbit.RabbitMoveHelper(this);
      ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
      this.navigator.setHeightRequirement(2.5F);
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityRabbit.AIPanic(this, 1.33D));
      this.tasks.addTask(2, new EntityAITempt(this, 1.0D, Items.carrot, false));
      this.tasks.addTask(2, new EntityAITempt(this, 1.0D, Items.golden_carrot, false));
      this.tasks.addTask(2, new EntityAITempt(this, 1.0D, Item.getItemFromBlock(Blocks.yellow_flower), false));
      this.tasks.addTask(3, new EntityAIMate(this, 0.8D));
      this.tasks.addTask(5, new EntityRabbit.AIRaidFarm(this));
      this.tasks.addTask(5, new EntityAIWander(this, 0.6D));
      this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
      this.aiAvoidWolves = new EntityRabbit.AIAvoidEntity(this, EntityWolf.class, 16.0F, 1.33D, 1.33D);
      this.tasks.addTask(4, this.aiAvoidWolves);
      this.setMovementSpeed(0.0D);
   }

   public void setRabbitType(int var1) {
      if (var1 == 99) {
         this.tasks.removeTask(this.aiAvoidWolves);
         this.tasks.addTask(4, new EntityRabbit.AIEvilAttack(this));
         this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
         this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
         this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityWolf.class, true));
         if (!this.hasCustomName()) {
            this.setCustomNameTag(StatCollector.translateToLocal("entity.KillerBunny.name"));
         }
      }

      this.dataWatcher.updateObject(18, (byte)var1);
   }

   static boolean access$1(EntityRabbit var0) {
      return var0.isCarrotEaten();
   }

   public void doMovementAction(EntityRabbit.EnumMoveType var1) {
      this.setJumping(true, var1);
      this.field_175535_bn = var1.func_180073_d();
      this.field_175540_bm = 0;
   }

   private void updateMoveTypeDuration() {
      this.currentMoveTypeDuration = this.getMoveTypeDuration();
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      var1.setInteger("RabbitType", this.getRabbitType());
      var1.setInteger("MoreCarrotTicks", this.carrotTicks);
   }

   public void updateAITasks() {
      if (this.moveHelper.getSpeed() > 0.8D) {
         this.setMoveType(EntityRabbit.EnumMoveType.SPRINT);
      } else if (this.moveType != EntityRabbit.EnumMoveType.ATTACK) {
         this.setMoveType(EntityRabbit.EnumMoveType.HOP);
      }

      if (this.currentMoveTypeDuration > 0) {
         --this.currentMoveTypeDuration;
      }

      if (this.carrotTicks > 0) {
         this.carrotTicks -= this.rand.nextInt(3);
         if (this.carrotTicks < 0) {
            this.carrotTicks = 0;
         }
      }

      if (this.onGround) {
         if (!this.field_175537_bp) {
            this.setJumping(false, EntityRabbit.EnumMoveType.NONE);
            this.func_175517_cu();
         }

         if (this.getRabbitType() == 99 && this.currentMoveTypeDuration == 0) {
            EntityLivingBase var1 = this.getAttackTarget();
            if (var1 != null && this.getDistanceSqToEntity(var1) < 16.0D) {
               this.calculateRotationYaw(var1.posX, var1.posZ);
               this.moveHelper.setMoveTo(var1.posX, var1.posY, var1.posZ, this.moveHelper.getSpeed());
               this.doMovementAction(EntityRabbit.EnumMoveType.ATTACK);
               this.field_175537_bp = true;
            }
         }

         EntityRabbit.RabbitJumpHelper var4 = (EntityRabbit.RabbitJumpHelper)this.jumpHelper;
         if (!var4.getIsJumping()) {
            if (this.moveHelper.isUpdating() && this.currentMoveTypeDuration == 0) {
               PathEntity var2 = this.navigator.getPath();
               Vec3 var3 = new Vec3(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ());
               if (var2 != null && var2.getCurrentPathIndex() < var2.getCurrentPathLength()) {
                  var3 = var2.getPosition(this);
               }

               this.calculateRotationYaw(var3.xCoord, var3.zCoord);
               this.doMovementAction(this.moveType);
            }
         } else if (!var4.func_180065_d()) {
            this.func_175518_cr();
         }
      }

      this.field_175537_bp = this.onGround;
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      this.setRabbitType(var1.getInteger("RabbitType"));
      this.carrotTicks = var1.getInteger("MoreCarrotTicks");
   }

   public void setMovementSpeed(double var1) {
      this.getNavigator().setSpeed(var1);
      this.moveHelper.setMoveTo(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ(), var1);
   }

   protected int getMoveTypeDuration() {
      return this.moveType.getDuration();
   }

   protected String getJumpingSound() {
      return "mob.rabbit.hop";
   }

   public int getTotalArmorValue() {
      return this.getRabbitType() == 99 ? 8 : super.getTotalArmorValue();
   }

   protected String getLivingSound() {
      return "mob.rabbit.idle";
   }

   public boolean isBreedingItem(ItemStack var1) {
      return var1 != null && var1.getItem() != false;
   }

   protected void createEatingParticles() {
      this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, 0.0D, 0.0D, 0.0D, Block.getStateId(Blocks.carrots.getStateFromMeta(7)));
      this.carrotTicks = 100;
   }

   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, IEntityLivingData var2) {
      Object var6 = super.onInitialSpawn(var1, var2);
      int var3 = this.rand.nextInt(6);
      boolean var4 = false;
      if (var6 instanceof EntityRabbit.RabbitTypeData) {
         var3 = ((EntityRabbit.RabbitTypeData)var6).typeData;
         var4 = true;
      } else {
         var6 = new EntityRabbit.RabbitTypeData(var3);
      }

      this.setRabbitType(var3);
      if (var4) {
         this.setGrowingAge(-24000);
      }

      return (IEntityLivingData)var6;
   }

   protected float getJumpUpwardsMotion() {
      return this.moveHelper.isUpdating() && this.moveHelper.getY() > this.posY + 0.5D ? 0.5F : this.moveType.func_180074_b();
   }

   public float func_175521_o(float var1) {
      return this.field_175535_bn == 0 ? 0.0F : ((float)this.field_175540_bm + var1) / (float)this.field_175535_bn;
   }

   public boolean func_175523_cj() {
      return this.field_175536_bo;
   }

   private void func_175520_cs() {
      ((EntityRabbit.RabbitJumpHelper)this.jumpHelper).func_180066_a(false);
   }

   public EntityRabbit createChild(EntityAgeable var1) {
      EntityRabbit var2 = new EntityRabbit(this.worldObj);
      if (var1 instanceof EntityRabbit) {
         var2.setRabbitType(this.rand.nextBoolean() ? this.getRabbitType() : ((EntityRabbit)var1).getRabbitType());
      }

      return var2;
   }

   protected void addRandomDrop() {
      this.entityDropItem(new ItemStack(Items.rabbit_foot, 1), 0.0F);
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (this.field_175540_bm != this.field_175535_bn) {
         if (this.field_175540_bm == 0 && !this.worldObj.isRemote) {
            this.worldObj.setEntityState(this, (byte)1);
         }

         ++this.field_175540_bm;
      } else if (this.field_175535_bn != 0) {
         this.field_175540_bm = 0;
         this.field_175535_bn = 0;
      }

   }

   public void handleStatusUpdate(byte var1) {
      if (var1 == 1) {
         this.createRunningParticles();
         this.field_175535_bn = 10;
         this.field_175540_bm = 0;
      } else {
         super.handleStatusUpdate(var1);
      }

   }

   public void spawnRunningParticles() {
   }

   private void func_175518_cr() {
      ((EntityRabbit.RabbitJumpHelper)this.jumpHelper).func_180066_a(true);
   }

   protected String getDeathSound() {
      return "mob.rabbit.death";
   }

   private void func_175517_cu() {
      this.updateMoveTypeDuration();
      this.func_175520_cs();
   }

   public void setJumping(boolean var1, EntityRabbit.EnumMoveType var2) {
      super.setJumping(var1);
      if (!var1) {
         if (this.moveType == EntityRabbit.EnumMoveType.ATTACK) {
            this.moveType = EntityRabbit.EnumMoveType.HOP;
         }
      } else {
         this.setMovementSpeed(1.5D * (double)var2.getSpeed());
         this.playSound(this.getJumpingSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
      }

      this.field_175536_bo = var1;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
   }

   protected void dropFewItems(boolean var1, int var2) {
      int var3 = this.rand.nextInt(2) + this.rand.nextInt(1 + var2);

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         this.dropItem(Items.rabbit_hide, 1);
      }

      var3 = this.rand.nextInt(2);

      for(var4 = 0; var4 < var3; ++var4) {
         if (this.isBurning()) {
            this.dropItem(Items.cooked_rabbit, 1);
         } else {
            this.dropItem(Items.rabbit, 1);
         }
      }

   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      return this.isEntityInvulnerable(var1) ? false : super.attackEntityFrom(var1, var2);
   }

   private boolean isCarrotEaten() {
      return this.carrotTicks == 0;
   }

   public boolean attackEntityAsMob(Entity var1) {
      if (this.getRabbitType() == 99) {
         this.playSound("mob.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         return var1.attackEntityFrom(DamageSource.causeMobDamage(this), 8.0F);
      } else {
         return var1.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
      }
   }

   public int getRabbitType() {
      return this.dataWatcher.getWatchableObjectByte(18);
   }

   private void calculateRotationYaw(double var1, double var3) {
      this.rotationYaw = (float)(MathHelper.func_181159_b(var3 - this.posZ, var1 - this.posX) * 180.0D / 3.141592653589793D) - 90.0F;
   }

   static class AIAvoidEntity extends EntityAIAvoidEntity {
      private EntityRabbit entityInstance;

      public AIAvoidEntity(EntityRabbit var1, Class var2, float var3, double var4, double var6) {
         super(var1, var2, var3, var4, var6);
         this.entityInstance = var1;
      }

      public void updateTask() {
         super.updateTask();
      }
   }

   public static class RabbitTypeData implements IEntityLivingData {
      public int typeData;

      public RabbitTypeData(int var1) {
         this.typeData = var1;
      }
   }

   public class RabbitJumpHelper extends EntityJumpHelper {
      private EntityRabbit theEntity;
      private boolean field_180068_d;
      final EntityRabbit this$0;

      public void func_180066_a(boolean var1) {
         this.field_180068_d = var1;
      }

      public void doJump() {
         if (this.isJumping) {
            this.theEntity.doMovementAction(EntityRabbit.EnumMoveType.STEP);
            this.isJumping = false;
         }

      }

      public boolean func_180065_d() {
         return this.field_180068_d;
      }

      public boolean getIsJumping() {
         return this.isJumping;
      }

      public RabbitJumpHelper(EntityRabbit var1, EntityRabbit var2) {
         super(var2);
         this.this$0 = var1;
         this.field_180068_d = false;
         this.theEntity = var2;
      }
   }

   static class AIPanic extends EntityAIPanic {
      private EntityRabbit theEntity;

      public AIPanic(EntityRabbit var1, double var2) {
         super(var1, var2);
         this.theEntity = var1;
      }

      public void updateTask() {
         super.updateTask();
         this.theEntity.setMovementSpeed(this.speed);
      }
   }

   static class AIEvilAttack extends EntityAIAttackOnCollide {
      protected double func_179512_a(EntityLivingBase var1) {
         return (double)(4.0F + var1.width);
      }

      public AIEvilAttack(EntityRabbit var1) {
         super(var1, EntityLivingBase.class, 1.4D, true);
      }
   }

   static class RabbitMoveHelper extends EntityMoveHelper {
      private EntityRabbit theEntity;

      public RabbitMoveHelper(EntityRabbit var1) {
         super(var1);
         this.theEntity = var1;
      }

      public void onUpdateMoveHelper() {
         if (this.theEntity.onGround && !this.theEntity.func_175523_cj()) {
            this.theEntity.setMovementSpeed(0.0D);
         }

         super.onUpdateMoveHelper();
      }
   }

   static enum EnumMoveType {
      NONE(0.0F, 0.0F, 30, 1);

      private final float speed;
      STEP(1.0F, 0.45F, 14, 14);

      private final int field_180085_i;
      ATTACK(2.0F, 0.7F, 7, 8);

      private final int duration;
      private static final EntityRabbit.EnumMoveType[] ENUM$VALUES = new EntityRabbit.EnumMoveType[]{NONE, HOP, STEP, SPRINT, ATTACK};
      SPRINT(1.75F, 0.4F, 1, 8);

      private final float field_180077_g;
      HOP(0.8F, 0.2F, 20, 10);

      public float getSpeed() {
         return this.speed;
      }

      public float func_180074_b() {
         return this.field_180077_g;
      }

      private EnumMoveType(float var3, float var4, int var5, int var6) {
         this.speed = var3;
         this.field_180077_g = var4;
         this.duration = var5;
         this.field_180085_i = var6;
      }

      public int func_180073_d() {
         return this.field_180085_i;
      }

      public int getDuration() {
         return this.duration;
      }
   }

   static class AIRaidFarm extends EntityAIMoveToBlock {
      private boolean field_179499_e = false;
      private boolean field_179498_d;
      private final EntityRabbit field_179500_c;

      public void startExecuting() {
         super.startExecuting();
      }

      public void resetTask() {
         super.resetTask();
      }

      public boolean continueExecuting() {
         return this.field_179499_e && super.continueExecuting();
      }

      public void updateTask() {
         super.updateTask();
         this.field_179500_c.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.field_179500_c.getVerticalFaceSpeed());
         if (this.getIsAboveDestination()) {
            World var1 = this.field_179500_c.worldObj;
            BlockPos var2 = this.destinationBlock.up();
            IBlockState var3 = var1.getBlockState(var2);
            Block var4 = var3.getBlock();
            if (this.field_179499_e && var4 instanceof BlockCarrot && (Integer)var3.getValue(BlockCarrot.AGE) == 7) {
               var1.setBlockState(var2, Blocks.air.getDefaultState(), 2);
               var1.destroyBlock(var2, true);
               this.field_179500_c.createEatingParticles();
            }

            this.field_179499_e = false;
            this.runDelay = 10;
         }

      }

      protected boolean shouldMoveTo(World var1, BlockPos var2) {
         Block var3 = var1.getBlockState(var2).getBlock();
         if (var3 == Blocks.farmland) {
            var2 = var2.up();
            IBlockState var4 = var1.getBlockState(var2);
            var3 = var4.getBlock();
            if (var3 instanceof BlockCarrot && (Integer)var4.getValue(BlockCarrot.AGE) == 7 && this.field_179498_d && !this.field_179499_e) {
               this.field_179499_e = true;
               return true;
            }
         }

         return false;
      }

      public boolean shouldExecute() {
         if (this.runDelay <= 0) {
            if (!this.field_179500_c.worldObj.getGameRules().getBoolean("mobGriefing")) {
               return false;
            }

            this.field_179499_e = false;
            this.field_179498_d = EntityRabbit.access$1(this.field_179500_c);
         }

         return super.shouldExecute();
      }

      public AIRaidFarm(EntityRabbit var1) {
         super(var1, 0.699999988079071D, 16);
         this.field_179500_c = var1;
      }
   }
}
