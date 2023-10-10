// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.openbsd;

import com.sun.jna.Memory;
import java.util.Iterator;
import com.sun.jna.Pointer;
import oshi.jna.platform.unix.OpenBsdLibc;
import oshi.util.platform.unix.openbsd.OpenBsdSysctlUtil;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import oshi.util.Memoizer;
import oshi.hardware.VirtualMemory;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractGlobalMemory;

@ThreadSafe
final class OpenBsdGlobalMemory extends AbstractGlobalMemory
{
    private final Supplier<Long> available;
    private final Supplier<Long> total;
    private final Supplier<Long> pageSize;
    private final Supplier<VirtualMemory> vm;
    
    OpenBsdGlobalMemory() {
        this.available = Memoizer.memoize(OpenBsdGlobalMemory::queryAvailable, Memoizer.defaultExpiration());
        this.total = Memoizer.memoize(OpenBsdGlobalMemory::queryPhysMem);
        this.pageSize = Memoizer.memoize(OpenBsdGlobalMemory::queryPageSize);
        this.vm = Memoizer.memoize(this::createVirtualMemory);
    }
    
    @Override
    public long getAvailable() {
        return this.available.get() * this.getPageSize();
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
    
    private static long queryAvailable() {
        long free = 0L;
        long inactive = 0L;
        for (final String line : ExecutingCommand.runNative("vmstat -s")) {
            if (line.endsWith("pages free")) {
                free = ParseUtil.getFirstIntValue(line);
            }
            else {
                if (!line.endsWith("pages inactive")) {
                    continue;
                }
                inactive = ParseUtil.getFirstIntValue(line);
            }
        }
        final int[] mib = { 10, 0, 3 };
        final Memory m = OpenBsdSysctlUtil.sysctl(mib);
        final OpenBsdLibc.Bcachestats cache = new OpenBsdLibc.Bcachestats(m);
        return cache.numbufpages + free + inactive;
    }
    
    private static long queryPhysMem() {
        return OpenBsdSysctlUtil.sysctl("hw.physmem", 0L);
    }
    
    private static long queryPageSize() {
        return OpenBsdSysctlUtil.sysctl("hw.pagesize", 4096L);
    }
    
    private VirtualMemory createVirtualMemory() {
        return new OpenBsdVirtualMemory(this);
    }
}
