// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.utils;

import java.util.Iterator;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.ui.sidegui.SideGUI;
import dev.tenacity.module.impl.render.HUDMod;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.render.ColorUtil;
import java.util.stream.Collectors;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import java.util.function.Function;
import java.util.Arrays;
import dev.tenacity.utils.animations.ContinualAnimation;
import dev.tenacity.utils.animations.Animation;
import java.util.Map;
import java.awt.Color;
import dev.tenacity.ui.Screen;

public class CarouselButtons implements Screen
{
    private float x;
    private float y;
    private float rectWidth;
    private float rectHeight;
    private float alpha;
    private boolean hovering;
    private Color backgroundColor;
    private String currentButton;
    private final Map<String, Animation> options;
    private final ContinualAnimation rectAnimation;
    
    public CarouselButtons(final String... options) {
        this.hovering = false;
        this.backgroundColor = new Color(39, 39, 39);
        this.rectAnimation = new ContinualAnimation();
        this.options = Arrays.stream(options).collect(Collectors.toMap((Function<? super String, ? extends String>)Function.identity(), v -> new DecelerateAnimation(250, 1.0)));
        this.currentButton = options[0];
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        final float buttonWidth = this.options.size() * this.rectWidth;
        final Color textColor = ColorUtil.applyOpacity(Color.WHITE, this.alpha);
        this.hovering = HoveringUtil.isHovering(this.x, this.y, buttonWidth, this.rectHeight, mouseX, mouseY);
        RoundedUtil.drawRound(this.x, this.y, buttonWidth, this.rectHeight, 5.0f, ColorUtil.applyOpacity(this.backgroundColor, this.alpha));
        final Color accentColor = ColorUtil.applyOpacity(HUDMod.getClientColors().getFirst(), this.getAlpha());
        final float coloredX = this.rectAnimation.getOutput();
        RoundedUtil.drawRound(this.x + coloredX, this.y, this.rectWidth, this.rectHeight, 5.0f, accentColor);
        int seperation = 0;
        for (final Map.Entry<String, Animation> entry : this.options.entrySet()) {
            final float buttonX = this.x + seperation;
            final boolean isCurrentButton = entry.getKey().equals(this.currentButton);
            final Animation animation = entry.getValue();
            if (isCurrentButton) {
                this.rectAnimation.animate((float)seperation, 18);
            }
            final boolean hovering = SideGUI.isHovering(buttonX, this.y, buttonWidth, this.rectHeight, mouseX, mouseY);
            animation.setDirection(hovering ? Direction.FORWARDS : Direction.BACKWARDS);
            final float textAlpha = 0.5f + 0.3f * animation.getOutput().floatValue();
            CarouselButtons.tenacityFont24.drawCenteredString(entry.getKey(), this.x + this.rectWidth / 2.0f + seperation, this.y + CarouselButtons.tenacityFont24.getMiddleOfBox(this.rectHeight), ColorUtil.applyOpacity(isCurrentButton ? textColor : ColorUtil.applyOpacity(textColor, textAlpha), this.getAlpha()));
            seperation += (int)this.rectWidth;
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        int seperation = 0;
        for (final Map.Entry<String, Animation> entry : this.options.entrySet()) {
            final float buttonX = this.x + seperation;
            final boolean hovering = SideGUI.isHovering(buttonX, this.y, this.rectWidth, this.rectHeight, mouseX, mouseY);
            if (hovering) {
                this.currentButton = entry.getKey();
            }
            seperation += (int)this.rectWidth;
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public float getTotalWidth() {
        return this.options.size() * this.rectWidth;
    }
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public float getRectWidth() {
        return this.rectWidth;
    }
    
    public float getRectHeight() {
        return this.rectHeight;
    }
    
    public float getAlpha() {
        return this.alpha;
    }
    
    public void setX(final float x) {
        this.x = x;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
    
    public void setRectWidth(final float rectWidth) {
        this.rectWidth = rectWidth;
    }
    
    public void setRectHeight(final float rectHeight) {
        this.rectHeight = rectHeight;
    }
    
    public void setAlpha(final float alpha) {
        this.alpha = alpha;
    }
    
    public boolean isHovering() {
        return this.hovering;
    }
    
    public void setBackgroundColor(final Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    
    public String getCurrentButton() {
        return this.currentButton;
    }
}
