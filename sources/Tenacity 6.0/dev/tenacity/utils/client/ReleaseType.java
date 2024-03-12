// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.client;

public enum ReleaseType
{
    PUBLIC("Public"), 
    BETA("Beta"), 
    DEV("Developer");
    
    private final String name;
    
    public String getName() {
        return this.name;
    }
    
    private ReleaseType(final String name) {
        this.name = name;
    }
}
