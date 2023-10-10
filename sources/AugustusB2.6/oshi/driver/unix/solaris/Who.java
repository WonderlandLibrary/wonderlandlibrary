// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix.solaris;

import com.sun.jna.Native;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import oshi.software.os.OSSession;
import java.util.List;
import oshi.jna.platform.unix.SolarisLibc;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Who
{
    private static final SolarisLibc LIBC;
    
    private Who() {
    }
    
    public static synchronized List<OSSession> queryUtxent() {
        final List<OSSession> whoList = new ArrayList<OSSession>();
        Who.LIBC.setutxent();
        try {
            SolarisLibc.SolarisUtmpx ut;
            while ((ut = Who.LIBC.getutxent()) != null) {
                if (ut.ut_type == 7 || ut.ut_type == 6) {
                    final String user = Native.toString(ut.ut_user, StandardCharsets.US_ASCII);
                    if ("LOGIN".equals(user)) {
                        continue;
                    }
                    final String device = Native.toString(ut.ut_line, StandardCharsets.US_ASCII);
                    final String host = Native.toString(ut.ut_host, StandardCharsets.US_ASCII);
                    final long loginTime = ut.ut_tv.tv_sec.longValue() * 1000L + ut.ut_tv.tv_usec.longValue() / 1000L;
                    if (user.isEmpty() || device.isEmpty() || loginTime < 0L || loginTime > System.currentTimeMillis()) {
                        return oshi.driver.unix.Who.queryWho();
                    }
                    whoList.add(new OSSession(user, device, loginTime, host));
                }
            }
        }
        finally {
            Who.LIBC.endutxent();
        }
        return whoList;
    }
    
    static {
        LIBC = SolarisLibc.INSTANCE;
    }
}
