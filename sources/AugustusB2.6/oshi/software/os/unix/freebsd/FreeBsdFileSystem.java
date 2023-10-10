// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.freebsd;

import oshi.util.platform.unix.freebsd.BsdSysctlUtil;
import java.util.Iterator;
import java.util.Map;
import oshi.software.os.linux.LinuxOSFileStore;
import java.io.File;
import oshi.util.FileSystemUtil;
import oshi.util.ParseUtil;
import java.util.ArrayList;
import oshi.util.ExecutingCommand;
import java.util.HashMap;
import oshi.software.os.OSFileStore;
import java.nio.file.PathMatcher;
import java.util.List;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractFileSystem;

@ThreadSafe
public final class FreeBsdFileSystem extends AbstractFileSystem
{
    public static final String OSHI_FREEBSD_FS_PATH_EXCLUDES = "oshi.os.freebsd.filesystem.path.excludes";
    public static final String OSHI_FREEBSD_FS_PATH_INCLUDES = "oshi.os.freebsd.filesystem.path.includes";
    public static final String OSHI_FREEBSD_FS_VOLUME_EXCLUDES = "oshi.os.freebsd.filesystem.volume.excludes";
    public static final String OSHI_FREEBSD_FS_VOLUME_INCLUDES = "oshi.os.freebsd.filesystem.volume.includes";
    private static final List<PathMatcher> FS_PATH_EXCLUDES;
    private static final List<PathMatcher> FS_PATH_INCLUDES;
    private static final List<PathMatcher> FS_VOLUME_EXCLUDES;
    private static final List<PathMatcher> FS_VOLUME_INCLUDES;
    
    @Override
    public List<OSFileStore> getFileStores(final boolean localOnly) {
        final Map<String, String> uuidMap = new HashMap<String, String>();
        String device = "";
        for (String line : ExecutingCommand.runNative("geom part list")) {
            if (line.contains("Name: ")) {
                device = line.substring(line.lastIndexOf(32) + 1);
            }
            if (device.isEmpty()) {
                continue;
            }
            line = line.trim();
            if (!line.startsWith("rawuuid:")) {
                continue;
            }
            uuidMap.put(device, line.substring(line.lastIndexOf(32) + 1));
            device = "";
        }
        final List<OSFileStore> fsList = new ArrayList<OSFileStore>();
        final Map<String, Long> inodeFreeMap = new HashMap<String, Long>();
        final Map<String, Long> inodeTotalMap = new HashMap<String, Long>();
        for (final String line2 : ExecutingCommand.runNative("df -i")) {
            if (line2.startsWith("/")) {
                final String[] split = ParseUtil.whitespaces.split(line2);
                if (split.length <= 7) {
                    continue;
                }
                inodeFreeMap.put(split[0], ParseUtil.parseLongOrDefault(split[6], 0L));
                inodeTotalMap.put(split[0], inodeFreeMap.get(split[0]) + ParseUtil.parseLongOrDefault(split[5], 0L));
            }
        }
        for (final String fs : ExecutingCommand.runNative("mount -p")) {
            final String[] split = ParseUtil.whitespaces.split(fs);
            if (split.length < 5) {
                continue;
            }
            final String volume = split[0];
            final String path = split[1];
            final String type = split[2];
            final String options = split[3];
            if (localOnly && FreeBsdFileSystem.NETWORK_FS_TYPES.contains(type)) {
                continue;
            }
            if (!path.equals("/")) {
                if (FreeBsdFileSystem.PSEUDO_FS_TYPES.contains(type)) {
                    continue;
                }
                if (FileSystemUtil.isFileStoreExcluded(path, volume, FreeBsdFileSystem.FS_PATH_INCLUDES, FreeBsdFileSystem.FS_PATH_EXCLUDES, FreeBsdFileSystem.FS_VOLUME_INCLUDES, FreeBsdFileSystem.FS_VOLUME_EXCLUDES)) {
                    continue;
                }
            }
            String name = path.substring(path.lastIndexOf(47) + 1);
            if (name.isEmpty()) {
                name = volume.substring(volume.lastIndexOf(47) + 1);
            }
            final File f = new File(path);
            final long totalSpace = f.getTotalSpace();
            final long usableSpace = f.getUsableSpace();
            final long freeSpace = f.getFreeSpace();
            String description;
            if (volume.startsWith("/dev") || path.equals("/")) {
                description = "Local Disk";
            }
            else if (volume.equals("tmpfs")) {
                description = "Ram Disk";
            }
            else if (FreeBsdFileSystem.NETWORK_FS_TYPES.contains(type)) {
                description = "Network Disk";
            }
            else {
                description = "Mount Point";
            }
            final String uuid = uuidMap.getOrDefault(name, "");
            fsList.add(new LinuxOSFileStore(name, volume, name, path, options, uuid, "", description, type, freeSpace, usableSpace, totalSpace, inodeFreeMap.containsKey(path) ? inodeFreeMap.get(path) : 0L, inodeTotalMap.containsKey(path) ? inodeTotalMap.get(path) : 0L));
        }
        return fsList;
    }
    
    @Override
    public long getOpenFileDescriptors() {
        return BsdSysctlUtil.sysctl("kern.openfiles", 0);
    }
    
    @Override
    public long getMaxFileDescriptors() {
        return BsdSysctlUtil.sysctl("kern.maxfiles", 0);
    }
    
    static {
        FS_PATH_EXCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.freebsd.filesystem.path.excludes");
        FS_PATH_INCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.freebsd.filesystem.path.includes");
        FS_VOLUME_EXCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.freebsd.filesystem.volume.excludes");
        FS_VOLUME_INCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.freebsd.filesystem.volume.includes");
    }
}
