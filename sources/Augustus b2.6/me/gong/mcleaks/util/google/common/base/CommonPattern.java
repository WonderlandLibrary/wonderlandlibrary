// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.base;

import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class CommonPattern
{
    abstract CommonMatcher matcher(final CharSequence p0);
    
    abstract String pattern();
    
    abstract int flags();
    
    @Override
    public abstract String toString();
    
    @Override
    public abstract int hashCode();
    
    @Override
    public abstract boolean equals(final Object p0);
}
