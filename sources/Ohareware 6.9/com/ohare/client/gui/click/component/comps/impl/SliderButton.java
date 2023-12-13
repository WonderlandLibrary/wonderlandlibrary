package com.ohare.client.gui.click.component.comps.impl;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.ohare.client.gui.click.component.Component;
import com.ohare.client.gui.click.component.comps.ModButton;
import com.ohare.client.utils.MathUtils;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.font.Fonts;
import com.ohare.client.utils.value.impl.NumberValue;

import net.minecraft.util.MathHelper;

/**
 * made by Xen for Insanity
 *
 * @since 6/4/2019
 **/
public class SliderButton extends Component {
    private ModButton modButton;
    private NumberValue numberValue;

    public SliderButton(ModButton modButton, NumberValue numberValue, float posX, float posY, float width, float height) {
        super(numberValue.getLabel(), posX, posY, width, height);
        this.modButton = modButton;
        this.numberValue = numberValue;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float sliderwidth = 74;
        float length = MathHelper
                .floor_double(((numberValue.getValue()).floatValue() - numberValue.getMinimum().floatValue())
                        / (numberValue.getMaximum().floatValue() - numberValue.getMinimum().floatValue()) * sliderwidth);
        boolean isHovered = mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY(), 80, 10);
        RenderUtil.drawBorderedRect(getPosX(), getPosY(), 80, 10, 0.5, new Color(36, 41, 51, 255).getRGB(), isHovered ? new Color(0x505760).getRGB() : new Color(0xFF3b4149).getRGB());
        RenderUtil.drawRect(getPosX() + length  + 1f,getPosY() + 1,4f,8,new Color(0, 107, 214, 255).getRGB());
        Fonts.sliderfont.drawString(numberValue.getLabel() + ": " + numberValue.getValue().toString(), getPosX() + 40 - Fonts.sliderfont.getStringWidth(numberValue.getLabel() + ": " + numberValue.getValue().toString()) / 2, getPosY() + 4, -1);

        if (isHovered) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && modButton.getParent().getParent().getTimer().sleep(100)) {
                if (numberValue.getValue() instanceof Double) {
                    numberValue
                            .setValue(MathUtils.round(numberValue.getValue().doubleValue()
                                    - numberValue.getInc().doubleValue(), 2));
                }
                if (numberValue.getValue() instanceof Float) {
                    numberValue.setValue(
                            (float) MathUtils.round(numberValue.getValue().floatValue()
                                    - numberValue.getInc().floatValue(), 2));

                }
                if (numberValue.getValue() instanceof Long) {
                    numberValue.setValue(
                            (long) MathUtils.round(numberValue.getValue().longValue()
                                    - numberValue.getInc().longValue(), 2));

                }
                if (numberValue.getValue() instanceof Integer) {
                    numberValue
                            .setValue((int) MathUtils.round(numberValue.getValue().intValue()
                                    - numberValue.getInc().intValue(), 2));

                }
                if (numberValue.getValue() instanceof Short) {
                    numberValue.setValue(
                            (short) MathUtils.round(numberValue.getValue().shortValue()
                                    - numberValue.getInc().shortValue(), 2));
                }
                if (numberValue.getValue() instanceof Byte) {
                    numberValue.setValue(
                            (byte) MathUtils.round(numberValue.getValue().byteValue()
                                    - numberValue.getInc().byteValue(), 2));
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && modButton.getParent().getParent().getTimer().sleep(100)) {
                if (numberValue.getValue() instanceof Double) {
                    numberValue
                            .setValue(MathUtils.round(numberValue.getValue().doubleValue()
                                    + numberValue.getInc().doubleValue(), 2));
                }
                if (numberValue.getValue() instanceof Float) {
                    numberValue.setValue(
                            (float) MathUtils.round(numberValue.getValue().floatValue()
                                    + numberValue.getInc().floatValue(), 2));

                }
                if (numberValue.getValue() instanceof Long) {
                    numberValue.setValue(
                            (long) MathUtils.round(numberValue.getValue().longValue()
                                    + numberValue.getInc().longValue(), 2));

                }
                if (numberValue.getValue() instanceof Integer) {
                    numberValue
                            .setValue((int) MathUtils.round(numberValue.getValue().intValue()
                                    + numberValue.getInc().intValue(), 2));

                }
                if (numberValue.getValue() instanceof Short) {
                    numberValue.setValue(
                            (short) MathUtils.round(numberValue.getValue().shortValue()
                                    + numberValue.getInc().shortValue(), 2));
                }
                if (numberValue.getValue() instanceof Byte) {
                    numberValue.setValue(
                            (byte) MathUtils.round(numberValue.getValue().byteValue()
                                    + numberValue.getInc().byteValue(), 2));
                }
            }
            if (Mouse.isButtonDown(0)) {
                if (numberValue.getValue() instanceof Double) {
                    numberValue
                            .setValue(
                                    MathUtils.round(
                                            ((mouseX - getPosX())
                                                    * (numberValue.getMaximum().doubleValue()
                                                    - numberValue.getMinimum()
                                                    .doubleValue())
                                                    / sliderwidth
                                                    + numberValue.getMinimum().doubleValue()),
                                            2));
                }
                if (numberValue.getValue() instanceof Float) {
                    numberValue
                            .setValue(
                                    (float) MathUtils.round(
                                            ((mouseX - getPosX())
                                                    * (numberValue.getMaximum().floatValue()
                                                    - numberValue.getMinimum()
                                                    .floatValue())
                                                    / sliderwidth
                                                    + numberValue.getMinimum().floatValue()),
                                            2));
                }
                if (numberValue.getValue() instanceof Long) {
                    numberValue
                            .setValue(
                                    (long) MathUtils.round(
                                            ((mouseX - getPosX())
                                                    * (numberValue.getMaximum().longValue()
                                                    - numberValue.getMinimum()
                                                    .longValue())
                                                    / sliderwidth
                                                    + numberValue.getMinimum().longValue()),
                                            2));
                }
                if (numberValue.getValue() instanceof Integer) {
                    numberValue
                            .setValue(
                                    (int) MathUtils.round(
                                            ((mouseX - getPosX())
                                                    * (numberValue.getMaximum().intValue()
                                                    - numberValue.getMinimum()
                                                    .intValue())
                                                    / sliderwidth
                                                    + numberValue.getMinimum().intValue()),
                                            2));
                }
                if (numberValue.getValue() instanceof Short) {
                    numberValue
                            .setValue(
                                    (short) MathUtils.round(
                                            ((mouseX - getPosX())
                                                    * (numberValue.getMaximum().shortValue()
                                                    - numberValue.getMinimum()
                                                    .shortValue())
                                                    / sliderwidth
                                                    + numberValue.getMinimum().shortValue()),
                                            2));
                }
                if (numberValue.getValue() instanceof Byte) {
                    numberValue
                            .setValue(
                                    (byte) MathUtils.round(
                                            ((mouseX - getPosX())
                                                    * (numberValue.getMaximum().byteValue()
                                                    - numberValue.getMinimum()
                                                    .byteValue())
                                                    / sliderwidth
                                                    + numberValue.getMinimum().byteValue()),
                                            2));
                }
            }
        }
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
