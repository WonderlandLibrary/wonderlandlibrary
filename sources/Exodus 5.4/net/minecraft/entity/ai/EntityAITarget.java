/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.StringUtils;

public abstract class EntityAITarget
extends EntityAIBase {
    private int targetUnseenTicks;
    protected final EntityCreature taskOwner;
    private int targetSearchStatus;
    private int targetSearchDelay;
    private boolean nearbyOnly;
    protected boolean shouldCheckSight;

    private boolean canEasilyReach(EntityLivingBase entityLivingBase) {
        int n;
        this.targetSearchDelay = 10 + this.taskOwner.getRNG().nextInt(5);
        PathEntity pathEntity = this.taskOwner.getNavigator().getPathToEntityLiving(entityLivingBase);
        if (pathEntity == null) {
            return false;
        }
        PathPoint pathPoint = pathEntity.getFinalPathPoint();
        if (pathPoint == null) {
            return false;
        }
        int n2 = pathPoint.xCoord - MathHelper.floor_double(entityLivingBase.posX);
        return (double)(n2 * n2 + (n = pathPoint.zCoord - MathHelper.floor_double(entityLivingBase.posZ)) * n) <= 2.25;
    }

    protected double getTargetDistance() {
        IAttributeInstance iAttributeInstance = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.followRange);
        return iAttributeInstance == null ? 16.0 : iAttributeInstance.getAttributeValue();
    }

    public EntityAITarget(EntityCreature entityCreature, boolean bl) {
        this(entityCreature, bl, false);
    }

    @Override
    public boolean continueExecuting() {
        EntityLivingBase entityLivingBase = this.taskOwner.getAttackTarget();
        if (entityLivingBase == null) {
            return false;
        }
        if (!entityLivingBase.isEntityAlive()) {
            return false;
        }
        Team team = this.taskOwner.getTeam();
        Team team2 = entityLivingBase.getTeam();
        if (team != null && team2 == team) {
            return false;
        }
        double d = this.getTargetDistance();
        if (this.taskOwner.getDistanceSqToEntity(entityLivingBase) > d * d) {
            return false;
        }
        if (this.shouldCheckSight) {
            if (this.taskOwner.getEntitySenses().canSee(entityLivingBase)) {
                this.targetUnseenTicks = 0;
            } else if (++this.targetUnseenTicks > 60) {
                return false;
            }
        }
        return !(entityLivingBase instanceof EntityPlayer) || !((EntityPlayer)entityLivingBase).capabilities.disableDamage;
    }

    public static boolean isSuitableTarget(EntityLiving entityLiving, EntityLivingBase entityLivingBase, boolean bl, boolean bl2) {
        if (entityLivingBase == null) {
            return false;
        }
        if (entityLivingBase == entityLiving) {
            return false;
        }
        if (!entityLivingBase.isEntityAlive()) {
            return false;
        }
        if (!entityLiving.canAttackClass(entityLivingBase.getClass())) {
            return false;
        }
        Team team = entityLiving.getTeam();
        Team team2 = entityLivingBase.getTeam();
        if (team != null && team2 == team) {
            return false;
        }
        if (entityLiving instanceof IEntityOwnable && StringUtils.isNotEmpty((CharSequence)((IEntityOwnable)((Object)entityLiving)).getOwnerId())) {
            if (entityLivingBase instanceof IEntityOwnable && ((IEntityOwnable)((Object)entityLiving)).getOwnerId().equals(((IEntityOwnable)((Object)entityLivingBase)).getOwnerId())) {
                return false;
            }
            if (entityLivingBase == ((IEntityOwnable)((Object)entityLiving)).getOwner()) {
                return false;
            }
        } else if (entityLivingBase instanceof EntityPlayer && !bl && ((EntityPlayer)entityLivingBase).capabilities.disableDamage) {
            return false;
        }
        return !bl2 || entityLiving.getEntitySenses().canSee(entityLivingBase);
    }

    @Override
    public void startExecuting() {
        this.targetSearchStatus = 0;
        this.targetSearchDelay = 0;
        this.targetUnseenTicks = 0;
    }

    public EntityAITarget(EntityCreature entityCreature, boolean bl, boolean bl2) {
        this.taskOwner = entityCreature;
        this.shouldCheckSight = bl;
        this.nearbyOnly = bl2;
    }

    @Override
    public void resetTask() {
        this.taskOwner.setAttackTarget(null);
    }

    protected boolean isSuitableTarget(EntityLivingBase entityLivingBase, boolean bl) {
        if (!EntityAITarget.isSuitableTarget(this.taskOwner, entityLivingBase, bl, this.shouldCheckSight)) {
            return false;
        }
        if (!this.taskOwner.isWithinHomeDistanceFromPosition(new BlockPos(entityLivingBase))) {
            return false;
        }
        if (this.nearbyOnly) {
            if (--this.targetSearchDelay <= 0) {
                this.targetSearchStatus = 0;
            }
            if (this.targetSearchStatus == 0) {
                int n = this.targetSearchStatus = this.canEasilyReach(entityLivingBase) ? 1 : 2;
            }
            if (this.targetSearchStatus == 2) {
                return false;
            }
        }
        return true;
    }
}

