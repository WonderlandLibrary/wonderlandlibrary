// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.linux;

import org.slf4j.LoggerFactory;
import oshi.util.Util;
import java.nio.file.Path;
import oshi.util.UserGroupInfo;
import oshi.hardware.platform.linux.LinuxGlobalMemory;
import java.nio.file.InvalidPathException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.math.BigInteger;
import oshi.util.ExecutingCommand;
import java.io.InputStream;
import java.io.FileInputStream;
import oshi.driver.linux.proc.ProcessStat;
import oshi.software.os.OSThread;
import java.io.IOException;
import java.io.File;
import java.util.Collections;
import oshi.util.ParseUtil;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Arrays;
import oshi.util.FileUtil;
import oshi.util.platform.linux.ProcPath;
import oshi.util.Memoizer;
import oshi.software.os.OSProcess;
import java.util.Map;
import java.util.List;
import java.util.function.Supplier;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractOSProcess;

@ThreadSafe
public class LinuxOSProcess extends AbstractOSProcess
{
    private static final Logger LOG;
    private static final int[] PROC_PID_STAT_ORDERS;
    private Supplier<Integer> bitness;
    private Supplier<String> commandLine;
    private Supplier<List<String>> arguments;
    private Supplier<Map<String, String>> environmentVariables;
    private String name;
    private String path;
    private String user;
    private String userID;
    private String group;
    private String groupID;
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
    private long minorFaults;
    private long majorFaults;
    private long contextSwitches;
    
