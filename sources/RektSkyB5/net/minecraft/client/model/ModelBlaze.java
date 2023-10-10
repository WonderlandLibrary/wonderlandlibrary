/*
 * Decompiled with CFR 0.152.
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

    public ModelBlaze() {
        for (int i2 = 0; i2 < this.blazeSticks.length; ++i2) {
            this.blazeSticks[i2] = new ModelRenderer(this, 0, 16);
            this.blazeSticks[i2].addBox(0.0f, 0.0f, 0.0f, 2, 8, 2);
        }
        this.blazeHead = new ModelRenderer(this, 0, 0);
        this.blazeHead.addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
    }

    @Override
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        this.blazeHead.render(scale);
        for (int i2 = 0; i2 < this.blazeSticks.length; ++i2) {
            this.blazeSticks[i2].render(scale);
        }
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn) {
        float f2 = p_78087_3_ * (float)Math.PI * -0.1f;
        for (int i2 = 0; i2 < 4; ++i2) {
            this.blazeSticks[i2].rotationPointY = -2.0f + MathHelper.cos(((float)(i2 * 2) + p_78087_3_) * 0.25f);
            this.blazeSticks[i2].rotationPointX = MathHelper.cos(f2) * 9.0f;
            this.blazeSticks[i2].rotationPointZ = MathHelper.sin(f2) * 9.0f;
            f2 += 1.0f;
        }
        f2 = 0.7853982f + p_78087_3_ * (float)Math.PI * 0.03f;
        for (int j2 = 4; j2 < 8; ++j2) {
            this.blazeSticks[j2].rotationPointY = 2.0f + MathHelper.cos(((float)(j2 * 2) + p_78087_3_) * 0.25f);
            this.blazeSticks[j2].rotationPointX = MathHelper.cos(f2) * 7.0f;
            this.blazeSticks[j2].rotationPointZ = MathHelper.sin(f2) * 7.0f;
            f2 += 1.0f;
        }
        f2 = 0.47123894f + p_78087_3_ * (float)Math.PI * -0.05f;
        for (int k2 = 8; k2 < 12; ++k2) {
            this.blazeSticks[k2].rotationPointY = 11.0f + MathHelper.cos(((float)k2 * 1.5f + p_78087_3_) * 0.5f);
            this.blazeSticks[k2].rotationPointX = MathHelper.cos(f2) * 5.0f;
            this.blazeSticks[k2].rotationPointZ = MathHelper.sin(f2) * 5.0f;
            f2 += 1.0f;
        }
        this.blazeHead.rotateAngleY = p_78087_4_ / 57.295776f;
        this.blazeHead.rotateAngleX = p_78087_5_ / 57.295776f;
    }
}

