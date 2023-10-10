// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix;

import java.util.regex.Matcher;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import oshi.util.ParseUtil;
import oshi.util.Util;
import java.awt.Rectangle;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;
import oshi.util.ExecutingCommand;
import java.util.HashMap;
import oshi.software.os.OSDesktopWindow;
import java.util.List;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Xwininfo
{
    private static final String[] NET_CLIENT_LIST_STACKING;
    private static final String[] XWININFO_ROOT_TREE;
    private static final String[] XPROP_NET_WM_PID_ID;
    
    private Xwininfo() {
    }
    
    public static List<OSDesktopWindow> queryXWindows(final boolean visibleOnly) {
        final Map<String, Integer> zOrderMap = new HashMap<String, Integer>();
        int z = 0;
        final List<String> stacking = ExecutingCommand.runNative(Xwininfo.NET_CLIENT_LIST_STACKING, null);
        if (!stacking.isEmpty()) {
            final String stack = stacking.get(0);
            final int bottom = stack.indexOf("0x");
            if (bottom >= 0) {
                for (final String id : stack.substring(bottom).split(", ")) {
                    zOrderMap.put(id, ++z);
                }
            }
        }
        final Pattern windowPattern = Pattern.compile("(0x\\S+) (?:\"(.+)\")?.*: \\((?:\"(.+)\" \".+\")?\\)  (\\d+)x(\\d+)\\+.+  \\+(-?\\d+)\\+(-?\\d+)");
        final Map<String, String> windowNameMap = new HashMap<String, String>();
        final Map<String, String> windowPathMap = new HashMap<String, String>();
        final Map<String, Rectangle> windowMap = new LinkedHashMap<String, Rectangle>();
        for (final String line : ExecutingCommand.runNative(Xwininfo.XWININFO_ROOT_TREE, null)) {
            final Matcher m = windowPattern.matcher(line.trim());
            if (m.matches()) {
                final String id2 = m.group(1);
                if (visibleOnly && !zOrderMap.containsKey(id2)) {
                    continue;
                }
                final String windowName = m.group(2);
                if (!Util.isBlank(windowName)) {
                    windowNameMap.put(id2, windowName);
                }
                final String windowPath = m.group(3);
                if (!Util.isBlank(windowPath)) {
                    windowPathMap.put(id2, windowPath);
                }
                windowMap.put(id2, new Rectangle(ParseUtil.parseIntOrDefault(m.group(6), 0), ParseUtil.parseIntOrDefault(m.group(7), 0), ParseUtil.parseIntOrDefault(m.group(4), 0), ParseUtil.parseIntOrDefault(m.group(5), 0)));
            }
        }
        final List<OSDesktopWindow> windowList = new ArrayList<OSDesktopWindow>();
        for (final Map.Entry<String, Rectangle> e : windowMap.entrySet()) {
            final String id2 = e.getKey();
            final long pid = queryPidFromId(id2);
            final boolean visible = zOrderMap.containsKey(id2);
            windowList.add(new OSDesktopWindow(ParseUtil.hexStringToLong(id2, 0L), windowNameMap.getOrDefault(id2, ""), windowPathMap.getOrDefault(id2, ""), e.getValue(), pid, zOrderMap.getOrDefault(id2, 0), visible));
        }
        return windowList;
    }
    
    private static long queryPidFromId(final String id) {
        final String[] cmd = new String[Xwininfo.XPROP_NET_WM_PID_ID.length + 1];
        System.arraycopy(Xwininfo.XPROP_NET_WM_PID_ID, 0, cmd, 0, Xwininfo.XPROP_NET_WM_PID_ID.length);
        cmd[Xwininfo.XPROP_NET_WM_PID_ID.length] = id;
        final List<String> pidStr = ExecutingCommand.runNative(cmd, null);
        if (pidStr.isEmpty()) {
            return 0L;
        }
        return ParseUtil.getFirstIntValue(pidStr.get(0));
    }
    
    static {
        NET_CLIENT_LIST_STACKING = ParseUtil.whitespaces.split("xprop -root _NET_CLIENT_LIST_STACKING");
        XWININFO_ROOT_TREE = ParseUtil.whitespaces.split("xwininfo -root -tree");
        XPROP_NET_WM_PID_ID = ParseUtil.whitespaces.split("xprop _NET_WM_PID -id");
    }
}
