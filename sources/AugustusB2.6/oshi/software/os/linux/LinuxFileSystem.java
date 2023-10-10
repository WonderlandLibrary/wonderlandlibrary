// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.linux;

import org.slf4j.LoggerFactory;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import java.nio.file.Path;
import java.util.Iterator;
import com.sun.jna.Native;
import com.sun.jna.platform.linux.LibC;
import java.nio.file.Files;
import java.nio.file.Paths;
import oshi.util.FileSystemUtil;
import oshi.util.FileUtil;
import oshi.util.platform.linux.ProcPath;
import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import oshi.software.os.OSFileStore;
import java.nio.file.PathMatcher;
import java.util.List;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractFileSystem;

@ThreadSafe
public class LinuxFileSystem extends AbstractFileSystem
{
    private static final Logger LOG;
    public static final String OSHI_LINUX_FS_PATH_EXCLUDES = "oshi.os.linux.filesystem.path.excludes";
    public static final String OSHI_LINUX_FS_PATH_INCLUDES = "oshi.os.linux.filesystem.path.includes";
    public static final String OSHI_LINUX_FS_VOLUME_EXCLUDES = "oshi.os.linux.filesystem.volume.excludes";
    public static final String OSHI_LINUX_FS_VOLUME_INCLUDES = "oshi.os.linux.filesystem.volume.includes";
    private static final List<PathMatcher> FS_PATH_EXCLUDES;
    private static final List<PathMatcher> FS_PATH_INCLUDES;
    private static final List<PathMatcher> FS_VOLUME_EXCLUDES;
    private static final List<PathMatcher> FS_VOLUME_INCLUDES;
    private static final String UNICODE_SPACE = "\\040";
    
    @Override
    public List<OSFileStore> getFileStores(final boolean localOnly) {
        final Map<String, String> volumeDeviceMap = new HashMap<String, String>();
        final File devMapper = new File("/dev/mapper");
        final File[] volumes = devMapper.listFiles();
        if (volumes != null) {
            for (final File volume : volumes) {
                try {
                    volumeDeviceMap.put(volume.getCanonicalPath(), volume.getAbsolutePath());
                }
                catch (IOException e) {
                    LinuxFileSystem.LOG.error("Couldn't get canonical path for {}. {}", volume.getName(), e.getMessage());
                }
            }
        }
        final Map<String, String> uuidMap = new HashMap<String, String>();
        final File uuidDir = new File("/dev/disk/by-uuid");
        final File[] uuids = uuidDir.listFiles();
        if (uuids != null) {
            for (final File uuid : uuids) {
                try {
                    final String canonicalPath = uuid.getCanonicalPath();
                    uuidMap.put(canonicalPath, uuid.getName().toLowerCase());
                    if (volumeDeviceMap.containsKey(canonicalPath)) {
                        uuidMap.put(volumeDeviceMap.get(canonicalPath), uuid.getName().toLowerCase());
                    }
                }
                catch (IOException e2) {
                    LinuxFileSystem.LOG.error("Couldn't get canonical path for {}. {}", uuid.getName(), e2.getMessage());
                }
            }
        }
        return getFileStoreMatching(null, uuidMap, localOnly);
    }
    
    static List<OSFileStore> getFileStoreMatching(final String nameToMatch, final Map<String, String> uuidMap) {
        return getFileStoreMatching(nameToMatch, uuidMap, false);
    }
    
