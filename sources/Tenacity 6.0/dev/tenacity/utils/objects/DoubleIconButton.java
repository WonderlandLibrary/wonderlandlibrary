// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.objects;

import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.utils.font.CustomFont;
import java.awt.Color;
import dev.tenacity.ui.Screen;

public class DoubleIconButton implements Screen
{
    private float x;
    private float y;
    private float alpha;
    private Color accentColor;
    private CustomFont iconFont;
    private boolean enabled;
    private boolean hovering;
    private final Animation hoverAnimation;
    private final String disabledIcon;
    private final String enabledIcon;
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        this.accentColor = ColorUtil.applyOpacity(this.accentColor, this.alpha);
        this.hovering = HoveringUtil.isHovering(this.x - 2.0f, this.y - 2.0f, this.getWidth() + 4.0f, this.getHeight() + 4.0f, mouseX, mouseY);
        this.hoverAnimation.setDirection(this.hovering ? Direction.FORWARDS : Direction.BACKWARDS);
        if (this.enabled) {
            if (!this.hoverAnimation.isDone() || this.hovering) {
                this.iconFont.drawString(this.disabledIcon, this.x, this.y, ColorUtil.applyOpacity(this.accentColor, this.alpha));
            }
            this.iconFont.drawString(this.enabledIcon, this.x, this.y, ColorUtil.applyOpacity(this.accentColor, (1.0f - 0.45f * this.hoverAnimation.getOutput().floatValue()) * this.alpha));
        }
        else {
            this.iconFont.drawString(this.disabledIcon, this.x, this.y, ColorUtil.applyOpacity(ColorUtil.interpolateColorC(Color.WHITE, this.accentColor, this.hoverAnimation.getOutput().floatValue()), this.alpha));
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.hovering) {
            this.enabled = !this.enabled;
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public float getWidth() {
        return this.iconFont.getStringWidth(this.enabled ? this.enabledIcon : this.disabledIcon);
    }
    
    public float getHeight() {
        return (float)this.iconFont.getHeight();
    }
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public float getAlpha() {
        return this.alpha;
    }
    
    public Color getAccentColor() {
        return this.accentColor;
    }
    
    public CustomFont getIconFont() {
        return this.iconFont;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public boolean isHovering() {
        return this.hovering;
    }
    
    public Animation getHoverAnimation() {
        return this.hoverAnimation;
    }
    
    public String getDisabledIcon() {
        return this.disabledIcon;
    }
    
    public String getEnabledIcon() {
        return this.enabledIcon;
    }
    
    public void setX(final float x) {
        this.x = x;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
    
    public void setAlpha(final float alpha) {
        this.alpha = alpha;
    }
    
    public void setAccentColor(final Color accentColor) {
        this.accentColor = accentColor;
    }
    
    public void setIconFont(final CustomFont iconFont) {
        this.iconFont = iconFont;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public void setHovering(final boolean hovering) {
        this.hovering = hovering;
    }
    
    public DoubleIconButton(final String disabledIcon, final String enabledIcon) {
        this.iconFont = DoubleIconButton.iconFont20;
        this.enabled = false;
        this.hoverAnimation = new DecelerateAnimation(200, 1.0);
        this.disabledIcon = disabledIcon;
        this.enabledIcon = enabledIcon;
    }
}
