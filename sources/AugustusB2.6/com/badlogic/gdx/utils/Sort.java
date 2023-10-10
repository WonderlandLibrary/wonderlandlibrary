// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.utils;

import java.util.Comparator;

public final class Sort
{
    private static Sort instance;
    private TimSort timSort;
    private ComparableTimSort comparableTimSort;
    
    public final void sort(final Object[] a, final int fromIndex, final int toIndex) {
        if (this.comparableTimSort == null) {
            this.comparableTimSort = new ComparableTimSort();
        }
        this.comparableTimSort.doSort(a, 0, toIndex);
    }
    
    public final <T> void sort(final Array<T> a, final Comparator<? super T> c) {
        if (this.timSort == null) {
            this.timSort = new TimSort();
        }
        this.timSort.doSort(a.items, c, 0, a.size);
    }
    
    public final <T> void sort(final T[] a, final Comparator<? super T> c, final int fromIndex, final int toIndex) {
        if (this.timSort == null) {
            this.timSort = new TimSort();
        }
        this.timSort.doSort(a, c, 0, toIndex);
    }
    
    public static Sort instance() {
        if (Sort.instance == null) {
            Sort.instance = new Sort();
        }
        return Sort.instance;
    }
}
