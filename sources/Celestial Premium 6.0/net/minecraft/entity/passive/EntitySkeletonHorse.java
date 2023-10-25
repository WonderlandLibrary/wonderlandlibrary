/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISkeletonRiders;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySkeletonHorse
extends AbstractHorse {
    private final EntityAISkeletonRiders skeletonTrapAI = new EntityAISkeletonRiders(this);
    private boolean skeletonTrap;
    private int skeletonTrapTime;

    public EntitySkeletonHorse(World p_i47295_1_) {
        super(p_i47295_1_);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2f);
        this.getEntityAttribute(JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        return SoundEvents.ENTITY_SKELETON_HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        super.getDeathSound();
        return SoundEvents.ENTITY_SKELETON_HORSE_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        super.getHurtSound(p_184601_1_);
        return SoundEvents.ENTITY_SKELETON_HORSE_HURT;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    public double getMountedYOffset() {
        return super.getMountedYOffset() - 0.1875;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_SKELETON_HORSE;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.func_190690_dh() && this.skeletonTrapTime++ >= 18000) {
            this.setDead();
        }
    }

    public static void func_190692_b(DataFixer p_190692_0_) {
        AbstractHorse.func_190683_c(p_190692_0_, EntitySkeletonHorse.class);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("SkeletonTrap", this.func_190690_dh());
        compound.setInteger("SkeletonTrapTime", this.skeletonTrapTime);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.func_190691_p(compound.getBoolean("SkeletonTrap"));
        this.skeletonTrapTime = compound.getInteger("SkeletonTrapTime");
    }

    public boolean func_190690_dh() {
        return this.skeletonTrap;
    }

    public void func_190691_p(boolean p_190691_1_) {
        if (p_190691_1_ != this.skeletonTrap) {
            this.skeletonTrap = p_190691_1_;
            if (p_190691_1_) {
                this.tasks.addTask(1, this.skeletonTrapAI);
            } else {
                this.tasks.removeTask(this.skeletonTrapAI);
            }
        }
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        boolean flag;
        ItemStack itemstack = player.getHeldItem(hand);
        boolean bl = flag = !itemstack.isEmpty();
        if (flag && itemstack.getItem() == Items.SPAWN_EGG) {
            return super.processInteract(player, hand);
        }
        if (!this.isTame()) {
            return false;
        }
        if (this.isChild()) {
            return super.processInteract(player, hand);
        }
        if (player.isSneaking()) {
            this.openGUI(player);
            return true;
        }
        if (this.isBeingRidden()) {
            return super.processInteract(player, hand);
        }
        if (flag) {
            if (itemstack.getItem() == Items.SADDLE && !this.isHorseSaddled()) {
                this.openGUI(player);
                return true;
            }
            if (itemstack.interactWithEntity(player, this, hand)) {
                return true;
            }
        }
        this.mountTo(player);
        return true;
    }
}

