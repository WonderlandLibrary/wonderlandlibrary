// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.mac;

import org.slf4j.LoggerFactory;
import java.util.EnumMap;
import oshi.hardware.HWDiskStore;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import oshi.util.platform.mac.CFUtil;
import com.sun.jna.platform.mac.IOKit;
import com.sun.jna.Pointer;
import java.util.ArrayList;
import com.sun.jna.PointerType;
import com.sun.jna.platform.mac.IOKitUtil;
import java.util.Iterator;
import oshi.driver.mac.disk.Fsstat;
import java.util.Map;
import oshi.hardware.HWPartition;
import java.util.List;
import org.slf4j.Logger;
import com.sun.jna.platform.mac.DiskArbitration;
import com.sun.jna.platform.mac.CoreFoundation;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractHWDiskStore;

@ThreadSafe
public final class MacHWDiskStore extends AbstractHWDiskStore
{
    private static final CoreFoundation CF;
    private static final DiskArbitration DA;
    private static final Logger LOG;
    private long reads;
    private long readBytes;
    private long writes;
    private long writeBytes;
    private long currentQueueLength;
    private long transferTime;
    private long timeStamp;
    private List<HWPartition> partitionList;
    
    private MacHWDiskStore(final String name, final String model, final String serial, final long size, final DiskArbitration.DASessionRef session, final Map<String, String> mountPointMap, final Map<CFKey, CoreFoundation.CFStringRef> cfKeyMap) {
        super(name, model, serial, size);
        this.reads = 0L;
        this.readBytes = 0L;
        this.writes = 0L;
        this.writeBytes = 0L;
        this.currentQueueLength = 0L;
        this.transferTime = 0L;
        this.timeStamp = 0L;
        this.updateDiskStats(session, mountPointMap, cfKeyMap);
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
        final DiskArbitration.DASessionRef session = MacHWDiskStore.DA.DASessionCreate(MacHWDiskStore.CF.CFAllocatorGetDefault());
        if (session == null) {
            MacHWDiskStore.LOG.error("Unable to open session to DiskArbitration framework.");
            return false;
        }
        final Map<CFKey, CoreFoundation.CFStringRef> cfKeyMap = mapCFKeys();
        final boolean diskFound = this.updateDiskStats(session, Fsstat.queryPartitionToMountMap(), cfKeyMap);
        session.release();
        for (final CoreFoundation.CFTypeRef value : cfKeyMap.values()) {
            value.release();
        }
        return diskFound;
    }
    
