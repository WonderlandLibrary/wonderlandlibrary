/*
 * Decompiled with CFR 0.152.
 */
package org.reflections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.reflections.ReflectionsException;
import org.reflections.util.Utils;

public class Store {
    private final ConcurrentHashMap<String, Map<String, Collection<String>>> storeMap = new ConcurrentHashMap();

    protected Store() {
    }

    public Set<String> keySet() {
        return this.storeMap.keySet();
    }

    private Map<String, Collection<String>> get(String index) {
        Map<String, Collection<String>> mmap = this.storeMap.get(index);
        if (mmap == null) {
            throw new ReflectionsException("Scanner " + index + " was not configured");
        }
        return mmap;
    }

    public Set<String> get(Class<?> scannerClass, String key) {
        return this.get(Utils.index(scannerClass), Collections.singletonList(key));
    }

    public Set<String> get(String index, String key) {
        return this.get(index, Collections.singletonList(key));
    }

    public Set<String> get(Class<?> scannerClass, Collection<String> keys) {
        return this.get(Utils.index(scannerClass), keys);
    }

    private Set<String> get(String index, Collection<String> keys) {
        Map<String, Collection<String>> mmap = this.get(index);
        LinkedHashSet<String> result = new LinkedHashSet<String>();
        for (String key : keys) {
            Collection<String> values = mmap.get(key);
            if (values == null) continue;
            result.addAll(values);
        }
        return result;
    }

    public Set<String> getAllIncluding(Class<?> scannerClass, Collection<String> keys) {
        String index = Utils.index(scannerClass);
        Map<String, Collection<String>> mmap = this.get(index);
        ArrayList<String> workKeys = new ArrayList<String>(keys);
        HashSet<String> result = new HashSet<String>();
        for (int i2 = 0; i2 < workKeys.size(); ++i2) {
            Collection<String> values;
            String key = (String)workKeys.get(i2);
            if (!result.add(key) || (values = mmap.get(key)) == null) continue;
            workKeys.addAll(values);
        }
        return result;
    }

    public Set<String> getAll(Class<?> scannerClass, String key) {
        return this.getAllIncluding(scannerClass, this.get(scannerClass, key));
    }

    public Set<String> getAll(Class<?> scannerClass, Collection<String> keys) {
        return this.getAllIncluding(scannerClass, this.get(scannerClass, keys));
    }

    public Set<String> keys(String index) {
        Map<String, Collection<String>> map = this.storeMap.get(index);
        return map != null ? new HashSet<String>(map.keySet()) : Collections.emptySet();
    }

    public Set<String> values(String index) {
        Map<String, Collection<String>> map = this.storeMap.get(index);
        return map != null ? map.values().stream().flatMap(Collection::stream).collect(Collectors.toSet()) : Collections.emptySet();
    }

    public boolean put(Class<?> scannerClass, String key, String value) {
        return this.put(Utils.index(scannerClass), key, value);
    }

    public boolean put(String index, String key, String value) {
        return this.storeMap.computeIfAbsent(index, s2 -> new ConcurrentHashMap()).computeIfAbsent(key, s2 -> new ArrayList()).add(value);
    }

    void merge(Store store) {
        if (store != null) {
            for (String indexName : store.keySet()) {
                Map<String, Collection<String>> index = store.get(indexName);
                if (index == null) continue;
                for (String key : index.keySet()) {
                    for (String string : index.get(key)) {
                        this.put(indexName, key, string);
                    }
                }
            }
        }
    }
}

