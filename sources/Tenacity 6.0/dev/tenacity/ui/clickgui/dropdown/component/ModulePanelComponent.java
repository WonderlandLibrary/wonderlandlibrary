package dev.tenacity.ui.clickgui.dropdown.component;

import dev.tenacity.module.Module;
import dev.tenacity.module.impl.render.HUDModule;
import dev.tenacity.setting.impl.BooleanSetting;
import dev.tenacity.setting.impl.ColorSetting;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.setting.impl.NumberSetting;
import dev.tenacity.ui.IScreen;
import dev.tenacity.ui.clickgui.dropdown.component.setting.SettingPanelComponent;
import dev.tenacity.ui.clickgui.dropdown.component.setting.impl.BooleanSettingComponent;
import dev.tenacity.ui.clickgui.dropdown.component.setting.impl.ColorSettingComponent;
import dev.tenacity.ui.clickgui.dropdown.component.setting.impl.ModeSettingComponent;
import dev.tenacity.ui.clickgui.dropdown.component.setting.impl.NumberSettingComponent;
import dev.tenacity.util.misc.HoverUtil;
import dev.tenacity.util.render.ColorUtil;
import dev.tenacity.util.render.RenderUtil;
import dev.tenacity.util.render.Theme;
import dev.tenacity.util.render.animation.AbstractAnimation;
import dev.tenacity.util.render.animation.AnimationDirection;
import dev.tenacity.util.render.animation.impl.DecelerateAnimation;
import dev.tenacity.util.render.animation.impl.SmoothStepAnimation;
import dev.tenacity.util.render.font.CustomFont;
import dev.tenacity.util.render.font.FontUtil;
import dev.tenacity.util.tuples.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class ModulePanelComponent implements IScreen {

    private final Module module;

    private float posX, posY;

    private float settingOffset;

    private final AbstractAnimation toggleColorAnimation = new SmoothStepAnimation(250, 1, AnimationDirection.FORWARDS),
            expandModuleAnimation = new DecelerateAnimation(250, 1, AnimationDirection.BACKWARDS);

    private final List<SettingPanelComponent<?>> settingPanelComponentList = new ArrayList<>();

    public ModulePanelComponent(final Module module) {
        this.module = module;

        module.getSettingList().forEach(setting -> {
            if(setting instanceof BooleanSetting)
                settingPanelComponentList.add(new BooleanSettingComponent((BooleanSetting) setting));
            if(setting instanceof NumberSetting)
                settingPanelComponentList.add(new NumberSettingComponent((NumberSetting) setting));
            if(setting instanceof ModeSetting)
                settingPanelComponentList.add(new ModeSettingComponent((ModeSetting) setting));
            if(setting instanceof ColorSetting)
                settingPanelComponentList.add(new ColorSettingComponent((ColorSetting) setting));
        });
    }

    @Override
    public void initGUI() {
    }

    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if(module.isExpanded())
            settingPanelComponentList.forEach(settingPanelComponent -> settingPanelComponent.keyTyped(typedChar, keyCode));
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        final Pair<Color, Color> clientColors = Theme.getThemeColors(HUDModule.theme.getCurrentMode());
        final CustomFont font = FontUtil.getFont("OpenSans-SemiBold", 18);

        final int width = 100;
        final int height = 20;

        toggleColorAnimation.setDirection(module.isEnabled() ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);

        final Color animatedColorBG = ColorUtil.interpolateColorC(ColorUtil.getSurfaceVariantColor(), clientColors.getFirst(), toggleColorAnimation.getOutput().floatValue());

        RenderUtil.drawRect(posX, posY, width, height, animatedColorBG.getRGB());
        font.drawString(module.getName(), posX + (width / 2f) - (font.getStringWidth(module.getName()) / 2), posY + (height / 4f), -1);

        settingOffset = 0;
        if(module.isExpanded() || !expandModuleAnimation.isDone()) {
            settingPanelComponentList.forEach(settingPanelComponent -> {
                if(settingPanelComponent.getSetting().isHidden()) return;

                settingPanelComponent.setPosX(posX);
                settingPanelComponent.setPosY(posY + height + (settingOffset * 15));

                settingPanelComponent.drawScreen(mouseX, mouseY);

                settingOffset += settingPanelComponent.getAddedHeight() * expandModuleAnimation.getOutput();
            });
        }
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if(HoverUtil.isHovering(posX, posY, 100, 20, mouseX, mouseY))
            if(mouseButton == 0)
                module.toggle();
            else {
                module.setExpanded(!module.isExpanded());

                expandModuleAnimation.setDirection(module.isExpanded() ? AnimationDirection.FORWARDS : AnimationDirection.BACKWARDS);
            }

        if(module.isExpanded())
            settingPanelComponentList.forEach(settingPanelComponent -> settingPanelComponent.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if(module.isExpanded())
            settingPanelComponentList.forEach(settingPanelComponent -> settingPanelComponent.mouseReleased(mouseX, mouseY, state));
    }

    public void setPosX(final float posX) {
        this.posX = posX;
    }

    public void setPosY(final float posY) {
        this.posY = posY;
    }

    public float getSettingOffset() {
        return settingOffset;
    }
}
