// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.registry;

import oshi.annotation.concurrent.Immutable;
import oshi.util.GlobalConfig;
import oshi.util.tuples.Pair;
import java.util.Iterator;
import oshi.util.tuples.Triplet;
import com.sun.jna.platform.win32.WinBase;
import java.util.HashMap;
import java.util.List;
import oshi.driver.windows.perfmon.ProcessInformation;
import java.util.Map;
import java.util.Collection;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class ProcessPerformanceData
{
    private static final String PROCESS = "Process";
    public static final String WIN_HKEY_PERFDATA = "oshi.os.windows.hkeyperfdata";
    private static final boolean PERFDATA;
    
    private ProcessPerformanceData() {
    }
    
    public static Map<Integer, PerfCounterBlock> buildProcessMapFromRegistry(final Collection<Integer> pids) {
        Triplet<List<Map<ProcessInformation.ProcessPerformanceProperty, Object>>, Long, Long> processData = null;
        if (ProcessPerformanceData.PERFDATA) {
            processData = HkeyPerformanceDataUtil.readPerfDataFromRegistry("Process", ProcessInformation.ProcessPerformanceProperty.class);
        }
        if (processData == null) {
            return null;
        }
        final List<Map<ProcessInformation.ProcessPerformanceProperty, Object>> processInstanceMaps = processData.getA();
        final long now = processData.getC();
        final Map<Integer, PerfCounterBlock> processMap = new HashMap<Integer, PerfCounterBlock>();
        for (final Map<ProcessInformation.ProcessPerformanceProperty, Object> processInstanceMap : processInstanceMaps) {
            final int pid = processInstanceMap.get(ProcessInformation.ProcessPerformanceProperty.IDPROCESS);
            final String name = processInstanceMap.get(ProcessInformation.ProcessPerformanceProperty.NAME);
            if ((pids == null || pids.contains(pid)) && !"_Total".equals(name)) {
                long ctime = processInstanceMap.get(ProcessInformation.ProcessPerformanceProperty.ELAPSEDTIME);
                if (ctime > now) {
                    ctime = WinBase.FILETIME.filetimeToDate((int)(ctime >> 32), (int)(ctime & 0xFFFFFFFFL)).getTime();
                }
                long upTime = now - ctime;
                if (upTime < 1L) {
                    upTime = 1L;
                }
                processMap.put(pid, new PerfCounterBlock(name, processInstanceMap.get(ProcessInformation.ProcessPerformanceProperty.CREATINGPROCESSID), processInstanceMap.get(ProcessInformation.ProcessPerformanceProperty.PRIORITYBASE), processInstanceMap.get(ProcessInformation.ProcessPerformanceProperty.PRIVATEBYTES), ctime, upTime, processInstanceMap.get(ProcessInformation.ProcessPerformanceProperty.IOREADBYTESPERSEC), processInstanceMap.get(ProcessInformation.ProcessPerformanceProperty.IOWRITEBYTESPERSEC), processInstanceMap.get(ProcessInformation.ProcessPerformanceProperty.PAGEFAULTSPERSEC)));
            }
        }
        return processMap;
    }
    
    public static Map<Integer, PerfCounterBlock> buildProcessMapFromPerfCounters(final Collection<Integer> pids) {
        final Map<Integer, PerfCounterBlock> processMap = new HashMap<Integer, PerfCounterBlock>();
        final Pair<List<String>, Map<ProcessInformation.ProcessPerformanceProperty, List<Long>>> instanceValues = ProcessInformation.queryProcessCounters();
        final long now = System.currentTimeMillis();
        final List<String> instances = instanceValues.getA();
        final Map<ProcessInformation.ProcessPerformanceProperty, List<Long>> valueMap = instanceValues.getB();
        final List<Long> pidList = valueMap.get(ProcessInformation.ProcessPerformanceProperty.IDPROCESS);
        final List<Long> ppidList = valueMap.get(ProcessInformation.ProcessPerformanceProperty.CREATINGPROCESSID);
        final List<Long> priorityList = valueMap.get(ProcessInformation.ProcessPerformanceProperty.PRIORITYBASE);
        final List<Long> ioReadList = valueMap.get(ProcessInformation.ProcessPerformanceProperty.IOREADBYTESPERSEC);
        final List<Long> ioWriteList = valueMap.get(ProcessInformation.ProcessPerformanceProperty.IOWRITEBYTESPERSEC);
        final List<Long> workingSetSizeList = valueMap.get(ProcessInformation.ProcessPerformanceProperty.PRIVATEBYTES);
        final List<Long> elapsedTimeList = valueMap.get(ProcessInformation.ProcessPerformanceProperty.ELAPSEDTIME);
        final List<Long> pageFaultsList = valueMap.get(ProcessInformation.ProcessPerformanceProperty.PAGEFAULTSPERSEC);
        for (int inst = 0; inst < instances.size(); ++inst) {
            final int pid = pidList.get(inst).intValue();
            if (pids == null || pids.contains(pid)) {
                long ctime = elapsedTimeList.get(inst);
                if (ctime > now) {
                    ctime = WinBase.FILETIME.filetimeToDate((int)(ctime >> 32), (int)(ctime & 0xFFFFFFFFL)).getTime();
                }
                long upTime = now - ctime;
                if (upTime < 1L) {
                    upTime = 1L;
                }
                processMap.put(pid, new PerfCounterBlock(instances.get(inst), ppidList.get(inst).intValue(), priorityList.get(inst).intValue(), workingSetSizeList.get(inst), ctime, upTime, ioReadList.get(inst), ioWriteList.get(inst), pageFaultsList.get(inst).intValue()));
            }
        }
        return processMap;
    }
    
    static {
        PERFDATA = GlobalConfig.get("oshi.os.windows.hkeyperfdata", true);
    }
    
    @Immutable
    public static class PerfCounterBlock
    {
        private final String name;
        private final int parentProcessID;
        private final int priority;
        private final long residentSetSize;
        private final long startTime;
        private final long upTime;
        private final long bytesRead;
        private final long bytesWritten;
        private final int pageFaults;
        
        public PerfCounterBlock(final String name, final int parentProcessID, final int priority, final long residentSetSize, final long startTime, final long upTime, final long bytesRead, final long bytesWritten, final int pageFaults) {
            this.name = name;
            this.parentProcessID = parentProcessID;
            this.priority = priority;
            this.residentSetSize = residentSetSize;
            this.startTime = startTime;
            this.upTime = upTime;
            this.bytesRead = bytesRead;
            this.bytesWritten = bytesWritten;
            this.pageFaults = pageFaults;
        }
        
        public String getName() {
            return this.name;
        }
        
        public int getParentProcessID() {
            return this.parentProcessID;
        }
        
        public int getPriority() {
            return this.priority;
        }
        
        public long getResidentSetSize() {
            return this.residentSetSize;
        }
        
        public long getStartTime() {
            return this.startTime;
        }
        
        public long getUpTime() {
            return this.upTime;
        }
        
        public long getBytesRead() {
            return this.bytesRead;
        }
        
        public long getBytesWritten() {
            return this.bytesWritten;
        }
        
        public long getPageFaults() {
            return this.pageFaults;
        }
    }
}
