// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.mac;

import java.util.HashMap;
import org.slf4j.LoggerFactory;
import oshi.util.platform.mac.SysctlUtil;
import com.sun.jna.platform.mac.IOKit;
import com.sun.jna.Pointer;
import com.sun.jna.platform.mac.IOKitUtil;
import oshi.util.platform.mac.CFUtil;
import com.sun.jna.PointerType;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.io.File;
import oshi.util.FileSystemUtil;
import com.sun.jna.Native;
import java.nio.charset.StandardCharsets;
import com.sun.jna.platform.mac.CoreFoundation;
import com.sun.jna.platform.mac.DiskArbitration;
import com.sun.jna.platform.mac.SystemB;
import java.util.ArrayList;
import oshi.software.os.OSFileStore;
import java.util.Map;
import java.util.regex.Pattern;
import java.nio.file.PathMatcher;
import java.util.List;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractFileSystem;

@ThreadSafe
public class MacFileSystem extends AbstractFileSystem
{
    private static final Logger LOG;
    public static final String OSHI_MAC_FS_PATH_EXCLUDES = "oshi.os.mac.filesystem.path.excludes";
    public static final String OSHI_MAC_FS_PATH_INCLUDES = "oshi.os.mac.filesystem.path.includes";
    public static final String OSHI_MAC_FS_VOLUME_EXCLUDES = "oshi.os.mac.filesystem.volume.excludes";
    public static final String OSHI_MAC_FS_VOLUME_INCLUDES = "oshi.os.mac.filesystem.volume.includes";
    private static final List<PathMatcher> FS_PATH_EXCLUDES;
    private static final List<PathMatcher> FS_PATH_INCLUDES;
    private static final List<PathMatcher> FS_VOLUME_EXCLUDES;
    private static final List<PathMatcher> FS_VOLUME_INCLUDES;
    private static final Pattern LOCAL_DISK;
    private static final int MNT_RDONLY = 1;
    private static final int MNT_SYNCHRONOUS = 2;
    private static final int MNT_NOEXEC = 4;
    private static final int MNT_NOSUID = 8;
    private static final int MNT_NODEV = 16;
    private static final int MNT_UNION = 32;
    private static final int MNT_ASYNC = 64;
    private static final int MNT_CPROTECT = 128;
    private static final int MNT_EXPORTED = 256;
    private static final int MNT_QUARANTINE = 1024;
    private static final int MNT_LOCAL = 4096;
    private static final int MNT_QUOTA = 8192;
    private static final int MNT_ROOTFS = 16384;
    private static final int MNT_DOVOLFS = 32768;
    private static final int MNT_DONTBROWSE = 1048576;
    private static final int MNT_IGNORE_OWNERSHIP = 2097152;
    private static final int MNT_AUTOMOUNTED = 4194304;
    private static final int MNT_JOURNALED = 8388608;
    private static final int MNT_NOUSERXATTR = 16777216;
    private static final int MNT_DEFWRITE = 33554432;
    private static final int MNT_MULTILABEL = 67108864;
    private static final int MNT_NOATIME = 268435456;
    private static final Map<Integer, String> OPTIONS_MAP;
    
    @Override
    public List<OSFileStore> getFileStores(final boolean localOnly) {
        return getFileStoreMatching(null, localOnly);
    }
    
    static List<OSFileStore> getFileStoreMatching(final String nameToMatch) {
        return getFileStoreMatching(nameToMatch, false);
    }
    
