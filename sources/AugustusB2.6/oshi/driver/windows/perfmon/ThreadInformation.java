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
public final class ThreadInformation
{
    private static final String THREAD = "Thread";
    private static final String WIN32_PERF_RAW_DATA_PERF_PROC_THREAD = "Win32_PerfRawData_PerfProc_Thread WHERE NOT Name LIKE \"%_Total\"";
    
    private ThreadInformation() {
    }
    
    public static Pair<List<String>, Map<ThreadPerformanceProperty, List<Long>>> queryThreadCounters() {
        return PerfCounterWildcardQuery.queryInstancesAndValues(ThreadPerformanceProperty.class, "Thread", "Win32_PerfRawData_PerfProc_Thread WHERE NOT Name LIKE \"%_Total\"");
    }
    
    public enum ThreadPerformanceProperty implements PerfCounterWildcardQuery.PdhCounterWildcardProperty
    {
        NAME("^*_Total"), 
        PERCENTUSERTIME("% User Time"), 
        PERCENTPRIVILEGEDTIME("% Privileged Time"), 
        ELAPSEDTIME("Elapsed Time"), 
        PRIORITYCURRENT("Priority Current"), 
        STARTADDRESS("Start Address"), 
        THREADSTATE("Thread State"), 
        THREADWAITREASON("Thread Wait Reason"), 
        IDPROCESS("ID Process"), 
        IDTHREAD("ID Thread"), 
        CONTEXTSWITCHESPERSEC("Context Switches/sec");
        
        private final String counter;
        
        private ThreadPerformanceProperty(final String counter) {
            this.counter = counter;
        }
        
        @Override
        public String getCounter() {
            return this.counter;
        }
        
        private static /* synthetic */ ThreadPerformanceProperty[] $values() {
            return new ThreadPerformanceProperty[] { ThreadPerformanceProperty.NAME, ThreadPerformanceProperty.PERCENTUSERTIME, ThreadPerformanceProperty.PERCENTPRIVILEGEDTIME, ThreadPerformanceProperty.ELAPSEDTIME, ThreadPerformanceProperty.PRIORITYCURRENT, ThreadPerformanceProperty.STARTADDRESS, ThreadPerformanceProperty.THREADSTATE, ThreadPerformanceProperty.THREADWAITREASON, ThreadPerformanceProperty.IDPROCESS, ThreadPerformanceProperty.IDTHREAD, ThreadPerformanceProperty.CONTEXTSWITCHESPERSEC };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
