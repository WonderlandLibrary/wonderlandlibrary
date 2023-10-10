// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.freebsd;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Arrays;
import oshi.util.platform.unix.freebsd.BsdSysctlUtil;
import org.slf4j.LoggerFactory;
import java.util.Iterator;
import java.util.ArrayList;
import oshi.software.os.OSThread;
import oshi.util.ExecutingCommand;
import oshi.util.platform.unix.freebsd.ProcstatUtil;
import com.sun.jna.Native;
import java.util.Collections;
import oshi.util.ParseUtil;
import com.sun.jna.Pointer;
import oshi.jna.platform.unix.FreeBsdLibc;
import com.sun.jna.platform.unix.LibCAPI;
import com.sun.jna.Memory;
import oshi.util.Memoizer;
import oshi.software.os.OSProcess;
import java.util.Map;
import java.util.List;
import java.util.function.Supplier;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractOSProcess;

@ThreadSafe
public class FreeBsdOSProcess extends AbstractOSProcess
{
    private static final Logger LOG;
    private static final int ARGMAX;
    static final String PS_THREAD_COLUMNS;
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
    private String commandLineBackup;
    
    public FreeBsdOSProcess(final int pid, final Map<FreeBsdOperatingSystem.PsKeywords, String> psMap) {
        super(pid);
        this.bitness = Memoizer.memoize(this::queryBitness);
        this.commandLine = Memoizer.memoize(this::queryCommandLine);
        this.arguments = Memoizer.memoize(this::queryArguments);
        this.environmentVariables = Memoizer.memoize(this::queryEnvironmentVariables);
        this.path = "";
        this.state = OSProcess.State.INVALID;
        this.updateAttributes(psMap);
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
        final String cl = String.join(" ", this.getArguments());
        return cl.isEmpty() ? this.commandLineBackup : cl;
    }
    
    @Override
    public List<String> getArguments() {
        return this.arguments.get();
    }
    
    private List<String> queryArguments() {
        if (FreeBsdOSProcess.ARGMAX > 0) {
            final int[] mib = { 1, 14, 7, this.getProcessID() };
            final Memory m = new Memory(FreeBsdOSProcess.ARGMAX);
            final LibCAPI.size_t.ByReference size = new LibCAPI.size_t.ByReference(new LibCAPI.size_t((long)FreeBsdOSProcess.ARGMAX));
            if (FreeBsdLibc.INSTANCE.sysctl(mib, mib.length, m, size, null, LibCAPI.size_t.ZERO) == 0) {
                return Collections.unmodifiableList((List<? extends String>)ParseUtil.parseByteArrayToStrings(m.getByteArray(0L, size.getValue().intValue())));
            }
            FreeBsdOSProcess.LOG.warn("Failed sysctl call for process arguments (kern.proc.args), process {} may not exist. Error code: {}", (Object)this.getProcessID(), Native.getLastError());
        }
        return Collections.emptyList();
    }
    
    @Override
    public Map<String, String> getEnvironmentVariables() {
        return this.environmentVariables.get();
    }
    
    private Map<String, String> queryEnvironmentVariables() {
        if (FreeBsdOSProcess.ARGMAX > 0) {
            final int[] mib = { 1, 14, 35, this.getProcessID() };
            final Memory m = new Memory(FreeBsdOSProcess.ARGMAX);
            final LibCAPI.size_t.ByReference size = new LibCAPI.size_t.ByReference(new LibCAPI.size_t((long)FreeBsdOSProcess.ARGMAX));
            if (FreeBsdLibc.INSTANCE.sysctl(mib, mib.length, m, size, null, LibCAPI.size_t.ZERO) == 0) {
                return Collections.unmodifiableMap((Map<? extends String, ? extends String>)ParseUtil.parseByteArrayToStringMap(m.getByteArray(0L, size.getValue().intValue())));
            }
            FreeBsdOSProcess.LOG.warn("Failed sysctl call for process environment variables (kern.proc.env), process {} may not exist. Error code: {}", (Object)this.getProcessID(), Native.getLastError());
        }
        return Collections.emptyMap();
    }
    
