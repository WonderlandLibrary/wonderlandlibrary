// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.Iterator;
import java.util.Comparator;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
interface SortedIterable<T> extends Iterable<T>
{
    Comparator<? super T> comparator();
    
    Iterator<T> iterator();
}
