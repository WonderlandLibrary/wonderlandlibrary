// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.Map;
import me.gong.mcleaks.util.google.common.base.Predicate;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
interface FilteredMultimap<K, V> extends Multimap<K, V>
{
    Multimap<K, V> unfiltered();
    
    Predicate<? super Map.Entry<K, V>> entryPredicate();
}