    public LinuxOSProcess(final int pid) {
        super(pid);
        this.bitness = Memoizer.memoize(this::queryBitness);
        this.commandLine = Memoizer.memoize(this::queryCommandLine);
        this.arguments = Memoizer.memoize(this::queryArguments);
        this.environmentVariables = Memoizer.memoize(this::queryEnvironmentVariables);
        this.path = "";
        this.state = OSProcess.State.INVALID;
        this.updateAttributes();
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
    
    private String queryCommandLine() {
        return Arrays.stream(FileUtil.getStringFromFile(String.format(ProcPath.PID_CMDLINE, this.getProcessID())).split("\u0000")).collect(Collectors.joining(" "));
    }
    
    @Override
    public List<String> getArguments() {
        return this.arguments.get();
    }
    
    private List<String> queryArguments() {
        return Collections.unmodifiableList((List<? extends String>)ParseUtil.parseByteArrayToStrings(FileUtil.readAllBytes(String.format(ProcPath.PID_CMDLINE, this.getProcessID()))));
    }
    
    @Override
    public Map<String, String> getEnvironmentVariables() {
        return this.environmentVariables.get();
    }
    
    private Map<String, String> queryEnvironmentVariables() {
        return Collections.unmodifiableMap((Map<? extends String, ? extends String>)ParseUtil.parseByteArrayToStringMap(FileUtil.readAllBytes(String.format(ProcPath.PID_ENVIRON, this.getProcessID()))));
    }
    
    @Override
    public String getCurrentWorkingDirectory() {
        try {
            final String cwdLink = String.format(ProcPath.PID_CWD, this.getProcessID());
            final String cwd = new File(cwdLink).getCanonicalPath();
            if (!cwd.equals(cwdLink)) {
                return cwd;
            }
        }
        catch (IOException e) {
            LinuxOSProcess.LOG.trace("Couldn't find cwd for pid {}: {}", (Object)this.getProcessID(), e.getMessage());
        }
        return "";
    }
    
    @Override
    public String getUser() {
        return this.user;
    }
    
    @Override
    public String getUserID() {
        return this.userID;
    }
    
    @Override
    public String getGroup() {
        return this.group;
    }
    
    @Override
    public String getGroupID() {
        return this.groupID;
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
    public List<OSThread> getThreadDetails() {
        return ProcessStat.getThreadIds(this.getProcessID()).stream().map(id -> new LinuxOSThread(this.getProcessID(), id)).collect((Collector<? super Object, ?, List<OSThread>>)Collectors.toList());
    }
    
    @Override
    public long getMinorFaults() {
        return this.minorFaults;
    }
    
    @Override
    public long getMajorFaults() {
        return this.majorFaults;
    }
    
    @Override
    public long getContextSwitches() {
        return this.contextSwitches;
    }
    
    @Override
    public long getOpenFiles() {
        return ProcessStat.getFileDescriptorFiles(this.getProcessID()).length;
    }
    
    @Override
    public int getBitness() {
        return this.bitness.get();
    }
    
    private int queryBitness() {
        final byte[] buffer = new byte[5];
        if (!this.path.isEmpty()) {
            try {
                final InputStream is = new FileInputStream(this.path);
                try {
                    if (is.read(buffer) == buffer.length) {
                        final int n = (buffer[4] == 1) ? 32 : 64;
                        is.close();
                        return n;
                    }
                    is.close();
                }
                catch (Throwable t) {
                    try {
                        is.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                    throw t;
                }
            }
            catch (IOException e) {
                LinuxOSProcess.LOG.warn("Failed to read process file: {}", this.path);
            }
        }
        return 0;
    }
    
    @Override
    public long getAffinityMask() {
        final String mask = ExecutingCommand.getFirstAnswer("taskset -p " + this.getProcessID());
        final String[] split = ParseUtil.whitespaces.split(mask);
        try {
            return new BigInteger(split[split.length - 1], 16).longValue();
        }
        catch (NumberFormatException e) {
            return 0L;
        }
    }
    
    @Override
    public boolean updateAttributes() {
        final String procPidExe = String.format(ProcPath.PID_EXE, this.getProcessID());
        try {
            final Path link = Paths.get(procPidExe, new String[0]);
            this.path = Files.readSymbolicLink(link).toString();
            final int index = this.path.indexOf(" (deleted)");
            if (index != -1) {
                this.path = this.path.substring(0, index);
            }
        }
        catch (InvalidPathException | IOException | UnsupportedOperationException | SecurityException ex2) {
            final Exception ex;
            final Exception e = ex;
            LinuxOSProcess.LOG.debug("Unable to open symbolic link {}", procPidExe);
        }
        final Map<String, String> io = FileUtil.getKeyValueMapFromFile(String.format(ProcPath.PID_IO, this.getProcessID()), ":");
        final Map<String, String> status = FileUtil.getKeyValueMapFromFile(String.format(ProcPath.PID_STATUS, this.getProcessID()), ":");
        final String stat = FileUtil.getStringFromFile(String.format(ProcPath.PID_STAT, this.getProcessID()));
        if (stat.isEmpty()) {
            this.state = OSProcess.State.INVALID;
            return false;
        }
        getMissingDetails(status, stat);
        final long now = System.currentTimeMillis();
        final long[] statArray = ParseUtil.parseStringToLongArray(stat, LinuxOSProcess.PROC_PID_STAT_ORDERS, ProcessStat.PROC_PID_STAT_LENGTH, ' ');
        this.startTime = (LinuxOperatingSystem.BOOTTIME * LinuxOperatingSystem.getHz() + statArray[ProcPidStat.START_TIME.ordinal()]) * 1000L / LinuxOperatingSystem.getHz();
        if (this.startTime >= now) {
            this.startTime = now - 1L;
        }
        this.parentProcessID = (int)statArray[ProcPidStat.PPID.ordinal()];
        this.threadCount = (int)statArray[ProcPidStat.THREAD_COUNT.ordinal()];
        this.priority = (int)statArray[ProcPidStat.PRIORITY.ordinal()];
        this.virtualSize = statArray[ProcPidStat.VSZ.ordinal()];
        this.residentSetSize = statArray[ProcPidStat.RSS.ordinal()] * LinuxGlobalMemory.PAGE_SIZE;
        this.kernelTime = statArray[ProcPidStat.KERNEL_TIME.ordinal()] * 1000L / LinuxOperatingSystem.getHz();
        this.userTime = statArray[ProcPidStat.USER_TIME.ordinal()] * 1000L / LinuxOperatingSystem.getHz();
        this.minorFaults = statArray[ProcPidStat.MINOR_FAULTS.ordinal()];
        this.majorFaults = statArray[ProcPidStat.MAJOR_FAULTS.ordinal()];
        final long nonVoluntaryContextSwitches = ParseUtil.parseLongOrDefault(status.get("nonvoluntary_ctxt_switches"), 0L);
        final long voluntaryContextSwitches = ParseUtil.parseLongOrDefault(status.get("voluntary_ctxt_switches"), 0L);
        this.contextSwitches = voluntaryContextSwitches + nonVoluntaryContextSwitches;
        this.upTime = now - this.startTime;
        this.bytesRead = ParseUtil.parseLongOrDefault(io.getOrDefault("read_bytes", ""), 0L);
        this.bytesWritten = ParseUtil.parseLongOrDefault(io.getOrDefault("write_bytes", ""), 0L);
        this.userID = ParseUtil.whitespaces.split(status.getOrDefault("Uid", ""))[0];
        this.user = UserGroupInfo.getUser(this.userID);
        this.groupID = ParseUtil.whitespaces.split(status.getOrDefault("Gid", ""))[0];
        this.group = UserGroupInfo.getGroupName(this.groupID);
        this.name = status.getOrDefault("Name", "");
        this.state = ProcessStat.getState(status.getOrDefault("State", "U").charAt(0));
        return true;
    }
    
    private static void getMissingDetails(final Map<String, String> status, final String stat) {
        if (status == null || stat == null) {
            return;
        }
        final int nameStart = stat.indexOf(40);
        final int nameEnd = stat.indexOf(41);
        if (Util.isBlank(status.get("Name")) && nameStart > 0 && nameStart < nameEnd) {
            final String statName = stat.substring(nameStart + 1, nameEnd);
            status.put("Name", statName);
        }
        if (Util.isBlank(status.get("State")) && nameEnd > 0 && stat.length() > nameEnd + 2) {
            final String statState = String.valueOf(stat.charAt(nameEnd + 2));
            status.put("State", statState);
        }
    }
    
    static {
        LOG = LoggerFactory.getLogger(LinuxOSProcess.class);
        PROC_PID_STAT_ORDERS = new int[ProcPidStat.values().length];
        for (final ProcPidStat stat : ProcPidStat.values()) {
            LinuxOSProcess.PROC_PID_STAT_ORDERS[stat.ordinal()] = stat.getOrder() - 1;
        }
    }
    
    private enum ProcPidStat
    {
        PPID(4), 
        MINOR_FAULTS(10), 
        MAJOR_FAULTS(12), 
        USER_TIME(14), 
        KERNEL_TIME(15), 
        PRIORITY(18), 
        THREAD_COUNT(20), 
        START_TIME(22), 
        VSZ(23), 
        RSS(24);
        
        private final int order;
        
        public int getOrder() {
            return this.order;
        }
        
        private ProcPidStat(final int order) {
            this.order = order;
        }
        
        private static /* synthetic */ ProcPidStat[] $values() {
            return new ProcPidStat[] { ProcPidStat.PPID, ProcPidStat.MINOR_FAULTS, ProcPidStat.MAJOR_FAULTS, ProcPidStat.USER_TIME, ProcPidStat.KERNEL_TIME, ProcPidStat.PRIORITY, ProcPidStat.THREAD_COUNT, ProcPidStat.START_TIME, ProcPidStat.VSZ, ProcPidStat.RSS };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
