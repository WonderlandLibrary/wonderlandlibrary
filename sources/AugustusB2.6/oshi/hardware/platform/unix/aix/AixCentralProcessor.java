// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.aix;

import oshi.util.FileUtil;
import oshi.util.ParseUtil;
import java.util.Arrays;
import java.util.Map;
import java.util.ArrayList;
import oshi.driver.unix.aix.Lssrad;
import oshi.driver.unix.aix.perfstat.PerfstatConfig;
import java.util.List;
import oshi.util.tuples.Pair;
import java.util.Iterator;
import com.sun.jna.Native;
import oshi.util.ExecutingCommand;
import oshi.hardware.CentralProcessor;
import oshi.util.Memoizer;
import oshi.driver.unix.aix.perfstat.PerfstatCpu;
import com.sun.jna.platform.unix.aix.Perfstat;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractCentralProcessor;

@ThreadSafe
final class AixCentralProcessor extends AbstractCentralProcessor
{
    private final Supplier<Perfstat.perfstat_cpu_total_t> cpuTotal;
    private final Supplier<Perfstat.perfstat_cpu_t[]> cpuProc;
    private static final int SBITS;
    private Perfstat.perfstat_partition_config_t config;
    private static final long USER_HZ;
    
    AixCentralProcessor() {
        this.cpuTotal = Memoizer.memoize(PerfstatCpu::queryCpuTotal, Memoizer.defaultExpiration());
        this.cpuProc = Memoizer.memoize(PerfstatCpu::queryCpu, Memoizer.defaultExpiration());
    }
    
    @Override
    protected CentralProcessor.ProcessorIdentifier queryProcessorId() {
        String cpuVendor = "unknown";
        String cpuName = "";
        String cpuFamily = "";
        boolean cpu64bit = false;
        final String nameMarker = "Processor Type:";
        final String familyMarker = "Processor Version:";
        final String bitnessMarker = "CPU Type:";
        for (final String checkLine : ExecutingCommand.runNative("prtconf")) {
            if (checkLine.startsWith("Processor Type:")) {
                cpuName = checkLine.split("Processor Type:")[1].trim();
                if (cpuName.startsWith("P")) {
                    cpuVendor = "IBM";
                }
                else {
                    if (!cpuName.startsWith("I")) {
                        continue;
                    }
                    cpuVendor = "Intel";
                }
            }
            else if (checkLine.startsWith("Processor Version:")) {
                cpuFamily = checkLine.split("Processor Version:")[1].trim();
            }
            else {
                if (!checkLine.startsWith("CPU Type:")) {
                    continue;
                }
                cpu64bit = checkLine.split("CPU Type:")[1].contains("64");
            }
        }
        String cpuModel = "";
        String cpuStepping = "";
        String machineId = Native.toString(this.config.machineID);
        if (machineId.isEmpty()) {
            machineId = ExecutingCommand.getFirstAnswer("uname -m");
        }
        if (machineId.length() > 10) {
            final int m = machineId.length() - 4;
            final int s = machineId.length() - 2;
            cpuModel = machineId.substring(m, s);
            cpuStepping = machineId.substring(s);
        }
        return new CentralProcessor.ProcessorIdentifier(cpuVendor, cpuName, cpuFamily, cpuModel, cpuStepping, machineId, cpu64bit, (long)(this.config.processorMHz * 1000000.0));
    }
    
    @Override
    protected Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>> initProcessorCounts() {
        this.config = PerfstatConfig.queryConfig();
        int physProcs = (int)this.config.numProcessors.max;
        if (physProcs < 1) {
            physProcs = 1;
        }
        int lcpus = this.config.lcpus;
        if (lcpus < 1) {
            lcpus = 1;
        }
        final Map<Integer, Pair<Integer, Integer>> nodePkgMap = Lssrad.queryNodesPackages();
        final List<CentralProcessor.LogicalProcessor> logProcs = new ArrayList<CentralProcessor.LogicalProcessor>();
        for (int proc = 0; proc < lcpus; ++proc) {
            final Pair<Integer, Integer> nodePkg = nodePkgMap.get(proc);
            logProcs.add(new CentralProcessor.LogicalProcessor(proc, proc / physProcs, (nodePkg == null) ? 0 : nodePkg.getB(), (nodePkg == null) ? 0 : nodePkg.getA()));
        }
        return new Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>>(logProcs, null);
    }
    
