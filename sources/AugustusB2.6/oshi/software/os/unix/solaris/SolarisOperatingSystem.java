// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.solaris;

import oshi.util.Memoizer;
import java.util.Iterator;
import oshi.software.os.OSService;
import oshi.software.os.NetworkParams;
import com.sun.jna.platform.unix.solaris.LibKstat;
import oshi.util.platform.unix.solaris.KstatUtil;
import oshi.driver.linux.proc.ProcessStat;
import oshi.jna.platform.unix.SolarisLibc;
import oshi.util.Constants;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Collection;
import oshi.software.os.OSProcess;
import oshi.driver.unix.solaris.Who;
import oshi.software.os.OSSession;
import java.util.List;
import oshi.software.os.InternetProtocolStats;
import oshi.software.os.FileSystem;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import oshi.software.os.OperatingSystem;
import oshi.util.tuples.Pair;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractOperatingSystem;

@ThreadSafe
public class SolarisOperatingSystem extends AbstractOperatingSystem
{
    private static final String VERSION;
    private static final String BUILD_NUMBER;
    public static final boolean IS_11_4_OR_HIGHER;
    private static final Supplier<Pair<Long, Long>> BOOT_UPTIME;
    private static final long BOOTTIME;
    
    public String queryManufacturer() {
        return "Oracle";
    }
    
    public Pair<String, OperatingSystem.OSVersionInfo> queryFamilyVersionInfo() {
        return new Pair<String, OperatingSystem.OSVersionInfo>("SunOS", new OperatingSystem.OSVersionInfo(SolarisOperatingSystem.VERSION, "Solaris", SolarisOperatingSystem.BUILD_NUMBER));
    }
    
    @Override
    protected int queryBitness(final int jvmBitness) {
        if (jvmBitness == 64) {
            return 64;
        }
        return ParseUtil.parseIntOrDefault(ExecutingCommand.getFirstAnswer("isainfo -b"), 32);
    }
    
    @Override
    public FileSystem getFileSystem() {
        return new SolarisFileSystem();
    }
    
    @Override
    public InternetProtocolStats getInternetProtocolStats() {
        return new SolarisInternetProtocolStats();
    }
    
    @Override
    public List<OSSession> getSessions() {
        return SolarisOperatingSystem.USE_WHO_COMMAND ? super.getSessions() : Who.queryUtxent();
    }
    
    @Override
    public OSProcess getProcess(final int pid) {
        final List<OSProcess> procs = getProcessListFromProcfs(pid);
        if (procs.isEmpty()) {
            return null;
        }
        return procs.get(0);
    }
    
    public List<OSProcess> queryAllProcesses() {
        return queryAllProcessesFromPrStat();
    }
    
    public List<OSProcess> queryChildProcesses(final int parentPid) {
        final List<OSProcess> allProcs = queryAllProcessesFromPrStat();
        final Set<Integer> descendantPids = AbstractOperatingSystem.getChildrenOrDescendants(allProcs, parentPid, false);
        return allProcs.stream().filter(p -> descendantPids.contains(p.getProcessID())).collect((Collector<? super Object, ?, List<OSProcess>>)Collectors.toList());
    }
    
    public List<OSProcess> queryDescendantProcesses(final int parentPid) {
        final List<OSProcess> allProcs = queryAllProcessesFromPrStat();
        final Set<Integer> descendantPids = AbstractOperatingSystem.getChildrenOrDescendants(allProcs, parentPid, true);
        return allProcs.stream().filter(p -> descendantPids.contains(p.getProcessID())).collect((Collector<? super Object, ?, List<OSProcess>>)Collectors.toList());
    }
    
    private static List<OSProcess> queryAllProcessesFromPrStat() {
        return getProcessListFromProcfs(-1);
    }
    
    private static List<OSProcess> getProcessListFromProcfs(final int pid) {
        final List<OSProcess> procs = new ArrayList<OSProcess>();
        File[] numericFiles = null;
        if (pid < 0) {
            final File directory = new File("/proc");
            numericFiles = directory.listFiles(file -> Constants.DIGITS.matcher(file.getName()).matches());
        }
        else {
            final File pidFile = new File("/proc/" + pid);
            if (pidFile.exists()) {
                numericFiles = new File[] { pidFile };
            }
        }
        if (numericFiles == null) {
            return procs;
        }
        for (final File pidFile2 : numericFiles) {
            final int pidNum = ParseUtil.parseIntOrDefault(pidFile2.getName(), 0);
            final OSProcess proc = new SolarisOSProcess(pidNum);
            if (proc.getState() != OSProcess.State.INVALID) {
                procs.add(proc);
            }
        }
        return procs;
    }
    
