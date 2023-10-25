/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.plugins.convert;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DateTypeConverter {
    private static final Map<Class<? extends Date>, MethodHandle> CONSTRUCTORS = new ConcurrentHashMap<Class<? extends Date>, MethodHandle>();

    public static <D extends Date> D fromMillis(long millis, Class<D> type) {
        try {
            return (D)CONSTRUCTORS.get(type).invoke(millis);
        }
        catch (Throwable ignored) {
            return null;
        }
    }

    private DateTypeConverter() {
    }

    static {
        MethodHandles.Lookup lookup = MethodHandles.publicLookup();
        for (Class dateClass : Arrays.asList(Date.class, java.sql.Date.class, Time.class, Timestamp.class)) {
            try {
                CONSTRUCTORS.put(dateClass, lookup.findConstructor(dateClass, MethodType.methodType(Void.TYPE, Long.TYPE)));
            }
            catch (IllegalAccessException | NoSuchMethodException reflectiveOperationException) {}
        }
    }
}

