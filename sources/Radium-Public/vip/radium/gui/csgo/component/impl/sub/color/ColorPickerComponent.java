package vip.radium.gui.csgo.component.impl.sub.color;

import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import vip.radium.gui.csgo.SkeetUI;
import vip.radium.gui.csgo.component.ButtonComponent;
import vip.radium.gui.csgo.component.Component;
import vip.radium.gui.csgo.component.ExpandableComponent;
import vip.radium.gui.csgo.component.PredicateComponent;
import vip.radium.property.ValueChangeListener;
import vip.radium.utils.render.LockedResolution;
import vip.radium.utils.render.RenderingUtils;

import java.awt.*;

public abstract class ColorPickerComponent extends ButtonComponent implements PredicateComponent, ExpandableComponent {

    private static final int MARGIN = 3;
    private static final int SLIDER_THICKNESS = 8;

    private static final float SELECTOR_WIDTH = 1;
    private static final float HALF_WIDTH = SELECTOR_WIDTH / 2;

    private static final float OUTLINE_WIDTH = 0.5F;

    private boolean expanded;

    private float hue;
    private float saturation;
    private float brightness;
    private float alpha;

    private boolean colorSelectorDragging;
    private boolean hueSelectorDragging;
    private boolean alphaSelectorDragging;

    public ColorPickerComponent(Component parent, float x, float y, float width, float height) {
        super(parent, x, y, width, height);

        addValueChangeListener(this::onValueChange);
    }

    private static void drawCheckeredBackground(float x, float y, float x2, float y2) {
        Gui.drawRect(x, y, x2, y2, SkeetUI.getColor(0xFFFFFF));

        for (boolean offset = false; y < y2; y++) {
            for (float x1 = x + ((offset = !offset) ? 1 : 0); x1 < x2; x1 += 2) {
                if (x1 > x2 - 1)
                    continue;
                Gui.drawRect(x1, y, x1 + 1, y + 1, SkeetUI.getColor(0x808080));
            }
        }
    }

