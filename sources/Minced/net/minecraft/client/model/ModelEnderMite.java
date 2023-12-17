// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;

public class ModelEnderMite extends ModelBase
{
    private static final int[][] BODY_SIZES;
    private static final int[][] BODY_TEXS;
    private static final int BODY_COUNT;
    private final ModelRenderer[] bodyParts;
    
    public ModelEnderMite() {
        this.bodyParts = new ModelRenderer[ModelEnderMite.BODY_COUNT];
        float f = -3.5f;
        for (int i = 0; i < this.bodyParts.length; ++i) {
            (this.bodyParts[i] = new ModelRenderer(this, ModelEnderMite.BODY_TEXS[i][0], ModelEnderMite.BODY_TEXS[i][1])).addBox(ModelEnderMite.BODY_SIZES[i][0] * -0.5f, 0.0f, ModelEnderMite.BODY_SIZES[i][2] * -0.5f, ModelEnderMite.BODY_SIZES[i][0], ModelEnderMite.BODY_SIZES[i][1], ModelEnderMite.BODY_SIZES[i][2]);
            this.bodyParts[i].setRotationPoint(0.0f, (float)(24 - ModelEnderMite.BODY_SIZES[i][1]), f);
            if (i < this.bodyParts.length - 1) {
                f += (ModelEnderMite.BODY_SIZES[i][2] + ModelEnderMite.BODY_SIZES[i + 1][2]) * 0.5f;
            }
        }
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        for (final ModelRenderer modelrenderer : this.bodyParts) {
            modelrenderer.render(scale);
        }
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        for (int i = 0; i < this.bodyParts.length; ++i) {
            this.bodyParts[i].rotateAngleY = MathHelper.cos(ageInTicks * 0.9f + i * 0.15f * 3.1415927f) * 3.1415927f * 0.01f * (1 + Math.abs(i - 2));
            this.bodyParts[i].rotationPointX = MathHelper.sin(ageInTicks * 0.9f + i * 0.15f * 3.1415927f) * 3.1415927f * 0.1f * Math.abs(i - 2);
        }
    }
    
    static {
        BODY_SIZES = new int[][] { { 4, 3, 2 }, { 6, 4, 5 }, { 3, 3, 1 }, { 1, 2, 1 } };
        BODY_TEXS = new int[][] { { 0, 0 }, { 0, 5 }, { 0, 14 }, { 0, 18 } };
        BODY_COUNT = ModelEnderMite.BODY_SIZES.length;
    }
}