    public long[] querySystemCpuLoadTicks() {
        final Perfstat.perfstat_cpu_total_t perfstat = this.cpuTotal.get();
        final long[] ticks = new long[CentralProcessor.TickType.values().length];
        ticks[CentralProcessor.TickType.USER.ordinal()] = perfstat.user * 1000L / AixCentralProcessor.USER_HZ;
        ticks[CentralProcessor.TickType.SYSTEM.ordinal()] = perfstat.sys * 1000L / AixCentralProcessor.USER_HZ;
        ticks[CentralProcessor.TickType.IDLE.ordinal()] = perfstat.idle * 1000L / AixCentralProcessor.USER_HZ;
        ticks[CentralProcessor.TickType.IOWAIT.ordinal()] = perfstat.wait * 1000L / AixCentralProcessor.USER_HZ;
        ticks[CentralProcessor.TickType.IRQ.ordinal()] = perfstat.devintrs * 1000L / AixCentralProcessor.USER_HZ;
        ticks[CentralProcessor.TickType.SOFTIRQ.ordinal()] = perfstat.softintrs * 1000L / AixCentralProcessor.USER_HZ;
        ticks[CentralProcessor.TickType.STEAL.ordinal()] = (perfstat.idle_stolen_purr + perfstat.busy_stolen_purr) * 1000L / AixCentralProcessor.USER_HZ;
        return ticks;
    }
    
    public long[] queryCurrentFreq() {
        final long[] freqs = new long[this.getLogicalProcessorCount()];
        Arrays.fill(freqs, -1L);
        final String freqMarker = "runs at";
        int idx = 0;
        for (final String checkLine : ExecutingCommand.runNative("pmcycles -m")) {
            if (checkLine.contains(freqMarker)) {
                freqs[idx++] = ParseUtil.parseHertz(checkLine.split(freqMarker)[1].trim());
                if (idx >= freqs.length) {
                    break;
                }
                continue;
            }
        }
        return freqs;
    }
    
    @Override
    protected long queryMaxFreq() {
        final Perfstat.perfstat_cpu_total_t perfstat = this.cpuTotal.get();
        return perfstat.processorHZ;
    }
    
    @Override
    public double[] getSystemLoadAverage(final int nelem) {
        if (nelem < 1 || nelem > 3) {
            throw new IllegalArgumentException("Must include from one to three elements.");
        }
        final double[] average = new double[nelem];
        final long[] loadavg = this.cpuTotal.get().loadavg;
        for (int i = 0; i < nelem; ++i) {
            average[i] = loadavg[i] / (double)(1L << AixCentralProcessor.SBITS);
        }
        return average;
    }
    
    public long[][] queryProcessorCpuLoadTicks() {
        final Perfstat.perfstat_cpu_t[] cpu = this.cpuProc.get();
        final long[][] ticks = new long[cpu.length][CentralProcessor.TickType.values().length];
        for (int i = 0; i < cpu.length; ++i) {
            (ticks[i] = new long[CentralProcessor.TickType.values().length])[CentralProcessor.TickType.USER.ordinal()] = cpu[i].user * 1000L / AixCentralProcessor.USER_HZ;
            ticks[i][CentralProcessor.TickType.SYSTEM.ordinal()] = cpu[i].sys * 1000L / AixCentralProcessor.USER_HZ;
            ticks[i][CentralProcessor.TickType.IDLE.ordinal()] = cpu[i].idle * 1000L / AixCentralProcessor.USER_HZ;
            ticks[i][CentralProcessor.TickType.IOWAIT.ordinal()] = cpu[i].wait * 1000L / AixCentralProcessor.USER_HZ;
            ticks[i][CentralProcessor.TickType.IRQ.ordinal()] = cpu[i].devintrs * 1000L / AixCentralProcessor.USER_HZ;
            ticks[i][CentralProcessor.TickType.SOFTIRQ.ordinal()] = cpu[i].softintrs * 1000L / AixCentralProcessor.USER_HZ;
            ticks[i][CentralProcessor.TickType.STEAL.ordinal()] = (cpu[i].idle_stolen_purr + cpu[i].busy_stolen_purr) * 1000L / AixCentralProcessor.USER_HZ;
        }
        return ticks;
    }
    
    public long queryContextSwitches() {
        return this.cpuTotal.get().pswitch;
    }
    
    public long queryInterrupts() {
        final Perfstat.perfstat_cpu_total_t cpu = this.cpuTotal.get();
        return cpu.devintrs + cpu.softintrs;
    }
    
    private static int querySbits() {
        for (final String s : FileUtil.readFile("/usr/include/sys/proc.h")) {
            if (s.contains("SBITS") && s.contains("#define")) {
                return ParseUtil.parseLastInt(s, 16);
            }
        }
        return 16;
    }
    
    static {
        SBITS = querySbits();
        USER_HZ = ParseUtil.parseLongOrDefault(ExecutingCommand.getFirstAnswer("getconf CLK_TCK"), 100L);
    }
}
