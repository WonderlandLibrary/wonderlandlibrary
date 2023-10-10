// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.linux;

import oshi.util.ExecutingCommand;
import java.util.Iterator;
import java.util.List;
import oshi.util.ParseUtil;
import oshi.util.FileUtil;
import oshi.util.platform.linux.ProcPath;
import oshi.util.Memoizer;
import oshi.hardware.VirtualMemory;
import oshi.util.tuples.Pair;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractGlobalMemory;

@ThreadSafe
public final class LinuxGlobalMemory extends AbstractGlobalMemory
{
    public static final long PAGE_SIZE;
    private final Supplier<Pair<Long, Long>> availTotal;
    private final Supplier<VirtualMemory> vm;
    
    public LinuxGlobalMemory() {
        this.availTotal = Memoizer.memoize(LinuxGlobalMemory::readMemInfo, Memoizer.defaultExpiration());
        this.vm = Memoizer.memoize(this::createVirtualMemory);
    }
    
    @Override
    public long getAvailable() {
        return this.availTotal.get().getA();
    }
    
    @Override
    public long getTotal() {
        return this.availTotal.get().getB();
    }
    
    @Override
    public long getPageSize() {
        return LinuxGlobalMemory.PAGE_SIZE;
    }
    
    @Override
    public VirtualMemory getVirtualMemory() {
        return this.vm.get();
    }
    
    private static Pair<Long, Long> readMemInfo() {
        long memFree = 0L;
        long activeFile = 0L;
        long inactiveFile = 0L;
        long sReclaimable = 0L;
        long memTotal = 0L;
        final List<String> procMemInfo = FileUtil.readFile(ProcPath.MEMINFO);
        for (final String checkLine : procMemInfo) {
            final String[] memorySplit = ParseUtil.whitespaces.split(checkLine, 2);
            if (memorySplit.length > 1) {
                final String s = memorySplit[0];
                switch (s) {
                    case "MemTotal:": {
                        memTotal = ParseUtil.parseDecimalMemorySizeToBinary(memorySplit[1]);
                        continue;
                    }
                    case "MemAvailable:": {
                        final long memAvailable = ParseUtil.parseDecimalMemorySizeToBinary(memorySplit[1]);
                        return new Pair<Long, Long>(memAvailable, memTotal);
                    }
                    case "MemFree:": {
                        memFree = ParseUtil.parseDecimalMemorySizeToBinary(memorySplit[1]);
                        continue;
                    }
                    case "Active(file):": {
                        activeFile = ParseUtil.parseDecimalMemorySizeToBinary(memorySplit[1]);
                        continue;
                    }
                    case "Inactive(file):": {
                        inactiveFile = ParseUtil.parseDecimalMemorySizeToBinary(memorySplit[1]);
                        continue;
                    }
                    case "SReclaimable:": {
                        sReclaimable = ParseUtil.parseDecimalMemorySizeToBinary(memorySplit[1]);
                        continue;
                    }
                }
            }
        }
        return new Pair<Long, Long>(memFree + activeFile + inactiveFile + sReclaimable, memTotal);
    }
    
    private VirtualMemory createVirtualMemory() {
        return new LinuxVirtualMemory(this);
    }
    
    static {
        PAGE_SIZE = ParseUtil.parseLongOrDefault(ExecutingCommand.getFirstAnswer("getconf PAGE_SIZE"), 4096L);
    }
}
