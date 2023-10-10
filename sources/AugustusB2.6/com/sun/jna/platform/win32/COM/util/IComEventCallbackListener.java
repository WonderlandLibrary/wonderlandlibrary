// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.platform.win32.COM.IDispatchCallback;

public interface IComEventCallbackListener
{
    void setDispatchCallbackListener(final IDispatchCallback p0);
    
    void errorReceivingCallbackEvent(final String p0, final Exception p1);
}
