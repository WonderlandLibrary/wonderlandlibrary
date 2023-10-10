// 
// Decompiled by Procyon v0.5.36
// 

package org.yaml.snakeyaml.emitter;

public final class ScalarAnalysis
{
    private String scalar;
    private boolean empty;
    private boolean multiline;
    private boolean allowFlowPlain;
    private boolean allowBlockPlain;
    private boolean allowSingleQuoted;
    private boolean allowBlock;
    
    public ScalarAnalysis(final String scalar, final boolean empty, final boolean multiline, final boolean allowFlowPlain, final boolean allowBlockPlain, final boolean allowSingleQuoted, final boolean allowBlock) {
        this.scalar = scalar;
        this.empty = empty;
        this.multiline = multiline;
        this.allowFlowPlain = allowFlowPlain;
        this.allowBlockPlain = allowBlockPlain;
        this.allowSingleQuoted = allowSingleQuoted;
        this.allowBlock = allowBlock;
    }
    
    public String getScalar() {
        return this.scalar;
    }
    
    public boolean isEmpty() {
        return this.empty;
    }
    
    public boolean isMultiline() {
        return this.multiline;
    }
    
    public boolean isAllowFlowPlain() {
        return this.allowFlowPlain;
    }
    
    public boolean isAllowBlockPlain() {
        return this.allowBlockPlain;
    }
    
    public boolean isAllowSingleQuoted() {
        return this.allowSingleQuoted;
    }
    
    public boolean isAllowBlock() {
        return this.allowBlock;
    }
}
