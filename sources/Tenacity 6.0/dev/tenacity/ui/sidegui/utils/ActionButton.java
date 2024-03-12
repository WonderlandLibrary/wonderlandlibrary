// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.utils;

import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.ui.sidegui.SideGUI;
import dev.tenacity.utils.animations.Animation;
import java.awt.Color;
import dev.tenacity.utils.font.CustomFont;
import dev.tenacity.ui.Screen;

public class ActionButton implements Screen
{
    private float x;
    private float y;
    private float width;
    private float height;
    private float alpha;
    private boolean bypass;
    private final String name;
    private boolean bold;
    private CustomFont font;
    private Color color;
    private Runnable clickAction;
    private final Animation hoverAnimation;
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        boolean hovering = SideGUI.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
        if (this.bypass) {
            hovering = HoveringUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
        }
        this.hoverAnimation.setDirection(hovering ? Direction.FORWARDS : Direction.BACKWARDS);
        final Color rectColor = ColorUtil.interpolateColorC(this.color, this.color.brighter(), this.hoverAnimation.getOutput().floatValue());
        RoundedUtil.drawRound(this.x, this.y, this.width, this.height, 5.0f, ColorUtil.applyOpacity(rectColor, this.alpha));
        if (this.font != null) {
            this.font.drawCenteredString(this.name, this.x + this.width / 2.0f, this.y + this.font.getMiddleOfBox(this.height), ColorUtil.applyOpacity(-1, this.alpha));
        }
        else if (this.bold) {
            ActionButton.tenacityBoldFont18.drawCenteredString(this.name, this.x + this.width / 2.0f, this.y + ActionButton.tenacityFont18.getMiddleOfBox(this.height), ColorUtil.applyOpacity(-1, this.alpha));
        }
        else {
            ActionButton.tenacityFont18.drawCenteredString(this.name, this.x + this.width / 2.0f, this.y + ActionButton.tenacityFont18.getMiddleOfBox(this.height), ColorUtil.applyOpacity(-1, this.alpha));
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        boolean hovering = SideGUI.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
        if (this.bypass) {
            hovering = HoveringUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
        }
        if (hovering && button == 0 && this.clickAction != null) {
            this.clickAction.run();
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
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
    
    public void setBypass(final boolean bypass) {
        this.bypass = bypass;
    }
    
    public void setBold(final boolean bold) {
        this.bold = bold;
    }
    
    public void setFont(final CustomFont font) {
        this.font = font;
    }
    
    public void setColor(final Color color) {
        this.color = color;
    }
    
    public void setClickAction(final Runnable clickAction) {
        this.clickAction = clickAction;
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
    
    public boolean isBypass() {
        return this.bypass;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isBold() {
        return this.bold;
    }
    
    public CustomFont getFont() {
        return this.font;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public Runnable getClickAction() {
        return this.clickAction;
    }
    
    public Animation getHoverAnimation() {
        return this.hoverAnimation;
    }
    
    public ActionButton(final String name) {
        this.bypass = false;
        this.bold = false;
        this.color = ColorUtil.tripleColor(55);
        this.hoverAnimation = new DecelerateAnimation(250, 1.0);
        this.name = name;
    }
}
