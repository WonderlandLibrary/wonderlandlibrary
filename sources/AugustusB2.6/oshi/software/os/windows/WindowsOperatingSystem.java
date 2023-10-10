// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.windows;

import java.util.concurrent.TimeUnit;
import com.sun.jna.platform.win32.VersionHelpers;
import org.slf4j.LoggerFactory;
import com.sun.jna.platform.win32.WinBase;
import oshi.driver.windows.EnumWindows;
import oshi.software.os.OSDesktopWindow;
import oshi.util.GlobalConfig;
import com.sun.jna.platform.win32.Winsvc;
import java.util.Collections;
import com.sun.jna.platform.win32.W32ServiceManager;
import oshi.software.os.OSService;
import oshi.software.os.NetworkParams;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Psapi;
import java.util.Iterator;
import java.util.HashSet;
import oshi.driver.windows.registry.ProcessWtsData;
import java.util.Arrays;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.Tlhelp32;
import java.util.HashMap;
import java.util.Set;
import oshi.software.os.OSProcess;
import oshi.driver.windows.registry.NetSessionData;
import java.util.Collection;
import oshi.driver.windows.registry.SessionWtsData;
import oshi.driver.windows.registry.HkeyUserData;
import oshi.software.os.OSSession;
import oshi.software.os.InternetProtocolStats;
import oshi.software.os.FileSystem;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.WinNT;
import oshi.driver.windows.wmi.Win32Processor;
import java.util.List;
import java.util.ArrayList;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.util.platform.windows.WmiUtil;
import oshi.driver.windows.wmi.Win32OperatingSystem;
import oshi.software.os.OperatingSystem;
import oshi.util.tuples.Pair;
import oshi.util.Memoizer;
import oshi.driver.windows.registry.ThreadPerformanceData;
import oshi.driver.windows.registry.ProcessPerformanceData;
import java.util.Map;
import java.util.function.Supplier;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractOperatingSystem;

@ThreadSafe
public class WindowsOperatingSystem extends AbstractOperatingSystem
{
    private static final Logger LOG;
    public static final String OSHI_OS_WINDOWS_PROCSTATE_SUSPENDED = "oshi.os.windows.procstate.suspended";
    private static final boolean USE_PROCSTATE_SUSPENDED;
    private static final boolean IS_VISTA_OR_GREATER;
    private static final int TOKENELEVATION = 20;
    private static Supplier<String> systemLog;
    private static final long BOOTTIME;
    private static final boolean X86;
    private static final boolean WOW;
    private Supplier<Map<Integer, ProcessPerformanceData.PerfCounterBlock>> processMapFromRegistry;
    private Supplier<Map<Integer, ProcessPerformanceData.PerfCounterBlock>> processMapFromPerfCounters;
    private Supplier<Map<Integer, ThreadPerformanceData.PerfCounterBlock>> threadMapFromRegistry;
    private Supplier<Map<Integer, ThreadPerformanceData.PerfCounterBlock>> threadMapFromPerfCounters;
    
    public WindowsOperatingSystem() {
        this.processMapFromRegistry = Memoizer.memoize(WindowsOperatingSystem::queryProcessMapFromRegistry, Memoizer.defaultExpiration());
        this.processMapFromPerfCounters = Memoizer.memoize(WindowsOperatingSystem::queryProcessMapFromPerfCounters, Memoizer.defaultExpiration());
        this.threadMapFromRegistry = Memoizer.memoize(WindowsOperatingSystem::queryThreadMapFromRegistry, Memoizer.defaultExpiration());
        this.threadMapFromPerfCounters = Memoizer.memoize(WindowsOperatingSystem::queryThreadMapFromPerfCounters, Memoizer.defaultExpiration());
    }
    
    public String queryManufacturer() {
        return "Microsoft";
    }
    
