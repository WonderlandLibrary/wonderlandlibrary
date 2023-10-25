package tech.drainwalk.gui.menu.window.windows.settingwindow.components.settings;

import lombok.Getter;
import tech.drainwalk.animation.Animation;
import tech.drainwalk.animation.EasingList;
import tech.drainwalk.font.FontManager;
import tech.drainwalk.gui.menu.hovered.Hovered;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.gui.menu.window.windows.settingwindow.SettingWindow;
import tech.drainwalk.gui.menu.window.windows.settingwindow.components.SettingComponent;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.option.options.BooleanOption;
import tech.drainwalk.utility.color.ColorUtility;
import tech.drainwalk.utility.render.RenderUtility;

public class BooleanSettingComponent extends SettingComponent {
    @Getter
    private final BooleanOption option;
    private final Animation animation = new Animation();

    public BooleanSettingComponent(float x, float y, Module module, BooleanOption option, SettingWindow parent) {
        super(x, y, module, parent);
        this.option = option;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        animation.animate(0, 1, 0.2f, EasingList.NONE, partialTicks);
        hoveredAnimation.animate(0, 1, 0.2f, EasingList.NONE, partialTicks);

        float[] textColor = ColorUtility.getRGBAf(ClientColor.textStay);
        float[] c = ColorUtility.getRGBAf(ColorUtility.interpolateColor(ClientColor.checkBoxStayBG, ClientColor.mainStay, animation.getAnimationValue()));
        float[] c2 = ColorUtility.getRGBAf(ColorUtility.interpolateColor(ClientColor.checkBoxStay, ClientColor.main, animation.getAnimationValue()));


        FontManager.SEMI_BOLD_14.drawSubstringDefault(option.getSettingName(), x + (15 / 2f) , y + (57 / 2f) - 0.5f,
                ColorUtility.rgbaFloat(textColor[0] + (hoveredAnimation.getAnimationValue() / 12f), textColor[1] + (hoveredAnimation.getAnimationValue() / 12f), textColor[2] + (hoveredAnimation.getAnimationValue() / 12f), visibleAnimation.getAnimationValue()), 39);

        RenderUtility.drawRoundedRect(x + (178 / 2f) - 4 + 2, y + (58.76f / 2f) - 2, 20.24f / 2f, 9.8f / 2f, 4.25f, ColorUtility.rgbaFloat(c[0] + (hoveredAnimation.getAnimationValue() / 20f), c[1] + (hoveredAnimation.getAnimationValue() / 20f), c[2] + (hoveredAnimation.getAnimationValue() / 20f), visibleAnimation.getAnimationValue()));
        RenderUtility.drawRoundedRect(x + (187.68f / 2f) - 4 + 2- (6.5f * (1 - animation.getAnimationValue())), y + (57f / 2f) - 2f, 13f / 2f, 13f / 2f, 6f, ColorUtility.rgbaFloat(c2[0] + (hoveredAnimation.getAnimationValue() / 12f), c2[1] + (hoveredAnimation.getAnimationValue() / 12f), c2[2] + (hoveredAnimation.getAnimationValue() / 12f), visibleAnimation.getAnimationValue()));
    }

    @Override
    public void updateScreen(int mouseX, int mouseY) {
        animation.update(option.getValue());
        hoveredAnimation.update(Hovered.isHovered(mouseX, mouseY, x + (15 / 2f) - 1f, y + (57 / 2f) - 5, 93, 13));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (Hovered.isHovered(mouseX, mouseY, x + (15 / 2f) - 1f, y + (57 / 2f) - 5, 93, 13)) {
            option.setValue(!option.getValue());
        }
    }
}
