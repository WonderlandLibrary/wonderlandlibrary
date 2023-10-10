// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.linux;

import java.util.Map;
import oshi.util.ParseUtil;
import oshi.driver.linux.proc.ProcessStat;
import oshi.util.FileUtil;
import oshi.util.platform.linux.ProcPath;
import oshi.software.os.OSProcess;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractOSThread;

@ThreadSafe
public class LinuxOSThread extends AbstractOSThread
{
    private static final int[] PROC_TASK_STAT_ORDERS;
    private final int threadId;
    private String name;
    private OSProcess.State state;
    private long minorFaults;
    private long majorFaults;
    private long startMemoryAddress;
    private long contextSwitches;
    private long kernelTime;
    private long userTime;
    private long startTime;
    private long upTime;
    private int priority;
    
    public LinuxOSThread(final int processId, final int tid) {
        super(processId);
        this.state = OSProcess.State.INVALID;
        this.threadId = tid;
        this.updateAttributes();
    }
    
    @Override
    public int getThreadId() {
        return this.threadId;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public OSProcess.State getState() {
        return this.state;
    }
    
    @Override
    public long getStartTime() {
        return this.startTime;
    }
    
    @Override
    public long getStartMemoryAddress() {
        return this.startMemoryAddress;
    }
    
    @Override
    public long getContextSwitches() {
        return this.contextSwitches;
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
    public int getPriority() {
        return this.priority;
    }
    
    @Override
    public boolean updateAttributes() {
        this.name = FileUtil.getStringFromFile(String.format(ProcPath.TASK_COMM, this.getOwningProcessId(), this.threadId));
        final Map<String, String> status = FileUtil.getKeyValueMapFromFile(String.format(ProcPath.TASK_STATUS, this.getOwningProcessId(), this.threadId), ":");
        final String stat = FileUtil.getStringFromFile(String.format(ProcPath.TASK_STAT, this.getOwningProcessId(), this.threadId));
        if (stat.isEmpty()) {
            this.state = OSProcess.State.INVALID;
            return false;
        }
        final long now = System.currentTimeMillis();
        final long[] statArray = ParseUtil.parseStringToLongArray(stat, LinuxOSThread.PROC_TASK_STAT_ORDERS, ProcessStat.PROC_PID_STAT_LENGTH, ' ');
        this.startTime = (LinuxOperatingSystem.BOOTTIME * LinuxOperatingSystem.getHz() + statArray[ThreadPidStat.START_TIME.ordinal()]) * 1000L / LinuxOperatingSystem.getHz();
        if (this.startTime >= now) {
            this.startTime = now - 1L;
        }
        this.minorFaults = statArray[ThreadPidStat.MINOR_FAULTS.ordinal()];
        this.majorFaults = statArray[ThreadPidStat.MAJOR_FAULT.ordinal()];
        this.startMemoryAddress = statArray[ThreadPidStat.START_CODE.ordinal()];
        final long voluntaryContextSwitches = ParseUtil.parseLongOrDefault(status.get("voluntary_ctxt_switches"), 0L);
        final long nonVoluntaryContextSwitches = ParseUtil.parseLongOrDefault(status.get("nonvoluntary_ctxt_switches"), 0L);
        this.contextSwitches = voluntaryContextSwitches + nonVoluntaryContextSwitches;
        this.state = ProcessStat.getState(status.getOrDefault("State", "U").charAt(0));
        this.kernelTime = statArray[ThreadPidStat.KERNEL_TIME.ordinal()] * 1000L / LinuxOperatingSystem.getHz();
        this.userTime = statArray[ThreadPidStat.USER_TIME.ordinal()] * 1000L / LinuxOperatingSystem.getHz();
        this.upTime = now - this.startTime;
        this.priority = (int)statArray[ThreadPidStat.PRIORITY.ordinal()];
        return true;
    }
    
    static {
        PROC_TASK_STAT_ORDERS = new int[ThreadPidStat.values().length];
        for (final ThreadPidStat stat : ThreadPidStat.values()) {
            LinuxOSThread.PROC_TASK_STAT_ORDERS[stat.ordinal()] = stat.getOrder() - 1;
        }
    }
    
    private enum ThreadPidStat
    {
        PPID(4), 
        MINOR_FAULTS(10), 
        MAJOR_FAULT(12), 
        USER_TIME(14), 
        KERNEL_TIME(15), 
        PRIORITY(18), 
        THREAD_COUNT(20), 
        START_TIME(22), 
        VSZ(23), 
        RSS(24), 
        START_CODE(26);
        
        private final int order;
        
        private ThreadPidStat(final int order) {
            this.order = order;
        }
        
        public int getOrder() {
            return this.order;
        }
        
        private static /* synthetic */ ThreadPidStat[] $values() {
            return new ThreadPidStat[] { ThreadPidStat.PPID, ThreadPidStat.MINOR_FAULTS, ThreadPidStat.MAJOR_FAULT, ThreadPidStat.USER_TIME, ThreadPidStat.KERNEL_TIME, ThreadPidStat.PRIORITY, ThreadPidStat.THREAD_COUNT, ThreadPidStat.START_TIME, ThreadPidStat.VSZ, ThreadPidStat.RSS, ThreadPidStat.START_CODE };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
