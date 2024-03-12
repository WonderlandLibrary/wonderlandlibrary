// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.utils;

import dev.tenacity.utils.tuples.mutable.MutablePair;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.ui.Screen;

public class TooltipObject implements Screen
{
    private boolean hovering;
    private boolean round;
    private final Animation fadeInAnimation;
    private String tooltip;
    private String additionalInformation;
    private float width;
    private float height;
    
    public TooltipObject(final String tooltip) {
        this.hovering = false;
        this.round = true;
        this.fadeInAnimation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
        this.width = 150.0f;
        this.height = 40.0f;
        this.tooltip = tooltip;
    }
    
    public TooltipObject() {
        this.hovering = false;
        this.round = true;
        this.fadeInAnimation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
        this.width = 150.0f;
        this.height = 40.0f;
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        this.fadeInAnimation.setDirection(this.hovering ? Direction.FORWARDS : Direction.BACKWARDS);
        final float x = (float)(mouseX - 2);
        final float y = (float)(mouseY + 13);
        final float fadeAnim = this.fadeInAnimation.getOutput().floatValue();
        if (this.tooltip == null || this.fadeInAnimation.finished(Direction.BACKWARDS)) {
            return;
        }
        if (this.tooltip.contains("\n")) {
            RenderUtil.scissorStart(x - 1.5f, y - 1.5f, (this.width + 4.0f) * fadeAnim, this.height + 4.0f);
            RoundedUtil.drawRound(x - 0.75f, y - 0.75f, this.width + 1.5f, this.height + 1.5f, 3.0f, ColorUtil.tripleColor(45, fadeAnim));
            RoundedUtil.drawRound(x, y, this.width, this.height, 2.5f, ColorUtil.applyOpacity(ColorUtil.tripleColor(15), fadeAnim));
            final MutablePair<Float, Float> whPair = TooltipObject.tenacityFont14.drawNewLineText(this.tooltip, x + 2.0f, y + 2.0f, ColorUtil.applyOpacity(-1, fadeAnim), 3.0f);
            float additionalHeight = 0.0f;
            if (this.additionalInformation != null) {
                additionalHeight = TooltipObject.tenacityFont14.drawWrappedText(this.additionalInformation, x + 2.0f, y + 1.5f + whPair.getSecond(), ColorUtil.applyOpacity(-1, fadeAnim), this.width, 3.0f);
            }
            RenderUtil.scissorEnd();
            if (this.additionalInformation != null) {
                this.width = Math.max(150.0f, whPair.getFirst() + 4.0f);
            }
            else {
                this.width = whPair.getFirst() + 4.0f;
            }
            this.height = whPair.getSecond() + additionalHeight;
        }
        else {
            this.width = TooltipObject.tenacityFont14.getStringWidth(this.tooltip) + 4.0f;
            this.height = (float)(TooltipObject.tenacityFont14.getHeight() + 2);
            RenderUtil.scissorStart(x - 1.5f, y - 1.5f, (this.width + 4.0f) * fadeAnim, this.height + 4.0f);
            if (this.round) {
                RoundedUtil.drawRound(x - 0.75f, y - 0.75f, this.width + 1.5f, this.height + 1.5f, 3.0f, ColorUtil.tripleColor(45, fadeAnim));
                RoundedUtil.drawRound(x, y, this.width, this.height, 2.5f, ColorUtil.applyOpacity(ColorUtil.tripleColor(15), fadeAnim));
            }
            else {
                RenderUtil.drawBorderedRect(x, y, this.width, this.height, 1.0f, ColorUtil.tripleColor(15, fadeAnim).getRGB(), ColorUtil.tripleColor(45, fadeAnim).getRGB());
            }
            TooltipObject.tenacityFont14.drawCenteredString(this.tooltip, x + this.width / 2.0f, y + TooltipObject.tenacityFont14.getMiddleOfBox(this.height), ColorUtil.applyOpacity(-1, fadeAnim));
            RenderUtil.scissorEnd();
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public void setTip(final String tooltip) {
        this.tooltip = tooltip;
    }
    
    public void setAdditionalInformation(final String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
    
    public void setHovering(final boolean hovering) {
        this.hovering = hovering;
    }
    
    public boolean isHovering() {
        return this.hovering;
    }
    
    public void setRound(final boolean round) {
        this.round = round;
    }
    
    public Animation getFadeInAnimation() {
        return this.fadeInAnimation;
    }
}
