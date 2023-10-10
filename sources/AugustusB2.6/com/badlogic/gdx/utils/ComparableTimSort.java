// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.utils;

import java.io.Closeable;

public class ComparableTimSort
{
    private Object[] a;
    private int minGallop;
    private Object[] tmp;
    private int tmpCount;
    private int stackSize;
    private final int[] runBase;
    private final int[] runLen;
    
    ComparableTimSort() {
        this.minGallop = 7;
        this.stackSize = 0;
        this.tmp = new Object[256];
        this.runBase = new int[40];
        this.runLen = new int[40];
    }
    
    public final void doSort(final Object[] a, int lo, final int hi) {
        this.stackSize = 0;
        final int length = a.length;
        final int n2 = lo;
        final int n3 = length;
        if (n2 > hi) {
            throw new IllegalArgumentException("fromIndex(" + n2 + ") > toIndex(" + hi + ")");
        }
        if (n2 < 0) {
            throw new ArrayIndexOutOfBoundsException(n2);
        }
        if (hi > n3) {
            throw new ArrayIndexOutOfBoundsException(hi);
        }
        int nRemaining;
        if ((nRemaining = hi - lo) < 2) {
            return;
        }
        if (nRemaining < 32) {
            final int initRunLen = countRunAndMakeAscending(a, lo, hi);
            binarySort(a, lo, hi, lo + initRunLen);
            return;
        }
        this.a = a;
        this.tmpCount = 0;
        final int minRun = minRunLength(nRemaining);
        int runLen;
        do {
            if ((runLen = countRunAndMakeAscending(a, lo, hi)) < minRun) {
                final int force = (nRemaining <= minRun) ? nRemaining : minRun;
                final int lo2 = lo;
                binarySort(a, lo2, lo2 + force, lo + runLen);
                runLen = force;
            }
            final int n4 = lo;
            final int n5 = runLen;
            this.runBase[this.stackSize] = n4;
            this.runLen[this.stackSize] = n5;
            ++this.stackSize;
            this.mergeCollapse();
            lo += runLen;
        } while ((nRemaining -= runLen) != 0);
        this.mergeForceCollapse();
        this.a = null;
        final Object[] tmp = this.tmp;
        for (int i = 0, n = this.tmpCount; i < n; ++i) {
            tmp[i] = null;
        }
    }
    
    private static void binarySort(final Object[] a, final int lo, final int hi, int start) {
        if (start == lo) {
            ++start;
        }
        while (start < hi) {
            final Comparable<Object> pivot = (Comparable<Object>)a[start];
            int left = lo;
            int right = start;
            while (left < right) {
                final int mid = left + right >>> 1;
                if (pivot.compareTo(a[mid]) < 0) {
                    right = mid;
                }
                else {
                    left = mid + 1;
                }
            }
            final int n;
            switch (n = start - left) {
                case 2: {
                    a[left + 2] = a[left + 1];
                }
                case 1: {
                    a[left + 1] = a[left];
                    break;
                }
                default: {
                    System.arraycopy(a, left, a, left + 1, n);
                    break;
                }
            }
            a[left] = pivot;
            ++start;
        }
    }
    
    private static int countRunAndMakeAscending(Object[] a, final int lo, int hi) {
        int runHi;
        if ((runHi = lo + 1) == hi) {
            return 1;
        }
        if (((Comparable)a[runHi++]).compareTo(a[lo]) < 0) {
            while (runHi < hi && ((Comparable)a[runHi]).compareTo(a[runHi - 1]) < 0) {
                ++runHi;
            }
            final Object[] array = a;
            int n = runHi;
            hi = lo;
            a = array;
            --n;
            while (hi < n) {
                final Object o = a[hi];
                a[hi++] = a[n];
                a[n--] = o;
            }
        }
        else {
            while (runHi < hi && ((Comparable)a[runHi]).compareTo(a[runHi - 1]) >= 0) {
                ++runHi;
            }
        }
        return runHi - lo;
    }
    
