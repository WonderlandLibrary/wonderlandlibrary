// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.mac;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Library;

interface XAttr extends Library
{
    public static final XAttr INSTANCE = Native.load(null, XAttr.class);
    public static final int XATTR_NOFOLLOW = 1;
    public static final int XATTR_CREATE = 2;
    public static final int XATTR_REPLACE = 4;
    public static final int XATTR_NOSECURITY = 8;
    public static final int XATTR_NODEFAULT = 16;
    public static final int XATTR_SHOWCOMPRESSION = 32;
    public static final int XATTR_MAXNAMELEN = 127;
    public static final String XATTR_FINDERINFO_NAME = "com.apple.FinderInfo";
    public static final String XATTR_RESOURCEFORK_NAME = "com.apple.ResourceFork";
    
    long getxattr(final String p0, final String p1, final Pointer p2, final long p3, final int p4, final int p5);
    
    int setxattr(final String p0, final String p1, final Pointer p2, final long p3, final int p4, final int p5);
    
    int removexattr(final String p0, final String p1, final int p2);
    
    long listxattr(final String p0, final Pointer p1, final long p2, final int p3);
}
