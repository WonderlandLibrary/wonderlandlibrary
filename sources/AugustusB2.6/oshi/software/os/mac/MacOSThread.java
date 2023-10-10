// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.mac;

import oshi.software.os.OSProcess;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractOSThread;

@ThreadSafe
public class MacOSThread extends AbstractOSThread
{
    private final int threadId;
    private final OSProcess.State state;
    private final long kernelTime;
    private final long userTime;
    private final long startTime;
    private final long upTime;
    private final int priority;
    
    public MacOSThread(final int pid, final int threadId, final OSProcess.State state, final long kernelTime, final long userTime, final long startTime, final long upTime, final int priority) {
        super(pid);
        this.threadId = threadId;
        this.state = state;
        this.kernelTime = kernelTime;
        this.userTime = userTime;
        this.startTime = startTime;
        this.upTime = upTime;
        this.priority = priority;
    }
    
    @Override
    public int getThreadId() {
        return this.threadId;
    }
    
    @Override
    public OSProcess.State getState() {
        return this.state;
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
}
