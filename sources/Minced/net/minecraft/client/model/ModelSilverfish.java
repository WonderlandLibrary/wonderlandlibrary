// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;

public class ModelSilverfish extends ModelBase
{
    private final ModelRenderer[] silverfishBodyParts;
    private final ModelRenderer[] silverfishWings;
    private final float[] zPlacement;
    private static final int[][] SILVERFISH_BOX_LENGTH;
    private static final int[][] SILVERFISH_TEXTURE_POSITIONS;
    
    public ModelSilverfish() {
        this.silverfishBodyParts = new ModelRenderer[7];
        this.zPlacement = new float[7];
        float f = -3.5f;
        for (int i = 0; i < this.silverfishBodyParts.length; ++i) {
            (this.silverfishBodyParts[i] = new ModelRenderer(this, ModelSilverfish.SILVERFISH_TEXTURE_POSITIONS[i][0], ModelSilverfish.SILVERFISH_TEXTURE_POSITIONS[i][1])).addBox(ModelSilverfish.SILVERFISH_BOX_LENGTH[i][0] * -0.5f, 0.0f, ModelSilverfish.SILVERFISH_BOX_LENGTH[i][2] * -0.5f, ModelSilverfish.SILVERFISH_BOX_LENGTH[i][0], ModelSilverfish.SILVERFISH_BOX_LENGTH[i][1], ModelSilverfish.SILVERFISH_BOX_LENGTH[i][2]);
            this.silverfishBodyParts[i].setRotationPoint(0.0f, (float)(24 - ModelSilverfish.SILVERFISH_BOX_LENGTH[i][1]), f);
            this.zPlacement[i] = f;
            if (i < this.silverfishBodyParts.length - 1) {
                f += (ModelSilverfish.SILVERFISH_BOX_LENGTH[i][2] + ModelSilverfish.SILVERFISH_BOX_LENGTH[i + 1][2]) * 0.5f;
            }
        }
        this.silverfishWings = new ModelRenderer[3];
        (this.silverfishWings[0] = new ModelRenderer(this, 20, 0)).addBox(-5.0f, 0.0f, ModelSilverfish.SILVERFISH_BOX_LENGTH[2][2] * -0.5f, 10, 8, ModelSilverfish.SILVERFISH_BOX_LENGTH[2][2]);
        this.silverfishWings[0].setRotationPoint(0.0f, 16.0f, this.zPlacement[2]);
        (this.silverfishWings[1] = new ModelRenderer(this, 20, 11)).addBox(-3.0f, 0.0f, ModelSilverfish.SILVERFISH_BOX_LENGTH[4][2] * -0.5f, 6, 4, ModelSilverfish.SILVERFISH_BOX_LENGTH[4][2]);
        this.silverfishWings[1].setRotationPoint(0.0f, 20.0f, this.zPlacement[4]);
        (this.silverfishWings[2] = new ModelRenderer(this, 20, 18)).addBox(-3.0f, 0.0f, ModelSilverfish.SILVERFISH_BOX_LENGTH[4][2] * -0.5f, 6, 5, ModelSilverfish.SILVERFISH_BOX_LENGTH[1][2]);
        this.silverfishWings[2].setRotationPoint(0.0f, 19.0f, this.zPlacement[1]);
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        for (final ModelRenderer modelrenderer : this.silverfishBodyParts) {
            modelrenderer.render(scale);
        }
        for (final ModelRenderer modelrenderer2 : this.silverfishWings) {
            modelrenderer2.render(scale);
        }
    }
    
    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        for (int i = 0; i < this.silverfishBodyParts.length; ++i) {
            this.silverfishBodyParts[i].rotateAngleY = MathHelper.cos(ageInTicks * 0.9f + i * 0.15f * 3.1415927f) * 3.1415927f * 0.05f * (1 + Math.abs(i - 2));
            this.silverfishBodyParts[i].rotationPointX = MathHelper.sin(ageInTicks * 0.9f + i * 0.15f * 3.1415927f) * 3.1415927f * 0.2f * Math.abs(i - 2);
        }
        this.silverfishWings[0].rotateAngleY = this.silverfishBodyParts[2].rotateAngleY;
        this.silverfishWings[1].rotateAngleY = this.silverfishBodyParts[4].rotateAngleY;
        this.silverfishWings[1].rotationPointX = this.silverfishBodyParts[4].rotationPointX;
        this.silverfishWings[2].rotateAngleY = this.silverfishBodyParts[1].rotateAngleY;
        this.silverfishWings[2].rotationPointX = this.silverfishBodyParts[1].rotationPointX;
    }
    
    static {
        SILVERFISH_BOX_LENGTH = new int[][] { { 3, 2, 2 }, { 4, 3, 2 }, { 6, 4, 3 }, { 3, 3, 3 }, { 2, 2, 3 }, { 2, 1, 2 }, { 1, 1, 2 } };
        SILVERFISH_TEXTURE_POSITIONS = new int[][] { { 0, 0 }, { 0, 4 }, { 0, 9 }, { 0, 16 }, { 0, 22 }, { 11, 0 }, { 13, 4 } };
    }
}
