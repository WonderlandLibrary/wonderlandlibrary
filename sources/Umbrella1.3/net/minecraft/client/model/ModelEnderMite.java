/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelEnderMite
extends ModelBase {
    private static final int[][] field_178716_a = new int[][]{{4, 3, 2}, {6, 4, 5}, {3, 3, 1}, {1, 2, 1}};
    private static final int[][] field_178714_b;
    private static final int field_178715_c;
    private final ModelRenderer[] field_178713_d = new ModelRenderer[field_178715_c];
    private static final String __OBFID = "CL_00002629";

    static {
        int[][] arrarrn = new int[4][];
        arrarrn[0] = new int[2];
        int[] arrn = new int[2];
        arrn[1] = 5;
        arrarrn[1] = arrn;
        int[] arrn2 = new int[2];
        arrn2[1] = 14;
        arrarrn[2] = arrn2;
        int[] arrn3 = new int[2];
        arrn3[1] = 18;
        arrarrn[3] = arrn3;
        field_178714_b = arrarrn;
        field_178715_c = field_178716_a.length;
    }

    public ModelEnderMite() {
        float var1 = -3.5f;
        for (int var2 = 0; var2 < this.field_178713_d.length; ++var2) {
            this.field_178713_d[var2] = new ModelRenderer(this, field_178714_b[var2][0], field_178714_b[var2][1]);
            this.field_178713_d[var2].addBox((float)field_178716_a[var2][0] * -0.5f, 0.0f, (float)field_178716_a[var2][2] * -0.5f, field_178716_a[var2][0], field_178716_a[var2][1], field_178716_a[var2][2]);
            this.field_178713_d[var2].setRotationPoint(0.0f, 24 - field_178716_a[var2][1], var1);
            if (var2 >= this.field_178713_d.length - 1) continue;
            var1 += (float)(field_178716_a[var2][2] + field_178716_a[var2 + 1][2]) * 0.5f;
        }
    }

    @Override
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        for (int var8 = 0; var8 < this.field_178713_d.length; ++var8) {
            this.field_178713_d[var8].render(p_78088_7_);
        }
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        for (int var8 = 0; var8 < this.field_178713_d.length; ++var8) {
            this.field_178713_d[var8].rotateAngleY = MathHelper.cos(p_78087_3_ * 0.9f + (float)var8 * 0.15f * (float)Math.PI) * (float)Math.PI * 0.01f * (float)(1 + Math.abs(var8 - 2));
            this.field_178713_d[var8].rotationPointX = MathHelper.sin(p_78087_3_ * 0.9f + (float)var8 * 0.15f * (float)Math.PI) * (float)Math.PI * 0.1f * (float)Math.abs(var8 - 2);
        }
    }
}

