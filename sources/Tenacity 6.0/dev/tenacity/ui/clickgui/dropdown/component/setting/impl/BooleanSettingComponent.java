package dev.tenacity.ui.clickgui.dropdown.component.setting.impl;

import dev.tenacity.setting.impl.BooleanSetting;
import dev.tenacity.ui.clickgui.dropdown.component.setting.SettingPanelComponent;
import dev.tenacity.util.misc.HoverUtil;
import dev.tenacity.util.render.ColorUtil;
import dev.tenacity.util.render.RenderUtil;
import dev.tenacity.util.render.animation.AbstractAnimation;
import dev.tenacity.util.render.animation.AnimationDirection;
import dev.tenacity.util.render.animation.impl.DecelerateAnimation;
import dev.tenacity.util.render.font.CustomFont;
import dev.tenacity.util.render.font.FontUtil;
import dev.tenacity.util.render.shader.impl.RoundedUtil;

import java.awt.*;

public final class BooleanSettingComponent extends SettingPanelComponent<BooleanSetting> {

    private final AbstractAnimation togglePositionAndColorAnimation = new DecelerateAnimation(200, 1, AnimationDirection.FORWARDS);

    public BooleanSettingComponent(final BooleanSetting setting) {
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

        togglePositionAndColorAnimation.setDirection(getSetting().isEnabled() ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);

        final Color dotColor = ColorUtil.interpolateColorC(ColorUtil.getSurfaceVariantColor(), ColorUtil.getPrimaryColor(), togglePositionAndColorAnimation.getOutput().floatValue());

        RenderUtil.drawRect(getPosX(), getPosY(), getWidth(), getHeight(), ColorUtil.getSurfaceColor().getRGB());
        font.drawString(getSetting().name, getPosX() + 3, getPosY() + (getHeight() / 4f), -1);

        RoundedUtil.drawRoundOutline(getPosX() + (getWidth() - 25), getPosY() + (getHeight() / 4f) - 1, 20, 10, 5, 0.5f, ColorUtil.getSurfaceColor(), ColorUtil.getSurfaceVariantColor());

        RoundedUtil.drawRound(getPosX() + (getWidth() - 25) + 3 + (togglePositionAndColorAnimation.getOutput().floatValue() * 8), getPosY() + (getHeight() / 4f) + 1, 6, 6, 2.5f, dotColor);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if(HoverUtil.isHovering(getPosX(), getPosY(), getWidth(), getHeight(), mouseX, mouseY))
            getSetting().toggle();
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {

    }
}