    private static List<OSFileStore> getFileStoreMatching(final String nameToMatch, final Map<String, String> uuidMap, final boolean localOnly) {
        final List<OSFileStore> fsList = new ArrayList<OSFileStore>();
        final Map<String, String> labelMap = queryLabelMap();
        final List<String> mounts = FileUtil.readFile(ProcPath.MOUNTS);
        for (final String mount : mounts) {
            final String[] split = mount.split(" ");
            if (split.length < 6) {
                continue;
            }
            String name;
            final String volume = name = split[0].replace("\\040", " ");
            final String path = split[1].replace("\\040", " ");
            if (path.equals("/")) {
                name = "/";
            }
            final String type = split[2];
            if (localOnly && LinuxFileSystem.NETWORK_FS_TYPES.contains(type)) {
                continue;
            }
            if (!path.equals("/")) {
                if (LinuxFileSystem.PSEUDO_FS_TYPES.contains(type)) {
                    continue;
                }
                if (FileSystemUtil.isFileStoreExcluded(path, volume, LinuxFileSystem.FS_PATH_INCLUDES, LinuxFileSystem.FS_PATH_EXCLUDES, LinuxFileSystem.FS_VOLUME_INCLUDES, LinuxFileSystem.FS_VOLUME_EXCLUDES)) {
                    continue;
                }
            }
            final String options = split[3];
            if (nameToMatch != null && !nameToMatch.equals(name)) {
                continue;
            }
            final String uuid = (uuidMap != null) ? uuidMap.getOrDefault(split[0], "") : "";
            String description;
            if (volume.startsWith("/dev")) {
                description = "Local Disk";
            }
            else if (volume.equals("tmpfs")) {
                description = "Ram Disk";
            }
            else if (LinuxFileSystem.NETWORK_FS_TYPES.contains(type)) {
                description = "Network Disk";
            }
            else {
                description = "Mount Point";
            }
            String logicalVolume = "";
            final String volumeMapperDirectory = "/dev/mapper/";
            final Path link = Paths.get(volume, new String[0]);
            if (link.toFile().exists() && Files.isSymbolicLink(link)) {
                try {
                    final Path slink = Files.readSymbolicLink(link);
                    final Path full = Paths.get(volumeMapperDirectory + slink.toString(), new String[0]);
                    if (full.toFile().exists()) {
                        logicalVolume = full.normalize().toString();
                    }
                }
                catch (IOException e) {
                    LinuxFileSystem.LOG.warn("Couldn't access symbolic path  {}. {}", link, e.getMessage());
                }
            }
            long totalInodes = 0L;
            long freeInodes = 0L;
            long totalSpace = 0L;
            long usableSpace = 0L;
            long freeSpace = 0L;
            try {
                final LibC.Statvfs vfsStat = new LibC.Statvfs();
                if (0 == LibC.INSTANCE.statvfs(path, vfsStat)) {
                    totalInodes = vfsStat.f_files.longValue();
                    freeInodes = vfsStat.f_ffree.longValue();
                    totalSpace = vfsStat.f_blocks.longValue() * vfsStat.f_frsize.longValue();
                    usableSpace = vfsStat.f_bavail.longValue() * vfsStat.f_frsize.longValue();
                    freeSpace = vfsStat.f_bfree.longValue() * vfsStat.f_frsize.longValue();
                }
                else {
                    LinuxFileSystem.LOG.warn("Failed to get information to use statvfs. path: {}, Error code: {}", path, Native.getLastError());
                }
            }
            catch (UnsatisfiedLinkError | NoClassDefFoundError unsatisfiedLinkError) {
                final LinkageError linkageError;
                final LinkageError e2 = linkageError;
                LinuxFileSystem.LOG.error("Failed to get file counts from statvfs. {}", e2.getMessage());
            }
            if (totalSpace == 0L) {
                final File tmpFile = new File(path);
                totalSpace = tmpFile.getTotalSpace();
                usableSpace = tmpFile.getUsableSpace();
                freeSpace = tmpFile.getFreeSpace();
            }
            fsList.add(new LinuxOSFileStore(name, volume, labelMap.getOrDefault(path, name), path, options, uuid, logicalVolume, description, type, freeSpace, usableSpace, totalSpace, freeInodes, totalInodes));
        }
        return fsList;
    }
    
    private static Map<String, String> queryLabelMap() {
        final Map<String, String> labelMap = new HashMap<String, String>();
        for (final String line : ExecutingCommand.runNative("lsblk -o mountpoint,label")) {
            final String[] split = ParseUtil.whitespaces.split(line, 2);
            if (split.length == 2) {
                labelMap.put(split[0], split[1]);
            }
        }
        return labelMap;
    }
    
    @Override
    public long getOpenFileDescriptors() {
        return getFileDescriptors(0);
    }
    
    @Override
    public long getMaxFileDescriptors() {
        return getFileDescriptors(2);
    }
    
    private static long getFileDescriptors(final int index) {
        final String filename = ProcPath.SYS_FS_FILE_NR;
        if (index < 0 || index > 2) {
            throw new IllegalArgumentException("Index must be between 0 and 2.");
        }
        final List<String> osDescriptors = FileUtil.readFile(filename);
        if (!osDescriptors.isEmpty()) {
            final String[] splittedLine = osDescriptors.get(0).split("\\D+");
            return ParseUtil.parseLongOrDefault(splittedLine[index], 0L);
        }
        return 0L;
    }
    
    static {
        LOG = LoggerFactory.getLogger(LinuxFileSystem.class);
        FS_PATH_EXCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.linux.filesystem.path.excludes");
        FS_PATH_INCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.linux.filesystem.path.includes");
        FS_VOLUME_EXCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.linux.filesystem.volume.excludes");
        FS_VOLUME_INCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.linux.filesystem.volume.includes");
    }
}
