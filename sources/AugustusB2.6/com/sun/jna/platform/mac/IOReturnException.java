// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.mac;

public class IOReturnException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    private int ioReturn;
    
    public IOReturnException(final int kr) {
        this(kr, formatMessage(kr));
    }
    
    protected IOReturnException(final int kr, final String msg) {
        super(msg);
        this.ioReturn = kr;
    }
    
    public int getIOReturnCode() {
        return this.ioReturn;
    }
    
    public static int getSystem(final int kr) {
        return kr >> 26 & 0x3F;
    }
    
    public static int getSubSystem(final int kr) {
        return kr >> 14 & 0xFFF;
    }
    
    public static int getCode(final int kr) {
        return kr & 0x3FFF;
    }
    
    private static String formatMessage(final int kr) {
        return "IOReturn error code: " + kr + " (system=" + getSystem(kr) + ", subSystem=" + getSubSystem(kr) + ", code=" + getCode(kr) + ")";
    }
}
