package org.dreamcore.client.ui.newclickgui.settings;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import org.dreamcore.client.helpers.render.RenderHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.settings.impl.ColorSetting;
import org.dreamcore.client.ui.newclickgui.FeaturePanel;

import java.awt.*;

public class ColorPickerComponent extends Component {

    public static int heightOffset = 80;
    public static Tessellator tessellator = Tessellator.getInstance();
    public static BufferBuilder buffer = tessellator.getBuffer();
    private float hue;
    private float saturation;
    private float brightness;
    private float alpha;

    private boolean colorSelectorDragging;

    private boolean hueSelectorDragging;

    private boolean alphaSelectorDragging;

    public ColorPickerComponent(FeaturePanel featurePanel, ColorSetting setting) {

        this.featurePanel = featurePanel;
        this.setting = setting;

        int value = setting.getColorValue();
        float[] hsb = getHSBFromColor(value);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];

        this.alpha = (value >> 24 & 0xFF) / 255.0F;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {

        int textColor = 0xFFFFFF;

        mc.circleregular.drawStringWithOutline(setting.getName(), x - 195, y + height / 2 - mc.circleregular.getFontHeight() / 4, textColor);

        {
            // Box with gradient
            float cpLeft = x + 2;
            float cpTop = y + height + 2;
            float cpRight = x + heightOffset - 2;
            float cpBottom = y + height + heightOffset - 2;

            if (mouseX <= cpLeft || mouseY <= cpTop || mouseX >= cpRight || mouseY >= cpBottom)
                colorSelectorDragging = false;

            float colorSelectorX = saturation * (cpRight - cpLeft);
            float colorSelectorY = (1 - brightness) * (cpBottom - cpTop);

            if (colorSelectorDragging) {
                float wWidth = cpRight - cpLeft;
                float xDif = mouseX - cpLeft;
                this.saturation = xDif / wWidth;
                colorSelectorX = xDif;

                float hHeight = cpBottom - cpTop;
                float yDif = mouseY - cpTop;
                this.brightness = 1 - (yDif / hHeight);
                colorSelectorY = yDif;

                updateColor(Color.HSBtoRGB(hue, saturation, brightness), false);
            }

            RectHelper.drawRect(cpLeft, cpTop, cpRight, cpBottom, 0xFF000000);

            drawColorPickerRect(cpLeft + 0.5F, cpTop + 0.5F, cpRight - 0.5F, cpBottom - 0.5F);
            // Selector
            float selectorWidth = 2;
            float half = selectorWidth / 2;

            float csRight = cpLeft + colorSelectorX + half;
            float csBottom = cpTop + colorSelectorY + half;
            RenderHelper.drawCircle(csRight, csBottom, 1.4F, true, Color.BLACK);
            RenderHelper.drawCircle(csRight, csBottom, 1, true, Color.WHITE);
        }

        // Hue Slider
        {
            float sLeft = x + heightOffset - 1;
            float sTop = y + height + 2;
            float sRight = sLeft + 5;
            float sBottom = y + height + heightOffset - 2;

            if (mouseX <= sLeft || mouseY <= sTop || mouseX >= sRight || mouseY >= sBottom) {
                hueSelectorDragging = false;
            }

            float hueSelectorY = this.hue * (sBottom - sTop);

            if (hueSelectorDragging) {
                float hsHeight = sBottom - sTop;
                float yDif = mouseY - sTop;
                this.hue = yDif / hsHeight;
                hueSelectorY = yDif;

                updateColor(Color.HSBtoRGB(hue, saturation, brightness), false);
            }

            // Outline
            RectHelper.drawSmoothRect(sLeft, sTop, sRight, sBottom, 0xFF000000);
            float inc = 0.2F;
            float times = 1 / inc;
            float sHeight = sBottom - sTop;
            float sY = sTop + 0.5F;
            float size = sHeight / times;

            // Color
            for (int i = 0; i < times; i++) {
                boolean last = i == times - 1;
                if (last) {
                    size--;
                }
                gui.drawGradientRect(sLeft + 0.5F, sY, sRight - 0.5F, sY + size, Color.HSBtoRGB(inc * i, 1.0F, 1.0F), Color.HSBtoRGB(inc * (i + 1), 1.0F, 1.0F));
                if (!last) {
                    sY += size;
                }
            }

            float selectorHeight = 1.5f;
            float outlineWidth = 0.5F;
            float half = selectorHeight / 2;

            float csTop = sTop + hueSelectorY - half;
            float csBottom = sTop + hueSelectorY + half;

            RectHelper.drawRect(sLeft - outlineWidth, csTop - outlineWidth, sRight + outlineWidth, csBottom + outlineWidth, 0xFF000000);

            RectHelper.drawRect(sLeft, csTop, sRight, csBottom, -1);
        }

        // Alpha Slider
        {
            float sLeft = x + heightOffset + 6;
            float sTop = y + height + 2;
            float sRight = sLeft + 5;
            float sBottom = y + height + heightOffset - 2;

            if (mouseX <= sLeft || mouseY <= sTop || mouseX >= sRight || mouseY >= sBottom) {
                alphaSelectorDragging = false;
            }

            int color = Color.HSBtoRGB(hue, saturation, brightness);

            int r = color >> 16 & 0xFF;
            int g = color >> 8 & 0xFF;
            int b = color & 0xFF;

            float alphaSelectorY = alpha * (sBottom - sTop);

            if (alphaSelectorDragging) {
                float hsHeight = sBottom - sTop;
                float yDif = mouseY - sTop;
                this.alpha = yDif / hsHeight;
                alphaSelectorY = yDif;

                updateColor(new Color(r, g, b, (int) (alpha * 255)).getRGB(), true);
            }

            // Outline
            Gui.drawRect(sLeft, sTop, sRight, sBottom, Color.GRAY.getRGB());
            // Background
            drawCheckeredBackground(sLeft + 0.5F, sTop + 0.5F, sRight - 0.5F, sBottom - 0.5F);
            // Colored bit
            gui.drawGradientRect(sLeft + 0.5F, sTop + 0.5F, sRight - 0.5F, sBottom - 0.5F, new Color(r, g, b, 0).getRGB(), new Color(r, g, b, 255).getRGB());

            float selectorHeight = 2;
            float outlineWidth = 0.5F;
            float half = selectorHeight / 2;

            float csTop = sTop + alphaSelectorY - half;
            float csBottom = sTop + alphaSelectorY + half;

            RectHelper.drawRect(sLeft - outlineWidth, csTop - outlineWidth, sRight + outlineWidth, csBottom + outlineWidth, 0xFF000000);

            RectHelper.drawRect(sLeft, csTop, sRight, csBottom, -1);
        }
    }

