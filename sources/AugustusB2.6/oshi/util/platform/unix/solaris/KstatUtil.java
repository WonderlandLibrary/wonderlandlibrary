// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util.platform.unix.solaris;

import oshi.util.Util;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import oshi.jna.platform.unix.Kstat2StatusException;
import java.util.Arrays;
import oshi.jna.platform.unix.Kstat2;
import com.sun.jna.Pointer;
import oshi.util.FormatUtil;
import com.sun.jna.Native;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.ReentrantLock;
import com.sun.jna.platform.unix.solaris.LibKstat;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class KstatUtil
{
    private static final Logger LOG;
    private static final LibKstat KS;
    private static final LibKstat.KstatCtl KC;
    public static final ReentrantLock CHAIN;
    
    private KstatUtil() {
    }
    
    public static KstatChain openChain() {
        return new KstatChain();
    }
    
    public static String dataLookupString(final LibKstat.Kstat ksp, final String name) {
        if (ksp.ks_type != 1 && ksp.ks_type != 4) {
            throw new IllegalArgumentException("Not a kstat_named or kstat_timer kstat.");
        }
        final Pointer p = KstatUtil.KS.kstat_data_lookup(ksp, name);
        if (p == null) {
            KstatUtil.LOG.debug("Failed to lookup kstat value for key {}", name);
            return "";
        }
        final LibKstat.KstatNamed data = new LibKstat.KstatNamed(p);
        switch (data.data_type) {
            case 0: {
                return Native.toString(data.value.charc, StandardCharsets.UTF_8);
            }
            case 1: {
                return Integer.toString(data.value.i32);
            }
            case 2: {
                return FormatUtil.toUnsignedString(data.value.ui32);
            }
            case 3: {
                return Long.toString(data.value.i64);
            }
            case 4: {
                return FormatUtil.toUnsignedString(data.value.ui64);
            }
            case 9: {
                return data.value.str.addr.getString(0L);
            }
            default: {
                KstatUtil.LOG.error("Unimplemented kstat data type {}", (Object)data.data_type);
                return "";
            }
        }
    }
    
    public static long dataLookupLong(final LibKstat.Kstat ksp, final String name) {
        if (ksp.ks_type != 1 && ksp.ks_type != 4) {
            throw new IllegalArgumentException("Not a kstat_named or kstat_timer kstat.");
        }
        final Pointer p = KstatUtil.KS.kstat_data_lookup(ksp, name);
        if (p == null) {
            if (KstatUtil.LOG.isDebugEnabled()) {
                KstatUtil.LOG.debug("Failed lo lookup kstat value on {}:{}:{} for key {}", Native.toString(ksp.ks_module, StandardCharsets.US_ASCII), ksp.ks_instance, Native.toString(ksp.ks_name, StandardCharsets.US_ASCII), name);
            }
            return 0L;
        }
        final LibKstat.KstatNamed data = new LibKstat.KstatNamed(p);
        switch (data.data_type) {
            case 1: {
                return data.value.i32;
            }
            case 2: {
                return FormatUtil.getUnsignedInt(data.value.ui32);
            }
            case 3: {
                return data.value.i64;
            }
            case 4: {
                return data.value.ui64;
            }
            default: {
                KstatUtil.LOG.error("Unimplemented or non-numeric kstat data type {}", (Object)data.data_type);
                return 0L;
            }
        }
    }
    
    public static Object[] queryKstat2(final String mapStr, final String... names) {
        final Object[] result = new Object[names.length];
        final Kstat2.Kstat2MatcherList matchers = new Kstat2.Kstat2MatcherList();
        KstatUtil.CHAIN.lock();
        try {
            matchers.addMatcher(0, mapStr);
            final Kstat2.Kstat2Handle handle = new Kstat2.Kstat2Handle();
            try {
                final Kstat2.Kstat2Map map = handle.lookupMap(mapStr);
                for (int i = 0; i < names.length; ++i) {
                    result[i] = map.getValue(names[i]);
                }
            }
            finally {
                handle.close();
            }
        }
        catch (Kstat2StatusException e) {
            KstatUtil.LOG.debug("Failed to get stats on {} for names {}: {}", mapStr, Arrays.toString(names), e.getMessage());
        }
        finally {
            KstatUtil.CHAIN.unlock();
            matchers.free();
        }
        return result;
    }
    
    public static List<Object[]> queryKstat2List(final String beforeStr, final String afterStr, final String... names) {
        final List<Object[]> results = new ArrayList<Object[]>();
        int s = 0;
        final Kstat2.Kstat2MatcherList matchers = new Kstat2.Kstat2MatcherList();
        KstatUtil.CHAIN.lock();
        try {
            matchers.addMatcher(1, beforeStr + "*" + afterStr);
            final Kstat2.Kstat2Handle handle = new Kstat2.Kstat2Handle();
            try {
                for (s = 0; s < Integer.MAX_VALUE; ++s) {
                    final Object[] result = new Object[names.length];
                    final Kstat2.Kstat2Map map = handle.lookupMap(beforeStr + s + afterStr);
                    for (int i = 0; i < names.length; ++i) {
                        result[i] = map.getValue(names[i]);
                    }
                    results.add(result);
                }
            }
            finally {
                handle.close();
            }
        }
        catch (Kstat2StatusException e) {
            KstatUtil.LOG.debug("Failed to get stats on {}{}{} for names {}: {}", beforeStr, s, afterStr, Arrays.toString(names), e.getMessage());
        }
        finally {
            KstatUtil.CHAIN.unlock();
            matchers.free();
        }
        return results;
    }
    
    static {
        LOG = LoggerFactory.getLogger(KstatUtil.class);
        KS = LibKstat.INSTANCE;
        KC = KstatUtil.KS.kstat_open();
        CHAIN = new ReentrantLock();
    }
    
    public static final class KstatChain implements AutoCloseable
    {
        private KstatChain() {
            KstatUtil.CHAIN.lock();
            update();
        }
        
        public static boolean read(final LibKstat.Kstat ksp) {
            int retry = 0;
            while (0 > KstatUtil.KS.kstat_read(KstatUtil.KC, ksp, null)) {
                if (11 != Native.getLastError() || 5 <= ++retry) {
                    if (KstatUtil.LOG.isDebugEnabled()) {
                        KstatUtil.LOG.debug("Failed to read kstat {}:{}:{}", Native.toString(ksp.ks_module, StandardCharsets.US_ASCII), ksp.ks_instance, Native.toString(ksp.ks_name, StandardCharsets.US_ASCII));
                    }
                    return false;
                }
                Util.sleep(8 << retry);
            }
            return true;
        }
        
        public static LibKstat.Kstat lookup(final String module, final int instance, final String name) {
            return KstatUtil.KS.kstat_lookup(KstatUtil.KC, module, instance, name);
        }
        
        public static List<LibKstat.Kstat> lookupAll(final String module, final int instance, final String name) {
            final List<LibKstat.Kstat> kstats = new ArrayList<LibKstat.Kstat>();
            for (LibKstat.Kstat ksp = KstatUtil.KS.kstat_lookup(KstatUtil.KC, module, instance, name); ksp != null; ksp = ksp.next()) {
                if ((module == null || module.equals(Native.toString(ksp.ks_module, StandardCharsets.US_ASCII))) && (instance < 0 || instance == ksp.ks_instance) && (name == null || name.equals(Native.toString(ksp.ks_name, StandardCharsets.US_ASCII)))) {
                    kstats.add(ksp);
                }
            }
            return kstats;
        }
        
        public static int update() {
            return KstatUtil.KS.kstat_chain_update(KstatUtil.KC);
        }
        
        @Override
        public void close() {
            KstatUtil.CHAIN.unlock();
        }
    }
}
