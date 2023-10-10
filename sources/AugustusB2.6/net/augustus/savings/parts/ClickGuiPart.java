// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.savings.parts;

import net.augustus.modules.Categorys;

public class ClickGuiPart
{
    private int x;
    private int y;
    private boolean open;
    private Categorys categorys;
    
    public ClickGuiPart(final int x, final int y, final boolean open, final Categorys categorys) {
        this.x = x;
        this.y = y;
        this.open = open;
        this.categorys = categorys;
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public void setOpen(final boolean open) {
        this.open = open;
    }
    
    public Categorys getCategory() {
        return this.categorys;
    }
    
    public void setCategory(final Categorys categorys) {
        this.categorys = categorys;
    }
}
