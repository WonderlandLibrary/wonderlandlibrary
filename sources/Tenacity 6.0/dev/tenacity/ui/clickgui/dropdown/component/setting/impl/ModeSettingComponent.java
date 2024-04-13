package dev.tenacity.ui.clickgui.dropdown.component.setting.impl;

import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.ui.clickgui.dropdown.component.setting.SettingPanelComponent;
import dev.tenacity.util.misc.ChatUtil;
import dev.tenacity.util.misc.HoverUtil;
import dev.tenacity.util.render.ColorUtil;
import dev.tenacity.util.render.RenderUtil;
import dev.tenacity.util.render.font.CustomFont;
import dev.tenacity.util.render.font.FontUtil;
import dev.tenacity.util.render.shader.impl.RoundedUtil;

import java.awt.*;

public final class ModeSettingComponent extends SettingPanelComponent<ModeSetting> {

    private boolean expanded;

    public ModeSettingComponent(final ModeSetting setting) {
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

        RenderUtil.drawRect(getPosX(), getPosY(), getWidth(), getHeight() * getAddedHeight(), ColorUtil.getSurfaceColor().getRGB());

        RoundedUtil.drawRound(getPosX() + 2.5f, getPosY() + getHeight() / 4f, getWidth() - 5, (getHeight() * getAddedHeight()) - 8, getHeight() / 6f, ColorUtil.getSurfaceVariantColor());

        font.drawString(getSetting().name, getPosX() + 2.5f + ((getWidth() / 2f) - 5) - (font.getStringWidth(getSetting().name) / 2f), getPosY() + getHeight() / 4f, -1);

        if(expanded) {
            int xOffset = 0;
            for(final String mode : getSetting().getModeList()) {
                font.drawString(mode, getPosX() + 3, getPosY() + getHeight() / 4f + ((getHeight() * getAddedHeight()) - 8) / 4 + (xOffset * 10), getSetting().isMode(mode) ? -1 : new Color(240, 240, 240, 255).getRGB());

                xOffset++;
            }
        }
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if(HoverUtil.isHovering(getPosX() + 2.5f, getPosY() + getHeight() / 4f, getWidth() - 5, getHeight() / 2f, mouseX, mouseY) && mouseButton == 1) {
            expanded = !expanded;
            if(expanded)
                setAddedHeight(getSetting().getModeList().size());
            else
                setAddedHeight(1);
        }

        if(expanded && mouseButton == 0) {
            for(int i = 0; i < getSetting().getModeList().size(); i++) {
                if(HoverUtil.isHovering(getPosX() + 3, getPosY() + getHeight() / 4f + ((getHeight() * getAddedHeight()) - 8) / 4 + (i * 10), getWidth(), 5, mouseX, mouseY))
                    getSetting().setCurrentMode(i);
            }
        }
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {

    }
}
