// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.skeet.component.impl.sub.color;

import org.lwjgl.opengl.GL11;
import vip.Resolute.util.render.RenderUtils;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.Gui;
import vip.Resolute.ui.click.skeet.SkeetUI;
import vip.Resolute.ui.click.skeet.framework.Component;
import vip.Resolute.ui.click.skeet.framework.ExpandableComponent;
import vip.Resolute.ui.click.skeet.framework.PredicateComponent;
import vip.Resolute.ui.click.skeet.framework.ButtonComponent;

public abstract class ColorPickerComponent extends ButtonComponent implements PredicateComponent, ExpandableComponent
{
    private static final int MARGIN = 3;
    private static final int SLIDER_THICKNESS = 8;
    private static final float SELECTOR_WIDTH = 1.0f;
    private static final float HALF_WIDTH = 0.5f;
    private static final float OUTLINE_WIDTH = 0.5f;
    private boolean expanded;
    private float hue;
    private float saturation;
    private float brightness;
    private float alpha;
    private boolean colorSelectorDragging;
    private boolean hueSelectorDragging;
    private boolean alphaSelectorDragging;
    
    public ColorPickerComponent(final Component parent, final float x, final float y, final float width, final float height) {
        super(parent, x, y, width, height);
    }
    
    private static void drawCheckeredBackground(final float x, float y, final float x2, final float y2) {
        Gui.drawRect(x, y, x2, y2, SkeetUI.getColor(16777215));
        boolean offset = false;
        while (y < y2) {
            for (float x3 = x + ((offset = !offset) ? 1 : 0); x3 < x2; x3 += 2.0f) {
                if (x3 <= x2 - 1.0f) {
                    Gui.drawRect(x3, y, x3 + 1.0f, y + 1.0f, SkeetUI.getColor(8421504));
                }
            }
            ++y;
        }
    }
    