    private boolean updateDiskStats(final DiskArbitration.DASessionRef session, final Map<String, String> mountPointMap, final Map<CFKey, CoreFoundation.CFStringRef> cfKeyMap) {
        final String bsdName = this.getName();
        CoreFoundation.CFMutableDictionaryRef matchingDict = IOKitUtil.getBSDNameMatchingDict(bsdName);
        if (matchingDict != null) {
            final IOKit.IOIterator driveListIter = IOKitUtil.getMatchingServices(matchingDict);
            if (driveListIter != null) {
                final IOKit.IORegistryEntry drive = driveListIter.next();
                if (drive != null) {
                    if (drive.conformsTo("IOMedia")) {
                        final IOKit.IORegistryEntry parent = drive.getParentEntry("IOService");
                        if (parent != null && (parent.conformsTo("IOBlockStorageDriver") || parent.conformsTo("AppleAPFSContainerScheme"))) {
                            final CoreFoundation.CFMutableDictionaryRef properties = parent.createCFProperties();
                            Pointer result = properties.getValue(cfKeyMap.get(CFKey.STATISTICS));
                            final CoreFoundation.CFDictionaryRef statistics = new CoreFoundation.CFDictionaryRef(result);
                            this.timeStamp = System.currentTimeMillis();
                            result = statistics.getValue(cfKeyMap.get(CFKey.READ_OPS));
                            final CoreFoundation.CFNumberRef stat = new CoreFoundation.CFNumberRef(result);
                            this.reads = stat.longValue();
                            result = statistics.getValue(cfKeyMap.get(CFKey.READ_BYTES));
                            stat.setPointer(result);
                            this.readBytes = stat.longValue();
                            result = statistics.getValue(cfKeyMap.get(CFKey.WRITE_OPS));
                            stat.setPointer(result);
                            this.writes = stat.longValue();
                            result = statistics.getValue(cfKeyMap.get(CFKey.WRITE_BYTES));
                            stat.setPointer(result);
                            this.writeBytes = stat.longValue();
                            final Pointer readTimeResult = statistics.getValue(cfKeyMap.get(CFKey.READ_TIME));
                            final Pointer writeTimeResult = statistics.getValue(cfKeyMap.get(CFKey.WRITE_TIME));
                            if (readTimeResult != null && writeTimeResult != null) {
                                stat.setPointer(readTimeResult);
                                long xferTime = stat.longValue();
                                stat.setPointer(writeTimeResult);
                                xferTime += stat.longValue();
                                this.transferTime = xferTime / 1000000L;
                            }
                            properties.release();
                        }
                        else {
                            MacHWDiskStore.LOG.debug("Unable to find block storage driver properties for {}", bsdName);
                        }
                        final List<HWPartition> partitions = new ArrayList<HWPartition>();
                        final CoreFoundation.CFMutableDictionaryRef properties2 = drive.createCFProperties();
                        Pointer result2 = properties2.getValue(cfKeyMap.get(CFKey.BSD_UNIT));
                        final CoreFoundation.CFNumberRef bsdUnit = new CoreFoundation.CFNumberRef(result2);
                        result2 = properties2.getValue(cfKeyMap.get(CFKey.LEAF));
                        final CoreFoundation.CFBooleanRef cfFalse = new CoreFoundation.CFBooleanRef(result2);
                        final CoreFoundation.CFMutableDictionaryRef propertyDict = MacHWDiskStore.CF.CFDictionaryCreateMutable(MacHWDiskStore.CF.CFAllocatorGetDefault(), new CoreFoundation.CFIndex(0L), null, null);
                        propertyDict.setValue(cfKeyMap.get(CFKey.BSD_UNIT), bsdUnit);
                        propertyDict.setValue(cfKeyMap.get(CFKey.WHOLE), cfFalse);
                        matchingDict = MacHWDiskStore.CF.CFDictionaryCreateMutable(MacHWDiskStore.CF.CFAllocatorGetDefault(), new CoreFoundation.CFIndex(0L), null, null);
                        matchingDict.setValue(cfKeyMap.get(CFKey.IO_PROPERTY_MATCH), propertyDict);
                        final IOKit.IOIterator serviceIterator = IOKitUtil.getMatchingServices(matchingDict);
                        properties2.release();
                        propertyDict.release();
                        if (serviceIterator != null) {
                            for (IOKit.IORegistryEntry sdService = IOKit.INSTANCE.IOIteratorNext(serviceIterator); sdService != null; sdService = IOKit.INSTANCE.IOIteratorNext(serviceIterator)) {
                                String name;
                                final String partBsdName = name = sdService.getStringProperty("BSD Name");
                                String type = "";
                                final DiskArbitration.DADiskRef disk = MacHWDiskStore.DA.DADiskCreateFromBSDName(MacHWDiskStore.CF.CFAllocatorGetDefault(), session, partBsdName);
                                if (disk != null) {
                                    final CoreFoundation.CFDictionaryRef diskInfo = MacHWDiskStore.DA.DADiskCopyDescription(disk);
                                    if (diskInfo != null) {
                                        result2 = diskInfo.getValue(cfKeyMap.get(CFKey.DA_MEDIA_NAME));
                                        type = CFUtil.cfPointerToString(result2);
                                        result2 = diskInfo.getValue(cfKeyMap.get(CFKey.DA_VOLUME_NAME));
                                        if (result2 == null) {
                                            name = type;
                                        }
                                        else {
                                            name = CFUtil.cfPointerToString(result2);
                                        }
                                        diskInfo.release();
                                    }
                                    disk.release();
                                }
                                final String mountPoint = mountPointMap.getOrDefault(partBsdName, "");
                                final Long size = sdService.getLongProperty("Size");
                                final Integer bsdMajor = sdService.getIntegerProperty("BSD Major");
                                final Integer bsdMinor = sdService.getIntegerProperty("BSD Minor");
                                final String uuid = sdService.getStringProperty("UUID");
                                partitions.add(new HWPartition(partBsdName, name, type, (uuid == null) ? "unknown" : uuid, (size == null) ? 0L : size, (bsdMajor == null) ? 0 : bsdMajor, (bsdMinor == null) ? 0 : bsdMinor, mountPoint));
                                sdService.release();
                            }
                            serviceIterator.release();
                        }
                        this.partitionList = Collections.unmodifiableList((List<? extends HWPartition>)partitions.stream().sorted(Comparator.comparing((Function<? super Object, ? extends Comparable>)HWPartition::getName)).collect((Collector<? super Object, ?, List<? extends T>>)Collectors.toList()));
                        if (parent != null) {
                            parent.release();
                        }
                    }
                    else {
                        MacHWDiskStore.LOG.error("Unable to find IOMedia device or parent for {}", bsdName);
                    }
                    drive.release();
                }
                driveListIter.release();
                return true;
            }
        }
        return false;
    }
    
