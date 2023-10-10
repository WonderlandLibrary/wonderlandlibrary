// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.internal;

import java.util.LinkedList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class Lists
{
    public static <K> List<K> newArrayList() {
        return new ArrayList<K>();
    }
    
    public static <K> List<K> newArrayList(final Collection<K> c) {
        return new ArrayList<K>((Collection<? extends K>)c);
    }
    
    public static <K> List<K> newArrayList(final K... a) {
        return new ArrayList<K>((Collection<? extends K>)Arrays.asList(a));
    }
    
    public static <K> List<K> newArrayList(final int initialCapacity) {
        return new ArrayList<K>(initialCapacity);
    }
    
    public static <K> LinkedList<K> newLinkedList() {
        return new LinkedList<K>();
    }
    
    public static <K> LinkedList<K> newLinkedList(final Collection<K> c) {
        return new LinkedList<K>((Collection<? extends K>)c);
    }
}
