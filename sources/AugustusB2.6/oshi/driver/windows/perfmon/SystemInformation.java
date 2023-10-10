// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.perfmon;

import oshi.util.platform.windows.PerfCounterQuery;
import java.util.Map;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class SystemInformation
{
    private static final String SYSTEM = "System";
    private static final String WIN32_PERF_RAW_DATA_PERF_OS_SYSTEM = "Win32_PerfRawData_PerfOS_System";
    
    private SystemInformation() {
    }
    
    public static Map<ContextSwitchProperty, Long> queryContextSwitchCounters() {
        return PerfCounterQuery.queryValues(ContextSwitchProperty.class, "System", "Win32_PerfRawData_PerfOS_System");
    }
    
    public enum ContextSwitchProperty implements PerfCounterQuery.PdhCounterProperty
    {
        CONTEXTSWITCHESPERSEC((String)null, "Context Switches/sec");
        
        private final String instance;
        private final String counter;
        
        private ContextSwitchProperty(final String instance, final String counter) {
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
        
        private static /* synthetic */ ContextSwitchProperty[] $values() {
            return new ContextSwitchProperty[] { ContextSwitchProperty.CONTEXTSWITCHESPERSEC };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
