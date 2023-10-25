/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;

public class EntityAIArrowAttack
extends EntityAIBase {
    private EntityLivingBase attackTarget;
    private double entityMoveSpeed;
    private float maxAttackDistance;
    private int rangedAttackTime = -1;
    private int maxRangedAttackTime;
    private float field_96562_i;
    private int field_75318_f;
    private int field_96561_g;
    private final IRangedAttackMob rangedAttackEntityHost;
    private final EntityLiving entityHost;

    @Override
    public boolean continueExecuting() {
        return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
    }

    public EntityAIArrowAttack(IRangedAttackMob iRangedAttackMob, double d, int n, float f) {
        this(iRangedAttackMob, d, n, n, f);
    }

    @Override
    public void updateTask() {
        double d = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
        boolean bl = this.entityHost.getEntitySenses().canSee(this.attackTarget);
        this.field_75318_f = bl ? ++this.field_75318_f : 0;
        if (d <= (double)this.maxAttackDistance && this.field_75318_f >= 20) {
            this.entityHost.getNavigator().clearPathEntity();
        } else {
            this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
        }
        this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0f, 30.0f);
        if (--this.rangedAttackTime == 0) {
            if (d > (double)this.maxAttackDistance || !bl) {
                return;
            }
            float f = MathHelper.sqrt_double(d) / this.field_96562_i;
            float f2 = MathHelper.clamp_float(f, 0.1f, 1.0f);
            this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, f2);
            this.rangedAttackTime = MathHelper.floor_float(f * (float)(this.maxRangedAttackTime - this.field_96561_g) + (float)this.field_96561_g);
        } else if (this.rangedAttackTime < 0) {
            float f = MathHelper.sqrt_double(d) / this.field_96562_i;
            this.rangedAttackTime = MathHelper.floor_float(f * (float)(this.maxRangedAttackTime - this.field_96561_g) + (float)this.field_96561_g);
        }
    }

    @Override
    public void resetTask() {
        this.attackTarget = null;
        this.field_75318_f = 0;
        this.rangedAttackTime = -1;
    }

    public EntityAIArrowAttack(IRangedAttackMob iRangedAttackMob, double d, int n, int n2, float f) {
        if (!(iRangedAttackMob instanceof EntityLivingBase)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        }
        this.rangedAttackEntityHost = iRangedAttackMob;
        this.entityHost = (EntityLiving)((Object)iRangedAttackMob);
        this.entityMoveSpeed = d;
        this.field_96561_g = n;
        this.maxRangedAttackTime = n2;
        this.field_96562_i = f;
        this.maxAttackDistance = f * f;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase entityLivingBase = this.entityHost.getAttackTarget();
        if (entityLivingBase == null) {
            return false;
        }
        this.attackTarget = entityLivingBase;
        return true;
    }
}

