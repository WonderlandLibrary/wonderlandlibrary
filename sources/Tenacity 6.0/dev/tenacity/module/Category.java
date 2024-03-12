// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module;

import dev.tenacity.utils.objects.Drag;
import dev.tenacity.utils.objects.Scroll;

public enum Category
{
    COMBAT("Combat", "c"), 
    MOVEMENT("Movement", "f"), 
    RENDER("Render", "d"), 
    PLAYER("Player", "e"), 
    EXPLOIT("Exploit", "a"), 
    MISC("Misc", "b"), 
    SCRIPTS("Scripts", "g");
    
    public final String name;
    public final String icon;
    public final int posX;
    public final boolean expanded;
    private final Scroll scroll;
    private final Drag drag;
    public int posY;
    
    private Category(final String name, final String icon) {
        this.scroll = new Scroll();
        this.posY = 20;
        this.name = name;
        this.icon = icon;
        this.posX = 20 + Module.categoryCount * 120;
        this.drag = new Drag((float)this.posX, (float)this.posY);
        this.expanded = true;
        ++Module.categoryCount;
    }
    
    public Scroll getScroll() {
        return this.scroll;
    }
    
    public Drag getDrag() {
        return this.drag;
    }
}
