// 
// Decompiled by Procyon v0.6.0
// 

package com.google.thirdparty.publicsuffix;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
enum PublicSuffixType
{
    PRIVATE(':', ','), 
    ICANN('!', '?');
    
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
    
    static PublicSuffixType fromIsPrivate(final boolean isPrivate) {
        return isPrivate ? PublicSuffixType.PRIVATE : PublicSuffixType.ICANN;
    }
}
