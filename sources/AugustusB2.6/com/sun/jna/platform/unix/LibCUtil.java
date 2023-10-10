// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.unix;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Function;
import com.sun.jna.NativeLibrary;

public class LibCUtil
{
    private static final NativeLibrary LIBC;
    private static Function mmap;
    private static boolean mmap64;
    private static Function ftruncate;
    private static boolean ftruncate64;
    
    private LibCUtil() {
    }
    
    public static Pointer mmap(final Pointer addr, final long length, final int prot, final int flags, final int fd, final long offset) {
        final Object[] params = new Object[6];
        params[0] = addr;
        if (Native.SIZE_T_SIZE == 4) {
            require32Bit(length, "length");
            params[1] = (int)length;
        }
        else {
            params[1] = length;
        }
        params[2] = prot;
        params[3] = flags;
        params[4] = fd;
        if (LibCUtil.mmap64 || Native.LONG_SIZE > 4) {
            params[5] = offset;
        }
        else {
            require32Bit(offset, "offset");
            params[5] = (int)offset;
        }
        return LibCUtil.mmap.invokePointer(params);
    }
    
    public static int ftruncate(final int fd, final long length) {
        final Object[] params = { fd, null };
        if (LibCUtil.ftruncate64 || Native.LONG_SIZE > 4) {
            params[1] = length;
        }
        else {
            require32Bit(length, "length");
            params[1] = (int)length;
        }
        return LibCUtil.ftruncate.invokeInt(params);
    }
    
    public static void require32Bit(final long val, final String value) {
        if (val > 2147483647L) {
            throw new IllegalArgumentException(value + " exceeds 32bit");
        }
    }
    
    static {
        LIBC = NativeLibrary.getInstance("c");
        LibCUtil.mmap = null;
        LibCUtil.mmap64 = false;
        LibCUtil.ftruncate = null;
        LibCUtil.ftruncate64 = false;
        try {
            LibCUtil.mmap = LibCUtil.LIBC.getFunction("mmap64", 64);
            LibCUtil.mmap64 = true;
        }
        catch (UnsatisfiedLinkError ex) {
            LibCUtil.mmap = LibCUtil.LIBC.getFunction("mmap", 64);
        }
        try {
            LibCUtil.ftruncate = LibCUtil.LIBC.getFunction("ftruncate64", 64);
            LibCUtil.ftruncate64 = true;
        }
        catch (UnsatisfiedLinkError ex) {
            LibCUtil.ftruncate = LibCUtil.LIBC.getFunction("ftruncate", 64);
        }
    }
}
