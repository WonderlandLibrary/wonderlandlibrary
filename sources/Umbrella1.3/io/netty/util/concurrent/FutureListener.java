/*
 * Decompiled with CFR 0.150.
 */
package io.netty.util.concurrent;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public interface FutureListener<V>
extends GenericFutureListener<Future<V>> {
}

