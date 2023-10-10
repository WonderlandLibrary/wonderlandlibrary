// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import java.util.SortedSet;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
interface SortedMultisetBridge<E> extends Multiset<E>
{
    SortedSet<E> elementSet();
}
