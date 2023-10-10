// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.aix;

import oshi.jna.platform.unix.AixLibc;
import oshi.driver.unix.aix.PsInfo;
import oshi.software.os.OSProcess;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractOSThread;

@ThreadSafe
public class AixOSThread extends AbstractOSThread
{
    private int threadId;
    private OSProcess.State state;
    private long startMemoryAddress;
    private long contextSwitches;
    private long kernelTime;
    private long userTime;
    private long startTime;
    private long upTime;
    private int priority;
    
    public AixOSThread(final int pid, final int tid) {
        super(pid);
        this.state = OSProcess.State.INVALID;
        this.threadId = tid;
        this.updateAttributes();
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
        final AixLibc.AIXLwpsInfo lwpsinfo = PsInfo.queryLwpsInfo(this.getOwningProcessId(), this.getThreadId());
        if (lwpsinfo == null) {
            this.state = OSProcess.State.INVALID;
            return false;
        }
        this.threadId = (int)lwpsinfo.pr_lwpid;
        this.startMemoryAddress = lwpsinfo.pr_addr;
        this.state = AixOSProcess.getStateFromOutput((char)lwpsinfo.pr_sname);
        this.priority = lwpsinfo.pr_pri;
        return true;
    }
}
