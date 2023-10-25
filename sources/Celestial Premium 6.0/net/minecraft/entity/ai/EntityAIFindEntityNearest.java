/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
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
    private static final Logger LOGGER = LogManager.getLogger();
    private final EntityLiving mob;
    private final Predicate<EntityLivingBase> predicate;
    private final EntityAINearestAttackableTarget.Sorter sorter;
    private EntityLivingBase target;
    private final Class<? extends EntityLivingBase> classToCheck;

    public EntityAIFindEntityNearest(EntityLiving mobIn, Class<? extends EntityLivingBase> p_i45884_2_) {
        this.mob = mobIn;
        this.classToCheck = p_i45884_2_;
        if (mobIn instanceof EntityCreature) {
            LOGGER.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }
        this.predicate = new Predicate<EntityLivingBase>(){

            @Override
            public boolean apply(@Nullable EntityLivingBase p_apply_1_) {
                double d0 = EntityAIFindEntityNearest.this.getFollowRange();
                if (p_apply_1_.isSneaking()) {
                    d0 *= (double)0.8f;
                }
                if (p_apply_1_.isInvisible()) {
                    return false;
                }
                return (double)p_apply_1_.getDistanceToEntity(EntityAIFindEntityNearest.this.mob) > d0 ? false : EntityAITarget.isSuitableTarget(EntityAIFindEntityNearest.this.mob, p_apply_1_, false, true);
            }
        };
        this.sorter = new EntityAINearestAttackableTarget.Sorter(mobIn);
    }

    @Override
    public boolean shouldExecute() {
        double d0 = this.getFollowRange();
        List<EntityLivingBase> list = this.mob.world.getEntitiesWithinAABB(this.classToCheck, this.mob.getEntityBoundingBox().expand(d0, 4.0, d0), this.predicate);
        Collections.sort(list, this.sorter);
        if (list.isEmpty()) {
            return false;
        }
        this.target = list.get(0);
        return true;
    }

    @Override
    public boolean continueExecuting() {
        EntityLivingBase entitylivingbase = this.mob.getAttackTarget();
        if (entitylivingbase == null) {
            return false;
        }
        if (!entitylivingbase.isEntityAlive()) {
            return false;
        }
        double d0 = this.getFollowRange();
        if (this.mob.getDistanceSqToEntity(entitylivingbase) > d0 * d0) {
            return false;
        }
        return !(entitylivingbase instanceof EntityPlayerMP) || !((EntityPlayerMP)entitylivingbase).interactionManager.isCreative();
    }

    @Override
    public void startExecuting() {
        this.mob.setAttackTarget(this.target);
        super.startExecuting();
    }

    @Override
    public void resetTask() {
        this.mob.setAttackTarget(null);
        super.startExecuting();
    }

    protected double getFollowRange() {
        IAttributeInstance iattributeinstance = this.mob.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0 : iattributeinstance.getAttributeValue();
    }
}

