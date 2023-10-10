// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os;

public interface OSThread
{
    int getThreadId();
    
    default String getName() {
        return "";
    }
    
    OSProcess.State getState();
    
    double getThreadCpuLoadCumulative();
    
    double getThreadCpuLoadBetweenTicks(final OSThread p0);
    
    int getOwningProcessId();
    
    default long getStartMemoryAddress() {
        return 0L;
    }
    
    default long getContextSwitches() {
        return 0L;
    }
    
    default long getMinorFaults() {
        return 0L;
    }
    
    default long getMajorFaults() {
        return 0L;
    }
    
    long getKernelTime();
    
    long getUserTime();
    
    long getUpTime();
    
    long getStartTime();
    
    int getPriority();
    
    default boolean updateAttributes() {
        return false;
    }
}
