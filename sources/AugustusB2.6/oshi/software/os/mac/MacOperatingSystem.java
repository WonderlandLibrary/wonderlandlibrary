// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.mac;

import org.slf4j.LoggerFactory;
import oshi.driver.mac.WindowInfo;
import oshi.software.os.OSDesktopWindow;
import java.util.Iterator;
import java.util.Arrays;
import java.io.File;
import java.util.HashSet;
import oshi.software.os.OSService;
import oshi.software.os.NetworkParams;
import com.sun.jna.Structure;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Collection;
import com.sun.jna.platform.mac.SystemB;
import java.util.ArrayList;
import oshi.software.os.OSProcess;
import oshi.driver.mac.Who;
import oshi.software.os.OSSession;
import java.util.List;
import oshi.software.os.InternetProtocolStats;
import oshi.software.os.FileSystem;
import java.util.Properties;
import oshi.util.Util;
import oshi.util.FileUtil;
import oshi.software.os.OperatingSystem;
import oshi.util.tuples.Pair;
import oshi.util.platform.mac.SysctlUtil;
import oshi.util.ExecutingCommand;
import oshi.util.ParseUtil;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractOperatingSystem;

@ThreadSafe
public class MacOperatingSystem extends AbstractOperatingSystem
{
    private static final Logger LOG;
    public static final String MACOS_VERSIONS_PROPERTIES = "oshi.macos.versions.properties";
    private static final String SYSTEM_LIBRARY_LAUNCH_AGENTS = "/System/Library/LaunchAgents";
    private static final String SYSTEM_LIBRARY_LAUNCH_DAEMONS = "/System/Library/LaunchDaemons";
    private int maxProc;
    private final String osXVersion;
    private final int major;
    private final int minor;
    private static final long BOOTTIME;
    
    public MacOperatingSystem() {
        this.maxProc = 1024;
        String version = System.getProperty("os.version");
        int verMajor = ParseUtil.getFirstIntValue(version);
        int verMinor = ParseUtil.getNthIntValue(version, 2);
        if (verMajor == 10 && verMinor > 15) {
            final String swVers = ExecutingCommand.getFirstAnswer("sw_vers -productVersion");
            if (!swVers.isEmpty()) {
                version = swVers;
            }
            verMajor = ParseUtil.getFirstIntValue(version);
            verMinor = ParseUtil.getNthIntValue(version, 2);
        }
        this.osXVersion = version;
        this.major = verMajor;
        this.minor = verMinor;
        this.maxProc = SysctlUtil.sysctl("kern.maxproc", 4096);
    }
    
    public String queryManufacturer() {
        return "Apple";
    }
    
    public Pair<String, OperatingSystem.OSVersionInfo> queryFamilyVersionInfo() {
        final String family = (this.major > 10 || (this.major == 10 && this.minor >= 12)) ? "macOS" : System.getProperty("os.name");
        final String codeName = this.parseCodeName();
        final String buildNumber = SysctlUtil.sysctl("kern.osversion", "");
        return new Pair<String, OperatingSystem.OSVersionInfo>(family, new OperatingSystem.OSVersionInfo(this.osXVersion, codeName, buildNumber));
    }
    
    private String parseCodeName() {
        final Properties verProps = FileUtil.readPropertiesFromFilename("oshi.macos.versions.properties");
        String codeName = null;
        if (this.major > 10) {
            codeName = verProps.getProperty(Integer.toString(this.major));
        }
        else if (this.major == 10) {
            codeName = verProps.getProperty(this.major + "." + this.minor);
        }
        if (Util.isBlank(codeName)) {
            MacOperatingSystem.LOG.warn("Unable to parse version {}.{} to a codename.", (Object)this.major, this.minor);
        }
        return codeName;
    }
    
    @Override
    protected int queryBitness(final int jvmBitness) {
        if (jvmBitness == 64 || (this.major == 10 && this.minor > 6)) {
            return 64;
        }
        return ParseUtil.parseIntOrDefault(ExecutingCommand.getFirstAnswer("getconf LONG_BIT"), 32);
    }
    
    @Override
    public FileSystem getFileSystem() {
        return new MacFileSystem();
    }
    
    @Override
    public InternetProtocolStats getInternetProtocolStats() {
        return new MacInternetProtocolStats(this.isElevated());
    }
    
    @Override
    public List<OSSession> getSessions() {
        return MacOperatingSystem.USE_WHO_COMMAND ? super.getSessions() : Who.queryUtxent();
    }
    
    public List<OSProcess> queryAllProcesses() {
        final List<OSProcess> procs = new ArrayList<OSProcess>();
        final int[] pids = new int[this.maxProc];
        for (int numberOfProcesses = SystemB.INSTANCE.proc_listpids(1, 0, pids, pids.length * SystemB.INT_SIZE) / SystemB.INT_SIZE, i = 0; i < numberOfProcesses; ++i) {
            if (pids[i] != 0) {
                final OSProcess proc = this.getProcess(pids[i]);
                if (proc != null) {
                    procs.add(proc);
                }
            }
        }
        return procs;
    }
    
