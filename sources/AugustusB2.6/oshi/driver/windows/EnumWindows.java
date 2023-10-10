// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows;

import java.util.Iterator;
import java.util.Map;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.DesktopWindow;
import java.util.HashMap;
import java.util.ArrayList;
import com.sun.jna.platform.WindowUtils;
import oshi.software.os.OSDesktopWindow;
import java.util.List;
import com.sun.jna.platform.win32.WinDef;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class EnumWindows
{
    private static final WinDef.DWORD GW_HWNDNEXT;
    
    private EnumWindows() {
    }
    
    public static List<OSDesktopWindow> queryDesktopWindows(final boolean visibleOnly) {
        final List<DesktopWindow> windows = WindowUtils.getAllWindows(true);
        final List<OSDesktopWindow> windowList = new ArrayList<OSDesktopWindow>();
        final Map<WinDef.HWND, Integer> zOrderMap = new HashMap<WinDef.HWND, Integer>();
        for (final DesktopWindow window : windows) {
            final WinDef.HWND hWnd = window.getHWND();
            if (hWnd != null) {
                final boolean visible = User32.INSTANCE.IsWindowVisible(hWnd);
                if (visibleOnly && !visible) {
                    continue;
                }
                if (!zOrderMap.containsKey(hWnd)) {
                    updateWindowZOrderMap(hWnd, zOrderMap);
                }
                final IntByReference pProcessId = new IntByReference();
                User32.INSTANCE.GetWindowThreadProcessId(hWnd, pProcessId);
                windowList.add(new OSDesktopWindow(Pointer.nativeValue(hWnd.getPointer()), window.getTitle(), window.getFilePath(), window.getLocAndSize(), pProcessId.getValue(), zOrderMap.get(hWnd), visible));
            }
        }
        return windowList;
    }
    
    private static void updateWindowZOrderMap(final WinDef.HWND hWnd, final Map<WinDef.HWND, Integer> zOrderMap) {
        if (hWnd != null) {
            int zOrder = 1;
            WinDef.HWND h = new WinDef.HWND(hWnd.getPointer());
            do {
                zOrderMap.put(h, --zOrder);
            } while ((h = User32.INSTANCE.GetWindow(h, EnumWindows.GW_HWNDNEXT)) != null);
            final int offset = zOrder * -1;
            zOrderMap.replaceAll((k, v) -> v + offset);
        }
    }
    
    static {
        GW_HWNDNEXT = new WinDef.DWORD(2L);
    }
}
