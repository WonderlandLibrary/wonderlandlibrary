/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util;

import io.netty.util.UniqueName;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.ConcurrentMap;

public final class AttributeKey<T>
extends UniqueName {
    private static final ConcurrentMap<String, Boolean> names = PlatformDependent.newConcurrentHashMap();

    @Deprecated
    public AttributeKey(String string) {
        super(names, string, new Object[0]);
    }

    public static <T> AttributeKey<T> valueOf(String string) {
        return new AttributeKey<T>(string);
    }
}

