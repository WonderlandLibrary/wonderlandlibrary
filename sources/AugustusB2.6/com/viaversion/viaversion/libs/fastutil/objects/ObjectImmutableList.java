// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.io.ObjectStreamException;
import java.io.InvalidObjectException;
import com.viaversion.viaversion.libs.fastutil.SafeMath;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.lang.reflect.Array;
import java.util.function.Consumer;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import java.util.stream.Collector;
import java.io.Serializable;
import java.util.RandomAccess;

public class ObjectImmutableList<K> extends ObjectLists.ImmutableListBase<K> implements ObjectList<K>, RandomAccess, Cloneable, Serializable
{
    private static final long serialVersionUID = 0L;
    static final ObjectImmutableList EMPTY;
    private final K[] a;
    private static final Collector<Object, ?, ObjectImmutableList<Object>> TO_LIST_COLLECTOR;
    
    private static final <K> K[] emptyArray() {
        return (K[])ObjectArrays.EMPTY_ARRAY;
    }
    
    public ObjectImmutableList(final K[] a) {
        this.a = a;
    }
    
    public ObjectImmutableList(final Collection<? extends K> c) {
        this(c.isEmpty() ? emptyArray() : ObjectIterators.unwrap((Iterator<?>)c.iterator()));
    }
    
    public ObjectImmutableList(final ObjectCollection<? extends K> c) {
        this(c.isEmpty() ? emptyArray() : ObjectIterators.unwrap((Iterator<?>)c.iterator()));
    }
    
    public ObjectImmutableList(final ObjectList<? extends K> l) {
        this(l.isEmpty() ? emptyArray() : new Object[l.size()]);
        l.getElements(0, this.a, 0, l.size());
    }
    
    public ObjectImmutableList(final K[] a, final int offset, final int length) {
        this((length == 0) ? emptyArray() : new Object[length]);
        System.arraycopy(a, offset, this.a, 0, length);
    }
    
    public ObjectImmutableList(final ObjectIterator<? extends K> i) {
        this(i.hasNext() ? ObjectIterators.unwrap((Iterator<?>)i) : emptyArray());
    }
    
    public static <K> ObjectImmutableList<K> of() {
        return (ObjectImmutableList<K>)ObjectImmutableList.EMPTY;
    }
    
    @SafeVarargs
    public static <K> ObjectImmutableList<K> of(final K... init) {
        return (init.length == 0) ? of() : new ObjectImmutableList<K>(init);
    }
    
    private static <K> ObjectImmutableList<K> convertTrustedToImmutableList(final ObjectArrayList<K> arrayList) {
        if (arrayList.isEmpty()) {
            return of();
        }
        K[] backingArray = arrayList.elements();
        if (arrayList.size() != backingArray.length) {
            backingArray = Arrays.copyOf(backingArray, arrayList.size());
        }
        return new ObjectImmutableList<K>(backingArray);
    }
    
    public static <K> Collector<K, ?, ObjectImmutableList<K>> toList() {
        return (Collector<K, ?, ObjectImmutableList<K>>)ObjectImmutableList.TO_LIST_COLLECTOR;
    }
    
    public static <K> Collector<K, ?, ObjectImmutableList<K>> toListWithExpectedSize(final int expectedSize) {
        if (expectedSize <= 10) {
            return toList();
        }
        final Object o3;
        return Collector.of(new ObjectCollections.SizeDecreasingSupplier<Object, Object>(expectedSize, size -> {
            if (size <= 10) {
                // new(com.viaversion.viaversion.libs.fastutil.objects.ObjectArrayList.class)
                new ObjectArrayList();
            }
            else {
                // new(com.viaversion.viaversion.libs.fastutil.objects.ObjectArrayList.class)
                new ObjectArrayList(size);
            }
            return o3;
        }), ObjectArrayList::add, ObjectArrayList::combine, ObjectImmutableList::convertTrustedToImmutableList, new Collector.Characteristics[0]);
    }
    
