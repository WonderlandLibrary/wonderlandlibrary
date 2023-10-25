/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
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
    public float wingRotDelta = 1.0f;
    public float field_70888_h;
    public float destPos;
    public float field_70884_g;
    public int timeUntilNextEgg;
    public boolean chickenJockey;

    @Override
    protected boolean canDespawn() {
        return this.isChickenJockey() && this.riddenByEntity == null;
    }

    @Override
    public EntityChicken createChild(EntityAgeable entityAgeable) {
        return new EntityChicken(this.worldObj);
    }

    @Override
    public void fall(float f, float f2) {
    }

    @Override
    protected int getExperiencePoints(EntityPlayer entityPlayer) {
        return this.isChickenJockey() ? 10 : super.getExperiencePoints(entityPlayer);
    }

    @Override
    protected String getDeathSound() {
        return "mob.chicken.hurt";
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setBoolean("IsChickenJockey", this.chickenJockey);
        nBTTagCompound.setInteger("EggLayTime", this.timeUntilNextEgg);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25);
    }

    @Override
    protected String getHurtSound() {
        return "mob.chicken.hurt";
    }

    @Override
    public float getEyeHeight() {
        return this.height;
    }

    @Override
    public boolean isBreedingItem(ItemStack itemStack) {
        return itemStack != null && itemStack.getItem() == Items.wheat_seeds;
    }

    @Override
    protected Item getDropItem() {
        return Items.feather;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.field_70888_h = this.wingRotation;
        this.field_70884_g = this.destPos;
        this.destPos = (float)((double)this.destPos + (double)(this.onGround ? -1 : 4) * 0.3);
        this.destPos = MathHelper.clamp_float(this.destPos, 0.0f, 1.0f);
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

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.chickenJockey = nBTTagCompound.getBoolean("IsChickenJockey");
        if (nBTTagCompound.hasKey("EggLayTime")) {
            this.timeUntilNextEgg = nBTTagCompound.getInteger("EggLayTime");
        }
    }

    @Override
    protected String getLivingSound() {
        return "mob.chicken.say";
    }

    @Override
    protected void playStepSound(BlockPos blockPos, Block block) {
        this.playSound("mob.chicken.step", 0.15f, 1.0f);
    }

    @Override
    protected void dropFewItems(boolean bl, int n) {
        int n2 = this.rand.nextInt(3) + this.rand.nextInt(1 + n);
        int n3 = 0;
        while (n3 < n2) {
            this.dropItem(Items.feather, 1);
            ++n3;
        }
        if (this.isBurning()) {
            this.dropItem(Items.cooked_chicken, 1);
        } else {
            this.dropItem(Items.chicken, 1);
        }
    }

    @Override
    public void updateRiderPosition() {
        super.updateRiderPosition();
        float f = MathHelper.sin(this.renderYawOffset * (float)Math.PI / 180.0f);
        float f2 = MathHelper.cos(this.renderYawOffset * (float)Math.PI / 180.0f);
        float f3 = 0.1f;
        float f4 = 0.0f;
        this.riddenByEntity.setPosition(this.posX + (double)(f3 * f), this.posY + (double)(this.height * 0.5f) + this.riddenByEntity.getYOffset() + (double)f4, this.posZ - (double)(f3 * f2));
        if (this.riddenByEntity instanceof EntityLivingBase) {
            ((EntityLivingBase)this.riddenByEntity).renderYawOffset = this.renderYawOffset;
        }
    }

    public EntityChicken(World world) {
        super(world);
        this.setSize(0.4f, 0.7f);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.4));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0));
        this.tasks.addTask(3, new EntityAITempt(this, 1.0, Items.wheat_seeds, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }

    public boolean isChickenJockey() {
        return this.chickenJockey;
    }

    public void setChickenJockey(boolean bl) {
        this.chickenJockey = bl;
    }
}

