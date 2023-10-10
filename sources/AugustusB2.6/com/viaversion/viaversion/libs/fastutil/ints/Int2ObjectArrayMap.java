// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectCollection;
import java.util.function.IntConsumer;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterators;
import java.util.Spliterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import java.util.function.Consumer;
import java.util.NoSuchElementException;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;
import java.util.Iterator;
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrays;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;
import java.io.Serializable;

public class Int2ObjectArrayMap<V> extends AbstractInt2ObjectMap<V> implements Serializable, Cloneable
{
    private static final long serialVersionUID = 1L;
    private transient int[] key;
    private transient Object[] value;
    private int size;
    private transient Int2ObjectMap.FastEntrySet<V> entries;
    private transient IntSet keys;
    private transient ObjectCollection<V> values;
    
    public Int2ObjectArrayMap(final int[] key, final Object[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
    }
    
    public Int2ObjectArrayMap() {
        this.key = IntArrays.EMPTY_ARRAY;
        this.value = ObjectArrays.EMPTY_ARRAY;
    }
    
    public Int2ObjectArrayMap(final int capacity) {
        this.key = new int[capacity];
        this.value = new Object[capacity];
    }
    
    public Int2ObjectArrayMap(final Int2ObjectMap<V> m) {
        this(m.size());
        int i = 0;
        for (final Int2ObjectMap.Entry<V> e : m.int2ObjectEntrySet()) {
            this.key[i] = e.getIntKey();
            this.value[i] = e.getValue();
            ++i;
        }
        this.size = i;
    }
    
    public Int2ObjectArrayMap(final Map<? extends Integer, ? extends V> m) {
        this(m.size());
        int i = 0;
        for (final Map.Entry<? extends Integer, ? extends V> e : m.entrySet()) {
            this.key[i] = (int)e.getKey();
            this.value[i] = e.getValue();
            ++i;
        }
        this.size = i;
    }
    
    public Int2ObjectArrayMap(final int[] key, final Object[] value, final int size) {
        this.key = key;
        this.value = value;
        this.size = size;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
        if (size > key.length) {
            throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the backing-arrays size (" + key.length + ")");
        }
    }
    
    @Override
    public Int2ObjectMap.FastEntrySet<V> int2ObjectEntrySet() {
        if (this.entries == null) {
            this.entries = new EntrySet();
        }
        return this.entries;
    }
    
    private int findKey(final int k) {
        final int[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] == k) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public V get(final int k) {
        final int[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] == k) {
                return (V)this.value[i];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public void clear() {
        int i = this.size;
        while (i-- != 0) {
            this.value[i] = null;
        }
        this.size = 0;
    }
    
    @Override
    public boolean containsKey(final int k) {
        return this.findKey(k) != -1;
    }
    
    @Override
    public boolean containsValue(final Object v) {
        int i = this.size;
        while (i-- != 0) {
            if (Objects.equals(this.value[i], v)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    @Override
    public V put(final int k, final V v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final V oldValue = (V)this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final int[] newKey = new int[(this.size == 0) ? 2 : (this.size * 2)];
            final Object[] newValue = new Object[(this.size == 0) ? 2 : (this.size * 2)];
            int i = this.size;
            while (i-- != 0) {
                newKey[i] = this.key[i];
                newValue[i] = this.value[i];
            }
            this.key = newKey;
            this.value = newValue;
        }
        this.key[this.size] = k;
        this.value[this.size] = v;
        ++this.size;
        return this.defRetValue;
    }
    
    @Override
    public V remove(final int k) {
        final int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        final V oldValue = (V)this.value[oldPos];
        final int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        this.value[this.size] = null;
        return oldValue;
    }
    
    @Override
    public IntSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public ObjectCollection<V> values() {
        if (this.values == null) {
            this.values = new ValuesCollection();
        }
        return this.values;
    }
    
    public Int2ObjectArrayMap<V> clone() {
        Int2ObjectArrayMap<V> c;
        try {
            c = (Int2ObjectArrayMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.key = this.key.clone();
        c.value = this.value.clone();
        c.entries = null;
        c.keys = null;
        c.values = null;
        return c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0, max = this.size; i < max; ++i) {
            s.writeInt(this.key[i]);
            s.writeObject(this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new int[this.size];
        this.value = new Object[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readInt();
            this.value[i] = s.readObject();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Int2ObjectMap.Entry<V>> implements Int2ObjectMap.FastEntrySet<V>
    {
        @Override
        public ObjectIterator<Int2ObjectMap.Entry<V>> iterator() {
            return new ObjectIterator<Int2ObjectMap.Entry<V>>() {
                int curr = -1;
                int next = 0;
                
                @Override
                public boolean hasNext() {
                    return this.next < Int2ObjectArrayMap.this.size;
                }
                
                @Override
                public Int2ObjectMap.Entry<V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final int[] access$100 = Int2ObjectArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry<V>(access$100[next], Int2ObjectArrayMap.this.value[this.next++]);
                }
                
                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Int2ObjectArrayMap.this.size-- - this.next--;
                    System.arraycopy(Int2ObjectArrayMap.this.key, this.next + 1, Int2ObjectArrayMap.this.key, this.next, tail);
                    System.arraycopy(Int2ObjectArrayMap.this.value, this.next + 1, Int2ObjectArrayMap.this.value, this.next, tail);
                    Int2ObjectArrayMap.this.value[Int2ObjectArrayMap.this.size] = null;
                }
                
                @Override
                public void forEachRemaining(final Consumer<? super Int2ObjectMap.Entry<V>> action) {
                    final int max = Int2ObjectArrayMap.this.size;
                    while (this.next < max) {
                        final int[] access$100 = Int2ObjectArrayMap.this.key;
                        final int next = this.next;
                        this.curr = next;
                        action.accept((Object)new BasicEntry(access$100[next], Int2ObjectArrayMap.this.value[this.next++]));
                    }
                }
            };
        }
        
        @Override
        public ObjectIterator<Int2ObjectMap.Entry<V>> fastIterator() {
            return new ObjectIterator<Int2ObjectMap.Entry<V>>() {
                int next = 0;
                int curr = -1;
                final BasicEntry<V> entry = new BasicEntry<V>();
                
                @Override
                public boolean hasNext() {
                    return this.next < Int2ObjectArrayMap.this.size;
                }
                
                @Override
                public Int2ObjectMap.Entry<V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry<V> entry = this.entry;
                    final int[] access$100 = Int2ObjectArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = access$100[next];
                    this.entry.value = (V)Int2ObjectArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Int2ObjectArrayMap.this.size-- - this.next--;
                    System.arraycopy(Int2ObjectArrayMap.this.key, this.next + 1, Int2ObjectArrayMap.this.key, this.next, tail);
                    System.arraycopy(Int2ObjectArrayMap.this.value, this.next + 1, Int2ObjectArrayMap.this.value, this.next, tail);
                    Int2ObjectArrayMap.this.value[Int2ObjectArrayMap.this.size] = null;
                }
                
                @Override
                public void forEachRemaining(final Consumer<? super Int2ObjectMap.Entry<V>> action) {
                    final int max = Int2ObjectArrayMap.this.size;
                    while (this.next < max) {
                        final BasicEntry<V> entry = this.entry;
                        final int[] access$100 = Int2ObjectArrayMap.this.key;
                        final int next = this.next;
                        this.curr = next;
                        entry.key = access$100[next];
                        this.entry.value = (V)Int2ObjectArrayMap.this.value[this.next++];
                        action.accept(this.entry);
                    }
                }
            };
        }
        
        @Override
        public ObjectSpliterator<Int2ObjectMap.Entry<V>> spliterator() {
            return new EntrySetSpliterator(0, Int2ObjectArrayMap.this.size);
        }
        
        @Override
        public void forEach(final Consumer<? super Int2ObjectMap.Entry<V>> action) {
            for (int i = 0, max = Int2ObjectArrayMap.this.size; i < max; ++i) {
                action.accept((Object)new BasicEntry(Int2ObjectArrayMap.this.key[i], (V)Int2ObjectArrayMap.this.value[i]));
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Int2ObjectMap.Entry<V>> action) {
            final BasicEntry<V> entry = new BasicEntry<V>();
            for (int i = 0, max = Int2ObjectArrayMap.this.size; i < max; ++i) {
                entry.key = Int2ObjectArrayMap.this.key[i];
                entry.value = (V)Int2ObjectArrayMap.this.value[i];
                action.accept(entry);
            }
        }
        
        @Override
        public int size() {
            return Int2ObjectArrayMap.this.size;
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                return false;
            }
            final int k = (int)e.getKey();
            return Int2ObjectArrayMap.this.containsKey(k) && Objects.equals(Int2ObjectArrayMap.this.get(k), e.getValue());
        }
        
        @Override
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                return false;
            }
            final int k = (int)e.getKey();
            final V v = (V)e.getValue();
            final int oldPos = Int2ObjectArrayMap.this.findKey(k);
            if (oldPos == -1 || !Objects.equals(v, Int2ObjectArrayMap.this.value[oldPos])) {
                return false;
            }
            final int tail = Int2ObjectArrayMap.this.size - oldPos - 1;
            System.arraycopy(Int2ObjectArrayMap.this.key, oldPos + 1, Int2ObjectArrayMap.this.key, oldPos, tail);
            System.arraycopy(Int2ObjectArrayMap.this.value, oldPos + 1, Int2ObjectArrayMap.this.value, oldPos, tail);
            Int2ObjectArrayMap.this.size--;
            Int2ObjectArrayMap.this.value[Int2ObjectArrayMap.this.size] = null;
            return true;
        }
        
        final class EntrySetSpliterator extends ObjectSpliterators.EarlyBindingSizeIndexBasedSpliterator<Int2ObjectMap.Entry<V>> implements ObjectSpliterator<Int2ObjectMap.Entry<V>>
        {
            EntrySetSpliterator(final int pos, final int maxPos) {
                super(pos, maxPos);
            }
            
            @Override
            public int characteristics() {
                return 16465;
            }
            
            @Override
            protected final Int2ObjectMap.Entry<V> get(final int location) {
                return new BasicEntry<V>(Int2ObjectArrayMap.this.key[location], (V)Int2ObjectArrayMap.this.value[location]);
            }
            
            @Override
            protected final EntrySetSpliterator makeForSplit(final int pos, final int maxPos) {
                return new EntrySetSpliterator(pos, maxPos);
            }
        }
    }
    
    private final class KeySet extends AbstractIntSet
    {
        @Override
        public boolean contains(final int k) {
            return Int2ObjectArrayMap.this.findKey(k) != -1;
        }
        
        @Override
        public boolean remove(final int k) {
            final int oldPos = Int2ObjectArrayMap.this.findKey(k);
            if (oldPos == -1) {
                return false;
            }
            final int tail = Int2ObjectArrayMap.this.size - oldPos - 1;
            System.arraycopy(Int2ObjectArrayMap.this.key, oldPos + 1, Int2ObjectArrayMap.this.key, oldPos, tail);
            System.arraycopy(Int2ObjectArrayMap.this.value, oldPos + 1, Int2ObjectArrayMap.this.value, oldPos, tail);
            Int2ObjectArrayMap.this.size--;
            Int2ObjectArrayMap.this.value[Int2ObjectArrayMap.this.size] = null;
            return true;
        }
        
        @Override
        public IntIterator iterator() {
            return new IntIterator() {
                int pos = 0;
                
                @Override
                public boolean hasNext() {
                    return this.pos < Int2ObjectArrayMap.this.size;
                }
                
                @Override
                public int nextInt() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return Int2ObjectArrayMap.this.key[this.pos++];
                }
                
                @Override
                public void remove() {
                    if (this.pos == 0) {
                        throw new IllegalStateException();
                    }
                    final int tail = Int2ObjectArrayMap.this.size - this.pos;
                    System.arraycopy(Int2ObjectArrayMap.this.key, this.pos, Int2ObjectArrayMap.this.key, this.pos - 1, tail);
                    System.arraycopy(Int2ObjectArrayMap.this.value, this.pos, Int2ObjectArrayMap.this.value, this.pos - 1, tail);
                    Int2ObjectArrayMap.this.size--;
                    --this.pos;
                    Int2ObjectArrayMap.this.value[Int2ObjectArrayMap.this.size] = null;
                }
                
                @Override
                public void forEachRemaining(final IntConsumer action) {
                    final int max = Int2ObjectArrayMap.this.size;
                    while (this.pos < max) {
                        action.accept(Int2ObjectArrayMap.this.key[this.pos++]);
                    }
                }
            };
        }
        
        @Override
        public IntSpliterator spliterator() {
            return new KeySetSpliterator(0, Int2ObjectArrayMap.this.size);
        }
        
        @Override
        public void forEach(final IntConsumer action) {
            for (int i = 0, max = Int2ObjectArrayMap.this.size; i < max; ++i) {
                action.accept(Int2ObjectArrayMap.this.key[i]);
            }
        }
        
        @Override
        public int size() {
            return Int2ObjectArrayMap.this.size;
        }
        
        @Override
        public void clear() {
            Int2ObjectArrayMap.this.clear();
        }
        
        final class KeySetSpliterator extends IntSpliterators.EarlyBindingSizeIndexBasedSpliterator implements IntSpliterator
        {
            KeySetSpliterator(final int pos, final int maxPos) {
                super(pos, maxPos);
            }
            
            @Override
            public int characteristics() {
                return 16721;
            }
            
            @Override
            protected final int get(final int location) {
                return Int2ObjectArrayMap.this.key[location];
            }
            
            @Override
            protected final KeySetSpliterator makeForSplit(final int pos, final int maxPos) {
                return new KeySetSpliterator(pos, maxPos);
            }
            
            @Override
            public void forEachRemaining(final IntConsumer action) {
                final int max = Int2ObjectArrayMap.this.size;
                while (this.pos < max) {
                    action.accept(Int2ObjectArrayMap.this.key[this.pos++]);
                }
            }
        }
    }
    
    private final class ValuesCollection extends AbstractObjectCollection<V>
    {
        @Override
        public boolean contains(final Object v) {
            return Int2ObjectArrayMap.this.containsValue(v);
        }
        
        @Override
        public ObjectIterator<V> iterator() {
            return new ObjectIterator<V>() {
                int pos = 0;
                
                @Override
                public boolean hasNext() {
                    return this.pos < Int2ObjectArrayMap.this.size;
                }
                
                @Override
                public V next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return (V)Int2ObjectArrayMap.this.value[this.pos++];
                }
                
                @Override
                public void remove() {
                    if (this.pos == 0) {
                        throw new IllegalStateException();
                    }
                    final int tail = Int2ObjectArrayMap.this.size - this.pos;
                    System.arraycopy(Int2ObjectArrayMap.this.key, this.pos, Int2ObjectArrayMap.this.key, this.pos - 1, tail);
                    System.arraycopy(Int2ObjectArrayMap.this.value, this.pos, Int2ObjectArrayMap.this.value, this.pos - 1, tail);
                    Int2ObjectArrayMap.this.size--;
                    --this.pos;
                    Int2ObjectArrayMap.this.value[Int2ObjectArrayMap.this.size] = null;
                }
                
                @Override
                public void forEachRemaining(final Consumer<? super V> action) {
                    final int max = Int2ObjectArrayMap.this.size;
                    while (this.pos < max) {
                        action.accept((Object)Int2ObjectArrayMap.this.value[this.pos++]);
                    }
                }
            };
        }
        
        @Override
        public ObjectSpliterator<V> spliterator() {
            return new ValuesSpliterator(0, Int2ObjectArrayMap.this.size);
        }
        
        @Override
        public void forEach(final Consumer<? super V> action) {
            for (int i = 0, max = Int2ObjectArrayMap.this.size; i < max; ++i) {
                action.accept((Object)Int2ObjectArrayMap.this.value[i]);
            }
        }
        
        @Override
        public int size() {
            return Int2ObjectArrayMap.this.size;
        }
        
        @Override
        public void clear() {
            Int2ObjectArrayMap.this.clear();
        }
        
        final class ValuesSpliterator extends ObjectSpliterators.EarlyBindingSizeIndexBasedSpliterator<V> implements ObjectSpliterator<V>
        {
            ValuesSpliterator(final int pos, final int maxPos) {
                super(pos, maxPos);
            }
            
            @Override
            public int characteristics() {
                return 16464;
            }
            
            @Override
            protected final V get(final int location) {
                return (V)Int2ObjectArrayMap.this.value[location];
            }
            
            @Override
            protected final ValuesSpliterator makeForSplit(final int pos, final int maxPos) {
                return new ValuesSpliterator(pos, maxPos);
            }
            
            @Override
            public void forEachRemaining(final Consumer<? super V> action) {
                final int max = Int2ObjectArrayMap.this.size;
                while (this.pos < max) {
                    action.accept((Object)Int2ObjectArrayMap.this.value[this.pos++]);
                }
            }
        }
    }
}
