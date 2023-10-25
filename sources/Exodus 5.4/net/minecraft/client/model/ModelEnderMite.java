/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelEnderMite
extends ModelBase {
    private final ModelRenderer[] field_178713_d = new ModelRenderer[field_178715_c];
    private static final int[][] field_178716_a = new int[][]{{4, 3, 2}, {6, 4, 5}, {3, 3, 1}, {1, 2, 1}};
    private static final int[][] field_178714_b;
    private static final int field_178715_c;

    static {
        int[][] nArrayArray = new int[4][];
        nArrayArray[0] = new int[2];
        int[] nArray = new int[2];
        nArray[1] = 5;
        nArrayArray[1] = nArray;
        int[] nArray2 = new int[2];
        nArray2[1] = 14;
        nArrayArray[2] = nArray2;
        int[] nArray3 = new int[2];
        nArray3[1] = 18;
        nArrayArray[3] = nArray3;
        field_178714_b = nArrayArray;
        field_178715_c = field_178716_a.length;
    }

    public ModelEnderMite() {
        float f = -3.5f;
        int n = 0;
        while (n < this.field_178713_d.length) {
            this.field_178713_d[n] = new ModelRenderer(this, field_178714_b[n][0], field_178714_b[n][1]);
            this.field_178713_d[n].addBox((float)field_178716_a[n][0] * -0.5f, 0.0f, (float)field_178716_a[n][2] * -0.5f, field_178716_a[n][0], field_178716_a[n][1], field_178716_a[n][2]);
            this.field_178713_d[n].setRotationPoint(0.0f, 24 - field_178716_a[n][1], f);
            if (n < this.field_178713_d.length - 1) {
                f += (float)(field_178716_a[n][2] + field_178716_a[n + 1][2]) * 0.5f;
            }
            ++n;
        }
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        int n = 0;
        while (n < this.field_178713_d.length) {
            this.field_178713_d[n].render(f6);
            ++n;
        }
    }

    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        int n = 0;
        while (n < this.field_178713_d.length) {
            this.field_178713_d[n].rotateAngleY = MathHelper.cos(f3 * 0.9f + (float)n * 0.15f * (float)Math.PI) * (float)Math.PI * 0.01f * (float)(1 + Math.abs(n - 2));
            this.field_178713_d[n].rotationPointX = MathHelper.sin(f3 * 0.9f + (float)n * 0.15f * (float)Math.PI) * (float)Math.PI * 0.1f * (float)Math.abs(n - 2);
            ++n;
        }
    }
}

