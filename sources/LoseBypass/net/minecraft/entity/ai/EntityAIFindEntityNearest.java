/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
    private final Predicate<EntityLivingBase> field_179443_c;
    private final EntityAINearestAttackableTarget.Sorter field_179440_d;
    private EntityLivingBase field_179441_e;
    private Class<? extends EntityLivingBase> field_179439_f;

    public EntityAIFindEntityNearest(EntityLiving p_i45884_1_, Class<? extends EntityLivingBase> p_i45884_2_) {
        this.field_179442_b = p_i45884_1_;
        this.field_179439_f = p_i45884_2_;
        if (p_i45884_1_ instanceof EntityCreature) {
            field_179444_a.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }
        this.field_179443_c = new Predicate<EntityLivingBase>(){

            @Override
            public boolean apply(EntityLivingBase p_apply_1_) {
                double d0 = EntityAIFindEntityNearest.this.func_179438_f();
                if (p_apply_1_.isSneaking()) {
                    d0 *= (double)0.8f;
                }
                if (p_apply_1_.isInvisible()) {
                    return false;
                }
                if ((double)p_apply_1_.getDistanceToEntity(EntityAIFindEntityNearest.this.field_179442_b) > d0) {
                    return false;
                }
                boolean bl = EntityAITarget.isSuitableTarget(EntityAIFindEntityNearest.this.field_179442_b, p_apply_1_, false, true);
                return bl;
            }
        };
        this.field_179440_d = new EntityAINearestAttackableTarget.Sorter(p_i45884_1_);
    }

    @Override
    public boolean shouldExecute() {
        double d0 = this.func_179438_f();
        List<EntityLivingBase> list = this.field_179442_b.worldObj.getEntitiesWithinAABB(this.field_179439_f, this.field_179442_b.getEntityBoundingBox().expand(d0, 4.0, d0), this.field_179443_c);
        Collections.sort(list, this.field_179440_d);
        if (list.isEmpty()) {
            return false;
        }
        this.field_179441_e = list.get(0);
        return true;
    }

    @Override
    public boolean continueExecuting() {
        EntityLivingBase entitylivingbase = this.field_179442_b.getAttackTarget();
        if (entitylivingbase == null) {
            return false;
        }
        if (!entitylivingbase.isEntityAlive()) {
            return false;
        }
        double d0 = this.func_179438_f();
        if (this.field_179442_b.getDistanceSqToEntity(entitylivingbase) > d0 * d0) {
            return false;
        }
        if (!(entitylivingbase instanceof EntityPlayerMP)) return true;
        if (!((EntityPlayerMP)entitylivingbase).theItemInWorldManager.isCreative()) return true;
        return false;
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
        IAttributeInstance iattributeinstance = this.field_179442_b.getEntityAttribute(SharedMonsterAttributes.followRange);
        if (iattributeinstance == null) {
            return 16.0;
        }
        double d = iattributeinstance.getAttributeValue();
        return d;
    }
}

