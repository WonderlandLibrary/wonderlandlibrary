// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import org.apache.logging.log4j.LogManager;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.scoreboard.Team;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import net.minecraft.entity.player.EntityPlayer;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLiving;
import org.apache.logging.log4j.Logger;

public class EntityAIFindEntityNearestPlayer extends EntityAIBase
{
    private static final Logger LOGGER;
    private final EntityLiving entityLiving;
    private final Predicate<Entity> predicate;
    private final EntityAINearestAttackableTarget.Sorter sorter;
    private EntityLivingBase entityTarget;
    
    public EntityAIFindEntityNearestPlayer(final EntityLiving entityLivingIn) {
        this.entityLiving = entityLivingIn;
        if (entityLivingIn instanceof EntityCreature) {
            EntityAIFindEntityNearestPlayer.LOGGER.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }
        this.predicate = (Predicate<Entity>)new Predicate<Entity>() {
            public boolean apply(@Nullable final Entity p_apply_1_) {
                if (!(p_apply_1_ instanceof EntityPlayer)) {
                    return false;
                }
                if (((EntityPlayer)p_apply_1_).capabilities.disableDamage) {
                    return false;
                }
                double d0 = EntityAIFindEntityNearestPlayer.this.maxTargetRange();
                if (p_apply_1_.isSneaking()) {
                    d0 *= 0.800000011920929;
                }
                if (p_apply_1_.isInvisible()) {
                    float f = ((EntityPlayer)p_apply_1_).getArmorVisibility();
                    if (f < 0.1f) {
                        f = 0.1f;
                    }
                    d0 *= 0.7f * f;
                }
                return p_apply_1_.getDistance(EntityAIFindEntityNearestPlayer.this.entityLiving) <= d0 && EntityAITarget.isSuitableTarget(EntityAIFindEntityNearestPlayer.this.entityLiving, (EntityLivingBase)p_apply_1_, false, true);
            }
        };
        this.sorter = new EntityAINearestAttackableTarget.Sorter(entityLivingIn);
    }
    
    @Override
    public boolean shouldExecute() {
        final double d0 = this.maxTargetRange();
        final List<EntityPlayer> list = this.entityLiving.world.getEntitiesWithinAABB((Class<? extends EntityPlayer>)EntityPlayer.class, this.entityLiving.getEntityBoundingBox().grow(d0, 4.0, d0), (com.google.common.base.Predicate<? super EntityPlayer>)this.predicate);
        Collections.sort(list, this.sorter);
        if (list.isEmpty()) {
            return false;
        }
        this.entityTarget = list.get(0);
        return true;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        final EntityLivingBase entitylivingbase = this.entityLiving.getAttackTarget();
        if (entitylivingbase == null) {
            return false;
        }
        if (!entitylivingbase.isEntityAlive()) {
            return false;
        }
        if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).capabilities.disableDamage) {
            return false;
        }
        final Team team = this.entityLiving.getTeam();
        final Team team2 = entitylivingbase.getTeam();
        if (team != null && team2 == team) {
            return false;
        }
        final double d0 = this.maxTargetRange();
        return this.entityLiving.getDistanceSq(entitylivingbase) <= d0 * d0 && (!(entitylivingbase instanceof EntityPlayerMP) || !((EntityPlayerMP)entitylivingbase).interactionManager.isCreative());
    }
    
    @Override
    public void startExecuting() {
        this.entityLiving.setAttackTarget(this.entityTarget);
        super.startExecuting();
    }
    
    @Override
    public void resetTask() {
        this.entityLiving.setAttackTarget(null);
        super.startExecuting();
    }
    
    protected double maxTargetRange() {
        final IAttributeInstance iattributeinstance = this.entityLiving.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return (iattributeinstance == null) ? 16.0 : iattributeinstance.getAttributeValue();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
