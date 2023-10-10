// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.logging.Level;
import com.google.common.base.Strings;
import java.util.Arrays;
import java.lang.reflect.Array;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.Map;
import java.util.logging.Logger;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
final class Platform
{
    private static final Logger logger;
    
    static <K, V> Map<K, V> newHashMapWithExpectedSize(final int expectedSize) {
        return (Map<K, V>)Maps.newHashMapWithExpectedSize(expectedSize);
    }
    
    static <K, V> Map<K, V> newLinkedHashMapWithExpectedSize(final int expectedSize) {
        return (Map<K, V>)Maps.newLinkedHashMapWithExpectedSize(expectedSize);
    }
    
    static <E> Set<E> newHashSetWithExpectedSize(final int expectedSize) {
        return (Set<E>)Sets.newHashSetWithExpectedSize(expectedSize);
    }
    
    static <E> Set<E> newConcurrentHashSet() {
        return (Set<E>)ConcurrentHashMap.newKeySet();
    }
    
    static <E> Set<E> newLinkedHashSetWithExpectedSize(final int expectedSize) {
        return (Set<E>)Sets.newLinkedHashSetWithExpectedSize(expectedSize);
    }
    
    static <K, V> Map<K, V> preservesInsertionOrderOnPutsMap() {
        return (Map<K, V>)Maps.newLinkedHashMap();
    }
    
    static <E> Set<E> preservesInsertionOrderOnAddsSet() {
        return (Set<E>)Sets.newLinkedHashSet();
    }
    
    static <T> T[] newArray(final T[] reference, final int length) {
        final Class<?> type = reference.getClass().getComponentType();
        final T[] result = (T[])Array.newInstance(type, length);
        return result;
    }
    
    static <T> T[] copy(final Object[] source, final int from, final int to, final T[] arrayOfType) {
        return Arrays.copyOfRange(source, from, to, (Class<? extends T[]>)arrayOfType.getClass());
    }
    
    static MapMaker tryWeakKeys(final MapMaker mapMaker) {
        return mapMaker.weakKeys();
    }
    
    static int reduceIterationsIfGwt(final int iterations) {
        return iterations;
    }
    
    static int reduceExponentIfGwt(final int exponent) {
        return exponent;
    }
    
    static void checkGwtRpcEnabled() {
        final String propertyName = "guava.gwt.emergency_reenable_rpc";
        if (!Boolean.parseBoolean(System.getProperty(propertyName, "false"))) {
            throw new UnsupportedOperationException(Strings.lenientFormat("We are removing GWT-RPC support for Guava types. You can temporarily reenable support by setting the system property %s to true. For more about system properties, see %s. For more about Guava's GWT-RPC support, see %s.", propertyName, "https://stackoverflow.com/q/5189914/28465", "https://groups.google.com/d/msg/guava-announce/zHZTFg7YF3o/rQNnwdHeEwAJ"));
        }
        Platform.logger.log(Level.WARNING, "Later in 2020, we will remove GWT-RPC support for Guava types. You are seeing this warning because you are sending a Guava type over GWT-RPC, which will break. You can identify which type by looking at the class name in the attached stack trace.", new Throwable());
    }
    
    private Platform() {
    }
    
    static {
        logger = Logger.getLogger(Platform.class.getName());
    }
}
