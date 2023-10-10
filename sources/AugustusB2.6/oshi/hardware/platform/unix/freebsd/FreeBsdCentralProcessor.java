// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.freebsd;

import org.slf4j.LoggerFactory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.LibCAPI;
import com.sun.jna.Memory;
import com.sun.jna.Structure;
import oshi.jna.platform.unix.FreeBsdLibc;
import java.util.ArrayList;
import java.util.Map;
import oshi.util.ParseUtil;
import java.util.HashMap;
import oshi.util.tuples.Pair;
import java.util.regex.Matcher;
import java.util.Iterator;
import java.util.List;
import oshi.util.ExecutingCommand;
import oshi.util.FileUtil;
import oshi.util.platform.unix.freebsd.BsdSysctlUtil;
import oshi.hardware.CentralProcessor;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractCentralProcessor;

@ThreadSafe
final class FreeBsdCentralProcessor extends AbstractCentralProcessor
{
    private static final Logger LOG;
    private static final Pattern CPUMASK;
    
    @Override
    protected CentralProcessor.ProcessorIdentifier queryProcessorId() {
        final Pattern identifierPattern = Pattern.compile("Origin=\"([^\"]*)\".*Id=(\\S+).*Family=(\\S+).*Model=(\\S+).*Stepping=(\\S+).*");
        final Pattern featuresPattern = Pattern.compile("Features=(\\S+)<.*");
        String cpuVendor = "";
        String cpuName = BsdSysctlUtil.sysctl("hw.model", "");
        String cpuFamily = "";
        String cpuModel = "";
        String cpuStepping = "";
        final long cpuFreq = BsdSysctlUtil.sysctl("hw.clockrate", 0L) * 1000000L;
        long processorIdBits = 0L;
        final List<String> cpuInfo = FileUtil.readFile("/var/run/dmesg.boot");
        for (String line : cpuInfo) {
            line = line.trim();
            if (line.startsWith("CPU:") && cpuName.isEmpty()) {
                cpuName = line.replace("CPU:", "").trim();
            }
            else if (line.startsWith("Origin=")) {
                final Matcher m = identifierPattern.matcher(line);
                if (!m.matches()) {
                    continue;
                }
                cpuVendor = m.group(1);
                processorIdBits |= Long.decode(m.group(2));
                cpuFamily = Integer.decode(m.group(3)).toString();
                cpuModel = Integer.decode(m.group(4)).toString();
                cpuStepping = Integer.decode(m.group(5)).toString();
            }
            else {
                if (!line.startsWith("Features=")) {
                    continue;
                }
                final Matcher m = featuresPattern.matcher(line);
                if (m.matches()) {
                    processorIdBits |= (long)Long.decode(m.group(1)) << 32;
                    break;
                }
                break;
            }
        }
        final boolean cpu64bit = ExecutingCommand.getFirstAnswer("uname -m").trim().contains("64");
        final String processorID = getProcessorIDfromDmiDecode(processorIdBits);
        return new CentralProcessor.ProcessorIdentifier(cpuVendor, cpuName, cpuFamily, cpuModel, cpuStepping, processorID, cpu64bit, cpuFreq);
    }
    
    @Override
    protected Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>> initProcessorCounts() {
        final List<CentralProcessor.LogicalProcessor> logProcs = parseTopology();
        if (logProcs.isEmpty()) {
            logProcs.add(new CentralProcessor.LogicalProcessor(0, 0, 0));
        }
        final Map<Integer, String> dmesg = new HashMap<Integer, String>();
        final Pattern normal = Pattern.compile("cpu(\\\\d+): (.+) on .*");
        final Pattern hybrid = Pattern.compile("CPU\\\\s*(\\\\d+): (.+) affinity:.*");
        for (final String s : FileUtil.readFile("/var/run/dmesg.boot")) {
            final Matcher h = hybrid.matcher(s);
            if (h.matches()) {
                final int coreId = ParseUtil.parseIntOrDefault(h.group(1), 0);
                dmesg.put(coreId, h.group(2).trim());
            }
            else {
                final Matcher n = normal.matcher(s);
                if (!n.matches()) {
                    continue;
                }
                final int coreId2 = ParseUtil.parseIntOrDefault(n.group(1), 0);
                dmesg.putIfAbsent(coreId2, n.group(2).trim());
            }
        }
        if (dmesg.isEmpty()) {
            return new Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>>(logProcs, null);
        }
        return new Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>>(logProcs, this.createProcListFromDmesg(logProcs, dmesg));
    }
    
