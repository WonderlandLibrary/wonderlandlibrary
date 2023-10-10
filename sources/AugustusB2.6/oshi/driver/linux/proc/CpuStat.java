// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.linux.proc;

import java.util.Iterator;
import java.util.List;
import oshi.util.ParseUtil;
import oshi.util.FileUtil;
import oshi.util.platform.linux.ProcPath;
import oshi.hardware.CentralProcessor;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class CpuStat
{
    private CpuStat() {
    }
    
    public static long[] getSystemCpuLoadTicks() {
        final long[] ticks = new long[CentralProcessor.TickType.values().length];
        final List<String> procStat = FileUtil.readFile(ProcPath.STAT);
        if (procStat.isEmpty()) {
            return ticks;
        }
        final String tickStr = procStat.get(0);
        final String[] tickArr = ParseUtil.whitespaces.split(tickStr);
        if (tickArr.length <= CentralProcessor.TickType.IDLE.getIndex()) {
            return ticks;
        }
        for (int i = 0; i < CentralProcessor.TickType.values().length; ++i) {
            ticks[i] = ParseUtil.parseLongOrDefault(tickArr[i + 1], 0L);
        }
        return ticks;
    }
    
    public static long[][] getProcessorCpuLoadTicks(final int logicalProcessorCount) {
        final long[][] ticks = new long[logicalProcessorCount][CentralProcessor.TickType.values().length];
        int cpu = 0;
        final List<String> procStat = FileUtil.readFile(ProcPath.STAT);
        for (final String stat : procStat) {
            if (stat.startsWith("cpu") && !stat.startsWith("cpu ")) {
                final String[] tickArr = ParseUtil.whitespaces.split(stat);
                if (tickArr.length <= CentralProcessor.TickType.IDLE.getIndex()) {
                    return ticks;
                }
                for (int i = 0; i < CentralProcessor.TickType.values().length; ++i) {
                    ticks[cpu][i] = ParseUtil.parseLongOrDefault(tickArr[i + 1], 0L);
                }
                if (++cpu >= logicalProcessorCount) {
                    break;
                }
                continue;
            }
        }
        return ticks;
    }
    
    public static long getContextSwitches() {
        final List<String> procStat = FileUtil.readFile(ProcPath.STAT);
        for (final String stat : procStat) {
            if (stat.startsWith("ctxt ")) {
                final String[] ctxtArr = ParseUtil.whitespaces.split(stat);
                if (ctxtArr.length == 2) {
                    return ParseUtil.parseLongOrDefault(ctxtArr[1], 0L);
                }
                continue;
            }
        }
        return 0L;
    }
    
    public static long getInterrupts() {
        final List<String> procStat = FileUtil.readFile(ProcPath.STAT);
        for (final String stat : procStat) {
            if (stat.startsWith("intr ")) {
                final String[] intrArr = ParseUtil.whitespaces.split(stat);
                if (intrArr.length > 2) {
                    return ParseUtil.parseLongOrDefault(intrArr[1], 0L);
                }
                continue;
            }
        }
        return 0L;
    }
    
    public static long getBootTime() {
        final List<String> procStat = FileUtil.readFile(ProcPath.STAT);
        for (final String stat : procStat) {
            if (stat.startsWith("btime")) {
                final String[] bTime = ParseUtil.whitespaces.split(stat);
                return ParseUtil.parseLongOrDefault(bTime[1], 0L);
            }
        }
        return 0L;
    }
}
