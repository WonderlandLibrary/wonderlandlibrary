// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.perfmon;

import oshi.util.platform.windows.PerfCounterQuery;
import java.util.Map;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class MemoryInformation
{
    private static final String MEMORY = "Memory";
    private static final String WIN32_PERF_RAW_DATA_PERF_OS_MEMORY = "Win32_PerfRawData_PerfOS_Memory";
    
    private MemoryInformation() {
    }
    
    public static Map<PageSwapProperty, Long> queryPageSwaps() {
        return PerfCounterQuery.queryValues(PageSwapProperty.class, "Memory", "Win32_PerfRawData_PerfOS_Memory");
    }
    
    public enum PageSwapProperty implements PerfCounterQuery.PdhCounterProperty
    {
        PAGESINPUTPERSEC((String)null, "Pages Input/sec"), 
        PAGESOUTPUTPERSEC((String)null, "Pages Output/sec");
        
        private final String instance;
        private final String counter;
        
        private PageSwapProperty(final String instance, final String counter) {
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
        
        private static /* synthetic */ PageSwapProperty[] $values() {
            return new PageSwapProperty[] { PageSwapProperty.PAGESINPUTPERSEC, PageSwapProperty.PAGESOUTPUTPERSEC };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
