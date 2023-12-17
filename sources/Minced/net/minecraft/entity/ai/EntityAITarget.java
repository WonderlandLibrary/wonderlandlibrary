// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.IEntityOwnable;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.scoreboard.Team;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityCreature;

public abstract class EntityAITarget extends EntityAIBase
{
    protected final EntityCreature taskOwner;
    protected boolean shouldCheckSight;
    private final boolean nearbyOnly;
    private int targetSearchStatus;
    private int targetSearchDelay;
    private int targetUnseenTicks;
    protected EntityLivingBase target;
    protected int unseenMemoryTicks;
    
    public EntityAITarget(final EntityCreature creature, final boolean checkSight) {
        this(creature, checkSight, false);
    }
    
    public EntityAITarget(final EntityCreature creature, final boolean checkSight, final boolean onlyNearby) {
        this.unseenMemoryTicks = 60;
        this.taskOwner = creature;
        this.shouldCheckSight = checkSight;
        this.nearbyOnly = onlyNearby;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        EntityLivingBase entitylivingbase = this.taskOwner.getAttackTarget();
        if (entitylivingbase == null) {
            entitylivingbase = this.target;
        }
        if (entitylivingbase == null) {
            return false;
        }
        if (!entitylivingbase.isEntityAlive()) {
            return false;
        }
        final Team team = this.taskOwner.getTeam();
        final Team team2 = entitylivingbase.getTeam();
        if (team != null && team2 == team) {
            return false;
        }
        final double d0 = this.getTargetDistance();
        if (this.taskOwner.getDistanceSq(entitylivingbase) > d0 * d0) {
            return false;
        }
        if (this.shouldCheckSight) {
            if (this.taskOwner.getEntitySenses().canSee(entitylivingbase)) {
                this.targetUnseenTicks = 0;
            }
            else if (++this.targetUnseenTicks > this.unseenMemoryTicks) {
                return false;
            }
        }
        if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).capabilities.disableDamage) {
            return false;
        }
        this.taskOwner.setAttackTarget(entitylivingbase);
        return true;
    }
    
    protected double getTargetDistance() {
        final IAttributeInstance iattributeinstance = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return (iattributeinstance == null) ? 16.0 : iattributeinstance.getAttributeValue();
    }
    
    @Override
    public void startExecuting() {
        this.targetSearchStatus = 0;
        this.targetSearchDelay = 0;
        this.targetUnseenTicks = 0;
    }
    
    @Override
    public void resetTask() {
        this.taskOwner.setAttackTarget(null);
        this.target = null;
    }
    
    public static boolean isSuitableTarget(final EntityLiving attacker, @Nullable final EntityLivingBase target, final boolean includeInvincibles, final boolean checkSight) {
        if (target == null) {
            return false;
        }
        if (target == attacker) {
            return false;
        }
        if (!target.isEntityAlive()) {
            return false;
        }
        if (!attacker.canAttackClass(target.getClass())) {
            return false;
        }
        if (attacker.isOnSameTeam(target)) {
            return false;
        }
        if (attacker instanceof IEntityOwnable && ((IEntityOwnable)attacker).getOwnerId() != null) {
            if (target instanceof IEntityOwnable && ((IEntityOwnable)attacker).getOwnerId().equals(((IEntityOwnable)target).getOwnerId())) {
                return false;
            }
            if (target == ((IEntityOwnable)attacker).getOwner()) {
                return false;
            }
        }
        else if (target instanceof EntityPlayer && !includeInvincibles && ((EntityPlayer)target).capabilities.disableDamage) {
            return false;
        }
        return !checkSight || attacker.getEntitySenses().canSee(target);
    }
    
    protected boolean isSuitableTarget(@Nullable final EntityLivingBase target, final boolean includeInvincibles) {
        if (!isSuitableTarget(this.taskOwner, target, includeInvincibles, this.shouldCheckSight)) {
            return false;
        }
        if (!this.taskOwner.isWithinHomeDistanceFromPosition(new BlockPos(target))) {
            return false;
        }
        if (this.nearbyOnly) {
            if (--this.targetSearchDelay <= 0) {
                this.targetSearchStatus = 0;
            }
            if (this.targetSearchStatus == 0) {
                this.targetSearchStatus = (this.canEasilyReach(target) ? 1 : 2);
            }
            if (this.targetSearchStatus == 2) {
                return false;
            }
        }
        return true;
    }
    
    private boolean canEasilyReach(final EntityLivingBase target) {
        this.targetSearchDelay = 10 + this.taskOwner.getRNG().nextInt(5);
        final Path path = this.taskOwner.getNavigator().getPathToEntityLiving(target);
        if (path == null) {
            return false;
        }
        final PathPoint pathpoint = path.getFinalPathPoint();
        if (pathpoint == null) {
            return false;
        }
        final int i = pathpoint.x - MathHelper.floor(target.posX);
        final int j = pathpoint.z - MathHelper.floor(target.posZ);
        return i * i + j * j <= 2.25;
    }
    
    public EntityAITarget setUnseenMemoryTicks(final int p_190882_1_) {
        this.unseenMemoryTicks = p_190882_1_;
        return this;
    }
}
