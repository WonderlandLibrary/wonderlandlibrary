// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.material.button;

import net.augustus.material.Main;
import net.augustus.material.Tab;
import net.augustus.utils.skid.tomorrow.AnimationUtils;
import net.augustus.settings.Setting;

public class Button
{
    public float x;
    public float y;
    public Setting v;
    public float animation;
    public Runnable event;
    public boolean drag;
    public AnimationUtils animationUtils;
    public Tab tab;
    
    public Button(final float x, final float y, final Setting v, final Tab moduleTab) {
        this.animationUtils = new AnimationUtils();
        this.x = x;
        this.y = y;
        this.v = v;
        this.tab = moduleTab;
    }
    
    public void drawButton(final float mouseX, final float mouseY) {
    }
    
    public void draw(final float mouseX, final float mouseY) {
        this.drawButton(mouseX, mouseY);
    }
    
    public void mouseClicked(final float mouseX, final float mouseY) {
    }
    
    public void mouseClick(final float mouseX, final float mouseY) {
        if (Main.currentTab != this.tab) {
            return;
        }
        this.mouseClicked(mouseX, mouseY);
    }
}
