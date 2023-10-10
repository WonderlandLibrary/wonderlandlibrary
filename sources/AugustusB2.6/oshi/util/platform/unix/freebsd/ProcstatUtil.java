// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util.platform.unix.freebsd;

import java.util.Iterator;
import java.util.List;
import oshi.util.ParseUtil;
import java.util.HashMap;
import oshi.util.ExecutingCommand;
import java.util.Map;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class ProcstatUtil
{
    private ProcstatUtil() {
    }
    
    public static Map<Integer, String> getCwdMap(final int pid) {
        final List<String> procstat = ExecutingCommand.runNative("procstat -f " + ((pid < 0) ? "-a" : Integer.valueOf(pid)));
        final Map<Integer, String> cwdMap = new HashMap<Integer, String>();
        for (final String line : procstat) {
            final String[] split = ParseUtil.whitespaces.split(line.trim(), 10);
            if (split.length == 10 && split[2].equals("cwd")) {
                cwdMap.put(ParseUtil.parseIntOrDefault(split[0], -1), split[9]);
            }
        }
        return cwdMap;
    }
    
    public static String getCwd(final int pid) {
        final List<String> procstat = ExecutingCommand.runNative("procstat -f " + pid);
        for (final String line : procstat) {
            final String[] split = ParseUtil.whitespaces.split(line.trim(), 10);
            if (split.length == 10 && split[2].equals("cwd")) {
                return split[9];
            }
        }
        return "";
    }
    
    public static long getOpenFiles(final int pid) {
        long fd = 0L;
        final List<String> procstat = ExecutingCommand.runNative("procstat -f " + pid);
        for (final String line : procstat) {
            final String[] split = ParseUtil.whitespaces.split(line.trim(), 10);
            if (split.length == 10 && !"Vd-".contains(split[4])) {
                ++fd;
            }
        }
        return fd;
    }
}