    public Pair<String, OperatingSystem.OSVersionInfo> queryFamilyVersionInfo() {
        String version = System.getProperty("os.name");
        if (version.startsWith("Windows ")) {
            version = version.substring(8);
        }
        String sp = null;
        int suiteMask = 0;
        String buildNumber = null;
        final WbemcliUtil.WmiResult<Win32OperatingSystem.OSVersionProperty> versionInfo = Win32OperatingSystem.queryOsVersion();
        if (versionInfo.getResultCount() > 0) {
            sp = WmiUtil.getString(versionInfo, Win32OperatingSystem.OSVersionProperty.CSDVERSION, 0);
            if (!sp.isEmpty() && !"unknown".equals(sp)) {
                version = version + " " + sp.replace("Service Pack ", "SP");
            }
            suiteMask = WmiUtil.getUint32(versionInfo, Win32OperatingSystem.OSVersionProperty.SUITEMASK, 0);
            buildNumber = WmiUtil.getString(versionInfo, Win32OperatingSystem.OSVersionProperty.BUILDNUMBER, 0);
        }
        final String codeName = parseCodeName(suiteMask);
        return new Pair<String, OperatingSystem.OSVersionInfo>("Windows", new OperatingSystem.OSVersionInfo(version, codeName, buildNumber));
    }
    
    private static String parseCodeName(final int suiteMask) {
        final List<String> suites = new ArrayList<String>();
        if ((suiteMask & 0x2) != 0x0) {
            suites.add("Enterprise");
        }
        if ((suiteMask & 0x4) != 0x0) {
            suites.add("BackOffice");
        }
        if ((suiteMask & 0x8) != 0x0) {
            suites.add("Communications Server");
        }
        if ((suiteMask & 0x80) != 0x0) {
            suites.add("Datacenter");
        }
        if ((suiteMask & 0x200) != 0x0) {
            suites.add("Home");
        }
        if ((suiteMask & 0x400) != 0x0) {
            suites.add("Web Server");
        }
        if ((suiteMask & 0x2000) != 0x0) {
            suites.add("Storage Server");
        }
        if ((suiteMask & 0x4000) != 0x0) {
            suites.add("Compute Cluster");
        }
        if ((suiteMask & 0x8000) != 0x0) {
            suites.add("Home Server");
        }
        return String.join(",", suites);
    }
    
    @Override
    protected int queryBitness(final int jvmBitness) {
        if (jvmBitness < 64 && System.getenv("ProgramFiles(x86)") != null && WindowsOperatingSystem.IS_VISTA_OR_GREATER) {
            final WbemcliUtil.WmiResult<Win32Processor.BitnessProperty> bitnessMap = Win32Processor.queryBitness();
            if (bitnessMap.getResultCount() > 0) {
                return WmiUtil.getUint16(bitnessMap, Win32Processor.BitnessProperty.ADDRESSWIDTH, 0);
            }
        }
        return jvmBitness;
    }
    
    @Override
    public boolean isElevated() {
        final WinNT.HANDLEByReference hToken = new WinNT.HANDLEByReference();
        final boolean success = Advapi32.INSTANCE.OpenProcessToken(Kernel32.INSTANCE.GetCurrentProcess(), 8, hToken);
        if (!success) {
            WindowsOperatingSystem.LOG.error("OpenProcessToken failed. Error: {}", (Object)Native.getLastError());
            return false;
        }
        try {
            final oshi.jna.platform.windows.WinNT.TOKEN_ELEVATION elevation = new oshi.jna.platform.windows.WinNT.TOKEN_ELEVATION();
            if (Advapi32.INSTANCE.GetTokenInformation(hToken.getValue(), 20, elevation, elevation.size(), new IntByReference())) {
                return elevation.TokenIsElevated > 0;
            }
        }
        finally {
            Kernel32.INSTANCE.CloseHandle(hToken.getValue());
        }
        return false;
    }
    
    @Override
    public FileSystem getFileSystem() {
        return new WindowsFileSystem();
    }
    
    @Override
    public InternetProtocolStats getInternetProtocolStats() {
        return new WindowsInternetProtocolStats();
    }
    
    @Override
    public List<OSSession> getSessions() {
        final List<OSSession> whoList = HkeyUserData.queryUserSessions();
        whoList.addAll(SessionWtsData.queryUserSessions());
        whoList.addAll(NetSessionData.queryUserSessions());
        return whoList;
    }
    
