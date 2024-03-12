// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.utils;

import java.util.Iterator;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.ui.sidegui.SideGUI;
import java.util.Arrays;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.ContinualAnimation;
import dev.tenacity.utils.animations.Animation;
import java.util.List;
import java.awt.Color;
import dev.tenacity.ui.Screen;

public class DropdownObject implements Screen
{
    private float x;
    private float y;
    private float width;
    private float height;
    private float alpha;
    private Color accentColor;
    private final String name;
    private String selection;
    private List<String> options;
    private boolean opened;
    private boolean bypass;
    private final Animation openAnimation;
    private final Animation hoverAnimation;
    private final ContinualAnimation hoverRectAnimation;
    private final Animation hoverRectFadeAnimation;
    
    public DropdownObject(final String name, final String... options) {
        this.opened = false;
        this.bypass = false;
        this.openAnimation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
        this.hoverAnimation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
        this.hoverRectAnimation = new ContinualAnimation();
        this.hoverRectFadeAnimation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
        this.name = name;
        this.selection = options[0];
        this.options = Arrays.asList(options);
    }
    
    public DropdownObject(final String name) {
        this.opened = false;
        this.bypass = false;
        this.openAnimation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
        this.hoverAnimation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
        this.hoverRectAnimation = new ContinualAnimation();
        this.hoverRectFadeAnimation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
        this.name = name;
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        boolean hoveringMainRect = SideGUI.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
        if (this.bypass) {
            hoveringMainRect = HoveringUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
        }
        this.hoverAnimation.setDirection(hoveringMainRect ? Direction.FORWARDS : Direction.BACKWARDS);
        final Color textColor = ColorUtil.applyOpacity(Color.WHITE, this.alpha);
        this.openAnimation.setDirection(this.opened ? Direction.FORWARDS : Direction.BACKWARDS);
        final float openAnim = this.openAnimation.getOutput().floatValue();
        if (!this.openAnimation.isDone() || this.opened) {
            final float dropdownY = this.getY() + (this.getHeight() + 4.0f) * openAnim;
            final float dropdownHeight = this.options.size() * this.getHeight();
            RoundedUtil.drawRound(this.getX(), dropdownY, this.getWidth(), dropdownHeight, 3.0f, ColorUtil.tripleColor(17, this.getAlpha() * openAnim));
            final boolean mouseOutsideRect = mouseY < dropdownY || mouseY > dropdownY + dropdownHeight || mouseX < this.getX() || mouseX > this.getX() + this.getWidth();
            this.hoverRectFadeAnimation.setDirection(mouseOutsideRect ? Direction.BACKWARDS : Direction.FORWARDS);
            this.hoverRectFadeAnimation.setDuration(mouseOutsideRect ? 200 : 350);
            RoundedUtil.drawRound(this.getX(), this.hoverRectAnimation.getOutput(), this.getWidth(), this.getHeight(), 3.0f, ColorUtil.tripleColor(26, this.getAlpha() * this.hoverRectFadeAnimation.getOutput().floatValue() * openAnim));
            int seperation = 0;
            for (final String option : this.options) {
                boolean hovering = SideGUI.isHovering(this.getX(), dropdownY + seperation, this.getWidth(), this.getHeight(), mouseX, mouseY);
                if (this.bypass) {
                    hovering = HoveringUtil.isHovering(this.getX(), dropdownY + seperation, this.getWidth(), this.getHeight(), mouseX, mouseY);
                }
                if (hovering) {
                    this.hoverRectAnimation.animate(dropdownY + seperation, 18);
                }
                final Color optionColor = this.selection.equals(option) ? this.accentColor : textColor;
                DropdownObject.tenacityFont18.drawString(option, this.getX() + 5.0f, dropdownY + seperation + DropdownObject.tenacityFont18.getMiddleOfBox(this.getHeight()), ColorUtil.applyOpacity(optionColor, openAnim));
                seperation += (int)this.getHeight();
            }
        }
        RoundedUtil.drawRound(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 3.0f, ColorUtil.tripleColor(17 + (int)(3.0f * this.hoverAnimation.getOutput().floatValue()), this.getAlpha()));
        DropdownObject.tenacityFont18.drawString("§l" + this.name + ":§r " + this.selection, this.getX() + 4.0f, this.getY() + DropdownObject.tenacityFont18.getMiddleOfBox(this.getHeight()), textColor);
        final float iconX = this.getX() + this.getWidth() - 10.0f;
        final float iconY = this.getY() + DropdownObject.iconFont16.getMiddleOfBox(this.getHeight());
        RenderUtil.rotateStart(iconX, iconY, DropdownObject.iconFont20.getStringWidth("z"), (float)DropdownObject.iconFont20.getHeight(), 180.0f * this.openAnimation.getOutput().floatValue());
        DropdownObject.iconFont20.drawString("z", this.getX() + this.getWidth() - 10.0f, this.getY() + DropdownObject.iconFont20.getMiddleOfBox(this.getHeight()) + 1.0f, textColor);
        RenderUtil.rotateEnd();
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        boolean hoveringMainRect = SideGUI.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
        if (this.bypass) {
            hoveringMainRect = HoveringUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
        }
        if (hoveringMainRect && button == 1) {
            this.opened = !this.opened;
        }
        if (this.opened) {
            final float dropdownY = this.getY() + (this.getHeight() + 4.0f);
            int seperation = 0;
            for (final String option : this.options) {
                boolean hovering = SideGUI.isHovering(this.getX(), dropdownY + seperation, this.getWidth(), this.getHeight(), mouseX, mouseY);
                if (this.bypass) {
                    hovering = HoveringUtil.isHovering(this.getX(), dropdownY + seperation, this.getWidth(), this.getHeight(), mouseX, mouseY);
                }
                if (hovering && button == 0) {
                    this.selection = option;
                    this.opened = false;
                }
                seperation += (int)this.getHeight();
            }
        }
    }
    
    public boolean isClosed() {
        return this.openAnimation.finished(Direction.BACKWARDS);
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public void setupOptions(final String... options) {
        this.options = Arrays.asList(options);
        this.selection = options[0];
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
    
    public void setAccentColor(final Color accentColor) {
        this.accentColor = accentColor;
    }
    
    public String getSelection() {
        return this.selection;
    }
    
    public void setBypass(final boolean bypass) {
        this.bypass = bypass;
    }
}
