// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.advancements;

public enum AdvancementState
{
    OBTAINED(0), 
    UNOBTAINED(1);
    
    private final int id;
    
    private AdvancementState(final int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
}
