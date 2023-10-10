// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.cache;

import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
public enum RemovalCause
{
    EXPLICIT {
        @Override
        boolean wasEvicted() {
            return false;
        }
    }, 
    REPLACED {
        @Override
        boolean wasEvicted() {
            return false;
        }
    }, 
    COLLECTED {
        @Override
        boolean wasEvicted() {
            return true;
        }
    }, 
    EXPIRED {
        @Override
        boolean wasEvicted() {
            return true;
        }
    }, 
    SIZE {
        @Override
        boolean wasEvicted() {
            return true;
        }
    };
    
    abstract boolean wasEvicted();
}
