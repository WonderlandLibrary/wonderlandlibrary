// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft;

public class VillagerData
{
    private final int type;
    private final int profession;
    private final int level;
    
    public VillagerData(final int type, final int profession, final int level) {
        this.type = type;
        this.profession = profession;
        this.level = level;
    }
    
    public int type() {
        return this.type;
    }
    
    public int profession() {
        return this.profession;
    }
    
    public int level() {
        return this.level;
    }
    
    @Deprecated
    public int getType() {
        return this.type;
    }
    
    @Deprecated
    public int getProfession() {
        return this.profession;
    }
    
    @Deprecated
    public int getLevel() {
        return this.level;
    }
}
