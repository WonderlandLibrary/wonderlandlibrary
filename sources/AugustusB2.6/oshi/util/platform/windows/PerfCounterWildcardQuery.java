// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util.platform.windows;

import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.LoggerFactory;
import java.util.Objects;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.EnumMap;
import oshi.util.Util;
import java.util.Collections;
import com.sun.jna.platform.win32.PdhUtil;
import java.util.Map;
import java.util.List;
import oshi.util.tuples.Pair;
import java.util.Set;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class PerfCounterWildcardQuery
{
    private static final Logger LOG;
    private static final Set<String> FAILED_QUERY_CACHE;
    
    private PerfCounterWildcardQuery() {
    }
    
    public static <T extends Enum<T>> Pair<List<String>, Map<T, List<Long>>> queryInstancesAndValues(final Class<T> propertyEnum, final String perfObject, final String perfWmiClass) {
        if (!PerfCounterWildcardQuery.FAILED_QUERY_CACHE.contains(perfObject)) {
            final Pair<List<String>, Map<T, List<Long>>> instancesAndValuesMap = queryInstancesAndValuesFromPDH(propertyEnum, perfObject);
            if (!instancesAndValuesMap.getA().isEmpty()) {
                return instancesAndValuesMap;
            }
            PerfCounterWildcardQuery.LOG.warn("Disabling further attempts to query {}.", perfObject);
            PerfCounterWildcardQuery.FAILED_QUERY_CACHE.add(perfObject);
        }
        return queryInstancesAndValuesFromWMI(propertyEnum, perfWmiClass);
    }
    
    public static <T extends Enum<T>> Pair<List<String>, Map<T, List<Long>>> queryInstancesAndValuesFromPDH(final Class<T> propertyEnum, final String perfObject) {
        final T[] props = propertyEnum.getEnumConstants();
        if (props.length < 2) {
            throw new IllegalArgumentException("Enum " + propertyEnum.getName() + " must have at least two elements, an instance filter and a counter.");
        }
        final String instanceFilter = ((PdhCounterWildcardProperty)propertyEnum.getEnumConstants()[0]).getCounter().toLowerCase();
        final String perfObjectLocalized = PerfCounterQuery.localizeIfNeeded(perfObject);
        PdhUtil.PdhEnumObjectItems objectItems;
        try {
            objectItems = PdhUtil.PdhEnumObjectItems(null, null, perfObjectLocalized, 100);
        }
        catch (PdhUtil.PdhException e) {
            return new Pair<List<String>, Map<T, List<Long>>>(Collections.emptyList(), Collections.emptyMap());
        }
        final List<String> instances = objectItems.getInstances();
        instances.removeIf(i -> !Util.wildcardMatch(i.toLowerCase(), instanceFilter));
        final EnumMap<T, List<Long>> valuesMap = new EnumMap<T, List<Long>>(propertyEnum);
        final PerfCounterQueryHandler pdhQueryHandler = new PerfCounterQueryHandler();
        try {
            final EnumMap<T, List<PerfDataUtil.PerfCounter>> counterListMap = new EnumMap<T, List<PerfDataUtil.PerfCounter>>(propertyEnum);
            for (int j = 1; j < props.length; ++j) {
                final T prop = props[j];
                final List<PerfDataUtil.PerfCounter> counterList = new ArrayList<PerfDataUtil.PerfCounter>(instances.size());
                for (final String instance : instances) {
                    final PerfDataUtil.PerfCounter counter = PerfDataUtil.createCounter(perfObject, instance, ((PdhCounterWildcardProperty)prop).getCounter());
                    if (!pdhQueryHandler.addCounterToQuery(counter)) {
                        final Pair<List<String>, Map<T, List<Long>>> pair = new Pair<List<String>, Map<T, List<Long>>>(Collections.emptyList(), Collections.emptyMap());
                        pdhQueryHandler.close();
                        return pair;
                    }
                    counterList.add(counter);
                }
                counterListMap.put(prop, counterList);
            }
            if (0L < pdhQueryHandler.updateQuery()) {
                for (int j = 1; j < props.length; ++j) {
                    final T prop = props[j];
                    final List<Long> values = new ArrayList<Long>();
                    for (final PerfDataUtil.PerfCounter counter2 : counterListMap.get(prop)) {
                        values.add(pdhQueryHandler.queryCounter(counter2));
                    }
                    valuesMap.put(prop, values);
                }
            }
            pdhQueryHandler.close();
        }
        catch (Throwable t) {
            try {
                pdhQueryHandler.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
        return new Pair<List<String>, Map<T, List<Long>>>(instances, valuesMap);
    }
    
    public static <T extends Enum<T>> Pair<List<String>, Map<T, List<Long>>> queryInstancesAndValuesFromWMI(final Class<T> propertyEnum, final String wmiClass) {
        final List<String> instances = new ArrayList<String>();
        final EnumMap<T, List<Long>> valuesMap = new EnumMap<T, List<Long>>(propertyEnum);
        final WbemcliUtil.WmiQuery<T> query = new WbemcliUtil.WmiQuery<T>(wmiClass, propertyEnum);
        final WbemcliUtil.WmiResult<T> result = Objects.requireNonNull(WmiQueryHandler.createInstance()).queryWMI(query);
        if (result.getResultCount() > 0) {
            for (final T prop : propertyEnum.getEnumConstants()) {
                if (prop.ordinal() == 0) {
                    for (int i = 0; i < result.getResultCount(); ++i) {
                        instances.add(WmiUtil.getString(result, prop, i));
                    }
                }
                else {
                    final List<Long> values = new ArrayList<Long>();
                    for (int j = 0; j < result.getResultCount(); ++j) {
                        switch (result.getCIMType(prop)) {
                            case 18: {
                                values.add((long)WmiUtil.getUint16(result, prop, j));
                                break;
                            }
                            case 19: {
                                values.add(WmiUtil.getUint32asLong(result, prop, j));
                                break;
                            }
                            case 21: {
                                values.add(WmiUtil.getUint64(result, prop, j));
                                break;
                            }
                            case 101: {
                                values.add(WmiUtil.getDateTime(result, prop, j).toInstant().toEpochMilli());
                                break;
                            }
                            default: {
                                throw new ClassCastException("Unimplemented CIM Type Mapping.");
                            }
                        }
                    }
                    valuesMap.put(prop, values);
                }
            }
        }
        return new Pair<List<String>, Map<T, List<Long>>>(instances, valuesMap);
    }
    
    static {
        LOG = LoggerFactory.getLogger(PerfCounterWildcardQuery.class);
        FAILED_QUERY_CACHE = ConcurrentHashMap.newKeySet();
    }
    
    public interface PdhCounterWildcardProperty
    {
        String getCounter();
    }
}
