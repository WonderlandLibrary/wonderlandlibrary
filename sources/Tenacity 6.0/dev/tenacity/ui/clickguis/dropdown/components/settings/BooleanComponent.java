// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.dropdown.components.settings;

import dev.tenacity.utils.render.RoundedUtil;
import java.awt.Color;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.ui.clickguis.dropdown.components.SettingComponent;

public class BooleanComponent extends SettingComponent<BooleanSetting>
{
    private final Animation toggleAnimation;
    private final Animation hoverAnimation;
    
    public BooleanComponent(final BooleanSetting booleanSetting) {
        super(booleanSetting);
        this.toggleAnimation = new DecelerateAnimation(250, 1.0, Direction.BACKWARDS);
        this.hoverAnimation = new DecelerateAnimation(250, 1.0, Direction.BACKWARDS);
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        this.toggleAnimation.setDirection(this.getSetting().isEnabled() ? Direction.FORWARDS : Direction.BACKWARDS);
        RenderUtil.resetColor();
        BooleanComponent.tenacityFont16.drawString(this.getSetting().name, this.x + 5.0f, this.y + BooleanComponent.tenacityFont16.getMiddleOfBox(this.height), ColorUtil.applyOpacity(this.textColor, 0.5f + 0.5f * this.toggleAnimation.getOutput().floatValue()));
        final float switchWidth = 17.0f;
        final float switchHeight = 7.0f;
        final float booleanX = this.x + this.width - (switchWidth + 5.5f);
        final float booleanY = this.y + this.height / 2.0f - switchHeight / 2.0f;
        final boolean hovering = HoveringUtil.isHovering(booleanX - 2.0f, booleanY - 2.0f, switchWidth + 4.0f, switchHeight + 4.0f, mouseX, mouseY);
        this.hoverAnimation.setDirection(hovering ? Direction.FORWARDS : Direction.BACKWARDS);
        final Color accentCircle = ColorUtil.applyOpacity(this.clientColors.getSecond(), this.alpha);
        Color rectColor = ColorUtil.interpolateColorC(this.settingRectColor.brighter().brighter(), accentCircle, this.toggleAnimation.getOutput().floatValue());
        rectColor = ColorUtil.interpolateColorC(rectColor, ColorUtil.brighter(rectColor, 0.8f), this.hoverAnimation.getOutput().floatValue());
        RenderUtil.resetColor();
        RoundedUtil.drawRound(booleanX, booleanY, switchWidth, switchHeight, 3.0f, rectColor);
        RenderUtil.resetColor();
        RoundedUtil.drawRound(this.x + this.width - (switchWidth + 4.0f) + (switchWidth - 8.0f) * this.toggleAnimation.getOutput().floatValue(), this.y + BooleanComponent.tenacityFont16.getMiddleOfBox(this.height) + 0.5f, 5.0f, 5.0f, 2.0f, this.textColor);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final float switchWidth = 17.0f;
        final float switchHeight = 7.0f;
        final float booleanX = this.x + this.width - (switchWidth + 5.5f);
        final float booleanY = this.y + this.height / 2.0f - switchHeight / 2.0f;
        final boolean hovering = HoveringUtil.isHovering(booleanX - 2.0f, booleanY - 2.0f, switchWidth + 4.0f, switchHeight + 4.0f, mouseX, mouseY);
        if (this.isClickable(booleanY + switchHeight) && hovering && button == 0) {
            this.getSetting().toggle();
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
}
