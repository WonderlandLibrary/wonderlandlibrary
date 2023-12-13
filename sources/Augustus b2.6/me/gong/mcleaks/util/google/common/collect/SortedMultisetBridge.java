// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.Set;
import java.util.SortedSet;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;

@GwtIncompatible
interface SortedMultisetBridge<E> extends Multiset<E>
{
    SortedSet<E> elementSet();
}