    @Override
    public void drawComponent(LockedResolution lockedResolution, int mouseX, int mouseY) {
        final float x = getX();
        final float y = getY();
        final float width = getWidth();
        final float height = getHeight();

        final int black = SkeetUI.getColor(0);

        Gui.drawRect(x - 0.5, y - 0.5, x + width + 0.5, y + height + 0.5, black);

        final int guiAlpha = (int) SkeetUI.getAlpha();

        {
            final int color = getColor();

            final int colorAlpha = color >> 24 & 0xFF;

            final int minAlpha = Math.min(guiAlpha, colorAlpha);

            if (colorAlpha < 255)
                drawCheckeredBackground(x, y, x + width, y + height);

            final int newColor = new Color(
                    color >> 16 & 0xFF,
                    color >> 8 & 0xFF,
                    color & 0xFF,
                    minAlpha).getRGB();

            Gui.drawGradientRect(x, y, x + width, y + height, newColor, RenderingUtils.darker(newColor));
        }

        if (isExpanded()) {
            GL11.glTranslated(0, 0, 3.0);

            final float expandedX = getExpandedX();
            final float expandedY = getExpandedY();

            final float expandedWidth = getExpandedWidth();
            final float expandedHeight = getExpandedHeight();

            // Background
            {
                Gui.drawRect(expandedX, expandedY,
                        expandedX + expandedWidth, expandedY + expandedHeight, black);

                Gui.drawRect(expandedX + 0.5, expandedY + 0.5,
                        expandedX + expandedWidth - 0.5, expandedY + expandedHeight - 0.5, SkeetUI.getColor(0x39393B));

                Gui.drawRect(expandedX + 1, expandedY + 1,
                        expandedX + expandedWidth - 1, expandedY + expandedHeight - 1, SkeetUI.getColor(0x232323));
            }

            final float colorPickerSize = expandedWidth - MARGIN * 3 - SLIDER_THICKNESS;

            final float colorPickerLeft = expandedX + MARGIN;
            final float colorPickerTop = expandedY + MARGIN;
            final float colorPickerRight = colorPickerLeft + colorPickerSize;
            final float colorPickerBottom = colorPickerTop + colorPickerSize;

            final int selectorWhiteOverlayColor = new Color(0xFF, 0xFF, 0xFF, Math.min(guiAlpha, 180)).getRGB();

            // Color picker
            {
                if (mouseX <= colorPickerLeft || mouseY <= colorPickerTop || mouseX >= colorPickerRight || mouseY >= colorPickerBottom)
                    colorSelectorDragging = false;

                Gui.drawRect(colorPickerLeft - 0.5, colorPickerTop - 0.5,
                        colorPickerRight + 0.5, colorPickerBottom + 0.5, SkeetUI.getColor(0));

                drawColorPickerRect(colorPickerLeft, colorPickerTop, colorPickerRight, colorPickerBottom);

                float colorSelectorX = saturation * (colorPickerRight - colorPickerLeft);
                float colorSelectorY = (1 - brightness) * (colorPickerBottom - colorPickerTop);

                if (colorSelectorDragging) {
                    float wWidth = colorPickerRight - colorPickerLeft;
                    float xDif = mouseX - colorPickerLeft;
                    this.saturation = xDif / wWidth;
                    colorSelectorX = xDif;

                    float hHeight = colorPickerBottom - colorPickerTop;
                    float yDif = mouseY - colorPickerTop;
                    this.brightness = 1 - (yDif / hHeight);
                    colorSelectorY = yDif;

                    updateColor(Color.HSBtoRGB(hue, saturation, brightness), false);
                }

                // Color selector
                {
                    final float csLeft = colorPickerLeft + colorSelectorX - HALF_WIDTH;
                    final float csTop = colorPickerTop + colorSelectorY - HALF_WIDTH;
                    final float csRight = colorPickerLeft + colorSelectorX + HALF_WIDTH;
                    final float csBottom = colorPickerTop + colorSelectorY + HALF_WIDTH;


                    Gui.drawRect(csLeft - OUTLINE_WIDTH, csTop - OUTLINE_WIDTH, csLeft, csBottom + OUTLINE_WIDTH,
                            black);

                    Gui.drawRect(csRight, csTop - OUTLINE_WIDTH, csRight + OUTLINE_WIDTH, csBottom + OUTLINE_WIDTH,
                            black);

                    Gui.drawRect(csLeft, csTop - OUTLINE_WIDTH, csRight, csTop,
                            black);

                    Gui.drawRect(csLeft, csBottom, csRight, csBottom + OUTLINE_WIDTH,
                            black);

                    Gui.drawRect(csLeft, csTop, csRight, csBottom, selectorWhiteOverlayColor);
                }
            }

            // Hue bar
            {
                final float hueSliderLeft = colorPickerRight + MARGIN;
                final float hueSliderTop = colorPickerTop;
                final float hueSliderRight = hueSliderLeft + SLIDER_THICKNESS;
                final float hueSliderBottom = colorPickerBottom;

                if (mouseX <= hueSliderLeft || mouseY <= hueSliderTop || mouseX >= hueSliderRight || mouseY >= hueSliderBottom)
                    hueSelectorDragging = false;

                final float hueSliderYDif = hueSliderBottom - hueSliderTop;

                float hueSelectorY = (1 - this.hue) * hueSliderYDif;

                if (hueSelectorDragging) {
                    float yDif = mouseY - hueSliderTop;
                    this.hue = 1 - (yDif / hueSliderYDif);
                    hueSelectorY = yDif;

                    updateColor(Color.HSBtoRGB(hue, saturation, brightness), false);
                }

                Gui.drawRect(hueSliderLeft - 0.5, hueSliderTop - 0.5, hueSliderRight + 0.5, hueSliderBottom + 0.5,
                        black);

                final float inc = 0.2F;
                final float times = 1 / inc;
                final float sHeight = hueSliderBottom - hueSliderTop;
                final float size = sHeight / times;
                float sY = hueSliderTop;

                // Draw colored hue bar
                for (int i = 0; i < times; i++) {
                    boolean last = i == times - 1;
                    Gui.drawGradientRect(hueSliderLeft, sY, hueSliderRight,
                            sY + size,
                            SkeetUI.getColor(Color.HSBtoRGB(1 - inc * i, 1.0F, 1.0F)),
                            SkeetUI.getColor(Color.HSBtoRGB(1 - inc * (i + 1), 1.0F, 1.0F)));
                    if (!last)
                        sY += size;
                }

                // Hue Selector
                {
                    final float hsTop = hueSliderTop + hueSelectorY - HALF_WIDTH;
                    final float hsBottom = hueSliderTop + hueSelectorY + HALF_WIDTH;

                    Gui.drawRect(hueSliderLeft - OUTLINE_WIDTH, hsTop - OUTLINE_WIDTH, hueSliderLeft, hsBottom + OUTLINE_WIDTH,
                            black);

                    Gui.drawRect(hueSliderRight, hsTop - OUTLINE_WIDTH, hueSliderRight + OUTLINE_WIDTH, hsBottom + OUTLINE_WIDTH,
                            black);

                    Gui.drawRect(hueSliderLeft, hsTop - OUTLINE_WIDTH, hueSliderRight, hsTop,
                            black);

                    Gui.drawRect(hueSliderLeft, hsBottom, hueSliderRight, hsBottom + OUTLINE_WIDTH,
                            black);

                    Gui.drawRect(hueSliderLeft, hsTop, hueSliderRight, hsBottom, selectorWhiteOverlayColor);
                }
            }

            // Alpha bar
            {
                final float alphaSliderLeft = colorPickerLeft;
                final float alphaSliderTop = colorPickerBottom + MARGIN;
                final float alphaSliderRight = colorPickerRight;
                final float alphaSliderBottom = alphaSliderTop + SLIDER_THICKNESS;

                if (mouseX <= alphaSliderLeft || mouseY <= alphaSliderTop || mouseX >= alphaSliderRight || mouseY >= alphaSliderBottom)
                    alphaSelectorDragging = false;

                int color = Color.HSBtoRGB(hue, saturation, brightness);

                int r = color >> 16 & 0xFF;
                int g = color >> 8 & 0xFF;
                int b = color & 0xFF;

                final float hsHeight = alphaSliderRight - alphaSliderLeft;

                float alphaSelectorX = alpha * hsHeight;

                if (alphaSelectorDragging) {
                    float xDif = mouseX - alphaSliderLeft;
                    this.alpha = xDif / hsHeight;
                    alphaSelectorX = xDif;

                    updateColor(new Color(r, g, b, (int) (alpha * 255)).getRGB(), true);
                }

                Gui.drawRect(alphaSliderLeft - 0.5, alphaSliderTop - 0.5, alphaSliderRight + 0.5, alphaSliderBottom + 0.5, black);

                drawCheckeredBackground(alphaSliderLeft, alphaSliderTop, alphaSliderRight, alphaSliderBottom);

                RenderingUtils.drawGradientRect(alphaSliderLeft, alphaSliderTop, alphaSliderRight,
                        alphaSliderBottom,
                        true,
                        new Color(r, g, b, 0).getRGB(),
                        new Color(r, g, b, Math.min(guiAlpha, 0xFF)).getRGB());

                // Alpha selector
                {
                    final float asLeft = alphaSliderLeft + alphaSelectorX - HALF_WIDTH;
                    final float asTop = alphaSliderTop;
                    final float asRight = alphaSliderLeft + alphaSelectorX + HALF_WIDTH;
                    final float asBottom = alphaSliderBottom;


                    Gui.drawRect(asLeft - OUTLINE_WIDTH,
                            asTop,
                            asRight + OUTLINE_WIDTH,
                            asBottom,
                            black);

                    Gui.drawRect(asLeft,
                            asTop,
                            asRight,
                            asBottom,
                            selectorWhiteOverlayColor);
                }
            }

            GL11.glTranslated(0, 0, -3.0);
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        super.onMouseClick(mouseX, mouseY, button);

        if (isExpanded() && button == 0) {
            final float expandedX = getExpandedX();
            final float expandedY = getExpandedY();

            final float expandedWidth = getExpandedWidth();
            final float expandedHeight = getExpandedHeight();

            final float colorPickerSize = expandedWidth - MARGIN * 3 - SLIDER_THICKNESS;

            final float colorPickerLeft = expandedX + MARGIN;
            final float colorPickerTop = expandedY + MARGIN;
            final float colorPickerRight = colorPickerLeft + colorPickerSize;
            final float colorPickerBottom = colorPickerTop + colorPickerSize;

            final float alphaSliderLeft = colorPickerLeft;
            final float alphaSliderTop = colorPickerBottom + MARGIN;
            final float alphaSliderRight = colorPickerRight;
            final float alphaSliderBottom = alphaSliderTop + SLIDER_THICKNESS;

            final float hueSliderLeft = colorPickerRight + MARGIN;
            final float hueSliderTop = colorPickerTop;
            final float hueSliderRight = hueSliderLeft + SLIDER_THICKNESS;
            final float hueSliderBottom = colorPickerBottom;

            colorSelectorDragging = !colorSelectorDragging && mouseX > colorPickerLeft && mouseY > colorPickerTop &&
                    mouseX < colorPickerRight && mouseY < colorPickerBottom;

            alphaSelectorDragging = !alphaSelectorDragging && mouseX > alphaSliderLeft && mouseY > alphaSliderTop &&
                    mouseX < alphaSliderRight && mouseY < alphaSliderBottom;

            hueSelectorDragging = !hueSelectorDragging && mouseX > hueSliderLeft && mouseY > hueSliderTop &&
                    mouseX < hueSliderRight && mouseY < hueSliderBottom;
        }
    }

    @Override
    public void onMouseRelease(int button) {
        if (colorSelectorDragging)
            colorSelectorDragging = false;
        if (alphaSelectorDragging)
            alphaSelectorDragging = false;
        if (hueSelectorDragging)
            hueSelectorDragging = false;
    }

    private void updateColor(int hex, boolean hasAlpha) {
        if (hasAlpha)
            setColor(hex);
        else {
            setColor(new Color(
                    hex >> 16 & 0xFF,
                    hex >> 8 & 0xFF,
                    hex & 0xFF,
                    (int) (alpha * 255)).getRGB());
        }
    }

    public abstract int getColor();

    public abstract void setColor(int color);

    public abstract void addValueChangeListener(ValueChangeListener<Integer> valueChangeListener);

    public void onValueChange(int oldValue, int value) {
        float[] hsb = getHSBFromColor(value);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];

        this.alpha = (value >> 24 & 0xFF) / 255.0F;
    }

    private float[] getHSBFromColor(int hex) {
        int r = hex >> 16 & 0xFF;
        int g = hex >> 8 & 0xFF;
        int b = hex & 0xFF;
        return Color.RGBtoHSB(r, g, b, null);
    }

    private void drawColorPickerRect(float left, float top, float right, float bottom) {
        final int hueBasedColor = SkeetUI.getColor(Color.HSBtoRGB(hue, 1.0F, 1.0F));

        RenderingUtils.drawGradientRect(left, top,right, bottom, true, SkeetUI.getColor(0xFFFFFF), hueBasedColor);

        Gui.drawGradientRect(left, top, right, bottom, 0, SkeetUI.getColor(0x000000));
    }

    @Override
    public float getExpandedX() {
        return getX() + getWidth() - SkeetUI.HALF_GROUP_BOX * 2;
    }

    @Override
    public float getExpandedY() {
        return getY() + getHeight();
    }

    @Override
    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public void onPress(int mouseButton) {
        if (mouseButton == 1)
            setExpanded(!isExpanded());
    }

    @Override
    public float getExpandedWidth() {
        final float right = getX() + getWidth();
        return right - getExpandedX();
    }

    @Override
    public float getExpandedHeight() {
        return getExpandedWidth();
    }
}
