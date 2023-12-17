// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import java.util.List;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import javax.annotation.Nullable;
import net.minecraft.pathfinding.PathNavigate;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLiving;

public class EntityAIFollow extends EntityAIBase
{
    private final EntityLiving entity;
    private final Predicate<EntityLiving> followPredicate;
    private EntityLiving followingEntity;
    private final double speedModifier;
    private final PathNavigate navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private float oldWaterCost;
    private final float areaSize;
    
    public EntityAIFollow(final EntityLiving p_i47417_1_, final double p_i47417_2_, final float p_i47417_4_, final float p_i47417_5_) {
        this.entity = p_i47417_1_;
        this.followPredicate = (Predicate<EntityLiving>)new Predicate<EntityLiving>() {
            public boolean apply(@Nullable final EntityLiving p_apply_1_) {
                return p_apply_1_ != null && p_i47417_1_.getClass() != p_apply_1_.getClass();
            }
        };
        this.speedModifier = p_i47417_2_;
        this.navigation = p_i47417_1_.getNavigator();
        this.stopDistance = p_i47417_4_;
        this.areaSize = p_i47417_5_;
        this.setMutexBits(3);
        if (!(p_i47417_1_.getNavigator() instanceof PathNavigateGround) && !(p_i47417_1_.getNavigator() instanceof PathNavigateFlying)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
        }
    }
    
    @Override
    public boolean shouldExecute() {
        final List<EntityLiving> list = this.entity.world.getEntitiesWithinAABB((Class<? extends EntityLiving>)EntityLiving.class, this.entity.getEntityBoundingBox().grow(this.areaSize), (com.google.common.base.Predicate<? super EntityLiving>)this.followPredicate);
        if (!list.isEmpty()) {
            for (final EntityLiving entityliving : list) {
                if (!entityliving.isInvisible()) {
                    this.followingEntity = entityliving;
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return this.followingEntity != null && !this.navigation.noPath() && this.entity.getDistanceSq(this.followingEntity) > this.stopDistance * this.stopDistance;
    }
    
    @Override
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.entity.getPathPriority(PathNodeType.WATER);
        this.entity.setPathPriority(PathNodeType.WATER, 0.0f);
    }
    
    @Override
    public void resetTask() {
        this.followingEntity = null;
        this.navigation.clearPath();
        this.entity.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }
    
    @Override
    public void updateTask() {
        if (this.followingEntity != null && !this.entity.getLeashed()) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.followingEntity, 10.0f, (float)this.entity.getVerticalFaceSpeed());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                final double d0 = this.entity.posX - this.followingEntity.posX;
                final double d2 = this.entity.posY - this.followingEntity.posY;
                final double d3 = this.entity.posZ - this.followingEntity.posZ;
                final double d4 = d0 * d0 + d2 * d2 + d3 * d3;
                if (d4 > this.stopDistance * this.stopDistance) {
                    this.navigation.tryMoveToEntityLiving(this.followingEntity, this.speedModifier);
                }
                else {
                    this.navigation.clearPath();
                    final EntityLookHelper entitylookhelper = this.followingEntity.getLookHelper();
                    if (d4 <= this.stopDistance || (entitylookhelper.getLookPosX() == this.entity.posX && entitylookhelper.getLookPosY() == this.entity.posY && entitylookhelper.getLookPosZ() == this.entity.posZ)) {
                        final double d5 = this.followingEntity.posX - this.entity.posX;
                        final double d6 = this.followingEntity.posZ - this.entity.posZ;
                        this.navigation.tryMoveToXYZ(this.entity.posX - d5, this.entity.posY, this.entity.posZ - d6, this.speedModifier);
                    }
                }
            }
        }
    }
}
