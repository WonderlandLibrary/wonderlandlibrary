// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
interface PatternCompiler
{
    CommonPattern compile(final String p0);
    
    boolean isPcreLike();
}
