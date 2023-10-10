// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public enum FileWriteMode
{
    APPEND;
    
    private static /* synthetic */ FileWriteMode[] $values() {
        return new FileWriteMode[] { FileWriteMode.APPEND };
    }
    
    static {
        $VALUES = $values();
    }
}
