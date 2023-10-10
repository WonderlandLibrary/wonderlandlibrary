// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.util;

import java.util.List;

public interface IRunningObjectTable
{
    Iterable<IDispatch> enumRunning();
    
     <T> List<T> getActiveObjectsByInterface(final Class<T> p0);
}
