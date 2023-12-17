// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.Entity;

public class ModelZombie extends ModelBiped
{
    public ModelZombie() {
        this(0.0f, false);
    }
    
    public ModelZombie(final float modelSize, final boolean p_i1168_2_) {
        super(modelSize, 0.0f, 64, p_i1168_2_ ? 32 : 64);
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        final boolean flag = entityIn instanceof EntityZombie && ((EntityZombie)entityIn).isArmsRaised();
        final float f = MathHelper.sin(this.swingProgress * 3.1415927f);
        final float f2 = MathHelper.sin((1.0f - (1.0f - this.swingProgress) * (1.0f - this.swingProgress)) * 3.1415927f);
        this.bipedRightArm.rotateAngleZ = 0.0f;
        this.bipedLeftArm.rotateAngleZ = 0.0f;
        this.bipedRightArm.rotateAngleY = -(0.1f - f * 0.6f);
        this.bipedLeftArm.rotateAngleY = 0.1f - f * 0.6f;
        final float f3 = -3.1415927f / (flag ? 1.5f : 2.25f);
        this.bipedRightArm.rotateAngleX = f3;
        this.bipedLeftArm.rotateAngleX = f3;
        final ModelRenderer bipedRightArm = this.bipedRightArm;
        bipedRightArm.rotateAngleX += f * 1.2f - f2 * 0.4f;
        final ModelRenderer bipedLeftArm = this.bipedLeftArm;
        bipedLeftArm.rotateAngleX += f * 1.2f - f2 * 0.4f;
        final ModelRenderer bipedRightArm2 = this.bipedRightArm;
        bipedRightArm2.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
        final ModelRenderer bipedLeftArm2 = this.bipedLeftArm;
        bipedLeftArm2.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
        final ModelRenderer bipedRightArm3 = this.bipedRightArm;
        bipedRightArm3.rotateAngleX += MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
        final ModelRenderer bipedLeftArm3 = this.bipedLeftArm;
        bipedLeftArm3.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
    }
}
