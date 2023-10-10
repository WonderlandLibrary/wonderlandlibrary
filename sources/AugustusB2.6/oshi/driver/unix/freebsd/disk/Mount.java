// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix.freebsd.disk;

import java.util.regex.Matcher;
import java.util.Iterator;
import oshi.util.ExecutingCommand;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Mount
{
    private static final String MOUNT_CMD = "mount";
    private static final Pattern MOUNT_PATTERN;
    
    private Mount() {
    }
    
    public static Map<String, String> queryPartitionToMountMap() {
        final Map<String, String> mountMap = new HashMap<String, String>();
        for (final String mnt : ExecutingCommand.runNative("mount")) {
            final Matcher m = Mount.MOUNT_PATTERN.matcher(mnt);
            if (m.matches()) {
                mountMap.put(m.group(1), m.group(2));
            }
        }
        return mountMap;
    }
    
    static {
        MOUNT_PATTERN = Pattern.compile("/dev/(\\S+p\\d+) on (\\S+) .*");
    }
}
