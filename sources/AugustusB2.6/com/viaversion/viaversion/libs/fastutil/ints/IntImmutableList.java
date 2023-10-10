// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.io.ObjectStreamException;
import java.io.InvalidObjectException;
import com.viaversion.viaversion.libs.fastutil.SafeMath;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import java.io.Serializable;
import java.util.RandomAccess;

public class IntImmutableList extends IntLists.ImmutableListBase implements IntList, RandomAccess, Cloneable, Serializable
{
    private static final long serialVersionUID = 0L;
    static final IntImmutableList EMPTY;
    private final int[] a;
    
    public IntImmutableList(final int[] a) {
        this.a = a;
    }
    
    public IntImmutableList(final Collection<? extends Integer> c) {
        this(c.isEmpty() ? IntArrays.EMPTY_ARRAY : IntIterators.unwrap(IntIterators.asIntIterator(c.iterator())));
    }
    
    public IntImmutableList(final IntCollection c) {
        this(c.isEmpty() ? IntArrays.EMPTY_ARRAY : IntIterators.unwrap(c.iterator()));
    }
    
    public IntImmutableList(final IntList l) {
        this(l.isEmpty() ? IntArrays.EMPTY_ARRAY : new int[l.size()]);
        l.getElements(0, this.a, 0, l.size());
    }
    
    public IntImmutableList(final int[] a, final int offset, final int length) {
        this((length == 0) ? IntArrays.EMPTY_ARRAY : new int[length]);
        System.arraycopy(a, offset, this.a, 0, length);
    }
    
    public IntImmutableList(final IntIterator i) {
        this(i.hasNext() ? IntIterators.unwrap(i) : IntArrays.EMPTY_ARRAY);
    }
    
    public static IntImmutableList of() {
        return IntImmutableList.EMPTY;
    }
    
    public static IntImmutableList of(final int... init) {
        return (init.length == 0) ? of() : new IntImmutableList(init);
    }
    
    private static IntImmutableList convertTrustedToImmutableList(final IntArrayList arrayList) {
        if (arrayList.isEmpty()) {
            return of();
        }
        int[] backingArray = arrayList.elements();
        if (arrayList.size() != backingArray.length) {
            backingArray = Arrays.copyOf(backingArray, arrayList.size());
        }
        return new IntImmutableList(backingArray);
    }
    
    public static IntImmutableList toList(final IntStream stream) {
        return convertTrustedToImmutableList(IntArrayList.toList(stream));
    }
    
    public static IntImmutableList toListWithExpectedSize(final IntStream stream, final int expectedSize) {
        return convertTrustedToImmutableList(IntArrayList.toListWithExpectedSize(stream, expectedSize));
    }
    
