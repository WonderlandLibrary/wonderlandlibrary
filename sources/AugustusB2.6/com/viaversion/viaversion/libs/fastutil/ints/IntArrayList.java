// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.SafeMath;
import java.util.ListIterator;
import java.util.Spliterator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import java.io.Serializable;
import java.util.RandomAccess;

public class IntArrayList extends AbstractIntList implements RandomAccess, Cloneable, Serializable
{
    private static final long serialVersionUID = -7046029254386353130L;
    public static final int DEFAULT_INITIAL_CAPACITY = 10;
    protected transient int[] a;
    protected int size;
    
    private static final int[] copyArraySafe(final int[] a, final int length) {
        if (length == 0) {
            return IntArrays.EMPTY_ARRAY;
        }
        return Arrays.copyOf(a, length);
    }
    
    private static final int[] copyArrayFromSafe(final IntArrayList l) {
        return copyArraySafe(l.a, l.size);
    }
    
    protected IntArrayList(final int[] a, final boolean wrapped) {
        this.a = a;
    }
    
    private void initArrayFromCapacity(final int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
        }
        if (capacity == 0) {
            this.a = IntArrays.EMPTY_ARRAY;
        }
        else {
            this.a = new int[capacity];
        }
    }
    
    public IntArrayList(final int capacity) {
        this.initArrayFromCapacity(capacity);
    }
    
    public IntArrayList() {
        this.a = IntArrays.DEFAULT_EMPTY_ARRAY;
    }
    
    public IntArrayList(final Collection<? extends Integer> c) {
        if (c instanceof IntArrayList) {
            this.a = copyArrayFromSafe((IntArrayList)c);
            this.size = this.a.length;
        }
        else {
            this.initArrayFromCapacity(c.size());
            if (c instanceof IntList) {
                ((IntList)c).getElements(0, this.a, 0, this.size = c.size());
            }
            else {
                this.size = IntIterators.unwrap(IntIterators.asIntIterator(c.iterator()), this.a);
            }
        }
    }
    
    public IntArrayList(final IntCollection c) {
        if (c instanceof IntArrayList) {
            this.a = copyArrayFromSafe((IntArrayList)c);
            this.size = this.a.length;
        }
        else {
            this.initArrayFromCapacity(c.size());
            if (c instanceof IntList) {
                ((IntList)c).getElements(0, this.a, 0, this.size = c.size());
            }
            else {
                this.size = IntIterators.unwrap(c.iterator(), this.a);
            }
        }
    }
    
    public IntArrayList(final IntList l) {
        if (l instanceof IntArrayList) {
            this.a = copyArrayFromSafe((IntArrayList)l);
            this.size = this.a.length;
        }
        else {
            this.initArrayFromCapacity(l.size());
            l.getElements(0, this.a, 0, this.size = l.size());
        }
    }
    
    public IntArrayList(final int[] a) {
        this(a, 0, a.length);
    }
    
    public IntArrayList(final int[] a, final int offset, final int length) {
        this(length);
        System.arraycopy(a, offset, this.a, 0, length);
        this.size = length;
    }
    
    public IntArrayList(final Iterator<? extends Integer> i) {
        this();
        while (i.hasNext()) {
            this.add((int)i.next());
        }
    }
    
    public IntArrayList(final IntIterator i) {
        this();
        while (i.hasNext()) {
            this.add(i.nextInt());
        }
    }
    
    public int[] elements() {
        return this.a;
    }
    
    public static IntArrayList wrap(final int[] a, final int length) {
        if (length > a.length) {
            throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + a.length + ")");
        }
        final IntArrayList l = new IntArrayList(a, true);
        l.size = length;
        return l;
    }
    
    public static IntArrayList wrap(final int[] a) {
        return wrap(a, a.length);
    }
    
    public static IntArrayList of() {
        return new IntArrayList();
    }
    
    public static IntArrayList of(final int... init) {
        return wrap(init);
    }
    
    public static IntArrayList toList(final IntStream stream) {
        return stream.collect(IntArrayList::new, IntArrayList::add, IntList::addAll);
    }
    
    public static IntArrayList toListWithExpectedSize(final IntStream stream, final int expectedSize) {
        if (expectedSize <= 10) {
            return toList(stream);
        }
        final Object o3;
        return stream.collect(new IntCollections.SizeDecreasingSupplier<IntArrayList>(expectedSize, size -> {
            if (size <= 10) {
                // new(com.viaversion.viaversion.libs.fastutil.ints.IntArrayList.class)
                new IntArrayList();
            }
            else {
                // new(com.viaversion.viaversion.libs.fastutil.ints.IntArrayList.class)
                new IntArrayList(size);
            }
            return o3;
        }), IntArrayList::add, IntList::addAll);
    }
    
    public void ensureCapacity(final int capacity) {
        if (capacity <= this.a.length || (this.a == IntArrays.DEFAULT_EMPTY_ARRAY && capacity <= 10)) {
            return;
        }
        this.a = IntArrays.ensureCapacity(this.a, capacity, this.size);
        assert this.size <= this.a.length;
    }
    
    private void grow(int capacity) {
        if (capacity <= this.a.length) {
            return;
        }
        if (this.a != IntArrays.DEFAULT_EMPTY_ARRAY) {
            capacity = (int)Math.max(Math.min(this.a.length + (long)(this.a.length >> 1), 2147483639L), capacity);
        }
        else if (capacity < 10) {
            capacity = 10;
        }
        this.a = IntArrays.forceCapacity(this.a, capacity, this.size);
        assert this.size <= this.a.length;
    }
    
    @Override
    public void add(final int index, final int k) {
        this.ensureIndex(index);
        this.grow(this.size + 1);
        if (index != this.size) {
            System.arraycopy(this.a, index, this.a, index + 1, this.size - index);
        }
        this.a[index] = k;
        ++this.size;
        assert this.size <= this.a.length;
    }
    
    @Override
    public boolean add(final int k) {
        this.grow(this.size + 1);
        this.a[this.size++] = k;
        assert this.size <= this.a.length;
        return true;
    }
    
    @Override
    public int getInt(final int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        return this.a[index];
    }
    
    @Override
    public int indexOf(final int k) {
        for (int i = 0; i < this.size; ++i) {
            if (k == this.a[i]) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final int k) {
        int i = this.size;
        while (i-- != 0) {
            if (k == this.a[i]) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int removeInt(final int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final int old = this.a[index];
        --this.size;
        if (index != this.size) {
            System.arraycopy(this.a, index + 1, this.a, index, this.size - index);
        }
        assert this.size <= this.a.length;
        return old;
    }
    
    @Override
    public boolean rem(final int k) {
        final int index = this.indexOf(k);
        if (index == -1) {
            return false;
        }
        this.removeInt(index);
        assert this.size <= this.a.length;
        return true;
    }
    
    @Override
    public int set(final int index, final int k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final int old = this.a[index];
        this.a[index] = k;
        return old;
    }
    
    @Override
    public void clear() {
        this.size = 0;
        assert this.size <= this.a.length;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public void size(final int size) {
        if (size > this.a.length) {
            this.a = IntArrays.forceCapacity(this.a, size, this.size);
        }
        if (size > this.size) {
            Arrays.fill(this.a, this.size, size, 0);
        }
        this.size = size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public void trim() {
        this.trim(0);
    }
    
    public void trim(final int n) {
        if (n >= this.a.length || this.size == this.a.length) {
            return;
        }
        final int[] t = new int[Math.max(n, this.size)];
        System.arraycopy(this.a, 0, t, 0, this.size);
        this.a = t;
        assert this.size <= this.a.length;
    }
    
    @Override
    public IntList subList(final int from, final int to) {
        if (from == 0 && to == this.size()) {
            return this;
        }
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new SubList(from, to);
    }
    
    @Override
    public void getElements(final int from, final int[] a, final int offset, final int length) {
        IntArrays.ensureOffsetLength(a, offset, length);
        System.arraycopy(this.a, from, a, offset, length);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        com.viaversion.viaversion.libs.fastutil.Arrays.ensureFromTo(this.size, from, to);
        System.arraycopy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
    }
    
    @Override
    public void addElements(final int index, final int[] a, final int offset, final int length) {
        this.ensureIndex(index);
        IntArrays.ensureOffsetLength(a, offset, length);
        this.grow(this.size + length);
        System.arraycopy(this.a, index, this.a, index + length, this.size - index);
        System.arraycopy(a, offset, this.a, index, length);
        this.size += length;
    }
    
    @Override
    public void setElements(final int index, final int[] a, final int offset, final int length) {
        this.ensureIndex(index);
        IntArrays.ensureOffsetLength(a, offset, length);
        if (index + length > this.size) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + this.size + ")");
        }
        System.arraycopy(a, offset, this.a, index, length);
    }
    
    @Override
    public void forEach(final IntConsumer action) {
        for (int i = 0; i < this.size; ++i) {
            action.accept(this.a[i]);
        }
    }
    
    @Override
    public boolean addAll(int index, final IntCollection c) {
        if (c instanceof IntList) {
            return this.addAll(index, (IntList)c);
        }
        this.ensureIndex(index);
        int n = c.size();
        if (n == 0) {
            return false;
        }
        this.grow(this.size + n);
        System.arraycopy(this.a, index, this.a, index + n, this.size - index);
        final IntIterator i = c.iterator();
        this.size += n;
        while (n-- != 0) {
            this.a[index++] = i.nextInt();
        }
        assert this.size <= this.a.length;
        return true;
    }
    
    @Override
    public boolean addAll(final int index, final IntList l) {
        this.ensureIndex(index);
        final int n = l.size();
        if (n == 0) {
            return false;
        }
        this.grow(this.size + n);
        System.arraycopy(this.a, index, this.a, index + n, this.size - index);
        l.getElements(0, this.a, index, n);
        this.size += n;
        assert this.size <= this.a.length;
        return true;
    }
    
    @Override
    public boolean removeAll(final IntCollection c) {
        final int[] a = this.a;
        int j = 0;
        for (int i = 0; i < this.size; ++i) {
            if (!c.contains(a[i])) {
                a[j++] = a[i];
            }
        }
        final boolean modified = this.size != j;
        this.size = j;
        return modified;
    }
    
    @Override
    public int[] toArray(int[] a) {
        if (a == null || a.length < this.size) {
            a = Arrays.copyOf(a, this.size);
        }
        System.arraycopy(this.a, 0, a, 0, this.size);
        return a;
    }
    
    @Override
    public IntListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new IntListIterator() {
            int pos = index;
            int last = -1;
            
            @Override
            public boolean hasNext() {
                return this.pos < IntArrayList.this.size;
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
                final int[] a = IntArrayList.this.a;
                final int last = this.pos++;
                this.last = last;
                return a[last];
            }
            
            @Override
            public int previousInt() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final int[] a = IntArrayList.this.a;
                final int n = this.pos - 1;
                this.pos = n;
                this.last = n;
                return a[n];
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
            public void add(final int k) {
                IntArrayList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            @Override
            public void set(final int k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                IntArrayList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                IntArrayList.this.removeInt(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
            
            @Override
            public void forEachRemaining(final IntConsumer action) {
                while (this.pos < IntArrayList.this.size) {
                    final int[] a = IntArrayList.this.a;
                    final int last = this.pos++;
                    this.last = last;
                    action.accept(a[last]);
                }
            }
            
            @Override
            public int back(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                final int remaining = IntArrayList.this.size - this.pos;
                if (n < remaining) {
                    this.pos -= n;
                }
                else {
                    n = remaining;
                    this.pos = 0;
                }
                this.last = this.pos;
                return n;
            }
            
            @Override
            public int skip(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                final int remaining = IntArrayList.this.size - this.pos;
                if (n < remaining) {
                    this.pos += n;
                }
                else {
                    n = remaining;
                    this.pos = IntArrayList.this.size;
                }
                this.last = this.pos - 1;
                return n;
            }
        };
    }
    
    @Override
    public IntSpliterator spliterator() {
        return new Spliterator();
    }
    
    @Override
    public void sort(final IntComparator comp) {
        if (comp == null) {
            IntArrays.stableSort(this.a, 0, this.size);
        }
        else {
            IntArrays.stableSort(this.a, 0, this.size, comp);
        }
    }
    
    @Override
    public void unstableSort(final IntComparator comp) {
        if (comp == null) {
            IntArrays.unstableSort(this.a, 0, this.size);
        }
        else {
            IntArrays.unstableSort(this.a, 0, this.size, comp);
        }
    }
    
    public IntArrayList clone() {
        IntArrayList cloned = null;
        if (this.getClass() == IntArrayList.class) {
            cloned = new IntArrayList(copyArraySafe(this.a, this.size), false);
            cloned.size = this.size;
        }
        else {
            try {
                cloned = (IntArrayList)super.clone();
            }
            catch (CloneNotSupportedException err) {
                throw new InternalError(err);
            }
            cloned.a = copyArraySafe(this.a, this.size);
        }
        return cloned;
    }
    
    public boolean equals(final IntArrayList l) {
        if (l == this) {
            return true;
        }
        int s = this.size();
        if (s != l.size()) {
            return false;
        }
        final int[] a1 = this.a;
        final int[] a2 = l.a;
        if (a1 == a2 && s == l.size()) {
            return true;
        }
        while (s-- != 0) {
            if (a1[s] != a2[s]) {
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
        if (o instanceof IntArrayList) {
            return this.equals((IntArrayList)o);
        }
        if (o instanceof SubList) {
            return ((SubList)o).equals(this);
        }
        return super.equals(o);
    }
    
    public int compareTo(final IntArrayList l) {
        final int s1 = this.size();
        final int s2 = l.size();
        final int[] a1 = this.a;
        final int[] a2 = l.a;
        if (a1 == a2 && s1 == s2) {
            return 0;
        }
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
        if (l instanceof IntArrayList) {
            return this.compareTo((IntArrayList)l);
        }
        if (l instanceof SubList) {
            return -((SubList)l).compareTo(this);
        }
        return super.compareTo(l);
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeInt(this.a[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = new int[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.a[i] = s.readInt();
        }
    }
    
    private class SubList extends IntRandomAccessSubList
    {
        private static final long serialVersionUID = -3185226345314976296L;
        
        protected SubList(final int from, final int to) {
            super(IntArrayList.this, from, to);
        }
        
        private int[] getParentArray() {
            return IntArrayList.this.a;
        }
        
        @Override
        public int getInt(final int i) {
            this.ensureRestrictedIndex(i);
            return IntArrayList.this.a[i + this.from];
        }
        
        @Override
        public IntListIterator listIterator(final int index) {
            return new SubListIterator(index);
        }
        
        @Override
        public IntSpliterator spliterator() {
            return new SubListSpliterator();
        }
        
        boolean contentsEquals(final int[] otherA, final int otherAFrom, final int otherATo) {
            if (IntArrayList.this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return true;
            }
            if (otherATo - otherAFrom != this.size()) {
                return false;
            }
            int pos = this.from;
            int otherPos = otherAFrom;
            while (pos < this.to) {
                if (IntArrayList.this.a[pos++] != otherA[otherPos++]) {
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
            if (o instanceof IntArrayList) {
                final IntArrayList other = (IntArrayList)o;
                return this.contentsEquals(other.a, 0, other.size());
            }
            if (o instanceof SubList) {
                final SubList other2 = (SubList)o;
                return this.contentsEquals(other2.getParentArray(), other2.from, other2.to);
            }
            return super.equals(o);
        }
        
        int contentsCompareTo(final int[] otherA, final int otherAFrom, final int otherATo) {
            if (IntArrayList.this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return 0;
            }
            int i = this.from;
            for (int j = otherAFrom; i < this.to && i < otherATo; ++i, ++j) {
                final int e1 = IntArrayList.this.a[i];
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
            if (l instanceof IntArrayList) {
                final IntArrayList other = (IntArrayList)l;
                return this.contentsCompareTo(other.a, 0, other.size());
            }
            if (l instanceof SubList) {
                final SubList other2 = (SubList)l;
                return this.contentsCompareTo(other2.getParentArray(), other2.from, other2.to);
            }
            return super.compareTo(l);
        }
        
        private final class SubListIterator extends IntIterators.AbstractIndexBasedListIterator
        {
            SubListIterator(final int index) {
                super(0, index);
            }
            
            @Override
            protected final int get(final int i) {
                return IntArrayList.this.a[SubList.this.from + i];
            }
            
            @Override
            protected final void add(final int i, final int k) {
                SubList.this.add(i, k);
            }
            
            @Override
            protected final void set(final int i, final int k) {
                SubList.this.set(i, k);
            }
            
            @Override
            protected final void remove(final int i) {
                SubList.this.removeInt(i);
            }
            
            @Override
            protected final int getMaxPos() {
                return SubList.this.to - SubList.this.from;
            }
            
            @Override
            public int nextInt() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final int[] a = IntArrayList.this.a;
                final int from = SubList.this.from;
                final int lastReturned = this.pos++;
                this.lastReturned = lastReturned;
                return a[from + lastReturned];
            }
            
            @Override
            public int previousInt() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final int[] a = IntArrayList.this.a;
                final int from = SubList.this.from;
                final int n = this.pos - 1;
                this.pos = n;
                this.lastReturned = n;
                return a[from + n];
            }
            
            @Override
            public void forEachRemaining(final IntConsumer action) {
                final int max = SubList.this.to - SubList.this.from;
                while (this.pos < max) {
                    final int[] a = IntArrayList.this.a;
                    final int from = SubList.this.from;
                    final int lastReturned = this.pos++;
                    this.lastReturned = lastReturned;
                    action.accept(a[from + lastReturned]);
                }
            }
        }
        
        private final class SubListSpliterator extends IntSpliterators.LateBindingSizeIndexBasedSpliterator
        {
            SubListSpliterator() {
                super(SubList.this.from);
            }
            
            private SubListSpliterator(final int pos, final int maxPos) {
                super(pos, maxPos);
            }
            
            @Override
            protected final int getMaxPosFromBackingStore() {
                return SubList.this.to;
            }
            
            @Override
            protected final int get(final int i) {
                return IntArrayList.this.a[i];
            }
            
            @Override
            protected final SubListSpliterator makeForSplit(final int pos, final int maxPos) {
                return new SubListSpliterator(pos, maxPos);
            }
            
            @Override
            public boolean tryAdvance(final IntConsumer action) {
                if (this.pos >= this.getMaxPos()) {
                    return false;
                }
                action.accept(IntArrayList.this.a[this.pos++]);
                return true;
            }
            
            @Override
            public void forEachRemaining(final IntConsumer action) {
                final int max = this.getMaxPos();
                while (this.pos < max) {
                    action.accept(IntArrayList.this.a[this.pos++]);
                }
            }
        }
    }
    
    private final class Spliterator implements IntSpliterator
    {
        boolean hasSplit;
        int pos;
        int max;
        
        public Spliterator(final IntArrayList list) {
            this(list, 0, list.size, false);
        }
        
        private Spliterator(final int pos, final int max, final boolean hasSplit) {
            this.hasSplit = false;
            assert pos <= max : "pos " + pos + " must be <= max " + max;
            this.pos = pos;
            this.max = max;
            this.hasSplit = hasSplit;
        }
        
        private int getWorkingMax() {
            return this.hasSplit ? this.max : IntArrayList.this.size;
        }
        
        @Override
        public int characteristics() {
            return 16720;
        }
        
        @Override
        public long estimateSize() {
            return this.getWorkingMax() - this.pos;
        }
        
        @Override
        public boolean tryAdvance(final IntConsumer action) {
            if (this.pos >= this.getWorkingMax()) {
                return false;
            }
            action.accept(IntArrayList.this.a[this.pos++]);
            return true;
        }
        
        @Override
        public void forEachRemaining(final IntConsumer action) {
            final int max = this.getWorkingMax();
            while (this.pos < max) {
                action.accept(IntArrayList.this.a[this.pos]);
                ++this.pos;
            }
        }
        
        @Override
        public long skip(long n) {
            if (n < 0L) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            final int max = this.getWorkingMax();
            if (this.pos >= max) {
                return 0L;
            }
            final int remaining = max - this.pos;
            if (n < remaining) {
                this.pos = SafeMath.safeLongToInt(this.pos + n);
                return n;
            }
            n = remaining;
            this.pos = max;
            return n;
        }
        
        @Override
        public IntSpliterator trySplit() {
            final int max = this.getWorkingMax();
            final int retLen = max - this.pos >> 1;
            if (retLen <= 1) {
                return null;
            }
            this.max = max;
            final int retMax;
            final int myNewPos = retMax = this.pos + retLen;
            final int oldPos = this.pos;
            this.pos = myNewPos;
            this.hasSplit = true;
            return new Spliterator(oldPos, retMax, true);
        }
    }
}
