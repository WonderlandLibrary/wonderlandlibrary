// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util.platform.mac;

import org.slf4j.LoggerFactory;
import com.sun.jna.Structure;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Memory;
import com.sun.jna.platform.unix.LibCAPI;
import com.sun.jna.platform.mac.SystemB;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class SysctlUtil
{
    private static final Logger LOG;
    private static final String SYSCTL_FAIL = "Failed sysctl call: {}, Error code: {}";
    
    private SysctlUtil() {
    }
    
    public static int sysctl(final String name, final int def) {
        final LibCAPI.size_t.ByReference size = new LibCAPI.size_t.ByReference((long)SystemB.INT_SIZE);
        final Pointer p = new Memory(size.longValue());
        if (0 != oshi.jna.platform.mac.SystemB.INSTANCE.sysctlbyname(name, p, size, null, LibCAPI.size_t.ZERO)) {
            SysctlUtil.LOG.warn("Failed sysctl call: {}, Error code: {}", name, Native.getLastError());
            return def;
        }
        return p.getInt(0L);
    }
    
    public static long sysctl(final String name, final long def) {
        final LibCAPI.size_t.ByReference size = new LibCAPI.size_t.ByReference((long)SystemB.UINT64_SIZE);
        final Pointer p = new Memory(size.longValue());
        if (0 != oshi.jna.platform.mac.SystemB.INSTANCE.sysctlbyname(name, p, size, null, LibCAPI.size_t.ZERO)) {
            SysctlUtil.LOG.error("Failed sysctl call: {}, Error code: {}", name, Native.getLastError());
            return def;
        }
        return p.getLong(0L);
    }
    
    public static String sysctl(final String name, final String def) {
        final LibCAPI.size_t.ByReference size = new LibCAPI.size_t.ByReference();
        if (0 != oshi.jna.platform.mac.SystemB.INSTANCE.sysctlbyname(name, null, size, null, LibCAPI.size_t.ZERO)) {
            SysctlUtil.LOG.error("Failed sysctl call: {}, Error code: {}", name, Native.getLastError());
            return def;
        }
        final Pointer p = new Memory(size.longValue() + 1L);
        if (0 != oshi.jna.platform.mac.SystemB.INSTANCE.sysctlbyname(name, p, size, null, LibCAPI.size_t.ZERO)) {
            SysctlUtil.LOG.error("Failed sysctl call: {}, Error code: {}", name, Native.getLastError());
            return def;
        }
        return p.getString(0L);
    }
    
    public static boolean sysctl(final String name, final Structure struct) {
        if (0 != oshi.jna.platform.mac.SystemB.INSTANCE.sysctlbyname(name, struct.getPointer(), new LibCAPI.size_t.ByReference((long)struct.size()), null, LibCAPI.size_t.ZERO)) {
            SysctlUtil.LOG.error("Failed sysctl call: {}, Error code: {}", name, Native.getLastError());
            return false;
        }
        struct.read();
        return true;
    }
    
    public static Memory sysctl(final String name) {
        final LibCAPI.size_t.ByReference size = new LibCAPI.size_t.ByReference();
        if (0 != oshi.jna.platform.mac.SystemB.INSTANCE.sysctlbyname(name, null, size, null, LibCAPI.size_t.ZERO)) {
            SysctlUtil.LOG.error("Failed sysctl call: {}, Error code: {}", name, Native.getLastError());
            return null;
        }
        final Memory m = new Memory(size.longValue());
        if (0 != oshi.jna.platform.mac.SystemB.INSTANCE.sysctlbyname(name, m, size, null, LibCAPI.size_t.ZERO)) {
            SysctlUtil.LOG.error("Failed sysctl call: {}, Error code: {}", name, Native.getLastError());
            return null;
        }
        return m;
    }
    
    static {
        LOG = LoggerFactory.getLogger(SysctlUtil.class);
    }
}
