/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.eyes;

import net.minecraft.client.eyes.cosmetic.CosmeticModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;

public class ModelEyes
extends CosmeticModelBase {
    private ModelRenderer[] iris = new ModelRenderer[12];
    private ModelRenderer[] pupils = new ModelRenderer[6];
    private final float POP1 = 0.5235988f;
    private final float POP2 = 1.0471976f;
    private final float POP3 = 1.5707964f;
    private final float POP4 = 2.0943952f;
    private final float POP5 = 2.6179938f;
    private final float POP6 = (float)Math.PI;

    public ModelEyes(RenderPlayer player) {
        super(player);
        int i;
        for (i = 0; i < this.iris.length; ++i) {
            this.iris[i] = new ModelRenderer(this, 0, 0);
            this.iris[i].setRotationPoint(0.0f, 0.0f, 0.0f);
            this.iris[i].addBox(-0.5f, -1.88f, -1.0f, 1, 3, 1, 0.0f);
        }
        this.setRotationAngle(this.iris[0], 0.0f, 0.0f, -0.5235988f);
        this.setRotationAngle(this.iris[1], 0.0f, 0.0f, -1.0471976f);
        this.setRotationAngle(this.iris[2], 0.0f, 0.0f, -1.5707964f);
        this.setRotationAngle(this.iris[3], 0.0f, 0.0f, -2.0943952f);
        this.setRotationAngle(this.iris[4], 0.0f, 0.0f, -2.6179938f);
        this.setRotationAngle(this.iris[5], 0.0f, 0.0f, (float)(-Math.PI));
        this.setRotationAngle(this.iris[6], 0.0f, 0.0f, 2.6179938f);
        this.setRotationAngle(this.iris[7], 0.0f, 0.0f, 2.0943952f);
        this.setRotationAngle(this.iris[8], 0.0f, 0.0f, 1.5707964f);
        this.setRotationAngle(this.iris[9], 0.0f, 0.0f, 1.0471976f);
        this.setRotationAngle(this.iris[10], 0.0f, 0.0f, 0.5235988f);
        for (i = 0; i < this.pupils.length; ++i) {
            this.pupils[i] = new ModelRenderer(this, 0, 0);
            this.pupils[i].setRotationPoint(0.0f, 0.0f, 0.0f);
            this.pupils[i].addBox(-0.5f, -0.88f, -1.5f, 1, 1, 1, 0.0f);
        }
        this.setRotationAngle(this.pupils[0], 0.0f, 0.0f, -1.0471976f);
        this.setRotationAngle(this.pupils[1], 0.0f, 0.0f, -2.0943952f);
        this.setRotationAngle(this.pupils[2], 0.0f, 0.0f, (float)(-Math.PI));
        this.setRotationAngle(this.pupils[3], 0.0f, 0.0f, 2.0943952f);
        this.setRotationAngle(this.pupils[4], 0.0f, 0.0f, 1.0471976f);
    }

    private void setRotationAngle(ModelRenderer m, float x, float y, float z) {
        m.rotateAngleX = x;
        m.rotateAngleY = y;
        m.rotateAngleZ = z;
    }

    @Override
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
    }

    public void renderIris(float pt) {
        for (int i = 0; i < this.iris.length; ++i) {
            this.iris[i].render(pt);
        }
    }

    public void renderPupil(float pt) {
        for (int i = 0; i < this.pupils.length; ++i) {
            this.pupils[i].render(pt);
        }
    }

    public void movePupil(float x, float y, float size) {
        float shiftFactor = (1.45f - size * 0.525f) / size;
        for (int i = 0; i < this.pupils.length; ++i) {
            this.pupils[i].rotationPointX = -x * shiftFactor;
            this.pupils[i].rotationPointY = -y * shiftFactor * (float)Math.cos(Math.toRadians(x / 1.0f * 90.0f));
        }
    }
}

