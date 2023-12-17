// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.util.math.AxisAlignedBB;
import java.util.Random;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundCategory;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.entity.EntityFlying;

public class EntityGhast extends EntityFlying implements IMob
{
    private static final DataParameter<Boolean> ATTACKING;
    private int explosionStrength;
    
    public EntityGhast(final World worldIn) {
        super(worldIn);
        this.explosionStrength = 1;
        this.setSize(4.0f, 4.0f);
        this.isImmuneToFire = true;
        this.experienceValue = 5;
        this.moveHelper = new GhastMoveHelper(this);
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(5, new AIRandomFly(this));
        this.tasks.addTask(7, new AILookAround(this));
        this.tasks.addTask(7, new AIFireballAttack(this));
        this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
    }
    
    public boolean isAttacking() {
        return this.dataManager.get(EntityGhast.ATTACKING);
    }
    
    public void setAttacking(final boolean attacking) {
        this.dataManager.set(EntityGhast.ATTACKING, attacking);
    }
    
    public int getFireballStrength() {
        return this.explosionStrength;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (source.getImmediateSource() instanceof EntityLargeFireball && source.getTrueSource() instanceof EntityPlayer) {
            super.attackEntityFrom(source, 1000.0f);
            return true;
        }
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityGhast.ATTACKING, false);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100.0);
    }
    
    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_GHAST_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_GHAST_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_GHAST_DEATH;
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_GHAST;
    }
    
    @Override
    protected float getSoundVolume() {
        return 10.0f;
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return this.rand.nextInt(20) == 0 && super.getCanSpawnHere() && this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
    }
    
    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }
    
    public static void registerFixesGhast(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityGhast.class);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("ExplosionPower", this.explosionStrength);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("ExplosionPower", 99)) {
            this.explosionStrength = compound.getInteger("ExplosionPower");
        }
    }
    
    @Override
    public float getEyeHeight() {
        return 2.6f;
    }
    
    static {
        ATTACKING = EntityDataManager.createKey(EntityGhast.class, DataSerializers.BOOLEAN);
    }
    
    static class AIFireballAttack extends EntityAIBase
    {
        private final EntityGhast parentEntity;
        public int attackTimer;
        
        public AIFireballAttack(final EntityGhast ghast) {
            this.parentEntity = ghast;
        }
        
        @Override
        public boolean shouldExecute() {
            return this.parentEntity.getAttackTarget() != null;
        }
        
        @Override
        public void startExecuting() {
            this.attackTimer = 0;
        }
        
        @Override
        public void resetTask() {
            this.parentEntity.setAttacking(false);
        }
        
        @Override
        public void updateTask() {
            final EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
            final double d0 = 64.0;
            if (entitylivingbase.getDistanceSq(this.parentEntity) < 4096.0 && this.parentEntity.canEntityBeSeen(entitylivingbase)) {
                final World world = this.parentEntity.world;
                ++this.attackTimer;
                if (this.attackTimer == 10) {
                    world.playEvent(null, 1015, new BlockPos(this.parentEntity), 0);
                }
                if (this.attackTimer == 20) {
                    final double d2 = 4.0;
                    final Vec3d vec3d = this.parentEntity.getLook(1.0f);
                    final double d3 = entitylivingbase.posX - (this.parentEntity.posX + vec3d.x * 4.0);
                    final double d4 = entitylivingbase.getEntityBoundingBox().minY + entitylivingbase.height / 2.0f - (0.5 + this.parentEntity.posY + this.parentEntity.height / 2.0f);
                    final double d5 = entitylivingbase.posZ - (this.parentEntity.posZ + vec3d.z * 4.0);
                    world.playEvent(null, 1016, new BlockPos(this.parentEntity), 0);
                    final EntityLargeFireball entitylargefireball = new EntityLargeFireball(world, this.parentEntity, d3, d4, d5);
                    entitylargefireball.explosionPower = this.parentEntity.getFireballStrength();
                    entitylargefireball.posX = this.parentEntity.posX + vec3d.x * 4.0;
                    entitylargefireball.posY = this.parentEntity.posY + this.parentEntity.height / 2.0f + 0.5;
                    entitylargefireball.posZ = this.parentEntity.posZ + vec3d.z * 4.0;
                    world.spawnEntity(entitylargefireball);
                    this.attackTimer = -40;
                }
            }
            else if (this.attackTimer > 0) {
                --this.attackTimer;
            }
            this.parentEntity.setAttacking(this.attackTimer > 10);
        }
    }
    
    static class AILookAround extends EntityAIBase
    {
        private final EntityGhast parentEntity;
        
        public AILookAround(final EntityGhast ghast) {
            this.parentEntity = ghast;
            this.setMutexBits(2);
        }
        
        @Override
        public boolean shouldExecute() {
            return true;
        }
        
        @Override
        public void updateTask() {
            if (this.parentEntity.getAttackTarget() == null) {
                this.parentEntity.rotationYaw = -(float)MathHelper.atan2(this.parentEntity.motionX, this.parentEntity.motionZ) * 57.295776f;
                this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
            }
            else {
                final EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
                final double d0 = 64.0;
                if (entitylivingbase.getDistanceSq(this.parentEntity) < 4096.0) {
                    final double d2 = entitylivingbase.posX - this.parentEntity.posX;
                    final double d3 = entitylivingbase.posZ - this.parentEntity.posZ;
                    this.parentEntity.rotationYaw = -(float)MathHelper.atan2(d2, d3) * 57.295776f;
                    this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
                }
            }
        }
    }
    
    static class AIRandomFly extends EntityAIBase
    {
        private final EntityGhast parentEntity;
        
        public AIRandomFly(final EntityGhast ghast) {
            this.parentEntity = ghast;
            this.setMutexBits(1);
        }
        
        @Override
        public boolean shouldExecute() {
            final EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();
            if (!entitymovehelper.isUpdating()) {
                return true;
            }
            final double d0 = entitymovehelper.getX() - this.parentEntity.posX;
            final double d2 = entitymovehelper.getY() - this.parentEntity.posY;
            final double d3 = entitymovehelper.getZ() - this.parentEntity.posZ;
            final double d4 = d0 * d0 + d2 * d2 + d3 * d3;
            return d4 < 1.0 || d4 > 3600.0;
        }
        
        @Override
        public boolean shouldContinueExecuting() {
            return false;
        }
        
        @Override
        public void startExecuting() {
            final Random random = this.parentEntity.getRNG();
            final double d0 = this.parentEntity.posX + (random.nextFloat() * 2.0f - 1.0f) * 16.0f;
            final double d2 = this.parentEntity.posY + (random.nextFloat() * 2.0f - 1.0f) * 16.0f;
            final double d3 = this.parentEntity.posZ + (random.nextFloat() * 2.0f - 1.0f) * 16.0f;
            this.parentEntity.getMoveHelper().setMoveTo(d0, d2, d3, 1.0);
        }
    }
    
    static class GhastMoveHelper extends EntityMoveHelper
    {
        private final EntityGhast parentEntity;
        private int courseChangeCooldown;
        
        public GhastMoveHelper(final EntityGhast ghast) {
            super(ghast);
            this.parentEntity = ghast;
        }
        
        @Override
        public void onUpdateMoveHelper() {
            if (this.action == Action.MOVE_TO) {
                final double d0 = this.posX - this.parentEntity.posX;
                final double d2 = this.posY - this.parentEntity.posY;
                final double d3 = this.posZ - this.parentEntity.posZ;
                double d4 = d0 * d0 + d2 * d2 + d3 * d3;
                if (this.courseChangeCooldown-- <= 0) {
                    this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
                    d4 = MathHelper.sqrt(d4);
                    if (this.isNotColliding(this.posX, this.posY, this.posZ, d4)) {
                        final EntityGhast parentEntity = this.parentEntity;
                        parentEntity.motionX += d0 / d4 * 0.1;
                        final EntityGhast parentEntity2 = this.parentEntity;
                        parentEntity2.motionY += d2 / d4 * 0.1;
                        final EntityGhast parentEntity3 = this.parentEntity;
                        parentEntity3.motionZ += d3 / d4 * 0.1;
                    }
                    else {
                        this.action = Action.WAIT;
                    }
                }
            }
        }
        
        private boolean isNotColliding(final double x, final double y, final double z, final double p_179926_7_) {
            final double d0 = (x - this.parentEntity.posX) / p_179926_7_;
            final double d2 = (y - this.parentEntity.posY) / p_179926_7_;
            final double d3 = (z - this.parentEntity.posZ) / p_179926_7_;
            AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();
            for (int i = 1; i < p_179926_7_; ++i) {
                axisalignedbb = axisalignedbb.offset(d0, d2, d3);
                if (!this.parentEntity.world.getCollisionBoxes(this.parentEntity, axisalignedbb).isEmpty()) {
                    return false;
                }
            }
            return true;
        }
    }
}
