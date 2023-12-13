/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSquid
extends ModelBase {
    ModelRenderer squidBody;
    ModelRenderer[] squidTentacles = new ModelRenderer[8];

    public ModelSquid() {
        int i2 = -16;
        this.squidBody = new ModelRenderer(this, 0, 0);
        this.squidBody.addBox(-6.0f, -8.0f, -6.0f, 12, 16, 12);
        this.squidBody.rotationPointY += (float)(24 + i2);
        for (int j2 = 0; j2 < this.squidTentacles.length; ++j2) {
            this.squidTentacles[j2] = new ModelRenderer(this, 48, 0);
            double d0 = (double)j2 * Math.PI * 2.0 / (double)this.squidTentacles.length;
            float f2 = (float)Math.cos(d0) * 5.0f;
            float f1 = (float)Math.sin(d0) * 5.0f;
            this.squidTentacles[j2].addBox(-1.0f, 0.0f, -1.0f, 2, 18, 2);
            this.squidTentacles[j2].rotationPointX = f2;
            this.squidTentacles[j2].rotationPointZ = f1;
            this.squidTentacles[j2].rotationPointY = 31 + i2;
            d0 = (double)j2 * Math.PI * -2.0 / (double)this.squidTentacles.length + 1.5707963267948966;
            this.squidTentacles[j2].rotateAngleY = (float)d0;
        }
    }

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn) {
        for (ModelRenderer modelrenderer : this.squidTentacles) {
            modelrenderer.rotateAngleX = p_78087_3_;
        }
    }

    @Override
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        this.squidBody.render(scale);
        for (int i2 = 0; i2 < this.squidTentacles.length; ++i2) {
            this.squidTentacles[i2].render(scale);
        }
    }
}

