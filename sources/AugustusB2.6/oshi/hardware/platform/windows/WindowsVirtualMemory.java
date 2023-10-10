// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.windows;

import org.slf4j.LoggerFactory;
import java.util.Map;
import oshi.driver.windows.perfmon.MemoryInformation;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Psapi;
import oshi.driver.windows.perfmon.PagingFile;
import oshi.util.Memoizer;
import oshi.util.tuples.Pair;
import oshi.util.tuples.Triplet;
import java.util.function.Supplier;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractVirtualMemory;

@ThreadSafe
final class WindowsVirtualMemory extends AbstractVirtualMemory
{
    private static final Logger LOG;
    private final WindowsGlobalMemory global;
    private final Supplier<Long> used;
    private final Supplier<Triplet<Long, Long, Long>> totalVmaxVused;
    private final Supplier<Pair<Long, Long>> swapInOut;
    
    WindowsVirtualMemory(final WindowsGlobalMemory windowsGlobalMemory) {
        this.used = Memoizer.memoize(WindowsVirtualMemory::querySwapUsed, Memoizer.defaultExpiration());
        this.totalVmaxVused = Memoizer.memoize(WindowsVirtualMemory::querySwapTotalVirtMaxVirtUsed, Memoizer.defaultExpiration());
        this.swapInOut = Memoizer.memoize(WindowsVirtualMemory::queryPageSwaps, Memoizer.defaultExpiration());
        this.global = windowsGlobalMemory;
    }
    
    @Override
    public long getSwapUsed() {
        return this.global.getPageSize() * this.used.get();
    }
    
    @Override
    public long getSwapTotal() {
        return this.global.getPageSize() * this.totalVmaxVused.get().getA();
    }
    
    @Override
    public long getVirtualMax() {
        return this.global.getPageSize() * this.totalVmaxVused.get().getB();
    }
    
    @Override
    public long getVirtualInUse() {
        return this.global.getPageSize() * this.totalVmaxVused.get().getC();
    }
    
    @Override
    public long getSwapPagesIn() {
        return this.swapInOut.get().getA();
    }
    
    @Override
    public long getSwapPagesOut() {
        return this.swapInOut.get().getB();
    }
    
    private static long querySwapUsed() {
        return PagingFile.querySwapUsed().getOrDefault(PagingFile.PagingPercentProperty.PERCENTUSAGE, 0L);
    }
    
    private static Triplet<Long, Long, Long> querySwapTotalVirtMaxVirtUsed() {
        final Psapi.PERFORMANCE_INFORMATION perfInfo = new Psapi.PERFORMANCE_INFORMATION();
        if (!Psapi.INSTANCE.GetPerformanceInfo(perfInfo, perfInfo.size())) {
            WindowsVirtualMemory.LOG.error("Failed to get Performance Info. Error code: {}", (Object)Kernel32.INSTANCE.GetLastError());
            return new Triplet<Long, Long, Long>(0L, 0L, 0L);
        }
        return new Triplet<Long, Long, Long>(perfInfo.CommitLimit.longValue() - perfInfo.PhysicalTotal.longValue(), perfInfo.CommitLimit.longValue(), perfInfo.CommitTotal.longValue());
    }
    
    private static Pair<Long, Long> queryPageSwaps() {
        final Map<MemoryInformation.PageSwapProperty, Long> valueMap = MemoryInformation.queryPageSwaps();
        return new Pair<Long, Long>(valueMap.getOrDefault(MemoryInformation.PageSwapProperty.PAGESINPUTPERSEC, 0L), valueMap.getOrDefault(MemoryInformation.PageSwapProperty.PAGESOUTPUTPERSEC, 0L));
    }
    
    static {
        LOG = LoggerFactory.getLogger(WindowsVirtualMemory.class);
    }
}
