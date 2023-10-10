// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterators;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterator;
import java.util.function.IntConsumer;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntCollection;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;
import java.util.Iterator;
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import java.io.Serializable;

public class Object2IntArrayMap<K> extends AbstractObject2IntMap<K> implements Serializable, Cloneable
{
    private static final long serialVersionUID = 1L;
    private transient Object[] key;
    private transient int[] value;
    private int size;
    private transient Object2IntMap.FastEntrySet<K> entries;
    private transient ObjectSet<K> keys;
    private transient IntCollection values;
    
    public Object2IntArrayMap(final Object[] key, final int[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
    }
    
    public Object2IntArrayMap() {
        this.key = ObjectArrays.EMPTY_ARRAY;
        this.value = IntArrays.EMPTY_ARRAY;
    }
    
    public Object2IntArrayMap(final int capacity) {
        this.key = new Object[capacity];
        this.value = new int[capacity];
    }
    
    public Object2IntArrayMap(final Object2IntMap<K> m) {
        this(m.size());
        int i = 0;
        for (final Object2IntMap.Entry<K> e : m.object2IntEntrySet()) {
            this.key[i] = e.getKey();
            this.value[i] = e.getIntValue();
            ++i;
        }
        this.size = i;
    }
    
    public Object2IntArrayMap(final Map<? extends K, ? extends Integer> m) {
        this(m.size());
        int i = 0;
        for (final Map.Entry<? extends K, ? extends Integer> e : m.entrySet()) {
            this.key[i] = e.getKey();
            this.value[i] = (int)e.getValue();
            ++i;
        }
        this.size = i;
    }
    
    public Object2IntArrayMap(final Object[] key, final int[] value, final int size) {
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
    public Object2IntMap.FastEntrySet<K> object2IntEntrySet() {
        if (this.entries == null) {
            this.entries = new EntrySet();
        }
        return this.entries;
    }
    
    private int findKey(final Object k) {
        final Object[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (Objects.equals(key[i], k)) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int getInt(final Object k) {
        final Object[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (Objects.equals(key[i], k)) {
                return this.value[i];
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
            this.key[i] = null;
        }
        this.size = 0;
    }
    
    @Override
    public boolean containsKey(final Object k) {
        return this.findKey(k) != -1;
    }
    
    @Override
    public boolean containsValue(final int v) {
        int i = this.size;
        while (i-- != 0) {
            if (this.value[i] == v) {
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
    public int put(final K k, final int v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final int oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final Object[] newKey = new Object[(this.size == 0) ? 2 : (this.size * 2)];
            final int[] newValue = new int[(this.size == 0) ? 2 : (this.size * 2)];
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
    public int removeInt(final Object k) {
        final int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        final int oldValue = this.value[oldPos];
        final int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        this.key[this.size] = null;
        return oldValue;
    }
    
    @Override
    public ObjectSet<K> keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public IntCollection values() {
        if (this.values == null) {
            this.values = new ValuesCollection();
        }
        return this.values;
    }
    
    public Object2IntArrayMap<K> clone() {
        Object2IntArrayMap<K> c;
        try {
            c = (Object2IntArrayMap)super.clone();
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
            s.writeObject(this.key[i]);
            s.writeInt(this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new Object[this.size];
        this.value = new int[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readObject();
            this.value[i] = s.readInt();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Object2IntMap.Entry<K>> implements Object2IntMap.FastEntrySet<K>
    {
        @Override
        public ObjectIterator<Object2IntMap.Entry<K>> iterator() {
            return new ObjectIterator<Object2IntMap.Entry<K>>() {
                int curr = -1;
                int next = 0;
                
                @Override
                public boolean hasNext() {
                    return this.next < Object2IntArrayMap.this.size;
                }
                
                @Override
                public Object2IntMap.Entry<K> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final Object[] access$100 = Object2IntArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry<K>(access$100[next], Object2IntArrayMap.this.value[this.next++]);
                }
                
                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Object2IntArrayMap.this.size-- - this.next--;
                    System.arraycopy(Object2IntArrayMap.this.key, this.next + 1, Object2IntArrayMap.this.key, this.next, tail);
                    System.arraycopy(Object2IntArrayMap.this.value, this.next + 1, Object2IntArrayMap.this.value, this.next, tail);
                    Object2IntArrayMap.this.key[Object2IntArrayMap.this.size] = null;
                }
                
                @Override
                public void forEachRemaining(final Consumer<? super Object2IntMap.Entry<K>> action) {
                    final int max = Object2IntArrayMap.this.size;
                    while (this.next < max) {
                        final Object[] access$100 = Object2IntArrayMap.this.key;
                        final int next = this.next;
                        this.curr = next;
                        action.accept((Object)new BasicEntry(access$100[next], Object2IntArrayMap.this.value[this.next++]));
                    }
                }
            };
        }
        
        @Override
        public ObjectIterator<Object2IntMap.Entry<K>> fastIterator() {
            return new ObjectIterator<Object2IntMap.Entry<K>>() {
                int next = 0;
                int curr = -1;
                final BasicEntry<K> entry = new BasicEntry<K>();
                
                @Override
                public boolean hasNext() {
                    return this.next < Object2IntArrayMap.this.size;
                }
                
                @Override
                public Object2IntMap.Entry<K> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry<K> entry = this.entry;
                    final Object[] access$100 = Object2IntArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = (K)access$100[next];
                    this.entry.value = Object2IntArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Object2IntArrayMap.this.size-- - this.next--;
                    System.arraycopy(Object2IntArrayMap.this.key, this.next + 1, Object2IntArrayMap.this.key, this.next, tail);
                    System.arraycopy(Object2IntArrayMap.this.value, this.next + 1, Object2IntArrayMap.this.value, this.next, tail);
                    Object2IntArrayMap.this.key[Object2IntArrayMap.this.size] = null;
                }
                
                @Override
                public void forEachRemaining(final Consumer<? super Object2IntMap.Entry<K>> action) {
                    final int max = Object2IntArrayMap.this.size;
                    while (this.next < max) {
                        final BasicEntry<K> entry = this.entry;
                        final Object[] access$100 = Object2IntArrayMap.this.key;
                        final int next = this.next;
                        this.curr = next;
                        entry.key = (K)access$100[next];
                        this.entry.value = Object2IntArrayMap.this.value[this.next++];
                        action.accept(this.entry);
                    }
                }
            };
        }
        
        @Override
        public ObjectSpliterator<Object2IntMap.Entry<K>> spliterator() {
            return new EntrySetSpliterator(0, Object2IntArrayMap.this.size);
        }
        
        @Override
        public void forEach(final Consumer<? super Object2IntMap.Entry<K>> action) {
            for (int i = 0, max = Object2IntArrayMap.this.size; i < max; ++i) {
                action.accept((Object)new BasicEntry((K)Object2IntArrayMap.this.key[i], Object2IntArrayMap.this.value[i]));
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Object2IntMap.Entry<K>> action) {
            final BasicEntry<K> entry = new BasicEntry<K>();
            for (int i = 0, max = Object2IntArrayMap.this.size; i < max; ++i) {
                entry.key = (K)Object2IntArrayMap.this.key[i];
                entry.value = Object2IntArrayMap.this.value[i];
                action.accept(entry);
            }
        }
        
        @Override
        public int size() {
            return Object2IntArrayMap.this.size;
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            if (e.getValue() == null || !(e.getValue() instanceof Integer)) {
                return false;
            }
            final K k = (K)e.getKey();
            return Object2IntArrayMap.this.containsKey(k) && Object2IntArrayMap.this.getInt(k) == (int)e.getValue();
        }
        
        @Override
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            if (e.getValue() == null || !(e.getValue() instanceof Integer)) {
                return false;
            }
            final K k = (K)e.getKey();
            final int v = (int)e.getValue();
            final int oldPos = Object2IntArrayMap.this.findKey(k);
            if (oldPos == -1 || v != Object2IntArrayMap.this.value[oldPos]) {
                return false;
            }
            final int tail = Object2IntArrayMap.this.size - oldPos - 1;
            System.arraycopy(Object2IntArrayMap.this.key, oldPos + 1, Object2IntArrayMap.this.key, oldPos, tail);
            System.arraycopy(Object2IntArrayMap.this.value, oldPos + 1, Object2IntArrayMap.this.value, oldPos, tail);
            Object2IntArrayMap.this.size--;
            Object2IntArrayMap.this.key[Object2IntArrayMap.this.size] = null;
            return true;
        }
        
        final class EntrySetSpliterator extends ObjectSpliterators.EarlyBindingSizeIndexBasedSpliterator<Object2IntMap.Entry<K>> implements ObjectSpliterator<Object2IntMap.Entry<K>>
        {
            EntrySetSpliterator(final int pos, final int maxPos) {
                super(pos, maxPos);
            }
            
            @Override
            public int characteristics() {
                return 16465;
            }
            
            @Override
            protected final Object2IntMap.Entry<K> get(final int location) {
                return new BasicEntry<K>((K)Object2IntArrayMap.this.key[location], Object2IntArrayMap.this.value[location]);
            }
            
            @Override
            protected final EntrySetSpliterator makeForSplit(final int pos, final int maxPos) {
                return new EntrySetSpliterator(pos, maxPos);
            }
        }
    }
    
    private final class KeySet extends AbstractObjectSet<K>
    {
        @Override
        public boolean contains(final Object k) {
            return Object2IntArrayMap.this.findKey(k) != -1;
        }
        
        @Override
        public boolean remove(final Object k) {
            final int oldPos = Object2IntArrayMap.this.findKey(k);
            if (oldPos == -1) {
                return false;
            }
            final int tail = Object2IntArrayMap.this.size - oldPos - 1;
            System.arraycopy(Object2IntArrayMap.this.key, oldPos + 1, Object2IntArrayMap.this.key, oldPos, tail);
            System.arraycopy(Object2IntArrayMap.this.value, oldPos + 1, Object2IntArrayMap.this.value, oldPos, tail);
            Object2IntArrayMap.this.size--;
            Object2IntArrayMap.this.key[Object2IntArrayMap.this.size] = null;
            return true;
        }
        
        @Override
        public ObjectIterator<K> iterator() {
            return new ObjectIterator<K>() {
                int pos = 0;
                
                @Override
                public boolean hasNext() {
                    return this.pos < Object2IntArrayMap.this.size;
                }
                
                @Override
                public K next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return (K)Object2IntArrayMap.this.key[this.pos++];
                }
                
                @Override
                public void remove() {
                    if (this.pos == 0) {
                        throw new IllegalStateException();
                    }
                    final int tail = Object2IntArrayMap.this.size - this.pos;
                    System.arraycopy(Object2IntArrayMap.this.key, this.pos, Object2IntArrayMap.this.key, this.pos - 1, tail);
                    System.arraycopy(Object2IntArrayMap.this.value, this.pos, Object2IntArrayMap.this.value, this.pos - 1, tail);
                    Object2IntArrayMap.this.size--;
                    --this.pos;
                    Object2IntArrayMap.this.key[Object2IntArrayMap.this.size] = null;
                }
                
                @Override
                public void forEachRemaining(final Consumer<? super K> action) {
                    final int max = Object2IntArrayMap.this.size;
                    while (this.pos < max) {
                        action.accept((Object)Object2IntArrayMap.this.key[this.pos++]);
                    }
                }
            };
        }
        
        @Override
        public ObjectSpliterator<K> spliterator() {
            return new KeySetSpliterator(0, Object2IntArrayMap.this.size);
        }
        
        @Override
        public void forEach(final Consumer<? super K> action) {
            for (int i = 0, max = Object2IntArrayMap.this.size; i < max; ++i) {
                action.accept((Object)Object2IntArrayMap.this.key[i]);
            }
        }
        
        @Override
        public int size() {
            return Object2IntArrayMap.this.size;
        }
        
        @Override
        public void clear() {
            Object2IntArrayMap.this.clear();
        }
        
        final class KeySetSpliterator extends ObjectSpliterators.EarlyBindingSizeIndexBasedSpliterator<K> implements ObjectSpliterator<K>
        {
            KeySetSpliterator(final int pos, final int maxPos) {
                super(pos, maxPos);
            }
            
            @Override
            public int characteristics() {
                return 16465;
            }
            
            @Override
            protected final K get(final int location) {
                return (K)Object2IntArrayMap.this.key[location];
            }
            
            @Override
            protected final KeySetSpliterator makeForSplit(final int pos, final int maxPos) {
                return new KeySetSpliterator(pos, maxPos);
            }
            
            @Override
            public void forEachRemaining(final Consumer<? super K> action) {
                final int max = Object2IntArrayMap.this.size;
                while (this.pos < max) {
                    action.accept((Object)Object2IntArrayMap.this.key[this.pos++]);
                }
            }
        }
    }
    
    private final class ValuesCollection extends AbstractIntCollection
    {
        @Override
        public boolean contains(final int v) {
            return Object2IntArrayMap.this.containsValue(v);
        }
        
        @Override
        public IntIterator iterator() {
            return new IntIterator() {
                int pos = 0;
                
                @Override
                public boolean hasNext() {
                    return this.pos < Object2IntArrayMap.this.size;
                }
                
                @Override
                public int nextInt() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return Object2IntArrayMap.this.value[this.pos++];
                }
                
                @Override
                public void remove() {
                    if (this.pos == 0) {
                        throw new IllegalStateException();
                    }
                    final int tail = Object2IntArrayMap.this.size - this.pos;
                    System.arraycopy(Object2IntArrayMap.this.key, this.pos, Object2IntArrayMap.this.key, this.pos - 1, tail);
                    System.arraycopy(Object2IntArrayMap.this.value, this.pos, Object2IntArrayMap.this.value, this.pos - 1, tail);
                    Object2IntArrayMap.this.size--;
                    --this.pos;
                    Object2IntArrayMap.this.key[Object2IntArrayMap.this.size] = null;
                }
                
                @Override
                public void forEachRemaining(final IntConsumer action) {
                    final int max = Object2IntArrayMap.this.size;
                    while (this.pos < max) {
                        action.accept(Object2IntArrayMap.this.value[this.pos++]);
                    }
                }
            };
        }
        
        @Override
        public IntSpliterator spliterator() {
            return new ValuesSpliterator(0, Object2IntArrayMap.this.size);
        }
        
        @Override
        public void forEach(final IntConsumer action) {
            for (int i = 0, max = Object2IntArrayMap.this.size; i < max; ++i) {
                action.accept(Object2IntArrayMap.this.value[i]);
            }
        }
        
        @Override
        public int size() {
            return Object2IntArrayMap.this.size;
        }
        
        @Override
        public void clear() {
            Object2IntArrayMap.this.clear();
        }
        
        final class ValuesSpliterator extends IntSpliterators.EarlyBindingSizeIndexBasedSpliterator implements IntSpliterator
        {
            ValuesSpliterator(final int pos, final int maxPos) {
                super(pos, maxPos);
            }
            
            @Override
            public int characteristics() {
                return 16720;
            }
            
            @Override
            protected final int get(final int location) {
                return Object2IntArrayMap.this.value[location];
            }
            
            @Override
            protected final ValuesSpliterator makeForSplit(final int pos, final int maxPos) {
                return new ValuesSpliterator(pos, maxPos);
            }
            
            @Override
            public void forEachRemaining(final IntConsumer action) {
                final int max = Object2IntArrayMap.this.size;
                while (this.pos < max) {
                    action.accept(Object2IntArrayMap.this.value[this.pos++]);
                }
            }
        }
    }
}
