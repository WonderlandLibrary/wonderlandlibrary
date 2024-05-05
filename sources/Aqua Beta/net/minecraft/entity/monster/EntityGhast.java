package net.minecraft.entity.monster;

import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityGhast
extends EntityFlying
implements IMob {
    private int explosionStrength = 1;

    public EntityGhast(World worldIn) {
        super(worldIn);
        this.setSize(4.0f, 4.0f);
        this.isImmuneToFire = true;
        this.experienceValue = 5;
        this.moveHelper = new GhastMoveHelper(this);
        this.tasks.addTask(5, (EntityAIBase)new AIRandomFly(this));
        this.tasks.addTask(7, (EntityAIBase)new AILookAround(this));
        this.tasks.addTask(7, (EntityAIBase)new AIFireballAttack(this));
        this.targetTasks.addTask(1, (EntityAIBase)new EntityAIFindEntityNearestPlayer((EntityLiving)this));
    }

    public boolean isAttacking() {
        return this.dataWatcher.getWatchableObjectByte(16) != 0;
    }

    public void setAttacking(boolean attacking) {
        this.dataWatcher.updateObject(16, (Object)((byte)(attacking ? 1 : 0)));
    }

    public int getFireballStrength() {
        return this.explosionStrength;
    }

    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if ("fireball".equals((Object)source.getDamageType()) && source.getEntity() instanceof EntityPlayer) {
            super.attackEntityFrom(source, 1000.0f);
            ((EntityPlayer)source.getEntity()).triggerAchievement((StatBase)AchievementList.ghast);
            return true;
        }
        return super.attackEntityFrom(source, amount);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (Object)0);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(100.0);
    }

    protected String getLivingSound() {
        return "mob.ghast.moan";
    }

    protected String getHurtSound() {
        return "mob.ghast.scream";
    }

    protected String getDeathSound() {
        return "mob.ghast.death";
    }

    protected Item getDropItem() {
        return Items.gunpowder;
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
        int i = this.rand.nextInt(2) + this.rand.nextInt(1 + lootingModifier);
        for (int j = 0; j < i; ++j) {
            this.dropItem(Items.ghast_tear, 1);
        }
        i = this.rand.nextInt(3) + this.rand.nextInt(1 + lootingModifier);
        for (int k = 0; k < i; ++k) {
            this.dropItem(Items.gunpowder, 1);
        }
    }

    protected float getSoundVolume() {
        return 10.0f;
    }

    public boolean getCanSpawnHere() {
        return this.rand.nextInt(20) == 0 && super.getCanSpawnHere() && this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    public int getMaxSpawnedInChunk() {
        return 1;
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("ExplosionPower", this.explosionStrength);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("ExplosionPower", 99)) {
            this.explosionStrength = tagCompund.getInteger("ExplosionPower");
        }
    }

    public float getEyeHeight() {
        return 2.6f;
    }
}
