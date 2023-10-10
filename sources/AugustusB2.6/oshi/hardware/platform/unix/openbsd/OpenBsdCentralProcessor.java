// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.openbsd;

import java.util.Arrays;
import oshi.jna.platform.unix.OpenBsdLibc;
import com.sun.jna.Native;
import com.sun.jna.Memory;
import java.util.regex.Matcher;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import oshi.util.tuples.Triplet;
import oshi.util.ExecutingCommand;
import oshi.util.ParseUtil;
import oshi.util.platform.unix.openbsd.OpenBsdSysctlUtil;
import oshi.hardware.CentralProcessor;
import oshi.util.Memoizer;
import java.util.regex.Pattern;
import oshi.util.tuples.Pair;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractCentralProcessor;

@ThreadSafe
public class OpenBsdCentralProcessor extends AbstractCentralProcessor
{
    private final Supplier<Pair<Long, Long>> vmStats;
    private static final Pattern DMESG_CPU;
    
    public OpenBsdCentralProcessor() {
        this.vmStats = Memoizer.memoize(OpenBsdCentralProcessor::queryVmStats, Memoizer.defaultExpiration());
    }
    
    @Override
    protected CentralProcessor.ProcessorIdentifier queryProcessorId() {
        final String cpuVendor = OpenBsdSysctlUtil.sysctl("machdep.cpuvendor", "");
        final int[] mib = { 6, 2 };
        final String cpuName = OpenBsdSysctlUtil.sysctl(mib, "");
        final int cpuid = ParseUtil.hexStringToInt(OpenBsdSysctlUtil.sysctl("machdep.cpuid", ""), 0);
        final int cpufeature = ParseUtil.hexStringToInt(OpenBsdSysctlUtil.sysctl("machdep.cpufeature", ""), 0);
        final Triplet<Integer, Integer, Integer> cpu = cpuidToFamilyModelStepping(cpuid);
        final String cpuFamily = cpu.getA().toString();
        final String cpuModel = cpu.getB().toString();
        final String cpuStepping = cpu.getC().toString();
        long cpuFreq = ParseUtil.parseHertz(cpuName);
        if (cpuFreq < 0L) {
            cpuFreq = this.queryMaxFreq();
        }
        mib[1] = 1;
        final String machine = OpenBsdSysctlUtil.sysctl(mib, "");
        final boolean cpu64bit = (machine != null && machine.contains("64")) || ExecutingCommand.getFirstAnswer("uname -m").trim().contains("64");
        final String processorID = String.format("%08x%08x", cpufeature, cpuid);
        return new CentralProcessor.ProcessorIdentifier(cpuVendor, cpuName, cpuFamily, cpuModel, cpuStepping, processorID, cpu64bit, cpuFreq);
    }
    
    private static Triplet<Integer, Integer, Integer> cpuidToFamilyModelStepping(final int cpuid) {
        final int family = (cpuid >> 16 & 0xFF0) | (cpuid >> 8 & 0xF);
        final int model = (cpuid >> 12 & 0xF0) | (cpuid >> 4 & 0xF);
        final int stepping = cpuid & 0xF;
        return new Triplet<Integer, Integer, Integer>(family, model, stepping);
    }
    
    @Override
    protected long queryMaxFreq() {
        return this.queryCurrentFreq()[0];
    }
    
    @Override
    protected long[] queryCurrentFreq() {
        final long[] freq = { 0L };
        final int[] mib = { 6, 12 };
        freq[0] = OpenBsdSysctlUtil.sysctl(mib, 0L) * 1000000L;
        return freq;
    }
    
    @Override
    protected Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>> initProcessorCounts() {
        final Map<Integer, Integer> coreMap = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> packageMap = new HashMap<Integer, Integer>();
        for (final String line : ExecutingCommand.runNative("dmesg")) {
            final Matcher m = OpenBsdCentralProcessor.DMESG_CPU.matcher(line);
            if (m.matches()) {
                final int cpu = ParseUtil.parseIntOrDefault(m.group(1), 0);
                coreMap.put(cpu, ParseUtil.parseIntOrDefault(m.group(3), 0));
                packageMap.put(cpu, ParseUtil.parseIntOrDefault(m.group(4), 0));
            }
        }
        int logicalProcessorCount = OpenBsdSysctlUtil.sysctl("hw.ncpuonline", 1);
        if (logicalProcessorCount < coreMap.keySet().size()) {
            logicalProcessorCount = coreMap.keySet().size();
        }
        final List<CentralProcessor.LogicalProcessor> logProcs = new ArrayList<CentralProcessor.LogicalProcessor>(logicalProcessorCount);
        for (int i = 0; i < logicalProcessorCount; ++i) {
            logProcs.add(new CentralProcessor.LogicalProcessor(i, coreMap.getOrDefault(i, 0), packageMap.getOrDefault(i, 0)));
        }
        final Map<Integer, String> dmesg = new HashMap<Integer, String>();
        final Pattern p = Pattern.compile("cpu(\\\\d+).*: ((ARM|AMD|Intel).+)");
        for (final String s : ExecutingCommand.runNative("dmesg")) {
            final Matcher j = p.matcher(s);
            if (j.matches()) {
                final int coreId = ParseUtil.parseIntOrDefault(j.group(1), 0);
                dmesg.put(coreId, j.group(2).trim());
            }
        }
        if (dmesg.isEmpty()) {
            return new Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>>(logProcs, null);
        }
        return new Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>>(logProcs, this.createProcListFromDmesg(logProcs, dmesg));
    }
    
