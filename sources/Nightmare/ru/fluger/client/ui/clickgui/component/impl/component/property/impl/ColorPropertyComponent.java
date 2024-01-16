// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.clickgui.component.impl.component.property.impl;

import ru.fluger.client.settings.Setting;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.ui.clickgui.ClickGuiScreen;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.feature.impl.hud.ClickGui;
import ru.fluger.client.helpers.render.rect.RectHelper;
import java.awt.Color;
import ru.fluger.client.ui.clickgui.component.Component;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.ui.clickgui.component.impl.component.property.PropertyComponent;
import ru.fluger.client.ui.clickgui.component.impl.ExpandableComponent;

public class ColorPropertyComponent extends ExpandableComponent implements PropertyComponent
{
    private static final int COLOR_PICKER_HEIGHT = 80;
    public static bve tessellator;
    public static buk buffer;
    private final ColorSetting colorProperty;
    private float hue;
    private float saturation;
    private float brightness;
    private float alpha;
    private boolean colorSelectorDragging;
    private boolean hueSelectorDragging;
    private boolean alphaSelectorDragging;
    
    public ColorPropertyComponent(final Component parent, final ColorSetting colorProperty, final float x, final float y, final float width, final float height) {
        super(parent, colorProperty.getName(), x, y, width, height);
        this.colorProperty = colorProperty;
        final int value = colorProperty.getColor();
        final float[] hsb = this.getHSBFromColor(value);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
        this.alpha = (value >> 24 & 0xFF) / 255.0f;
    }
    
