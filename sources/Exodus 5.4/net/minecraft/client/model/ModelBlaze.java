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

    @Override
    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        this.blazeHead.render(f6);
        int n = 0;
        while (n < this.blazeSticks.length) {
            this.blazeSticks[n].render(f6);
            ++n;
        }
    }

    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        float f7 = f3 * (float)Math.PI * -0.1f;
        int n = 0;
        while (n < 4) {
            this.blazeSticks[n].rotationPointY = -2.0f + MathHelper.cos(((float)(n * 2) + f3) * 0.25f);
            this.blazeSticks[n].rotationPointX = MathHelper.cos(f7) * 9.0f;
            this.blazeSticks[n].rotationPointZ = MathHelper.sin(f7) * 9.0f;
            f7 += 1.0f;
            ++n;
        }
        f7 = 0.7853982f + f3 * (float)Math.PI * 0.03f;
        n = 4;
        while (n < 8) {
            this.blazeSticks[n].rotationPointY = 2.0f + MathHelper.cos(((float)(n * 2) + f3) * 0.25f);
            this.blazeSticks[n].rotationPointX = MathHelper.cos(f7) * 7.0f;
            this.blazeSticks[n].rotationPointZ = MathHelper.sin(f7) * 7.0f;
            f7 += 1.0f;
            ++n;
        }
        f7 = 0.47123894f + f3 * (float)Math.PI * -0.05f;
        n = 8;
        while (n < 12) {
            this.blazeSticks[n].rotationPointY = 11.0f + MathHelper.cos(((float)n * 1.5f + f3) * 0.5f);
            this.blazeSticks[n].rotationPointX = MathHelper.cos(f7) * 5.0f;
            this.blazeSticks[n].rotationPointZ = MathHelper.sin(f7) * 5.0f;
            f7 += 1.0f;
            ++n;
        }
        this.blazeHead.rotateAngleY = f4 / 57.295776f;
        this.blazeHead.rotateAngleX = f5 / 57.295776f;
    }

    public ModelBlaze() {
        int n = 0;
        while (n < this.blazeSticks.length) {
            this.blazeSticks[n] = new ModelRenderer(this, 0, 16);
            this.blazeSticks[n].addBox(0.0f, 0.0f, 0.0f, 2, 8, 2);
            ++n;
        }
        this.blazeHead = new ModelRenderer(this, 0, 0);
        this.blazeHead.addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
    }
}