    private static int minRunLength(int n) {
        int r = 0;
        while (n >= 32) {
            r |= (n & 0x1);
            n >>= 1;
        }
        return n + r;
    }
    
    private void mergeCollapse() {
        while (this.stackSize > 1) {
            int n;
            if ((n = this.stackSize - 2) > 0 && this.runLen[n - 1] <= this.runLen[n] + this.runLen[n + 1]) {
                if (this.runLen[n - 1] < this.runLen[n + 1]) {
                    --n;
                }
                this.mergeAt(n);
            }
            else {
                if (this.runLen[n] > this.runLen[n + 1]) {
                    break;
                }
                this.mergeAt(n);
            }
        }
    }
    
    private void mergeForceCollapse() {
        while (this.stackSize > 1) {
            int n;
            if ((n = this.stackSize - 2) > 0 && this.runLen[n - 1] < this.runLen[n + 1]) {
                --n;
            }
            this.mergeAt(n);
        }
    }
    
    private void mergeAt(int i) {
        int base1 = this.runBase[i];
        int len1 = this.runLen[i];
        final int base2 = this.runBase[i + 1];
        int len2 = this.runLen[i + 1];
        this.runLen[i] = len1 + len2;
        if (i == this.stackSize - 3) {
            this.runBase[i + 1] = this.runBase[i + 2];
            this.runLen[i + 1] = this.runLen[i + 2];
        }
        --this.stackSize;
        i = gallopRight((Comparable<Object>)this.a[base2], this.a, base1, len1, 0);
        base1 += i;
        if ((len1 -= i) == 0) {
            return;
        }
        final Comparable key = (Comparable)this.a[base1 + len1 - 1];
        final Object[] a = this.a;
        final int base3 = base2;
        final int len3 = len2;
        if ((len2 = gallopLeft(key, a, base3, len3, len3 - 1)) == 0) {
            return;
        }
        if (len1 <= len2) {
            this.mergeLo(base1, len1, base2, len2);
            return;
        }
        this.mergeHi(base1, len1, base2, len2);
    }
    
    private static int gallopLeft(final Comparable<Object> key, final Object[] a, final int base, int len, final int hint) {
        int lastOfs = 0;
        int ofs = 1;
        if (key.compareTo(a[base + hint]) > 0) {
            for (len -= hint; ofs < len && key.compareTo(a[base + hint + ofs]) > 0; ofs = len) {
                lastOfs = ofs;
                if ((ofs = (ofs << 1) + 1) <= 0) {}
            }
            if (ofs > len) {
                ofs = len;
            }
            lastOfs += hint;
            ofs += hint;
        }
        else {
            for (len = hint + 1; ofs < len && key.compareTo(a[base + hint - ofs]) <= 0; ofs = len) {
                lastOfs = ofs;
                if ((ofs = (ofs << 1) + 1) <= 0) {}
            }
            if (ofs > len) {
                ofs = len;
            }
            len = lastOfs;
            lastOfs = hint - ofs;
            ofs = hint - len;
        }
        ++lastOfs;
        while (lastOfs < ofs) {
            len = lastOfs + (ofs - lastOfs >>> 1);
            if (key.compareTo(a[base + len]) > 0) {
                lastOfs = len + 1;
            }
            else {
                ofs = len;
            }
        }
        return ofs;
    }
    
