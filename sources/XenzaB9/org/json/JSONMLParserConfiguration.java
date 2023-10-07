// 
// Decompiled by Procyon v0.6.0
// 

package org.json;

public class JSONMLParserConfiguration
{
    public static final int UNDEFINED_MAXIMUM_NESTING_DEPTH = -1;
    public static final int DEFAULT_MAXIMUM_NESTING_DEPTH = 512;
    public static final JSONMLParserConfiguration ORIGINAL;
    public static final JSONMLParserConfiguration KEEP_STRINGS;
    private boolean keepStrings;
    private int maxNestingDepth;
    
    public JSONMLParserConfiguration() {
        this.maxNestingDepth = 512;
        this.keepStrings = false;
    }
    
    private JSONMLParserConfiguration(final boolean keepStrings, final int maxNestingDepth) {
        this.maxNestingDepth = 512;
        this.keepStrings = keepStrings;
        this.maxNestingDepth = maxNestingDepth;
    }
    
    @Override
    protected JSONMLParserConfiguration clone() {
        return new JSONMLParserConfiguration(this.keepStrings, this.maxNestingDepth);
    }
    
    public boolean isKeepStrings() {
        return this.keepStrings;
    }
    
    public JSONMLParserConfiguration withKeepStrings(final boolean newVal) {
        final JSONMLParserConfiguration newConfig = this.clone();
        newConfig.keepStrings = newVal;
        return newConfig;
    }
    
    public int getMaxNestingDepth() {
        return this.maxNestingDepth;
    }
    
    public JSONMLParserConfiguration withMaxNestingDepth(final int maxNestingDepth) {
        final JSONMLParserConfiguration newConfig = this.clone();
        if (maxNestingDepth > -1) {
            newConfig.maxNestingDepth = maxNestingDepth;
        }
        else {
            newConfig.maxNestingDepth = -1;
        }
        return newConfig;
    }
    
    static {
        ORIGINAL = new JSONMLParserConfiguration();
        KEEP_STRINGS = new JSONMLParserConfiguration().withKeepStrings(true);
    }
}
