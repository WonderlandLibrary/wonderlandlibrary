// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.mac.disk;

import com.sun.jna.Native;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import com.sun.jna.platform.mac.SystemB;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Fsstat
{
    private Fsstat() {
    }
    
    public static int queryFsstat(final SystemB.Statfs[] buf, final int bufsize, final int flags) {
        return SystemB.INSTANCE.getfsstat64(buf, bufsize, flags);
    }
    
    public static SystemB.Statfs[] getFileSystems(final int numfs) {
        final SystemB.Statfs[] fs = new SystemB.Statfs[numfs];
        queryFsstat(fs, numfs * new SystemB.Statfs().size(), 16);
        return fs;
    }
    
    public static Map<String, String> queryPartitionToMountMap() {
        final Map<String, String> mountPointMap = new HashMap<String, String>();
        final int numfs = queryFsstat(null, 0, 0);
        final SystemB.Statfs[] fileSystems;
        final SystemB.Statfs[] fs = fileSystems = getFileSystems(numfs);
        for (final SystemB.Statfs f : fileSystems) {
            final String mntFrom = Native.toString(f.f_mntfromname, StandardCharsets.UTF_8);
            mountPointMap.put(mntFrom.replace("/dev/", ""), Native.toString(f.f_mntonname, StandardCharsets.UTF_8));
        }
        return mountPointMap;
    }
}