    private static int gallopRight(final Comparable<Object> key, final Object[] a, final int base, int len, final int hint) {
        int ofs = 1;
        int lastOfs = 0;
        if (key.compareTo(a[base + hint]) < 0) {
            for (len = hint + 1; ofs < len && key.compareTo(a[base + hint - ofs]) < 0; ofs = len) {
                lastOfs = ofs;
                if ((ofs = (ofs << 1) + 1) <= 0) {}
            }
            if (ofs > len) {
                ofs = len;
            }
            len = lastOfs;
            lastOfs = hint - ofs;
            ofs = hint - len;
        }
        else {
            for (len -= hint; ofs < len && key.compareTo(a[base + hint + ofs]) >= 0; ofs = len) {
                lastOfs = ofs;
                if ((ofs = (ofs << 1) + 1) <= 0) {}
            }
            if (ofs > len) {
                ofs = len;
            }
            lastOfs += hint;
            ofs += hint;
        }
        ++lastOfs;
        while (lastOfs < ofs) {
            len = lastOfs + (ofs - lastOfs >>> 1);
            if (key.compareTo(a[base + len]) < 0) {
                ofs = len;
            }
            else {
                lastOfs = len + 1;
            }
        }
        return ofs;
    }
    
    private void mergeLo(int base1, int len1, int base2, int len2) {
        final Object[] a = this.a;
        final Object[] tmp = this.ensureCapacity(len1);
        System.arraycopy(a, base1, tmp, 0, len1);
        int cursor1 = 0;
        base2 = base2;
        base1 = base1;
        a[base1++] = a[base2++];
        if (--len2 == 0) {
            System.arraycopy(tmp, 0, a, base1, len1);
            return;
        }
        if (len1 == 1) {
            System.arraycopy(a, base2, a, base1, len2);
            a[base1 + len2] = tmp[0];
            return;
        }
        int minGallop = this.minGallop;
    Label_0394:
        while (true) {
            int count1 = 0;
            int count2 = 0;
            do {
                if (((Comparable)a[base2]).compareTo(tmp[cursor1]) < 0) {
                    a[base1++] = a[base2++];
                    ++count2;
                    count1 = 0;
                    if (--len2 == 0) {
                        break Label_0394;
                    }
                    continue;
                }
                else {
                    a[base1++] = tmp[cursor1++];
                    ++count1;
                    count2 = 0;
                    if (--len1 != 1) {
                        continue;
                    }
                    break Label_0394;
                }
            } while ((count1 | count2) < minGallop);
            do {
                if ((count1 = gallopRight((Comparable<Object>)a[base2], tmp, cursor1, len1, 0)) != 0) {
                    System.arraycopy(tmp, cursor1, a, base1, count1);
                    base1 += count1;
                    cursor1 += count1;
                    if ((len1 -= count1) <= 1) {
                        break Label_0394;
                    }
                }
                a[base1++] = a[base2++];
                if (--len2 == 0) {
                    break Label_0394;
                }
                if ((count2 = gallopLeft((Comparable<Object>)tmp[cursor1], a, base2, len2, 0)) != 0) {
                    System.arraycopy(a, base2, a, base1, count2);
                    base1 += count2;
                    base2 += count2;
                    if ((len2 -= count2) == 0) {
                        break Label_0394;
                    }
                }
                a[base1++] = tmp[cursor1++];
                if (--len1 == 1) {
                    break Label_0394;
                }
                --minGallop;
            } while (count1 >= 7 | count2 >= 7);
            if (minGallop < 0) {
                minGallop = 0;
            }
            minGallop += 2;
        }
        this.minGallop = ((minGallop <= 0) ? 1 : minGallop);
        if (len1 == 1) {
            System.arraycopy(a, base2, a, base1, len2);
            a[base1 + len2] = tmp[cursor1];
            return;
        }
        if (len1 == 0) {
            throw new IllegalArgumentException("Comparison method violates its general contract!");
        }
        System.arraycopy(tmp, cursor1, a, base1, len1);
    }
    