    @Override
    public String getCurrentWorkingDirectory() {
        return ProcstatUtil.getCwd(this.getProcessID());
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
    public long getOpenFiles() {
        return ProcstatUtil.getOpenFiles(this.getProcessID());
    }
    
    @Override
    public int getBitness() {
        return this.bitness.get();
    }
    
    @Override
    public long getAffinityMask() {
        long bitMask = 0L;
        final String cpuset = ExecutingCommand.getFirstAnswer("cpuset -gp " + this.getProcessID());
        final String[] split = cpuset.split(":");
        if (split.length > 1) {
            final String[] split2;
            final String[] bits = split2 = split[1].split(",");
            for (final String bit : split2) {
                final int bitToSet = ParseUtil.parseIntOrDefault(bit.trim(), -1);
                if (bitToSet >= 0) {
                    bitMask |= 1L << bitToSet;
                }
            }
        }
        return bitMask;
    }
    
    private int queryBitness() {
        final int[] mib = { 1, 14, 9, this.getProcessID() };
        final Pointer abi = new Memory(32L);
        final LibCAPI.size_t.ByReference size = new LibCAPI.size_t.ByReference(new LibCAPI.size_t(32L));
        if (0 == FreeBsdLibc.INSTANCE.sysctl(mib, mib.length, abi, size, null, LibCAPI.size_t.ZERO)) {
            final String elf = abi.getString(0L);
            if (elf.contains("ELF32")) {
                return 32;
            }
            if (elf.contains("ELF64")) {
                return 64;
            }
        }
        return 0;
    }
    
    @Override
    public List<OSThread> getThreadDetails() {
        final List<OSThread> threads = new ArrayList<OSThread>();
        String psCommand = "ps -awwxo " + FreeBsdOSProcess.PS_THREAD_COLUMNS + " -H";
        if (this.getProcessID() >= 0) {
            psCommand = psCommand + " -p " + this.getProcessID();
        }
        final List<String> threadList = ExecutingCommand.runNative(psCommand);
        if (threadList.size() > 1) {
            threadList.remove(0);
            for (final String thread : threadList) {
                final Map<PsThreadColumns, String> threadMap = ParseUtil.stringToEnumMap(PsThreadColumns.class, thread.trim(), ' ');
                if (threadMap.containsKey(PsThreadColumns.PRI)) {
                    threads.add(new FreeBsdOSThread(this.getProcessID(), threadMap));
                }
            }
        }
        return threads;
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
    public boolean updateAttributes() {
        final String psCommand = "ps -awwxo " + FreeBsdOperatingSystem.PS_COMMAND_ARGS + " -p " + this.getProcessID();
        final List<String> procList = ExecutingCommand.runNative(psCommand);
        if (procList.size() > 1) {
            final Map<FreeBsdOperatingSystem.PsKeywords, String> psMap = ParseUtil.stringToEnumMap(FreeBsdOperatingSystem.PsKeywords.class, procList.get(1).trim(), ' ');
            if (psMap.containsKey(FreeBsdOperatingSystem.PsKeywords.ARGS)) {
                return this.updateAttributes(psMap);
            }
        }
        this.state = OSProcess.State.INVALID;
        return false;
    }
    
    private boolean updateAttributes(final Map<FreeBsdOperatingSystem.PsKeywords, String> psMap) {
        final long now = System.currentTimeMillis();
        switch (psMap.get(FreeBsdOperatingSystem.PsKeywords.STATE).charAt(0)) {
            case 'R': {
                this.state = OSProcess.State.RUNNING;
                break;
            }
            case 'I':
            case 'S': {
                this.state = OSProcess.State.SLEEPING;
                break;
            }
            case 'D':
            case 'L':
            case 'U': {
                this.state = OSProcess.State.WAITING;
                break;
            }
            case 'Z': {
                this.state = OSProcess.State.ZOMBIE;
                break;
            }
            case 'T': {
                this.state = OSProcess.State.STOPPED;
                break;
            }
            default: {
                this.state = OSProcess.State.OTHER;
                break;
            }
        }
        this.parentProcessID = ParseUtil.parseIntOrDefault(psMap.get(FreeBsdOperatingSystem.PsKeywords.PPID), 0);
        this.user = psMap.get(FreeBsdOperatingSystem.PsKeywords.USER);
        this.userID = psMap.get(FreeBsdOperatingSystem.PsKeywords.UID);
        this.group = psMap.get(FreeBsdOperatingSystem.PsKeywords.GROUP);
        this.groupID = psMap.get(FreeBsdOperatingSystem.PsKeywords.GID);
        this.threadCount = ParseUtil.parseIntOrDefault(psMap.get(FreeBsdOperatingSystem.PsKeywords.NLWP), 0);
        this.priority = ParseUtil.parseIntOrDefault(psMap.get(FreeBsdOperatingSystem.PsKeywords.PRI), 0);
        this.virtualSize = ParseUtil.parseLongOrDefault(psMap.get(FreeBsdOperatingSystem.PsKeywords.VSZ), 0L) * 1024L;
        this.residentSetSize = ParseUtil.parseLongOrDefault(psMap.get(FreeBsdOperatingSystem.PsKeywords.RSS), 0L) * 1024L;
        final long elapsedTime = ParseUtil.parseDHMSOrDefault(psMap.get(FreeBsdOperatingSystem.PsKeywords.ETIMES), 0L);
        this.upTime = ((elapsedTime < 1L) ? 1L : elapsedTime);
        this.startTime = now - this.upTime;
        this.kernelTime = ParseUtil.parseDHMSOrDefault(psMap.get(FreeBsdOperatingSystem.PsKeywords.SYSTIME), 0L);
        this.userTime = ParseUtil.parseDHMSOrDefault(psMap.get(FreeBsdOperatingSystem.PsKeywords.TIME), 0L) - this.kernelTime;
        this.path = psMap.get(FreeBsdOperatingSystem.PsKeywords.COMM);
        this.name = this.path.substring(this.path.lastIndexOf(47) + 1);
        this.minorFaults = ParseUtil.parseLongOrDefault(psMap.get(FreeBsdOperatingSystem.PsKeywords.MAJFLT), 0L);
        this.majorFaults = ParseUtil.parseLongOrDefault(psMap.get(FreeBsdOperatingSystem.PsKeywords.MINFLT), 0L);
        final long nonVoluntaryContextSwitches = ParseUtil.parseLongOrDefault(psMap.get(FreeBsdOperatingSystem.PsKeywords.NVCSW), 0L);
        final long voluntaryContextSwitches = ParseUtil.parseLongOrDefault(psMap.get(FreeBsdOperatingSystem.PsKeywords.NIVCSW), 0L);
        this.contextSwitches = voluntaryContextSwitches + nonVoluntaryContextSwitches;
        this.commandLineBackup = psMap.get(FreeBsdOperatingSystem.PsKeywords.ARGS);
        return true;
    }
    
    static {
        LOG = LoggerFactory.getLogger(FreeBsdOSProcess.class);
        ARGMAX = BsdSysctlUtil.sysctl("kern.argmax", 0);
        PS_THREAD_COLUMNS = Arrays.stream(PsThreadColumns.values()).map((Function<? super PsThreadColumns, ?>)Enum::name).map((Function<? super Object, ?>)String::toLowerCase).collect((Collector<? super Object, ?, String>)Collectors.joining(","));
    }
    
    enum PsThreadColumns
    {
        TDNAME, 
        LWP, 
        STATE, 
        ETIMES, 
        SYSTIME, 
        TIME, 
        TDADDR, 
        NIVCSW, 
        NVCSW, 
        MAJFLT, 
        MINFLT, 
        PRI;
        
        private static /* synthetic */ PsThreadColumns[] $values() {
            return new PsThreadColumns[] { PsThreadColumns.TDNAME, PsThreadColumns.LWP, PsThreadColumns.STATE, PsThreadColumns.ETIMES, PsThreadColumns.SYSTIME, PsThreadColumns.TIME, PsThreadColumns.TDADDR, PsThreadColumns.NIVCSW, PsThreadColumns.NVCSW, PsThreadColumns.MAJFLT, PsThreadColumns.MINFLT, PsThreadColumns.PRI };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
