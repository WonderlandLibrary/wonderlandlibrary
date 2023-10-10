// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.mac;

import org.slf4j.LoggerFactory;
import com.sun.jna.Native;
import oshi.util.ParseUtil;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.Structure;
import oshi.util.platform.mac.SysctlUtil;
import com.sun.jna.platform.mac.SystemB;
import oshi.util.Memoizer;
import oshi.util.tuples.Pair;
import java.util.function.Supplier;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractVirtualMemory;

@ThreadSafe
final class MacVirtualMemory extends AbstractVirtualMemory
{
    private static final Logger LOG;
    private final MacGlobalMemory global;
    private final Supplier<Pair<Long, Long>> usedTotal;
    private final Supplier<Pair<Long, Long>> inOut;
    
    MacVirtualMemory(final MacGlobalMemory macGlobalMemory) {
        this.usedTotal = Memoizer.memoize(MacVirtualMemory::querySwapUsage, Memoizer.defaultExpiration());
        this.inOut = Memoizer.memoize(MacVirtualMemory::queryVmStat, Memoizer.defaultExpiration());
        this.global = macGlobalMemory;
    }
    
    @Override
    public long getSwapUsed() {
        return this.usedTotal.get().getA();
    }
    
    @Override
    public long getSwapTotal() {
        return this.usedTotal.get().getB();
    }
    
    @Override
    public long getVirtualMax() {
        return this.global.getTotal() + this.getSwapTotal();
    }
    
    @Override
    public long getVirtualInUse() {
        return this.global.getTotal() - this.global.getAvailable() + this.getSwapUsed();
    }
    
    @Override
    public long getSwapPagesIn() {
        return this.inOut.get().getA();
    }
    
    @Override
    public long getSwapPagesOut() {
        return this.inOut.get().getB();
    }
    
    private static Pair<Long, Long> querySwapUsage() {
        long swapUsed = 0L;
        long swapTotal = 0L;
        final SystemB.XswUsage xswUsage = new SystemB.XswUsage();
        if (SysctlUtil.sysctl("vm.swapusage", xswUsage)) {
            swapUsed = xswUsage.xsu_used;
            swapTotal = xswUsage.xsu_total;
        }
        return new Pair<Long, Long>(swapUsed, swapTotal);
    }
    
    private static Pair<Long, Long> queryVmStat() {
        long swapPagesIn = 0L;
        long swapPagesOut = 0L;
        final SystemB.VMStatistics vmStats = new SystemB.VMStatistics();
        if (0 == SystemB.INSTANCE.host_statistics(SystemB.INSTANCE.mach_host_self(), 2, vmStats, new IntByReference(vmStats.size() / SystemB.INT_SIZE))) {
            swapPagesIn = ParseUtil.unsignedIntToLong(vmStats.pageins);
            swapPagesOut = ParseUtil.unsignedIntToLong(vmStats.pageouts);
        }
        else {
            MacVirtualMemory.LOG.error("Failed to get host VM info. Error code: {}", (Object)Native.getLastError());
        }
        return new Pair<Long, Long>(swapPagesIn, swapPagesOut);
    }
    
    static {
        LOG = LoggerFactory.getLogger(MacVirtualMemory.class);
    }
}