    public static List<HWDiskStore> getDisks() {
        final Map<String, String> mountPointMap = Fsstat.queryPartitionToMountMap();
        final Map<CFKey, CoreFoundation.CFStringRef> cfKeyMap = mapCFKeys();
        final List<HWDiskStore> diskList = new ArrayList<HWDiskStore>();
        final DiskArbitration.DASessionRef session = MacHWDiskStore.DA.DASessionCreate(MacHWDiskStore.CF.CFAllocatorGetDefault());
        if (session == null) {
            MacHWDiskStore.LOG.error("Unable to open session to DiskArbitration framework.");
            return Collections.emptyList();
        }
        final List<String> bsdNames = new ArrayList<String>();
        final IOKit.IOIterator iter = IOKitUtil.getMatchingServices("IOMedia");
        if (iter != null) {
            for (IOKit.IORegistryEntry media = iter.next(); media != null; media = iter.next()) {
                final Boolean whole = media.getBooleanProperty("Whole");
                if (whole != null && whole) {
                    final DiskArbitration.DADiskRef disk = MacHWDiskStore.DA.DADiskCreateFromIOMedia(MacHWDiskStore.CF.CFAllocatorGetDefault(), session, media);
                    bsdNames.add(MacHWDiskStore.DA.DADiskGetBSDName(disk));
                    disk.release();
                }
                media.release();
            }
            iter.release();
        }
        for (final String bsdName : bsdNames) {
            String model = "";
            String serial = "";
            long size = 0L;
            final String path = "/dev/" + bsdName;
            final DiskArbitration.DADiskRef disk2 = MacHWDiskStore.DA.DADiskCreateFromBSDName(MacHWDiskStore.CF.CFAllocatorGetDefault(), session, path);
            if (disk2 != null) {
                final CoreFoundation.CFDictionaryRef diskInfo = MacHWDiskStore.DA.DADiskCopyDescription(disk2);
                if (diskInfo != null) {
                    Pointer result = diskInfo.getValue(cfKeyMap.get(CFKey.DA_DEVICE_MODEL));
                    model = CFUtil.cfPointerToString(result);
                    result = diskInfo.getValue(cfKeyMap.get(CFKey.DA_MEDIA_SIZE));
                    final CoreFoundation.CFNumberRef sizePtr = new CoreFoundation.CFNumberRef(result);
                    size = sizePtr.longValue();
                    diskInfo.release();
                    if (!"Disk Image".equals(model)) {
                        final CoreFoundation.CFStringRef modelNameRef = CoreFoundation.CFStringRef.createCFString(model);
                        final CoreFoundation.CFMutableDictionaryRef propertyDict = MacHWDiskStore.CF.CFDictionaryCreateMutable(MacHWDiskStore.CF.CFAllocatorGetDefault(), new CoreFoundation.CFIndex(0L), null, null);
                        propertyDict.setValue(cfKeyMap.get(CFKey.MODEL), modelNameRef);
                        final CoreFoundation.CFMutableDictionaryRef matchingDict = MacHWDiskStore.CF.CFDictionaryCreateMutable(MacHWDiskStore.CF.CFAllocatorGetDefault(), new CoreFoundation.CFIndex(0L), null, null);
                        matchingDict.setValue(cfKeyMap.get(CFKey.IO_PROPERTY_MATCH), propertyDict);
                        final IOKit.IOIterator serviceIterator = IOKitUtil.getMatchingServices(matchingDict);
                        modelNameRef.release();
                        propertyDict.release();
                        if (serviceIterator != null) {
                            for (IOKit.IORegistryEntry sdService = serviceIterator.next(); sdService != null; sdService = serviceIterator.next()) {
                                serial = sdService.getStringProperty("Serial Number");
                                sdService.release();
                                if (serial != null) {
                                    break;
                                }
                                sdService.release();
                            }
                            serviceIterator.release();
                        }
                        if (serial == null) {
                            serial = "";
                        }
                    }
                }
                disk2.release();
                if (size <= 0L) {
                    continue;
                }
                final HWDiskStore diskStore = new MacHWDiskStore(bsdName, model.trim(), serial.trim(), size, session, mountPointMap, cfKeyMap);
                diskList.add(diskStore);
            }
        }
        session.release();
        for (final CoreFoundation.CFTypeRef value : cfKeyMap.values()) {
            value.release();
        }
        return diskList;
    }
    
