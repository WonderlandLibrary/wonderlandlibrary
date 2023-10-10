// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.openbsd;

import java.util.regex.Matcher;
import java.util.Iterator;
import oshi.util.tuples.Quartet;
import oshi.util.ParseUtil;
import java.util.regex.Pattern;
import oshi.util.ExecutingCommand;
import oshi.driver.unix.openbsd.disk.Disklabel;
import oshi.util.platform.unix.openbsd.OpenBsdSysctlUtil;
import java.util.ArrayList;
import oshi.hardware.HWDiskStore;
import oshi.util.Memoizer;
import oshi.hardware.HWPartition;
import java.util.List;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractHWDiskStore;

@ThreadSafe
public final class OpenBsdHWDiskStore extends AbstractHWDiskStore
{
    private final Supplier<List<String>> iostat;
    private long reads;
    private long readBytes;
    private long writes;
    private long writeBytes;
    private long currentQueueLength;
    private long transferTime;
    private long timeStamp;
    private List<HWPartition> partitionList;
    
    private OpenBsdHWDiskStore(final String name, final String model, final String serial, final long size) {
        super(name, model, serial, size);
        this.iostat = Memoizer.memoize(OpenBsdHWDiskStore::querySystatIostat, Memoizer.defaultExpiration());
        this.reads = 0L;
        this.readBytes = 0L;
        this.writes = 0L;
        this.writeBytes = 0L;
        this.currentQueueLength = 0L;
        this.transferTime = 0L;
        this.timeStamp = 0L;
    }
    
    public static List<HWDiskStore> getDisks() {
        final List<HWDiskStore> diskList = new ArrayList<HWDiskStore>();
        List<String> dmesg = null;
        final String[] split;
        final String[] devices = split = OpenBsdSysctlUtil.sysctl("hw.disknames", "").split(",");
        for (final String device : split) {
            final String diskName = device.split(":")[0];
            final Quartet<String, String, Long, List<HWPartition>> diskdata = Disklabel.getDiskParams(diskName);
            String model = diskdata.getA();
            long size = diskdata.getC();
            if (size <= 1L) {
                if (dmesg == null) {
                    dmesg = ExecutingCommand.runNative("dmesg");
                }
                final Pattern diskAt = Pattern.compile(diskName + " at .*<(.+)>.*");
                final Pattern diskMB = Pattern.compile(diskName + ":.* (\\d+)MB, (?:(\\d+) bytes\\/sector, )?(?:(\\d+) sectors).*");
                for (final String line : dmesg) {
                    Matcher m = diskAt.matcher(line);
                    if (m.matches()) {
                        model = m.group(1);
                    }
                    m = diskMB.matcher(line);
                    if (m.matches()) {
                        final long sectors = ParseUtil.parseLongOrDefault(m.group(3), 0L);
                        long bytesPerSector = ParseUtil.parseLongOrDefault(m.group(2), 0L);
                        if (bytesPerSector == 0L && sectors > 0L) {
                            size = ParseUtil.parseLongOrDefault(m.group(1), 0L) << 20;
                            bytesPerSector = size / sectors;
                            bytesPerSector = Long.highestOneBit(bytesPerSector + bytesPerSector >> 1);
                        }
                        size = bytesPerSector * sectors;
                        break;
                    }
                }
            }
            final OpenBsdHWDiskStore store = new OpenBsdHWDiskStore(diskName, model, diskdata.getB(), size);
            store.partitionList = diskdata.getD();
            store.updateAttributes();
            diskList.add(store);
        }
        return diskList;
    }
    
    @Override
    public long getReads() {
        return this.reads;
    }
    
    @Override
    public long getReadBytes() {
        return this.readBytes;
    }
    
    @Override
    public long getWrites() {
        return this.writes;
    }
    
    @Override
    public long getWriteBytes() {
        return this.writeBytes;
    }
    
    @Override
    public long getCurrentQueueLength() {
        return this.currentQueueLength;
    }
    
    @Override
    public long getTransferTime() {
        return this.transferTime;
    }
    
    @Override
    public long getTimeStamp() {
        return this.timeStamp;
    }
    
    @Override
    public List<HWPartition> getPartitions() {
        return this.partitionList;
    }
    
    @Override
    public boolean updateAttributes() {
        final long now = System.currentTimeMillis();
        boolean diskFound = false;
        for (final String line : this.iostat.get()) {
            final String[] split = ParseUtil.whitespaces.split(line);
            if (split.length < 7 && split[0].equals(this.getName())) {
                diskFound = true;
                this.readBytes = ParseUtil.parseMultipliedToLongs(split[1]);
                this.writeBytes = ParseUtil.parseMultipliedToLongs(split[2]);
                this.reads = (long)ParseUtil.parseDoubleOrDefault(split[3], 0.0);
                this.writes = (long)ParseUtil.parseDoubleOrDefault(split[4], 0.0);
                this.transferTime = (long)(ParseUtil.parseDoubleOrDefault(split[5], 0.0) * 1000.0);
                this.timeStamp = now;
            }
        }
        return diskFound;
    }
    
    private static List<String> querySystatIostat() {
        return ExecutingCommand.runNative("systat -ab iostat");
    }
}
