// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.linux;

import java.util.Arrays;
import java.io.File;
import oshi.util.FileUtil;
import oshi.util.platform.linux.ProcPath;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import oshi.util.ParseUtil;
import com.sun.jna.platform.linux.Udev;
import oshi.hardware.HWDiskStore;
import java.util.ArrayList;
import oshi.hardware.HWPartition;
import java.util.List;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractHWDiskStore;

@ThreadSafe
public final class LinuxHWDiskStore extends AbstractHWDiskStore
{
    private static final String BLOCK = "block";
    private static final String DISK = "disk";
    private static final String PARTITION = "partition";
    private static final String STAT = "stat";
    private static final String SIZE = "size";
    private static final String MINOR = "MINOR";
    private static final String MAJOR = "MAJOR";
    private static final String ID_FS_TYPE = "ID_FS_TYPE";
    private static final String ID_FS_UUID = "ID_FS_UUID";
    private static final String ID_MODEL = "ID_MODEL";
    private static final String ID_SERIAL_SHORT = "ID_SERIAL_SHORT";
    private static final String DM_UUID = "DM_UUID";
    private static final String DM_VG_NAME = "DM_VG_NAME";
    private static final String DM_LV_NAME = "DM_LV_NAME";
    private static final String LOGICAL_VOLUME_GROUP = "Logical Volume Group";
    private static final String DEV_LOCATION = "/dev/";
    private static final String DEV_MAPPER = "/dev/mapper/";
    private static final int SECTORSIZE = 512;
    private static final int[] UDEV_STAT_ORDERS;
    private static final int UDEV_STAT_LENGTH;
    private long reads;
    private long readBytes;
    private long writes;
    private long writeBytes;
    private long currentQueueLength;
    private long transferTime;
    private long timeStamp;
    private List<HWPartition> partitionList;
    
