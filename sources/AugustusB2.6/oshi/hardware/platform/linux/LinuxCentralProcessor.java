// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.linux;

import oshi.jna.platform.linux.LinuxLibc;
import java.io.File;
import java.util.Arrays;
import oshi.software.os.linux.LinuxOperatingSystem;
import java.util.stream.LongStream;
import oshi.driver.linux.proc.CpuStat;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.sun.jna.platform.linux.Udev;
import java.util.HashMap;
import java.util.ArrayList;
import oshi.util.tuples.Pair;
import java.util.Iterator;
import java.util.List;
import oshi.util.ExecutingCommand;
import oshi.driver.linux.Lshw;
import oshi.util.ParseUtil;
import oshi.util.FileUtil;
import oshi.util.platform.linux.ProcPath;
import oshi.hardware.CentralProcessor;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractCentralProcessor;

@ThreadSafe
final class LinuxCentralProcessor extends AbstractCentralProcessor
{
    @Override
    protected CentralProcessor.ProcessorIdentifier queryProcessorId() {
        String cpuVendor = "";
        String cpuName = "";
        String cpuFamily = "";
        String cpuModel = "";
        String cpuStepping = "";
        long cpuFreq = 0L;
        boolean cpu64bit = false;
        final StringBuilder armStepping = new StringBuilder();
        String[] flags = new String[0];
        final List<String> cpuInfo = FileUtil.readFile(ProcPath.CPUINFO);
        for (final String line : cpuInfo) {
            final String[] splitLine = ParseUtil.whitespacesColonWhitespace.split(line);
            if (splitLine.length < 2) {
                if (!line.startsWith("CPU architecture: ")) {
                    continue;
                }
                cpuFamily = line.replace("CPU architecture: ", "").trim();
            }
            else {
                final String s = splitLine[0];
                switch (s) {
                    case "vendor_id":
                    case "CPU implementer": {
                        cpuVendor = splitLine[1];
                        continue;
                    }
                    case "model name":
                    case "Processor": {
                        cpuName = splitLine[1];
                        continue;
                    }
                    case "flags": {
                        final String[] split;
                        flags = (split = splitLine[1].toLowerCase().split(" "));
                        for (final String flag : split) {
                            if ("lm".equals(flag)) {
                                cpu64bit = true;
                                break;
                            }
                        }
                        continue;
                    }
                    case "stepping": {
                        cpuStepping = splitLine[1];
                        continue;
                    }
                    case "CPU variant": {
                        if (!armStepping.toString().startsWith("r")) {
                            armStepping.insert(0, "r" + splitLine[1]);
                            continue;
                        }
                        continue;
                    }
                    case "CPU revision": {
                        if (!armStepping.toString().contains("p")) {
                            armStepping.append('p').append(splitLine[1]);
                            continue;
                        }
                        continue;
                    }
                    case "model":
                    case "CPU part": {
                        cpuModel = splitLine[1];
                        continue;
                    }
                    case "cpu family": {
                        cpuFamily = splitLine[1];
                        continue;
                    }
                    case "cpu MHz": {
                        cpuFreq = ParseUtil.parseHertz(splitLine[1]);
                        continue;
                    }
                }
            }
        }
        if (cpuName.contains("Hz")) {
            cpuFreq = -1L;
        }
        else {
            final long cpuCapacity = Lshw.queryCpuCapacity();
            if (cpuCapacity > cpuFreq) {
                cpuFreq = cpuCapacity;
            }
        }
        if (cpuStepping.isEmpty()) {
            cpuStepping = armStepping.toString();
        }
        final String processorID = getProcessorID(cpuVendor, cpuStepping, cpuModel, cpuFamily, flags);
        if (cpuVendor.startsWith("0x")) {
            final List<String> lscpu = ExecutingCommand.runNative("lscpu");
            for (final String line2 : lscpu) {
                if (line2.startsWith("Architecture:")) {
                    cpuVendor = line2.replace("Architecture:", "").trim();
                }
            }
        }
        return new CentralProcessor.ProcessorIdentifier(cpuVendor, cpuName, cpuFamily, cpuModel, cpuStepping, processorID, cpu64bit, cpuFreq);
    }
    
