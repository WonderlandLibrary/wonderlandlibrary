// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.enums;

public enum DevelopmentStage
{
    DEVELOPMENT("DEVELOPMENT", 0, "dev"), 
    BETA("BETA", 1, "beta"), 
    ALPHA("ALPHA", 2, "alpha"), 
    PRODUCTION("PRODUCTION", 3, "");
    
    public final String stage;
    
    private DevelopmentStage(final String name, final int ordinal, final String arg0) {
        this.stage = arg0;
    }
}
