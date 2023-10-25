/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityGhast
extends EntityFlying
implements IMob {
    private int explosionStrength = 1;

    @Override
    protected float getSoundVolume() {
        return 10.0f;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
    }

    public boolean isAttacking() {
        return this.dataWatcher.getWatchableObjectByte(16) != 0;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (byte)0);
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (this.isEntityInvulnerable(damageSource)) {
            return false;
        }
        if ("fireball".equals(damageSource.getDamageType()) && damageSource.getEntity() instanceof EntityPlayer) {
            super.attackEntityFrom(damageSource, 1000.0f);
            ((EntityPlayer)damageSource.getEntity()).triggerAchievement(AchievementList.ghast);
            return true;
        }
        return super.attackEntityFrom(damageSource, f);
    }

    public void setAttacking(boolean bl) {
        this.dataWatcher.updateObject(16, (byte)(bl ? 1 : 0));
    }

    @Override
    protected String getLivingSound() {
        return "mob.ghast.moan";
    }

    @Override
    protected String getDeathSound() {
        return "mob.ghast.death";
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(100.0);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setInteger("ExplosionPower", this.explosionStrength);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        if (nBTTagCompound.hasKey("ExplosionPower", 99)) {
            this.explosionStrength = nBTTagCompound.getInteger("ExplosionPower");
        }
    }

    @Override
    protected void dropFewItems(boolean bl, int n) {
        int n2 = this.rand.nextInt(2) + this.rand.nextInt(1 + n);
        int n3 = 0;
        while (n3 < n2) {
            this.dropItem(Items.ghast_tear, 1);
            ++n3;
        }
        n2 = this.rand.nextInt(3) + this.rand.nextInt(1 + n);
        n3 = 0;
        while (n3 < n2) {
            this.dropItem(Items.gunpowder, 1);
            ++n3;
        }
    }

    @Override
    protected String getHurtSound() {
        return "mob.ghast.scream";
    }

    @Override
    protected Item getDropItem() {
        return Items.gunpowder;
    }

    public EntityGhast(World world) {
        super(world);
        this.setSize(4.0f, 4.0f);
        this.isImmuneToFire = true;
        this.experienceValue = 5;
        this.moveHelper = new GhastMoveHelper(this);
        this.tasks.addTask(5, new AIRandomFly(this));
        this.tasks.addTask(7, new AILookAround(this));
        this.tasks.addTask(7, new AIFireballAttack(this));
        this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
    }

    @Override
    public float getEyeHeight() {
        return 2.6f;
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.rand.nextInt(20) == 0 && super.getCanSpawnHere() && this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    public int getFireballStrength() {
        return this.explosionStrength;
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    static class AIRandomFly
    extends EntityAIBase {
        private EntityGhast parentEntity;

        @Override
        public boolean continueExecuting() {
            return false;
        }

        public AIRandomFly(EntityGhast entityGhast) {
            this.parentEntity = entityGhast;
            this.setMutexBits(1);
        }

        @Override
        public void startExecuting() {
            Random random = this.parentEntity.getRNG();
            double d = this.parentEntity.posX + (double)((random.nextFloat() * 2.0f - 1.0f) * 16.0f);
            double d2 = this.parentEntity.posY + (double)((random.nextFloat() * 2.0f - 1.0f) * 16.0f);
            double d3 = this.parentEntity.posZ + (double)((random.nextFloat() * 2.0f - 1.0f) * 16.0f);
            this.parentEntity.getMoveHelper().setMoveTo(d, d2, d3, 1.0);
        }

        @Override
        public boolean shouldExecute() {
            double d;
            double d2;
            EntityMoveHelper entityMoveHelper = this.parentEntity.getMoveHelper();
            if (!entityMoveHelper.isUpdating()) {
                return true;
            }
            double d3 = entityMoveHelper.getX() - this.parentEntity.posX;
            double d4 = d3 * d3 + (d2 = entityMoveHelper.getY() - this.parentEntity.posY) * d2 + (d = entityMoveHelper.getZ() - this.parentEntity.posZ) * d;
            return d4 < 1.0 || d4 > 3600.0;
        }
    }

    static class AIFireballAttack
    extends EntityAIBase {
        public int attackTimer;
        private EntityGhast parentEntity;

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
            EntityLivingBase entityLivingBase = this.parentEntity.getAttackTarget();
            double d = 64.0;
            if (entityLivingBase.getDistanceSqToEntity(this.parentEntity) < d * d && this.parentEntity.canEntityBeSeen(entityLivingBase)) {
                World world = this.parentEntity.worldObj;
                ++this.attackTimer;
                if (this.attackTimer == 10) {
                    world.playAuxSFXAtEntity(null, 1007, new BlockPos(this.parentEntity), 0);
                }
                if (this.attackTimer == 20) {
                    double d2 = 4.0;
                    Vec3 vec3 = this.parentEntity.getLook(1.0f);
                    double d3 = entityLivingBase.posX - (this.parentEntity.posX + vec3.xCoord * d2);
                    double d4 = entityLivingBase.getEntityBoundingBox().minY + (double)(entityLivingBase.height / 2.0f) - (0.5 + this.parentEntity.posY + (double)(this.parentEntity.height / 2.0f));
                    double d5 = entityLivingBase.posZ - (this.parentEntity.posZ + vec3.zCoord * d2);
                    world.playAuxSFXAtEntity(null, 1008, new BlockPos(this.parentEntity), 0);
                    EntityLargeFireball entityLargeFireball = new EntityLargeFireball(world, this.parentEntity, d3, d4, d5);
                    entityLargeFireball.explosionPower = this.parentEntity.getFireballStrength();
                    entityLargeFireball.posX = this.parentEntity.posX + vec3.xCoord * d2;
                    entityLargeFireball.posY = this.parentEntity.posY + (double)(this.parentEntity.height / 2.0f) + 0.5;
                    entityLargeFireball.posZ = this.parentEntity.posZ + vec3.zCoord * d2;
                    world.spawnEntityInWorld(entityLargeFireball);
                    this.attackTimer = -40;
                }
            } else if (this.attackTimer > 0) {
                --this.attackTimer;
            }
            this.parentEntity.setAttacking(this.attackTimer > 10);
        }

        public AIFireballAttack(EntityGhast entityGhast) {
            this.parentEntity = entityGhast;
        }

        @Override
        public boolean shouldExecute() {
            return this.parentEntity.getAttackTarget() != null;
        }
    }

    static class GhastMoveHelper
    extends EntityMoveHelper {
        private EntityGhast parentEntity;
        private int courseChangeCooldown;

        private boolean isNotColliding(double d, double d2, double d3, double d4) {
            double d5 = (d - this.parentEntity.posX) / d4;
            double d6 = (d2 - this.parentEntity.posY) / d4;
            double d7 = (d3 - this.parentEntity.posZ) / d4;
            AxisAlignedBB axisAlignedBB = this.parentEntity.getEntityBoundingBox();
            int n = 1;
            while ((double)n < d4) {
                if (!this.parentEntity.worldObj.getCollidingBoundingBoxes(this.parentEntity, axisAlignedBB = axisAlignedBB.offset(d5, d6, d7)).isEmpty()) {
                    return false;
                }
                ++n;
            }
            return true;
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.update) {
                double d = this.posX - this.parentEntity.posX;
                double d2 = this.posY - this.parentEntity.posY;
                double d3 = this.posZ - this.parentEntity.posZ;
                double d4 = d * d + d2 * d2 + d3 * d3;
                if (this.courseChangeCooldown-- <= 0) {
                    this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
                    if (this.isNotColliding(this.posX, this.posY, this.posZ, d4 = (double)MathHelper.sqrt_double(d4))) {
                        this.parentEntity.motionX += d / d4 * 0.1;
                        this.parentEntity.motionY += d2 / d4 * 0.1;
                        this.parentEntity.motionZ += d3 / d4 * 0.1;
                    } else {
                        this.update = false;
                    }
                }
            }
        }

        public GhastMoveHelper(EntityGhast entityGhast) {
            super(entityGhast);
            this.parentEntity = entityGhast;
        }
    }

    static class AILookAround
    extends EntityAIBase {
        private EntityGhast parentEntity;

        @Override
        public void updateTask() {
            if (this.parentEntity.getAttackTarget() == null) {
                this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw = -((float)MathHelper.func_181159_b(this.parentEntity.motionX, this.parentEntity.motionZ)) * 180.0f / (float)Math.PI;
            } else {
                EntityLivingBase entityLivingBase = this.parentEntity.getAttackTarget();
                double d = 64.0;
                if (entityLivingBase.getDistanceSqToEntity(this.parentEntity) < d * d) {
                    double d2 = entityLivingBase.posX - this.parentEntity.posX;
                    double d3 = entityLivingBase.posZ - this.parentEntity.posZ;
                    this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw = -((float)MathHelper.func_181159_b(d2, d3)) * 180.0f / (float)Math.PI;
                }
            }
        }

        @Override
        public boolean shouldExecute() {
            return true;
        }

        public AILookAround(EntityGhast entityGhast) {
            this.parentEntity = entityGhast;
            this.setMutexBits(2);
        }
    }
}

