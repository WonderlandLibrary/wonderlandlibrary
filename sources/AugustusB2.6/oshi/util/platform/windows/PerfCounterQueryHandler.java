// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util.platform.windows;

import org.slf4j.LoggerFactory;
import oshi.util.FormatUtil;
import java.util.Iterator;
import java.util.HashMap;
import com.sun.jna.platform.win32.WinNT;
import java.util.Map;
import org.slf4j.Logger;
import oshi.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public final class PerfCounterQueryHandler implements AutoCloseable
{
    private static final Logger LOG;
    private Map<PerfDataUtil.PerfCounter, WinNT.HANDLEByReference> counterHandleMap;
    private WinNT.HANDLEByReference queryHandle;
    
    public PerfCounterQueryHandler() {
        this.counterHandleMap = new HashMap<PerfDataUtil.PerfCounter, WinNT.HANDLEByReference>();
        this.queryHandle = null;
    }
    
    public boolean addCounterToQuery(final PerfDataUtil.PerfCounter counter) {
        if (this.queryHandle == null) {
            this.queryHandle = new WinNT.HANDLEByReference();
            if (!PerfDataUtil.openQuery(this.queryHandle)) {
                PerfCounterQueryHandler.LOG.warn("Failed to open a query for PDH counter: {}", counter.getCounterPath());
                this.queryHandle = null;
                return false;
            }
        }
        final WinNT.HANDLEByReference p = new WinNT.HANDLEByReference();
        if (!PerfDataUtil.addCounter(this.queryHandle, counter.getCounterPath(), p)) {
            PerfCounterQueryHandler.LOG.warn("Failed to add counter for PDH counter: {}", counter.getCounterPath());
            return false;
        }
        this.counterHandleMap.put(counter, p);
        return true;
    }
    
    public boolean removeCounterFromQuery(final PerfDataUtil.PerfCounter counter) {
        boolean success = false;
        final WinNT.HANDLEByReference href = this.counterHandleMap.remove(counter);
        if (href != null) {
            success = PerfDataUtil.removeCounter(href);
        }
        if (this.counterHandleMap.isEmpty()) {
            PerfDataUtil.closeQuery(this.queryHandle);
            this.queryHandle = null;
        }
        return success;
    }
    
    public void removeAllCounters() {
        for (final WinNT.HANDLEByReference href : this.counterHandleMap.values()) {
            PerfDataUtil.removeCounter(href);
        }
        this.counterHandleMap.clear();
        if (this.queryHandle != null) {
            PerfDataUtil.closeQuery(this.queryHandle);
        }
        this.queryHandle = null;
    }
    
    public long updateQuery() {
        if (this.queryHandle == null) {
            PerfCounterQueryHandler.LOG.warn("Query does not exist to update.");
            return 0L;
        }
        return PerfDataUtil.updateQueryTimestamp(this.queryHandle);
    }
    
    public long queryCounter(final PerfDataUtil.PerfCounter counter) {
        if (!this.counterHandleMap.containsKey(counter)) {
            if (PerfCounterQueryHandler.LOG.isWarnEnabled()) {
                PerfCounterQueryHandler.LOG.warn("Counter {} does not exist to query.", counter.getCounterPath());
            }
            return 0L;
        }
        final long value = PerfDataUtil.queryCounter(this.counterHandleMap.get(counter));
        if (value < 0L) {
            if (PerfCounterQueryHandler.LOG.isWarnEnabled()) {
                PerfCounterQueryHandler.LOG.warn("Error querying counter {}: {}", counter.getCounterPath(), String.format(FormatUtil.formatError((int)value), new Object[0]));
            }
            return 0L;
        }
        return value;
    }
    
    @Override
    public void close() {
        this.removeAllCounters();
    }
    
    static {
        LOG = LoggerFactory.getLogger(PerfCounterQueryHandler.class);
    }
}
