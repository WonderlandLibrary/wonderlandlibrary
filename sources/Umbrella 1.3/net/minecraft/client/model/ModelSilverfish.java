/*
 * Decompiled with CFR 0.150.
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
    private static final int[][] silverfishTexturePositions;
    private static final String __OBFID = "CL_00000855";

    static {
        int[][] arrarrn = new int[7][];
        arrarrn[0] = new int[2];
        int[] arrn = new int[2];
        arrn[1] = 4;
        arrarrn[1] = arrn;
        int[] arrn2 = new int[2];
        arrn2[1] = 9;
        arrarrn[2] = arrn2;
        int[] arrn3 = new int[2];
        arrn3[1] = 16;
        arrarrn[3] = arrn3;
        int[] arrn4 = new int[2];
        arrn4[1] = 22;
        arrarrn[4] = arrn4;
        int[] arrn5 = new int[2];
        arrn5[0] = 11;
        arrarrn[5] = arrn5;
        arrarrn[6] = new int[]{13, 4};
        silverfishTexturePositions = arrarrn;
    }

    public ModelSilverfish() {
        float var1 = -3.5f;
        for (int var2 = 0; var2 < this.silverfishBodyParts.length; ++var2) {
            this.silverfishBodyParts[var2] = new ModelRenderer(this, silverfishTexturePositions[var2][0], silverfishTexturePositions[var2][1]);
            this.silverfishBodyParts[var2].addBox((float)silverfishBoxLength[var2][0] * -0.5f, 0.0f, (float)silverfishBoxLength[var2][2] * -0.5f, silverfishBoxLength[var2][0], silverfishBoxLength[var2][1], silverfishBoxLength[var2][2]);
            this.silverfishBodyParts[var2].setRotationPoint(0.0f, 24 - silverfishBoxLength[var2][1], var1);
            this.field_78170_c[var2] = var1;
            if (var2 >= this.silverfishBodyParts.length - 1) continue;
            var1 += (float)(silverfishBoxLength[var2][2] + silverfishBoxLength[var2 + 1][2]) * 0.5f;
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
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        int var8;
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        for (var8 = 0; var8 < this.silverfishBodyParts.length; ++var8) {
            this.silverfishBodyParts[var8].render(p_78088_7_);
        }
        for (var8 = 0; var8 < this.silverfishWings.length; ++var8) {
            this.silverfishWings[var8].render(p_78088_7_);
        }
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        for (int var8 = 0; var8 < this.silverfishBodyParts.length; ++var8) {
            this.silverfishBodyParts[var8].rotateAngleY = MathHelper.cos(p_78087_3_ * 0.9f + (float)var8 * 0.15f * (float)Math.PI) * (float)Math.PI * 0.05f * (float)(1 + Math.abs(var8 - 2));
            this.silverfishBodyParts[var8].rotationPointX = MathHelper.sin(p_78087_3_ * 0.9f + (float)var8 * 0.15f * (float)Math.PI) * (float)Math.PI * 0.2f * (float)Math.abs(var8 - 2);
        }
        this.silverfishWings[0].rotateAngleY = this.silverfishBodyParts[2].rotateAngleY;
        this.silverfishWings[1].rotateAngleY = this.silverfishBodyParts[4].rotateAngleY;
        this.silverfishWings[1].rotationPointX = this.silverfishBodyParts[4].rotationPointX;
        this.silverfishWings[2].rotateAngleY = this.silverfishBodyParts[1].rotateAngleY;
        this.silverfishWings[2].rotationPointX = this.silverfishBodyParts[1].rotationPointX;
    }
}

