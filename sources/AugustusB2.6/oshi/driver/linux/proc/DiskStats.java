// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.linux.proc;

import java.util.Iterator;
import java.util.List;
import java.util.EnumMap;
import oshi.util.ParseUtil;
import oshi.util.FileUtil;
import oshi.util.platform.linux.ProcPath;
import java.util.HashMap;
import java.util.Map;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class DiskStats
{
    private DiskStats() {
    }
    
    public static Map<String, Map<IoStat, Long>> getDiskStats() {
        final Map<String, Map<IoStat, Long>> diskStatMap = new HashMap<String, Map<IoStat, Long>>();
        final IoStat[] enumArray = IoStat.class.getEnumConstants();
        final List<String> diskStats = FileUtil.readFile(ProcPath.DISKSTATS);
        for (final String stat : diskStats) {
            final String[] split = ParseUtil.whitespaces.split(stat.trim());
            final Map<IoStat, Long> statMap = new EnumMap<IoStat, Long>(IoStat.class);
            String name = null;
            for (int i = 0; i < enumArray.length && i < split.length; ++i) {
                if (enumArray[i] == IoStat.NAME) {
                    name = split[i];
                }
                else {
                    statMap.put(enumArray[i], ParseUtil.parseLongOrDefault(split[i], 0L));
                }
            }
            if (name != null) {
                diskStatMap.put(name, statMap);
            }
        }
        return diskStatMap;
    }
    
    public enum IoStat
    {
        MAJOR, 
        MINOR, 
        NAME, 
        READS, 
        READS_MERGED, 
        READS_SECTOR, 
        READS_MS, 
        WRITES, 
        WRITES_MERGED, 
        WRITES_SECTOR, 
        WRITES_MS, 
        IO_QUEUE_LENGTH, 
        IO_MS, 
        IO_MS_WEIGHTED, 
        DISCARDS, 
        DISCARDS_MERGED, 
        DISCARDS_SECTOR, 
        DISCARDS_MS, 
        FLUSHES, 
        FLUSHES_MS;
        
        private static /* synthetic */ IoStat[] $values() {
            return new IoStat[] { IoStat.MAJOR, IoStat.MINOR, IoStat.NAME, IoStat.READS, IoStat.READS_MERGED, IoStat.READS_SECTOR, IoStat.READS_MS, IoStat.WRITES, IoStat.WRITES_MERGED, IoStat.WRITES_SECTOR, IoStat.WRITES_MS, IoStat.IO_QUEUE_LENGTH, IoStat.IO_MS, IoStat.IO_MS_WEIGHTED, IoStat.DISCARDS, IoStat.DISCARDS_MERGED, IoStat.DISCARDS_SECTOR, IoStat.DISCARDS_MS, IoStat.FLUSHES, IoStat.FLUSHES_MS };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
