// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.freebsd;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import java.util.Collections;
import oshi.util.tuples.Triplet;
import java.util.Arrays;
import oshi.util.platform.unix.freebsd.BsdSysctlUtil;
import oshi.driver.unix.freebsd.disk.GeomDiskList;
import oshi.driver.unix.freebsd.disk.GeomPartList;
import java.util.ArrayList;
import oshi.hardware.HWDiskStore;
import java.util.Iterator;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import oshi.hardware.HWPartition;
import java.util.List;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractHWDiskStore;

@ThreadSafe
public final class FreeBsdHWDiskStore extends AbstractHWDiskStore
{
    private long reads;
    private long readBytes;
    private long writes;
    private long writeBytes;
    private long currentQueueLength;
    private long transferTime;
    private long timeStamp;
    private List<HWPartition> partitionList;
    
    private FreeBsdHWDiskStore(final String name, final String model, final String serial, final long size) {
        super(name, model, serial, size);
        this.reads = 0L;
        this.readBytes = 0L;
        this.writes = 0L;
        this.writeBytes = 0L;
        this.currentQueueLength = 0L;
        this.transferTime = 0L;
        this.timeStamp = 0L;
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
        final List<String> output = ExecutingCommand.runNative("iostat -Ix " + this.getName());
        final long now = System.currentTimeMillis();
        boolean diskFound = false;
        for (final String line : output) {
            final String[] split = ParseUtil.whitespaces.split(line);
            if (split.length >= 7) {
                if (!split[0].equals(this.getName())) {
                    continue;
                }
                diskFound = true;
                this.reads = (long)ParseUtil.parseDoubleOrDefault(split[1], 0.0);
                this.writes = (long)ParseUtil.parseDoubleOrDefault(split[2], 0.0);
                this.readBytes = (long)(ParseUtil.parseDoubleOrDefault(split[3], 0.0) * 1024.0);
                this.writeBytes = (long)(ParseUtil.parseDoubleOrDefault(split[4], 0.0) * 1024.0);
                this.currentQueueLength = ParseUtil.parseLongOrDefault(split[5], 0L);
                this.transferTime = (long)(ParseUtil.parseDoubleOrDefault(split[6], 0.0) * 1000.0);
                this.timeStamp = now;
            }
        }
        return diskFound;
    }
    
    public static List<HWDiskStore> getDisks() {
        final List<HWDiskStore> diskList = new ArrayList<HWDiskStore>();
        final Map<String, List<HWPartition>> partitionMap = GeomPartList.queryPartitions();
        final Map<String, Triplet<String, String, Long>> diskInfoMap = GeomDiskList.queryDisks();
        final List<String> devices = Arrays.asList(ParseUtil.whitespaces.split(BsdSysctlUtil.sysctl("kern.disks", "")));
        final List<String> iostat = ExecutingCommand.runNative("iostat -Ix");
        final long now = System.currentTimeMillis();
        for (final String line : iostat) {
            final String[] split = ParseUtil.whitespaces.split(line);
            if (split.length > 6 && devices.contains(split[0])) {
                final Triplet<String, String, Long> storeInfo = diskInfoMap.get(split[0]);
                final FreeBsdHWDiskStore store = (storeInfo == null) ? new FreeBsdHWDiskStore(split[0], "unknown", "unknown", 0L) : new FreeBsdHWDiskStore(split[0], storeInfo.getA(), storeInfo.getB(), storeInfo.getC());
                store.reads = (long)ParseUtil.parseDoubleOrDefault(split[1], 0.0);
                store.writes = (long)ParseUtil.parseDoubleOrDefault(split[2], 0.0);
                store.readBytes = (long)(ParseUtil.parseDoubleOrDefault(split[3], 0.0) * 1024.0);
                store.writeBytes = (long)(ParseUtil.parseDoubleOrDefault(split[4], 0.0) * 1024.0);
                store.currentQueueLength = ParseUtil.parseLongOrDefault(split[5], 0L);
                store.transferTime = (long)(ParseUtil.parseDoubleOrDefault(split[6], 0.0) * 1000.0);
                store.partitionList = Collections.unmodifiableList((List<? extends HWPartition>)partitionMap.getOrDefault(split[0], Collections.emptyList()).stream().sorted(Comparator.comparing((Function<? super Object, ? extends Comparable>)HWPartition::getName)).collect((Collector<? super Object, ?, List<? extends T>>)Collectors.toList()));
                store.timeStamp = now;
                diskList.add(store);
            }
        }
        return diskList;
    }
}
