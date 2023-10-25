/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.monster;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityGhast
extends EntityFlying
implements IMob {
    private static final DataParameter<Boolean> ATTACKING = EntityDataManager.createKey(EntityGhast.class, DataSerializers.BOOLEAN);
    private int explosionStrength = 1;

    public EntityGhast(World worldIn) {
        super(worldIn);
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
        return this.dataManager.get(ATTACKING);
    }

    public void setAttacking(boolean attacking) {
        this.dataManager.set(ATTACKING, attacking);
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
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (source.getSourceOfDamage() instanceof EntityLargeFireball && source.getEntity() instanceof EntityPlayer) {
            super.attackEntityFrom(source, 1000.0f);
            return true;
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ATTACKING, false);
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
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_GHAST_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_GHAST_DEATH;
    }

    @Override
    @Nullable
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

    public static void registerFixesGhast(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityGhast.class);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("ExplosionPower", this.explosionStrength);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("ExplosionPower", 99)) {
            this.explosionStrength = compound.getInteger("ExplosionPower");
        }
    }

    @Override
    public float getEyeHeight() {
        return 2.6f;
    }

    static class GhastMoveHelper
    extends EntityMoveHelper {
        private final EntityGhast parentEntity;
        private int courseChangeCooldown;

        public GhastMoveHelper(EntityGhast ghast) {
            super(ghast);
            this.parentEntity = ghast;
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO) {
                double d0 = this.posX - this.parentEntity.posX;
                double d1 = this.posY - this.parentEntity.posY;
                double d2 = this.posZ - this.parentEntity.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (this.courseChangeCooldown-- <= 0) {
                    this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
                    if (this.isNotColliding(this.posX, this.posY, this.posZ, d3 = (double)MathHelper.sqrt(d3))) {
                        this.parentEntity.motionX += d0 / d3 * 0.1;
                        this.parentEntity.motionY += d1 / d3 * 0.1;
                        this.parentEntity.motionZ += d2 / d3 * 0.1;
                    } else {
                        this.action = EntityMoveHelper.Action.WAIT;
                    }
                }
            }
        }

        private boolean isNotColliding(double x, double y, double z, double p_179926_7_) {
            double d0 = (x - this.parentEntity.posX) / p_179926_7_;
            double d1 = (y - this.parentEntity.posY) / p_179926_7_;
            double d2 = (z - this.parentEntity.posZ) / p_179926_7_;
            AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();
            int i = 1;
            while ((double)i < p_179926_7_) {
                if (!this.parentEntity.world.getCollisionBoxes(this.parentEntity, axisalignedbb = axisalignedbb.offset(d0, d1, d2)).isEmpty()) {
                    return false;
                }
                ++i;
            }
            return true;
        }
    }

    static class AIRandomFly
    extends EntityAIBase {
        private final EntityGhast parentEntity;

        public AIRandomFly(EntityGhast ghast) {
            this.parentEntity = ghast;
            this.setMutexBits(1);
        }

        @Override
        public boolean shouldExecute() {
            double d2;
            double d1;
            EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();
            if (!entitymovehelper.isUpdating()) {
                return true;
            }
            double d0 = entitymovehelper.getX() - this.parentEntity.posX;
            double d3 = d0 * d0 + (d1 = entitymovehelper.getY() - this.parentEntity.posY) * d1 + (d2 = entitymovehelper.getZ() - this.parentEntity.posZ) * d2;
            return d3 < 1.0 || d3 > 3600.0;
        }

        @Override
        public boolean continueExecuting() {
            return false;
        }

        @Override
        public void startExecuting() {
            Random random = this.parentEntity.getRNG();
            double d0 = this.parentEntity.posX + (double)((random.nextFloat() * 2.0f - 1.0f) * 16.0f);
            double d1 = this.parentEntity.posY + (double)((random.nextFloat() * 2.0f - 1.0f) * 16.0f);
            double d2 = this.parentEntity.posZ + (double)((random.nextFloat() * 2.0f - 1.0f) * 16.0f);
            this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0);
        }
    }

    static class AILookAround
    extends EntityAIBase {
        private final EntityGhast parentEntity;

        public AILookAround(EntityGhast ghast) {
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
                this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw = -((float)MathHelper.atan2(this.parentEntity.motionX, this.parentEntity.motionZ)) * 57.295776f;
            } else {
                EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
                double d0 = 64.0;
                if (entitylivingbase.getDistanceSqToEntity(this.parentEntity) < 4096.0) {
                    double d1 = entitylivingbase.posX - this.parentEntity.posX;
                    double d2 = entitylivingbase.posZ - this.parentEntity.posZ;
                    this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw = -((float)MathHelper.atan2(d1, d2)) * 57.295776f;
                }
            }
        }
    }

    static class AIFireballAttack
    extends EntityAIBase {
        private final EntityGhast parentEntity;
        public int attackTimer;

        public AIFireballAttack(EntityGhast ghast) {
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
            EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
            double d0 = 64.0;
            if (entitylivingbase.getDistanceSqToEntity(this.parentEntity) < 4096.0 && this.parentEntity.canEntityBeSeen(entitylivingbase)) {
                World world = this.parentEntity.world;
                ++this.attackTimer;
                if (this.attackTimer == 10) {
                    world.playEvent(null, 1015, new BlockPos(this.parentEntity), 0);
                }
                if (this.attackTimer == 20) {
                    double d1 = 4.0;
                    Vec3d vec3d = this.parentEntity.getLook(1.0f);
                    double d2 = entitylivingbase.posX - (this.parentEntity.posX + vec3d.x * 4.0);
                    double d3 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0f) - (0.5 + this.parentEntity.posY + (double)(this.parentEntity.height / 2.0f));
                    double d4 = entitylivingbase.posZ - (this.parentEntity.posZ + vec3d.z * 4.0);
                    world.playEvent(null, 1016, new BlockPos(this.parentEntity), 0);
                    EntityLargeFireball entitylargefireball = new EntityLargeFireball(world, this.parentEntity, d2, d3, d4);
                    entitylargefireball.explosionPower = this.parentEntity.getFireballStrength();
                    entitylargefireball.posX = this.parentEntity.posX + vec3d.x * 4.0;
                    entitylargefireball.posY = this.parentEntity.posY + (double)(this.parentEntity.height / 2.0f) + 0.5;
                    entitylargefireball.posZ = this.parentEntity.posZ + vec3d.z * 4.0;
                    world.spawnEntityInWorld(entitylargefireball);
                    this.attackTimer = -40;
                }
            } else if (this.attackTimer > 0) {
                --this.attackTimer;
            }
            this.parentEntity.setAttacking(this.attackTimer > 10);
        }
    }
}

