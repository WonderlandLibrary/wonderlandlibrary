/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.MathHelper;

public class EntityMoveHelper {
    protected double speed;
    protected double posX;
    protected EntityLiving entity;
    protected double posY;
    protected double posZ;
    protected boolean update;

    public void onUpdateMoveHelper() {
        this.entity.setMoveForward(0.0f);
        if (this.update) {
            double d;
            this.update = false;
            double d2 = this.posX - this.entity.posX;
            int n = MathHelper.floor_double(this.entity.getEntityBoundingBox().minY + 0.5);
            double d3 = this.posY - (double)n;
            double d4 = d2 * d2 + d3 * d3 + (d = this.posZ - this.entity.posZ) * d;
            if (d4 >= 2.500000277905201E-7) {
                float f = (float)(MathHelper.func_181159_b(d, d2) * 180.0 / Math.PI) - 90.0f;
                this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f, 30.0f);
                this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
                if (d3 > 0.0 && d2 * d2 + d * d < 1.0) {
                    this.entity.getJumpHelper().setJumping();
                }
            }
        }
    }

    public double getZ() {
        return this.posZ;
    }

    public double getY() {
        return this.posY;
    }

    public boolean isUpdating() {
        return this.update;
    }

    public void setMoveTo(double d, double d2, double d3, double d4) {
        this.posX = d;
        this.posY = d2;
        this.posZ = d3;
        this.speed = d4;
        this.update = true;
    }

    public EntityMoveHelper(EntityLiving entityLiving) {
        this.entity = entityLiving;
        this.posX = entityLiving.posX;
        this.posY = entityLiving.posY;
        this.posZ = entityLiving.posZ;
    }

    public double getX() {
        return this.posX;
    }

    protected float limitAngle(float f, float f2, float f3) {
        float f4;
        float f5 = MathHelper.wrapAngleTo180_float(f2 - f);
        if (f5 > f3) {
            f5 = f3;
        }
        if (f5 < -f3) {
            f5 = -f3;
        }
        if ((f4 = f + f5) < 0.0f) {
            f4 += 360.0f;
        } else if (f4 > 360.0f) {
            f4 -= 360.0f;
        }
        return f4;
    }

    public double getSpeed() {
        return this.speed;
    }
}

