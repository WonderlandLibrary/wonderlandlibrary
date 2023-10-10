// 
// Decompiled by Procyon v0.5.36
// 

package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.error.Mark;

public abstract class Token
{
    private final Mark startMark;
    private final Mark endMark;
    
    public Token(final Mark startMark, final Mark endMark) {
        if (startMark == null || endMark == null) {
            throw new YAMLException("Token requires marks.");
        }
        this.startMark = startMark;
        this.endMark = endMark;
    }
    
    public Mark getStartMark() {
        return this.startMark;
    }
    
    public Mark getEndMark() {
        return this.endMark;
    }
    
    public abstract ID getTokenId();
    
    public enum ID
    {
        Alias("<alias>"), 
        Anchor("<anchor>"), 
        BlockEnd("<block end>"), 
        BlockEntry("-"), 
        BlockMappingStart("<block mapping start>"), 
        BlockSequenceStart("<block sequence start>"), 
        Directive("<directive>"), 
        DocumentEnd("<document end>"), 
        DocumentStart("<document start>"), 
        FlowEntry(","), 
        FlowMappingEnd("}"), 
        FlowMappingStart("{"), 
        FlowSequenceEnd("]"), 
        FlowSequenceStart("["), 
        Key("?"), 
        Scalar("<scalar>"), 
        StreamEnd("<stream end>"), 
        StreamStart("<stream start>"), 
        Tag("<tag>"), 
        Value(":"), 
        Whitespace("<whitespace>"), 
        Comment("#"), 
        Error("<error>");
        
        private final String description;
        
        private ID(final String s) {
            this.description = s;
        }
        
        @Override
        public String toString() {
            return this.description;
        }
    }
}