    private static Map<CFKey, CoreFoundation.CFStringRef> mapCFKeys() {
        final Map<CFKey, CoreFoundation.CFStringRef> keyMap = new EnumMap<CFKey, CoreFoundation.CFStringRef>(CFKey.class);
        for (final CFKey cfKey : CFKey.values()) {
            keyMap.put(cfKey, CoreFoundation.CFStringRef.createCFString(cfKey.getKey()));
        }
        return keyMap;
    }
    
    static {
        CF = CoreFoundation.INSTANCE;
        DA = DiskArbitration.INSTANCE;
        LOG = LoggerFactory.getLogger(MacHWDiskStore.class);
    }
    
    private enum CFKey
    {
        IO_PROPERTY_MATCH("IOPropertyMatch"), 
        STATISTICS("Statistics"), 
        READ_OPS("Operations (Read)"), 
        READ_BYTES("Bytes (Read)"), 
        READ_TIME("Total Time (Read)"), 
        WRITE_OPS("Operations (Write)"), 
        WRITE_BYTES("Bytes (Write)"), 
        WRITE_TIME("Total Time (Write)"), 
        BSD_UNIT("BSD Unit"), 
        LEAF("Leaf"), 
        WHOLE("Whole"), 
        DA_MEDIA_NAME("DAMediaName"), 
        DA_VOLUME_NAME("DAVolumeName"), 
        DA_MEDIA_SIZE("DAMediaSize"), 
        DA_DEVICE_MODEL("DADeviceModel"), 
        MODEL("Model");
        
        private final String key;
        
        private CFKey(final String key) {
            this.key = key;
        }
        
        public String getKey() {
            return this.key;
        }
        
        private static /* synthetic */ CFKey[] $values() {
            return new CFKey[] { CFKey.IO_PROPERTY_MATCH, CFKey.STATISTICS, CFKey.READ_OPS, CFKey.READ_BYTES, CFKey.READ_TIME, CFKey.WRITE_OPS, CFKey.WRITE_BYTES, CFKey.WRITE_TIME, CFKey.BSD_UNIT, CFKey.LEAF, CFKey.WHOLE, CFKey.DA_MEDIA_NAME, CFKey.DA_VOLUME_NAME, CFKey.DA_MEDIA_SIZE, CFKey.DA_DEVICE_MODEL, CFKey.MODEL };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
