// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.solaris;

import oshi.util.Util;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import oshi.driver.unix.solaris.PsInfo;
import oshi.util.Memoizer;
import oshi.software.os.OSProcess;
import oshi.jna.platform.unix.SolarisLibc;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractOSThread;

@ThreadSafe
public class SolarisOSThread extends AbstractOSThread
{
    private Supplier<SolarisLibc.SolarisLwpsInfo> lwpsinfo;
    private Supplier<SolarisLibc.SolarisPrUsage> prusage;
    private String name;
    private int threadId;
    private OSProcess.State state;
    private long startMemoryAddress;
    private long contextSwitches;
    private long kernelTime;
    private long userTime;
    private long startTime;
    private long upTime;
    private int priority;
    
    public SolarisOSThread(final int pid, final int lwpid) {
        super(pid);
        this.lwpsinfo = Memoizer.memoize(this::queryLwpsInfo, Memoizer.defaultExpiration());
        this.prusage = Memoizer.memoize(this::queryPrUsage, Memoizer.defaultExpiration());
        this.state = OSProcess.State.INVALID;
        this.threadId = lwpid;
        this.updateAttributes();
    }
    
    private SolarisLibc.SolarisLwpsInfo queryLwpsInfo() {
        return PsInfo.queryLwpsInfo(this.getOwningProcessId(), this.getThreadId());
    }
    
    private SolarisLibc.SolarisPrUsage queryPrUsage() {
        return PsInfo.queryPrUsage(this.getOwningProcessId(), this.getThreadId());
    }
    
    @Override
    public String getName() {
        return (this.name != null) ? this.name : "";
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
        final SolarisLibc.SolarisLwpsInfo info = this.lwpsinfo.get();
        if (info == null) {
            this.state = OSProcess.State.INVALID;
            return false;
        }
        final SolarisLibc.SolarisPrUsage usage = this.prusage.get();
        final long now = System.currentTimeMillis();
        this.state = SolarisOSProcess.getStateFromOutput((char)info.pr_sname);
        this.startTime = info.pr_start.tv_sec.longValue() * 1000L + info.pr_start.tv_nsec.longValue() / 1000000L;
        final long elapsedTime = now - this.startTime;
        this.upTime = ((elapsedTime < 1L) ? 1L : elapsedTime);
        this.kernelTime = 0L;
        this.userTime = info.pr_time.tv_sec.longValue() * 1000L + info.pr_time.tv_nsec.longValue() / 1000000L;
        this.startMemoryAddress = Pointer.nativeValue(info.pr_addr);
        this.priority = info.pr_pri;
        if (usage != null) {
            this.userTime = usage.pr_utime.tv_sec.longValue() * 1000L + usage.pr_utime.tv_nsec.longValue() / 1000000L;
            this.kernelTime = usage.pr_stime.tv_sec.longValue() * 1000L + usage.pr_stime.tv_nsec.longValue() / 1000000L;
            this.contextSwitches = usage.pr_ictx.longValue() + usage.pr_vctx.longValue();
        }
        this.name = Native.toString(info.pr_name);
        if (Util.isBlank(this.name)) {
            this.name = Native.toString(info.pr_oldname);
        }
        return true;
    }
}