    @Override
    public void drawComponent(final ScaledResolution lockedResolution, final int mouseX, final int mouseY) {
        final float x = this.getX();
        final float y = this.getY();
        final float width = this.getWidth();
        final float height = this.getHeight();
        final int black = SkeetUI.getColor(0);
        Gui.drawRect(x - 0.5, y - 0.5, x + width + 0.5, y + height + 0.5, black);
        final int guiAlpha = (int)SkeetUI.getAlpha();
        final int color = this.getColor();
        final int colorAlpha = color >> 24 & 0xFF;
        final int minAlpha = Math.min(guiAlpha, colorAlpha);
        if (colorAlpha < 255) {
            drawCheckeredBackground(x, y, x + width, y + height);
        }
        final int newColor = new Color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, minAlpha).getRGB();
        Gui.drawGradientRect(x, y, x + width, y + height, newColor, RenderUtils.darker(newColor));
        if (this.isExpanded()) {
            GL11.glTranslated(0.0, 0.0, 3.0);
            final float expandedX = this.getExpandedX();
            final float expandedY = this.getExpandedY();
            final float expandedWidth = this.getExpandedWidth();
            final float expandedHeight = this.getExpandedHeight();
            Gui.drawRect(expandedX, expandedY, expandedX + expandedWidth, expandedY + expandedHeight, black);
            Gui.drawRect(expandedX + 0.5, expandedY + 0.5, expandedX + expandedWidth - 0.5, expandedY + expandedHeight - 0.5, SkeetUI.getColor(3750203));
            Gui.drawRect(expandedX + 1.0f, expandedY + 1.0f, expandedX + expandedWidth - 1.0f, expandedY + expandedHeight - 1.0f, SkeetUI.getColor(2302755));
            final float colorPickerSize = expandedWidth - 9.0f - 8.0f;
            final float colorPickerLeft = expandedX + 3.0f;
            final float colorPickerTop = expandedY + 3.0f;
            final float colorPickerRight = colorPickerLeft + colorPickerSize;
            final float colorPickerBottom = colorPickerTop + colorPickerSize;
            final int selectorWhiteOverlayColor = new Color(255, 255, 255, Math.min(guiAlpha, 180)).getRGB();
            if (mouseX <= colorPickerLeft || mouseY <= colorPickerTop || mouseX >= colorPickerRight || mouseY >= colorPickerBottom) {
                this.colorSelectorDragging = false;
            }
            Gui.drawRect(colorPickerLeft - 0.5, colorPickerTop - 0.5, colorPickerRight + 0.5, colorPickerBottom + 0.5, SkeetUI.getColor(0));
            this.drawColorPickerRect(colorPickerLeft, colorPickerTop, colorPickerRight, colorPickerBottom);
            float hueSliderLeft = this.saturation * (colorPickerRight - colorPickerLeft);
            float alphaSliderTop = (1.0f - this.brightness) * (colorPickerBottom - colorPickerTop);
            if (this.colorSelectorDragging) {
                final float hueSliderRight = colorPickerRight - colorPickerLeft;
                final float alphaSliderBottom = mouseX - colorPickerLeft;
                this.saturation = alphaSliderBottom / hueSliderRight;
                hueSliderLeft = alphaSliderBottom;
                final float hueSliderYDif = colorPickerBottom - colorPickerTop;
                final float hueSelectorY = mouseY - colorPickerTop;
                this.brightness = 1.0f - hueSelectorY / hueSliderYDif;
                alphaSliderTop = hueSelectorY;
                this.updateColor(Color.getHSBColor(this.hue, this.saturation, this.brightness), false);
            }
            float hueSliderRight = colorPickerLeft + hueSliderLeft - 0.5f;
            float alphaSliderBottom = colorPickerTop + alphaSliderTop - 0.5f;
            float hueSliderYDif = colorPickerLeft + hueSliderLeft + 0.5f;
            float hueSelectorY = colorPickerTop + alphaSliderTop + 0.5f;
            Gui.drawRect(hueSliderRight - 0.5f, alphaSliderBottom - 0.5f, hueSliderRight, hueSelectorY + 0.5f, black);
            Gui.drawRect(hueSliderYDif, alphaSliderBottom - 0.5f, hueSliderYDif + 0.5f, hueSelectorY + 0.5f, black);
            Gui.drawRect(hueSliderRight, alphaSliderBottom - 0.5f, hueSliderYDif, alphaSliderBottom, black);
            Gui.drawRect(hueSliderRight, hueSelectorY, hueSliderYDif, hueSelectorY + 0.5f, black);
            Gui.drawRect(hueSliderRight, alphaSliderBottom, hueSliderYDif, hueSelectorY, selectorWhiteOverlayColor);
            hueSliderLeft = colorPickerRight + 3.0f;
            hueSliderRight = hueSliderLeft + 8.0f;
            if (mouseX <= hueSliderLeft || mouseY <= colorPickerTop || mouseX >= hueSliderRight || mouseY >= colorPickerBottom) {
                this.hueSelectorDragging = false;
            }
            hueSliderYDif = colorPickerBottom - colorPickerTop;
            hueSelectorY = (1.0f - this.hue) * hueSliderYDif;
            if (this.hueSelectorDragging) {
                final float inc = mouseY - colorPickerTop;
                this.hue = 1.0f - inc / hueSliderYDif;
                hueSelectorY = inc;
                this.updateColor(Color.getHSBColor(this.hue, this.saturation, this.brightness), false);
            }
            Gui.drawRect(hueSliderLeft - 0.5, colorPickerTop - 0.5, hueSliderRight + 0.5, colorPickerBottom + 0.5, black);
            final float inc = 0.2f;
            final float times = 5.0f;
            float hsHeight = colorPickerBottom - colorPickerTop;
            float alphaSelectorX = hsHeight / 5.0f;
            float asLeft = colorPickerTop;
            for (int i = 0; i < 5.0f; ++i) {
                final boolean last = i == 4.0f;
                Gui.drawGradientRect(hueSliderLeft, asLeft, hueSliderRight, asLeft + alphaSelectorX, SkeetUI.getColor(Color.HSBtoRGB(1.0f - 0.2f * i, 1.0f, 1.0f)), SkeetUI.getColor(Color.HSBtoRGB(1.0f - 0.2f * (i + 1), 1.0f, 1.0f)));
                if (!last) {
                    asLeft += alphaSelectorX;
                }
            }
            final float hsTop = colorPickerTop + hueSelectorY - 0.5f;
            float asRight = colorPickerTop + hueSelectorY + 0.5f;
            Gui.drawRect(hueSliderLeft - 0.5f, hsTop - 0.5f, hueSliderLeft, asRight + 0.5f, black);
            Gui.drawRect(hueSliderRight, hsTop - 0.5f, hueSliderRight + 0.5f, asRight + 0.5f, black);
            Gui.drawRect(hueSliderLeft, hsTop - 0.5f, hueSliderRight, hsTop, black);
            Gui.drawRect(hueSliderLeft, asRight, hueSliderRight, asRight + 0.5f, black);
            Gui.drawRect(hueSliderLeft, hsTop, hueSliderRight, asRight, selectorWhiteOverlayColor);
            alphaSliderTop = colorPickerBottom + 3.0f;
            alphaSliderBottom = alphaSliderTop + 8.0f;
            if (mouseX <= colorPickerLeft || mouseY <= alphaSliderTop || mouseX >= colorPickerRight || mouseY >= alphaSliderBottom) {
                this.alphaSelectorDragging = false;
            }
            final int color2 = Color.HSBtoRGB(this.hue, this.saturation, this.brightness);
            final int r = color2 >> 16 & 0xFF;
            final int g = color2 >> 8 & 0xFF;
            final int b = color2 & 0xFF;
            hsHeight = colorPickerRight - colorPickerLeft;
            alphaSelectorX = this.alpha * hsHeight;
            if (this.alphaSelectorDragging) {
                asLeft = mouseX - colorPickerLeft;
                this.alpha = asLeft / hsHeight;
                alphaSelectorX = asLeft;
                this.updateColor(new Color(r, g, b, (int)(this.alpha * 255.0f)), true);
            }
            Gui.drawRect(colorPickerLeft - 0.5, alphaSliderTop - 0.5, colorPickerRight + 0.5, alphaSliderBottom + 0.5, black);
            drawCheckeredBackground(colorPickerLeft, alphaSliderTop, colorPickerRight, alphaSliderBottom);
            RenderUtils.drawGradientRect(colorPickerLeft, alphaSliderTop, colorPickerRight, alphaSliderBottom, true, new Color(r, g, b, 0).getRGB(), new Color(r, g, b, Math.min(guiAlpha, 255)).getRGB());
            asLeft = colorPickerLeft + alphaSelectorX - 0.5f;
            asRight = colorPickerLeft + alphaSelectorX + 0.5f;
            Gui.drawRect(asLeft - 0.5f, alphaSliderTop, asRight + 0.5f, alphaSliderBottom, black);
            Gui.drawRect(asLeft, alphaSliderTop, asRight, alphaSliderBottom, selectorWhiteOverlayColor);
            GL11.glTranslated(0.0, 0.0, -3.0);
        }
    }
    
    @Override
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        super.onMouseClick(mouseX, mouseY, button);
        if (this.isExpanded() && button == 0) {
            final float expandedX = this.getExpandedX();
            final float expandedY = this.getExpandedY();
            final float expandedWidth = this.getExpandedWidth();
            final float expandedHeight = this.getExpandedHeight();
            final float colorPickerSize = expandedWidth - 9.0f - 8.0f;
            final float colorPickerLeft = expandedX + 3.0f;
            final float colorPickerTop = expandedY + 3.0f;
            final float colorPickerRight = colorPickerLeft + colorPickerSize;
            final float colorPickerBottom = colorPickerTop + colorPickerSize;
            final float alphaSliderTop = colorPickerBottom + 3.0f;
            final float alphaSliderBottom = alphaSliderTop + 8.0f;
            final float hueSliderLeft = colorPickerRight + 3.0f;
            final float hueSliderRight = hueSliderLeft + 8.0f;
            this.colorSelectorDragging = (!this.colorSelectorDragging && mouseX > colorPickerLeft && mouseY > colorPickerTop && mouseX < colorPickerRight && mouseY < colorPickerBottom);
            this.alphaSelectorDragging = (!this.alphaSelectorDragging && mouseX > colorPickerLeft && mouseY > alphaSliderTop && mouseX < colorPickerRight && mouseY < alphaSliderBottom);
            this.hueSelectorDragging = (!this.hueSelectorDragging && mouseX > hueSliderLeft && mouseY > colorPickerTop && mouseX < hueSliderRight && mouseY < colorPickerBottom);
        }
    }
    
    @Override
    public void onMouseRelease(final int button) {
        if (this.colorSelectorDragging) {
            this.colorSelectorDragging = false;
        }
        if (this.alphaSelectorDragging) {
            this.alphaSelectorDragging = false;
        }
        if (this.hueSelectorDragging) {
            this.hueSelectorDragging = false;
        }
    }
    
    private void updateColor(final Color hex, final boolean hasAlpha) {
        if (hasAlpha) {
            this.setColor(hex);
        }
        else {
            this.setColor(new Color(hex.getRed(), hex.getGreen(), hex.getBlue(), (int)(this.alpha * 255.0f)));
        }
    }
    
    public abstract int getColor();
    
    public abstract void setColor(final Color p0);
    
    public void onValueChange(final int oldValue, final int value) {
        final float[] hsb = this.getHSBFromColor(value);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
        this.alpha = (value >> 24 & 0xFF) / 255.0f;
    }
    
    private float[] getHSBFromColor(final int hex) {
        final int r = hex >> 16 & 0xFF;
        final int g = hex >> 8 & 0xFF;
        final int b = hex & 0xFF;
        return Color.RGBtoHSB(r, g, b, null);
    }
    
    private void drawColorPickerRect(final float left, final float top, final float right, final float bottom) {
        final int hueBasedColor = SkeetUI.getColor(Color.HSBtoRGB(this.hue, 1.0f, 1.0f));
        RenderUtils.drawGradientRect(left, top, right, bottom, true, SkeetUI.getColor(16777215), hueBasedColor);
        Gui.drawGradientRect(left, top, right, bottom, 0, SkeetUI.getColor(0));
    }
    
    @Override
    public float getExpandedX() {
        return this.getX() + this.getWidth() - 80.333336f;
    }
    
    @Override
    public float getExpandedY() {
        return this.getY() + this.getHeight();
    }
    
    @Override
    public boolean isExpanded() {
        return this.expanded;
    }
    
    @Override
    public void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }
    
    @Override
    public void onPress(final int mouseButton) {
        if (mouseButton == 1) {
            this.setExpanded(!this.isExpanded());
        }
    }
    
    @Override
    public float getExpandedWidth() {
        final float right = this.getX() + this.getWidth();
        return right - this.getExpandedX();
    }
    
    @Override
    public float getExpandedHeight() {
        return this.getExpandedWidth();
    }
}