    @Override
    protected Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>> initProcessorCounts() {
        final List<CentralProcessor.LogicalProcessor> logProcs = new ArrayList<CentralProcessor.LogicalProcessor>();
        final Map<Integer, Integer> coreEfficiencyMap = new HashMap<Integer, Integer>();
        final Map<Integer, String> modAliasMap = new HashMap<Integer, String>();
        final Udev.UdevContext udev = Udev.INSTANCE.udev_new();
        try {
            final Udev.UdevEnumerate enumerate = udev.enumerateNew();
            try {
                enumerate.addMatchSubsystem("cpu");
                enumerate.scanDevices();
                for (Udev.UdevListEntry entry = enumerate.getListEntry(); entry != null; entry = entry.getNext()) {
                    final String syspath = entry.getName();
                    final int processor = ParseUtil.getFirstIntValue(syspath);
                    final int coreId = FileUtil.getIntFromFile(syspath + "/topology/core_id");
                    final int pkgId = FileUtil.getIntFromFile(syspath + "/topology/physical_package_id");
                    final int pkgCoreKey = (pkgId << 16) + coreId;
                    coreEfficiencyMap.put(pkgCoreKey, FileUtil.getIntFromFile(syspath + "/cpu_capacity"));
                    final Udev.UdevDevice device = udev.deviceNewFromSyspath(syspath);
                    if (device != null) {
                        try {
                            modAliasMap.put(pkgCoreKey, device.getPropertyValue("MODALIAS"));
                        }
                        finally {
                            device.unref();
                        }
                    }
                    int nodeId = 0;
                    final String prefix = syspath + "/node";
                    try {
                        final Stream<Path> path = Files.list(Paths.get(syspath, new String[0]));
                        try {
                            final Optional<Path> first = path.filter(p -> p.toString().startsWith(prefix)).findFirst();
                            if (first.isPresent()) {
                                nodeId = ParseUtil.getFirstIntValue(first.get().getFileName().toString());
                            }
                            if (path != null) {
                                path.close();
                            }
                        }
                        catch (Throwable t) {
                            if (path != null) {
                                try {
                                    path.close();
                                }
                                catch (Throwable exception) {
                                    t.addSuppressed(exception);
                                }
                            }
                            throw t;
                        }
                    }
                    catch (IOException ex) {}
                    logProcs.add(new CentralProcessor.LogicalProcessor(processor, coreId, pkgId, nodeId));
                }
            }
            finally {
                enumerate.unref();
            }
        }
        finally {
            udev.unref();
        }
        if (logProcs.isEmpty()) {
            logProcs.add(new CentralProcessor.LogicalProcessor(0, 0, 0));
            coreEfficiencyMap.put(0, 0);
        }
        final int pkgId2;
        final int coreId2;
        final Map<K, String> map;
        final List<CentralProcessor.PhysicalProcessor> physProcs = coreEfficiencyMap.entrySet().stream().sorted((Comparator<? super Object>)Map.Entry.comparingByKey()).map(e -> {
            pkgId2 = e.getKey() >> 16;
            coreId2 = (e.getKey() & 0xFFFF);
            return new CentralProcessor.PhysicalProcessor(pkgId2, coreId2, (int)e.getValue(), map.getOrDefault(e.getKey(), ""));
        }).collect((Collector<? super Object, ?, List<CentralProcessor.PhysicalProcessor>>)Collectors.toList());
        return new Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>>(logProcs, physProcs);
    }
    
    public long[] querySystemCpuLoadTicks() {
        long[] ticks = CpuStat.getSystemCpuLoadTicks();
        if (LongStream.of(ticks).sum() == 0L) {
            ticks = CpuStat.getSystemCpuLoadTicks();
        }
        final long hz = LinuxOperatingSystem.getHz();
        for (int i = 0; i < ticks.length; ++i) {
            ticks[i] = ticks[i] * 1000L / hz;
        }
        return ticks;
    }
    
    public long[] queryCurrentFreq() {
        final long[] freqs = new long[this.getLogicalProcessorCount()];
        long max = 0L;
        final Udev.UdevContext udev = Udev.INSTANCE.udev_new();
        try {
            final Udev.UdevEnumerate enumerate = udev.enumerateNew();
            try {
                enumerate.addMatchSubsystem("cpu");
                enumerate.scanDevices();
                for (Udev.UdevListEntry entry = enumerate.getListEntry(); entry != null; entry = entry.getNext()) {
                    final String syspath = entry.getName();
                    final int cpu = ParseUtil.getFirstIntValue(syspath);
                    if (cpu >= 0 && cpu < freqs.length) {
                        freqs[cpu] = FileUtil.getLongFromFile(syspath + "/cpufreq/scaling_cur_freq");
                        if (freqs[cpu] == 0L) {
                            freqs[cpu] = FileUtil.getLongFromFile(syspath + "/cpufreq/cpuinfo_cur_freq");
                        }
                    }
                    if (max < freqs[cpu]) {
                        max = freqs[cpu];
                    }
                }
                if (max > 0L) {
                    for (int i = 0; i < freqs.length; ++i) {
                        final long[] array = freqs;
                        final int n = i;
                        array[n] *= 1000L;
                    }
                    return freqs;
                }
            }
            finally {
                enumerate.unref();
            }
        }
        finally {
            udev.unref();
        }
        Arrays.fill(freqs, -1L);
        final List<String> cpuInfo = FileUtil.readFile(ProcPath.CPUINFO);
        int proc = 0;
        for (final String s : cpuInfo) {
            if (s.toLowerCase().contains("cpu mhz")) {
                freqs[proc] = Math.round(ParseUtil.parseLastDouble(s, 0.0) * 1000000.0);
                if (++proc >= freqs.length) {
                    break;
                }
                continue;
            }
        }
        return freqs;
    }
    