    @Override
    public int getInt(final int index) {
        if (index >= this.a.length) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.a.length + ")");
        }
        return this.a[index];
    }
    
    @Override
    public int indexOf(final int k) {
        for (int i = 0, size = this.a.length; i < size; ++i) {
            if (k == this.a[i]) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final int k) {
        int i = this.a.length;
        while (i-- != 0) {
            if (k == this.a[i]) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int size() {
        return this.a.length;
    }
    
    @Override
    public boolean isEmpty() {
        return this.a.length == 0;
    }
    
    @Override
    public void getElements(final int from, final int[] a, final int offset, final int length) {
        IntArrays.ensureOffsetLength(a, offset, length);
        System.arraycopy(this.a, from, a, offset, length);
    }
    
    @Override
    public void forEach(final IntConsumer action) {
        for (int i = 0; i < this.a.length; ++i) {
            action.accept(this.a[i]);
        }
    }
    
    @Override
    public int[] toIntArray() {
        return this.a.clone();
    }
    
    @Override
    public int[] toArray(int[] a) {
        if (a == null || a.length < this.size()) {
            a = new int[this.a.length];
        }
        System.arraycopy(this.a, 0, a, 0, a.length);
        return a;
    }
    
    @Override
    public IntListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new IntListIterator() {
            int pos = index;
            
            @Override
            public boolean hasNext() {
                return this.pos < IntImmutableList.this.a.length;
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            @Override
            public int nextInt() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return IntImmutableList.this.a[this.pos++];
            }
            
            @Override
            public int previousInt() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final int[] access$000 = IntImmutableList.this.a;
                final int pos = this.pos - 1;
                this.pos = pos;
                return access$000[pos];
            }
            
            @Override
            public int nextIndex() {
                return this.pos;
            }
            
            @Override
            public int previousIndex() {
                return this.pos - 1;
            }
            
            @Override
            public void forEachRemaining(final IntConsumer action) {
                while (this.pos < IntImmutableList.this.a.length) {
                    action.accept(IntImmutableList.this.a[this.pos++]);
                }
            }
            
            @Override
            public void add(final int k) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void set(final int k) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public int back(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                final int remaining = IntImmutableList.this.a.length - this.pos;
                if (n < remaining) {
                    this.pos -= n;
                }
                else {
                    n = remaining;
                    this.pos = 0;
                }
                return n;
            }
            
            @Override
            public int skip(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                final int remaining = IntImmutableList.this.a.length - this.pos;
                if (n < remaining) {
                    this.pos += n;
                }
                else {
                    n = remaining;
                    this.pos = IntImmutableList.this.a.length;
                }
                return n;
            }
        };
    }
    
    @Override
    public IntSpliterator spliterator() {
        return new Spliterator();
    }
    
    @Override
    public IntList subList(final int from, final int to) {
        if (from == 0 && to == this.size()) {
            return this;
        }
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from == to) {
            return IntImmutableList.EMPTY;
        }
        if (from > to) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new ImmutableSubList(this, from, to);
    }
    
    public IntImmutableList clone() {
        return this;
    }
    
    public boolean equals(final IntImmutableList l) {
        if (l == this) {
            return true;
        }
        if (this.a == l.a) {
            return true;
        }
        final int s = this.size();
        if (s != l.size()) {
            return false;
        }
        final int[] a1 = this.a;
        final int[] a2 = l.a;
        return Arrays.equals(a1, a2);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof List)) {
            return false;
        }
        if (o instanceof IntImmutableList) {
            return this.equals((IntImmutableList)o);
        }
        if (o instanceof ImmutableSubList) {
            return ((ImmutableSubList)o).equals(this);
        }
        return super.equals(o);
    }
    
    public int compareTo(final IntImmutableList l) {
        if (this.a == l.a) {
            return 0;
        }
        final int s1 = this.size();
        final int s2 = l.size();
        final int[] a1 = this.a;
        final int[] a2 = l.a;
        int i;
        for (i = 0; i < s1 && i < s2; ++i) {
            final int e1 = a1[i];
            final int e2 = a2[i];
            final int r;
            if ((r = Integer.compare(e1, e2)) != 0) {
                return r;
            }
        }
        return (i < s2) ? -1 : ((i < s1) ? 1 : 0);
    }
    
    @Override
    public int compareTo(final List<? extends Integer> l) {
        if (l instanceof IntImmutableList) {
            return this.compareTo((IntImmutableList)l);
        }
        if (l instanceof ImmutableSubList) {
            final ImmutableSubList other = (ImmutableSubList)l;
            return -other.compareTo(this);
        }
        return super.compareTo(l);
    }
    
    static {
        EMPTY = new IntImmutableList(IntArrays.EMPTY_ARRAY);
    }
    
    private final class Spliterator implements IntSpliterator
    {
        int pos;
        int max;
        
        public Spliterator(final IntImmutableList x0) {
            this(x0, 0, x0.a.length);
        }
        
        private Spliterator(final int pos, final int max) {
            assert pos <= max : "pos " + pos + " must be <= max " + max;
            this.pos = pos;
            this.max = max;
        }
        
        @Override
        public int characteristics() {
            return 17744;
        }
        
        @Override
        public long estimateSize() {
            return this.max - this.pos;
        }
        
        @Override
        public boolean tryAdvance(final IntConsumer action) {
            if (this.pos >= this.max) {
                return false;
            }
            action.accept(IntImmutableList.this.a[this.pos++]);
            return true;
        }
        
        @Override
        public void forEachRemaining(final IntConsumer action) {
            while (this.pos < this.max) {
                action.accept(IntImmutableList.this.a[this.pos]);
                ++this.pos;
            }
        }
        
        @Override
        public long skip(long n) {
            if (n < 0L) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (this.pos >= this.max) {
                return 0L;
            }
            final int remaining = this.max - this.pos;
            if (n < remaining) {
                this.pos = SafeMath.safeLongToInt(this.pos + n);
                return n;
            }
            n = remaining;
            this.pos = this.max;
            return n;
        }
        
        @Override
        public IntSpliterator trySplit() {
            final int retLen = this.max - this.pos >> 1;
            if (retLen <= 1) {
                return null;
            }
            final int retMax;
            final int myNewPos = retMax = this.pos + retLen;
            final int oldPos = this.pos;
            this.pos = myNewPos;
            return new Spliterator(oldPos, retMax);
        }
    }
    
    private static final class ImmutableSubList extends IntLists.ImmutableListBase implements RandomAccess, Serializable
    {
        private static final long serialVersionUID = 7054639518438982401L;
        final IntImmutableList innerList;
        final int from;
        final int to;
        final transient int[] a;
        
        ImmutableSubList(final IntImmutableList innerList, final int from, final int to) {
            this.innerList = innerList;
            this.from = from;
            this.to = to;
            this.a = innerList.a;
        }
        
        @Override
        public int getInt(final int index) {
            this.ensureRestrictedIndex(index);
            return this.a[index + this.from];
        }
        
        @Override
        public int indexOf(final int k) {
            for (int i = this.from; i < this.to; ++i) {
                if (k == this.a[i]) {
                    return i - this.from;
                }
            }
            return -1;
        }
        
        @Override
        public int lastIndexOf(final int k) {
            int i = this.to;
            while (i-- != this.from) {
                if (k == this.a[i]) {
                    return i - this.from;
                }
            }
            return -1;
        }
        
        @Override
        public int size() {
            return this.to - this.from;
        }
        
        @Override
        public boolean isEmpty() {
            return this.to <= this.from;
        }
        
        @Override
        public void getElements(final int fromSublistIndex, final int[] a, final int offset, final int length) {
            IntArrays.ensureOffsetLength(a, offset, length);
            this.ensureRestrictedIndex(fromSublistIndex);
            if (this.from + length > this.to) {
                throw new IndexOutOfBoundsException("Final index " + (this.from + length) + " (startingIndex: " + this.from + " + length: " + length + ") is greater then list length " + this.size());
            }
            System.arraycopy(this.a, fromSublistIndex + this.from, a, offset, length);
        }
        
        @Override
        public void forEach(final IntConsumer action) {
            for (int i = this.from; i < this.to; ++i) {
                action.accept(this.a[i]);
            }
        }
        
        @Override
        public int[] toIntArray() {
            return Arrays.copyOfRange(this.a, this.from, this.to);
        }
        
        @Override
        public int[] toArray(int[] a) {
            if (a == null || a.length < this.size()) {
                a = new int[this.size()];
            }
            System.arraycopy(this.a, this.from, a, 0, this.size());
            return a;
        }
        
        @Override
        public IntListIterator listIterator(final int index) {
            this.ensureIndex(index);
            return new IntListIterator() {
                int pos = index;
                
                @Override
                public boolean hasNext() {
                    return this.pos < ImmutableSubList.this.to;
                }
                
                @Override
                public boolean hasPrevious() {
                    return this.pos > ImmutableSubList.this.from;
                }
                
                @Override
                public int nextInt() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return ImmutableSubList.this.a[this.pos++ + ImmutableSubList.this.from];
                }
                
                @Override
                public int previousInt() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final int[] a = ImmutableSubList.this.a;
                    final int pos = this.pos - 1;
                    this.pos = pos;
                    return a[pos + ImmutableSubList.this.from];
                }
                
                @Override
                public int nextIndex() {
                    return this.pos;
                }
                
                @Override
                public int previousIndex() {
                    return this.pos - 1;
                }
                
                @Override
                public void forEachRemaining(final IntConsumer action) {
                    while (this.pos < ImmutableSubList.this.to) {
                        action.accept(ImmutableSubList.this.a[this.pos++ + ImmutableSubList.this.from]);
                    }
                }
                
                @Override
                public void add(final int k) {
                    throw new UnsupportedOperationException();
                }
                
                @Override
                public void set(final int k) {
                    throw new UnsupportedOperationException();
                }
                
                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
                
                @Override
                public int back(int n) {
                    if (n < 0) {
                        throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                    }
                    final int remaining = ImmutableSubList.this.to - this.pos;
                    if (n < remaining) {
                        this.pos -= n;
                    }
                    else {
                        n = remaining;
                        this.pos = 0;
                    }
                    return n;
                }
                
                @Override
                public int skip(int n) {
                    if (n < 0) {
                        throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                    }
                    final int remaining = ImmutableSubList.this.to - this.pos;
                    if (n < remaining) {
                        this.pos += n;
                    }
                    else {
                        n = remaining;
                        this.pos = ImmutableSubList.this.to;
                    }
                    return n;
                }
            };
        }
        
        @Override
        public IntSpliterator spliterator() {
            return new SubListSpliterator();
        }
        
        boolean contentsEquals(final int[] otherA, final int otherAFrom, final int otherATo) {
            if (this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return true;
            }
            if (otherATo - otherAFrom != this.size()) {
                return false;
            }
            int pos = this.from;
            int otherPos = otherAFrom;
            while (pos < this.to) {
                if (this.a[pos++] != otherA[otherPos++]) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (!(o instanceof List)) {
                return false;
            }
            if (o instanceof IntImmutableList) {
                final IntImmutableList other = (IntImmutableList)o;
                return this.contentsEquals(other.a, 0, other.size());
            }
            if (o instanceof ImmutableSubList) {
                final ImmutableSubList other2 = (ImmutableSubList)o;
                return this.contentsEquals(other2.a, other2.from, other2.to);
            }
            return super.equals(o);
        }
        
        int contentsCompareTo(final int[] otherA, final int otherAFrom, final int otherATo) {
            if (this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return 0;
            }
            int i = this.from;
            for (int j = otherAFrom; i < this.to && i < otherATo; ++i, ++j) {
                final int e1 = this.a[i];
                final int e2 = otherA[j];
                final int r;
                if ((r = Integer.compare(e1, e2)) != 0) {
                    return r;
                }
            }
            return (i < otherATo) ? -1 : ((i < this.to) ? 1 : 0);
        }
        
        @Override
        public int compareTo(final List<? extends Integer> l) {
            if (l instanceof IntImmutableList) {
                final IntImmutableList other = (IntImmutableList)l;
                return this.contentsCompareTo(other.a, 0, other.size());
            }
            if (l instanceof ImmutableSubList) {
                final ImmutableSubList other2 = (ImmutableSubList)l;
                return this.contentsCompareTo(other2.a, other2.from, other2.to);
            }
            return super.compareTo(l);
        }
        
        private Object readResolve() throws ObjectStreamException {
            try {
                return this.innerList.subList(this.from, this.to);
            }
            catch (IllegalArgumentException | IndexOutOfBoundsException ex3) {
                final RuntimeException ex2;
                final RuntimeException ex = ex2;
                throw (InvalidObjectException)new InvalidObjectException(ex.getMessage()).initCause(ex);
            }
        }
        
        @Override
        public IntList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from == to) {
                return IntImmutableList.EMPTY;
            }
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new ImmutableSubList(this.innerList, from + this.from, to + this.from);
        }
        
        private final class SubListSpliterator extends IntSpliterators.EarlyBindingSizeIndexBasedSpliterator
        {
            SubListSpliterator() {
                super(ImmutableSubList.this.from, ImmutableSubList.this.to);
            }
            
            private SubListSpliterator(final int pos, final int maxPos) {
                super(pos, maxPos);
            }
            
            @Override
            protected final int get(final int i) {
                return ImmutableSubList.this.a[i];
            }
            
            @Override
            protected final SubListSpliterator makeForSplit(final int pos, final int maxPos) {
                return new SubListSpliterator(pos, maxPos);
            }
            
            @Override
            public boolean tryAdvance(final IntConsumer action) {
                if (this.pos >= this.maxPos) {
                    return false;
                }
                action.accept(ImmutableSubList.this.a[this.pos++]);
                return true;
            }
            
            @Override
            public void forEachRemaining(final IntConsumer action) {
                final int max = this.maxPos;
                while (this.pos < max) {
                    action.accept(ImmutableSubList.this.a[this.pos++]);
                }
            }
            
            @Override
            public int characteristics() {
                return 17744;
            }
        }
    }
}
