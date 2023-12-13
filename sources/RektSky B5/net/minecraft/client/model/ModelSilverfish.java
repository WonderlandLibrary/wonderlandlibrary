/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelSilverfish
extends ModelBase {
    private ModelRenderer[] silverfishBodyParts = new ModelRenderer[7];
    private ModelRenderer[] silverfishWings;
    private float[] field_78170_c = new float[7];
    private static final int[][] silverfishBoxLength = new int[][]{{3, 2, 2}, {4, 3, 2}, {6, 4, 3}, {3, 3, 3}, {2, 2, 3}, {2, 1, 2}, {1, 1, 2}};
    private static final int[][] silverfishTexturePositions = new int[][]{{0, 0}, {0, 4}, {0, 9}, {0, 16}, {0, 22}, {11, 0}, {13, 4}};

    public ModelSilverfish() {
        float f2 = -3.5f;
        for (int i2 = 0; i2 < this.silverfishBodyParts.length; ++i2) {
            this.silverfishBodyParts[i2] = new ModelRenderer(this, silverfishTexturePositions[i2][0], silverfishTexturePositions[i2][1]);
            this.silverfishBodyParts[i2].addBox((float)silverfishBoxLength[i2][0] * -0.5f, 0.0f, (float)silverfishBoxLength[i2][2] * -0.5f, silverfishBoxLength[i2][0], silverfishBoxLength[i2][1], silverfishBoxLength[i2][2]);
            this.silverfishBodyParts[i2].setRotationPoint(0.0f, 24 - silverfishBoxLength[i2][1], f2);
            this.field_78170_c[i2] = f2;
            if (i2 >= this.silverfishBodyParts.length - 1) continue;
            f2 += (float)(silverfishBoxLength[i2][2] + silverfishBoxLength[i2 + 1][2]) * 0.5f;
        }
        this.silverfishWings = new ModelRenderer[3];
        this.silverfishWings[0] = new ModelRenderer(this, 20, 0);
        this.silverfishWings[0].addBox(-5.0f, 0.0f, (float)silverfishBoxLength[2][2] * -0.5f, 10, 8, silverfishBoxLength[2][2]);
        this.silverfishWings[0].setRotationPoint(0.0f, 16.0f, this.field_78170_c[2]);
        this.silverfishWings[1] = new ModelRenderer(this, 20, 11);
        this.silverfishWings[1].addBox(-3.0f, 0.0f, (float)silverfishBoxLength[4][2] * -0.5f, 6, 4, silverfishBoxLength[4][2]);
        this.silverfishWings[1].setRotationPoint(0.0f, 20.0f, this.field_78170_c[4]);
        this.silverfishWings[2] = new ModelRenderer(this, 20, 18);
        this.silverfishWings[2].addBox(-3.0f, 0.0f, (float)silverfishBoxLength[4][2] * -0.5f, 6, 5, silverfishBoxLength[1][2]);
        this.silverfishWings[2].setRotationPoint(0.0f, 19.0f, this.field_78170_c[1]);
    }

    @Override
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        for (int i2 = 0; i2 < this.silverfishBodyParts.length; ++i2) {
            this.silverfishBodyParts[i2].render(scale);
        }
        for (int j2 = 0; j2 < this.silverfishWings.length; ++j2) {
            this.silverfishWings[j2].render(scale);
        }
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn) {
        for (int i2 = 0; i2 < this.silverfishBodyParts.length; ++i2) {
            this.silverfishBodyParts[i2].rotateAngleY = MathHelper.cos(p_78087_3_ * 0.9f + (float)i2 * 0.15f * (float)Math.PI) * (float)Math.PI * 0.05f * (float)(1 + Math.abs(i2 - 2));
            this.silverfishBodyParts[i2].rotationPointX = MathHelper.sin(p_78087_3_ * 0.9f + (float)i2 * 0.15f * (float)Math.PI) * (float)Math.PI * 0.2f * (float)Math.abs(i2 - 2);
        }
        this.silverfishWings[0].rotateAngleY = this.silverfishBodyParts[2].rotateAngleY;
        this.silverfishWings[1].rotateAngleY = this.silverfishBodyParts[4].rotateAngleY;
        this.silverfishWings[1].rotationPointX = this.silverfishBodyParts[4].rotationPointX;
        this.silverfishWings[2].rotateAngleY = this.silverfishBodyParts[1].rotateAngleY;
        this.silverfishWings[2].rotationPointX = this.silverfishBodyParts[1].rotationPointX;
    }
}

