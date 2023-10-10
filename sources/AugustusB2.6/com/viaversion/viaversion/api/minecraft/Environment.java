// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft;

public enum Environment
{
    NORMAL(0), 
    NETHER(-1), 
    END(1);
    
    private final int id;
    
    private Environment(final int id) {
        this.id = id;
    }
    
    public int id() {
        return this.id;
    }
    
    @Deprecated
    public int getId() {
        return this.id;
    }
    
    public static Environment getEnvironmentById(final int id) {
        switch (id) {
            default: {
                return Environment.NETHER;
            }
            case 0: {
                return Environment.NORMAL;
            }
            case 1: {
                return Environment.END;
            }
        }
    }
}
