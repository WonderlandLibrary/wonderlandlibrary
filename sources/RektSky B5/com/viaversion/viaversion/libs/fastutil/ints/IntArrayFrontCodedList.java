/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.libs.fastutil.ints.IntBigArrays
 *  com.viaversion.viaversion.libs.fastutil.longs.LongArrays
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.BigArrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrayList;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntBigArrays;
import com.viaversion.viaversion.libs.fastutil.longs.LongArrays;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectList;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectListIterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

public class IntArrayFrontCodedList
extends AbstractObjectList<int[]>
implements Serializable,
Cloneable,
RandomAccess {
    private static final long serialVersionUID = 1L;
    protected final int n;
    protected final int ratio;
    protected final int[][] array;
    protected transient long[] p;

    public IntArrayFrontCodedList(Iterator<int[]> arrays, int ratio) {
        if (ratio < 1) {
            throw new IllegalArgumentException("Illegal ratio (" + ratio + ")");
        }
        int[][] array = IntBigArrays.EMPTY_BIG_ARRAY;
        long[] p2 = LongArrays.EMPTY_ARRAY;
        int[][] a2 = new int[2][];
        long curSize = 0L;
        int n2 = 0;
        int b2 = 0;
        while (arrays.hasNext()) {
            a2[b2] = arrays.next();
            int length = a2[b2].length;
            if (n2 % ratio == 0) {
                p2 = LongArrays.grow((long[])p2, (int)(n2 / ratio + 1));
                p2[n2 / ratio] = curSize;
                array = BigArrays.grow(array, curSize + (long)IntArrayFrontCodedList.count(length) + (long)length, curSize);
                curSize += (long)IntArrayFrontCodedList.writeInt(array, length, curSize);
                BigArrays.copyToBig(a2[b2], 0, array, curSize, (long)length);
                curSize += (long)length;
            } else {
                int common;
                int minLength = a2[1 - b2].length;
                if (length < minLength) {
                    minLength = length;
                }
                for (common = 0; common < minLength && a2[0][common] == a2[1][common]; ++common) {
                }
                array = BigArrays.grow(array, curSize + (long)IntArrayFrontCodedList.count(length -= common) + (long)IntArrayFrontCodedList.count(common) + (long)length, curSize);
                curSize += (long)IntArrayFrontCodedList.writeInt(array, length, curSize);
                curSize += (long)IntArrayFrontCodedList.writeInt(array, common, curSize);
                BigArrays.copyToBig(a2[b2], common, array, curSize, (long)length);
                curSize += (long)length;
            }
            b2 = 1 - b2;
            ++n2;
        }
        this.n = n2;
        this.ratio = ratio;
        this.array = BigArrays.trim(array, curSize);
        this.p = LongArrays.trim((long[])p2, (int)((n2 + ratio - 1) / ratio));
    }

    public IntArrayFrontCodedList(Collection<int[]> c2, int ratio) {
        this(c2.iterator(), ratio);
    }

    static int readInt(int[][] a2, long pos) {
        return BigArrays.get(a2, pos);
    }

    static int count(int length) {
        return 1;
    }

    static int writeInt(int[][] a2, int length, long pos) {
        BigArrays.set(a2, pos, length);
        return 1;
    }

    public int ratio() {
        return this.ratio;
    }

    private int length(int index) {
        int[][] array = this.array;
        int delta = index % this.ratio;
        long pos = this.p[index / this.ratio];
        int length = IntArrayFrontCodedList.readInt(array, pos);
        if (delta == 0) {
            return length;
        }
        pos += (long)(IntArrayFrontCodedList.count(length) + length);
        length = IntArrayFrontCodedList.readInt(array, pos);
        int common = IntArrayFrontCodedList.readInt(array, pos + (long)IntArrayFrontCodedList.count(length));
        for (int i2 = 0; i2 < delta - 1; ++i2) {
            length = IntArrayFrontCodedList.readInt(array, pos += (long)(IntArrayFrontCodedList.count(length) + IntArrayFrontCodedList.count(common) + length));
            common = IntArrayFrontCodedList.readInt(array, pos + (long)IntArrayFrontCodedList.count(length));
        }
        return length + common;
    }

    public int arrayLength(int index) {
        this.ensureRestrictedIndex(index);
        return this.length(index);
    }

    private int extract(int index, int[] a2, int offset, int length) {
        long startPos;
        int delta = index % this.ratio;
        long pos = startPos = this.p[index / this.ratio];
        int arrayLength = IntArrayFrontCodedList.readInt(this.array, pos);
        int currLen = 0;
        if (delta == 0) {
            pos = this.p[index / this.ratio] + (long)IntArrayFrontCodedList.count(arrayLength);
            BigArrays.copyFromBig(this.array, pos, a2, offset, Math.min(length, arrayLength));
            return arrayLength;
        }
        int common = 0;
        for (int i2 = 0; i2 < delta; ++i2) {
            long prevArrayPos = pos + (long)IntArrayFrontCodedList.count(arrayLength) + (long)(i2 != 0 ? IntArrayFrontCodedList.count(common) : 0);
            common = IntArrayFrontCodedList.readInt(this.array, (pos = prevArrayPos + (long)arrayLength) + (long)IntArrayFrontCodedList.count(arrayLength = IntArrayFrontCodedList.readInt(this.array, pos)));
            int actualCommon = Math.min(common, length);
            if (actualCommon <= currLen) {
                currLen = actualCommon;
                continue;
            }
            BigArrays.copyFromBig(this.array, prevArrayPos, a2, currLen + offset, actualCommon - currLen);
            currLen = actualCommon;
        }
        if (currLen < length) {
            BigArrays.copyFromBig(this.array, pos + (long)IntArrayFrontCodedList.count(arrayLength) + (long)IntArrayFrontCodedList.count(common), a2, currLen + offset, Math.min(arrayLength, length - currLen));
        }
        return arrayLength + common;
    }

    @Override
    public int[] get(int index) {
        return this.getArray(index);
    }

    public int[] getArray(int index) {
        this.ensureRestrictedIndex(index);
        int length = this.length(index);
        int[] a2 = new int[length];
        this.extract(index, a2, 0, length);
        return a2;
    }

    public int get(int index, int[] a2, int offset, int length) {
        this.ensureRestrictedIndex(index);
        IntArrays.ensureOffsetLength(a2, offset, length);
        int arrayLength = this.extract(index, a2, offset, length);
        if (length >= arrayLength) {
            return arrayLength;
        }
        return length - arrayLength;
    }

    public int get(int index, int[] a2) {
        return this.get(index, a2, 0, a2.length);
    }

    @Override
    public int size() {
        return this.n;
    }

    @Override
    public ObjectListIterator<int[]> listIterator(final int start) {
        this.ensureIndex(start);
        return new ObjectListIterator<int[]>(){
            int[] s = IntArrays.EMPTY_ARRAY;
            int i = 0;
            long pos = 0L;
            boolean inSync;
            {
                if (start != 0) {
                    if (start == IntArrayFrontCodedList.this.n) {
                        this.i = start;
                    } else {
                        this.pos = IntArrayFrontCodedList.this.p[start / IntArrayFrontCodedList.this.ratio];
                        int j2 = start % IntArrayFrontCodedList.this.ratio;
                        this.i = start - j2;
                        while (j2-- != 0) {
                            this.next();
                        }
                    }
                }
            }

            @Override
            public boolean hasNext() {
                return this.i < IntArrayFrontCodedList.this.n;
            }

            @Override
            public boolean hasPrevious() {
                return this.i > 0;
            }

            @Override
            public int previousIndex() {
                return this.i - 1;
            }

            @Override
            public int nextIndex() {
                return this.i;
            }

            @Override
            public int[] next() {
                int length;
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                if (this.i % IntArrayFrontCodedList.this.ratio == 0) {
                    this.pos = IntArrayFrontCodedList.this.p[this.i / IntArrayFrontCodedList.this.ratio];
                    length = IntArrayFrontCodedList.readInt(IntArrayFrontCodedList.this.array, this.pos);
                    this.s = IntArrays.ensureCapacity(this.s, length, 0);
                    BigArrays.copyFromBig(IntArrayFrontCodedList.this.array, this.pos + (long)IntArrayFrontCodedList.count(length), this.s, 0, length);
                    this.pos += (long)(length + IntArrayFrontCodedList.count(length));
                    this.inSync = true;
                } else if (this.inSync) {
                    length = IntArrayFrontCodedList.readInt(IntArrayFrontCodedList.this.array, this.pos);
                    int common = IntArrayFrontCodedList.readInt(IntArrayFrontCodedList.this.array, this.pos + (long)IntArrayFrontCodedList.count(length));
                    this.s = IntArrays.ensureCapacity(this.s, length + common, common);
                    BigArrays.copyFromBig(IntArrayFrontCodedList.this.array, this.pos + (long)IntArrayFrontCodedList.count(length) + (long)IntArrayFrontCodedList.count(common), this.s, common, length);
                    this.pos += (long)(IntArrayFrontCodedList.count(length) + IntArrayFrontCodedList.count(common) + length);
                    length += common;
                } else {
                    length = IntArrayFrontCodedList.this.length(this.i);
                    this.s = IntArrays.ensureCapacity(this.s, length, 0);
                    IntArrayFrontCodedList.this.extract(this.i, this.s, 0, length);
                }
                ++this.i;
                return IntArrays.copy(this.s, 0, length);
            }

            @Override
            public int[] previous() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                this.inSync = false;
                return IntArrayFrontCodedList.this.getArray(--this.i);
            }
        };
    }

    public IntArrayFrontCodedList clone() {
        return this;
    }

    @Override
    public String toString() {
        StringBuffer s2 = new StringBuffer();
        s2.append("[");
        for (int i2 = 0; i2 < this.n; ++i2) {
            if (i2 != 0) {
                s2.append(", ");
            }
            s2.append(IntArrayList.wrap(this.getArray(i2)).toString());
        }
        s2.append("]");
        return s2.toString();
    }

    protected long[] rebuildPointerArray() {
        long[] p2 = new long[(this.n + this.ratio - 1) / this.ratio];
        int[][] a2 = this.array;
        long pos = 0L;
        int j2 = 0;
        int skip = this.ratio - 1;
        for (int i2 = 0; i2 < this.n; ++i2) {
            int length = IntArrayFrontCodedList.readInt(a2, pos);
            int count = IntArrayFrontCodedList.count(length);
            if (++skip == this.ratio) {
                skip = 0;
                p2[j2++] = pos;
                pos += (long)(count + length);
                continue;
            }
            pos += (long)(count + IntArrayFrontCodedList.count(IntArrayFrontCodedList.readInt(a2, pos + (long)count)) + length);
        }
        return p2;
    }

    private void readObject(ObjectInputStream s2) throws IOException, ClassNotFoundException {
        s2.defaultReadObject();
        this.p = this.rebuildPointerArray();
    }
}

