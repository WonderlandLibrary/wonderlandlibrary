// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.dropdown.components.settings;

import java.util.Iterator;
import dev.tenacity.utils.render.RenderUtil;
import java.util.function.BiFunction;
import dev.tenacity.utils.render.Theme;
import dev.tenacity.utils.tuples.mutable.MutablePair;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import java.awt.Color;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.ContinualAnimation;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.ui.clickguis.dropdown.components.SettingComponent;

public class ModeComponent extends SettingComponent<ModeSetting>
{
    private final Animation hoverAnimation;
    private final Animation openAnimation;
    private final Animation selectionBox;
    private boolean opened;
    public float realHeight;
    public float normalCount;
    private final ContinualAnimation selectionBoxY;
    private String hoveringMode;
    
    public ModeComponent(final ModeSetting modeSetting) {
        super(modeSetting);
        this.hoverAnimation = new DecelerateAnimation(250, 1.0, Direction.BACKWARDS);
        this.openAnimation = new DecelerateAnimation(250, 1.0, Direction.BACKWARDS);
        this.selectionBox = new DecelerateAnimation(250, 1.0, Direction.BACKWARDS);
        this.selectionBoxY = new ContinualAnimation();
        this.hoveringMode = "";
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
        final ModeSetting modeSetting = this.getSetting();
        final float boxHeight = 18.0f;
        final float boxY = this.y + this.realHeight / 2.0f - boxHeight / 2.0f + 4.0f;
        final float boxX = this.x + 5.0f;
        final float boxWidth = this.width - 10.0f;
        final boolean themeSetting = modeSetting.name.equals("Theme Selection");
        final boolean hoveringBox = HoveringUtil.isHovering(boxX, boxY, boxWidth, boxHeight, mouseX, mouseY);
        this.hoverAnimation.setDirection(hoveringBox ? Direction.FORWARDS : Direction.BACKWARDS);
        this.openAnimation.setDirection(this.opened ? Direction.FORWARDS : Direction.BACKWARDS);
        Color outlineColor = ColorUtil.interpolateColorC(this.settingRectColor.brighter().brighter(), this.clientColors.getSecond(), 0.3f * this.hoverAnimation.getOutput().floatValue());
        outlineColor = ColorUtil.interpolateColorC(outlineColor, this.clientColors.getSecond(), this.openAnimation.getOutput().floatValue());
        final Color rectColor = ColorUtil.interpolateColorC(this.settingRectColor.brighter(), this.settingRectColor.brighter().brighter(), 0.5f * this.hoverAnimation.getOutput().floatValue() + this.openAnimation.getOutput().floatValue());
        RoundedUtil.drawRound(boxX, boxY, boxWidth, boxHeight, 4.0f, outlineColor);
        RoundedUtil.drawRound(boxX + 1.0f, boxY + 1.0f, boxWidth - 2.0f, boxHeight - 2.0f, 3.0f, rectColor);
        ModeComponent.tenacityFont14.drawString(modeSetting.name, boxX + 1.0f, this.y + 3.0f, this.textColor);
        ModeComponent.tenacityFont16.drawString(modeSetting.getMode(), boxX + 5.0f, boxY + ModeComponent.tenacityFont16.getMiddleOfBox(boxHeight), this.textColor);
        if (themeSetting) {
            final MutablePair<Color, Color> themeColors = Theme.getThemeColors(modeSetting.getMode()).apply((BiFunction<? super Color, ? super Color, ? extends MutablePair<Color, Color>>)MutablePair::of);
            themeColors.computeFirst(color -> ColorUtil.applyOpacity(color, this.alpha));
            themeColors.computeSecond(color -> ColorUtil.applyOpacity(color, this.alpha));
            final float height = 8.0f;
            final float width = 8.0f;
            final float middleOfRect = boxHeight / 2.0f - height / 2.0f;
            final float spacing = 3.0f;
            RoundedUtil.drawRound(boxX + 7.5f + ModeComponent.tenacityFont16.getStringWidth(modeSetting.getMode()), boxY + middleOfRect, width, height, 2.25f, themeColors.getFirst());
            RoundedUtil.drawRound(boxX + 7.5f + ModeComponent.tenacityFont16.getStringWidth(modeSetting.getMode()) + (spacing + width), boxY + middleOfRect, width, height, 2.25f, themeColors.getSecond());
        }
        RenderUtil.resetColor();
        RenderUtil.resetColor();
        final float arrowX = boxX + boxWidth - 11.0f;
        final float arrowY = boxY + ModeComponent.iconFont20.getMiddleOfBox(boxHeight) + 1.0f;
        final float openAnim = this.openAnimation.getOutput().floatValue();
        RenderUtil.rotateStart(arrowX, arrowY, ModeComponent.iconFont20.getStringWidth("z"), (float)ModeComponent.iconFont20.getHeight(), 180.0f * openAnim);
        ModeComponent.iconFont20.drawString("z", boxX + boxWidth - 11.0f, boxY + ModeComponent.iconFont20.getMiddleOfBox(boxHeight) + 1.0f, this.textColor);
        RenderUtil.rotateEnd();
        if (this.opened || !this.openAnimation.isDone()) {
            final float rectHeight = 15.0f;
            float rectCount = 0.0f;
            final float modeHeight = (modeSetting.modes.size() - 1) * rectHeight;
            final float modeY = boxY + boxHeight + 4.0f;
            final float modeX = boxX - 0.25f;
            RoundedUtil.drawRound(modeX, modeY, boxWidth, Math.max(4.0f, modeHeight * openAnim), 4.0f, ColorUtil.applyOpacity(this.settingRectColor.brighter(), openAnim));
            final boolean mouseOutsideRect = mouseY < modeY || mouseY > modeY + modeHeight || mouseX < modeX || mouseX > modeX + boxWidth;
            this.selectionBox.setDirection(mouseOutsideRect ? Direction.BACKWARDS : Direction.FORWARDS);
            RoundedUtil.drawRound(modeX + 1.5f, modeY + 1.5f + this.selectionBoxY.getOutput(), boxWidth - 3.0f, rectHeight - 3.0f, 2.5f, ColorUtil.applyOpacity(this.settingRectColor.brighter().brighter(), openAnim * this.selectionBox.getOutput().floatValue()));
            for (final String mode : modeSetting.modes) {
                if (mode.equals(modeSetting.getMode())) {
                    continue;
                }
                final boolean hoveringMode = HoveringUtil.isHovering(modeX, modeY + rectCount * rectHeight, boxWidth, rectHeight, mouseX, mouseY);
                if (hoveringMode) {
                    this.hoveringMode = mode;
                }
                if (mode.equals(this.hoveringMode)) {
                    this.selectionBoxY.animate(rectCount * rectHeight, 17);
                }
                RenderUtil.resetColor();
                ModeComponent.tenacityFont16.drawString(mode, modeX + 5.0f, modeY + (ModeComponent.tenacityFont16.getMiddleOfBox(rectHeight) + rectHeight * rectCount) * this.openAnimation.getOutput().floatValue(), ColorUtil.applyOpacity(this.textColor, openAnim));
                if (themeSetting) {
                    final MutablePair<Color, Color> themeColors2 = Theme.getThemeColors(mode).apply((BiFunction<? super Color, ? super Color, ? extends MutablePair<Color, Color>>)MutablePair::of);
                    themeColors2.computeFirst(color -> ColorUtil.applyOpacity(color, openAnim));
                    themeColors2.computeSecond(color -> ColorUtil.applyOpacity(color, openAnim));
                    final float height2 = 8.0f;
                    final float width2 = 8.0f;
                    final float spacing2 = 3.0f;
                    final float middleOfRect2 = rectHeight / 2.0f - height2 / 2.0f;
                    final float v = modeY + (middleOfRect2 + rectHeight * rectCount) * openAnim;
                    RoundedUtil.drawRound(modeX + boxWidth - (width2 + 4.0f + (width2 + spacing2)), v, width2, height2, 2.25f, themeColors2.getFirst());
                    RoundedUtil.drawRound(modeX + boxWidth - (width2 + 4.0f), v, width2, height2, 2.25f, themeColors2.getSecond());
                }
                ++rectCount;
            }
            this.countSize = 2.0f + (0.25f + rectCount * (rectHeight / (this.realHeight / this.normalCount))) * this.openAnimation.getOutput().floatValue();
        }
        else {
            this.countSize = 2.0f;
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final ModeSetting modeSetting = this.getSetting();
        final float boxHeight = 18.0f;
        final float boxY = this.y + this.realHeight / 2.0f - boxHeight / 2.0f + 3.0f;
        final float boxX = this.x + 6.0f;
        final float boxWidth = this.width - 10.0f;
        if (this.isClickable(boxY + boxHeight) && HoveringUtil.isHovering(boxX, boxY, boxWidth, boxHeight, mouseX, mouseY) && button == 1) {
            this.opened = !this.opened;
        }
        if (this.opened) {
            final float rectHeight = 15.0f;
            float rectCount = 0.0f;
            final float modeY = boxY + boxHeight + 4.0f;
            final float modeX = boxX - 1.0f;
            for (final String mode : modeSetting.modes) {
                if (mode.equals(modeSetting.getMode())) {
                    continue;
                }
                final boolean hoveringMode = HoveringUtil.isHovering(modeX, modeY + rectCount * rectHeight, boxWidth, rectHeight, mouseX, mouseY);
                if (this.isClickable(modeY + rectCount * rectHeight + rectHeight) && hoveringMode && button == 0) {
                    modeSetting.setCurrentMode(mode);
                    this.opened = false;
                    return;
                }
                ++rectCount;
            }
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
}