    private static List<CentralProcessor.LogicalProcessor> parseTopology() {
        final String[] topology = BsdSysctlUtil.sysctl("kern.sched.topology_spec", "").split("\\n|\\r");
        long group1 = 1L;
        final List<Long> group2 = new ArrayList<Long>();
        final List<Long> group3 = new ArrayList<Long>();
        int groupLevel = 0;
        for (final String topo : topology) {
            if (topo.contains("<group level=")) {
                ++groupLevel;
            }
            else if (topo.contains("</group>")) {
                --groupLevel;
            }
            else if (topo.contains("<cpu")) {
                final Matcher m = FreeBsdCentralProcessor.CPUMASK.matcher(topo);
                if (m.matches()) {
                    switch (groupLevel) {
                        case 1: {
                            group1 = Long.parseLong(m.group(1), 16);
                            break;
                        }
                        case 2: {
                            group2.add(Long.parseLong(m.group(1), 16));
                            break;
                        }
                        case 3: {
                            group3.add(Long.parseLong(m.group(1), 16));
                            break;
                        }
                    }
                }
            }
        }
        return matchBitmasks(group1, group2, group3);
    }
    
    private static List<CentralProcessor.LogicalProcessor> matchBitmasks(final long group1, final List<Long> group2, final List<Long> group3) {
        final List<CentralProcessor.LogicalProcessor> logProcs = new ArrayList<CentralProcessor.LogicalProcessor>();
        final int lowBit = Long.numberOfTrailingZeros(group1);
        for (int hiBit = 63 - Long.numberOfLeadingZeros(group1), i = lowBit; i <= hiBit; ++i) {
            if ((group1 & 1L << i) > 0L) {
                final int numaNode = 0;
                final CentralProcessor.LogicalProcessor logProc = new CentralProcessor.LogicalProcessor(i, getMatchingBitmask(group3, i), getMatchingBitmask(group2, i), numaNode);
                logProcs.add(logProc);
            }
        }
        return logProcs;
    }
    
    private static int getMatchingBitmask(final List<Long> bitmasks, final int lp) {
        for (int j = 0; j < bitmasks.size(); ++j) {
            if (((long)bitmasks.get(j) & 1L << lp) != 0x0L) {
                return j;
            }
        }
        return 0;
    }
    
    public long[] querySystemCpuLoadTicks() {
        final long[] ticks = new long[CentralProcessor.TickType.values().length];
        final FreeBsdLibc.CpTime cpTime = new FreeBsdLibc.CpTime();
        BsdSysctlUtil.sysctl("kern.cp_time", cpTime);
        ticks[CentralProcessor.TickType.USER.getIndex()] = cpTime.cpu_ticks[0];
        ticks[CentralProcessor.TickType.NICE.getIndex()] = cpTime.cpu_ticks[1];
        ticks[CentralProcessor.TickType.SYSTEM.getIndex()] = cpTime.cpu_ticks[2];
        ticks[CentralProcessor.TickType.IRQ.getIndex()] = cpTime.cpu_ticks[3];
        ticks[CentralProcessor.TickType.IDLE.getIndex()] = cpTime.cpu_ticks[4];
        return ticks;
    }
    
    public long[] queryCurrentFreq() {
        final long[] freq = { BsdSysctlUtil.sysctl("dev.cpu.0.freq", -1L) };
        if (freq[0] > 0L) {
            final long[] array = freq;
            final int n = 0;
            array[n] *= 1000000L;
        }
        else {
            freq[0] = BsdSysctlUtil.sysctl("machdep.tsc_freq", -1L);
        }
        return freq;
    }
    
    public long queryMaxFreq() {
        long max = -1L;
        final String freqLevels = BsdSysctlUtil.sysctl("dev.cpu.0.freq_levels", "");
        for (final String s : ParseUtil.whitespaces.split(freqLevels)) {
            final long freq = ParseUtil.parseLongOrDefault(s.split("/")[0], -1L);
            if (max < freq) {
                max = freq;
            }
        }
        if (max > 0L) {
            max *= 1000000L;
        }
        else {
            max = BsdSysctlUtil.sysctl("machdep.tsc_freq", -1L);
        }
        return max;
    }
    
