/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import io.netty.util.internal.IntegerHolder;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.ThreadLocalRandom;
import io.netty.util.internal.TypeParameterMatcher;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

class UnpaddedInternalThreadLocalMap {
    StringBuilder stringBuilder;
    Map<Charset, CharsetDecoder> charsetDecoderCache;
    static final AtomicInteger nextIndex = new AtomicInteger();
    ThreadLocalRandom random;
    Object[] indexedVariables;
    int futureListenerStackDepth;
    static ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap;
    Map<Class<?>, Boolean> handlerSharableCache;
    Map<Class<?>, TypeParameterMatcher> typeParameterMatcherGetCache;
    int localChannelReaderStackDepth;
    Map<Charset, CharsetEncoder> charsetEncoderCache;
    Map<Class<?>, Map<String, TypeParameterMatcher>> typeParameterMatcherFindCache;
    IntegerHolder counterHashCode;

    UnpaddedInternalThreadLocalMap(Object[] objectArray) {
        this.indexedVariables = objectArray;
    }
}

