// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.windows;

import oshi.driver.windows.perfmon.ProcessInformation;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.util.platform.windows.WmiUtil;
import oshi.driver.windows.wmi.Win32LogicalDisk;
import oshi.util.ParseUtil;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.WinBase;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import oshi.software.os.OSFileStore;
import java.util.List;
import com.sun.jna.platform.win32.Kernel32;
import java.util.Map;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractFileSystem;

@ThreadSafe
public class WindowsFileSystem extends AbstractFileSystem
{
    private static final int BUFSIZE = 255;
    private static final int SEM_FAILCRITICALERRORS = 1;
    private static final int FILE_CASE_SENSITIVE_SEARCH = 1;
    private static final int FILE_CASE_PRESERVED_NAMES = 2;
    private static final int FILE_FILE_COMPRESSION = 16;
    private static final int FILE_DAX_VOLUME = 536870912;
    private static final int FILE_NAMED_STREAMS = 262144;
    private static final int FILE_PERSISTENT_ACLS = 8;
    private static final int FILE_READ_ONLY_VOLUME = 524288;
    private static final int FILE_SEQUENTIAL_WRITE_ONCE = 1048576;
    private static final int FILE_SUPPORTS_ENCRYPTION = 131072;
    private static final int FILE_SUPPORTS_OBJECT_IDS = 65536;
    private static final int FILE_SUPPORTS_REPARSE_POINTS = 128;
    private static final int FILE_SUPPORTS_SPARSE_FILES = 64;
    private static final int FILE_SUPPORTS_TRANSACTIONS = 2097152;
    private static final int FILE_SUPPORTS_USN_JOURNAL = 33554432;
    private static final int FILE_UNICODE_ON_DISK = 4;
    private static final int FILE_VOLUME_IS_COMPRESSED = 32768;
    private static final int FILE_VOLUME_QUOTAS = 32;
    private static final Map<Integer, String> OPTIONS_MAP;
    private static final long MAX_WINDOWS_HANDLES;
    
    public WindowsFileSystem() {
        Kernel32.INSTANCE.SetErrorMode(1);
    }
    
    @Override
    public List<OSFileStore> getFileStores(final boolean localOnly) {
        final ArrayList<OSFileStore> result = getLocalVolumes(null);
        final Map<String, OSFileStore> volumeMap = new HashMap<String, OSFileStore>();
        for (final OSFileStore volume : result) {
            volumeMap.put(volume.getMount(), volume);
        }
        for (final OSFileStore wmiVolume : getWmiVolumes(null, localOnly)) {
            if (volumeMap.containsKey(wmiVolume.getMount())) {
                final OSFileStore volume2 = volumeMap.get(wmiVolume.getMount());
                result.remove(volume2);
                result.add(new WindowsOSFileStore(wmiVolume.getName(), volume2.getVolume(), volume2.getLabel().isEmpty() ? wmiVolume.getLabel() : volume2.getLabel(), volume2.getMount(), volume2.getOptions(), volume2.getUUID(), "", volume2.getDescription(), volume2.getType(), volume2.getFreeSpace(), volume2.getUsableSpace(), volume2.getTotalSpace(), 0L, 0L));
            }
            else {
                if (localOnly) {
                    continue;
                }
                result.add(wmiVolume);
            }
        }
        return result;
    }
    
    static ArrayList<OSFileStore> getLocalVolumes(final String volumeToMatch) {
        final ArrayList<OSFileStore> fs = new ArrayList<OSFileStore>();
        final char[] aVolume = new char[255];
        final WinNT.HANDLE hVol = Kernel32.INSTANCE.FindFirstVolume(aVolume, 255);
        if (WinBase.INVALID_HANDLE_VALUE.equals(hVol)) {
            return fs;
        }
        Label_0043: {
            break Label_0043;
            try {
                do {
                    final char[] fstype = new char[16];
                    final char[] name = new char[255];
                    final char[] mount = new char[255];
                    final IntByReference pFlags = new IntByReference();
                    final WinNT.LARGE_INTEGER userFreeBytes = new WinNT.LARGE_INTEGER(0L);
                    final WinNT.LARGE_INTEGER totalBytes = new WinNT.LARGE_INTEGER(0L);
                    final WinNT.LARGE_INTEGER systemFreeBytes = new WinNT.LARGE_INTEGER(0L);
                    final String volume = Native.toString(aVolume);
                    Kernel32.INSTANCE.GetVolumeInformation(volume, name, 255, null, null, pFlags, fstype, 16);
                    final int flags = pFlags.getValue();
                    Kernel32.INSTANCE.GetVolumePathNamesForVolumeName(volume, mount, 255, null);
                    final String strMount = Native.toString(mount);
                    if (!strMount.isEmpty() && (volumeToMatch == null || volumeToMatch.equals(volume))) {
                        final String strName = Native.toString(name);
                        final String strFsType = Native.toString(fstype);
                        final StringBuilder options = new StringBuilder(((0x80000 & flags) == 0x0) ? "rw" : "ro");
                        final String moreOptions = WindowsFileSystem.OPTIONS_MAP.entrySet().stream().filter(e -> (e.getKey() & flags) > 0).map((Function<? super Object, ?>)Map.Entry::getValue).collect((Collector<? super Object, ?, String>)Collectors.joining(","));
                        if (!moreOptions.isEmpty()) {
                            options.append(',').append(moreOptions);
                        }
                        Kernel32.INSTANCE.GetDiskFreeSpaceEx(volume, userFreeBytes, totalBytes, systemFreeBytes);
                        final String uuid = ParseUtil.parseUuidOrDefault(volume, "");
                        fs.add(new WindowsOSFileStore(String.format("%s (%s)", strName, strMount), volume, strName, strMount, options.toString(), uuid, "", getDriveType(strMount), strFsType, systemFreeBytes.getValue(), userFreeBytes.getValue(), totalBytes.getValue(), 0L, 0L));
                    }
                } while (Kernel32.INSTANCE.FindNextVolume(hVol, aVolume, 255));
                return fs;
            }
            finally {
                Kernel32.INSTANCE.FindVolumeClose(hVol);
            }
        }
    }
    
