package me.utils;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;

public interface WindowUtils$User32
extends StdCallLibrary {
    public static final WindowUtils$User32 INSTANCE = Native.loadLibrary("user32", WindowUtils$User32.class);

    public boolean EnumWindows(WinUser.WNDENUMPROC var1, Pointer var2);

    public int GetWindowTextA(WinDef.HWND var1, byte[] var2, int var3);
}
