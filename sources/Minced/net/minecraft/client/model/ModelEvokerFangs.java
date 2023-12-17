// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;

public class ModelEvokerFangs extends ModelBase
{
    private final ModelRenderer base;
    private final ModelRenderer upperJaw;
    private final ModelRenderer lowerJaw;
    
    public ModelEvokerFangs() {
        (this.base = new ModelRenderer(this, 0, 0)).setRotationPoint(-5.0f, 22.0f, -5.0f);
        this.base.addBox(0.0f, 0.0f, 0.0f, 10, 12, 10);
        (this.upperJaw = new ModelRenderer(this, 40, 0)).setRotationPoint(1.5f, 22.0f, -4.0f);
        this.upperJaw.addBox(0.0f, 0.0f, 0.0f, 4, 14, 8);
        (this.lowerJaw = new ModelRenderer(this, 40, 0)).setRotationPoint(-1.5f, 22.0f, 4.0f);
        this.lowerJaw.addBox(0.0f, 0.0f, 0.0f, 4, 14, 8);
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        float f = limbSwing * 2.0f;
        if (f > 1.0f) {
            f = 1.0f;
        }
        f = 1.0f - f * f * f;
        this.upperJaw.rotateAngleZ = 3.1415927f - f * 0.35f * 3.1415927f;
        this.lowerJaw.rotateAngleZ = 3.1415927f + f * 0.35f * 3.1415927f;
        this.lowerJaw.rotateAngleY = 3.1415927f;
        final float f2 = (limbSwing + MathHelper.sin(limbSwing * 2.7f)) * 0.6f * 12.0f;
        this.upperJaw.rotationPointY = 24.0f - f2;
        this.lowerJaw.rotationPointY = this.upperJaw.rotationPointY;
        this.base.rotationPointY = this.upperJaw.rotationPointY;
        this.base.render(scale);
        this.upperJaw.render(scale);
        this.lowerJaw.render(scale);
    }
}
