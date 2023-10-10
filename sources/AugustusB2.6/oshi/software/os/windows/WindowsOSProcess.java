// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.windows;

import com.sun.jna.platform.win32.VersionHelpers;
import oshi.util.GlobalConfig;
import org.slf4j.LoggerFactory;
import oshi.util.ParseUtil;
import com.sun.jna.Memory;
import oshi.jna.platform.windows.NtDll;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Advapi32;
import java.io.File;
import java.util.Arrays;
import com.sun.jna.platform.win32.Shell32Util;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.util.platform.windows.WmiUtil;
import oshi.driver.windows.wmi.Win32Process;
import oshi.driver.windows.wmi.Win32ProcessCached;
import java.util.Iterator;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.ptr.IntByReference;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.Collections;
import oshi.software.os.OSThread;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.Kernel32;
import oshi.util.Memoizer;
import oshi.driver.windows.registry.ThreadPerformanceData;
import oshi.driver.windows.registry.ProcessWtsData;
import oshi.driver.windows.registry.ProcessPerformanceData;
import oshi.software.os.OSProcess;
import java.util.Map;
import oshi.util.tuples.Triplet;
import java.util.List;
import oshi.util.tuples.Pair;
import java.util.function.Supplier;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractOSProcess;

@ThreadSafe
public class WindowsOSProcess extends AbstractOSProcess
{
    private static final Logger LOG;
    public static final String OSHI_OS_WINDOWS_COMMANDLINE_BATCH = "oshi.os.windows.commandline.batch";
    private static final boolean USE_BATCH_COMMANDLINE;
    private static final boolean USE_PROCSTATE_SUSPENDED;
    private static final boolean IS_VISTA_OR_GREATER;
    private static final boolean IS_WINDOWS7_OR_GREATER;
    private final WindowsOperatingSystem os;
    private Supplier<Pair<String, String>> userInfo;
    private Supplier<Pair<String, String>> groupInfo;
    private Supplier<String> currentWorkingDirectory;
    private Supplier<String> commandLine;
    private Supplier<List<String>> args;
    private Supplier<Triplet<String, String, Map<String, String>>> cwdCmdEnv;
    private String name;
    private String path;
    private OSProcess.State state;
    private int parentProcessID;
    private int threadCount;
    private int priority;
    private long virtualSize;
    private long residentSetSize;
    private long kernelTime;
    private long userTime;
    private long startTime;
    private long upTime;
    private long bytesRead;
    private long bytesWritten;
    private long openFiles;
    private int bitness;
    private long pageFaults;
    
