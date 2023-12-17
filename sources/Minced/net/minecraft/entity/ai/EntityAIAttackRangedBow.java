// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.item.ItemBow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.init.Items;

public class EntityAIAttackRangedBow<T extends EntityMob> extends EntityAIBase
{
    private final T entity;
    private final double moveSpeedAmp;
    private int attackCooldown;
    private final float maxAttackDistance;
    private int attackTime;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime;
    
    public EntityAIAttackRangedBow(final T mob, final double moveSpeedAmpIn, final int attackCooldownIn, final float maxAttackDistanceIn) {
        this.attackTime = -1;
        this.strafingTime = -1;
        this.entity = mob;
        this.moveSpeedAmp = moveSpeedAmpIn;
        this.attackCooldown = attackCooldownIn;
        this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
        this.setMutexBits(3);
    }
    
    public void setAttackCooldown(final int p_189428_1_) {
        this.attackCooldown = p_189428_1_;
    }
    
    @Override
    public boolean shouldExecute() {
        return ((EntityLiving)this.entity).getAttackTarget() != null && this.isBowInMainhand();
    }
    
    protected boolean isBowInMainhand() {
        return !((EntityLivingBase)this.entity).getHeldItemMainhand().isEmpty() && ((EntityLivingBase)this.entity).getHeldItemMainhand().getItem() == Items.BOW;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return (this.shouldExecute() || !((EntityLiving)this.entity).getNavigator().noPath()) && this.isBowInMainhand();
    }
    
    @Override
    public void startExecuting() {
        super.startExecuting();
        ((IRangedAttackMob)this.entity).setSwingingArms(true);
    }
    
    @Override
    public void resetTask() {
        super.resetTask();
        ((IRangedAttackMob)this.entity).setSwingingArms(false);
        this.seeTime = 0;
        this.attackTime = -1;
        ((EntityLivingBase)this.entity).resetActiveHand();
    }
    
    @Override
    public void updateTask() {
        final EntityLivingBase entitylivingbase = ((EntityLiving)this.entity).getAttackTarget();
        if (entitylivingbase != null) {
            final double d0 = ((Entity)this.entity).getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
            final boolean flag = ((EntityLiving)this.entity).getEntitySenses().canSee(entitylivingbase);
            final boolean flag2 = this.seeTime > 0;
            if (flag != flag2) {
                this.seeTime = 0;
            }
            if (flag) {
                ++this.seeTime;
            }
            else {
                --this.seeTime;
            }
            if (d0 <= this.maxAttackDistance && this.seeTime >= 20) {
                ((EntityLiving)this.entity).getNavigator().clearPath();
                ++this.strafingTime;
            }
            else {
                ((EntityLiving)this.entity).getNavigator().tryMoveToEntityLiving(entitylivingbase, this.moveSpeedAmp);
                this.strafingTime = -1;
            }
            if (this.strafingTime >= 20) {
                if (((EntityLivingBase)this.entity).getRNG().nextFloat() < 0.3) {
                    this.strafingClockwise = !this.strafingClockwise;
                }
                if (((EntityLivingBase)this.entity).getRNG().nextFloat() < 0.3) {
                    this.strafingBackwards = !this.strafingBackwards;
                }
                this.strafingTime = 0;
            }
            if (this.strafingTime > -1) {
                if (d0 > this.maxAttackDistance * 0.75f) {
                    this.strafingBackwards = false;
                }
                else if (d0 < this.maxAttackDistance * 0.25f) {
                    this.strafingBackwards = true;
                }
                ((EntityLiving)this.entity).getMoveHelper().strafe(this.strafingBackwards ? -0.5f : 0.5f, this.strafingClockwise ? 0.5f : -0.5f);
                ((EntityLiving)this.entity).faceEntity(entitylivingbase, 30.0f, 30.0f);
            }
            else {
                ((EntityLiving)this.entity).getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0f, 30.0f);
            }
            if (((EntityLivingBase)this.entity).isHandActive()) {
                if (!flag && this.seeTime < -60) {
                    ((EntityLivingBase)this.entity).resetActiveHand();
                }
                else if (flag) {
                    final int i = ((EntityLivingBase)this.entity).getItemInUseMaxCount();
                    if (i >= 20) {
                        ((EntityLivingBase)this.entity).resetActiveHand();
                        ((IRangedAttackMob)this.entity).attackEntityWithRangedAttack(entitylivingbase, ItemBow.getArrowVelocity(i));
                        this.attackTime = this.attackCooldown;
                    }
                }
            }
            else {
                final int attackTime = this.attackTime - 1;
                this.attackTime = attackTime;
                if (attackTime <= 0 && this.seeTime >= -60) {
                    ((EntityLivingBase)this.entity).setActiveHand(EnumHand.MAIN_HAND);
                }
            }
        }
    }
}
