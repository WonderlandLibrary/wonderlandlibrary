// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.aix;

import java.util.Collections;
import java.util.function.Function;
import oshi.driver.unix.aix.Lspv;
import oshi.util.tuples.Pair;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;
import oshi.driver.unix.aix.Lscfg;
import java.util.ArrayList;
import oshi.driver.unix.aix.Ls;
import oshi.hardware.HWDiskStore;
import com.sun.jna.Native;
import oshi.hardware.HWPartition;
import java.util.List;
import com.sun.jna.platform.unix.aix.Perfstat;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractHWDiskStore;

@ThreadSafe
public final class AixHWDiskStore extends AbstractHWDiskStore
{
    private final Supplier<Perfstat.perfstat_disk_t[]> diskStats;
    private long reads;
    private long readBytes;
    private long writes;
    private long writeBytes;
    private long currentQueueLength;
    private long transferTime;
    private long timeStamp;
    private List<HWPartition> partitionList;
    
    private AixHWDiskStore(final String name, final String model, final String serial, final long size, final Supplier<Perfstat.perfstat_disk_t[]> diskStats) {
        super(name, model, serial, size);
        this.reads = 0L;
        this.readBytes = 0L;
        this.writes = 0L;
        this.writeBytes = 0L;
        this.currentQueueLength = 0L;
        this.transferTime = 0L;
        this.timeStamp = 0L;
        this.diskStats = diskStats;
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
        for (final Perfstat.perfstat_disk_t stat : this.diskStats.get()) {
            final String name = Native.toString(stat.name);
            if (name.equals(this.getName())) {
                final long blks = stat.rblks + stat.wblks;
                this.reads = stat.xfers;
                if (blks > 0L) {
                    this.writes = stat.xfers * stat.wblks / blks;
                    this.reads -= this.writes;
                }
                this.readBytes = stat.rblks * stat.bsize;
                this.writeBytes = stat.wblks * stat.bsize;
                this.currentQueueLength = stat.qdepth;
                this.transferTime = stat.time;
                return true;
            }
        }
        return false;
    }
    
    public static List<HWDiskStore> getDisks(final Supplier<Perfstat.perfstat_disk_t[]> diskStats) {
        final Map<String, Pair<Integer, Integer>> majMinMap = Ls.queryDeviceMajorMinor();
        final List<AixHWDiskStore> storeList = new ArrayList<AixHWDiskStore>();
        for (final Perfstat.perfstat_disk_t disk : diskStats.get()) {
            final String storeName = Native.toString(disk.name);
            final Pair<String, String> ms = Lscfg.queryModelSerial(storeName);
            final String model = (ms.getA() == null) ? Native.toString(disk.description) : ms.getA();
            final String serial = (ms.getB() == null) ? "unknown" : ms.getB();
            storeList.add(createStore(storeName, model, serial, disk.size << 20, diskStats, majMinMap));
        }
        return storeList.stream().sorted(Comparator.comparingInt(s -> s.getPartitions().isEmpty() ? Integer.MAX_VALUE : s.getPartitions().get(0).getMajor())).collect((Collector<? super Object, ?, List<HWDiskStore>>)Collectors.toList());
    }
    
    private static AixHWDiskStore createStore(final String diskName, final String model, final String serial, final long size, final Supplier<Perfstat.perfstat_disk_t[]> diskStats, final Map<String, Pair<Integer, Integer>> majMinMap) {
        final AixHWDiskStore store = new AixHWDiskStore(diskName, model.isEmpty() ? "unknown" : model, serial, size, diskStats);
        store.partitionList = Collections.unmodifiableList((List<? extends HWPartition>)Lspv.queryLogicalVolumes(diskName, majMinMap).stream().sorted(Comparator.comparing((Function<? super Object, ? extends Comparable>)HWPartition::getMinor).thenComparing((Function<? super Object, ? extends Comparable>)HWPartition::getName)).collect((Collector<? super Object, ?, List<? extends T>>)Collectors.toList()));
        store.updateAttributes();
        return store;
    }
}
