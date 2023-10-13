/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBlaze
extends ModelBase {
    private ModelRenderer[] blazeSticks = new ModelRenderer[12];
    private ModelRenderer blazeHead;
    private static final String __OBFID = "CL_00000831";

    public ModelBlaze() {
        for (int var1 = 0; var1 < this.blazeSticks.length; ++var1) {
            this.blazeSticks[var1] = new ModelRenderer(this, 0, 16);
            this.blazeSticks[var1].addBox(0.0f, 0.0f, 0.0f, 2, 8, 2);
        }
        this.blazeHead = new ModelRenderer(this, 0, 0);
        this.blazeHead.addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
    }

    @Override
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.blazeHead.render(p_78088_7_);
        for (int var8 = 0; var8 < this.blazeSticks.length; ++var8) {
            this.blazeSticks[var8].render(p_78088_7_);
        }
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        int var9;
        float var8 = p_78087_3_ * (float)Math.PI * -0.1f;
        for (var9 = 0; var9 < 4; ++var9) {
            this.blazeSticks[var9].rotationPointY = -2.0f + MathHelper.cos(((float)(var9 * 2) + p_78087_3_) * 0.25f);
            this.blazeSticks[var9].rotationPointX = MathHelper.cos(var8) * 9.0f;
            this.blazeSticks[var9].rotationPointZ = MathHelper.sin(var8) * 9.0f;
            var8 += 1.0f;
        }
        var8 = 0.7853982f + p_78087_3_ * (float)Math.PI * 0.03f;
        for (var9 = 4; var9 < 8; ++var9) {
            this.blazeSticks[var9].rotationPointY = 2.0f + MathHelper.cos(((float)(var9 * 2) + p_78087_3_) * 0.25f);
            this.blazeSticks[var9].rotationPointX = MathHelper.cos(var8) * 7.0f;
            this.blazeSticks[var9].rotationPointZ = MathHelper.sin(var8) * 7.0f;
            var8 += 1.0f;
        }
        var8 = 0.47123894f + p_78087_3_ * (float)Math.PI * -0.05f;
        for (var9 = 8; var9 < 12; ++var9) {
            this.blazeSticks[var9].rotationPointY = 11.0f + MathHelper.cos(((float)var9 * 1.5f + p_78087_3_) * 0.5f);
            this.blazeSticks[var9].rotationPointX = MathHelper.cos(var8) * 5.0f;
            this.blazeSticks[var9].rotationPointZ = MathHelper.sin(var8) * 5.0f;
            var8 += 1.0f;
        }
        this.blazeHead.rotateAngleY = p_78087_4_ / 57.295776f;
        this.blazeHead.rotateAngleX = p_78087_5_ / 57.295776f;
    }
}

