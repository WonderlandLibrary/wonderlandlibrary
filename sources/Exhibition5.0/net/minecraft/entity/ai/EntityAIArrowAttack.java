// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.EntityLiving;

public class EntityAIArrowAttack extends EntityAIBase
{
    private final EntityLiving entityHost;
    private final IRangedAttackMob rangedAttackEntityHost;
    private EntityLivingBase attackTarget;
    private int rangedAttackTime;
    private double entityMoveSpeed;
    private int field_75318_f;
    private int field_96561_g;
    private int maxRangedAttackTime;
    private float field_96562_i;
    private float maxAttackDistance;
    private static final String __OBFID = "CL_00001609";
    
    public EntityAIArrowAttack(final IRangedAttackMob p_i1649_1_, final double p_i1649_2_, final int p_i1649_4_, final float p_i1649_5_) {
        this(p_i1649_1_, p_i1649_2_, p_i1649_4_, p_i1649_4_, p_i1649_5_);
    }
    
    public EntityAIArrowAttack(final IRangedAttackMob p_i1650_1_, final double p_i1650_2_, final int p_i1650_4_, final int p_i1650_5_, final float p_i1650_6_) {
        this.rangedAttackTime = -1;
        if (!(p_i1650_1_ instanceof EntityLivingBase)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        }
        this.rangedAttackEntityHost = p_i1650_1_;
        this.entityHost = (EntityLiving)p_i1650_1_;
        this.entityMoveSpeed = p_i1650_2_;
        this.field_96561_g = p_i1650_4_;
        this.maxRangedAttackTime = p_i1650_5_;
        this.field_96562_i = p_i1650_6_;
        this.maxAttackDistance = p_i1650_6_ * p_i1650_6_;
        this.setMutexBits(3);
    }
    
    @Override
    public boolean shouldExecute() {
        final EntityLivingBase var1 = this.entityHost.getAttackTarget();
        if (var1 == null) {
            return false;
        }
        this.attackTarget = var1;
        return true;
    }
    
    @Override
    public boolean continueExecuting() {
        return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
    }
    
    @Override
    public void resetTask() {
        this.attackTarget = null;
        this.field_75318_f = 0;
        this.rangedAttackTime = -1;
    }
    
    @Override
    public void updateTask() {
        final double var1 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
        final boolean var2 = this.entityHost.getEntitySenses().canSee(this.attackTarget);
        if (var2) {
            ++this.field_75318_f;
        }
        else {
            this.field_75318_f = 0;
        }
        if (var1 <= this.maxAttackDistance && this.field_75318_f >= 20) {
            this.entityHost.getNavigator().clearPathEntity();
        }
        else {
            this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
        }
        this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0f, 30.0f);
        final int rangedAttackTime = this.rangedAttackTime - 1;
        this.rangedAttackTime = rangedAttackTime;
        if (rangedAttackTime == 0) {
            if (var1 > this.maxAttackDistance || !var2) {
                return;
            }
            final float var3 = MathHelper.sqrt_double(var1) / this.field_96562_i;
            final float var4 = MathHelper.clamp_float(var3, 0.1f, 1.0f);
            this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, var4);
            this.rangedAttackTime = MathHelper.floor_float(var3 * (this.maxRangedAttackTime - this.field_96561_g) + this.field_96561_g);
        }
        else if (this.rangedAttackTime < 0) {
            final float var3 = MathHelper.sqrt_double(var1) / this.field_96562_i;
            this.rangedAttackTime = MathHelper.floor_float(var3 * (this.maxRangedAttackTime - this.field_96561_g) + this.field_96561_g);
        }
    }
}
