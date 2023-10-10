// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.WinNT;

public class COMException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    private final WinNT.HRESULT hresult;
    
    public COMException() {
        this("", (Throwable)null);
    }
    
    public COMException(final String message) {
        this(message, (Throwable)null);
    }
    
    public COMException(final Throwable cause) {
        this(null, cause);
    }
    
    public COMException(final String message, final Throwable cause) {
        super(message, cause);
        this.hresult = null;
    }
    
    public COMException(final String message, final WinNT.HRESULT hresult) {
        super(message);
        this.hresult = hresult;
    }
    
    public WinNT.HRESULT getHresult() {
        return this.hresult;
    }
    
    public boolean matchesErrorCode(final int errorCode) {
        return this.hresult != null && this.hresult.intValue() == errorCode;
    }
}
