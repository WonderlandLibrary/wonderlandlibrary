/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Native
 *  com.sun.jna.Pointer
 *  com.sun.jna.win32.StdCallLibrary
 *  com.sun.jna.win32.StdCallLibrary$StdCallCallback
 */
package net.ccbluex.liquidbounce.api.util;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface IWrappedUser
extends StdCallLibrary {
    public static final IWrappedUser INSTANCE = (IWrappedUser)Native.loadLibrary((String)"user32", IWrappedUser.class);

    public int GetWindowTextA(Pointer var1, byte[] var2, int var3);

    public boolean EnumWindows(WNDENUMPROC var1, Pointer var2);

    public static interface WNDENUMPROC
    extends StdCallLibrary.StdCallCallback {
        public boolean callback(Pointer var1, Pointer var2);
    }
}

