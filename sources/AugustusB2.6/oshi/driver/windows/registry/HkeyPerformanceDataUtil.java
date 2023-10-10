// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.registry;

import org.slf4j.LoggerFactory;
import java.util.Collections;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.Memory;
import oshi.util.tuples.Pair;
import oshi.util.platform.windows.PerfCounterWildcardQuery;
import java.util.EnumMap;
import java.util.ArrayList;
import java.util.HashMap;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinPerf;
import java.util.List;
import oshi.util.tuples.Triplet;
import java.util.Map;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class HkeyPerformanceDataUtil
{
    private static final Logger LOG;
    private static final String HKEY_PERFORMANCE_TEXT = "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Perflib\\009";
    private static final String COUNTER = "Counter";
    private static final Map<String, Integer> COUNTER_INDEX_MAP;
    
    private HkeyPerformanceDataUtil() {
    }
    
    public static <T extends Enum> Triplet<List<Map<T, Object>>, Long, Long> readPerfDataFromRegistry(final String objectName, final Class<T> counterEnum) {
        final Pair<Integer, EnumMap<T, Integer>> indices = getCounterIndices(objectName, counterEnum);
        if (indices == null) {
            return null;
        }
        final Memory pPerfData = readPerfDataBuffer(objectName);
        if (pPerfData == null) {
            return null;
        }
        final WinPerf.PERF_DATA_BLOCK perfData = new WinPerf.PERF_DATA_BLOCK(pPerfData.share(0L));
        final long perfTime100nSec = perfData.PerfTime100nSec.getValue();
        final long now = WinBase.FILETIME.filetimeToDate((int)(perfTime100nSec >> 32), (int)(perfTime100nSec & 0xFFFFFFFFL)).getTime();
        long perfObjectOffset = perfData.HeaderLength;
        for (int obj = 0; obj < perfData.NumObjectTypes; ++obj) {
            final WinPerf.PERF_OBJECT_TYPE perfObject = new WinPerf.PERF_OBJECT_TYPE(pPerfData.share(perfObjectOffset));
            if (perfObject.ObjectNameTitleIndex == HkeyPerformanceDataUtil.COUNTER_INDEX_MAP.get(objectName)) {
                long perfCounterOffset = perfObjectOffset + perfObject.HeaderLength;
                final Map<Integer, Integer> counterOffsetMap = new HashMap<Integer, Integer>();
                final Map<Integer, Integer> counterSizeMap = new HashMap<Integer, Integer>();
                for (int counter = 0; counter < perfObject.NumCounters; ++counter) {
                    final WinPerf.PERF_COUNTER_DEFINITION perfCounter = new WinPerf.PERF_COUNTER_DEFINITION(pPerfData.share(perfCounterOffset));
                    counterOffsetMap.put(perfCounter.CounterNameTitleIndex, perfCounter.CounterOffset);
                    counterSizeMap.put(perfCounter.CounterNameTitleIndex, perfCounter.CounterSize);
                    perfCounterOffset += perfCounter.ByteLength;
                }
                long perfInstanceOffset = perfObjectOffset + perfObject.DefinitionLength;
                final List<Map<T, Object>> counterMaps = new ArrayList<Map<T, Object>>(perfObject.NumInstances);
                for (int inst = 0; inst < perfObject.NumInstances; ++inst) {
                    final WinPerf.PERF_INSTANCE_DEFINITION perfInstance = new WinPerf.PERF_INSTANCE_DEFINITION(pPerfData.share(perfInstanceOffset));
                    final long perfCounterBlockOffset = perfInstanceOffset + perfInstance.ByteLength;
                    final Map<T, Object> counterMap = new EnumMap<T, Object>(counterEnum);
                    final T[] counterKeys = counterEnum.getEnumConstants();
                    counterMap.put(counterKeys[0], pPerfData.getWideString(perfInstanceOffset + perfInstance.NameOffset));
                    for (int i = 1; i < counterKeys.length; ++i) {
                        final T key = counterKeys[i];
                        final int keyIndex = HkeyPerformanceDataUtil.COUNTER_INDEX_MAP.get(((PerfCounterWildcardQuery.PdhCounterWildcardProperty)key).getCounter());
                        final int size = counterSizeMap.getOrDefault(keyIndex, 0);
                        if (size == 4) {
                            counterMap.put(key, pPerfData.getInt(perfCounterBlockOffset + counterOffsetMap.get(keyIndex)));
                        }
                        else {
                            if (size != 8) {
                                return null;
                            }
                            counterMap.put(key, pPerfData.getLong(perfCounterBlockOffset + counterOffsetMap.get(keyIndex)));
                        }
                    }
                    counterMaps.add(counterMap);
                    perfInstanceOffset = perfCounterBlockOffset + new WinPerf.PERF_COUNTER_BLOCK(pPerfData.share(perfCounterBlockOffset)).ByteLength;
                }
                return new Triplet<List<Map<T, Object>>, Long, Long>(counterMaps, perfTime100nSec, now);
            }
            perfObjectOffset += perfObject.TotalByteLength;
        }
        return null;
    }
    
    private static <T extends java.lang.Enum> Pair<Integer, EnumMap<T, Integer>> getCounterIndices(final String objectName, final Class<T> counterEnum) {
        if (!HkeyPerformanceDataUtil.COUNTER_INDEX_MAP.containsKey(objectName)) {
            HkeyPerformanceDataUtil.LOG.debug("Couldn't find counter index of {}.", objectName);
            return null;
        }
        final int counterIndex = HkeyPerformanceDataUtil.COUNTER_INDEX_MAP.get(objectName);
        final T[] enumConstants = counterEnum.getEnumConstants();
        final EnumMap<T, Integer> indexMap = new EnumMap<T, Integer>(counterEnum);
        for (int i = 1; i < enumConstants.length; ++i) {
            final T key = enumConstants[i];
            final String counterName = ((PerfCounterWildcardQuery.PdhCounterWildcardProperty)key).getCounter();
            if (!HkeyPerformanceDataUtil.COUNTER_INDEX_MAP.containsKey(counterName)) {
                HkeyPerformanceDataUtil.LOG.debug("Couldn't find counter index of {}.", counterName);
                return null;
            }
            indexMap.put(key, HkeyPerformanceDataUtil.COUNTER_INDEX_MAP.get(counterName));
        }
        return new Pair<Integer, EnumMap<T, Integer>>(counterIndex, indexMap);
    }
    
    private static Memory readPerfDataBuffer(final String objectName) {
        final String objectIndexStr = Integer.toString(HkeyPerformanceDataUtil.COUNTER_INDEX_MAP.get(objectName));
        int bufferSize = 4096;
        final IntByReference lpcbData = new IntByReference(bufferSize);
        Memory pPerfData = new Memory(bufferSize);
        int ret = Advapi32.INSTANCE.RegQueryValueEx(WinReg.HKEY_PERFORMANCE_DATA, objectIndexStr, 0, null, pPerfData, lpcbData);
        if (ret != 0 && ret != 234) {
            HkeyPerformanceDataUtil.LOG.error("Error reading performance data from registry for {}.", objectName);
            return null;
        }
        while (ret == 234) {
            bufferSize += 4096;
            lpcbData.setValue(bufferSize);
            pPerfData = new Memory(bufferSize);
            ret = Advapi32.INSTANCE.RegQueryValueEx(WinReg.HKEY_PERFORMANCE_DATA, objectIndexStr, 0, null, pPerfData, lpcbData);
        }
        return pPerfData;
    }
    
    private static Map<String, Integer> mapCounterIndicesFromRegistry() {
        final HashMap<String, Integer> indexMap = new HashMap<String, Integer>();
        try {
            final String[] counterText = Advapi32Util.registryGetStringArray(WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Perflib\\009", "Counter");
            for (int i = 1; i < counterText.length; i += 2) {
                indexMap.putIfAbsent(counterText[i], Integer.parseInt(counterText[i - 1]));
            }
        }
        catch (Win32Exception we) {
            HkeyPerformanceDataUtil.LOG.error("Unable to locate English counter names in registry Perflib 009. Counters may need to be rebuilt: ", we);
        }
        catch (NumberFormatException nfe) {
            HkeyPerformanceDataUtil.LOG.error("Unable to parse English counter names in registry Perflib 009.");
        }
        return Collections.unmodifiableMap((Map<? extends String, ? extends Integer>)indexMap);
    }
    
    static {
        LOG = LoggerFactory.getLogger(HkeyPerformanceDataUtil.class);
        COUNTER_INDEX_MAP = mapCounterIndicesFromRegistry();
    }
}
