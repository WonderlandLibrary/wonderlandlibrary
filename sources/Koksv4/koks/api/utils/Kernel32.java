package koks.api.utils;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

public interface Kernel32 extends com.sun.jna.platform.win32.Kernel32 {
    Kernel32 INSTANCE = (Kernel32) Native.load("kernel32.dll", Kernel32.class, W32APIOptions.DEFAULT_OPTIONS);

    boolean IsDebuggerPresent();
}
