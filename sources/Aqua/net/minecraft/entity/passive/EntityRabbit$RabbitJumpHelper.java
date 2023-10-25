package net.minecraft.entity.passive;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.passive.EntityRabbit;

public class EntityRabbit.RabbitJumpHelper
extends EntityJumpHelper {
    private EntityRabbit theEntity;
    private boolean field_180068_d;

    public EntityRabbit.RabbitJumpHelper(EntityRabbit rabbit) {
        super((EntityLiving)rabbit);
        this.field_180068_d = false;
        this.theEntity = rabbit;
    }

    public boolean getIsJumping() {
        return this.isJumping;
    }

    public boolean func_180065_d() {
        return this.field_180068_d;
    }

    public void func_180066_a(boolean p_180066_1_) {
        this.field_180068_d = p_180066_1_;
    }

    public void doJump() {
        if (this.isJumping) {
            this.theEntity.doMovementAction(EntityRabbit.EnumMoveType.STEP);
            this.isJumping = false;
        }
    }
}
