/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAIFindEntityNearestPlayer
extends EntityAIBase {
    private static final Logger LOGGER = LogManager.getLogger();
    private final EntityLiving entityLiving;
    private final Predicate<Entity> predicate;
    private final EntityAINearestAttackableTarget.Sorter sorter;
    private EntityLivingBase entityTarget;

    public EntityAIFindEntityNearestPlayer(EntityLiving entityLivingIn) {
        this.entityLiving = entityLivingIn;
        if (entityLivingIn instanceof EntityCreature) {
            LOGGER.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }
        this.predicate = new Predicate<Entity>(){

            @Override
            public boolean apply(@Nullable Entity p_apply_1_) {
                if (!(p_apply_1_ instanceof EntityPlayer)) {
                    return false;
                }
                if (((EntityPlayer)p_apply_1_).capabilities.disableDamage) {
                    return false;
                }
                double d0 = EntityAIFindEntityNearestPlayer.this.maxTargetRange();
                if (p_apply_1_.isSneaking()) {
                    d0 *= (double)0.8f;
                }
                if (p_apply_1_.isInvisible()) {
                    float f = ((EntityPlayer)p_apply_1_).getArmorVisibility();
                    if (f < 0.1f) {
                        f = 0.1f;
                    }
                    d0 *= (double)(0.7f * f);
                }
                return (double)p_apply_1_.getDistanceToEntity(EntityAIFindEntityNearestPlayer.this.entityLiving) > d0 ? false : EntityAITarget.isSuitableTarget(EntityAIFindEntityNearestPlayer.this.entityLiving, (EntityLivingBase)p_apply_1_, false, true);
            }
        };
        this.sorter = new EntityAINearestAttackableTarget.Sorter(entityLivingIn);
    }

    @Override
    public boolean shouldExecute() {
        double d0 = this.maxTargetRange();
        List<Entity> list = this.entityLiving.world.getEntitiesWithinAABB(EntityPlayer.class, this.entityLiving.getEntityBoundingBox().expand(d0, 4.0, d0), this.predicate);
        Collections.sort(list, this.sorter);
        if (list.isEmpty()) {
            return false;
        }
        this.entityTarget = (EntityLivingBase)list.get(0);
        return true;
    }

    @Override
    public boolean continueExecuting() {
        EntityLivingBase entitylivingbase = this.entityLiving.getAttackTarget();
        if (entitylivingbase == null) {
            return false;
        }
        if (!entitylivingbase.isEntityAlive()) {
            return false;
        }
        if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).capabilities.disableDamage) {
            return false;
        }
        Team team = this.entityLiving.getTeam();
        Team team1 = entitylivingbase.getTeam();
        if (team != null && team1 == team) {
            return false;
        }
        double d0 = this.maxTargetRange();
        if (this.entityLiving.getDistanceSqToEntity(entitylivingbase) > d0 * d0) {
            return false;
        }
        return !(entitylivingbase instanceof EntityPlayerMP) || !((EntityPlayerMP)entitylivingbase).interactionManager.isCreative();
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
        IAttributeInstance iattributeinstance = this.entityLiving.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0 : iattributeinstance.getAttributeValue();
    }
}

