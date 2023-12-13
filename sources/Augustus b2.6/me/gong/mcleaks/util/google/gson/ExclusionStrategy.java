// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.gson;

public interface ExclusionStrategy
{
    boolean shouldSkipField(final FieldAttributes p0);
    
    boolean shouldSkipClass(final Class<?> p0);
}