    private static List<OSFileStore> getFileStoreMatching(final String nameToMatch, final boolean localOnly) {
        final List<OSFileStore> fsList = new ArrayList<OSFileStore>();
        int numfs = SystemB.INSTANCE.getfsstat64(null, 0, 0);
        if (numfs > 0) {
            final DiskArbitration.DASessionRef session = DiskArbitration.INSTANCE.DASessionCreate(CoreFoundation.INSTANCE.CFAllocatorGetDefault());
            if (session == null) {
                MacFileSystem.LOG.error("Unable to open session to DiskArbitration framework.");
            }
            else {
                final CoreFoundation.CFStringRef daVolumeNameKey = CoreFoundation.CFStringRef.createCFString("DAVolumeName");
                final SystemB.Statfs[] fs = new SystemB.Statfs[numfs];
                numfs = SystemB.INSTANCE.getfsstat64(fs, numfs * new SystemB.Statfs().size(), 16);
                for (int f = 0; f < numfs; ++f) {
                    final String volume = Native.toString(fs[f].f_mntfromname, StandardCharsets.UTF_8);
                    final String path = Native.toString(fs[f].f_mntonname, StandardCharsets.UTF_8);
                    final String type = Native.toString(fs[f].f_fstypename, StandardCharsets.UTF_8);
                    final int flags = fs[f].f_flags;
                    if (!localOnly || (flags & 0x1000) != 0x0) {
                        if (!path.equals("/")) {
                            if (MacFileSystem.PSEUDO_FS_TYPES.contains(type)) {
                                continue;
                            }
                            if (FileSystemUtil.isFileStoreExcluded(path, volume, MacFileSystem.FS_PATH_INCLUDES, MacFileSystem.FS_PATH_EXCLUDES, MacFileSystem.FS_VOLUME_INCLUDES, MacFileSystem.FS_VOLUME_EXCLUDES)) {
                                continue;
                            }
                        }
                        String description = "Volume";
                        if (MacFileSystem.LOCAL_DISK.matcher(volume).matches()) {
                            description = "Local Disk";
                        }
                        else if (volume.startsWith("localhost:") || volume.startsWith("//") || volume.startsWith("smb://") || MacFileSystem.NETWORK_FS_TYPES.contains(type)) {
                            description = "Network Drive";
                        }
                        final File file = new File(path);
                        String name = file.getName();
                        if (name.isEmpty()) {
                            name = file.getPath();
                        }
                        if (nameToMatch == null || nameToMatch.equals(name)) {
                            final StringBuilder options = new StringBuilder(((0x1 & flags) == 0x0) ? "rw" : "ro");
                            final String moreOptions = MacFileSystem.OPTIONS_MAP.entrySet().stream().filter(e -> (e.getKey() & flags) > 0).map((Function<? super Object, ?>)Map.Entry::getValue).collect((Collector<? super Object, ?, String>)Collectors.joining(","));
                            if (!moreOptions.isEmpty()) {
                                options.append(',').append(moreOptions);
                            }
                            String uuid = "";
                            final String bsdName = volume.replace("/dev/disk", "disk");
                            if (bsdName.startsWith("disk")) {
                                final DiskArbitration.DADiskRef disk = DiskArbitration.INSTANCE.DADiskCreateFromBSDName(CoreFoundation.INSTANCE.CFAllocatorGetDefault(), session, volume);
                                if (disk != null) {
                                    final CoreFoundation.CFDictionaryRef diskInfo = DiskArbitration.INSTANCE.DADiskCopyDescription(disk);
                                    if (diskInfo != null) {
                                        final Pointer result = diskInfo.getValue(daVolumeNameKey);
                                        name = CFUtil.cfPointerToString(result);
                                        diskInfo.release();
                                    }
                                    disk.release();
                                }
                                final CoreFoundation.CFMutableDictionaryRef matchingDict = IOKitUtil.getBSDNameMatchingDict(bsdName);
                                if (matchingDict != null) {
                                    final IOKit.IOIterator fsIter = IOKitUtil.getMatchingServices(matchingDict);
                                    if (fsIter != null) {
                                        final IOKit.IORegistryEntry fsEntry = fsIter.next();
                                        if (fsEntry != null && fsEntry.conformsTo("IOMedia")) {
                                            uuid = fsEntry.getStringProperty("UUID");
                                            if (uuid != null) {
                                                uuid = uuid.toLowerCase();
                                            }
                                            fsEntry.release();
                                        }
                                        fsIter.release();
                                    }
                                }
                            }
                            fsList.add(new MacOSFileStore(name, volume, name, path, options.toString(), (uuid == null) ? "" : uuid, "", description, type, file.getFreeSpace(), file.getUsableSpace(), file.getTotalSpace(), fs[f].f_ffree, fs[f].f_files));
                        }
                    }
                }
                daVolumeNameKey.release();
                session.release();
            }
        }
        return fsList;
    }
    
    @Override
    public long getOpenFileDescriptors() {
        return SysctlUtil.sysctl("kern.num_files", 0);
    }
    
    @Override
    public long getMaxFileDescriptors() {
        return SysctlUtil.sysctl("kern.maxfiles", 0);
    }
    
    static {
        LOG = LoggerFactory.getLogger(MacFileSystem.class);
        FS_PATH_EXCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.mac.filesystem.path.excludes");
        FS_PATH_INCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.mac.filesystem.path.includes");
        FS_VOLUME_EXCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.mac.filesystem.volume.excludes");
        FS_VOLUME_INCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.mac.filesystem.volume.includes");
        LOCAL_DISK = Pattern.compile("/dev/disk\\d");
        (OPTIONS_MAP = new HashMap<Integer, String>()).put(2, "synchronous");
        MacFileSystem.OPTIONS_MAP.put(4, "noexec");
        MacFileSystem.OPTIONS_MAP.put(8, "nosuid");
        MacFileSystem.OPTIONS_MAP.put(16, "nodev");
        MacFileSystem.OPTIONS_MAP.put(32, "union");
        MacFileSystem.OPTIONS_MAP.put(64, "asynchronous");
        MacFileSystem.OPTIONS_MAP.put(128, "content-protection");
        MacFileSystem.OPTIONS_MAP.put(256, "exported");
        MacFileSystem.OPTIONS_MAP.put(1024, "quarantined");
        MacFileSystem.OPTIONS_MAP.put(4096, "local");
        MacFileSystem.OPTIONS_MAP.put(8192, "quotas");
        MacFileSystem.OPTIONS_MAP.put(16384, "rootfs");
        MacFileSystem.OPTIONS_MAP.put(32768, "volfs");
        MacFileSystem.OPTIONS_MAP.put(1048576, "nobrowse");
        MacFileSystem.OPTIONS_MAP.put(2097152, "noowners");
        MacFileSystem.OPTIONS_MAP.put(4194304, "automounted");
        MacFileSystem.OPTIONS_MAP.put(8388608, "journaled");
        MacFileSystem.OPTIONS_MAP.put(16777216, "nouserxattr");
        MacFileSystem.OPTIONS_MAP.put(33554432, "defwrite");
        MacFileSystem.OPTIONS_MAP.put(67108864, "multilabel");
        MacFileSystem.OPTIONS_MAP.put(268435456, "noatime");
    }
}
