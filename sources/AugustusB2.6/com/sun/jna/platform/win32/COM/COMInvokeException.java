// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.WinNT;

public class COMInvokeException extends COMException
{
    private static final long serialVersionUID = 1L;
    private final Integer wCode;
    private final String source;
    private final String description;
    private final String helpFile;
    private final Integer helpContext;
    private final Integer scode;
    private final Integer errorArg;
    
    public COMInvokeException() {
        this("", (Throwable)null);
    }
    
    public COMInvokeException(final String message) {
        this(message, (Throwable)null);
    }
    
    public COMInvokeException(final Throwable cause) {
        this(null, cause);
    }
    
    public COMInvokeException(final String message, final Throwable cause) {
        super(message, cause);
        this.description = null;
        this.errorArg = null;
        this.helpContext = null;
        this.helpFile = null;
        this.scode = null;
        this.source = null;
        this.wCode = null;
    }
    
    public COMInvokeException(final String message, final WinNT.HRESULT hresult, final Integer errorArg, final String description, final Integer helpContext, final String helpFile, final Integer scode, final String source, final Integer wCode) {
        super(formatMessage(hresult, message, errorArg), hresult);
        this.description = description;
        this.errorArg = errorArg;
        this.helpContext = helpContext;
        this.helpFile = helpFile;
        this.scode = scode;
        this.source = source;
        this.wCode = wCode;
    }
    
    public Integer getErrorArg() {
        return this.errorArg;
    }
    
    public Integer getWCode() {
        return this.wCode;
    }
    
    public String getSource() {
        return this.source;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getHelpFile() {
        return this.helpFile;
    }
    
    public Integer getHelpContext() {
        return this.helpContext;
    }
    
    public Integer getScode() {
        return this.scode;
    }
    
    private static String formatMessage(final WinNT.HRESULT hresult, final String message, final Integer errArg) {
        if (hresult.intValue() == -2147352571 || hresult.intValue() == -2147352572) {
            return message + " (puArgErr=" + errArg + ")";
        }
        return message;
    }
}
