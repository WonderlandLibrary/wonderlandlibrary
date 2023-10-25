/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;

public class EntityAIFollowParent
extends EntityAIBase {
    EntityAnimal parentAnimal;
    private int delayCounter;
    double moveSpeed;
    EntityAnimal childAnimal;

    @Override
    public void startExecuting() {
        this.delayCounter = 0;
    }

    @Override
    public boolean continueExecuting() {
        if (this.childAnimal.getGrowingAge() >= 0) {
            return false;
        }
        if (!this.parentAnimal.isEntityAlive()) {
            return false;
        }
        double d = this.childAnimal.getDistanceSqToEntity(this.parentAnimal);
        return d >= 9.0 && d <= 256.0;
    }

    @Override
    public void resetTask() {
        this.parentAnimal = null;
    }

    @Override
    public boolean shouldExecute() {
        if (this.childAnimal.getGrowingAge() >= 0) {
            return false;
        }
        List<?> list = this.childAnimal.worldObj.getEntitiesWithinAABB(this.childAnimal.getClass(), this.childAnimal.getEntityBoundingBox().expand(8.0, 4.0, 8.0));
        EntityAnimal entityAnimal = null;
        double d = Double.MAX_VALUE;
        for (EntityAnimal entityAnimal2 : list) {
            double d2;
            if (entityAnimal2.getGrowingAge() < 0 || !((d2 = this.childAnimal.getDistanceSqToEntity(entityAnimal2)) <= d)) continue;
            d = d2;
            entityAnimal = entityAnimal2;
        }
        if (entityAnimal == null) {
            return false;
        }
        if (d < 9.0) {
            return false;
        }
        this.parentAnimal = entityAnimal;
        return true;
    }

    @Override
    public void updateTask() {
        if (--this.delayCounter <= 0) {
            this.delayCounter = 10;
            this.childAnimal.getNavigator().tryMoveToEntityLiving(this.parentAnimal, this.moveSpeed);
        }
    }

    public EntityAIFollowParent(EntityAnimal entityAnimal, double d) {
        this.childAnimal = entityAnimal;
        this.moveSpeed = d;
    }
}