    @Override
    public OSProcess getProcess(final int pid) {
        final OSProcess proc = new MacOSProcess(pid, this.minor);
        return proc.getState().equals(OSProcess.State.INVALID) ? null : proc;
    }
    
    public List<OSProcess> queryChildProcesses(final int parentPid) {
        final List<OSProcess> allProcs = this.queryAllProcesses();
        final Set<Integer> descendantPids = AbstractOperatingSystem.getChildrenOrDescendants(allProcs, parentPid, false);
        return allProcs.stream().filter(p -> descendantPids.contains(p.getProcessID())).collect((Collector<? super Object, ?, List<OSProcess>>)Collectors.toList());
    }
    
    public List<OSProcess> queryDescendantProcesses(final int parentPid) {
        final List<OSProcess> allProcs = this.queryAllProcesses();
        final Set<Integer> descendantPids = AbstractOperatingSystem.getChildrenOrDescendants(allProcs, parentPid, true);
        return allProcs.stream().filter(p -> descendantPids.contains(p.getProcessID())).collect((Collector<? super Object, ?, List<OSProcess>>)Collectors.toList());
    }
    
    @Override
    public int getProcessId() {
        return SystemB.INSTANCE.getpid();
    }
    
    @Override
    public int getProcessCount() {
        return SystemB.INSTANCE.proc_listpids(1, 0, null, 0) / SystemB.INT_SIZE;
    }
    
    @Override
    public int getThreadCount() {
        final int[] pids = new int[this.getProcessCount() + 10];
        final int numberOfProcesses = SystemB.INSTANCE.proc_listpids(1, 0, pids, pids.length) / SystemB.INT_SIZE;
        int numberOfThreads = 0;
        final SystemB.ProcTaskInfo taskInfo = new SystemB.ProcTaskInfo();
        for (int i = 0; i < numberOfProcesses; ++i) {
            final int exit = SystemB.INSTANCE.proc_pidinfo(pids[i], 4, 0L, taskInfo, taskInfo.size());
            if (exit != -1) {
                numberOfThreads += taskInfo.pti_threadnum;
            }
        }
        return numberOfThreads;
    }
    
    @Override
    public long getSystemUptime() {
        return System.currentTimeMillis() / 1000L - MacOperatingSystem.BOOTTIME;
    }
    
    @Override
    public long getSystemBootTime() {
        return MacOperatingSystem.BOOTTIME;
    }
    
    @Override
    public NetworkParams getNetworkParams() {
        return new MacNetworkParams();
    }
    
    @Override
    public List<OSService> getServices() {
        final List<OSService> services = new ArrayList<OSService>();
        final Set<String> running = new HashSet<String>();
        for (final OSProcess p : this.getChildProcesses(1, OperatingSystem.ProcessFiltering.ALL_PROCESSES, OperatingSystem.ProcessSorting.PID_ASC, 0)) {
            final OSService s = new OSService(p.getName(), p.getProcessID(), OSService.State.RUNNING);
            services.add(s);
            running.add(p.getName());
        }
        final ArrayList<File> files = new ArrayList<File>();
        File dir = new File("/System/Library/LaunchAgents");
        if (dir.exists() && dir.isDirectory()) {
            files.addAll(Arrays.asList(dir.listFiles((f, name) -> name.toLowerCase().endsWith(".plist"))));
        }
        else {
            MacOperatingSystem.LOG.error("Directory: /System/Library/LaunchAgents does not exist");
        }
        dir = new File("/System/Library/LaunchDaemons");
        if (dir.exists() && dir.isDirectory()) {
            files.addAll(Arrays.asList(dir.listFiles((f, name) -> name.toLowerCase().endsWith(".plist"))));
        }
        else {
            MacOperatingSystem.LOG.error("Directory: /System/Library/LaunchDaemons does not exist");
        }
        for (final File f : files) {
            final String name2 = f.getName().substring(0, f.getName().length() - 6);
            final int index = name2.lastIndexOf(46);
            final String shortName = (index < 0 || index > name2.length() - 2) ? name2 : name2.substring(index + 1);
            if (!running.contains(name2) && !running.contains(shortName)) {
                final OSService s2 = new OSService(name2, 0, OSService.State.STOPPED);
                services.add(s2);
            }
        }
        return services;
    }
    
    @Override
    public List<OSDesktopWindow> getDesktopWindows(final boolean visibleOnly) {
        return WindowInfo.queryDesktopWindows(visibleOnly);
    }
    
    static {
        LOG = LoggerFactory.getLogger(MacOperatingSystem.class);
        final SystemB.Timeval tv = new SystemB.Timeval();
        if (!SysctlUtil.sysctl("kern.boottime", tv) || tv.tv_sec.longValue() == 0L) {
            BOOTTIME = ParseUtil.parseLongOrDefault(ExecutingCommand.getFirstAnswer("sysctl -n kern.boottime").split(",")[0].replaceAll("\\D", ""), System.currentTimeMillis() / 1000L);
        }
        else {
            BOOTTIME = tv.tv_sec.longValue();
        }
    }
}
