// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

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
import java.util.Iterator;
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public class Int2IntArrayMap extends AbstractInt2IntMap implements Serializable, Cloneable
{
    private static final long serialVersionUID = 1L;
    private transient int[] key;
    private transient int[] value;
    private int size;
    private transient Int2IntMap.FastEntrySet entries;
    private transient IntSet keys;
    private transient IntCollection values;
    
    public Int2IntArrayMap(final int[] key, final int[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
    }
    
    public Int2IntArrayMap() {
        this.key = IntArrays.EMPTY_ARRAY;
        this.value = IntArrays.EMPTY_ARRAY;
    }
    
    public Int2IntArrayMap(final int capacity) {
        this.key = new int[capacity];
        this.value = new int[capacity];
    }
    
    public Int2IntArrayMap(final Int2IntMap m) {
        this(m.size());
        int i = 0;
        for (final Int2IntMap.Entry e : m.int2IntEntrySet()) {
            this.key[i] = e.getIntKey();
            this.value[i] = e.getIntValue();
            ++i;
        }
        this.size = i;
    }
    
    public Int2IntArrayMap(final Map<? extends Integer, ? extends Integer> m) {
        this(m.size());
        int i = 0;
        for (final Map.Entry<? extends Integer, ? extends Integer> e : m.entrySet()) {
            this.key[i] = (int)e.getKey();
            this.value[i] = (int)e.getValue();
            ++i;
        }
        this.size = i;
    }
    
    public Int2IntArrayMap(final int[] key, final int[] value, final int size) {
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
    public Int2IntMap.FastEntrySet int2IntEntrySet() {
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
    public int get(final int k) {
        final int[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] == k) {
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
        this.size = 0;
    }
    
    @Override
    public boolean containsKey(final int k) {
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
    public int put(final int k, final int v) {
        final int oldKey = this.findKey(k);
        if (oldKey != -1) {
            final int oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            final int[] newKey = new int[(this.size == 0) ? 2 : (this.size * 2)];
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
    public int remove(final int k) {
        final int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        final int oldValue = this.value[oldPos];
        final int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
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
    public IntCollection values() {
        if (this.values == null) {
            this.values = new ValuesCollection();
        }
        return this.values;
    }
    
    public Int2IntArrayMap clone() {
        Int2IntArrayMap c;
        try {
            c = (Int2IntArrayMap)super.clone();
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
            s.writeInt(this.value[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new int[this.size];
        this.value = new int[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readInt();
            this.value[i] = s.readInt();
        }
    }
    
    private final class EntrySet extends AbstractObjectSet<Int2IntMap.Entry> implements Int2IntMap.FastEntrySet
    {
        @Override
        public ObjectIterator<Int2IntMap.Entry> iterator() {
            return new ObjectIterator<Int2IntMap.Entry>() {
                int curr = -1;
                int next = 0;
                
                @Override
                public boolean hasNext() {
                    return this.next < Int2IntArrayMap.this.size;
                }
                
                @Override
                public Int2IntMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final int[] access$100 = Int2IntArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    return new BasicEntry(access$100[next], Int2IntArrayMap.this.value[this.next++]);
                }
                
                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Int2IntArrayMap.this.size-- - this.next--;
                    System.arraycopy(Int2IntArrayMap.this.key, this.next + 1, Int2IntArrayMap.this.key, this.next, tail);
                    System.arraycopy(Int2IntArrayMap.this.value, this.next + 1, Int2IntArrayMap.this.value, this.next, tail);
                }
                
                @Override
                public void forEachRemaining(final Consumer<? super Int2IntMap.Entry> action) {
                    final int max = Int2IntArrayMap.this.size;
                    while (this.next < max) {
                        final int[] access$100 = Int2IntArrayMap.this.key;
                        final int next = this.next;
                        this.curr = next;
                        action.accept((Object)new BasicEntry(access$100[next], Int2IntArrayMap.this.value[this.next++]));
                    }
                }
            };
        }
        
        @Override
        public ObjectIterator<Int2IntMap.Entry> fastIterator() {
            return new ObjectIterator<Int2IntMap.Entry>() {
                int next = 0;
                int curr = -1;
                final BasicEntry entry = new BasicEntry();
                
                @Override
                public boolean hasNext() {
                    return this.next < Int2IntArrayMap.this.size;
                }
                
                @Override
                public Int2IntMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BasicEntry entry = this.entry;
                    final int[] access$100 = Int2IntArrayMap.this.key;
                    final int next = this.next;
                    this.curr = next;
                    entry.key = access$100[next];
                    this.entry.value = Int2IntArrayMap.this.value[this.next++];
                    return this.entry;
                }
                
                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    final int tail = Int2IntArrayMap.this.size-- - this.next--;
                    System.arraycopy(Int2IntArrayMap.this.key, this.next + 1, Int2IntArrayMap.this.key, this.next, tail);
                    System.arraycopy(Int2IntArrayMap.this.value, this.next + 1, Int2IntArrayMap.this.value, this.next, tail);
                }
                
                @Override
                public void forEachRemaining(final Consumer<? super Int2IntMap.Entry> action) {
                    final int max = Int2IntArrayMap.this.size;
                    while (this.next < max) {
                        final BasicEntry entry = this.entry;
                        final int[] access$100 = Int2IntArrayMap.this.key;
                        final int next = this.next;
                        this.curr = next;
                        entry.key = access$100[next];
                        this.entry.value = Int2IntArrayMap.this.value[this.next++];
                        action.accept(this.entry);
                    }
                }
            };
        }
        
        @Override
        public ObjectSpliterator<Int2IntMap.Entry> spliterator() {
            return new EntrySetSpliterator(0, Int2IntArrayMap.this.size);
        }
        
        @Override
        public void forEach(final Consumer<? super Int2IntMap.Entry> action) {
            for (int i = 0, max = Int2IntArrayMap.this.size; i < max; ++i) {
                action.accept((Object)new BasicEntry(Int2IntArrayMap.this.key[i], Int2IntArrayMap.this.value[i]));
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Int2IntMap.Entry> action) {
            final BasicEntry entry = new BasicEntry();
            for (int i = 0, max = Int2IntArrayMap.this.size; i < max; ++i) {
                entry.key = Int2IntArrayMap.this.key[i];
                entry.value = Int2IntArrayMap.this.value[i];
                action.accept(entry);
            }
        }
        
        @Override
        public int size() {
            return Int2IntArrayMap.this.size;
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
            if (e.getValue() == null || !(e.getValue() instanceof Integer)) {
                return false;
            }
            final int k = (int)e.getKey();
            return Int2IntArrayMap.this.containsKey(k) && Int2IntArrayMap.this.get(k) == (int)e.getValue();
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
            if (e.getValue() == null || !(e.getValue() instanceof Integer)) {
                return false;
            }
            final int k = (int)e.getKey();
            final int v = (int)e.getValue();
            final int oldPos = Int2IntArrayMap.this.findKey(k);
            if (oldPos == -1 || v != Int2IntArrayMap.this.value[oldPos]) {
                return false;
            }
            final int tail = Int2IntArrayMap.this.size - oldPos - 1;
            System.arraycopy(Int2IntArrayMap.this.key, oldPos + 1, Int2IntArrayMap.this.key, oldPos, tail);
            System.arraycopy(Int2IntArrayMap.this.value, oldPos + 1, Int2IntArrayMap.this.value, oldPos, tail);
            Int2IntArrayMap.this.size--;
            return true;
        }
        
        final class EntrySetSpliterator extends ObjectSpliterators.EarlyBindingSizeIndexBasedSpliterator<Int2IntMap.Entry> implements ObjectSpliterator<Int2IntMap.Entry>
        {
            EntrySetSpliterator(final int pos, final int maxPos) {
                super(pos, maxPos);
            }
            
            @Override
            public int characteristics() {
                return 16465;
            }
            
            @Override
            protected final Int2IntMap.Entry get(final int location) {
                return new BasicEntry(Int2IntArrayMap.this.key[location], Int2IntArrayMap.this.value[location]);
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
            return Int2IntArrayMap.this.findKey(k) != -1;
        }
        
        @Override
        public boolean remove(final int k) {
            final int oldPos = Int2IntArrayMap.this.findKey(k);
            if (oldPos == -1) {
                return false;
            }
            final int tail = Int2IntArrayMap.this.size - oldPos - 1;
            System.arraycopy(Int2IntArrayMap.this.key, oldPos + 1, Int2IntArrayMap.this.key, oldPos, tail);
            System.arraycopy(Int2IntArrayMap.this.value, oldPos + 1, Int2IntArrayMap.this.value, oldPos, tail);
            Int2IntArrayMap.this.size--;
            return true;
        }
        
        @Override
        public IntIterator iterator() {
            return new IntIterator() {
                int pos = 0;
                
                @Override
                public boolean hasNext() {
                    return this.pos < Int2IntArrayMap.this.size;
                }
                
                @Override
                public int nextInt() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return Int2IntArrayMap.this.key[this.pos++];
                }
                
                @Override
                public void remove() {
                    if (this.pos == 0) {
                        throw new IllegalStateException();
                    }
                    final int tail = Int2IntArrayMap.this.size - this.pos;
                    System.arraycopy(Int2IntArrayMap.this.key, this.pos, Int2IntArrayMap.this.key, this.pos - 1, tail);
                    System.arraycopy(Int2IntArrayMap.this.value, this.pos, Int2IntArrayMap.this.value, this.pos - 1, tail);
                    Int2IntArrayMap.this.size--;
                    --this.pos;
                }
                
                @Override
                public void forEachRemaining(final IntConsumer action) {
                    final int max = Int2IntArrayMap.this.size;
                    while (this.pos < max) {
                        action.accept(Int2IntArrayMap.this.key[this.pos++]);
                    }
                }
            };
        }
        
        @Override
        public IntSpliterator spliterator() {
            return new KeySetSpliterator(0, Int2IntArrayMap.this.size);
        }
        
        @Override
        public void forEach(final IntConsumer action) {
            for (int i = 0, max = Int2IntArrayMap.this.size; i < max; ++i) {
                action.accept(Int2IntArrayMap.this.key[i]);
            }
        }
        
        @Override
        public int size() {
            return Int2IntArrayMap.this.size;
        }
        
        @Override
        public void clear() {
            Int2IntArrayMap.this.clear();
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
                return Int2IntArrayMap.this.key[location];
            }
            
            @Override
            protected final KeySetSpliterator makeForSplit(final int pos, final int maxPos) {
                return new KeySetSpliterator(pos, maxPos);
            }
            
            @Override
            public void forEachRemaining(final IntConsumer action) {
                final int max = Int2IntArrayMap.this.size;
                while (this.pos < max) {
                    action.accept(Int2IntArrayMap.this.key[this.pos++]);
                }
            }
        }
    }
    
    private final class ValuesCollection extends AbstractIntCollection
    {
        @Override
        public boolean contains(final int v) {
            return Int2IntArrayMap.this.containsValue(v);
        }
        
        @Override
        public IntIterator iterator() {
            return new IntIterator() {
                int pos = 0;
                
                @Override
                public boolean hasNext() {
                    return this.pos < Int2IntArrayMap.this.size;
                }
                
                @Override
                public int nextInt() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return Int2IntArrayMap.this.value[this.pos++];
                }
                
                @Override
                public void remove() {
                    if (this.pos == 0) {
                        throw new IllegalStateException();
                    }
                    final int tail = Int2IntArrayMap.this.size - this.pos;
                    System.arraycopy(Int2IntArrayMap.this.key, this.pos, Int2IntArrayMap.this.key, this.pos - 1, tail);
                    System.arraycopy(Int2IntArrayMap.this.value, this.pos, Int2IntArrayMap.this.value, this.pos - 1, tail);
                    Int2IntArrayMap.this.size--;
                    --this.pos;
                }
                
                @Override
                public void forEachRemaining(final IntConsumer action) {
                    final int max = Int2IntArrayMap.this.size;
                    while (this.pos < max) {
                        action.accept(Int2IntArrayMap.this.value[this.pos++]);
                    }
                }
            };
        }
        
        @Override
        public IntSpliterator spliterator() {
            return new ValuesSpliterator(0, Int2IntArrayMap.this.size);
        }
        
        @Override
        public void forEach(final IntConsumer action) {
            for (int i = 0, max = Int2IntArrayMap.this.size; i < max; ++i) {
                action.accept(Int2IntArrayMap.this.value[i]);
            }
        }
        
        @Override
        public int size() {
            return Int2IntArrayMap.this.size;
        }
        
        @Override
        public void clear() {
            Int2IntArrayMap.this.clear();
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
                return Int2IntArrayMap.this.value[location];
            }
            
            @Override
            protected final ValuesSpliterator makeForSplit(final int pos, final int maxPos) {
                return new ValuesSpliterator(pos, maxPos);
            }
            
            @Override
            public void forEachRemaining(final IntConsumer action) {
                final int max = Int2IntArrayMap.this.size;
                while (this.pos < max) {
                    action.accept(Int2IntArrayMap.this.value[this.pos++]);
                }
            }
        }
    }
}
