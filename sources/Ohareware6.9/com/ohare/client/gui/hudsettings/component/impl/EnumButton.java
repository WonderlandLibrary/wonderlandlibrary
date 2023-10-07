package com.ohare.client.gui.hudsettings.component.impl;

import java.awt.Color;

import org.apache.commons.lang3.StringUtils;

import com.ohare.client.gui.hudsettings.component.Component;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.font.Fonts;
import com.ohare.client.utils.value.impl.EnumValue;

/**
 * made by oHare for oHareWare
 *
 * @since 7/26/2019
 **/
public class EnumButton extends Component {
    private EnumValue enumValue;

    public EnumButton(EnumValue enumValue, float posX, float posY) {
        super(enumValue.getLabel(), posX, posY);
        this.enumValue = enumValue;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        boolean isHovered = mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY(), Fonts.clickfont.getStringWidth(getLabel() + ": " + StringUtils.capitalize(enumValue.getValue().toString().toLowerCase())) + 4, 10);
        RenderUtil.drawBorderedRect(getPosX(), getPosY(), Fonts.clickfont.getStringWidth(getLabel() + ": " + StringUtils.capitalize(enumValue.getValue().toString().toLowerCase())) + 4, 10, 0.5, new Color(36, 41, 51, 255).getRGB(), isHovered ? new Color(0x505760).getRGB() : new Color(0xFF3b4149).getRGB());
        Fonts.clickfont.drawStringWithShadow(getLabel() + ": " + StringUtils.capitalize(enumValue.getValue().toString().toLowerCase()), getPosX() + 2, getPosY() + 3, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean isHovered = mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY(), Fonts.clickfont.getStringWidth(getLabel() + ": " + StringUtils.capitalize(enumValue.getValue().toString().toLowerCase())) + 4, 10);
        if (isHovered) {
            if (mouseButton == 0) enumValue.increment();
            else if (mouseButton == 1) enumValue.decrement();
        }
    }
}
