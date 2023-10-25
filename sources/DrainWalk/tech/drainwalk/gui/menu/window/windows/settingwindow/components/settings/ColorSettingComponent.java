package tech.drainwalk.gui.menu.window.windows.settingwindow.components.settings;

import lombok.Getter;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import tech.drainwalk.animation.EasingList;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.option.options.ColorOption;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.font.FontManager;
import tech.drainwalk.gui.menu.MenuMain;
import tech.drainwalk.gui.menu.hovered.Hovered;
import tech.drainwalk.gui.menu.window.windows.settingwindow.SettingWindow;
import tech.drainwalk.gui.menu.window.windows.settingwindow.components.SettingComponent;
import tech.drainwalk.utility.color.ColorUtility;
import tech.drainwalk.utility.render.RenderUtility;

import java.awt.*;
import java.nio.ByteBuffer;

public class ColorSettingComponent extends SettingComponent {
    @Getter
    private final ColorOption option;
    private int selectionMouseX;
    private int selectionMouseY;

    public ColorSettingComponent(float x, float y, Module module, ColorOption option, SettingWindow parent) {
        super(x, y, module, parent);
        this.option = option;
    }








    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        hoveredAnimation.animate(0, 1, 0.2f, EasingList.NONE, partialTicks);

        float[] textColor = ColorUtility.getRGBAf(ClientColor.textStay);

        FontManager.SEMI_BOLD_14.drawSubstringDefault(option.getSettingName(), x + (15 / 2f), y + (57 / 2f) - 0.5f,
                ColorUtility.rgbaFloat(textColor[0] + (hoveredAnimation.getAnimationValue() / 12f), textColor[1] + (hoveredAnimation.getAnimationValue() / 12f), textColor[2] + (hoveredAnimation.getAnimationValue() / 12f), visibleAnimation.getAnimationValue()), 39);
        float[] color = ColorUtility.getRGBAf(option.getValue());
        RenderUtility.drawRoundedRect(x + (178 / 2f) - 1.5f+ 4, y + (58.76f / 2f) - 3, 6.5f, 6.5f, 5, ColorUtility.rgbFloat(color[0], color[1], color[2]));

    }

    @Override
    public void updateScreen(int mouseX, int mouseY) {
        hoveredAnimation.update(Hovered.isHovered(mouseX, mouseY, x + (15 / 2f) - 1f, y + (57 / 2f) - 5, 93, 13));

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(Hovered.isHovered(mouseX,mouseY,x + (178 / 2f) - 1.5f+ 4, y + (58.76f / 2f) - 3, 6.5f, 6.5f) && mouseButton == 0) {
            float[] color = ColorUtility.getRGBAf(option.getValue());
            float[] hueValue = Color.RGBtoHSB((int) (color[0] * 255f), (int) (color[1] * 255f), (int) (color[2] * 255f), color);
            MenuMain.colorPickerWindow.hueSliderValue = (int) (hueValue[0] * 360f);
            MenuMain.colorPickerWindow.alphaSliderValue = (int) (color[3] * 255f);
            MenuMain.colorPickerWindow.color = option.getValue();
            MenuMain.colorPickerWindow.option = option;
            MenuMain.colorPickerWindow.setWindowActive(true);
            MenuMain.colorPickerWindow.setWindowX(mouseX + 4);
            MenuMain.colorPickerWindow.setWindowY(mouseY + 2);
        }
    }
}
