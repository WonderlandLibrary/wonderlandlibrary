/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAIFindEntityNearest
extends EntityAIBase {
    private static final Logger field_179444_a = LogManager.getLogger();
    private EntityLiving field_179442_b;
    private final Predicate field_179443_c;
    private final EntityAINearestAttackableTarget.Sorter field_179440_d;
    private EntityLivingBase field_179441_e;
    private Class field_179439_f;
    private static final String __OBFID = "CL_00002250";

    public EntityAIFindEntityNearest(EntityLiving p_i45884_1_, Class p_i45884_2_) {
        this.field_179442_b = p_i45884_1_;
        this.field_179439_f = p_i45884_2_;
        if (p_i45884_1_ instanceof EntityCreature) {
            field_179444_a.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }
        this.field_179443_c = new Predicate(){
            private static final String __OBFID = "CL_00002249";

            public boolean func_179876_a(EntityLivingBase p_179876_1_) {
                double var2 = EntityAIFindEntityNearest.this.func_179438_f();
                if (p_179876_1_.isSneaking()) {
                    var2 *= (double)0.8f;
                }
                return p_179876_1_.isInvisible() ? false : ((double)p_179876_1_.getDistanceToEntity(EntityAIFindEntityNearest.this.field_179442_b) > var2 ? false : EntityAITarget.func_179445_a(EntityAIFindEntityNearest.this.field_179442_b, p_179876_1_, false, true));
            }

            public boolean apply(Object p_apply_1_) {
                return this.func_179876_a((EntityLivingBase)p_apply_1_);
            }
        };
        this.field_179440_d = new EntityAINearestAttackableTarget.Sorter(p_i45884_1_);
    }

    @Override
    public boolean shouldExecute() {
        double var1 = this.func_179438_f();
        List var3 = this.field_179442_b.worldObj.func_175647_a(this.field_179439_f, this.field_179442_b.getEntityBoundingBox().expand(var1, 4.0, var1), this.field_179443_c);
        Collections.sort(var3, this.field_179440_d);
        if (var3.isEmpty()) {
            return false;
        }
        this.field_179441_e = (EntityLivingBase)var3.get(0);
        return true;
    }

    @Override
    public boolean continueExecuting() {
        EntityLivingBase var1 = this.field_179442_b.getAttackTarget();
        if (var1 == null) {
            return false;
        }
        if (!var1.isEntityAlive()) {
            return false;
        }
        double var2 = this.func_179438_f();
        return this.field_179442_b.getDistanceSqToEntity(var1) > var2 * var2 ? false : !(var1 instanceof EntityPlayerMP) || !((EntityPlayerMP)var1).theItemInWorldManager.isCreative();
    }

    @Override
    public void startExecuting() {
        this.field_179442_b.setAttackTarget(this.field_179441_e);
        super.startExecuting();
    }

    @Override
    public void resetTask() {
        this.field_179442_b.setAttackTarget(null);
        super.startExecuting();
    }

    protected double func_179438_f() {
        IAttributeInstance var1 = this.field_179442_b.getEntityAttribute(SharedMonsterAttributes.followRange);
        return var1 == null ? 16.0 : var1.getAttributeValue();
    }
}

