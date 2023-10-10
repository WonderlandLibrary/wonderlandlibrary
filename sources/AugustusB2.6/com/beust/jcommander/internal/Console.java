// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.internal;

public interface Console
{
    void print(final String p0);
    
    void println(final String p0);
    
    char[] readPassword(final boolean p0);
}
