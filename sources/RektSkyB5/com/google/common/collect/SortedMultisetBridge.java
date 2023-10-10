/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.collect;

import com.google.common.collect.Multiset;
import java.util.SortedSet;

interface SortedMultisetBridge<E>
extends Multiset<E> {
    @Override
    public SortedSet<E> elementSet();
}

