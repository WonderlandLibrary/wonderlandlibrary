// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EntitySelectors;
import javax.annotation.Nullable;
import com.google.common.base.Predicates;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.Path;
import net.minecraft.entity.EntityCreature;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;

public class EntityAIAvoidEntity<T extends Entity> extends EntityAIBase
{
    private final Predicate<Entity> canBeSeenSelector;
    protected EntityCreature entity;
    private final double farSpeed;
    private final double nearSpeed;
    protected T closestLivingEntity;
    private final float avoidDistance;
    private Path path;
    private final PathNavigate navigation;
    private final Class<T> classToAvoid;
    private final Predicate<? super T> avoidTargetSelector;
    
    public EntityAIAvoidEntity(final EntityCreature entityIn, final Class<T> classToAvoidIn, final float avoidDistanceIn, final double farSpeedIn, final double nearSpeedIn) {
        this(entityIn, (Class)classToAvoidIn, Predicates.alwaysTrue(), avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }
    
    public EntityAIAvoidEntity(final EntityCreature entityIn, final Class<T> classToAvoidIn, final Predicate<? super T> avoidTargetSelectorIn, final float avoidDistanceIn, final double farSpeedIn, final double nearSpeedIn) {
        this.canBeSeenSelector = (Predicate<Entity>)new Predicate<Entity>() {
            public boolean apply(@Nullable final Entity p_apply_1_) {
                return p_apply_1_.isEntityAlive() && EntityAIAvoidEntity.this.entity.getEntitySenses().canSee(p_apply_1_) && !EntityAIAvoidEntity.this.entity.isOnSameTeam(p_apply_1_);
            }
        };
        this.entity = entityIn;
        this.classToAvoid = classToAvoidIn;
        this.avoidTargetSelector = avoidTargetSelectorIn;
        this.avoidDistance = avoidDistanceIn;
        this.farSpeed = farSpeedIn;
        this.nearSpeed = nearSpeedIn;
        this.navigation = entityIn.getNavigator();
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        final List<T> list = this.entity.world.getEntitiesWithinAABB((Class<? extends T>)this.classToAvoid, this.entity.getEntityBoundingBox().grow(this.avoidDistance, 3.0, this.avoidDistance), (com.google.common.base.Predicate<? super T>)Predicates.and(new Predicate[] { EntitySelectors.CAN_AI_TARGET, this.canBeSeenSelector, this.avoidTargetSelector }));
        if (list.isEmpty()) {
            return false;
        }
        this.closestLivingEntity = list.get(0);
        final Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.entity, 16, 7, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));
        if (vec3d == null) {
            return false;
        }
        if (this.closestLivingEntity.getDistanceSq(vec3d.x, vec3d.y, vec3d.z) < this.closestLivingEntity.getDistanceSq(this.entity)) {
            return false;
        }
        this.path = this.navigation.getPathToXYZ(vec3d.x, vec3d.y, vec3d.z);
        return this.path != null;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return !this.navigation.noPath();
    }
    
    @Override
    public void startExecuting() {
        this.navigation.setPath(this.path, this.farSpeed);
    }
    
    @Override
    public void resetTask() {
        this.closestLivingEntity = null;
    }
    
    @Override
    public void updateTask() {
        if (this.entity.getDistanceSq(this.closestLivingEntity) < 49.0) {
            this.entity.getNavigator().setSpeed(this.nearSpeed);
        }
        else {
            this.entity.getNavigator().setSpeed(this.farSpeed);
        }
    }
}
