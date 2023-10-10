// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.registry;

import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.util.ArrayList;
import oshi.software.os.OSSession;
import java.util.List;
import com.sun.jna.platform.win32.Netapi32;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class NetSessionData
{
    private static final Netapi32 NET;
    
    private NetSessionData() {
    }
    
    public static List<OSSession> queryUserSessions() {
        final List<OSSession> sessions = new ArrayList<OSSession>();
        final PointerByReference bufptr = new PointerByReference();
        final IntByReference entriesread = new IntByReference();
        final IntByReference totalentries = new IntByReference();
        if (0 == NetSessionData.NET.NetSessionEnum(null, null, null, 10, bufptr, -1, entriesread, totalentries, null)) {
            final Pointer buf = bufptr.getValue();
            final Netapi32.SESSION_INFO_10 si10 = new Netapi32.SESSION_INFO_10(buf);
            if (entriesread.getValue() > 0) {
                final Netapi32.SESSION_INFO_10[] array;
                final Netapi32.SESSION_INFO_10[] sessionInfo = array = (Netapi32.SESSION_INFO_10[])si10.toArray(entriesread.getValue());
                for (final Netapi32.SESSION_INFO_10 si11 : array) {
                    final long logonTime = System.currentTimeMillis() - 1000L * si11.sesi10_time;
                    sessions.add(new OSSession(si11.sesi10_username, "Network session", logonTime, si11.sesi10_cname));
                }
            }
            NetSessionData.NET.NetApiBufferFree(buf);
        }
        return sessions;
    }
    
    static {
        NET = Netapi32.INSTANCE;
    }
}
