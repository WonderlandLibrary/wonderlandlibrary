// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.linux;

import oshi.driver.linux.proc.CpuStat;
import org.slf4j.LoggerFactory;
import oshi.software.os.OSService;
import java.util.Properties;
import oshi.software.os.NetworkParams;
import oshi.driver.linux.proc.UpTime;
import com.sun.jna.Native;
import com.sun.jna.platform.linux.LibC;
import oshi.jna.platform.linux.LinuxLibc;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Set;
import java.io.File;
import java.util.HashSet;
import oshi.driver.linux.proc.ProcessStat;
import oshi.software.os.OSProcess;
import oshi.driver.linux.Who;
import oshi.software.os.OSSession;
import oshi.software.os.InternetProtocolStats;
import oshi.software.os.FileSystem;
import oshi.util.ExecutingCommand;
import java.util.List;
import oshi.util.tuples.Triplet;
import oshi.util.ParseUtil;
import oshi.util.FileUtil;
import oshi.util.platform.linux.ProcPath;
import oshi.software.os.OperatingSystem;
import oshi.util.tuples.Pair;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractOperatingSystem;

@ThreadSafe
public class LinuxOperatingSystem extends AbstractOperatingSystem
{
    private static final Logger LOG;
    private static final String OS_RELEASE_LOG = "os-release: {}";
    private static final String LSB_RELEASE_A_LOG = "lsb_release -a: {}";
    private static final String LSB_RELEASE_LOG = "lsb-release: {}";
    private static final String RELEASE_DELIM = " release ";
    private static final String DOUBLE_QUOTES = "(?:^\")|(?:\"$)";
    private static final String FILENAME_PROPERTIES = "oshi.linux.filename.properties";
    private static final long USER_HZ;
    static final long BOOTTIME;
    private static final int[] PPID_INDEX;
    
    public LinuxOperatingSystem() {
        super.getVersionInfo();
    }
    
    public String queryManufacturer() {
        return "GNU/Linux";
    }
    
    public Pair<String, OperatingSystem.OSVersionInfo> queryFamilyVersionInfo() {
        final Triplet<String, String, String> familyVersionCodename = queryFamilyVersionCodenameFromReleaseFiles();
        String buildNumber = null;
        final List<String> procVersion = FileUtil.readFile(ProcPath.VERSION);
        if (!procVersion.isEmpty()) {
            final String[] split2;
            final String[] split = split2 = ParseUtil.whitespaces.split(procVersion.get(0));
            for (final String s : split2) {
                if (!"Linux".equals(s) && !"version".equals(s)) {
                    buildNumber = s;
                    break;
                }
            }
        }
        final OperatingSystem.OSVersionInfo versionInfo = new OperatingSystem.OSVersionInfo(familyVersionCodename.getB(), familyVersionCodename.getC(), buildNumber);
        return new Pair<String, OperatingSystem.OSVersionInfo>(familyVersionCodename.getA(), versionInfo);
    }
    
    @Override
    protected int queryBitness(final int jvmBitness) {
        if (jvmBitness < 64 && !ExecutingCommand.getFirstAnswer("uname -m").contains("64")) {
            return jvmBitness;
        }
        return 64;
    }
    
    @Override
    public FileSystem getFileSystem() {
        return new LinuxFileSystem();
    }
    
    @Override
    public InternetProtocolStats getInternetProtocolStats() {
        return new LinuxInternetProtocolStats();
    }
    
    @Override
    public List<OSSession> getSessions() {
        return LinuxOperatingSystem.USE_WHO_COMMAND ? super.getSessions() : Who.queryUtxent();
    }
    
    @Override
    public OSProcess getProcess(final int pid) {
        final OSProcess proc = new LinuxOSProcess(pid);
        if (!proc.getState().equals(OSProcess.State.INVALID)) {
            return proc;
        }
        return null;
    }
    
    public List<OSProcess> queryAllProcesses() {
        return this.queryChildProcesses(-1);
    }
    
    public List<OSProcess> queryChildProcesses(final int parentPid) {
        final File[] pidFiles = ProcessStat.getPidFiles();
        if (parentPid >= 0) {
            return queryProcessList(AbstractOperatingSystem.getChildrenOrDescendants(getParentPidsFromProcFiles(pidFiles), parentPid, false));
        }
        final Set<Integer> descendantPids = new HashSet<Integer>();
        for (final File procFile : pidFiles) {
            final int pid = ParseUtil.parseIntOrDefault(procFile.getName(), -2);
            if (pid != -2) {
                descendantPids.add(pid);
            }
        }
        return queryProcessList(descendantPids);
    }
    
