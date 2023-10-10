// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.platform.win32.COM.IDispatchCallback;

public abstract class AbstractComEventCallbackListener implements IComEventCallbackListener
{
    IDispatchCallback dispatchCallback;
    
    public AbstractComEventCallbackListener() {
        this.dispatchCallback = null;
    }
    
    @Override
    public void setDispatchCallbackListener(final IDispatchCallback dispatchCallback) {
        this.dispatchCallback = dispatchCallback;
    }
}
