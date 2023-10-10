// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class CommonMatcher
{
    public abstract boolean matches();
    
    public abstract boolean find();
    
    public abstract boolean find(final int p0);
    
    public abstract String replaceAll(final String p0);
    
    public abstract int end();
    
    public abstract int start();
}
