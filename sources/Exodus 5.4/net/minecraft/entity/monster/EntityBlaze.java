/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.monster;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBlaze
extends EntityMob {
    private int heightOffsetUpdateTime;
    private float heightOffset = 0.5f;

    @Override
    public void onLivingUpdate() {
        if (!this.onGround && this.motionY < 0.0) {
            this.motionY *= 0.6;
        }
        if (this.worldObj.isRemote) {
            if (this.rand.nextInt(24) == 0 && !this.isSilent()) {
                this.worldObj.playSound(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5, "fire.fire", 1.0f + this.rand.nextFloat(), this.rand.nextFloat() * 0.7f + 0.3f, false);
            }
            int n = 0;
            while (n < 2) {
                this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5) * (double)this.width, 0.0, 0.0, 0.0, new int[0]);
                ++n;
            }
        }
        super.onLivingUpdate();
    }

    public boolean func_70845_n() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    @Override
    protected String getHurtSound() {
        return "mob.blaze.hit";
    }

    @Override
    protected Item getDropItem() {
        return Items.blaze_rod;
    }

    @Override
    public void fall(float f, float f2) {
    }

    @Override
    public float getBrightness(float f) {
        return 1.0f;
    }

    @Override
    protected void updateAITasks() {
        EntityLivingBase entityLivingBase;
        if (this.isWet()) {
            this.attackEntityFrom(DamageSource.drown, 1.0f);
        }
        --this.heightOffsetUpdateTime;
        if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5f + (float)this.rand.nextGaussian() * 3.0f;
        }
        if ((entityLivingBase = this.getAttackTarget()) != null && entityLivingBase.posY + (double)entityLivingBase.getEyeHeight() > this.posY + (double)this.getEyeHeight() + (double)this.heightOffset) {
            this.motionY += ((double)0.3f - this.motionY) * (double)0.3f;
            this.isAirBorne = true;
        }
        super.updateAITasks();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte(0));
    }

    public void setOnFire(boolean bl) {
        byte by = this.dataWatcher.getWatchableObjectByte(16);
        by = bl ? (byte)(by | 1) : (byte)(by & 0xFFFFFFFE);
        this.dataWatcher.updateObject(16, by);
    }

    @Override
    public int getBrightnessForRender(float f) {
        return 0xF000F0;
    }

    @Override
    protected String getDeathSound() {
        return "mob.blaze.death";
    }

    @Override
    protected void dropFewItems(boolean bl, int n) {
        if (bl) {
            int n2 = this.rand.nextInt(2 + n);
            int n3 = 0;
            while (n3 < n2) {
                this.dropItem(Items.blaze_rod, 1);
                ++n3;
            }
        }
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }

    @Override
    public boolean isBurning() {
        return this.func_70845_n();
    }

    @Override
    protected String getLivingSound() {
        return "mob.blaze.breathe";
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23f);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(48.0);
    }

    public EntityBlaze(World world) {
        super(world);
        this.isImmuneToFire = true;
        this.experienceValue = 10;
        this.tasks.addTask(4, new AIFireballAttack(this));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget((EntityCreature)this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>((EntityCreature)this, EntityPlayer.class, true));
    }

    static class AIFireballAttack
    extends EntityAIBase {
        private EntityBlaze blaze;
        private int field_179467_b;
        private int field_179468_c;

        public AIFireballAttack(EntityBlaze entityBlaze) {
            this.blaze = entityBlaze;
            this.setMutexBits(3);
        }

        @Override
        public void startExecuting() {
            this.field_179467_b = 0;
        }

        @Override
        public void resetTask() {
            this.blaze.setOnFire(false);
        }

        @Override
        public void updateTask() {
            --this.field_179468_c;
            EntityLivingBase entityLivingBase = this.blaze.getAttackTarget();
            double d = this.blaze.getDistanceSqToEntity(entityLivingBase);
            if (d < 4.0) {
                if (this.field_179468_c <= 0) {
                    this.field_179468_c = 20;
                    this.blaze.attackEntityAsMob(entityLivingBase);
                }
                this.blaze.getMoveHelper().setMoveTo(entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ, 1.0);
            } else if (d < 256.0) {
                double d2 = entityLivingBase.posX - this.blaze.posX;
                double d3 = entityLivingBase.getEntityBoundingBox().minY + (double)(entityLivingBase.height / 2.0f) - (this.blaze.posY + (double)(this.blaze.height / 2.0f));
                double d4 = entityLivingBase.posZ - this.blaze.posZ;
                if (this.field_179468_c <= 0) {
                    ++this.field_179467_b;
                    if (this.field_179467_b == 1) {
                        this.field_179468_c = 60;
                        this.blaze.setOnFire(true);
                    } else if (this.field_179467_b <= 4) {
                        this.field_179468_c = 6;
                    } else {
                        this.field_179468_c = 100;
                        this.field_179467_b = 0;
                        this.blaze.setOnFire(false);
                    }
                    if (this.field_179467_b > 1) {
                        float f = MathHelper.sqrt_float(MathHelper.sqrt_double(d)) * 0.5f;
                        this.blaze.worldObj.playAuxSFXAtEntity(null, 1009, new BlockPos((int)this.blaze.posX, (int)this.blaze.posY, (int)this.blaze.posZ), 0);
                        int n = 0;
                        while (n < 1) {
                            EntitySmallFireball entitySmallFireball = new EntitySmallFireball(this.blaze.worldObj, this.blaze, d2 + this.blaze.getRNG().nextGaussian() * (double)f, d3, d4 + this.blaze.getRNG().nextGaussian() * (double)f);
                            entitySmallFireball.posY = this.blaze.posY + (double)(this.blaze.height / 2.0f) + 0.5;
                            this.blaze.worldObj.spawnEntityInWorld(entitySmallFireball);
                            ++n;
                        }
                    }
                }
                this.blaze.getLookHelper().setLookPositionWithEntity(entityLivingBase, 10.0f, 10.0f);
            } else {
                this.blaze.getNavigator().clearPathEntity();
                this.blaze.getMoveHelper().setMoveTo(entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ, 1.0);
            }
            super.updateTask();
        }

        @Override
        public boolean shouldExecute() {
            EntityLivingBase entityLivingBase = this.blaze.getAttackTarget();
            return entityLivingBase != null && entityLivingBase.isEntityAlive();
        }
    }
}

