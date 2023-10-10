// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.SafeMath;
import java.util.function.Consumer;
import java.util.Spliterator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.io.Serializable;

public class ObjectArraySet<K> extends AbstractObjectSet<K> implements Serializable, Cloneable
{
    private static final long serialVersionUID = 1L;
    private transient Object[] a;
    private int size;
    
    public ObjectArraySet(final Object[] a) {
        this.a = a;
        this.size = a.length;
    }
    
    public ObjectArraySet() {
        this.a = ObjectArrays.EMPTY_ARRAY;
    }
    
    public ObjectArraySet(final int capacity) {
        this.a = new Object[capacity];
    }
    
    public ObjectArraySet(final ObjectCollection<K> c) {
        this(c.size());
        this.addAll((Collection<? extends K>)c);
    }
    
    public ObjectArraySet(final Collection<? extends K> c) {
        this(c.size());
        this.addAll(c);
    }
    
    public ObjectArraySet(final ObjectSet<K> c) {
        this(c.size());
        int i = 0;
        for (final Object x : c) {
            this.a[i] = x;
            ++i;
        }
        this.size = i;
    }
    
    public ObjectArraySet(final Set<? extends K> c) {
        this(c.size());
        int i = 0;
        for (final K x : c) {
            this.a[i] = x;
            ++i;
        }
        this.size = i;
    }
    
    public ObjectArraySet(final Object[] a, final int size) {
        this.a = a;
        this.size = size;
        if (size > a.length) {
            throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the array size (" + a.length + ")");
        }
    }
    
    public static <K> ObjectArraySet<K> of() {
        return (ObjectArraySet<K>)ofUnchecked();
    }
    
    public static <K> ObjectArraySet<K> of(final K e) {
        return ofUnchecked(e);
    }
    
    @SafeVarargs
    public static <K> ObjectArraySet<K> of(final K... a) {
        if (a.length == 2) {
            if (Objects.equals(a[0], a[1])) {
                throw new IllegalArgumentException("Duplicate element: " + a[1]);
            }
        }
        else if (a.length > 2) {
            ObjectOpenHashSet.of(a);
        }
        return (ObjectArraySet<K>)ofUnchecked((Object[])a);
    }
    
    public static <K> ObjectArraySet<K> ofUnchecked() {
        return new ObjectArraySet<K>();
    }
    
    @SafeVarargs
    public static <K> ObjectArraySet<K> ofUnchecked(final K... a) {
        return new ObjectArraySet<K>(a);
    }
    
    private int findKey(final Object o) {
        int i = this.size;
        while (i-- != 0) {
            if (Objects.equals(this.a[i], o)) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public ObjectIterator<K> iterator() {
        return new ObjectIterator<K>() {
            int next = 0;
            
            @Override
            public boolean hasNext() {
                return this.next < ObjectArraySet.this.size;
            }
            
            @Override
            public K next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return (K)ObjectArraySet.this.a[this.next++];
            }
            
            @Override
            public void remove() {
                final int tail = ObjectArraySet.this.size-- - this.next--;
                System.arraycopy(ObjectArraySet.this.a, this.next + 1, ObjectArraySet.this.a, this.next, tail);
                ObjectArraySet.this.a[ObjectArraySet.this.size] = null;
            }
            
            @Override
            public int skip(int n) {
                if (n < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n);
                }
                final int remaining = ObjectArraySet.this.size - this.next;
                if (n < remaining) {
                    this.next += n;
                    return n;
                }
                n = remaining;
                this.next = ObjectArraySet.this.size;
                return n;
            }
        };
    }
    
    @Override
    public ObjectSpliterator<K> spliterator() {
        return new Spliterator();
    }
    
    @Override
    public boolean contains(final Object k) {
        return this.findKey(k) != -1;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean remove(final Object k) {
        final int pos = this.findKey(k);
        if (pos == -1) {
            return false;
        }
        for (int tail = this.size - pos - 1, i = 0; i < tail; ++i) {
            this.a[pos + i] = this.a[pos + i + 1];
        }
        --this.size;
        this.a[this.size] = null;
        return true;
    }
    
    @Override
    public boolean add(final K k) {
        final int pos = this.findKey(k);
        if (pos != -1) {
            return false;
        }
        if (this.size == this.a.length) {
            final Object[] b = new Object[(this.size == 0) ? 2 : (this.size * 2)];
            int i = this.size;
            while (i-- != 0) {
                b[i] = this.a[i];
            }
            this.a = b;
        }
        this.a[this.size++] = k;
        return true;
    }
    
    @Override
    public void clear() {
        Arrays.fill(this.a, 0, this.size, null);
        this.size = 0;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(this.a, this.size, (Class<? extends Object[]>)Object[].class);
    }
    
    @Override
    public <K> K[] toArray(K[] a) {
        if (a == null) {
            a = (K[])new Object[this.size];
        }
        else if (a.length < this.size) {
            a = (K[])Array.newInstance(a.getClass().getComponentType(), this.size);
        }
        System.arraycopy(this.a, 0, a, 0, this.size);
        if (a.length > this.size) {
            a[this.size] = null;
        }
        return a;
    }
    
    public ObjectArraySet<K> clone() {
        ObjectArraySet<K> c;
        try {
            c = (ObjectArraySet)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.a = this.a.clone();
        return c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeObject(this.a[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = new Object[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.a[i] = s.readObject();
        }
    }
    
    private final class Spliterator implements ObjectSpliterator<K>
    {
        boolean hasSplit;
        int pos;
        int max;
        
        public Spliterator(final ObjectArraySet x0) {
            this(x0, 0, x0.size, false);
        }
        
        private Spliterator(final int pos, final int max, final boolean hasSplit) {
            this.hasSplit = false;
            assert pos <= max : "pos " + pos + " must be <= max " + max;
            this.pos = pos;
            this.max = max;
            this.hasSplit = hasSplit;
        }
        
        private int getWorkingMax() {
            return this.hasSplit ? this.max : ObjectArraySet.this.size;
        }
        
        @Override
        public int characteristics() {
            return 16465;
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
            action.accept((Object)ObjectArraySet.this.a[this.pos++]);
            return true;
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super K> action) {
            final int max = this.getWorkingMax();
            while (this.pos < max) {
                action.accept((Object)ObjectArraySet.this.a[this.pos]);
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
