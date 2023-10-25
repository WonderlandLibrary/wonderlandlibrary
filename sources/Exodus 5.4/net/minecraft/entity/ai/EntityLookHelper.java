/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class EntityLookHelper {
    private boolean isLooking;
    private double posY;
    private double posX;
    private EntityLiving entity;
    private double posZ;
    private float deltaLookYaw;
    private float deltaLookPitch;

    public boolean getIsLooking() {
        return this.isLooking;
    }

    public static float updateRotation(float f, float f2, float f3) {
        float f4 = MathHelper.wrapAngleTo180_float(f2 - f);
        if (f4 > f3) {
            f4 = f3;
        }
        if (f4 < -f3) {
            f4 = -f3;
        }
        return f + f4;
    }

    public double getLookPosZ() {
        return this.posZ;
    }

    public void setLookPosition(double d, double d2, double d3, float f, float f2) {
        this.posX = d;
        this.posY = d2;
        this.posZ = d3;
        this.deltaLookYaw = f;
        this.deltaLookPitch = f2;
        this.isLooking = true;
    }

    public void onUpdateLook() {
        this.entity.rotationPitch = 0.0f;
        if (this.isLooking) {
            this.isLooking = false;
            double d = this.posX - this.entity.posX;
            double d2 = this.posY - (this.entity.posY + (double)this.entity.getEyeHeight());
            double d3 = this.posZ - this.entity.posZ;
            double d4 = MathHelper.sqrt_double(d * d + d3 * d3);
            float f = (float)(MathHelper.func_181159_b(d3, d) * 180.0 / Math.PI) - 90.0f;
            float f2 = (float)(-(MathHelper.func_181159_b(d2, d4) * 180.0 / Math.PI));
            this.entity.rotationPitch = EntityLookHelper.updateRotation(this.entity.rotationPitch, f2, this.deltaLookPitch);
            this.entity.rotationYawHead = EntityLookHelper.updateRotation(this.entity.rotationYawHead, f, this.deltaLookYaw);
        } else {
            this.entity.rotationYawHead = EntityLookHelper.updateRotation(this.entity.rotationYawHead, this.entity.renderYawOffset, 10.0f);
        }
        float f = MathHelper.wrapAngleTo180_float(this.entity.rotationYawHead - this.entity.renderYawOffset);
        if (!this.entity.getNavigator().noPath()) {
            if (f < -75.0f) {
                this.entity.rotationYawHead = this.entity.renderYawOffset - 75.0f;
            }
            if (f > 75.0f) {
                this.entity.rotationYawHead = this.entity.renderYawOffset + 75.0f;
            }
        }
    }

    public EntityLookHelper(EntityLiving entityLiving) {
        this.entity = entityLiving;
    }

    public double getLookPosX() {
        return this.posX;
    }

    public double getLookPosY() {
        return this.posY;
    }

    public void setLookPositionWithEntity(Entity entity, float f, float f2) {
        this.posX = entity.posX;
        this.posY = entity instanceof EntityLivingBase ? entity.posY + (double)entity.getEyeHeight() : (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0;
        this.posZ = entity.posZ;
        this.deltaLookYaw = f;
        this.deltaLookPitch = f2;
        this.isLooking = true;
    }
}

