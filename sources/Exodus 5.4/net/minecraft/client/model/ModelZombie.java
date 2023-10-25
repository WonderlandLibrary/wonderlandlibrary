/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelZombie
extends ModelBiped {
    @Override
    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        super.setRotationAngles(f, f2, f3, f4, f5, f6, entity);
        float f7 = MathHelper.sin(this.swingProgress * (float)Math.PI);
        float f8 = MathHelper.sin((1.0f - (1.0f - this.swingProgress) * (1.0f - this.swingProgress)) * (float)Math.PI);
        this.bipedRightArm.rotateAngleZ = 0.0f;
        this.bipedLeftArm.rotateAngleZ = 0.0f;
        this.bipedRightArm.rotateAngleY = -(0.1f - f7 * 0.6f);
        this.bipedLeftArm.rotateAngleY = 0.1f - f7 * 0.6f;
        this.bipedRightArm.rotateAngleX = -1.5707964f;
        this.bipedLeftArm.rotateAngleX = -1.5707964f;
        this.bipedRightArm.rotateAngleX -= f7 * 1.2f - f8 * 0.4f;
        this.bipedLeftArm.rotateAngleX -= f7 * 1.2f - f8 * 0.4f;
        this.bipedRightArm.rotateAngleZ += MathHelper.cos(f3 * 0.09f) * 0.05f + 0.05f;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(f3 * 0.09f) * 0.05f + 0.05f;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(f3 * 0.067f) * 0.05f;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(f3 * 0.067f) * 0.05f;
    }

    protected ModelZombie(float f, float f2, int n, int n2) {
        super(f, f2, n, n2);
    }

    public ModelZombie() {
        this(0.0f, false);
    }

    public ModelZombie(float f, boolean bl) {
        super(f, 0.0f, 64, bl ? 32 : 64);
    }
}