    static List<OSFileStore> getWmiVolumes(final String nameToMatch, final boolean localOnly) {
        final List<OSFileStore> fs = new ArrayList<OSFileStore>();
        final WbemcliUtil.WmiResult<Win32LogicalDisk.LogicalDiskProperty> drives = Win32LogicalDisk.queryLogicalDisk(nameToMatch, localOnly);
        for (int i = 0; i < drives.getResultCount(); ++i) {
            final long free = WmiUtil.getUint64(drives, Win32LogicalDisk.LogicalDiskProperty.FREESPACE, i);
            final long total = WmiUtil.getUint64(drives, Win32LogicalDisk.LogicalDiskProperty.SIZE, i);
            String description = WmiUtil.getString(drives, Win32LogicalDisk.LogicalDiskProperty.DESCRIPTION, i);
            final String name = WmiUtil.getString(drives, Win32LogicalDisk.LogicalDiskProperty.NAME, i);
            final String label = WmiUtil.getString(drives, Win32LogicalDisk.LogicalDiskProperty.VOLUMENAME, i);
            final String options = (WmiUtil.getUint16(drives, Win32LogicalDisk.LogicalDiskProperty.ACCESS, i) == 1) ? "ro" : "rw";
            final int type = WmiUtil.getUint32(drives, Win32LogicalDisk.LogicalDiskProperty.DRIVETYPE, i);
            String volume;
            if (type != 4) {
                final char[] chrVolume = new char[255];
                Kernel32.INSTANCE.GetVolumeNameForVolumeMountPoint(name + "\\", chrVolume, 255);
                volume = Native.toString(chrVolume);
            }
            else {
                volume = WmiUtil.getString(drives, Win32LogicalDisk.LogicalDiskProperty.PROVIDERNAME, i);
                final String[] split = volume.split("\\\\");
                if (split.length > 1 && split[split.length - 1].length() > 0) {
                    description = split[split.length - 1];
                }
            }
            fs.add(new WindowsOSFileStore(String.format("%s (%s)", description, name), volume, label, name + "\\", options, "", "", getDriveType(name), WmiUtil.getString(drives, Win32LogicalDisk.LogicalDiskProperty.FILESYSTEM, i), free, free, total, 0L, 0L));
        }
        return fs;
    }
    
    private static String getDriveType(final String drive) {
        switch (Kernel32.INSTANCE.GetDriveType(drive)) {
            case 2: {
                return "Removable drive";
            }
            case 3: {
                return "Fixed drive";
            }
            case 4: {
                return "Network drive";
            }
            case 5: {
                return "CD-ROM";
            }
            case 6: {
                return "RAM drive";
            }
            default: {
                return "Unknown drive type";
            }
        }
    }
    
    @Override
    public long getOpenFileDescriptors() {
        final Map<ProcessInformation.HandleCountProperty, List<Long>> valueListMap = ProcessInformation.queryHandles().getB();
        final List<Long> valueList = valueListMap.get(ProcessInformation.HandleCountProperty.HANDLECOUNT);
        long descriptors = 0L;
        if (valueList != null) {
            for (int i = 0; i < valueList.size(); ++i) {
                descriptors += valueList.get(i);
            }
        }
        return descriptors;
    }
    
    @Override
    public long getMaxFileDescriptors() {
        return WindowsFileSystem.MAX_WINDOWS_HANDLES;
    }
    
    static {
        (OPTIONS_MAP = new HashMap<Integer, String>()).put(2, "casepn");
        WindowsFileSystem.OPTIONS_MAP.put(1, "casess");
        WindowsFileSystem.OPTIONS_MAP.put(16, "fcomp");
        WindowsFileSystem.OPTIONS_MAP.put(536870912, "dax");
        WindowsFileSystem.OPTIONS_MAP.put(262144, "streams");
        WindowsFileSystem.OPTIONS_MAP.put(8, "acls");
        WindowsFileSystem.OPTIONS_MAP.put(1048576, "wronce");
        WindowsFileSystem.OPTIONS_MAP.put(131072, "efs");
        WindowsFileSystem.OPTIONS_MAP.put(65536, "oids");
        WindowsFileSystem.OPTIONS_MAP.put(128, "reparse");
        WindowsFileSystem.OPTIONS_MAP.put(64, "sparse");
        WindowsFileSystem.OPTIONS_MAP.put(2097152, "trans");
        WindowsFileSystem.OPTIONS_MAP.put(33554432, "journaled");
        WindowsFileSystem.OPTIONS_MAP.put(4, "unicode");
        WindowsFileSystem.OPTIONS_MAP.put(32768, "vcomp");
        WindowsFileSystem.OPTIONS_MAP.put(32, "quota");
        if (System.getenv("ProgramFiles(x86)") == null) {
            MAX_WINDOWS_HANDLES = 16744448L;
        }
        else {
            MAX_WINDOWS_HANDLES = 16711680L;
        }
    }
}
