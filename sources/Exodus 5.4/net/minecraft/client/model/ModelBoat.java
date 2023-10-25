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
        int n = 24;
        int n2 = 6;
        int n3 = 20;
        int n4 = 4;
        this.boatSides[0].addBox((float)(-n / 2), (float)(-n3 / 2 + 2), -3.0f, n, n3 - 4, 4, 0.0f);
        this.boatSides[0].setRotationPoint(0.0f, n4, 0.0f);
        this.boatSides[1].addBox((float)(-n / 2 + 2), (float)(-n2 - 1), -1.0f, n - 4, n2, 2, 0.0f);
        this.boatSides[1].setRotationPoint(-n / 2 + 1, n4, 0.0f);
        this.boatSides[2].addBox((float)(-n / 2 + 2), (float)(-n2 - 1), -1.0f, n - 4, n2, 2, 0.0f);
        this.boatSides[2].setRotationPoint(n / 2 - 1, n4, 0.0f);
        this.boatSides[3].addBox((float)(-n / 2 + 2), (float)(-n2 - 1), -1.0f, n - 4, n2, 2, 0.0f);
        this.boatSides[3].setRotationPoint(0.0f, n4, -n3 / 2 + 1);
        this.boatSides[4].addBox((float)(-n / 2 + 2), (float)(-n2 - 1), -1.0f, n - 4, n2, 2, 0.0f);
        this.boatSides[4].setRotationPoint(0.0f, n4, n3 / 2 - 1);
        this.boatSides[0].rotateAngleX = 1.5707964f;
        this.boatSides[1].rotateAngleY = 4.712389f;
        this.boatSides[2].rotateAngleY = 1.5707964f;
        this.boatSides[3].rotateAngleY = (float)Math.PI;
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        int n = 0;
        while (n < 5) {
            this.boatSides[n].render(f6);
            ++n;
        }
    }
}

