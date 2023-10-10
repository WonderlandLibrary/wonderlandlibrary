// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public enum RecursiveDeleteOption
{
    ALLOW_INSECURE;
    
    private static /* synthetic */ RecursiveDeleteOption[] $values() {
        return new RecursiveDeleteOption[] { RecursiveDeleteOption.ALLOW_INSECURE };
    }
    
    static {
        $VALUES = $values();
    }
}
