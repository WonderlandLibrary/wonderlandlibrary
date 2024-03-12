// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.panels.infopanel;

import java.util.Iterator;
import java.util.List;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.render.ColorUtil;
import java.awt.Color;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.ui.Screen;

public class InfoButton implements Screen
{
    private final String question;
    private final String answer;
    private float x;
    private float y;
    private float width;
    private float height;
    private float alpha;
    private float count;
    private final Animation openAnimation;
    private final Animation hoverAnimation;
    
    public InfoButton(final String question, final String answer) {
        this.count = 1.0f;
        this.openAnimation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
        this.hoverAnimation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
        this.question = question;
        this.answer = answer;
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        final Color textColor = ColorUtil.applyOpacity(Color.WHITE, this.alpha);
        final boolean hovering = HoveringUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
        this.hoverAnimation.setDirection(hovering ? Direction.FORWARDS : Direction.BACKWARDS);
        this.hoverAnimation.setDuration(hovering ? 200 : 400);
        float additionalCount = 0.0f;
        if (!this.openAnimation.isDone() || this.openAnimation.finished(Direction.FORWARDS)) {
            final float heightIncrement = 3.0f;
            final float openAnim = this.openAnimation.getOutput().floatValue();
            final List<String> lines = InfoButton.tenacityFont16.getWrappedLines(this.answer, this.x + 5.0f, this.width - 5.0f, heightIncrement);
            final int spacing = 3;
            final float totalAnswerHeight = lines.size() * (InfoButton.tenacityFont16.getHeight() + heightIncrement) + 4.0f;
            final float additionalHeight = this.height + totalAnswerHeight + spacing * 2;
            RenderUtil.scissorStart(this.x - 1.0f, this.y + 5.0f, this.width + 2.0f, additionalHeight - 5.0f);
            float answerY = this.y + this.height + spacing - (spacing + totalAnswerHeight) * (1.0f - openAnim);
            RoundedUtil.drawRound(this.x, answerY, this.width, totalAnswerHeight, 5.0f, ColorUtil.tripleColor(55, this.alpha));
            for (final String line : lines) {
                InfoButton.tenacityFont16.drawString(line, this.x + 3.0f, answerY + 3.5f, textColor);
                answerY += InfoButton.tenacityFont16.getHeight() + heightIncrement;
            }
            RenderUtil.scissorEnd();
            additionalCount = (totalAnswerHeight + spacing) * openAnim / this.height;
        }
        final int additionalColor = (int)(5.0f * this.hoverAnimation.getOutput().floatValue());
        RoundedUtil.drawRound(this.x, this.y, this.width, this.height, 5.0f, ColorUtil.tripleColor(37 + additionalColor, this.alpha));
        InfoButton.tenacityBoldFont18.drawString(this.question, this.x + 5.0f, this.y + InfoButton.tenacityBoldFont18.getMiddleOfBox(this.height), textColor);
        final float iconX = this.x + this.width - (InfoButton.iconFont20.getStringWidth("z") + 5.0f);
        final float iconY = this.y + InfoButton.iconFont20.getMiddleOfBox(this.height) + 1.0f;
        RenderUtil.rotateStart(iconX, iconY, InfoButton.iconFont20.getStringWidth("z"), (float)InfoButton.iconFont20.getHeight(), 180.0f * this.openAnimation.getOutput().floatValue());
        InfoButton.iconFont20.drawString("z", iconX, iconY, textColor);
        RenderUtil.rotateEnd();
        this.count = 1.0f + additionalCount;
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (HoveringUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY) && button == 1) {
            this.openAnimation.changeDirection();
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public String getQuestion() {
        return this.question;
    }
    
    public String getAnswer() {
        return this.answer;
    }
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public float getWidth() {
        return this.width;
    }
    
    public float getHeight() {
        return this.height;
    }
    
    public float getAlpha() {
        return this.alpha;
    }
    
    public float getCount() {
        return this.count;
    }
    
    public Animation getOpenAnimation() {
        return this.openAnimation;
    }
    
    public Animation getHoverAnimation() {
        return this.hoverAnimation;
    }
    
    public void setX(final float x) {
        this.x = x;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
    
    public void setWidth(final float width) {
        this.width = width;
    }
    
    public void setHeight(final float height) {
        this.height = height;
    }
    
    public void setAlpha(final float alpha) {
        this.alpha = alpha;
    }
    
    public void setCount(final float count) {
        this.count = count;
    }
}
