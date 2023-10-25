/*
 * Decompiled with CFR 0.152.
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
    private Class<? extends EntityLivingBase> field_179439_f;
    private EntityLiving field_179442_b;
    private final Predicate<EntityLivingBase> field_179443_c;
    private EntityLivingBase field_179441_e;
    private static final Logger field_179444_a = LogManager.getLogger();
    private final EntityAINearestAttackableTarget.Sorter field_179440_d;

    @Override
    public void startExecuting() {
        this.field_179442_b.setAttackTarget(this.field_179441_e);
        super.startExecuting();
    }

    @Override
    public boolean continueExecuting() {
        EntityLivingBase entityLivingBase = this.field_179442_b.getAttackTarget();
        if (entityLivingBase == null) {
            return false;
        }
        if (!entityLivingBase.isEntityAlive()) {
            return false;
        }
        double d = this.func_179438_f();
        return this.field_179442_b.getDistanceSqToEntity(entityLivingBase) > d * d ? false : !(entityLivingBase instanceof EntityPlayerMP) || !((EntityPlayerMP)entityLivingBase).theItemInWorldManager.isCreative();
    }

    @Override
    public boolean shouldExecute() {
        double d = this.func_179438_f();
        List<EntityLivingBase> list = this.field_179442_b.worldObj.getEntitiesWithinAABB(this.field_179439_f, this.field_179442_b.getEntityBoundingBox().expand(d, 4.0, d), this.field_179443_c);
        Collections.sort(list, this.field_179440_d);
        if (list.isEmpty()) {
            return false;
        }
        this.field_179441_e = list.get(0);
        return true;
    }

    protected double func_179438_f() {
        IAttributeInstance iAttributeInstance = this.field_179442_b.getEntityAttribute(SharedMonsterAttributes.followRange);
        return iAttributeInstance == null ? 16.0 : iAttributeInstance.getAttributeValue();
    }

    public EntityAIFindEntityNearest(EntityLiving entityLiving, Class<? extends EntityLivingBase> clazz) {
        this.field_179442_b = entityLiving;
        this.field_179439_f = clazz;
        if (entityLiving instanceof EntityCreature) {
            field_179444_a.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }
        this.field_179443_c = new Predicate<EntityLivingBase>(){

            public boolean apply(EntityLivingBase entityLivingBase) {
                double d = EntityAIFindEntityNearest.this.func_179438_f();
                if (entityLivingBase.isSneaking()) {
                    d *= (double)0.8f;
                }
                return entityLivingBase.isInvisible() ? false : ((double)entityLivingBase.getDistanceToEntity(EntityAIFindEntityNearest.this.field_179442_b) > d ? false : EntityAITarget.isSuitableTarget(EntityAIFindEntityNearest.this.field_179442_b, entityLivingBase, false, true));
            }
        };
        this.field_179440_d = new EntityAINearestAttackableTarget.Sorter(entityLiving);
    }

    @Override
    public void resetTask() {
        this.field_179442_b.setAttackTarget(null);
        super.startExecuting();
    }
}

