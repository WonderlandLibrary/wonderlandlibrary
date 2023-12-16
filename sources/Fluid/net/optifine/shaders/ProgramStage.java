// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.shaders;

public enum ProgramStage
{
    NONE("NONE", 0, ""), 
    SHADOW("SHADOW", 1, "shadow"), 
    GBUFFERS("GBUFFERS", 2, "gbuffers"), 
    DEFERRED("DEFERRED", 3, "deferred"), 
    COMPOSITE("COMPOSITE", 4, "composite");
    
    private String name;
    
    private ProgramStage(final String s, final int n, final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
