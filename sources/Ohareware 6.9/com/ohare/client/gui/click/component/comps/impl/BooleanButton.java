package com.ohare.client.gui.click.component.comps.impl;

import java.awt.Color;

import com.ohare.client.gui.click.component.Component;
import com.ohare.client.gui.click.component.comps.ModButton;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.font.Fonts;
import com.ohare.client.utils.value.impl.BooleanValue;

/**
 * made by Xen for Insanity
 *
 * @since 6/4/2019
 **/
public class BooleanButton extends Component {
    private BooleanValue booleanValue;
    private ModButton modButton;

    public BooleanButton(ModButton modButton,BooleanValue booleanValue, float posX, float posY, float width, float height) {
        super(booleanValue.getLabel(), posX, posY, width, height);
        this.booleanValue = booleanValue;
        this.modButton = modButton;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        boolean isHovered = mouseWithinBounds(mouseX,mouseY,getPosX(), getPosY(), 10, 10);
        RenderUtil.drawBorderedRect(getPosX(), getPosY(), 10, 10,0.5, new Color(36,41,51,255).getRGB(),isHovered ? new Color(0x505760).getRGB() : new Color(0xFF3b4149).getRGB());
        Fonts.clickfont.drawString(booleanValue.getLabel(), getPosX() + 12, getPosY() + 3, -1);
        if(booleanValue.isEnabled()) {
            RenderUtil.drawCheckMark(getPosX() + 5,getPosY() - 2,10,new Color(0, 107, 214, 255).getRGB());
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);


        if(mouseWithinBounds(mouseX,mouseY,getPosX(), getPosY(), 10, 10) && mouseButton == 0) {
            booleanValue.toggle();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }
}
