// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.locks.LockSupport;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
final class OverflowAvoidingLockSupport
{
    static final long MAX_NANOSECONDS_THRESHOLD = 2147483647999999999L;
    
    private OverflowAvoidingLockSupport() {
    }
    
    static void parkNanos(@CheckForNull final Object blocker, final long nanos) {
        LockSupport.parkNanos(blocker, Math.min(nanos, 2147483647999999999L));
    }
}
