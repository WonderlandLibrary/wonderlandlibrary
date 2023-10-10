// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix.solaris.disk;

import java.util.Iterator;
import java.util.List;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import java.util.HashMap;
import java.util.Map;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Lshal
{
    private static final String LSHAL_CMD = "lshal";
    
    private Lshal() {
    }
    
    public static Map<String, Integer> queryDiskToMajorMap() {
        final Map<String, Integer> majorMap = new HashMap<String, Integer>();
        final List<String> lshal = ExecutingCommand.runNative("lshal");
        String diskName = null;
        for (String line : lshal) {
            if (line.startsWith("udi ")) {
                final String udi = ParseUtil.getSingleQuoteStringValue(line);
                diskName = udi.substring(udi.lastIndexOf(47) + 1);
            }
            else {
                line = line.trim();
                if (!line.startsWith("block.major") || diskName == null) {
                    continue;
                }
                majorMap.put(diskName, ParseUtil.getFirstIntValue(line));
            }
        }
        return majorMap;
    }
}
