/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class EntityLookHelper {
    private EntityLiving entity;
    private float deltaLookYaw;
    private float deltaLookPitch;
    private boolean isLooking;
    private double posX;
    private double posY;
    private double posZ;
    private static final String __OBFID = "CL_00001572";

    public EntityLookHelper(EntityLiving p_i1613_1_) {
        this.entity = p_i1613_1_;
    }

    public void setLookPositionWithEntity(Entity p_75651_1_, float p_75651_2_, float p_75651_3_) {
        this.posX = p_75651_1_.posX;
        this.posY = p_75651_1_ instanceof EntityLivingBase ? p_75651_1_.posY + (double)p_75651_1_.getEyeHeight() : (p_75651_1_.getEntityBoundingBox().minY + p_75651_1_.getEntityBoundingBox().maxY) / 2.0;
        this.posZ = p_75651_1_.posZ;
        this.deltaLookYaw = p_75651_2_;
        this.deltaLookPitch = p_75651_3_;
        this.isLooking = true;
    }

    public void setLookPosition(double p_75650_1_, double p_75650_3_, double p_75650_5_, float p_75650_7_, float p_75650_8_) {
        this.posX = p_75650_1_;
        this.posY = p_75650_3_;
        this.posZ = p_75650_5_;
        this.deltaLookYaw = p_75650_7_;
        this.deltaLookPitch = p_75650_8_;
        this.isLooking = true;
    }

    public void onUpdateLook() {
        this.entity.rotationPitch = 0.0f;
        if (this.isLooking) {
            this.isLooking = false;
            double var1 = this.posX - this.entity.posX;
            double var3 = this.posY - (this.entity.posY + (double)this.entity.getEyeHeight());
            double var5 = this.posZ - this.entity.posZ;
            double var7 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
            float var9 = (float)(Math.atan2(var5, var1) * 180.0 / Math.PI) - 90.0f;
            float var10 = (float)(-(Math.atan2(var3, var7) * 180.0 / Math.PI));
            this.entity.rotationPitch = this.updateRotation(this.entity.rotationPitch, var10, this.deltaLookPitch);
            this.entity.rotationYawHead = this.updateRotation(this.entity.rotationYawHead, var9, this.deltaLookYaw);
        } else {
            this.entity.rotationYawHead = this.updateRotation(this.entity.rotationYawHead, this.entity.renderYawOffset, 10.0f);
        }
        float var11 = MathHelper.wrapAngleTo180_float(this.entity.rotationYawHead - this.entity.renderYawOffset);
        if (!this.entity.getNavigator().noPath()) {
            if (var11 < -75.0f) {
                this.entity.rotationYawHead = this.entity.renderYawOffset - 75.0f;
            }
            if (var11 > 75.0f) {
                this.entity.rotationYawHead = this.entity.renderYawOffset + 75.0f;
            }
        }
    }

    private float updateRotation(float p_75652_1_, float p_75652_2_, float p_75652_3_) {
        float var4 = MathHelper.wrapAngleTo180_float(p_75652_2_ - p_75652_1_);
        if (var4 > p_75652_3_) {
            var4 = p_75652_3_;
        }
        if (var4 < -p_75652_3_) {
            var4 = -p_75652_3_;
        }
        return p_75652_1_ + var4;
    }

    public boolean func_180424_b() {
        return this.isLooking;
    }

    public double func_180423_e() {
        return this.posX;
    }

    public double func_180422_f() {
        return this.posY;
    }

    public double func_180421_g() {
        return this.posZ;
    }
}

