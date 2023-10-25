package net.minecraft.entity.passive;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.passive.EntityRabbit;

static class EntityRabbit.AIAvoidEntity<T extends Entity>
extends EntityAIAvoidEntity<T> {
    private EntityRabbit entityInstance;

    public EntityRabbit.AIAvoidEntity(EntityRabbit rabbit, Class<T> p_i46403_2_, float p_i46403_3_, double p_i46403_4_, double p_i46403_6_) {
        super((EntityCreature)rabbit, p_i46403_2_, p_i46403_3_, p_i46403_4_, p_i46403_6_);
        this.entityInstance = rabbit;
    }

    public void updateTask() {
        super.updateTask();
    }
}
