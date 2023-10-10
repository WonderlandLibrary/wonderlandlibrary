// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.aix;

import oshi.driver.unix.aix.perfstat.PerfstatMemory;
import java.util.Iterator;
import oshi.util.ParseUtil;
import java.util.ArrayList;
import oshi.hardware.PhysicalMemory;
import oshi.util.Memoizer;
import oshi.hardware.VirtualMemory;
import java.util.List;
import com.sun.jna.platform.unix.aix.Perfstat;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractGlobalMemory;

@ThreadSafe
final class AixGlobalMemory extends AbstractGlobalMemory
{
    private final Supplier<Perfstat.perfstat_memory_total_t> perfstatMem;
    private final Supplier<List<String>> lscfg;
    private static final long PAGESIZE = 4096L;
    private final Supplier<VirtualMemory> vm;
    
    AixGlobalMemory(final Supplier<List<String>> lscfg) {
        this.perfstatMem = Memoizer.memoize(AixGlobalMemory::queryPerfstat, Memoizer.defaultExpiration());
        this.vm = Memoizer.memoize(this::createVirtualMemory);
        this.lscfg = lscfg;
    }
    
    @Override
    public long getAvailable() {
        return this.perfstatMem.get().real_avail * 4096L;
    }
    
    @Override
    public long getTotal() {
        return this.perfstatMem.get().real_total * 4096L;
    }
    
    @Override
    public long getPageSize() {
        return 4096L;
    }
    
    @Override
    public VirtualMemory getVirtualMemory() {
        return this.vm.get();
    }
    
    @Override
    public List<PhysicalMemory> getPhysicalMemory() {
        final List<PhysicalMemory> pmList = new ArrayList<PhysicalMemory>();
        boolean isMemModule = false;
        String bankLabel = "unknown";
        String locator = "";
        long capacity = 0L;
        for (final String line : this.lscfg.get()) {
            final String s = line.trim();
            if (s.endsWith("memory-module")) {
                isMemModule = true;
            }
            else {
                if (!isMemModule) {
                    continue;
                }
                if (s.startsWith("Node:")) {
                    bankLabel = s.substring(5).trim();
                    if (!bankLabel.startsWith("IBM,")) {
                        continue;
                    }
                    bankLabel = bankLabel.substring(4);
                }
                else if (s.startsWith("Physical Location:")) {
                    locator = "/" + s.substring(18).trim();
                }
                else if (s.startsWith("Size")) {
                    capacity = ParseUtil.parseLongOrDefault(ParseUtil.removeLeadingDots(s.substring(4).trim()), 0L) << 20;
                }
                else {
                    if (!s.startsWith("Hardware Location Code")) {
                        continue;
                    }
                    if (capacity > 0L) {
                        pmList.add(new PhysicalMemory(bankLabel + locator, capacity, 0L, "IBM", "unknown"));
                    }
                    bankLabel = "unknown";
                    locator = "";
                    capacity = 0L;
                    isMemModule = false;
                }
            }
        }
        return pmList;
    }
    
    private static Perfstat.perfstat_memory_total_t queryPerfstat() {
        return PerfstatMemory.queryMemoryTotal();
    }
    
    private VirtualMemory createVirtualMemory() {
        return new AixVirtualMemory(this.perfstatMem);
    }
}
