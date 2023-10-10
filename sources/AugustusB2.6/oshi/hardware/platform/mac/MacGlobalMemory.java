// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.mac;

import org.slf4j.LoggerFactory;
import com.sun.jna.ptr.LongByReference;
import oshi.util.platform.mac.SysctlUtil;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.mac.SystemB;
import java.util.Iterator;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import java.util.ArrayList;
import oshi.hardware.PhysicalMemory;
import java.util.List;
import oshi.util.Memoizer;
import oshi.hardware.VirtualMemory;
import java.util.function.Supplier;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractGlobalMemory;

@ThreadSafe
final class MacGlobalMemory extends AbstractGlobalMemory
{
    private static final Logger LOG;
    private final Supplier<Long> available;
    private final Supplier<Long> total;
    private final Supplier<Long> pageSize;
    private final Supplier<VirtualMemory> vm;
    
    MacGlobalMemory() {
        this.available = Memoizer.memoize(this::queryVmStats, Memoizer.defaultExpiration());
        this.total = Memoizer.memoize(MacGlobalMemory::queryPhysMem);
        this.pageSize = Memoizer.memoize(MacGlobalMemory::queryPageSize);
        this.vm = Memoizer.memoize(this::createVirtualMemory);
    }
    
    @Override
    public long getAvailable() {
        return this.available.get();
    }
    
    @Override
    public long getTotal() {
        return this.total.get();
    }
    
    @Override
    public long getPageSize() {
        return this.pageSize.get();
    }
    
    @Override
    public VirtualMemory getVirtualMemory() {
        return this.vm.get();
    }
    
    @Override
    public List<PhysicalMemory> getPhysicalMemory() {
        final List<PhysicalMemory> pmList = new ArrayList<PhysicalMemory>();
        final List<String> sp = ExecutingCommand.runNative("system_profiler SPMemoryDataType");
        int bank = 0;
        String bankLabel = "unknown";
        long capacity = 0L;
        long speed = 0L;
        String manufacturer = "unknown";
        String memoryType = "unknown";
        for (final String line : sp) {
            if (line.trim().startsWith("BANK")) {
                if (bank++ > 0) {
                    pmList.add(new PhysicalMemory(bankLabel, capacity, speed, manufacturer, memoryType));
                }
                bankLabel = line.trim();
                final int colon = bankLabel.lastIndexOf(58);
                if (colon <= 0) {
                    continue;
                }
                bankLabel = bankLabel.substring(0, colon - 1);
            }
            else {
                if (bank <= 0) {
                    continue;
                }
                final String[] split = line.trim().split(":");
                if (split.length != 2) {
                    continue;
                }
                final String s = split[0];
                switch (s) {
                    case "Size": {
                        capacity = ParseUtil.parseDecimalMemorySizeToBinary(split[1].trim());
                        continue;
                    }
                    case "Type": {
                        memoryType = split[1].trim();
                        continue;
                    }
                    case "Speed": {
                        speed = ParseUtil.parseHertz(split[1]);
                        continue;
                    }
                    case "Manufacturer": {
                        manufacturer = split[1].trim();
                        continue;
                    }
                }
            }
        }
        pmList.add(new PhysicalMemory(bankLabel, capacity, speed, manufacturer, memoryType));
        return pmList;
    }
    
    private long queryVmStats() {
        final SystemB.VMStatistics vmStats = new SystemB.VMStatistics();
        if (0 != SystemB.INSTANCE.host_statistics(SystemB.INSTANCE.mach_host_self(), 2, vmStats, new IntByReference(vmStats.size() / SystemB.INT_SIZE))) {
            MacGlobalMemory.LOG.error("Failed to get host VM info. Error code: {}", (Object)Native.getLastError());
            return 0L;
        }
        return (vmStats.free_count + vmStats.inactive_count) * this.getPageSize();
    }
    
    private static long queryPhysMem() {
        return SysctlUtil.sysctl("hw.memsize", 0L);
    }
    
    private static long queryPageSize() {
        final LongByReference pPageSize = new LongByReference();
        if (0 == SystemB.INSTANCE.host_page_size(SystemB.INSTANCE.mach_host_self(), pPageSize)) {
            return pPageSize.getValue();
        }
        MacGlobalMemory.LOG.error("Failed to get host page size. Error code: {}", (Object)Native.getLastError());
        return 4098L;
    }
    
    private VirtualMemory createVirtualMemory() {
        return new MacVirtualMemory(this);
    }
    
    static {
        LOG = LoggerFactory.getLogger(MacGlobalMemory.class);
    }
}