    @Override
    public List<OSProcess> getProcesses(final Collection<Integer> pids) {
        return this.processMapToList(pids);
    }
    
    public List<OSProcess> queryAllProcesses() {
        return this.processMapToList(null);
    }
    
    public List<OSProcess> queryChildProcesses(final int parentPid) {
        final Set<Integer> descendantPids = AbstractOperatingSystem.getChildrenOrDescendants(getParentPidsFromSnapshot(), parentPid, false);
        return this.processMapToList(descendantPids);
    }
    
    public List<OSProcess> queryDescendantProcesses(final int parentPid) {
        final Set<Integer> descendantPids = AbstractOperatingSystem.getChildrenOrDescendants(getParentPidsFromSnapshot(), parentPid, true);
        return this.processMapToList(descendantPids);
    }
    
    private static Map<Integer, Integer> getParentPidsFromSnapshot() {
        final Map<Integer, Integer> parentPidMap = new HashMap<Integer, Integer>();
        final Tlhelp32.PROCESSENTRY32.ByReference processEntry = new Tlhelp32.PROCESSENTRY32.ByReference();
        final WinNT.HANDLE snapshot = Kernel32.INSTANCE.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0L));
        try {
            while (Kernel32.INSTANCE.Process32Next(snapshot, processEntry)) {
                parentPidMap.put(processEntry.th32ProcessID.intValue(), processEntry.th32ParentProcessID.intValue());
            }
        }
        finally {
            Kernel32.INSTANCE.CloseHandle(snapshot);
        }
        return parentPidMap;
    }
    
    @Override
    public OSProcess getProcess(final int pid) {
        final List<OSProcess> procList = this.processMapToList(Arrays.asList(pid));
        return procList.isEmpty() ? null : procList.get(0);
    }
    
    private List<OSProcess> processMapToList(final Collection<Integer> pids) {
        Map<Integer, ProcessPerformanceData.PerfCounterBlock> processMap = this.processMapFromRegistry.get();
        if (processMap == null || processMap.isEmpty()) {
            processMap = ((pids == null) ? this.processMapFromPerfCounters.get() : ProcessPerformanceData.buildProcessMapFromPerfCounters(pids));
        }
        Map<Integer, ThreadPerformanceData.PerfCounterBlock> threadMap = null;
        if (WindowsOperatingSystem.USE_PROCSTATE_SUSPENDED) {
            threadMap = this.threadMapFromRegistry.get();
            if (threadMap == null || threadMap.isEmpty()) {
                threadMap = ((pids == null) ? this.threadMapFromPerfCounters.get() : ThreadPerformanceData.buildThreadMapFromPerfCounters(pids));
            }
        }
        final Map<Integer, ProcessWtsData.WtsInfo> processWtsMap = ProcessWtsData.queryProcessWtsMap(pids);
        final Set<Integer> mapKeys = new HashSet<Integer>(processWtsMap.keySet());
        mapKeys.retainAll(processMap.keySet());
        final List<OSProcess> processList = new ArrayList<OSProcess>();
        for (final Integer pid : mapKeys) {
            processList.add(new WindowsOSProcess(pid, this, processMap, processWtsMap, threadMap));
        }
        return processList;
    }
    
    private static Map<Integer, ProcessPerformanceData.PerfCounterBlock> queryProcessMapFromRegistry() {
        return ProcessPerformanceData.buildProcessMapFromRegistry(null);
    }
    
    private static Map<Integer, ProcessPerformanceData.PerfCounterBlock> queryProcessMapFromPerfCounters() {
        return ProcessPerformanceData.buildProcessMapFromPerfCounters(null);
    }
    
    private static Map<Integer, ThreadPerformanceData.PerfCounterBlock> queryThreadMapFromRegistry() {
        return ThreadPerformanceData.buildThreadMapFromRegistry(null);
    }
    
    private static Map<Integer, ThreadPerformanceData.PerfCounterBlock> queryThreadMapFromPerfCounters() {
        return ThreadPerformanceData.buildThreadMapFromPerfCounters(null);
    }
    
    @Override
    public int getProcessId() {
        return Kernel32.INSTANCE.GetCurrentProcessId();
    }
    
    @Override
    public int getProcessCount() {
        final Psapi.PERFORMANCE_INFORMATION perfInfo = new Psapi.PERFORMANCE_INFORMATION();
        if (!Psapi.INSTANCE.GetPerformanceInfo(perfInfo, perfInfo.size())) {
            WindowsOperatingSystem.LOG.error("Failed to get Performance Info. Error code: {}", (Object)Kernel32.INSTANCE.GetLastError());
            return 0;
        }
        return perfInfo.ProcessCount.intValue();
    }
    
    @Override
    public int getThreadCount() {
        final Psapi.PERFORMANCE_INFORMATION perfInfo = new Psapi.PERFORMANCE_INFORMATION();
        if (!Psapi.INSTANCE.GetPerformanceInfo(perfInfo, perfInfo.size())) {
            WindowsOperatingSystem.LOG.error("Failed to get Performance Info. Error code: {}", (Object)Kernel32.INSTANCE.GetLastError());
            return 0;
        }
        return perfInfo.ThreadCount.intValue();
    }
    
    @Override
    public long getSystemUptime() {
        return querySystemUptime();
    }
    
    private static long querySystemUptime() {
        if (WindowsOperatingSystem.IS_VISTA_OR_GREATER) {
            return Kernel32.INSTANCE.GetTickCount64() / 1000L;
        }
        return Kernel32.INSTANCE.GetTickCount() / 1000L;
    }
    
    @Override
    public long getSystemBootTime() {
        return WindowsOperatingSystem.BOOTTIME;
    }
    
    private static long querySystemBootTime() {
        final String eventLog = WindowsOperatingSystem.systemLog.get();
        if (eventLog != null) {
            try {
                final Advapi32Util.EventLogIterator iter = new Advapi32Util.EventLogIterator(null, eventLog, 8);
                long event6005Time = 0L;
                while (iter.hasNext()) {
                    final Advapi32Util.EventLogRecord record = iter.next();
                    if (record.getStatusCode() == 12) {
                        return record.getRecord().TimeGenerated.longValue();
                    }
                    if (record.getStatusCode() != 6005) {
                        continue;
                    }
                    if (event6005Time > 0L) {
                        return event6005Time;
                    }
                    event6005Time = record.getRecord().TimeGenerated.longValue();
                }
                if (event6005Time > 0L) {
                    return event6005Time;
                }
            }
            catch (Win32Exception e) {
                WindowsOperatingSystem.LOG.warn("Can't open event log \"{}\".", eventLog);
            }
        }
        return System.currentTimeMillis() / 1000L - querySystemUptime();
    }
    
    @Override
    public NetworkParams getNetworkParams() {
        return new WindowsNetworkParams();
    }
    
    private static boolean enableDebugPrivilege() {
        final WinNT.HANDLEByReference hToken = new WinNT.HANDLEByReference();
        boolean success = Advapi32.INSTANCE.OpenProcessToken(Kernel32.INSTANCE.GetCurrentProcess(), 40, hToken);
        if (!success) {
            WindowsOperatingSystem.LOG.error("OpenProcessToken failed. Error: {}", (Object)Native.getLastError());
            return false;
        }
        try {
            final WinNT.LUID luid = new WinNT.LUID();
            success = Advapi32.INSTANCE.LookupPrivilegeValue(null, "SeDebugPrivilege", luid);
            if (!success) {
                WindowsOperatingSystem.LOG.error("LookupPrivilegeValue failed. Error: {}", (Object)Native.getLastError());
                return false;
            }
            final WinNT.TOKEN_PRIVILEGES tkp = new WinNT.TOKEN_PRIVILEGES(1);
            tkp.Privileges[0] = new WinNT.LUID_AND_ATTRIBUTES(luid, new WinDef.DWORD(2L));
            success = Advapi32.INSTANCE.AdjustTokenPrivileges(hToken.getValue(), false, tkp, 0, null, null);
            final int err = Native.getLastError();
            if (!success) {
                WindowsOperatingSystem.LOG.error("AdjustTokenPrivileges failed. Error: {}", (Object)err);
                return false;
            }
            if (err == 1300) {
                WindowsOperatingSystem.LOG.debug("Debug privileges not enabled.");
                return false;
            }
        }
        finally {
            Kernel32.INSTANCE.CloseHandle(hToken.getValue());
        }
        return true;
    }
    
    @Override
    public List<OSService> getServices() {
        try {
            final W32ServiceManager sm = new W32ServiceManager();
            try {
                sm.open(4);
                final Winsvc.ENUM_SERVICE_STATUS_PROCESS[] services = sm.enumServicesStatusExProcess(48, 3, null);
                final List<OSService> svcArray = new ArrayList<OSService>();
                for (final Winsvc.ENUM_SERVICE_STATUS_PROCESS service : services) {
                    OSService.State state = null;
                    switch (service.ServiceStatusProcess.dwCurrentState) {
                        case 1: {
                            state = OSService.State.STOPPED;
                            break;
                        }
                        case 4: {
                            state = OSService.State.RUNNING;
                            break;
                        }
                        default: {
                            state = OSService.State.OTHER;
                            break;
                        }
                    }
                    svcArray.add(new OSService(service.lpDisplayName, service.ServiceStatusProcess.dwProcessId, state));
                }
                final List<OSService> list = svcArray;
                sm.close();
                return list;
            }
            catch (Throwable t) {
                try {
                    sm.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
                throw t;
            }
        }
        catch (Win32Exception ex) {
            WindowsOperatingSystem.LOG.error("Win32Exception: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }
    
    private static String querySystemLog() {
        final String systemLog = GlobalConfig.get("oshi.os.windows.eventlog", "System");
        if (systemLog.isEmpty()) {
            return null;
        }
        final WinNT.HANDLE h = Advapi32.INSTANCE.OpenEventLog(null, systemLog);
        if (h == null) {
            WindowsOperatingSystem.LOG.warn("Unable to open configured system Event log \"{}\". Calculating boot time from uptime.", systemLog);
            return null;
        }
        return systemLog;
    }
    
    @Override
    public List<OSDesktopWindow> getDesktopWindows(final boolean visibleOnly) {
        return EnumWindows.queryDesktopWindows(visibleOnly);
    }
    
    static boolean isX86() {
        return WindowsOperatingSystem.X86;
    }
    
    private static boolean isCurrentX86() {
        final WinBase.SYSTEM_INFO sysinfo = new WinBase.SYSTEM_INFO();
        Kernel32.INSTANCE.GetNativeSystemInfo(sysinfo);
        return 0 == sysinfo.processorArchitecture.pi.wProcessorArchitecture.intValue();
    }
    
    static boolean isWow() {
        return WindowsOperatingSystem.WOW;
    }
    
    static boolean isWow(final WinNT.HANDLE h) {
        if (WindowsOperatingSystem.X86) {
            return true;
        }
        final IntByReference isWow = new IntByReference();
        Kernel32.INSTANCE.IsWow64Process(h, isWow);
        return isWow.getValue() != 0;
    }
    
    private static boolean isCurrentWow() {
        if (WindowsOperatingSystem.X86) {
            return true;
        }
        final WinNT.HANDLE h = Kernel32.INSTANCE.GetCurrentProcess();
        if (h != null) {
            try {
                return isWow(h);
            }
            finally {
                Kernel32.INSTANCE.CloseHandle(h);
            }
        }
        return false;
    }
    
    static {
        LOG = LoggerFactory.getLogger(WindowsOperatingSystem.class);
        USE_PROCSTATE_SUSPENDED = GlobalConfig.get("oshi.os.windows.procstate.suspended", false);
        IS_VISTA_OR_GREATER = VersionHelpers.IsWindowsVistaOrGreater();
        WindowsOperatingSystem.systemLog = Memoizer.memoize(WindowsOperatingSystem::querySystemLog, TimeUnit.HOURS.toNanos(1L));
        BOOTTIME = querySystemBootTime();
        enableDebugPrivilege();
        X86 = isCurrentX86();
        WOW = isCurrentWow();
    }
}
