// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules;

public enum Category
{
    Combat("Combat", 0, "Combat"), 
    Movement("Movement", 1, "Movement"), 
    Player("Player", 2, "Player"), 
    Render("Render", 3, "Render"), 
    Misc("Misc", 4, "Misc"), 
    Exploit("Exploit", 5, "Exploit");
    
    public String name;
    public int moduleIndex;
    
    private Category(final String name2, final int ordinal, final String name) {
        this.name = name;
    }
}
