/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 */
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
    private final Predicate<Entity> canBeSeenSelector = new Predicate<Entity>(){

        public boolean apply(Entity entity) {
            return entity.isEntityAlive() && EntityAIAvoidEntity.this.theEntity.getEntitySenses().canSee(entity);
        }
    };
    private float avoidDistance;
    private Class<T> field_181064_i;
    protected EntityCreature theEntity;
    private double nearSpeed;
    private Predicate<? super T> avoidTargetSelector;
    private PathNavigate entityPathNavigate;
    private PathEntity entityPathEntity;
    protected T closestLivingEntity;
    private double farSpeed;

    @Override
    public void resetTask() {
        this.closestLivingEntity = null;
    }

    @Override
    public void updateTask() {
        if (this.theEntity.getDistanceSqToEntity((Entity)this.closestLivingEntity) < 49.0) {
            this.theEntity.getNavigator().setSpeed(this.nearSpeed);
        } else {
            this.theEntity.getNavigator().setSpeed(this.farSpeed);
        }
    }

    @Override
    public boolean continueExecuting() {
        return !this.entityPathNavigate.noPath();
    }

    @Override
    public boolean shouldExecute() {
        List<T> list = this.theEntity.worldObj.getEntitiesWithinAABB(this.field_181064_i, this.theEntity.getEntityBoundingBox().expand(this.avoidDistance, 3.0, this.avoidDistance), Predicates.and((Predicate[])new Predicate[]{EntitySelectors.NOT_SPECTATING, this.canBeSeenSelector, this.avoidTargetSelector}));
        if (list.isEmpty()) {
            return false;
        }
        this.closestLivingEntity = (Entity)list.get(0);
        Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.theEntity, 16, 7, new Vec3(((Entity)this.closestLivingEntity).posX, ((Entity)this.closestLivingEntity).posY, ((Entity)this.closestLivingEntity).posZ));
        if (vec3 == null) {
            return false;
        }
        if (((Entity)this.closestLivingEntity).getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord) < ((Entity)this.closestLivingEntity).getDistanceSqToEntity(this.theEntity)) {
            return false;
        }
        this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord);
        return this.entityPathEntity == null ? false : this.entityPathEntity.isDestinationSame(vec3);
    }

    @Override
    public void startExecuting() {
        this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
    }

    public EntityAIAvoidEntity(EntityCreature entityCreature, Class<T> clazz, Predicate<? super T> predicate, float f, double d, double d2) {
        this.theEntity = entityCreature;
        this.field_181064_i = clazz;
        this.avoidTargetSelector = predicate;
        this.avoidDistance = f;
        this.farSpeed = d;
        this.nearSpeed = d2;
        this.entityPathNavigate = entityCreature.getNavigator();
        this.setMutexBits(1);
    }

    public EntityAIAvoidEntity(EntityCreature entityCreature, Class<T> clazz, float f, double d, double d2) {
        this(entityCreature, clazz, Predicates.alwaysTrue(), f, d, d2);
    }
}

