// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.dropdown.components.settings;

import java.awt.Color;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.misc.MathUtils;
import org.lwjgl.input.Keyboard;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.ContinualAnimation;
import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.ui.clickguis.dropdown.components.SettingComponent;

public class NumberComponent extends SettingComponent<NumberSetting>
{
    private final Animation hoverAnimation;
    private final Pair<Animation, Animation> textAnimations;
    private boolean dragging;
    private final ContinualAnimation animationWidth;
    public float clickCountAdd;
    private boolean selected;
    
    public NumberComponent(final NumberSetting numberSetting) {
        super(numberSetting);
        this.hoverAnimation = new DecelerateAnimation(250, 1.0, Direction.BACKWARDS);
        this.textAnimations = (Pair<Animation, Animation>)Pair.of(new DecelerateAnimation(250, 1.0), new DecelerateAnimation(250, 1.0));
        this.animationWidth = new ContinualAnimation();
        this.clickCountAdd = 0.0f;
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.selected) {
            Keyboard.enableRepeatEvents(true);
            final double increment = this.getSetting().getIncrement();
            switch (keyCode) {
                case 203: {
                    this.getSetting().setValue(this.getSetting().getValue() - increment);
                    break;
                }
                case 205: {
                    this.getSetting().setValue(this.getSetting().getValue() + increment);
                    break;
                }
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        final NumberSetting numberSetting = this.getSetting();
        String value = String.valueOf(MathUtils.round(this.getSetting().getValue(), 2));
        value = (value.contains(".") ? value.replaceAll("0*$", "").replaceAll("\\.$", "") : value);
        final float sliderX = this.x + 5.0f;
        final float sliderWidth = this.width - 10.0f;
        final float sliderY = this.y + 13.0f;
        final float sliderHeight = 3.0f;
        this.textAnimations.getFirst().setDirection(this.dragging ? Direction.BACKWARDS : Direction.FORWARDS);
        this.textAnimations.getSecond().setDirection((this.selected && !this.dragging) ? Direction.FORWARDS : Direction.BACKWARDS);
        final boolean hovering = HoveringUtil.isHovering(sliderX, sliderY - 2.0f, sliderWidth, sliderHeight + 4.0f, mouseX, mouseY);
        this.hoverAnimation.setDirection((hovering || this.dragging) ? Direction.FORWARDS : Direction.BACKWARDS);
        final float firstTextAnim = this.textAnimations.getFirst().getOutput().floatValue();
        final float funnyWidth = NumberComponent.tenacityFont16.getStringWidth(numberSetting.name) - NumberComponent.tenacityFont16.getStringWidth(": " + value);
        NumberComponent.tenacityFont16.drawString(": §l" + value, sliderX + funnyWidth + NumberComponent.tenacityFont16.getStringWidth(": " + value) * firstTextAnim, this.y + 2.0f, ColorUtil.applyOpacity(this.textColor, firstTextAnim));
        final String text = "You can use arrow keys";
        NumberComponent.tenacityFont14.drawCenteredString(text, this.x + this.width / 2.0f, sliderY + sliderHeight + 4.5f, ColorUtil.applyOpacity(-1, this.textAnimations.getSecond().getOutput().floatValue() * 0.25f));
        NumberComponent.tenacityFont16.drawString(numberSetting.name, sliderX, this.y + 2.0f, this.textColor);
        RoundedUtil.drawRound(sliderX, sliderY, sliderWidth, sliderHeight, 1.5f, ColorUtil.brighter(this.settingRectColor, 0.7f - 0.2f * this.hoverAnimation.getOutput().floatValue()));
        final double currentValue = numberSetting.getValue();
        if (this.dragging) {
            final float percent = Math.min(1.0f, Math.max(0.0f, (mouseX - sliderX) / sliderWidth));
            final double newValue = MathUtils.interpolate(numberSetting.getMinValue(), numberSetting.getMaxValue(), percent);
            numberSetting.setValue(newValue);
        }
        final float widthPercentage = (float)((currentValue - numberSetting.getMinValue()) / (numberSetting.getMaxValue() - numberSetting.getMinValue()));
        this.animationWidth.animate(sliderWidth * widthPercentage, 20);
        final float animatedWidth = this.animationWidth.getOutput();
        RoundedUtil.drawRound(sliderX, sliderY, animatedWidth, sliderHeight, 1.5f, this.clientColors.getSecond());
        float size = 7.0f;
        RoundedUtil.drawRound(sliderX + animatedWidth - size / 2.0f, sliderY - (size / 4.0f + 0.5f), size, size, size / 2.0f - 0.5f, this.settingRectColor);
        size = 5.0f;
        RoundedUtil.drawRound(sliderX + animatedWidth - size / 2.0f, sliderY - size / 4.0f, size, size, size / 2.0f - 0.5f, this.textColor);
        final float secondTextAnim = 1.0f - this.textAnimations.getFirst().getOutput().floatValue();
        final float rectWidth = NumberComponent.tenacityFont14.getStringWidth("§l" + value) + 4.0f;
        final float rectX = Math.max(this.x, 2.0f + (sliderX + animatedWidth - size / 2.0f) - rectWidth / 2.0f);
        final float rectY = sliderY + sliderHeight + 4.0f;
        final float rectHeight = (float)(NumberComponent.tenacityFont14.getHeight() + 2);
        RoundedUtil.drawRound(rectX, rectY, rectWidth, rectHeight, 3.0f, ColorUtil.applyOpacity(this.settingRectColor.brighter(), secondTextAnim));
        NumberComponent.tenacityFont14.drawString("§l" + value, rectX + 2.0f, rectY + NumberComponent.tenacityFont14.getMiddleOfBox(rectHeight), ColorUtil.applyOpacity(this.textColor, secondTextAnim));
        this.clickCountAdd = 0.3f * secondTextAnim + 0.3f * this.textAnimations.getSecond().getOutput().floatValue();
        this.countSize = (float)(1.5 + this.clickCountAdd);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final float sliderX = this.x + 5.0f;
        final float sliderWidth = this.width - 10.0f;
        final float sliderY = this.y + this.height / 2.0f + 2.0f;
        final float sliderHeight = 3.0f;
        if (!HoveringUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY)) {
            this.selected = false;
        }
        if (this.isClickable(sliderY + sliderHeight) && HoveringUtil.isHovering(sliderX, sliderY - 2.0f, sliderWidth, sliderHeight + 4.0f, mouseX, mouseY) && button == 0) {
            this.selected = true;
            this.dragging = true;
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (this.dragging) {
            this.dragging = false;
        }
    }
}
