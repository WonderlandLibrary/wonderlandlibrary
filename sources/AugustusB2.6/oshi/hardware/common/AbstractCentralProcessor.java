// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.common;

import org.slf4j.LoggerFactory;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Map;
import oshi.util.ParseUtil;
import java.util.Arrays;
import java.util.Iterator;
import oshi.util.tuples.Pair;
import java.util.HashSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.Collections;
import oshi.util.Memoizer;
import java.util.List;
import java.util.function.Supplier;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.CentralProcessor;

@ThreadSafe
public abstract class AbstractCentralProcessor implements CentralProcessor
{
    private static final Logger LOG;
    private final Supplier<ProcessorIdentifier> cpuid;
    private final Supplier<Long> maxFreq;
    private final Supplier<long[]> currentFreq;
    private final Supplier<Long> contextSwitches;
    private final Supplier<Long> interrupts;
    private final Supplier<long[]> systemCpuLoadTicks;
    private final Supplier<long[][]> processorCpuLoadTicks;
    private final int physicalPackageCount;
    private final int physicalProcessorCount;
    private final int logicalProcessorCount;
    private final List<LogicalProcessor> logicalProcessors;
    private final List<PhysicalProcessor> physicalProcessors;
    
    protected AbstractCentralProcessor() {
        this.cpuid = Memoizer.memoize(this::queryProcessorId);
        this.maxFreq = Memoizer.memoize(this::queryMaxFreq, Memoizer.defaultExpiration());
        this.currentFreq = Memoizer.memoize(this::queryCurrentFreq, Memoizer.defaultExpiration());
        this.contextSwitches = Memoizer.memoize(this::queryContextSwitches, Memoizer.defaultExpiration());
        this.interrupts = Memoizer.memoize(this::queryInterrupts, Memoizer.defaultExpiration());
        this.systemCpuLoadTicks = Memoizer.memoize(this::querySystemCpuLoadTicks, Memoizer.defaultExpiration());
        this.processorCpuLoadTicks = Memoizer.memoize(this::queryProcessorCpuLoadTicks, Memoizer.defaultExpiration());
        final Pair<List<LogicalProcessor>, List<PhysicalProcessor>> processorLists = this.initProcessorCounts();
        this.logicalProcessors = Collections.unmodifiableList((List<? extends LogicalProcessor>)processorLists.getA());
        if (processorLists.getB() == null) {
            final Set<Integer> pkgCoreKeys = this.logicalProcessors.stream().map(p -> (p.getPhysicalPackageNumber() << 16) + p.getPhysicalProcessorNumber()).collect((Collector<? super Object, ?, Set<Integer>>)Collectors.toSet());
            final List<PhysicalProcessor> physProcs = pkgCoreKeys.stream().sorted().map(k -> new PhysicalProcessor(k >> 16, k & 0xFFFF)).collect((Collector<? super Object, ?, List<PhysicalProcessor>>)Collectors.toList());
            this.physicalProcessors = Collections.unmodifiableList((List<? extends PhysicalProcessor>)physProcs);
        }
        else {
            this.physicalProcessors = Collections.unmodifiableList((List<? extends PhysicalProcessor>)processorLists.getB());
        }
        final Set<Integer> physPkgs = new HashSet<Integer>();
        for (final LogicalProcessor logProc : this.logicalProcessors) {
            final int pkg = logProc.getPhysicalPackageNumber();
            physPkgs.add(pkg);
        }
        this.logicalProcessorCount = this.logicalProcessors.size();
        this.physicalProcessorCount = this.physicalProcessors.size();
        this.physicalPackageCount = physPkgs.size();
    }
    
    protected abstract Pair<List<LogicalProcessor>, List<PhysicalProcessor>> initProcessorCounts();
    
    protected abstract ProcessorIdentifier queryProcessorId();
    
    @Override
    public ProcessorIdentifier getProcessorIdentifier() {
        return this.cpuid.get();
    }
    
    @Override
    public long getMaxFreq() {
        return this.maxFreq.get();
    }
    
    protected abstract long queryMaxFreq();
    
    @Override
    public long[] getCurrentFreq() {
        final long[] freq = this.currentFreq.get();
        if (freq.length == this.getLogicalProcessorCount()) {
            return freq;
        }
        final long[] freqs = new long[this.getLogicalProcessorCount()];
        Arrays.fill(freqs, freq[0]);
        return freqs;
    }
    
    protected abstract long[] queryCurrentFreq();
    
    @Override
    public long getContextSwitches() {
        return this.contextSwitches.get();
    }
    
    protected abstract long queryContextSwitches();
    
    @Override
    public long getInterrupts() {
        return this.interrupts.get();
    }
    
