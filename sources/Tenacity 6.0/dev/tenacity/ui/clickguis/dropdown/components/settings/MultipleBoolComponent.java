// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.dropdown.components.settings;

import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import java.awt.Color;
import java.util.Iterator;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import java.util.List;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.module.settings.impl.BooleanSetting;
import java.util.HashMap;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.ui.clickguis.dropdown.components.SettingComponent;

public class MultipleBoolComponent extends SettingComponent<MultipleBoolSetting>
{
    public float realHeight;
    public float normalCount;
    private final HashMap<BooleanSetting, Pair<Animation, Animation>> booleanSettingAnimations;
    private boolean opened;
    private final List<BooleanSetting> sortedSettings;
    private float additionalHeight;
    
    public MultipleBoolComponent(final MultipleBoolSetting setting) {
        super(setting);
        this.booleanSettingAnimations = new HashMap<BooleanSetting, Pair<Animation, Animation>>();
        this.additionalHeight = 0.0f;
        this.sortedSettings = setting.getBoolSettings().stream().sorted(Comparator.comparingDouble((ToDoubleFunction<? super BooleanSetting>)this::getEnabledWidth)).collect((Collector<? super BooleanSetting, ?, List<BooleanSetting>>)Collectors.toList());
        for (final BooleanSetting booleanSetting : this.sortedSettings) {
            this.booleanSettingAnimations.put(booleanSetting, (Pair<Animation, Animation>)Pair.of(new DecelerateAnimation(250, 1.0), new DecelerateAnimation(250, 1.0)));
        }
        this.normalCount = 2.0f;
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        final float boxHeight = 15.0f;
        final float boxY = this.y + this.realHeight / 2.0f - boxHeight / 2.0f + 4.0f;
        final float boxX = this.x + 6.0f;
        final float boxWidth = this.width - 10.0f;
        final float enabledCount = (float)this.sortedSettings.stream().filter(BooleanSetting::isEnabled).count();
        final Color outlineColor = ColorUtil.interpolateColorC(this.settingRectColor.brighter().brighter(), this.clientColors.getSecond(), enabledCount / this.sortedSettings.size());
        final Color rectColor = this.settingRectColor.brighter();
        RoundedUtil.drawRound(this.x + 5.0f, boxY, this.width - 10.0f, boxHeight + this.additionalHeight, 4.0f, outlineColor);
        RoundedUtil.drawRound(this.x + 6.0f, boxY + 1.0f, this.width - 12.0f, boxHeight - 2.0f + this.additionalHeight, 3.0f, rectColor);
        MultipleBoolComponent.tenacityFont14.drawString(this.getSetting().name, this.x + 6.0f, this.y + 4.0f, this.textColor);
        float addHeight = 0.0f;
        float xOffset = 2.0f;
        float yOffset = 3.0f;
        final float spacing = 3.0f;
        final float avaliableWidth = boxWidth - 2.0f;
        for (final BooleanSetting setting : this.sortedSettings) {
            final float enabledWidth = this.getEnabledWidth(setting);
            final float enabledHeight = (float)(MultipleBoolComponent.tenacityFont14.getHeight() + 4);
            if (xOffset + enabledWidth > avaliableWidth) {
                xOffset = 2.0f;
                yOffset += enabledHeight + spacing;
                addHeight += yOffset + enabledHeight + spacing - (boxHeight + addHeight);
            }
            final float enabledX = boxX + xOffset;
            final float enabledY = boxY + yOffset;
            final boolean hovering = HoveringUtil.isHovering(enabledX, enabledY, enabledWidth, enabledHeight, mouseX, mouseY);
            final Animation hoverAnimation = this.booleanSettingAnimations.get(setting).getFirst();
            final Animation toggleAnimation = this.booleanSettingAnimations.get(setting).getSecond();
            hoverAnimation.setDirection(hovering ? Direction.FORWARDS : Direction.BACKWARDS);
            toggleAnimation.setDirection(setting.isEnabled() ? Direction.FORWARDS : Direction.BACKWARDS);
            Color rectColorBool = ColorUtil.interpolateColorC(this.settingRectColor.brighter().brighter(), this.clientColors.getSecond(), toggleAnimation.getOutput().floatValue());
            rectColorBool = ColorUtil.interpolateColorC(rectColorBool, rectColorBool.brighter(), hoverAnimation.getOutput().floatValue());
            RoundedUtil.drawRound(enabledX, enabledY, enabledWidth, enabledHeight, 3.0f, rectColorBool);
            MultipleBoolComponent.tenacityFont14.drawString(setting.name, enabledX + 2.0f, enabledY + 2.0f, ColorUtil.applyOpacity(this.textColor, 0.5f + 0.5f * toggleAnimation.getOutput().floatValue()));
            xOffset += enabledWidth + spacing;
        }
        this.additionalHeight = addHeight;
        final float increment = (boxY - this.y + boxHeight + addHeight + 3.0f - this.realHeight) / (this.realHeight / this.normalCount);
        this.countSize = this.normalCount + increment;
    }
    
    private float getEnabledWidth(final BooleanSetting setting) {
        return MultipleBoolComponent.tenacityFont14.getStringWidth(setting.name) + 4.0f;
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final float boxHeight = 15.0f;
        final float boxY = this.y + this.realHeight / 2.0f - boxHeight / 2.0f + 4.0f;
        final float boxX = this.x + 6.0f;
        final float boxWidth = this.width - 10.0f;
        float xOffset = 2.0f;
        float yOffset = 3.0f;
        final float spacing = 3.0f;
        final float avaliableWidth = boxWidth - 2.0f;
        for (final BooleanSetting setting : this.sortedSettings) {
            final float enabledWidth = this.getEnabledWidth(setting);
            final float enabledHeight = (float)(MultipleBoolComponent.tenacityFont14.getHeight() + 4);
            if (xOffset + enabledWidth > avaliableWidth) {
                xOffset = 2.0f;
                yOffset += enabledHeight + spacing;
            }
            final float enabledX = boxX + xOffset;
            final float enabledY = boxY + yOffset;
            final boolean hovered = HoveringUtil.isHovering(enabledX, enabledY, enabledWidth, enabledHeight, mouseX, mouseY);
            if (this.isClickable(enabledY + enabledHeight) && hovered && button == 0) {
                setting.toggle();
            }
            xOffset += enabledWidth + spacing;
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
}
