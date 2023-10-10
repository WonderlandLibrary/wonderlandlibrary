// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.aix;

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
public class AixFileSystem extends AbstractFileSystem
{
    public static final String OSHI_AIX_FS_PATH_EXCLUDES = "oshi.os.aix.filesystem.path.excludes";
    public static final String OSHI_AIX_FS_PATH_INCLUDES = "oshi.os.aix.filesystem.path.includes";
    public static final String OSHI_AIX_FS_VOLUME_EXCLUDES = "oshi.os.aix.filesystem.volume.excludes";
    public static final String OSHI_AIX_FS_VOLUME_INCLUDES = "oshi.os.aix.filesystem.volume.includes";
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
        final Map<String, Long> inodeTotalMap = new HashMap<String, Long>();
        final String command = "df -i" + (localOnly ? " -l" : "");
        for (final String line : ExecutingCommand.runNative(command)) {
            if (line.startsWith("/")) {
                final String[] split = ParseUtil.whitespaces.split(line);
                if (split.length <= 5) {
                    continue;
                }
                inodeTotalMap.put(split[0], ParseUtil.parseLongOrDefault(split[1], 0L));
                inodeFreeMap.put(split[0], ParseUtil.parseLongOrDefault(split[3], 0L));
            }
        }
        for (final String fs : ExecutingCommand.runNative("mount")) {
            final String[] split = ParseUtil.whitespaces.split("x" + fs);
            if (split.length > 7) {
                final String volume = split[1];
                final String path = split[2];
                final String type = split[3];
                final String options = split[4];
                if (localOnly && AixFileSystem.NETWORK_FS_TYPES.contains(type)) {
                    continue;
                }
                if (!path.equals("/")) {
                    if (AixFileSystem.PSEUDO_FS_TYPES.contains(type)) {
                        continue;
                    }
                    if (FileSystemUtil.isFileStoreExcluded(path, volume, AixFileSystem.FS_PATH_INCLUDES, AixFileSystem.FS_PATH_EXCLUDES, AixFileSystem.FS_VOLUME_INCLUDES, AixFileSystem.FS_VOLUME_EXCLUDES)) {
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
                    description = "Ram Disk";
                }
                else if (AixFileSystem.NETWORK_FS_TYPES.contains(type)) {
                    description = "Network Disk";
                }
                else {
                    description = "Mount Point";
                }
                fsList.add(new AixOSFileStore(name, volume, name, path, options, "", "", description, type, freeSpace, usableSpace, totalSpace, inodeFreeMap.getOrDefault(volume, 0L), inodeTotalMap.getOrDefault(volume, 0L)));
            }
        }
        return fsList;
    }
    
    @Override
    public long getOpenFileDescriptors() {
        boolean header = false;
        long openfiles = 0L;
        for (final String f : ExecutingCommand.runNative("lsof -nl")) {
            if (!header) {
                header = f.startsWith("COMMAND");
            }
            else {
                ++openfiles;
            }
        }
        return openfiles;
    }
    
    @Override
    public long getMaxFileDescriptors() {
        return ParseUtil.parseLongOrDefault(ExecutingCommand.getFirstAnswer("ulimit -n"), 0L);
    }
    
    static {
        FS_PATH_EXCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.aix.filesystem.path.excludes");
        FS_PATH_INCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.aix.filesystem.path.includes");
        FS_VOLUME_EXCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.aix.filesystem.volume.excludes");
        FS_VOLUME_INCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.aix.filesystem.volume.includes");
    }
}