    public List<OSProcess> queryDescendantProcesses(final int parentPid) {
        final File[] pidFiles = ProcessStat.getPidFiles();
        return queryProcessList(AbstractOperatingSystem.getChildrenOrDescendants(getParentPidsFromProcFiles(pidFiles), parentPid, true));
    }
    
    private static List<OSProcess> queryProcessList(final Set<Integer> descendantPids) {
        final List<OSProcess> procs = new ArrayList<OSProcess>();
        for (final int pid : descendantPids) {
            final OSProcess proc = new LinuxOSProcess(pid);
            if (!proc.getState().equals(OSProcess.State.INVALID)) {
                procs.add(proc);
            }
        }
        return procs;
    }
    
    private static Map<Integer, Integer> getParentPidsFromProcFiles(final File[] pidFiles) {
        final Map<Integer, Integer> parentPidMap = new HashMap<Integer, Integer>();
        for (final File procFile : pidFiles) {
            final int pid = ParseUtil.parseIntOrDefault(procFile.getName(), 0);
            parentPidMap.put(pid, getParentPidFromProcFile(pid));
        }
        return parentPidMap;
    }
    
    private static int getParentPidFromProcFile(final int pid) {
        final String stat = FileUtil.getStringFromFile(String.format("/proc/%d/stat", pid));
        if (stat.isEmpty()) {
            return 0;
        }
        final long[] statArray = ParseUtil.parseStringToLongArray(stat, LinuxOperatingSystem.PPID_INDEX, ProcessStat.PROC_PID_STAT_LENGTH, ' ');
        return (int)statArray[0];
    }
    
    @Override
    public int getProcessId() {
        return LinuxLibc.INSTANCE.getpid();
    }
    
    @Override
    public int getProcessCount() {
        return ProcessStat.getPidFiles().length;
    }
    
    @Override
    public int getThreadCount() {
        try {
            final LibC.Sysinfo info = new LibC.Sysinfo();
            if (0 != LibC.INSTANCE.sysinfo(info)) {
                LinuxOperatingSystem.LOG.error("Failed to get process thread count. Error code: {}", (Object)Native.getLastError());
                return 0;
            }
            return info.procs;
        }
        catch (UnsatisfiedLinkError | NoClassDefFoundError unsatisfiedLinkError) {
            final LinkageError linkageError;
            final LinkageError e = linkageError;
            LinuxOperatingSystem.LOG.error("Failed to get procs from sysinfo. {}", e.getMessage());
            return 0;
        }
    }
    
    @Override
    public long getSystemUptime() {
        return (long)UpTime.getSystemUptimeSeconds();
    }
    
    @Override
    public long getSystemBootTime() {
        return LinuxOperatingSystem.BOOTTIME;
    }
    
    @Override
    public NetworkParams getNetworkParams() {
        return new LinuxNetworkParams();
    }
    
    private static Triplet<String, String, String> queryFamilyVersionCodenameFromReleaseFiles() {
        Triplet<String, String, String> familyVersionCodename;
        if ((familyVersionCodename = readDistribRelease("/etc/system-release")) != null) {
            return familyVersionCodename;
        }
        if ((familyVersionCodename = readOsRelease()) != null) {
            return familyVersionCodename;
        }
        if ((familyVersionCodename = execLsbRelease()) != null) {
            return familyVersionCodename;
        }
        if ((familyVersionCodename = readLsbRelease()) != null) {
            return familyVersionCodename;
        }
        final String etcDistribRelease = getReleaseFilename();
        if ((familyVersionCodename = readDistribRelease(etcDistribRelease)) != null) {
            return familyVersionCodename;
        }
        final String family = filenameToFamily(etcDistribRelease.replace("/etc/", "").replace("release", "").replace("version", "").replace("-", "").replace("_", ""));
        return new Triplet<String, String, String>(family, "unknown", "unknown");
    }
    
