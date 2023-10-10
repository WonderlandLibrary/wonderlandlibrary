/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.MathHelper;

public class EntityMoveHelper {
    protected EntityLiving entity;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected double speed;
    protected boolean update;

    public EntityMoveHelper(EntityLiving entitylivingIn) {
        this.entity = entitylivingIn;
        this.posX = entitylivingIn.posX;
        this.posY = entitylivingIn.posY;
        this.posZ = entitylivingIn.posZ;
    }

    public boolean isUpdating() {
        return this.update;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setMoveTo(double x2, double y2, double z2, double speedIn) {
        this.posX = x2;
        this.posY = y2;
        this.posZ = z2;
        this.speed = speedIn;
        this.update = true;
    }

    public void onUpdateMoveHelper() {
        this.entity.setMoveForward(0.0f);
        if (this.update) {
            double d1;
            this.update = false;
            double d0 = this.posX - this.entity.posX;
            int i2 = MathHelper.floor_double(this.entity.getEntityBoundingBox().minY + 0.5);
            double d2 = this.posY - (double)i2;
            double d3 = d0 * d0 + d2 * d2 + (d1 = this.posZ - this.entity.posZ) * d1;
            if (d3 >= 2.500000277905201E-7) {
                float f2 = (float)(MathHelper.func_181159_b(d1, d0) * 180.0 / Math.PI) - 90.0f;
                this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f2, 30.0f);
                this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
                if (d2 > 0.0 && d0 * d0 + d1 * d1 < 1.0) {
                    this.entity.getJumpHelper().setJumping();
                }
            }
        }
    }

    protected float limitAngle(float p_75639_1_, float p_75639_2_, float p_75639_3_) {
        float f1;
        float f2 = MathHelper.wrapAngleTo180_float(p_75639_2_ - p_75639_1_);
        if (f2 > p_75639_3_) {
            f2 = p_75639_3_;
        }
        if (f2 < -p_75639_3_) {
            f2 = -p_75639_3_;
        }
        if ((f1 = p_75639_1_ + f2) < 0.0f) {
            f1 += 360.0f;
        } else if (f1 > 360.0f) {
            f1 -= 360.0f;
        }
        return f1;
    }

    public double getX() {
        return this.posX;
    }

    public double getY() {
        return this.posY;
    }

    public double getZ() {
        return this.posZ;
    }
}

