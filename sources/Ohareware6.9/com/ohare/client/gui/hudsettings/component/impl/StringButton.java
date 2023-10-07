package com.ohare.client.gui.hudsettings.component.impl;

import java.awt.Color;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import com.ohare.client.gui.hudsettings.component.Component;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.font.Fonts;
import com.ohare.client.utils.value.impl.StringValue;

/**
 * made by Xen for OhareWare
 *
 * @since 7/26/2019
 **/
public class StringButton extends Component {
    private StringValue stringValue;
    private boolean editinig;
    private String content = "";

    public StringButton(StringValue stringValue, float posX, float posY) {
        super(stringValue.getLabel(), posX, posY);
        this.stringValue = stringValue;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        boolean isHovered = mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY(), Fonts.clickfont.getStringWidth(isEditinig() ? content : getLabel() + ": " + StringUtils.capitalize(stringValue.getValue().toLowerCase())) + 4, 10);
        RenderUtil.drawBorderedRect(getPosX(), getPosY(), Fonts.clickfont.getStringWidth(isEditinig() ? content : getLabel() + ": " + StringUtils.capitalize(stringValue.getValue().toLowerCase())) + 4, 10, 0.5, new Color(36, 41, 51, 255).getRGB(), isHovered ? new Color(0x505760).getRGB() : new Color(0xFF3b4149).getRGB());

        Fonts.clickfont.drawStringWithShadow(isEditinig() ? content : getLabel() + ": " + stringValue.getValue(), getPosX() + 2, getPosY() + 3, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY(), Fonts.clickfont.getStringWidth(getLabel() + ": " + StringUtils.capitalize(stringValue.getValue().toLowerCase())) + 4, 10)) {
            if(mouseButton == 0) {
                setEditinig(!isEditinig());
                if (isEditinig()) {
                    content = stringValue.getValue();
                }
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        super.keyTyped(typedChar, key);
        if (isEditinig()) {
            String specialChars = "/*!@#$%^&*()\"{}_[]|\\?/<>,.";
            if (key == Keyboard.KEY_BACK) {
                if (content.length() > 1) {
                    content = content.substring(0, content.length() - 1);
                } else if (content.length() == 1) {
                    content = "";
                }
            } else if (key == Keyboard.KEY_RETURN) {
                stringValue.setValue(content);
                content = "";
                setEditinig(false);
            } else if (Character.isLetterOrDigit(typedChar) || Character.isSpaceChar(typedChar) || specialChars.contains(Character.toString(typedChar))) {
                if (Fonts.hudfont.getStringWidth(content) < 230) {
                    content += Character.toString(typedChar);
                }
            }
        }

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    private void setEditinig(boolean editinig) {
        this.editinig = editinig;
    }

    private boolean isEditinig() {
        return editinig;
    }
}