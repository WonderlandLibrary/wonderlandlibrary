// 
// Decompiled by Procyon v0.5.36
// 

package com.google.thirdparty.publicsuffix;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public enum PublicSuffixType
{
    PRIVATE(':', ','), 
    REGISTRY('!', '?');
    
    private final char innerNodeCode;
    private final char leafNodeCode;
    
    private PublicSuffixType(final char innerNodeCode, final char leafNodeCode) {
        this.innerNodeCode = innerNodeCode;
        this.leafNodeCode = leafNodeCode;
    }
    
    char getLeafNodeCode() {
        return this.leafNodeCode;
    }
    
    char getInnerNodeCode() {
        return this.innerNodeCode;
    }
    
    static PublicSuffixType fromCode(final char code) {
        for (final PublicSuffixType value : values()) {
            if (value.getInnerNodeCode() == code || value.getLeafNodeCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException(new StringBuilder(38).append("No enum corresponding to given code: ").append(code).toString());
    }
    
    private static /* synthetic */ PublicSuffixType[] $values() {
        return new PublicSuffixType[] { PublicSuffixType.PRIVATE, PublicSuffixType.REGISTRY };
    }
    
    static {
        $VALUES = $values();
    }
}
