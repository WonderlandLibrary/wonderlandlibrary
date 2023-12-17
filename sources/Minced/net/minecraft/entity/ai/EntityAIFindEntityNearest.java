// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import org.apache.logging.log4j.LogManager;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import net.minecraft.entity.Entity;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLiving;
import org.apache.logging.log4j.Logger;

public class EntityAIFindEntityNearest extends EntityAIBase
{
    private static final Logger LOGGER;
    private final EntityLiving mob;
    private final Predicate<EntityLivingBase> predicate;
    private final EntityAINearestAttackableTarget.Sorter sorter;
    private EntityLivingBase target;
    private final Class<? extends EntityLivingBase> classToCheck;
    
    public EntityAIFindEntityNearest(final EntityLiving mobIn, final Class<? extends EntityLivingBase> p_i45884_2_) {
        this.mob = mobIn;
        this.classToCheck = p_i45884_2_;
        if (mobIn instanceof EntityCreature) {
            EntityAIFindEntityNearest.LOGGER.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }
        this.predicate = (Predicate<EntityLivingBase>)new Predicate<EntityLivingBase>() {
            public boolean apply(@Nullable final EntityLivingBase p_apply_1_) {
                double d0 = EntityAIFindEntityNearest.this.getFollowRange();
                if (p_apply_1_.isSneaking()) {
                    d0 *= 0.800000011920929;
                }
                return !p_apply_1_.isInvisible() && p_apply_1_.getDistance(EntityAIFindEntityNearest.this.mob) <= d0 && EntityAITarget.isSuitableTarget(EntityAIFindEntityNearest.this.mob, p_apply_1_, false, true);
            }
        };
        this.sorter = new EntityAINearestAttackableTarget.Sorter(mobIn);
    }
    
    @Override
    public boolean shouldExecute() {
        final double d0 = this.getFollowRange();
        final List<EntityLivingBase> list = this.mob.world.getEntitiesWithinAABB(this.classToCheck, this.mob.getEntityBoundingBox().grow(d0, 4.0, d0), (com.google.common.base.Predicate<? super EntityLivingBase>)this.predicate);
        Collections.sort(list, this.sorter);
        if (list.isEmpty()) {
            return false;
        }
        this.target = list.get(0);
        return true;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        final EntityLivingBase entitylivingbase = this.mob.getAttackTarget();
        if (entitylivingbase == null) {
            return false;
        }
        if (!entitylivingbase.isEntityAlive()) {
            return false;
        }
        final double d0 = this.getFollowRange();
        return this.mob.getDistanceSq(entitylivingbase) <= d0 * d0 && (!(entitylivingbase instanceof EntityPlayerMP) || !((EntityPlayerMP)entitylivingbase).interactionManager.isCreative());
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
        final IAttributeInstance iattributeinstance = this.mob.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return (iattributeinstance == null) ? 16.0 : iattributeinstance.getAttributeValue();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
