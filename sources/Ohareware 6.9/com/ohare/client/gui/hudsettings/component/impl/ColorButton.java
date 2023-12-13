package com.ohare.client.gui.hudsettings.component.impl;

import com.ohare.client.gui.hudsettings.component.Component;
import com.ohare.client.utils.value.impl.ColorValue;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;


/**
 * made by Xen for OhareWare
 *
 * @since 7/26/2019
 **/
public class ColorButton extends Component {
    private ColorValue colorValue;

    public ColorButton(ColorValue colorValue, float posX, float posY) {
        super(colorValue.getLabel(), posX, posY);
        this.colorValue = colorValue;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);

    }
}
