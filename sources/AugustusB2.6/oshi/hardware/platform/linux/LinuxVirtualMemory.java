// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.linux;

import java.util.Iterator;
import java.util.List;
import oshi.util.ParseUtil;
import oshi.util.FileUtil;
import oshi.util.platform.linux.ProcPath;
import oshi.util.Memoizer;
import oshi.util.tuples.Pair;
import oshi.util.tuples.Triplet;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractVirtualMemory;

@ThreadSafe
final class LinuxVirtualMemory extends AbstractVirtualMemory
{
    private final LinuxGlobalMemory global;
    private final Supplier<Triplet<Long, Long, Long>> usedTotalCommitLim;
    private final Supplier<Pair<Long, Long>> inOut;
    
    LinuxVirtualMemory(final LinuxGlobalMemory linuxGlobalMemory) {
        this.usedTotalCommitLim = Memoizer.memoize(LinuxVirtualMemory::queryMemInfo, Memoizer.defaultExpiration());
        this.inOut = Memoizer.memoize(LinuxVirtualMemory::queryVmStat, Memoizer.defaultExpiration());
        this.global = linuxGlobalMemory;
    }
    
    @Override
    public long getSwapUsed() {
        return this.usedTotalCommitLim.get().getA();
    }
    
    @Override
    public long getSwapTotal() {
        return this.usedTotalCommitLim.get().getB();
    }
    
    @Override
    public long getVirtualMax() {
        return this.usedTotalCommitLim.get().getC();
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
    
    private static Triplet<Long, Long, Long> queryMemInfo() {
        long swapFree = 0L;
        long swapTotal = 0L;
        long commitLimit = 0L;
        final List<String> procMemInfo = FileUtil.readFile(ProcPath.MEMINFO);
        for (final String checkLine : procMemInfo) {
            final String[] memorySplit = ParseUtil.whitespaces.split(checkLine);
            if (memorySplit.length > 1) {
                final String s = memorySplit[0];
                switch (s) {
                    case "SwapTotal:": {
                        swapTotal = parseMeminfo(memorySplit);
                        continue;
                    }
                    case "SwapFree:": {
                        swapFree = parseMeminfo(memorySplit);
                        continue;
                    }
                    case "CommitLimit:": {
                        commitLimit = parseMeminfo(memorySplit);
                        continue;
                    }
                }
            }
        }
        return new Triplet<Long, Long, Long>(swapTotal - swapFree, swapTotal, commitLimit);
    }
    
    private static Pair<Long, Long> queryVmStat() {
        long swapPagesIn = 0L;
        long swapPagesOut = 0L;
        final List<String> procVmStat = FileUtil.readFile(ProcPath.VMSTAT);
        for (final String checkLine : procVmStat) {
            final String[] memorySplit = ParseUtil.whitespaces.split(checkLine);
            if (memorySplit.length > 1) {
                final String s = memorySplit[0];
                switch (s) {
                    case "pswpin": {
                        swapPagesIn = ParseUtil.parseLongOrDefault(memorySplit[1], 0L);
                        continue;
                    }
                    case "pswpout": {
                        swapPagesOut = ParseUtil.parseLongOrDefault(memorySplit[1], 0L);
                        continue;
                    }
                }
            }
        }
        return new Pair<Long, Long>(swapPagesIn, swapPagesOut);
    }
    
    private static long parseMeminfo(final String[] memorySplit) {
        if (memorySplit.length < 2) {
            return 0L;
        }
        long memory = ParseUtil.parseLongOrDefault(memorySplit[1], 0L);
        if (memorySplit.length > 2 && "kB".equals(memorySplit[2])) {
            memory *= 1024L;
        }
        return memory;
    }
}
