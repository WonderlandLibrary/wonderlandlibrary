// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.windows;

import java.util.HashMap;
import org.slf4j.LoggerFactory;
import java.util.regex.Matcher;
import com.sun.jna.platform.win32.Kernel32;
import oshi.driver.windows.wmi.Win32DiskPartition;
import oshi.driver.windows.wmi.Win32LogicalDiskToPartition;
import oshi.driver.windows.wmi.Win32DiskDriveToDiskPartition;
import oshi.util.tuples.Pair;
import java.util.Map;
import oshi.driver.windows.perfmon.PhysicalDisk;
import java.util.Iterator;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.COM.COMException;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import java.util.Collection;
import oshi.util.ParseUtil;
import oshi.util.platform.windows.WmiUtil;
import oshi.driver.windows.wmi.Win32DiskDrive;
import java.util.ArrayList;
import java.util.Objects;
import oshi.util.platform.windows.WmiQueryHandler;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractHWDiskStore;

@ThreadSafe
public final class WindowsHWDiskStore extends AbstractHWDiskStore
{
    private static final Logger LOG;
    private static final String PHYSICALDRIVE_PREFIX = "\\\\.\\PHYSICALDRIVE";
    private static final Pattern DEVICE_ID;
    private static final int GUID_BUFSIZE = 100;
    private long reads;
    private long readBytes;
    private long writes;
    private long writeBytes;
    private long currentQueueLength;
    private long transferTime;
    private long timeStamp;
    private List<HWPartition> partitionList;
    
    private WindowsHWDiskStore(final String name, final String model, final String serial, final long size) {
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
        String index = null;
        final List<HWPartition> partitions = this.getPartitions();
        if (!partitions.isEmpty()) {
            index = Integer.toString(partitions.get(0).getMajor());
        }
        else {
            if (!this.getName().startsWith("\\\\.\\PHYSICALDRIVE")) {
                WindowsHWDiskStore.LOG.warn("Couldn't match index for {}", this.getName());
                return false;
            }
            index = this.getName().substring("\\\\.\\PHYSICALDRIVE".length(), this.getName().length());
        }
        final DiskStats stats = queryReadWriteStats(index);
        if (stats.readMap.containsKey(index)) {
            this.reads = stats.readMap.getOrDefault(index, 0L);
            this.readBytes = stats.readByteMap.getOrDefault(index, 0L);
            this.writes = stats.writeMap.getOrDefault(index, 0L);
            this.writeBytes = stats.writeByteMap.getOrDefault(index, 0L);
            this.currentQueueLength = stats.queueLengthMap.getOrDefault(index, 0L);
            this.transferTime = stats.diskTimeMap.getOrDefault(index, 0L);
            this.timeStamp = stats.timeStamp;
            return true;
        }
        return false;
    }
    
    public static List<HWDiskStore> getDisks() {
        final WmiQueryHandler h = Objects.requireNonNull(WmiQueryHandler.createInstance());
        boolean comInit = false;
        try {
            comInit = h.initCOM();
            final List<HWDiskStore> result = new ArrayList<HWDiskStore>();
            final DiskStats stats = queryReadWriteStats(null);
            final PartitionMaps maps = queryPartitionMaps(h);
            final WbemcliUtil.WmiResult<Win32DiskDrive.DiskDriveProperty> vals = Win32DiskDrive.queryDiskDrive(h);
            for (int i = 0; i < vals.getResultCount(); ++i) {
                final WindowsHWDiskStore ds = new WindowsHWDiskStore(WmiUtil.getString(vals, Win32DiskDrive.DiskDriveProperty.NAME, i), String.format("%s %s", WmiUtil.getString(vals, Win32DiskDrive.DiskDriveProperty.MODEL, i), WmiUtil.getString(vals, Win32DiskDrive.DiskDriveProperty.MANUFACTURER, i)).trim(), ParseUtil.hexStringToString(WmiUtil.getString(vals, Win32DiskDrive.DiskDriveProperty.SERIALNUMBER, i)), WmiUtil.getUint64(vals, Win32DiskDrive.DiskDriveProperty.SIZE, i));
                final String index = Integer.toString(WmiUtil.getUint32(vals, Win32DiskDrive.DiskDriveProperty.INDEX, i));
                ds.reads = stats.readMap.getOrDefault(index, 0L);
                ds.readBytes = stats.readByteMap.getOrDefault(index, 0L);
                ds.writes = stats.writeMap.getOrDefault(index, 0L);
                ds.writeBytes = stats.writeByteMap.getOrDefault(index, 0L);
                ds.currentQueueLength = stats.queueLengthMap.getOrDefault(index, 0L);
                ds.transferTime = stats.diskTimeMap.getOrDefault(index, 0L);
                ds.timeStamp = stats.timeStamp;
                final List<HWPartition> partitions = new ArrayList<HWPartition>();
                final List<String> partList = maps.driveToPartitionMap.get(ds.getName());
                if (partList != null && !partList.isEmpty()) {
                    for (final String part : partList) {
                        if (maps.partitionMap.containsKey(part)) {
                            partitions.addAll(maps.partitionMap.get(part));
                        }
                    }
                }
                ds.partitionList = Collections.unmodifiableList((List<? extends HWPartition>)partitions.stream().sorted(Comparator.comparing((Function<? super Object, ? extends Comparable>)HWPartition::getName)).collect((Collector<? super Object, ?, List<? extends T>>)Collectors.toList()));
                result.add(ds);
            }
            return result;
        }
        catch (COMException e) {
            WindowsHWDiskStore.LOG.warn("COM exception: {}", e.getMessage());
            return Collections.emptyList();
        }
        finally {
            if (comInit) {
                h.unInitCOM();
            }
        }
    }
    
