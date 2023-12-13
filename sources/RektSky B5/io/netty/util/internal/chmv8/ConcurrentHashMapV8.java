/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal.chmv8;

import io.netty.util.internal.IntegerHolder;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.chmv8.CountedCompleter;
import io.netty.util.internal.chmv8.ForkJoinPool;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import sun.misc.Unsafe;

public class ConcurrentHashMapV8<K, V>
implements ConcurrentMap<K, V>,
Serializable {
    private static final long serialVersionUID = 7249069246763182397L;
    private static final int MAXIMUM_CAPACITY = 0x40000000;
    private static final int DEFAULT_CAPACITY = 16;
    static final int MAX_ARRAY_SIZE = 0x7FFFFFF7;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    private static final float LOAD_FACTOR = 0.75f;
    static final int TREEIFY_THRESHOLD = 8;
    static final int UNTREEIFY_THRESHOLD = 6;
    static final int MIN_TREEIFY_CAPACITY = 64;
    private static final int MIN_TRANSFER_STRIDE = 16;
    static final int MOVED = -1;
    static final int TREEBIN = -2;
    static final int RESERVED = -3;
    static final int HASH_BITS = Integer.MAX_VALUE;
    static final int NCPU = Runtime.getRuntime().availableProcessors();
    private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[]{new ObjectStreamField("segments", Segment[].class), new ObjectStreamField("segmentMask", Integer.TYPE), new ObjectStreamField("segmentShift", Integer.TYPE)};
    volatile transient Node<K, V>[] table;
    private volatile transient Node<K, V>[] nextTable;
    private volatile transient long baseCount;
    private volatile transient int sizeCtl;
    private volatile transient int transferIndex;
    private volatile transient int transferOrigin;
    private volatile transient int cellsBusy;
    private volatile transient CounterCell[] counterCells;
    private transient KeySetView<K, V> keySet;
    private transient ValuesView<K, V> values;
    private transient EntrySetView<K, V> entrySet;
    static final AtomicInteger counterHashCodeGenerator = new AtomicInteger();
    static final int SEED_INCREMENT = 1640531527;
    private static final Unsafe U;
    private static final long SIZECTL;
    private static final long TRANSFERINDEX;
    private static final long TRANSFERORIGIN;
    private static final long BASECOUNT;
    private static final long CELLSBUSY;
    private static final long CELLVALUE;
    private static final long ABASE;
    private static final int ASHIFT;

    static final int spread(int h2) {
        return (h2 ^ h2 >>> 16) & Integer.MAX_VALUE;
    }

    private static final int tableSizeFor(int c2) {
        int n2 = c2 - 1;
        n2 |= n2 >>> 1;
        n2 |= n2 >>> 2;
        n2 |= n2 >>> 4;
        n2 |= n2 >>> 8;
        return (n2 |= n2 >>> 16) < 0 ? 1 : (n2 >= 0x40000000 ? 0x40000000 : n2 + 1);
    }

    static Class<?> comparableClassFor(Object x2) {
        if (x2 instanceof Comparable) {
            Class<?> c2 = x2.getClass();
            if (c2 == String.class) {
                return c2;
            }
            Type[] ts = c2.getGenericInterfaces();
            if (ts != null) {
                for (int i2 = 0; i2 < ts.length; ++i2) {
                    Type[] as;
                    ParameterizedType p2;
                    Type t2 = ts[i2];
                    if (!(t2 instanceof ParameterizedType) || (p2 = (ParameterizedType)t2).getRawType() != Comparable.class || (as = p2.getActualTypeArguments()) == null || as.length != 1 || as[0] != c2) continue;
                    return c2;
                }
            }
        }
        return null;
    }

    static int compareComparables(Class<?> kc, Object k2, Object x2) {
        return x2 == null || x2.getClass() != kc ? 0 : ((Comparable)k2).compareTo(x2);
    }

    static final <K, V> Node<K, V> tabAt(Node<K, V>[] tab, int i2) {
        return (Node)U.getObjectVolatile(tab, ((long)i2 << ASHIFT) + ABASE);
    }

    static final <K, V> boolean casTabAt(Node<K, V>[] tab, int i2, Node<K, V> c2, Node<K, V> v2) {
        return U.compareAndSwapObject(tab, ((long)i2 << ASHIFT) + ABASE, c2, v2);
    }

    static final <K, V> void setTabAt(Node<K, V>[] tab, int i2, Node<K, V> v2) {
        U.putObjectVolatile(tab, ((long)i2 << ASHIFT) + ABASE, v2);
    }

    public ConcurrentHashMapV8() {
    }

    public ConcurrentHashMapV8(int initialCapacity) {
        int cap;
        if (initialCapacity < 0) {
            throw new IllegalArgumentException();
        }
        this.sizeCtl = cap = initialCapacity >= 0x20000000 ? 0x40000000 : ConcurrentHashMapV8.tableSizeFor(initialCapacity + (initialCapacity >>> 1) + 1);
    }

    public ConcurrentHashMapV8(Map<? extends K, ? extends V> m2) {
        this.sizeCtl = 16;
        this.putAll(m2);
    }

    public ConcurrentHashMapV8(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, 1);
    }

    public ConcurrentHashMapV8(int initialCapacity, float loadFactor, int concurrencyLevel) {
        long size;
        int cap;
        if (!(loadFactor > 0.0f) || initialCapacity < 0 || concurrencyLevel <= 0) {
            throw new IllegalArgumentException();
        }
        if (initialCapacity < concurrencyLevel) {
            initialCapacity = concurrencyLevel;
        }
        this.sizeCtl = cap = (size = (long)(1.0 + (double)((float)initialCapacity / loadFactor))) >= 0x40000000L ? 0x40000000 : ConcurrentHashMapV8.tableSizeFor((int)size);
    }

    @Override
    public int size() {
        long n2 = this.sumCount();
        return n2 < 0L ? 0 : (n2 > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)n2);
    }

    @Override
    public boolean isEmpty() {
        return this.sumCount() <= 0L;
    }

    @Override
    public V get(Object key) {
        Node<K, V> e2;
        int n2;
        int h2 = ConcurrentHashMapV8.spread(key.hashCode());
        Node<K, V>[] tab = this.table;
        if (this.table != null && (n2 = tab.length) > 0 && (e2 = ConcurrentHashMapV8.tabAt(tab, n2 - 1 & h2)) != null) {
            Object ek;
            int eh = e2.hash;
            if (eh == h2) {
                ek = e2.key;
                if (ek == key || ek != null && key.equals(ek)) {
                    return e2.val;
                }
            } else if (eh < 0) {
                Node<K, V> p2 = e2.find(h2, key);
                return p2 != null ? (V)p2.val : null;
            }
            while ((e2 = e2.next) != null) {
                if (e2.hash != h2 || (ek = e2.key) != key && (ek == null || !key.equals(ek))) continue;
                return e2.val;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return this.get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        Node<K, V>[] t2 = this.table;
        if (this.table != null) {
            Node<K, V> p2;
            Traverser<K, V> it = new Traverser<K, V>(t2, t2.length, 0, t2.length);
            while ((p2 = it.advance()) != null) {
                Object v2 = p2.val;
                if (v2 != value && (v2 == null || !value.equals(v2))) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public V put(K key, V value) {
        return this.putVal(key, value, false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    final V putVal(K key, V value, boolean onlyIfAbsent) {
        int binCount;
        block19: {
            V oldVal;
            int i2;
            if (key == null || value == null) {
                throw new NullPointerException();
            }
            int hash = ConcurrentHashMapV8.spread(key.hashCode());
            binCount = 0;
            Node<K, V>[] tab = this.table;
            while (true) {
                int n2;
                if (tab == null || (n2 = tab.length) == 0) {
                    tab = this.initTable();
                    continue;
                }
                i2 = n2 - 1 & hash;
                Node<K, V> f2 = ConcurrentHashMapV8.tabAt(tab, i2);
                if (f2 == null) {
                    if (!ConcurrentHashMapV8.casTabAt(tab, i2, null, new Node<K, V>(hash, key, value, null))) continue;
                    break block19;
                }
                int fh = f2.hash;
                if (fh == -1) {
                    tab = this.helpTransfer(tab, f2);
                    continue;
                }
                oldVal = null;
                Node<K, V> node = f2;
                synchronized (node) {
                    block20: {
                        if (ConcurrentHashMapV8.tabAt(tab, i2) == f2) {
                            if (fh >= 0) {
                                binCount = 1;
                                Node<K, V> e2 = f2;
                                while (true) {
                                    Object ek;
                                    if (e2.hash == hash && ((ek = e2.key) == key || ek != null && key.equals(ek))) {
                                        oldVal = e2.val;
                                        if (!onlyIfAbsent) {
                                            e2.val = value;
                                        }
                                        break block20;
                                    }
                                    Node<K, V> pred = e2;
                                    e2 = e2.next;
                                    if (e2 == null) {
                                        pred.next = new Node<K, V>(hash, key, value, null);
                                        break block20;
                                    }
                                    ++binCount;
                                }
                            }
                            if (f2 instanceof TreeBin) {
                                binCount = 2;
                                TreeNode<K, V> p2 = ((TreeBin)f2).putTreeVal(hash, key, value);
                                if (p2 != null) {
                                    oldVal = p2.val;
                                    if (!onlyIfAbsent) {
                                        p2.val = value;
                                    }
                                }
                            }
                        }
                    }
                }
                if (binCount != 0) break;
            }
            if (binCount >= 8) {
                this.treeifyBin(tab, i2);
            }
            if (oldVal != null) {
                return oldVal;
            }
        }
        this.addCount(1L, binCount);
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m2) {
        this.tryPresize(m2.size());
        for (Map.Entry<K, V> e2 : m2.entrySet()) {
            this.putVal(e2.getKey(), e2.getValue(), false);
        }
    }

    @Override
    public V remove(Object key) {
        return this.replaceNode(key, null, null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    final V replaceNode(Object key, V value, Object cv) {
        int i2;
        Node<K, V> f2;
        int n2;
        int hash = ConcurrentHashMapV8.spread(key.hashCode());
        Node<K, V>[] tab = this.table;
        while (tab != null && (n2 = tab.length) != 0 && (f2 = ConcurrentHashMapV8.tabAt(tab, i2 = n2 - 1 & hash)) != null) {
            int fh = f2.hash;
            if (fh == -1) {
                tab = this.helpTransfer(tab, f2);
                continue;
            }
            Object oldVal = null;
            boolean validated = false;
            Node<K, V> node = f2;
            synchronized (node) {
                if (ConcurrentHashMapV8.tabAt(tab, i2) == f2) {
                    if (fh >= 0) {
                        validated = true;
                        Node<K, V> e2 = f2;
                        Node<K, V> pred = null;
                        do {
                            Object ek;
                            if (e2.hash == hash && ((ek = e2.key) == key || ek != null && key.equals(ek))) {
                                Object ev = e2.val;
                                if (cv == null || cv == ev || ev != null && cv.equals(ev)) {
                                    oldVal = ev;
                                    if (value != null) {
                                        e2.val = value;
                                    } else if (pred != null) {
                                        pred.next = e2.next;
                                    } else {
                                        ConcurrentHashMapV8.setTabAt(tab, i2, e2.next);
                                    }
                                }
                                break;
                            }
                            pred = e2;
                        } while ((e2 = e2.next) != null);
                    } else if (f2 instanceof TreeBin) {
                        TreeNode p2;
                        validated = true;
                        TreeBin t2 = (TreeBin)f2;
                        TreeNode r2 = t2.root;
                        if (r2 != null && (p2 = r2.findTreeNode(hash, key, null)) != null) {
                            Object pv = p2.val;
                            if (cv == null || cv == pv || pv != null && cv.equals(pv)) {
                                oldVal = pv;
                                if (value != null) {
                                    p2.val = value;
                                } else if (t2.removeTreeNode(p2)) {
                                    ConcurrentHashMapV8.setTabAt(tab, i2, ConcurrentHashMapV8.untreeify(t2.first));
                                }
                            }
                        }
                    }
                }
            }
            if (!validated) continue;
            if (oldVal == null) break;
            if (value == null) {
                this.addCount(-1L, -1);
            }
            return (V)oldVal;
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void clear() {
        long delta = 0L;
        int i2 = 0;
        Node<K, V>[] tab = this.table;
        while (tab != null && i2 < tab.length) {
            Node<K, V> f2 = ConcurrentHashMapV8.tabAt(tab, i2);
            if (f2 == null) {
                ++i2;
                continue;
            }
            int fh = f2.hash;
            if (fh == -1) {
                tab = this.helpTransfer(tab, f2);
                i2 = 0;
                continue;
            }
            Node<K, V> node = f2;
            synchronized (node) {
                if (ConcurrentHashMapV8.tabAt(tab, i2) == f2) {
                    Node<K, V> p2;
                    Node<K, V> node2 = fh >= 0 ? f2 : (p2 = f2 instanceof TreeBin ? ((TreeBin)f2).first : null);
                    while (p2 != null) {
                        --delta;
                        p2 = p2.next;
                    }
                    ConcurrentHashMapV8.setTabAt(tab, i2++, null);
                }
            }
        }
        if (delta != 0L) {
            this.addCount(delta, -1);
        }
    }

    public KeySetView<K, V> keySet() {
        KeySetView<K, V> ks = this.keySet;
        return ks != null ? ks : (this.keySet = new KeySetView(this, null));
    }

    @Override
    public Collection<V> values() {
        ValuesView<K, V> vs = this.values;
        return vs != null ? vs : (this.values = new ValuesView(this));
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        EntrySetView<K, V> es = this.entrySet;
        return es != null ? es : (this.entrySet = new EntrySetView(this));
    }

    @Override
    public int hashCode() {
        int h2 = 0;
        Node<K, V>[] t2 = this.table;
        if (this.table != null) {
            Node<K, V> p2;
            Traverser<K, V> it = new Traverser<K, V>(t2, t2.length, 0, t2.length);
            while ((p2 = it.advance()) != null) {
                h2 += p2.key.hashCode() ^ p2.val.hashCode();
            }
        }
        return h2;
    }

    public String toString() {
        Node<K, V>[] t2 = this.table;
        int f2 = this.table == null ? 0 : t2.length;
        Traverser<K, V> it = new Traverser<K, V>(t2, f2, 0, f2);
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        Node<K, V> p2 = it.advance();
        if (p2 != null) {
            while (true) {
                Object k2 = p2.key;
                Object v2 = p2.val;
                sb.append((Object)(k2 == this ? "(this Map)" : k2));
                sb.append('=');
                sb.append((Object)(v2 == this ? "(this Map)" : v2));
                p2 = it.advance();
                if (p2 == null) break;
                sb.append(',').append(' ');
            }
        }
        return sb.append('}').toString();
    }

    @Override
    public boolean equals(Object o2) {
        if (o2 != this) {
            Node<K, V> p2;
            if (!(o2 instanceof Map)) {
                return false;
            }
            Map m2 = (Map)o2;
            Node<K, V>[] t2 = this.table;
            int f2 = this.table == null ? 0 : t2.length;
            Traverser<K, V> it = new Traverser<K, V>(t2, f2, 0, f2);
            while ((p2 = it.advance()) != null) {
                Object val2 = p2.val;
                Object v2 = m2.get(p2.key);
                if (v2 != null && (v2 == val2 || v2.equals(val2))) continue;
                return false;
            }
            for (Map.Entry e2 : m2.entrySet()) {
                V v3;
                Object mv;
                Object mk = e2.getKey();
                if (mk != null && (mv = e2.getValue()) != null && (v3 = this.get(mk)) != null && (mv == v3 || mv.equals(v3))) continue;
                return false;
            }
        }
        return true;
    }

    private void writeObject(ObjectOutputStream s2) throws IOException {
        int ssize;
        int sshift = 0;
        for (ssize = 1; ssize < 16; ssize <<= 1) {
            ++sshift;
        }
        int segmentShift = 32 - sshift;
        int segmentMask = ssize - 1;
        Segment[] segments = new Segment[16];
        for (int i2 = 0; i2 < segments.length; ++i2) {
            segments[i2] = new Segment(0.75f);
        }
        s2.putFields().put("segments", segments);
        s2.putFields().put("segmentShift", segmentShift);
        s2.putFields().put("segmentMask", segmentMask);
        s2.writeFields();
        Node<K, V>[] t2 = this.table;
        if (this.table != null) {
            Node<K, V> p2;
            Traverser<K, V> it = new Traverser<K, V>(t2, t2.length, 0, t2.length);
            while ((p2 = it.advance()) != null) {
                s2.writeObject(p2.key);
                s2.writeObject(p2.val);
            }
        }
        s2.writeObject(null);
        s2.writeObject(null);
        segments = null;
    }

    private void readObject(ObjectInputStream s2) throws IOException, ClassNotFoundException {
        this.sizeCtl = -1;
        s2.defaultReadObject();
        long size = 0L;
        Node<Object, Object> p2 = null;
        while (true) {
            Object k2 = s2.readObject();
            Object v2 = s2.readObject();
            if (k2 == null || v2 == null) break;
            p2 = new Node<Object, Object>(ConcurrentHashMapV8.spread(k2.hashCode()), k2, v2, p2);
            ++size;
        }
        if (size == 0L) {
            this.sizeCtl = 0;
        } else {
            int n2;
            if (size >= 0x20000000L) {
                n2 = 0x40000000;
            } else {
                int sz = (int)size;
                n2 = ConcurrentHashMapV8.tableSizeFor(sz + (sz >>> 1) + 1);
            }
            Node[] tab = new Node[n2];
            int mask = n2 - 1;
            long added = 0L;
            while (p2 != null) {
                boolean insertAtFront;
                Node next = p2.next;
                int h2 = p2.hash;
                int j2 = h2 & mask;
                Node<K, V> first = ConcurrentHashMapV8.tabAt(tab, j2);
                if (first == null) {
                    insertAtFront = true;
                } else {
                    Object k3 = p2.key;
                    if (first.hash < 0) {
                        TreeBin t2 = (TreeBin)first;
                        if (t2.putTreeVal(h2, k3, p2.val) == null) {
                            ++added;
                        }
                        insertAtFront = false;
                    } else {
                        int binCount = 0;
                        insertAtFront = true;
                        Node<Object, Object> q2 = first;
                        while (q2 != null) {
                            Object qk;
                            if (q2.hash == h2 && ((qk = q2.key) == k3 || qk != null && k3.equals(qk))) {
                                insertAtFront = false;
                                break;
                            }
                            ++binCount;
                            q2 = q2.next;
                        }
                        if (insertAtFront && binCount >= 8) {
                            insertAtFront = false;
                            ++added;
                            p2.next = first;
                            TreeNode hd = null;
                            TreeNode tl = null;
                            q2 = p2;
                            while (q2 != null) {
                                TreeNode t3 = new TreeNode(q2.hash, q2.key, q2.val, null, null);
                                t3.prev = tl;
                                if (t3.prev == null) {
                                    hd = t3;
                                } else {
                                    tl.next = t3;
                                }
                                tl = t3;
                                q2 = q2.next;
                            }
                            ConcurrentHashMapV8.setTabAt(tab, j2, new TreeBin(hd));
                        }
                    }
                }
                if (insertAtFront) {
                    ++added;
                    p2.next = first;
                    ConcurrentHashMapV8.setTabAt(tab, j2, p2);
                }
                p2 = next;
            }
            this.table = tab;
            this.sizeCtl = n2 - (n2 >>> 2);
            this.baseCount = added;
        }
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return this.putVal(key, value, true);
    }

    @Override
    public boolean remove(Object key, Object value) {
        if (key == null) {
            throw new NullPointerException();
        }
        return value != null && this.replaceNode(key, null, value) != null;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        if (key == null || oldValue == null || newValue == null) {
            throw new NullPointerException();
        }
        return this.replaceNode(key, newValue, oldValue) != null;
    }

    @Override
    public V replace(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        return this.replaceNode(key, value, null);
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        V v2 = this.get(key);
        return v2 == null ? defaultValue : v2;
    }

    @Override
    public void forEach(BiAction<? super K, ? super V> action) {
        if (action == null) {
            throw new NullPointerException();
        }
        Node<K, V>[] t2 = this.table;
        if (this.table != null) {
            Node<K, V> p2;
            Traverser<K, V> it = new Traverser<K, V>(t2, t2.length, 0, t2.length);
            while ((p2 = it.advance()) != null) {
                action.apply(p2.key, p2.val);
            }
        }
    }

    @Override
    public void replaceAll(BiFun<? super K, ? super V, ? extends V> function) {
        if (function == null) {
            throw new NullPointerException();
        }
        Node<K, V>[] t2 = this.table;
        if (this.table != null) {
            Node<K, V> p2;
            Traverser<K, V> it = new Traverser<K, V>(t2, t2.length, 0, t2.length);
            while ((p2 = it.advance()) != null) {
                V newValue;
                Object oldValue = p2.val;
                Object key = p2.key;
                do {
                    if ((newValue = function.apply(key, oldValue)) != null) continue;
                    throw new NullPointerException();
                } while (this.replaceNode(key, newValue, oldValue) == null && (oldValue = this.get(key)) != null);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public V computeIfAbsent(K key, Fun<? super K, ? extends V> mappingFunction) {
        int binCount;
        Object val2;
        block30: {
            boolean added;
            int i2;
            if (key == null || mappingFunction == null) {
                throw new NullPointerException();
            }
            int h2 = ConcurrentHashMapV8.spread(key.hashCode());
            val2 = null;
            binCount = 0;
            Node<K, V>[] tab = this.table;
            while (true) {
                Node node;
                int n2;
                if (tab == null || (n2 = tab.length) == 0) {
                    tab = this.initTable();
                    continue;
                }
                i2 = n2 - 1 & h2;
                Node<K, V> f2 = ConcurrentHashMapV8.tabAt(tab, i2);
                if (f2 == null) {
                    ReservationNode r2;
                    node = r2 = new ReservationNode();
                    synchronized (node) {
                        if (ConcurrentHashMapV8.casTabAt(tab, i2, null, r2)) {
                            binCount = 1;
                            Node<K, Object> node2 = null;
                            try {
                                V v2 = mappingFunction.apply(key);
                                val2 = v2;
                                if (v2 != null) {
                                    node2 = new Node<K, Object>(h2, key, val2, null);
                                }
                            }
                            finally {
                                ConcurrentHashMapV8.setTabAt(tab, i2, node2);
                            }
                        }
                    }
                    if (binCount == 0) continue;
                    break block30;
                }
                int fh = f2.hash;
                if (fh == -1) {
                    tab = this.helpTransfer(tab, f2);
                    continue;
                }
                added = false;
                node = f2;
                synchronized (node) {
                    block31: {
                        if (ConcurrentHashMapV8.tabAt(tab, i2) == f2) {
                            if (fh >= 0) {
                                binCount = 1;
                                Node<K, V> e2 = f2;
                                while (true) {
                                    Object ek;
                                    if (e2.hash == h2 && ((ek = e2.key) == key || ek != null && key.equals(ek))) {
                                        val2 = e2.val;
                                        break block31;
                                    }
                                    Node<K, V> pred = e2;
                                    e2 = e2.next;
                                    if (e2 == null) {
                                        V v3 = mappingFunction.apply(key);
                                        val2 = v3;
                                        if (v3 != null) {
                                            added = true;
                                            pred.next = new Node<K, Object>(h2, key, val2, null);
                                        }
                                        break block31;
                                    }
                                    ++binCount;
                                }
                            }
                            if (f2 instanceof TreeBin) {
                                TreeNode p2;
                                binCount = 2;
                                TreeBin t2 = (TreeBin)f2;
                                TreeNode r3 = t2.root;
                                if (r3 != null && (p2 = r3.findTreeNode(h2, key, null)) != null) {
                                    val2 = p2.val;
                                } else {
                                    V v4 = mappingFunction.apply(key);
                                    val2 = v4;
                                    if (v4 != null) {
                                        added = true;
                                        t2.putTreeVal(h2, key, val2);
                                    }
                                }
                            }
                        }
                    }
                }
                if (binCount != 0) break;
            }
            if (binCount >= 8) {
                this.treeifyBin(tab, i2);
            }
            if (!added) {
                return (V)val2;
            }
        }
        if (val2 != null) {
            this.addCount(1L, binCount);
        }
        return (V)val2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public V computeIfPresent(K key, BiFun<? super K, ? super V, ? extends V> remappingFunction) {
        if (key == null || remappingFunction == null) {
            throw new NullPointerException();
        }
        int h2 = ConcurrentHashMapV8.spread(key.hashCode());
        V val2 = null;
        int delta = 0;
        int binCount = 0;
        Node<K, V>[] tab = this.table;
        while (true) {
            int n2;
            if (tab == null || (n2 = tab.length) == 0) {
                tab = this.initTable();
                continue;
            }
            int i2 = n2 - 1 & h2;
            Node<K, V> f2 = ConcurrentHashMapV8.tabAt(tab, i2);
            if (f2 == null) break;
            int fh = f2.hash;
            if (fh == -1) {
                tab = this.helpTransfer(tab, f2);
                continue;
            }
            Node<K, V> node = f2;
            synchronized (node) {
                if (ConcurrentHashMapV8.tabAt(tab, i2) == f2) {
                    if (fh >= 0) {
                        binCount = 1;
                        Node<K, V> e2 = f2;
                        Node<K, V> pred = null;
                        while (true) {
                            Object ek;
                            if (e2.hash == h2 && ((ek = e2.key) == key || ek != null && key.equals(ek))) {
                                val2 = remappingFunction.apply(key, e2.val);
                                if (val2 != null) {
                                    e2.val = val2;
                                } else {
                                    delta = -1;
                                    Node en = e2.next;
                                    if (pred != null) {
                                        pred.next = en;
                                    } else {
                                        ConcurrentHashMapV8.setTabAt(tab, i2, en);
                                    }
                                }
                            } else {
                                pred = e2;
                                e2 = e2.next;
                                if (e2 != null) {
                                    ++binCount;
                                    continue;
                                }
                            }
                            break;
                        }
                    } else if (f2 instanceof TreeBin) {
                        TreeNode p2;
                        binCount = 2;
                        TreeBin t2 = (TreeBin)f2;
                        TreeNode r2 = t2.root;
                        if (r2 != null && (p2 = r2.findTreeNode(h2, key, null)) != null) {
                            val2 = remappingFunction.apply(key, p2.val);
                            if (val2 != null) {
                                p2.val = val2;
                            } else {
                                delta = -1;
                                if (t2.removeTreeNode(p2)) {
                                    ConcurrentHashMapV8.setTabAt(tab, i2, ConcurrentHashMapV8.untreeify(t2.first));
                                }
                            }
                        }
                    }
                }
            }
            if (binCount != 0) break;
        }
        if (delta != 0) {
            this.addCount(delta, binCount);
        }
        return val2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public V compute(K key, BiFun<? super K, ? super V, ? extends V> remappingFunction) {
        int binCount;
        int delta;
        Object val2;
        block36: {
            int i2;
            if (key == null || remappingFunction == null) {
                throw new NullPointerException();
            }
            int h2 = ConcurrentHashMapV8.spread(key.hashCode());
            val2 = null;
            delta = 0;
            binCount = 0;
            Node<K, V>[] tab = this.table;
            while (true) {
                int n2;
                if (tab == null || (n2 = tab.length) == 0) {
                    tab = this.initTable();
                    continue;
                }
                i2 = n2 - 1 & h2;
                Node<K, V> f2 = ConcurrentHashMapV8.tabAt(tab, i2);
                if (f2 == null) {
                    ReservationNode r2;
                    ReservationNode reservationNode = r2 = new ReservationNode();
                    synchronized (reservationNode) {
                        if (ConcurrentHashMapV8.casTabAt(tab, i2, null, r2)) {
                            binCount = 1;
                            Node<K, Object> node = null;
                            try {
                                V v2 = remappingFunction.apply(key, null);
                                val2 = v2;
                                if (v2 != null) {
                                    delta = 1;
                                    node = new Node<K, Object>(h2, key, val2, null);
                                }
                            }
                            finally {
                                ConcurrentHashMapV8.setTabAt(tab, i2, node);
                            }
                        }
                    }
                    if (binCount == 0) continue;
                    break block36;
                }
                int fh = f2.hash;
                if (fh == -1) {
                    tab = this.helpTransfer(tab, f2);
                    continue;
                }
                Node<K, V> node = f2;
                synchronized (node) {
                    block37: {
                        if (ConcurrentHashMapV8.tabAt(tab, i2) == f2) {
                            if (fh >= 0) {
                                binCount = 1;
                                Node<K, V> e2 = f2;
                                Node<K, V> pred = null;
                                while (true) {
                                    Object ek;
                                    if (e2.hash == h2 && ((ek = e2.key) == key || ek != null && key.equals(ek))) {
                                        val2 = remappingFunction.apply(key, e2.val);
                                        if (val2 != null) {
                                            e2.val = val2;
                                        } else {
                                            delta = -1;
                                            Node en = e2.next;
                                            if (pred != null) {
                                                pred.next = en;
                                            } else {
                                                ConcurrentHashMapV8.setTabAt(tab, i2, en);
                                            }
                                        }
                                        break block37;
                                    }
                                    pred = e2;
                                    e2 = e2.next;
                                    if (e2 == null) {
                                        val2 = remappingFunction.apply(key, null);
                                        if (val2 != null) {
                                            delta = 1;
                                            pred.next = new Node<K, Object>(h2, key, val2, null);
                                        }
                                        break block37;
                                    }
                                    ++binCount;
                                }
                            }
                            if (f2 instanceof TreeBin) {
                                binCount = 1;
                                TreeBin t2 = (TreeBin)f2;
                                TreeNode r3 = t2.root;
                                TreeNode p2 = r3 != null ? r3.findTreeNode(h2, key, null) : null;
                                Object pv = p2 == null ? null : p2.val;
                                val2 = remappingFunction.apply(key, pv);
                                if (val2 != null) {
                                    if (p2 != null) {
                                        p2.val = val2;
                                    } else {
                                        delta = 1;
                                        t2.putTreeVal(h2, key, val2);
                                    }
                                } else if (p2 != null) {
                                    delta = -1;
                                    if (t2.removeTreeNode(p2)) {
                                        ConcurrentHashMapV8.setTabAt(tab, i2, ConcurrentHashMapV8.untreeify(t2.first));
                                    }
                                }
                            }
                        }
                    }
                }
                if (binCount != 0) break;
            }
            if (binCount >= 8) {
                this.treeifyBin(tab, i2);
            }
        }
        if (delta != 0) {
            this.addCount(delta, binCount);
        }
        return val2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public V merge(K key, V value, BiFun<? super V, ? super V, ? extends V> remappingFunction) {
        int binCount;
        int delta;
        Object val2;
        block26: {
            int i2;
            if (key == null || value == null || remappingFunction == null) {
                throw new NullPointerException();
            }
            int h2 = ConcurrentHashMapV8.spread(key.hashCode());
            val2 = null;
            delta = 0;
            binCount = 0;
            Node<K, V>[] tab = this.table;
            while (true) {
                int n2;
                if (tab == null || (n2 = tab.length) == 0) {
                    tab = this.initTable();
                    continue;
                }
                i2 = n2 - 1 & h2;
                Node<K, V> f2 = ConcurrentHashMapV8.tabAt(tab, i2);
                if (f2 == null) {
                    if (!ConcurrentHashMapV8.casTabAt(tab, i2, null, new Node<K, V>(h2, key, value, null))) continue;
                    delta = 1;
                    val2 = value;
                    break block26;
                }
                int fh = f2.hash;
                if (fh == -1) {
                    tab = this.helpTransfer(tab, f2);
                    continue;
                }
                Node<K, V> node = f2;
                synchronized (node) {
                    block27: {
                        if (ConcurrentHashMapV8.tabAt(tab, i2) == f2) {
                            if (fh >= 0) {
                                binCount = 1;
                                Node<K, V> e2 = f2;
                                Node<K, V> pred = null;
                                while (true) {
                                    Object ek;
                                    if (e2.hash == h2 && ((ek = e2.key) == key || ek != null && key.equals(ek))) {
                                        val2 = remappingFunction.apply(e2.val, value);
                                        if (val2 != null) {
                                            e2.val = val2;
                                        } else {
                                            delta = -1;
                                            Node en = e2.next;
                                            if (pred != null) {
                                                pred.next = en;
                                            } else {
                                                ConcurrentHashMapV8.setTabAt(tab, i2, en);
                                            }
                                        }
                                        break block27;
                                    }
                                    pred = e2;
                                    e2 = e2.next;
                                    if (e2 == null) {
                                        delta = 1;
                                        val2 = value;
                                        pred.next = new Node<K, Object>(h2, key, val2, null);
                                        break block27;
                                    }
                                    ++binCount;
                                }
                            }
                            if (f2 instanceof TreeBin) {
                                binCount = 2;
                                TreeBin t2 = (TreeBin)f2;
                                TreeNode r2 = t2.root;
                                TreeNode p2 = r2 == null ? null : r2.findTreeNode(h2, key, null);
                                val2 = p2 == null ? value : remappingFunction.apply(p2.val, value);
                                if (val2 != null) {
                                    if (p2 != null) {
                                        p2.val = val2;
                                    } else {
                                        delta = 1;
                                        t2.putTreeVal(h2, key, val2);
                                    }
                                } else if (p2 != null) {
                                    delta = -1;
                                    if (t2.removeTreeNode(p2)) {
                                        ConcurrentHashMapV8.setTabAt(tab, i2, ConcurrentHashMapV8.untreeify(t2.first));
                                    }
                                }
                            }
                        }
                    }
                }
                if (binCount != 0) break;
            }
            if (binCount >= 8) {
                this.treeifyBin(tab, i2);
            }
        }
        if (delta != 0) {
            this.addCount(delta, binCount);
        }
        return val2;
    }

    @Deprecated
    public boolean contains(Object value) {
        return this.containsValue(value);
    }

    public Enumeration<K> keys() {
        Node<K, V>[] t2 = this.table;
        int f2 = this.table == null ? 0 : t2.length;
        return new KeyIterator<K, V>(t2, f2, 0, f2, this);
    }

    public Enumeration<V> elements() {
        Node<K, V>[] t2 = this.table;
        int f2 = this.table == null ? 0 : t2.length;
        return new ValueIterator<K, V>(t2, f2, 0, f2, this);
    }

    public long mappingCount() {
        long n2 = this.sumCount();
        return n2 < 0L ? 0L : n2;
    }

    public static <K> KeySetView<K, Boolean> newKeySet() {
        return new KeySetView<K, Boolean>(new ConcurrentHashMapV8(), Boolean.TRUE);
    }

    public static <K> KeySetView<K, Boolean> newKeySet(int initialCapacity) {
        return new KeySetView<K, Boolean>(new ConcurrentHashMapV8(initialCapacity), Boolean.TRUE);
    }

    public KeySetView<K, V> keySet(V mappedValue) {
        if (mappedValue == null) {
            throw new NullPointerException();
        }
        return new KeySetView(this, mappedValue);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final Node<K, V>[] initTable() {
        Node<K, V>[] tab;
        block6: {
            int sc;
            while (true) {
                tab = this.table;
                if (this.table != null && tab.length != 0) break block6;
                sc = this.sizeCtl;
                if (sc < 0) {
                    Thread.yield();
                    continue;
                }
                if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) break;
            }
            try {
                tab = this.table;
                if (this.table == null || tab.length == 0) {
                    int n2 = sc > 0 ? sc : 16;
                    Node[] nt = new Node[n2];
                    tab = nt;
                    this.table = nt;
                    sc = n2 - (n2 >>> 2);
                }
            }
            finally {
                this.sizeCtl = sc;
            }
        }
        return tab;
    }

    private final void addCount(long x2, int check) {
        long s2;
        long b2;
        CounterCell[] as = this.counterCells;
        if (this.counterCells != null || !U.compareAndSwapLong(this, BASECOUNT, b2 = this.baseCount, s2 = b2 + x2)) {
            long v2;
            CounterCell a2;
            int m2;
            boolean uncontended = true;
            InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
            IntegerHolder hc = threadLocals.counterHashCode();
            if (hc == null || as == null || (m2 = as.length - 1) < 0 || (a2 = as[m2 & hc.value]) == null || !(uncontended = U.compareAndSwapLong(a2, CELLVALUE, v2 = a2.value, v2 + x2))) {
                this.fullAddCount(threadLocals, x2, hc, uncontended);
                return;
            }
            if (check <= 1) {
                return;
            }
            s2 = this.sumCount();
        }
        if (check >= 0) {
            int sc;
            while (s2 >= (long)(sc = this.sizeCtl)) {
                Node<K, V>[] tab = this.table;
                if (this.table == null || tab.length >= 0x40000000) break;
                if (sc < 0) {
                    if (sc == -1 || this.transferIndex <= this.transferOrigin) break;
                    Node<K, V>[] nt = this.nextTable;
                    if (this.nextTable == null) break;
                    if (U.compareAndSwapInt(this, SIZECTL, sc, sc - 1)) {
                        this.transfer(tab, nt);
                    }
                } else if (U.compareAndSwapInt(this, SIZECTL, sc, -2)) {
                    this.transfer(tab, null);
                }
                s2 = this.sumCount();
            }
        }
    }

    final Node<K, V>[] helpTransfer(Node<K, V>[] tab, Node<K, V> f2) {
        if (f2 instanceof ForwardingNode) {
            Node<K, V>[] nextTab = ((ForwardingNode)f2).nextTable;
            if (((ForwardingNode)f2).nextTable != null) {
                int sc;
                if (nextTab == this.nextTable && tab == this.table && this.transferIndex > this.transferOrigin && (sc = this.sizeCtl) < -1 && U.compareAndSwapInt(this, SIZECTL, sc, sc - 1)) {
                    this.transfer(tab, nextTab);
                }
                return nextTab;
            }
        }
        return this.table;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final void tryPresize(int size) {
        int sc;
        int c2;
        int n2 = c2 = size >= 0x20000000 ? 0x40000000 : ConcurrentHashMapV8.tableSizeFor(size + (size >>> 1) + 1);
        while ((sc = this.sizeCtl) >= 0) {
            int n3;
            Node<K, V>[] tab = this.table;
            if (tab == null || (n3 = tab.length) == 0) {
                int n4 = n3 = sc > c2 ? sc : c2;
                if (!U.compareAndSwapInt(this, SIZECTL, sc, -1)) continue;
                try {
                    if (this.table != tab) continue;
                    Node[] nt = new Node[n3];
                    this.table = nt;
                    sc = n3 - (n3 >>> 2);
                    continue;
                }
                finally {
                    this.sizeCtl = sc;
                    continue;
                }
            }
            if (c2 <= sc || n3 >= 0x40000000) break;
            if (tab != this.table || !U.compareAndSwapInt(this, SIZECTL, sc, -2)) continue;
            this.transfer(tab, null);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final void transfer(Node<K, V>[] tab, Node<K, V>[] nextTab) {
        int n2 = tab.length;
        int stride = NCPU > 1 ? (n2 >>> 3) / NCPU : n2;
        if (stride < 16) {
            stride = 16;
        }
        if (nextTab == null) {
            try {
                Node[] nt = new Node[n2 << 1];
                nextTab = nt;
            }
            catch (Throwable ex) {
                this.sizeCtl = Integer.MAX_VALUE;
                return;
            }
            this.nextTable = nextTab;
            this.transferOrigin = n2;
            this.transferIndex = n2;
            ForwardingNode<K, V> rev = new ForwardingNode<K, V>(tab);
            int k2 = n2;
            while (k2 > 0) {
                int nextk;
                int m2;
                for (m2 = nextk = k2 > stride ? k2 - stride : 0; m2 < k2; ++m2) {
                    nextTab[m2] = rev;
                }
                for (m2 = n2 + nextk; m2 < n2 + k2; ++m2) {
                    nextTab[m2] = rev;
                }
                k2 = nextk;
                U.putOrderedInt(this, TRANSFERORIGIN, k2);
            }
        }
        int nextn = nextTab.length;
        ForwardingNode<K, V> fwd = new ForwardingNode<K, V>(nextTab);
        boolean advance = true;
        boolean finishing = false;
        int i2 = 0;
        int bound = 0;
        while (true) {
            if (advance) {
                if (--i2 >= bound || finishing) {
                    advance = false;
                    continue;
                }
                int nextIndex = this.transferIndex;
                if (nextIndex <= this.transferOrigin) {
                    i2 = -1;
                    advance = false;
                    continue;
                }
                int nextBound = nextIndex > stride ? nextIndex - stride : 0;
                if (!U.compareAndSwapInt(this, TRANSFERINDEX, nextIndex, nextBound)) continue;
                bound = nextBound;
                i2 = nextIndex - 1;
                advance = false;
                continue;
            }
            if (i2 < 0 || i2 >= n2 || i2 + n2 >= nextn) {
                int sc;
                if (finishing) {
                    this.nextTable = null;
                    this.table = nextTab;
                    this.sizeCtl = (n2 << 1) - (n2 >>> 1);
                    return;
                }
                do {
                    sc = this.sizeCtl;
                } while (!U.compareAndSwapInt(this, SIZECTL, sc, ++sc));
                if (sc != -1) {
                    return;
                }
                advance = true;
                finishing = true;
                i2 = n2;
                continue;
            }
            TreeBin f2 = ConcurrentHashMapV8.tabAt(tab, i2);
            if (f2 == null) {
                if (!ConcurrentHashMapV8.casTabAt(tab, i2, null, fwd)) continue;
                ConcurrentHashMapV8.setTabAt(nextTab, i2, null);
                ConcurrentHashMapV8.setTabAt(nextTab, i2 + n2, null);
                advance = true;
                continue;
            }
            int fh = f2.hash;
            if (fh == -1) {
                advance = true;
                continue;
            }
            TreeBin treeBin = f2;
            synchronized (treeBin) {
                if (ConcurrentHashMapV8.tabAt(tab, i2) == f2) {
                    Node hn;
                    Node ln;
                    if (fh >= 0) {
                        int runBit = fh & n2;
                        TreeBin lastRun = f2;
                        Node p2 = f2.next;
                        while (p2 != null) {
                            int b2 = p2.hash & n2;
                            if (b2 != runBit) {
                                runBit = b2;
                                lastRun = p2;
                            }
                            p2 = p2.next;
                        }
                        if (runBit == 0) {
                            ln = lastRun;
                            hn = null;
                        } else {
                            hn = lastRun;
                            ln = null;
                        }
                        p2 = f2;
                        while (p2 != lastRun) {
                            int ph = p2.hash;
                            Object pk = p2.key;
                            Object pv = p2.val;
                            if ((ph & n2) == 0) {
                                ln = new Node(ph, pk, pv, ln);
                            } else {
                                hn = new Node(ph, pk, pv, hn);
                            }
                            p2 = p2.next;
                        }
                        ConcurrentHashMapV8.setTabAt(nextTab, i2, ln);
                        ConcurrentHashMapV8.setTabAt(nextTab, i2 + n2, hn);
                        ConcurrentHashMapV8.setTabAt(tab, i2, fwd);
                        advance = true;
                    } else if (f2 instanceof TreeBin) {
                        TreeBin t2 = f2;
                        TreeNode lo = null;
                        TreeNode loTail = null;
                        TreeNode hi = null;
                        TreeNode hiTail = null;
                        int lc = 0;
                        int hc = 0;
                        Node e2 = t2.first;
                        while (e2 != null) {
                            int h2 = e2.hash;
                            TreeNode p3 = new TreeNode(h2, e2.key, e2.val, null, null);
                            if ((h2 & n2) == 0) {
                                p3.prev = loTail;
                                if (p3.prev == null) {
                                    lo = p3;
                                } else {
                                    loTail.next = p3;
                                }
                                loTail = p3;
                                ++lc;
                            } else {
                                p3.prev = hiTail;
                                if (p3.prev == null) {
                                    hi = p3;
                                } else {
                                    hiTail.next = p3;
                                }
                                hiTail = p3;
                                ++hc;
                            }
                            e2 = e2.next;
                        }
                        TreeBin treeBin2 = lc <= 6 ? ConcurrentHashMapV8.untreeify(lo) : (ln = hc != 0 ? new TreeBin(lo) : t2);
                        hn = hc <= 6 ? ConcurrentHashMapV8.untreeify(hi) : (lc != 0 ? new TreeBin(hi) : t2);
                        ConcurrentHashMapV8.setTabAt(nextTab, i2, ln);
                        ConcurrentHashMapV8.setTabAt(nextTab, i2 + n2, hn);
                        ConcurrentHashMapV8.setTabAt(tab, i2, fwd);
                        advance = true;
                    }
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final void treeifyBin(Node<K, V>[] tab, int index) {
        if (tab != null) {
            int n2 = tab.length;
            if (n2 < 64) {
                int sc;
                if (tab == this.table && (sc = this.sizeCtl) >= 0 && U.compareAndSwapInt(this, SIZECTL, sc, -2)) {
                    this.transfer(tab, null);
                }
            } else {
                Node<K, V> b2 = ConcurrentHashMapV8.tabAt(tab, index);
                if (b2 != null && b2.hash >= 0) {
                    Node<K, V> node = b2;
                    synchronized (node) {
                        if (ConcurrentHashMapV8.tabAt(tab, index) == b2) {
                            TreeNode hd = null;
                            TreeNode tl = null;
                            Node<K, V> e2 = b2;
                            while (e2 != null) {
                                TreeNode p2 = new TreeNode(e2.hash, e2.key, e2.val, null, null);
                                p2.prev = tl;
                                if (p2.prev == null) {
                                    hd = p2;
                                } else {
                                    tl.next = p2;
                                }
                                tl = p2;
                                e2 = e2.next;
                            }
                            ConcurrentHashMapV8.setTabAt(tab, index, new TreeBin(hd));
                        }
                    }
                }
            }
        }
    }

    static <K, V> Node<K, V> untreeify(Node<K, V> b2) {
        Node hd = null;
        Node tl = null;
        Node<K, V> q2 = b2;
        while (q2 != null) {
            Node p2 = new Node(q2.hash, q2.key, q2.val, null);
            if (tl == null) {
                hd = p2;
            } else {
                tl.next = p2;
            }
            tl = p2;
            q2 = q2.next;
        }
        return hd;
    }

    final int batchFor(long b2) {
        long n2;
        if (b2 == Long.MAX_VALUE || (n2 = this.sumCount()) <= 1L || n2 < b2) {
            return 0;
        }
        int sp = ForkJoinPool.getCommonPoolParallelism() << 2;
        return b2 <= 0L || (n2 /= b2) >= (long)sp ? sp : (int)n2;
    }

    public void forEach(long parallelismThreshold, BiAction<? super K, ? super V> action) {
        if (action == null) {
            throw new NullPointerException();
        }
        new ForEachMappingTask<K, V>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, action).invoke();
    }

    public <U> void forEach(long parallelismThreshold, BiFun<? super K, ? super V, ? extends U> transformer, Action<? super U> action) {
        if (transformer == null || action == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedMappingTask<K, V, U>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, transformer, action).invoke();
    }

    public <U> U search(long parallelismThreshold, BiFun<? super K, ? super V, ? extends U> searchFunction) {
        if (searchFunction == null) {
            throw new NullPointerException();
        }
        return (U)new SearchMappingsTask<K, V, U>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, searchFunction, new AtomicReference()).invoke();
    }

    public <U> U reduce(long parallelismThreshold, BiFun<? super K, ? super V, ? extends U> transformer, BiFun<? super U, ? super U, ? extends U> reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (U)new MapReduceMappingsTask<K, V, U>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, reducer).invoke();
    }

    public double reduceToDouble(long parallelismThreshold, ObjectByObjectToDouble<? super K, ? super V> transformer, double basis, DoubleByDoubleToDouble reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (Double)new MapReduceMappingsToDoubleTask<K, V>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke();
    }

    public long reduceToLong(long parallelismThreshold, ObjectByObjectToLong<? super K, ? super V> transformer, long basis, LongByLongToLong reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (Long)new MapReduceMappingsToLongTask<K, V>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke();
    }

    public int reduceToInt(long parallelismThreshold, ObjectByObjectToInt<? super K, ? super V> transformer, int basis, IntByIntToInt reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (Integer)new MapReduceMappingsToIntTask<K, V>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke();
    }

    public void forEachKey(long parallelismThreshold, Action<? super K> action) {
        if (action == null) {
            throw new NullPointerException();
        }
        new ForEachKeyTask<K, V>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, action).invoke();
    }

    public <U> void forEachKey(long parallelismThreshold, Fun<? super K, ? extends U> transformer, Action<? super U> action) {
        if (transformer == null || action == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedKeyTask<K, V, U>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, transformer, action).invoke();
    }

    public <U> U searchKeys(long parallelismThreshold, Fun<? super K, ? extends U> searchFunction) {
        if (searchFunction == null) {
            throw new NullPointerException();
        }
        return (U)new SearchKeysTask<K, V, U>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, searchFunction, new AtomicReference()).invoke();
    }

    public K reduceKeys(long parallelismThreshold, BiFun<? super K, ? super K, ? extends K> reducer) {
        if (reducer == null) {
            throw new NullPointerException();
        }
        return (K)new ReduceKeysTask<K, V>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, null, reducer).invoke();
    }

    public <U> U reduceKeys(long parallelismThreshold, Fun<? super K, ? extends U> transformer, BiFun<? super U, ? super U, ? extends U> reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (U)new MapReduceKeysTask<K, V, U>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, reducer).invoke();
    }

    public double reduceKeysToDouble(long parallelismThreshold, ObjectToDouble<? super K> transformer, double basis, DoubleByDoubleToDouble reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (Double)new MapReduceKeysToDoubleTask<K, V>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke();
    }

    public long reduceKeysToLong(long parallelismThreshold, ObjectToLong<? super K> transformer, long basis, LongByLongToLong reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (Long)new MapReduceKeysToLongTask<K, V>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke();
    }

    public int reduceKeysToInt(long parallelismThreshold, ObjectToInt<? super K> transformer, int basis, IntByIntToInt reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (Integer)new MapReduceKeysToIntTask<K, V>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke();
    }

    public void forEachValue(long parallelismThreshold, Action<? super V> action) {
        if (action == null) {
            throw new NullPointerException();
        }
        new ForEachValueTask<K, V>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, action).invoke();
    }

    public <U> void forEachValue(long parallelismThreshold, Fun<? super V, ? extends U> transformer, Action<? super U> action) {
        if (transformer == null || action == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedValueTask<K, V, U>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, transformer, action).invoke();
    }

    public <U> U searchValues(long parallelismThreshold, Fun<? super V, ? extends U> searchFunction) {
        if (searchFunction == null) {
            throw new NullPointerException();
        }
        return (U)new SearchValuesTask<K, V, U>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, searchFunction, new AtomicReference()).invoke();
    }

    public V reduceValues(long parallelismThreshold, BiFun<? super V, ? super V, ? extends V> reducer) {
        if (reducer == null) {
            throw new NullPointerException();
        }
        return new ReduceValuesTask<K, V>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, null, reducer).invoke();
    }

    public <U> U reduceValues(long parallelismThreshold, Fun<? super V, ? extends U> transformer, BiFun<? super U, ? super U, ? extends U> reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (U)new MapReduceValuesTask<K, V, U>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, reducer).invoke();
    }

    public double reduceValuesToDouble(long parallelismThreshold, ObjectToDouble<? super V> transformer, double basis, DoubleByDoubleToDouble reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (Double)new MapReduceValuesToDoubleTask<K, V>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke();
    }

    public long reduceValuesToLong(long parallelismThreshold, ObjectToLong<? super V> transformer, long basis, LongByLongToLong reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (Long)new MapReduceValuesToLongTask<K, V>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke();
    }

    public int reduceValuesToInt(long parallelismThreshold, ObjectToInt<? super V> transformer, int basis, IntByIntToInt reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (Integer)new MapReduceValuesToIntTask<K, V>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke();
    }

    public void forEachEntry(long parallelismThreshold, Action<? super Map.Entry<K, V>> action) {
        if (action == null) {
            throw new NullPointerException();
        }
        new ForEachEntryTask<K, V>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, action).invoke();
    }

    public <U> void forEachEntry(long parallelismThreshold, Fun<Map.Entry<K, V>, ? extends U> transformer, Action<? super U> action) {
        if (transformer == null || action == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedEntryTask<K, V, U>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, transformer, action).invoke();
    }

    public <U> U searchEntries(long parallelismThreshold, Fun<Map.Entry<K, V>, ? extends U> searchFunction) {
        if (searchFunction == null) {
            throw new NullPointerException();
        }
        return (U)new SearchEntriesTask<K, V, U>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, searchFunction, new AtomicReference()).invoke();
    }

    public Map.Entry<K, V> reduceEntries(long parallelismThreshold, BiFun<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer) {
        if (reducer == null) {
            throw new NullPointerException();
        }
        return (Map.Entry)new ReduceEntriesTask<K, V>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, null, reducer).invoke();
    }

    public <U> U reduceEntries(long parallelismThreshold, Fun<Map.Entry<K, V>, ? extends U> transformer, BiFun<? super U, ? super U, ? extends U> reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (U)new MapReduceEntriesTask<K, V, U>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, reducer).invoke();
    }

    public double reduceEntriesToDouble(long parallelismThreshold, ObjectToDouble<Map.Entry<K, V>> transformer, double basis, DoubleByDoubleToDouble reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (Double)new MapReduceEntriesToDoubleTask<K, V>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke();
    }

    public long reduceEntriesToLong(long parallelismThreshold, ObjectToLong<Map.Entry<K, V>> transformer, long basis, LongByLongToLong reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (Long)new MapReduceEntriesToLongTask<K, V>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke();
    }

    public int reduceEntriesToInt(long parallelismThreshold, ObjectToInt<Map.Entry<K, V>> transformer, int basis, IntByIntToInt reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return (Integer)new MapReduceEntriesToIntTask<K, V>(null, this.batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke();
    }

    final long sumCount() {
        CounterCell[] as = this.counterCells;
        long sum = this.baseCount;
        if (as != null) {
            for (int i2 = 0; i2 < as.length; ++i2) {
                CounterCell a2 = as[i2];
                if (a2 == null) continue;
                sum += a2.value;
            }
        }
        return sum;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final void fullAddCount(InternalThreadLocalMap threadLocals, long x2, IntegerHolder hc, boolean wasUncontended) {
        int h2;
        if (hc == null) {
            hc = new IntegerHolder();
            int s2 = counterHashCodeGenerator.addAndGet(1640531527);
            hc.value = s2 == 0 ? 1 : s2;
            h2 = hc.value;
            threadLocals.setCounterHashCode(hc);
        } else {
            h2 = hc.value;
        }
        boolean collide = false;
        while (true) {
            long v2;
            int n2;
            CounterCell[] as = this.counterCells;
            if (this.counterCells != null && (n2 = as.length) > 0) {
                CounterCell a2 = as[n2 - 1 & h2];
                if (a2 == null) {
                    if (this.cellsBusy == 0) {
                        CounterCell r2 = new CounterCell(x2);
                        if (this.cellsBusy == 0 && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                            boolean created = false;
                            try {
                                int j2;
                                int m2;
                                CounterCell[] rs = this.counterCells;
                                if (this.counterCells != null && (m2 = rs.length) > 0 && rs[j2 = m2 - 1 & h2] == null) {
                                    rs[j2] = r2;
                                    created = true;
                                }
                            }
                            finally {
                                this.cellsBusy = 0;
                            }
                            if (!created) continue;
                            break;
                        }
                    }
                    collide = false;
                } else if (!wasUncontended) {
                    wasUncontended = true;
                } else {
                    v2 = a2.value;
                    if (U.compareAndSwapLong(a2, CELLVALUE, v2, v2 + x2)) break;
                    if (this.counterCells != as || n2 >= NCPU) {
                        collide = false;
                    } else if (!collide) {
                        collide = true;
                    } else if (this.cellsBusy == 0 && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                        try {
                            if (this.counterCells == as) {
                                CounterCell[] rs = new CounterCell[n2 << 1];
                                for (int i2 = 0; i2 < n2; ++i2) {
                                    rs[i2] = as[i2];
                                }
                                this.counterCells = rs;
                            }
                        }
                        finally {
                            this.cellsBusy = 0;
                        }
                        collide = false;
                        continue;
                    }
                }
                h2 ^= h2 << 13;
                h2 ^= h2 >>> 17;
                h2 ^= h2 << 5;
                continue;
            }
            if (this.cellsBusy == 0 && this.counterCells == as && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                boolean init = false;
                try {
                    if (this.counterCells == as) {
                        CounterCell[] rs = new CounterCell[2];
                        rs[h2 & 1] = new CounterCell(x2);
                        this.counterCells = rs;
                        init = true;
                    }
                }
                finally {
                    this.cellsBusy = 0;
                }
                if (!init) continue;
                break;
            }
            v2 = this.baseCount;
            if (U.compareAndSwapLong(this, BASECOUNT, v2, v2 + x2)) break;
        }
        hc.value = h2;
    }

    private static Unsafe getUnsafe() {
        try {
            return Unsafe.getUnsafe();
        }
        catch (SecurityException tryReflectionInstead) {
            try {
                return AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>(){

                    @Override
                    public Unsafe run() throws Exception {
                        Class<Unsafe> k2 = Unsafe.class;
                        for (Field f2 : k2.getDeclaredFields()) {
                            f2.setAccessible(true);
                            Object x2 = f2.get(null);
                            if (!k2.isInstance(x2)) continue;
                            return (Unsafe)k2.cast(x2);
                        }
                        throw new NoSuchFieldError("the Unsafe");
                    }
                });
            }
            catch (PrivilegedActionException e2) {
                throw new RuntimeException("Could not initialize intrinsics", e2.getCause());
            }
        }
    }

    static {
        try {
            U = ConcurrentHashMapV8.getUnsafe();
            Class<ConcurrentHashMapV8> k2 = ConcurrentHashMapV8.class;
            SIZECTL = U.objectFieldOffset(k2.getDeclaredField("sizeCtl"));
            TRANSFERINDEX = U.objectFieldOffset(k2.getDeclaredField("transferIndex"));
            TRANSFERORIGIN = U.objectFieldOffset(k2.getDeclaredField("transferOrigin"));
            BASECOUNT = U.objectFieldOffset(k2.getDeclaredField("baseCount"));
            CELLSBUSY = U.objectFieldOffset(k2.getDeclaredField("cellsBusy"));
            Class<CounterCell> ck = CounterCell.class;
            CELLVALUE = U.objectFieldOffset(ck.getDeclaredField("value"));
            Class<Node[]> ak = Node[].class;
            ABASE = U.arrayBaseOffset(ak);
            int scale = U.arrayIndexScale(ak);
            if ((scale & scale - 1) != 0) {
                throw new Error("data type scale not a power of two");
            }
            ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
        }
        catch (Exception e2) {
            throw new Error(e2);
        }
    }

    static final class CounterHashCode {
        int code;

        CounterHashCode() {
        }
    }

    static final class CounterCell {
        volatile long p0;
        volatile long p1;
        volatile long p2;
        volatile long p3;
        volatile long p4;
        volatile long p5;
        volatile long p6;
        volatile long value;
        volatile long q0;
        volatile long q1;
        volatile long q2;
        volatile long q3;
        volatile long q4;
        volatile long q5;
        volatile long q6;

        CounterCell(long x2) {
            this.value = x2;
        }
    }

    static final class MapReduceMappingsToIntTask<K, V>
    extends BulkTask<K, V, Integer> {
        final ObjectByObjectToInt<? super K, ? super V> transformer;
        final IntByIntToInt reducer;
        final int basis;
        int result;
        MapReduceMappingsToIntTask<K, V> rights;
        MapReduceMappingsToIntTask<K, V> nextRight;

        MapReduceMappingsToIntTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, MapReduceMappingsToIntTask<K, V> nextRight, ObjectByObjectToInt<? super K, ? super V> transformer, int basis, IntByIntToInt reducer) {
            super(p2, b2, i2, f2, t2);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override
        public final Integer getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            IntByIntToInt reducer;
            ObjectByObjectToInt<K, V> transformer = this.transformer;
            if (transformer != null && (reducer = this.reducer) != null) {
                Node p2;
                int f2;
                int h2;
                int r2 = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    this.rights = new MapReduceMappingsToIntTask<K, V>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, this.rights, transformer, r2, reducer);
                    this.rights.fork();
                }
                while ((p2 = this.advance()) != null) {
                    r2 = reducer.apply(r2, transformer.apply(p2.key, p2.val));
                }
                this.result = r2;
                for (CountedCompleter<?> c2 = this.firstComplete(); c2 != null; c2 = c2.nextComplete()) {
                    MapReduceMappingsToIntTask t2 = (MapReduceMappingsToIntTask)c2;
                    MapReduceMappingsToIntTask<K, V> s2 = t2.rights;
                    while (s2 != null) {
                        t2.result = reducer.apply(t2.result, s2.result);
                        s2 = t2.rights = s2.nextRight;
                    }
                }
            }
        }
    }

    static final class MapReduceEntriesToIntTask<K, V>
    extends BulkTask<K, V, Integer> {
        final ObjectToInt<Map.Entry<K, V>> transformer;
        final IntByIntToInt reducer;
        final int basis;
        int result;
        MapReduceEntriesToIntTask<K, V> rights;
        MapReduceEntriesToIntTask<K, V> nextRight;

        MapReduceEntriesToIntTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, MapReduceEntriesToIntTask<K, V> nextRight, ObjectToInt<Map.Entry<K, V>> transformer, int basis, IntByIntToInt reducer) {
            super(p2, b2, i2, f2, t2);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override
        public final Integer getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            IntByIntToInt reducer;
            ObjectToInt<Map.Entry<K, V>> transformer = this.transformer;
            if (transformer != null && (reducer = this.reducer) != null) {
                Node p2;
                int f2;
                int h2;
                int r2 = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    this.rights = new MapReduceEntriesToIntTask<K, V>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, this.rights, transformer, r2, reducer);
                    this.rights.fork();
                }
                while ((p2 = this.advance()) != null) {
                    r2 = reducer.apply(r2, transformer.apply(p2));
                }
                this.result = r2;
                for (CountedCompleter<?> c2 = this.firstComplete(); c2 != null; c2 = c2.nextComplete()) {
                    MapReduceEntriesToIntTask t2 = (MapReduceEntriesToIntTask)c2;
                    MapReduceEntriesToIntTask<K, V> s2 = t2.rights;
                    while (s2 != null) {
                        t2.result = reducer.apply(t2.result, s2.result);
                        s2 = t2.rights = s2.nextRight;
                    }
                }
            }
        }
    }

    static final class MapReduceValuesToIntTask<K, V>
    extends BulkTask<K, V, Integer> {
        final ObjectToInt<? super V> transformer;
        final IntByIntToInt reducer;
        final int basis;
        int result;
        MapReduceValuesToIntTask<K, V> rights;
        MapReduceValuesToIntTask<K, V> nextRight;

        MapReduceValuesToIntTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, MapReduceValuesToIntTask<K, V> nextRight, ObjectToInt<? super V> transformer, int basis, IntByIntToInt reducer) {
            super(p2, b2, i2, f2, t2);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override
        public final Integer getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            IntByIntToInt reducer;
            ObjectToInt<V> transformer = this.transformer;
            if (transformer != null && (reducer = this.reducer) != null) {
                Node p2;
                int f2;
                int h2;
                int r2 = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    this.rights = new MapReduceValuesToIntTask<K, V>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, this.rights, transformer, r2, reducer);
                    this.rights.fork();
                }
                while ((p2 = this.advance()) != null) {
                    r2 = reducer.apply(r2, transformer.apply(p2.val));
                }
                this.result = r2;
                for (CountedCompleter<?> c2 = this.firstComplete(); c2 != null; c2 = c2.nextComplete()) {
                    MapReduceValuesToIntTask t2 = (MapReduceValuesToIntTask)c2;
                    MapReduceValuesToIntTask<K, V> s2 = t2.rights;
                    while (s2 != null) {
                        t2.result = reducer.apply(t2.result, s2.result);
                        s2 = t2.rights = s2.nextRight;
                    }
                }
            }
        }
    }

    static final class MapReduceKeysToIntTask<K, V>
    extends BulkTask<K, V, Integer> {
        final ObjectToInt<? super K> transformer;
        final IntByIntToInt reducer;
        final int basis;
        int result;
        MapReduceKeysToIntTask<K, V> rights;
        MapReduceKeysToIntTask<K, V> nextRight;

        MapReduceKeysToIntTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, MapReduceKeysToIntTask<K, V> nextRight, ObjectToInt<? super K> transformer, int basis, IntByIntToInt reducer) {
            super(p2, b2, i2, f2, t2);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override
        public final Integer getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            IntByIntToInt reducer;
            ObjectToInt<K> transformer = this.transformer;
            if (transformer != null && (reducer = this.reducer) != null) {
                Node p2;
                int f2;
                int h2;
                int r2 = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    this.rights = new MapReduceKeysToIntTask<K, V>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, this.rights, transformer, r2, reducer);
                    this.rights.fork();
                }
                while ((p2 = this.advance()) != null) {
                    r2 = reducer.apply(r2, transformer.apply(p2.key));
                }
                this.result = r2;
                for (CountedCompleter<?> c2 = this.firstComplete(); c2 != null; c2 = c2.nextComplete()) {
                    MapReduceKeysToIntTask t2 = (MapReduceKeysToIntTask)c2;
                    MapReduceKeysToIntTask<K, V> s2 = t2.rights;
                    while (s2 != null) {
                        t2.result = reducer.apply(t2.result, s2.result);
                        s2 = t2.rights = s2.nextRight;
                    }
                }
            }
        }
    }

    static final class MapReduceMappingsToLongTask<K, V>
    extends BulkTask<K, V, Long> {
        final ObjectByObjectToLong<? super K, ? super V> transformer;
        final LongByLongToLong reducer;
        final long basis;
        long result;
        MapReduceMappingsToLongTask<K, V> rights;
        MapReduceMappingsToLongTask<K, V> nextRight;

        MapReduceMappingsToLongTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, MapReduceMappingsToLongTask<K, V> nextRight, ObjectByObjectToLong<? super K, ? super V> transformer, long basis, LongByLongToLong reducer) {
            super(p2, b2, i2, f2, t2);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override
        public final Long getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            LongByLongToLong reducer;
            ObjectByObjectToLong<K, V> transformer = this.transformer;
            if (transformer != null && (reducer = this.reducer) != null) {
                Node p2;
                int f2;
                int h2;
                long r2 = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    this.rights = new MapReduceMappingsToLongTask<K, V>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, this.rights, transformer, r2, reducer);
                    this.rights.fork();
                }
                while ((p2 = this.advance()) != null) {
                    r2 = reducer.apply(r2, transformer.apply(p2.key, p2.val));
                }
                this.result = r2;
                for (CountedCompleter<?> c2 = this.firstComplete(); c2 != null; c2 = c2.nextComplete()) {
                    MapReduceMappingsToLongTask t2 = (MapReduceMappingsToLongTask)c2;
                    MapReduceMappingsToLongTask<K, V> s2 = t2.rights;
                    while (s2 != null) {
                        t2.result = reducer.apply(t2.result, s2.result);
                        s2 = t2.rights = s2.nextRight;
                    }
                }
            }
        }
    }

    static final class MapReduceEntriesToLongTask<K, V>
    extends BulkTask<K, V, Long> {
        final ObjectToLong<Map.Entry<K, V>> transformer;
        final LongByLongToLong reducer;
        final long basis;
        long result;
        MapReduceEntriesToLongTask<K, V> rights;
        MapReduceEntriesToLongTask<K, V> nextRight;

        MapReduceEntriesToLongTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, MapReduceEntriesToLongTask<K, V> nextRight, ObjectToLong<Map.Entry<K, V>> transformer, long basis, LongByLongToLong reducer) {
            super(p2, b2, i2, f2, t2);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override
        public final Long getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            LongByLongToLong reducer;
            ObjectToLong<Map.Entry<K, V>> transformer = this.transformer;
            if (transformer != null && (reducer = this.reducer) != null) {
                Node p2;
                int f2;
                int h2;
                long r2 = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    this.rights = new MapReduceEntriesToLongTask<K, V>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, this.rights, transformer, r2, reducer);
                    this.rights.fork();
                }
                while ((p2 = this.advance()) != null) {
                    r2 = reducer.apply(r2, transformer.apply(p2));
                }
                this.result = r2;
                for (CountedCompleter<?> c2 = this.firstComplete(); c2 != null; c2 = c2.nextComplete()) {
                    MapReduceEntriesToLongTask t2 = (MapReduceEntriesToLongTask)c2;
                    MapReduceEntriesToLongTask<K, V> s2 = t2.rights;
                    while (s2 != null) {
                        t2.result = reducer.apply(t2.result, s2.result);
                        s2 = t2.rights = s2.nextRight;
                    }
                }
            }
        }
    }

    static final class MapReduceValuesToLongTask<K, V>
    extends BulkTask<K, V, Long> {
        final ObjectToLong<? super V> transformer;
        final LongByLongToLong reducer;
        final long basis;
        long result;
        MapReduceValuesToLongTask<K, V> rights;
        MapReduceValuesToLongTask<K, V> nextRight;

        MapReduceValuesToLongTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, MapReduceValuesToLongTask<K, V> nextRight, ObjectToLong<? super V> transformer, long basis, LongByLongToLong reducer) {
            super(p2, b2, i2, f2, t2);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override
        public final Long getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            LongByLongToLong reducer;
            ObjectToLong<V> transformer = this.transformer;
            if (transformer != null && (reducer = this.reducer) != null) {
                Node p2;
                int f2;
                int h2;
                long r2 = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    this.rights = new MapReduceValuesToLongTask<K, V>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, this.rights, transformer, r2, reducer);
                    this.rights.fork();
                }
                while ((p2 = this.advance()) != null) {
                    r2 = reducer.apply(r2, transformer.apply(p2.val));
                }
                this.result = r2;
                for (CountedCompleter<?> c2 = this.firstComplete(); c2 != null; c2 = c2.nextComplete()) {
                    MapReduceValuesToLongTask t2 = (MapReduceValuesToLongTask)c2;
                    MapReduceValuesToLongTask<K, V> s2 = t2.rights;
                    while (s2 != null) {
                        t2.result = reducer.apply(t2.result, s2.result);
                        s2 = t2.rights = s2.nextRight;
                    }
                }
            }
        }
    }

    static final class MapReduceKeysToLongTask<K, V>
    extends BulkTask<K, V, Long> {
        final ObjectToLong<? super K> transformer;
        final LongByLongToLong reducer;
        final long basis;
        long result;
        MapReduceKeysToLongTask<K, V> rights;
        MapReduceKeysToLongTask<K, V> nextRight;

        MapReduceKeysToLongTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, MapReduceKeysToLongTask<K, V> nextRight, ObjectToLong<? super K> transformer, long basis, LongByLongToLong reducer) {
            super(p2, b2, i2, f2, t2);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override
        public final Long getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            LongByLongToLong reducer;
            ObjectToLong<K> transformer = this.transformer;
            if (transformer != null && (reducer = this.reducer) != null) {
                Node p2;
                int f2;
                int h2;
                long r2 = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    this.rights = new MapReduceKeysToLongTask<K, V>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, this.rights, transformer, r2, reducer);
                    this.rights.fork();
                }
                while ((p2 = this.advance()) != null) {
                    r2 = reducer.apply(r2, transformer.apply(p2.key));
                }
                this.result = r2;
                for (CountedCompleter<?> c2 = this.firstComplete(); c2 != null; c2 = c2.nextComplete()) {
                    MapReduceKeysToLongTask t2 = (MapReduceKeysToLongTask)c2;
                    MapReduceKeysToLongTask<K, V> s2 = t2.rights;
                    while (s2 != null) {
                        t2.result = reducer.apply(t2.result, s2.result);
                        s2 = t2.rights = s2.nextRight;
                    }
                }
            }
        }
    }

    static final class MapReduceMappingsToDoubleTask<K, V>
    extends BulkTask<K, V, Double> {
        final ObjectByObjectToDouble<? super K, ? super V> transformer;
        final DoubleByDoubleToDouble reducer;
        final double basis;
        double result;
        MapReduceMappingsToDoubleTask<K, V> rights;
        MapReduceMappingsToDoubleTask<K, V> nextRight;

        MapReduceMappingsToDoubleTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, MapReduceMappingsToDoubleTask<K, V> nextRight, ObjectByObjectToDouble<? super K, ? super V> transformer, double basis, DoubleByDoubleToDouble reducer) {
            super(p2, b2, i2, f2, t2);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override
        public final Double getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            DoubleByDoubleToDouble reducer;
            ObjectByObjectToDouble<K, V> transformer = this.transformer;
            if (transformer != null && (reducer = this.reducer) != null) {
                Node p2;
                int f2;
                int h2;
                double r2 = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    this.rights = new MapReduceMappingsToDoubleTask<K, V>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, this.rights, transformer, r2, reducer);
                    this.rights.fork();
                }
                while ((p2 = this.advance()) != null) {
                    r2 = reducer.apply(r2, transformer.apply(p2.key, p2.val));
                }
                this.result = r2;
                for (CountedCompleter<?> c2 = this.firstComplete(); c2 != null; c2 = c2.nextComplete()) {
                    MapReduceMappingsToDoubleTask t2 = (MapReduceMappingsToDoubleTask)c2;
                    MapReduceMappingsToDoubleTask<K, V> s2 = t2.rights;
                    while (s2 != null) {
                        t2.result = reducer.apply(t2.result, s2.result);
                        s2 = t2.rights = s2.nextRight;
                    }
                }
            }
        }
    }

    static final class MapReduceEntriesToDoubleTask<K, V>
    extends BulkTask<K, V, Double> {
        final ObjectToDouble<Map.Entry<K, V>> transformer;
        final DoubleByDoubleToDouble reducer;
        final double basis;
        double result;
        MapReduceEntriesToDoubleTask<K, V> rights;
        MapReduceEntriesToDoubleTask<K, V> nextRight;

        MapReduceEntriesToDoubleTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, MapReduceEntriesToDoubleTask<K, V> nextRight, ObjectToDouble<Map.Entry<K, V>> transformer, double basis, DoubleByDoubleToDouble reducer) {
            super(p2, b2, i2, f2, t2);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override
        public final Double getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            DoubleByDoubleToDouble reducer;
            ObjectToDouble<Map.Entry<K, V>> transformer = this.transformer;
            if (transformer != null && (reducer = this.reducer) != null) {
                Node p2;
                int f2;
                int h2;
                double r2 = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    this.rights = new MapReduceEntriesToDoubleTask<K, V>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, this.rights, transformer, r2, reducer);
                    this.rights.fork();
                }
                while ((p2 = this.advance()) != null) {
                    r2 = reducer.apply(r2, transformer.apply(p2));
                }
                this.result = r2;
                for (CountedCompleter<?> c2 = this.firstComplete(); c2 != null; c2 = c2.nextComplete()) {
                    MapReduceEntriesToDoubleTask t2 = (MapReduceEntriesToDoubleTask)c2;
                    MapReduceEntriesToDoubleTask<K, V> s2 = t2.rights;
                    while (s2 != null) {
                        t2.result = reducer.apply(t2.result, s2.result);
                        s2 = t2.rights = s2.nextRight;
                    }
                }
            }
        }
    }

    static final class MapReduceValuesToDoubleTask<K, V>
    extends BulkTask<K, V, Double> {
        final ObjectToDouble<? super V> transformer;
        final DoubleByDoubleToDouble reducer;
        final double basis;
        double result;
        MapReduceValuesToDoubleTask<K, V> rights;
        MapReduceValuesToDoubleTask<K, V> nextRight;

        MapReduceValuesToDoubleTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, MapReduceValuesToDoubleTask<K, V> nextRight, ObjectToDouble<? super V> transformer, double basis, DoubleByDoubleToDouble reducer) {
            super(p2, b2, i2, f2, t2);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override
        public final Double getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            DoubleByDoubleToDouble reducer;
            ObjectToDouble<V> transformer = this.transformer;
            if (transformer != null && (reducer = this.reducer) != null) {
                Node p2;
                int f2;
                int h2;
                double r2 = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    this.rights = new MapReduceValuesToDoubleTask<K, V>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, this.rights, transformer, r2, reducer);
                    this.rights.fork();
                }
                while ((p2 = this.advance()) != null) {
                    r2 = reducer.apply(r2, transformer.apply(p2.val));
                }
                this.result = r2;
                for (CountedCompleter<?> c2 = this.firstComplete(); c2 != null; c2 = c2.nextComplete()) {
                    MapReduceValuesToDoubleTask t2 = (MapReduceValuesToDoubleTask)c2;
                    MapReduceValuesToDoubleTask<K, V> s2 = t2.rights;
                    while (s2 != null) {
                        t2.result = reducer.apply(t2.result, s2.result);
                        s2 = t2.rights = s2.nextRight;
                    }
                }
            }
        }
    }

    static final class MapReduceKeysToDoubleTask<K, V>
    extends BulkTask<K, V, Double> {
        final ObjectToDouble<? super K> transformer;
        final DoubleByDoubleToDouble reducer;
        final double basis;
        double result;
        MapReduceKeysToDoubleTask<K, V> rights;
        MapReduceKeysToDoubleTask<K, V> nextRight;

        MapReduceKeysToDoubleTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, MapReduceKeysToDoubleTask<K, V> nextRight, ObjectToDouble<? super K> transformer, double basis, DoubleByDoubleToDouble reducer) {
            super(p2, b2, i2, f2, t2);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override
        public final Double getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            DoubleByDoubleToDouble reducer;
            ObjectToDouble<K> transformer = this.transformer;
            if (transformer != null && (reducer = this.reducer) != null) {
                Node p2;
                int f2;
                int h2;
                double r2 = this.basis;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    this.rights = new MapReduceKeysToDoubleTask<K, V>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, this.rights, transformer, r2, reducer);
                    this.rights.fork();
                }
                while ((p2 = this.advance()) != null) {
                    r2 = reducer.apply(r2, transformer.apply(p2.key));
                }
                this.result = r2;
                for (CountedCompleter<?> c2 = this.firstComplete(); c2 != null; c2 = c2.nextComplete()) {
                    MapReduceKeysToDoubleTask t2 = (MapReduceKeysToDoubleTask)c2;
                    MapReduceKeysToDoubleTask<K, V> s2 = t2.rights;
                    while (s2 != null) {
                        t2.result = reducer.apply(t2.result, s2.result);
                        s2 = t2.rights = s2.nextRight;
                    }
                }
            }
        }
    }

    static final class MapReduceMappingsTask<K, V, U>
    extends BulkTask<K, V, U> {
        final BiFun<? super K, ? super V, ? extends U> transformer;
        final BiFun<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceMappingsTask<K, V, U> rights;
        MapReduceMappingsTask<K, V, U> nextRight;

        MapReduceMappingsTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, MapReduceMappingsTask<K, V, U> nextRight, BiFun<? super K, ? super V, ? extends U> transformer, BiFun<? super U, ? super U, ? extends U> reducer) {
            super(p2, b2, i2, f2, t2);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.reducer = reducer;
        }

        @Override
        public final U getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            BiFun<U, U, U> reducer;
            BiFun<K, V, U> transformer = this.transformer;
            if (transformer != null && (reducer = this.reducer) != null) {
                Node p2;
                int f2;
                int h2;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    this.rights = new MapReduceMappingsTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, this.rights, transformer, reducer);
                    this.rights.fork();
                }
                Object r2 = null;
                while ((p2 = this.advance()) != null) {
                    U u2 = transformer.apply(p2.key, p2.val);
                    if (u2 == null) continue;
                    r2 = r2 == null ? u2 : reducer.apply(r2, u2);
                }
                this.result = r2;
                for (CountedCompleter<?> c2 = this.firstComplete(); c2 != null; c2 = c2.nextComplete()) {
                    MapReduceMappingsTask t2 = (MapReduceMappingsTask)c2;
                    MapReduceMappingsTask<K, V, U> s2 = t2.rights;
                    while (s2 != null) {
                        U sr = s2.result;
                        if (sr != null) {
                            U tr = t2.result;
                            t2.result = tr == null ? sr : reducer.apply(tr, sr);
                        }
                        s2 = t2.rights = s2.nextRight;
                    }
                }
            }
        }
    }

    static final class MapReduceEntriesTask<K, V, U>
    extends BulkTask<K, V, U> {
        final Fun<Map.Entry<K, V>, ? extends U> transformer;
        final BiFun<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceEntriesTask<K, V, U> rights;
        MapReduceEntriesTask<K, V, U> nextRight;

        MapReduceEntriesTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, MapReduceEntriesTask<K, V, U> nextRight, Fun<Map.Entry<K, V>, ? extends U> transformer, BiFun<? super U, ? super U, ? extends U> reducer) {
            super(p2, b2, i2, f2, t2);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.reducer = reducer;
        }

        @Override
        public final U getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            BiFun<U, U, U> reducer;
            Fun<Map.Entry<K, V>, U> transformer = this.transformer;
            if (transformer != null && (reducer = this.reducer) != null) {
                Node p2;
                int f2;
                int h2;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    this.rights = new MapReduceEntriesTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, this.rights, transformer, reducer);
                    this.rights.fork();
                }
                Object r2 = null;
                while ((p2 = this.advance()) != null) {
                    U u2 = transformer.apply(p2);
                    if (u2 == null) continue;
                    r2 = r2 == null ? u2 : reducer.apply(r2, u2);
                }
                this.result = r2;
                for (CountedCompleter<?> c2 = this.firstComplete(); c2 != null; c2 = c2.nextComplete()) {
                    MapReduceEntriesTask t2 = (MapReduceEntriesTask)c2;
                    MapReduceEntriesTask<K, V, U> s2 = t2.rights;
                    while (s2 != null) {
                        U sr = s2.result;
                        if (sr != null) {
                            U tr = t2.result;
                            t2.result = tr == null ? sr : reducer.apply(tr, sr);
                        }
                        s2 = t2.rights = s2.nextRight;
                    }
                }
            }
        }
    }

    static final class MapReduceValuesTask<K, V, U>
    extends BulkTask<K, V, U> {
        final Fun<? super V, ? extends U> transformer;
        final BiFun<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceValuesTask<K, V, U> rights;
        MapReduceValuesTask<K, V, U> nextRight;

        MapReduceValuesTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, MapReduceValuesTask<K, V, U> nextRight, Fun<? super V, ? extends U> transformer, BiFun<? super U, ? super U, ? extends U> reducer) {
            super(p2, b2, i2, f2, t2);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.reducer = reducer;
        }

        @Override
        public final U getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            BiFun<U, U, U> reducer;
            Fun<V, U> transformer = this.transformer;
            if (transformer != null && (reducer = this.reducer) != null) {
                Node p2;
                int f2;
                int h2;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    this.rights = new MapReduceValuesTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, this.rights, transformer, reducer);
                    this.rights.fork();
                }
                Object r2 = null;
                while ((p2 = this.advance()) != null) {
                    U u2 = transformer.apply(p2.val);
                    if (u2 == null) continue;
                    r2 = r2 == null ? u2 : reducer.apply(r2, u2);
                }
                this.result = r2;
                for (CountedCompleter<?> c2 = this.firstComplete(); c2 != null; c2 = c2.nextComplete()) {
                    MapReduceValuesTask t2 = (MapReduceValuesTask)c2;
                    MapReduceValuesTask<K, V, U> s2 = t2.rights;
                    while (s2 != null) {
                        U sr = s2.result;
                        if (sr != null) {
                            U tr = t2.result;
                            t2.result = tr == null ? sr : reducer.apply(tr, sr);
                        }
                        s2 = t2.rights = s2.nextRight;
                    }
                }
            }
        }
    }

    static final class MapReduceKeysTask<K, V, U>
    extends BulkTask<K, V, U> {
        final Fun<? super K, ? extends U> transformer;
        final BiFun<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceKeysTask<K, V, U> rights;
        MapReduceKeysTask<K, V, U> nextRight;

        MapReduceKeysTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, MapReduceKeysTask<K, V, U> nextRight, Fun<? super K, ? extends U> transformer, BiFun<? super U, ? super U, ? extends U> reducer) {
            super(p2, b2, i2, f2, t2);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.reducer = reducer;
        }

        @Override
        public final U getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            BiFun<U, U, U> reducer;
            Fun<K, U> transformer = this.transformer;
            if (transformer != null && (reducer = this.reducer) != null) {
                Node p2;
                int f2;
                int h2;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    this.rights = new MapReduceKeysTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, this.rights, transformer, reducer);
                    this.rights.fork();
                }
                Object r2 = null;
                while ((p2 = this.advance()) != null) {
                    U u2 = transformer.apply(p2.key);
                    if (u2 == null) continue;
                    r2 = r2 == null ? u2 : reducer.apply(r2, u2);
                }
                this.result = r2;
                for (CountedCompleter<?> c2 = this.firstComplete(); c2 != null; c2 = c2.nextComplete()) {
                    MapReduceKeysTask t2 = (MapReduceKeysTask)c2;
                    MapReduceKeysTask<K, V, U> s2 = t2.rights;
                    while (s2 != null) {
                        U sr = s2.result;
                        if (sr != null) {
                            U tr = t2.result;
                            t2.result = tr == null ? sr : reducer.apply(tr, sr);
                        }
                        s2 = t2.rights = s2.nextRight;
                    }
                }
            }
        }
    }

    static final class ReduceEntriesTask<K, V>
    extends BulkTask<K, V, Map.Entry<K, V>> {
        final BiFun<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer;
        Map.Entry<K, V> result;
        ReduceEntriesTask<K, V> rights;
        ReduceEntriesTask<K, V> nextRight;

        ReduceEntriesTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, ReduceEntriesTask<K, V> nextRight, BiFun<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer) {
            super(p2, b2, i2, f2, t2);
            this.nextRight = nextRight;
            this.reducer = reducer;
        }

        @Override
        public final Map.Entry<K, V> getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            BiFun<Map.Entry<K, V>, Map.Entry<K, V>, Map.Entry<K, V>> reducer = this.reducer;
            if (reducer != null) {
                Node p2;
                int f2;
                int h2;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    this.rights = new ReduceEntriesTask<K, V>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, this.rights, reducer);
                    this.rights.fork();
                }
                Node r2 = null;
                while ((p2 = this.advance()) != null) {
                    r2 = r2 == null ? p2 : reducer.apply(r2, p2);
                }
                this.result = r2;
                for (CountedCompleter<?> c2 = this.firstComplete(); c2 != null; c2 = c2.nextComplete()) {
                    ReduceEntriesTask t2 = (ReduceEntriesTask)c2;
                    ReduceEntriesTask<K, V> s2 = t2.rights;
                    while (s2 != null) {
                        Map.Entry<K, V> sr = s2.result;
                        if (sr != null) {
                            Map.Entry<K, V> tr = t2.result;
                            t2.result = tr == null ? sr : reducer.apply(tr, sr);
                        }
                        s2 = t2.rights = s2.nextRight;
                    }
                }
            }
        }
    }

    static final class ReduceValuesTask<K, V>
    extends BulkTask<K, V, V> {
        final BiFun<? super V, ? super V, ? extends V> reducer;
        V result;
        ReduceValuesTask<K, V> rights;
        ReduceValuesTask<K, V> nextRight;

        ReduceValuesTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, ReduceValuesTask<K, V> nextRight, BiFun<? super V, ? super V, ? extends V> reducer) {
            super(p2, b2, i2, f2, t2);
            this.nextRight = nextRight;
            this.reducer = reducer;
        }

        @Override
        public final V getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            BiFun<V, V, V> reducer = this.reducer;
            if (reducer != null) {
                Node p2;
                int f2;
                int h2;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    this.rights = new ReduceValuesTask<K, V>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, this.rights, reducer);
                    this.rights.fork();
                }
                Object r2 = null;
                while ((p2 = this.advance()) != null) {
                    Object v2 = p2.val;
                    r2 = r2 == null ? v2 : reducer.apply(r2, v2);
                }
                this.result = r2;
                for (CountedCompleter<?> c2 = this.firstComplete(); c2 != null; c2 = c2.nextComplete()) {
                    ReduceValuesTask t2 = (ReduceValuesTask)c2;
                    ReduceValuesTask<K, V> s2 = t2.rights;
                    while (s2 != null) {
                        V sr = s2.result;
                        if (sr != null) {
                            V tr = t2.result;
                            t2.result = tr == null ? sr : reducer.apply(tr, sr);
                        }
                        s2 = t2.rights = s2.nextRight;
                    }
                }
            }
        }
    }

    static final class ReduceKeysTask<K, V>
    extends BulkTask<K, V, K> {
        final BiFun<? super K, ? super K, ? extends K> reducer;
        K result;
        ReduceKeysTask<K, V> rights;
        ReduceKeysTask<K, V> nextRight;

        ReduceKeysTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, ReduceKeysTask<K, V> nextRight, BiFun<? super K, ? super K, ? extends K> reducer) {
            super(p2, b2, i2, f2, t2);
            this.nextRight = nextRight;
            this.reducer = reducer;
        }

        @Override
        public final K getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            BiFun<K, K, K> reducer = this.reducer;
            if (reducer != null) {
                Node p2;
                int f2;
                int h2;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    this.rights = new ReduceKeysTask<K, V>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, this.rights, reducer);
                    this.rights.fork();
                }
                Object r2 = null;
                while ((p2 = this.advance()) != null) {
                    Object u2 = p2.key;
                    r2 = r2 == null ? u2 : (u2 == null ? r2 : reducer.apply(r2, u2));
                }
                this.result = r2;
                for (CountedCompleter<?> c2 = this.firstComplete(); c2 != null; c2 = c2.nextComplete()) {
                    ReduceKeysTask t2 = (ReduceKeysTask)c2;
                    ReduceKeysTask<K, V> s2 = t2.rights;
                    while (s2 != null) {
                        K sr = s2.result;
                        if (sr != null) {
                            K tr = t2.result;
                            t2.result = tr == null ? sr : reducer.apply(tr, sr);
                        }
                        s2 = t2.rights = s2.nextRight;
                    }
                }
            }
        }
    }

    static final class SearchMappingsTask<K, V, U>
    extends BulkTask<K, V, U> {
        final BiFun<? super K, ? super V, ? extends U> searchFunction;
        final AtomicReference<U> result;

        SearchMappingsTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, BiFun<? super K, ? super V, ? extends U> searchFunction, AtomicReference<U> result) {
            super(p2, b2, i2, f2, t2);
            this.searchFunction = searchFunction;
            this.result = result;
        }

        @Override
        public final U getRawResult() {
            return this.result.get();
        }

        @Override
        public final void compute() {
            AtomicReference<U> result;
            BiFun<K, V, U> searchFunction = this.searchFunction;
            if (searchFunction != null && (result = this.result) != null) {
                int f2;
                int h2;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    if (result.get() != null) {
                        return;
                    }
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    new SearchMappingsTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, searchFunction, result).fork();
                }
                while (result.get() == null) {
                    Node p2 = this.advance();
                    if (p2 == null) {
                        this.propagateCompletion();
                        break;
                    }
                    U u2 = searchFunction.apply(p2.key, p2.val);
                    if (u2 == null) continue;
                    if (!result.compareAndSet(null, u2)) break;
                    this.quietlyCompleteRoot();
                    break;
                }
            }
        }
    }

    static final class SearchEntriesTask<K, V, U>
    extends BulkTask<K, V, U> {
        final Fun<Map.Entry<K, V>, ? extends U> searchFunction;
        final AtomicReference<U> result;

        SearchEntriesTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, Fun<Map.Entry<K, V>, ? extends U> searchFunction, AtomicReference<U> result) {
            super(p2, b2, i2, f2, t2);
            this.searchFunction = searchFunction;
            this.result = result;
        }

        @Override
        public final U getRawResult() {
            return this.result.get();
        }

        @Override
        public final void compute() {
            AtomicReference<U> result;
            Fun<Map.Entry<K, V>, U> searchFunction = this.searchFunction;
            if (searchFunction != null && (result = this.result) != null) {
                int f2;
                int h2;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    if (result.get() != null) {
                        return;
                    }
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    new SearchEntriesTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, searchFunction, result).fork();
                }
                while (result.get() == null) {
                    Node p2 = this.advance();
                    if (p2 == null) {
                        this.propagateCompletion();
                        break;
                    }
                    U u2 = searchFunction.apply(p2);
                    if (u2 == null) continue;
                    if (result.compareAndSet(null, u2)) {
                        this.quietlyCompleteRoot();
                    }
                    return;
                }
            }
        }
    }

    static final class SearchValuesTask<K, V, U>
    extends BulkTask<K, V, U> {
        final Fun<? super V, ? extends U> searchFunction;
        final AtomicReference<U> result;

        SearchValuesTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, Fun<? super V, ? extends U> searchFunction, AtomicReference<U> result) {
            super(p2, b2, i2, f2, t2);
            this.searchFunction = searchFunction;
            this.result = result;
        }

        @Override
        public final U getRawResult() {
            return this.result.get();
        }

        @Override
        public final void compute() {
            AtomicReference<U> result;
            Fun<V, U> searchFunction = this.searchFunction;
            if (searchFunction != null && (result = this.result) != null) {
                int f2;
                int h2;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    if (result.get() != null) {
                        return;
                    }
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    new SearchValuesTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, searchFunction, result).fork();
                }
                while (result.get() == null) {
                    Node p2 = this.advance();
                    if (p2 == null) {
                        this.propagateCompletion();
                        break;
                    }
                    U u2 = searchFunction.apply(p2.val);
                    if (u2 == null) continue;
                    if (!result.compareAndSet(null, u2)) break;
                    this.quietlyCompleteRoot();
                    break;
                }
            }
        }
    }

    static final class SearchKeysTask<K, V, U>
    extends BulkTask<K, V, U> {
        final Fun<? super K, ? extends U> searchFunction;
        final AtomicReference<U> result;

        SearchKeysTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, Fun<? super K, ? extends U> searchFunction, AtomicReference<U> result) {
            super(p2, b2, i2, f2, t2);
            this.searchFunction = searchFunction;
            this.result = result;
        }

        @Override
        public final U getRawResult() {
            return this.result.get();
        }

        @Override
        public final void compute() {
            AtomicReference<U> result;
            Fun<K, U> searchFunction = this.searchFunction;
            if (searchFunction != null && (result = this.result) != null) {
                int f2;
                int h2;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    if (result.get() != null) {
                        return;
                    }
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    new SearchKeysTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, searchFunction, result).fork();
                }
                while (result.get() == null) {
                    Node p2 = this.advance();
                    if (p2 == null) {
                        this.propagateCompletion();
                        break;
                    }
                    U u2 = searchFunction.apply(p2.key);
                    if (u2 == null) continue;
                    if (!result.compareAndSet(null, u2)) break;
                    this.quietlyCompleteRoot();
                    break;
                }
            }
        }
    }

    static final class ForEachTransformedMappingTask<K, V, U>
    extends BulkTask<K, V, Void> {
        final BiFun<? super K, ? super V, ? extends U> transformer;
        final Action<? super U> action;

        ForEachTransformedMappingTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, BiFun<? super K, ? super V, ? extends U> transformer, Action<? super U> action) {
            super(p2, b2, i2, f2, t2);
            this.transformer = transformer;
            this.action = action;
        }

        @Override
        public final void compute() {
            Action<U> action;
            BiFun<K, V, U> transformer = this.transformer;
            if (transformer != null && (action = this.action) != null) {
                Node p2;
                int f2;
                int h2;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    new ForEachTransformedMappingTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, transformer, action).fork();
                }
                while ((p2 = this.advance()) != null) {
                    U u2 = transformer.apply(p2.key, p2.val);
                    if (u2 == null) continue;
                    action.apply(u2);
                }
                this.propagateCompletion();
            }
        }
    }

    static final class ForEachTransformedEntryTask<K, V, U>
    extends BulkTask<K, V, Void> {
        final Fun<Map.Entry<K, V>, ? extends U> transformer;
        final Action<? super U> action;

        ForEachTransformedEntryTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, Fun<Map.Entry<K, V>, ? extends U> transformer, Action<? super U> action) {
            super(p2, b2, i2, f2, t2);
            this.transformer = transformer;
            this.action = action;
        }

        @Override
        public final void compute() {
            Action<U> action;
            Fun<Map.Entry<K, V>, U> transformer = this.transformer;
            if (transformer != null && (action = this.action) != null) {
                Node p2;
                int f2;
                int h2;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    new ForEachTransformedEntryTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, transformer, action).fork();
                }
                while ((p2 = this.advance()) != null) {
                    U u2 = transformer.apply(p2);
                    if (u2 == null) continue;
                    action.apply(u2);
                }
                this.propagateCompletion();
            }
        }
    }

    static final class ForEachTransformedValueTask<K, V, U>
    extends BulkTask<K, V, Void> {
        final Fun<? super V, ? extends U> transformer;
        final Action<? super U> action;

        ForEachTransformedValueTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, Fun<? super V, ? extends U> transformer, Action<? super U> action) {
            super(p2, b2, i2, f2, t2);
            this.transformer = transformer;
            this.action = action;
        }

        @Override
        public final void compute() {
            Action<U> action;
            Fun<V, U> transformer = this.transformer;
            if (transformer != null && (action = this.action) != null) {
                Node p2;
                int f2;
                int h2;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    new ForEachTransformedValueTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, transformer, action).fork();
                }
                while ((p2 = this.advance()) != null) {
                    U u2 = transformer.apply(p2.val);
                    if (u2 == null) continue;
                    action.apply(u2);
                }
                this.propagateCompletion();
            }
        }
    }

    static final class ForEachTransformedKeyTask<K, V, U>
    extends BulkTask<K, V, Void> {
        final Fun<? super K, ? extends U> transformer;
        final Action<? super U> action;

        ForEachTransformedKeyTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, Fun<? super K, ? extends U> transformer, Action<? super U> action) {
            super(p2, b2, i2, f2, t2);
            this.transformer = transformer;
            this.action = action;
        }

        @Override
        public final void compute() {
            Action<U> action;
            Fun<K, U> transformer = this.transformer;
            if (transformer != null && (action = this.action) != null) {
                Node p2;
                int f2;
                int h2;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    new ForEachTransformedKeyTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, transformer, action).fork();
                }
                while ((p2 = this.advance()) != null) {
                    U u2 = transformer.apply(p2.key);
                    if (u2 == null) continue;
                    action.apply(u2);
                }
                this.propagateCompletion();
            }
        }
    }

    static final class ForEachMappingTask<K, V>
    extends BulkTask<K, V, Void> {
        final BiAction<? super K, ? super V> action;

        ForEachMappingTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, BiAction<? super K, ? super V> action) {
            super(p2, b2, i2, f2, t2);
            this.action = action;
        }

        @Override
        public final void compute() {
            BiAction<K, V> action = this.action;
            if (action != null) {
                Node p2;
                int f2;
                int h2;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    new ForEachMappingTask<K, V>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, action).fork();
                }
                while ((p2 = this.advance()) != null) {
                    action.apply(p2.key, p2.val);
                }
                this.propagateCompletion();
            }
        }
    }

    static final class ForEachEntryTask<K, V>
    extends BulkTask<K, V, Void> {
        final Action<? super Map.Entry<K, V>> action;

        ForEachEntryTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, Action<? super Map.Entry<K, V>> action) {
            super(p2, b2, i2, f2, t2);
            this.action = action;
        }

        @Override
        public final void compute() {
            Action<Map.Entry<K, V>> action = this.action;
            if (action != null) {
                Node p2;
                int f2;
                int h2;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    new ForEachEntryTask<K, V>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, action).fork();
                }
                while ((p2 = this.advance()) != null) {
                    action.apply(p2);
                }
                this.propagateCompletion();
            }
        }
    }

    static final class ForEachValueTask<K, V>
    extends BulkTask<K, V, Void> {
        final Action<? super V> action;

        ForEachValueTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, Action<? super V> action) {
            super(p2, b2, i2, f2, t2);
            this.action = action;
        }

        @Override
        public final void compute() {
            Action<V> action = this.action;
            if (action != null) {
                Node p2;
                int f2;
                int h2;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    new ForEachValueTask<K, V>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, action).fork();
                }
                while ((p2 = this.advance()) != null) {
                    action.apply(p2.val);
                }
                this.propagateCompletion();
            }
        }
    }

    static final class ForEachKeyTask<K, V>
    extends BulkTask<K, V, Void> {
        final Action<? super K> action;

        ForEachKeyTask(BulkTask<K, V, ?> p2, int b2, int i2, int f2, Node<K, V>[] t2, Action<? super K> action) {
            super(p2, b2, i2, f2, t2);
            this.action = action;
        }

        @Override
        public final void compute() {
            Action<K> action = this.action;
            if (action != null) {
                Node p2;
                int f2;
                int h2;
                int i2 = this.baseIndex;
                while (this.batch > 0 && (h2 = (f2 = this.baseLimit) + i2 >>> 1) > i2) {
                    this.addToPendingCount(1);
                    this.baseLimit = h2;
                    new ForEachKeyTask<K, V>(this, this.batch >>>= 1, this.baseLimit, f2, this.tab, action).fork();
                }
                while ((p2 = this.advance()) != null) {
                    action.apply(p2.key);
                }
                this.propagateCompletion();
            }
        }
    }

    static abstract class BulkTask<K, V, R>
    extends CountedCompleter<R> {
        Node<K, V>[] tab;
        Node<K, V> next;
        int index;
        int baseIndex;
        int baseLimit;
        final int baseSize;
        int batch;

        BulkTask(BulkTask<K, V, ?> par, int b2, int i2, int f2, Node<K, V>[] t2) {
            super(par);
            this.batch = b2;
            this.index = this.baseIndex = i2;
            this.tab = t2;
            if (t2 == null) {
                this.baseLimit = 0;
                this.baseSize = 0;
            } else if (par == null) {
                this.baseSize = this.baseLimit = t2.length;
            } else {
                this.baseLimit = f2;
                this.baseSize = par.baseSize;
            }
        }

        final Node<K, V> advance() {
            Node<K, V> e2 = this.next;
            if (e2 != null) {
                e2 = e2.next;
            }
            while (true) {
                int n2;
                Node<K, V>[] t2;
                block9: {
                    block8: {
                        int i2;
                        if (e2 != null) {
                            this.next = e2;
                            return this.next;
                        }
                        if (this.baseIndex >= this.baseLimit) break block8;
                        t2 = this.tab;
                        if (this.tab != null && (n2 = t2.length) > (i2 = this.index) && i2 >= 0) break block9;
                    }
                    this.next = null;
                    return null;
                }
                e2 = ConcurrentHashMapV8.tabAt(t2, this.index);
                if (e2 != null && e2.hash < 0) {
                    if (e2 instanceof ForwardingNode) {
                        this.tab = ((ForwardingNode)e2).nextTable;
                        e2 = null;
                        continue;
                    }
                    e2 = e2 instanceof TreeBin ? ((TreeBin)e2).first : null;
                }
                if ((this.index += this.baseSize) < n2) continue;
                this.index = ++this.baseIndex;
            }
        }
    }

    static final class EntrySetView<K, V>
    extends CollectionView<K, V, Map.Entry<K, V>>
    implements Set<Map.Entry<K, V>>,
    Serializable {
        private static final long serialVersionUID = 2249069246763182397L;

        EntrySetView(ConcurrentHashMapV8<K, V> map) {
            super(map);
        }

        @Override
        public boolean contains(Object o2) {
            Object v2;
            Object r2;
            Map.Entry e2;
            Object k2;
            return o2 instanceof Map.Entry && (k2 = (e2 = (Map.Entry)o2).getKey()) != null && (r2 = this.map.get(k2)) != null && (v2 = e2.getValue()) != null && (v2 == r2 || v2.equals(r2));
        }

        @Override
        public boolean remove(Object o2) {
            Object v2;
            Map.Entry e2;
            Object k2;
            return o2 instanceof Map.Entry && (k2 = (e2 = (Map.Entry)o2).getKey()) != null && (v2 = e2.getValue()) != null && this.map.remove(k2, v2);
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            ConcurrentHashMapV8 m2 = this.map;
            Node<K, V>[] t2 = m2.table;
            int f2 = m2.table == null ? 0 : t2.length;
            return new EntryIterator(t2, f2, 0, f2, m2);
        }

        @Override
        public boolean add(Map.Entry<K, V> e2) {
            return this.map.putVal(e2.getKey(), e2.getValue(), false) == null;
        }

        @Override
        public boolean addAll(Collection<? extends Map.Entry<K, V>> c2) {
            boolean added = false;
            for (Map.Entry<K, V> e2 : c2) {
                if (!this.add(e2)) continue;
                added = true;
            }
            return added;
        }

        @Override
        public final int hashCode() {
            int h2 = 0;
            Node<K, V>[] t2 = this.map.table;
            if (this.map.table != null) {
                Node p2;
                Traverser it = new Traverser(t2, t2.length, 0, t2.length);
                while ((p2 = it.advance()) != null) {
                    h2 += p2.hashCode();
                }
            }
            return h2;
        }

        @Override
        public final boolean equals(Object o2) {
            Set c2;
            return o2 instanceof Set && ((c2 = (Set)o2) == this || this.containsAll(c2) && c2.containsAll(this));
        }

        public ConcurrentHashMapSpliterator<Map.Entry<K, V>> spliterator166() {
            ConcurrentHashMapV8 m2 = this.map;
            long n2 = m2.sumCount();
            Node<K, V>[] t2 = m2.table;
            int f2 = m2.table == null ? 0 : t2.length;
            return new EntrySpliterator(t2, f2, 0, f2, n2 < 0L ? 0L : n2, m2);
        }

        @Override
        public void forEach(Action<? super Map.Entry<K, V>> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            Node<K, V>[] t2 = this.map.table;
            if (this.map.table != null) {
                Node p2;
                Traverser it = new Traverser(t2, t2.length, 0, t2.length);
                while ((p2 = it.advance()) != null) {
                    action.apply(new MapEntry(p2.key, p2.val, this.map));
                }
            }
        }
    }

    static final class ValuesView<K, V>
    extends CollectionView<K, V, V>
    implements Collection<V>,
    Serializable {
        private static final long serialVersionUID = 2249069246763182397L;

        ValuesView(ConcurrentHashMapV8<K, V> map) {
            super(map);
        }

        @Override
        public final boolean contains(Object o2) {
            return this.map.containsValue(o2);
        }

        @Override
        public final boolean remove(Object o2) {
            if (o2 != null) {
                Iterator<V> it = this.iterator();
                while (it.hasNext()) {
                    if (!o2.equals(it.next())) continue;
                    it.remove();
                    return true;
                }
            }
            return false;
        }

        @Override
        public final Iterator<V> iterator() {
            ConcurrentHashMapV8 m2 = this.map;
            Node<K, V>[] t2 = m2.table;
            int f2 = m2.table == null ? 0 : t2.length;
            return new ValueIterator(t2, f2, 0, f2, m2);
        }

        @Override
        public final boolean add(V e2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public final boolean addAll(Collection<? extends V> c2) {
            throw new UnsupportedOperationException();
        }

        public ConcurrentHashMapSpliterator<V> spliterator166() {
            ConcurrentHashMapV8 m2 = this.map;
            long n2 = m2.sumCount();
            Node<K, V>[] t2 = m2.table;
            int f2 = m2.table == null ? 0 : t2.length;
            return new ValueSpliterator(t2, f2, 0, f2, n2 < 0L ? 0L : n2);
        }

        @Override
        public void forEach(Action<? super V> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            Node<K, V>[] t2 = this.map.table;
            if (this.map.table != null) {
                Node p2;
                Traverser it = new Traverser(t2, t2.length, 0, t2.length);
                while ((p2 = it.advance()) != null) {
                    action.apply(p2.val);
                }
            }
        }
    }

    public static class KeySetView<K, V>
    extends CollectionView<K, V, K>
    implements Set<K>,
    Serializable {
        private static final long serialVersionUID = 7249069246763182397L;
        private final V value;

        KeySetView(ConcurrentHashMapV8<K, V> map, V value) {
            super(map);
            this.value = value;
        }

        public V getMappedValue() {
            return this.value;
        }

        @Override
        public boolean contains(Object o2) {
            return this.map.containsKey(o2);
        }

        @Override
        public boolean remove(Object o2) {
            return this.map.remove(o2) != null;
        }

        @Override
        public Iterator<K> iterator() {
            ConcurrentHashMapV8 m2 = this.map;
            Node<K, V>[] t2 = m2.table;
            int f2 = m2.table == null ? 0 : t2.length;
            return new KeyIterator(t2, f2, 0, f2, m2);
        }

        @Override
        public boolean add(K e2) {
            V v2 = this.value;
            if (v2 == null) {
                throw new UnsupportedOperationException();
            }
            return this.map.putVal(e2, v2, true) == null;
        }

        @Override
        public boolean addAll(Collection<? extends K> c2) {
            boolean added = false;
            V v2 = this.value;
            if (v2 == null) {
                throw new UnsupportedOperationException();
            }
            for (K e2 : c2) {
                if (this.map.putVal(e2, v2, true) != null) continue;
                added = true;
            }
            return added;
        }

        @Override
        public int hashCode() {
            int h2 = 0;
            for (K e2 : this) {
                h2 += e2.hashCode();
            }
            return h2;
        }

        @Override
        public boolean equals(Object o2) {
            Set c2;
            return o2 instanceof Set && ((c2 = (Set)o2) == this || this.containsAll(c2) && c2.containsAll(this));
        }

        public ConcurrentHashMapSpliterator<K> spliterator166() {
            ConcurrentHashMapV8 m2 = this.map;
            long n2 = m2.sumCount();
            Node<K, V>[] t2 = m2.table;
            int f2 = m2.table == null ? 0 : t2.length;
            return new KeySpliterator(t2, f2, 0, f2, n2 < 0L ? 0L : n2);
        }

        @Override
        public void forEach(Action<? super K> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            Node<K, V>[] t2 = this.map.table;
            if (this.map.table != null) {
                Node p2;
                Traverser it = new Traverser(t2, t2.length, 0, t2.length);
                while ((p2 = it.advance()) != null) {
                    action.apply(p2.key);
                }
            }
        }
    }

    static abstract class CollectionView<K, V, E>
    implements Collection<E>,
    Serializable {
        private static final long serialVersionUID = 7249069246763182397L;
        final ConcurrentHashMapV8<K, V> map;
        private static final String oomeMsg = "Required array size too large";

        CollectionView(ConcurrentHashMapV8<K, V> map) {
            this.map = map;
        }

        public ConcurrentHashMapV8<K, V> getMap() {
            return this.map;
        }

        @Override
        public final void clear() {
            this.map.clear();
        }

        @Override
        public final int size() {
            return this.map.size();
        }

        @Override
        public final boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override
        public abstract Iterator<E> iterator();

        @Override
        public abstract boolean contains(Object var1);

        @Override
        public abstract boolean remove(Object var1);

        @Override
        public final Object[] toArray() {
            long sz = this.map.mappingCount();
            if (sz > 0x7FFFFFF7L) {
                throw new OutOfMemoryError(oomeMsg);
            }
            int n2 = (int)sz;
            Object[] r2 = new Object[n2];
            int i2 = 0;
            for (E e2 : this) {
                if (i2 == n2) {
                    if (n2 >= 0x7FFFFFF7) {
                        throw new OutOfMemoryError(oomeMsg);
                    }
                    n2 = n2 >= 0x3FFFFFFB ? 0x7FFFFFF7 : (n2 += (n2 >>> 1) + 1);
                    r2 = Arrays.copyOf(r2, n2);
                }
                r2[i2++] = e2;
            }
            return i2 == n2 ? r2 : Arrays.copyOf(r2, i2);
        }

        @Override
        public final <T> T[] toArray(T[] a2) {
            long sz = this.map.mappingCount();
            if (sz > 0x7FFFFFF7L) {
                throw new OutOfMemoryError(oomeMsg);
            }
            int m2 = (int)sz;
            T[] r2 = a2.length >= m2 ? a2 : (Object[])Array.newInstance(a2.getClass().getComponentType(), m2);
            int n2 = r2.length;
            int i2 = 0;
            for (E e2 : this) {
                if (i2 == n2) {
                    if (n2 >= 0x7FFFFFF7) {
                        throw new OutOfMemoryError(oomeMsg);
                    }
                    n2 = n2 >= 0x3FFFFFFB ? 0x7FFFFFF7 : (n2 += (n2 >>> 1) + 1);
                    r2 = Arrays.copyOf(r2, n2);
                }
                r2[i2++] = e2;
            }
            if (a2 == r2 && i2 < n2) {
                r2[i2] = null;
                return r2;
            }
            return i2 == n2 ? r2 : Arrays.copyOf(r2, i2);
        }

        public final String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            Iterator<E> it = this.iterator();
            if (it.hasNext()) {
                while (true) {
                    E e2;
                    sb.append((Object)((e2 = it.next()) == this ? "(this Collection)" : e2));
                    if (!it.hasNext()) break;
                    sb.append(',').append(' ');
                }
            }
            return sb.append(']').toString();
        }

        @Override
        public final boolean containsAll(Collection<?> c2) {
            if (c2 != this) {
                for (Object e2 : c2) {
                    if (e2 != null && this.contains(e2)) continue;
                    return false;
                }
            }
            return true;
        }

        @Override
        public final boolean removeAll(Collection<?> c2) {
            boolean modified = false;
            Iterator<E> it = this.iterator();
            while (it.hasNext()) {
                if (!c2.contains(it.next())) continue;
                it.remove();
                modified = true;
            }
            return modified;
        }

        @Override
        public final boolean retainAll(Collection<?> c2) {
            boolean modified = false;
            Iterator<E> it = this.iterator();
            while (it.hasNext()) {
                if (c2.contains(it.next())) continue;
                it.remove();
                modified = true;
            }
            return modified;
        }
    }

    static final class EntrySpliterator<K, V>
    extends Traverser<K, V>
    implements ConcurrentHashMapSpliterator<Map.Entry<K, V>> {
        final ConcurrentHashMapV8<K, V> map;
        long est;

        EntrySpliterator(Node<K, V>[] tab, int size, int index, int limit, long est, ConcurrentHashMapV8<K, V> map) {
            super(tab, size, index, limit);
            this.map = map;
            this.est = est;
        }

        @Override
        public ConcurrentHashMapSpliterator<Map.Entry<K, V>> trySplit() {
            EntrySpliterator<K, V> entrySpliterator;
            int i2 = this.baseIndex;
            int f2 = this.baseLimit;
            int h2 = i2 + f2 >>> 1;
            if (h2 <= i2) {
                entrySpliterator = null;
            } else {
                this.baseLimit = h2;
                EntrySpliterator<K, V> entrySpliterator2 = new EntrySpliterator<K, V>(this.tab, this.baseSize, this.baseLimit, f2, this.est >>>= 1, this.map);
                entrySpliterator = entrySpliterator2;
            }
            return entrySpliterator;
        }

        @Override
        public void forEachRemaining(Action<? super Map.Entry<K, V>> action) {
            Node p2;
            if (action == null) {
                throw new NullPointerException();
            }
            while ((p2 = this.advance()) != null) {
                action.apply(new MapEntry(p2.key, p2.val, this.map));
            }
        }

        @Override
        public boolean tryAdvance(Action<? super Map.Entry<K, V>> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            Node p2 = this.advance();
            if (p2 == null) {
                return false;
            }
            action.apply(new MapEntry(p2.key, p2.val, this.map));
            return true;
        }

        @Override
        public long estimateSize() {
            return this.est;
        }
    }

    static final class ValueSpliterator<K, V>
    extends Traverser<K, V>
    implements ConcurrentHashMapSpliterator<V> {
        long est;

        ValueSpliterator(Node<K, V>[] tab, int size, int index, int limit, long est) {
            super(tab, size, index, limit);
            this.est = est;
        }

        @Override
        public ConcurrentHashMapSpliterator<V> trySplit() {
            ValueSpliterator<K, V> valueSpliterator;
            int i2 = this.baseIndex;
            int f2 = this.baseLimit;
            int h2 = i2 + f2 >>> 1;
            if (h2 <= i2) {
                valueSpliterator = null;
            } else {
                this.baseLimit = h2;
                ValueSpliterator<K, V> valueSpliterator2 = new ValueSpliterator<K, V>(this.tab, this.baseSize, this.baseLimit, f2, this.est >>>= 1);
                valueSpliterator = valueSpliterator2;
            }
            return valueSpliterator;
        }

        @Override
        public void forEachRemaining(Action<? super V> action) {
            Node p2;
            if (action == null) {
                throw new NullPointerException();
            }
            while ((p2 = this.advance()) != null) {
                action.apply(p2.val);
            }
        }

        @Override
        public boolean tryAdvance(Action<? super V> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            Node p2 = this.advance();
            if (p2 == null) {
                return false;
            }
            action.apply(p2.val);
            return true;
        }

        @Override
        public long estimateSize() {
            return this.est;
        }
    }

    static final class KeySpliterator<K, V>
    extends Traverser<K, V>
    implements ConcurrentHashMapSpliterator<K> {
        long est;

        KeySpliterator(Node<K, V>[] tab, int size, int index, int limit, long est) {
            super(tab, size, index, limit);
            this.est = est;
        }

        @Override
        public ConcurrentHashMapSpliterator<K> trySplit() {
            KeySpliterator<K, V> keySpliterator;
            int i2 = this.baseIndex;
            int f2 = this.baseLimit;
            int h2 = i2 + f2 >>> 1;
            if (h2 <= i2) {
                keySpliterator = null;
            } else {
                this.baseLimit = h2;
                KeySpliterator<K, V> keySpliterator2 = new KeySpliterator<K, V>(this.tab, this.baseSize, this.baseLimit, f2, this.est >>>= 1);
                keySpliterator = keySpliterator2;
            }
            return keySpliterator;
        }

        @Override
        public void forEachRemaining(Action<? super K> action) {
            Node p2;
            if (action == null) {
                throw new NullPointerException();
            }
            while ((p2 = this.advance()) != null) {
                action.apply(p2.key);
            }
        }

        @Override
        public boolean tryAdvance(Action<? super K> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            Node p2 = this.advance();
            if (p2 == null) {
                return false;
            }
            action.apply(p2.key);
            return true;
        }

        @Override
        public long estimateSize() {
            return this.est;
        }
    }

    static final class MapEntry<K, V>
    implements Map.Entry<K, V> {
        final K key;
        V val;
        final ConcurrentHashMapV8<K, V> map;

        MapEntry(K key, V val2, ConcurrentHashMapV8<K, V> map) {
            this.key = key;
            this.val = val2;
            this.map = map;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.val;
        }

        @Override
        public int hashCode() {
            return this.key.hashCode() ^ this.val.hashCode();
        }

        public String toString() {
            return this.key + "=" + this.val;
        }

        @Override
        public boolean equals(Object o2) {
            Object v2;
            Map.Entry e2;
            Object k2;
            return !(!(o2 instanceof Map.Entry) || (k2 = (e2 = (Map.Entry)o2).getKey()) == null || (v2 = e2.getValue()) == null || k2 != this.key && !k2.equals(this.key) || v2 != this.val && !v2.equals(this.val));
        }

        @Override
        public V setValue(V value) {
            if (value == null) {
                throw new NullPointerException();
            }
            V v2 = this.val;
            this.val = value;
            this.map.put(this.key, value);
            return v2;
        }
    }

    static final class EntryIterator<K, V>
    extends BaseIterator<K, V>
    implements Iterator<Map.Entry<K, V>> {
        EntryIterator(Node<K, V>[] tab, int index, int size, int limit, ConcurrentHashMapV8<K, V> map) {
            super(tab, index, size, limit, map);
        }

        @Override
        public final Map.Entry<K, V> next() {
            Node p2 = this.next;
            if (p2 == null) {
                throw new NoSuchElementException();
            }
            Object k2 = p2.key;
            Object v2 = p2.val;
            this.lastReturned = p2;
            this.advance();
            return new MapEntry(k2, v2, this.map);
        }
    }

    static final class ValueIterator<K, V>
    extends BaseIterator<K, V>
    implements Iterator<V>,
    Enumeration<V> {
        ValueIterator(Node<K, V>[] tab, int index, int size, int limit, ConcurrentHashMapV8<K, V> map) {
            super(tab, index, size, limit, map);
        }

        @Override
        public final V next() {
            Node p2 = this.next;
            if (p2 == null) {
                throw new NoSuchElementException();
            }
            Object v2 = p2.val;
            this.lastReturned = p2;
            this.advance();
            return v2;
        }

        @Override
        public final V nextElement() {
            return this.next();
        }
    }

    static final class KeyIterator<K, V>
    extends BaseIterator<K, V>
    implements Iterator<K>,
    Enumeration<K> {
        KeyIterator(Node<K, V>[] tab, int index, int size, int limit, ConcurrentHashMapV8<K, V> map) {
            super(tab, index, size, limit, map);
        }

        @Override
        public final K next() {
            Node p2 = this.next;
            if (p2 == null) {
                throw new NoSuchElementException();
            }
            Object k2 = p2.key;
            this.lastReturned = p2;
            this.advance();
            return k2;
        }

        @Override
        public final K nextElement() {
            return this.next();
        }
    }

    static class BaseIterator<K, V>
    extends Traverser<K, V> {
        final ConcurrentHashMapV8<K, V> map;
        Node<K, V> lastReturned;

        BaseIterator(Node<K, V>[] tab, int size, int index, int limit, ConcurrentHashMapV8<K, V> map) {
            super(tab, size, index, limit);
            this.map = map;
            this.advance();
        }

        public final boolean hasNext() {
            return this.next != null;
        }

        public final boolean hasMoreElements() {
            return this.next != null;
        }

        public final void remove() {
            Node<K, V> p2 = this.lastReturned;
            if (p2 == null) {
                throw new IllegalStateException();
            }
            this.lastReturned = null;
            this.map.replaceNode(p2.key, null, null);
        }
    }

    static class Traverser<K, V> {
        Node<K, V>[] tab;
        Node<K, V> next;
        int index;
        int baseIndex;
        int baseLimit;
        final int baseSize;

        Traverser(Node<K, V>[] tab, int size, int index, int limit) {
            this.tab = tab;
            this.baseSize = size;
            this.baseIndex = this.index = index;
            this.baseLimit = limit;
            this.next = null;
        }

        final Node<K, V> advance() {
            Node<K, V> e2 = this.next;
            if (e2 != null) {
                e2 = e2.next;
            }
            while (true) {
                int n2;
                Node<K, V>[] t2;
                block9: {
                    block8: {
                        int i2;
                        if (e2 != null) {
                            this.next = e2;
                            return this.next;
                        }
                        if (this.baseIndex >= this.baseLimit) break block8;
                        t2 = this.tab;
                        if (this.tab != null && (n2 = t2.length) > (i2 = this.index) && i2 >= 0) break block9;
                    }
                    this.next = null;
                    return null;
                }
                e2 = ConcurrentHashMapV8.tabAt(t2, this.index);
                if (e2 != null && e2.hash < 0) {
                    if (e2 instanceof ForwardingNode) {
                        this.tab = ((ForwardingNode)e2).nextTable;
                        e2 = null;
                        continue;
                    }
                    e2 = e2 instanceof TreeBin ? ((TreeBin)e2).first : null;
                }
                if ((this.index += this.baseSize) < n2) continue;
                this.index = ++this.baseIndex;
            }
        }
    }

    static final class TreeBin<K, V>
    extends Node<K, V> {
        TreeNode<K, V> root;
        volatile TreeNode<K, V> first;
        volatile Thread waiter;
        volatile int lockState;
        static final int WRITER = 1;
        static final int WAITER = 2;
        static final int READER = 4;
        private static final Unsafe U;
        private static final long LOCKSTATE;

        TreeBin(TreeNode<K, V> b2) {
            super(-2, null, null, null);
            this.first = b2;
            TreeNode r2 = null;
            TreeNode x2 = b2;
            while (x2 != null) {
                TreeNode next = (TreeNode)x2.next;
                x2.right = null;
                x2.left = null;
                if (r2 == null) {
                    x2.parent = null;
                    x2.red = false;
                    r2 = x2;
                } else {
                    TreeNode xp;
                    int dir;
                    Object key = x2.key;
                    int hash = x2.hash;
                    Class<?> kc = null;
                    TreeNode p2 = r2;
                    do {
                        int ph;
                        dir = (ph = p2.hash) > hash ? -1 : (ph < hash ? 1 : (kc != null || (kc = ConcurrentHashMapV8.comparableClassFor(key)) != null ? ConcurrentHashMapV8.compareComparables(kc, key, p2.key) : 0));
                        xp = p2;
                    } while ((p2 = dir <= 0 ? p2.left : p2.right) != null);
                    x2.parent = xp;
                    if (dir <= 0) {
                        xp.left = x2;
                    } else {
                        xp.right = x2;
                    }
                    r2 = TreeBin.balanceInsertion(r2, x2);
                }
                x2 = next;
            }
            this.root = r2;
        }

        private final void lockRoot() {
            if (!U.compareAndSwapInt(this, LOCKSTATE, 0, 1)) {
                this.contendedLock();
            }
        }

        private final void unlockRoot() {
            this.lockState = 0;
        }

        private final void contendedLock() {
            boolean waiting = false;
            while (true) {
                int s2;
                if (((s2 = this.lockState) & 1) == 0) {
                    if (!U.compareAndSwapInt(this, LOCKSTATE, s2, 1)) continue;
                    if (waiting) {
                        this.waiter = null;
                    }
                    return;
                }
                if ((s2 & 2) == 0) {
                    if (!U.compareAndSwapInt(this, LOCKSTATE, s2, s2 | 2)) continue;
                    waiting = true;
                    this.waiter = Thread.currentThread();
                    continue;
                }
                if (!waiting) continue;
                LockSupport.park(this);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        final Node<K, V> find(int h2, Object k2) {
            if (k2 != null) {
                Node e2 = this.first;
                while (e2 != null) {
                    int s2 = this.lockState;
                    if ((s2 & 3) != 0) {
                        Object ek;
                        if (e2.hash == h2 && ((ek = e2.key) == k2 || ek != null && k2.equals(ek))) {
                            return e2;
                        }
                    } else if (U.compareAndSwapInt(this, LOCKSTATE, s2, s2 + 4)) {
                        TreeNode<K, V> p2;
                        try {
                            TreeNode<K, V> r2 = this.root;
                            p2 = r2 == null ? null : r2.findTreeNode(h2, k2, null);
                        }
                        finally {
                            Thread w2;
                            int ls;
                            while (!U.compareAndSwapInt(this, LOCKSTATE, ls = this.lockState, ls - 4)) {
                            }
                            if (ls == 6 && (w2 = this.waiter) != null) {
                                LockSupport.unpark(w2);
                            }
                        }
                        return p2;
                    }
                    e2 = e2.next;
                }
            }
            return null;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        final TreeNode<K, V> putTreeVal(int h2, K k2, V v2) {
            block21: {
                TreeNode<K, V> xp;
                int dir;
                Class<?> kc = null;
                TreeNode<K, V> p2 = this.root;
                do {
                    if (p2 == null) {
                        this.root = new TreeNode<K, V>(h2, k2, v2, null, null);
                        this.first = this.root;
                        break block21;
                    }
                    int ph = p2.hash;
                    if (ph > h2) {
                        dir = -1;
                    } else if (ph < h2) {
                        dir = 1;
                    } else {
                        Object pk = p2.key;
                        if (pk == k2 || pk != null && k2.equals(pk)) {
                            return p2;
                        }
                        if (kc == null && (kc = ConcurrentHashMapV8.comparableClassFor(k2)) == null || (dir = ConcurrentHashMapV8.compareComparables(kc, k2, pk)) == 0) {
                            if (p2.left == null) {
                                dir = 1;
                            } else {
                                TreeNode q2;
                                TreeNode pr = p2.right;
                                if (pr == null || (q2 = pr.findTreeNode(h2, k2, kc)) == null) {
                                    dir = -1;
                                } else {
                                    return q2;
                                }
                            }
                        }
                    }
                    xp = p2;
                } while ((p2 = dir < 0 ? p2.left : p2.right) != null);
                TreeNode<K, V> f2 = this.first;
                TreeNode<K, V> x2 = new TreeNode<K, V>(h2, k2, v2, f2, xp);
                this.first = x2;
                if (f2 != null) {
                    f2.prev = x2;
                }
                if (dir < 0) {
                    xp.left = x2;
                } else {
                    xp.right = x2;
                }
                if (!xp.red) {
                    x2.red = true;
                } else {
                    this.lockRoot();
                    try {
                        this.root = TreeBin.balanceInsertion(this.root, x2);
                    }
                    finally {
                        this.unlockRoot();
                    }
                }
            }
            assert (TreeBin.checkInvariants(this.root));
            return null;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        final boolean removeTreeNode(TreeNode<K, V> p2) {
            TreeNode rl;
            TreeNode next = (TreeNode)p2.next;
            TreeNode pred = p2.prev;
            if (pred == null) {
                this.first = next;
            } else {
                pred.next = next;
            }
            if (next != null) {
                next.prev = pred;
            }
            if (this.first == null) {
                this.root = null;
                return true;
            }
            TreeNode<K, V> r2 = this.root;
            if (r2 == null || r2.right == null || (rl = r2.left) == null || rl.left == null) {
                return true;
            }
            this.lockRoot();
            try {
                TreeNode pp;
                TreeNode replacement;
                TreeNode pl = p2.left;
                TreeNode pr = p2.right;
                if (pl != null && pr != null) {
                    TreeNode sl;
                    TreeNode s2 = pr;
                    while ((sl = s2.left) != null) {
                        s2 = sl;
                    }
                    boolean c2 = s2.red;
                    s2.red = p2.red;
                    p2.red = c2;
                    TreeNode sr = s2.right;
                    TreeNode pp2 = p2.parent;
                    if (s2 == pr) {
                        p2.parent = s2;
                        s2.right = p2;
                    } else {
                        TreeNode sp = s2.parent;
                        p2.parent = sp;
                        if (p2.parent != null) {
                            if (s2 == sp.left) {
                                sp.left = p2;
                            } else {
                                sp.right = p2;
                            }
                        }
                        s2.right = pr;
                        pr.parent = s2;
                    }
                    p2.left = null;
                    s2.left = pl;
                    pl.parent = s2;
                    p2.right = sr;
                    if (p2.right != null) {
                        sr.parent = p2;
                    }
                    if ((s2.parent = pp2) == null) {
                        r2 = s2;
                    } else if (p2 == pp2.left) {
                        pp2.left = s2;
                    } else {
                        pp2.right = s2;
                    }
                    replacement = sr != null ? sr : p2;
                } else {
                    replacement = pl != null ? pl : (pr != null ? pr : p2);
                }
                if (replacement != p2) {
                    replacement.parent = p2.parent;
                    pp = replacement.parent;
                    if (pp == null) {
                        r2 = replacement;
                    } else if (p2 == pp.left) {
                        pp.left = replacement;
                    } else {
                        pp.right = replacement;
                    }
                    p2.parent = null;
                    p2.right = null;
                    p2.left = null;
                }
                TreeNode<K, V> treeNode = this.root = p2.red ? r2 : TreeBin.balanceDeletion(r2, replacement);
                if (p2 == replacement && (pp = p2.parent) != null) {
                    if (p2 == pp.left) {
                        pp.left = null;
                    } else if (p2 == pp.right) {
                        pp.right = null;
                    }
                    p2.parent = null;
                }
            }
            finally {
                this.unlockRoot();
            }
            assert (TreeBin.checkInvariants(this.root));
            return false;
        }

        static <K, V> TreeNode<K, V> rotateLeft(TreeNode<K, V> root, TreeNode<K, V> p2) {
            TreeNode r2;
            if (p2 != null && (r2 = p2.right) != null) {
                p2.right = r2.left;
                TreeNode rl = p2.right;
                if (p2.right != null) {
                    rl.parent = p2;
                }
                TreeNode pp = r2.parent = p2.parent;
                if (r2.parent == null) {
                    root = r2;
                    r2.red = false;
                } else if (pp.left == p2) {
                    pp.left = r2;
                } else {
                    pp.right = r2;
                }
                r2.left = p2;
                p2.parent = r2;
            }
            return root;
        }

        static <K, V> TreeNode<K, V> rotateRight(TreeNode<K, V> root, TreeNode<K, V> p2) {
            TreeNode l2;
            if (p2 != null && (l2 = p2.left) != null) {
                p2.left = l2.right;
                TreeNode lr = p2.left;
                if (p2.left != null) {
                    lr.parent = p2;
                }
                TreeNode pp = l2.parent = p2.parent;
                if (l2.parent == null) {
                    root = l2;
                    l2.red = false;
                } else if (pp.right == p2) {
                    pp.right = l2;
                } else {
                    pp.left = l2;
                }
                l2.right = p2;
                p2.parent = l2;
            }
            return root;
        }

        static <K, V> TreeNode<K, V> balanceInsertion(TreeNode<K, V> root, TreeNode<K, V> x2) {
            x2.red = true;
            while (true) {
                TreeNode xpp;
                TreeNode xp;
                if ((xp = x2.parent) == null) {
                    x2.red = false;
                    return x2;
                }
                if (!xp.red || (xpp = xp.parent) == null) {
                    return root;
                }
                TreeNode xppl = xpp.left;
                if (xp == xppl) {
                    TreeNode xppr = xpp.right;
                    if (xppr != null && xppr.red) {
                        xppr.red = false;
                        xp.red = false;
                        xpp.red = true;
                        x2 = xpp;
                        continue;
                    }
                    if (x2 == xp.right) {
                        x2 = xp;
                        root = TreeBin.rotateLeft(root, x2);
                        xp = x2.parent;
                        TreeNode treeNode = xpp = xp == null ? null : xp.parent;
                    }
                    if (xp == null) continue;
                    xp.red = false;
                    if (xpp == null) continue;
                    xpp.red = true;
                    root = TreeBin.rotateRight(root, xpp);
                    continue;
                }
                if (xppl != null && xppl.red) {
                    xppl.red = false;
                    xp.red = false;
                    xpp.red = true;
                    x2 = xpp;
                    continue;
                }
                if (x2 == xp.left) {
                    x2 = xp;
                    root = TreeBin.rotateRight(root, x2);
                    xp = x2.parent;
                    TreeNode treeNode = xpp = xp == null ? null : xp.parent;
                }
                if (xp == null) continue;
                xp.red = false;
                if (xpp == null) continue;
                xpp.red = true;
                root = TreeBin.rotateLeft(root, xpp);
            }
        }

        static <K, V> TreeNode<K, V> balanceDeletion(TreeNode<K, V> root, TreeNode<K, V> x2) {
            while (x2 != null && x2 != root) {
                TreeNode sr;
                TreeNode sl;
                TreeNode xp = x2.parent;
                if (xp == null) {
                    x2.red = false;
                    return x2;
                }
                if (x2.red) {
                    x2.red = false;
                    return root;
                }
                TreeNode xpl = xp.left;
                if (xpl == x2) {
                    TreeNode xpr = xp.right;
                    if (xpr != null && xpr.red) {
                        xpr.red = false;
                        xp.red = true;
                        root = TreeBin.rotateLeft(root, xp);
                        xp = x2.parent;
                        TreeNode treeNode = xpr = xp == null ? null : xp.right;
                    }
                    if (xpr == null) {
                        x2 = xp;
                        continue;
                    }
                    sl = xpr.left;
                    sr = xpr.right;
                    if (!(sr != null && sr.red || sl != null && sl.red)) {
                        xpr.red = true;
                        x2 = xp;
                        continue;
                    }
                    if (sr == null || !sr.red) {
                        if (sl != null) {
                            sl.red = false;
                        }
                        xpr.red = true;
                        root = TreeBin.rotateRight(root, xpr);
                        xp = x2.parent;
                        TreeNode treeNode = xpr = xp == null ? null : xp.right;
                    }
                    if (xpr != null) {
                        xpr.red = xp == null ? false : xp.red;
                        sr = xpr.right;
                        if (sr != null) {
                            sr.red = false;
                        }
                    }
                    if (xp != null) {
                        xp.red = false;
                        root = TreeBin.rotateLeft(root, xp);
                    }
                    x2 = root;
                    continue;
                }
                if (xpl != null && xpl.red) {
                    xpl.red = false;
                    xp.red = true;
                    root = TreeBin.rotateRight(root, xp);
                    xp = x2.parent;
                    TreeNode treeNode = xpl = xp == null ? null : xp.left;
                }
                if (xpl == null) {
                    x2 = xp;
                    continue;
                }
                sl = xpl.left;
                sr = xpl.right;
                if (!(sl != null && sl.red || sr != null && sr.red)) {
                    xpl.red = true;
                    x2 = xp;
                    continue;
                }
                if (sl == null || !sl.red) {
                    if (sr != null) {
                        sr.red = false;
                    }
                    xpl.red = true;
                    root = TreeBin.rotateLeft(root, xpl);
                    xp = x2.parent;
                    TreeNode treeNode = xpl = xp == null ? null : xp.left;
                }
                if (xpl != null) {
                    xpl.red = xp == null ? false : xp.red;
                    sl = xpl.left;
                    if (sl != null) {
                        sl.red = false;
                    }
                }
                if (xp != null) {
                    xp.red = false;
                    root = TreeBin.rotateRight(root, xp);
                }
                x2 = root;
            }
            return root;
        }

        static <K, V> boolean checkInvariants(TreeNode<K, V> t2) {
            TreeNode tp = t2.parent;
            TreeNode tl = t2.left;
            TreeNode tr = t2.right;
            TreeNode tb = t2.prev;
            TreeNode tn = (TreeNode)t2.next;
            if (tb != null && tb.next != t2) {
                return false;
            }
            if (tn != null && tn.prev != t2) {
                return false;
            }
            if (tp != null && t2 != tp.left && t2 != tp.right) {
                return false;
            }
            if (tl != null && (tl.parent != t2 || tl.hash > t2.hash)) {
                return false;
            }
            if (tr != null && (tr.parent != t2 || tr.hash < t2.hash)) {
                return false;
            }
            if (t2.red && tl != null && tl.red && tr != null && tr.red) {
                return false;
            }
            if (tl != null && !TreeBin.checkInvariants(tl)) {
                return false;
            }
            return tr == null || TreeBin.checkInvariants(tr);
        }

        static {
            try {
                U = ConcurrentHashMapV8.getUnsafe();
                Class<TreeBin> k2 = TreeBin.class;
                LOCKSTATE = U.objectFieldOffset(k2.getDeclaredField("lockState"));
            }
            catch (Exception e2) {
                throw new Error(e2);
            }
        }
    }

    static final class TreeNode<K, V>
    extends Node<K, V> {
        TreeNode<K, V> parent;
        TreeNode<K, V> left;
        TreeNode<K, V> right;
        TreeNode<K, V> prev;
        boolean red;

        TreeNode(int hash, K key, V val2, Node<K, V> next, TreeNode<K, V> parent) {
            super(hash, key, val2, next);
            this.parent = parent;
        }

        @Override
        Node<K, V> find(int h2, Object k2) {
            return this.findTreeNode(h2, k2, null);
        }

        final TreeNode<K, V> findTreeNode(int h2, Object k2, Class<?> kc) {
            if (k2 != null) {
                TreeNode<K, V> p2 = this;
                do {
                    TreeNode<K, V> q2;
                    int dir;
                    TreeNode<K, V> pl = p2.left;
                    TreeNode<K, V> pr = p2.right;
                    int ph = p2.hash;
                    if (ph > h2) {
                        p2 = pl;
                        continue;
                    }
                    if (ph < h2) {
                        p2 = pr;
                        continue;
                    }
                    Object pk = p2.key;
                    if (pk == k2 || pk != null && k2.equals(pk)) {
                        return p2;
                    }
                    if (pl == null && pr == null) break;
                    if ((kc != null || (kc = ConcurrentHashMapV8.comparableClassFor(k2)) != null) && (dir = ConcurrentHashMapV8.compareComparables(kc, k2, pk)) != 0) {
                        p2 = dir < 0 ? pl : pr;
                        continue;
                    }
                    if (pl == null) {
                        p2 = pr;
                        continue;
                    }
                    if (pr == null || (q2 = pr.findTreeNode(h2, k2, kc)) == null) {
                        p2 = pl;
                        continue;
                    }
                    return q2;
                } while (p2 != null);
            }
            return null;
        }
    }

    static final class ReservationNode<K, V>
    extends Node<K, V> {
        ReservationNode() {
            super(-3, null, null, null);
        }

        @Override
        Node<K, V> find(int h2, Object k2) {
            return null;
        }
    }

    static final class ForwardingNode<K, V>
    extends Node<K, V> {
        final Node<K, V>[] nextTable;

        ForwardingNode(Node<K, V>[] tab) {
            super(-1, null, null, null);
            this.nextTable = tab;
        }

        @Override
        Node<K, V> find(int h2, Object k2) {
            Node<K, V>[] tab = this.nextTable;
            block0: while (true) {
                Node<K, V> e2;
                int n2;
                if (k2 == null || tab == null || (n2 = tab.length) == 0 || (e2 = ConcurrentHashMapV8.tabAt(tab, n2 - 1 & h2)) == null) {
                    return null;
                }
                do {
                    Object ek;
                    int eh;
                    if ((eh = e2.hash) == h2 && ((ek = e2.key) == k2 || ek != null && k2.equals(ek))) {
                        return e2;
                    }
                    if (eh >= 0) continue;
                    if (e2 instanceof ForwardingNode) {
                        tab = ((ForwardingNode)e2).nextTable;
                        continue block0;
                    }
                    return e2.find(h2, k2);
                } while ((e2 = e2.next) != null);
                break;
            }
            return null;
        }
    }

    static class Segment<K, V>
    extends ReentrantLock
    implements Serializable {
        private static final long serialVersionUID = 2249069246763182397L;
        final float loadFactor;

        Segment(float lf) {
            this.loadFactor = lf;
        }
    }

    static class Node<K, V>
    implements Map.Entry<K, V> {
        final int hash;
        final K key;
        volatile V val;
        volatile Node<K, V> next;

        Node(int hash, K key, V val2, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.val = val2;
            this.next = next;
        }

        @Override
        public final K getKey() {
            return this.key;
        }

        @Override
        public final V getValue() {
            return this.val;
        }

        @Override
        public final int hashCode() {
            return this.key.hashCode() ^ this.val.hashCode();
        }

        public final String toString() {
            return this.key + "=" + this.val;
        }

        @Override
        public final V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public final boolean equals(Object o2) {
            V u2;
            Object v2;
            Map.Entry e2;
            Object k2;
            return !(!(o2 instanceof Map.Entry) || (k2 = (e2 = (Map.Entry)o2).getKey()) == null || (v2 = e2.getValue()) == null || k2 != this.key && !k2.equals(this.key) || v2 != (u2 = this.val) && !v2.equals(u2));
        }

        Node<K, V> find(int h2, Object k2) {
            Node<K, V> e2 = this;
            if (k2 != null) {
                do {
                    K ek;
                    if (e2.hash != h2 || (ek = e2.key) != k2 && (ek == null || !k2.equals(ek))) continue;
                    return e2;
                } while ((e2 = e2.next) != null);
            }
            return null;
        }
    }

    public static interface IntByIntToInt {
        public int apply(int var1, int var2);
    }

    public static interface LongByLongToLong {
        public long apply(long var1, long var3);
    }

    public static interface DoubleByDoubleToDouble {
        public double apply(double var1, double var3);
    }

    public static interface ObjectByObjectToInt<A, B> {
        public int apply(A var1, B var2);
    }

    public static interface ObjectByObjectToLong<A, B> {
        public long apply(A var1, B var2);
    }

    public static interface ObjectByObjectToDouble<A, B> {
        public double apply(A var1, B var2);
    }

    public static interface ObjectToInt<A> {
        public int apply(A var1);
    }

    public static interface ObjectToLong<A> {
        public long apply(A var1);
    }

    public static interface ObjectToDouble<A> {
        public double apply(A var1);
    }

    public static interface BiFun<A, B, T> {
        public T apply(A var1, B var2);
    }

    public static interface Fun<A, T> {
        public T apply(A var1);
    }

    public static interface BiAction<A, B> {
        public void apply(A var1, B var2);
    }

    public static interface Action<A> {
        public void apply(A var1);
    }

    public static interface ConcurrentHashMapSpliterator<T> {
        public ConcurrentHashMapSpliterator<T> trySplit();

        public long estimateSize();

        public void forEachRemaining(Action<? super T> var1);

        public boolean tryAdvance(Action<? super T> var1);
    }
}

