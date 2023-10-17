package de.tired.base.guis.newclickgui.renderables;


import de.tired.base.guis.newclickgui.abstracts.ClickGUIHandler;
import de.tired.base.guis.newclickgui.renderables.setting.RenderableCheckbox;
import de.tired.base.guis.newclickgui.renderables.setting.RenderableColorPicker;
import de.tired.base.guis.newclickgui.renderables.setting.RenderableSelector;
import de.tired.base.guis.newclickgui.renderables.setting.RenderableSlider;
import de.tired.base.guis.newclickgui.setting.ModeSetting;
import de.tired.base.guis.newclickgui.setting.NumberSetting;
import de.tired.base.guis.newclickgui.setting.Setting;
import de.tired.util.animation.Animation;
import de.tired.util.animation.ColorAnimation;
import de.tired.util.render.RenderUtil;
import de.tired.base.guis.newclickgui.setting.impl.BooleanSetting;
import de.tired.base.guis.newclickgui.setting.impl.ColorPickerSetting;
import de.tired.util.render.ScrollHandler;
import de.tired.base.font.FontManager;
import de.tired.util.render.ColorUtil;
import de.tired.base.module.Module;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.list.RoundedRectGradient;
import de.tired.util.render.shaderloader.list.RoundedRectShader;
import de.tired.Tired;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class RenderableModule extends ClickGUIHandler {

    public final Module moduleFeature;

    public double count;


    public double x, y;

    public Animation extendAnimation = new Animation();

    public int yAxisAdditional = 0;
    public int yAxisAdditionalNoScroll = 0;
    public boolean isHovering, extended;

    private final ArrayList<RenderableCheckbox> renderableCheckboxes;

    private final ArrayList<RenderableSlider> renderableSliders;

    private final ArrayList<RenderableSelector> renderableSelectors;

    public ScrollHandler scroll = new ScrollHandler();

    private ColorAnimation colorAnimation = new ColorAnimation();

    private final ArrayList<RenderableColorPicker> renderableColorPickers;

    private ColorAnimation color = new ColorAnimation();

    public RenderableModule(final Module moduleFeature) {
        this.moduleFeature = moduleFeature;
        this.renderableCheckboxes = new ArrayList<>();
        this.renderableSliders = new ArrayList<>();
        this.renderableSelectors = new ArrayList<>();
        this.renderableColorPickers = new ArrayList<>();

        for (Setting setting : Tired.INSTANCE.settingsManager.getSettingsByMod(moduleFeature)) {
            if (setting instanceof BooleanSetting) {
                renderableCheckboxes.add(new RenderableCheckbox((BooleanSetting) setting));
            } else if (setting instanceof NumberSetting) {
                renderableSliders.add(new RenderableSlider((NumberSetting) setting));
            } else if (setting instanceof ModeSetting) {
                renderableSelectors.add(new RenderableSelector((ModeSetting) setting));
            } else if (setting instanceof ColorPickerSetting) {
                renderableColorPickers.add(new RenderableColorPicker((ColorPickerSetting) setting));
            }
        }
    }

    public void onReset() {
        for (RenderableCheckbox checkBox : renderableCheckboxes) {
            if (checkBox.checkBox.isVisible())
                checkBox.onReset();
        }

        for (RenderableSlider slider : renderableSliders)
            if (slider.setting.isVisible())
                slider.onReset();


        for (RenderableSelector renderableSelector : renderableSelectors) {
            if (renderableSelector.modeSetting.isVisible()) {
                renderableSelector.onReset();
            }
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY) {
        renderableSliders.forEach(renderableSlider -> renderableSlider.onMouseReleased(mouseX, mouseY));
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseKey) {

        for (RenderableCheckbox checkBox : renderableCheckboxes) {
            if (checkBox.checkBox.isVisible())
                checkBox.onMouseClicked(mouseX, mouseY, mouseKey);
        }

        for (RenderableSlider slider : renderableSliders)
            if (slider.setting.isVisible())
                slider.onMouseClicked(mouseX, mouseY, mouseKey);

        for (RenderableColorPicker colorPicker : renderableColorPickers) {
            if (colorPicker.colorPickerSetting.isVisible()) {
                colorPicker.onMouseClicked(mouseX, mouseY, mouseKey);
            }
        }

        for (RenderableSelector renderableSelector : renderableSelectors) {
            if (renderableSelector.modeSetting.isVisible()) {
                renderableSelector.onMouseClicked(mouseX, mouseY, mouseKey);
            }
        }


        if (!isHovering) return;
        switch (mouseKey) {
            case 0:
                this.moduleFeature.executeMod();
                break;
            case 1:
                this.extended = !extended;
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int x, int y) {

    }


    public void drawScreen(int mouseX, int mouseY, int x, int y, ScrollHandler scrollHandler) {
        this.x = x;
        this.y = y;

        this.isHovering = this.isHovered(mouseX, mouseY, x, y, width, height);

        Color firstColor = ColorUtil.interpolateColorsBackAndForth(24, 0, new Color(84, 51, 158).brighter(), new Color(104, 127, 203), true);

        Color color1 = moduleFeature.isState() ? firstColor : new Color(45, 45, 45);

        color.update();
        color.animate(color1, .2);

        if (Tired.INSTANCE.settingsManager.getSettingsByMod(moduleFeature).size() >= 1) {
            RenderUtil.instance.doRenderShadow(color.getColor(), x + 3, y - 2, width - 5, (!extended) ? 22 + yAxisAdditional - 4 : 19 + yAxisAdditional, 23);
            ShaderManager.shaderBy(RoundedRectGradient.class).drawRound(x + 7, y, width - 14, (!extended) ? 19 + yAxisAdditional - 4 : 19 + yAxisAdditional, 6, color.getColor(), color.getColor(), color.getColor().darker(), color.getColor().darker());
        } else {
            RenderUtil.instance.doRenderShadow(color.getColor(), x + 7, y, width - 14, 19 + yAxisAdditional - 4, 13);
            ShaderManager.shaderBy(RoundedRectGradient.class).drawRound(x + 7, y, width - 14, 19 + yAxisAdditional - 4, 6, color.getColor(), color.getColor(), color.getColor().darker(), color.getColor().darker());
        }

        {
            if (moduleFeature.isState()) {
                GlStateManager.disableBlend();
                GlStateManager.disableLighting();
                GlStateManager.resetColor();
            }
        }
        FontManager.interSemiBold24.drawString(moduleFeature.getName(), calculateMiddle(moduleFeature.getName(), FontManager.interSemiBold24, x + 7, width - 14), y + 6, -1);

        colorAnimation.update();
        colorAnimation.animate(moduleFeature.isState() ? new Color(244, 244, 244, 122) : new Color(55, 55, 55), .2, false);

        if (extended && Tired.INSTANCE.settingsManager.getSettingsByMod(moduleFeature).size() >= 1)
            ShaderManager.shaderBy(RoundedRectShader.class).drawRound(x + 14, y + 19, width - 28, 1, 1f, colorAnimation.getColor());

        final AtomicInteger yAxis = new AtomicInteger((int) scroll.getScroll());

        if (extended) {
            //Code block for settings

            {
                {
                    for (RenderableCheckbox checkBox : renderableCheckboxes)
                        if (checkBox.checkBox.isVisible()) {
                            checkBox.drawScreen(mouseX, mouseY, x + 8, (int) (y + 6 + yAxis.addAndGet(16)));
                        }
                    for (RenderableSlider slider : renderableSliders)
                        if (slider.setting.isVisible()) {
                            slider.drawScreen(mouseX, mouseY, x + 8, (int) (y + 12 + yAxis.addAndGet(25)));
                        }

                    for (RenderableSelector renderableSelector : renderableSelectors) {
                        if (renderableSelector.modeSetting.isVisible()) {
                            renderableSelector.drawScreen(mouseX, mouseY, x + 4, (int) (y + 25) + yAxis.get());
                            yAxis.addAndGet(20);
                        }
                    }

                    for (RenderableColorPicker colorPicker : renderableColorPickers) {
                        if (colorPicker.colorPickerSetting.isVisible()) {
                            colorPicker.drawScreen(mouseX, mouseY, x + 8, (int) (y + 6 + 15 + yAxis.get()));
                            yAxis.addAndGet(colorPicker.calcHeight + 14);
                        }

                    }
                }
            }
        } else {
            this.onReset();
        }

        yAxisAdditional = yAxis.get() + 4;
        yAxisAdditionalNoScroll = yAxis.get();
    }
}
