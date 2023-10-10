// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

public abstract class WinioctlUtil
{
    public static final int FSCTL_GET_COMPRESSION;
    public static final int FSCTL_SET_COMPRESSION;
    public static final int FSCTL_SET_REPARSE_POINT;
    public static final int FSCTL_GET_REPARSE_POINT;
    public static final int FSCTL_DELETE_REPARSE_POINT;
    
    public static int CTL_CODE(final int DeviceType, final int Function, final int Method, final int Access) {
        return DeviceType << 16 | Access << 14 | Function << 2 | Method;
    }
    
    static {
        FSCTL_GET_COMPRESSION = CTL_CODE(9, 15, 0, 0);
        FSCTL_SET_COMPRESSION = CTL_CODE(9, 16, 0, 3);
        FSCTL_SET_REPARSE_POINT = CTL_CODE(9, 41, 0, 0);
        FSCTL_GET_REPARSE_POINT = CTL_CODE(9, 42, 0, 0);
        FSCTL_DELETE_REPARSE_POINT = CTL_CODE(9, 43, 0, 0);
    }
}
