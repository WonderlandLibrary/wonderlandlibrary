// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.sun.jna.LastErrorException;

public class Win32Exception extends LastErrorException
{
    private static final long serialVersionUID = 1L;
    private WinNT.HRESULT _hr;
    private static Method addSuppressedMethod;
    
    public WinNT.HRESULT getHR() {
        return this._hr;
    }
    
    public Win32Exception(final int code) {
        this(code, W32Errors.HRESULT_FROM_WIN32(code));
    }
    
    public Win32Exception(final WinNT.HRESULT hr) {
        this(W32Errors.HRESULT_CODE(hr.intValue()), hr);
    }
    
    protected Win32Exception(final int code, final WinNT.HRESULT hr) {
        this(code, hr, Kernel32Util.formatMessage(hr));
    }
    
    protected Win32Exception(final int code, final WinNT.HRESULT hr, final String msg) {
        super(code, msg);
        this._hr = hr;
    }
    
    void addSuppressedReflected(final Throwable exception) {
        if (Win32Exception.addSuppressedMethod == null) {
            return;
        }
        try {
            Win32Exception.addSuppressedMethod.invoke(this, exception);
        }
        catch (IllegalAccessException ex) {
            throw new RuntimeException("Failed to call addSuppressedMethod", ex);
        }
        catch (IllegalArgumentException ex2) {
            throw new RuntimeException("Failed to call addSuppressedMethod", ex2);
        }
        catch (InvocationTargetException ex3) {
            throw new RuntimeException("Failed to call addSuppressedMethod", ex3);
        }
    }
    
    static {
        Win32Exception.addSuppressedMethod = null;
        try {
            Win32Exception.addSuppressedMethod = Throwable.class.getMethod("addSuppressed", Throwable.class);
        }
        catch (NoSuchMethodException ex2) {}
        catch (SecurityException ex) {
            Logger.getLogger(Win32Exception.class.getName()).log(Level.SEVERE, "Failed to initialize 'addSuppressed' method", ex);
        }
    }
}
