// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.linux.proc;

import oshi.util.FileUtil;
import oshi.util.platform.linux.ProcPath;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class UpTime
{
    private UpTime() {
    }
    
    public static double getSystemUptimeSeconds() {
        final String uptime = FileUtil.getStringFromFile(ProcPath.UPTIME);
        final int spaceIndex = uptime.indexOf(32);
        try {
            if (spaceIndex < 0) {
                return 0.0;
            }
            return Double.parseDouble(uptime.substring(0, spaceIndex));
        }
        catch (NumberFormatException nfe) {
            return 0.0;
        }
    }
}
