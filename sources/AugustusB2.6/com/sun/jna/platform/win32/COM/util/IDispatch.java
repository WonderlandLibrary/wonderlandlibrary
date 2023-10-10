// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.platform.win32.OaIdl;

public interface IDispatch extends IUnknown
{
     <T> void setProperty(final String p0, final T p1);
    
     <T> T getProperty(final Class<T> p0, final String p1, final Object... p2);
    
     <T> T invokeMethod(final Class<T> p0, final String p1, final Object... p2);
    
     <T> void setProperty(final OaIdl.DISPID p0, final T p1);
    
     <T> T getProperty(final Class<T> p0, final OaIdl.DISPID p1, final Object... p2);
    
     <T> T invokeMethod(final Class<T> p0, final OaIdl.DISPID p1, final Object... p2);
}
