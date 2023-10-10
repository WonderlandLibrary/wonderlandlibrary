// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.freebsd;

import java.util.Iterator;
import java.util.List;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import java.util.Map;
import oshi.software.os.OSProcess;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractOSThread;

@ThreadSafe
public class FreeBsdOSThread extends AbstractOSThread
{
    private int threadId;
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
    
    public FreeBsdOSThread(final int processId, final Map<FreeBsdOSProcess.PsThreadColumns, String> threadMap) {
        super(processId);
        this.name = "";
        this.state = OSProcess.State.INVALID;
        this.updateAttributes(threadMap);
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
    public long getStartTime() {
        return this.startTime;
    }
    
    @Override
    public int getPriority() {
        return this.priority;
    }
    
    @Override
    public boolean updateAttributes() {
        final List<String> threadList = ExecutingCommand.runNative("ps -awwxo " + FreeBsdOSProcess.PS_THREAD_COLUMNS + " -H -p " + this.getOwningProcessId());
        final String lwpStr = Integer.toString(this.threadId);
        for (final String psOutput : threadList) {
            final Map<FreeBsdOSProcess.PsThreadColumns, String> threadMap = ParseUtil.stringToEnumMap(FreeBsdOSProcess.PsThreadColumns.class, psOutput.trim(), ' ');
            if (threadMap.containsKey(FreeBsdOSProcess.PsThreadColumns.PRI) && lwpStr.equals(threadMap.get(FreeBsdOSProcess.PsThreadColumns.LWP))) {
                return this.updateAttributes(threadMap);
            }
        }
        this.state = OSProcess.State.INVALID;
        return false;
    }
    
    private boolean updateAttributes(final Map<FreeBsdOSProcess.PsThreadColumns, String> threadMap) {
        this.name = threadMap.get(FreeBsdOSProcess.PsThreadColumns.TDNAME);
        this.threadId = ParseUtil.parseIntOrDefault(threadMap.get(FreeBsdOSProcess.PsThreadColumns.LWP), 0);
        switch (threadMap.get(FreeBsdOSProcess.PsThreadColumns.STATE).charAt(0)) {
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
        final long elapsedTime = ParseUtil.parseDHMSOrDefault(threadMap.get(FreeBsdOSProcess.PsThreadColumns.ETIMES), 0L);
        this.upTime = ((elapsedTime < 1L) ? 1L : elapsedTime);
        final long now = System.currentTimeMillis();
        this.startTime = now - this.upTime;
        this.kernelTime = ParseUtil.parseDHMSOrDefault(threadMap.get(FreeBsdOSProcess.PsThreadColumns.SYSTIME), 0L);
        this.userTime = ParseUtil.parseDHMSOrDefault(threadMap.get(FreeBsdOSProcess.PsThreadColumns.TIME), 0L) - this.kernelTime;
        this.startMemoryAddress = ParseUtil.hexStringToLong(threadMap.get(FreeBsdOSProcess.PsThreadColumns.TDADDR), 0L);
        final long nonVoluntaryContextSwitches = ParseUtil.parseLongOrDefault(threadMap.get(FreeBsdOSProcess.PsThreadColumns.NIVCSW), 0L);
        final long voluntaryContextSwitches = ParseUtil.parseLongOrDefault(threadMap.get(FreeBsdOSProcess.PsThreadColumns.NVCSW), 0L);
        this.contextSwitches = voluntaryContextSwitches + nonVoluntaryContextSwitches;
        this.majorFaults = ParseUtil.parseLongOrDefault(threadMap.get(FreeBsdOSProcess.PsThreadColumns.MAJFLT), 0L);
        this.minorFaults = ParseUtil.parseLongOrDefault(threadMap.get(FreeBsdOSProcess.PsThreadColumns.MINFLT), 0L);
        this.priority = ParseUtil.parseIntOrDefault(threadMap.get(FreeBsdOSProcess.PsThreadColumns.PRI), 0);
        return true;
    }
}
