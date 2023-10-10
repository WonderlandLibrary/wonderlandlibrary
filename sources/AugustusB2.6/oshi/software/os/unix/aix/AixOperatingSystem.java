// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.aix;

import java.io.File;
import oshi.util.ParseUtil;
import oshi.software.os.OSService;
import oshi.software.os.NetworkParams;
import oshi.driver.unix.aix.Uptime;
import oshi.driver.unix.aix.Who;
import oshi.jna.platform.unix.AixLibc;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Collection;
import oshi.software.os.OSProcess;
import java.util.List;
import oshi.software.os.InternetProtocolStats;
import oshi.software.os.FileSystem;
import com.sun.jna.Native;
import oshi.util.ExecutingCommand;
import oshi.util.Util;
import oshi.software.os.OperatingSystem;
import oshi.util.tuples.Pair;
import oshi.driver.unix.aix.perfstat.PerfstatProcess;
import oshi.util.Memoizer;
import oshi.driver.unix.aix.perfstat.PerfstatConfig;
import com.sun.jna.platform.unix.aix.Perfstat;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractOperatingSystem;

@ThreadSafe
public class AixOperatingSystem extends AbstractOperatingSystem
{
    private final Supplier<Perfstat.perfstat_partition_config_t> config;
    private final Supplier<Perfstat.perfstat_process_t[]> procCpu;
    private static final long BOOTTIME;
    
    public AixOperatingSystem() {
        this.config = Memoizer.memoize(PerfstatConfig::queryConfig);
        this.procCpu = Memoizer.memoize(PerfstatProcess::queryProcesses, Memoizer.defaultExpiration());
    }
    
    public String queryManufacturer() {
        return "IBM";
    }
    
    public Pair<String, OperatingSystem.OSVersionInfo> queryFamilyVersionInfo() {
        final Perfstat.perfstat_partition_config_t cfg = this.config.get();
        final String systemName = System.getProperty("os.name");
        final String archName = System.getProperty("os.arch");
        String versionNumber = System.getProperty("os.version");
        if (Util.isBlank(versionNumber)) {
            versionNumber = ExecutingCommand.getFirstAnswer("oslevel");
        }
        String releaseNumber = Native.toString(cfg.OSBuild);
        if (Util.isBlank(releaseNumber)) {
            releaseNumber = ExecutingCommand.getFirstAnswer("oslevel -s");
        }
        else {
            final int idx = releaseNumber.lastIndexOf(32);
            if (idx > 0 && idx < releaseNumber.length()) {
                releaseNumber = releaseNumber.substring(idx + 1);
            }
        }
        return new Pair<String, OperatingSystem.OSVersionInfo>(systemName, new OperatingSystem.OSVersionInfo(versionNumber, archName, releaseNumber));
    }
    
    @Override
    protected int queryBitness(final int jvmBitness) {
        if (jvmBitness == 64) {
            return 64;
        }
        return ((this.config.get().conf & 0x800000) > 0) ? 64 : 32;
    }
    
    @Override
    public FileSystem getFileSystem() {
        return new AixFileSystem();
    }
    
    @Override
    public InternetProtocolStats getInternetProtocolStats() {
        return new AixInternetProtocolStats();
    }
    
    public List<OSProcess> queryAllProcesses() {
        return this.getProcessListFromProcfs(-1);
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
        final List<OSProcess> procs = this.getProcessListFromProcfs(pid);
        if (procs.isEmpty()) {
            return null;
        }
        return procs.get(0);
    }
    
    private List<OSProcess> getProcessListFromProcfs(final int pid) {
        final List<OSProcess> procs = new ArrayList<OSProcess>();
        final Perfstat.perfstat_process_t[] perfstat = this.procCpu.get();
        final Map<Integer, Pair<Long, Long>> cpuMap = new HashMap<Integer, Pair<Long, Long>>();
        for (final Perfstat.perfstat_process_t stat : perfstat) {
            final int statpid = (int)stat.pid;
            if (pid < 0 || statpid == pid) {
                cpuMap.put(statpid, new Pair<Long, Long>((long)stat.ucpu_time, (long)stat.scpu_time));
            }
        }
        for (final Map.Entry<Integer, Pair<Long, Long>> entry : cpuMap.entrySet()) {
            final OSProcess proc = new AixOSProcess(entry.getKey(), entry.getValue(), this.procCpu);
            if (proc.getState() != OSProcess.State.INVALID) {
                procs.add(proc);
            }
        }
        return procs;
    }
    
    @Override
    public int getProcessId() {
        return AixLibc.INSTANCE.getpid();
    }
    
    @Override
    public int getProcessCount() {
        return this.procCpu.get().length;
    }
    
    @Override
    public int getThreadCount() {
        long tc = 0L;
        for (final Perfstat.perfstat_process_t proc : this.procCpu.get()) {
            tc += proc.num_threads;
        }
        return (int)tc;
    }
    
    @Override
    public long getSystemUptime() {
        return System.currentTimeMillis() / 1000L - AixOperatingSystem.BOOTTIME;
    }
    
    @Override
    public long getSystemBootTime() {
        return AixOperatingSystem.BOOTTIME;
    }
    
    private static long querySystemBootTimeMillis() {
        final long bootTime = Who.queryBootTime();
        if (bootTime >= 1000L) {
            return bootTime;
        }
        return System.currentTimeMillis() - Uptime.queryUpTime();
    }
    
    @Override
    public NetworkParams getNetworkParams() {
        return new AixNetworkParams();
    }
    
    @Override
    public List<OSService> getServices() {
        final List<OSService> services = new ArrayList<OSService>();
        final List<String> systemServicesInfoList = ExecutingCommand.runNative("lssrc -a");
        if (systemServicesInfoList.size() > 1) {
            systemServicesInfoList.remove(0);
            for (final String systemService : systemServicesInfoList) {
                final String[] serviceSplit = ParseUtil.whitespaces.split(systemService.trim());
                if (systemService.contains("active")) {
                    if (serviceSplit.length == 4) {
                        services.add(new OSService(serviceSplit[0], ParseUtil.parseIntOrDefault(serviceSplit[2], 0), OSService.State.RUNNING));
                    }
                    else {
                        if (serviceSplit.length != 3) {
                            continue;
                        }
                        services.add(new OSService(serviceSplit[0], ParseUtil.parseIntOrDefault(serviceSplit[1], 0), OSService.State.RUNNING));
                    }
                }
                else {
                    if (!systemService.contains("inoperative")) {
                        continue;
                    }
                    services.add(new OSService(serviceSplit[0], 0, OSService.State.STOPPED));
                }
            }
        }
        final File dir = new File("/etc/rc.d/init.d");
        final File[] listFiles;
        if (dir.exists() && dir.isDirectory() && (listFiles = dir.listFiles()) != null) {
            for (final File file : listFiles) {
                final String installedService = ExecutingCommand.getFirstAnswer(file.getAbsolutePath() + " status");
                if (installedService.contains("running")) {
                    services.add(new OSService(file.getName(), ParseUtil.parseLastInt(installedService, 0), OSService.State.RUNNING));
                }
                else {
                    services.add(new OSService(file.getName(), 0, OSService.State.STOPPED));
                }
            }
        }
        return services;
    }
    
    static {
        BOOTTIME = querySystemBootTimeMillis() / 1000L;
    }
}
