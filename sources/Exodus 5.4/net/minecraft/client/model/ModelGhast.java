/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import java.util.Random;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelGhast
extends ModelBase {
    ModelRenderer[] tentacles = new ModelRenderer[9];
    ModelRenderer body;

    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        int n = 0;
        while (n < this.tentacles.length) {
            this.tentacles[n].rotateAngleX = 0.2f * MathHelper.sin(f3 * 0.3f + (float)n) + 0.4f;
            ++n;
        }
    }

    public ModelGhast() {
        int n = -16;
        this.body = new ModelRenderer(this, 0, 0);
        this.body.addBox(-8.0f, -8.0f, -8.0f, 16, 16, 16);
        this.body.rotationPointY += (float)(24 + n);
        Random random = new Random(1660L);
        int n2 = 0;
        while (n2 < this.tentacles.length) {
            this.tentacles[n2] = new ModelRenderer(this, 0, 0);
            float f = (((float)(n2 % 3) - (float)(n2 / 3 % 2) * 0.5f + 0.25f) / 2.0f * 2.0f - 1.0f) * 5.0f;
            float f2 = ((float)(n2 / 3) / 2.0f * 2.0f - 1.0f) * 5.0f;
            int n3 = random.nextInt(7) + 8;
            this.tentacles[n2].addBox(-1.0f, 0.0f, -1.0f, 2, n3, 2);
            this.tentacles[n2].rotationPointX = f;
            this.tentacles[n2].rotationPointZ = f2;
            this.tentacles[n2].rotationPointY = 31 + n;
            ++n2;
        }
    }

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 0.6f, 0.0f);
        this.body.render(f6);
        ModelRenderer[] modelRendererArray = this.tentacles;
        int n = this.tentacles.length;
        int n2 = 0;
        while (n2 < n) {
            ModelRenderer modelRenderer = modelRendererArray[n2];
            modelRenderer.render(f6);
            ++n2;
        }
        GlStateManager.popMatrix();
    }
}

