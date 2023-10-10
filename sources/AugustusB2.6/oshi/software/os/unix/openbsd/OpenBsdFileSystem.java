// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.openbsd;

import oshi.util.platform.unix.openbsd.OpenBsdSysctlUtil;
import java.util.Iterator;
import java.util.Map;
import java.io.File;
import oshi.util.FileSystemUtil;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import java.util.HashMap;
import java.util.ArrayList;
import oshi.software.os.OSFileStore;
import java.nio.file.PathMatcher;
import java.util.List;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractFileSystem;

@ThreadSafe
public class OpenBsdFileSystem extends AbstractFileSystem
{
    public static final String OSHI_OPENBSD_FS_PATH_EXCLUDES = "oshi.os.openbsd.filesystem.path.excludes";
    public static final String OSHI_OPENBSD_FS_PATH_INCLUDES = "oshi.os.openbsd.filesystem.path.includes";
    public static final String OSHI_OPENBSD_FS_VOLUME_EXCLUDES = "oshi.os.openbsd.filesystem.volume.excludes";
    public static final String OSHI_OPENBSD_FS_VOLUME_INCLUDES = "oshi.os.openbsd.filesystem.volume.includes";
    private static final List<PathMatcher> FS_PATH_EXCLUDES;
    private static final List<PathMatcher> FS_PATH_INCLUDES;
    private static final List<PathMatcher> FS_VOLUME_EXCLUDES;
    private static final List<PathMatcher> FS_VOLUME_INCLUDES;
    
    @Override
    public List<OSFileStore> getFileStores(final boolean localOnly) {
        return getFileStoreMatching(null, localOnly);
    }
    
    static List<OSFileStore> getFileStoreMatching(final String nameToMatch) {
        return getFileStoreMatching(nameToMatch, false);
    }
    
    private static List<OSFileStore> getFileStoreMatching(final String nameToMatch, final boolean localOnly) {
        final List<OSFileStore> fsList = new ArrayList<OSFileStore>();
        final Map<String, Long> inodeFreeMap = new HashMap<String, Long>();
        final Map<String, Long> inodeUsedlMap = new HashMap<String, Long>();
        final String command = "df -i" + (localOnly ? " -l" : "");
        for (final String line : ExecutingCommand.runNative(command)) {
            if (line.startsWith("/")) {
                final String[] split = ParseUtil.whitespaces.split(line);
                if (split.length <= 6) {
                    continue;
                }
                inodeUsedlMap.put(split[0], ParseUtil.parseLongOrDefault(split[5], 0L));
                inodeFreeMap.put(split[0], ParseUtil.parseLongOrDefault(split[6], 0L));
            }
        }
        for (final String fs : ExecutingCommand.runNative("mount -v")) {
            final String[] split = ParseUtil.whitespaces.split(fs, 7);
            if (split.length == 7) {
                final String volume = split[0];
                final String uuid = split[1];
                final String path = split[3];
                final String type = split[5];
                final String options = split[6];
                if (localOnly && OpenBsdFileSystem.NETWORK_FS_TYPES.contains(type)) {
                    continue;
                }
                if (!path.equals("/")) {
                    if (OpenBsdFileSystem.PSEUDO_FS_TYPES.contains(type)) {
                        continue;
                    }
                    if (FileSystemUtil.isFileStoreExcluded(path, volume, OpenBsdFileSystem.FS_PATH_INCLUDES, OpenBsdFileSystem.FS_PATH_EXCLUDES, OpenBsdFileSystem.FS_VOLUME_INCLUDES, OpenBsdFileSystem.FS_VOLUME_EXCLUDES)) {
                        continue;
                    }
                }
                String name = path.substring(path.lastIndexOf(47) + 1);
                if (name.isEmpty()) {
                    name = volume.substring(volume.lastIndexOf(47) + 1);
                }
                if (nameToMatch != null && !nameToMatch.equals(name)) {
                    continue;
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
                    description = "Ram Disk (dynamic)";
                }
                else if (volume.equals("mfs")) {
                    description = "Ram Disk (fixed)";
                }
                else if (OpenBsdFileSystem.NETWORK_FS_TYPES.contains(type)) {
                    description = "Network Disk";
                }
                else {
                    description = "Mount Point";
                }
                fsList.add(new OpenBsdOSFileStore(name, volume, name, path, options, uuid, "", description, type, freeSpace, usableSpace, totalSpace, inodeFreeMap.getOrDefault(volume, 0L), inodeUsedlMap.getOrDefault(volume, 0L) + inodeFreeMap.getOrDefault(volume, 0L)));
            }
        }
        return fsList;
    }
    
    @Override
    public long getOpenFileDescriptors() {
        return OpenBsdSysctlUtil.sysctl("kern.nfiles", 0);
    }
    
    @Override
    public long getMaxFileDescriptors() {
        return OpenBsdSysctlUtil.sysctl("kern.maxfiles", 0);
    }
    
    static {
        FS_PATH_EXCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.openbsd.filesystem.path.excludes");
        FS_PATH_INCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.openbsd.filesystem.path.includes");
        FS_VOLUME_EXCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.openbsd.filesystem.volume.excludes");
        FS_VOLUME_INCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.openbsd.filesystem.volume.includes");
    }
}
