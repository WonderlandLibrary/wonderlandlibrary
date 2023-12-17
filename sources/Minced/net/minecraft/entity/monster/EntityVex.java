// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.entity.ai.EntityMoveHelper;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.MoverType;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EntityLiving;
import net.minecraft.network.datasync.DataParameter;

public class EntityVex extends EntityMob
{
    protected static final DataParameter<Byte> VEX_FLAGS;
    private EntityLiving owner;
    @Nullable
    private BlockPos boundOrigin;
    private boolean limitedLifespan;
    private int limitedLifeTicks;
    
    public EntityVex(final World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
        this.moveHelper = new AIMoveControl(this);
        this.setSize(0.4f, 0.8f);
        this.experienceValue = 3;
    }
    
    @Override
    public void move(final MoverType type, final double x, final double y, final double z) {
        super.move(type, x, y, z);
        this.doBlockCollisions();
    }
    
    @Override
    public void onUpdate() {
        this.noClip = true;
        super.onUpdate();
        this.noClip = false;
        this.setNoGravity(true);
        if (this.limitedLifespan && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.attackEntityFrom(DamageSource.STARVE, 1.0f);
        }
    }
    
    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(4, new AIChargeAttack());
        this.tasks.addTask(8, new AIMoveRandom());
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0f, 1.0f));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0f));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, (Class<?>[])new Class[] { EntityVex.class }));
        this.targetTasks.addTask(2, new AICopyOwnerTarget(this));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<Object>(this, EntityPlayer.class, true));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityVex.VEX_FLAGS, (Byte)0);
    }
    
    public static void registerFixesVex(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityVex.class);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("BoundX")) {
            this.boundOrigin = new BlockPos(compound.getInteger("BoundX"), compound.getInteger("BoundY"), compound.getInteger("BoundZ"));
        }
        if (compound.hasKey("LifeTicks")) {
            this.setLimitedLife(compound.getInteger("LifeTicks"));
        }
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.boundOrigin != null) {
            compound.setInteger("BoundX", this.boundOrigin.getX());
            compound.setInteger("BoundY", this.boundOrigin.getY());
            compound.setInteger("BoundZ", this.boundOrigin.getZ());
        }
        if (this.limitedLifespan) {
            compound.setInteger("LifeTicks", this.limitedLifeTicks);
        }
    }
    
    public EntityLiving getOwner() {
        return this.owner;
    }
    
    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }
    
    public void setBoundOrigin(@Nullable final BlockPos boundOriginIn) {
        this.boundOrigin = boundOriginIn;
    }
    
    private boolean getVexFlag(final int mask) {
        final int i = this.dataManager.get(EntityVex.VEX_FLAGS);
        return (i & mask) != 0x0;
    }
    
    private void setVexFlag(final int mask, final boolean value) {
        int i = this.dataManager.get(EntityVex.VEX_FLAGS);
        if (value) {
            i |= mask;
        }
        else {
            i &= ~mask;
        }
        this.dataManager.set(EntityVex.VEX_FLAGS, (byte)(i & 0xFF));
    }
    
    public boolean isCharging() {
        return this.getVexFlag(1);
    }
    
    public void setCharging(final boolean charging) {
        this.setVexFlag(1, charging);
    }
    
    public void setOwner(final EntityLiving ownerIn) {
        this.owner = ownerIn;
    }
    
    public void setLimitedLife(final int limitedLifeTicksIn) {
        this.limitedLifespan = true;
        this.limitedLifeTicks = limitedLifeTicksIn;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_VEX_AMBIENT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VEX_DEATH;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_VEX_HURT;
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_VEX;
    }
    
    @Override
    public int getBrightnessForRender() {
        return 15728880;
    }
    
    @Override
    public float getBrightness() {
        return 1.0f;
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable final IEntityLivingData livingdata) {
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);
        return super.onInitialSpawn(difficulty, livingdata);
    }
    
    @Override
    protected void setEquipmentBasedOnDifficulty(final DifficultyInstance difficulty) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.setDropChance(EntityEquipmentSlot.MAINHAND, 0.0f);
    }
    
    static {
        VEX_FLAGS = EntityDataManager.createKey(EntityVex.class, DataSerializers.BYTE);
    }
    
    class AIChargeAttack extends EntityAIBase
    {
        public AIChargeAttack() {
            this.setMutexBits(1);
        }
        
        @Override
        public boolean shouldExecute() {
            return EntityVex.this.getAttackTarget() != null && !EntityVex.this.getMoveHelper().isUpdating() && EntityVex.this.rand.nextInt(7) == 0 && EntityVex.this.getDistanceSq(EntityVex.this.getAttackTarget()) > 4.0;
        }
        
        @Override
        public boolean shouldContinueExecuting() {
            return EntityVex.this.getMoveHelper().isUpdating() && EntityVex.this.isCharging() && EntityVex.this.getAttackTarget() != null && EntityVex.this.getAttackTarget().isEntityAlive();
        }
        
        @Override
        public void startExecuting() {
            final EntityLivingBase entitylivingbase = EntityVex.this.getAttackTarget();
            final Vec3d vec3d = entitylivingbase.getPositionEyes(1.0f);
            EntityVex.this.moveHelper.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1.0);
            EntityVex.this.setCharging(true);
            EntityVex.this.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0f, 1.0f);
        }
        
        @Override
        public void resetTask() {
            EntityVex.this.setCharging(false);
        }
        
        @Override
        public void updateTask() {
            final EntityLivingBase entitylivingbase = EntityVex.this.getAttackTarget();
            if (EntityVex.this.getEntityBoundingBox().intersects(entitylivingbase.getEntityBoundingBox())) {
                EntityVex.this.attackEntityAsMob(entitylivingbase);
                EntityVex.this.setCharging(false);
            }
            else {
                final double d0 = EntityVex.this.getDistanceSq(entitylivingbase);
                if (d0 < 9.0) {
                    final Vec3d vec3d = entitylivingbase.getPositionEyes(1.0f);
                    EntityVex.this.moveHelper.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1.0);
                }
            }
        }
    }
    
    class AICopyOwnerTarget extends EntityAITarget
    {
        public AICopyOwnerTarget(final EntityCreature creature) {
            super(creature, false);
        }
        
        @Override
        public boolean shouldExecute() {
            return EntityVex.this.owner != null && EntityVex.this.owner.getAttackTarget() != null && this.isSuitableTarget(EntityVex.this.owner.getAttackTarget(), false);
        }
        
        @Override
        public void startExecuting() {
            EntityVex.this.setAttackTarget(EntityVex.this.owner.getAttackTarget());
            super.startExecuting();
        }
    }
    
    class AIMoveControl extends EntityMoveHelper
    {
        public AIMoveControl(final EntityVex vex) {
            super(vex);
        }
        
        @Override
        public void onUpdateMoveHelper() {
            if (this.action == Action.MOVE_TO) {
                final double d0 = this.posX - EntityVex.this.posX;
                final double d2 = this.posY - EntityVex.this.posY;
                final double d3 = this.posZ - EntityVex.this.posZ;
                double d4 = d0 * d0 + d2 * d2 + d3 * d3;
                d4 = MathHelper.sqrt(d4);
                if (d4 < EntityVex.this.getEntityBoundingBox().getAverageEdgeLength()) {
                    this.action = Action.WAIT;
                    final EntityVex this$0 = EntityVex.this;
                    this$0.motionX *= 0.5;
                    final EntityVex this$2 = EntityVex.this;
                    this$2.motionY *= 0.5;
                    final EntityVex this$3 = EntityVex.this;
                    this$3.motionZ *= 0.5;
                }
                else {
                    final EntityVex this$4 = EntityVex.this;
                    this$4.motionX += d0 / d4 * 0.05 * this.speed;
                    final EntityVex this$5 = EntityVex.this;
                    this$5.motionY += d2 / d4 * 0.05 * this.speed;
                    final EntityVex this$6 = EntityVex.this;
                    this$6.motionZ += d3 / d4 * 0.05 * this.speed;
                    if (EntityVex.this.getAttackTarget() == null) {
                        EntityVex.this.rotationYaw = -(float)MathHelper.atan2(EntityVex.this.motionX, EntityVex.this.motionZ) * 57.295776f;
                        EntityVex.this.renderYawOffset = EntityVex.this.rotationYaw;
                    }
                    else {
                        final double d5 = EntityVex.this.getAttackTarget().posX - EntityVex.this.posX;
                        final double d6 = EntityVex.this.getAttackTarget().posZ - EntityVex.this.posZ;
                        EntityVex.this.rotationYaw = -(float)MathHelper.atan2(d5, d6) * 57.295776f;
                        EntityVex.this.renderYawOffset = EntityVex.this.rotationYaw;
                    }
                }
            }
        }
    }
    
    class AIMoveRandom extends EntityAIBase
    {
        public AIMoveRandom() {
            this.setMutexBits(1);
        }
        
        @Override
        public boolean shouldExecute() {
            return !EntityVex.this.getMoveHelper().isUpdating() && EntityVex.this.rand.nextInt(7) == 0;
        }
        
        @Override
        public boolean shouldContinueExecuting() {
            return false;
        }
        
        @Override
        public void updateTask() {
            BlockPos blockpos = EntityVex.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = new BlockPos(EntityVex.this);
            }
            int i = 0;
            while (i < 3) {
                final BlockPos blockpos2 = blockpos.add(EntityVex.this.rand.nextInt(15) - 7, EntityVex.this.rand.nextInt(11) - 5, EntityVex.this.rand.nextInt(15) - 7);
                if (EntityVex.this.world.isAirBlock(blockpos2)) {
                    EntityVex.this.moveHelper.setMoveTo(blockpos2.getX() + 0.5, blockpos2.getY() + 0.5, blockpos2.getZ() + 0.5, 0.25);
                    if (EntityVex.this.getAttackTarget() == null) {
                        EntityVex.this.getLookHelper().setLookPosition(blockpos2.getX() + 0.5, blockpos2.getY() + 0.5, blockpos2.getZ() + 0.5, 180.0f, 20.0f);
                        break;
                    }
                    break;
                }
                else {
                    ++i;
                }
            }
        }
    }
}
