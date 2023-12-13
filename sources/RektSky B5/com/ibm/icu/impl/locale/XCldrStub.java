/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.locale;

import com.ibm.icu.util.ICUException;
import com.ibm.icu.util.ICUUncheckedIOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XCldrStub {
    public static <T> String join(T[] source, String separator) {
        StringBuilder result = new StringBuilder();
        for (int i2 = 0; i2 < source.length; ++i2) {
            if (i2 != 0) {
                result.append(separator);
            }
            result.append(source[i2]);
        }
        return result.toString();
    }

    public static <T> String join(Iterable<T> source, String separator) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (T item : source) {
            if (!first) {
                result.append(separator);
            } else {
                first = false;
            }
            result.append(item.toString());
        }
        return result.toString();
    }

    public static interface Predicate<T> {
        public boolean test(T var1);
    }

    public static class RegexUtilities {
        public static int findMismatch(Matcher m2, CharSequence s2) {
            boolean matches;
            int i2;
            for (i2 = 1; i2 < s2.length() && ((matches = m2.reset(s2.subSequence(0, i2)).matches()) || m2.hitEnd()); ++i2) {
            }
            return i2 - 1;
        }

        public static String showMismatch(Matcher m2, CharSequence s2) {
            int failPoint = RegexUtilities.findMismatch(m2, s2);
            String show = s2.subSequence(0, failPoint) + "\u2639" + s2.subSequence(failPoint, s2.length());
            return show;
        }
    }

    public static class FileUtilities {
        public static final Charset UTF8 = Charset.forName("utf-8");

        public static BufferedReader openFile(Class<?> class1, String file) {
            return FileUtilities.openFile(class1, file, UTF8);
        }

        public static BufferedReader openFile(Class<?> class1, String file, Charset charset) {
            try {
                InputStream resourceAsStream = class1.getResourceAsStream(file);
                if (charset == null) {
                    charset = UTF8;
                }
                InputStreamReader reader = new InputStreamReader(resourceAsStream, charset);
                BufferedReader bufferedReader = new BufferedReader(reader, 65536);
                return bufferedReader;
            }
            catch (Exception e2) {
                String className = class1 == null ? null : class1.getCanonicalName();
                String canonicalName = null;
                try {
                    String relativeFileName = FileUtilities.getRelativeFileName(class1, "../util/");
                    canonicalName = new File(relativeFileName).getCanonicalPath();
                }
                catch (Exception e1) {
                    throw new ICUUncheckedIOException("Couldn't open file: " + file + "; relative to class: " + className, e2);
                }
                throw new ICUUncheckedIOException("Couldn't open file " + file + "; in path " + canonicalName + "; relative to class: " + className, e2);
            }
        }

        public static String getRelativeFileName(Class<?> class1, String filename) {
            URL resource = class1 == null ? FileUtilities.class.getResource(filename) : class1.getResource(filename);
            String resourceString = resource.toString();
            if (resourceString.startsWith("file:")) {
                return resourceString.substring(5);
            }
            if (resourceString.startsWith("jar:file:")) {
                return resourceString.substring(9);
            }
            throw new ICUUncheckedIOException("File not found: " + resourceString);
        }
    }

    public static class ImmutableMultimap {
        public static <K, V> Multimap<K, V> copyOf(Multimap<K, V> values) {
            LinkedHashMap<K, Set<V>> temp = new LinkedHashMap<K, Set<V>>();
            for (Map.Entry<K, Set<V>> entry : values.asMap().entrySet()) {
                Set<V> value = entry.getValue();
                temp.put(entry.getKey(), value.size() == 1 ? Collections.singleton(value.iterator().next()) : Collections.unmodifiableSet(new LinkedHashSet<V>(value)));
            }
            return new Multimap(Collections.unmodifiableMap(temp), null);
        }
    }

    public static class ImmutableMap {
        public static <K, V> Map<K, V> copyOf(Map<K, V> values) {
            return Collections.unmodifiableMap(new LinkedHashMap<K, V>(values));
        }
    }

    public static class ImmutableSet {
        public static <T> Set<T> copyOf(Set<T> values) {
            return Collections.unmodifiableSet(new LinkedHashSet<T>(values));
        }
    }

    public static class Splitter {
        Pattern pattern;
        boolean trimResults = false;

        public Splitter(char c2) {
            this(Pattern.compile("\\Q" + c2 + "\\E"));
        }

        public Splitter(Pattern p2) {
            this.pattern = p2;
        }

        public static Splitter on(char c2) {
            return new Splitter(c2);
        }

        public static Splitter on(Pattern p2) {
            return new Splitter(p2);
        }

        public List<String> splitToList(String input) {
            String[] items = this.pattern.split(input);
            if (this.trimResults) {
                for (int i2 = 0; i2 < items.length; ++i2) {
                    items[i2] = items[i2].trim();
                }
            }
            return Arrays.asList(items);
        }

        public Splitter trimResults() {
            this.trimResults = true;
            return this;
        }

        public Iterable<String> split(String input) {
            return this.splitToList(input);
        }
    }

    public static class Joiner {
        private final String separator;

        private Joiner(String separator) {
            this.separator = separator;
        }

        public static final Joiner on(String separator) {
            return new Joiner(separator);
        }

        public <T> String join(T[] source) {
            return XCldrStub.join(source, this.separator);
        }

        public <T> String join(Iterable<T> source) {
            return XCldrStub.join(source, this.separator);
        }
    }

    public static class CollectionUtilities {
        public static <T, U extends Iterable<T>> String join(U source, String separator) {
            return XCldrStub.join(source, separator);
        }
    }

    public static class LinkedHashMultimap<K, V>
    extends Multimap<K, V> {
        private LinkedHashMultimap() {
            super(new LinkedHashMap(), LinkedHashSet.class);
        }

        public static <K, V> LinkedHashMultimap<K, V> create() {
            return new LinkedHashMultimap<K, V>();
        }
    }

    public static class TreeMultimap<K, V>
    extends Multimap<K, V> {
        private TreeMultimap() {
            super(new TreeMap(), TreeSet.class);
        }

        public static <K, V> TreeMultimap<K, V> create() {
            return new TreeMultimap<K, V>();
        }
    }

    public static class HashMultimap<K, V>
    extends Multimap<K, V> {
        private HashMultimap() {
            super(new HashMap(), HashSet.class);
        }

        public static <K, V> HashMultimap<K, V> create() {
            return new HashMultimap<K, V>();
        }
    }

    private static class ReusableEntry<K, V>
    implements Map.Entry<K, V> {
        K key;
        V value;

        private ReusableEntry() {
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }
    }

    private static class MultimapIterator<K, V>
    implements Iterator<Map.Entry<K, V>>,
    Iterable<Map.Entry<K, V>> {
        private final Iterator<Map.Entry<K, Set<V>>> it1;
        private Iterator<V> it2 = null;
        private final ReusableEntry<K, V> entry = new ReusableEntry();

        private MultimapIterator(Map<K, Set<V>> map) {
            this.it1 = map.entrySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return this.it1.hasNext() || this.it2 != null && this.it2.hasNext();
        }

        @Override
        public Map.Entry<K, V> next() {
            if (this.it2 != null && this.it2.hasNext()) {
                this.entry.value = this.it2.next();
            } else {
                Map.Entry<K, Set<V>> e2 = this.it1.next();
                this.entry.key = e2.getKey();
                this.it2 = e2.getValue().iterator();
            }
            return this.entry;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return this;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static class Multimaps {
        public static <K, V, R extends Multimap<K, V>> R invertFrom(Multimap<V, K> source, R target) {
            for (Map.Entry<V, Set<K>> entry : source.asMap().entrySet()) {
                target.putAll((Collection)entry.getValue(), entry.getKey());
            }
            return target;
        }

        public static <K, V, R extends Multimap<K, V>> R invertFrom(Map<V, K> source, R target) {
            for (Map.Entry<V, K> entry : source.entrySet()) {
                target.put(entry.getValue(), entry.getKey());
            }
            return target;
        }

        public static <K, V> Map<K, V> forMap(Map<K, V> map) {
            return map;
        }
    }

    public static class Multimap<K, V> {
        private final Map<K, Set<V>> map;
        private final Class<Set<V>> setClass;

        private Multimap(Map<K, Set<V>> map, Class<?> setClass) {
            this.map = map;
            this.setClass = setClass != null ? setClass : HashSet.class;
        }

        public Multimap<K, V> putAll(K key, V ... values) {
            if (values.length != 0) {
                this.createSetIfMissing(key).addAll(Arrays.asList(values));
            }
            return this;
        }

        public void putAll(K key, Collection<V> values) {
            if (!values.isEmpty()) {
                this.createSetIfMissing(key).addAll(values);
            }
        }

        public void putAll(Collection<K> keys, V value) {
            for (K key : keys) {
                this.put(key, value);
            }
        }

        public void putAll(Multimap<K, V> source) {
            for (Map.Entry<K, Set<V>> entry : source.map.entrySet()) {
                this.putAll(entry.getKey(), (Collection)entry.getValue());
            }
        }

        public void put(K key, V value) {
            this.createSetIfMissing(key).add(value);
        }

        private Set<V> createSetIfMissing(K key) {
            Set<V> old = this.map.get(key);
            if (old == null) {
                old = this.getInstance();
                this.map.put(key, old);
            }
            return old;
        }

        private Set<V> getInstance() {
            try {
                return this.setClass.newInstance();
            }
            catch (Exception e2) {
                throw new ICUException(e2);
            }
        }

        public Set<V> get(K key) {
            Set<V> result = this.map.get(key);
            return result;
        }

        public Set<K> keySet() {
            return this.map.keySet();
        }

        public Map<K, Set<V>> asMap() {
            return this.map;
        }

        public Set<V> values() {
            Collection<Set<V>> values = this.map.values();
            if (values.size() == 0) {
                return Collections.emptySet();
            }
            Set<V> result = this.getInstance();
            for (Set<V> valueSet : values) {
                result.addAll(valueSet);
            }
            return result;
        }

        public int size() {
            return this.map.size();
        }

        public Iterable<Map.Entry<K, V>> entries() {
            return new MultimapIterator(this.map);
        }

        public boolean equals(Object obj) {
            return this == obj || obj != null && obj.getClass() == this.getClass() && this.map.equals(((Multimap)obj).map);
        }

        public int hashCode() {
            return this.map.hashCode();
        }
    }
}

