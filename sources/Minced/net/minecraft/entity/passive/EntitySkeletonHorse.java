// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;
import net.minecraft.entity.ai.EntityAISkeletonRiders;

public class EntitySkeletonHorse extends AbstractHorse
{
    private final EntityAISkeletonRiders skeletonTrapAI;
    private boolean skeletonTrap;
    private int skeletonTrapTime;
    
    public EntitySkeletonHorse(final World worldIn) {
        super(worldIn);
        this.skeletonTrapAI = new EntityAISkeletonRiders(this);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224);
        this.getEntityAttribute(EntitySkeletonHorse.JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
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
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        super.getHurtSound(damageSourceIn);
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
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_SKELETON_HORSE;
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.isTrap() && this.skeletonTrapTime++ >= 18000) {
            this.setDead();
        }
    }
    
    public static void registerFixesSkeletonHorse(final DataFixer fixer) {
        AbstractHorse.registerFixesAbstractHorse(fixer, EntitySkeletonHorse.class);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("SkeletonTrap", this.isTrap());
        compound.setInteger("SkeletonTrapTime", this.skeletonTrapTime);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setTrap(compound.getBoolean("SkeletonTrap"));
        this.skeletonTrapTime = compound.getInteger("SkeletonTrapTime");
    }
    
    public boolean isTrap() {
        return this.skeletonTrap;
    }
    
    public void setTrap(final boolean trap) {
        if (trap != this.skeletonTrap) {
            this.skeletonTrap = trap;
            if (trap) {
                this.tasks.addTask(1, this.skeletonTrapAI);
            }
            else {
                this.tasks.removeTask(this.skeletonTrapAI);
            }
        }
    }
    
    @Override
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        final boolean flag = !itemstack.isEmpty();
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
