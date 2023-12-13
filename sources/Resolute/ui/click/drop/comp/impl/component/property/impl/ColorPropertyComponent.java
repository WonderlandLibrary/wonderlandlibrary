// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.drop.comp.impl.component.property.impl;

import vip.Resolute.settings.Setting;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.Minecraft;
import vip.Resolute.util.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import vip.Resolute.ui.click.drop.ClickGui;
import vip.Resolute.modules.impl.render.ClickGUI;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.ui.click.drop.comp.Component;
import vip.Resolute.settings.impl.ColorSetting;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import vip.Resolute.ui.click.drop.comp.impl.component.property.PropertyComponent;
import vip.Resolute.ui.click.drop.comp.impl.ExpandableComponent;

public class ColorPropertyComponent extends ExpandableComponent implements PropertyComponent
{
    private static final int COLOR_PICKER_HEIGHT = 80;
    public static Tessellator tessellator;
    public static WorldRenderer worldrenderer;
    private final ColorSetting colorProperty;
    private float hue;
    private float saturation;
    private float brightness;
    private float alpha;
    private boolean colorSelectorDragging;
    private boolean hueSelectorDragging;
    private boolean alphaSelectorDragging;
    
    public ColorPropertyComponent(final Component parent, final ColorSetting colorProperty, final int x, final int y, final int width, final int height) {
        super(parent, colorProperty.name, x, y, width, height);
        this.colorProperty = colorProperty;
    }
    
