// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;

@Deprecated
@ElementTypesAreNonnullByDefault
@GwtCompatible
public class ComputationException extends RuntimeException
{
    private static final long serialVersionUID = 0L;
    
    public ComputationException(@CheckForNull final Throwable cause) {
        super(cause);
    }
}
