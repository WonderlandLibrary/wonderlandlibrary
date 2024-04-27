package me.utils;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface QQUtils$User32
extends StdCallLibrary {
    public static final QQUtils$User32 INSTANCE = Native.loadLibrary("user32", QQUtils$User32.class);

    public boolean EnumWindows(WNDENUMPROC var1, Pointer var2);

    public int GetWindowTextA(Pointer var1, byte[] var2, int var3);

    public static interface WNDENUMPROC
    extends StdCallLibrary.StdCallCallback {
        public boolean callback(Pointer var1, Pointer var2);
    }
}
