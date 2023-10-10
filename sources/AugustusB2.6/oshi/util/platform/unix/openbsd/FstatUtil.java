// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util.platform.unix.openbsd;

import java.util.Iterator;
import oshi.util.ParseUtil;
import java.util.List;
import oshi.util.ExecutingCommand;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class FstatUtil
{
    private FstatUtil() {
    }
    
    public static String getCwd(final int pid) {
        final List<String> ps = ExecutingCommand.runNative("ps -axwwo cwd -p " + pid);
        if (ps.size() > 1) {
            return ps.get(1);
        }
        return "";
    }
    
    public static long getOpenFiles(final int pid) {
        long fd = 0L;
        final List<String> fstat = ExecutingCommand.runNative("fstat -sp " + pid);
        for (final String line : fstat) {
            final String[] split = ParseUtil.whitespaces.split(line.trim(), 11);
            if (split.length == 11 && !"pipe".contains(split[4]) && !"unix".contains(split[4])) {
                ++fd;
            }
        }
        return fd - 1L;
    }
}
