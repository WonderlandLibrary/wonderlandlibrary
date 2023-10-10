// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.windows;

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Collections;
import oshi.driver.windows.registry.ThreadPerformanceData;
import oshi.software.os.OSProcess;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractOSThread;

@ThreadSafe
public class WindowsOSThread extends AbstractOSThread
{
    private final int threadId;
    private String name;
    private OSProcess.State state;
    private long startMemoryAddress;
    private long contextSwitches;
    private long kernelTime;
    private long userTime;
    private long startTime;
    private long upTime;
    private int priority;
    
    public WindowsOSThread(final int pid, final int tid, final String procName, final ThreadPerformanceData.PerfCounterBlock pcb) {
        super(pid);
        this.threadId = tid;
        this.updateAttributes(procName, pcb);
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
    public long getKernelTime() {
        return this.kernelTime;
    }
    
    @Override
    public long getUserTime() {
        return this.userTime;
    }
    
    @Override
    public long getStartTime() {
        return this.startTime;
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
        final Set<Integer> pids = Collections.singleton(this.getOwningProcessId());
        Map<Integer, ThreadPerformanceData.PerfCounterBlock> threads = ThreadPerformanceData.buildThreadMapFromRegistry(pids);
        if (threads == null) {
            threads = ThreadPerformanceData.buildThreadMapFromPerfCounters(pids);
        }
        return this.updateAttributes(this.name.split("/")[0], threads.get(this.getThreadId()));
    }
    
    private boolean updateAttributes(final String procName, final ThreadPerformanceData.PerfCounterBlock pcb) {
        if (pcb == null) {
            this.state = OSProcess.State.INVALID;
            return false;
        }
        if (pcb.getName().contains("/") || procName.isEmpty()) {
            this.name = pcb.getName();
        }
        else {
            this.name = procName + "/" + pcb.getName();
        }
        if (pcb.getThreadWaitReason() == 5) {
            this.state = OSProcess.State.SUSPENDED;
        }
        else {
            switch (pcb.getThreadState()) {
                case 0: {
                    this.state = OSProcess.State.NEW;
                    break;
                }
                case 2:
                case 3: {
                    this.state = OSProcess.State.RUNNING;
                    break;
                }
                case 4: {
                    this.state = OSProcess.State.STOPPED;
                    break;
                }
                case 5: {
                    this.state = OSProcess.State.SLEEPING;
                    break;
                }
                case 1:
                case 6: {
                    this.state = OSProcess.State.WAITING;
                    break;
                }
                default: {
                    this.state = OSProcess.State.OTHER;
                    break;
                }
            }
        }
        this.startMemoryAddress = pcb.getStartAddress();
        this.contextSwitches = pcb.getContextSwitches();
        this.kernelTime = pcb.getKernelTime();
        this.userTime = pcb.getUserTime();
        this.startTime = pcb.getStartTime();
        this.upTime = System.currentTimeMillis() - pcb.getStartTime();
        this.priority = pcb.getPriority();
        return true;
    }
}
