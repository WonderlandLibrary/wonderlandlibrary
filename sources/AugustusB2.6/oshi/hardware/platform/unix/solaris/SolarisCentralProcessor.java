// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.solaris;

import oshi.jna.platform.unix.SolarisLibc;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.HashMap;
import oshi.util.ParseUtil;
import java.util.ArrayList;
import java.util.List;
import oshi.util.tuples.Pair;
import com.sun.jna.platform.unix.solaris.LibKstat;
import oshi.util.platform.unix.solaris.KstatUtil;
import oshi.software.os.unix.solaris.SolarisOperatingSystem;
import oshi.util.ExecutingCommand;
import oshi.hardware.CentralProcessor;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractCentralProcessor;

@ThreadSafe
final class SolarisCentralProcessor extends AbstractCentralProcessor
{
    private static final String KSTAT_SYSTEM_CPU = "kstat:/system/cpu/";
    private static final String INFO = "/info";
    private static final String SYS = "/sys";
    private static final String KSTAT_PM_CPU = "kstat:/pm/cpu/";
    private static final String PSTATE = "/pstate";
    private static final String CPU_INFO = "cpu_info";
    
    @Override
    protected CentralProcessor.ProcessorIdentifier queryProcessorId() {
        final boolean cpu64bit = "64".equals(ExecutingCommand.getFirstAnswer("isainfo -b").trim());
        if (SolarisOperatingSystem.IS_11_4_OR_HIGHER) {
            return queryProcessorId2(cpu64bit);
        }
        String cpuVendor = "";
        String cpuName = "";
        String cpuFamily = "";
        String cpuModel = "";
        String cpuStepping = "";
        long cpuFreq = 0L;
        final KstatUtil.KstatChain kc = KstatUtil.openChain();
        try {
            final LibKstat.Kstat ksp = KstatUtil.KstatChain.lookup("cpu_info", -1, null);
            if (ksp != null && KstatUtil.KstatChain.read(ksp)) {
                cpuVendor = KstatUtil.dataLookupString(ksp, "vendor_id");
                cpuName = KstatUtil.dataLookupString(ksp, "brand");
                cpuFamily = KstatUtil.dataLookupString(ksp, "family");
                cpuModel = KstatUtil.dataLookupString(ksp, "model");
                cpuStepping = KstatUtil.dataLookupString(ksp, "stepping");
                cpuFreq = KstatUtil.dataLookupLong(ksp, "clock_MHz") * 1000000L;
            }
            if (kc != null) {
                kc.close();
            }
        }
        catch (Throwable t) {
            if (kc != null) {
                try {
                    kc.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
        final String processorID = getProcessorID(cpuStepping, cpuModel, cpuFamily);
        return new CentralProcessor.ProcessorIdentifier(cpuVendor, cpuName, cpuFamily, cpuModel, cpuStepping, processorID, cpu64bit, cpuFreq);
    }
    
    private static CentralProcessor.ProcessorIdentifier queryProcessorId2(final boolean cpu64bit) {
        final Object[] results = KstatUtil.queryKstat2("kstat:/system/cpu/0/info", "vendor_id", "brand", "family", "model", "stepping", "clock_MHz");
        final String cpuVendor = (String)((results[0] == null) ? "" : results[0]);
        final String cpuName = (String)((results[1] == null) ? "" : results[1]);
        final String cpuFamily = (results[2] == null) ? "" : results[2].toString();
        final String cpuModel = (results[3] == null) ? "" : results[3].toString();
        final String cpuStepping = (results[4] == null) ? "" : results[4].toString();
        final long cpuFreq = (long)((results[5] == null) ? 0L : results[5]);
        final String processorID = getProcessorID(cpuStepping, cpuModel, cpuFamily);
        return new CentralProcessor.ProcessorIdentifier(cpuVendor, cpuName, cpuFamily, cpuModel, cpuStepping, processorID, cpu64bit, cpuFreq);
    }
    
    @Override
    protected Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>> initProcessorCounts() {
        final Map<Integer, Integer> numaNodeMap = mapNumaNodes();
        if (SolarisOperatingSystem.IS_11_4_OR_HIGHER) {
            return new Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>>(initProcessorCounts2(numaNodeMap), null);
        }
        final List<CentralProcessor.LogicalProcessor> logProcs = new ArrayList<CentralProcessor.LogicalProcessor>();
        final KstatUtil.KstatChain kc = KstatUtil.openChain();
        try {
            final List<LibKstat.Kstat> kstats = KstatUtil.KstatChain.lookupAll("cpu_info", -1, null);
            for (final LibKstat.Kstat ksp : kstats) {
                if (ksp != null && KstatUtil.KstatChain.read(ksp)) {
                    final int procId = logProcs.size();
                    final String chipId = KstatUtil.dataLookupString(ksp, "chip_id");
                    final String coreId = KstatUtil.dataLookupString(ksp, "core_id");
                    final CentralProcessor.LogicalProcessor logProc = new CentralProcessor.LogicalProcessor(procId, ParseUtil.parseIntOrDefault(coreId, 0), ParseUtil.parseIntOrDefault(chipId, 0), numaNodeMap.getOrDefault(procId, 0));
                    logProcs.add(logProc);
                }
            }
            if (kc != null) {
                kc.close();
            }
        }
        catch (Throwable t) {
            if (kc != null) {
                try {
                    kc.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
        if (logProcs.isEmpty()) {
            logProcs.add(new CentralProcessor.LogicalProcessor(0, 0, 0));
        }
        final Map<Integer, String> dmesg = new HashMap<Integer, String>();
        final Pattern p = Pattern.compile(".* cpu(\\\\d+): ((ARM|AMD|Intel).+)");
        for (final String s : ExecutingCommand.runNative("dmesg")) {
            final Matcher m = p.matcher(s);
            if (m.matches()) {
                final int coreId2 = ParseUtil.parseIntOrDefault(m.group(1), 0);
                dmesg.put(coreId2, m.group(2).trim());
            }
        }
        if (dmesg.isEmpty()) {
            return new Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>>(logProcs, null);
        }
        return new Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>>(logProcs, this.createProcListFromDmesg(logProcs, dmesg));
    }
    
    private static List<CentralProcessor.LogicalProcessor> initProcessorCounts2(final Map<Integer, Integer> numaNodeMap) {
        final List<CentralProcessor.LogicalProcessor> logProcs = new ArrayList<CentralProcessor.LogicalProcessor>();
        final List<Object[]> results = KstatUtil.queryKstat2List("kstat:/system/cpu/", "/info", "chip_id", "core_id");
        for (final Object[] result : results) {
            final int procId = logProcs.size();
            final long chipId = (long)((result[0] == null) ? 0L : result[0]);
            final long coreId = (long)((result[1] == null) ? 0L : result[1]);
            final CentralProcessor.LogicalProcessor logProc = new CentralProcessor.LogicalProcessor(procId, (int)coreId, (int)chipId, numaNodeMap.getOrDefault(procId, 0));
            logProcs.add(logProc);
        }
        if (logProcs.isEmpty()) {
            logProcs.add(new CentralProcessor.LogicalProcessor(0, 0, 0));
        }
        return logProcs;
    }
    
    private static Map<Integer, Integer> mapNumaNodes() {
        final Map<Integer, Integer> numaNodeMap = new HashMap<Integer, Integer>();
        int lgroup = 0;
        for (final String line : ExecutingCommand.runNative("lgrpinfo -c leaves")) {
            if (line.startsWith("lgroup")) {
                lgroup = ParseUtil.getFirstIntValue(line);
            }
            else {
                if (!line.contains("CPUs:") && !line.contains("CPU:")) {
                    continue;
                }
                for (final Integer cpu : ParseUtil.parseHyphenatedIntList(line.split(":")[1])) {
                    numaNodeMap.put(cpu, lgroup);
                }
            }
        }
        return numaNodeMap;
    }
    
    public long[] querySystemCpuLoadTicks() {
        final long[] ticks = new long[CentralProcessor.TickType.values().length];
        final long[][] procTicks = this.getProcessorCpuLoadTicks();
        for (int i = 0; i < ticks.length; ++i) {
            for (final long[] procTick : procTicks) {
                final long[] array2 = ticks;
                final int n = i;
                array2[n] += procTick[i];
            }
            final long[] array3 = ticks;
            final int n2 = i;
            array3[n2] /= procTicks.length;
        }
        return ticks;
    }
    
    public long[] queryCurrentFreq() {
        if (SolarisOperatingSystem.IS_11_4_OR_HIGHER) {
            return queryCurrentFreq2(this.getLogicalProcessorCount());
        }
        final long[] freqs = new long[this.getLogicalProcessorCount()];
        Arrays.fill(freqs, -1L);
        final KstatUtil.KstatChain kc = KstatUtil.openChain();
        try {
            for (int i = 0; i < freqs.length; ++i) {
                for (final LibKstat.Kstat ksp : KstatUtil.KstatChain.lookupAll("cpu_info", i, null)) {
                    if (KstatUtil.KstatChain.read(ksp)) {
                        freqs[i] = KstatUtil.dataLookupLong(ksp, "current_clock_Hz");
                    }
                }
            }
            if (kc != null) {
                kc.close();
            }
        }
        catch (Throwable t) {
            if (kc != null) {
                try {
                    kc.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
        return freqs;
    }
    
    private static long[] queryCurrentFreq2(final int processorCount) {
        final long[] freqs = new long[processorCount];
        Arrays.fill(freqs, -1L);
        final List<Object[]> results = KstatUtil.queryKstat2List("kstat:/system/cpu/", "/info", "current_clock_Hz");
        int cpu = -1;
        for (final Object[] result : results) {
            if (++cpu >= freqs.length) {
                break;
            }
            freqs[cpu] = (long)((result[0] == null) ? -1L : result[0]);
        }
        return freqs;
    }
    
    public long queryMaxFreq() {
        if (SolarisOperatingSystem.IS_11_4_OR_HIGHER) {
            return queryMaxFreq2();
        }
        long max = -1L;
        final KstatUtil.KstatChain kc = KstatUtil.openChain();
        try {
            for (final LibKstat.Kstat ksp : KstatUtil.KstatChain.lookupAll("cpu_info", 0, null)) {
                if (KstatUtil.KstatChain.read(ksp)) {
                    final String suppFreq = KstatUtil.dataLookupString(ksp, "supported_frequencies_Hz");
                    if (suppFreq.isEmpty()) {
                        continue;
                    }
                    for (final String s : suppFreq.split(":")) {
                        final long freq = ParseUtil.parseLongOrDefault(s, -1L);
                        if (max < freq) {
                            max = freq;
                        }
                    }
                }
            }
            if (kc != null) {
                kc.close();
            }
        }
        catch (Throwable t) {
            if (kc != null) {
                try {
                    kc.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
        return max;
    }
    
    private static long queryMaxFreq2() {
        long max = -1L;
        final List<Object[]> results = KstatUtil.queryKstat2List("kstat:/pm/cpu/", "/pstate", "supported_frequencies");
        for (final Object[] result : results) {
            for (final long freq : (result[0] == null) ? new long[0] : result[0]) {
                if (freq > max) {
                    max = freq;
                }
            }
        }
        return max;
    }
    
    @Override
    public double[] getSystemLoadAverage(final int nelem) {
        if (nelem < 1 || nelem > 3) {
            throw new IllegalArgumentException("Must include from one to three elements.");
        }
        final double[] average = new double[nelem];
        final int retval = SolarisLibc.INSTANCE.getloadavg(average, nelem);
        if (retval < nelem) {
            for (int i = Math.max(retval, 0); i < average.length; ++i) {
                average[i] = -1.0;
            }
        }
        return average;
    }
    
    public long[][] queryProcessorCpuLoadTicks() {
        if (SolarisOperatingSystem.IS_11_4_OR_HIGHER) {
            return queryProcessorCpuLoadTicks2(this.getLogicalProcessorCount());
        }
        final long[][] ticks = new long[this.getLogicalProcessorCount()][CentralProcessor.TickType.values().length];
        int cpu = -1;
        final KstatUtil.KstatChain kc = KstatUtil.openChain();
        try {
            for (final LibKstat.Kstat ksp : KstatUtil.KstatChain.lookupAll("cpu", -1, "sys")) {
                if (++cpu >= ticks.length) {
                    break;
                }
                if (!KstatUtil.KstatChain.read(ksp)) {
                    continue;
                }
                ticks[cpu][CentralProcessor.TickType.IDLE.getIndex()] = KstatUtil.dataLookupLong(ksp, "cpu_ticks_idle");
                ticks[cpu][CentralProcessor.TickType.SYSTEM.getIndex()] = KstatUtil.dataLookupLong(ksp, "cpu_ticks_kernel");
                ticks[cpu][CentralProcessor.TickType.USER.getIndex()] = KstatUtil.dataLookupLong(ksp, "cpu_ticks_user");
            }
            if (kc != null) {
                kc.close();
            }
        }
        catch (Throwable t) {
            if (kc != null) {
                try {
                    kc.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
        return ticks;
    }
    
    private static long[][] queryProcessorCpuLoadTicks2(final int processorCount) {
        final long[][] ticks = new long[processorCount][CentralProcessor.TickType.values().length];
        final List<Object[]> results = KstatUtil.queryKstat2List("kstat:/system/cpu/", "/sys", "cpu_ticks_idle", "cpu_ticks_kernel", "cpu_ticks_user");
        int cpu = -1;
        for (final Object[] result : results) {
            if (++cpu >= ticks.length) {
                break;
            }
            ticks[cpu][CentralProcessor.TickType.IDLE.getIndex()] = (long)((result[0] == null) ? 0L : result[0]);
            ticks[cpu][CentralProcessor.TickType.SYSTEM.getIndex()] = (long)((result[1] == null) ? 0L : result[1]);
            ticks[cpu][CentralProcessor.TickType.USER.getIndex()] = (long)((result[2] == null) ? 0L : result[2]);
        }
        return ticks;
    }
    
    private static String getProcessorID(final String stepping, final String model, final String family) {
        final List<String> isainfo = ExecutingCommand.runNative("isainfo -v");
        final StringBuilder flags = new StringBuilder();
        for (final String line : isainfo) {
            if (line.startsWith("32-bit")) {
                break;
            }
            if (line.startsWith("64-bit")) {
                continue;
            }
            flags.append(' ').append(line.trim());
        }
        return AbstractCentralProcessor.createProcessorID(stepping, model, family, ParseUtil.whitespaces.split(flags.toString().toLowerCase()));
    }
    
    public long queryContextSwitches() {
        if (SolarisOperatingSystem.IS_11_4_OR_HIGHER) {
            return queryContextSwitches2();
        }
        long swtch = 0L;
        final List<String> kstat = ExecutingCommand.runNative("kstat -p cpu_stat:::/pswitch\\\\|inv_swtch/");
        for (final String s : kstat) {
            swtch += ParseUtil.parseLastLong(s, 0L);
        }
        return swtch;
    }
    
    private static long queryContextSwitches2() {
        long swtch = 0L;
        final List<Object[]> results = KstatUtil.queryKstat2List("kstat:/system/cpu/", "/sys", "pswitch", "inv_swtch");
        for (final Object[] result : results) {
            swtch += (long)((result[0] == null) ? 0L : result[0]);
            swtch += (long)((result[1] == null) ? 0L : result[1]);
        }
        return swtch;
    }
    
    public long queryInterrupts() {
        if (SolarisOperatingSystem.IS_11_4_OR_HIGHER) {
            return queryInterrupts2();
        }
        long intr = 0L;
        final List<String> kstat = ExecutingCommand.runNative("kstat -p cpu_stat:::/intr/");
        for (final String s : kstat) {
            intr += ParseUtil.parseLastLong(s, 0L);
        }
        return intr;
    }
    
    private static long queryInterrupts2() {
        long intr = 0L;
        final List<Object[]> results = KstatUtil.queryKstat2List("kstat:/system/cpu/", "/sys", "intr");
        for (final Object[] result : results) {
            intr += (long)((result[0] == null) ? 0L : result[0]);
        }
        return intr;
    }
}
