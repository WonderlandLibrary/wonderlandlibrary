/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public abstract class EntityAITarget
extends EntityAIBase {
    protected final EntityCreature taskOwner;
    protected boolean shouldCheckSight;
    private final boolean nearbyOnly;
    private int targetSearchStatus;
    private int targetSearchDelay;
    private int targetUnseenTicks;
    protected EntityLivingBase target;
    protected int unseenMemoryTicks = 60;

    public EntityAITarget(EntityCreature creature, boolean checkSight) {
        this(creature, checkSight, false);
    }

    public EntityAITarget(EntityCreature creature, boolean checkSight, boolean onlyNearby) {
        this.taskOwner = creature;
        this.shouldCheckSight = checkSight;
        this.nearbyOnly = onlyNearby;
    }

    @Override
    public boolean continueExecuting() {
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
        Team team = this.taskOwner.getTeam();
        Team team1 = entitylivingbase.getTeam();
        if (team != null && team1 == team) {
            return false;
        }
        double d0 = this.getTargetDistance();
        if (this.taskOwner.getDistanceSqToEntity(entitylivingbase) > d0 * d0) {
            return false;
        }
        if (this.shouldCheckSight) {
            if (this.taskOwner.getEntitySenses().canSee(entitylivingbase)) {
                this.targetUnseenTicks = 0;
            } else if (++this.targetUnseenTicks > this.unseenMemoryTicks) {
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
        IAttributeInstance iattributeinstance = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0 : iattributeinstance.getAttributeValue();
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

    public static boolean isSuitableTarget(EntityLiving attacker, @Nullable EntityLivingBase target, boolean includeInvincibles, boolean checkSight) {
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
        if (attacker instanceof IEntityOwnable && ((IEntityOwnable)((Object)attacker)).getOwnerId() != null) {
            if (target instanceof IEntityOwnable && ((IEntityOwnable)((Object)attacker)).getOwnerId().equals(((IEntityOwnable)((Object)target)).getOwnerId())) {
                return false;
            }
            if (target == ((IEntityOwnable)((Object)attacker)).getOwner()) {
                return false;
            }
        } else if (target instanceof EntityPlayer && !includeInvincibles && ((EntityPlayer)target).capabilities.disableDamage) {
            return false;
        }
        return !checkSight || attacker.getEntitySenses().canSee(target);
    }

    protected boolean isSuitableTarget(@Nullable EntityLivingBase target, boolean includeInvincibles) {
        if (!EntityAITarget.isSuitableTarget(this.taskOwner, target, includeInvincibles, this.shouldCheckSight)) {
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
                int n = this.targetSearchStatus = this.canEasilyReach(target) ? 1 : 2;
            }
            if (this.targetSearchStatus == 2) {
                return false;
            }
        }
        return true;
    }

    private boolean canEasilyReach(EntityLivingBase target) {
        int j;
        this.targetSearchDelay = 10 + this.taskOwner.getRNG().nextInt(5);
        Path path = this.taskOwner.getNavigator().getPathToEntityLiving(target);
        if (path == null) {
            return false;
        }
        PathPoint pathpoint = path.getFinalPathPoint();
        if (pathpoint == null) {
            return false;
        }
        int i = pathpoint.xCoord - MathHelper.floor(target.posX);
        return (double)(i * i + (j = pathpoint.zCoord - MathHelper.floor(target.posZ)) * j) <= 2.25;
    }

    public EntityAITarget func_190882_b(int p_190882_1_) {
        this.unseenMemoryTicks = p_190882_1_;
        return this;
    }
}

