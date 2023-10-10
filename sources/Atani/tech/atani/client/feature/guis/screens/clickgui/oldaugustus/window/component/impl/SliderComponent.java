package tech.atani.client.feature.guis.screens.clickgui.oldaugustus.window.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import tech.atani.client.utility.math.MathUtil;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.feature.value.impl.SliderValue;

import java.awt.*;

public class SliderComponent extends ValueComponent {

    private final SliderValue sliderValue;
    private FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

    public SliderComponent(SliderValue sliderValue, float posX, float posY, float height) {
        super(sliderValue, posX, posY, height);
        this.sliderValue = sliderValue;
    }

    @Override
    public float draw(int mouseX, int mouseY) {
        float sliderX = getPosX() + 2;
        float sliderWidth = getWidth() - 4;
        float sliderY = getPosY() + 2;
        float sliderHeight = getHeight() - 4;
        float length = MathHelper
                .floor_double(((sliderValue.getValue()).floatValue() - sliderValue.getMinimum().floatValue())
                        / (sliderValue.getMaximum().floatValue() - sliderValue.getMinimum().floatValue()) * sliderWidth);
        RenderUtil.drawRect(sliderX, sliderY, length, sliderHeight, new Color(41, 146, 222).getRGB());
        if(Mouse.isButtonDown(0) && RenderUtil.isHovered(mouseX, mouseY, this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight())) {
            double min1 = sliderValue.getMinimum().floatValue();
            double max1 = sliderValue.getMaximum().floatValue();
            double newValue = MathUtil.round((mouseX - sliderX) * (max1 - min1) / (sliderWidth - 1.0f) + min1, sliderValue.getDecimalPlaces());
            sliderValue.setValue(newValue);
        }
        fontRenderer.drawCenteredStringWithShadow(sliderValue.getName() + ": " + sliderValue.getValue().floatValue(), getPosX() + getWidth() / 2, getPosY() + getFinalHeight() / 2 - fontRenderer.FONT_HEIGHT / 2, new Color(139, 141, 145, 255).getRGB());
        return fontRenderer.getStringWidthInt(sliderValue.getName()  + ": " + sliderValue.getValue().floatValue()) + 6;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

}
