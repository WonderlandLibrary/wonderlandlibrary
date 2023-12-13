// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite;

import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class Element extends Module
{
    Frame frame;
    protected double x;
    protected double y;
    protected double width;
    protected double height;
    
    public Element(final String name) {
        super(name, "NONE", 0, Category.HUD, false);
    }
    
    public void registerFrame() {
        this.frame = HUD.getframeByName(this.getName());
        this.width = this.frame.width;
        this.height = this.frame.height;
    }
    
    @Override
    public void onUpdate() {
        if (this.frame != null) {
            if (this.frame.width != this.width) {
                this.frame.width = this.width;
            }
            if (this.frame.height != this.height) {
                this.frame.height = this.height;
            }
        }
        super.onUpdate();
    }
    
    public void onMiddleClick() {
    }
    
    public Frame getFrame() {
        return this.frame;
    }
    
    @Override
    public void onEnable() {
        if (this.frame != null && !this.frame.pinned) {
            this.frame.pinned = true;
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        if (this.frame != null && this.frame.pinned) {
            this.frame.pinned = false;
        }
        super.onDisable();
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
}