    protected abstract long queryInterrupts();
    
    @Override
    public List<LogicalProcessor> getLogicalProcessors() {
        return this.logicalProcessors;
    }
    
    @Override
    public List<PhysicalProcessor> getPhysicalProcessors() {
        return this.physicalProcessors;
    }
    
    @Override
    public long[] getSystemCpuLoadTicks() {
        return this.systemCpuLoadTicks.get();
    }
    
    protected abstract long[] querySystemCpuLoadTicks();
    
    @Override
    public long[][] getProcessorCpuLoadTicks() {
        return this.processorCpuLoadTicks.get();
    }
    
    protected abstract long[][] queryProcessorCpuLoadTicks();
    
    @Override
    public double getSystemCpuLoadBetweenTicks(final long[] oldTicks) {
        if (oldTicks.length != TickType.values().length) {
            throw new IllegalArgumentException("Tick array " + oldTicks.length + " should have " + TickType.values().length + " elements");
        }
        final long[] ticks = this.getSystemCpuLoadTicks();
        long total = 0L;
        for (int i = 0; i < ticks.length; ++i) {
            total += ticks[i] - oldTicks[i];
        }
        final long idle = ticks[TickType.IDLE.getIndex()] + ticks[TickType.IOWAIT.getIndex()] - oldTicks[TickType.IDLE.getIndex()] - oldTicks[TickType.IOWAIT.getIndex()];
        AbstractCentralProcessor.LOG.trace("Total ticks: {}  Idle ticks: {}", (Object)total, idle);
        return (total > 0L && idle >= 0L) ? ((total - idle) / (double)total) : 0.0;
    }
    
    @Override
    public double[] getProcessorCpuLoadBetweenTicks(final long[][] oldTicks) {
        if (oldTicks.length != this.logicalProcessorCount || oldTicks[0].length != TickType.values().length) {
            throw new IllegalArgumentException("Tick array " + oldTicks.length + " should have " + this.logicalProcessorCount + " arrays, each of which has " + TickType.values().length + " elements");
        }
        final long[][] ticks = this.getProcessorCpuLoadTicks();
        final double[] load = new double[this.logicalProcessorCount];
        for (int cpu = 0; cpu < this.logicalProcessorCount; ++cpu) {
            long total = 0L;
            for (int i = 0; i < ticks[cpu].length; ++i) {
                total += ticks[cpu][i] - oldTicks[cpu][i];
            }
            final long idle = ticks[cpu][TickType.IDLE.getIndex()] + ticks[cpu][TickType.IOWAIT.getIndex()] - oldTicks[cpu][TickType.IDLE.getIndex()] - oldTicks[cpu][TickType.IOWAIT.getIndex()];
            AbstractCentralProcessor.LOG.trace("CPU: {}  Total ticks: {}  Idle ticks: {}", cpu, total, idle);
            load[cpu] = ((total > 0L && idle >= 0L) ? ((total - idle) / (double)total) : 0.0);
        }
        return load;
    }
    
    @Override
    public int getLogicalProcessorCount() {
        return this.logicalProcessorCount;
    }
    
    @Override
    public int getPhysicalProcessorCount() {
        return this.physicalProcessorCount;
    }
    
    @Override
    public int getPhysicalPackageCount() {
        return this.physicalPackageCount;
    }
    
