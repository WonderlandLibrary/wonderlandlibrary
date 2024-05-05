package net.minecraft.entity.monster;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntitySlime;

static class EntitySlime.SlimeMoveHelper
extends EntityMoveHelper {
    private float field_179922_g;
    private int field_179924_h;
    private EntitySlime slime;
    private boolean field_179923_j;

    public EntitySlime.SlimeMoveHelper(EntitySlime slimeIn) {
        super((EntityLiving)slimeIn);
        this.slime = slimeIn;
    }

    public void func_179920_a(float p_179920_1_, boolean p_179920_2_) {
        this.field_179922_g = p_179920_1_;
        this.field_179923_j = p_179920_2_;
    }

    public void setSpeed(double speedIn) {
        this.speed = speedIn;
        this.update = true;
    }

    public void onUpdateMoveHelper() {
        this.entity.rotationYawHead = this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, this.field_179922_g, 30.0f);
        this.entity.renderYawOffset = this.entity.rotationYaw;
        if (!this.update) {
            this.entity.setMoveForward(0.0f);
        } else {
            this.update = false;
            if (this.entity.onGround) {
                this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
                if (this.field_179924_h-- <= 0) {
                    this.field_179924_h = this.slime.getJumpDelay();
                    if (this.field_179923_j) {
                        this.field_179924_h /= 3;
                    }
                    this.slime.getJumpHelper().setJumping();
                    if (this.slime.makesSoundOnJump()) {
                        this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), ((this.slime.getRNG().nextFloat() - this.slime.getRNG().nextFloat()) * 0.2f + 1.0f) * 0.8f);
                    }
                } else {
                    this.slime.moveForward = 0.0f;
                    this.slime.moveStrafing = 0.0f;
                    this.entity.setAIMoveSpeed(0.0f);
                }
            } else {
                this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
            }
        }
    }
}
