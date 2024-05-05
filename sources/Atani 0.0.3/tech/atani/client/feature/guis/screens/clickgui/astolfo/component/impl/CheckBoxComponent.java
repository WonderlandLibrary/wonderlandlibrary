package tech.atani.client.feature.guis.screens.clickgui.astolfo.component.impl;

import net.minecraft.client.gui.FontRenderer;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.feature.value.Value;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.font.storage.FontStorage;

import java.awt.*;

public class CheckBoxComponent extends ValueComponent {

    public CheckBoxComponent(Value value, float posX, float posY, float baseWidth, float baseHeight) {
        super(value, posX, posY, baseWidth, baseHeight);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        FontRenderer normal = FontStorage.getInstance().findFont("SFUI Medium", 16);
        normal.drawString(value.getName(), getPosX() + 5 + getAddX(), getPosY() + getBaseHeight() / 2 - normal.FONT_HEIGHT / 2, -1);
        normal.drawString("X", getPosX() + this.getBaseWidth() - 5 - normal.getStringWidthInt("X") + getAddX(), getPosY() + getBaseHeight() / 2 - normal.FONT_HEIGHT / 2, !((boolean)value.getValue()) ? new Color(200, 200, 200).getRGB() : -1);
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtil.isHovered(mouseX, mouseY, this.getPosX(), this.getPosY(), this.getBaseWidth(), this.getBaseHeight())) {
            CheckBoxValue checkBoxValue = (CheckBoxValue) value;
            checkBoxValue.setValue(!checkBoxValue.getValue());
        }
    }
}
