// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.solaris;

import oshi.util.Memoizer;
import com.sun.jna.platform.unix.solaris.LibKstat;
import oshi.util.platform.unix.solaris.KstatUtil;
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
import oshi.util.tuples.Pair;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractFileSystem;

@ThreadSafe
public class SolarisFileSystem extends AbstractFileSystem
{
    private static final Supplier<Pair<Long, Long>> FILE_DESC;
    public static final String OSHI_SOLARIS_FS_PATH_EXCLUDES = "oshi.os.solaris.filesystem.path.excludes";
    public static final String OSHI_SOLARIS_FS_PATH_INCLUDES = "oshi.os.solaris.filesystem.path.includes";
    public static final String OSHI_SOLARIS_FS_VOLUME_EXCLUDES = "oshi.os.solaris.filesystem.volume.excludes";
    public static final String OSHI_SOLARIS_FS_VOLUME_INCLUDES = "oshi.os.solaris.filesystem.volume.includes";
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
        String key = null;
        String total = null;
        String free = null;
        final String command = "df -g" + (localOnly ? " -l" : "");
        for (final String line : ExecutingCommand.runNative(command)) {
            if (line.startsWith("/")) {
                key = ParseUtil.whitespaces.split(line)[0];
                total = null;
            }
            else if (line.contains("available") && line.contains("total files")) {
                total = ParseUtil.getTextBetweenStrings(line, "available", "total files").trim();
            }
            else {
                if (!line.contains("free files")) {
                    continue;
                }
                free = ParseUtil.getTextBetweenStrings(line, "", "free files").trim();
                if (key == null || total == null) {
                    continue;
                }
                inodeFreeMap.put(key, ParseUtil.parseLongOrDefault(free, 0L));
                inodeTotalMap.put(key, ParseUtil.parseLongOrDefault(total, 0L));
                key = null;
            }
        }
        for (final String fs : ExecutingCommand.runNative("cat /etc/mnttab")) {
            final String[] split = ParseUtil.whitespaces.split(fs);
            if (split.length < 5) {
                continue;
            }
            final String volume = split[0];
            final String path = split[1];
            final String type = split[2];
            final String options = split[3];
            if (localOnly && SolarisFileSystem.NETWORK_FS_TYPES.contains(type)) {
                continue;
            }
            if (!path.equals("/")) {
                if (SolarisFileSystem.PSEUDO_FS_TYPES.contains(type)) {
                    continue;
                }
                if (FileSystemUtil.isFileStoreExcluded(path, volume, SolarisFileSystem.FS_PATH_INCLUDES, SolarisFileSystem.FS_PATH_EXCLUDES, SolarisFileSystem.FS_VOLUME_INCLUDES, SolarisFileSystem.FS_VOLUME_EXCLUDES)) {
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
            else if (SolarisFileSystem.NETWORK_FS_TYPES.contains(type)) {
                description = "Network Disk";
            }
            else {
                description = "Mount Point";
            }
            fsList.add(new SolarisOSFileStore(name, volume, name, path, options, "", "", description, type, freeSpace, usableSpace, totalSpace, inodeFreeMap.containsKey(path) ? inodeFreeMap.get(path) : 0L, inodeTotalMap.containsKey(path) ? inodeTotalMap.get(path) : 0L));
        }
        return fsList;
    }
    
    @Override
    public long getOpenFileDescriptors() {
        if (SolarisOperatingSystem.IS_11_4_OR_HIGHER) {
            return SolarisFileSystem.FILE_DESC.get().getA();
        }
        final KstatUtil.KstatChain kc = KstatUtil.openChain();
        try {
            final LibKstat.Kstat ksp = KstatUtil.KstatChain.lookup(null, -1, "file_cache");
            if (ksp != null && KstatUtil.KstatChain.read(ksp)) {
                final long dataLookupLong = KstatUtil.dataLookupLong(ksp, "buf_inuse");
                if (kc != null) {
                    kc.close();
                }
                return dataLookupLong;
            }
            if (kc != null) {
                kc.close();
            }
        }
        catch (Throwable t) {
            if (kc != null) {
                try {
                    kc.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
        return 0L;
    }
    
    @Override
    public long getMaxFileDescriptors() {
        if (SolarisOperatingSystem.IS_11_4_OR_HIGHER) {
            return SolarisFileSystem.FILE_DESC.get().getB();
        }
        final KstatUtil.KstatChain kc = KstatUtil.openChain();
        try {
            final LibKstat.Kstat ksp = KstatUtil.KstatChain.lookup(null, -1, "file_cache");
            if (ksp != null && KstatUtil.KstatChain.read(ksp)) {
                final long dataLookupLong = KstatUtil.dataLookupLong(ksp, "buf_max");
                if (kc != null) {
                    kc.close();
                }
                return dataLookupLong;
            }
            if (kc != null) {
                kc.close();
            }
        }
        catch (Throwable t) {
            if (kc != null) {
                try {
                    kc.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
        return 0L;
    }
    
    private static Pair<Long, Long> queryFileDescriptors() {
        final Object[] results = KstatUtil.queryKstat2("kstat:/kmem_cache/kmem_default/file_cache", "buf_inuse", "buf_max");
        final long inuse = (long)((results[0] == null) ? 0L : results[0]);
        final long max = (long)((results[1] == null) ? 0L : results[1]);
        return new Pair<Long, Long>(inuse, max);
    }
    
    static {
        FILE_DESC = Memoizer.memoize(SolarisFileSystem::queryFileDescriptors, Memoizer.defaultExpiration());
        FS_PATH_EXCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.solaris.filesystem.path.excludes");
        FS_PATH_INCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.solaris.filesystem.path.includes");
        FS_VOLUME_EXCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.solaris.filesystem.volume.excludes");
        FS_VOLUME_INCLUDES = FileSystemUtil.loadAndParseFileSystemConfig("oshi.os.solaris.filesystem.volume.includes");
    }
}