    @Override
    public double[] getSystemLoadAverage(final int nelem) {
        if (nelem < 1 || nelem > 3) {
            throw new IllegalArgumentException("Must include from one to three elements.");
        }
        final double[] average = new double[nelem];
        final int retval = FreeBsdLibc.INSTANCE.getloadavg(average, nelem);
        if (retval < nelem) {
            for (int i = Math.max(retval, 0); i < average.length; ++i) {
                average[i] = -1.0;
            }
        }
        return average;
    }
    
    public long[][] queryProcessorCpuLoadTicks() {
        final long[][] ticks = new long[this.getLogicalProcessorCount()][CentralProcessor.TickType.values().length];
        final long size = new FreeBsdLibc.CpTime().size();
        final long arraySize = size * this.getLogicalProcessorCount();
        final Pointer p = new Memory(arraySize);
        final String name = "kern.cp_times";
        if (0 != FreeBsdLibc.INSTANCE.sysctlbyname(name, p, new LibCAPI.size_t.ByReference(new LibCAPI.size_t(arraySize)), null, LibCAPI.size_t.ZERO)) {
            FreeBsdCentralProcessor.LOG.error("Failed sysctl call: {}, Error code: {}", name, Native.getLastError());
            return ticks;
        }
        for (int cpu = 0; cpu < this.getLogicalProcessorCount(); ++cpu) {
            ticks[cpu][CentralProcessor.TickType.USER.getIndex()] = p.getLong(size * cpu + 0 * FreeBsdLibc.UINT64_SIZE);
            ticks[cpu][CentralProcessor.TickType.NICE.getIndex()] = p.getLong(size * cpu + 1 * FreeBsdLibc.UINT64_SIZE);
            ticks[cpu][CentralProcessor.TickType.SYSTEM.getIndex()] = p.getLong(size * cpu + 2 * FreeBsdLibc.UINT64_SIZE);
            ticks[cpu][CentralProcessor.TickType.IRQ.getIndex()] = p.getLong(size * cpu + 3 * FreeBsdLibc.UINT64_SIZE);
            ticks[cpu][CentralProcessor.TickType.IDLE.getIndex()] = p.getLong(size * cpu + 4 * FreeBsdLibc.UINT64_SIZE);
        }
        return ticks;
    }
    
    private static String getProcessorIDfromDmiDecode(final long processorID) {
        boolean procInfo = false;
        String marker = "Processor Information";
        for (final String checkLine : ExecutingCommand.runNative("dmidecode -t system")) {
            if (!procInfo && checkLine.contains(marker)) {
                marker = "ID:";
                procInfo = true;
            }
            else {
                if (procInfo && checkLine.contains(marker)) {
                    return checkLine.split(marker)[1].trim();
                }
                continue;
            }
        }
        return String.format("%016X", processorID);
    }
    
    public long queryContextSwitches() {
        final String name = "vm.stats.sys.v_swtch";
        final LibCAPI.size_t.ByReference size = new LibCAPI.size_t.ByReference(new LibCAPI.size_t((long)FreeBsdLibc.INT_SIZE));
        final Pointer p = new Memory(size.longValue());
        if (0 != FreeBsdLibc.INSTANCE.sysctlbyname(name, p, size, null, LibCAPI.size_t.ZERO)) {
            return 0L;
        }
        return ParseUtil.unsignedIntToLong(p.getInt(0L));
    }
    
    public long queryInterrupts() {
        final String name = "vm.stats.sys.v_intr";
        final LibCAPI.size_t.ByReference size = new LibCAPI.size_t.ByReference(new LibCAPI.size_t((long)FreeBsdLibc.INT_SIZE));
        final Pointer p = new Memory(size.longValue());
        if (0 != FreeBsdLibc.INSTANCE.sysctlbyname(name, p, size, null, LibCAPI.size_t.ZERO)) {
            return 0L;
        }
        return ParseUtil.unsignedIntToLong(p.getInt(0L));
    }
    
    static {
        LOG = LoggerFactory.getLogger(FreeBsdCentralProcessor.class);
        CPUMASK = Pattern.compile(".*<cpu\\s.*mask=\"(?:0x)?(\\p{XDigit}+)\".*>.*</cpu>.*");
    }
}
