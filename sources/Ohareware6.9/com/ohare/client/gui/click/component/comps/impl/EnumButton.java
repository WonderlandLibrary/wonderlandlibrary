package com.ohare.client.gui.click.component.comps.impl;

import java.awt.Color;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Mouse;

import com.ohare.client.gui.click.component.Component;
import com.ohare.client.gui.click.component.comps.ModButton;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.font.Fonts;
import com.ohare.client.utils.value.impl.EnumValue;

/**
 * made by oHare for oHareWare
 *
 * @since 6/5/2019
 **/
public class EnumButton extends Component {
    private EnumValue enumValue;
    private ModButton modButton;
    public EnumButton(ModButton modButton,EnumValue enumValue, float posX, float posY, float width, float height) {
        super(enumValue.getLabel(), posX, posY, width, height);
        this.enumValue = enumValue;
        this.modButton = modButton;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        boolean isHovered = mouseWithinBounds(mouseX,mouseY,getPosX(), getPosY(), 10, 10);
        RenderUtil.drawBorderedRect(getPosX(), getPosY(), 10, 10,0.5, new Color(36,41,51,255).getRGB(),isHovered ? new Color(0x505760).getRGB() : new Color(0xFF3b4149).getRGB());
        Fonts.clickfont.drawString( modButton.getParent().getParent().isEnumextended() && modButton.getParent().getParent().getExtendedenum() == enumValue ? "<" : ">", getPosX() + 4.5f - Fonts.clickfont.getStringWidth(modButton.getParent().getParent().isEnumextended() && modButton.getParent().getParent().getExtendedenum() == enumValue ? "<" : ">") / 2, getPosY() + 3, modButton.getParent().getParent().isEnumextended() && modButton.getParent().getParent().getExtendedenum() == enumValue ? new Color(0, 107, 214, 255).getRGB():-1);
        Fonts.sliderfont.drawString(enumValue.getLabel() + ": " + StringUtils.capitalize(enumValue.getValue().toString().toLowerCase()), getPosX() + 12, getPosY() + 4, -1);
        if (modButton.getParent().getParent().isEnumextended() && modButton.getParent().getParent().getExtendedenum() == enumValue) {
            ArrayList<String> array = new ArrayList<>();
            for (Enum en : enumValue.getConstants()) {
                array.add(en.name());
            }
            int largestString = (Fonts.clickfont.getStringWidth(array.get(0)));
            for (int i = 0; i < array.size(); i++) {
                if (Fonts.clickfont.getStringWidth(array.get(i))> largestString) {
                    largestString = Fonts.clickfont.getStringWidth(array.get(i));
                }
            }
            if (18 + ((enumValue.getConstants().length - 1) * 14) > 178) {
                if (mouseWithinBounds(mouseX, mouseY, modButton.getParent().getPosX() + modButton.getParent().getLargestString() * 2 + 20 + (((modButton.getLargestString() + 14) > (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14) ? (modButton.getLargestString() + 14) : (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14)) > 100 ? ((modButton.getLargestString() + 14) > (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14) ? (modButton.getLargestString() + 14) : (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14)) : 96) - 4,modButton.getParent().getPosY(),largestString + 8 > Fonts.hudfont.getStringWidth(enumValue.getLabel() + "'s Values") + 8 ? largestString + 8:Fonts.hudfont.getStringWidth(enumValue.getLabel() + "'s Values") + 8,20 + ((enumValue.getConstants().length - 1) * 14) > 180 ? 180 : 20 + ((enumValue.getConstants().length - 1) * 14))) {
                    int wheel = Mouse.getDWheel();
                    if (Mouse.hasWheel()) {
                        if (wheel > 0) {
                            modButton.getParent().getParent().setEnumscrollY(modButton.getParent().getParent().getEnumscrollY() - 4);
                        } else if (wheel < 0) {
                            if (modButton.getParent().getParent().getEnumscrollY() >= ((enumValue.getConstants().length - 1) * 14)) {
                                modButton.getParent().getParent().setEnumscrollY(((enumValue.getConstants().length - 1) * 14));
                            } else {
                                modButton.getParent().getParent().setEnumscrollY(modButton.getParent().getParent().getEnumscrollY() + 4);
                            }
                        }
                        if (modButton.getParent().getParent().getEnumscrollY() < 0) {
                            modButton.getParent().getParent().setEnumscrollY(0);
                        }
                    }
                }
            } else {
                if (modButton.getParent().getParent().getEnumscrollY() != 0) modButton.getParent().getParent().setEnumscrollY(0);
            }
            RenderUtil.drawBorderedRect(modButton.getParent().getPosX() + modButton.getParent().getLargestString() * 2 + 20 + (((modButton.getLargestString() + 14) > (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14) ? (modButton.getLargestString() + 14) : (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14)) > 100 ? ((modButton.getLargestString() + 14) > (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14) ? (modButton.getLargestString() + 14) : (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14)) : 96) - 4,modButton.getParent().getPosY(),largestString + 8 > Fonts.hudfont.getStringWidth(enumValue.getLabel() + "'s Values") - 8 ? largestString + 16:Fonts.hudfont.getStringWidth(enumValue.getLabel() + "'s Values") + 8,20 + ((enumValue.getConstants().length - 1) * 14) > 180 ? 180 : 20 + ((enumValue.getConstants().length - 1) * 14),0.5, new Color(28, 34, 44, 255).getRGB(), new Color(31, 38, 48, 255).getRGB());
            Fonts.hudfont.drawString(enumValue.getLabel() + "'s Values", modButton.getParent().getPosX() + modButton.getParent().getLargestString() * 2 + 24 + (((modButton.getLargestString() + 14) > (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14) ? (modButton.getLargestString() + 14) : (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14)) > 100 ? ((modButton.getLargestString() + 14) > (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14) ? (modButton.getLargestString() + 14) : (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14)) : 96) - 4, modButton.getParent().getPosY() + 6 - modButton.getParent().getParent().getEnumscrollY(), -1);
            int y = modButton.getParent().getPosY() + 20 - modButton.getParent().getParent().getEnumscrollY();
            for (Enum en : enumValue.getConstants()) {
                if (en != enumValue.getValue()) {
                    RenderUtil.drawRoundedRect(modButton.getParent().getPosX() + modButton.getParent().getLargestString() * 2 + 24 + (((modButton.getLargestString() + 14) > (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14) ? (modButton.getLargestString() + 14) : (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14)) > 100 ? ((modButton.getLargestString() + 14) > (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14) ? (modButton.getLargestString() + 14) : (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14)) : 96) - 4,y - 3,largestString + 8 > Fonts.hudfont.getStringWidth(enumValue.getLabel() + "'s Values") + 8 ? largestString + 8:Fonts.hudfont.getStringWidth(enumValue.getLabel() + "'s Values"),10,4,(mouseWithinBounds(mouseX, mouseY, modButton.getParent().getPosX() + modButton.getParent().getLargestString() * 2 + 24 + (((modButton.getLargestString() + 14) > (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14) ? (modButton.getLargestString() + 14) : (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14)) > 100 ? ((modButton.getLargestString() + 14) > (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14) ? (modButton.getLargestString() + 14) : (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14)) : 96) - 4,y - 3,largestString + 8 > Fonts.hudfont.getStringWidth(enumValue.getLabel() + "'s Values") + 8 ? largestString + 8:Fonts.hudfont.getStringWidth(enumValue.getLabel() + "'s Values"),10) ?  new Color(0, 130, 240, 255).getRGB() : new Color(0, 107, 214, 255).getRGB()));
                    Fonts.clickfont.drawString(StringUtils.capitalize(en.name().toLowerCase()),modButton.getParent().getPosX() + modButton.getParent().getLargestString() * 2 + 24 + (((modButton.getLargestString() + 14) > (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14) ? (modButton.getLargestString() + 14) : (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14)) > 100 ? ((modButton.getLargestString() + 14) > (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14) ? (modButton.getLargestString() + 14) : (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14)) : 96) - 4 + (largestString + 8 > Fonts.hudfont.getStringWidth(enumValue.getLabel() + "'s Values") + 8 ? largestString + 8:Fonts.hudfont.getStringWidth(enumValue.getLabel() + "'s Values")) / 2 - Fonts.clickfont.getStringWidth(StringUtils.capitalize(en.name().toLowerCase())) / 2,y + 0.5f,-1);
                    y += 14;
                }
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseWithinBounds(mouseX,mouseY,getPosX(), getPosY(), 10, 10)) {
            if (mouseButton == 0) {
                if (modButton.getParent().getParent().isEnumextended() && modButton.getParent().getParent().getExtendedenum() == enumValue) {
                    modButton.getParent().getParent().setEnumextended(false);
                    modButton.getParent().getParent().setExtendedenum(null);
                    modButton.getParent().getParent().setEnumscrollY(0);
                } else {
                    modButton.getParent().getParent().setEnumextended(true);
                    modButton.getParent().getParent().setExtendedenum(enumValue);
                    modButton.getParent().getParent().setEnumscrollY(0);
                }
            }
        }
        if (modButton.getParent().getParent().isEnumextended() && modButton.getParent().getParent().getExtendedenum() == enumValue) {
            ArrayList<String> array = new ArrayList<>();
            for (Enum en : enumValue.getConstants()) {
                array.add(en.name());
            }
            int largestString = (Fonts.clickfont.getStringWidth(array.get(0)));
            for (int i = 0; i < array.size(); i++) {
                if (Fonts.clickfont.getStringWidth(array.get(i))> largestString) {
                    largestString = Fonts.clickfont.getStringWidth(array.get(i));
                }
            }
            int y = modButton.getParent().getPosY() + 20 - modButton.getParent().getParent().getEnumscrollY();
            for (Enum en : enumValue.getConstants()) {
                if (en != enumValue.getValue()) {
                    if (mouseButton == 0 && modButton.mouseWithinBounds(mouseX,mouseY,modButton.getParent().getPosX() + modButton.getParent().getLargestString() * 2 + 24 + (((modButton.getLargestString() + 14) > (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14) ? (modButton.getLargestString() + 14) : (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14)) > 100 ? ((modButton.getLargestString() + 14) > (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14) ? (modButton.getLargestString() + 14) : (Fonts.hudfont.getStringWidth(modButton.getParent().getParent().getExtendedmod().getLabel() + " Settings") + 14)) : 96) - 4,y - 3,largestString + 8 > Fonts.hudfont.getStringWidth(enumValue.getLabel() + "'s Values") + 8 ? largestString + 8:Fonts.hudfont.getStringWidth(enumValue.getLabel() + "'s Values"),10)) {
                        enumValue.setValue(en);
                        modButton.getParent().getParent().setEnumextended(false);
                        modButton.getParent().getParent().setExtendedenum(null);
                        modButton.getParent().getParent().setEnumscrollY(0);
                    }
                    y += 14;
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

}
