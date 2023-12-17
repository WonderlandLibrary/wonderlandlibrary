// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.EntityLiving;

public class EntityAIAttackRanged extends EntityAIBase
{
    private final EntityLiving entityHost;
    private final IRangedAttackMob rangedAttackEntityHost;
    private EntityLivingBase attackTarget;
    private int rangedAttackTime;
    private final double entityMoveSpeed;
    private int seeTime;
    private final int attackIntervalMin;
    private final int maxRangedAttackTime;
    private final float attackRadius;
    private final float maxAttackDistance;
    
    public EntityAIAttackRanged(final IRangedAttackMob attacker, final double movespeed, final int maxAttackTime, final float maxAttackDistanceIn) {
        this(attacker, movespeed, maxAttackTime, maxAttackTime, maxAttackDistanceIn);
    }
    
    public EntityAIAttackRanged(final IRangedAttackMob attacker, final double movespeed, final int p_i1650_4_, final int maxAttackTime, final float maxAttackDistanceIn) {
        this.rangedAttackTime = -1;
        if (!(attacker instanceof EntityLivingBase)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        }
        this.rangedAttackEntityHost = attacker;
        this.entityHost = (EntityLiving)attacker;
        this.entityMoveSpeed = movespeed;
        this.attackIntervalMin = p_i1650_4_;
        this.maxRangedAttackTime = maxAttackTime;
        this.attackRadius = maxAttackDistanceIn;
        this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
        this.setMutexBits(3);
    }
    
    @Override
    public boolean shouldExecute() {
        final EntityLivingBase entitylivingbase = this.entityHost.getAttackTarget();
        if (entitylivingbase == null) {
            return false;
        }
        this.attackTarget = entitylivingbase;
        return true;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
    }
    
    @Override
    public void resetTask() {
        this.attackTarget = null;
        this.seeTime = 0;
        this.rangedAttackTime = -1;
    }
    
    @Override
    public void updateTask() {
        final double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
        final boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);
        if (flag) {
            ++this.seeTime;
        }
        else {
            this.seeTime = 0;
        }
        if (d0 <= this.maxAttackDistance && this.seeTime >= 20) {
            this.entityHost.getNavigator().clearPath();
        }
        else {
            this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
        }
        this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0f, 30.0f);
        final int rangedAttackTime = this.rangedAttackTime - 1;
        this.rangedAttackTime = rangedAttackTime;
        if (rangedAttackTime == 0) {
            if (!flag) {
                return;
            }
            final float f = MathHelper.sqrt(d0) / this.attackRadius;
            final float lvt_5_1_ = MathHelper.clamp(f, 0.1f, 1.0f);
            this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, lvt_5_1_);
            this.rangedAttackTime = MathHelper.floor(f * (this.maxRangedAttackTime - this.attackIntervalMin) + this.attackIntervalMin);
        }
        else if (this.rangedAttackTime < 0) {
            final float f2 = MathHelper.sqrt(d0) / this.attackRadius;
            this.rangedAttackTime = MathHelper.floor(f2 * (this.maxRangedAttackTime - this.attackIntervalMin) + this.attackIntervalMin);
        }
    }
}
