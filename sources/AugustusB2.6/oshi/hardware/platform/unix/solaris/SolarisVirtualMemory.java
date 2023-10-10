// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.solaris;

import java.util.regex.Matcher;
import java.util.Iterator;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import oshi.util.Memoizer;
import oshi.driver.unix.solaris.kstat.SystemPages;
import oshi.util.tuples.Pair;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractVirtualMemory;

@ThreadSafe
final class SolarisVirtualMemory extends AbstractVirtualMemory
{
    private static final Pattern SWAP_INFO;
    private final SolarisGlobalMemory global;
    private final Supplier<Pair<Long, Long>> availTotal;
    private final Supplier<Pair<Long, Long>> usedTotal;
    private final Supplier<Long> pagesIn;
    private final Supplier<Long> pagesOut;
    
    SolarisVirtualMemory(final SolarisGlobalMemory solarisGlobalMemory) {
        this.availTotal = Memoizer.memoize(SystemPages::queryAvailableTotal, Memoizer.defaultExpiration());
        this.usedTotal = Memoizer.memoize(SolarisVirtualMemory::querySwapInfo, Memoizer.defaultExpiration());
        this.pagesIn = Memoizer.memoize(SolarisVirtualMemory::queryPagesIn, Memoizer.defaultExpiration());
        this.pagesOut = Memoizer.memoize(SolarisVirtualMemory::queryPagesOut, Memoizer.defaultExpiration());
        this.global = solarisGlobalMemory;
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
        return this.global.getPageSize() * this.availTotal.get().getB() + this.getSwapTotal();
    }
    
    @Override
    public long getVirtualInUse() {
        return this.global.getPageSize() * (this.availTotal.get().getB() - this.availTotal.get().getA()) + this.getSwapUsed();
    }
    
    @Override
    public long getSwapPagesIn() {
        return this.pagesIn.get();
    }
    
    @Override
    public long getSwapPagesOut() {
        return this.pagesOut.get();
    }
    
    private static long queryPagesIn() {
        long swapPagesIn = 0L;
        for (final String s : ExecutingCommand.runNative("kstat -p cpu_stat:::pgswapin")) {
            swapPagesIn += ParseUtil.parseLastLong(s, 0L);
        }
        return swapPagesIn;
    }
    
    private static long queryPagesOut() {
        long swapPagesOut = 0L;
        for (final String s : ExecutingCommand.runNative("kstat -p cpu_stat:::pgswapout")) {
            swapPagesOut += ParseUtil.parseLastLong(s, 0L);
        }
        return swapPagesOut;
    }
    
    private static Pair<Long, Long> querySwapInfo() {
        long swapTotal = 0L;
        long swapUsed = 0L;
        final String swap = ExecutingCommand.getAnswerAt("swap -lk", 1);
        final Matcher m = SolarisVirtualMemory.SWAP_INFO.matcher(swap);
        if (m.matches()) {
            swapTotal = ParseUtil.parseLongOrDefault(m.group(1), 0L) << 10;
            swapUsed = swapTotal - (ParseUtil.parseLongOrDefault(m.group(2), 0L) << 10);
        }
        return new Pair<Long, Long>(swapUsed, swapTotal);
    }
    
    static {
        SWAP_INFO = Pattern.compile(".+\\s(\\d+)K\\s+(\\d+)K$");
    }
}
