package tech.drainwalk.gui.menu.window.windows.settingwindow.components.settings;

import lombok.Getter;
import net.minecraft.client.renderer.GlStateManager;
import tech.drainwalk.animation.Animation;
import tech.drainwalk.animation.EasingList;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.option.options.MultiOption;
import tech.drainwalk.client.option.options.MultiOptionValue;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.font.FontManager;
import tech.drainwalk.gui.menu.hovered.Hovered;
import tech.drainwalk.gui.menu.window.windows.settingwindow.SettingWindow;
import tech.drainwalk.gui.menu.window.windows.settingwindow.components.SettingComponent;
import tech.drainwalk.utility.color.ColorUtility;
import tech.drainwalk.utility.render.RenderUtility;

import java.util.ArrayList;
import java.util.List;

public class MultiSettingComponent extends SettingComponent {
    @Getter
    private final MultiOption option;

    private boolean isOpened = false;
    @Getter
    protected final Animation openAnimation = new Animation();
    public MultiSettingComponent(float x, float y, Module module, MultiOption option, SettingWindow parent) {
        super(x, y, module, parent);
        this.option = option;
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        openAnimation.animate(0, 1, 0.4f, EasingList.NONE, partialTicks);
        hoveredAnimation.animate(0, 1, 0.2f, EasingList.NONE, partialTicks);
        float[] textColor = ColorUtility.getRGBAf(ClientColor.textStay);

        FontManager.SEMI_BOLD_14.drawSubstringDefault(option.getSettingName(), x + (15 / 2f) , y + (57 / 2f) - 0.5f,
                ColorUtility.rgbaFloat(textColor[0] + (hoveredAnimation.getAnimationValue() / 12f), textColor[1] + (hoveredAnimation.getAnimationValue() / 12f), textColor[2] + (hoveredAnimation.getAnimationValue() / 12f), visibleAnimation.getAnimationValue()), 39);
        RenderUtility.drawRoundedOutlineRect(x + (114 / 2f) - 2.5f+  1, y + 25.5f, 86 / 2f, 9f + (option.getValues().length * 8 + 2f) * openAnimation.getAnimationValue(), 3, 1.25f,ColorUtility.getColorWithAlpha(ClientColor.panelLines, visibleAnimation.getAnimationValue()));
        StringBuilder values = new StringBuilder();
        GlStateManager.pushMatrix();
        float posX = x + (187 / 2f) - 1f+  1;
        float posY = y + 28.5f;
        GlStateManager.translate(((posX - 1.5f) + (8 / 2f) / 2), ((posY + .5f) + (6 / 2f) / 2), 0);
        GlStateManager.rotate(openAnimation.getAnimationValue() * 180, 0, 0, 1);
        GlStateManager.translate(-((posX -1.5f) + (8 / 2f) / 2), -((posY + .5f) + (6 / 2f) / 2), 0);
    //    RenderUtility.drawRect(posX - 1f, posY, 8 / 2f, 6 / 2f, -1);
        FontManager.ICONS_12.drawString("h", posX - 2.5f, posY + 1.5f, ColorUtility.getColorWithAlpha(ColorUtility.interpolateColor(ClientColor.textStay,ClientColor.textMain, openAnimation.getAnimationValue()), visibleAnimation.getAnimationValue()));
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        int index = 0;
        for (MultiOptionValue value : option.getValues()) {
            value.getAnimation().animate(0, 1, 0.25f, EasingList.NONE, partialTicks);
            value.getHoveredAnimation().animate(0, 1, 0.5f, EasingList.NONE, partialTicks);
            if (value.isToggle()) {
                values.append(value.getName());
                    values.append(", ");
            }
            float[] color = ColorUtility.getRGBAf(ColorUtility.interpolateColor(ClientColor.textStay, ClientColor.textMain, value.getAnimation().getAnimationValue()));
            GlStateManager.translate(0, 0 - ((1 - openAnimation.getAnimationValue()) * 9), 0);
            GlStateManager.pushMatrix();
            GlStateManager.translate(value.getHoveredAnimation().getAnimationValue(), 0, 0);

            FontManager.SEMI_BOLD_12.drawSubstringDefault(value.getName(), x + (114 / 2f)+ 1, y + 29f + 8.5f + (index * 8), ColorUtility.rgbaFloat(color[0] + (value.getHoveredAnimation().getAnimationValue() / 7f), color[1] + (value.getHoveredAnimation().getAnimationValue() / 7f), color[2] + (value.getHoveredAnimation().getAnimationValue() / 7f), openAnimation.getAnimationValue() - (1 -visibleAnimation.getAnimationValue())), 30);
            GlStateManager.popMatrix();
            index++;
        }
        GlStateManager.popMatrix();

        offsetSize = ((option.getValues().length * 8 + 2f) * openAnimation.getAnimationValue()) + 14;
        FontManager.SEMI_BOLD_12.drawSubstringDefault(values.toString().length() >= 1 ? values.toString() : "NONE", x + (114 / 2f) +1, y + 29f, ColorUtility.getColorWithAlpha(ColorUtility.interpolateColor(ClientColor.textStay,ClientColor.textMain, openAnimation.getAnimationValue()), visibleAnimation.getAnimationValue()), 28);
    }

    @Override
    public void updateScreen(int mouseX, int mouseY) {
        openAnimation.update(isOpened);
        int index = 0;
        for (MultiOptionValue value : option.getValues()) {
            value.getAnimation().update(value.isToggle());
            value.getHoveredAnimation().update(Hovered.isHovered(mouseX, mouseY, x + (114 / 2f)+1, y + 29f + 6f - 1f + (index * 8), 38f, 7));
            index++;
        }

        hoveredAnimation.update(Hovered.isHovered(mouseX, mouseY, x + (15 / 2f) - 1f, y + (57 / 2f) - 5, 93, 13));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isOpened) {
            int index = 0;
            for (MultiOptionValue value : option.getValues()) {

                if (Hovered.isHovered(mouseX, mouseY, x + (114 / 2f)+1, y + 29f + 6f - 1f + (index * 8), 38f, 7) && mouseButton == 0) {
                    value.setToggle(!value.isToggle());
                }
                index++;
            }
        }
        if (Hovered.isHovered(mouseX, mouseY, x + (114 / 2f) - 2.5f + 0.5f, y + 25.5f, 86 / 2f, 9f)) {
            if (mouseButton == 0) {
                isOpened = !isOpened;
            }
        }
    }
}
