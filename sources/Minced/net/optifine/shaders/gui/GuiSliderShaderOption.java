// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import net.optifine.shaders.config.ShaderOption;

public class GuiSliderShaderOption extends GuiButtonShaderOption
{
    private float sliderValue;
    public boolean dragging;
    private ShaderOption shaderOption;
    
    public GuiSliderShaderOption(final int buttonId, final int x, final int y, final int w, final int h, final ShaderOption shaderOption, final String text) {
        super(buttonId, x, y, w, h, shaderOption, text);
        this.sliderValue = 1.0f;
        this.shaderOption = null;
        this.shaderOption = shaderOption;
        this.sliderValue = shaderOption.getIndexNormalized();
        this.displayString = GuiShaderOptions.getButtonText(shaderOption, this.width);
    }
    
    @Override
    protected int getHoverState(final boolean mouseOver) {
        return 0;
    }
    
    @Override
    protected void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            if (this.dragging && !GuiScreen.isShiftKeyDown()) {
                this.sliderValue = (mouseX - (this.x + 4)) / (float)(this.width - 8);
                this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0f, 1.0f);
                this.shaderOption.setIndexNormalized(this.sliderValue);
                this.sliderValue = this.shaderOption.getIndexNormalized();
                this.displayString = GuiShaderOptions.getButtonText(this.shaderOption, this.width);
            }
            mc.getTextureManager().bindTexture(GuiSliderShaderOption.BUTTON_TEXTURES);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.drawTexturedModalRect(this.x + (int)(this.sliderValue * (this.width - 8)), this.y, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.x + (int)(this.sliderValue * (this.width - 8)) + 4, this.y, 196, 66, 4, 20);
        }
    }
    
    @Override
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.sliderValue = (mouseX - (this.x + 4)) / (float)(this.width - 8);
            this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0f, 1.0f);
            this.shaderOption.setIndexNormalized(this.sliderValue);
            this.displayString = GuiShaderOptions.getButtonText(this.shaderOption, this.width);
            return this.dragging = true;
        }
        return false;
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY) {
        this.dragging = false;
    }
    
    @Override
    public void valueChanged() {
        this.sliderValue = this.shaderOption.getIndexNormalized();
    }
    
    @Override
    public boolean isSwitchable() {
        return false;
    }
}
