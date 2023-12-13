// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.planeGui.comp;

import net.augustus.settings.Setting;
import net.augustus.modules.Module;
import net.augustus.planeGui.Clickgui;

public class Comp
{
    public double x;
    public double y;
    public double x2;
    public double y2;
    public Clickgui parent;
    public Module module;
    public Setting setting;
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public void drawScreen(final int mouseX, final int mouseY) {
    }
    
    public boolean isInside(final int mouseX, final int mouseY, final double x, final double y, final double x2, final double y2) {
        return mouseX > x && mouseX < x2 && mouseY > y && mouseY < y2;
    }
    
    public void keyTyped(final char typedChar, final int keyCode) {
    }
}
