package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.Vec3;

public class EntityAIAvoidEntity<T extends Entity>
extends EntityAIBase {
    private final Predicate<Entity> canBeSeenSelector = new /* Unavailable Anonymous Inner Class!! */;
    protected EntityCreature theEntity;
    private double farSpeed;
    private double nearSpeed;
    protected T closestLivingEntity;
    private float avoidDistance;
    private PathEntity entityPathEntity;
    private PathNavigate entityPathNavigate;
    private Class<T> classToAvoid;
    private Predicate<? super T> avoidTargetSelector;

    public EntityAIAvoidEntity(EntityCreature theEntityIn, Class<T> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        this(theEntityIn, classToAvoidIn, Predicates.alwaysTrue(), avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    public EntityAIAvoidEntity(EntityCreature theEntityIn, Class<T> classToAvoidIn, Predicate<? super T> avoidTargetSelectorIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        this.theEntity = theEntityIn;
        this.classToAvoid = classToAvoidIn;
        this.avoidTargetSelector = avoidTargetSelectorIn;
        this.avoidDistance = avoidDistanceIn;
        this.farSpeed = farSpeedIn;
        this.nearSpeed = nearSpeedIn;
        this.entityPathNavigate = theEntityIn.getNavigator();
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        List list = this.theEntity.worldObj.getEntitiesWithinAABB(this.classToAvoid, this.theEntity.getEntityBoundingBox().expand((double)this.avoidDistance, 3.0, (double)this.avoidDistance), Predicates.and((Predicate[])new Predicate[]{EntitySelectors.NOT_SPECTATING, this.canBeSeenSelector, this.avoidTargetSelector}));
        if (list.isEmpty()) {
            return false;
        }
        this.closestLivingEntity = (Entity)list.get(0);
        Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom((EntityCreature)this.theEntity, (int)16, (int)7, (Vec3)new Vec3(((Entity)this.closestLivingEntity).posX, ((Entity)this.closestLivingEntity).posY, ((Entity)this.closestLivingEntity).posZ));
        if (vec3 == null) {
            return false;
        }
        if (this.closestLivingEntity.getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord) < this.closestLivingEntity.getDistanceSqToEntity((Entity)this.theEntity)) {
            return false;
        }
        this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord);
        return this.entityPathEntity == null ? false : this.entityPathEntity.isDestinationSame(vec3);
    }

    public boolean continueExecuting() {
        return !this.entityPathNavigate.noPath();
    }

    public void startExecuting() {
        this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
    }

    public void resetTask() {
        this.closestLivingEntity = null;
    }

    public void updateTask() {
        if (this.theEntity.getDistanceSqToEntity(this.closestLivingEntity) < 49.0) {
            this.theEntity.getNavigator().setSpeed(this.nearSpeed);
        } else {
            this.theEntity.getNavigator().setSpeed(this.farSpeed);
        }
    }
}
