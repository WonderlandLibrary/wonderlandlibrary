// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.openbsd;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Arrays;
import org.slf4j.LoggerFactory;
import java.util.Iterator;
import oshi.software.os.OSThread;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import oshi.util.platform.unix.openbsd.FstatUtil;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.ArrayList;
import com.sun.jna.Pointer;
import oshi.jna.platform.unix.OpenBsdLibc;
import com.sun.jna.platform.unix.LibCAPI;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import oshi.util.Memoizer;
import oshi.software.os.OSProcess;
import java.util.Map;
import java.util.List;
import java.util.function.Supplier;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractOSProcess;

@ThreadSafe
public class OpenBsdOSProcess extends AbstractOSProcess
{
    private static final Logger LOG;
    static final String PS_THREAD_COLUMNS;
    private static final int ARGMAX;
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
    private int bitness;
    private String commandLineBackup;
    
    public OpenBsdOSProcess(final int pid, final Map<OpenBsdOperatingSystem.PsKeywords, String> psMap) {
        super(pid);
        this.commandLine = Memoizer.memoize(this::queryCommandLine);
        this.arguments = Memoizer.memoize(this::queryArguments);
        this.environmentVariables = Memoizer.memoize(this::queryEnvironmentVariables);
        this.path = "";
        this.state = OSProcess.State.INVALID;
        this.bitness = Native.LONG_SIZE * 8;
        this.updateThreadCount();
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
        if (OpenBsdOSProcess.ARGMAX > 0) {
            final int[] mib = { 1, 55, this.getProcessID(), 1 };
            final Memory m = new Memory(OpenBsdOSProcess.ARGMAX);
            final LibCAPI.size_t.ByReference size = new LibCAPI.size_t.ByReference(new LibCAPI.size_t((long)OpenBsdOSProcess.ARGMAX));
            if (OpenBsdLibc.INSTANCE.sysctl(mib, mib.length, m, size, null, LibCAPI.size_t.ZERO) == 0) {
                final List<String> args = new ArrayList<String>();
                for (long offset = 0L, baseAddr = Pointer.nativeValue(m), maxAddr = baseAddr + size.getValue().longValue(), argAddr = Pointer.nativeValue(m.getPointer(offset)); argAddr > baseAddr && argAddr < maxAddr; argAddr = Pointer.nativeValue(m.getPointer(offset))) {
                    args.add(m.getString(argAddr - baseAddr));
                    offset += Native.POINTER_SIZE;
                }
                return Collections.unmodifiableList((List<? extends String>)args);
            }
        }
        return Collections.emptyList();
    }
    
    @Override
    public Map<String, String> getEnvironmentVariables() {
        return this.environmentVariables.get();
    }
    
    private Map<String, String> queryEnvironmentVariables() {
        final int[] mib = { 1, 55, this.getProcessID(), 3 };
        final Memory m = new Memory(OpenBsdOSProcess.ARGMAX);
        final LibCAPI.size_t.ByReference size = new LibCAPI.size_t.ByReference(new LibCAPI.size_t((long)OpenBsdOSProcess.ARGMAX));
        if (OpenBsdLibc.INSTANCE.sysctl(mib, mib.length, m, size, null, LibCAPI.size_t.ZERO) == 0) {
            final Map<String, String> env = new LinkedHashMap<String, String>();
            for (long offset = 0L, baseAddr = Pointer.nativeValue(m), maxAddr = baseAddr + size.longValue(), argAddr = Pointer.nativeValue(m.getPointer(offset)); argAddr > baseAddr && argAddr < maxAddr; argAddr = Pointer.nativeValue(m.getPointer(offset))) {
                final String envStr = m.getString(argAddr - baseAddr);
                final int idx = envStr.indexOf(61);
                if (idx > 0) {
                    env.put(envStr.substring(0, idx), envStr.substring(idx + 1));
                }
                offset += Native.POINTER_SIZE;
            }
            return Collections.unmodifiableMap((Map<? extends String, ? extends String>)env);
        }
        return Collections.emptyMap();
    }
    
    @Override
    public String getCurrentWorkingDirectory() {
        return FstatUtil.getCwd(this.getProcessID());
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
        return FstatUtil.getOpenFiles(this.getProcessID());
    }
    