    public long queryMaxFreq() {
        long max = Arrays.stream(this.getCurrentFreq()).max().orElse(-1L);
        if (max > 0L) {
            max /= 1000L;
        }
        final Udev.UdevContext udev = Udev.INSTANCE.udev_new();
        try {
            final Udev.UdevEnumerate enumerate = udev.enumerateNew();
            try {
                enumerate.addMatchSubsystem("cpu");
                enumerate.scanDevices();
                final Udev.UdevListEntry entry = enumerate.getListEntry();
                if (entry != null) {
                    final String syspath = entry.getName();
                    final String cpuFreqPath = syspath.substring(0, syspath.lastIndexOf(File.separatorChar)) + "/cpuFreq";
                    final String policyPrefix = cpuFreqPath + "/policy";
                    try {
                        final Stream<Path> path = Files.list(Paths.get(cpuFreqPath, new String[0]));
                        try {
                            long freq;
                            final Optional<Long> maxPolicy = path.filter(p -> p.toString().startsWith(policyPrefix)).map(p -> {
                                freq = FileUtil.getLongFromFile(p.toString() + "/scaling_max_freq");
                                if (freq == 0L) {
                                    freq = FileUtil.getLongFromFile(p.toString() + "/cpuinfo_max_freq");
                                }
                                return freq;
                            }).max(Long::compare);
                            if (maxPolicy.isPresent() && max < maxPolicy.get()) {
                                max = maxPolicy.get();
                            }
                            if (path != null) {
                                path.close();
                            }
                        }
                        catch (Throwable t) {
                            if (path != null) {
                                try {
                                    path.close();
                                }
                                catch (Throwable exception) {
                                    t.addSuppressed(exception);
                                }
                            }
                            throw t;
                        }
                    }
                    catch (IOException ex) {}
                }
            }
            finally {
                enumerate.unref();
            }
        }
        finally {
            udev.unref();
        }
        if (max == 0L) {
            return -1L;
        }
        max *= 1000L;
        final long lshwMax = Lshw.queryCpuCapacity();
        return (lshwMax > max) ? lshwMax : max;
    }
    
    @Override
    public double[] getSystemLoadAverage(final int nelem) {
        if (nelem < 1 || nelem > 3) {
            throw new IllegalArgumentException("Must include from one to three elements.");
        }
        final double[] average = new double[nelem];
        final int retval = LinuxLibc.INSTANCE.getloadavg(average, nelem);
        if (retval < nelem) {
            for (int i = Math.max(retval, 0); i < average.length; ++i) {
                average[i] = -1.0;
            }
        }
        return average;
    }
    
    public long[][] queryProcessorCpuLoadTicks() {
        long[][] ticks = CpuStat.getProcessorCpuLoadTicks(this.getLogicalProcessorCount());
        if (LongStream.of(ticks[0]).sum() == 0L) {
            ticks = CpuStat.getProcessorCpuLoadTicks(this.getLogicalProcessorCount());
        }
        final long hz = LinuxOperatingSystem.getHz();
        for (int i = 0; i < ticks.length; ++i) {
            for (int j = 0; j < ticks[i].length; ++j) {
                ticks[i][j] = ticks[i][j] * 1000L / hz;
            }
        }
        return ticks;
    }
    
    private static String getProcessorID(final String vendor, final String stepping, final String model, final String family, final String[] flags) {
        boolean procInfo = false;
        String marker = "Processor Information";
        for (final String checkLine : ExecutingCommand.runNative("dmidecode -t 4")) {
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
        marker = "eax=";
        for (final String checkLine : ExecutingCommand.runNative("cpuid -1r")) {
            if (checkLine.contains(marker) && checkLine.trim().startsWith("0x00000001")) {
                String eax = "";
                String edx = "";
                for (final String register : ParseUtil.whitespaces.split(checkLine)) {
                    if (register.startsWith("eax=")) {
                        eax = ParseUtil.removeMatchingString(register, "eax=0x");
                    }
                    else if (register.startsWith("edx=")) {
                        edx = ParseUtil.removeMatchingString(register, "edx=0x");
                    }
                }
                return edx + eax;
            }
        }
        if (vendor.startsWith("0x")) {
            return createMIDR(vendor, stepping, model, family) + "00000000";
        }
        return AbstractCentralProcessor.createProcessorID(stepping, model, family, flags);
    }
    
    private static String createMIDR(final String vendor, final String stepping, final String model, final String family) {
        int midrBytes = 0;
        if (stepping.startsWith("r") && stepping.contains("p")) {
            final String[] rev = stepping.substring(1).split("p");
            midrBytes |= ParseUtil.parseLastInt(rev[1], 0);
            midrBytes |= ParseUtil.parseLastInt(rev[0], 0) << 20;
        }
        midrBytes |= ParseUtil.parseLastInt(model, 0) << 4;
        midrBytes |= ParseUtil.parseLastInt(family, 0) << 16;
        midrBytes |= ParseUtil.parseLastInt(vendor, 0) << 24;
        return String.format("%08X", midrBytes);
    }
    
    public long queryContextSwitches() {
        return CpuStat.getContextSwitches();
    }
    
    public long queryInterrupts() {
        return CpuStat.getInterrupts();
    }
}
