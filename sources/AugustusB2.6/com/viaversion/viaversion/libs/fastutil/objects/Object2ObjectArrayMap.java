// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

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
import java.io.Serializable;

public class Object2ObjectArrayMap<K, V> extends AbstractObject2ObjectMap<K, V> implements Serializable, Cloneable
{
    private static final long serialVersionUID = 1L;
    private transient Object[] key;
    private transient Object[] value;
    private int size;
    private transient Object2ObjectMap.FastEntrySet<K, V> entries;
    private transient ObjectSet<K> keys;
    private transient ObjectCollection<V> values;
    
    public Object2ObjectArrayMap(final Object[] key, final Object[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
    }
    
    public Object2ObjectArrayMap() {
        this.key = ObjectArrays.EMPTY_ARRAY;
        this.value = ObjectArrays.EMPTY_ARRAY;
    }
    
    public Object2ObjectArrayMap(final int capacity) {
        this.key = new Object[capacity];
        this.value = new Object[capacity];
    }
    
    public Object2ObjectArrayMap(final Object2ObjectMap<K, V> m) {
        this(m.size());
        int i = 0;
        for (final Object2ObjectMap.Entry<K, V> e : m.object2ObjectEntrySet()) {
            this.key[i] = e.getKey();
            this.value[i] = e.getValue();
            ++i;
        }
        this.size = i;
    }
    
    public Object2ObjectArrayMap(final Map<? extends K, ? extends V> m) {
        this(m.size());
        int i = 0;
        for (final Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            this.key[i] = e.getKey();
            this.value[i] = e.getValue();
            ++i;
        }
        this.size = i;
    }
    
    public Object2ObjectArrayMap(final Object[] key, final Object[] value, final int size) {
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
    public Object2ObjectMap.FastEntrySet<K, V> object2ObjectEntrySet() {
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
    public V get(final Object k) {
        final Object[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (Objects.equals(key[i], k)) {
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
            this.key[i] = null;
            this.value[i] = null;
        }
        this.size = 0;
    }
    
    @Override
    public boolean containsKey(final Object k) {
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
    public V put(final K k, final V v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final V oldValue = (V)this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final Object[] newKey = new Object[(this.size == 0) ? 2 : (this.size * 2)];
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
    public V remove(final Object k) {
        final int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        final V oldValue = (V)this.value[oldPos];
        final int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        this.key[this.size] = null;
        this.value[this.size] = null;
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
    public ObjectCollection<V> values() {
        if (this.values == null) {
            this.values = new ValuesCollection();
        }
        return this.values;
    }
    
    public Object2ObjectArrayMap<K, V> clone() {
        Object2ObjectArrayMap<K, V> c;
        try {
            c = (Object2ObjectArrayMap)super.clone();
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
            s.writeObject(this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new Object[this.size];
        this.value = new Object[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readObject();
            this.value[i] = s.readObject();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Object2ObjectMap.Entry<K, V>> implements Object2ObjectMap.FastEntrySet<K, V>
    {
        @Override
        public ObjectIterator<Object2ObjectMap.Entry<K, V>> iterator() {
            return new ObjectIterator<Object2ObjectMap.Entry<K, V>>() {
                int curr = -1;
                int next = 0;
                
                @Override
                public boolean hasNext() {
                    return this.next < Object2ObjectArrayMap.this.size;
                }
                
                @Override
                public Object2ObjectMap.Entry<K, V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final Object[] access$100 = Object2ObjectArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry<K, V>(access$100[next], Object2ObjectArrayMap.this.value[this.next++]);
                }
                
                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Object2ObjectArrayMap.this.size-- - this.next--;
                    System.arraycopy(Object2ObjectArrayMap.this.key, this.next + 1, Object2ObjectArrayMap.this.key, this.next, tail);
                    System.arraycopy(Object2ObjectArrayMap.this.value, this.next + 1, Object2ObjectArrayMap.this.value, this.next, tail);
                    Object2ObjectArrayMap.this.key[Object2ObjectArrayMap.this.size] = null;
                    Object2ObjectArrayMap.this.value[Object2ObjectArrayMap.this.size] = null;
                }
                
                @Override
                public void forEachRemaining(final Consumer<? super Object2ObjectMap.Entry<K, V>> action) {
                    final int max = Object2ObjectArrayMap.this.size;
                    while (this.next < max) {
                        final Object[] access$100 = Object2ObjectArrayMap.this.key;
                        final int next = this.next;
                        this.curr = next;
                        action.accept((Object)new BasicEntry(access$100[next], Object2ObjectArrayMap.this.value[this.next++]));
                    }
                }
            };
        }
        
        @Override
        public ObjectIterator<Object2ObjectMap.Entry<K, V>> fastIterator() {
            return new ObjectIterator<Object2ObjectMap.Entry<K, V>>() {
                int next = 0;
                int curr = -1;
                final BasicEntry<K, V> entry = new BasicEntry<K, V>();
                
                @Override
                public boolean hasNext() {
                    return this.next < Object2ObjectArrayMap.this.size;
                }
                
                @Override
                public Object2ObjectMap.Entry<K, V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry<K, V> entry = this.entry;
                    final Object[] access$100 = Object2ObjectArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = (K)access$100[next];
                    this.entry.value = (V)Object2ObjectArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Object2ObjectArrayMap.this.size-- - this.next--;
                    System.arraycopy(Object2ObjectArrayMap.this.key, this.next + 1, Object2ObjectArrayMap.this.key, this.next, tail);
                    System.arraycopy(Object2ObjectArrayMap.this.value, this.next + 1, Object2ObjectArrayMap.this.value, this.next, tail);
                    Object2ObjectArrayMap.this.key[Object2ObjectArrayMap.this.size] = null;
                    Object2ObjectArrayMap.this.value[Object2ObjectArrayMap.this.size] = null;
                }
                
                @Override
                public void forEachRemaining(final Consumer<? super Object2ObjectMap.Entry<K, V>> action) {
                    final int max = Object2ObjectArrayMap.this.size;
                    while (this.next < max) {
                        final BasicEntry<K, V> entry = this.entry;
                        final Object[] access$100 = Object2ObjectArrayMap.this.key;
                        final int next = this.next;
                        this.curr = next;
                        entry.key = (K)access$100[next];
                        this.entry.value = (V)Object2ObjectArrayMap.this.value[this.next++];
                        action.accept(this.entry);
                    }
                }
            };
        }
        
        @Override
        public ObjectSpliterator<Object2ObjectMap.Entry<K, V>> spliterator() {
            return new EntrySetSpliterator(0, Object2ObjectArrayMap.this.size);
        }
        
        @Override
        public void forEach(final Consumer<? super Object2ObjectMap.Entry<K, V>> action) {
            for (int i = 0, max = Object2ObjectArrayMap.this.size; i < max; ++i) {
                action.accept((Object)new BasicEntry((K)Object2ObjectArrayMap.this.key[i], (V)Object2ObjectArrayMap.this.value[i]));
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Object2ObjectMap.Entry<K, V>> action) {
            final BasicEntry<K, V> entry = new BasicEntry<K, V>();
            for (int i = 0, max = Object2ObjectArrayMap.this.size; i < max; ++i) {
                entry.key = (K)Object2ObjectArrayMap.this.key[i];
                entry.value = (V)Object2ObjectArrayMap.this.value[i];
                action.accept(entry);
            }
        }
        
        @Override
        public int size() {
            return Object2ObjectArrayMap.this.size;
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            final K k = (K)e.getKey();
            return Object2ObjectArrayMap.this.containsKey(k) && Objects.equals(Object2ObjectArrayMap.this.get(k), e.getValue());
        }
        
        @Override
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            final K k = (K)e.getKey();
            final V v = (V)e.getValue();
            final int oldPos = Object2ObjectArrayMap.this.findKey(k);
            if (oldPos == -1 || !Objects.equals(v, Object2ObjectArrayMap.this.value[oldPos])) {
                return false;
            }
            final int tail = Object2ObjectArrayMap.this.size - oldPos - 1;
            System.arraycopy(Object2ObjectArrayMap.this.key, oldPos + 1, Object2ObjectArrayMap.this.key, oldPos, tail);
            System.arraycopy(Object2ObjectArrayMap.this.value, oldPos + 1, Object2ObjectArrayMap.this.value, oldPos, tail);
            Object2ObjectArrayMap.this.size--;
            Object2ObjectArrayMap.this.key[Object2ObjectArrayMap.this.size] = null;
            Object2ObjectArrayMap.this.value[Object2ObjectArrayMap.this.size] = null;
            return true;
        }
        
        final class EntrySetSpliterator extends ObjectSpliterators.EarlyBindingSizeIndexBasedSpliterator<Object2ObjectMap.Entry<K, V>> implements ObjectSpliterator<Object2ObjectMap.Entry<K, V>>
        {
            EntrySetSpliterator(final int pos, final int maxPos) {
                super(pos, maxPos);
            }
            
            @Override
            public int characteristics() {
                return 16465;
            }
            
            @Override
            protected final Object2ObjectMap.Entry<K, V> get(final int location) {
                return new BasicEntry<K, V>((K)Object2ObjectArrayMap.this.key[location], (V)Object2ObjectArrayMap.this.value[location]);
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
            return Object2ObjectArrayMap.this.findKey(k) != -1;
        }
        
        @Override
        public boolean remove(final Object k) {
            final int oldPos = Object2ObjectArrayMap.this.findKey(k);
            if (oldPos == -1) {
                return false;
            }
            final int tail = Object2ObjectArrayMap.this.size - oldPos - 1;
            System.arraycopy(Object2ObjectArrayMap.this.key, oldPos + 1, Object2ObjectArrayMap.this.key, oldPos, tail);
            System.arraycopy(Object2ObjectArrayMap.this.value, oldPos + 1, Object2ObjectArrayMap.this.value, oldPos, tail);
            Object2ObjectArrayMap.this.size--;
            Object2ObjectArrayMap.this.key[Object2ObjectArrayMap.this.size] = null;
            Object2ObjectArrayMap.this.value[Object2ObjectArrayMap.this.size] = null;
            return true;
        }
        
        @Override
        public ObjectIterator<K> iterator() {
            return new ObjectIterator<K>() {
                int pos = 0;
                
                @Override
                public boolean hasNext() {
                    return this.pos < Object2ObjectArrayMap.this.size;
                }
                
                @Override
                public K next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return (K)Object2ObjectArrayMap.this.key[this.pos++];
                }
                
                @Override
                public void remove() {
                    if (this.pos == 0) {
                        throw new IllegalStateException();
                    }
                    final int tail = Object2ObjectArrayMap.this.size - this.pos;
                    System.arraycopy(Object2ObjectArrayMap.this.key, this.pos, Object2ObjectArrayMap.this.key, this.pos - 1, tail);
                    System.arraycopy(Object2ObjectArrayMap.this.value, this.pos, Object2ObjectArrayMap.this.value, this.pos - 1, tail);
                    Object2ObjectArrayMap.this.size--;
                    --this.pos;
                    Object2ObjectArrayMap.this.key[Object2ObjectArrayMap.this.size] = null;
                    Object2ObjectArrayMap.this.value[Object2ObjectArrayMap.this.size] = null;
                }
                
                @Override
                public void forEachRemaining(final Consumer<? super K> action) {
                    final int max = Object2ObjectArrayMap.this.size;
                    while (this.pos < max) {
                        action.accept((Object)Object2ObjectArrayMap.this.key[this.pos++]);
                    }
                }
            };
        }
        
        @Override
        public ObjectSpliterator<K> spliterator() {
            return new KeySetSpliterator(0, Object2ObjectArrayMap.this.size);
        }
        
        @Override
        public void forEach(final Consumer<? super K> action) {
            for (int i = 0, max = Object2ObjectArrayMap.this.size; i < max; ++i) {
                action.accept((Object)Object2ObjectArrayMap.this.key[i]);
            }
        }
        
        @Override
        public int size() {
            return Object2ObjectArrayMap.this.size;
        }
        
        @Override
        public void clear() {
            Object2ObjectArrayMap.this.clear();
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
                return (K)Object2ObjectArrayMap.this.key[location];
            }
            
            @Override
            protected final KeySetSpliterator makeForSplit(final int pos, final int maxPos) {
                return new KeySetSpliterator(pos, maxPos);
            }
            
            @Override
            public void forEachRemaining(final Consumer<? super K> action) {
                final int max = Object2ObjectArrayMap.this.size;
                while (this.pos < max) {
                    action.accept((Object)Object2ObjectArrayMap.this.key[this.pos++]);
                }
            }
        }
    }
    
    private final class ValuesCollection extends AbstractObjectCollection<V>
    {
        @Override
        public boolean contains(final Object v) {
            return Object2ObjectArrayMap.this.containsValue(v);
        }
        
        @Override
        public ObjectIterator<V> iterator() {
            return new ObjectIterator<V>() {
                int pos = 0;
                
                @Override
                public boolean hasNext() {
                    return this.pos < Object2ObjectArrayMap.this.size;
                }
                
                @Override
                public V next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return (V)Object2ObjectArrayMap.this.value[this.pos++];
                }
                
                @Override
                public void remove() {
                    if (this.pos == 0) {
                        throw new IllegalStateException();
                    }
                    final int tail = Object2ObjectArrayMap.this.size - this.pos;
                    System.arraycopy(Object2ObjectArrayMap.this.key, this.pos, Object2ObjectArrayMap.this.key, this.pos - 1, tail);
                    System.arraycopy(Object2ObjectArrayMap.this.value, this.pos, Object2ObjectArrayMap.this.value, this.pos - 1, tail);
                    Object2ObjectArrayMap.this.size--;
                    --this.pos;
                    Object2ObjectArrayMap.this.key[Object2ObjectArrayMap.this.size] = null;
                    Object2ObjectArrayMap.this.value[Object2ObjectArrayMap.this.size] = null;
                }
                
                @Override
                public void forEachRemaining(final Consumer<? super V> action) {
                    final int max = Object2ObjectArrayMap.this.size;
                    while (this.pos < max) {
                        action.accept((Object)Object2ObjectArrayMap.this.value[this.pos++]);
                    }
                }
            };
        }
        
        @Override
        public ObjectSpliterator<V> spliterator() {
            return new ValuesSpliterator(0, Object2ObjectArrayMap.this.size);
        }
        
        @Override
        public void forEach(final Consumer<? super V> action) {
            for (int i = 0, max = Object2ObjectArrayMap.this.size; i < max; ++i) {
                action.accept((Object)Object2ObjectArrayMap.this.value[i]);
            }
        }
        
        @Override
        public int size() {
            return Object2ObjectArrayMap.this.size;
        }
        
        @Override
        public void clear() {
            Object2ObjectArrayMap.this.clear();
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
                return (V)Object2ObjectArrayMap.this.value[location];
            }
            
            @Override
            protected final ValuesSpliterator makeForSplit(final int pos, final int maxPos) {
                return new ValuesSpliterator(pos, maxPos);
            }
            
            @Override
            public void forEachRemaining(final Consumer<? super V> action) {
                final int max = Object2ObjectArrayMap.this.size;
                while (this.pos < max) {
                    action.accept((Object)Object2ObjectArrayMap.this.value[this.pos++]);
                }
            }
        }
    }
}
