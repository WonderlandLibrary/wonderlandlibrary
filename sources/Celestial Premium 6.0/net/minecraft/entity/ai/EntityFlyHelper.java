/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.MathHelper;

public class EntityFlyHelper
extends EntityMoveHelper {
    public EntityFlyHelper(EntityLiving p_i47418_1_) {
        super(p_i47418_1_);
    }

    @Override
    public void onUpdateMoveHelper() {
        if (this.action == EntityMoveHelper.Action.MOVE_TO) {
            this.action = EntityMoveHelper.Action.WAIT;
            this.entity.setNoGravity(true);
            double d0 = this.posX - this.entity.posX;
            double d1 = this.posY - this.entity.posY;
            double d2 = this.posZ - this.entity.posZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d3 < 2.500000277905201E-7) {
                this.entity.setMoveForward(0.0f);
                this.entity.func_191989_p(0.0f);
                return;
            }
            float f = (float)(MathHelper.atan2(d2, d0) * 57.29577951308232) - 90.0f;
            this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f, 10.0f);
            float f1 = this.entity.onGround ? (float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()) : (float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.field_193334_e).getAttributeValue());
            this.entity.setAIMoveSpeed(f1);
            double d4 = MathHelper.sqrt(d0 * d0 + d2 * d2);
            float f2 = (float)(-(MathHelper.atan2(d1, d4) * 57.29577951308232));
            this.entity.rotationPitch = this.limitAngle(this.entity.rotationPitch, f2, 10.0f);
            this.entity.setMoveForward(d1 > 0.0 ? f1 : -f1);
        } else {
            this.entity.setNoGravity(false);
            this.entity.setMoveForward(0.0f);
            this.entity.func_191989_p(0.0f);
        }
    }
}

