/*
 * Decompiled with CFR 0.150.
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
    private float heightOffset = 0.5f;
    private int heightOffsetUpdateTime;
    private static final String __OBFID = "CL_00001682";

    public EntityBlaze(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
        this.experienceValue = 10;
        this.tasks.addTask(4, new AIFireballAttack());
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget((EntityCreature)this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget((EntityCreature)this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23f);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(48.0);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte(0));
    }

    @Override
    protected String getLivingSound() {
        return "mob.blaze.breathe";
    }

    @Override
    protected String getHurtSound() {
        return "mob.blaze.hit";
    }

    @Override
    protected String getDeathSound() {
        return "mob.blaze.death";
    }

    @Override
    public int getBrightnessForRender(float p_70070_1_) {
        return 0xF000F0;
    }

    @Override
    public float getBrightness(float p_70013_1_) {
        return 1.0f;
    }

    @Override
    public void onLivingUpdate() {
        if (!this.onGround && this.motionY < 0.0) {
            this.motionY *= 0.6;
        }
        if (this.worldObj.isRemote) {
            if (this.rand.nextInt(24) == 0 && !this.isSlient()) {
                this.worldObj.playSound(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5, "fire.fire", 1.0f + this.rand.nextFloat(), this.rand.nextFloat() * 0.7f + 0.3f, false);
            }
            for (int var1 = 0; var1 < 2; ++var1) {
                this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5) * (double)this.width, 0.0, 0.0, 0.0, new int[0]);
            }
        }
        super.onLivingUpdate();
    }

    @Override
    protected void updateAITasks() {
        EntityLivingBase var1;
        if (this.isWet()) {
            this.attackEntityFrom(DamageSource.drown, 1.0f);
        }
        --this.heightOffsetUpdateTime;
        if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5f + (float)this.rand.nextGaussian() * 3.0f;
        }
        if ((var1 = this.getAttackTarget()) != null && var1.posY + (double)var1.getEyeHeight() > this.posY + (double)this.getEyeHeight() + (double)this.heightOffset) {
            this.motionY += ((double)0.3f - this.motionY) * (double)0.3f;
            this.isAirBorne = true;
        }
        super.updateAITasks();
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected Item getDropItem() {
        return Items.blaze_rod;
    }

    @Override
    public boolean isBurning() {
        return this.func_70845_n();
    }

    @Override
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        if (p_70628_1_) {
            int var3 = this.rand.nextInt(2 + p_70628_2_);
            for (int var4 = 0; var4 < var3; ++var4) {
                this.dropItem(Items.blaze_rod, 1);
            }
        }
    }

    public boolean func_70845_n() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void func_70844_e(boolean p_70844_1_) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        var2 = p_70844_1_ ? (byte)(var2 | 1) : (byte)(var2 & 0xFFFFFFFE);
        this.dataWatcher.updateObject(16, var2);
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }

    class AIFireballAttack
    extends EntityAIBase {
        private EntityBlaze field_179469_a;
        private int field_179467_b;
        private int field_179468_c;
        private static final String __OBFID = "CL_00002225";

        public AIFireballAttack() {
            this.field_179469_a = EntityBlaze.this;
            this.setMutexBits(3);
        }

        @Override
        public boolean shouldExecute() {
            EntityLivingBase var1 = this.field_179469_a.getAttackTarget();
            return var1 != null && var1.isEntityAlive();
        }

        @Override
        public void startExecuting() {
            this.field_179467_b = 0;
        }

        @Override
        public void resetTask() {
            this.field_179469_a.func_70844_e(false);
        }

        @Override
        public void updateTask() {
            --this.field_179468_c;
            EntityLivingBase var1 = this.field_179469_a.getAttackTarget();
            double var2 = this.field_179469_a.getDistanceSqToEntity(var1);
            if (var2 < 4.0) {
                if (this.field_179468_c <= 0) {
                    this.field_179468_c = 20;
                    this.field_179469_a.attackEntityAsMob(var1);
                }
                this.field_179469_a.getMoveHelper().setMoveTo(var1.posX, var1.posY, var1.posZ, 1.0);
            } else if (var2 < 256.0) {
                double var4 = var1.posX - this.field_179469_a.posX;
                double var6 = var1.getEntityBoundingBox().minY + (double)(var1.height / 2.0f) - (this.field_179469_a.posY + (double)(this.field_179469_a.height / 2.0f));
                double var8 = var1.posZ - this.field_179469_a.posZ;
                if (this.field_179468_c <= 0) {
                    ++this.field_179467_b;
                    if (this.field_179467_b == 1) {
                        this.field_179468_c = 60;
                        this.field_179469_a.func_70844_e(true);
                    } else if (this.field_179467_b <= 4) {
                        this.field_179468_c = 6;
                    } else {
                        this.field_179468_c = 100;
                        this.field_179467_b = 0;
                        this.field_179469_a.func_70844_e(false);
                    }
                    if (this.field_179467_b > 1) {
                        float var10 = MathHelper.sqrt_float(MathHelper.sqrt_double(var2)) * 0.5f;
                        this.field_179469_a.worldObj.playAuxSFXAtEntity(null, 1009, new BlockPos((int)this.field_179469_a.posX, (int)this.field_179469_a.posY, (int)this.field_179469_a.posZ), 0);
                        for (int var11 = 0; var11 < 1; ++var11) {
                            EntitySmallFireball var12 = new EntitySmallFireball(this.field_179469_a.worldObj, this.field_179469_a, var4 + this.field_179469_a.getRNG().nextGaussian() * (double)var10, var6, var8 + this.field_179469_a.getRNG().nextGaussian() * (double)var10);
                            var12.posY = this.field_179469_a.posY + (double)(this.field_179469_a.height / 2.0f) + 0.5;
                            this.field_179469_a.worldObj.spawnEntityInWorld(var12);
                        }
                    }
                }
                this.field_179469_a.getLookHelper().setLookPositionWithEntity(var1, 10.0f, 10.0f);
            } else {
                this.field_179469_a.getNavigator().clearPathEntity();
                this.field_179469_a.getMoveHelper().setMoveTo(var1.posX, var1.posY, var1.posZ, 1.0);
            }
            super.updateTask();
        }
    }
}

