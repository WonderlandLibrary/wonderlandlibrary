// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.windows;

import org.slf4j.LoggerFactory;
import oshi.driver.windows.perfmon.SystemInformation;
import com.sun.jna.Pointer;
import com.sun.jna.Memory;
import oshi.jna.platform.windows.PowrProf;
import java.util.Arrays;
import oshi.driver.windows.perfmon.ProcessorInformation;
import java.util.Iterator;
import java.util.HashMap;
import oshi.driver.windows.LogicalProcessorInformation;
import com.sun.jna.platform.win32.VersionHelpers;
import java.util.List;
import oshi.util.tuples.Pair;
import oshi.util.ParseUtil;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.util.platform.windows.WmiUtil;
import oshi.driver.windows.wmi.Win32Processor;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import oshi.hardware.CentralProcessor;
import java.util.Map;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractCentralProcessor;

@ThreadSafe
final class WindowsCentralProcessor extends AbstractCentralProcessor
{
    private static final Logger LOG;
    private Map<String, Integer> numaNodeProcToLogicalProcMap;
    
    @Override
    protected CentralProcessor.ProcessorIdentifier queryProcessorId() {
        String cpuVendor = "";
        String cpuName = "";
        String cpuIdentifier = "";
        String cpuFamily = "";
        String cpuModel = "";
        String cpuStepping = "";
        long cpuVendorFreq = 0L;
        boolean cpu64bit = false;
        final String cpuRegistryRoot = "HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\";
        final String[] processorIds = Advapi32Util.registryGetKeys(WinReg.HKEY_LOCAL_MACHINE, "HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\");
        if (processorIds.length > 0) {
            final String cpuRegistryPath = "HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\" + processorIds[0];
            cpuVendor = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, cpuRegistryPath, "VendorIdentifier");
            cpuName = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, cpuRegistryPath, "ProcessorNameString");
            cpuIdentifier = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, cpuRegistryPath, "Identifier");
            try {
                cpuVendorFreq = Advapi32Util.registryGetIntValue(WinReg.HKEY_LOCAL_MACHINE, cpuRegistryPath, "~MHz") * 1000000L;
            }
            catch (Win32Exception ex) {}
        }
        if (!cpuIdentifier.isEmpty()) {
            cpuFamily = parseIdentifier(cpuIdentifier, "Family");
            cpuModel = parseIdentifier(cpuIdentifier, "Model");
            cpuStepping = parseIdentifier(cpuIdentifier, "Stepping");
        }
        final WinBase.SYSTEM_INFO sysinfo = new WinBase.SYSTEM_INFO();
        Kernel32.INSTANCE.GetNativeSystemInfo(sysinfo);
        final int processorArchitecture = sysinfo.processorArchitecture.pi.wProcessorArchitecture.intValue();
        if (processorArchitecture == 9 || processorArchitecture == 12 || processorArchitecture == 6) {
            cpu64bit = true;
        }
        final WbemcliUtil.WmiResult<Win32Processor.ProcessorIdProperty> processorId = Win32Processor.queryProcessorId();
        String processorID;
        if (processorId.getResultCount() > 0) {
            processorID = WmiUtil.getString(processorId, Win32Processor.ProcessorIdProperty.PROCESSORID, 0);
        }
        else {
            processorID = AbstractCentralProcessor.createProcessorID(cpuStepping, cpuModel, cpuFamily, cpu64bit ? new String[] { "ia64" } : new String[0]);
        }
        return new CentralProcessor.ProcessorIdentifier(cpuVendor, cpuName, cpuFamily, cpuModel, cpuStepping, processorID, cpu64bit, cpuVendorFreq);
    }
    
    private static String parseIdentifier(final String identifier, final String key) {
        final String[] idSplit = ParseUtil.whitespaces.split(identifier);
        boolean found = false;
        for (final String s : idSplit) {
            if (found) {
                return s;
            }
            found = s.equals(key);
        }
        return "";
    }
    
    @Override
    protected Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>> initProcessorCounts() {
        if (VersionHelpers.IsWindows7OrGreater()) {
            final Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>> procs = LogicalProcessorInformation.getLogicalProcessorInformationEx();
            int curNode = -1;
            int procNum = 0;
            int lp = 0;
            this.numaNodeProcToLogicalProcMap = new HashMap<String, Integer>();
            for (final CentralProcessor.LogicalProcessor logProc : procs.getA()) {
                final int node = logProc.getNumaNode();
                if (node != curNode) {
                    curNode = node;
                    procNum = 0;
                }
                this.numaNodeProcToLogicalProcMap.put(String.format("%d,%d", logProc.getNumaNode(), procNum++), lp++);
            }
            return procs;
        }
        return LogicalProcessorInformation.getLogicalProcessorInformation();
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
        }
        return ticks;
    }
    
    public long[] queryCurrentFreq() {
        if (VersionHelpers.IsWindows7OrGreater()) {
            final Pair<List<String>, Map<ProcessorInformation.ProcessorFrequencyProperty, List<Long>>> instanceValuePair = ProcessorInformation.queryFrequencyCounters();
            final List<String> instances = instanceValuePair.getA();
            final Map<ProcessorInformation.ProcessorFrequencyProperty, List<Long>> valueMap = instanceValuePair.getB();
            final List<Long> percentMaxList = valueMap.get(ProcessorInformation.ProcessorFrequencyProperty.PERCENTOFMAXIMUMFREQUENCY);
            if (!instances.isEmpty()) {
                final long maxFreq = this.getMaxFreq();
                final long[] freqs = new long[this.getLogicalProcessorCount()];
                for (int p = 0; p < instances.size(); ++p) {
                    final int cpu = instances.get(p).contains(",") ? this.numaNodeProcToLogicalProcMap.getOrDefault(instances.get(p), 0) : ParseUtil.parseIntOrDefault(instances.get(p), 0);
                    if (cpu < this.getLogicalProcessorCount()) {
                        freqs[cpu] = percentMaxList.get(cpu) * maxFreq / 100L;
                    }
                }
                return freqs;
            }
        }
        return this.queryNTPower(2);
    }
    
    public long queryMaxFreq() {
        final long[] freqs = this.queryNTPower(1);
        return Arrays.stream(freqs).max().orElse(-1L);
    }
    
    private long[] queryNTPower(final int fieldIndex) {
        PowrProf.ProcessorPowerInformation ppi = new PowrProf.ProcessorPowerInformation();
        final long[] freqs = new long[this.getLogicalProcessorCount()];
        final int bufferSize = ppi.size() * freqs.length;
        final Memory mem = new Memory(bufferSize);
        if (0 != PowrProf.INSTANCE.CallNtPowerInformation(11, null, 0, mem, bufferSize)) {
            WindowsCentralProcessor.LOG.error("Unable to get Processor Information");
            Arrays.fill(freqs, -1L);
            return freqs;
        }
        for (int i = 0; i < freqs.length; ++i) {
            ppi = new PowrProf.ProcessorPowerInformation(mem.share(i * (long)ppi.size()));
            if (fieldIndex == 1) {
                freqs[i] = ppi.maxMhz * 1000000L;
            }
            else if (fieldIndex == 2) {
                freqs[i] = ppi.currentMhz * 1000000L;
            }
            else {
                freqs[i] = -1L;
            }
        }
        return freqs;
    }
    
    @Override
    public double[] getSystemLoadAverage(final int nelem) {
        if (nelem < 1 || nelem > 3) {
            throw new IllegalArgumentException("Must include from one to three elements.");
        }
        final double[] average = new double[nelem];
        for (int i = 0; i < average.length; ++i) {
            average[i] = -1.0;
        }
        return average;
    }
    
    public long[][] queryProcessorCpuLoadTicks() {
        final Pair<List<String>, Map<ProcessorInformation.ProcessorTickCountProperty, List<Long>>> instanceValuePair = ProcessorInformation.queryProcessorCounters();
        final List<String> instances = instanceValuePair.getA();
        final Map<ProcessorInformation.ProcessorTickCountProperty, List<Long>> valueMap = instanceValuePair.getB();
        final List<Long> systemList = valueMap.get(ProcessorInformation.ProcessorTickCountProperty.PERCENTPRIVILEGEDTIME);
        final List<Long> userList = valueMap.get(ProcessorInformation.ProcessorTickCountProperty.PERCENTUSERTIME);
        final List<Long> irqList = valueMap.get(ProcessorInformation.ProcessorTickCountProperty.PERCENTINTERRUPTTIME);
        final List<Long> softIrqList = valueMap.get(ProcessorInformation.ProcessorTickCountProperty.PERCENTDPCTIME);
        final List<Long> idleList = valueMap.get(ProcessorInformation.ProcessorTickCountProperty.PERCENTPROCESSORTIME);
        final long[][] ticks = new long[this.getLogicalProcessorCount()][CentralProcessor.TickType.values().length];
        if (instances.isEmpty() || systemList == null || userList == null || irqList == null || softIrqList == null || idleList == null) {
            return ticks;
        }
        for (int p = 0; p < instances.size(); ++p) {
            final int cpu = instances.get(p).contains(",") ? this.numaNodeProcToLogicalProcMap.getOrDefault(instances.get(p), 0) : ParseUtil.parseIntOrDefault(instances.get(p), 0);
            if (cpu < this.getLogicalProcessorCount()) {
                ticks[cpu][CentralProcessor.TickType.SYSTEM.getIndex()] = systemList.get(cpu);
                ticks[cpu][CentralProcessor.TickType.USER.getIndex()] = userList.get(cpu);
                ticks[cpu][CentralProcessor.TickType.IRQ.getIndex()] = irqList.get(cpu);
                ticks[cpu][CentralProcessor.TickType.SOFTIRQ.getIndex()] = softIrqList.get(cpu);
                ticks[cpu][CentralProcessor.TickType.IDLE.getIndex()] = idleList.get(cpu);
                final long[] array = ticks[cpu];
                final int index = CentralProcessor.TickType.SYSTEM.getIndex();
                array[index] -= ticks[cpu][CentralProcessor.TickType.IRQ.getIndex()] + ticks[cpu][CentralProcessor.TickType.SOFTIRQ.getIndex()];
                final long[] array2 = ticks[cpu];
                final int index2 = CentralProcessor.TickType.SYSTEM.getIndex();
                array2[index2] /= 10000L;
                final long[] array3 = ticks[cpu];
                final int index3 = CentralProcessor.TickType.USER.getIndex();
                array3[index3] /= 10000L;
                final long[] array4 = ticks[cpu];
                final int index4 = CentralProcessor.TickType.IRQ.getIndex();
                array4[index4] /= 10000L;
                final long[] array5 = ticks[cpu];
                final int index5 = CentralProcessor.TickType.SOFTIRQ.getIndex();
                array5[index5] /= 10000L;
                final long[] array6 = ticks[cpu];
                final int index6 = CentralProcessor.TickType.IDLE.getIndex();
                array6[index6] /= 10000L;
            }
        }
        return ticks;
    }
    
    public long queryContextSwitches() {
        return SystemInformation.queryContextSwitchCounters().getOrDefault(SystemInformation.ContextSwitchProperty.CONTEXTSWITCHESPERSEC, 0L);
    }
    
    public long queryInterrupts() {
        return ProcessorInformation.queryInterruptCounters().getOrDefault(ProcessorInformation.InterruptsProperty.INTERRUPTSPERSEC, 0L);
    }
    
    static {
        LOG = LoggerFactory.getLogger(WindowsCentralProcessor.class);
    }
}
