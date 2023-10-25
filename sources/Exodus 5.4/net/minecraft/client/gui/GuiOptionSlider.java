/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MathHelper;

public class GuiOptionSlider
extends GuiButton {
    private final float field_146132_r;
    public boolean dragging;
    private float sliderValue = 1.0f;
    private GameSettings.Options options;
    private final float field_146131_s;

    @Override
    public boolean mousePressed(Minecraft minecraft, int n, int n2) {
        if (super.mousePressed(minecraft, n, n2)) {
            this.sliderValue = (float)(n - (this.xPosition + 4)) / (float)(this.width - 8);
            this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0f, 1.0f);
            Minecraft.gameSettings.setOptionFloatValue(this.options, this.options.denormalizeValue(this.sliderValue));
            this.displayString = Minecraft.gameSettings.getKeyBinding(this.options);
            this.dragging = true;
            return true;
        }
        return false;
    }

    @Override
    protected int getHoverState(boolean bl) {
        return 0;
    }

    public GuiOptionSlider(int n, int n2, int n3, GameSettings.Options options) {
        this(n, n2, n3, options, 0.0f, 1.0f);
    }

    @Override
    public void mouseReleased(int n, int n2) {
        this.dragging = false;
    }

    @Override
    protected void mouseDragged(Minecraft minecraft, int n, int n2) {
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = (float)(n - (this.xPosition + 4)) / (float)(this.width - 8);
                this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0f, 1.0f);
                float f = this.options.denormalizeValue(this.sliderValue);
                Minecraft.gameSettings.setOptionFloatValue(this.options, f);
                this.sliderValue = this.options.normalizeValue(f);
                this.displayString = Minecraft.gameSettings.getKeyBinding(this.options);
            }
            minecraft.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    public GuiOptionSlider(int n, int n2, int n3, GameSettings.Options options, float f, float f2) {
        super(n, n2, n3, 150, 20, "");
        this.options = options;
        this.field_146132_r = f;
        this.field_146131_s = f2;
        Minecraft minecraft = Minecraft.getMinecraft();
        this.sliderValue = options.normalizeValue(Minecraft.gameSettings.getOptionFloatValue(options));
        this.displayString = Minecraft.gameSettings.getKeyBinding(options);
    }
}

