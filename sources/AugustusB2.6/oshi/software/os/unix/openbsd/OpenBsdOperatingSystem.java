// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.openbsd;

import java.util.function.Function;
import java.util.Arrays;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.HashSet;
import oshi.software.os.OSService;
import oshi.software.os.NetworkParams;
import oshi.jna.platform.unix.OpenBsdLibc;
import java.util.Map;
import java.util.Iterator;
import oshi.util.ParseUtil;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Collection;
import oshi.software.os.OSProcess;
import java.util.List;
import oshi.software.os.InternetProtocolStats;
import oshi.software.os.FileSystem;
import oshi.util.ExecutingCommand;
import oshi.util.platform.unix.openbsd.OpenBsdSysctlUtil;
import oshi.software.os.OperatingSystem;
import oshi.util.tuples.Pair;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractOperatingSystem;

@ThreadSafe
public class OpenBsdOperatingSystem extends AbstractOperatingSystem
{
    private static final Logger LOG;
    private static final long BOOTTIME;
    static final String PS_COMMAND_ARGS;
    
    public String queryManufacturer() {
        return "Unix/BSD";
    }
    
    public Pair<String, OperatingSystem.OSVersionInfo> queryFamilyVersionInfo() {
        final int[] mib = { 1, 1 };
        final String family = OpenBsdSysctlUtil.sysctl(mib, "OpenBSD");
        mib[1] = 2;
        final String version = OpenBsdSysctlUtil.sysctl(mib, "");
        mib[1] = 4;
        final String versionInfo = OpenBsdSysctlUtil.sysctl(mib, "");
        final String buildNumber = versionInfo.split(":")[0].replace(family, "").replace(version, "").trim();
        return new Pair<String, OperatingSystem.OSVersionInfo>(family, new OperatingSystem.OSVersionInfo(version, null, buildNumber));
    }
    
    @Override
    protected int queryBitness(final int jvmBitness) {
        if (jvmBitness < 64 && ExecutingCommand.getFirstAnswer("uname -m").indexOf("64") == -1) {
            return jvmBitness;
        }
        return 64;
    }
    
    @Override
    public FileSystem getFileSystem() {
        return new OpenBsdFileSystem();
    }
    
    @Override
    public InternetProtocolStats getInternetProtocolStats() {
        return new OpenBsdInternetProtocolStats();
    }
    
    public List<OSProcess> queryAllProcesses() {
        return getProcessListFromPS(-1);
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
    public OSProcess getProcess(final int pid) {
        final List<OSProcess> procs = getProcessListFromPS(pid);
        if (procs.isEmpty()) {
            return null;
        }
        return procs.get(0);
    }
    
    private static List<OSProcess> getProcessListFromPS(final int pid) {
        final List<OSProcess> procs = new ArrayList<OSProcess>();
        String psCommand = "ps -awwxo " + OpenBsdOperatingSystem.PS_COMMAND_ARGS;
        if (pid >= 0) {
            psCommand = psCommand + " -p " + pid;
        }
        final List<String> procList = ExecutingCommand.runNative(psCommand);
        if (procList.isEmpty() || procList.size() < 2) {
            return procs;
        }
        procList.remove(0);
        for (final String proc : procList) {
            final Map<PsKeywords, String> psMap = ParseUtil.stringToEnumMap(PsKeywords.class, proc.trim(), ' ');
            if (psMap.containsKey(PsKeywords.ARGS)) {
                procs.add(new OpenBsdOSProcess((pid < 0) ? ParseUtil.parseIntOrDefault(psMap.get(PsKeywords.PID), 0) : pid, psMap));
            }
        }
        return procs;
    }
    
    @Override
    public int getProcessId() {
        return OpenBsdLibc.INSTANCE.getpid();
    }
    
    @Override
    public int getProcessCount() {
        final List<String> procList = ExecutingCommand.runNative("ps -axo pid");
        if (!procList.isEmpty()) {
            return procList.size() - 1;
        }
        return 0;
    }
    
    @Override
    public int getThreadCount() {
        final List<String> threadList = ExecutingCommand.runNative("ps -axHo tid");
        if (!threadList.isEmpty()) {
            return threadList.size() - 1;
        }
        return 0;
    }
    
    @Override
    public long getSystemUptime() {
        return System.currentTimeMillis() / 1000L - OpenBsdOperatingSystem.BOOTTIME;
    }
    
    @Override
    public long getSystemBootTime() {
        return OpenBsdOperatingSystem.BOOTTIME;
    }
    
    private static long querySystemBootTime() {
        return ParseUtil.parseLongOrDefault(ExecutingCommand.getFirstAnswer("sysctl -n kern.boottime").split(",")[0].replaceAll("\\D", ""), System.currentTimeMillis() / 1000L);
    }
    
    @Override
    public NetworkParams getNetworkParams() {
        return new OpenBsdNetworkParams();
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
        final File dir = new File("/etc/rc.d");
        final File[] listFiles;
        if (dir.exists() && dir.isDirectory() && (listFiles = dir.listFiles()) != null) {
            for (final File f : listFiles) {
                final String name = f.getName();
                if (!running.contains(name)) {
                    final OSService s2 = new OSService(name, 0, OSService.State.STOPPED);
                    services.add(s2);
                }
            }
        }
        else {
            OpenBsdOperatingSystem.LOG.error("Directory: /etc/rc.d does not exist");
        }
        return services;
    }
    
    static {
        LOG = LoggerFactory.getLogger(OpenBsdOperatingSystem.class);
        BOOTTIME = querySystemBootTime();
        PS_COMMAND_ARGS = Arrays.stream(PsKeywords.values()).map((Function<? super PsKeywords, ?>)Enum::name).map((Function<? super Object, ?>)String::toLowerCase).collect((Collector<? super Object, ?, String>)Collectors.joining(","));
    }
    
    enum PsKeywords
    {
        STATE, 
        PID, 
        PPID, 
        USER, 
        UID, 
        GROUP, 
        GID, 
        PRI, 
        VSZ, 
        RSS, 
        ETIME, 
        CPUTIME, 
        COMM, 
        MAJFLT, 
        MINFLT, 
        NVCSW, 
        NIVCSW, 
        ARGS;
        
        private static /* synthetic */ PsKeywords[] $values() {
            return new PsKeywords[] { PsKeywords.STATE, PsKeywords.PID, PsKeywords.PPID, PsKeywords.USER, PsKeywords.UID, PsKeywords.GROUP, PsKeywords.GID, PsKeywords.PRI, PsKeywords.VSZ, PsKeywords.RSS, PsKeywords.ETIME, PsKeywords.CPUTIME, PsKeywords.COMM, PsKeywords.MAJFLT, PsKeywords.MINFLT, PsKeywords.NVCSW, PsKeywords.NIVCSW, PsKeywords.ARGS };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
