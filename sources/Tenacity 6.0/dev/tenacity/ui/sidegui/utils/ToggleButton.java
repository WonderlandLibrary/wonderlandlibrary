// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.utils;

import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.ui.sidegui.SideGUI;
import dev.tenacity.utils.misc.HoveringUtil;
import java.awt.Color;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.Tenacity;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.ui.Screen;

public class ToggleButton implements Screen
{
    private float x;
    private float y;
    private float alpha;
    private boolean enabled;
    private final String name;
    private boolean bypass;
    private final float WH = 10.0f;
    private final Animation toggleAnimation;
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        final int textColor = ColorUtil.applyOpacity(-1, this.alpha);
        ToggleButton.tenacityFont16.drawString(this.name, this.x - (ToggleButton.tenacityFont16.getStringWidth(this.name) + 5.0f), this.y + ToggleButton.tenacityFont16.getMiddleOfBox(10.0f), textColor);
        this.toggleAnimation.setDirection(this.enabled ? Direction.FORWARDS : Direction.BACKWARDS);
        final float toggleAnim = this.toggleAnimation.getOutput().floatValue();
        final Color roundColor = ColorUtil.interpolateColorC(ColorUtil.tripleColor(64), Tenacity.INSTANCE.getSideGui().getGreenEnabledColor(), toggleAnim);
        RoundedUtil.drawRound(this.x, this.y, 10.0f, 10.0f, 4.75f, roundColor);
        if (this.enabled || !this.toggleAnimation.isDone()) {
            RenderUtil.scaleStart(this.x + this.getWH() / 2.0f, this.y + this.getWH() / 2.0f, toggleAnim);
            ToggleButton.iconFont16.drawString("o", this.x + 1.0f, this.y + 3.5f, ColorUtil.applyOpacity(textColor, toggleAnim));
            RenderUtil.scaleEnd();
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (button == 0) {
            if (this.bypass && HoveringUtil.isHovering(this.x, this.y, 10.0f, 10.0f, mouseX, mouseY)) {
                this.enabled = !this.enabled;
            }
            else if (SideGUI.isHovering(this.x, this.y, 10.0f, 10.0f, mouseX, mouseY)) {
                this.enabled = !this.enabled;
            }
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public float getActualX() {
        return this.x - (ToggleButton.tenacityFont16.getStringWidth(this.name) + 5.0f);
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isBypass() {
        return this.bypass;
    }
    
    public float getWH() {
        this.getClass();
        return 10.0f;
    }
    
    public Animation getToggleAnimation() {
        return this.toggleAnimation;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public void setBypass(final boolean bypass) {
        this.bypass = bypass;
    }
    
    public ToggleButton(final String name) {
        this.toggleAnimation = new DecelerateAnimation(250, 1.0);
        this.name = name;
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
    
    public void setX(final float x) {
        this.x = x;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
    
    public void setAlpha(final float alpha) {
        this.alpha = alpha;
    }
}
