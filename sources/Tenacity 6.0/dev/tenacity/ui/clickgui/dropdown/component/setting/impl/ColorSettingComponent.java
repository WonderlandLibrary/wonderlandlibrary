package dev.tenacity.ui.clickgui.dropdown.component.setting.impl;

import dev.tenacity.setting.impl.ColorSetting;
import dev.tenacity.ui.clickgui.dropdown.component.setting.SettingPanelComponent;
import dev.tenacity.util.misc.HoverUtil;
import dev.tenacity.util.render.ColorUtil;
import dev.tenacity.util.render.RenderUtil;
import dev.tenacity.util.render.font.CustomFont;
import dev.tenacity.util.render.font.FontUtil;
import dev.tenacity.util.render.shader.impl.RoundedUtil;

import java.awt.*;

public final class ColorSettingComponent extends SettingPanelComponent<ColorSetting> {

    private boolean draggingPicker, draggingHue;

    public ColorSettingComponent(final ColorSetting setting) {
        super(setting);
        setAddedHeight(5);
    }

    @Override
    public void initGUI() {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        final CustomFont font = FontUtil.getFont("OpenSans-SemiBold", 14);

        RenderUtil.drawRect(getPosX(), getPosY(), getWidth(), getHeight() * getAddedHeight(), ColorUtil.getSurfaceColor().getRGB());

        font.drawString(getSetting().name, getPosX() + (getWidth() / 2f) - (font.getStringWidth(getSetting().name) / 2), getPosY() + 1f, -1);

        final float gradientX = getPosX() + 3.5f,
                gradientY = getPosY() + 10f,
                gradientWidth = getWidth() - 7f,
                gradientHeight = getHeight() * getAddedHeight() - 25;
                
        if(draggingHue) {
            getSetting().setHue(Math.min(1, Math.max(0, (mouseX - (getPosX() + 2.5f)) / (gradientWidth))));
        } else if (draggingPicker) {
            getSetting().setBrightness(Math.min(1, Math.max(0, 1 - (mouseY - (gradientY)) / (gradientHeight))));
            getSetting().setSaturation(Math.min(1, Math.max(0, (mouseX - (gradientX)) / (gradientWidth))));
        }

        float[] hsb = {getSetting().getHue(), getSetting().getSaturation(), getSetting().getBrightness()};

        // Saturation & Brightness Picker
        RoundedUtil.drawRound(gradientX, gradientY, gradientWidth, gradientHeight, 5, Color.getHSBColor(hsb[0], 1, 1));
        RoundedUtil.drawGradientHorizontal(gradientX, gradientY, gradientWidth, gradientHeight, 5, Color.getHSBColor(hsb[0], 0, 1), ColorUtil.applyOpacity(Color.getHSBColor(hsb[0], 0, 1), 0));
        RoundedUtil.drawGradientVertical(gradientX, gradientY, gradientWidth, gradientHeight, 5, ColorUtil.applyOpacity(Color.getHSBColor(hsb[0], 1, 0), 0), Color.getHSBColor(hsb[0], 1, 0));

        RenderUtil.drawRainbowRect(getPosX() + 2.5f, getPosY() + 64.5f, gradientWidth, getHeight() * getAddedHeight() - 68);

        if(HoverUtil.isHovering(gradientX, gradientY, gradientWidth, gradientHeight, mouseX, mouseY) || HoverUtil.isHovering(getPosX() + 2.5f, getPosY() + 64.5f, gradientWidth, getHeight() * getAddedHeight() - 68, mouseX, mouseY)) {
            RoundedUtil.drawRound(mouseX-1.25F, mouseY-1.25F, 2.5F, 2.5F, 2F, new Color(255, 255, 255));
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        final float gradientX = getPosX() + 3.5f,
                gradientY = getPosY() + 10f,
                gradientWidth = getWidth() - 7f,
                gradientHeight = getHeight() * getAddedHeight() - 25;

        if(HoverUtil.isHovering(gradientX, gradientY, gradientWidth, gradientHeight, mouseX, mouseY)) {
            draggingPicker = true;
        } else if (HoverUtil.isHovering(getPosX() + 2.5f, getPosY() + 64.5f, gradientWidth, getHeight() * getAddedHeight() - 68, mouseX, mouseY)) {
            draggingHue = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        draggingPicker = draggingHue = false;
    }
}
