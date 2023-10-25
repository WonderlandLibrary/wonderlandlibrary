/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.clickgui.component.impl.component.property.impl;

import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.celestial.client.feature.impl.hud.ClickGui;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.settings.Setting;
import org.celestial.client.settings.impl.ColorSetting;
import org.celestial.client.ui.clickgui.ClickGuiScreen;
import org.celestial.client.ui.clickgui.component.Component;
import org.celestial.client.ui.clickgui.component.impl.ExpandableComponent;
import org.celestial.client.ui.clickgui.component.impl.component.property.PropertyComponent;
import org.lwjgl.opengl.GL11;

public class ColorPropertyComponent
extends ExpandableComponent
implements PropertyComponent {
    private static final int COLOR_PICKER_HEIGHT = 80;
    public static Tessellator tessellator = Tessellator.getInstance();
    public static BufferBuilder buffer = tessellator.getBuffer();
    private final ColorSetting colorProperty;
    private float hue;
    private float saturation;
    private float brightness;
    private float alpha;
    private boolean colorSelectorDragging;
    private boolean hueSelectorDragging;
    private boolean alphaSelectorDragging;

    public ColorPropertyComponent(Component parent, ColorSetting colorProperty, float x, float y, float width, float height) {
        super(parent, colorProperty.getName(), x, y, width, height);
        this.colorProperty = colorProperty;
        int value = colorProperty.getColor();
        float[] hsb = this.getHSBFromColor(value);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
        this.alpha = (float)(value >> 24 & 0xFF) / 255.0f;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        float x = (float)this.getX();
        float y = (float)this.getY();
        float width = this.getWidth();
        float height = this.getHeight();
        int textColor = 0xFFFFFF;
        RectHelper.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 160).getRGB());
        ColorPropertyComponent.mc.fontRenderer.drawStringWithShadow(this.getName(), x + 2.0f, y + height / 2.0f - 3.0f, textColor);
        float left = x + width - 13.0f;
        float top = y + height / 2.0f - 2.0f;
        float right = x + width - 2.0f;
        float bottom = y + height / 2.0f + 2.0f;
        if (ClickGui.glow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(new Color(this.colorProperty.getColor()).brighter(), (double)((int)left), (double)((int)top), 12.0, 3.0, 10);
        }
        RectHelper.drawRect(left - 0.5f, top - 0.5f, right + 0.5f, bottom + 0.5f, -16777216);
        this.drawCheckeredBackground(left, top, right, bottom);
        RectHelper.drawRect(left, top, right, bottom, this.colorProperty.getColor());
        if (this.isExpanded()) {
            RectHelper.drawRect(x + 1.0f, y + height, x + width - 1.0f, y + (float)this.getHeightWithExpand(), ClickGuiScreen.getInstance().getPalette().getSecondaryBackgroundColor().getRGB());
            float cpLeft = x + 2.0f;
            float cpTop = y + height + 2.0f;
            float cpRight = x + 80.0f - 2.0f;
            float cpBottom = y + height + 80.0f - 2.0f;
            if ((float)mouseX <= cpLeft - 1.0f || (float)mouseY <= cpTop - 1.0f || (float)mouseX >= cpRight + 1.0f || (float)mouseY >= cpBottom + 1.0f) {
                this.colorSelectorDragging = false;
            }
            float colorSelectorX = this.saturation * (cpRight - cpLeft);
            float colorSelectorY = (1.0f - this.brightness) * (cpBottom - cpTop);
            if (this.colorSelectorDragging) {
                float wWidth = cpRight - cpLeft;
                float xDif = (float)mouseX - cpLeft;
                this.saturation = xDif / wWidth;
                colorSelectorX = xDif;
                float hHeight = cpBottom - cpTop;
                float yDif = (float)mouseY - cpTop;
                this.brightness = 1.0f - yDif / hHeight;
                colorSelectorY = yDif;
                this.updateColor(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), false);
            }
            RectHelper.drawRect(cpLeft, cpTop, cpRight, cpBottom, -16777216);
            this.drawColorPickerRect(cpLeft + 0.5f, cpTop + 0.5f, cpRight - 0.5f, cpBottom - 0.5f);
            float selectorWidth = 2.0f;
            float outlineWidth = 0.5f;
            float half = selectorWidth / 2.0f;
            float csLeft = cpLeft + colorSelectorX - half;
            float csTop = cpTop + colorSelectorY - half;
            if (ClickGui.glow.getCurrentValue()) {
                RenderHelper.renderBlurredShadow(new Color(-16777216).darker(), (double)((int)csLeft - (int)outlineWidth), (double)((int)csTop - (int)outlineWidth), 5.0, 5.0, 5);
            }
            RectHelper.drawRectWithEdge(csLeft - outlineWidth, csTop - outlineWidth, outlineWidth + 3.0f, outlineWidth + 3.0f, new Color(245, 245, 245), new Color(30, 30, 30));
            float sLeft = x + 80.0f - 1.0f;
            float sTop = y + height + 2.0f;
            float sRight = sLeft + 8.0f;
            float sBottom = y + height + 80.0f - 2.0f;
            if ((float)mouseX <= sLeft || (float)mouseY <= sTop || (float)mouseX >= sRight || (float)mouseY >= sBottom) {
                this.hueSelectorDragging = false;
            }
            float hueSelectorY = this.hue * (sBottom - sTop);
            if (this.hueSelectorDragging) {
                float hsHeight = sBottom - sTop;
                float yDif = (float)mouseY - sTop;
                this.hue = yDif / hsHeight;
                hueSelectorY = yDif;
                this.updateColor(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), false);
            }
            RectHelper.drawRect(sLeft, sTop, (double)sRight + 1.1, sBottom, -16777216);
            float inc = 0.2f;
            float times = 1.0f / inc;
            float sHeight = sBottom - sTop;
            float sY = sTop + 0.5f;
            float size = sHeight / times;
            int i = 0;
            while ((float)i < times) {
                boolean last;
                boolean bl = last = (float)i == times - 1.0f;
                if (last) {
                    size -= 1.0f;
                }
                gui.drawGradientRect(sLeft + 0.5f, sY, sRight + 1.0f, sY + size, Color.HSBtoRGB(inc * (float)i, 1.0f, 1.0f), Color.HSBtoRGB(inc * (float)(i + 1), 1.0f, 1.0f));
                if (!last) {
                    sY += size;
                }
                ++i;
            }
            float selectorHeight = 2.0f;
            float outlineWidth2 = 0.5f;
            float half2 = selectorHeight / 2.0f;
            float csTop2 = sTop + hueSelectorY - half2;
            if (ClickGui.glow.getCurrentValue()) {
                RenderHelper.renderBlurredShadow(new Color(-16777216).darker(), (double)((int)sLeft - (int)outlineWidth2), (double)((int)csTop2 - (int)outlineWidth2), 10.0, 5.0, 5);
            }
            RectHelper.drawRectWithEdge(sLeft - outlineWidth2, csTop2 - outlineWidth2, outlineWidth2 + 10.0f, outlineWidth2 + 2.0f, new Color(245, 245, 245), new Color(30, 30, 30));
            sLeft = x + 80.0f + 10.0f;
            sTop = y + height + 2.0f;
            sRight = sLeft + 9.0f;
            sBottom = y + height + 80.0f - 2.0f;
            if ((float)mouseX <= sLeft || (float)mouseY <= sTop || (float)mouseX >= sRight || (float)mouseY >= sBottom) {
                this.alphaSelectorDragging = false;
            }
            int color = Color.HSBtoRGB(this.hue, this.saturation, this.brightness);
            int r = color >> 16 & 0xFF;
            int g = color >> 8 & 0xFF;
            int b = color & 0xFF;
            float alphaSelectorY = this.alpha * (sBottom - sTop);
            if (this.alphaSelectorDragging) {
                float hsHeight = sBottom - sTop;
                float yDif = (float)mouseY - sTop;
                this.alpha = yDif / hsHeight;
                alphaSelectorY = yDif;
                this.updateColor(new Color(r, g, b, (int)(this.alpha * 255.0f)).getRGB(), false);
            }
            RectHelper.drawRect(sLeft, sTop, (double)sRight + 1.1, sBottom, -16777216);
            this.drawCheckeredBackground(sLeft + 0.5f, sTop + 0.5f, sRight + 0.5f, sBottom - 0.5f);
            gui.drawGradientRect(sLeft + 0.5f, sTop + 0.5f, sRight + 1.0f, sBottom - 0.5f, new Color(r, g, b, 50).getRGB(), new Color(r, g, b, 255).getRGB());
            float selectorHeight2 = 2.0f;
            float outlineWidth3 = 0.5f;
            float half3 = selectorHeight2 / 2.0f;
            float csTop3 = sTop + alphaSelectorY - half3;
            if (ClickGui.glow.getCurrentValue()) {
                RenderHelper.renderBlurredShadow(new Color(-16777216).darker(), (double)((int)sLeft - (int)outlineWidth3), (double)((int)csTop3 - (int)outlineWidth3), 10.0, 5.0, 5);
            }
            RectHelper.drawRectWithEdge(sLeft - outlineWidth3, csTop3 - outlineWidth3, outlineWidth3 + 10.0f, outlineWidth3 + 2.0f, new Color(245, 245, 245), new Color(30, 30, 30));
            float xOff = 94.0f;
            float sLeft2 = x + xOff + 7.0f;
            float sTop2 = y + height + 2.0f;
            float sRight2 = sLeft2 + width - xOff - 7.0f;
            float sBottom2 = y + height + 40.0f + 8.0f;
            RectHelper.drawRect(sLeft2, sTop2, sRight2, sBottom2 + 31.0f, -16777216);
            this.drawCheckeredBackground(sLeft2 + 0.5f, sTop2 + 0.5f, sRight2 - 0.5f, sBottom2 + 30.0f);
            RectHelper.drawRect(sLeft2 + 3.0f, sTop2 + 4.0f, sRight2 - 3.0f, sBottom2 + 28.0f, this.colorProperty.getColor());
        }
    }

    private void drawCheckeredBackground(float x, float y, float right, float bottom) {
        RectHelper.drawRect(x, y, right, bottom, -1);
        boolean off = false;
        while (y < bottom) {
            off = !off;
            for (float x1 = x + (float)(off ? true : false); x1 < right; x1 += 2.0f) {
                RectHelper.drawRect(x1, y, x1 + 1.0f, y + 1.0f, -8355712);
            }
            y += 1.0f;
        }
    }

    private void updateColor(int hex, boolean hasAlpha) {
        if (hasAlpha) {
            this.colorProperty.setColor(hex);
        } else {
            this.colorProperty.setColor(new Color(hex >> 16 & 0xFF, hex >> 8 & 0xFF, hex & 0xFF, (int)(this.alpha * 255.0f)).getRGB());
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        super.onMouseClick(mouseX, mouseY, button);
        if (this.isExpanded() && button == 0) {
            float x = (float)this.getX();
            float y = (float)this.getY();
            float cpLeft = x - 2.0f;
            float cpTop = y + this.getHeight() - 2.0f;
            float cpRight = x + 80.0f + 2.0f;
            float cpBottom = y + this.getHeight() + 80.0f + 2.0f;
            float sLeft = x + 80.0f - 1.0f;
            float sTop = y + this.getHeight() + 2.0f;
            float sRight = sLeft + 10.0f;
            float sBottom = y + this.getHeight() + 80.0f - 2.0f;
            float asLeft = x + 80.0f + 6.0f;
            float asTop = y + this.getHeight() + 2.0f;
            float asRight = asLeft + 10.0f;
            float asBottom = y + this.getHeight() + 80.0f - 2.0f;
            this.colorSelectorDragging = !this.colorSelectorDragging && (float)mouseX > cpLeft && (float)mouseY > cpTop && (float)mouseX < cpRight && (float)mouseY < cpBottom;
            this.hueSelectorDragging = !this.hueSelectorDragging && (float)mouseX > sLeft && (float)mouseY > sTop && (float)mouseX < sRight && (float)mouseY < sBottom;
            this.alphaSelectorDragging = !this.alphaSelectorDragging && (float)mouseX > asLeft && (float)mouseY > asTop && (float)mouseX < asRight && (float)mouseY < asBottom;
        }
    }

    @Override
    public void onMouseRelease(int button) {
        if (this.hueSelectorDragging) {
            this.hueSelectorDragging = false;
        } else if (this.colorSelectorDragging) {
            this.colorSelectorDragging = false;
        } else if (this.alphaSelectorDragging) {
            this.alphaSelectorDragging = false;
        }
    }

    private float[] getHSBFromColor(int hex) {
        int r = hex >> 16 & 0xFF;
        int g = hex >> 8 & 0xFF;
        int b = hex & 0xFF;
        return Color.RGBtoHSB(r, g, b, null);
    }

    public void drawColorPickerRect(float left, float top, float right, float bottom) {
        int hueBasedColor = Color.HSBtoRGB(this.hue, 1.0f, 1.0f);
        GL11.glDisable(3553);
        GL11.glShadeModel(7425);
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(right, top, 0.0).color(hueBasedColor).endVertex();
        buffer.pos(left, top, 0.0).color(-1).endVertex();
        buffer.pos(left, bottom, 0.0).color(new Color(0).getRGB()).endVertex();
        buffer.pos(right, bottom, 0.0).color(new Color(0).getRGB()).endVertex();
        tessellator.draw();
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
    public void onPress(int mouseX, int mouseY, int button) {
    }

    @Override
    public Setting getProperty() {
        return this.colorProperty;
    }
}