    private void drawCheckeredBackground(float x, float y, float right, float bottom) {
        RectHelper.drawRect(x, y, right, bottom, -1);

        for (boolean off = false; y < bottom; y++) {
            for (float x1 = x + ((off = !off) ? 1 : 0); x1 < right; x1 += 2) {
                RectHelper.drawRect(x1, y, x1 + 1, y + 1, 0xFF808080);
            }
        }
    }

    private void updateColor(int hex, boolean hasAlpha) {
        if (hasAlpha) {
            ((ColorSetting) setting).setColorValue(hex);
        } else {
            ((ColorSetting) setting).setColorValue(new Color(hex >> 16 & 0xFF, hex >> 8 & 0xFF, hex & 0xFF, (int) (alpha * 255)).getRGB());
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0) {
            float cpLeft = x + 2;
            float cpTop = y + height + 2;
            float cpRight = x + heightOffset - 2;
            float cpBottom = y + height + heightOffset - 2;

            float sLeft = x + heightOffset - 1;
            float sTop = y + height + 2;
            float sRight = sLeft + 5;
            float sBottom = y + height + heightOffset - 2;

            float asLeft = x + heightOffset + 6;
            float asTop = y + height + 2;
            float asRight = asLeft + 5;
            float asBottom = y + height + heightOffset - 2;

            colorSelectorDragging = !colorSelectorDragging && mouseX > cpLeft && mouseY > cpTop && mouseX < cpRight && mouseY < cpBottom;
            hueSelectorDragging = !hueSelectorDragging && mouseX > sLeft && mouseY > sTop && mouseX < sRight && mouseY < sBottom;
            alphaSelectorDragging = !alphaSelectorDragging && mouseX > asLeft && mouseY > asTop && mouseX < asRight && mouseY < asBottom;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (hueSelectorDragging) {
            hueSelectorDragging = false;
        } else if (colorSelectorDragging) {
            colorSelectorDragging = false;
        } else if (alphaSelectorDragging) {
            alphaSelectorDragging = false;
        }
    }

    private float[] getHSBFromColor(int hex) {
        int r = hex >> 16 & 0xFF;
        int g = hex >> 8 & 0xFF;
        int b = hex & 0xFF;
        return Color.RGBtoHSB(r, g, b, null);
    }

    public void drawColorPickerRect(float left, float top, float right, float bottom) {
        int hueBasedColor = Color.HSBtoRGB(hue, 1, 1);
        GlStateManager.disable(GL11.GL_TEXTURE_2D);
        GlStateManager.enableBlend();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(right, top, 0).color(hueBasedColor).endVertex();
        buffer.pos(left, top, 0).color(-1).endVertex();
        buffer.pos(left, bottom, 0).color(-1).endVertex();
        buffer.pos(right, bottom, 0).color(hueBasedColor).endVertex();
        tessellator.draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(right, top, 0).color(0x18000000).endVertex();
        buffer.pos(left, top, 0).color(0x18000000).endVertex();
        buffer.pos(left, bottom, 0).color(-16777216).endVertex();
        buffer.pos(right, bottom, 0).color(-16777216).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.enable(GL11.GL_TEXTURE_2D);
    }
}
