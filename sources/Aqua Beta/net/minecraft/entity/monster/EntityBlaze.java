package net.minecraft.entity.monster;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityBlaze
extends EntityMob {
    private float heightOffset = 0.5f;
    private int heightOffsetUpdateTime;

    public EntityBlaze(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
        this.experienceValue = 10;
        this.tasks.addTask(4, (EntityAIBase)new AIFireballAttack(this));
        this.tasks.addTask(5, (EntityAIBase)new EntityAIMoveTowardsRestriction((EntityCreature)this, 1.0));
        this.tasks.addTask(7, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0));
        this.tasks.addTask(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
        this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)this, true, new Class[0]));
        this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)this, EntityPlayer.class, true));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue((double)0.23f);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(48.0);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (Object)new Byte(0));
    }

    protected String getLivingSound() {
        return "mob.blaze.breathe";
    }

    protected String getHurtSound() {
        return "mob.blaze.hit";
    }

    protected String getDeathSound() {
        return "mob.blaze.death";
    }

    public int getBrightnessForRender(float partialTicks) {
        return 0xF000F0;
    }

    public float getBrightness(float partialTicks) {
        return 1.0f;
    }

    public void onLivingUpdate() {
        if (!this.onGround && this.motionY < 0.0) {
            this.motionY *= 0.6;
        }
        if (this.worldObj.isRemote) {
            if (this.rand.nextInt(24) == 0 && !this.isSilent()) {
                this.worldObj.playSound(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5, "fire.fire", 1.0f + this.rand.nextFloat(), this.rand.nextFloat() * 0.7f + 0.3f, false);
            }
            for (int i = 0; i < 2; ++i) {
                this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5) * (double)this.width, 0.0, 0.0, 0.0, new int[0]);
            }
        }
        super.onLivingUpdate();
    }

    protected void updateAITasks() {
        EntityLivingBase entitylivingbase;
        if (this.isWet()) {
            this.attackEntityFrom(DamageSource.drown, 1.0f);
        }
        --this.heightOffsetUpdateTime;
        if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5f + (float)this.rand.nextGaussian() * 3.0f;
        }
        if ((entitylivingbase = this.getAttackTarget()) != null && entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() > this.posY + (double)this.getEyeHeight() + (double)this.heightOffset) {
            this.motionY += ((double)0.3f - this.motionY) * (double)0.3f;
            this.isAirBorne = true;
        }
        super.updateAITasks();
    }

    public void fall(float distance, float damageMultiplier) {
    }

    protected Item getDropItem() {
        return Items.blaze_rod;
    }

    public boolean isBurning() {
        return this.func_70845_n();
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
        if (wasRecentlyHit) {
            int i = this.rand.nextInt(2 + lootingModifier);
            for (int j = 0; j < i; ++j) {
                this.dropItem(Items.blaze_rod, 1);
            }
        }
    }

    public boolean func_70845_n() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setOnFire(boolean onFire) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);
        b0 = onFire ? (byte)(b0 | 1) : (byte)(b0 & 0xFFFFFFFE);
        this.dataWatcher.updateObject(16, (Object)b0);
    }

    protected boolean isValidLightLevel() {
        return true;
    }
}
