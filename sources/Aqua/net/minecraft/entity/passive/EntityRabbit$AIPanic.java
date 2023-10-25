package net.minecraft.entity.passive;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.passive.EntityRabbit;

static class EntityRabbit.AIPanic
extends EntityAIPanic {
    private EntityRabbit theEntity;

    public EntityRabbit.AIPanic(EntityRabbit rabbit, double speedIn) {
        super((EntityCreature)rabbit, speedIn);
        this.theEntity = rabbit;
    }

    public void updateTask() {
        super.updateTask();
        this.theEntity.setMovementSpeed(this.speed);
    }
}
