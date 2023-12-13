// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.model;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;

public class ModelEnderMite extends ModelBase
{
    private static final int[][] field_178716_a;
    private static final int[][] field_178714_b;
    private static final int field_178715_c;
    private final ModelRenderer[] field_178713_d;
    private static final String __OBFID = "CL_00002629";
    
    public ModelEnderMite() {
        this.field_178713_d = new ModelRenderer[ModelEnderMite.field_178715_c];
        float var1 = -3.5f;
        for (int var2 = 0; var2 < this.field_178713_d.length; ++var2) {
            (this.field_178713_d[var2] = new ModelRenderer(this, ModelEnderMite.field_178714_b[var2][0], ModelEnderMite.field_178714_b[var2][1])).addBox(ModelEnderMite.field_178716_a[var2][0] * -0.5f, 0.0f, ModelEnderMite.field_178716_a[var2][2] * -0.5f, ModelEnderMite.field_178716_a[var2][0], ModelEnderMite.field_178716_a[var2][1], ModelEnderMite.field_178716_a[var2][2]);
            this.field_178713_d[var2].setRotationPoint(0.0f, 24 - ModelEnderMite.field_178716_a[var2][1], var1);
            if (var2 < this.field_178713_d.length - 1) {
                var1 += (ModelEnderMite.field_178716_a[var2][2] + ModelEnderMite.field_178716_a[var2 + 1][2]) * 0.5f;
            }
        }
    }
    
    @Override
    public void render(final Entity p_78088_1_, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        for (int var8 = 0; var8 < this.field_178713_d.length; ++var8) {
            this.field_178713_d[var8].render(p_78088_7_);
        }
    }
    
    @Override
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity p_78087_7_) {
        for (int var8 = 0; var8 < this.field_178713_d.length; ++var8) {
            this.field_178713_d[var8].rotateAngleY = MathHelper.cos(p_78087_3_ * 0.9f + var8 * 0.15f * 3.1415927f) * 3.1415927f * 0.01f * (1 + Math.abs(var8 - 2));
            this.field_178713_d[var8].rotationPointX = MathHelper.sin(p_78087_3_ * 0.9f + var8 * 0.15f * 3.1415927f) * 3.1415927f * 0.1f * Math.abs(var8 - 2);
        }
    }
    
    static {
        field_178716_a = new int[][] { { 4, 3, 2 }, { 6, 4, 5 }, { 3, 3, 1 }, { 1, 2, 1 } };
        field_178714_b = new int[][] { { 0, 0 }, { 0, 5 }, { 0, 14 }, { 0, 18 } };
        field_178715_c = ModelEnderMite.field_178716_a.length;
    }
}
