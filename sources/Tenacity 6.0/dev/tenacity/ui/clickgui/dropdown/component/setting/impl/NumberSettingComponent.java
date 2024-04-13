package dev.tenacity.ui.clickgui.dropdown.component.setting.impl;

import dev.tenacity.setting.impl.NumberSetting;
import dev.tenacity.ui.clickgui.dropdown.component.setting.SettingPanelComponent;
import dev.tenacity.util.misc.HoverUtil;
import dev.tenacity.util.misc.MathUtil;
import dev.tenacity.util.render.ColorUtil;
import dev.tenacity.util.render.RenderUtil;
import dev.tenacity.util.render.animation.ContinualAnimation;
import dev.tenacity.util.render.font.CustomFont;
import dev.tenacity.util.render.font.FontUtil;
import dev.tenacity.util.render.shader.impl.RoundedUtil;

import java.awt.*;

public final class NumberSettingComponent extends SettingPanelComponent<NumberSetting> {

    private boolean dragging;

    private final float sliderWidth = getWidth() - 10;

    private final ContinualAnimation dragAnimation = new ContinualAnimation();

    public NumberSettingComponent(final NumberSetting setting) {
        super(setting);
    }

    @Override
    public void initGUI() {

    }

    @Override
    public void keyTyped(final char typedChar, final int keyCode) {

    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        final CustomFont font = FontUtil.getFont("OpenSans-SemiBold", 14);

        RenderUtil.drawRect(getPosX(), getPosY(), getWidth(), getHeight(), ColorUtil.getSurfaceColor().getRGB());

        if(dragging) {
            final float percent = Math.min(1, Math.max(0, (mouseX - getPosX() + 3.5f) / sliderWidth));
            final double newValue = MathUtil.interpolate(getSetting().getMinValue(), getSetting().getMaxValue(), percent);

            getSetting().setCurrentValue(newValue);
        }
        final double widthPercentage = ((getSetting().getCurrentValue()) - getSetting().getMinValue()) / (getSetting().getMaxValue() - getSetting().getMinValue());

        dragAnimation.animate((float) (sliderWidth * widthPercentage), 15);

        final String text = getSetting().name + " " + getSetting().getCurrentValue();
        font.drawString(text, getPosX() + (getWidth() / 2f) - (font.getStringWidth(text) / 2), getPosY() + 0.5f, -1);

        RoundedUtil.drawRound(getPosX() + 3.5f, getPosY() + (getHeight() / 4f) + 6, sliderWidth, 3, 1, ColorUtil.getSurfaceVariantColor());
        RoundedUtil.drawRound(getPosX() + 3.5f, getPosY() + (getHeight() / 4f) + 6, dragAnimation.getOutput(), 3, 1, ColorUtil.getPrimaryColor());
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if(HoverUtil.isHovering(getPosX() + 3.5f, getPosY(), sliderWidth, getHeight(), mouseX, mouseY))
            dragging = true;
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if(dragging) dragging = false;
    }
}
