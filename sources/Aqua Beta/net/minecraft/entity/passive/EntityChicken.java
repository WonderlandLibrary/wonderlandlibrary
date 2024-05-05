package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityChicken
extends EntityAnimal {
    public float wingRotation;
    public float destPos;
    public float field_70884_g;
    public float field_70888_h;
    public float wingRotDelta = 1.0f;
    public int timeUntilNextEgg;
    public boolean chickenJockey;

    public EntityChicken(World worldIn) {
        super(worldIn);
        this.setSize(0.4f, 0.7f);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
        this.tasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)this, 1.4));
        this.tasks.addTask(2, (EntityAIBase)new EntityAIMate((EntityAnimal)this, 1.0));
        this.tasks.addTask(3, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.0, Items.wheat_seeds, false));
        this.tasks.addTask(4, (EntityAIBase)new EntityAIFollowParent((EntityAnimal)this, 1.1));
        this.tasks.addTask(5, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0));
        this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(7, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    }

    public float getEyeHeight() {
        return this.height;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25);
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.field_70888_h = this.wingRotation;
        this.field_70884_g = this.destPos;
        this.destPos = (float)((double)this.destPos + (double)(this.onGround ? -1 : 4) * 0.3);
        this.destPos = MathHelper.clamp_float((float)this.destPos, (float)0.0f, (float)1.0f);
        if (!this.onGround && this.wingRotDelta < 1.0f) {
            this.wingRotDelta = 1.0f;
        }
        this.wingRotDelta = (float)((double)this.wingRotDelta * 0.9);
        if (!this.onGround && this.motionY < 0.0) {
            this.motionY *= 0.6;
        }
        this.wingRotation += this.wingRotDelta * 2.0f;
        if (!(this.worldObj.isRemote || this.isChild() || this.isChickenJockey() || --this.timeUntilNextEgg > 0)) {
            this.playSound("mob.chicken.plop", 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            this.dropItem(Items.egg, 1);
            this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        }
    }

    public void fall(float distance, float damageMultiplier) {
    }

    protected String getLivingSound() {
        return "mob.chicken.say";
    }

    protected String getHurtSound() {
        return "mob.chicken.hurt";
    }

    protected String getDeathSound() {
        return "mob.chicken.hurt";
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound("mob.chicken.step", 0.15f, 1.0f);
    }

    protected Item getDropItem() {
        return Items.feather;
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
        int i = this.rand.nextInt(3) + this.rand.nextInt(1 + lootingModifier);
        for (int j = 0; j < i; ++j) {
            this.dropItem(Items.feather, 1);
        }
        if (this.isBurning()) {
            this.dropItem(Items.cooked_chicken, 1);
        } else {
            this.dropItem(Items.chicken, 1);
        }
    }

    public EntityChicken createChild(EntityAgeable ageable) {
        return new EntityChicken(this.worldObj);
    }

    public boolean isBreedingItem(ItemStack stack) {
        return stack != null && stack.getItem() == Items.wheat_seeds;
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.chickenJockey = tagCompund.getBoolean("IsChickenJockey");
        if (tagCompund.hasKey("EggLayTime")) {
            this.timeUntilNextEgg = tagCompund.getInteger("EggLayTime");
        }
    }

    protected int getExperiencePoints(EntityPlayer player) {
        return this.isChickenJockey() ? 10 : super.getExperiencePoints(player);
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("IsChickenJockey", this.chickenJockey);
        tagCompound.setInteger("EggLayTime", this.timeUntilNextEgg);
    }

    protected boolean canDespawn() {
        return this.isChickenJockey() && this.riddenByEntity == null;
    }

    public void updateRiderPosition() {
        super.updateRiderPosition();
        float f = MathHelper.sin((float)(this.renderYawOffset * (float)Math.PI / 180.0f));
        float f1 = MathHelper.cos((float)(this.renderYawOffset * (float)Math.PI / 180.0f));
        float f2 = 0.1f;
        float f3 = 0.0f;
        this.riddenByEntity.setPosition(this.posX + (double)(f2 * f), this.posY + (double)(this.height * 0.5f) + this.riddenByEntity.getYOffset() + (double)f3, this.posZ - (double)(f2 * f1));
        if (this.riddenByEntity instanceof EntityLivingBase) {
            ((EntityLivingBase)this.riddenByEntity).renderYawOffset = this.renderYawOffset;
        }
    }

    public boolean isChickenJockey() {
        return this.chickenJockey;
    }

    public void setChickenJockey(boolean jockey) {
        this.chickenJockey = jockey;
    }
}
