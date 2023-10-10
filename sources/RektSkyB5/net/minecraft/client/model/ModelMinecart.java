/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMinecart
extends ModelBase {
    public ModelRenderer[] sideModels = new ModelRenderer[7];

    public ModelMinecart() {
        this.sideModels[0] = new ModelRenderer(this, 0, 10);
        this.sideModels[1] = new ModelRenderer(this, 0, 0);
        this.sideModels[2] = new ModelRenderer(this, 0, 0);
        this.sideModels[3] = new ModelRenderer(this, 0, 0);
        this.sideModels[4] = new ModelRenderer(this, 0, 0);
        this.sideModels[5] = new ModelRenderer(this, 44, 10);
        int i2 = 20;
        int j2 = 8;
        int k2 = 16;
        int l2 = 4;
        this.sideModels[0].addBox((float)(-i2 / 2), (float)(-k2 / 2), -1.0f, i2, k2, 2, 0.0f);
        this.sideModels[0].setRotationPoint(0.0f, l2, 0.0f);
        this.sideModels[5].addBox((float)(-i2 / 2 + 1), (float)(-k2 / 2 + 1), -1.0f, i2 - 2, k2 - 2, 1, 0.0f);
        this.sideModels[5].setRotationPoint(0.0f, l2, 0.0f);
        this.sideModels[1].addBox((float)(-i2 / 2 + 2), (float)(-j2 - 1), -1.0f, i2 - 4, j2, 2, 0.0f);
        this.sideModels[1].setRotationPoint(-i2 / 2 + 1, l2, 0.0f);
        this.sideModels[2].addBox((float)(-i2 / 2 + 2), (float)(-j2 - 1), -1.0f, i2 - 4, j2, 2, 0.0f);
        this.sideModels[2].setRotationPoint(i2 / 2 - 1, l2, 0.0f);
        this.sideModels[3].addBox((float)(-i2 / 2 + 2), (float)(-j2 - 1), -1.0f, i2 - 4, j2, 2, 0.0f);
        this.sideModels[3].setRotationPoint(0.0f, l2, -k2 / 2 + 1);
        this.sideModels[4].addBox((float)(-i2 / 2 + 2), (float)(-j2 - 1), -1.0f, i2 - 4, j2, 2, 0.0f);
        this.sideModels[4].setRotationPoint(0.0f, l2, k2 / 2 - 1);
        this.sideModels[0].rotateAngleX = 1.5707964f;
        this.sideModels[1].rotateAngleY = 4.712389f;
        this.sideModels[2].rotateAngleY = 1.5707964f;
        this.sideModels[3].rotateAngleY = (float)Math.PI;
        this.sideModels[5].rotateAngleX = -1.5707964f;
    }

    @Override
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        this.sideModels[5].rotationPointY = 4.0f - p_78088_4_;
        for (int i2 = 0; i2 < 6; ++i2) {
            this.sideModels[i2].render(scale);
        }
    }
}

