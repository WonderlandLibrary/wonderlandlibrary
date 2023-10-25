/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import io.netty.util.concurrent.FastThreadLocalThread;
import io.netty.util.internal.IntegerHolder;
import io.netty.util.internal.ThreadLocalRandom;
import io.netty.util.internal.TypeParameterMatcher;
import io.netty.util.internal.UnpaddedInternalThreadLocalMap;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.WeakHashMap;

public final class InternalThreadLocalMap
extends UnpaddedInternalThreadLocalMap {
    public long rp5;
    public long rp1;
    public long rp9;
    public long rp8;
    public static final Object UNSET = new Object();
    public long rp4;
    public long rp3;
    public long rp2;
    public long rp7;
    public long rp6;

    public IntegerHolder counterHashCode() {
        return this.counterHashCode;
    }

    public Map<Class<?>, Boolean> handlerSharableCache() {
        WeakHashMap weakHashMap = this.handlerSharableCache;
        if (weakHashMap == null) {
            this.handlerSharableCache = weakHashMap = new WeakHashMap(4);
        }
        return weakHashMap;
    }

    public ThreadLocalRandom random() {
        ThreadLocalRandom threadLocalRandom = this.random;
        if (threadLocalRandom == null) {
            this.random = threadLocalRandom = new ThreadLocalRandom();
        }
        return threadLocalRandom;
    }

    public StringBuilder stringBuilder() {
        StringBuilder stringBuilder = this.stringBuilder;
        if (stringBuilder == null) {
            this.stringBuilder = stringBuilder = new StringBuilder(512);
        } else {
            stringBuilder.setLength(0);
        }
        return stringBuilder;
    }

    public Map<Class<?>, Map<String, TypeParameterMatcher>> typeParameterMatcherFindCache() {
        IdentityHashMap identityHashMap = this.typeParameterMatcherFindCache;
        if (identityHashMap == null) {
            this.typeParameterMatcherFindCache = identityHashMap = new IdentityHashMap();
        }
        return identityHashMap;
    }

    public boolean setIndexedVariable(int n, Object object) {
        Object[] objectArray = this.indexedVariables;
        if (n < objectArray.length) {
            Object object2 = objectArray[n];
            objectArray[n] = object;
            return object2 == UNSET;
        }
        this.expandIndexedVariableTableAndSet(n, object);
        return true;
    }

    public static void remove() {
        Thread thread = Thread.currentThread();
        if (thread instanceof FastThreadLocalThread) {
            ((FastThreadLocalThread)thread).setThreadLocalMap(null);
        } else {
            ThreadLocal<InternalThreadLocalMap> threadLocal = UnpaddedInternalThreadLocalMap.slowThreadLocalMap;
            if (threadLocal != null) {
                threadLocal.remove();
            }
        }
    }

    public void setCounterHashCode(IntegerHolder integerHolder) {
        this.counterHashCode = integerHolder;
    }

    public boolean isIndexedVariableSet(int n) {
        Object[] objectArray = this.indexedVariables;
        return n < objectArray.length && objectArray[n] != UNSET;
    }

    private static Object[] newIndexedVariableTable() {
        Object[] objectArray = new Object[32];
        Arrays.fill(objectArray, UNSET);
        return objectArray;
    }

    private static InternalThreadLocalMap slowGet() {
        InternalThreadLocalMap internalThreadLocalMap;
        ThreadLocal<InternalThreadLocalMap> threadLocal = UnpaddedInternalThreadLocalMap.slowThreadLocalMap;
        if (threadLocal == null) {
            threadLocal = new ThreadLocal();
            UnpaddedInternalThreadLocalMap.slowThreadLocalMap = threadLocal;
        }
        if ((internalThreadLocalMap = threadLocal.get()) == null) {
            internalThreadLocalMap = new InternalThreadLocalMap();
            threadLocal.set(internalThreadLocalMap);
        }
        return internalThreadLocalMap;
    }

    public static InternalThreadLocalMap get() {
        Thread thread = Thread.currentThread();
        if (thread instanceof FastThreadLocalThread) {
            return InternalThreadLocalMap.fastGet((FastThreadLocalThread)thread);
        }
        return InternalThreadLocalMap.slowGet();
    }

    public int localChannelReaderStackDepth() {
        return this.localChannelReaderStackDepth;
    }

    public int futureListenerStackDepth() {
        return this.futureListenerStackDepth;
    }

    public static InternalThreadLocalMap getIfSet() {
        ThreadLocal<InternalThreadLocalMap> threadLocal;
        Thread thread = Thread.currentThread();
        Object object = thread instanceof FastThreadLocalThread ? ((FastThreadLocalThread)thread).threadLocalMap() : ((threadLocal = UnpaddedInternalThreadLocalMap.slowThreadLocalMap) == null ? null : threadLocal.get());
        return object;
    }

    public void setFutureListenerStackDepth(int n) {
        this.futureListenerStackDepth = n;
    }

    public Object removeIndexedVariable(int n) {
        Object[] objectArray = this.indexedVariables;
        if (n < objectArray.length) {
            Object object = objectArray[n];
            objectArray[n] = UNSET;
            return object;
        }
        return UNSET;
    }

    public static void destroy() {
        slowThreadLocalMap = null;
    }

    public Map<Class<?>, TypeParameterMatcher> typeParameterMatcherGetCache() {
        IdentityHashMap identityHashMap = this.typeParameterMatcherGetCache;
        if (identityHashMap == null) {
            this.typeParameterMatcherGetCache = identityHashMap = new IdentityHashMap();
        }
        return identityHashMap;
    }

    public void setLocalChannelReaderStackDepth(int n) {
        this.localChannelReaderStackDepth = n;
    }

    private InternalThreadLocalMap() {
        super(InternalThreadLocalMap.newIndexedVariableTable());
    }

    public Object indexedVariable(int n) {
        Object[] objectArray = this.indexedVariables;
        return n < objectArray.length ? objectArray[n] : UNSET;
    }

    private static InternalThreadLocalMap fastGet(FastThreadLocalThread fastThreadLocalThread) {
        InternalThreadLocalMap internalThreadLocalMap = fastThreadLocalThread.threadLocalMap();
        if (internalThreadLocalMap == null) {
            internalThreadLocalMap = new InternalThreadLocalMap();
            fastThreadLocalThread.setThreadLocalMap(internalThreadLocalMap);
        }
        return internalThreadLocalMap;
    }

    public static int lastVariableIndex() {
        return nextIndex.get() - 1;
    }

    public Map<Charset, CharsetDecoder> charsetDecoderCache() {
        IdentityHashMap identityHashMap = this.charsetDecoderCache;
        if (identityHashMap == null) {
            this.charsetDecoderCache = identityHashMap = new IdentityHashMap();
        }
        return identityHashMap;
    }

    public int size() {
        int n = 0;
        if (this.futureListenerStackDepth != 0) {
            ++n;
        }
        if (this.localChannelReaderStackDepth != 0) {
            ++n;
        }
        if (this.handlerSharableCache != null) {
            ++n;
        }
        if (this.counterHashCode != null) {
            ++n;
        }
        if (this.random != null) {
            ++n;
        }
        if (this.typeParameterMatcherGetCache != null) {
            ++n;
        }
        if (this.typeParameterMatcherFindCache != null) {
            ++n;
        }
        if (this.stringBuilder != null) {
            ++n;
        }
        if (this.charsetEncoderCache != null) {
            ++n;
        }
        if (this.charsetDecoderCache != null) {
            ++n;
        }
        for (Object object : this.indexedVariables) {
            if (object == UNSET) continue;
            ++n;
        }
        return n - 1;
    }

    private void expandIndexedVariableTableAndSet(int n, Object object) {
        Object[] objectArray = this.indexedVariables;
        int n2 = objectArray.length;
        int n3 = n;
        n3 |= n3 >>> 1;
        n3 |= n3 >>> 2;
        n3 |= n3 >>> 4;
        n3 |= n3 >>> 8;
        n3 |= n3 >>> 16;
        Object[] objectArray2 = Arrays.copyOf(objectArray, ++n3);
        Arrays.fill(objectArray2, n2, objectArray2.length, UNSET);
        objectArray2[n] = object;
        this.indexedVariables = objectArray2;
    }

    public static int nextVariableIndex() {
        int n = nextIndex.getAndIncrement();
        if (n < 0) {
            nextIndex.decrementAndGet();
            throw new IllegalStateException("too many thread-local indexed variables");
        }
        return n;
    }

    public Map<Charset, CharsetEncoder> charsetEncoderCache() {
        IdentityHashMap identityHashMap = this.charsetEncoderCache;
        if (identityHashMap == null) {
            this.charsetEncoderCache = identityHashMap = new IdentityHashMap();
        }
        return identityHashMap;
    }
}