    private void mergeHi(final int base1, int len1, int base2, int len2) {
        final Object[] a = this.a;
        final Object[] tmp = this.ensureCapacity(len2);
        System.arraycopy(a, base2, tmp, 0, len2);
        int cursor1 = base1 + len1 - 1;
        int cursor2 = len2 - 1;
        base2 = base2 + len2 - 1;
        a[base2--] = a[cursor1--];
        if (--len1 == 0) {
            System.arraycopy(tmp, 0, a, base2 - (len2 - 1), len2);
            return;
        }
        if (len2 == 1) {
            base2 -= len1;
            cursor1 -= len1;
            System.arraycopy(a, cursor1 + 1, a, base2 + 1, len1);
            a[base2] = tmp[cursor2];
            return;
        }
        int minGallop = this.minGallop;
    Label_0452:
        while (true) {
            int count1 = 0;
            int count2 = 0;
            do {
                if (((Comparable)tmp[cursor2]).compareTo(a[cursor1]) < 0) {
                    a[base2--] = a[cursor1--];
                    ++count1;
                    count2 = 0;
                    if (--len1 == 0) {
                        break Label_0452;
                    }
                    continue;
                }
                else {
                    a[base2--] = tmp[cursor2--];
                    ++count2;
                    count1 = 0;
                    if (--len2 != 1) {
                        continue;
                    }
                    break Label_0452;
                }
            } while ((count1 | count2) < minGallop);
            do {
                final int n = len1;
                final Comparable key = (Comparable)tmp[cursor2];
                final Object[] a2 = a;
                final int len3 = len1;
                if ((count1 = n - gallopRight(key, a2, base1, len3, len3 - 1)) != 0) {
                    base2 -= count1;
                    cursor1 -= count1;
                    len1 -= count1;
                    System.arraycopy(a, cursor1 + 1, a, base2 + 1, count1);
                    if (len1 == 0) {
                        break Label_0452;
                    }
                }
                a[base2--] = tmp[cursor2--];
                if (--len2 == 1) {
                    break Label_0452;
                }
                final int n2 = len2;
                final Comparable key2 = (Comparable)a[cursor1];
                final Object[] a3 = tmp;
                final int base3 = 0;
                final int len4 = len2;
                if ((count2 = n2 - gallopLeft(key2, a3, base3, len4, len4 - 1)) != 0) {
                    base2 -= count2;
                    cursor2 -= count2;
                    len2 -= count2;
                    System.arraycopy(tmp, cursor2 + 1, a, base2 + 1, count2);
                    if (len2 <= 1) {
                        break Label_0452;
                    }
                }
                a[base2--] = a[cursor1--];
                if (--len1 == 0) {
                    break Label_0452;
                }
                --minGallop;
            } while (count1 >= 7 | count2 >= 7);
            if (minGallop < 0) {
                minGallop = 0;
            }
            minGallop += 2;
        }
        this.minGallop = ((minGallop <= 0) ? 1 : minGallop);
        if (len2 == 1) {
            base2 -= len1;
            cursor1 -= len1;
            System.arraycopy(a, cursor1 + 1, a, base2 + 1, len1);
            a[base2] = tmp[cursor2];
            return;
        }
        if (len2 == 0) {
            throw new IllegalArgumentException("Comparison method violates its general contract!");
        }
        System.arraycopy(tmp, 0, a, base2 - (len2 - 1), len2);
    }
    
    private Object[] ensureCapacity(final int minCapacity) {
        this.tmpCount = Math.max(this.tmpCount, minCapacity);
        if (this.tmp.length < minCapacity) {
            int newSize = minCapacity;
            newSize = ((newSize = ((newSize = ((newSize = ((newSize = (minCapacity | newSize >> 1)) | newSize >> 2)) | newSize >> 4)) | newSize >> 8)) | newSize >> 16);
            if (++newSize < 0) {
                newSize = minCapacity;
            }
            else {
                newSize = Math.min(newSize, this.a.length >>> 1);
            }
            final Object[] newArray = new Object[newSize];
            this.tmp = newArray;
        }
        return this.tmp;
    }
    
    public static void closeQuietly(final Closeable c) {
        try {
            c.close();
        }
        catch (Throwable t) {}
    }
}