    @Override
    protected long queryContextSwitches() {
        return this.vmStats.get().getA();
    }
    
    @Override
    protected long queryInterrupts() {
        return this.vmStats.get().getB();
    }
    
    private static Pair<Long, Long> queryVmStats() {
        long contextSwitches = 0L;
        long interrupts = 0L;
        final List<String> vmstat = ExecutingCommand.runNative("vmstat -s");
        for (final String line : vmstat) {
            if (line.endsWith("cpu context switches")) {
                contextSwitches = ParseUtil.getFirstIntValue(line);
            }
            else {
                if (!line.endsWith("interrupts")) {
                    continue;
                }
                interrupts = ParseUtil.getFirstIntValue(line);
            }
        }
        return new Pair<Long, Long>(contextSwitches, interrupts);
    }
    
    @Override
    protected long[] querySystemCpuLoadTicks() {
        final long[] ticks = new long[CentralProcessor.TickType.values().length];
        final int[] mib = { 1, 40 };
        final Memory m = OpenBsdSysctlUtil.sysctl(mib);
        final long[] cpuTicks = cpTimeToTicks(m, false);
        if (cpuTicks.length >= 5) {
            ticks[CentralProcessor.TickType.USER.getIndex()] = cpuTicks[0];
            ticks[CentralProcessor.TickType.NICE.getIndex()] = cpuTicks[1];
            ticks[CentralProcessor.TickType.SYSTEM.getIndex()] = cpuTicks[2];
            final int offset = (cpuTicks.length > 5) ? 1 : 0;
            ticks[CentralProcessor.TickType.IRQ.getIndex()] = cpuTicks[3 + offset];
            ticks[CentralProcessor.TickType.IDLE.getIndex()] = cpuTicks[4 + offset];
        }
        return ticks;
    }
    
    @Override
    protected long[][] queryProcessorCpuLoadTicks() {
        final long[][] ticks = new long[this.getLogicalProcessorCount()][CentralProcessor.TickType.values().length];
        final int[] mib = { 1, 71, 0 };
        for (int cpu = 0; cpu < this.getLogicalProcessorCount(); ++cpu) {
            mib[2] = cpu;
            final Memory m = OpenBsdSysctlUtil.sysctl(mib);
            final long[] cpuTicks = cpTimeToTicks(m, true);
            if (cpuTicks.length >= 5) {
                ticks[cpu][CentralProcessor.TickType.USER.getIndex()] = cpuTicks[0];
                ticks[cpu][CentralProcessor.TickType.NICE.getIndex()] = cpuTicks[1];
                ticks[cpu][CentralProcessor.TickType.SYSTEM.getIndex()] = cpuTicks[2];
                final int offset = (cpuTicks.length > 5) ? 1 : 0;
                ticks[cpu][CentralProcessor.TickType.IRQ.getIndex()] = cpuTicks[3 + offset];
                ticks[cpu][CentralProcessor.TickType.IDLE.getIndex()] = cpuTicks[4 + offset];
            }
        }
        return ticks;
    }
    
    private static long[] cpTimeToTicks(final Memory m, final boolean force64bit) {
        final long longBytes = force64bit ? 8L : Native.LONG_SIZE;
        final int arraySize = (m == null) ? 0 : ((int)(m.size() / longBytes));
        if (force64bit && m != null) {
            return m.getLongArray(0L, arraySize);
        }
        final long[] ticks = new long[arraySize];
        for (int i = 0; i < arraySize; ++i) {
            ticks[i] = m.getNativeLong(i * longBytes).longValue();
        }
        return ticks;
    }
    
    @Override
    public double[] getSystemLoadAverage(final int nelem) {
        if (nelem < 1 || nelem > 3) {
            throw new IllegalArgumentException("Must include from one to three elements.");
        }
        final double[] average = new double[nelem];
        final int retval = OpenBsdLibc.INSTANCE.getloadavg(average, nelem);
        if (retval < nelem) {
            Arrays.fill(average, -1.0);
        }
        return average;
    }
    
    static {
        DMESG_CPU = Pattern.compile("cpu(\\d+): smt (\\d+), core (\\d+), package (\\d+)");
    }
}
