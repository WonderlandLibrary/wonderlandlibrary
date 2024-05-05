package net.minecraft.entity.passive;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.passive.EntityRabbit;

static class EntityRabbit.RabbitMoveHelper
extends EntityMoveHelper {
    private EntityRabbit theEntity;

    public EntityRabbit.RabbitMoveHelper(EntityRabbit rabbit) {
        super((EntityLiving)rabbit);
        this.theEntity = rabbit;
    }

    public void onUpdateMoveHelper() {
        if (this.theEntity.onGround && !this.theEntity.func_175523_cj()) {
            this.theEntity.setMovementSpeed(0.0);
        }
        super.onUpdateMoveHelper();
    }
}
