// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.utils;

import dev.tenacity.Tenacity;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.ui.sidegui.SideGUI;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.utils.font.CustomFont;
import java.awt.Color;
import dev.tenacity.ui.Screen;

public class IconButton implements Screen
{
    private float x;
    private float y;
    private float alpha;
    public Color accentColor;
    public Color textColor;
    private Runnable clickAction;
    private boolean clickable;
    private CustomFont iconFont;
    private final Animation hoverAnimation;
    private String icon;
    private TooltipObject tooltip;
    
    public IconButton(final String icon) {
        this.textColor = new Color(191, 191, 191);
        this.clickable = true;
        this.iconFont = IconButton.iconFont16;
        this.hoverAnimation = new DecelerateAnimation(250, 1.0);
        this.icon = icon;
    }
    
    public IconButton(final String icon, final String tooltip) {
        this.textColor = new Color(191, 191, 191);
        this.clickable = true;
        this.iconFont = IconButton.iconFont16;
        this.hoverAnimation = new DecelerateAnimation(250, 1.0);
        this.icon = icon;
        this.tooltip = new TooltipObject(tooltip);
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        final float iconWidth = this.iconFont.getStringWidth(this.icon);
        final float iconHeight = (float)this.iconFont.getHeight();
        final boolean hovering = SideGUI.isHovering(this.x - 3.0f, this.y - 3.0f, iconWidth + 6.0f, iconHeight + 6.0f, mouseX, mouseY);
        this.hoverAnimation.setDirection(hovering ? Direction.FORWARDS : Direction.BACKWARDS);
        final Color iconColor = ColorUtil.interpolateColorC(this.textColor, this.accentColor, this.hoverAnimation.getOutput().floatValue());
        this.iconFont.drawString(this.icon, this.x, this.y, ColorUtil.applyOpacity(iconColor, this.alpha));
        if (this.tooltip != null) {
            Tenacity.INSTANCE.getSideGui().addTooltip(this.tooltip);
            this.tooltip.setHovering(hovering);
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final boolean hovering = SideGUI.isHovering(this.x - 3.0f, this.y - 3.0f, IconButton.iconFont16.getStringWidth(this.icon) + 6.0f, (float)(IconButton.iconFont16.getHeight() + 6), mouseX, mouseY);
        if (this.clickable && button == 0 && hovering && this.clickAction != null) {
            this.clickAction.run();
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public float getWidth() {
        return this.iconFont.getStringWidth(this.icon);
    }
    
    public float getHeight() {
        return (float)this.iconFont.getHeight();
    }
    
    public void setTooltip(final String tooltipText) {
        if (this.tooltip == null) {
            this.tooltip = new TooltipObject(tooltipText);
        }
        else {
            this.tooltip.setTip(tooltipText);
        }
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
    
    public void setTextColor(final Color textColor) {
        this.textColor = textColor;
    }
    
    public void setClickAction(final Runnable clickAction) {
        this.clickAction = clickAction;
    }
    
    public void setClickable(final boolean clickable) {
        this.clickable = clickable;
    }
    
    public void setIconFont(final CustomFont iconFont) {
        this.iconFont = iconFont;
    }
    
    public void setIcon(final String icon) {
        this.icon = icon;
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
    
    public String getIcon() {
        return this.icon;
    }
}
