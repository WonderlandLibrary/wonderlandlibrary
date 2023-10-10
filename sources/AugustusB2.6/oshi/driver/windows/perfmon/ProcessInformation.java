// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.perfmon;

import oshi.util.platform.windows.PerfCounterWildcardQuery;
import java.util.Map;
import java.util.List;
import oshi.util.tuples.Pair;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class ProcessInformation
{
    private static final String WIN32_PERFPROC_PROCESS = "Win32_PerfRawData_PerfProc_Process";
    private static final String PROCESS = "Process";
    private static final String WIN32_PERFPROC_PROCESS_WHERE_NOT_NAME_LIKE_TOTAL = "Win32_PerfRawData_PerfProc_Process WHERE NOT Name LIKE \"%_Total\"";
    
    private ProcessInformation() {
    }
    
    public static Pair<List<String>, Map<ProcessPerformanceProperty, List<Long>>> queryProcessCounters() {
        return PerfCounterWildcardQuery.queryInstancesAndValues(ProcessPerformanceProperty.class, "Process", "Win32_PerfRawData_PerfProc_Process WHERE NOT Name LIKE \"%_Total\"");
    }
    
    public static Pair<List<String>, Map<HandleCountProperty, List<Long>>> queryHandles() {
        return PerfCounterWildcardQuery.queryInstancesAndValues(HandleCountProperty.class, "Process", "Win32_PerfRawData_PerfProc_Process");
    }
    
    public enum ProcessPerformanceProperty implements PerfCounterWildcardQuery.PdhCounterWildcardProperty
    {
        NAME("^*_Total"), 
        PRIORITYBASE("Priority Base"), 
        ELAPSEDTIME("Elapsed Time"), 
        IDPROCESS("ID Process"), 
        CREATINGPROCESSID("Creating Process ID"), 
        IOREADBYTESPERSEC("IO Read Bytes/sec"), 
        IOWRITEBYTESPERSEC("IO Write Bytes/sec"), 
        PRIVATEBYTES("Working Set - Private"), 
        PAGEFAULTSPERSEC("Page Faults/sec");
        
        private final String counter;
        
        private ProcessPerformanceProperty(final String counter) {
            this.counter = counter;
        }
        
        @Override
        public String getCounter() {
            return this.counter;
        }
        
        private static /* synthetic */ ProcessPerformanceProperty[] $values() {
            return new ProcessPerformanceProperty[] { ProcessPerformanceProperty.NAME, ProcessPerformanceProperty.PRIORITYBASE, ProcessPerformanceProperty.ELAPSEDTIME, ProcessPerformanceProperty.IDPROCESS, ProcessPerformanceProperty.CREATINGPROCESSID, ProcessPerformanceProperty.IOREADBYTESPERSEC, ProcessPerformanceProperty.IOWRITEBYTESPERSEC, ProcessPerformanceProperty.PRIVATEBYTES, ProcessPerformanceProperty.PAGEFAULTSPERSEC };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    public enum HandleCountProperty implements PerfCounterWildcardQuery.PdhCounterWildcardProperty
    {
        NAME("_Total"), 
        HANDLECOUNT("Handle Count");
        
        private final String counter;
        
        private HandleCountProperty(final String counter) {
            this.counter = counter;
        }
        
        @Override
        public String getCounter() {
            return this.counter;
        }
        
        private static /* synthetic */ HandleCountProperty[] $values() {
            return new HandleCountProperty[] { HandleCountProperty.NAME, HandleCountProperty.HANDLECOUNT };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
