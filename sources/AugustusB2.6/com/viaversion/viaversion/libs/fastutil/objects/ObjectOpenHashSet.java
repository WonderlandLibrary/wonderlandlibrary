// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.function.Consumer;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.Iterator;
import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.HashCommon;
import java.util.stream.Collector;
import com.viaversion.viaversion.libs.fastutil.Hash;
import java.io.Serializable;

public class ObjectOpenHashSet<K> extends AbstractObjectSet<K> implements Serializable, Cloneable, Hash
{
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient K[] key;
    protected transient int mask;
    protected transient boolean containsNull;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    private static final Collector<Object, ?, ObjectOpenHashSet<Object>> TO_SET_COLLECTOR;
    
    public ObjectOpenHashSet(final int expected, final float f) {
        if (f <= 0.0f || f >= 1.0f) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than 1");
        }
        if (expected < 0) {
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
        }
        this.f = f;
        final int arraySize = HashCommon.arraySize(expected, f);
        this.n = arraySize;
        this.minN = arraySize;
        this.mask = this.n - 1;
        this.maxFill = HashCommon.maxFill(this.n, f);
        this.key = (K[])new Object[this.n + 1];
    }
    
    public ObjectOpenHashSet(final int expected) {
        this(expected, 0.75f);
    }
    
    public ObjectOpenHashSet() {
        this(16, 0.75f);
    }
    
    public ObjectOpenHashSet(final Collection<? extends K> c, final float f) {
        this(c.size(), f);
        this.addAll(c);
    }
    
    public ObjectOpenHashSet(final Collection<? extends K> c) {
        this(c, 0.75f);
    }
    
    public ObjectOpenHashSet(final ObjectCollection<? extends K> c, final float f) {
        this(c.size(), f);
        this.addAll(c);
    }
    
    public ObjectOpenHashSet(final ObjectCollection<? extends K> c) {
        this(c, 0.75f);
    }
    
    public ObjectOpenHashSet(final Iterator<? extends K> i, final float f) {
        this(16, f);
        while (i.hasNext()) {
            this.add(i.next());
        }
    }
    
    public ObjectOpenHashSet(final Iterator<? extends K> i) {
        this(i, 0.75f);
    }
    
    public ObjectOpenHashSet(final K[] a, final int offset, final int length, final float f) {
        this((length < 0) ? 0 : length, f);
        ObjectArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; ++i) {
            this.add(a[offset + i]);
        }
    }
    
    public ObjectOpenHashSet(final K[] a, final int offset, final int length) {
        this(a, offset, length, 0.75f);
    }
    
    public ObjectOpenHashSet(final K[] a, final float f) {
        this(a, 0, a.length, f);
    }
    
    public ObjectOpenHashSet(final K[] a) {
        this(a, 0.75f);
    }
    
    public static <K> ObjectOpenHashSet<K> of() {
        return new ObjectOpenHashSet<K>();
    }
    
    public static <K> ObjectOpenHashSet<K> of(final K e) {
        final ObjectOpenHashSet<K> result = new ObjectOpenHashSet<K>(1, 0.75f);
        result.add(e);
        return result;
    }
    
    public static <K> ObjectOpenHashSet<K> of(final K e0, final K e1) {
        final ObjectOpenHashSet<K> result = new ObjectOpenHashSet<K>(2, 0.75f);
        result.add(e0);
        if (!result.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        return result;
    }
    
    public static <K> ObjectOpenHashSet<K> of(final K e0, final K e1, final K e2) {
        final ObjectOpenHashSet<K> result = new ObjectOpenHashSet<K>(3, 0.75f);
        result.add(e0);
        if (!result.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        if (!result.add(e2)) {
            throw new IllegalArgumentException("Duplicate element: " + e2);
        }
        return result;
    }
    
    @SafeVarargs
    public static <K> ObjectOpenHashSet<K> of(final K... a) {
        final ObjectOpenHashSet<K> result = new ObjectOpenHashSet<K>(a.length, 0.75f);
        for (final K element : a) {
            if (!result.add(element)) {
                throw new IllegalArgumentException("Duplicate element " + element);
            }
        }
        return result;
    }
    
    private ObjectOpenHashSet<K> combine(final ObjectOpenHashSet<? extends K> toAddFrom) {
        this.addAll(toAddFrom);
        return this;
    }
    
    public static <K> Collector<K, ?, ObjectOpenHashSet<K>> toSet() {
        return (Collector<K, ?, ObjectOpenHashSet<K>>)ObjectOpenHashSet.TO_SET_COLLECTOR;
    }
    
    public static <K> Collector<K, ?, ObjectOpenHashSet<K>> toSetWithExpectedSize(final int expectedSize) {
        if (expectedSize <= 16) {
            return toSet();
        }
        final Object o3;
        return (Collector<K, ?, ObjectOpenHashSet<K>>)Collector.of(new ObjectCollections.SizeDecreasingSupplier<Object, Object>(expectedSize, size -> {
            if (size <= 16) {
                // new(com.viaversion.viaversion.libs.fastutil.objects.ObjectOpenHashSet.class)
                new ObjectOpenHashSet();
            }
            else {
                // new(com.viaversion.viaversion.libs.fastutil.objects.ObjectOpenHashSet.class)
                new ObjectOpenHashSet(size);
            }
            return o3;
        }), ObjectOpenHashSet::add, ObjectOpenHashSet::combine, Collector.Characteristics.UNORDERED);
    }
    
    private int realSize() {
        return this.containsNull ? (this.size - 1) : this.size;
    }
    
    private void ensureCapacity(final int capacity) {
        final int needed = HashCommon.arraySize(capacity, this.f);
        if (needed > this.n) {
            this.rehash(needed);
        }
    }
    
    private void tryCapacity(final long capacity) {
        final int needed = (int)Math.min(1073741824L, Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(capacity / this.f))));
        if (needed > this.n) {
            this.rehash(needed);
        }
    }
    
    @Override
    public boolean addAll(final Collection<? extends K> c) {
        if (this.f <= 0.5) {
            this.ensureCapacity(c.size());
        }
        else {
            this.tryCapacity(this.size() + c.size());
        }
        return super.addAll(c);
    }
    
    @Override
    public boolean add(final K k) {
        if (k == null) {
            if (this.containsNull) {
                return false;
            }
            this.containsNull = true;
        }
        else {
            final K[] key = this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) != null) {
                if (curr.equals(k)) {
                    return false;
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                    if (curr.equals(k)) {
                        return false;
                    }
                }
            }
            key[pos] = k;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return true;
    }
    
    public K addOrGet(final K k) {
        if (k == null) {
            if (this.containsNull) {
                return this.key[this.n];
            }
            this.containsNull = true;
        }
        else {
            final K[] key = this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) != null) {
                if (curr.equals(k)) {
                    return curr;
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                    if (curr.equals(k)) {
                        return curr;
                    }
                }
            }
            key[pos] = k;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return k;
    }
    
    protected final void shiftKeys(int pos) {
        final K[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            K curr;
            while ((curr = key[pos]) != null) {
                final int slot = HashCommon.mix(curr.hashCode()) & this.mask;
                Label_0090: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0090;
                        }
                        if (slot > pos) {
                            break Label_0090;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0090;
                    }
                    pos = (pos + 1 & this.mask);
                    continue;
                }
                key[last] = curr;
                continue Label_0006;
            }
            break;
        }
        key[last] = null;
    }
    
    private boolean removeEntry(final int pos) {
        --this.size;
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return true;
    }
    
    private boolean removeNullEntry() {
        this.containsNull = false;
        this.key[this.n] = null;
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return true;
    }
    
    @Override
    public boolean remove(final Object k) {
        if (k == null) {
            return this.containsNull && this.removeNullEntry();
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
            return false;
        }
        if (k.equals(curr)) {
            return this.removeEntry(pos);
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k.equals(curr)) {
                return this.removeEntry(pos);
            }
        }
        return false;
    }
    
    @Override
    public boolean contains(final Object k) {
        if (k == null) {
            return this.containsNull;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
            return false;
        }
        if (k.equals(curr)) {
            return true;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k.equals(curr)) {
                return true;
            }
        }
        return false;
    }
    
    public K get(final Object k) {
        if (k == null) {
            return this.key[this.n];
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
            return null;
        }
        if (k.equals(curr)) {
            return curr;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k.equals(curr)) {
                return curr;
            }
        }
        return null;
    }
    
    @Override
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNull = false;
        Arrays.fill(this.key, null);
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    @Override
    public ObjectIterator<K> iterator() {
        return new SetIterator();
    }
    
    @Override
    public ObjectSpliterator<K> spliterator() {
        return new SetSpliterator();
    }
    
    @Override
    public void forEach(final Consumer<? super K> action) {
        if (this.containsNull) {
            action.accept((Object)this.key[this.n]);
        }
        final K[] key = this.key;
        int pos = this.n;
        while (pos-- != 0) {
            if (key[pos] != null) {
                action.accept((Object)key[pos]);
            }
        }
    }
    
    public boolean trim() {
        return this.trim(this.size);
    }
    
    public boolean trim(final int n) {
        final int l = HashCommon.nextPowerOfTwo((int)Math.ceil(n / this.f));
        if (l >= this.n || this.size > HashCommon.maxFill(l, this.f)) {
            return true;
        }
        try {
            this.rehash(l);
        }
        catch (OutOfMemoryError cantDoIt) {
            return false;
        }
        return true;
    }
    
    protected void rehash(final int newN) {
        final K[] key = this.key;
        final int mask = newN - 1;
        final K[] newKey = (K[])new Object[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == null) {}
            int pos;
            if (newKey[pos = (HashCommon.mix(key[i].hashCode()) & mask)] != null) {
                while (newKey[pos = (pos + 1 & mask)] != null) {}
            }
            newKey[pos] = key[i];
        }
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
    }
    
    public ObjectOpenHashSet<K> clone() {
        ObjectOpenHashSet<K> c;
        try {
            c = (ObjectOpenHashSet)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.key = this.key.clone();
        c.containsNull = this.containsNull;
        return c;
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int j = this.realSize();
        int i = 0;
        while (j-- != 0) {
            while (this.key[i] == null) {
                ++i;
            }
            if (this != this.key[i]) {
                h += this.key[i].hashCode();
            }
            ++i;
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final ObjectIterator<K> i = this.iterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            s.writeObject(i.next());
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        final Object[] key2 = new Object[this.n + 1];
        this.key = (K[])key2;
        final K[] key = (K[])key2;
        int i = this.size;
        while (i-- != 0) {
            final K k = (K)s.readObject();
            int pos;
            if (k == null) {
                pos = this.n;
                this.containsNull = true;
            }
            else if (key[pos = (HashCommon.mix(k.hashCode()) & this.mask)] != null) {
                while (key[pos = (pos + 1 & this.mask)] != null) {}
            }
            key[pos] = k;
        }
    }
    
    private void checkTable() {
    }
    
    static {
        TO_SET_COLLECTOR = Collector.of(ObjectOpenHashSet::new, ObjectOpenHashSet::add, ObjectOpenHashSet::combine, Collector.Characteristics.UNORDERED);
    }
    
    private final class SetIterator implements ObjectIterator<K>
    {
        int pos;
        int last;
        int c;
        boolean mustReturnNull;
        ObjectArrayList<K> wrapped;
        
        private SetIterator() {
            this.pos = ObjectOpenHashSet.this.n;
            this.last = -1;
            this.c = ObjectOpenHashSet.this.size;
            this.mustReturnNull = ObjectOpenHashSet.this.containsNull;
        }
        
        @Override
        public boolean hasNext() {
            return this.c != 0;
        }
        
        @Override
        public K next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            --this.c;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.last = ObjectOpenHashSet.this.n;
                return ObjectOpenHashSet.this.key[ObjectOpenHashSet.this.n];
            }
            final K[] key = ObjectOpenHashSet.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != null) {
                    final K[] array = key;
                    final int pos = this.pos;
                    this.last = pos;
                    return array[pos];
                }
            }
            this.last = Integer.MIN_VALUE;
            return this.wrapped.get(-this.pos - 1);
        }
        
        private final void shiftKeys(int pos) {
            final K[] key = ObjectOpenHashSet.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & ObjectOpenHashSet.this.mask);
                K curr;
                while ((curr = key[pos]) != null) {
                    final int slot = HashCommon.mix(curr.hashCode()) & ObjectOpenHashSet.this.mask;
                    Label_0102: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0102;
                            }
                            if (slot > pos) {
                                break Label_0102;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0102;
                        }
                        pos = (pos + 1 & ObjectOpenHashSet.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new ObjectArrayList<K>(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    continue Label_0009;
                }
                break;
            }
            key[last] = null;
        }
        
        @Override
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == ObjectOpenHashSet.this.n) {
                ObjectOpenHashSet.this.containsNull = false;
                ObjectOpenHashSet.this.key[ObjectOpenHashSet.this.n] = null;
            }
            else {
                if (this.pos < 0) {
                    ObjectOpenHashSet.this.remove(this.wrapped.set(-this.pos - 1, null));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final ObjectOpenHashSet this$0 = ObjectOpenHashSet.this;
            --this$0.size;
            this.last = -1;
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super K> action) {
            final K[] key = ObjectOpenHashSet.this.key;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                this.last = ObjectOpenHashSet.this.n;
                action.accept((Object)key[ObjectOpenHashSet.this.n]);
                --this.c;
            }
            while (this.c != 0) {
                if (--this.pos < 0) {
                    this.last = Integer.MIN_VALUE;
                    action.accept((Object)this.wrapped.get(-this.pos - 1));
                    --this.c;
                }
                else {
                    if (key[this.pos] == null) {
                        continue;
                    }
                    final K[] array = key;
                    final int pos = this.pos;
                    this.last = pos;
                    action.accept((Object)array[pos]);
                    --this.c;
                }
            }
        }
    }
    
    private final class SetSpliterator implements ObjectSpliterator<K>
    {
        private static final int POST_SPLIT_CHARACTERISTICS = 1;
        int pos;
        int max;
        int c;
        boolean mustReturnNull;
        boolean hasSplit;
        
        SetSpliterator() {
            this.pos = 0;
            this.max = ObjectOpenHashSet.this.n;
            this.c = 0;
            this.mustReturnNull = ObjectOpenHashSet.this.containsNull;
            this.hasSplit = false;
        }
        
        SetSpliterator(final int pos, final int max, final boolean mustReturnNull, final boolean hasSplit) {
            this.pos = 0;
            this.max = ObjectOpenHashSet.this.n;
            this.c = 0;
            this.mustReturnNull = ObjectOpenHashSet.this.containsNull;
            this.hasSplit = false;
            this.pos = pos;
            this.max = max;
            this.mustReturnNull = mustReturnNull;
            this.hasSplit = hasSplit;
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super K> action) {
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                ++this.c;
                action.accept((Object)ObjectOpenHashSet.this.key[ObjectOpenHashSet.this.n]);
                return true;
            }
            final K[] key = ObjectOpenHashSet.this.key;
            while (this.pos < this.max) {
                if (key[this.pos] != null) {
                    ++this.c;
                    action.accept((Object)key[this.pos++]);
                    return true;
                }
                ++this.pos;
            }
            return false;
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super K> action) {
            final K[] key = ObjectOpenHashSet.this.key;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                action.accept((Object)key[ObjectOpenHashSet.this.n]);
                ++this.c;
            }
            while (this.pos < this.max) {
                if (key[this.pos] != null) {
                    action.accept((Object)key[this.pos]);
                    ++this.c;
                }
                ++this.pos;
            }
        }
        
        @Override
        public int characteristics() {
            return this.hasSplit ? 1 : 65;
        }
        
        @Override
        public long estimateSize() {
            if (!this.hasSplit) {
                return ObjectOpenHashSet.this.size - this.c;
            }
            return Math.min(ObjectOpenHashSet.this.size - this.c, (long)(ObjectOpenHashSet.this.realSize() / (double)ObjectOpenHashSet.this.n * (this.max - this.pos)) + (long)(this.mustReturnNull ? 1 : 0));
        }
        
        @Override
        public SetSpliterator trySplit() {
            if (this.pos >= this.max - 1) {
                return null;
            }
            final int retLen = this.max - this.pos >> 1;
            if (retLen <= 1) {
                return null;
            }
            final int myNewPos = this.pos + retLen;
            final int retPos = this.pos;
            final int retMax = myNewPos;
            final SetSpliterator split = new SetSpliterator(retPos, retMax, this.mustReturnNull, true);
            this.pos = myNewPos;
            this.mustReturnNull = false;
            this.hasSplit = true;
            return split;
        }
        
        @Override
        public long skip(long n) {
            if (n < 0L) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (n == 0L) {
                return 0L;
            }
            long skipped = 0L;
            if (this.mustReturnNull) {
                this.mustReturnNull = false;
                ++skipped;
                --n;
            }
            final K[] key = ObjectOpenHashSet.this.key;
            while (this.pos < this.max && n > 0L) {
                if (key[this.pos++] != null) {
                    ++skipped;
                    --n;
                }
            }
            return skipped;
        }
    }
}
