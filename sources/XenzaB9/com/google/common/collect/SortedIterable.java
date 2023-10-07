// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
interface SortedIterable<T> extends Iterable<T>
{
    Comparator<? super T> comparator();
    
    Iterator<T> iterator();
}
