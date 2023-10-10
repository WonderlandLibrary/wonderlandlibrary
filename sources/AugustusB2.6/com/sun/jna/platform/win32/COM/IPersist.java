// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.Guid;

public interface IPersist extends IUnknown
{
    Guid.CLSID GetClassID();
}
