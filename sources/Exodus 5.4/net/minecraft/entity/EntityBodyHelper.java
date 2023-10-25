/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class EntityBodyHelper {
    private EntityLivingBase theLiving;
    private float prevRenderYawHead;
    private int rotationTickCounter;

    public EntityBodyHelper(EntityLivingBase entityLivingBase) {
        this.theLiving = entityLivingBase;
    }

    public void updateRenderAngles() {
        double d = this.theLiving.posX - this.theLiving.prevPosX;
        double d2 = this.theLiving.posZ - this.theLiving.prevPosZ;
        if (d * d + d2 * d2 > 2.500000277905201E-7) {
            this.theLiving.renderYawOffset = this.theLiving.rotationYaw;
            this.prevRenderYawHead = this.theLiving.rotationYawHead = this.computeAngleWithBound(this.theLiving.renderYawOffset, this.theLiving.rotationYawHead, 75.0f);
            this.rotationTickCounter = 0;
        } else {
            float f = 75.0f;
            if (Math.abs(this.theLiving.rotationYawHead - this.prevRenderYawHead) > 15.0f) {
                this.rotationTickCounter = 0;
                this.prevRenderYawHead = this.theLiving.rotationYawHead;
            } else {
                ++this.rotationTickCounter;
                int n = 10;
                if (this.rotationTickCounter > 10) {
                    f = Math.max(1.0f - (float)(this.rotationTickCounter - 10) / 10.0f, 0.0f) * 75.0f;
                }
            }
            this.theLiving.renderYawOffset = this.computeAngleWithBound(this.theLiving.rotationYawHead, this.theLiving.renderYawOffset, f);
        }
    }

    private float computeAngleWithBound(float f, float f2, float f3) {
        float f4 = MathHelper.wrapAngleTo180_float(f - f2);
        if (f4 < -f3) {
            f4 = -f3;
        }
        if (f4 >= f3) {
            f4 = f3;
        }
        return f - f4;
    }
}