    @Override
    public int getProcessId() {
        return SolarisLibc.INSTANCE.getpid();
    }
    
    @Override
    public int getProcessCount() {
        return ProcessStat.getPidFiles().length;
    }
    
    @Override
    public int getThreadCount() {
        final List<String> threadList = ExecutingCommand.runNative("ps -eLo pid");
        if (!threadList.isEmpty()) {
            return threadList.size() - 1;
        }
        return this.getProcessCount();
    }
    
    @Override
    public long getSystemUptime() {
        return querySystemUptime();
    }
    
    private static long querySystemUptime() {
        if (SolarisOperatingSystem.IS_11_4_OR_HIGHER) {
            return SolarisOperatingSystem.BOOT_UPTIME.get().getB();
        }
        final KstatUtil.KstatChain kc = KstatUtil.openChain();
        try {
            final LibKstat.Kstat ksp = KstatUtil.KstatChain.lookup("unix", 0, "system_misc");
            if (ksp != null) {
                final long n = ksp.ks_snaptime / 1000000000L;
                if (kc != null) {
                    kc.close();
                }
                return n;
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
    public long getSystemBootTime() {
        return SolarisOperatingSystem.BOOTTIME;
    }
    
    private static long querySystemBootTime() {
        if (SolarisOperatingSystem.IS_11_4_OR_HIGHER) {
            return SolarisOperatingSystem.BOOT_UPTIME.get().getA();
        }
        final KstatUtil.KstatChain kc = KstatUtil.openChain();
        try {
            final LibKstat.Kstat ksp = KstatUtil.KstatChain.lookup("unix", 0, "system_misc");
            if (ksp != null && KstatUtil.KstatChain.read(ksp)) {
                final long dataLookupLong = KstatUtil.dataLookupLong(ksp, "boot_time");
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
        return System.currentTimeMillis() / 1000L - querySystemUptime();
    }
    
    private static Pair<Long, Long> queryBootAndUptime() {
        final Object[] results = KstatUtil.queryKstat2("/misc/unix/system_misc", "boot_time", "snaptime");
        final long boot = (long)((results[0] == null) ? System.currentTimeMillis() : results[0]);
        final long snap = (results[1] == null) ? 0L : ((long)results[1] / 1000000000L);
        return new Pair<Long, Long>(boot, snap);
    }
    
    @Override
    public NetworkParams getNetworkParams() {
        return new SolarisNetworkParams();
    }
    
    @Override
    public List<OSService> getServices() {
        final List<OSService> services = new ArrayList<OSService>();
        final List<String> legacySvcs = new ArrayList<String>();
        final File dir = new File("/etc/init.d");
        final File[] listFiles;
        if (dir.exists() && dir.isDirectory() && (listFiles = dir.listFiles()) != null) {
            for (final File f : listFiles) {
                legacySvcs.add(f.getName());
            }
        }
        final List<String> svcs = ExecutingCommand.runNative("svcs -p");
        for (final String line : svcs) {
            if (line.startsWith("online")) {
                final int delim = line.lastIndexOf(":/");
                if (delim <= 0) {
                    continue;
                }
                String name = line.substring(delim + 1);
                if (name.endsWith(":default")) {
                    name = name.substring(0, name.length() - 8);
                }
                services.add(new OSService(name, 0, OSService.State.STOPPED));
            }
            else if (line.startsWith(" ")) {
                final String[] split = ParseUtil.whitespaces.split(line.trim());
                if (split.length != 3) {
                    continue;
                }
                services.add(new OSService(split[2], ParseUtil.parseIntOrDefault(split[1], 0), OSService.State.RUNNING));
            }
            else {
                if (!line.startsWith("legacy_run")) {
                    continue;
                }
                for (final String svc : legacySvcs) {
                    if (line.endsWith(svc)) {
                        services.add(new OSService(svc, 0, OSService.State.STOPPED));
                        break;
                    }
                }
            }
        }
        return services;
    }
    
    static {
        final String[] split = ParseUtil.whitespaces.split(ExecutingCommand.getFirstAnswer("uname -rv"));
        VERSION = split[0];
        BUILD_NUMBER = ((split.length > 1) ? split[1] : "");
        IS_11_4_OR_HIGHER = ("11.4".compareTo(SolarisOperatingSystem.BUILD_NUMBER) <= 0);
        BOOT_UPTIME = Memoizer.memoize(SolarisOperatingSystem::queryBootAndUptime, Memoizer.defaultExpiration());
        BOOTTIME = querySystemBootTime();
    }
}
