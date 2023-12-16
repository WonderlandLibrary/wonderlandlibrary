// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.util.Vec3;
import net.minecraft.util.EntitySelectors;
import com.google.common.base.Predicates;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.entity.EntityCreature;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;

public class EntityAIAvoidEntity<T extends Entity> extends EntityAIBase
{
    private final Predicate<Entity> canBeSeenSelector;
    protected EntityCreature theEntity;
    private double farSpeed;
    private double nearSpeed;
    protected T closestLivingEntity;
    private float avoidDistance;
    private PathEntity entityPathEntity;
    private PathNavigate entityPathNavigate;
    private Class<T> classToAvoid;
    private Predicate<? super T> avoidTargetSelector;
    
    public EntityAIAvoidEntity(final EntityCreature theEntityIn, final Class<T> classToAvoidIn, final float avoidDistanceIn, final double farSpeedIn, final double nearSpeedIn) {
        this(theEntityIn, (Class)classToAvoidIn, Predicates.alwaysTrue(), avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }
    
    public EntityAIAvoidEntity(final EntityCreature theEntityIn, final Class<T> classToAvoidIn, final Predicate<? super T> avoidTargetSelectorIn, final float avoidDistanceIn, final double farSpeedIn, final double nearSpeedIn) {
        this.canBeSeenSelector = (Predicate<Entity>)new Predicate<Entity>() {
            public boolean apply(final Entity p_apply_1_) {
                return p_apply_1_.isEntityAlive() && EntityAIAvoidEntity.this.theEntity.getEntitySenses().canSee(p_apply_1_);
            }
        };
        this.theEntity = theEntityIn;
        this.classToAvoid = classToAvoidIn;
        this.avoidTargetSelector = avoidTargetSelectorIn;
        this.avoidDistance = avoidDistanceIn;
        this.farSpeed = farSpeedIn;
        this.nearSpeed = nearSpeedIn;
        this.entityPathNavigate = theEntityIn.getNavigator();
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        final List<T> list = this.theEntity.worldObj.getEntitiesWithinAABB((Class<? extends T>)this.classToAvoid, this.theEntity.getEntityBoundingBox().expand(this.avoidDistance, 3.0, this.avoidDistance), (com.google.common.base.Predicate<? super T>)Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING, this.canBeSeenSelector, this.avoidTargetSelector }));
        if (list.isEmpty()) {
            return false;
        }
        this.closestLivingEntity = list.get(0);
        final Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.theEntity, 16, 7, new Vec3(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));
        if (vec3 == null) {
            return false;
        }
        if (this.closestLivingEntity.getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord) < this.closestLivingEntity.getDistanceSqToEntity(this.theEntity)) {
            return false;
        }
        this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord);
        return this.entityPathEntity != null && this.entityPathEntity.isDestinationSame(vec3);
    }
    
    @Override
    public boolean continueExecuting() {
        return !this.entityPathNavigate.noPath();
    }
    
    @Override
    public void startExecuting() {
        this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
    }
    
    @Override
    public void resetTask() {
        this.closestLivingEntity = null;
    }
    
    @Override
    public void updateTask() {
        if (this.theEntity.getDistanceSqToEntity(this.closestLivingEntity) < 49.0) {
            this.theEntity.getNavigator().setSpeed(this.nearSpeed);
        }
        else {
            this.theEntity.getNavigator().setSpeed(this.farSpeed);
        }
    }
}
