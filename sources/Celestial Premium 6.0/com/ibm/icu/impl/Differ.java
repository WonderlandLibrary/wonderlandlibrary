/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class Differ<T> {
    private int STACKSIZE;
    private int EQUALSIZE;
    private T[] a;
    private T[] b;
    private T last = null;
    private T next = null;
    private int aCount = 0;
    private int bCount = 0;
    private int aLine = 1;
    private int bLine = 1;
    private int maxSame = 0;
    private int aTop = 0;
    private int bTop = 0;

    public Differ(int stackSize, int matchCount) {
        this.STACKSIZE = stackSize;
        this.EQUALSIZE = matchCount;
        this.a = new Object[stackSize + matchCount];
        this.b = new Object[stackSize + matchCount];
    }

    public void add(T aStr, T bStr) {
        this.addA(aStr);
        this.addB(bStr);
    }

    public void addA(T aStr) {
        this.flush();
        this.a[this.aCount++] = aStr;
    }

    public void addB(T bStr) {
        this.flush();
        this.b[this.bCount++] = bStr;
    }

    public int getALine(int offset) {
        return this.aLine + this.maxSame + offset;
    }

    public T getA(int offset) {
        if (offset < 0) {
            return this.last;
        }
        if (offset > this.aTop - this.maxSame) {
            return this.next;
        }
        return this.a[offset];
    }

    public int getACount() {
        return this.aTop - this.maxSame;
    }

    public int getBCount() {
        return this.bTop - this.maxSame;
    }

    public int getBLine(int offset) {
        return this.bLine + this.maxSame + offset;
    }

    public T getB(int offset) {
        if (offset < 0) {
            return this.last;
        }
        if (offset > this.bTop - this.maxSame) {
            return this.next;
        }
        return this.b[offset];
    }

    public void checkMatch(boolean finalPass) {
        int i;
        int max = this.aCount;
        if (max > this.bCount) {
            max = this.bCount;
        }
        for (i = 0; i < max && this.a[i].equals(this.b[i]); ++i) {
        }
        this.aTop = this.bTop = (this.maxSame = i);
        if (this.maxSame > 0) {
            this.last = this.a[this.maxSame - 1];
        }
        this.next = null;
        if (finalPass) {
            this.aTop = this.aCount;
            this.bTop = this.bCount;
            this.next = null;
            return;
        }
        if (this.aCount - this.maxSame < this.EQUALSIZE || this.bCount - this.maxSame < this.EQUALSIZE) {
            return;
        }
        int match = this.find(this.a, this.aCount - this.EQUALSIZE, this.aCount, this.b, this.maxSame, this.bCount);
        if (match != -1) {
            this.aTop = this.aCount - this.EQUALSIZE;
            this.bTop = match;
            this.next = this.a[this.aTop];
            return;
        }
        match = this.find(this.b, this.bCount - this.EQUALSIZE, this.bCount, this.a, this.maxSame, this.aCount);
        if (match != -1) {
            this.bTop = this.bCount - this.EQUALSIZE;
            this.aTop = match;
            this.next = this.b[this.bTop];
            return;
        }
        if (this.aCount >= this.STACKSIZE || this.bCount >= this.STACKSIZE) {
            this.aCount = (this.aCount + this.maxSame) / 2;
            this.bCount = (this.bCount + this.maxSame) / 2;
            this.next = null;
        }
    }

    public int find(T[] aArr, int aStart, int aEnd, T[] bArr, int bStart, int bEnd) {
        int len = aEnd - aStart;
        int bEndMinus = bEnd - len;
        block0: for (int i = bStart; i <= bEndMinus; ++i) {
            for (int j = 0; j < len; ++j) {
                if (!bArr[i + j].equals(aArr[aStart + j])) continue block0;
            }
            return i;
        }
        return -1;
    }

    private void flush() {
        int newCount;
        if (this.aTop != 0) {
            newCount = this.aCount - this.aTop;
            System.arraycopy(this.a, this.aTop, this.a, 0, newCount);
            this.aCount = newCount;
            this.aLine += this.aTop;
            this.aTop = 0;
        }
        if (this.bTop != 0) {
            newCount = this.bCount - this.bTop;
            System.arraycopy(this.b, this.bTop, this.b, 0, newCount);
            this.bCount = newCount;
            this.bLine += this.bTop;
            this.bTop = 0;
        }
    }
}