    @Override
    public void drawComponent(final ScaledResolution scaledResolution, final int mouseX, final int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        final int x = this.getX();
        final int y = this.getY();
        final int width = this.getWidth();
        final int height = this.getHeight();
        final int textColor = 16777215;
        final int bgColor = this.getSecondaryBackgroundColor(this.isHovered(mouseX, mouseY));
        Gui.drawRect((float)x, (float)y, (float)(x + width + 1), (float)(y + height), bgColor);
        Gui.drawRect((float)(x - 2), (float)y, (float)x, (float)(y + height), ClickGUI.color.getColor());
        final float left = (float)(x + width - 13);
        final float top = y + height / 2.0f - 2.0f;
        final float right = (float)(x + width - 2);
        final float bottom = y + height / 2.0f + 2.0f;
        Gui.drawRect(left - 0.5f, top - 0.5f, right + 0.5f, bottom + 0.5f, -16777216);
        this.drawCheckeredBackground(left, top, right, bottom);
        Gui.drawRect(left, top, right, bottom, this.colorProperty.getColor());
        if (this.isExpanded()) {
            Gui.drawRect((float)(x - 2), (float)(y + height), (float)(x + 1), (float)(y + this.getHeightWithExpand()), ClickGUI.color.getColor());
            Gui.drawRect((float)(x + 1), (float)(y + height), (float)(x + width - 1 + 2), (float)(y + this.getHeightWithExpand()), ClickGui.getInstance().getPalette().getSecondaryBackgroundColor().getRGB());
            final float cpLeft = (float)(x + 2);
            final float cpTop = (float)(y + height + 2);
            final float cpRight = (float)(x + 80 - 2);
            final float cpBottom = (float)(y + height + 80 - 2);
            if (mouseX <= cpLeft || mouseY <= cpTop || mouseX >= cpRight || mouseY >= cpBottom) {
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
            Gui.drawRect(cpLeft, cpTop, cpRight, cpBottom, -16777216);
            this.drawColorPickerRect(cpLeft + 0.5f, cpTop + 0.5f, cpRight - 0.5f, cpBottom - 0.5f);
            final float selectorWidth = 2.0f;
            final float outlineWidth = 0.5f;
            final float half = 1.0f;
            final float csLeft = cpLeft + colorSelectorX - 1.0f;
            final float csTop = cpTop + colorSelectorY - 1.0f;
            final float csRight = cpLeft + colorSelectorX + 1.0f;
            final float csBottom = cpTop + colorSelectorY + 1.0f;
            Gui.drawRect(csLeft - 0.5f, csTop - 0.5f, csRight + 0.5f, csBottom + 0.5f, -16777216);
            Gui.drawRect(csLeft, csTop, csRight, csBottom, Color.HSBtoRGB(this.hue, this.saturation, this.brightness));
            float sLeft = (float)(x + 80 - 1);
            float sTop = (float)(y + height + 2);
            float sRight = sLeft + 5.0f;
            float sBottom = (float)(y + height + 80 - 2);
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
            Gui.drawRect(sLeft, sTop, sRight, sBottom, -16777216);
            final float inc = 0.2f;
            final float times = 5.0f;
            final float sHeight = sBottom - sTop;
            float sY = sTop + 0.5f;
            float size = sHeight / 5.0f;
            for (int i = 0; i < 5.0f; ++i) {
                final boolean last = i == 4.0f;
                if (last) {
                    --size;
                }
                Gui.drawGradientRect(sLeft + 0.5f, sY, sRight - 0.5f, sY + size, Color.HSBtoRGB(0.2f * i, 1.0f, 1.0f), Color.HSBtoRGB(0.2f * (i + 1), 1.0f, 1.0f));
                if (!last) {
                    sY += size;
                }
            }
            final float selectorHeight = 2.0f;
            final float outlineWidth2 = 0.5f;
            final float half2 = 1.0f;
            final float csTop2 = sTop + hueSelectorY - 1.0f;
            final float csBottom2 = sTop + hueSelectorY + 1.0f;
            Gui.drawRect(sLeft - 0.5f, csTop2 - 0.5f, sRight + 0.5f, csBottom2 + 0.5f, -16777216);
            Gui.drawRect(sLeft, csTop2, sRight, csBottom2, Color.HSBtoRGB(this.hue, 1.0f, 1.0f));
            sLeft = (float)(x + 80 + 6);
            sTop = (float)(y + height + 2);
            sRight = sLeft + 5.0f;
            sBottom = (float)(y + height + 80 - 2);
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
                this.updateColor(new Color(r, g, b, (int)(this.alpha * 255.0f)).getRGB(), true);
            }
            Gui.drawRect(sLeft, sTop, sRight, sBottom, -16777216);
            this.drawCheckeredBackground(sLeft + 0.5f, sTop + 0.5f, sRight - 0.5f, sBottom - 0.5f);
            Gui.drawGradientRect(sLeft + 0.5f, sTop + 0.5f, sRight - 0.5f, sBottom - 0.5f, new Color(r, g, b, 0).getRGB(), new Color(r, g, b, 255).getRGB());
            final float selectorHeight2 = 2.0f;
            final float outlineWidth3 = 0.5f;
            final float half3 = 1.0f;
            final float csTop3 = sTop + alphaSelectorY - 1.0f;
            final float csBottom3 = sTop + alphaSelectorY + 1.0f;
            final float bx = sRight + 0.5f;
            final float ay = csTop3 - 0.5f;
            final float by = csBottom3 + 0.5f;
            GL11.glDisable(3553);
            RenderUtils.color(-16777216);
            GL11.glBegin(2);
            GL11.glVertex2f(sLeft, ay);
            GL11.glVertex2f(sLeft, by);
            GL11.glVertex2f(bx, by);
            GL11.glVertex2f(bx, ay);
            GL11.glEnd();
            GL11.glEnable(3553);
            final float xOff = 93.0f;
            final float sLeft2 = x + 93.0f;
            final float sTop2 = (float)(y + height + 2);
            final float sRight2 = sLeft2 + width - 93.0f - 3.0f;
            final float sBottom2 = y + height + 40.0f + 8.0f;
            Gui.drawRect(sLeft2, sTop2, sRight2, sBottom2, -16777216);
            this.drawCheckeredBackground(sLeft2 + 0.5f, sTop2 + 0.5f, sRight2 - 0.5f, sBottom2 - 0.5f);
            Gui.drawRect(sLeft2 + 4.0f, sTop2 + 4.0f, sRight2 - 4.0f, sBottom2 - 4.0f, this.colorProperty.getColor());
        }
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.getName(), (float)(x + 2), y + height / 2.0f - 3.0f, 16777215);
    }
    
    private void drawCheckeredBackground(final float x, float y, final float x2, final float y2) {
        Gui.drawRect(x, y, x2, y2, -1);
        boolean off = false;
        while (y < y2) {
            for (float x3 = x + ((off = !off) ? 1 : 0); x3 < x2; x3 += 2.0f) {
                Gui.drawRect(x3, y, x3 + 1.0f, y + 1.0f, -8355712);
            }
            ++y;
        }
    }
    
    private void updateColor(final int hex, final boolean hasAlpha) {
        if (hasAlpha) {
            this.colorProperty.setValue(hex);
        }
        else {
            this.colorProperty.setValue(new Color(hex >> 16 & 0xFF, hex >> 8 & 0xFF, hex & 0xFF, (int)(this.alpha * 255.0f)).getRGB());
        }
    }
    
    @Override
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        super.onMouseClick(mouseX, mouseY, button);
        if (this.isExpanded() && button == 0) {
            final int x = this.getX();
            final int y = this.getY();
            final float cpLeft = (float)(x + 2);
            final float cpTop = (float)(y + this.getHeight() + 2);
            final float cpRight = (float)(x + 80 - 2);
            final float cpBottom = (float)(y + this.getHeight() + 80 - 2);
            final float sLeft = (float)(x + 80 - 1);
            final float sTop = (float)(y + this.getHeight() + 2);
            final float sRight = sLeft + 5.0f;
            final float sBottom = (float)(y + this.getHeight() + 80 - 2);
            final float asLeft = (float)(x + 80 + 6);
            final float asTop = (float)(y + this.getHeight() + 2);
            final float asRight = asLeft + 5.0f;
            final float asBottom = (float)(y + this.getHeight() + 80 - 2);
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
        RenderUtils.startBlending();
        GL11.glShadeModel(7425);
        ColorPropertyComponent.worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        ColorPropertyComponent.worldrenderer.pos(right, top, 0.0).color(hueBasedColor).endVertex();
        ColorPropertyComponent.worldrenderer.pos(left, top, 0.0).color(-1).endVertex();
        ColorPropertyComponent.worldrenderer.pos(left, bottom, 0.0).color(-1).endVertex();
        ColorPropertyComponent.worldrenderer.pos(right, bottom, 0.0).color(hueBasedColor).endVertex();
        ColorPropertyComponent.tessellator.draw();
        ColorPropertyComponent.worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        ColorPropertyComponent.worldrenderer.pos(right, top, 0.0).color(402653184).endVertex();
        ColorPropertyComponent.worldrenderer.pos(left, top, 0.0).color(402653184).endVertex();
        ColorPropertyComponent.worldrenderer.pos(left, bottom, 0.0).color(-16777216).endVertex();
        ColorPropertyComponent.worldrenderer.pos(right, bottom, 0.0).color(-16777216).endVertex();
        ColorPropertyComponent.tessellator.draw();
        RenderUtils.endBlending();
        GL11.glShadeModel(7424);
        GL11.glEnable(3553);
    }
    
    @Override
    public boolean canExpand() {
        return true;
    }
    
    @Override
    public int getHeightWithExpand() {
        return this.getHeight() + 80;
    }
    
    @Override
    public void onPress(final int mouseX, final int mouseY, final int button) {
    }
    
    @Override
    public ColorSetting getProperty() {
        return this.colorProperty;
    }
    
    static {
        ColorPropertyComponent.tessellator = Tessellator.getInstance();
        ColorPropertyComponent.worldrenderer = ColorPropertyComponent.tessellator.getWorldRenderer();
    }
}
