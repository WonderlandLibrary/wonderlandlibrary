package me.utils;

import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface QQUtils$User32$WNDENUMPROC
extends StdCallLibrary.StdCallCallback {
    public boolean callback(Pointer var1, Pointer var2);
}
