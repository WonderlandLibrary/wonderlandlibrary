// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.common;

import oshi.util.Memoizer;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.os.OSProcess;

@ThreadSafe
public abstract class AbstractOSProcess implements OSProcess
{
    private final Supplier<Double> cumulativeCpuLoad;
    private int processID;
    
    protected AbstractOSProcess(final int pid) {
        this.cumulativeCpuLoad = Memoizer.memoize(this::queryCumulativeCpuLoad, Memoizer.defaultExpiration());
        this.processID = pid;
    }
    
    @Override
    public int getProcessID() {
        return this.processID;
    }
    
    @Override
    public double getProcessCpuLoadCumulative() {
        return this.cumulativeCpuLoad.get();
    }
    
    private double queryCumulativeCpuLoad() {
        return (this.getUpTime() > 0.0) ? ((this.getKernelTime() + this.getUserTime()) / (double)this.getUpTime()) : 0.0;
    }
    
    @Override
    public double getProcessCpuLoadBetweenTicks(final OSProcess priorSnapshot) {
        if (priorSnapshot != null && this.processID == priorSnapshot.getProcessID() && this.getUpTime() > priorSnapshot.getUpTime()) {
            return (this.getUserTime() - priorSnapshot.getUserTime() + this.getKernelTime() - priorSnapshot.getKernelTime()) / (double)(this.getUpTime() - priorSnapshot.getUpTime());
        }
        return this.getProcessCpuLoadCumulative();
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("OSProcess@");
        builder.append(Integer.toHexString(this.hashCode()));
        builder.append("[processID=").append(this.processID);
        builder.append(", name=").append(this.getName()).append(']');
        return builder.toString();
    }
}
