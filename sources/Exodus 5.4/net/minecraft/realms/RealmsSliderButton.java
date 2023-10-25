/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.realms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.realms.RealmsButton;
import net.minecraft.util.MathHelper;

public class RealmsSliderButton
extends RealmsButton {
    public boolean sliding;
    public float value = 1.0f;
    private int steps;
    private final float maxValue;
    private final float minValue;

    public RealmsSliderButton(int n, int n2, int n3, int n4, int n5, int n6, float f, float f2) {
        super(n, n2, n3, n4, 20, "");
        this.minValue = f;
        this.maxValue = f2;
        this.value = this.toPct(n6);
        this.getProxy().displayString = this.getMessage();
    }

    public String getMessage() {
        return "";
    }

    @Override
    public void clicked(int n, int n2) {
        this.value = (float)(n - (this.getProxy().xPosition + 4)) / (float)(this.getProxy().getButtonWidth() - 8);
        this.value = MathHelper.clamp_float(this.value, 0.0f, 1.0f);
        this.clicked(this.toValue(this.value));
        this.getProxy().displayString = this.getMessage();
        this.sliding = true;
    }

    protected float clampSteps(float f) {
        if (this.steps > 0) {
            f = this.steps * Math.round(f / (float)this.steps);
        }
        return f;
    }

    public float toValue(float f) {
        return this.clamp(this.minValue + (this.maxValue - this.minValue) * MathHelper.clamp_float(f, 0.0f, 1.0f));
    }

    public float clamp(float f) {
        f = this.clampSteps(f);
        return MathHelper.clamp_float(f, this.minValue, this.maxValue);
    }

    @Override
    public int getYImage(boolean bl) {
        return 0;
    }

    public RealmsSliderButton(int n, int n2, int n3, int n4, int n5, int n6) {
        this(n, n2, n3, n4, n6, 0, 1.0f, n5);
    }

    @Override
    public void renderBg(int n, int n2) {
        if (this.getProxy().visible) {
            if (this.sliding) {
                this.value = (float)(n - (this.getProxy().xPosition + 4)) / (float)(this.getProxy().getButtonWidth() - 8);
                this.value = MathHelper.clamp_float(this.value, 0.0f, 1.0f);
                float f = this.toValue(this.value);
                this.clicked(f);
                this.value = this.toPct(f);
                this.getProxy().displayString = this.getMessage();
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(WIDGETS_LOCATION);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.blit(this.getProxy().xPosition + (int)(this.value * (float)(this.getProxy().getButtonWidth() - 8)), this.getProxy().yPosition, 0, 66, 4, 20);
            this.blit(this.getProxy().xPosition + (int)(this.value * (float)(this.getProxy().getButtonWidth() - 8)) + 4, this.getProxy().yPosition, 196, 66, 4, 20);
        }
    }

    public float toPct(float f) {
        return MathHelper.clamp_float((this.clamp(f) - this.minValue) / (this.maxValue - this.minValue), 0.0f, 1.0f);
    }

    @Override
    public void released(int n, int n2) {
        this.sliding = false;
    }

    public void clicked(float f) {
    }
}