    private static Triplet<String, String, String> readOsRelease() {
        String family = null;
        String versionId = "unknown";
        String codeName = "unknown";
        final List<String> osRelease = FileUtil.readFile("/etc/os-release");
        for (String line : osRelease) {
            if (line.startsWith("VERSION=")) {
                LinuxOperatingSystem.LOG.debug("os-release: {}", line);
                line = line.replace("VERSION=", "").replaceAll("(?:^\")|(?:\"$)", "").trim();
                String[] split = line.split("[()]");
                if (split.length <= 1) {
                    split = line.split(", ");
                }
                if (split.length > 0) {
                    versionId = split[0].trim();
                }
                if (split.length <= 1) {
                    continue;
                }
                codeName = split[1].trim();
            }
            else if (line.startsWith("NAME=") && family == null) {
                LinuxOperatingSystem.LOG.debug("os-release: {}", line);
                family = line.replace("NAME=", "").replaceAll("(?:^\")|(?:\"$)", "").trim();
            }
            else {
                if (!line.startsWith("VERSION_ID=") || !versionId.equals("unknown")) {
                    continue;
                }
                LinuxOperatingSystem.LOG.debug("os-release: {}", line);
                versionId = line.replace("VERSION_ID=", "").replaceAll("(?:^\")|(?:\"$)", "").trim();
            }
        }
        return (family == null) ? null : new Triplet<String, String, String>(family, versionId, codeName);
    }
    
    private static Triplet<String, String, String> execLsbRelease() {
        String family = null;
        String versionId = "unknown";
        String codeName = "unknown";
        for (String line : ExecutingCommand.runNative("lsb_release -a")) {
            if (line.startsWith("Description:")) {
                LinuxOperatingSystem.LOG.debug("lsb_release -a: {}", line);
                line = line.replace("Description:", "").trim();
                if (!line.contains(" release ")) {
                    continue;
                }
                final Triplet<String, String, String> triplet = parseRelease(line, " release ");
                family = triplet.getA();
                if (versionId.equals("unknown")) {
                    versionId = triplet.getB();
                }
                if (!codeName.equals("unknown")) {
                    continue;
                }
                codeName = triplet.getC();
            }
            else if (line.startsWith("Distributor ID:") && family == null) {
                LinuxOperatingSystem.LOG.debug("lsb_release -a: {}", line);
                family = line.replace("Distributor ID:", "").trim();
            }
            else if (line.startsWith("Release:") && versionId.equals("unknown")) {
                LinuxOperatingSystem.LOG.debug("lsb_release -a: {}", line);
                versionId = line.replace("Release:", "").trim();
            }
            else {
                if (!line.startsWith("Codename:") || !codeName.equals("unknown")) {
                    continue;
                }
                LinuxOperatingSystem.LOG.debug("lsb_release -a: {}", line);
                codeName = line.replace("Codename:", "").trim();
            }
        }
        return (family == null) ? null : new Triplet<String, String, String>(family, versionId, codeName);
    }
    
    private static Triplet<String, String, String> readLsbRelease() {
        String family = null;
        String versionId = "unknown";
        String codeName = "unknown";
        final List<String> osRelease = FileUtil.readFile("/etc/lsb-release");
        for (String line : osRelease) {
            if (line.startsWith("DISTRIB_DESCRIPTION=")) {
                LinuxOperatingSystem.LOG.debug("lsb-release: {}", line);
                line = line.replace("DISTRIB_DESCRIPTION=", "").replaceAll("(?:^\")|(?:\"$)", "").trim();
                if (!line.contains(" release ")) {
                    continue;
                }
                final Triplet<String, String, String> triplet = parseRelease(line, " release ");
                family = triplet.getA();
                if (versionId.equals("unknown")) {
                    versionId = triplet.getB();
                }
                if (!codeName.equals("unknown")) {
                    continue;
                }
                codeName = triplet.getC();
            }
            else if (line.startsWith("DISTRIB_ID=") && family == null) {
                LinuxOperatingSystem.LOG.debug("lsb-release: {}", line);
                family = line.replace("DISTRIB_ID=", "").replaceAll("(?:^\")|(?:\"$)", "").trim();
            }
            else if (line.startsWith("DISTRIB_RELEASE=") && versionId.equals("unknown")) {
                LinuxOperatingSystem.LOG.debug("lsb-release: {}", line);
                versionId = line.replace("DISTRIB_RELEASE=", "").replaceAll("(?:^\")|(?:\"$)", "").trim();
            }
            else {
                if (!line.startsWith("DISTRIB_CODENAME=") || !codeName.equals("unknown")) {
                    continue;
                }
                LinuxOperatingSystem.LOG.debug("lsb-release: {}", line);
                codeName = line.replace("DISTRIB_CODENAME=", "").replaceAll("(?:^\")|(?:\"$)", "").trim();
            }
        }
        return (family == null) ? null : new Triplet<String, String, String>(family, versionId, codeName);
    }
    