    public WindowsOSProcess(final int pid, final WindowsOperatingSystem os, final Map<Integer, ProcessPerformanceData.PerfCounterBlock> processMap, final Map<Integer, ProcessWtsData.WtsInfo> processWtsMap, final Map<Integer, ThreadPerformanceData.PerfCounterBlock> threadMap) {
        super(pid);
        this.userInfo = Memoizer.memoize(this::queryUserInfo);
        this.groupInfo = Memoizer.memoize(this::queryGroupInfo);
        this.currentWorkingDirectory = Memoizer.memoize(this::queryCwd);
        this.commandLine = Memoizer.memoize(this::queryCommandLine);
        this.args = Memoizer.memoize(this::queryArguments);
        this.cwdCmdEnv = Memoizer.memoize(this::queryCwdCommandlineEnvironment);
        this.state = OSProcess.State.INVALID;
        this.os = os;
        this.bitness = os.getBitness();
        this.updateAttributes(processMap.get(pid), processWtsMap.get(pid), threadMap);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getPath() {
        return this.path;
    }
    
    @Override
    public String getCommandLine() {
        return this.commandLine.get();
    }
    
    @Override
    public List<String> getArguments() {
        return this.args.get();
    }
    
    @Override
    public Map<String, String> getEnvironmentVariables() {
        return this.cwdCmdEnv.get().getC();
    }
    
    @Override
    public String getCurrentWorkingDirectory() {
        return this.currentWorkingDirectory.get();
    }
    
    @Override
    public String getUser() {
        return this.userInfo.get().getA();
    }
    
    @Override
    public String getUserID() {
        return this.userInfo.get().getB();
    }
    
    @Override
    public String getGroup() {
        return this.groupInfo.get().getA();
    }
    
    @Override
    public String getGroupID() {
        return this.groupInfo.get().getB();
    }
    
    @Override
    public OSProcess.State getState() {
        return this.state;
    }
    
    @Override
    public int getParentProcessID() {
        return this.parentProcessID;
    }
    
    @Override
    public int getThreadCount() {
        return this.threadCount;
    }
    
    @Override
    public int getPriority() {
        return this.priority;
    }
    
    @Override
    public long getVirtualSize() {
        return this.virtualSize;
    }
    
    @Override
    public long getResidentSetSize() {
        return this.residentSetSize;
    }
    
    @Override
    public long getKernelTime() {
        return this.kernelTime;
    }
    
    @Override
    public long getUserTime() {
        return this.userTime;
    }
    
    @Override
    public long getUpTime() {
        return this.upTime;
    }
    
    @Override
    public long getStartTime() {
        return this.startTime;
    }
    
    @Override
    public long getBytesRead() {
        return this.bytesRead;
    }
    
    @Override
    public long getBytesWritten() {
        return this.bytesWritten;
    }
    
    @Override
    public long getOpenFiles() {
        return this.openFiles;
    }
    
    @Override
    public int getBitness() {
        return this.bitness;
    }
    
    @Override
    public long getAffinityMask() {
        final WinNT.HANDLE pHandle = Kernel32.INSTANCE.OpenProcess(1024, false, this.getProcessID());
        if (pHandle != null) {
            try {
                final BaseTSD.ULONG_PTRByReference processAffinity = new BaseTSD.ULONG_PTRByReference();
                final BaseTSD.ULONG_PTRByReference systemAffinity = new BaseTSD.ULONG_PTRByReference();
                if (Kernel32.INSTANCE.GetProcessAffinityMask(pHandle, processAffinity, systemAffinity)) {
                    return Pointer.nativeValue(processAffinity.getValue().toPointer());
                }
            }
            finally {
                Kernel32.INSTANCE.CloseHandle(pHandle);
            }
            Kernel32.INSTANCE.CloseHandle(pHandle);
        }
        return 0L;
    }
    
    @Override
    public long getMinorFaults() {
        return this.pageFaults;
    }
    
    @Override
    public List<OSThread> getThreadDetails() {
        Map<Integer, ThreadPerformanceData.PerfCounterBlock> threads = ThreadPerformanceData.buildThreadMapFromRegistry(Collections.singleton(this.getProcessID()));
        if (threads != null) {
            threads = ThreadPerformanceData.buildThreadMapFromPerfCounters(Collections.singleton(this.getProcessID()));
        }
        if (threads == null) {
            return Collections.emptyList();
        }
        return threads.entrySet().stream().map(entry -> new WindowsOSThread(this.getProcessID(), entry.getKey(), this.name, (ThreadPerformanceData.PerfCounterBlock)entry.getValue())).collect((Collector<? super Object, ?, List<OSThread>>)Collectors.toList());
    }
    
    @Override
    public boolean updateAttributes() {
        final Set<Integer> pids = Collections.singleton(this.getProcessID());
        Map<Integer, ProcessPerformanceData.PerfCounterBlock> pcb = ProcessPerformanceData.buildProcessMapFromRegistry(null);
        if (pcb == null) {
            pcb = ProcessPerformanceData.buildProcessMapFromPerfCounters(pids);
        }
        Map<Integer, ThreadPerformanceData.PerfCounterBlock> tcb = null;
        if (WindowsOSProcess.USE_PROCSTATE_SUSPENDED) {
            tcb = ThreadPerformanceData.buildThreadMapFromRegistry(null);
            if (tcb == null) {
                tcb = ThreadPerformanceData.buildThreadMapFromPerfCounters(null);
            }
        }
        final Map<Integer, ProcessWtsData.WtsInfo> wts = ProcessWtsData.queryProcessWtsMap(pids);
        return this.updateAttributes(pcb.get(this.getProcessID()), wts.get(this.getProcessID()), tcb);
    }
    
    private boolean updateAttributes(final ProcessPerformanceData.PerfCounterBlock pcb, final ProcessWtsData.WtsInfo wts, final Map<Integer, ThreadPerformanceData.PerfCounterBlock> threadMap) {
        this.name = pcb.getName();
        this.path = wts.getPath();
        this.parentProcessID = pcb.getParentProcessID();
        this.threadCount = wts.getThreadCount();
        this.priority = pcb.getPriority();
        this.virtualSize = wts.getVirtualSize();
        this.residentSetSize = pcb.getResidentSetSize();
        this.kernelTime = wts.getKernelTime();
        this.userTime = wts.getUserTime();
        this.startTime = pcb.getStartTime();
        this.upTime = pcb.getUpTime();
        this.bytesRead = pcb.getBytesRead();
        this.bytesWritten = pcb.getBytesWritten();
        this.openFiles = wts.getOpenFiles();
        this.pageFaults = pcb.getPageFaults();
        this.state = OSProcess.State.RUNNING;
        if (threadMap != null) {
            final int pid = this.getProcessID();
            for (final ThreadPerformanceData.PerfCounterBlock tcb : threadMap.values()) {
                if (tcb.getOwningProcessID() == pid) {
                    if (tcb.getThreadWaitReason() != 5) {
                        this.state = OSProcess.State.RUNNING;
                        break;
                    }
                    this.state = OSProcess.State.SUSPENDED;
                }
            }
        }
        final WinNT.HANDLE pHandle = Kernel32.INSTANCE.OpenProcess(1024, false, this.getProcessID());
        if (pHandle != null) {
            try {
                if (WindowsOSProcess.IS_VISTA_OR_GREATER && this.bitness == 64) {
                    final IntByReference wow64 = new IntByReference(0);
                    if (Kernel32.INSTANCE.IsWow64Process(pHandle, wow64) && wow64.getValue() > 0) {
                        this.bitness = 32;
                    }
                }
                final WinNT.HANDLEByReference phToken = new WinNT.HANDLEByReference();
                try {
                    if (WindowsOSProcess.IS_WINDOWS7_OR_GREATER) {
                        this.path = Kernel32Util.QueryFullProcessImageName(pHandle, 0);
                    }
                }
                catch (Win32Exception e) {
                    this.state = OSProcess.State.INVALID;
                    final WinNT.HANDLE token = phToken.getValue();
                    if (token != null) {}
                }
                finally {
                    final WinNT.HANDLE token2 = phToken.getValue();
                    if (token2 != null) {
                        Kernel32.INSTANCE.CloseHandle(token2);
                    }
                }
            }
            finally {
                Kernel32.INSTANCE.CloseHandle(pHandle);
            }
        }
        return !this.state.equals(OSProcess.State.INVALID);
    }
    
    private String queryCommandLine() {
        if (!this.cwdCmdEnv.get().getB().isEmpty()) {
            return this.cwdCmdEnv.get().getB();
        }
        if (WindowsOSProcess.USE_BATCH_COMMANDLINE) {
            return Win32ProcessCached.getInstance().getCommandLine(this.getProcessID(), this.getStartTime());
        }
        final WbemcliUtil.WmiResult<Win32Process.CommandLineProperty> commandLineProcs = Win32Process.queryCommandLines(Collections.singleton(this.getProcessID()));
        if (commandLineProcs.getResultCount() > 0) {
            return WmiUtil.getString(commandLineProcs, Win32Process.CommandLineProperty.COMMANDLINE, 0);
        }
        return "";
    }
    
    private List<String> queryArguments() {
        final String cl = this.getCommandLine();
        if (!cl.isEmpty()) {
            return Arrays.asList(Shell32Util.CommandLineToArgv(cl));
        }
        return Collections.emptyList();
    }
    
    private String queryCwd() {
        if (!this.cwdCmdEnv.get().getA().isEmpty()) {
            return this.cwdCmdEnv.get().getA();
        }
        if (this.getProcessID() == this.os.getProcessId()) {
            final String cwd = new File(".").getAbsolutePath();
            if (!cwd.isEmpty()) {
                return cwd.substring(0, cwd.length() - 1);
            }
        }
        return "";
    }
    
    private Pair<String, String> queryUserInfo() {
        Pair<String, String> pair = null;
        final WinNT.HANDLE pHandle = Kernel32.INSTANCE.OpenProcess(1024, false, this.getProcessID());
        if (pHandle != null) {
            final WinNT.HANDLEByReference phToken = new WinNT.HANDLEByReference();
            try {
                if (Advapi32.INSTANCE.OpenProcessToken(pHandle, 10, phToken)) {
                    final Advapi32Util.Account account = Advapi32Util.getTokenAccount(phToken.getValue());
                    pair = new Pair<String, String>(account.name, account.sidString);
                }
                else {
                    final int error = Kernel32.INSTANCE.GetLastError();
                    if (error != 5) {
                        WindowsOSProcess.LOG.error("Failed to get process token for process {}: {}", (Object)this.getProcessID(), Kernel32.INSTANCE.GetLastError());
                    }
                }
            }
            catch (Win32Exception e) {
                WindowsOSProcess.LOG.warn("Failed to query user info for process {} ({}): {}", this.getProcessID(), this.getName(), e.getMessage());
            }
            finally {
                final WinNT.HANDLE token = phToken.getValue();
                if (token != null) {
                    Kernel32.INSTANCE.CloseHandle(token);
                }
                Kernel32.INSTANCE.CloseHandle(pHandle);
            }
        }
        if (pair == null) {
            return new Pair<String, String>("unknown", "unknown");
        }
        return pair;
    }
    
    private Pair<String, String> queryGroupInfo() {
        Pair<String, String> pair = null;
        final WinNT.HANDLE pHandle = Kernel32.INSTANCE.OpenProcess(1024, false, this.getProcessID());
        if (pHandle != null) {
            final WinNT.HANDLEByReference phToken = new WinNT.HANDLEByReference();
            if (Advapi32.INSTANCE.OpenProcessToken(pHandle, 10, phToken)) {
                final Advapi32Util.Account account = Advapi32Util.getTokenPrimaryGroup(phToken.getValue());
                pair = new Pair<String, String>(account.name, account.sidString);
            }
            else {
                final int error = Kernel32.INSTANCE.GetLastError();
                if (error != 5) {
                    WindowsOSProcess.LOG.error("Failed to get process token for process {}: {}", (Object)this.getProcessID(), Kernel32.INSTANCE.GetLastError());
                }
            }
            final WinNT.HANDLE token = phToken.getValue();
            if (token != null) {
                Kernel32.INSTANCE.CloseHandle(token);
            }
            Kernel32.INSTANCE.CloseHandle(pHandle);
        }
        if (pair == null) {
            return new Pair<String, String>("unknown", "unknown");
        }
        return pair;
    }
    
    private Triplet<String, String, Map<String, String>> queryCwdCommandlineEnvironment() {
        final WinNT.HANDLE h = Kernel32.INSTANCE.OpenProcess(1040, false, this.getProcessID());
        if (h != null) {
            try {
                if (WindowsOperatingSystem.isX86() == WindowsOperatingSystem.isWow(h)) {
                    final IntByReference nRead = new IntByReference();
                    final NtDll.PROCESS_BASIC_INFORMATION pbi = new NtDll.PROCESS_BASIC_INFORMATION();
                    final int ret = NtDll.INSTANCE.NtQueryInformationProcess(h, 0, pbi.getPointer(), pbi.size(), nRead);
                    if (ret != 0) {
                        return defaultCwdCommandlineEnvironment();
                    }
                    pbi.read();
                    final NtDll.PEB peb = new NtDll.PEB();
                    Kernel32.INSTANCE.ReadProcessMemory(h, pbi.PebBaseAddress, peb.getPointer(), peb.size(), nRead);
                    if (nRead.getValue() == 0) {
                        return defaultCwdCommandlineEnvironment();
                    }
                    peb.read();
                    final NtDll.RTL_USER_PROCESS_PARAMETERS upp = new NtDll.RTL_USER_PROCESS_PARAMETERS();
                    Kernel32.INSTANCE.ReadProcessMemory(h, peb.ProcessParameters, upp.getPointer(), upp.size(), nRead);
                    if (nRead.getValue() == 0) {
                        return defaultCwdCommandlineEnvironment();
                    }
                    upp.read();
                    final String cwd = readUnicodeString(h, upp.CurrentDirectory.DosPath);
                    final String cl = readUnicodeString(h, upp.CommandLine);
                    final int envSize = upp.EnvironmentSize.intValue();
                    if (envSize > 0) {
                        final Memory buffer = new Memory(envSize);
                        Kernel32.INSTANCE.ReadProcessMemory(h, upp.Environment, buffer, envSize, nRead);
                        if (nRead.getValue() > 0) {
                            final char[] env = buffer.getCharArray(0L, envSize / 2);
                            final Map<String, String> envMap = ParseUtil.parseCharArrayToStringMap(env);
                            envMap.remove("");
                            return new Triplet<String, String, Map<String, String>>(cwd, cl, Collections.unmodifiableMap((Map<? extends String, ? extends String>)envMap));
                        }
                    }
                    return new Triplet<String, String, Map<String, String>>(cwd, cl, Collections.emptyMap());
                }
            }
            finally {
                Kernel32.INSTANCE.CloseHandle(h);
            }
        }
        return defaultCwdCommandlineEnvironment();
    }
    
    private static Triplet<String, String, Map<String, String>> defaultCwdCommandlineEnvironment() {
        return new Triplet<String, String, Map<String, String>>("", "", Collections.emptyMap());
    }
    
    private static String readUnicodeString(final WinNT.HANDLE h, final NtDll.UNICODE_STRING s) {
        final IntByReference nRead = new IntByReference();
        if (s.Length > 0) {
            final Memory m = new Memory(s.Length + 2L);
            m.clear();
            Kernel32.INSTANCE.ReadProcessMemory(h, s.Buffer, m, s.Length, nRead);
            if (nRead.getValue() > 0) {
                return m.getWideString(0L);
            }
        }
        return "";
    }
    
    static {
        LOG = LoggerFactory.getLogger(WindowsOSProcess.class);
        USE_BATCH_COMMANDLINE = GlobalConfig.get("oshi.os.windows.commandline.batch", false);
        USE_PROCSTATE_SUSPENDED = GlobalConfig.get("oshi.os.windows.procstate.suspended", false);
        IS_VISTA_OR_GREATER = VersionHelpers.IsWindowsVistaOrGreater();
        IS_WINDOWS7_OR_GREATER = VersionHelpers.IsWindows7OrGreater();
    }
}
