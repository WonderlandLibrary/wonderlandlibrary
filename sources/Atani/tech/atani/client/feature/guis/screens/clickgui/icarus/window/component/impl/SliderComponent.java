package tech.atani.client.feature.guis.screens.clickgui.icarus.window.component.impl;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.utility.math.MathUtil;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.utility.render.shader.shaders.RoundedShader;

import java.awt.*;

public class SliderComponent extends ValueComponent {

    private final SliderValue sliderValue;
    private FontRenderer fontRenderer = FontStorage.getInstance().findFont("ArialMT", 16);

    public SliderComponent(SliderValue sliderValue, float posX, float posY, float height) {
        super(sliderValue, posX, posY, height);
        this.sliderValue = sliderValue;
    }

    @Override
    public float draw(int mouseX, int mouseY) {
        float sliderX = getPosX() + 5;
        float sliderWidth = getWidth() - 10;
        float sliderY = getPosY() + 13;
        float sliderHeight = 1.5f;
        float length = MathHelper
                .floor_double(((sliderValue.getValue()).floatValue() - sliderValue.getMinimum().floatValue())
                        / (sliderValue.getMaximum().floatValue() - sliderValue.getMinimum().floatValue()) * sliderWidth);
        RoundedShader.drawRound(sliderX, sliderY, sliderWidth, sliderHeight, 2, new Color(30, 30, 30));
        RoundedShader.drawGradientRound(sliderX, sliderY, length, sliderHeight, 2, new Color(ICARUS_FIRST), new Color(ICARUS_FIRST), new Color(ICARUS_SECOND), new Color(ICARUS_SECOND));
        if(Mouse.isButtonDown(0) && RenderUtil.isHovered(mouseX, mouseY, this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight())) {
            double min1 = sliderValue.getMinimum().floatValue();
            double max1 = sliderValue.getMaximum().floatValue();
            double newValue = MathUtil.round((mouseX - sliderX) * (max1 - min1) / (sliderWidth - 1.0f) + min1, sliderValue.getDecimalPlaces());
            sliderValue.setValue(newValue);
        }
        fontRenderer.drawString(sliderValue.getName(), getPosX() + 5, getPosY() + getFinalHeight() / 2 - fontRenderer.FONT_HEIGHT / 2 - 1, -1);
        fontRenderer.drawString(sliderValue.getValue().floatValue() + "", getPosX() + getWidth() - 5 - fontRenderer.getStringWidthInt(sliderValue.getValue().floatValue() + ""), getPosY() + getFinalHeight() / 2 - fontRenderer.FONT_HEIGHT / 2 - 1, -1);
        return fontRenderer.getStringWidthInt(sliderValue.getName()  + ": " + sliderValue.getValue().floatValue()) + 6;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

}
