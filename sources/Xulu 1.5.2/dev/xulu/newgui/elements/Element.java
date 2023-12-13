// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.newgui.elements;

import java.io.IOException;
import dev.xulu.newgui.util.FontUtil;
import dev.xulu.settings.Value;
import dev.xulu.newgui.NewGUI;

public class Element
{
    public NewGUI clickgui;
    public ModuleButton parent;
    public Value set;
    public double offset;
    public double x;
    public double y;
    public double width;
    public double height;
    public String setstrg;
    public boolean comboextended;
    
    public void setup() {
        this.clickgui = this.parent.parent.clickgui;
    }
    
    public void update() {
        this.x = this.parent.x - 2.0;
        this.y = this.parent.y + this.offset;
        this.width = this.parent.width + 4.0;
        this.height = this.parent.height + 1.0;
        final String sname = this.set.getName();
        if (this.set.isToggle()) {
            this.setstrg = sname.substring(0, 1).toUpperCase() + sname.substring(1, sname.length());
            final double textx = this.x + this.width - FontUtil.getStringWidth(this.setstrg);
            if (textx < this.x + 13.0) {
                this.width += this.x + 13.0 - textx + 1.0;
            }
        }
        else if (this.set.isMode()) {
            this.setstrg = sname.substring(0, 1).toUpperCase() + sname.substring(1, sname.length());
        }
        else if (this.set.isNumber()) {
            this.setstrg = sname.substring(0, 1).toUpperCase() + sname.substring(1, sname.length());
            String displaymax;
            if (this.set.getValue() instanceof Integer) {
                displaymax = "" + Math.round(this.set.getMax() * 100.0) / 100.0;
            }
            else if (this.set.getValue() instanceof Short) {
                displaymax = "" + Math.round(this.set.getMax() * 100.0) / 100.0;
            }
            else if (this.set.getValue() instanceof Long) {
                displaymax = "" + Math.round(this.set.getMax() * 100.0) / 100.0;
            }
            else if (this.set.getValue() instanceof Float) {
                displaymax = "" + Math.round(this.set.getMax() * 100.0) / 100.0;
            }
            else if (this.set.getValue() instanceof Double) {
                displaymax = "" + Math.round(this.set.getMax() * 100.0) / 100.0;
            }
            else {
                displaymax = "";
            }
            final double textx2 = this.x + this.width - FontUtil.getStringWidth(this.setstrg) - FontUtil.getStringWidth(displaymax) - 4.0;
            if (textx2 < this.x) {
                this.width += this.x - textx2 + 1.0;
            }
        }
        else if (this.set.isBind()) {
            this.setstrg = sname.substring(0, 1).toUpperCase() + sname.substring(1, sname.length());
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
    }
    
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        return this.isHovered(mouseX, mouseY);
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public boolean keyTyped(final char typedChar, final int keyCode) throws IOException {
        return false;
    }
    
    public boolean isHovered(final int mouseX, final int mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
    }
}
