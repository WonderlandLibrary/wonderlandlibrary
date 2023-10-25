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

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.sideModels[5].rotationPointY = 4.0f - f3;
        int n = 0;
        while (n < 6) {
            this.sideModels[n].render(f6);
            ++n;
        }
    }

    public ModelMinecart() {
        this.sideModels[0] = new ModelRenderer(this, 0, 10);
        this.sideModels[1] = new ModelRenderer(this, 0, 0);
        this.sideModels[2] = new ModelRenderer(this, 0, 0);
        this.sideModels[3] = new ModelRenderer(this, 0, 0);
        this.sideModels[4] = new ModelRenderer(this, 0, 0);
        this.sideModels[5] = new ModelRenderer(this, 44, 10);
        int n = 20;
        int n2 = 8;
        int n3 = 16;
        int n4 = 4;
        this.sideModels[0].addBox((float)(-n / 2), (float)(-n3 / 2), -1.0f, n, n3, 2, 0.0f);
        this.sideModels[0].setRotationPoint(0.0f, n4, 0.0f);
        this.sideModels[5].addBox((float)(-n / 2 + 1), (float)(-n3 / 2 + 1), -1.0f, n - 2, n3 - 2, 1, 0.0f);
        this.sideModels[5].setRotationPoint(0.0f, n4, 0.0f);
        this.sideModels[1].addBox((float)(-n / 2 + 2), (float)(-n2 - 1), -1.0f, n - 4, n2, 2, 0.0f);
        this.sideModels[1].setRotationPoint(-n / 2 + 1, n4, 0.0f);
        this.sideModels[2].addBox((float)(-n / 2 + 2), (float)(-n2 - 1), -1.0f, n - 4, n2, 2, 0.0f);
        this.sideModels[2].setRotationPoint(n / 2 - 1, n4, 0.0f);
        this.sideModels[3].addBox((float)(-n / 2 + 2), (float)(-n2 - 1), -1.0f, n - 4, n2, 2, 0.0f);
        this.sideModels[3].setRotationPoint(0.0f, n4, -n3 / 2 + 1);
        this.sideModels[4].addBox((float)(-n / 2 + 2), (float)(-n2 - 1), -1.0f, n - 4, n2, 2, 0.0f);
        this.sideModels[4].setRotationPoint(0.0f, n4, n3 / 2 - 1);
        this.sideModels[0].rotateAngleX = 1.5707964f;
        this.sideModels[1].rotateAngleY = 4.712389f;
        this.sideModels[2].rotateAngleY = 1.5707964f;
        this.sideModels[3].rotateAngleY = (float)Math.PI;
        this.sideModels[5].rotateAngleX = -1.5707964f;
    }
}