    @Override
    public int getBitness() {
        return this.bitness;
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
    
    @Override
    public List<OSThread> getThreadDetails() {
        final List<OSThread> threads = new ArrayList<OSThread>();
        String psCommand = "ps -aHwwxo " + OpenBsdOSProcess.PS_THREAD_COLUMNS;
        if (this.getProcessID() >= 0) {
            psCommand = psCommand + " -p " + this.getProcessID();
        }
        final List<String> threadList = ExecutingCommand.runNative(psCommand);
        if (threadList.isEmpty() || threadList.size() < 2) {
            return threads;
        }
        threadList.remove(0);
        for (final String thread : threadList) {
            final Map<PsThreadColumns, String> threadMap = ParseUtil.stringToEnumMap(PsThreadColumns.class, thread.trim(), ' ');
            if (threadMap.containsKey(PsThreadColumns.ARGS)) {
                threads.add(new OpenBsdOSThread(this.getProcessID(), threadMap));
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
        final String psCommand = "ps -awwxo " + OpenBsdOperatingSystem.PS_COMMAND_ARGS + " -p " + this.getProcessID();
        final List<String> procList = ExecutingCommand.runNative(psCommand);
        if (procList.size() > 1) {
            final Map<OpenBsdOperatingSystem.PsKeywords, String> psMap = ParseUtil.stringToEnumMap(OpenBsdOperatingSystem.PsKeywords.class, procList.get(1).trim(), ' ');
            if (psMap.containsKey(OpenBsdOperatingSystem.PsKeywords.ARGS)) {
                this.updateThreadCount();
                return this.updateAttributes(psMap);
            }
        }
        this.state = OSProcess.State.INVALID;
        return false;
    }
    
    private boolean updateAttributes(final Map<OpenBsdOperatingSystem.PsKeywords, String> psMap) {
        final long now = System.currentTimeMillis();
        switch (psMap.get(OpenBsdOperatingSystem.PsKeywords.STATE).charAt(0)) {
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
        this.parentProcessID = ParseUtil.parseIntOrDefault(psMap.get(OpenBsdOperatingSystem.PsKeywords.PPID), 0);
        this.user = psMap.get(OpenBsdOperatingSystem.PsKeywords.USER);
        this.userID = psMap.get(OpenBsdOperatingSystem.PsKeywords.UID);
        this.group = psMap.get(OpenBsdOperatingSystem.PsKeywords.GROUP);
        this.groupID = psMap.get(OpenBsdOperatingSystem.PsKeywords.GID);
        this.priority = ParseUtil.parseIntOrDefault(psMap.get(OpenBsdOperatingSystem.PsKeywords.PRI), 0);
        this.virtualSize = ParseUtil.parseLongOrDefault(psMap.get(OpenBsdOperatingSystem.PsKeywords.VSZ), 0L) * 1024L;
        this.residentSetSize = ParseUtil.parseLongOrDefault(psMap.get(OpenBsdOperatingSystem.PsKeywords.RSS), 0L) * 1024L;
        final long elapsedTime = ParseUtil.parseDHMSOrDefault(psMap.get(OpenBsdOperatingSystem.PsKeywords.ETIME), 0L);
        this.upTime = ((elapsedTime < 1L) ? 1L : elapsedTime);
        this.startTime = now - this.upTime;
        this.userTime = ParseUtil.parseDHMSOrDefault(psMap.get(OpenBsdOperatingSystem.PsKeywords.CPUTIME), 0L);
        this.kernelTime = 0L;
        this.path = psMap.get(OpenBsdOperatingSystem.PsKeywords.COMM);
        this.name = this.path.substring(this.path.lastIndexOf(47) + 1);
        this.minorFaults = ParseUtil.parseLongOrDefault(psMap.get(OpenBsdOperatingSystem.PsKeywords.MINFLT), 0L);
        this.majorFaults = ParseUtil.parseLongOrDefault(psMap.get(OpenBsdOperatingSystem.PsKeywords.MAJFLT), 0L);
        final long nonVoluntaryContextSwitches = ParseUtil.parseLongOrDefault(psMap.get(OpenBsdOperatingSystem.PsKeywords.NIVCSW), 0L);
        final long voluntaryContextSwitches = ParseUtil.parseLongOrDefault(psMap.get(OpenBsdOperatingSystem.PsKeywords.NVCSW), 0L);
        this.contextSwitches = voluntaryContextSwitches + nonVoluntaryContextSwitches;
        this.commandLineBackup = psMap.get(OpenBsdOperatingSystem.PsKeywords.ARGS);
        return true;
    }
    
    private void updateThreadCount() {
        final List<String> threadList = ExecutingCommand.runNative("ps -axHo tid -p " + this.getProcessID());
        if (!threadList.isEmpty()) {
            this.threadCount = threadList.size() - 1;
        }
        this.threadCount = 1;
    }
    
    static {
        LOG = LoggerFactory.getLogger(OpenBsdOSProcess.class);
        PS_THREAD_COLUMNS = Arrays.stream(PsThreadColumns.values()).map((Function<? super PsThreadColumns, ?>)Enum::name).map((Function<? super Object, ?>)String::toLowerCase).collect((Collector<? super Object, ?, String>)Collectors.joining(","));
        final int[] mib = { 1, 8 };
        final Memory m = new Memory(4L);
        final LibCAPI.size_t.ByReference size = new LibCAPI.size_t.ByReference(new LibCAPI.size_t(4L));
        if (OpenBsdLibc.INSTANCE.sysctl(mib, mib.length, m, size, null, LibCAPI.size_t.ZERO) == 0) {
            ARGMAX = m.getInt(0L);
        }
        else {
            OpenBsdOSProcess.LOG.warn("Failed sysctl call for process arguments max size (kern.argmax). Error code: {}", (Object)Native.getLastError());
            ARGMAX = 0;
        }
    }
    
    enum PsThreadColumns
    {
        TID, 
        STATE, 
        ETIME, 
        CPUTIME, 
        NIVCSW, 
        NVCSW, 
        MAJFLT, 
        MINFLT, 
        PRI, 
        ARGS;
        
        private static /* synthetic */ PsThreadColumns[] $values() {
            return new PsThreadColumns[] { PsThreadColumns.TID, PsThreadColumns.STATE, PsThreadColumns.ETIME, PsThreadColumns.CPUTIME, PsThreadColumns.NIVCSW, PsThreadColumns.NVCSW, PsThreadColumns.MAJFLT, PsThreadColumns.MINFLT, PsThreadColumns.PRI, PsThreadColumns.ARGS };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
