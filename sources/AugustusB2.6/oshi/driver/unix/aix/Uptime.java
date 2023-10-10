// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix.aix;

import java.util.regex.Matcher;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import java.util.regex.Pattern;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Uptime
{
    private static final long MINUTE_MS = 60000L;
    private static final long HOUR_MS = 3600000L;
    private static final long DAY_MS = 86400000L;
    private static final Pattern UPTIME_FORMAT_AIX;
    
    private Uptime() {
    }
    
    public static long queryUpTime() {
        long uptime = 0L;
        final String s = ExecutingCommand.getFirstAnswer("/usr/bin/uptime");
        final Matcher m = Uptime.UPTIME_FORMAT_AIX.matcher(s);
        if (m.matches()) {
            if (m.group(2) != null) {
                uptime += ParseUtil.parseLongOrDefault(m.group(2), 0L) * 86400000L;
            }
            if (m.group(4) != null) {
                uptime += ParseUtil.parseLongOrDefault(m.group(4), 0L) * 3600000L;
            }
            uptime += ParseUtil.parseLongOrDefault(m.group(5), 0L) * 60000L;
        }
        return uptime;
    }
    
    static {
        UPTIME_FORMAT_AIX = Pattern.compile(".*\\sup\\s+((\\d+)\\s+days?,?\\s+)?\\b((\\d+):)?(\\d+)(\\s+min(utes?)?)?,\\s+\\d+\\s+user.+");
    }
}
