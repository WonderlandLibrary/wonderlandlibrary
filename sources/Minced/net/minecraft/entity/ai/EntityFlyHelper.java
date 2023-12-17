// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.EntityLiving;

public class EntityFlyHelper extends EntityMoveHelper
{
    public EntityFlyHelper(final EntityLiving p_i47418_1_) {
        super(p_i47418_1_);
    }
    
    @Override
    public void onUpdateMoveHelper() {
        if (this.action == Action.MOVE_TO) {
            this.action = Action.WAIT;
            this.entity.setNoGravity(true);
            final double d0 = this.posX - this.entity.posX;
            final double d2 = this.posY - this.entity.posY;
            final double d3 = this.posZ - this.entity.posZ;
            final double d4 = d0 * d0 + d2 * d2 + d3 * d3;
            if (d4 < 2.500000277905201E-7) {
                this.entity.setMoveVertical(0.0f);
                this.entity.setMoveForward(0.0f);
                return;
            }
            final float f = (float)(MathHelper.atan2(d3, d0) * 57.29577951308232) - 90.0f;
            this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f, 10.0f);
            float f2;
            if (this.entity.onGround) {
                f2 = (float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
            }
            else {
                f2 = (float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).getAttributeValue());
            }
            this.entity.setAIMoveSpeed(f2);
            final double d5 = MathHelper.sqrt(d0 * d0 + d3 * d3);
            final float f3 = (float)(-(MathHelper.atan2(d2, d5) * 57.29577951308232));
            this.entity.rotationPitch = this.limitAngle(this.entity.rotationPitch, f3, 10.0f);
            this.entity.setMoveVertical((d2 > 0.0) ? f2 : (-f2));
        }
        else {
            this.entity.setNoGravity(false);
            this.entity.setMoveVertical(0.0f);
            this.entity.setMoveForward(0.0f);
        }
    }
}