    @Override
    public void drawComponent(final bit scaledResolution, final int mouseX, final int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        final float x = (float)this.getX();
        final float y = (float)this.getY();
        final float width = this.getWidth();
        final float height = this.getHeight();
        final int textColor = 16777215;
        RectHelper.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 0).getRGB());
        RectHelper.drawGradientRect(x, y, x + width, y + height, RenderHelper.injectAlpha(new Color(ClickGui.color.getColor()).darker().darker().darker().darker().darker().darker(), 120).getRGB(), RenderHelper.injectAlpha(new Color(ClickGui.color.getColor()).darker(), 120).getRGB());
        ColorPropertyComponent.mc.fontRenderer.drawStringWithShadow(this.getName(), x + 2.0f, y + height / 2.0f - 3.0f, textColor);
        final float left = x + width - 13.0f;
        final float top = y + height / 2.0f - 2.0f;
        final float right = x + width - 2.0f;
        final float bottom = y + height / 2.0f + 2.0f;
        if (ClickGui.glow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(new Color(this.colorProperty.getColor()).brighter(), (int)left, (int)top, 12.0, 3.0, 10);
        }
        RectHelper.drawRect(left, top, right, bottom, this.colorProperty.getColor());
        RectHelper.drawSmoothRect(left, top, right, bottom, new Color(this.colorProperty.getColor()).brighter().getRGB());
        if (this.isExpanded()) {
            RectHelper.drawRect(x + 1.0f, y + height, x + width - 1.0f, y + this.getHeightWithExpand(), ClickGuiScreen.getInstance().getPalette().getSecondaryBackgroundColor().getRGB());
            final float cpLeft = x + 2.0f;
            final float cpTop = y + height + 2.0f;
            final float cpRight = x + 80.0f - 2.0f;
            final float cpBottom = y + height + 80.0f - 2.0f;
            if (mouseX <= cpLeft - 1.0f || mouseY <= cpTop - 1.0f || mouseX >= cpRight + 1.0f || mouseY >= cpBottom + 1.0f) {
                this.colorSelectorDragging = false;
            }
            float colorSelectorX = this.saturation * (cpRight - cpLeft);
            float colorSelectorY = (1.0f - this.brightness) * (cpBottom - cpTop);
            if (this.colorSelectorDragging) {
                final float wWidth = cpRight - cpLeft;
                final float xDif = mouseX - cpLeft;
                this.saturation = xDif / wWidth;
                colorSelectorX = xDif;
                final float hHeight = cpBottom - cpTop;
                final float yDif = mouseY - cpTop;
                this.brightness = 1.0f - yDif / hHeight;
                colorSelectorY = yDif;
                this.updateColor(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), false);
            }
            RectHelper.drawRect(cpLeft, cpTop, cpRight, cpBottom, -16777216);
            this.drawColorPickerRect(cpLeft + 0.5f, cpTop + 0.5f, cpRight - 0.5f, cpBottom - 0.5f);
            final float selectorWidth = 2.0f;
            final float outlineWidth = 0.5f;
            final float half = selectorWidth / 2.0f;
            final float csLeft = cpLeft + colorSelectorX - half;
            final float csTop = cpTop + colorSelectorY - half;
            if (ClickGui.glow.getCurrentValue()) {
                RenderHelper.renderBlurredShadow(new Color(-16777216).darker(), (int)csLeft - (int)outlineWidth, (int)csTop - (int)outlineWidth, 5.0, 5.0, 5);
            }
            RectHelper.drawRectWithEdge(csLeft - outlineWidth, csTop - outlineWidth, outlineWidth + 3.0f, outlineWidth + 3.0f, new Color(245, 245, 245), new Color(30, 30, 30));
            float sLeft = x + 80.0f - 1.0f;
            float sTop = y + height + 2.0f;
            float sRight = sLeft + 8.0f;
            float sBottom = y + height + 80.0f - 2.0f;
            if (mouseX <= sLeft || mouseY <= sTop || mouseX >= sRight || mouseY >= sBottom) {
                this.hueSelectorDragging = false;
            }
            float hueSelectorY = this.hue * (sBottom - sTop);
            if (this.hueSelectorDragging) {
                final float hsHeight = sBottom - sTop;
                final float yDif2 = mouseY - sTop;
                this.hue = yDif2 / hsHeight;
                hueSelectorY = yDif2;
                this.updateColor(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), false);
            }
            RectHelper.drawRect(sLeft, sTop, sRight + 1.1, sBottom, -16777216);
            final float inc = 0.2f;
            final float times = 1.0f / inc;
            final float sHeight = sBottom - sTop;
            float sY = sTop + 0.5f;
            float size = sHeight / times;
            for (int i = 0; i < times; ++i) {
                final boolean bl;
                final boolean last = bl = (i == times - 1.0f);
                if (last) {
                    --size;
                }
                ColorPropertyComponent.gui.drawGradientRect(sLeft + 0.5f, sY, sRight + 1.0f, sY + size, Color.HSBtoRGB(inc * i, 1.0f, 1.0f), Color.HSBtoRGB(inc * (i + 1), 1.0f, 1.0f));
                if (!last) {
                    sY += size;
                }
            }
            final float selectorHeight = 2.0f;
            final float outlineWidth2 = 0.5f;
            final float half2 = selectorHeight / 2.0f;
            final float csTop2 = sTop + hueSelectorY - half2;
            if (ClickGui.glow.getCurrentValue()) {
                RenderHelper.renderBlurredShadow(new Color(-16777216).darker(), (int)sLeft - (int)outlineWidth2, (int)csTop2 - (int)outlineWidth2, 10.0, 5.0, 5);
            }
            RectHelper.drawRectWithEdge(sLeft - outlineWidth2, csTop2 - outlineWidth2, outlineWidth2 + 10.0f, outlineWidth2 + 2.0f, new Color(245, 245, 245), new Color(30, 30, 30));
            sLeft = x + 80.0f + 10.0f;
            sTop = y + height + 2.0f;
            sRight = sLeft + 9.0f;
            sBottom = y + height + 80.0f - 2.0f;
            if (mouseX <= sLeft || mouseY <= sTop || mouseX >= sRight || mouseY >= sBottom) {
                this.alphaSelectorDragging = false;
            }
            final int color = Color.HSBtoRGB(this.hue, this.saturation, this.brightness);
            final int r = color >> 16 & 0xFF;
            final int g = color >> 8 & 0xFF;
            final int b = color & 0xFF;
            float alphaSelectorY = this.alpha * (sBottom - sTop);
            if (this.alphaSelectorDragging) {
                final float hsHeight2 = sBottom - sTop;
                final float yDif3 = mouseY - sTop;
                this.alpha = yDif3 / hsHeight2;
                alphaSelectorY = yDif3;
                this.updateColor(new Color(r, g, b, (int)(this.alpha * 255.0f)).getRGB(), false);
            }
            RectHelper.drawRect(sLeft, sTop, sRight + 1.1, sBottom, -16777216);
            this.drawCheckeredBackground(sLeft + 0.5f, sTop + 0.5f, sRight + 0.5f, sBottom - 0.5f);
            ColorPropertyComponent.gui.drawGradientRect(sLeft + 0.5f, sTop + 0.5f, sRight + 1.0f, sBottom - 0.5f, new Color(r, g, b, 50).getRGB(), new Color(r, g, b, 255).getRGB());
            final float selectorHeight2 = 2.0f;
            final float outlineWidth3 = 0.5f;
            final float half3 = selectorHeight2 / 2.0f;
            final float csTop3 = sTop + alphaSelectorY - half3;
            if (ClickGui.glow.getCurrentValue()) {
                RenderHelper.renderBlurredShadow(new Color(-16777216).darker(), (int)sLeft - (int)outlineWidth3, (int)csTop3 - (int)outlineWidth3, 10.0, 5.0, 5);
            }
            RectHelper.drawRectWithEdge(sLeft - outlineWidth3, csTop3 - outlineWidth3, outlineWidth3 + 10.0f, outlineWidth3 + 2.0f, new Color(245, 245, 245), new Color(30, 30, 30));
            final float xOff = 94.0f;
            final float sLeft2 = x + xOff + 7.0f;
            final float sTop2 = y + height + 2.0f;
            final float sRight2 = sLeft2 + width - xOff - 7.0f;
            final float sBottom2 = y + height + 40.0f + 8.0f;
            RectHelper.drawRect(sLeft2, sTop2, sRight2, sBottom2 + 31.0f, -16777216);
            RectHelper.drawRect(sLeft2 + 0.5f, sTop2 + 0.5f, sRight2 - 0.5f, sBottom2 + 30.0f, new Color(this.colorProperty.getColor()).brighter().getRGB());
        }
    }
    
    private void drawCheckeredBackground(final float x, float y, final float right, final float bottom) {
        RectHelper.drawRect(x, y, right, bottom, -1);
        boolean off = false;
        while (y < bottom) {
            for (float x2 = x + ((off = !off) ? 1 : 0); x2 < right; x2 += 2.0f) {
                RectHelper.drawRect(x2, y, x2 + 1.0f, y + 1.0f, -8355712);
            }
            ++y;
        }
    }
    
    private void updateColor(final int hex, final boolean hasAlpha) {
        if (hasAlpha) {
            this.colorProperty.setColor(hex);
        }
        else {
            this.colorProperty.setColor(new Color(hex >> 16 & 0xFF, hex >> 8 & 0xFF, hex & 0xFF, (int)(this.alpha * 255.0f)).getRGB());
        }
    }
    
    @Override
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        super.onMouseClick(mouseX, mouseY, button);
        if (this.isExpanded() && button == 0) {
            final float x = (float)this.getX();
            final float y = (float)this.getY();
            final float cpLeft = x - 2.0f;
            final float cpTop = y + this.getHeight() - 2.0f;
            final float cpRight = x + 80.0f + 2.0f;
            final float cpBottom = y + this.getHeight() + 80.0f + 2.0f;
            final float sLeft = x + 80.0f - 1.0f;
            final float sTop = y + this.getHeight() + 2.0f;
            final float sRight = sLeft + 10.0f;
            final float sBottom = y + this.getHeight() + 80.0f - 2.0f;
            final float asLeft = x + 80.0f + 6.0f;
            final float asTop = y + this.getHeight() + 2.0f;
            final float asRight = asLeft + 10.0f;
            final float asBottom = y + this.getHeight() + 80.0f - 2.0f;
            this.colorSelectorDragging = (!this.colorSelectorDragging && mouseX > cpLeft && mouseY > cpTop && mouseX < cpRight && mouseY < cpBottom);
            this.hueSelectorDragging = (!this.hueSelectorDragging && mouseX > sLeft && mouseY > sTop && mouseX < sRight && mouseY < sBottom);
            this.alphaSelectorDragging = (!this.alphaSelectorDragging && mouseX > asLeft && mouseY > asTop && mouseX < asRight && mouseY < asBottom);
        }
    }
    
    @Override
    public void onMouseRelease(final int button) {
        if (this.hueSelectorDragging) {
            this.hueSelectorDragging = false;
        }
        else if (this.colorSelectorDragging) {
            this.colorSelectorDragging = false;
        }
        else if (this.alphaSelectorDragging) {
            this.alphaSelectorDragging = false;
        }
    }
    
    private float[] getHSBFromColor(final int hex) {
        final int r = hex >> 16 & 0xFF;
        final int g = hex >> 8 & 0xFF;
        final int b = hex & 0xFF;
        return Color.RGBtoHSB(r, g, b, null);
    }
    
    public void drawColorPickerRect(final float left, final float top, final float right, final float bottom) {
        final int hueBasedColor = Color.HSBtoRGB(this.hue, 1.0f, 1.0f);
        GL11.glDisable(3553);
        GL11.glShadeModel(7425);
        ColorPropertyComponent.buffer.a(7, cdy.f);
        ColorPropertyComponent.buffer.b(right, top, 0.0).color(hueBasedColor).d();
        ColorPropertyComponent.buffer.b(left, top, 0.0).color(-1).d();
        ColorPropertyComponent.buffer.b(left, bottom, 0.0).color(new Color(0).getRGB()).d();
        ColorPropertyComponent.buffer.b(right, bottom, 0.0).color(new Color(0).getRGB()).d();
        ColorPropertyComponent.tessellator.b();
        GL11.glShadeModel(7424);
        GL11.glEnable(3553);
    }
    
    @Override
    public boolean canExpand() {
        return true;
    }
    
    @Override
    public int getHeightWithExpand() {
        return (int)(this.getHeight() + 80.0f);
    }
    
    @Override
    public void onPress(final int mouseX, final int mouseY, final int button) {
    }
    
    @Override
    public Setting getProperty() {
        return this.colorProperty;
    }
    
    static {
        ColorPropertyComponent.tessellator = bve.a();
        ColorPropertyComponent.buffer = ColorPropertyComponent.tessellator.c();
    }
}