    private static DiskStats queryReadWriteStats(final String index) {
        final DiskStats stats = new DiskStats();
        final Pair<List<String>, Map<PhysicalDisk.PhysicalDiskProperty, List<Long>>> instanceValuePair = PhysicalDisk.queryDiskCounters();
        final List<String> instances = instanceValuePair.getA();
        final Map<PhysicalDisk.PhysicalDiskProperty, List<Long>> valueMap = instanceValuePair.getB();
        stats.timeStamp = System.currentTimeMillis();
        final List<Long> readList = valueMap.get(PhysicalDisk.PhysicalDiskProperty.DISKREADSPERSEC);
        final List<Long> readByteList = valueMap.get(PhysicalDisk.PhysicalDiskProperty.DISKREADBYTESPERSEC);
        final List<Long> writeList = valueMap.get(PhysicalDisk.PhysicalDiskProperty.DISKWRITESPERSEC);
        final List<Long> writeByteList = valueMap.get(PhysicalDisk.PhysicalDiskProperty.DISKWRITEBYTESPERSEC);
        final List<Long> queueLengthList = valueMap.get(PhysicalDisk.PhysicalDiskProperty.CURRENTDISKQUEUELENGTH);
        final List<Long> diskTimeList = valueMap.get(PhysicalDisk.PhysicalDiskProperty.PERCENTDISKTIME);
        if (instances.isEmpty() || readList == null || readByteList == null || writeList == null || writeByteList == null || queueLengthList == null || diskTimeList == null) {
            return stats;
        }
        for (int i = 0; i < instances.size(); ++i) {
            final String name = getIndexFromName(instances.get(i));
            if (index == null || index.equals(name)) {
                stats.readMap.put(name, readList.get(i));
                stats.readByteMap.put(name, readByteList.get(i));
                stats.writeMap.put(name, writeList.get(i));
                stats.writeByteMap.put(name, writeByteList.get(i));
                stats.queueLengthMap.put(name, queueLengthList.get(i));
                stats.diskTimeMap.put(name, diskTimeList.get(i) / 10000L);
            }
        }
        return stats;
    }
    
