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
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLiving;
import org.apache.logging.log4j.Logger;

public class EntityAIFindEntityNearest extends EntityAIBase
{
    private static final Logger LOGGER;
    private EntityLiving mob;
    private final Predicate<EntityLivingBase> field_179443_c;
    private final EntityAINearestAttackableTarget.Sorter field_179440_d;
    private EntityLivingBase target;
    private Class<? extends EntityLivingBase> field_179439_f;
    
    public EntityAIFindEntityNearest(final EntityLiving mobIn, final Class<? extends EntityLivingBase> p_i45884_2_) {
        this.mob = mobIn;
        this.field_179439_f = p_i45884_2_;
        if (mobIn instanceof EntityCreature) {
            EntityAIFindEntityNearest.LOGGER.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }
        this.field_179443_c = (Predicate<EntityLivingBase>)new Predicate<EntityLivingBase>() {
            public boolean apply(final EntityLivingBase p_apply_1_) {
                double d0 = EntityAIFindEntityNearest.this.getFollowRange();
                if (p_apply_1_.isSneaking()) {
                    d0 *= 0.800000011920929;
                }
                return !p_apply_1_.isInvisible() && p_apply_1_.getDistanceToEntity(EntityAIFindEntityNearest.this.mob) <= d0 && EntityAITarget.isSuitableTarget(EntityAIFindEntityNearest.this.mob, p_apply_1_, false, true);
            }
        };
        this.field_179440_d = new EntityAINearestAttackableTarget.Sorter(mobIn);
    }
    
    @Override
    public boolean shouldExecute() {
        final double d0 = this.getFollowRange();
        final List<EntityLivingBase> list = this.mob.worldObj.getEntitiesWithinAABB(this.field_179439_f, this.mob.getEntityBoundingBox().expand(d0, 4.0, d0), (com.google.common.base.Predicate<? super EntityLivingBase>)this.field_179443_c);
        Collections.sort(list, this.field_179440_d);
        if (list.isEmpty()) {
            return false;
        }
        this.target = list.get(0);
        return true;
    }
    
    @Override
    public boolean continueExecuting() {
        final EntityLivingBase entitylivingbase = this.mob.getAttackTarget();
        if (entitylivingbase == null) {
            return false;
        }
        if (!entitylivingbase.isEntityAlive()) {
            return false;
        }
        final double d0 = this.getFollowRange();
        return this.mob.getDistanceSqToEntity(entitylivingbase) <= d0 * d0 && (!(entitylivingbase instanceof EntityPlayerMP) || !((EntityPlayerMP)entitylivingbase).theItemInWorldManager.isCreative());
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
        final IAttributeInstance iattributeinstance = this.mob.getEntityAttribute(SharedMonsterAttributes.followRange);
        return (iattributeinstance == null) ? 16.0 : iattributeinstance.getAttributeValue();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
