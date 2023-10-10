// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.Variant;

public interface IEnumVariant extends IUnknown
{
    IEnumVariant Clone();
    
    Variant.VARIANT[] Next(final int p0);
    
    void Reset();
    
    void Skip(final int p0);
}
