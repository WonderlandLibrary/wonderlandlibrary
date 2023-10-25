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
        int n = -16;
        this.squidBody = new ModelRenderer(this, 0, 0);
        this.squidBody.addBox(-6.0f, -8.0f, -6.0f, 12, 16, 12);
        this.squidBody.rotationPointY += (float)(24 + n);
        int n2 = 0;
        while (n2 < this.squidTentacles.length) {
            this.squidTentacles[n2] = new ModelRenderer(this, 48, 0);
            double d = (double)n2 * Math.PI * 2.0 / (double)this.squidTentacles.length;
            float f = (float)Math.cos(d) * 5.0f;
            float f2 = (float)Math.sin(d) * 5.0f;
            this.squidTentacles[n2].addBox(-1.0f, 0.0f, -1.0f, 2, 18, 2);
            this.squidTentacles[n2].rotationPointX = f;
            this.squidTentacles[n2].rotationPointZ = f2;
            this.squidTentacles[n2].rotationPointY = 31 + n;
            d = (double)n2 * Math.PI * -2.0 / (double)this.squidTentacles.length + 1.5707963267948966;
            this.squidTentacles[n2].rotateAngleY = (float)d;
            ++n2;
        }
    }

    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        ModelRenderer[] modelRendererArray = this.squidTentacles;
        int n = this.squidTentacles.length;
        int n2 = 0;
        while (n2 < n) {
            ModelRenderer modelRenderer = modelRendererArray[n2];
            modelRenderer.rotateAngleX = f3;
            ++n2;
        }
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        this.squidBody.render(f6);
        int n = 0;
        while (n < this.squidTentacles.length) {
            this.squidTentacles[n].render(f6);
            ++n;
        }
    }
}

