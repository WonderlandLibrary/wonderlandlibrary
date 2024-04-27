package me.utils;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;
import java.util.ArrayList;

public class WindowUtils {
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    public static String[] getWindowNames() {
        User32 user32 = User32.INSTANCE;
        ArrayList list = new ArrayList();
        user32.EnumWindows((hwnd, arg1) -> {
            byte[] windowText = new byte[512];
            user32.GetWindowTextA(hwnd, windowText, 512);
            String wText = Native.toString(windowText);
            list.add(wText);
            return true;
        }, null);
        return list.toArray(new String[0]);
    }

    public static interface User32
    extends StdCallLibrary {
        public static final User32 INSTANCE = Native.loadLibrary("user32", User32.class);

        public boolean EnumWindows(WinUser.WNDENUMPROC var1, Pointer var2);

        public int GetWindowTextA(WinDef.HWND var1, byte[] var2, int var3);
    }
}