    private static PartitionMaps queryPartitionMaps(final WmiQueryHandler h) {
        final PartitionMaps maps = new PartitionMaps();
        final WbemcliUtil.WmiResult<Win32DiskDriveToDiskPartition.DriveToPartitionProperty> drivePartitionMap = Win32DiskDriveToDiskPartition.queryDriveToPartition(h);
        for (int i = 0; i < drivePartitionMap.getResultCount(); ++i) {
            final Matcher mAnt = WindowsHWDiskStore.DEVICE_ID.matcher(WmiUtil.getRefString(drivePartitionMap, Win32DiskDriveToDiskPartition.DriveToPartitionProperty.ANTECEDENT, i));
            final Matcher mDep = WindowsHWDiskStore.DEVICE_ID.matcher(WmiUtil.getRefString(drivePartitionMap, Win32DiskDriveToDiskPartition.DriveToPartitionProperty.DEPENDENT, i));
            if (mAnt.matches() && mDep.matches()) {
                maps.driveToPartitionMap.computeIfAbsent(mAnt.group(1).replace("\\\\", "\\"), x -> new ArrayList()).add(mDep.group(1));
            }
        }
        final WbemcliUtil.WmiResult<Win32LogicalDiskToPartition.DiskToPartitionProperty> diskPartitionMap = Win32LogicalDiskToPartition.queryDiskToPartition(h);
        for (int j = 0; j < diskPartitionMap.getResultCount(); ++j) {
            final Matcher mAnt = WindowsHWDiskStore.DEVICE_ID.matcher(WmiUtil.getRefString(diskPartitionMap, Win32LogicalDiskToPartition.DiskToPartitionProperty.ANTECEDENT, j));
            final Matcher mDep = WindowsHWDiskStore.DEVICE_ID.matcher(WmiUtil.getRefString(diskPartitionMap, Win32LogicalDiskToPartition.DiskToPartitionProperty.DEPENDENT, j));
            final long size = WmiUtil.getUint64(diskPartitionMap, Win32LogicalDiskToPartition.DiskToPartitionProperty.ENDINGADDRESS, j) - WmiUtil.getUint64(diskPartitionMap, Win32LogicalDiskToPartition.DiskToPartitionProperty.STARTINGADDRESS, j) + 1L;
            if (mAnt.matches() && mDep.matches()) {
                if (maps.partitionToLogicalDriveMap.containsKey(mAnt.group(1))) {
                    maps.partitionToLogicalDriveMap.get(mAnt.group(1)).add(new Pair(mDep.group(1) + "\\", size));
                }
                else {
                    final List<Pair<String, Long>> list = new ArrayList<Pair<String, Long>>();
                    list.add(new Pair<String, Long>(mDep.group(1) + "\\", size));
                    maps.partitionToLogicalDriveMap.put(mAnt.group(1), list);
                }
            }
        }
        final WbemcliUtil.WmiResult<Win32DiskPartition.DiskPartitionProperty> hwPartitionQueryMap = Win32DiskPartition.queryPartition(h);
        for (int k = 0; k < hwPartitionQueryMap.getResultCount(); ++k) {
            final String deviceID = WmiUtil.getString(hwPartitionQueryMap, Win32DiskPartition.DiskPartitionProperty.DEVICEID, k);
            final List<Pair<String, Long>> logicalDrives = maps.partitionToLogicalDriveMap.get(deviceID);
            if (logicalDrives != null) {
                for (int l = 0; l < logicalDrives.size(); ++l) {
                    final Pair<String, Long> logicalDrive = logicalDrives.get(l);
                    if (logicalDrive != null && !logicalDrive.getA().isEmpty()) {
                        final char[] volumeChr = new char[100];
                        Kernel32.INSTANCE.GetVolumeNameForVolumeMountPoint(logicalDrive.getA(), volumeChr, 100);
                        final String uuid = ParseUtil.parseUuidOrDefault(new String(volumeChr).trim(), "");
                        final HWPartition pt = new HWPartition(WmiUtil.getString(hwPartitionQueryMap, Win32DiskPartition.DiskPartitionProperty.NAME, k), WmiUtil.getString(hwPartitionQueryMap, Win32DiskPartition.DiskPartitionProperty.TYPE, k), WmiUtil.getString(hwPartitionQueryMap, Win32DiskPartition.DiskPartitionProperty.DESCRIPTION, k), uuid, logicalDrive.getB(), WmiUtil.getUint32(hwPartitionQueryMap, Win32DiskPartition.DiskPartitionProperty.DISKINDEX, k), WmiUtil.getUint32(hwPartitionQueryMap, Win32DiskPartition.DiskPartitionProperty.INDEX, k), logicalDrive.getA());
                        if (maps.partitionMap.containsKey(deviceID)) {
                            maps.partitionMap.get(deviceID).add(pt);
                        }
                        else {
                            final List<HWPartition> ptlist = new ArrayList<HWPartition>();
                            ptlist.add(pt);
                            maps.partitionMap.put(deviceID, ptlist);
                        }
                    }
                }
            }
        }
        return maps;
    }
    
    private static String getIndexFromName(final String s) {
        if (s.isEmpty()) {
            return s;
        }
        return s.split("\\s")[0];
    }
    
    static {
        LOG = LoggerFactory.getLogger(WindowsHWDiskStore.class);
        DEVICE_ID = Pattern.compile(".*\\.DeviceID=\"(.*)\"");
    }
    
    private static final class DiskStats
    {
        private final Map<String, Long> readMap;
        private final Map<String, Long> readByteMap;
        private final Map<String, Long> writeMap;
        private final Map<String, Long> writeByteMap;
        private final Map<String, Long> queueLengthMap;
        private final Map<String, Long> diskTimeMap;
        private long timeStamp;
        
        private DiskStats() {
            this.readMap = new HashMap<String, Long>();
            this.readByteMap = new HashMap<String, Long>();
            this.writeMap = new HashMap<String, Long>();
            this.writeByteMap = new HashMap<String, Long>();
            this.queueLengthMap = new HashMap<String, Long>();
            this.diskTimeMap = new HashMap<String, Long>();
        }
    }
    
    private static final class PartitionMaps
    {
        private final Map<String, List<String>> driveToPartitionMap;
        private final Map<String, List<Pair<String, Long>>> partitionToLogicalDriveMap;
        private final Map<String, List<HWPartition>> partitionMap;
        
        private PartitionMaps() {
            this.driveToPartitionMap = new HashMap<String, List<String>>();
            this.partitionToLogicalDriveMap = new HashMap<String, List<Pair<String, Long>>>();
            this.partitionMap = new HashMap<String, List<HWPartition>>();
        }
    }
}