    private static Triplet<String, String, String> readDistribRelease(final String filename) {
        if (new File(filename).exists()) {
            final List<String> osRelease = FileUtil.readFile(filename);
            for (final String line : osRelease) {
                LinuxOperatingSystem.LOG.debug("{}: {}", filename, line);
                if (line.contains(" release ")) {
                    return parseRelease(line, " release ");
                }
                if (line.contains(" VERSION ")) {
                    return parseRelease(line, " VERSION ");
                }
            }
        }
        return null;
    }
    
    private static Triplet<String, String, String> parseRelease(final String line, final String splitLine) {
        String[] split = line.split(splitLine);
        final String family = split[0].trim();
        String versionId = "unknown";
        String codeName = "unknown";
        if (split.length > 1) {
            split = split[1].split("[()]");
            if (split.length > 0) {
                versionId = split[0].trim();
            }
            if (split.length > 1) {
                codeName = split[1].trim();
            }
        }
        return new Triplet<String, String, String>(family, versionId, codeName);
    }
    
    protected static String getReleaseFilename() {
        final File etc = new File("/etc");
        final File[] matchingFiles = etc.listFiles(f -> (f.getName().endsWith("-release") || f.getName().endsWith("-version") || f.getName().endsWith("_release") || f.getName().endsWith("_version")) && !f.getName().endsWith("os-release") && !f.getName().endsWith("lsb-release") && !f.getName().endsWith("system-release"));
        if (matchingFiles != null && matchingFiles.length > 0) {
            return matchingFiles[0].getPath();
        }
        if (new File("/etc/release").exists()) {
            return "/etc/release";
        }
        return "/etc/issue";
    }
    
    private static String filenameToFamily(final String name) {
        if (name.isEmpty()) {
            return "Solaris";
        }
        if ("issue".equalsIgnoreCase(name)) {
            return "Unknown";
        }
        final Properties filenameProps = FileUtil.readPropertiesFromFilename("oshi.linux.filename.properties");
        final String family = filenameProps.getProperty(name.toLowerCase());
        return (family != null) ? family : (name.substring(0, 1).toUpperCase() + name.substring(1));
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
        boolean systemctlFound = false;
        final List<String> systemctl = ExecutingCommand.runNative("systemctl list-unit-files");
        for (final String str : systemctl) {
            final String[] split = ParseUtil.whitespaces.split(str);
            if (split.length >= 2 && split[0].endsWith(".service") && "enabled".equals(split[1])) {
                final String name2 = split[0].substring(0, split[0].length() - 8);
                final int index = name2.lastIndexOf(46);
                final String shortName = (index < 0 || index > name2.length() - 2) ? name2 : name2.substring(index + 1);
                if (running.contains(name2) || running.contains(shortName)) {
                    continue;
                }
                final OSService s2 = new OSService(name2, 0, OSService.State.STOPPED);
                services.add(s2);
                systemctlFound = true;
            }
        }
        if (!systemctlFound) {
            final File dir = new File("/etc/init");
            if (dir.exists() && dir.isDirectory()) {
                for (final File f : dir.listFiles((f, name) -> name.toLowerCase().endsWith(".conf"))) {
                    final String name3 = f.getName().substring(0, f.getName().length() - 5);
                    final int index2 = name3.lastIndexOf(46);
                    final String shortName2 = (index2 < 0 || index2 > name3.length() - 2) ? name3 : name3.substring(index2 + 1);
                    if (!running.contains(name3) && !running.contains(shortName2)) {
                        final OSService s3 = new OSService(name3, 0, OSService.State.STOPPED);
                        services.add(s3);
                    }
                }
            }
            else {
                LinuxOperatingSystem.LOG.error("Directory: /etc/init does not exist");
            }
        }
        return services;
    }
    
    public static long getHz() {
        return LinuxOperatingSystem.USER_HZ;
    }
    
    static {
        LOG = LoggerFactory.getLogger(LinuxOperatingSystem.class);
        USER_HZ = ParseUtil.parseLongOrDefault(ExecutingCommand.getFirstAnswer("getconf CLK_TCK"), 100L);
        long tempBT = CpuStat.getBootTime();
        if (tempBT == 0L) {
            tempBT = System.currentTimeMillis() / 1000L - (long)UpTime.getSystemUptimeSeconds();
        }
        BOOTTIME = tempBT;
        PPID_INDEX = new int[] { 3 };
    }
}
