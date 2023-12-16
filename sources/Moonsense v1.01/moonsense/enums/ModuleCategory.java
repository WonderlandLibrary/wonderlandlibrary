// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.enums;

public enum ModuleCategory
{
    ALL("ALL", 0, "All"), 
    NEW("NEW", 1, "New"), 
    HUD("HUD", 2, "Hud"), 
    SERVER("SERVER", 3, "Server"), 
    MECHANIC("MECHANIC", 4, "Mechanic");
    
    public final String tabName;
    
    private ModuleCategory(final String name, final int ordinal, final String tabName) {
        this.tabName = tabName;
    }
}
