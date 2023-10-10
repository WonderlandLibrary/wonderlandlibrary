// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.common;

import oshi.util.Memoizer;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.os.OSThread;

@ThreadSafe
public abstract class AbstractOSThread implements OSThread
{
    private final Supplier<Double> cumulativeCpuLoad;
    private final int owningProcessId;
    
    protected AbstractOSThread(final int processId) {
        this.cumulativeCpuLoad = Memoizer.memoize(this::queryCumulativeCpuLoad, Memoizer.defaultExpiration());
        this.owningProcessId = processId;
    }
    
    @Override
    public int getOwningProcessId() {
        return this.owningProcessId;
    }
    
    @Override
    public double getThreadCpuLoadCumulative() {
        return this.cumulativeCpuLoad.get();
    }
    
    private double queryCumulativeCpuLoad() {
        return (this.getUpTime() > 0.0) ? ((this.getKernelTime() + this.getUserTime()) / (double)this.getUpTime()) : 0.0;
    }
    
    @Override
    public double getThreadCpuLoadBetweenTicks(final OSThread priorSnapshot) {
        if (priorSnapshot != null && this.owningProcessId == priorSnapshot.getOwningProcessId() && this.getThreadId() == priorSnapshot.getThreadId() && this.getUpTime() > priorSnapshot.getUpTime()) {
            return (this.getUserTime() - priorSnapshot.getUserTime() + this.getKernelTime() - priorSnapshot.getKernelTime()) / (double)(this.getUpTime() - priorSnapshot.getUpTime());
        }
        return this.getThreadCpuLoadCumulative();
    }
    
    @Override
    public String toString() {
        return "OSThread [threadId=" + this.getThreadId() + ", owningProcessId=" + this.getOwningProcessId() + ", name=" + this.getName() + ", state=" + this.getState() + ", kernelTime=" + this.getKernelTime() + ", userTime=" + this.getUserTime() + ", upTime=" + this.getUpTime() + ", startTime=" + this.getStartTime() + ", startMemoryAddress=0x" + String.format("%x", this.getStartMemoryAddress()) + ", contextSwitches=" + this.getContextSwitches() + ", minorFaults=" + this.getMinorFaults() + ", majorFaults=" + this.getMajorFaults() + "]";
    }
}