    @Override
    public K get(final int index) {
        if (index >= this.a.length) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.a.length + ")");
        }
        return this.a[index];
    }
    
    @Override
    public int indexOf(final Object k) {
        for (int i = 0, size = this.a.length; i < size; ++i) {
            if (Objects.equals(k, this.a[i])) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final Object k) {
        int i = this.a.length;
        while (i-- != 0) {
            if (Objects.equals(k, this.a[i])) {
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
    public void getElements(final int from, final Object[] a, final int offset, final int length) {
        ObjectArrays.ensureOffsetLength(a, offset, length);
        System.arraycopy(this.a, from, a, offset, length);
    }
    
    @Override
    public void forEach(final Consumer<? super K> action) {
        for (int i = 0; i < this.a.length; ++i) {
            action.accept((Object)this.a[i]);
        }
    }
    
    @Override
    public Object[] toArray() {
        if (this.a.getClass().equals(Object[].class)) {
            return this.a.clone();
        }
        return Arrays.copyOf(this.a, this.a.length, (Class<? extends Object[]>)Object[].class);
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
            
            @Override
            public boolean hasNext() {
                return this.pos < ObjectImmutableList.this.a.length;
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
                return ObjectImmutableList.this.a[this.pos++];
            }
            
            @Override
            public K previous() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final Object[] access$000 = ObjectImmutableList.this.a;
                final int pos = this.pos - 1;
                this.pos = pos;
                return (K)access$000[pos];
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
            public void forEachRemaining(final Consumer<? super K> action) {
                while (this.pos < ObjectImmutableList.this.a.length) {
                    action.accept((Object)ObjectImmutableList.this.a[this.pos++]);
                }
            }
            
            @Override
            public void add(final K k) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void set(final K k) {
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
                final int remaining = ObjectImmutableList.this.a.length - this.pos;
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
                final int remaining = ObjectImmutableList.this.a.length - this.pos;
                if (n < remaining) {
                    this.pos += n;
                }
                else {
                    n = remaining;
                    this.pos = ObjectImmutableList.this.a.length;
                }
                return n;
            }
        };
    }
    
    @Override
    public ObjectSpliterator<K> spliterator() {
        return new Spliterator();
    }
    
    @Override
    public ObjectList<K> subList(final int from, final int to) {
        if (from == 0 && to == this.size()) {
            return this;
        }
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from == to) {
            return (ObjectList<K>)ObjectImmutableList.EMPTY;
        }
        if (from > to) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new ImmutableSubList<K>(this, from, to);
    }
    
    public ObjectImmutableList<K> clone() {
        return this;
    }
    
    public boolean equals(final ObjectImmutableList<K> l) {
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
        final K[] a1 = this.a;
        final K[] a2 = l.a;
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
        if (o instanceof ObjectImmutableList) {
            return this.equals((ObjectImmutableList)o);
        }
        if (o instanceof ImmutableSubList) {
            return ((ImmutableSubList)o).equals(this);
        }
        return super.equals(o);
    }
    
    public int compareTo(final ObjectImmutableList<? extends K> l) {
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
        if (l instanceof ObjectImmutableList) {
            return this.compareTo((ObjectImmutableList)l);
        }
        if (l instanceof ImmutableSubList) {
            final ImmutableSubList<K> other = (ImmutableSubList<K>)(ImmutableSubList)l;
            return -other.compareTo((List<? extends K>)this);
        }
        return super.compareTo(l);
    }
    
    static {
        EMPTY = new ObjectImmutableList((K[])ObjectArrays.EMPTY_ARRAY);
        TO_LIST_COLLECTOR = Collector.of(ObjectArrayList::new, ObjectArrayList::add, ObjectArrayList::combine, ObjectImmutableList::convertTrustedToImmutableList, new Collector.Characteristics[0]);
    }
    
    private final class Spliterator implements ObjectSpliterator<K>
    {
        int pos;
        int max;
        
        public Spliterator(final ObjectImmutableList x0) {
            this(x0, 0, x0.a.length);
        }
        
        private Spliterator(final int pos, final int max) {
            assert pos <= max : "pos " + pos + " must be <= max " + max;
            this.pos = pos;
            this.max = max;
        }
        
        @Override
        public int characteristics() {
            return 17488;
        }
        
        @Override
        public long estimateSize() {
            return this.max - this.pos;
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super K> action) {
            if (this.pos >= this.max) {
                return false;
            }
            action.accept((Object)ObjectImmutableList.this.a[this.pos++]);
            return true;
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super K> action) {
            while (this.pos < this.max) {
                action.accept((Object)ObjectImmutableList.this.a[this.pos]);
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
        public ObjectSpliterator<K> trySplit() {
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
    
    private static final class ImmutableSubList<K> extends ObjectLists.ImmutableListBase<K> implements RandomAccess, Serializable
    {
        private static final long serialVersionUID = 7054639518438982401L;
        final ObjectImmutableList<K> innerList;
        final int from;
        final int to;
        final transient K[] a;
        
        ImmutableSubList(final ObjectImmutableList<K> innerList, final int from, final int to) {
            this.innerList = innerList;
            this.from = from;
            this.to = to;
            this.a = (K[])((ObjectImmutableList<Object>)innerList).a;
        }
        
        @Override
        public K get(final int index) {
            this.ensureRestrictedIndex(index);
            return this.a[index + this.from];
        }
        
        @Override
        public int indexOf(final Object k) {
            for (int i = this.from; i < this.to; ++i) {
                if (Objects.equals(k, this.a[i])) {
                    return i - this.from;
                }
            }
            return -1;
        }
        
        @Override
        public int lastIndexOf(final Object k) {
            int i = this.to;
            while (i-- != this.from) {
                if (Objects.equals(k, this.a[i])) {
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
        public void getElements(final int fromSublistIndex, final Object[] a, final int offset, final int length) {
            ObjectArrays.ensureOffsetLength(a, offset, length);
            this.ensureRestrictedIndex(fromSublistIndex);
            if (this.from + length > this.to) {
                throw new IndexOutOfBoundsException("Final index " + (this.from + length) + " (startingIndex: " + this.from + " + length: " + length + ") is greater then list length " + this.size());
            }
            System.arraycopy(this.a, fromSublistIndex + this.from, a, offset, length);
        }
        
        @Override
        public void forEach(final Consumer<? super K> action) {
            for (int i = this.from; i < this.to; ++i) {
                action.accept((Object)this.a[i]);
            }
        }
        
        @Override
        public Object[] toArray() {
            return Arrays.copyOfRange(this.a, this.from, this.to, (Class<? extends Object[]>)Object[].class);
        }
        
        @Override
        public <K> K[] toArray(K[] a) {
            final int size = this.size();
            if (a == null) {
                a = (K[])new Object[size];
            }
            else if (a.length < size) {
                a = (K[])Array.newInstance(a.getClass().getComponentType(), size);
            }
            System.arraycopy(this.a, this.from, a, 0, size);
            if (a.length > size) {
                a[size] = null;
            }
            return a;
        }
        
        @Override
        public ObjectListIterator<K> listIterator(final int index) {
            this.ensureIndex(index);
            return new ObjectListIterator<K>() {
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
                public K next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return ImmutableSubList.this.a[this.pos++ + ImmutableSubList.this.from];
                }
                
                @Override
                public K previous() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final K[] a = ImmutableSubList.this.a;
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
                public void forEachRemaining(final Consumer<? super K> action) {
                    while (this.pos < ImmutableSubList.this.to) {
                        action.accept((Object)ImmutableSubList.this.a[this.pos++ + ImmutableSubList.this.from]);
                    }
                }
                
                @Override
                public void add(final K k) {
                    throw new UnsupportedOperationException();
                }
                
                @Override
                public void set(final K k) {
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
        public ObjectSpliterator<K> spliterator() {
            return new SubListSpliterator();
        }
        
        boolean contentsEquals(final K[] otherA, final int otherAFrom, final int otherATo) {
            if (this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return true;
            }
            if (otherATo - otherAFrom != this.size()) {
                return false;
            }
            int pos = this.from;
            int otherPos = otherAFrom;
            while (pos < this.to) {
                if (!Objects.equals(this.a[pos++], otherA[otherPos++])) {
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
            if (o instanceof ObjectImmutableList) {
                final ObjectImmutableList<K> other = (ObjectImmutableList<K>)o;
                return this.contentsEquals(((ObjectImmutableList<Object>)other).a, 0, other.size());
            }
            if (o instanceof ImmutableSubList) {
                final ImmutableSubList<K> other2 = (ImmutableSubList<K>)o;
                return this.contentsEquals(other2.a, other2.from, other2.to);
            }
            return super.equals(o);
        }
        
        int contentsCompareTo(final K[] otherA, final int otherAFrom, final int otherATo) {
            int i = this.from;
            for (int j = otherAFrom; i < this.to && i < otherATo; ++i, ++j) {
                final K e1 = this.a[i];
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
            if (l instanceof ObjectImmutableList) {
                final ObjectImmutableList<K> other = (ObjectImmutableList<K>)(ObjectImmutableList)l;
                return this.contentsCompareTo(((ObjectImmutableList<Object>)other).a, 0, other.size());
            }
            if (l instanceof ImmutableSubList) {
                final ImmutableSubList<K> other2 = (ImmutableSubList)l;
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
        public ObjectList<K> subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from == to) {
                return (ObjectList<K>)ObjectImmutableList.EMPTY;
            }
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new ImmutableSubList((ObjectImmutableList<Object>)this.innerList, from + this.from, to + this.from);
        }
        
        private final class SubListSpliterator extends ObjectSpliterators.EarlyBindingSizeIndexBasedSpliterator<K>
        {
            SubListSpliterator() {
                super(ImmutableSubList.this.from, ImmutableSubList.this.to);
            }
            
            private SubListSpliterator(final int pos, final int maxPos) {
                super(pos, maxPos);
            }
            
            @Override
            protected final K get(final int i) {
                return ImmutableSubList.this.a[i];
            }
            
            @Override
            protected final SubListSpliterator makeForSplit(final int pos, final int maxPos) {
                return new SubListSpliterator(pos, maxPos);
            }
            
            @Override
            public boolean tryAdvance(final Consumer<? super K> action) {
                if (this.pos >= this.maxPos) {
                    return false;
                }
                action.accept((Object)ImmutableSubList.this.a[this.pos++]);
                return true;
            }
            
            @Override
            public void forEachRemaining(final Consumer<? super K> action) {
                final int max = this.maxPos;
                while (this.pos < max) {
                    action.accept((Object)ImmutableSubList.this.a[this.pos++]);
                }
            }
            
            @Override
            public int characteristics() {
                return 17488;
            }
        }
    }
}