    protected static String createProcessorID(final String stepping, final String model, final String family, final String[] flags) {
        long processorIdBytes = 0L;
        final long steppingL = ParseUtil.parseLongOrDefault(stepping, 0L);
        final long modelL = ParseUtil.parseLongOrDefault(model, 0L);
        final long familyL = ParseUtil.parseLongOrDefault(family, 0L);
        processorIdBytes |= (steppingL & 0xFL);
        processorIdBytes |= (modelL & 0xFL) << 4;
        processorIdBytes |= (modelL & 0xF0L) << 16;
        processorIdBytes |= (familyL & 0xFL) << 8;
        processorIdBytes |= (familyL & 0xF0L) << 20;
        for (final String s : flags) {
            final String flag = s;
            switch (s) {
                case "fpu": {
                    processorIdBytes |= 0x100000000L;
                    break;
                }
                case "vme": {
                    processorIdBytes |= 0x200000000L;
                    break;
                }
                case "de": {
                    processorIdBytes |= 0x400000000L;
                    break;
                }
                case "pse": {
                    processorIdBytes |= 0x800000000L;
                    break;
                }
                case "tsc": {
                    processorIdBytes |= 0x1000000000L;
                    break;
                }
                case "msr": {
                    processorIdBytes |= 0x2000000000L;
                    break;
                }
                case "pae": {
                    processorIdBytes |= 0x4000000000L;
                    break;
                }
                case "mce": {
                    processorIdBytes |= 0x8000000000L;
                    break;
                }
                case "cx8": {
                    processorIdBytes |= 0x10000000000L;
                    break;
                }
                case "apic": {
                    processorIdBytes |= 0x20000000000L;
                    break;
                }
                case "sep": {
                    processorIdBytes |= 0x80000000000L;
                    break;
                }
                case "mtrr": {
                    processorIdBytes |= 0x100000000000L;
                    break;
                }
                case "pge": {
                    processorIdBytes |= 0x200000000000L;
                    break;
                }
                case "mca": {
                    processorIdBytes |= 0x400000000000L;
                    break;
                }
                case "cmov": {
                    processorIdBytes |= 0x800000000000L;
                    break;
                }
                case "pat": {
                    processorIdBytes |= 0x1000000000000L;
                    break;
                }
                case "pse-36": {
                    processorIdBytes |= 0x2000000000000L;
                    break;
                }
                case "psn": {
                    processorIdBytes |= 0x4000000000000L;
                    break;
                }
                case "clfsh": {
                    processorIdBytes |= 0x8000000000000L;
                    break;
                }
                case "ds": {
                    processorIdBytes |= 0x20000000000000L;
                    break;
                }
                case "acpi": {
                    processorIdBytes |= 0x40000000000000L;
                    break;
                }
                case "mmx": {
                    processorIdBytes |= 0x80000000000000L;
                    break;
                }
                case "fxsr": {
                    processorIdBytes |= 0x100000000000000L;
                    break;
                }
                case "sse": {
                    processorIdBytes |= 0x200000000000000L;
                    break;
                }
                case "sse2": {
                    processorIdBytes |= 0x400000000000000L;
                    break;
                }
                case "ss": {
                    processorIdBytes |= 0x800000000000000L;
                    break;
                }
                case "htt": {
                    processorIdBytes |= 0x1000000000000000L;
                    break;
                }
                case "tm": {
                    processorIdBytes |= 0x2000000000000000L;
                    break;
                }
                case "ia64": {
                    processorIdBytes |= 0x4000000000000000L;
                    break;
                }
                case "pbe": {
                    processorIdBytes |= Long.MIN_VALUE;
                    break;
                }
            }
        }
        return String.format("%016X", processorIdBytes);
    }
    
    protected List<PhysicalProcessor> createProcListFromDmesg(final List<LogicalProcessor> logProcs, final Map<Integer, String> dmesg) {
        final boolean isHybrid = dmesg.values().stream().distinct().count() > 1L;
        final List<PhysicalProcessor> physProcs = new ArrayList<PhysicalProcessor>();
        final Set<Integer> pkgCoreKeys = new HashSet<Integer>();
        for (final LogicalProcessor logProc : logProcs) {
            final int pkgId = logProc.getPhysicalPackageNumber();
            final int coreId = logProc.getPhysicalProcessorNumber();
            final int pkgCoreKey = (pkgId << 16) + coreId;
            if (!pkgCoreKeys.contains(pkgCoreKey)) {
                pkgCoreKeys.add(pkgCoreKey);
                final String idStr = dmesg.getOrDefault(logProc.getProcessorNumber(), "");
                int efficiency = 0;
                if (isHybrid && idStr.startsWith("ARM Cortex")) {
                    efficiency = ((ParseUtil.getFirstIntValue(idStr) >= 70) ? 1 : 0);
                }
                physProcs.add(new PhysicalProcessor(pkgId, coreId, efficiency, idStr));
            }
        }
        physProcs.sort(Comparator.comparingInt(PhysicalProcessor::getPhysicalPackageNumber).thenComparingInt(PhysicalProcessor::getPhysicalProcessorNumber));
        return physProcs;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(this.getProcessorIdentifier().getName());
        sb.append("\n ").append(this.getPhysicalPackageCount()).append(" physical CPU package(s)");
        sb.append("\n ").append(this.getPhysicalProcessorCount()).append(" physical CPU core(s)");
        sb.append("\n ").append(this.getLogicalProcessorCount()).append(" logical CPU(s)");
        sb.append('\n').append("Identifier: ").append(this.getProcessorIdentifier().getIdentifier());
        sb.append('\n').append("ProcessorID: ").append(this.getProcessorIdentifier().getProcessorID());
        sb.append('\n').append("Microarchitecture: ").append(this.getProcessorIdentifier().getMicroarchitecture());
        return sb.toString();
    }
    
    static {
        LOG = LoggerFactory.getLogger(AbstractCentralProcessor.class);
    }
}
