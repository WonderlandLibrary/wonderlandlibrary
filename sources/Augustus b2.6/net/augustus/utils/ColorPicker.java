// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.utils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.client.gui.ScaledResolution;
import java.awt.Color;
import java.util.function.Consumer;
import net.augustus.utils.interfaces.MC;

public class ColorPicker implements MC
{
    private final double radius;
    private final Consumer<Color> color;
    private double selectedX;
    private double selectedY;
    private float brightness;
    private float alpha;
    private ScaledResolution sr;
    
    public ColorPicker(final double radius, final Color selectedColor, final Consumer<Color> color) {
        this.brightness = 1.0f;
        this.alpha = 1.0f;
        this.radius = radius;
        this.alpha = selectedColor.getAlpha() / 255.0f;
        this.brightness = Color.RGBtoHSB(selectedColor.getRed(), selectedColor.getGreen(), selectedColor.getBlue(), null)[2];
        this.setColor(selectedColor);
        this.color = color;
    }
    
    public void click(final double mouseX, final double mouseY, final int button) {
        this.sr = new ScaledResolution(ColorPicker.mc);
        if (button == 0 && this.isPointInCircle(this.sr.getScaledWidth() / 2.0, this.sr.getScaledHeight() / 2.0, this.radius, mouseX, mouseY)) {
            this.selectedX = mouseX - this.sr.getScaledWidth() / 2.0;
            this.selectedY = mouseY - this.sr.getScaledHeight() / 2.0;
        }
        final int width = this.sr.getScaledWidth() / 2;
        final int height = this.sr.getScaledHeight() / 2;
        final double sliderX = width - 70;
        final double sliderWidth = 138.0;
        if (button == 0 && this.mouseOver(mouseX, mouseY, sliderX, height + 75, 140.0, 20.0)) {
            final double var1 = MathHelper.clamp_double((mouseX - sliderX) / sliderWidth, 0.0, 1.0) * 1.0;
            this.setBrightness((float)var1);
        }
        if (button == 0 && this.mouseOver(mouseX, mouseY, sliderX, height + 100, 140.0, 20.0)) {
            final double var1 = MathHelper.clamp_double((mouseX - sliderX) / sliderWidth, 0.0, 1.0) * 1.0;
            this.setAlpha((float)var1);
        }
    }
    
    public void draw(final int mouseX, final int mouseY) {
        this.sr = new ScaledResolution(ColorPicker.mc);
        final FontRenderer fr = ColorPicker.mc.fontRendererObj;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderUtil.drawColoredCircle(this.sr.getScaledWidth() / 2.0, this.sr.getScaledHeight() / 2.0, this.radius, this.getBrightness());
        RenderUtil.drawCircle(this.sr.getScaledWidth() / 2.0 + this.selectedX, this.sr.getScaledHeight() / 2.0 + this.selectedY, 2.5, -15592942);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        final int width = this.sr.getScaledWidth() / 2;
        final int height = this.sr.getScaledHeight() / 2;
        final double percentageOfCurrentValue = this.getBrightness();
        final double sliderX = width - 70;
        final double sliderWidth = 138.0;
        final double value1 = sliderX + 2.0 + percentageOfCurrentValue * sliderWidth;
        RenderUtil.drawRect(width - 70, height + 75, width + 70, height + 90, this.getColor().getRGB());
        RenderUtil.drawRect((int)(value1 - 2.0), height + 75, (int)value1, height + 90, Color.black.getRGB());
        final double percentageOfCurrentValue2 = this.getAlpha();
        final double value2 = sliderX + 2.0 + percentageOfCurrentValue2 * sliderWidth;
        RenderUtil.drawRect(width - 70, height + 100, width + 70, height + 115, this.getColor().getRGB());
        RenderUtil.drawRect((int)(value2 - 2.0), height + 100, (int)value2, height + 115, Color.black.getRGB());
        this.color.accept(this.getColor());
    }
    
    private float getNormalized() {
        return (float)((-Math.toDegrees(Math.atan2(this.selectedY, this.selectedX)) + 450.0) % 360.0) / 360.0f;
    }
    
    private Color getColor() {
        final Color color1 = Color.getHSBColor(this.getNormalized(), (float)(Math.hypot(this.selectedX, this.selectedY) / this.radius), this.getBrightness());
        return new Color(color1.getRed(), color1.getGreen(), color1.getBlue(), (int)(this.getAlpha() * 255.0f));
    }
    
    private void setColor(final Color selectedColor) {
        final float[] hsb = Color.RGBtoHSB(selectedColor.getRed(), selectedColor.getGreen(), selectedColor.getBlue(), null);
        this.selectedX = hsb[1] * this.radius * (Math.sin(Math.toRadians(hsb[0] * 360.0f)) / Math.sin(Math.toRadians(90.0)));
        this.selectedY = hsb[1] * this.radius * (Math.sin(Math.toRadians(90.0f - hsb[0] * 360.0f)) / Math.sin(Math.toRadians(90.0)));
    }
    
    private boolean isPointInCircle(final double x, final double y, final double radius, final double pX, final double pY) {
        return (pX - x) * (pX - x) + (pY - y) * (pY - y) <= radius * radius;
    }
    
    public boolean mouseOver(final double mouseX, final double mouseY, final double posX, final double posY, final double width, final double height) {
        return mouseX >= posX && mouseX <= posX + width && mouseY >= posY && mouseY <= posY + height;
    }
    
    public float getBrightness() {
        return this.brightness;
    }
    
    public void setBrightness(final float brightness) {
        this.brightness = brightness;
    }
    
    public float getAlpha() {
        return this.alpha;
    }
    
    public void setAlpha(final float alpha) {
        this.alpha = alpha;
    }
}
