// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.convert;

import java.util.Iterator;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.sql.Timestamp;
import java.sql.Time;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.invoke.MethodHandle;
import java.util.Date;
import java.util.Map;

public final class DateTypeConverter
{
    private static final Map<Class<? extends Date>, MethodHandle> CONSTRUCTORS;
    
    public static <D extends Date> D fromMillis(final long millis, final Class<D> type) {
        try {
            return (D)DateTypeConverter.CONSTRUCTORS.get(type).invoke(millis);
        }
        catch (Throwable ignored) {
            return null;
        }
    }
    
    private DateTypeConverter() {
    }
    
    static {
        CONSTRUCTORS = new ConcurrentHashMap<Class<? extends Date>, MethodHandle>();
        final MethodHandles.Lookup lookup = MethodHandles.publicLookup();
        for (final Class<? extends Date> dateClass : Arrays.asList(Date.class, java.sql.Date.class, Time.class, Timestamp.class)) {
            try {
                DateTypeConverter.CONSTRUCTORS.put(dateClass, lookup.findConstructor(dateClass, MethodType.methodType(Void.TYPE, Long.TYPE)));
            }
            catch (NoSuchMethodException ex) {}
            catch (IllegalAccessException ex2) {}
        }
    }
}
