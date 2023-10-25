/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;

public class EntityAIAttackRangedBow<T extends EntityMob>
extends EntityAIBase {
    private final T entity;
    private final double moveSpeedAmp;
    private int attackCooldown;
    private final float maxAttackDistance;
    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public EntityAIAttackRangedBow(T p_i47515_1_, double p_i47515_2_, int p_i47515_4_, float p_i47515_5_) {
        this.entity = p_i47515_1_;
        this.moveSpeedAmp = p_i47515_2_;
        this.attackCooldown = p_i47515_4_;
        this.maxAttackDistance = p_i47515_5_ * p_i47515_5_;
        this.setMutexBits(3);
    }

    public void setAttackCooldown(int p_189428_1_) {
        this.attackCooldown = p_189428_1_;
    }

    @Override
    public boolean shouldExecute() {
        return ((EntityLiving)this.entity).getAttackTarget() == null ? false : this.isBowInMainhand();
    }

    protected boolean isBowInMainhand() {
        return !((EntityLivingBase)this.entity).getHeldItemMainhand().isEmpty() && ((EntityLivingBase)this.entity).getHeldItemMainhand().getItem() == Items.BOW;
    }

    @Override
    public boolean continueExecuting() {
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
        EntityLivingBase entitylivingbase = ((EntityLiving)this.entity).getAttackTarget();
        if (entitylivingbase != null) {
            boolean flag1;
            double d0 = ((Entity)this.entity).getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
            boolean flag = ((EntityLiving)this.entity).getEntitySenses().canSee(entitylivingbase);
            boolean bl = flag1 = this.seeTime > 0;
            if (flag != flag1) {
                this.seeTime = 0;
            }
            this.seeTime = flag ? ++this.seeTime : --this.seeTime;
            if (d0 <= (double)this.maxAttackDistance && this.seeTime >= 20) {
                ((EntityLiving)this.entity).getNavigator().clearPathEntity();
                ++this.strafingTime;
            } else {
                ((EntityLiving)this.entity).getNavigator().tryMoveToEntityLiving(entitylivingbase, this.moveSpeedAmp);
                this.strafingTime = -1;
            }
            if (this.strafingTime >= 20) {
                if ((double)((EntityLivingBase)this.entity).getRNG().nextFloat() < 0.3) {
                    boolean bl2 = this.strafingClockwise = !this.strafingClockwise;
                }
                if ((double)((EntityLivingBase)this.entity).getRNG().nextFloat() < 0.3) {
                    this.strafingBackwards = !this.strafingBackwards;
                }
                this.strafingTime = 0;
            }
            if (this.strafingTime > -1) {
                if (d0 > (double)(this.maxAttackDistance * 0.75f)) {
                    this.strafingBackwards = false;
                } else if (d0 < (double)(this.maxAttackDistance * 0.25f)) {
                    this.strafingBackwards = true;
                }
                ((EntityLiving)this.entity).getMoveHelper().strafe(this.strafingBackwards ? -0.5f : 0.5f, this.strafingClockwise ? 0.5f : -0.5f);
                ((EntityLiving)this.entity).faceEntity(entitylivingbase, 30.0f, 30.0f);
            } else {
                ((EntityLiving)this.entity).getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0f, 30.0f);
            }
            if (((EntityLivingBase)this.entity).isHandActive()) {
                int i;
                if (!flag && this.seeTime < -60) {
                    ((EntityLivingBase)this.entity).resetActiveHand();
                } else if (flag && (i = ((EntityLivingBase)this.entity).getItemInUseMaxCount()) >= 20) {
                    ((EntityLivingBase)this.entity).resetActiveHand();
                    ((IRangedAttackMob)this.entity).attackEntityWithRangedAttack(entitylivingbase, ItemBow.getArrowVelocity(i));
                    this.attackTime = this.attackCooldown;
                }
            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                ((EntityLivingBase)this.entity).setActiveHand(EnumHand.MAIN_HAND);
            }
        }
    }
}