    private LinuxHWDiskStore(final String name, final String model, final String serial, final long size) {
        super(name, model, serial, size);
        this.reads = 0L;
        this.readBytes = 0L;
        this.writes = 0L;
        this.writeBytes = 0L;
        this.currentQueueLength = 0L;
        this.transferTime = 0L;
        this.timeStamp = 0L;
        this.partitionList = new ArrayList<HWPartition>();
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
    
    public static List<HWDiskStore> getDisks() {
        return getDisks(null);
    }
    
    private static List<HWDiskStore> getDisks(final LinuxHWDiskStore storeToUpdate) {
        LinuxHWDiskStore store = null;
        final List<HWDiskStore> result = new ArrayList<HWDiskStore>();
        final Map<String, String> mountsMap = readMountsMap();
        final Udev.UdevContext udev = Udev.INSTANCE.udev_new();
        try {
            final Udev.UdevEnumerate enumerate = udev.enumerateNew();
            try {
                enumerate.addMatchSubsystem("block");
                enumerate.scanDevices();
                for (Udev.UdevListEntry entry = enumerate.getListEntry(); entry != null; entry = entry.getNext()) {
                    final String syspath = entry.getName();
                    final Udev.UdevDevice device = udev.deviceNewFromSyspath(syspath);
                    if (device != null) {
                        try {
                            final String devnode = device.getDevnode();
                            if (devnode != null && !devnode.startsWith("/dev/loop") && !devnode.startsWith("/dev/ram")) {
                                if ("disk".equals(device.getDevtype())) {
                                    String devModel = device.getPropertyValue("ID_MODEL");
                                    String devSerial = device.getPropertyValue("ID_SERIAL_SHORT");
                                    final long devSize = ParseUtil.parseLongOrDefault(device.getSysattrValue("size"), 0L) * 512L;
                                    if (devnode.startsWith("/dev/dm")) {
                                        devModel = "Logical Volume Group";
                                        devSerial = device.getPropertyValue("DM_UUID");
                                        store = new LinuxHWDiskStore(devnode, devModel, (devSerial == null) ? "unknown" : devSerial, devSize);
                                        final String vgName = device.getPropertyValue("DM_VG_NAME");
                                        final String lvName = device.getPropertyValue("DM_LV_NAME");
                                        store.partitionList.add(new HWPartition(getPartitionNameForDmDevice(vgName, lvName), device.getSysname(), (device.getPropertyValue("ID_FS_TYPE") == null) ? "partition" : device.getPropertyValue("ID_FS_TYPE"), (device.getPropertyValue("ID_FS_UUID") == null) ? "" : device.getPropertyValue("ID_FS_UUID"), ParseUtil.parseLongOrDefault(device.getSysattrValue("size"), 0L) * 512L, ParseUtil.parseIntOrDefault(device.getPropertyValue("MAJOR"), 0), ParseUtil.parseIntOrDefault(device.getPropertyValue("MINOR"), 0), getMountPointOfDmDevice(vgName, lvName)));
                                    }
                                    else {
                                        store = new LinuxHWDiskStore(devnode, (devModel == null) ? "unknown" : devModel, (devSerial == null) ? "unknown" : devSerial, devSize);
                                    }
                                    if (storeToUpdate == null) {
                                        computeDiskStats(store, device.getSysattrValue("stat"));
                                        result.add(store);
                                    }
                                    else if (store.getName().equals(storeToUpdate.getName()) && store.getModel().equals(storeToUpdate.getModel()) && store.getSerial().equals(storeToUpdate.getSerial()) && store.getSize() == storeToUpdate.getSize()) {
                                        computeDiskStats(storeToUpdate, device.getSysattrValue("stat"));
                                        result.add(storeToUpdate);
                                        break;
                                    }
                                }
                                else if (storeToUpdate == null && store != null && "partition".equals(device.getDevtype())) {
                                    final Udev.UdevDevice parent = device.getParentWithSubsystemDevtype("block", "disk");
                                    if (parent != null && store.getName().equals(parent.getDevnode())) {
                                        final String name = device.getDevnode();
                                        store.partitionList.add(new HWPartition(name, device.getSysname(), (device.getPropertyValue("ID_FS_TYPE") == null) ? "partition" : device.getPropertyValue("ID_FS_TYPE"), (device.getPropertyValue("ID_FS_UUID") == null) ? "" : device.getPropertyValue("ID_FS_UUID"), ParseUtil.parseLongOrDefault(device.getSysattrValue("size"), 0L) * 512L, ParseUtil.parseIntOrDefault(device.getPropertyValue("MAJOR"), 0), ParseUtil.parseIntOrDefault(device.getPropertyValue("MINOR"), 0), mountsMap.getOrDefault(name, getDependentNamesFromHoldersDirectory(device.getSysname()))));
                                    }
                                }
                            }
                        }
                        finally {
                            device.unref();
                        }
                    }
                }
            }
            finally {
                enumerate.unref();
            }
        }
        finally {
            udev.unref();
        }
        for (final HWDiskStore hwds : result) {
            ((LinuxHWDiskStore)hwds).partitionList = Collections.unmodifiableList((List<? extends HWPartition>)hwds.getPartitions().stream().sorted(Comparator.comparing((Function<? super Object, ? extends Comparable>)HWPartition::getName)).collect((Collector<? super Object, ?, List<? extends T>>)Collectors.toList()));
        }
        return result;
    }
    
    @Override
    public boolean updateAttributes() {
        return !getDisks(this).isEmpty();
    }
    
    private static Map<String, String> readMountsMap() {
        final Map<String, String> mountsMap = new HashMap<String, String>();
        final List<String> mounts = FileUtil.readFile(ProcPath.MOUNTS);
        for (final String mount : mounts) {
            final String[] split = ParseUtil.whitespaces.split(mount);
            if (split.length >= 2) {
                if (!split[0].startsWith("/dev/")) {
                    continue;
                }
                mountsMap.put(split[0], split[1]);
            }
        }
        return mountsMap;
    }
    
    private static void computeDiskStats(final LinuxHWDiskStore store, final String devstat) {
        final long[] devstatArray = ParseUtil.parseStringToLongArray(devstat, LinuxHWDiskStore.UDEV_STAT_ORDERS, LinuxHWDiskStore.UDEV_STAT_LENGTH, ' ');
        store.timeStamp = System.currentTimeMillis();
        store.reads = devstatArray[UdevStat.READS.ordinal()];
        store.readBytes = devstatArray[UdevStat.READ_BYTES.ordinal()] * 512L;
        store.writes = devstatArray[UdevStat.WRITES.ordinal()];
        store.writeBytes = devstatArray[UdevStat.WRITE_BYTES.ordinal()] * 512L;
        store.currentQueueLength = devstatArray[UdevStat.QUEUE_LENGTH.ordinal()];
        store.transferTime = devstatArray[UdevStat.ACTIVE_MS.ordinal()];
    }
    
    private static String getPartitionNameForDmDevice(final String vgName, final String lvName) {
        return "/dev/" + vgName + '/' + lvName;
    }
    
    private static String getMountPointOfDmDevice(final String vgName, final String lvName) {
        return "/dev/mapper/" + vgName + '-' + lvName;
    }
    
    private static String getDependentNamesFromHoldersDirectory(final String sysPath) {
        final File holdersDir = new File(sysPath + "/holders");
        final File[] holders = holdersDir.listFiles();
        if (holders != null) {
            return Arrays.stream(holders).map((Function<? super File, ?>)File::getName).collect((Collector<? super Object, ?, String>)Collectors.joining(" "));
        }
        return "";
    }
    
    static {
        UDEV_STAT_ORDERS = new int[UdevStat.values().length];
        for (final UdevStat stat : UdevStat.values()) {
            LinuxHWDiskStore.UDEV_STAT_ORDERS[stat.ordinal()] = stat.getOrder();
        }
        final String stat2 = FileUtil.getStringFromFile(ProcPath.DISKSTATS);
        int statLength = 11;
        if (!stat2.isEmpty()) {
            statLength = ParseUtil.countStringToLongArray(stat2, ' ');
        }
        UDEV_STAT_LENGTH = statLength;
    }
    
    enum UdevStat
    {
        READS(0), 
        READ_BYTES(2), 
        WRITES(4), 
        WRITE_BYTES(6), 
        QUEUE_LENGTH(8), 
        ACTIVE_MS(9);
        
        private int order;
        
        public int getOrder() {
            return this.order;
        }
        
        private UdevStat(final int order) {
            this.order = order;
        }
        
        private static /* synthetic */ UdevStat[] $values() {
            return new UdevStat[] { UdevStat.READS, UdevStat.READ_BYTES, UdevStat.WRITES, UdevStat.WRITE_BYTES, UdevStat.QUEUE_LENGTH, UdevStat.ACTIVE_MS };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
