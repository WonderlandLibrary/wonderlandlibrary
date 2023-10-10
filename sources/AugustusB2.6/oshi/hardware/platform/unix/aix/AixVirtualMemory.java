// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.aix;

import com.sun.jna.platform.unix.aix.Perfstat;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractVirtualMemory;

@ThreadSafe
final class AixVirtualMemory extends AbstractVirtualMemory
{
    private final Supplier<Perfstat.perfstat_memory_total_t> perfstatMem;
    private static final long PAGESIZE = 4096L;
    
    AixVirtualMemory(final Supplier<Perfstat.perfstat_memory_total_t> perfstatMem) {
        this.perfstatMem = perfstatMem;
    }
    
    @Override
    public long getSwapUsed() {
        final Perfstat.perfstat_memory_total_t perfstat = this.perfstatMem.get();
        return (perfstat.pgsp_total - perfstat.pgsp_free) * 4096L;
    }
    
    @Override
    public long getSwapTotal() {
        return this.perfstatMem.get().pgsp_total * 4096L;
    }
    
    @Override
    public long getVirtualMax() {
        return this.perfstatMem.get().virt_total * 4096L;
    }
    
    @Override
    public long getVirtualInUse() {
        return this.perfstatMem.get().virt_active * 4096L;
    }
    
    @Override
    public long getSwapPagesIn() {
        return this.perfstatMem.get().pgspins;
    }
    
    @Override
    public long getSwapPagesOut() {
        return this.perfstatMem.get().pgspouts;
    }
}
