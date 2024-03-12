// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.panels;

import dev.tenacity.module.impl.render.HUDMod;
import dev.tenacity.utils.render.ColorUtil;
import java.awt.Color;
import dev.tenacity.ui.Screen;

public abstract class Panel implements Screen
{
    private float x;
    private float y;
    private float width;
    private float height;
    private float alpha;
    
    public Color getTextColor() {
        return ColorUtil.applyOpacity(Color.WHITE, this.alpha);
    }
    
    public Color getAccentColor() {
        return ColorUtil.applyOpacity(HUDMod.getClientColors().getFirst(), this.alpha);
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
}
