package tech.atani.client.feature.guis.screens.clickgui.oldaugustus.window.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.feature.value.impl.CheckBoxValue;

import java.awt.*;

public class CheckboxComponent extends ValueComponent {

    private final CheckBoxValue checkBoxValue;
    private FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

    public CheckboxComponent(CheckBoxValue checkBoxValue, float posX, float posY, float height) {
        super(checkBoxValue, posX, posY, height);
        this.checkBoxValue = checkBoxValue;
    }

    @Override
    public float draw(int mouseX, int mouseY) {
        fontRenderer.drawStringWithShadow("x", getPosX() + 5, getPosY() + getFinalHeight() / 2 - fontRenderer.FONT_HEIGHT / 2 - 1, !checkBoxValue.getValue() ? new Color(139, 141, 145, 255).getRGB() : new Color(41, 146, 222).getRGB());
        fontRenderer.drawStringWithShadow(checkBoxValue.getName(), getPosX() + 13 + fontRenderer.getStringWidthInt("X"), getPosY() + getFinalHeight() / 2 - fontRenderer.FONT_HEIGHT / 2, new Color(139, 141, 145, 255).getRGB());
        return fontRenderer.getStringWidthInt(checkBoxValue.getName()) + 15 + fontRenderer.getStringWidthInt("X");
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtil.isHovered(mouseX, mouseY, getPosX(), getPosY(), getWidth(), getHeight()) && mouseButton == 0) {
            checkBoxValue.setValue(!checkBoxValue.getValue());
        }
    }

}
