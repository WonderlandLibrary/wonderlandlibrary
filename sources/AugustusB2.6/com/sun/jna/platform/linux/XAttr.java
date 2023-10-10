// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.linux;

import com.sun.jna.IntegerType;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Library;

public interface XAttr extends Library
{
    public static final XAttr INSTANCE = Native.load(XAttr.class);
    public static final int XATTR_CREATE = 1;
    public static final int XATTR_REPLACE = 2;
    public static final int EPERM = 1;
    public static final int E2BIG = 7;
    public static final int EEXIST = 17;
    public static final int ENOSPC = 28;
    public static final int ERANGE = 34;
    public static final int ENODATA = 61;
    public static final int ENOATTR = 61;
    public static final int ENOTSUP = 95;
    public static final int EDQUOT = 122;
    
    int setxattr(final String p0, final String p1, final Pointer p2, final size_t p3, final int p4);
    
    int setxattr(final String p0, final String p1, final byte[] p2, final size_t p3, final int p4);
    
    int lsetxattr(final String p0, final String p1, final Pointer p2, final size_t p3, final int p4);
    
    int lsetxattr(final String p0, final String p1, final byte[] p2, final size_t p3, final int p4);
    
    int fsetxattr(final int p0, final String p1, final Pointer p2, final size_t p3, final int p4);
    
    int fsetxattr(final int p0, final String p1, final byte[] p2, final size_t p3, final int p4);
    
    ssize_t getxattr(final String p0, final String p1, final Pointer p2, final size_t p3);
    
    ssize_t getxattr(final String p0, final String p1, final byte[] p2, final size_t p3);
    
    ssize_t lgetxattr(final String p0, final String p1, final Pointer p2, final size_t p3);
    
    ssize_t lgetxattr(final String p0, final String p1, final byte[] p2, final size_t p3);
    
    ssize_t fgetxattr(final int p0, final String p1, final Pointer p2, final size_t p3);
    
    ssize_t fgetxattr(final int p0, final String p1, final byte[] p2, final size_t p3);
    
    ssize_t listxattr(final String p0, final Pointer p1, final size_t p2);
    
    ssize_t listxattr(final String p0, final byte[] p1, final size_t p2);
    
    ssize_t llistxattr(final String p0, final Pointer p1, final size_t p2);
    
    ssize_t llistxattr(final String p0, final byte[] p1, final size_t p2);
    
    ssize_t flistxattr(final int p0, final Pointer p1, final size_t p2);
    
    ssize_t flistxattr(final int p0, final byte[] p1, final size_t p2);
    
    int removexattr(final String p0, final String p1);
    
    int lremovexattr(final String p0, final String p1);
    
    int fremovexattr(final int p0, final String p1);
    
    public static class size_t extends IntegerType
    {
        public static final size_t ZERO;
        private static final long serialVersionUID = 1L;
        
        public size_t() {
            this(0L);
        }
        
        public size_t(final long value) {
            super(Native.SIZE_T_SIZE, value, true);
        }
        
        static {
            ZERO = new size_t();
        }
    }
    
    public static class ssize_t extends IntegerType
    {
        public static final ssize_t ZERO;
        private static final long serialVersionUID = 1L;
        
        public ssize_t() {
            this(0L);
        }
        
        public ssize_t(final long value) {
            super(Native.SIZE_T_SIZE, value, false);
        }
        
        static {
            ZERO = new ssize_t();
        }
    }
}
