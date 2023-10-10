// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util.platform.windows;

import oshi.annotation.concurrent.Immutable;
import com.sun.jna.platform.win32.VersionHelpers;
import org.slf4j.LoggerFactory;
import oshi.util.ParseUtil;
import oshi.util.FormatUtil;
import oshi.util.Util;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Pdh;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.BaseTSD;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class PerfDataUtil
{
    private static final Logger LOG;
    private static final BaseTSD.DWORD_PTR PZERO;
    private static final WinDef.DWORDByReference PDH_FMT_RAW;
    private static final Pdh PDH;
    private static final boolean IS_VISTA_OR_GREATER;
    
    private PerfDataUtil() {
    }
    
    public static PerfCounter createCounter(final String object, final String instance, final String counter) {
        return new PerfCounter(object, instance, counter);
    }
    
    public static long updateQueryTimestamp(final WinNT.HANDLEByReference query) {
        final WinDef.LONGLONGByReference pllTimeStamp = new WinDef.LONGLONGByReference();
        int ret = PerfDataUtil.IS_VISTA_OR_GREATER ? PerfDataUtil.PDH.PdhCollectQueryDataWithTime(query.getValue(), pllTimeStamp) : PerfDataUtil.PDH.PdhCollectQueryData(query.getValue());
        for (int retries = 0; ret == -2147481643 && retries++ < 3; ret = (PerfDataUtil.IS_VISTA_OR_GREATER ? PerfDataUtil.PDH.PdhCollectQueryDataWithTime(query.getValue(), pllTimeStamp) : PerfDataUtil.PDH.PdhCollectQueryData(query.getValue()))) {
            Util.sleep(1 << retries);
        }
        if (ret != 0) {
            if (PerfDataUtil.LOG.isWarnEnabled()) {
                PerfDataUtil.LOG.warn("Failed to update counter. Error code: {}", String.format(FormatUtil.formatError(ret), new Object[0]));
            }
            return 0L;
        }
        return PerfDataUtil.IS_VISTA_OR_GREATER ? ParseUtil.filetimeToUtcMs(pllTimeStamp.getValue().longValue(), true) : System.currentTimeMillis();
    }
    
    public static boolean openQuery(final WinNT.HANDLEByReference q) {
        final int ret = PerfDataUtil.PDH.PdhOpenQuery(null, PerfDataUtil.PZERO, q);
        if (ret != 0) {
            if (PerfDataUtil.LOG.isErrorEnabled()) {
                PerfDataUtil.LOG.error("Failed to open PDH Query. Error code: {}", String.format(FormatUtil.formatError(ret), new Object[0]));
            }
            return false;
        }
        return true;
    }
    
    public static boolean closeQuery(final WinNT.HANDLEByReference q) {
        return 0 == PerfDataUtil.PDH.PdhCloseQuery(q.getValue());
    }
    
    public static long queryCounter(final WinNT.HANDLEByReference counter) {
        final Pdh.PDH_RAW_COUNTER counterValue = new Pdh.PDH_RAW_COUNTER();
        final int ret = PerfDataUtil.PDH.PdhGetRawCounterValue(counter.getValue(), PerfDataUtil.PDH_FMT_RAW, counterValue);
        if (ret != 0) {
            if (PerfDataUtil.LOG.isWarnEnabled()) {
                PerfDataUtil.LOG.warn("Failed to get counter. Error code: {}", String.format(FormatUtil.formatError(ret), new Object[0]));
            }
            return ret;
        }
        return counterValue.FirstValue;
    }
    
    public static boolean addCounter(final WinNT.HANDLEByReference query, final String path, final WinNT.HANDLEByReference p) {
        final int ret = PerfDataUtil.IS_VISTA_OR_GREATER ? PerfDataUtil.PDH.PdhAddEnglishCounter(query.getValue(), path, PerfDataUtil.PZERO, p) : PerfDataUtil.PDH.PdhAddCounter(query.getValue(), path, PerfDataUtil.PZERO, p);
        if (ret != 0) {
            if (PerfDataUtil.LOG.isWarnEnabled()) {
                PerfDataUtil.LOG.warn("Failed to add PDH Counter: {}, Error code: {}", path, String.format(FormatUtil.formatError(ret), new Object[0]));
            }
            return false;
        }
        return true;
    }
    
    public static boolean removeCounter(final WinNT.HANDLEByReference p) {
        return 0 == PerfDataUtil.PDH.PdhRemoveCounter(p.getValue());
    }
    
    static {
        LOG = LoggerFactory.getLogger(PerfDataUtil.class);
        PZERO = new BaseTSD.DWORD_PTR(0L);
        PDH_FMT_RAW = new WinDef.DWORDByReference(new WinDef.DWORD(16L));
        PDH = Pdh.INSTANCE;
        IS_VISTA_OR_GREATER = VersionHelpers.IsWindowsVistaOrGreater();
    }
    
    @Immutable
    public static class PerfCounter
    {
        private String object;
        private String instance;
        private String counter;
        
        public PerfCounter(final String objectName, final String instanceName, final String counterName) {
            this.object = objectName;
            this.instance = instanceName;
            this.counter = counterName;
        }
        
        public String getObject() {
            return this.object;
        }
        
        public String getInstance() {
            return this.instance;
        }
        
        public String getCounter() {
            return this.counter;
        }
        
        public String getCounterPath() {
            final StringBuilder sb = new StringBuilder();
            sb.append('\\').append(this.object);
            if (this.instance != null) {
                sb.append('(').append(this.instance).append(')');
            }
            sb.append('\\').append(this.counter);
            return sb.toString();
        }
    }
}
