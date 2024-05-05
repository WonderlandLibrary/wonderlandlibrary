package tech.drainwalk.gui.menu.window.windows.settingwindow.components.settings;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import tech.drainwalk.animation.EasingList;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.option.options.FloatOption;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.font.FontManager;
import tech.drainwalk.gui.menu.hovered.Hovered;
import tech.drainwalk.gui.menu.window.windows.settingwindow.SettingWindow;
import tech.drainwalk.gui.menu.window.windows.settingwindow.components.SettingComponent;
import tech.drainwalk.utility.color.ColorUtility;
import tech.drainwalk.utility.math.MathUtility;
import tech.drainwalk.utility.render.RenderUtility;

public class FloatSettingComponent extends SettingComponent {
    @Getter
    private final FloatOption option;
    private boolean sliderActive;
    private float prevSliderValue;

    public FloatSettingComponent(float x, float y, Module module, FloatOption option, SettingWindow parent) {
        super(x, y, module, parent);
        this.option = option;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        hoveredAnimation.animate(0, 1, 0.2f, EasingList.NONE, partialTicks);
        float[] textColor = ColorUtility.getRGBAf(ClientColor.textStay);
        FontManager.SEMI_BOLD_14.drawSubstringDefault(option.getSettingName(), x + (15 / 2f), y + (57 / 2f) - 0.5f,
                ColorUtility.rgbaFloat(textColor[0] + (hoveredAnimation.getAnimationValue() / 12f), textColor[1] + (hoveredAnimation.getAnimationValue() / 12f), textColor[2] + (hoveredAnimation.getAnimationValue() / 12f), visibleAnimation.getAnimationValue()), 39);
        float[] l = ColorUtility.getRGBAf(ClientColor.panelLines);

        RenderUtility.drawRect(x + (103 / 2f) + 5.5f+ 4, y + 29, 37, 1.5f,
                ColorUtility.rgbaFloat(l[0] + (hoveredAnimation.getAnimationValue() / 20f),
                        l[1] + (hoveredAnimation.getAnimationValue() / 20f),
                        l[2] + (hoveredAnimation.getAnimationValue() / 20f),
                        visibleAnimation.getAnimationValue()));
        float[] m = ColorUtility.getRGBAf(ClientColor.mainStay);
        float[] s = ColorUtility.getRGBAf(ClientColor.main);

        RenderUtility.drawRect(x + (103 / 2f) + 5.5f+ 4, y + 29, 37 * getPos(option), 1.5f, ColorUtility.rgbaFloat(m[0] + (hoveredAnimation.getAnimationValue() / 12f), m[1] + (hoveredAnimation.getAnimationValue() / 12f), m[2] + (hoveredAnimation.getAnimationValue() / 12f), visibleAnimation.getAnimationValue()));
        RenderUtility.drawRoundedRect(x + (103 / 2f) + 4f+ 4 + ((37 - 1.5f) * getPos(option)),
                y + 27.5f, 4.5f, 4.5f, 3.5f,
                ColorUtility.rgbaFloat(s[0] + (hoveredAnimation.getAnimationValue() / 12f), s[1] + (hoveredAnimation.getAnimationValue() / 12f), s[2] + (hoveredAnimation.getAnimationValue() / 12f), visibleAnimation.getAnimationValue()));
        float[] t = ColorUtility.getRGBAf(ClientColor.textMain);
        RenderUtility.drawRoundedOutlineRect(x + (103 / 2f)+ 4 - (FontManager.SEMI_BOLD_10.getStringWidth(option.getValue() + "")) - 1.5f, y + 29.5f - 3.5f, (FontManager.SEMI_BOLD_10.getStringWidth(option.getValue() + "")) + 4f, 8f, 3, 1.25f, ColorUtility.rgbaFloat(l[0] + (hoveredAnimation.getAnimationValue() / 20f),
                l[1] + (hoveredAnimation.getAnimationValue() / 20f),
                l[2] + (hoveredAnimation.getAnimationValue() / 20f),
                visibleAnimation.getAnimationValue()));
        FontManager.SEMI_BOLD_10.drawString(option.getValue() + "", x + (103 / 2f) + 0.5f+ 4 - (FontManager.SEMI_BOLD_10.getStringWidth(option.getValue() + "")), y + 29.5f,
                ColorUtility.rgbaFloat(t[0] + (hoveredAnimation.getAnimationValue() / 6f),
                        t[1] + (hoveredAnimation.getAnimationValue() / 6f),
                        t[2] + (hoveredAnimation.getAnimationValue() / 6f),
                        visibleAnimation.getAnimationValue()));

        if (sliderActive) {
            option.setValue(createSlider(option, x + (103 / 2f) + 5.5f+ 4, mouseX));
        }
    }

    @Override
    public void updateScreen(int mouseX, int mouseY) {
        hoveredAnimation.update(Hovered.isHovered(mouseX, mouseY, x + (15 / 2f) - 1f, y + (57 / 2f) - 5, 93, 13));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (Hovered.isHovered(mouseX, mouseY, x + (103 / 2f) + 5.5f, y + 29 - 6, 37, 1.5f + 10) && mouseButton == 0) {
            sliderActive = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        sliderActive = false;
    }

    public float createSlider(FloatOption floatSetting, float posX, int mouseX) {
        float delta = (int) (floatSetting.getMax() - floatSetting.getMin());
        float clickedX = mouseX - posX;
        float value = clickedX / 37;
        float outValue = floatSetting.getMin() + delta * value;
        return MathUtility.clamp((float) MathUtility.round(outValue, floatSetting.getInc()), floatSetting.getMin(), floatSetting.getMax());
    }

    public float getPos(FloatOption floatSetting) {
        float delta = (floatSetting.getMax() - floatSetting.getMin());
        return ((floatSetting.getValue() - floatSetting.getMin()) / delta);
    }

}
