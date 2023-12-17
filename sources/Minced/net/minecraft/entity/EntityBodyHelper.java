// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.util.math.MathHelper;

public class EntityBodyHelper
{
    private final EntityLivingBase living;
    private int rotationTickCounter;
    private float prevRenderYawHead;
    
    public EntityBodyHelper(final EntityLivingBase livingIn) {
        this.living = livingIn;
    }
    
    public void updateRenderAngles() {
        final double d0 = this.living.posX - this.living.prevPosX;
        final double d2 = this.living.posZ - this.living.prevPosZ;
        if (d0 * d0 + d2 * d2 > 2.500000277905201E-7) {
            this.living.renderYawOffset = this.living.rotationYaw;
            this.living.rotationYawHead = this.computeAngleWithBound(this.living.renderYawOffset, this.living.rotationYawHead, 75.0f);
            this.prevRenderYawHead = this.living.rotationYawHead;
            this.rotationTickCounter = 0;
        }
        else if (this.living.getPassengers().isEmpty() || !(this.living.getPassengers().get(0) instanceof EntityLiving)) {
            float f = 75.0f;
            if (Math.abs(this.living.rotationYawHead - this.prevRenderYawHead) > 15.0f) {
                this.rotationTickCounter = 0;
                this.prevRenderYawHead = this.living.rotationYawHead;
            }
            else {
                ++this.rotationTickCounter;
                final int i = 10;
                if (this.rotationTickCounter > 10) {
                    f = Math.max(1.0f - (this.rotationTickCounter - 10) / 10.0f, 0.0f) * 75.0f;
                }
            }
            this.living.renderYawOffset = this.computeAngleWithBound(this.living.rotationYawHead, this.living.renderYawOffset, f);
        }
    }
    
    private float computeAngleWithBound(final float p_75665_1_, final float p_75665_2_, final float p_75665_3_) {
        float f = MathHelper.wrapDegrees(p_75665_1_ - p_75665_2_);
        if (f < -p_75665_3_) {
            f = -p_75665_3_;
        }
        if (f >= p_75665_3_) {
            f = p_75665_3_;
        }
        return p_75665_1_ - f;
    }
}
