// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows;

import com.sun.jna.platform.win32.VersionHelpers;
import java.util.Iterator;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import java.util.Map;
import oshi.util.platform.windows.WmiUtil;
import oshi.driver.windows.wmi.Win32Processor;
import java.util.Comparator;
import java.util.HashMap;
import com.sun.jna.platform.win32.WinNT;
import java.util.ArrayList;
import com.sun.jna.platform.win32.Kernel32Util;
import oshi.hardware.CentralProcessor;
import java.util.List;
import oshi.util.tuples.Pair;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class LogicalProcessorInformation
{
    private static final boolean IS_WIN10_OR_GREATER;
    
    private LogicalProcessorInformation() {
    }
    
    public static Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>> getLogicalProcessorInformationEx() {
        final WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION_EX[] procInfo = Kernel32Util.getLogicalProcessorInformationEx(65535);
        final List<WinNT.GROUP_AFFINITY[]> packages = new ArrayList<WinNT.GROUP_AFFINITY[]>();
        final List<WinNT.GROUP_AFFINITY> cores = new ArrayList<WinNT.GROUP_AFFINITY>();
        final List<WinNT.NUMA_NODE_RELATIONSHIP> numaNodes = new ArrayList<WinNT.NUMA_NODE_RELATIONSHIP>();
        final Map<WinNT.GROUP_AFFINITY, Integer> coreEfficiencyMap = new HashMap<WinNT.GROUP_AFFINITY, Integer>();
        for (int i = 0; i < procInfo.length; ++i) {
            switch (procInfo[i].relationship) {
                case 3: {
                    packages.add(((WinNT.PROCESSOR_RELATIONSHIP)procInfo[i]).groupMask);
                    break;
                }
                case 0: {
                    final WinNT.PROCESSOR_RELATIONSHIP core = (WinNT.PROCESSOR_RELATIONSHIP)procInfo[i];
                    cores.add(core.groupMask[0]);
                    if (LogicalProcessorInformation.IS_WIN10_OR_GREATER) {
                        coreEfficiencyMap.put(core.groupMask[0], (int)core.efficiencyClass);
                        break;
                    }
                    break;
                }
                case 1: {
                    numaNodes.add((WinNT.NUMA_NODE_RELATIONSHIP)procInfo[i]);
                    break;
                }
            }
        }
        cores.sort(Comparator.comparing(c -> c.group * 64L + c.mask.longValue()));
        packages.sort(Comparator.comparing(p -> p[0].group * 64L + p[0].mask.longValue()));
        numaNodes.sort(Comparator.comparing(n -> n.nodeNumber));
        final Map<Integer, String> processorIdMap = new HashMap<Integer, String>();
        final WbemcliUtil.WmiResult<Win32Processor.ProcessorIdProperty> processorId = Win32Processor.queryProcessorId();
        for (int pkg = 0; pkg < processorId.getResultCount(); ++pkg) {
            processorIdMap.put(pkg, WmiUtil.getString(processorId, Win32Processor.ProcessorIdProperty.PROCESSORID, pkg));
        }
        final List<CentralProcessor.LogicalProcessor> logProcs = new ArrayList<CentralProcessor.LogicalProcessor>();
        final Map<Integer, Integer> corePkgMap = new HashMap<Integer, Integer>();
        final Map<Integer, String> pkgCpuidMap = new HashMap<Integer, String>();
        for (final WinNT.NUMA_NODE_RELATIONSHIP node : numaNodes) {
            final int nodeNum = node.nodeNumber;
            final int group = node.groupMask.group;
            final long mask = node.groupMask.mask.longValue();
            final int lowBit = Long.numberOfTrailingZeros(mask);
            for (int hiBit = 63 - Long.numberOfLeadingZeros(mask), lp = lowBit; lp <= hiBit; ++lp) {
                if ((mask & 1L << lp) != 0x0L) {
                    final int coreId = getMatchingCore(cores, group, lp);
                    final int pkgId = getMatchingPackage(packages, group, lp);
                    corePkgMap.put(coreId, pkgId);
                    pkgCpuidMap.put(coreId, processorIdMap.getOrDefault(pkgId, ""));
                    final CentralProcessor.LogicalProcessor logProc = new CentralProcessor.LogicalProcessor(lp, coreId, pkgId, nodeNum, group);
                    logProcs.add(logProc);
                }
            }
        }
        final List<CentralProcessor.PhysicalProcessor> physProcs = getPhysProcs(cores, coreEfficiencyMap, corePkgMap, pkgCpuidMap);
        return new Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>>(logProcs, physProcs);
    }
    
    private static List<CentralProcessor.PhysicalProcessor> getPhysProcs(final List<WinNT.GROUP_AFFINITY> cores, final Map<WinNT.GROUP_AFFINITY, Integer> coreEfficiencyMap, final Map<Integer, Integer> corePkgMap, final Map<Integer, String> coreCpuidMap) {
        final List<CentralProcessor.PhysicalProcessor> physProcs = new ArrayList<CentralProcessor.PhysicalProcessor>();
        for (int coreId = 0; coreId < cores.size(); ++coreId) {
            final int efficiency = coreEfficiencyMap.getOrDefault(cores.get(coreId), 0);
            final String cpuid = coreCpuidMap.getOrDefault(coreId, "");
            final int pkgId = corePkgMap.getOrDefault(coreId, 0);
            physProcs.add(new CentralProcessor.PhysicalProcessor(pkgId, coreId, efficiency, cpuid));
        }
        return physProcs;
    }
    
    private static int getMatchingPackage(final List<WinNT.GROUP_AFFINITY[]> packages, final int g, final int lp) {
        for (int i = 0; i < packages.size(); ++i) {
            for (int j = 0; j < packages.get(i).length; ++j) {
                if ((packages.get(i)[j].mask.longValue() & 1L << lp) != 0x0L && packages.get(i)[j].group == g) {
                    return i;
                }
            }
        }
        return 0;
    }
    
    private static int getMatchingCore(final List<WinNT.GROUP_AFFINITY> cores, final int g, final int lp) {
        for (int j = 0; j < cores.size(); ++j) {
            if ((cores.get(j).mask.longValue() & 1L << lp) != 0x0L && cores.get(j).group == g) {
                return j;
            }
        }
        return 0;
    }
    
    public static Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>> getLogicalProcessorInformation() {
        final List<Long> packageMaskList = new ArrayList<Long>();
        final List<Long> coreMaskList = new ArrayList<Long>();
        final WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION[] logicalProcessorInformation;
        final WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION[] processors = logicalProcessorInformation = Kernel32Util.getLogicalProcessorInformation();
        for (final WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION proc : logicalProcessorInformation) {
            if (proc.relationship == 3) {
                packageMaskList.add(proc.processorMask.longValue());
            }
            else if (proc.relationship == 0) {
                coreMaskList.add(proc.processorMask.longValue());
            }
        }
        coreMaskList.sort(null);
        packageMaskList.sort(null);
        final List<CentralProcessor.LogicalProcessor> logProcs = new ArrayList<CentralProcessor.LogicalProcessor>();
        for (int core = 0; core < coreMaskList.size(); ++core) {
            final long coreMask = coreMaskList.get(core);
            final int lowBit = Long.numberOfTrailingZeros(coreMask);
            for (int hiBit = 63 - Long.numberOfLeadingZeros(coreMask), i = lowBit; i <= hiBit; ++i) {
                if ((coreMask & 1L << i) != 0x0L) {
                    final CentralProcessor.LogicalProcessor logProc = new CentralProcessor.LogicalProcessor(i, core, getBitMatchingPackageNumber(packageMaskList, i));
                    logProcs.add(logProc);
                }
            }
        }
        return new Pair<List<CentralProcessor.LogicalProcessor>, List<CentralProcessor.PhysicalProcessor>>(logProcs, null);
    }
    
    private static int getBitMatchingPackageNumber(final List<Long> packageMaskList, final int logProc) {
        for (int i = 0; i < packageMaskList.size(); ++i) {
            if (((long)packageMaskList.get(i) & 1L << logProc) != 0x0L) {
                return i;
            }
        }
        return 0;
    }
    
    static {
        IS_WIN10_OR_GREATER = VersionHelpers.IsWindows10OrGreater();
    }
}
