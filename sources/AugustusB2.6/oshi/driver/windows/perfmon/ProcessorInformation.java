// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.perfmon;

import com.sun.jna.platform.win32.VersionHelpers;
import oshi.util.platform.windows.PerfCounterQuery;
import oshi.util.platform.windows.PerfCounterWildcardQuery;
import java.util.Map;
import java.util.List;
import oshi.util.tuples.Pair;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class ProcessorInformation
{
    private static final String PROCESSOR = "Processor";
    private static final String PROCESSOR_INFORMATION = "Processor Information";
    private static final String WIN32_PERF_RAW_DATA_COUNTERS_PROCESSOR_INFORMATION_WHERE_NOT_NAME_LIKE_TOTAL = "Win32_PerfRawData_Counters_ProcessorInformation WHERE NOT Name LIKE \"%_Total\"";
    private static final String WIN32_PERF_RAW_DATA_PERF_OS_PROCESSOR_WHERE_NAME_NOT_TOTAL = "Win32_PerfRawData_PerfOS_Processor WHERE Name!=\"_Total\"";
    private static final String WIN32_PERF_RAW_DATA_PERF_OS_PROCESSOR_WHERE_NAME_TOTAL = "Win32_PerfRawData_PerfOS_Processor WHERE Name=\"_Total\"";
    private static final boolean IS_WIN7_OR_GREATER;
    
    private ProcessorInformation() {
    }
    
    public static Pair<List<String>, Map<ProcessorTickCountProperty, List<Long>>> queryProcessorCounters() {
        return ProcessorInformation.IS_WIN7_OR_GREATER ? PerfCounterWildcardQuery.queryInstancesAndValues(ProcessorTickCountProperty.class, "Processor Information", "Win32_PerfRawData_Counters_ProcessorInformation WHERE NOT Name LIKE \"%_Total\"") : PerfCounterWildcardQuery.queryInstancesAndValues(ProcessorTickCountProperty.class, "Processor", "Win32_PerfRawData_PerfOS_Processor WHERE Name!=\"_Total\"");
    }
    
    public static Map<InterruptsProperty, Long> queryInterruptCounters() {
        return PerfCounterQuery.queryValues(InterruptsProperty.class, "Processor", "Win32_PerfRawData_PerfOS_Processor WHERE Name=\"_Total\"");
    }
    
    public static Pair<List<String>, Map<ProcessorFrequencyProperty, List<Long>>> queryFrequencyCounters() {
        return PerfCounterWildcardQuery.queryInstancesAndValues(ProcessorFrequencyProperty.class, "Processor Information", "Win32_PerfRawData_Counters_ProcessorInformation WHERE NOT Name LIKE \"%_Total\"");
    }
    
    static {
        IS_WIN7_OR_GREATER = VersionHelpers.IsWindows7OrGreater();
    }
    
    public enum ProcessorTickCountProperty implements PerfCounterWildcardQuery.PdhCounterWildcardProperty
    {
        NAME("^*_Total"), 
        PERCENTDPCTIME("% DPC Time"), 
        PERCENTINTERRUPTTIME("% Interrupt Time"), 
        PERCENTPRIVILEGEDTIME("% Privileged Time"), 
        PERCENTPROCESSORTIME("% Processor Time"), 
        PERCENTUSERTIME("% User Time");
        
        private final String counter;
        
        private ProcessorTickCountProperty(final String counter) {
            this.counter = counter;
        }
        
        @Override
        public String getCounter() {
            return this.counter;
        }
        
        private static /* synthetic */ ProcessorTickCountProperty[] $values() {
            return new ProcessorTickCountProperty[] { ProcessorTickCountProperty.NAME, ProcessorTickCountProperty.PERCENTDPCTIME, ProcessorTickCountProperty.PERCENTINTERRUPTTIME, ProcessorTickCountProperty.PERCENTPRIVILEGEDTIME, ProcessorTickCountProperty.PERCENTPROCESSORTIME, ProcessorTickCountProperty.PERCENTUSERTIME };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    public enum InterruptsProperty implements PerfCounterQuery.PdhCounterProperty
    {
        INTERRUPTSPERSEC("_Total", "Interrupts/sec");
        
        private final String instance;
        private final String counter;
        
        private InterruptsProperty(final String instance, final String counter) {
            this.instance = instance;
            this.counter = counter;
        }
        
        @Override
        public String getInstance() {
            return this.instance;
        }
        
        @Override
        public String getCounter() {
            return this.counter;
        }
        
        private static /* synthetic */ InterruptsProperty[] $values() {
            return new InterruptsProperty[] { InterruptsProperty.INTERRUPTSPERSEC };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    public enum ProcessorFrequencyProperty implements PerfCounterWildcardQuery.PdhCounterWildcardProperty
    {
        NAME("^*_Total"), 
        PERCENTOFMAXIMUMFREQUENCY("% of Maximum Frequency");
        
        private final String counter;
        
        private ProcessorFrequencyProperty(final String counter) {
            this.counter = counter;
        }
        
        @Override
        public String getCounter() {
            return this.counter;
        }
        
        private static /* synthetic */ ProcessorFrequencyProperty[] $values() {
            return new ProcessorFrequencyProperty[] { ProcessorFrequencyProperty.NAME, ProcessorFrequencyProperty.PERCENTOFMAXIMUMFREQUENCY };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
