// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public enum RemovalCause
{
    EXPLICIT(0) {
        @Override
        boolean wasEvicted() {
            return false;
        }
    }, 
    REPLACED(1) {
        @Override
        boolean wasEvicted() {
            return false;
        }
    }, 
    COLLECTED(2) {
        @Override
        boolean wasEvicted() {
            return true;
        }
    }, 
    EXPIRED(3) {
        @Override
        boolean wasEvicted() {
            return true;
        }
    }, 
    SIZE(4) {
        @Override
        boolean wasEvicted() {
            return true;
        }
    };
    
    abstract boolean wasEvicted();
    
    private static /* synthetic */ RemovalCause[] $values() {
        return new RemovalCause[] { RemovalCause.EXPLICIT, RemovalCause.REPLACED, RemovalCause.COLLECTED, RemovalCause.EXPIRED, RemovalCause.SIZE };
    }
    
    static {
        $VALUES = $values();
    }
}
