// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.SafeMath;
import java.util.ListIterator;
import java.util.Spliterator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.lang.reflect.Array;
import java.util.function.Consumer;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import java.util.stream.Collector;
import java.io.Serializable;
import java.util.RandomAccess;

public class ObjectArrayList<K> extends AbstractObjectList<K> implements RandomAccess, Cloneable, Serializable
{
    private static final long serialVersionUID = -7046029254386353131L;
    public static final int DEFAULT_INITIAL_CAPACITY = 10;
    protected final boolean wrapped;
    protected transient K[] a;
    protected int size;
    private static final Collector<Object, ?, ObjectArrayList<Object>> TO_LIST_COLLECTOR;
    
    private static final <K> K[] copyArraySafe(final K[] a, final int length) {
        if (length == 0) {
            return (K[])ObjectArrays.EMPTY_ARRAY;
        }
        return Arrays.copyOf(a, length, (Class<? extends K[]>)Object[].class);
    }
    
    private static final <K> K[] copyArrayFromSafe(final ObjectArrayList<K> l) {
        return copyArraySafe(l.a, l.size);
    }
    
    protected ObjectArrayList(final K[] a, final boolean wrapped) {
        this.a = a;
        this.wrapped = wrapped;
    }
    
    private void initArrayFromCapacity(final int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
        }
        if (capacity == 0) {
            this.a = (K[])ObjectArrays.EMPTY_ARRAY;
        }
        else {
            this.a = (K[])new Object[capacity];
        }
    }
    
    public ObjectArrayList(final int capacity) {
        this.initArrayFromCapacity(capacity);
        this.wrapped = false;
    }
    
    public ObjectArrayList() {
        this.a = (K[])ObjectArrays.DEFAULT_EMPTY_ARRAY;
        this.wrapped = false;
    }
    
    public ObjectArrayList(final Collection<? extends K> c) {
        if (c instanceof ObjectArrayList) {
            this.a = copyArrayFromSafe((ObjectArrayList)c);
            this.size = this.a.length;
        }
        else {
            this.initArrayFromCapacity(c.size());
            if (c instanceof ObjectList) {
                ((ObjectList)c).getElements(0, this.a, 0, this.size = c.size());
            }
            else {
                this.size = ObjectIterators.unwrap(c.iterator(), this.a);
            }
        }
        this.wrapped = false;
    }
    
    public ObjectArrayList(final ObjectCollection<? extends K> c) {
        if (c instanceof ObjectArrayList) {
            this.a = copyArrayFromSafe((ObjectArrayList)c);
            this.size = this.a.length;
        }
        else {
            this.initArrayFromCapacity(c.size());
            if (c instanceof ObjectList) {
                ((ObjectList)c).getElements(0, this.a, 0, this.size = c.size());
            }
            else {
                this.size = ObjectIterators.unwrap(c.iterator(), this.a);
            }
        }
        this.wrapped = false;
    }
    
    public ObjectArrayList(final ObjectList<? extends K> l) {
        if (l instanceof ObjectArrayList) {
            this.a = copyArrayFromSafe((ObjectArrayList)l);
            this.size = this.a.length;
        }
        else {
            this.initArrayFromCapacity(l.size());
            l.getElements(0, this.a, 0, this.size = l.size());
        }
        this.wrapped = false;
    }
    
    public ObjectArrayList(final K[] a) {
        this(a, 0, a.length);
    }
    
    public ObjectArrayList(final K[] a, final int offset, final int length) {
        this(length);
        System.arraycopy(a, offset, this.a, 0, length);
        this.size = length;
    }
    
    public ObjectArrayList(final Iterator<? extends K> i) {
        this();
        while (i.hasNext()) {
            this.add(i.next());
        }
    }
    
    public ObjectArrayList(final ObjectIterator<? extends K> i) {
        this();
        while (i.hasNext()) {
            this.add(i.next());
        }
    }
    
    public K[] elements() {
        return this.a;
    }
    
    public static <K> ObjectArrayList<K> wrap(final K[] a, final int length) {
        if (length > a.length) {
            throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + a.length + ")");
        }
        final ObjectArrayList<K> l = new ObjectArrayList<K>(a, true);
        l.size = length;
        return l;
    }
    
    public static <K> ObjectArrayList<K> wrap(final K[] a) {
        return wrap(a, a.length);
    }
    
    public static <K> ObjectArrayList<K> of() {
        return new ObjectArrayList<K>();
    }
    
    @SafeVarargs
    public static <K> ObjectArrayList<K> of(final K... init) {
        return (ObjectArrayList<K>)wrap((Object[])init);
    }
    
    ObjectArrayList<K> combine(final ObjectArrayList<? extends K> toAddFrom) {
        this.addAll(toAddFrom);
        return this;
    }
    
    public static <K> Collector<K, ?, ObjectArrayList<K>> toList() {
        return (Collector<K, ?, ObjectArrayList<K>>)ObjectArrayList.TO_LIST_COLLECTOR;
    }
    
    public static <K> Collector<K, ?, ObjectArrayList<K>> toListWithExpectedSize(final int expectedSize) {
        if (expectedSize <= 10) {
            return toList();
        }
        final Object o3;
        return (Collector<K, ?, ObjectArrayList<K>>)Collector.of(new ObjectCollections.SizeDecreasingSupplier<Object, Object>(expectedSize, size -> {
            if (size <= 10) {
                // new(com.viaversion.viaversion.libs.fastutil.objects.ObjectArrayList.class)
                new ObjectArrayList();
            }
            else {
                // new(com.viaversion.viaversion.libs.fastutil.objects.ObjectArrayList.class)
                new ObjectArrayList(size);
            }
            return o3;
        }), ObjectArrayList::add, ObjectArrayList::combine, new Collector.Characteristics[0]);
    }
    
    public void ensureCapacity(final int capacity) {
        if (capacity <= this.a.length || (this.a == ObjectArrays.DEFAULT_EMPTY_ARRAY && capacity <= 10)) {
            return;
        }
        if (this.wrapped) {
            this.a = ObjectArrays.ensureCapacity(this.a, capacity, this.size);
        }
        else if (capacity > this.a.length) {
            final Object[] t = new Object[capacity];
            System.arraycopy(this.a, 0, t, 0, this.size);
            this.a = (K[])t;
        }
        assert this.size <= this.a.length;
    }
    
    private void grow(int capacity) {
        if (capacity <= this.a.length) {
            return;
        }
        if (this.a != ObjectArrays.DEFAULT_EMPTY_ARRAY) {
            capacity = (int)Math.max(Math.min(this.a.length + (long)(this.a.length >> 1), 2147483639L), capacity);
        }
        else if (capacity < 10) {
            capacity = 10;
        }
        if (this.wrapped) {
            this.a = ObjectArrays.forceCapacity(this.a, capacity, this.size);
        }
        else {
            final Object[] t = new Object[capacity];
            System.arraycopy(this.a, 0, t, 0, this.size);
            this.a = (K[])t;
        }
        assert this.size <= this.a.length;
    }
    
    @Override
    public void add(final int index, final K k) {
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
    public boolean add(final K k) {
        this.grow(this.size + 1);
        this.a[this.size++] = k;
        assert this.size <= this.a.length;
        return true;
    }
    
    @Override
    public K get(final int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        return this.a[index];
    }
    
    @Override
    public int indexOf(final Object k) {
        for (int i = 0; i < this.size; ++i) {
            if (Objects.equals(k, this.a[i])) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final Object k) {
        int i = this.size;
        while (i-- != 0) {
            if (Objects.equals(k, this.a[i])) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public K remove(final int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final K old = this.a[index];
        --this.size;
        if (index != this.size) {
            System.arraycopy(this.a, index + 1, this.a, index, this.size - index);
        }
        this.a[this.size] = null;
        assert this.size <= this.a.length;
        return old;
    }
    
    @Override
    public boolean remove(final Object k) {
        final int index = this.indexOf(k);
        if (index == -1) {
            return false;
        }
        this.remove(index);
        assert this.size <= this.a.length;
        return true;
    }
    
    @Override
    public K set(final int index, final K k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final K old = this.a[index];
        this.a[index] = k;
        return old;
    }
    
    @Override
    public void clear() {
        Arrays.fill(this.a, 0, this.size, null);
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
            this.a = ObjectArrays.forceCapacity(this.a, size, this.size);
        }
        if (size > this.size) {
            Arrays.fill(this.a, this.size, size, null);
        }
        else {
            Arrays.fill(this.a, size, this.size, null);
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
        final K[] t = (K[])new Object[Math.max(n, this.size)];
        System.arraycopy(this.a, 0, t, 0, this.size);
        this.a = t;
        assert this.size <= this.a.length;
    }
    
    @Override
    public ObjectList<K> subList(final int from, final int to) {
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
    public void getElements(final int from, final Object[] a, final int offset, final int length) {
        ObjectArrays.ensureOffsetLength(a, offset, length);
        System.arraycopy(this.a, from, a, offset, length);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        com.viaversion.viaversion.libs.fastutil.Arrays.ensureFromTo(this.size, from, to);
        System.arraycopy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
        int i = to - from;
        while (i-- != 0) {
            this.a[this.size + i] = null;
        }
    }
    
    @Override
    public void addElements(final int index, final K[] a, final int offset, final int length) {
        this.ensureIndex(index);
        ObjectArrays.ensureOffsetLength(a, offset, length);
        this.grow(this.size + length);
        System.arraycopy(this.a, index, this.a, index + length, this.size - index);
        System.arraycopy(a, offset, this.a, index, length);
        this.size += length;
    }
    
    @Override
    public void setElements(final int index, final K[] a, final int offset, final int length) {
        this.ensureIndex(index);
        ObjectArrays.ensureOffsetLength(a, offset, length);
        if (index + length > this.size) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + this.size + ")");
        }
        System.arraycopy(a, offset, this.a, index, length);
    }
    
    @Override
    public void forEach(final Consumer<? super K> action) {
        for (int i = 0; i < this.size; ++i) {
            action.accept((Object)this.a[i]);
        }
    }
    
    @Override
    public boolean addAll(int index, final Collection<? extends K> c) {
        if (c instanceof ObjectList) {
            return this.addAll(index, (ObjectList)c);
        }
        this.ensureIndex(index);
        int n = c.size();
        if (n == 0) {
            return false;
        }
        this.grow(this.size + n);
        System.arraycopy(this.a, index, this.a, index + n, this.size - index);
        final Iterator<? extends K> i = c.iterator();
        this.size += n;
        while (n-- != 0) {
            this.a[index++] = (K)i.next();
        }
        assert this.size <= this.a.length;
        return true;
    }
    
    @Override
    public boolean addAll(final int index, final ObjectList<? extends K> l) {
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
    public boolean removeAll(final Collection<?> c) {
        final Object[] a = this.a;
        int j = 0;
        for (int i = 0; i < this.size; ++i) {
            if (!c.contains(a[i])) {
                a[j++] = a[i];
            }
        }
        Arrays.fill(a, j, this.size, null);
        final boolean modified = this.size != j;
        this.size = j;
        return modified;
    }
    
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(this.a, this.size(), (Class<? extends Object[]>)Object[].class);
    }
    
    @Override
    public <K> K[] toArray(K[] a) {
        if (a == null) {
            a = (K[])new Object[this.size()];
        }
        else if (a.length < this.size()) {
            a = (K[])Array.newInstance(a.getClass().getComponentType(), this.size());
        }
        System.arraycopy(this.a, 0, a, 0, this.size());
        if (a.length > this.size()) {
            a[this.size()] = null;
        }
        return a;
    }
    
    @Override
    public ObjectListIterator<K> listIterator(final int index) {
        this.ensureIndex(index);
        return new ObjectListIterator<K>() {
            int pos = index;
            int last = -1;
            
            @Override
            public boolean hasNext() {
                return this.pos < ObjectArrayList.this.size;
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            @Override
            public K next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final K[] a = ObjectArrayList.this.a;
                final int last = this.pos++;
                this.last = last;
                return a[last];
            }
            
            @Override
            public K previous() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final K[] a = ObjectArrayList.this.a;
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
            public void add(final K k) {
                ObjectArrayList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            @Override
            public void set(final K k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                ObjectArrayList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                ObjectArrayList.this.remove(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
            
            @Override
            public void forEachRemaining(final Consumer<? super K> action) {
                while (this.pos < ObjectArrayList.this.size) {
                    final K[] a = ObjectArrayList.this.a;
                    final int last = this.pos++;
                    this.last = last;
                    action.accept((Object)a[last]);
                }
            }
            
            @Override
            public int back(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                final int remaining = ObjectArrayList.this.size - this.pos;
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
                final int remaining = ObjectArrayList.this.size - this.pos;
                if (n < remaining) {
                    this.pos += n;
                }
                else {
                    n = remaining;
                    this.pos = ObjectArrayList.this.size;
                }
                this.last = this.pos - 1;
                return n;
            }
        };
    }
    
    @Override
    public ObjectSpliterator<K> spliterator() {
        return new Spliterator();
    }
    
    @Override
    public void sort(final Comparator<? super K> comp) {
        if (comp == null) {
            ObjectArrays.stableSort(this.a, 0, this.size);
        }
        else {
            ObjectArrays.stableSort(this.a, 0, this.size, (Comparator<K>)comp);
        }
    }
    
    @Override
    public void unstableSort(final Comparator<? super K> comp) {
        if (comp == null) {
            ObjectArrays.unstableSort(this.a, 0, this.size);
        }
        else {
            ObjectArrays.unstableSort(this.a, 0, this.size, (Comparator<K>)comp);
        }
    }
    
    public ObjectArrayList<K> clone() {
        ObjectArrayList<K> cloned = null;
        if (this.getClass() == ObjectArrayList.class) {
            cloned = new ObjectArrayList<K>(copyArraySafe(this.a, this.size), false);
            cloned.size = this.size;
        }
        else {
            try {
                cloned = (ObjectArrayList)super.clone();
            }
            catch (CloneNotSupportedException err) {
                throw new InternalError(err);
            }
            cloned.a = copyArraySafe(this.a, this.size);
        }
        return cloned;
    }
    
    public boolean equals(final ObjectArrayList<K> l) {
        if (l == this) {
            return true;
        }
        int s = this.size();
        if (s != l.size()) {
            return false;
        }
        final K[] a1 = this.a;
        final K[] a2 = l.a;
        if (a1 == a2 && s == l.size()) {
            return true;
        }
        while (s-- != 0) {
            if (!Objects.equals(a1[s], a2[s])) {
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
        if (o instanceof ObjectArrayList) {
            return this.equals((ObjectArrayList)o);
        }
        if (o instanceof SubList) {
            return ((SubList)o).equals(this);
        }
        return super.equals(o);
    }
    
    public int compareTo(final ObjectArrayList<? extends K> l) {
        final int s1 = this.size();
        final int s2 = l.size();
        final K[] a1 = this.a;
        final K[] a2 = (K[])l.a;
        int i;
        for (i = 0; i < s1 && i < s2; ++i) {
            final K e1 = a1[i];
            final K e2 = a2[i];
            final int r;
            if ((r = ((Comparable)e1).compareTo(e2)) != 0) {
                return r;
            }
        }
        return (i < s2) ? -1 : ((i < s1) ? 1 : 0);
    }
    
    @Override
    public int compareTo(final List<? extends K> l) {
        if (l instanceof ObjectArrayList) {
            return this.compareTo((ObjectArrayList)l);
        }
        if (l instanceof SubList) {
            return -((SubList)l).compareTo((List<? extends K>)this);
        }
        return super.compareTo(l);
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeObject(this.a[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = (K[])new Object[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.a[i] = (K)s.readObject();
        }
    }
    
    static {
        TO_LIST_COLLECTOR = Collector.of(ObjectArrayList::new, ObjectArrayList::add, ObjectArrayList::combine, new Collector.Characteristics[0]);
    }
    
    private class SubList extends ObjectRandomAccessSubList<K>
    {
        private static final long serialVersionUID = -3185226345314976296L;
        
        protected SubList(final int from, final int to) {
            super(ObjectArrayList.this, from, to);
        }
        
        private K[] getParentArray() {
            return ObjectArrayList.this.a;
        }
        
        @Override
        public K get(final int i) {
            this.ensureRestrictedIndex(i);
            return ObjectArrayList.this.a[i + this.from];
        }
        
        @Override
        public ObjectListIterator<K> listIterator(final int index) {
            return new SubListIterator(index);
        }
        
        @Override
        public ObjectSpliterator<K> spliterator() {
            return new SubListSpliterator();
        }
        
        boolean contentsEquals(final K[] otherA, final int otherAFrom, final int otherATo) {
            if (ObjectArrayList.this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return true;
            }
            if (otherATo - otherAFrom != this.size()) {
                return false;
            }
            int pos = this.from;
            int otherPos = otherAFrom;
            while (pos < this.to) {
                if (!Objects.equals(ObjectArrayList.this.a[pos++], otherA[otherPos++])) {
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
            if (o instanceof ObjectArrayList) {
                final ObjectArrayList<K> other = (ObjectArrayList<K>)o;
                return this.contentsEquals(other.a, 0, other.size());
            }
            if (o instanceof SubList) {
                final SubList other2 = (SubList)o;
                return this.contentsEquals(other2.getParentArray(), other2.from, other2.to);
            }
            return super.equals(o);
        }
        
        int contentsCompareTo(final K[] otherA, final int otherAFrom, final int otherATo) {
            int i = this.from;
            for (int j = otherAFrom; i < this.to && i < otherATo; ++i, ++j) {
                final K e1 = ObjectArrayList.this.a[i];
                final K e2 = otherA[j];
                final int r;
                if ((r = ((Comparable)e1).compareTo(e2)) != 0) {
                    return r;
                }
            }
            return (i < otherATo) ? -1 : ((i < this.to) ? 1 : 0);
        }
        
        @Override
        public int compareTo(final List<? extends K> l) {
            if (l instanceof ObjectArrayList) {
                final ObjectArrayList<K> other = (ObjectArrayList)l;
                return this.contentsCompareTo(other.a, 0, other.size());
            }
            if (l instanceof SubList) {
                final SubList other2 = (SubList)l;
                return this.contentsCompareTo(other2.getParentArray(), other2.from, other2.to);
            }
            return super.compareTo(l);
        }
        
        private final class SubListIterator extends ObjectIterators.AbstractIndexBasedListIterator<K>
        {
            SubListIterator(final int index) {
                super(0, index);
            }
            
            @Override
            protected final K get(final int i) {
                return ObjectArrayList.this.a[SubList.this.from + i];
            }
            
            @Override
            protected final void add(final int i, final K k) {
                SubList.this.add(i, k);
            }
            
            @Override
            protected final void set(final int i, final K k) {
                SubList.this.set(i, k);
            }
            
            @Override
            protected final void remove(final int i) {
                SubList.this.remove(i);
            }
            
            @Override
            protected final int getMaxPos() {
                return SubList.this.to - SubList.this.from;
            }
            
            @Override
            public K next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final K[] a = ObjectArrayList.this.a;
                final int from = SubList.this.from;
                final int lastReturned = this.pos++;
                this.lastReturned = lastReturned;
                return a[from + lastReturned];
            }
            
            @Override
            public K previous() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final K[] a = ObjectArrayList.this.a;
                final int from = SubList.this.from;
                final int n = this.pos - 1;
                this.pos = n;
                this.lastReturned = n;
                return a[from + n];
            }
            
            @Override
            public void forEachRemaining(final Consumer<? super K> action) {
                final int max = SubList.this.to - SubList.this.from;
                while (this.pos < max) {
                    final K[] a = ObjectArrayList.this.a;
                    final int from = SubList.this.from;
                    final int lastReturned = this.pos++;
                    this.lastReturned = lastReturned;
                    action.accept((Object)a[from + lastReturned]);
                }
            }
        }
        
        private final class SubListSpliterator extends ObjectSpliterators.LateBindingSizeIndexBasedSpliterator<K>
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
            protected final K get(final int i) {
                return ObjectArrayList.this.a[i];
            }
            
            @Override
            protected final SubListSpliterator makeForSplit(final int pos, final int maxPos) {
                return new SubListSpliterator(pos, maxPos);
            }
            
            @Override
            public boolean tryAdvance(final Consumer<? super K> action) {
                if (this.pos >= this.getMaxPos()) {
                    return false;
                }
                action.accept((Object)ObjectArrayList.this.a[this.pos++]);
                return true;
            }
            
            @Override
            public void forEachRemaining(final Consumer<? super K> action) {
                final int max = this.getMaxPos();
                while (this.pos < max) {
                    action.accept((Object)ObjectArrayList.this.a[this.pos++]);
                }
            }
        }
    }
    
    private final class Spliterator implements ObjectSpliterator<K>
    {
        boolean hasSplit;
        int pos;
        int max;
        
        public Spliterator(final ObjectArrayList list) {
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
            return this.hasSplit ? this.max : ObjectArrayList.this.size;
        }
        
        @Override
        public int characteristics() {
            return 16464;
        }
        
        @Override
        public long estimateSize() {
            return this.getWorkingMax() - this.pos;
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super K> action) {
            if (this.pos >= this.getWorkingMax()) {
                return false;
            }
            action.accept((Object)ObjectArrayList.this.a[this.pos++]);
            return true;
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super K> action) {
            final int max = this.getWorkingMax();
            while (this.pos < max) {
                action.accept((Object)ObjectArrayList.this.a[this.pos]);
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
        public ObjectSpliterator<K> trySplit() {
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
