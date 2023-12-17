// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules;

public enum Type
{
    COMBAT("Combat"), 
    MOVEMENT("Movement"), 
    RENDER("Render"), 
    PLAYER("Player"), 
    MISC("Misc"), 
    HUD("Hud");
    
    private final String displayName;
    
    private Type(final String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
}
