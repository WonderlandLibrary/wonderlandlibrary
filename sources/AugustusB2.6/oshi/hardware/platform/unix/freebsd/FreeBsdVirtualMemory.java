// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.freebsd;

import oshi.util.platform.unix.freebsd.BsdSysctlUtil;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import oshi.util.Memoizer;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractVirtualMemory;

@ThreadSafe
final class FreeBsdVirtualMemory extends AbstractVirtualMemory
{
    private final FreeBsdGlobalMemory global;
    private final Supplier<Long> used;
    private final Supplier<Long> total;
    private final Supplier<Long> pagesIn;
    private final Supplier<Long> pagesOut;
    
    FreeBsdVirtualMemory(final FreeBsdGlobalMemory freeBsdGlobalMemory) {
        this.used = Memoizer.memoize(FreeBsdVirtualMemory::querySwapUsed, Memoizer.defaultExpiration());
        this.total = Memoizer.memoize(FreeBsdVirtualMemory::querySwapTotal, Memoizer.defaultExpiration());
        this.pagesIn = Memoizer.memoize(FreeBsdVirtualMemory::queryPagesIn, Memoizer.defaultExpiration());
        this.pagesOut = Memoizer.memoize(FreeBsdVirtualMemory::queryPagesOut, Memoizer.defaultExpiration());
        this.global = freeBsdGlobalMemory;
    }
    
    @Override
    public long getSwapUsed() {
        return this.used.get();
    }
    
    @Override
    public long getSwapTotal() {
        return this.total.get();
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
        return this.pagesIn.get();
    }
    
    @Override
    public long getSwapPagesOut() {
        return this.pagesOut.get();
    }
    
    private static long querySwapUsed() {
        final String swapInfo = ExecutingCommand.getAnswerAt("swapinfo -k", 1);
        final String[] split = ParseUtil.whitespaces.split(swapInfo);
        if (split.length < 5) {
            return 0L;
        }
        return ParseUtil.parseLongOrDefault(split[2], 0L) << 10;
    }
    
    private static long querySwapTotal() {
        return BsdSysctlUtil.sysctl("vm.swap_total", 0L);
    }
    
    private static long queryPagesIn() {
        return BsdSysctlUtil.sysctl("vm.stats.vm.v_swappgsin", 0L);
    }
    
    private static long queryPagesOut() {
        return BsdSysctlUtil.sysctl("vm.stats.vm.v_swappgsout", 0L);
    }
}
