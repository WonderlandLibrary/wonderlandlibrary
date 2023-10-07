/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Settings;

import java.awt.Color;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Downward;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.ModuleRender;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.OtcClickGUi;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtilsFlux;
import net.ccbluex.liquidbounce.value.ColorValue;
import org.lwjgl.opengl.GL11;

public class ColorSetting
extends Downward<ColorValue> {
    public ColorValue colorValue;
    private float hue;
    private float saturation;
    private float brightness;
    private float alpha;
    private boolean colorSelectorDragging;
    private boolean hueSelectorDragging;
    private boolean alphaSelectorDragging;
    private float modulex;
    private float moduley;
    private float colory;

    public ColorSetting(ColorValue s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
        this.colorValue = s;
        this.updateValue((Integer)s.get());
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        this.modulex = OtcClickGUi.getMainx();
        this.moduley = OtcClickGUi.getMainy();
        this.colory = this.pos.y + (float)this.getScrollY();
        float x2 = this.modulex + 5.0f + this.pos.x + 115.0f;
        float y2 = this.moduley + 17.0f + this.colory + 10.0f;
        float width = 11.0f;
        float height = 5.0f;
        Fonts.fontTahoma.drawString(this.colorValue.getName(), this.modulex + 5.0f + this.pos.x + 4.0f, this.moduley + 17.0f + this.colory + 12.0f, new Color(200, 200, 200).getRGB());
        int black = RenderUtilsFlux.getColor(0);
        RenderUtilsFlux.drawRect(x2 - 0.5f, y2 - 0.5f, x2 + 11.0f + 0.5f, y2 + 5.0f + 0.5f, black);
        int guiAlpha = 255;
        int color = (Integer)this.colorValue.get();
        int colorAlpha = color >> 24 & 0xFF;
        int minAlpha = Math.min(255, colorAlpha);
        if (colorAlpha < 255) {
            RenderUtilsFlux.drawCheckeredBackground(x2, y2, x2 + 11.0f, y2 + 5.0f);
        }
        int newColor = new Color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, minAlpha).getRGB();
        RenderUtilsFlux.drawGradientRect(x2, y2, x2 + 11.0f, y2 + 5.0f, newColor, RenderUtilsFlux.darker(newColor, 0.6f));
        if (this.colorValue.isExpanded()) {
            float hueSelectorY;
            float hueSliderYDif;
            float alphaSliderBottom;
            float hueSliderRight;
            GL11.glTranslated((double)0.0, (double)0.0, (double)3.0);
            float expandedX = this.getExpandedX();
            float expandedY = this.getExpandedY();
            float expandedWidth = this.getExpandedWidth();
            float expandedHeight = this.getExpandedHeight();
            RenderUtilsFlux.drawBorderedRect(expandedX, expandedY, expandedX + expandedWidth, expandedY + expandedHeight + 70.0f, 1.0f, new Color(0, 0, 0, 0).getRGB(), new Color(85, 90, 96).getRGB());
            float colorPickerSize = expandedWidth - 9.0f - 8.0f;
            float colorPickerLeft = expandedX + 3.0f;
            float colorPickerTop = expandedY + 3.0f;
            float colorPickerRight = colorPickerLeft + colorPickerSize;
            float colorPickerBottom = colorPickerTop + colorPickerSize;
            int selectorWhiteOverlayColor = new Color(255, 255, 255, Math.min(255, 180)).getRGB();
            if ((float)mouseX <= colorPickerLeft || (float)mouseY <= colorPickerTop || (float)mouseX >= colorPickerRight || (float)mouseY >= colorPickerBottom) {
                this.colorSelectorDragging = false;
            }
            RenderUtilsFlux.drawRect(colorPickerLeft - 0.5f, colorPickerTop - 0.5f, colorPickerRight + 0.5f, colorPickerBottom + 0.5f, RenderUtilsFlux.getColor(0));
            this.drawColorPickerRect(colorPickerLeft, colorPickerTop, colorPickerRight, colorPickerBottom);
            float hueSliderLeft = this.saturation * (colorPickerRight - colorPickerLeft);
            float alphaSliderTop = (1.0f - this.brightness) * (colorPickerBottom - colorPickerTop);
            if (this.colorSelectorDragging) {
                hueSliderRight = colorPickerRight - colorPickerLeft;
                alphaSliderBottom = (float)mouseX - colorPickerLeft;
                this.saturation = alphaSliderBottom / hueSliderRight;
                hueSliderLeft = alphaSliderBottom;
                hueSliderYDif = colorPickerBottom - colorPickerTop;
                hueSelectorY = (float)mouseY - colorPickerTop;
                this.brightness = 1.0f - hueSelectorY / hueSliderYDif;
                alphaSliderTop = hueSelectorY;
                this.updateColor(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), false);
            }
            hueSliderRight = colorPickerLeft + hueSliderLeft - 0.5f;
            alphaSliderBottom = colorPickerTop + alphaSliderTop - 0.5f;
            hueSliderYDif = colorPickerLeft + hueSliderLeft + 0.5f;
            hueSelectorY = colorPickerTop + alphaSliderTop + 0.5f;
            RenderUtilsFlux.drawRect(hueSliderRight - 0.5f, alphaSliderBottom - 0.5f, hueSliderRight, hueSelectorY + 0.5f, black);
            RenderUtilsFlux.drawRect(hueSliderYDif, alphaSliderBottom - 0.5f, hueSliderYDif + 0.5f, hueSelectorY + 0.5f, black);
            RenderUtilsFlux.drawRect(hueSliderRight, alphaSliderBottom - 0.5f, hueSliderYDif, alphaSliderBottom, black);
            RenderUtilsFlux.drawRect(hueSliderRight, hueSelectorY, hueSliderYDif, hueSelectorY + 0.5f, black);
            RenderUtilsFlux.drawRect(hueSliderRight, alphaSliderBottom, hueSliderYDif, hueSelectorY, selectorWhiteOverlayColor);
            hueSliderLeft = colorPickerRight + 3.0f;
            hueSliderRight = hueSliderLeft + 8.0f;
            if ((float)mouseX <= hueSliderLeft || (float)mouseY <= colorPickerTop || (float)mouseX >= hueSliderRight || (float)mouseY >= colorPickerBottom) {
                this.hueSelectorDragging = false;
            }
            hueSliderYDif = colorPickerBottom - colorPickerTop;
            hueSelectorY = (1.0f - this.hue) * hueSliderYDif;
            if (this.hueSelectorDragging) {
                float inc = (float)mouseY - colorPickerTop;
                this.hue = 1.0f - inc / hueSliderYDif;
                hueSelectorY = inc;
                this.updateColor(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), false);
            }
            RenderUtilsFlux.drawRect(hueSliderLeft - 0.5f, colorPickerTop - 0.5f, hueSliderRight + 0.5f, colorPickerBottom + 0.5f, black);
            float hsHeight = colorPickerBottom - colorPickerTop;
            float alphaSelectorX = hsHeight / 5.0f;
            float asLeft = colorPickerTop;
            int i2 = 0;
            while ((float)i2 < 5.0f) {
                boolean last = (float)i2 == 4.0f;
                RenderUtilsFlux.drawGradientRect(hueSliderLeft, asLeft, hueSliderRight, asLeft + alphaSelectorX, RenderUtilsFlux.getColor(Color.HSBtoRGB(1.0f - 0.2f * (float)i2, 1.0f, 1.0f)), RenderUtilsFlux.getColor(Color.HSBtoRGB(1.0f - 0.2f * (float)(i2 + 1), 1.0f, 1.0f)));
                if (!last) {
                    asLeft += alphaSelectorX;
                }
                ++i2;
            }
            float hsTop = colorPickerTop + hueSelectorY - 0.5f;
            float asRight = colorPickerTop + hueSelectorY + 0.5f;
            RenderUtilsFlux.drawRect(hueSliderLeft - 0.5f, hsTop - 0.5f, hueSliderLeft, asRight + 0.5f, black);
            RenderUtilsFlux.drawRect(hueSliderRight, hsTop - 0.5f, hueSliderRight + 0.5f, asRight + 0.5f, black);
            RenderUtilsFlux.drawRect(hueSliderLeft, hsTop - 0.5f, hueSliderRight, hsTop, black);
            RenderUtilsFlux.drawRect(hueSliderLeft, asRight, hueSliderRight, asRight + 0.5f, black);
            RenderUtilsFlux.drawRect(hueSliderLeft, hsTop, hueSliderRight, asRight, selectorWhiteOverlayColor);
            alphaSliderTop = colorPickerBottom + 3.0f;
            alphaSliderBottom = alphaSliderTop + 8.0f;
            if ((float)mouseX <= colorPickerLeft || (float)mouseY <= alphaSliderTop || (float)mouseX >= colorPickerRight || (float)mouseY >= alphaSliderBottom) {
                this.alphaSelectorDragging = false;
            }
            int z2 = Color.HSBtoRGB(this.hue, this.saturation, this.brightness);
            int r2 = z2 >> 16 & 0xFF;
            int g2 = z2 >> 8 & 0xFF;
            int b2 = z2 & 0xFF;
            hsHeight = colorPickerRight - colorPickerLeft;
            alphaSelectorX = this.alpha * hsHeight;
            if (this.alphaSelectorDragging) {
                asLeft = (float)mouseX - colorPickerLeft;
                this.alpha = asLeft / hsHeight;
                alphaSelectorX = asLeft;
                this.updateColor(new Color(r2, g2, b2, (int)(this.alpha * 255.0f)).getRGB(), true);
            }
            RenderUtilsFlux.drawRect(colorPickerLeft - 0.5f, alphaSliderTop - 0.5f, colorPickerRight + 0.5f, alphaSliderBottom + 0.5f, black);
            RenderUtilsFlux.drawCheckeredBackground(colorPickerLeft, alphaSliderTop, colorPickerRight, alphaSliderBottom);
            RenderUtilsFlux.drawGradientRect(colorPickerLeft, alphaSliderTop, colorPickerRight, alphaSliderBottom, true, new Color(r2, g2, b2, 0).getRGB(), new Color(r2, g2, b2, Math.min(255, 255)).getRGB());
            asLeft = colorPickerLeft + alphaSelectorX - 0.5f;
            asRight = colorPickerLeft + alphaSelectorX + 0.5f;
            RenderUtilsFlux.drawRect(asLeft - 0.5f, alphaSliderTop, asRight + 0.5f, alphaSliderBottom, black);
            RenderUtilsFlux.drawRect(asLeft, alphaSliderTop, asRight, alphaSliderBottom, selectorWhiteOverlayColor);
            GL11.glTranslated((double)0.0, (double)0.0, (double)-3.0);
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (float)mouseX >= this.modulex + 5.0f + this.pos.x + 115.0f && (float)mouseX <= this.modulex + 5.0f + this.pos.x + 115.0f + 13.0f && (float)mouseY >= this.moduley + 17.0f + this.colory + 10.0f && (double)mouseY <= (double)(this.moduley + 17.0f + this.colory + 10.0f) - 0.5 + 8.0;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 1 && this.isHovered(mouseX, mouseY)) {
            this.colorValue.setExpanded(!this.colorValue.isExpanded());
        }
        if (this.colorValue.isExpanded() && mouseButton == 0) {
            float expandedX = this.getExpandedX();
            float expandedY = this.getExpandedY();
            float expandedWidth = this.getExpandedWidth();
            float expandedHeight = this.getExpandedHeight();
            float colorPickerSize = expandedWidth - 9.0f - 8.0f;
            float colorPickerLeft = expandedX + 3.0f;
            float colorPickerTop = expandedY + 3.0f;
            float colorPickerRight = colorPickerLeft + colorPickerSize;
            float colorPickerBottom = colorPickerTop + colorPickerSize;
            float alphaSliderTop = colorPickerBottom + 3.0f;
            float alphaSliderBottom = alphaSliderTop + 8.0f;
            float hueSliderLeft = colorPickerRight + 3.0f;
            float hueSliderRight = hueSliderLeft + 8.0f;
            this.colorSelectorDragging = !this.colorSelectorDragging && (float)mouseX > colorPickerLeft && (float)mouseY > colorPickerTop && (float)mouseX < colorPickerRight && (float)mouseY < colorPickerBottom;
            this.alphaSelectorDragging = !this.alphaSelectorDragging && (float)mouseX > colorPickerLeft && (float)mouseY > alphaSliderTop && (float)mouseX < colorPickerRight && (float)mouseY < alphaSliderBottom;
            this.hueSelectorDragging = !this.hueSelectorDragging && (float)mouseX > hueSliderLeft && (float)mouseY > colorPickerTop && (float)mouseX < hueSliderRight && (float)mouseY < colorPickerBottom;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.hueSelectorDragging) {
            this.hueSelectorDragging = false;
        } else if (this.colorSelectorDragging) {
            this.colorSelectorDragging = false;
        } else if (this.alphaSelectorDragging) {
            this.alphaSelectorDragging = false;
        }
    }

    public void updateColor(int hex, boolean hasAlpha) {
        if (hasAlpha) {
            this.colorValue.set(hex);
        } else {
            this.colorValue.set(new Color(hex >> 16 & 0xFF, hex >> 8 & 0xFF, hex & 0xFF, (int)(this.alpha * 255.0f)).getRGB());
        }
    }

    private void drawColorPickerRect(float left, float top, float right, float bottom) {
        int hueBasedColor = RenderUtilsFlux.getColor(Color.HSBtoRGB(this.hue, 1.0f, 1.0f));
        RenderUtilsFlux.drawGradientRect(left, top, right, bottom, true, RenderUtilsFlux.getColor(0xFFFFFF), hueBasedColor);
        RenderUtilsFlux.drawGradientRect(left, top, right, bottom, 0, RenderUtilsFlux.getColor(0));
    }

    public void updateValue(int value) {
        float[] hsb = this.getHSBFromColor(value);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
        this.alpha = (float)(value >> 24 & 0xFF) / 255.0f;
    }

    private float[] getHSBFromColor(int hex) {
        int r2 = hex >> 16 & 0xFF;
        int g2 = hex >> 8 & 0xFF;
        int b2 = hex & 0xFF;
        return Color.RGBtoHSB(r2, g2, b2, null);
    }

    public float getExpandedX() {
        return this.modulex + 5.0f + this.pos.x + 115.0f + 11.0f - 80.333336f;
    }

    public float getExpandedY() {
        return this.moduley + 17.0f + this.colory + 10.0f + 5.0f;
    }

    public float getExpandedWidth() {
        float right = this.modulex + 5.0f + this.pos.x + 115.0f + 11.0f;
        return right - this.getExpandedX();
    }

    public float getExpandedHeight() {
        return 11.0f;
    }
}

