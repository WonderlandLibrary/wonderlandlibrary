/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBoat
extends ModelBase {
    public ModelRenderer[] boatSides = new ModelRenderer[5];

    public ModelBoat() {
        this.boatSides[0] = new ModelRenderer(this, 0, 8);
        this.boatSides[1] = new ModelRenderer(this, 0, 0);
        this.boatSides[2] = new ModelRenderer(this, 0, 0);
        this.boatSides[3] = new ModelRenderer(this, 0, 0);
        this.boatSides[4] = new ModelRenderer(this, 0, 0);
        int i2 = 24;
        int j2 = 6;
        int k2 = 20;
        int l2 = 4;
        this.boatSides[0].addBox((float)(-i2 / 2), (float)(-k2 / 2 + 2), -3.0f, i2, k2 - 4, 4, 0.0f);
        this.boatSides[0].setRotationPoint(0.0f, l2, 0.0f);
        this.boatSides[1].addBox((float)(-i2 / 2 + 2), (float)(-j2 - 1), -1.0f, i2 - 4, j2, 2, 0.0f);
        this.boatSides[1].setRotationPoint(-i2 / 2 + 1, l2, 0.0f);
        this.boatSides[2].addBox((float)(-i2 / 2 + 2), (float)(-j2 - 1), -1.0f, i2 - 4, j2, 2, 0.0f);
        this.boatSides[2].setRotationPoint(i2 / 2 - 1, l2, 0.0f);
        this.boatSides[3].addBox((float)(-i2 / 2 + 2), (float)(-j2 - 1), -1.0f, i2 - 4, j2, 2, 0.0f);
        this.boatSides[3].setRotationPoint(0.0f, l2, -k2 / 2 + 1);
        this.boatSides[4].addBox((float)(-i2 / 2 + 2), (float)(-j2 - 1), -1.0f, i2 - 4, j2, 2, 0.0f);
        this.boatSides[4].setRotationPoint(0.0f, l2, k2 / 2 - 1);
        this.boatSides[0].rotateAngleX = 1.5707964f;
        this.boatSides[1].rotateAngleY = 4.712389f;
        this.boatSides[2].rotateAngleY = 1.5707964f;
        this.boatSides[3].rotateAngleY = (float)Math.PI;
    }

    @Override
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        for (int i2 = 0; i2 < 5; ++i2) {
            this.boatSides[i2].render(scale);
        }
    }
}

