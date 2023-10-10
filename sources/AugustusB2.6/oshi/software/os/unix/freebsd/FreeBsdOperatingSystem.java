// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.freebsd;

import java.util.function.Function;
import java.util.Arrays;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.HashSet;
import oshi.software.os.OSService;
import oshi.software.os.NetworkParams;
import com.sun.jna.Structure;
import oshi.jna.platform.unix.FreeBsdLibc;
import java.util.Map;
import java.util.Iterator;
import oshi.util.ParseUtil;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Collection;
import oshi.software.os.OSProcess;
import oshi.driver.unix.freebsd.Who;
import oshi.software.os.OSSession;
import java.util.List;
import oshi.software.os.InternetProtocolStats;
import oshi.software.os.FileSystem;
import oshi.util.ExecutingCommand;
import oshi.util.platform.unix.freebsd.BsdSysctlUtil;
import oshi.software.os.OperatingSystem;
import oshi.util.tuples.Pair;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractOperatingSystem;

@ThreadSafe
public class FreeBsdOperatingSystem extends AbstractOperatingSystem
{
    private static final Logger LOG;
    private static final long BOOTTIME;
    static final String PS_COMMAND_ARGS;
    
    public String queryManufacturer() {
        return "Unix/BSD";
    }
    
    public Pair<String, OperatingSystem.OSVersionInfo> queryFamilyVersionInfo() {
        final String family = BsdSysctlUtil.sysctl("kern.ostype", "FreeBSD");
        final String version = BsdSysctlUtil.sysctl("kern.osrelease", "");
        final String versionInfo = BsdSysctlUtil.sysctl("kern.version", "");
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
        return new FreeBsdFileSystem();
    }
    
    @Override
    public InternetProtocolStats getInternetProtocolStats() {
        return new FreeBsdInternetProtocolStats();
    }
    
    @Override
    public List<OSSession> getSessions() {
        return FreeBsdOperatingSystem.USE_WHO_COMMAND ? super.getSessions() : Who.queryUtxent();
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
        String psCommand = "ps -awwxo " + FreeBsdOperatingSystem.PS_COMMAND_ARGS;
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
                procs.add(new FreeBsdOSProcess((pid < 0) ? ParseUtil.parseIntOrDefault(psMap.get(PsKeywords.PID), 0) : pid, psMap));
            }
        }
        return procs;
    }
    
    @Override
    public int getProcessId() {
        return FreeBsdLibc.INSTANCE.getpid();
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
        int threads = 0;
        for (final String proc : ExecutingCommand.runNative("ps -axo nlwp")) {
            threads += ParseUtil.parseIntOrDefault(proc.trim(), 0);
        }
        return threads;
    }
    
    @Override
    public long getSystemUptime() {
        return System.currentTimeMillis() / 1000L - FreeBsdOperatingSystem.BOOTTIME;
    }
    
    @Override
    public long getSystemBootTime() {
        return FreeBsdOperatingSystem.BOOTTIME;
    }
    
    private static long querySystemBootTime() {
        final FreeBsdLibc.Timeval tv = new FreeBsdLibc.Timeval();
        if (!BsdSysctlUtil.sysctl("kern.boottime", tv) || tv.tv_sec == 0L) {
            return ParseUtil.parseLongOrDefault(ExecutingCommand.getFirstAnswer("sysctl -n kern.boottime").split(",")[0].replaceAll("\\D", ""), System.currentTimeMillis() / 1000L);
        }
        return tv.tv_sec;
    }
    
    @Override
    public NetworkParams getNetworkParams() {
        return new FreeBsdNetworkParams();
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
            FreeBsdOperatingSystem.LOG.error("Directory: /etc/init does not exist");
        }
        return services;
    }
    
    static {
        LOG = LoggerFactory.getLogger(FreeBsdOperatingSystem.class);
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
        NLWP, 
        PRI, 
        VSZ, 
        RSS, 
        ETIMES, 
        SYSTIME, 
        TIME, 
        COMM, 
        MAJFLT, 
        MINFLT, 
        NVCSW, 
        NIVCSW, 
        ARGS;
        
        private static /* synthetic */ PsKeywords[] $values() {
            return new PsKeywords[] { PsKeywords.STATE, PsKeywords.PID, PsKeywords.PPID, PsKeywords.USER, PsKeywords.UID, PsKeywords.GROUP, PsKeywords.GID, PsKeywords.NLWP, PsKeywords.PRI, PsKeywords.VSZ, PsKeywords.RSS, PsKeywords.ETIMES, PsKeywords.SYSTIME, PsKeywords.TIME, PsKeywords.COMM, PsKeywords.MAJFLT, PsKeywords.MINFLT, PsKeywords.NVCSW, PsKeywords.NIVCSW, PsKeywords.ARGS };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
