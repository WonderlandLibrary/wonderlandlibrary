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
    private static final int[][] silverfishTexturePositions;
    private ModelRenderer[] silverfishWings;
    private ModelRenderer[] silverfishBodyParts = new ModelRenderer[7];
    private static final int[][] silverfishBoxLength;
    private float[] field_78170_c = new float[7];

    static {
        silverfishBoxLength = new int[][]{{3, 2, 2}, {4, 3, 2}, {6, 4, 3}, {3, 3, 3}, {2, 2, 3}, {2, 1, 2}, {1, 1, 2}};
        int[][] nArrayArray = new int[7][];
        nArrayArray[0] = new int[2];
        int[] nArray = new int[2];
        nArray[1] = 4;
        nArrayArray[1] = nArray;
        int[] nArray2 = new int[2];
        nArray2[1] = 9;
        nArrayArray[2] = nArray2;
        int[] nArray3 = new int[2];
        nArray3[1] = 16;
        nArrayArray[3] = nArray3;
        int[] nArray4 = new int[2];
        nArray4[1] = 22;
        nArrayArray[4] = nArray4;
        int[] nArray5 = new int[2];
        nArray5[0] = 11;
        nArrayArray[5] = nArray5;
        nArrayArray[6] = new int[]{13, 4};
        silverfishTexturePositions = nArrayArray;
    }

    public ModelSilverfish() {
        float f = -3.5f;
        int n = 0;
        while (n < this.silverfishBodyParts.length) {
            this.silverfishBodyParts[n] = new ModelRenderer(this, silverfishTexturePositions[n][0], silverfishTexturePositions[n][1]);
            this.silverfishBodyParts[n].addBox((float)silverfishBoxLength[n][0] * -0.5f, 0.0f, (float)silverfishBoxLength[n][2] * -0.5f, silverfishBoxLength[n][0], silverfishBoxLength[n][1], silverfishBoxLength[n][2]);
            this.silverfishBodyParts[n].setRotationPoint(0.0f, 24 - silverfishBoxLength[n][1], f);
            this.field_78170_c[n] = f;
            if (n < this.silverfishBodyParts.length - 1) {
                f += (float)(silverfishBoxLength[n][2] + silverfishBoxLength[n + 1][2]) * 0.5f;
            }
            ++n;
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
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        int n = 0;
        while (n < this.silverfishBodyParts.length) {
            this.silverfishBodyParts[n].render(f6);
            ++n;
        }
        n = 0;
        while (n < this.silverfishWings.length) {
            this.silverfishWings[n].render(f6);
            ++n;
        }
    }

    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        int n = 0;
        while (n < this.silverfishBodyParts.length) {
            this.silverfishBodyParts[n].rotateAngleY = MathHelper.cos(f3 * 0.9f + (float)n * 0.15f * (float)Math.PI) * (float)Math.PI * 0.05f * (float)(1 + Math.abs(n - 2));
            this.silverfishBodyParts[n].rotationPointX = MathHelper.sin(f3 * 0.9f + (float)n * 0.15f * (float)Math.PI) * (float)Math.PI * 0.2f * (float)Math.abs(n - 2);
            ++n;
        }
        this.silverfishWings[0].rotateAngleY = this.silverfishBodyParts[2].rotateAngleY;
        this.silverfishWings[1].rotateAngleY = this.silverfishBodyParts[4].rotateAngleY;
        this.silverfishWings[1].rotationPointX = this.silverfishBodyParts[4].rotationPointX;
        this.silverfishWings[2].rotateAngleY = this.silverfishBodyParts[1].rotateAngleY;
        this.silverfishWings[2].rotationPointX = this.silverfishBodyParts[1].rotationPointX;
    }
}

