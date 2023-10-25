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
    private static final long TRANSFERINDEX;
    static final int MIN_TREEIFY_CAPACITY = 64;
    private static final int MIN_TRANSFER_STRIDE = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    private static final int DEFAULT_CAPACITY = 16;
    static final int MOVED = -1;
    private volatile transient long baseCount;
    private volatile transient int cellsBusy;
    private transient EntrySetView<K, V> entrySet;
    private static final int ASHIFT;
    static final int TREEBIN = -2;
    private static final long BASECOUNT;
    static final int MAX_ARRAY_SIZE = 0x7FFFFFF7;
    private static final long ABASE;
    static final int SEED_INCREMENT = 1640531527;
    private static final Unsafe U;
    private volatile transient CounterCell[] counterCells;
    private transient ValuesView<K, V> values;
    private volatile transient Node<K, V>[] nextTable;
    static final int NCPU;
    volatile transient Node<K, V>[] table;
    private transient KeySetView<K, V> keySet;
    static final int RESERVED = -3;
    private volatile transient int transferIndex;
    private static final ObjectStreamField[] serialPersistentFields;
    private static final long TRANSFERORIGIN;
    private static final long serialVersionUID = 7249069246763182397L;
    static final int HASH_BITS = Integer.MAX_VALUE;
    private static final int MAXIMUM_CAPACITY = 0x40000000;
    private volatile transient int sizeCtl;
    private static final long SIZECTL;
    static final AtomicInteger counterHashCodeGenerator;
    private static final long CELLVALUE;
    private volatile transient int transferOrigin;
    private static final long CELLSBUSY;
    static final int TREEIFY_THRESHOLD = 8;
    static final int UNTREEIFY_THRESHOLD = 6;

    @Override
    public V compute(K k, BiFun<? super K, ? super V, ? extends V> biFun) {
        int n;
        int n2;
        Object v;
        block28: {
            int n3;
            if (k == null || biFun == null) {
                throw new NullPointerException();
            }
            int n4 = ConcurrentHashMapV8.spread(k.hashCode());
            v = null;
            n2 = 0;
            n = 0;
            Node<K, V>[] nodeArray = this.table;
            while (true) {
                Node node;
                Node node2;
                Node node3;
                int n5;
                if (nodeArray == null || (n5 = nodeArray.length) == 0) {
                    nodeArray = this.initTable();
                    continue;
                }
                n3 = n5 - 1 & n4;
                Node<K, V> node4 = ConcurrentHashMapV8.tabAt(nodeArray, n3);
                if (node4 == null) {
                    node3 = new ReservationNode();
                    node2 = node3;
                    synchronized (node2) {
                        if (ConcurrentHashMapV8.casTabAt(nodeArray, n3, null, node3)) {
                            n = 1;
                            node = null;
                            V v2 = biFun.apply(k, null);
                            v = v2;
                            if (v2 != null) {
                                n2 = 1;
                                node = new Node<K, Object>(n4, k, v, null);
                            }
                            ConcurrentHashMapV8.setTabAt(nodeArray, n3, node);
                        }
                        if (n == 0) continue;
                    }
                    break block28;
                }
                int n6 = node4.hash;
                if (n6 == -1) {
                    nodeArray = this.helpTransfer(nodeArray, node4);
                    continue;
                }
                node3 = node4;
                synchronized (node3) {
                    block29: {
                        if (ConcurrentHashMapV8.tabAt(nodeArray, n3) == node4) {
                            Node node5;
                            Object object;
                            if (n6 >= 0) {
                                n = 1;
                                node2 = node4;
                                node = null;
                                while (true) {
                                    if (node2.hash == n4 && ((object = node2.key) == k || object != null && k.equals(object))) {
                                        v = biFun.apply(k, node2.val);
                                        if (v != null) {
                                            node2.val = v;
                                        } else {
                                            n2 = -1;
                                            node5 = node2.next;
                                            if (node != null) {
                                                node.next = node5;
                                            } else {
                                                ConcurrentHashMapV8.setTabAt(nodeArray, n3, node5);
                                            }
                                        }
                                        break block29;
                                    }
                                    node = node2;
                                    node2 = node2.next;
                                    if (node2 == null) {
                                        v = biFun.apply(k, null);
                                        if (v != null) {
                                            n2 = 1;
                                            node.next = new Node<K, Object>(n4, k, v, null);
                                        }
                                        break block29;
                                    }
                                    ++n;
                                }
                            }
                            if (node4 instanceof TreeBin) {
                                n = 1;
                                node2 = (TreeBin)node4;
                                node = ((TreeBin)node2).root;
                                object = node != null ? ((TreeNode)node).findTreeNode(n4, k, null) : null;
                                node5 = object == null ? null : ((TreeNode)object).val;
                                v = biFun.apply(k, node5);
                                if (v != null) {
                                    if (object != null) {
                                        ((TreeNode)object).val = v;
                                    } else {
                                        n2 = 1;
                                        ((TreeBin)node2).putTreeVal(n4, k, v);
                                    }
                                } else if (object != null) {
                                    n2 = -1;
                                    if (((TreeBin)node2).removeTreeNode(object)) {
                                        ConcurrentHashMapV8.setTabAt(nodeArray, n3, ConcurrentHashMapV8.untreeify(((TreeBin)node2).first));
                                    }
                                }
                            }
                        }
                    }
                    if (n != 0) break;
                }
            }
            if (n >= 8) {
                this.treeifyBin(nodeArray, n3);
            }
        }
        if (n2 != 0) {
            this.addCount(n2, n);
        }
        return v;
    }

    public <U> void forEachKey(long l, Fun<? super K, ? extends U> fun, Action<? super U> action) {
        if (fun == null || action == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedKeyTask<K, V, U>(null, this.batchFor(l), 0, 0, this.table, fun, action).invoke();
    }

    static final <K, V> boolean casTabAt(Node<K, V>[] nodeArray, int n, Node<K, V> node, Node<K, V> node2) {
        return U.compareAndSwapObject(nodeArray, ((long)n << ASHIFT) + ABASE, node, node2);
    }

    public <U> U search(long l, BiFun<? super K, ? super V, ? extends U> biFun) {
        if (biFun == null) {
            throw new NullPointerException();
        }
        return (U)new SearchMappingsTask<K, V, U>(null, this.batchFor(l), 0, 0, this.table, biFun, new AtomicReference()).invoke();
    }

    @Override
    public V getOrDefault(Object object, V v) {
        V v2 = this.get(object);
        return v2 == null ? v : v2;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        int n;
        int n2 = 0;
        for (n = 1; n < 16; n <<= 1) {
            ++n2;
        }
        int n3 = 32 - n2;
        int n4 = n - 1;
        Segment[] segmentArray = new Segment[16];
        for (int i = 0; i < segmentArray.length; ++i) {
            segmentArray[i] = new Segment(0.75f);
        }
        objectOutputStream.putFields().put("segments", segmentArray);
        objectOutputStream.putFields().put("segmentShift", n3);
        objectOutputStream.putFields().put("segmentMask", n4);
        objectOutputStream.writeFields();
        Node<K, V>[] nodeArray = this.table;
        if (this.table != null) {
            Node<K, V> node;
            Traverser<K, V> traverser = new Traverser<K, V>(nodeArray, nodeArray.length, 0, nodeArray.length);
            while ((node = traverser.advance()) != null) {
                objectOutputStream.writeObject(node.key);
                objectOutputStream.writeObject(node.val);
            }
        }
        objectOutputStream.writeObject(null);
        objectOutputStream.writeObject(null);
        segmentArray = null;
    }

    public ConcurrentHashMapV8() {
    }

    public long mappingCount() {
        long l = this.sumCount();
        return l < 0L ? 0L : l;
    }

    @Deprecated
    public boolean contains(Object object) {
        return this.containsValue(object);
    }

    public long reduceEntriesToLong(long l, ObjectToLong<Map.Entry<K, V>> objectToLong, long l2, LongByLongToLong longByLongToLong) {
        if (objectToLong == null || longByLongToLong == null) {
            throw new NullPointerException();
        }
        return (Long)new MapReduceEntriesToLongTask<K, V>(null, this.batchFor(l), 0, 0, this.table, null, objectToLong, l2, longByLongToLong).invoke();
    }

    public <U> void forEachEntry(long l, Fun<Map.Entry<K, V>, ? extends U> fun, Action<? super U> action) {
        if (fun == null || action == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedEntryTask<K, V, U>(null, this.batchFor(l), 0, 0, this.table, fun, action).invoke();
    }

    public <U> U searchEntries(long l, Fun<Map.Entry<K, V>, ? extends U> fun) {
        if (fun == null) {
            throw new NullPointerException();
        }
        return (U)new SearchEntriesTask<K, V, U>(null, this.batchFor(l), 0, 0, this.table, fun, new AtomicReference()).invoke();
    }

    public void forEachKey(long l, Action<? super K> action) {
        if (action == null) {
            throw new NullPointerException();
        }
        new ForEachKeyTask<K, V>(null, this.batchFor(l), 0, 0, this.table, action).invoke();
    }

    @Override
    public boolean isEmpty() {
        return this.sumCount() <= 0L;
    }

    public void forEach(long l, BiAction<? super K, ? super V> biAction) {
        if (biAction == null) {
            throw new NullPointerException();
        }
        new ForEachMappingTask<K, V>(null, this.batchFor(l), 0, 0, this.table, biAction).invoke();
    }

    static int compareComparables(Class<?> clazz, Object object, Object object2) {
        return object2 == null || object2.getClass() != clazz ? 0 : ((Comparable)object).compareTo(object2);
    }

    public <U> U reduce(long l, BiFun<? super K, ? super V, ? extends U> biFun, BiFun<? super U, ? super U, ? extends U> biFun2) {
        if (biFun == null || biFun2 == null) {
            throw new NullPointerException();
        }
        return (U)new MapReduceMappingsTask<K, V, U>(null, this.batchFor(l), 0, 0, this.table, null, biFun, biFun2).invoke();
    }

    private static final int tableSizeFor(int n) {
        int n2 = n - 1;
        n2 |= n2 >>> 1;
        n2 |= n2 >>> 2;
        n2 |= n2 >>> 4;
        n2 |= n2 >>> 8;
        return (n2 |= n2 >>> 16) < 0 ? 1 : (n2 >= 0x40000000 ? 0x40000000 : n2 + 1);
    }

    @Override
    public boolean containsKey(Object object) {
        return this.get(object) != null;
    }

    @Override
    public V get(Object object) {
        Node<K, V> node;
        int n;
        int n2 = ConcurrentHashMapV8.spread(object.hashCode());
        Node<K, V>[] nodeArray = this.table;
        if (this.table != null && (n = nodeArray.length) > 0 && (node = ConcurrentHashMapV8.tabAt(nodeArray, n - 1 & n2)) != null) {
            Object k;
            int n3 = node.hash;
            if (n3 == n2) {
                k = node.key;
                if (k == object || k != null && object.equals(k)) {
                    return node.val;
                }
            } else if (n3 < 0) {
                Node<K, V> node2 = node.find(n2, object);
                return node2 != null ? (V)node2.val : null;
            }
            while ((node = node.next) != null) {
                if (node.hash != n2 || (k = node.key) != object && (k == null || !object.equals(k))) continue;
                return node.val;
            }
        }
        return null;
    }

    @Override
    public V computeIfPresent(K k, BiFun<? super K, ? super V, ? extends V> biFun) {
        if (k == null || biFun == null) {
            throw new NullPointerException();
        }
        int n = ConcurrentHashMapV8.spread(k.hashCode());
        V v = null;
        int n2 = 0;
        int n3 = 0;
        Node<K, V>[] nodeArray = this.table;
        while (true) {
            int n4;
            if (nodeArray == null || (n4 = nodeArray.length) == 0) {
                nodeArray = this.initTable();
                continue;
            }
            int n5 = n4 - 1 & n;
            TreeBin treeBin = ConcurrentHashMapV8.tabAt(nodeArray, n5);
            if (treeBin == null) break;
            int n6 = treeBin.hash;
            if (n6 == -1) {
                nodeArray = this.helpTransfer(nodeArray, treeBin);
                continue;
            }
            TreeBin treeBin2 = treeBin;
            synchronized (treeBin2) {
                if (ConcurrentHashMapV8.tabAt(nodeArray, n5) == treeBin) {
                    Object object;
                    Node node;
                    Node node2;
                    if (n6 >= 0) {
                        n3 = 1;
                        node2 = treeBin;
                        node = null;
                        while (true) {
                            if (node2.hash == n && ((object = node2.key) == k || object != null && k.equals(object))) {
                                v = biFun.apply(k, node2.val);
                                if (v != null) {
                                    node2.val = v;
                                } else {
                                    n2 = -1;
                                    Node node3 = node2.next;
                                    if (node != null) {
                                        node.next = node3;
                                    } else {
                                        ConcurrentHashMapV8.setTabAt(nodeArray, n5, node3);
                                    }
                                }
                            } else {
                                node = node2;
                                node2 = node2.next;
                                if (node2 != null) {
                                    ++n3;
                                    continue;
                                }
                            }
                            break;
                        }
                    } else if (treeBin instanceof TreeBin) {
                        n3 = 2;
                        node2 = treeBin;
                        node = ((TreeBin)node2).root;
                        if (node != null && (object = ((TreeNode)node).findTreeNode(n, k, null)) != null) {
                            v = biFun.apply(k, ((TreeNode)object).val);
                            if (v != null) {
                                ((TreeNode)object).val = v;
                            } else {
                                n2 = -1;
                                if (((TreeBin)node2).removeTreeNode(object)) {
                                    ConcurrentHashMapV8.setTabAt(nodeArray, n5, ConcurrentHashMapV8.untreeify(((TreeBin)node2).first));
                                }
                            }
                        }
                    }
                }
                if (n3 != 0) break;
            }
        }
        if (n2 != 0) {
            this.addCount(n2, n3);
        }
        return v;
    }

    @Override
    public V put(K k, V v) {
        return this.putVal(k, v, false);
    }

    @Override
    public int size() {
        long l = this.sumCount();
        return l < 0L ? 0 : (l > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)l);
    }

    public double reduceEntriesToDouble(long l, ObjectToDouble<Map.Entry<K, V>> objectToDouble, double d, DoubleByDoubleToDouble doubleByDoubleToDouble) {
        if (objectToDouble == null || doubleByDoubleToDouble == null) {
            throw new NullPointerException();
        }
        return (Double)new MapReduceEntriesToDoubleTask<K, V>(null, this.batchFor(l), 0, 0, this.table, null, objectToDouble, d, doubleByDoubleToDouble).invoke();
    }

    final V replaceNode(Object object, V v, Object object2) {
        int n;
        TreeBin treeBin;
        int n2;
        int n3 = ConcurrentHashMapV8.spread(object.hashCode());
        Node<K, V>[] nodeArray = this.table;
        while (nodeArray != null && (n2 = nodeArray.length) != 0 && (treeBin = ConcurrentHashMapV8.tabAt(nodeArray, n = n2 - 1 & n3)) != null) {
            int n4 = treeBin.hash;
            if (n4 == -1) {
                nodeArray = this.helpTransfer(nodeArray, treeBin);
                continue;
            }
            V v2 = null;
            boolean bl = false;
            TreeBin treeBin2 = treeBin;
            synchronized (treeBin2) {
                if (ConcurrentHashMapV8.tabAt(nodeArray, n) == treeBin) {
                    Object object3;
                    Object object4;
                    Node node;
                    Node node2;
                    if (n4 >= 0) {
                        bl = true;
                        node2 = treeBin;
                        node = null;
                        do {
                            if (node2.hash == n3 && ((object4 = node2.key) == object || object4 != null && object.equals(object4))) {
                                object3 = node2.val;
                                if (object2 == null || object2 == object3 || object3 != null && object2.equals(object3)) {
                                    v2 = object3;
                                    if (v != null) {
                                        node2.val = v;
                                    } else if (node != null) {
                                        node.next = node2.next;
                                    } else {
                                        ConcurrentHashMapV8.setTabAt(nodeArray, n, node2.next);
                                    }
                                }
                                break;
                            }
                            node = node2;
                        } while ((node2 = node2.next) != null);
                    } else if (treeBin instanceof TreeBin) {
                        bl = true;
                        node2 = treeBin;
                        node = ((TreeBin)node2).root;
                        if (node != null && (object4 = ((TreeNode)node).findTreeNode(n3, object, null)) != null) {
                            object3 = ((TreeNode)object4).val;
                            if (object2 == null || object2 == object3 || object3 != null && object2.equals(object3)) {
                                v2 = object3;
                                if (v != null) {
                                    ((TreeNode)object4).val = v;
                                } else if (((TreeBin)node2).removeTreeNode(object4)) {
                                    ConcurrentHashMapV8.setTabAt(nodeArray, n, ConcurrentHashMapV8.untreeify(((TreeBin)node2).first));
                                }
                            }
                        }
                    }
                }
                if (!bl) continue;
            }
            if (v2 == null) break;
            if (v == null) {
                this.addCount(-1L, -1);
            }
            return v2;
        }
        return null;
    }

    static Class<?> comparableClassFor(Object object) {
        if (object instanceof Comparable) {
            Class<?> clazz = object.getClass();
            if (clazz == String.class) {
                return clazz;
            }
            Type[] typeArray = clazz.getGenericInterfaces();
            if (typeArray != null) {
                for (int i = 0; i < typeArray.length; ++i) {
                    Type[] typeArray2;
                    ParameterizedType parameterizedType;
                    Type type = typeArray[i];
                    if (!(type instanceof ParameterizedType) || (parameterizedType = (ParameterizedType)type).getRawType() != Comparable.class || (typeArray2 = parameterizedType.getActualTypeArguments()) == null || typeArray2.length != 1 || typeArray2[0] != clazz) continue;
                    return clazz;
                }
            }
        }
        return null;
    }

    public int reduceEntriesToInt(long l, ObjectToInt<Map.Entry<K, V>> objectToInt, int n, IntByIntToInt intByIntToInt) {
        if (objectToInt == null || intByIntToInt == null) {
            throw new NullPointerException();
        }
        return (Integer)new MapReduceEntriesToIntTask<K, V>(null, this.batchFor(l), 0, 0, this.table, null, objectToInt, n, intByIntToInt).invoke();
    }

    public Enumeration<K> keys() {
        Node<K, V>[] nodeArray = this.table;
        int n = this.table == null ? 0 : nodeArray.length;
        return new KeyIterator<K, V>(nodeArray, n, 0, n, this);
    }

    @Override
    public V remove(Object object) {
        return this.replaceNode(object, null, null);
    }

    private final void transfer(Node<K, V>[] nodeArray, Node<K, V>[] nodeArray2) {
        int n;
        int n2;
        int n3 = nodeArray.length;
        int n4 = NCPU > 1 ? (n3 >>> 3) / NCPU : n3;
        if (n4 < 16) {
            n4 = 16;
        }
        if (nodeArray2 == null) {
            Object object;
            try {
                object = new Node[n3 << 1];
                nodeArray2 = object;
            }
            catch (Throwable throwable) {
                this.sizeCtl = Integer.MAX_VALUE;
                return;
            }
            this.nextTable = nodeArray2;
            this.transferOrigin = n3;
            this.transferIndex = n3;
            object = new ForwardingNode<K, V>(nodeArray);
            int n5 = n3;
            while (n5 > 0) {
                for (n2 = n = n5 > n4 ? n5 - n4 : 0; n2 < n5; ++n2) {
                    nodeArray2[n2] = object;
                }
                for (n2 = n3 + n; n2 < n3 + n5; ++n2) {
                    nodeArray2[n2] = object;
                }
                n5 = n;
                U.putOrderedInt(this, TRANSFERORIGIN, n5);
            }
        }
        int n6 = nodeArray2.length;
        ForwardingNode<K, V> forwardingNode = new ForwardingNode<K, V>(nodeArray2);
        n = 1;
        n2 = 0;
        int n7 = 0;
        int n8 = 0;
        while (true) {
            if (n != 0) {
                if (--n7 >= n8 || n2 != 0) {
                    n = 0;
                    continue;
                }
                int n9 = this.transferIndex;
                if (n9 <= this.transferOrigin) {
                    n7 = -1;
                    n = 0;
                    continue;
                }
                int n10 = n9 > n4 ? n9 - n4 : 0;
                if (!U.compareAndSwapInt(this, TRANSFERINDEX, n9, n10)) continue;
                n8 = n10;
                n7 = n9 - 1;
                n = 0;
                continue;
            }
            if (n7 < 0 || n7 >= n3 || n7 + n3 >= n6) {
                int n11;
                if (n2 != 0) {
                    this.nextTable = null;
                    this.table = nodeArray2;
                    this.sizeCtl = (n3 << 1) - (n3 >>> 1);
                    return;
                }
                do {
                    n11 = this.sizeCtl;
                } while (!U.compareAndSwapInt(this, SIZECTL, n11, ++n11));
                if (n11 != -1) {
                    return;
                }
                n = 1;
                n2 = 1;
                n7 = n3;
                continue;
            }
            TreeBin treeBin = ConcurrentHashMapV8.tabAt(nodeArray, n7);
            if (treeBin == null) {
                if (!ConcurrentHashMapV8.casTabAt(nodeArray, n7, null, forwardingNode)) continue;
                ConcurrentHashMapV8.setTabAt(nodeArray2, n7, null);
                ConcurrentHashMapV8.setTabAt(nodeArray2, n7 + n3, null);
                n = 1;
                continue;
            }
            int n12 = treeBin.hash;
            if (n12 == -1) {
                n = 1;
                continue;
            }
            TreeBin treeBin2 = treeBin;
            synchronized (treeBin2) {
                if (ConcurrentHashMapV8.tabAt(nodeArray, n7) == treeBin) {
                    TreeNode treeNode;
                    Node node;
                    Node node2;
                    Node node3;
                    Node node4;
                    if (n12 >= 0) {
                        int n13;
                        int n14 = n12 & n3;
                        node4 = treeBin;
                        node3 = treeBin.next;
                        while (node3 != null) {
                            n13 = node3.hash & n3;
                            if (n13 != n14) {
                                n14 = n13;
                                node4 = node3;
                            }
                            node3 = node3.next;
                        }
                        if (n14 == 0) {
                            node2 = node4;
                            node = null;
                        } else {
                            node = node4;
                            node2 = null;
                        }
                        node3 = treeBin;
                        while (node3 != node4) {
                            n13 = node3.hash;
                            treeNode = (TreeNode)node3.key;
                            Object v = node3.val;
                            if ((n13 & n3) == 0) {
                                node2 = new Node(n13, treeNode, v, node2);
                            } else {
                                node = new Node(n13, treeNode, v, node);
                            }
                            node3 = node3.next;
                        }
                        ConcurrentHashMapV8.setTabAt(nodeArray2, n7, node2);
                        ConcurrentHashMapV8.setTabAt(nodeArray2, n7 + n3, node);
                        ConcurrentHashMapV8.setTabAt(nodeArray, n7, forwardingNode);
                        n = 1;
                    } else if (treeBin instanceof TreeBin) {
                        TreeBin treeBin3 = treeBin;
                        node4 = null;
                        node3 = null;
                        TreeNode treeNode2 = null;
                        treeNode = null;
                        int n15 = 0;
                        int n16 = 0;
                        Node node5 = treeBin3.first;
                        while (node5 != null) {
                            int n17 = node5.hash;
                            TreeNode treeNode3 = new TreeNode(n17, node5.key, node5.val, null, null);
                            if ((n17 & n3) == 0) {
                                treeNode3.prev = node3;
                                if (treeNode3.prev == null) {
                                    node4 = treeNode3;
                                } else {
                                    ((TreeNode)node3).next = treeNode3;
                                }
                                node3 = treeNode3;
                                ++n15;
                            } else {
                                treeNode3.prev = treeNode;
                                if (treeNode3.prev == null) {
                                    treeNode2 = treeNode3;
                                } else {
                                    treeNode.next = treeNode3;
                                }
                                treeNode = treeNode3;
                                ++n16;
                            }
                            node5 = node5.next;
                        }
                        TreeBin treeBin4 = n15 <= 6 ? ConcurrentHashMapV8.untreeify(node4) : (node2 = n16 != 0 ? new TreeBin(node4) : treeBin3);
                        node = n16 <= 6 ? ConcurrentHashMapV8.untreeify(treeNode2) : (n15 != 0 ? new TreeBin(treeNode2) : treeBin3);
                        ConcurrentHashMapV8.setTabAt(nodeArray2, n7, node2);
                        ConcurrentHashMapV8.setTabAt(nodeArray2, n7 + n3, node);
                        ConcurrentHashMapV8.setTabAt(nodeArray, n7, forwardingNode);
                        n = 1;
                    }
                }
            }
        }
    }

    private final void tryPresize(int n) {
        int n2;
        int n3;
        int n4 = n3 = n >= 0x20000000 ? 0x40000000 : ConcurrentHashMapV8.tableSizeFor(n + (n >>> 1) + 1);
        while ((n2 = this.sizeCtl) >= 0) {
            int n5;
            Node<K, V>[] nodeArray = this.table;
            if (nodeArray == null || (n5 = nodeArray.length) == 0) {
                int n6 = n5 = n2 > n3 ? n2 : n3;
                if (!U.compareAndSwapInt(this, SIZECTL, n2, -1)) continue;
                if (this.table == nodeArray) {
                    Node[] nodeArray2 = new Node[n5];
                    this.table = nodeArray2;
                    n2 = n5 - (n5 >>> 2);
                }
                this.sizeCtl = n2;
                continue;
            }
            if (n3 <= n2 || n5 >= 0x40000000) break;
            if (nodeArray != this.table || !U.compareAndSwapInt(this, SIZECTL, n2, -2)) continue;
            this.transfer(nodeArray, null);
        }
    }

    @Override
    public V replace(K k, V v) {
        if (k == null || v == null) {
            throw new NullPointerException();
        }
        return this.replaceNode(k, v, null);
    }

    public <U> U reduceEntries(long l, Fun<Map.Entry<K, V>, ? extends U> fun, BiFun<? super U, ? super U, ? extends U> biFun) {
        if (fun == null || biFun == null) {
            throw new NullPointerException();
        }
        return (U)new MapReduceEntriesTask<K, V, U>(null, this.batchFor(l), 0, 0, this.table, null, fun, biFun).invoke();
    }

    public static <K> KeySetView<K, Boolean> newKeySet(int n) {
        return new KeySetView<K, Boolean>(new ConcurrentHashMapV8(n), Boolean.TRUE);
    }

    public void forEachEntry(long l, Action<? super Map.Entry<K, V>> action) {
        if (action == null) {
            throw new NullPointerException();
        }
        new ForEachEntryTask<K, V>(null, this.batchFor(l), 0, 0, this.table, action).invoke();
    }

    public ConcurrentHashMapV8(Map<? extends K, ? extends V> map) {
        this.sizeCtl = 16;
        this.putAll(map);
    }

    final long sumCount() {
        CounterCell[] counterCellArray = this.counterCells;
        long l = this.baseCount;
        if (counterCellArray != null) {
            for (int i = 0; i < counterCellArray.length; ++i) {
                CounterCell counterCell = counterCellArray[i];
                if (counterCell == null) continue;
                l += counterCell.value;
            }
        }
        return l;
    }

    public <U> U searchValues(long l, Fun<? super V, ? extends U> fun) {
        if (fun == null) {
            throw new NullPointerException();
        }
        return (U)new SearchValuesTask<K, V, U>(null, this.batchFor(l), 0, 0, this.table, fun, new AtomicReference()).invoke();
    }

    @Override
    public void replaceAll(BiFun<? super K, ? super V, ? extends V> biFun) {
        if (biFun == null) {
            throw new NullPointerException();
        }
        Node<K, V>[] nodeArray = this.table;
        if (this.table != null) {
            Node<K, V> node;
            Traverser<K, V> traverser = new Traverser<K, V>(nodeArray, nodeArray.length, 0, nodeArray.length);
            while ((node = traverser.advance()) != null) {
                V v;
                Object v2 = node.val;
                Object k = node.key;
                do {
                    if ((v = biFun.apply(k, v2)) != null) continue;
                    throw new NullPointerException();
                } while (this.replaceNode(k, v, v2) == null && (v2 = this.get(k)) != null);
            }
        }
    }

    public double reduceKeysToDouble(long l, ObjectToDouble<? super K> objectToDouble, double d, DoubleByDoubleToDouble doubleByDoubleToDouble) {
        if (objectToDouble == null || doubleByDoubleToDouble == null) {
            throw new NullPointerException();
        }
        return (Double)new MapReduceKeysToDoubleTask<K, V>(null, this.batchFor(l), 0, 0, this.table, null, objectToDouble, d, doubleByDoubleToDouble).invoke();
    }

    public Map.Entry<K, V> reduceEntries(long l, BiFun<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> biFun) {
        if (biFun == null) {
            throw new NullPointerException();
        }
        return (Map.Entry)new ReduceEntriesTask<K, V>(null, this.batchFor(l), 0, 0, this.table, null, biFun).invoke();
    }

    public int reduceKeysToInt(long l, ObjectToInt<? super K> objectToInt, int n, IntByIntToInt intByIntToInt) {
        if (objectToInt == null || intByIntToInt == null) {
            throw new NullPointerException();
        }
        return (Integer)new MapReduceKeysToIntTask<K, V>(null, this.batchFor(l), 0, 0, this.table, null, objectToInt, n, intByIntToInt).invoke();
    }

    public KeySetView<K, V> keySet() {
        KeySetView<K, V> keySetView = this.keySet;
        return keySetView != null ? keySetView : (this.keySet = new KeySetView(this, null));
    }

    public <U> U reduceKeys(long l, Fun<? super K, ? extends U> fun, BiFun<? super U, ? super U, ? extends U> biFun) {
        if (fun == null || biFun == null) {
            throw new NullPointerException();
        }
        return (U)new MapReduceKeysTask<K, V, U>(null, this.batchFor(l), 0, 0, this.table, null, fun, biFun).invoke();
    }

    @Override
    public boolean containsValue(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
        Node<K, V>[] nodeArray = this.table;
        if (this.table != null) {
            Node<K, V> node;
            Traverser<K, V> traverser = new Traverser<K, V>(nodeArray, nodeArray.length, 0, nodeArray.length);
            while ((node = traverser.advance()) != null) {
                Object v = node.val;
                if (v != object && (v == null || !object.equals(v))) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<V> values() {
        ValuesView<K, V> valuesView = this.values;
        return valuesView != null ? valuesView : (this.values = new ValuesView(this));
    }

    public ConcurrentHashMapV8(int n, float f) {
        this(n, f, 1);
    }

    public void forEachValue(long l, Action<? super V> action) {
        if (action == null) {
            throw new NullPointerException();
        }
        new ForEachValueTask<K, V>(null, this.batchFor(l), 0, 0, this.table, action).invoke();
    }

    private final Node<K, V>[] initTable() {
        Node<K, V>[] nodeArray;
        block3: {
            int n;
            while (true) {
                nodeArray = this.table;
                if (this.table != null && nodeArray.length != 0) break block3;
                n = this.sizeCtl;
                if (n < 0) {
                    Thread.yield();
                    continue;
                }
                if (U.compareAndSwapInt(this, SIZECTL, n, -1)) break;
            }
            nodeArray = this.table;
            if (this.table == null || nodeArray.length == 0) {
                int n2 = n > 0 ? n : 16;
                Node[] nodeArray2 = new Node[n2];
                nodeArray = nodeArray2;
                this.table = nodeArray2;
                n = n2 - (n2 >>> 2);
            }
            this.sizeCtl = n;
        }
        return nodeArray;
    }

    public int reduceToInt(long l, ObjectByObjectToInt<? super K, ? super V> objectByObjectToInt, int n, IntByIntToInt intByIntToInt) {
        if (objectByObjectToInt == null || intByIntToInt == null) {
            throw new NullPointerException();
        }
        return (Integer)new MapReduceMappingsToIntTask<K, V>(null, this.batchFor(l), 0, 0, this.table, null, objectByObjectToInt, n, intByIntToInt).invoke();
    }

    final int batchFor(long l) {
        long l2;
        if (l == Long.MAX_VALUE || (l2 = this.sumCount()) <= 1L || l2 < l) {
            return 0;
        }
        int n = ForkJoinPool.getCommonPoolParallelism() << 2;
        return l <= 0L || (l2 /= l) >= (long)n ? n : (int)l2;
    }

    public ConcurrentHashMapV8(int n, float f, int n2) {
        long l;
        int n3;
        if (!(f > 0.0f) || n < 0 || n2 <= 0) {
            throw new IllegalArgumentException();
        }
        if (n < n2) {
            n = n2;
        }
        this.sizeCtl = n3 = (l = (long)(1.0 + (double)((float)n / f))) >= 0x40000000L ? 0x40000000 : ConcurrentHashMapV8.tableSizeFor((int)l);
    }

    @Override
    public int hashCode() {
        int n = 0;
        Node<K, V>[] nodeArray = this.table;
        if (this.table != null) {
            Node<K, V> node;
            Traverser<K, V> traverser = new Traverser<K, V>(nodeArray, nodeArray.length, 0, nodeArray.length);
            while ((node = traverser.advance()) != null) {
                n += node.key.hashCode() ^ node.val.hashCode();
            }
        }
        return n;
    }

    final V putVal(K k, V v, boolean bl) {
        int n;
        block17: {
            V v2;
            int n2;
            if (k == null || v == null) {
                throw new NullPointerException();
            }
            int n3 = ConcurrentHashMapV8.spread(k.hashCode());
            n = 0;
            Node<K, V>[] nodeArray = this.table;
            while (true) {
                int n4;
                if (nodeArray == null || (n4 = nodeArray.length) == 0) {
                    nodeArray = this.initTable();
                    continue;
                }
                n2 = n4 - 1 & n3;
                Node<K, V> node = ConcurrentHashMapV8.tabAt(nodeArray, n2);
                if (node == null) {
                    if (!ConcurrentHashMapV8.casTabAt(nodeArray, n2, null, new Node<K, V>(n3, k, v, null))) continue;
                    break block17;
                }
                int n5 = node.hash;
                if (n5 == -1) {
                    nodeArray = this.helpTransfer(nodeArray, node);
                    continue;
                }
                v2 = null;
                Node<K, V> node2 = node;
                synchronized (node2) {
                    block18: {
                        if (ConcurrentHashMapV8.tabAt(nodeArray, n2) == node) {
                            Node<K, V> node3;
                            if (n5 >= 0) {
                                n = 1;
                                node3 = node;
                                while (true) {
                                    Object k2;
                                    if (node3.hash == n3 && ((k2 = node3.key) == k || k2 != null && k.equals(k2))) {
                                        v2 = node3.val;
                                        if (!bl) {
                                            node3.val = v;
                                        }
                                        break block18;
                                    }
                                    Node<K, V> node4 = node3;
                                    node3 = node3.next;
                                    if (node3 == null) {
                                        node4.next = new Node<K, V>(n3, k, v, null);
                                        break block18;
                                    }
                                    ++n;
                                }
                            }
                            if (node instanceof TreeBin) {
                                n = 2;
                                node3 = ((TreeBin)node).putTreeVal(n3, k, v);
                                if (node3 != null) {
                                    v2 = node3.val;
                                    if (!bl) {
                                        node3.val = v;
                                    }
                                }
                            }
                        }
                    }
                    if (n != 0) break;
                }
            }
            if (n >= 8) {
                this.treeifyBin(nodeArray, n2);
            }
            if (v2 != null) {
                return v2;
            }
        }
        this.addCount(1L, n);
        return null;
    }

    static final int spread(int n) {
        return (n ^ n >>> 16) & Integer.MAX_VALUE;
    }

    @Override
    public void clear() {
        long l = 0L;
        int n = 0;
        Node<K, V>[] nodeArray = this.table;
        while (nodeArray != null && n < nodeArray.length) {
            Node<K, V> node = ConcurrentHashMapV8.tabAt(nodeArray, n);
            if (node == null) {
                ++n;
                continue;
            }
            int n2 = node.hash;
            if (n2 == -1) {
                nodeArray = this.helpTransfer(nodeArray, node);
                n = 0;
                continue;
            }
            Node<K, V> node2 = node;
            synchronized (node2) {
                if (ConcurrentHashMapV8.tabAt(nodeArray, n) == node) {
                    Node<K, V> node3;
                    Node<K, V> node4 = n2 >= 0 ? node : (node3 = node instanceof TreeBin ? ((TreeBin)node).first : null);
                    while (node3 != null) {
                        --l;
                        node3 = node3.next;
                    }
                    ConcurrentHashMapV8.setTabAt(nodeArray, n++, null);
                }
            }
        }
        if (l != 0L) {
            this.addCount(l, -1);
        }
    }

    private final void treeifyBin(Node<K, V>[] nodeArray, int n) {
        block5: {
            block6: {
                int n2;
                if (nodeArray == null) break block5;
                int n3 = nodeArray.length;
                if (n3 >= 64) break block6;
                if (nodeArray != this.table || (n2 = this.sizeCtl) < 0 || !U.compareAndSwapInt(this, SIZECTL, n2, -2)) break block5;
                this.transfer(nodeArray, null);
                break block5;
            }
            Node<K, V> node = ConcurrentHashMapV8.tabAt(nodeArray, n);
            if (node == null || node.hash < 0) break block5;
            Node<K, V> node2 = node;
            synchronized (node2) {
                if (ConcurrentHashMapV8.tabAt(nodeArray, n) == node) {
                    TreeNode treeNode = null;
                    TreeNode treeNode2 = null;
                    Node<K, V> node3 = node;
                    while (node3 != null) {
                        TreeNode treeNode3 = new TreeNode(node3.hash, node3.key, node3.val, null, null);
                        treeNode3.prev = treeNode2;
                        if (treeNode3.prev == null) {
                            treeNode = treeNode3;
                        } else {
                            treeNode2.next = treeNode3;
                        }
                        treeNode2 = treeNode3;
                        node3 = node3.next;
                    }
                    ConcurrentHashMapV8.setTabAt(nodeArray, n, new TreeBin(treeNode));
                }
            }
        }
    }

    public ConcurrentHashMapV8(int n) {
        int n2;
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        this.sizeCtl = n2 = n >= 0x20000000 ? 0x40000000 : ConcurrentHashMapV8.tableSizeFor(n + (n >>> 1) + 1);
    }

    static final <K, V> void setTabAt(Node<K, V>[] nodeArray, int n, Node<K, V> node) {
        U.putObjectVolatile(nodeArray, ((long)n << ASHIFT) + ABASE, node);
    }

    public <U> void forEachValue(long l, Fun<? super V, ? extends U> fun, Action<? super U> action) {
        if (fun == null || action == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedValueTask<K, V, U>(null, this.batchFor(l), 0, 0, this.table, fun, action).invoke();
    }

    public long reduceToLong(long l, ObjectByObjectToLong<? super K, ? super V> objectByObjectToLong, long l2, LongByLongToLong longByLongToLong) {
        if (objectByObjectToLong == null || longByLongToLong == null) {
            throw new NullPointerException();
        }
        return (Long)new MapReduceMappingsToLongTask<K, V>(null, this.batchFor(l), 0, 0, this.table, null, objectByObjectToLong, l2, longByLongToLong).invoke();
    }

    @Override
    public V merge(K k, V v, BiFun<? super V, ? super V, ? extends V> biFun) {
        int n;
        int n2;
        Object v2;
        block24: {
            int n3;
            if (k == null || v == null || biFun == null) {
                throw new NullPointerException();
            }
            int n4 = ConcurrentHashMapV8.spread(k.hashCode());
            v2 = null;
            n2 = 0;
            n = 0;
            Node<K, V>[] nodeArray = this.table;
            while (true) {
                int n5;
                if (nodeArray == null || (n5 = nodeArray.length) == 0) {
                    nodeArray = this.initTable();
                    continue;
                }
                n3 = n5 - 1 & n4;
                TreeBin treeBin = ConcurrentHashMapV8.tabAt(nodeArray, n3);
                if (treeBin == null) {
                    if (!ConcurrentHashMapV8.casTabAt(nodeArray, n3, null, new Node<K, V>(n4, k, v, null))) continue;
                    n2 = 1;
                    v2 = v;
                    break block24;
                }
                int n6 = treeBin.hash;
                if (n6 == -1) {
                    nodeArray = this.helpTransfer(nodeArray, treeBin);
                    continue;
                }
                TreeBin treeBin2 = treeBin;
                synchronized (treeBin2) {
                    block25: {
                        if (ConcurrentHashMapV8.tabAt(nodeArray, n3) == treeBin) {
                            Object object;
                            Node node;
                            Node node2;
                            if (n6 >= 0) {
                                n = 1;
                                node2 = treeBin;
                                node = null;
                                while (true) {
                                    if (node2.hash == n4 && ((object = node2.key) == k || object != null && k.equals(object))) {
                                        v2 = biFun.apply(node2.val, v);
                                        if (v2 != null) {
                                            node2.val = v2;
                                        } else {
                                            n2 = -1;
                                            Node node3 = node2.next;
                                            if (node != null) {
                                                node.next = node3;
                                            } else {
                                                ConcurrentHashMapV8.setTabAt(nodeArray, n3, node3);
                                            }
                                        }
                                        break block25;
                                    }
                                    node = node2;
                                    node2 = node2.next;
                                    if (node2 == null) {
                                        n2 = 1;
                                        v2 = v;
                                        node.next = new Node<K, Object>(n4, k, v2, null);
                                        break block25;
                                    }
                                    ++n;
                                }
                            }
                            if (treeBin instanceof TreeBin) {
                                n = 2;
                                node2 = treeBin;
                                node = ((TreeBin)node2).root;
                                object = node == null ? null : ((TreeNode)node).findTreeNode(n4, k, null);
                                v2 = object == null ? v : biFun.apply(((TreeNode)object).val, v);
                                if (v2 != null) {
                                    if (object != null) {
                                        ((TreeNode)object).val = v2;
                                    } else {
                                        n2 = 1;
                                        ((TreeBin)node2).putTreeVal(n4, k, v2);
                                    }
                                } else if (object != null) {
                                    n2 = -1;
                                    if (((TreeBin)node2).removeTreeNode(object)) {
                                        ConcurrentHashMapV8.setTabAt(nodeArray, n3, ConcurrentHashMapV8.untreeify(((TreeBin)node2).first));
                                    }
                                }
                            }
                        }
                    }
                    if (n != 0) break;
                }
            }
            if (n >= 8) {
                this.treeifyBin(nodeArray, n3);
            }
        }
        if (n2 != 0) {
            this.addCount(n2, n);
        }
        return v2;
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        Node[] nodeArray;
        this.sizeCtl = -1;
        objectInputStream.defaultReadObject();
        long l = 0L;
        Node<Object, Object> node = null;
        while (true) {
            Object object = objectInputStream.readObject();
            nodeArray = objectInputStream.readObject();
            if (object == null || nodeArray == null) break;
            node = new Node<Object, Object>(ConcurrentHashMapV8.spread(object.hashCode()), object, nodeArray, node);
            ++l;
        }
        if (l == 0L) {
            this.sizeCtl = 0;
        } else {
            int n;
            if (l >= 0x20000000L) {
                n = 0x40000000;
            } else {
                int n2 = (int)l;
                n = ConcurrentHashMapV8.tableSizeFor(n2 + (n2 >>> 1) + 1);
            }
            nodeArray = new Node[n];
            int n3 = n - 1;
            long l2 = 0L;
            while (node != null) {
                boolean bl;
                Node node2 = node.next;
                int n4 = node.hash;
                int n5 = n4 & n3;
                Node<K, V> node3 = ConcurrentHashMapV8.tabAt(nodeArray, n5);
                if (node3 == null) {
                    bl = true;
                } else {
                    Object k = node.key;
                    if (node3.hash < 0) {
                        TreeBin treeBin = (TreeBin)node3;
                        if (treeBin.putTreeVal(n4, k, node.val) == null) {
                            ++l2;
                        }
                        bl = false;
                    } else {
                        int n6 = 0;
                        bl = true;
                        Node<Object, Object> node4 = node3;
                        while (node4 != null) {
                            Object k2;
                            if (node4.hash == n4 && ((k2 = node4.key) == k || k2 != null && k.equals(k2))) {
                                bl = false;
                                break;
                            }
                            ++n6;
                            node4 = node4.next;
                        }
                        if (bl && n6 >= 8) {
                            bl = false;
                            ++l2;
                            node.next = node3;
                            TreeNode treeNode = null;
                            TreeNode treeNode2 = null;
                            node4 = node;
                            while (node4 != null) {
                                TreeNode treeNode3 = new TreeNode(node4.hash, node4.key, node4.val, null, null);
                                treeNode3.prev = treeNode2;
                                if (treeNode3.prev == null) {
                                    treeNode = treeNode3;
                                } else {
                                    treeNode2.next = treeNode3;
                                }
                                treeNode2 = treeNode3;
                                node4 = node4.next;
                            }
                            ConcurrentHashMapV8.setTabAt(nodeArray, n5, new TreeBin(treeNode));
                        }
                    }
                }
                if (bl) {
                    ++l2;
                    node.next = node3;
                    ConcurrentHashMapV8.setTabAt(nodeArray, n5, node);
                }
                node = node2;
            }
            this.table = nodeArray;
            this.sizeCtl = n - (n >>> 2);
            this.baseCount = l2;
        }
    }

    public KeySetView<K, V> keySet(V v) {
        if (v == null) {
            throw new NullPointerException();
        }
        return new KeySetView(this, v);
    }

    private static Unsafe getUnsafe() {
        try {
            return Unsafe.getUnsafe();
        }
        catch (SecurityException securityException) {
            try {
                return AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>(){

                    @Override
                    public Unsafe run() throws Exception {
                        Class<Unsafe> clazz = Unsafe.class;
                        for (Field field : clazz.getDeclaredFields()) {
                            field.setAccessible(true);
                            Object object = field.get(null);
                            if (!clazz.isInstance(object)) continue;
                            return (Unsafe)clazz.cast(object);
                        }
                        throw new NoSuchFieldError("the Unsafe");
                    }
                });
            }
            catch (PrivilegedActionException privilegedActionException) {
                throw new RuntimeException("Could not initialize intrinsics", privilegedActionException.getCause());
            }
        }
    }

    public long reduceValuesToLong(long l, ObjectToLong<? super V> objectToLong, long l2, LongByLongToLong longByLongToLong) {
        if (objectToLong == null || longByLongToLong == null) {
            throw new NullPointerException();
        }
        return (Long)new MapReduceValuesToLongTask<K, V>(null, this.batchFor(l), 0, 0, this.table, null, objectToLong, l2, longByLongToLong).invoke();
    }

    public Enumeration<V> elements() {
        Node<K, V>[] nodeArray = this.table;
        int n = this.table == null ? 0 : nodeArray.length;
        return new ValueIterator<K, V>(nodeArray, n, 0, n, this);
    }

    public int reduceValuesToInt(long l, ObjectToInt<? super V> objectToInt, int n, IntByIntToInt intByIntToInt) {
        if (objectToInt == null || intByIntToInt == null) {
            throw new NullPointerException();
        }
        return (Integer)new MapReduceValuesToIntTask<K, V>(null, this.batchFor(l), 0, 0, this.table, null, objectToInt, n, intByIntToInt).invoke();
    }

    @Override
    public V computeIfAbsent(K k, Fun<? super K, ? extends V> fun) {
        int n;
        Object object;
        block22: {
            boolean bl;
            int n2;
            if (k == null || fun == null) {
                throw new NullPointerException();
            }
            int n3 = ConcurrentHashMapV8.spread(k.hashCode());
            object = null;
            n = 0;
            Node<K, V>[] nodeArray = this.table;
            while (true) {
                Node node;
                Node node2;
                int n4;
                if (nodeArray == null || (n4 = nodeArray.length) == 0) {
                    nodeArray = this.initTable();
                    continue;
                }
                n2 = n4 - 1 & n3;
                TreeBin treeBin = ConcurrentHashMapV8.tabAt(nodeArray, n2);
                if (treeBin == null) {
                    ReservationNode reservationNode;
                    node2 = reservationNode = new ReservationNode();
                    synchronized (node2) {
                        if (ConcurrentHashMapV8.casTabAt(nodeArray, n2, null, reservationNode)) {
                            n = 1;
                            node = null;
                            V v = fun.apply(k);
                            object = v;
                            if (v != null) {
                                node = new Node<K, Object>(n3, k, object, null);
                            }
                            ConcurrentHashMapV8.setTabAt(nodeArray, n2, node);
                        }
                        if (n == 0) continue;
                    }
                    break block22;
                }
                int n5 = treeBin.hash;
                if (n5 == -1) {
                    nodeArray = this.helpTransfer(nodeArray, treeBin);
                    continue;
                }
                bl = false;
                node2 = treeBin;
                synchronized (node2) {
                    block23: {
                        if (ConcurrentHashMapV8.tabAt(nodeArray, n2) == treeBin) {
                            Object object2;
                            if (n5 >= 0) {
                                n = 1;
                                node = treeBin;
                                while (true) {
                                    if (node.hash == n3 && ((object2 = node.key) == k || object2 != null && k.equals(object2))) {
                                        object = node.val;
                                        break block23;
                                    }
                                    TreeBin treeBin2 = node;
                                    node = node.next;
                                    if (node == null) {
                                        V v = fun.apply(k);
                                        object = v;
                                        if (v != null) {
                                            bl = true;
                                            treeBin2.next = new Node<K, Object>(n3, k, object, null);
                                        }
                                        break block23;
                                    }
                                    ++n;
                                }
                            }
                            if (treeBin instanceof TreeBin) {
                                TreeNode treeNode;
                                n = 2;
                                node = treeBin;
                                object2 = ((TreeBin)node).root;
                                if (object2 != null && (treeNode = ((TreeNode)object2).findTreeNode(n3, k, null)) != null) {
                                    object = treeNode.val;
                                } else {
                                    V v = fun.apply(k);
                                    object = v;
                                    if (v != null) {
                                        bl = true;
                                        ((TreeBin)node).putTreeVal(n3, k, object);
                                    }
                                }
                            }
                        }
                    }
                    if (n != 0) break;
                }
            }
            if (n >= 8) {
                this.treeifyBin(nodeArray, n2);
            }
            if (!bl) {
                return (V)object;
            }
        }
        if (object != null) {
            this.addCount(1L, n);
        }
        return (V)object;
    }

    public long reduceKeysToLong(long l, ObjectToLong<? super K> objectToLong, long l2, LongByLongToLong longByLongToLong) {
        if (objectToLong == null || longByLongToLong == null) {
            throw new NullPointerException();
        }
        return (Long)new MapReduceKeysToLongTask<K, V>(null, this.batchFor(l), 0, 0, this.table, null, objectToLong, l2, longByLongToLong).invoke();
    }

    @Override
    public void forEach(BiAction<? super K, ? super V> biAction) {
        if (biAction == null) {
            throw new NullPointerException();
        }
        Node<K, V>[] nodeArray = this.table;
        if (this.table != null) {
            Node<K, V> node;
            Traverser<K, V> traverser = new Traverser<K, V>(nodeArray, nodeArray.length, 0, nodeArray.length);
            while ((node = traverser.advance()) != null) {
                biAction.apply(node.key, node.val);
            }
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        this.tryPresize(map.size());
        for (Map.Entry<K, V> entry : map.entrySet()) {
            this.putVal(entry.getKey(), entry.getValue(), false);
        }
    }

    static {
        NCPU = Runtime.getRuntime().availableProcessors();
        serialPersistentFields = new ObjectStreamField[]{new ObjectStreamField("segments", Segment[].class), new ObjectStreamField("segmentMask", Integer.TYPE), new ObjectStreamField("segmentShift", Integer.TYPE)};
        counterHashCodeGenerator = new AtomicInteger();
        try {
            U = ConcurrentHashMapV8.getUnsafe();
            Class<ConcurrentHashMapV8> clazz = ConcurrentHashMapV8.class;
            SIZECTL = U.objectFieldOffset(clazz.getDeclaredField("sizeCtl"));
            TRANSFERINDEX = U.objectFieldOffset(clazz.getDeclaredField("transferIndex"));
            TRANSFERORIGIN = U.objectFieldOffset(clazz.getDeclaredField("transferOrigin"));
            BASECOUNT = U.objectFieldOffset(clazz.getDeclaredField("baseCount"));
            CELLSBUSY = U.objectFieldOffset(clazz.getDeclaredField("cellsBusy"));
            Class<CounterCell> clazz2 = CounterCell.class;
            CELLVALUE = U.objectFieldOffset(clazz2.getDeclaredField("value"));
            Class<Node[]> clazz3 = Node[].class;
            ABASE = U.arrayBaseOffset(clazz3);
            int n = U.arrayIndexScale(clazz3);
            if ((n & n - 1) != 0) {
                throw new Error("data type scale not a power of two");
            }
            ASHIFT = 31 - Integer.numberOfLeadingZeros(n);
        }
        catch (Exception exception) {
            throw new Error(exception);
        }
    }

    public double reduceValuesToDouble(long l, ObjectToDouble<? super V> objectToDouble, double d, DoubleByDoubleToDouble doubleByDoubleToDouble) {
        if (objectToDouble == null || doubleByDoubleToDouble == null) {
            throw new NullPointerException();
        }
        return (Double)new MapReduceValuesToDoubleTask<K, V>(null, this.batchFor(l), 0, 0, this.table, null, objectToDouble, d, doubleByDoubleToDouble).invoke();
    }

    public <U> void forEach(long l, BiFun<? super K, ? super V, ? extends U> biFun, Action<? super U> action) {
        if (biFun == null || action == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedMappingTask<K, V, U>(null, this.batchFor(l), 0, 0, this.table, biFun, action).invoke();
    }

    public static <K> KeySetView<K, Boolean> newKeySet() {
        return new KeySetView<K, Boolean>(new ConcurrentHashMapV8(), Boolean.TRUE);
    }

    public V reduceValues(long l, BiFun<? super V, ? super V, ? extends V> biFun) {
        if (biFun == null) {
            throw new NullPointerException();
        }
        return new ReduceValuesTask<K, V>(null, this.batchFor(l), 0, 0, this.table, null, biFun).invoke();
    }

    public <U> U searchKeys(long l, Fun<? super K, ? extends U> fun) {
        if (fun == null) {
            throw new NullPointerException();
        }
        return (U)new SearchKeysTask<K, V, U>(null, this.batchFor(l), 0, 0, this.table, fun, new AtomicReference()).invoke();
    }

    public double reduceToDouble(long l, ObjectByObjectToDouble<? super K, ? super V> objectByObjectToDouble, double d, DoubleByDoubleToDouble doubleByDoubleToDouble) {
        if (objectByObjectToDouble == null || doubleByDoubleToDouble == null) {
            throw new NullPointerException();
        }
        return (Double)new MapReduceMappingsToDoubleTask<K, V>(null, this.batchFor(l), 0, 0, this.table, null, objectByObjectToDouble, d, doubleByDoubleToDouble).invoke();
    }

    private final void addCount(long l, int n) {
        Node<K, V>[] nodeArray;
        Node<K, V>[] nodeArray2;
        long l2;
        long l3;
        CounterCell[] counterCellArray = this.counterCells;
        if (this.counterCells != null || !U.compareAndSwapLong(this, BASECOUNT, l3 = this.baseCount, l2 = l3 + l)) {
            long l4;
            int n2;
            boolean bl = true;
            InternalThreadLocalMap internalThreadLocalMap = InternalThreadLocalMap.get();
            nodeArray2 = internalThreadLocalMap.counterHashCode();
            if (nodeArray2 == null || counterCellArray == null || (n2 = counterCellArray.length - 1) < 0 || (nodeArray = counterCellArray[n2 & nodeArray2.value]) == null || !(bl = U.compareAndSwapLong(nodeArray, CELLVALUE, l4 = nodeArray.value, l4 + l))) {
                this.fullAddCount(internalThreadLocalMap, l, (IntegerHolder)nodeArray2, bl);
                return;
            }
            if (n <= 1) {
                return;
            }
            l2 = this.sumCount();
        }
        if (n >= 0) {
            int n3;
            while (l2 >= (long)(n3 = this.sizeCtl)) {
                nodeArray2 = this.table;
                if (this.table == null || nodeArray2.length >= 0x40000000) break;
                if (n3 < 0) {
                    if (n3 == -1 || this.transferIndex <= this.transferOrigin) break;
                    nodeArray = this.nextTable;
                    if (this.nextTable == null) break;
                    if (U.compareAndSwapInt(this, SIZECTL, n3, n3 - 1)) {
                        this.transfer(nodeArray2, nodeArray);
                    }
                } else if (U.compareAndSwapInt(this, SIZECTL, n3, -2)) {
                    this.transfer(nodeArray2, null);
                }
                l2 = this.sumCount();
            }
        }
    }

    @Override
    public boolean remove(Object object, Object object2) {
        if (object == null) {
            throw new NullPointerException();
        }
        return object2 != null && this.replaceNode(object, null, object2) != null;
    }

    static final <K, V> Node<K, V> tabAt(Node<K, V>[] nodeArray, int n) {
        return (Node)U.getObjectVolatile(nodeArray, ((long)n << ASHIFT) + ABASE);
    }

    @Override
    public boolean replace(K k, V v, V v2) {
        if (k == null || v == null || v2 == null) {
            throw new NullPointerException();
        }
        return this.replaceNode(k, v2, v) != null;
    }

    public <U> U reduceValues(long l, Fun<? super V, ? extends U> fun, BiFun<? super U, ? super U, ? extends U> biFun) {
        if (fun == null || biFun == null) {
            throw new NullPointerException();
        }
        return (U)new MapReduceValuesTask<K, V, U>(null, this.batchFor(l), 0, 0, this.table, null, fun, biFun).invoke();
    }

    public String toString() {
        Node<K, V>[] nodeArray = this.table;
        int n = this.table == null ? 0 : nodeArray.length;
        Traverser<K, V> traverser = new Traverser<K, V>(nodeArray, n, 0, n);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('{');
        Node<K, V> node = traverser.advance();
        if (node != null) {
            while (true) {
                Object k = node.key;
                Object v = node.val;
                stringBuilder.append((Object)(k == this ? "(this Map)" : k));
                stringBuilder.append('=');
                stringBuilder.append((Object)(v == this ? "(this Map)" : v));
                node = traverser.advance();
                if (node == null) break;
                stringBuilder.append(',').append(' ');
            }
        }
        return stringBuilder.append('}').toString();
    }

    @Override
    public V putIfAbsent(K k, V v) {
        return this.putVal(k, v, true);
    }

    public K reduceKeys(long l, BiFun<? super K, ? super K, ? extends K> biFun) {
        if (biFun == null) {
            throw new NullPointerException();
        }
        return (K)new ReduceKeysTask<K, V>(null, this.batchFor(l), 0, 0, this.table, null, biFun).invoke();
    }

    final Node<K, V>[] helpTransfer(Node<K, V>[] nodeArray, Node<K, V> node) {
        if (node instanceof ForwardingNode) {
            Node<K, V>[] nodeArray2 = ((ForwardingNode)node).nextTable;
            if (((ForwardingNode)node).nextTable != null) {
                int n;
                if (nodeArray2 == this.nextTable && nodeArray == this.table && this.transferIndex > this.transferOrigin && (n = this.sizeCtl) < -1 && U.compareAndSwapInt(this, SIZECTL, n, n - 1)) {
                    this.transfer(nodeArray, nodeArray2);
                }
                return nodeArray2;
            }
        }
        return this.table;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        EntrySetView<K, V> entrySetView = this.entrySet;
        return entrySetView != null ? entrySetView : (this.entrySet = new EntrySetView(this));
    }

    static <K, V> Node<K, V> untreeify(Node<K, V> node) {
        Node node2 = null;
        Node node3 = null;
        Node<K, V> node4 = node;
        while (node4 != null) {
            Node node5 = new Node(node4.hash, node4.key, node4.val, null);
            if (node3 == null) {
                node2 = node5;
            } else {
                node3.next = node5;
            }
            node3 = node5;
            node4 = node4.next;
        }
        return node2;
    }

    @Override
    public boolean equals(Object object) {
        if (object != this) {
            Object object2;
            Object object3;
            if (!(object instanceof Map)) {
                return false;
            }
            Map map = (Map)object;
            Node<K, V>[] nodeArray = this.table;
            int n = this.table == null ? 0 : nodeArray.length;
            Traverser<K, V> traverser = new Traverser<K, V>(nodeArray, n, 0, n);
            while ((object3 = traverser.advance()) != null) {
                Object object4 = ((Node)object3).val;
                object2 = map.get(((Node)object3).key);
                if (object2 != null && (object2 == object4 || object2.equals(object4))) continue;
                return false;
            }
            for (Map.Entry entry : map.entrySet()) {
                V v;
                Object v2;
                object2 = entry.getKey();
                if (object2 != null && (v2 = entry.getValue()) != null && (v = this.get(object2)) != null && (v2 == v || v2.equals(v))) continue;
                return false;
            }
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    private final void fullAddCount(InternalThreadLocalMap internalThreadLocalMap, long l, IntegerHolder integerHolder, boolean bl) {
        int n;
        int n2;
        if (integerHolder == null) {
            integerHolder = new IntegerHolder();
            n2 = counterHashCodeGenerator.addAndGet(1640531527);
            integerHolder.value = n2 == 0 ? 1 : n2;
            n = integerHolder.value;
            internalThreadLocalMap.setCounterHashCode(integerHolder);
        } else {
            n = integerHolder.value;
        }
        n2 = 0;
        while (true) {
            long l2;
            int n3;
            CounterCell[] counterCellArray = this.counterCells;
            if (this.counterCells != null && (n3 = counterCellArray.length) > 0) {
                CounterCell[] counterCellArray2;
                CounterCell counterCell = counterCellArray[n3 - 1 & n];
                if (counterCell == null) {
                    if (this.cellsBusy == 0) {
                        counterCellArray2 = new CounterCell(l);
                        if (this.cellsBusy == 0 && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                            int n4;
                            int n5;
                            boolean bl2 = false;
                            CounterCell[] counterCellArray3 = this.counterCells;
                            if (this.counterCells != null && (n5 = counterCellArray3.length) > 0 && counterCellArray3[n4 = n5 - 1 & n] == null) {
                                counterCellArray3[n4] = counterCellArray2;
                                bl2 = true;
                            }
                            this.cellsBusy = 0;
                            if (!bl2) continue;
                            break;
                        }
                    }
                    n2 = 0;
                } else if (!bl) {
                    bl = true;
                } else {
                    l2 = counterCell.value;
                    if (U.compareAndSwapLong(counterCell, CELLVALUE, l2, l2 + l)) break;
                    if (this.counterCells != counterCellArray || n3 >= NCPU) {
                        n2 = 0;
                    } else if (n2 == 0) {
                        n2 = 1;
                    } else if (this.cellsBusy == 0 && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                        if (this.counterCells == counterCellArray) {
                            counterCellArray2 = new CounterCell[n3 << 1];
                            for (int i = 0; i < n3; ++i) {
                                counterCellArray2[i] = counterCellArray[i];
                            }
                            this.counterCells = counterCellArray2;
                        }
                        this.cellsBusy = 0;
                        n2 = 0;
                        continue;
                    }
                }
                n ^= n << 13;
                n ^= n >>> 17;
                n ^= n << 5;
                continue;
            }
            if (this.cellsBusy == 0 && this.counterCells == counterCellArray && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                boolean bl3 = false;
                if (this.counterCells == counterCellArray) {
                    CounterCell[] counterCellArray4 = new CounterCell[2];
                    counterCellArray4[n & 1] = new CounterCell(l);
                    this.counterCells = counterCellArray4;
                    bl3 = true;
                }
                this.cellsBusy = 0;
                if (!bl3) continue;
                break;
            }
            l2 = this.baseCount;
            if (U.compareAndSwapLong(this, BASECOUNT, l2, l2 + l)) break;
        }
        integerHolder.value = n;
    }

    static abstract class BulkTask<K, V, R>
    extends CountedCompleter<R> {
        Node<K, V> next;
        Node<K, V>[] tab;
        int batch;
        final int baseSize;
        int index;
        int baseLimit;
        int baseIndex;

        final Node<K, V> advance() {
            Node<K, V> node = this.next;
            if (node != null) {
                node = node.next;
            }
            while (true) {
                int n;
                Node<K, V>[] nodeArray;
                block9: {
                    block8: {
                        int n2;
                        if (node != null) {
                            this.next = node;
                            return this.next;
                        }
                        if (this.baseIndex >= this.baseLimit) break block8;
                        nodeArray = this.tab;
                        if (this.tab != null && (n = nodeArray.length) > (n2 = this.index) && n2 >= 0) break block9;
                    }
                    this.next = null;
                    return null;
                }
                node = ConcurrentHashMapV8.tabAt(nodeArray, this.index);
                if (node != null && node.hash < 0) {
                    if (node instanceof ForwardingNode) {
                        this.tab = ((ForwardingNode)node).nextTable;
                        node = null;
                        continue;
                    }
                    node = node instanceof TreeBin ? ((TreeBin)node).first : null;
                }
                if ((this.index += this.baseSize) < n) continue;
                this.index = ++this.baseIndex;
            }
        }

        BulkTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray) {
            super(bulkTask);
            this.batch = n;
            this.index = this.baseIndex = n2;
            this.tab = nodeArray;
            if (nodeArray == null) {
                this.baseLimit = 0;
                this.baseSize = 0;
            } else if (bulkTask == null) {
                this.baseSize = this.baseLimit = nodeArray.length;
            } else {
                this.baseLimit = n3;
                this.baseSize = bulkTask.baseSize;
            }
        }
    }

    public static class KeySetView<K, V>
    extends CollectionView<K, V, K>
    implements Set<K>,
    Serializable {
        private static final long serialVersionUID = 7249069246763182397L;
        private final V value;

        @Override
        public boolean add(K k) {
            V v = this.value;
            if (v == null) {
                throw new UnsupportedOperationException();
            }
            return this.map.putVal(k, v, true) == null;
        }

        @Override
        public void forEach(Action<? super K> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            Node<K, V>[] nodeArray = this.map.table;
            if (this.map.table != null) {
                Node node;
                Traverser traverser = new Traverser(nodeArray, nodeArray.length, 0, nodeArray.length);
                while ((node = traverser.advance()) != null) {
                    action.apply(node.key);
                }
            }
        }

        public V getMappedValue() {
            return this.value;
        }

        KeySetView(ConcurrentHashMapV8<K, V> concurrentHashMapV8, V v) {
            super(concurrentHashMapV8);
            this.value = v;
        }

        @Override
        public boolean contains(Object object) {
            return this.map.containsKey(object);
        }

        @Override
        public int hashCode() {
            int n = 0;
            for (K k : this) {
                n += k.hashCode();
            }
            return n;
        }

        @Override
        public boolean addAll(Collection<? extends K> collection) {
            boolean bl = false;
            V v = this.value;
            if (v == null) {
                throw new UnsupportedOperationException();
            }
            for (K k : collection) {
                if (this.map.putVal(k, v, true) != null) continue;
                bl = true;
            }
            return bl;
        }

        @Override
        public Iterator<K> iterator() {
            ConcurrentHashMapV8 concurrentHashMapV8 = this.map;
            Node<K, V>[] nodeArray = concurrentHashMapV8.table;
            int n = concurrentHashMapV8.table == null ? 0 : nodeArray.length;
            return new KeyIterator(nodeArray, n, 0, n, concurrentHashMapV8);
        }

        @Override
        public boolean equals(Object object) {
            Set set;
            return object instanceof Set && ((set = (Set)object) == this || this.containsAll(set) && set.containsAll(this));
        }

        public ConcurrentHashMapSpliterator<K> spliterator166() {
            ConcurrentHashMapV8 concurrentHashMapV8 = this.map;
            long l = concurrentHashMapV8.sumCount();
            Node<K, V>[] nodeArray = concurrentHashMapV8.table;
            int n = concurrentHashMapV8.table == null ? 0 : nodeArray.length;
            return new KeySpliterator(nodeArray, n, 0, n, l < 0L ? 0L : l);
        }

        @Override
        public boolean remove(Object object) {
            return this.map.remove(object) != null;
        }
    }

    static final class EntrySetView<K, V>
    extends CollectionView<K, V, Map.Entry<K, V>>
    implements Set<Map.Entry<K, V>>,
    Serializable {
        private static final long serialVersionUID = 2249069246763182397L;

        @Override
        public boolean add(Map.Entry<K, V> entry) {
            return this.map.putVal(entry.getKey(), entry.getValue(), false) == null;
        }

        @Override
        public void forEach(Action<? super Map.Entry<K, V>> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            Node<K, V>[] nodeArray = this.map.table;
            if (this.map.table != null) {
                Node node;
                Traverser traverser = new Traverser(nodeArray, nodeArray.length, 0, nodeArray.length);
                while ((node = traverser.advance()) != null) {
                    action.apply(new MapEntry(node.key, node.val, this.map));
                }
            }
        }

        EntrySetView(ConcurrentHashMapV8<K, V> concurrentHashMapV8) {
            super(concurrentHashMapV8);
        }

        public ConcurrentHashMapSpliterator<Map.Entry<K, V>> spliterator166() {
            ConcurrentHashMapV8 concurrentHashMapV8 = this.map;
            long l = concurrentHashMapV8.sumCount();
            Node<K, V>[] nodeArray = concurrentHashMapV8.table;
            int n = concurrentHashMapV8.table == null ? 0 : nodeArray.length;
            return new EntrySpliterator(nodeArray, n, 0, n, l < 0L ? 0L : l, concurrentHashMapV8);
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            ConcurrentHashMapV8 concurrentHashMapV8 = this.map;
            Node<K, V>[] nodeArray = concurrentHashMapV8.table;
            int n = concurrentHashMapV8.table == null ? 0 : nodeArray.length;
            return new EntryIterator(nodeArray, n, 0, n, concurrentHashMapV8);
        }

        @Override
        public boolean addAll(Collection<? extends Map.Entry<K, V>> collection) {
            boolean bl = false;
            for (Map.Entry<K, V> entry : collection) {
                if (!this.add(entry)) continue;
                bl = true;
            }
            return bl;
        }

        @Override
        public final boolean equals(Object object) {
            Set set;
            return object instanceof Set && ((set = (Set)object) == this || this.containsAll(set) && set.containsAll(this));
        }

        @Override
        public boolean contains(Object object) {
            Object v;
            Object v2;
            Map.Entry entry;
            Object k;
            return object instanceof Map.Entry && (k = (entry = (Map.Entry)object).getKey()) != null && (v2 = this.map.get(k)) != null && (v = entry.getValue()) != null && (v == v2 || v.equals(v2));
        }

        @Override
        public boolean remove(Object object) {
            Object v;
            Map.Entry entry;
            Object k;
            return object instanceof Map.Entry && (k = (entry = (Map.Entry)object).getKey()) != null && (v = entry.getValue()) != null && this.map.remove(k, v);
        }

        @Override
        public final int hashCode() {
            int n = 0;
            Node<K, V>[] nodeArray = this.map.table;
            if (this.map.table != null) {
                Node node;
                Traverser traverser = new Traverser(nodeArray, nodeArray.length, 0, nodeArray.length);
                while ((node = traverser.advance()) != null) {
                    n += node.hashCode();
                }
            }
            return n;
        }
    }

    static final class ForEachTransformedValueTask<K, V, U>
    extends BulkTask<K, V, Void> {
        final Action<? super U> action;
        final Fun<? super V, ? extends U> transformer;

        @Override
        public final void compute() {
            Action<U> action;
            Fun<V, U> fun = this.transformer;
            if (fun != null && (action = this.action) != null) {
                Node node;
                int n;
                int n2;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    new ForEachTransformedValueTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, fun, action).fork();
                }
                while ((node = this.advance()) != null) {
                    U u = fun.apply(node.val);
                    if (u == null) continue;
                    action.apply(u);
                }
                this.propagateCompletion();
            }
        }

        ForEachTransformedValueTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, Fun<? super V, ? extends U> fun, Action<? super U> action) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.transformer = fun;
            this.action = action;
        }
    }

    static final class MapReduceEntriesTask<K, V, U>
    extends BulkTask<K, V, U> {
        U result;
        MapReduceEntriesTask<K, V, U> nextRight;
        final Fun<Map.Entry<K, V>, ? extends U> transformer;
        final BiFun<? super U, ? super U, ? extends U> reducer;
        MapReduceEntriesTask<K, V, U> rights;

        @Override
        public final U getRawResult() {
            return this.result;
        }

        MapReduceEntriesTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, MapReduceEntriesTask<K, V, U> mapReduceEntriesTask, Fun<Map.Entry<K, V>, ? extends U> fun, BiFun<? super U, ? super U, ? extends U> biFun) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.nextRight = mapReduceEntriesTask;
            this.transformer = fun;
            this.reducer = biFun;
        }

        @Override
        public final void compute() {
            BiFun<U, U, U> biFun;
            Fun<Map.Entry<K, V>, U> fun = this.transformer;
            if (fun != null && (biFun = this.reducer) != null) {
                CountedCompleter<?> countedCompleter;
                int n;
                int n2;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    this.rights = new MapReduceEntriesTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, this.rights, fun, biFun);
                    this.rights.fork();
                }
                Object a = null;
                while ((countedCompleter = this.advance()) != null) {
                    U u = fun.apply((Map.Entry<K, V>)((Object)countedCompleter));
                    if (u == null) continue;
                    a = a == null ? u : biFun.apply(a, u);
                }
                this.result = a;
                for (countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    MapReduceEntriesTask mapReduceEntriesTask = (MapReduceEntriesTask)countedCompleter;
                    MapReduceEntriesTask<K, V, U> mapReduceEntriesTask2 = mapReduceEntriesTask.rights;
                    while (mapReduceEntriesTask2 != null) {
                        U u = mapReduceEntriesTask2.result;
                        if (u != null) {
                            U u2 = mapReduceEntriesTask.result;
                            mapReduceEntriesTask.result = u2 == null ? u : biFun.apply(u2, u);
                        }
                        mapReduceEntriesTask2 = mapReduceEntriesTask.rights = mapReduceEntriesTask2.nextRight;
                    }
                }
            }
        }
    }

    public static interface ObjectToInt<A> {
        public int apply(A var1);
    }

    static final class ForEachKeyTask<K, V>
    extends BulkTask<K, V, Void> {
        final Action<? super K> action;

        @Override
        public final void compute() {
            Action<K> action = this.action;
            if (action != null) {
                Node node;
                int n;
                int n2;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    new ForEachKeyTask<K, V>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, action).fork();
                }
                while ((node = this.advance()) != null) {
                    action.apply(node.key);
                }
                this.propagateCompletion();
            }
        }

        ForEachKeyTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, Action<? super K> action) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.action = action;
        }
    }

    static final class MapReduceValuesTask<K, V, U>
    extends BulkTask<K, V, U> {
        final BiFun<? super U, ? super U, ? extends U> reducer;
        MapReduceValuesTask<K, V, U> nextRight;
        U result;
        MapReduceValuesTask<K, V, U> rights;
        final Fun<? super V, ? extends U> transformer;

        @Override
        public final U getRawResult() {
            return this.result;
        }

        MapReduceValuesTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, MapReduceValuesTask<K, V, U> mapReduceValuesTask, Fun<? super V, ? extends U> fun, BiFun<? super U, ? super U, ? extends U> biFun) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.nextRight = mapReduceValuesTask;
            this.transformer = fun;
            this.reducer = biFun;
        }

        @Override
        public final void compute() {
            BiFun<U, U, U> biFun;
            Fun<V, U> fun = this.transformer;
            if (fun != null && (biFun = this.reducer) != null) {
                CountedCompleter<?> countedCompleter;
                int n;
                int n2;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    this.rights = new MapReduceValuesTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, this.rights, fun, biFun);
                    this.rights.fork();
                }
                Object a = null;
                while ((countedCompleter = this.advance()) != null) {
                    U u = fun.apply(((Node)((Object)countedCompleter)).val);
                    if (u == null) continue;
                    a = a == null ? u : biFun.apply(a, u);
                }
                this.result = a;
                for (countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    MapReduceValuesTask mapReduceValuesTask = (MapReduceValuesTask)countedCompleter;
                    MapReduceValuesTask<K, V, U> mapReduceValuesTask2 = mapReduceValuesTask.rights;
                    while (mapReduceValuesTask2 != null) {
                        U u = mapReduceValuesTask2.result;
                        if (u != null) {
                            U u2 = mapReduceValuesTask.result;
                            mapReduceValuesTask.result = u2 == null ? u : biFun.apply(u2, u);
                        }
                        mapReduceValuesTask2 = mapReduceValuesTask.rights = mapReduceValuesTask2.nextRight;
                    }
                }
            }
        }
    }

    static class Traverser<K, V> {
        int baseIndex;
        int index;
        final int baseSize;
        Node<K, V> next;
        int baseLimit;
        Node<K, V>[] tab;

        final Node<K, V> advance() {
            Node<K, V> node = this.next;
            if (node != null) {
                node = node.next;
            }
            while (true) {
                int n;
                Node<K, V>[] nodeArray;
                block9: {
                    block8: {
                        int n2;
                        if (node != null) {
                            this.next = node;
                            return this.next;
                        }
                        if (this.baseIndex >= this.baseLimit) break block8;
                        nodeArray = this.tab;
                        if (this.tab != null && (n = nodeArray.length) > (n2 = this.index) && n2 >= 0) break block9;
                    }
                    this.next = null;
                    return null;
                }
                node = ConcurrentHashMapV8.tabAt(nodeArray, this.index);
                if (node != null && node.hash < 0) {
                    if (node instanceof ForwardingNode) {
                        this.tab = ((ForwardingNode)node).nextTable;
                        node = null;
                        continue;
                    }
                    node = node instanceof TreeBin ? ((TreeBin)node).first : null;
                }
                if ((this.index += this.baseSize) < n) continue;
                this.index = ++this.baseIndex;
            }
        }

        Traverser(Node<K, V>[] nodeArray, int n, int n2, int n3) {
            this.tab = nodeArray;
            this.baseSize = n;
            this.baseIndex = this.index = n2;
            this.baseLimit = n3;
            this.next = null;
        }
    }

    public static interface Fun<A, T> {
        public T apply(A var1);
    }

    static final class MapReduceMappingsToIntTask<K, V>
    extends BulkTask<K, V, Integer> {
        final IntByIntToInt reducer;
        final int basis;
        MapReduceMappingsToIntTask<K, V> nextRight;
        final ObjectByObjectToInt<? super K, ? super V> transformer;
        int result;
        MapReduceMappingsToIntTask<K, V> rights;

        @Override
        public final Integer getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            IntByIntToInt intByIntToInt;
            ObjectByObjectToInt<K, V> objectByObjectToInt = this.transformer;
            if (objectByObjectToInt != null && (intByIntToInt = this.reducer) != null) {
                CountedCompleter<?> countedCompleter;
                int n;
                int n2;
                int n3 = this.basis;
                int n4 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n4 >>> 1) > n4) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    this.rights = new MapReduceMappingsToIntTask<K, V>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, this.rights, objectByObjectToInt, n3, intByIntToInt);
                    this.rights.fork();
                }
                while ((countedCompleter = this.advance()) != null) {
                    n3 = intByIntToInt.apply(n3, objectByObjectToInt.apply(((Node)((Object)countedCompleter)).key, ((Node)((Object)countedCompleter)).val));
                }
                this.result = n3;
                for (countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    MapReduceMappingsToIntTask mapReduceMappingsToIntTask = (MapReduceMappingsToIntTask)countedCompleter;
                    MapReduceMappingsToIntTask<K, V> mapReduceMappingsToIntTask2 = mapReduceMappingsToIntTask.rights;
                    while (mapReduceMappingsToIntTask2 != null) {
                        mapReduceMappingsToIntTask.result = intByIntToInt.apply(mapReduceMappingsToIntTask.result, mapReduceMappingsToIntTask2.result);
                        mapReduceMappingsToIntTask2 = mapReduceMappingsToIntTask.rights = mapReduceMappingsToIntTask2.nextRight;
                    }
                }
            }
        }

        MapReduceMappingsToIntTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, MapReduceMappingsToIntTask<K, V> mapReduceMappingsToIntTask, ObjectByObjectToInt<? super K, ? super V> objectByObjectToInt, int n4, IntByIntToInt intByIntToInt) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.nextRight = mapReduceMappingsToIntTask;
            this.transformer = objectByObjectToInt;
            this.basis = n4;
            this.reducer = intByIntToInt;
        }
    }

    static final class EntrySpliterator<K, V>
    extends Traverser<K, V>
    implements ConcurrentHashMapSpliterator<Map.Entry<K, V>> {
        long est;
        final ConcurrentHashMapV8<K, V> map;

        @Override
        public long estimateSize() {
            return this.est;
        }

        @Override
        public boolean tryAdvance(Action<? super Map.Entry<K, V>> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            Node node = this.advance();
            if (node == null) {
                return false;
            }
            action.apply(new MapEntry(node.key, node.val, this.map));
            return true;
        }

        EntrySpliterator(Node<K, V>[] nodeArray, int n, int n2, int n3, long l, ConcurrentHashMapV8<K, V> concurrentHashMapV8) {
            super(nodeArray, n, n2, n3);
            this.map = concurrentHashMapV8;
            this.est = l;
        }

        @Override
        public void forEachRemaining(Action<? super Map.Entry<K, V>> action) {
            Node node;
            if (action == null) {
                throw new NullPointerException();
            }
            while ((node = this.advance()) != null) {
                action.apply(new MapEntry(node.key, node.val, this.map));
            }
        }

        @Override
        public ConcurrentHashMapSpliterator<Map.Entry<K, V>> trySplit() {
            EntrySpliterator<K, V> entrySpliterator;
            int n = this.baseIndex;
            int n2 = this.baseLimit;
            int n3 = n + n2 >>> 1;
            if (n3 <= n) {
                entrySpliterator = null;
            } else {
                this.baseLimit = n3;
                EntrySpliterator<K, V> entrySpliterator2 = new EntrySpliterator<K, V>(this.tab, this.baseSize, this.baseLimit, n2, this.est >>>= 1, this.map);
                entrySpliterator = entrySpliterator2;
            }
            return entrySpliterator;
        }
    }

    public static interface ObjectByObjectToInt<A, B> {
        public int apply(A var1, B var2);
    }

    static final class MapReduceValuesToIntTask<K, V>
    extends BulkTask<K, V, Integer> {
        final IntByIntToInt reducer;
        MapReduceValuesToIntTask<K, V> rights;
        int result;
        final int basis;
        final ObjectToInt<? super V> transformer;
        MapReduceValuesToIntTask<K, V> nextRight;

        MapReduceValuesToIntTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, MapReduceValuesToIntTask<K, V> mapReduceValuesToIntTask, ObjectToInt<? super V> objectToInt, int n4, IntByIntToInt intByIntToInt) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.nextRight = mapReduceValuesToIntTask;
            this.transformer = objectToInt;
            this.basis = n4;
            this.reducer = intByIntToInt;
        }

        @Override
        public final void compute() {
            IntByIntToInt intByIntToInt;
            ObjectToInt<V> objectToInt = this.transformer;
            if (objectToInt != null && (intByIntToInt = this.reducer) != null) {
                CountedCompleter<?> countedCompleter;
                int n;
                int n2;
                int n3 = this.basis;
                int n4 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n4 >>> 1) > n4) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    this.rights = new MapReduceValuesToIntTask<K, V>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, this.rights, objectToInt, n3, intByIntToInt);
                    this.rights.fork();
                }
                while ((countedCompleter = this.advance()) != null) {
                    n3 = intByIntToInt.apply(n3, objectToInt.apply(((Node)((Object)countedCompleter)).val));
                }
                this.result = n3;
                for (countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    MapReduceValuesToIntTask mapReduceValuesToIntTask = (MapReduceValuesToIntTask)countedCompleter;
                    MapReduceValuesToIntTask<K, V> mapReduceValuesToIntTask2 = mapReduceValuesToIntTask.rights;
                    while (mapReduceValuesToIntTask2 != null) {
                        mapReduceValuesToIntTask.result = intByIntToInt.apply(mapReduceValuesToIntTask.result, mapReduceValuesToIntTask2.result);
                        mapReduceValuesToIntTask2 = mapReduceValuesToIntTask.rights = mapReduceValuesToIntTask2.nextRight;
                    }
                }
            }
        }

        @Override
        public final Integer getRawResult() {
            return this.result;
        }
    }

    public static interface Action<A> {
        public void apply(A var1);
    }

    static class BaseIterator<K, V>
    extends Traverser<K, V> {
        Node<K, V> lastReturned;
        final ConcurrentHashMapV8<K, V> map;

        BaseIterator(Node<K, V>[] nodeArray, int n, int n2, int n3, ConcurrentHashMapV8<K, V> concurrentHashMapV8) {
            super(nodeArray, n, n2, n3);
            this.map = concurrentHashMapV8;
            this.advance();
        }

        public final boolean hasNext() {
            return this.next != null;
        }

        public final void remove() {
            Node<K, V> node = this.lastReturned;
            if (node == null) {
                throw new IllegalStateException();
            }
            this.lastReturned = null;
            this.map.replaceNode(node.key, null, null);
        }

        public final boolean hasMoreElements() {
            return this.next != null;
        }
    }

    static final class MapReduceKeysToIntTask<K, V>
    extends BulkTask<K, V, Integer> {
        final ObjectToInt<? super K> transformer;
        final int basis;
        int result;
        MapReduceKeysToIntTask<K, V> nextRight;
        final IntByIntToInt reducer;
        MapReduceKeysToIntTask<K, V> rights;

        @Override
        public final void compute() {
            IntByIntToInt intByIntToInt;
            ObjectToInt<K> objectToInt = this.transformer;
            if (objectToInt != null && (intByIntToInt = this.reducer) != null) {
                CountedCompleter<?> countedCompleter;
                int n;
                int n2;
                int n3 = this.basis;
                int n4 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n4 >>> 1) > n4) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    this.rights = new MapReduceKeysToIntTask<K, V>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, this.rights, objectToInt, n3, intByIntToInt);
                    this.rights.fork();
                }
                while ((countedCompleter = this.advance()) != null) {
                    n3 = intByIntToInt.apply(n3, objectToInt.apply(((Node)((Object)countedCompleter)).key));
                }
                this.result = n3;
                for (countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    MapReduceKeysToIntTask mapReduceKeysToIntTask = (MapReduceKeysToIntTask)countedCompleter;
                    MapReduceKeysToIntTask<K, V> mapReduceKeysToIntTask2 = mapReduceKeysToIntTask.rights;
                    while (mapReduceKeysToIntTask2 != null) {
                        mapReduceKeysToIntTask.result = intByIntToInt.apply(mapReduceKeysToIntTask.result, mapReduceKeysToIntTask2.result);
                        mapReduceKeysToIntTask2 = mapReduceKeysToIntTask.rights = mapReduceKeysToIntTask2.nextRight;
                    }
                }
            }
        }

        @Override
        public final Integer getRawResult() {
            return this.result;
        }

        MapReduceKeysToIntTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, MapReduceKeysToIntTask<K, V> mapReduceKeysToIntTask, ObjectToInt<? super K> objectToInt, int n4, IntByIntToInt intByIntToInt) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.nextRight = mapReduceKeysToIntTask;
            this.transformer = objectToInt;
            this.basis = n4;
            this.reducer = intByIntToInt;
        }
    }

    static final class SearchMappingsTask<K, V, U>
    extends BulkTask<K, V, U> {
        final AtomicReference<U> result;
        final BiFun<? super K, ? super V, ? extends U> searchFunction;

        SearchMappingsTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, BiFun<? super K, ? super V, ? extends U> biFun, AtomicReference<U> atomicReference) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.searchFunction = biFun;
            this.result = atomicReference;
        }

        @Override
        public final void compute() {
            AtomicReference<U> atomicReference;
            BiFun<K, V, U> biFun = this.searchFunction;
            if (biFun != null && (atomicReference = this.result) != null) {
                int n;
                int n2;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    if (atomicReference.get() != null) {
                        return;
                    }
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    new SearchMappingsTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, biFun, atomicReference).fork();
                }
                while (atomicReference.get() == null) {
                    Node node = this.advance();
                    if (node == null) {
                        this.propagateCompletion();
                        break;
                    }
                    U u = biFun.apply(node.key, node.val);
                    if (u == null) continue;
                    if (!atomicReference.compareAndSet(null, u)) break;
                    this.quietlyCompleteRoot();
                    break;
                }
            }
        }

        @Override
        public final U getRawResult() {
            return this.result.get();
        }
    }

    static final class ReservationNode<K, V>
    extends Node<K, V> {
        @Override
        Node<K, V> find(int n, Object object) {
            return null;
        }

        ReservationNode() {
            super(-3, null, null, null);
        }
    }

    static final class MapReduceValuesToDoubleTask<K, V>
    extends BulkTask<K, V, Double> {
        final ObjectToDouble<? super V> transformer;
        final DoubleByDoubleToDouble reducer;
        double result;
        MapReduceValuesToDoubleTask<K, V> nextRight;
        final double basis;
        MapReduceValuesToDoubleTask<K, V> rights;

        MapReduceValuesToDoubleTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, MapReduceValuesToDoubleTask<K, V> mapReduceValuesToDoubleTask, ObjectToDouble<? super V> objectToDouble, double d, DoubleByDoubleToDouble doubleByDoubleToDouble) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.nextRight = mapReduceValuesToDoubleTask;
            this.transformer = objectToDouble;
            this.basis = d;
            this.reducer = doubleByDoubleToDouble;
        }

        @Override
        public final void compute() {
            DoubleByDoubleToDouble doubleByDoubleToDouble;
            ObjectToDouble<V> objectToDouble = this.transformer;
            if (objectToDouble != null && (doubleByDoubleToDouble = this.reducer) != null) {
                CountedCompleter<?> countedCompleter;
                int n;
                int n2;
                double d = this.basis;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    this.rights = new MapReduceValuesToDoubleTask<K, V>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, this.rights, objectToDouble, d, doubleByDoubleToDouble);
                    this.rights.fork();
                }
                while ((countedCompleter = this.advance()) != null) {
                    d = doubleByDoubleToDouble.apply(d, objectToDouble.apply(((Node)((Object)countedCompleter)).val));
                }
                this.result = d;
                for (countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    MapReduceValuesToDoubleTask mapReduceValuesToDoubleTask = (MapReduceValuesToDoubleTask)countedCompleter;
                    MapReduceValuesToDoubleTask<K, V> mapReduceValuesToDoubleTask2 = mapReduceValuesToDoubleTask.rights;
                    while (mapReduceValuesToDoubleTask2 != null) {
                        mapReduceValuesToDoubleTask.result = doubleByDoubleToDouble.apply(mapReduceValuesToDoubleTask.result, mapReduceValuesToDoubleTask2.result);
                        mapReduceValuesToDoubleTask2 = mapReduceValuesToDoubleTask.rights = mapReduceValuesToDoubleTask2.nextRight;
                    }
                }
            }
        }

        @Override
        public final Double getRawResult() {
            return this.result;
        }
    }

    static final class ValueSpliterator<K, V>
    extends Traverser<K, V>
    implements ConcurrentHashMapSpliterator<V> {
        long est;

        @Override
        public void forEachRemaining(Action<? super V> action) {
            Node node;
            if (action == null) {
                throw new NullPointerException();
            }
            while ((node = this.advance()) != null) {
                action.apply(node.val);
            }
        }

        ValueSpliterator(Node<K, V>[] nodeArray, int n, int n2, int n3, long l) {
            super(nodeArray, n, n2, n3);
            this.est = l;
        }

        @Override
        public ConcurrentHashMapSpliterator<V> trySplit() {
            ValueSpliterator<K, V> valueSpliterator;
            int n = this.baseIndex;
            int n2 = this.baseLimit;
            int n3 = n + n2 >>> 1;
            if (n3 <= n) {
                valueSpliterator = null;
            } else {
                this.baseLimit = n3;
                ValueSpliterator<K, V> valueSpliterator2 = new ValueSpliterator<K, V>(this.tab, this.baseSize, this.baseLimit, n2, this.est >>>= 1);
                valueSpliterator = valueSpliterator2;
            }
            return valueSpliterator;
        }

        @Override
        public long estimateSize() {
            return this.est;
        }

        @Override
        public boolean tryAdvance(Action<? super V> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            Node node = this.advance();
            if (node == null) {
                return false;
            }
            action.apply(node.val);
            return true;
        }
    }

    public static interface ObjectByObjectToDouble<A, B> {
        public double apply(A var1, B var2);
    }

    static final class ForEachTransformedEntryTask<K, V, U>
    extends BulkTask<K, V, Void> {
        final Fun<Map.Entry<K, V>, ? extends U> transformer;
        final Action<? super U> action;

        @Override
        public final void compute() {
            Action<U> action;
            Fun<Map.Entry<K, V>, U> fun = this.transformer;
            if (fun != null && (action = this.action) != null) {
                Node node;
                int n;
                int n2;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    new ForEachTransformedEntryTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, fun, action).fork();
                }
                while ((node = this.advance()) != null) {
                    U u = fun.apply(node);
                    if (u == null) continue;
                    action.apply(u);
                }
                this.propagateCompletion();
            }
        }

        ForEachTransformedEntryTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, Fun<Map.Entry<K, V>, ? extends U> fun, Action<? super U> action) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.transformer = fun;
            this.action = action;
        }
    }

    static final class MapReduceKeysToDoubleTask<K, V>
    extends BulkTask<K, V, Double> {
        final DoubleByDoubleToDouble reducer;
        final ObjectToDouble<? super K> transformer;
        final double basis;
        MapReduceKeysToDoubleTask<K, V> nextRight;
        double result;
        MapReduceKeysToDoubleTask<K, V> rights;

        MapReduceKeysToDoubleTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, MapReduceKeysToDoubleTask<K, V> mapReduceKeysToDoubleTask, ObjectToDouble<? super K> objectToDouble, double d, DoubleByDoubleToDouble doubleByDoubleToDouble) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.nextRight = mapReduceKeysToDoubleTask;
            this.transformer = objectToDouble;
            this.basis = d;
            this.reducer = doubleByDoubleToDouble;
        }

        @Override
        public final Double getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            DoubleByDoubleToDouble doubleByDoubleToDouble;
            ObjectToDouble<K> objectToDouble = this.transformer;
            if (objectToDouble != null && (doubleByDoubleToDouble = this.reducer) != null) {
                CountedCompleter<?> countedCompleter;
                int n;
                int n2;
                double d = this.basis;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    this.rights = new MapReduceKeysToDoubleTask<K, V>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, this.rights, objectToDouble, d, doubleByDoubleToDouble);
                    this.rights.fork();
                }
                while ((countedCompleter = this.advance()) != null) {
                    d = doubleByDoubleToDouble.apply(d, objectToDouble.apply(((Node)((Object)countedCompleter)).key));
                }
                this.result = d;
                for (countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    MapReduceKeysToDoubleTask mapReduceKeysToDoubleTask = (MapReduceKeysToDoubleTask)countedCompleter;
                    MapReduceKeysToDoubleTask<K, V> mapReduceKeysToDoubleTask2 = mapReduceKeysToDoubleTask.rights;
                    while (mapReduceKeysToDoubleTask2 != null) {
                        mapReduceKeysToDoubleTask.result = doubleByDoubleToDouble.apply(mapReduceKeysToDoubleTask.result, mapReduceKeysToDoubleTask2.result);
                        mapReduceKeysToDoubleTask2 = mapReduceKeysToDoubleTask.rights = mapReduceKeysToDoubleTask2.nextRight;
                    }
                }
            }
        }
    }

    static abstract class CollectionView<K, V, E>
    implements Collection<E>,
    Serializable {
        private static final String oomeMsg = "Required array size too large";
        final ConcurrentHashMapV8<K, V> map;
        private static final long serialVersionUID = 7249069246763182397L;

        @Override
        public final boolean retainAll(Collection<?> collection) {
            boolean bl = false;
            Iterator<E> iterator = this.iterator();
            while (iterator.hasNext()) {
                if (collection.contains(iterator.next())) continue;
                iterator.remove();
                bl = true;
            }
            return bl;
        }

        @Override
        public abstract Iterator<E> iterator();

        public ConcurrentHashMapV8<K, V> getMap() {
            return this.map;
        }

        @Override
        public final Object[] toArray() {
            long l = this.map.mappingCount();
            if (l > 0x7FFFFFF7L) {
                throw new OutOfMemoryError(oomeMsg);
            }
            int n = (int)l;
            Object[] objectArray = new Object[n];
            int n2 = 0;
            for (E e : this) {
                if (n2 == n) {
                    if (n >= 0x7FFFFFF7) {
                        throw new OutOfMemoryError(oomeMsg);
                    }
                    n = n >= 0x3FFFFFFB ? 0x7FFFFFF7 : (n += (n >>> 1) + 1);
                    objectArray = Arrays.copyOf(objectArray, n);
                }
                objectArray[n2++] = e;
            }
            return n2 == n ? objectArray : Arrays.copyOf(objectArray, n2);
        }

        @Override
        public final <T> T[] toArray(T[] TArray) {
            long l = this.map.mappingCount();
            if (l > 0x7FFFFFF7L) {
                throw new OutOfMemoryError(oomeMsg);
            }
            int n = (int)l;
            T[] TArray2 = TArray.length >= n ? TArray : (Object[])Array.newInstance(TArray.getClass().getComponentType(), n);
            int n2 = TArray2.length;
            int n3 = 0;
            for (E e : this) {
                if (n3 == n2) {
                    if (n2 >= 0x7FFFFFF7) {
                        throw new OutOfMemoryError(oomeMsg);
                    }
                    n2 = n2 >= 0x3FFFFFFB ? 0x7FFFFFF7 : (n2 += (n2 >>> 1) + 1);
                    TArray2 = Arrays.copyOf(TArray2, n2);
                }
                TArray2[n3++] = e;
            }
            if (TArray == TArray2 && n3 < n2) {
                TArray2[n3] = null;
                return TArray2;
            }
            return n3 == n2 ? TArray2 : Arrays.copyOf(TArray2, n3);
        }

        @Override
        public final boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override
        public abstract boolean contains(Object var1);

        CollectionView(ConcurrentHashMapV8<K, V> concurrentHashMapV8) {
            this.map = concurrentHashMapV8;
        }

        @Override
        public final int size() {
            return this.map.size();
        }

        @Override
        public abstract boolean remove(Object var1);

        @Override
        public final boolean containsAll(Collection<?> collection) {
            if (collection != this) {
                for (Object obj : collection) {
                    if (obj != null && this.contains(obj)) continue;
                    return false;
                }
            }
            return true;
        }

        @Override
        public final void clear() {
            this.map.clear();
        }

        public final String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('[');
            Iterator<E> iterator = this.iterator();
            if (iterator.hasNext()) {
                while (true) {
                    E e;
                    stringBuilder.append((Object)((e = iterator.next()) == this ? "(this Collection)" : e));
                    if (!iterator.hasNext()) break;
                    stringBuilder.append(',').append(' ');
                }
            }
            return stringBuilder.append(']').toString();
        }

        @Override
        public final boolean removeAll(Collection<?> collection) {
            boolean bl = false;
            Iterator<E> iterator = this.iterator();
            while (iterator.hasNext()) {
                if (!collection.contains(iterator.next())) continue;
                iterator.remove();
                bl = true;
            }
            return bl;
        }
    }

    static class Node<K, V>
    implements Map.Entry<K, V> {
        volatile V val;
        final int hash;
        final K key;
        volatile Node<K, V> next;

        Node(int n, K k, V v, Node<K, V> node) {
            this.hash = n;
            this.key = k;
            this.val = v;
            this.next = node;
        }

        @Override
        public final V setValue(V v) {
            throw new UnsupportedOperationException();
        }

        Node<K, V> find(int n, Object object) {
            Node<K, V> node = this;
            if (object != null) {
                do {
                    K k;
                    if (node.hash != n || (k = node.key) != object && (k == null || !object.equals(k))) continue;
                    return node;
                } while ((node = node.next) != null);
            }
            return null;
        }

        public final String toString() {
            return this.key + "=" + this.val;
        }

        @Override
        public final boolean equals(Object object) {
            V v;
            Object v2;
            Map.Entry entry;
            Object k;
            return !(!(object instanceof Map.Entry) || (k = (entry = (Map.Entry)object).getKey()) == null || (v2 = entry.getValue()) == null || k != this.key && !k.equals(this.key) || v2 != (v = this.val) && !v2.equals(v));
        }

        @Override
        public final K getKey() {
            return this.key;
        }

        @Override
        public final int hashCode() {
            return this.key.hashCode() ^ this.val.hashCode();
        }

        @Override
        public final V getValue() {
            return this.val;
        }
    }

    static final class MapReduceMappingsToDoubleTask<K, V>
    extends BulkTask<K, V, Double> {
        MapReduceMappingsToDoubleTask<K, V> nextRight;
        final DoubleByDoubleToDouble reducer;
        MapReduceMappingsToDoubleTask<K, V> rights;
        double result;
        final double basis;
        final ObjectByObjectToDouble<? super K, ? super V> transformer;

        @Override
        public final Double getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            DoubleByDoubleToDouble doubleByDoubleToDouble;
            ObjectByObjectToDouble<K, V> objectByObjectToDouble = this.transformer;
            if (objectByObjectToDouble != null && (doubleByDoubleToDouble = this.reducer) != null) {
                CountedCompleter<?> countedCompleter;
                int n;
                int n2;
                double d = this.basis;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    this.rights = new MapReduceMappingsToDoubleTask<K, V>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, this.rights, objectByObjectToDouble, d, doubleByDoubleToDouble);
                    this.rights.fork();
                }
                while ((countedCompleter = this.advance()) != null) {
                    d = doubleByDoubleToDouble.apply(d, objectByObjectToDouble.apply(((Node)((Object)countedCompleter)).key, ((Node)((Object)countedCompleter)).val));
                }
                this.result = d;
                for (countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    MapReduceMappingsToDoubleTask mapReduceMappingsToDoubleTask = (MapReduceMappingsToDoubleTask)countedCompleter;
                    MapReduceMappingsToDoubleTask<K, V> mapReduceMappingsToDoubleTask2 = mapReduceMappingsToDoubleTask.rights;
                    while (mapReduceMappingsToDoubleTask2 != null) {
                        mapReduceMappingsToDoubleTask.result = doubleByDoubleToDouble.apply(mapReduceMappingsToDoubleTask.result, mapReduceMappingsToDoubleTask2.result);
                        mapReduceMappingsToDoubleTask2 = mapReduceMappingsToDoubleTask.rights = mapReduceMappingsToDoubleTask2.nextRight;
                    }
                }
            }
        }

        MapReduceMappingsToDoubleTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, MapReduceMappingsToDoubleTask<K, V> mapReduceMappingsToDoubleTask, ObjectByObjectToDouble<? super K, ? super V> objectByObjectToDouble, double d, DoubleByDoubleToDouble doubleByDoubleToDouble) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.nextRight = mapReduceMappingsToDoubleTask;
            this.transformer = objectByObjectToDouble;
            this.basis = d;
            this.reducer = doubleByDoubleToDouble;
        }
    }

    public static interface LongByLongToLong {
        public long apply(long var1, long var3);
    }

    static final class KeySpliterator<K, V>
    extends Traverser<K, V>
    implements ConcurrentHashMapSpliterator<K> {
        long est;

        @Override
        public void forEachRemaining(Action<? super K> action) {
            Node node;
            if (action == null) {
                throw new NullPointerException();
            }
            while ((node = this.advance()) != null) {
                action.apply(node.key);
            }
        }

        @Override
        public boolean tryAdvance(Action<? super K> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            Node node = this.advance();
            if (node == null) {
                return false;
            }
            action.apply(node.key);
            return true;
        }

        KeySpliterator(Node<K, V>[] nodeArray, int n, int n2, int n3, long l) {
            super(nodeArray, n, n2, n3);
            this.est = l;
        }

        @Override
        public ConcurrentHashMapSpliterator<K> trySplit() {
            KeySpliterator<K, V> keySpliterator;
            int n = this.baseIndex;
            int n2 = this.baseLimit;
            int n3 = n + n2 >>> 1;
            if (n3 <= n) {
                keySpliterator = null;
            } else {
                this.baseLimit = n3;
                KeySpliterator<K, V> keySpliterator2 = new KeySpliterator<K, V>(this.tab, this.baseSize, this.baseLimit, n2, this.est >>>= 1);
                keySpliterator = keySpliterator2;
            }
            return keySpliterator;
        }

        @Override
        public long estimateSize() {
            return this.est;
        }
    }

    static final class ReduceEntriesTask<K, V>
    extends BulkTask<K, V, Map.Entry<K, V>> {
        final BiFun<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer;
        ReduceEntriesTask<K, V> nextRight;
        ReduceEntriesTask<K, V> rights;
        Map.Entry<K, V> result;

        ReduceEntriesTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, ReduceEntriesTask<K, V> reduceEntriesTask, BiFun<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> biFun) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.nextRight = reduceEntriesTask;
            this.reducer = biFun;
        }

        @Override
        public final void compute() {
            BiFun<Map.Entry<K, V>, Map.Entry<K, V>, Map.Entry<K, V>> biFun = this.reducer;
            if (biFun != null) {
                CountedCompleter<?> countedCompleter;
                int n;
                int n2;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    this.rights = new ReduceEntriesTask<K, V>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, this.rights, biFun);
                    this.rights.fork();
                }
                Node node = null;
                while ((countedCompleter = this.advance()) != null) {
                    node = node == null ? countedCompleter : biFun.apply(node, (Map.Entry<K, V>)((Object)countedCompleter));
                }
                this.result = node;
                for (countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    ReduceEntriesTask reduceEntriesTask = (ReduceEntriesTask)countedCompleter;
                    ReduceEntriesTask<K, V> reduceEntriesTask2 = reduceEntriesTask.rights;
                    while (reduceEntriesTask2 != null) {
                        Map.Entry<K, V> entry = reduceEntriesTask2.result;
                        if (entry != null) {
                            Map.Entry<K, V> entry2 = reduceEntriesTask.result;
                            reduceEntriesTask.result = entry2 == null ? entry : biFun.apply(entry2, entry);
                        }
                        reduceEntriesTask2 = reduceEntriesTask.rights = reduceEntriesTask2.nextRight;
                    }
                }
            }
        }

        @Override
        public final Map.Entry<K, V> getRawResult() {
            return this.result;
        }
    }

    static final class ReduceValuesTask<K, V>
    extends BulkTask<K, V, V> {
        ReduceValuesTask<K, V> nextRight;
        final BiFun<? super V, ? super V, ? extends V> reducer;
        V result;
        ReduceValuesTask<K, V> rights;

        ReduceValuesTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, ReduceValuesTask<K, V> reduceValuesTask, BiFun<? super V, ? super V, ? extends V> biFun) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.nextRight = reduceValuesTask;
            this.reducer = biFun;
        }

        @Override
        public final void compute() {
            BiFun<V, V, V> biFun = this.reducer;
            if (biFun != null) {
                CountedCompleter<?> countedCompleter;
                int n;
                int n2;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    this.rights = new ReduceValuesTask<K, V>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, this.rights, biFun);
                    this.rights.fork();
                }
                Object a = null;
                while ((countedCompleter = this.advance()) != null) {
                    Object v = ((Node)((Object)countedCompleter)).val;
                    a = a == null ? v : biFun.apply(a, v);
                }
                this.result = a;
                for (countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    ReduceValuesTask reduceValuesTask = (ReduceValuesTask)countedCompleter;
                    ReduceValuesTask<K, V> reduceValuesTask2 = reduceValuesTask.rights;
                    while (reduceValuesTask2 != null) {
                        V v = reduceValuesTask2.result;
                        if (v != null) {
                            V v2 = reduceValuesTask.result;
                            reduceValuesTask.result = v2 == null ? v : biFun.apply(v2, v);
                        }
                        reduceValuesTask2 = reduceValuesTask.rights = reduceValuesTask2.nextRight;
                    }
                }
            }
        }

        @Override
        public final V getRawResult() {
            return this.result;
        }
    }

    public static interface ConcurrentHashMapSpliterator<T> {
        public void forEachRemaining(Action<? super T> var1);

        public ConcurrentHashMapSpliterator<T> trySplit();

        public long estimateSize();

        public boolean tryAdvance(Action<? super T> var1);
    }

    public static interface IntByIntToInt {
        public int apply(int var1, int var2);
    }

    static final class MapReduceEntriesToLongTask<K, V>
    extends BulkTask<K, V, Long> {
        MapReduceEntriesToLongTask<K, V> rights;
        long result;
        final LongByLongToLong reducer;
        final long basis;
        final ObjectToLong<Map.Entry<K, V>> transformer;
        MapReduceEntriesToLongTask<K, V> nextRight;

        @Override
        public final Long getRawResult() {
            return this.result;
        }

        MapReduceEntriesToLongTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, MapReduceEntriesToLongTask<K, V> mapReduceEntriesToLongTask, ObjectToLong<Map.Entry<K, V>> objectToLong, long l, LongByLongToLong longByLongToLong) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.nextRight = mapReduceEntriesToLongTask;
            this.transformer = objectToLong;
            this.basis = l;
            this.reducer = longByLongToLong;
        }

        @Override
        public final void compute() {
            LongByLongToLong longByLongToLong;
            ObjectToLong<Map.Entry<K, V>> objectToLong = this.transformer;
            if (objectToLong != null && (longByLongToLong = this.reducer) != null) {
                CountedCompleter<?> countedCompleter;
                int n;
                int n2;
                long l = this.basis;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    this.rights = new MapReduceEntriesToLongTask<K, V>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, this.rights, objectToLong, l, longByLongToLong);
                    this.rights.fork();
                }
                while ((countedCompleter = this.advance()) != null) {
                    l = longByLongToLong.apply(l, objectToLong.apply((Map.Entry<K, V>)((Object)countedCompleter)));
                }
                this.result = l;
                for (countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    MapReduceEntriesToLongTask mapReduceEntriesToLongTask = (MapReduceEntriesToLongTask)countedCompleter;
                    MapReduceEntriesToLongTask<K, V> mapReduceEntriesToLongTask2 = mapReduceEntriesToLongTask.rights;
                    while (mapReduceEntriesToLongTask2 != null) {
                        mapReduceEntriesToLongTask.result = longByLongToLong.apply(mapReduceEntriesToLongTask.result, mapReduceEntriesToLongTask2.result);
                        mapReduceEntriesToLongTask2 = mapReduceEntriesToLongTask.rights = mapReduceEntriesToLongTask2.nextRight;
                    }
                }
            }
        }
    }

    static final class TreeBin<K, V>
    extends Node<K, V> {
        static final int WAITER = 2;
        static final int WRITER = 1;
        volatile Thread waiter;
        private static final long LOCKSTATE;
        static final int READER = 4;
        volatile TreeNode<K, V> first;
        private static final Unsafe U;
        TreeNode<K, V> root;
        volatile int lockState;

        static <K, V> TreeNode<K, V> balanceInsertion(TreeNode<K, V> treeNode, TreeNode<K, V> treeNode2) {
            treeNode2.red = true;
            while (true) {
                TreeNode treeNode3;
                TreeNode treeNode4;
                if ((treeNode4 = treeNode2.parent) == null) {
                    treeNode2.red = false;
                    return treeNode2;
                }
                if (!treeNode4.red || (treeNode3 = treeNode4.parent) == null) {
                    return treeNode;
                }
                TreeNode treeNode5 = treeNode3.left;
                if (treeNode4 == treeNode5) {
                    TreeNode treeNode6 = treeNode3.right;
                    if (treeNode6 != null && treeNode6.red) {
                        treeNode6.red = false;
                        treeNode4.red = false;
                        treeNode3.red = true;
                        treeNode2 = treeNode3;
                        continue;
                    }
                    if (treeNode2 == treeNode4.right) {
                        treeNode2 = treeNode4;
                        treeNode = TreeBin.rotateLeft(treeNode, treeNode2);
                        treeNode4 = treeNode2.parent;
                        TreeNode treeNode7 = treeNode3 = treeNode4 == null ? null : treeNode4.parent;
                    }
                    if (treeNode4 == null) continue;
                    treeNode4.red = false;
                    if (treeNode3 == null) continue;
                    treeNode3.red = true;
                    treeNode = TreeBin.rotateRight(treeNode, treeNode3);
                    continue;
                }
                if (treeNode5 != null && treeNode5.red) {
                    treeNode5.red = false;
                    treeNode4.red = false;
                    treeNode3.red = true;
                    treeNode2 = treeNode3;
                    continue;
                }
                if (treeNode2 == treeNode4.left) {
                    treeNode2 = treeNode4;
                    treeNode = TreeBin.rotateRight(treeNode, treeNode2);
                    treeNode4 = treeNode2.parent;
                    TreeNode treeNode8 = treeNode3 = treeNode4 == null ? null : treeNode4.parent;
                }
                if (treeNode4 == null) continue;
                treeNode4.red = false;
                if (treeNode3 == null) continue;
                treeNode3.red = true;
                treeNode = TreeBin.rotateLeft(treeNode, treeNode3);
            }
        }

        final TreeNode<K, V> putTreeVal(int n, K k, V v) {
            block18: {
                TreeNode<K, V> treeNode;
                int n2;
                Class<?> clazz = null;
                TreeNode<K, V> treeNode2 = this.root;
                do {
                    if (treeNode2 == null) {
                        this.root = new TreeNode<K, V>(n, k, v, null, null);
                        this.first = this.root;
                        break block18;
                    }
                    int n3 = treeNode2.hash;
                    if (n3 > n) {
                        n2 = -1;
                    } else if (n3 < n) {
                        n2 = 1;
                    } else {
                        Object object = treeNode2.key;
                        if (object == k || object != null && k.equals(object)) {
                            return treeNode2;
                        }
                        if (clazz == null && (clazz = ConcurrentHashMapV8.comparableClassFor(k)) == null || (n2 = ConcurrentHashMapV8.compareComparables(clazz, k, object)) == 0) {
                            if (treeNode2.left == null) {
                                n2 = 1;
                            } else {
                                TreeNode treeNode3;
                                TreeNode treeNode4 = treeNode2.right;
                                if (treeNode4 == null || (treeNode3 = treeNode4.findTreeNode(n, k, clazz)) == null) {
                                    n2 = -1;
                                } else {
                                    return treeNode3;
                                }
                            }
                        }
                    }
                    treeNode = treeNode2;
                } while ((treeNode2 = n2 < 0 ? treeNode2.left : treeNode2.right) != null);
                TreeNode<K, V> treeNode5 = this.first;
                TreeNode<K, V> treeNode6 = new TreeNode<K, V>(n, k, v, treeNode5, treeNode);
                this.first = treeNode6;
                if (treeNode5 != null) {
                    treeNode5.prev = treeNode6;
                }
                if (n2 < 0) {
                    treeNode.left = treeNode6;
                } else {
                    treeNode.right = treeNode6;
                }
                if (!treeNode.red) {
                    treeNode6.red = true;
                } else {
                    this.lockRoot();
                    this.root = TreeBin.balanceInsertion(this.root, treeNode6);
                    this.unlockRoot();
                }
            }
            assert (TreeBin.checkInvariants(this.root));
            return null;
        }

        private final void contendedLock() {
            boolean bl = false;
            while (true) {
                int n;
                if (((n = this.lockState) & 1) == 0) {
                    if (!U.compareAndSwapInt(this, LOCKSTATE, n, 1)) continue;
                    if (bl) {
                        this.waiter = null;
                    }
                    return;
                }
                if ((n & 2) == 0) {
                    if (!U.compareAndSwapInt(this, LOCKSTATE, n, n | 2)) continue;
                    bl = true;
                    this.waiter = Thread.currentThread();
                    continue;
                }
                if (!bl) continue;
                LockSupport.park(this);
            }
        }

        static <K, V> boolean checkInvariants(TreeNode<K, V> treeNode) {
            TreeNode treeNode2 = treeNode.parent;
            TreeNode treeNode3 = treeNode.left;
            TreeNode treeNode4 = treeNode.right;
            TreeNode treeNode5 = treeNode.prev;
            TreeNode treeNode6 = (TreeNode)treeNode.next;
            if (treeNode5 != null && treeNode5.next != treeNode) {
                return false;
            }
            if (treeNode6 != null && treeNode6.prev != treeNode) {
                return false;
            }
            if (treeNode2 != null && treeNode != treeNode2.left && treeNode != treeNode2.right) {
                return false;
            }
            if (treeNode3 != null && (treeNode3.parent != treeNode || treeNode3.hash > treeNode.hash)) {
                return false;
            }
            if (treeNode4 != null && (treeNode4.parent != treeNode || treeNode4.hash < treeNode.hash)) {
                return false;
            }
            if (treeNode.red && treeNode3 != null && treeNode3.red && treeNode4 != null && treeNode4.red) {
                return false;
            }
            if (treeNode3 != null && !TreeBin.checkInvariants(treeNode3)) {
                return false;
            }
            return treeNode4 == null || TreeBin.checkInvariants(treeNode4);
        }

        final boolean removeTreeNode(TreeNode<K, V> treeNode) {
            TreeNode treeNode2;
            TreeNode treeNode3;
            TreeNode treeNode4;
            TreeNode treeNode5 = (TreeNode)treeNode.next;
            TreeNode treeNode6 = treeNode.prev;
            if (treeNode6 == null) {
                this.first = treeNode5;
            } else {
                treeNode6.next = treeNode5;
            }
            if (treeNode5 != null) {
                treeNode5.prev = treeNode6;
            }
            if (this.first == null) {
                this.root = null;
                return true;
            }
            TreeNode<K, V> treeNode7 = this.root;
            if (treeNode7 == null || treeNode7.right == null || (treeNode4 = treeNode7.left) == null || treeNode4.left == null) {
                return true;
            }
            this.lockRoot();
            TreeNode treeNode8 = treeNode.left;
            TreeNode treeNode9 = treeNode.right;
            if (treeNode8 != null && treeNode9 != null) {
                TreeNode treeNode10;
                treeNode3 = treeNode9;
                while ((treeNode10 = treeNode3.left) != null) {
                    treeNode3 = treeNode10;
                }
                boolean bl = treeNode3.red;
                treeNode3.red = treeNode.red;
                treeNode.red = bl;
                TreeNode treeNode11 = treeNode3.right;
                TreeNode treeNode12 = treeNode.parent;
                if (treeNode3 == treeNode9) {
                    treeNode.parent = treeNode3;
                    treeNode3.right = treeNode;
                } else {
                    TreeNode treeNode13 = treeNode3.parent;
                    treeNode.parent = treeNode13;
                    if (treeNode.parent != null) {
                        if (treeNode3 == treeNode13.left) {
                            treeNode13.left = treeNode;
                        } else {
                            treeNode13.right = treeNode;
                        }
                    }
                    treeNode3.right = treeNode9;
                    treeNode9.parent = treeNode3;
                }
                treeNode.left = null;
                treeNode3.left = treeNode8;
                treeNode8.parent = treeNode3;
                treeNode.right = treeNode11;
                if (treeNode.right != null) {
                    treeNode11.parent = treeNode;
                }
                if ((treeNode3.parent = treeNode12) == null) {
                    treeNode7 = treeNode3;
                } else if (treeNode == treeNode12.left) {
                    treeNode12.left = treeNode3;
                } else {
                    treeNode12.right = treeNode3;
                }
                treeNode2 = treeNode11 != null ? treeNode11 : treeNode;
            } else {
                treeNode2 = treeNode8 != null ? treeNode8 : (treeNode9 != null ? treeNode9 : treeNode);
            }
            if (treeNode2 != treeNode) {
                treeNode2.parent = treeNode.parent;
                treeNode3 = treeNode2.parent;
                if (treeNode3 == null) {
                    treeNode7 = treeNode2;
                } else if (treeNode == treeNode3.left) {
                    treeNode3.left = treeNode2;
                } else {
                    treeNode3.right = treeNode2;
                }
                treeNode.parent = null;
                treeNode.right = null;
                treeNode.left = null;
            }
            TreeNode<K, V> treeNode14 = this.root = treeNode.red ? treeNode7 : TreeBin.balanceDeletion(treeNode7, treeNode2);
            if (treeNode == treeNode2 && (treeNode3 = treeNode.parent) != null) {
                if (treeNode == treeNode3.left) {
                    treeNode3.left = null;
                } else if (treeNode == treeNode3.right) {
                    treeNode3.right = null;
                }
                treeNode.parent = null;
            }
            this.unlockRoot();
            assert (TreeBin.checkInvariants(this.root));
            return false;
        }

        static <K, V> TreeNode<K, V> rotateRight(TreeNode<K, V> treeNode, TreeNode<K, V> treeNode2) {
            TreeNode treeNode3;
            if (treeNode2 != null && (treeNode3 = treeNode2.left) != null) {
                treeNode2.left = treeNode3.right;
                TreeNode treeNode4 = treeNode2.left;
                if (treeNode2.left != null) {
                    treeNode4.parent = treeNode2;
                }
                TreeNode treeNode5 = treeNode3.parent = treeNode2.parent;
                if (treeNode3.parent == null) {
                    treeNode = treeNode3;
                    treeNode3.red = false;
                } else if (treeNode5.right == treeNode2) {
                    treeNode5.right = treeNode3;
                } else {
                    treeNode5.left = treeNode3;
                }
                treeNode3.right = treeNode2;
                treeNode2.parent = treeNode3;
            }
            return treeNode;
        }

        @Override
        final Node<K, V> find(int n, Object object) {
            if (object != null) {
                Node node = this.first;
                while (node != null) {
                    int n2 = this.lockState;
                    if ((n2 & 3) != 0) {
                        Object k;
                        if (node.hash == n && ((k = node.key) == object || k != null && object.equals(k))) {
                            return node;
                        }
                    } else if (U.compareAndSwapInt(this, LOCKSTATE, n2, n2 + 4)) {
                        Thread thread;
                        int n3;
                        TreeNode<K, V> treeNode;
                        TreeNode<K, V> treeNode2 = this.root;
                        TreeNode<K, V> treeNode3 = treeNode = treeNode2 == null ? null : treeNode2.findTreeNode(n, object, null);
                        while (!U.compareAndSwapInt(this, LOCKSTATE, n3 = this.lockState, n3 - 4)) {
                        }
                        if (n3 == 6 && (thread = this.waiter) != null) {
                            LockSupport.unpark(thread);
                        }
                        return treeNode;
                    }
                    node = node.next;
                }
            }
            return null;
        }

        static <K, V> TreeNode<K, V> rotateLeft(TreeNode<K, V> treeNode, TreeNode<K, V> treeNode2) {
            TreeNode treeNode3;
            if (treeNode2 != null && (treeNode3 = treeNode2.right) != null) {
                treeNode2.right = treeNode3.left;
                TreeNode treeNode4 = treeNode2.right;
                if (treeNode2.right != null) {
                    treeNode4.parent = treeNode2;
                }
                TreeNode treeNode5 = treeNode3.parent = treeNode2.parent;
                if (treeNode3.parent == null) {
                    treeNode = treeNode3;
                    treeNode3.red = false;
                } else if (treeNode5.left == treeNode2) {
                    treeNode5.left = treeNode3;
                } else {
                    treeNode5.right = treeNode3;
                }
                treeNode3.left = treeNode2;
                treeNode2.parent = treeNode3;
            }
            return treeNode;
        }

        private final void unlockRoot() {
            this.lockState = 0;
        }

        static <K, V> TreeNode<K, V> balanceDeletion(TreeNode<K, V> treeNode, TreeNode<K, V> treeNode2) {
            while (treeNode2 != null && treeNode2 != treeNode) {
                TreeNode treeNode3;
                TreeNode treeNode4;
                TreeNode treeNode5 = treeNode2.parent;
                if (treeNode5 == null) {
                    treeNode2.red = false;
                    return treeNode2;
                }
                if (treeNode2.red) {
                    treeNode2.red = false;
                    return treeNode;
                }
                TreeNode treeNode6 = treeNode5.left;
                if (treeNode6 == treeNode2) {
                    TreeNode treeNode7 = treeNode5.right;
                    if (treeNode7 != null && treeNode7.red) {
                        treeNode7.red = false;
                        treeNode5.red = true;
                        treeNode = TreeBin.rotateLeft(treeNode, treeNode5);
                        treeNode5 = treeNode2.parent;
                        TreeNode treeNode8 = treeNode7 = treeNode5 == null ? null : treeNode5.right;
                    }
                    if (treeNode7 == null) {
                        treeNode2 = treeNode5;
                        continue;
                    }
                    treeNode4 = treeNode7.left;
                    treeNode3 = treeNode7.right;
                    if (!(treeNode3 != null && treeNode3.red || treeNode4 != null && treeNode4.red)) {
                        treeNode7.red = true;
                        treeNode2 = treeNode5;
                        continue;
                    }
                    if (treeNode3 == null || !treeNode3.red) {
                        if (treeNode4 != null) {
                            treeNode4.red = false;
                        }
                        treeNode7.red = true;
                        treeNode = TreeBin.rotateRight(treeNode, treeNode7);
                        treeNode5 = treeNode2.parent;
                        TreeNode treeNode9 = treeNode7 = treeNode5 == null ? null : treeNode5.right;
                    }
                    if (treeNode7 != null) {
                        treeNode7.red = treeNode5 == null ? false : treeNode5.red;
                        treeNode3 = treeNode7.right;
                        if (treeNode3 != null) {
                            treeNode3.red = false;
                        }
                    }
                    if (treeNode5 != null) {
                        treeNode5.red = false;
                        treeNode = TreeBin.rotateLeft(treeNode, treeNode5);
                    }
                    treeNode2 = treeNode;
                    continue;
                }
                if (treeNode6 != null && treeNode6.red) {
                    treeNode6.red = false;
                    treeNode5.red = true;
                    treeNode = TreeBin.rotateRight(treeNode, treeNode5);
                    treeNode5 = treeNode2.parent;
                    TreeNode treeNode10 = treeNode6 = treeNode5 == null ? null : treeNode5.left;
                }
                if (treeNode6 == null) {
                    treeNode2 = treeNode5;
                    continue;
                }
                treeNode4 = treeNode6.left;
                treeNode3 = treeNode6.right;
                if (!(treeNode4 != null && treeNode4.red || treeNode3 != null && treeNode3.red)) {
                    treeNode6.red = true;
                    treeNode2 = treeNode5;
                    continue;
                }
                if (treeNode4 == null || !treeNode4.red) {
                    if (treeNode3 != null) {
                        treeNode3.red = false;
                    }
                    treeNode6.red = true;
                    treeNode = TreeBin.rotateLeft(treeNode, treeNode6);
                    treeNode5 = treeNode2.parent;
                    TreeNode treeNode11 = treeNode6 = treeNode5 == null ? null : treeNode5.left;
                }
                if (treeNode6 != null) {
                    treeNode6.red = treeNode5 == null ? false : treeNode5.red;
                    treeNode4 = treeNode6.left;
                    if (treeNode4 != null) {
                        treeNode4.red = false;
                    }
                }
                if (treeNode5 != null) {
                    treeNode5.red = false;
                    treeNode = TreeBin.rotateRight(treeNode, treeNode5);
                }
                treeNode2 = treeNode;
            }
            return treeNode;
        }

        TreeBin(TreeNode<K, V> treeNode) {
            super(-2, null, null, null);
            this.first = treeNode;
            TreeNode treeNode2 = null;
            TreeNode treeNode3 = treeNode;
            while (treeNode3 != null) {
                TreeNode treeNode4 = (TreeNode)treeNode3.next;
                treeNode3.right = null;
                treeNode3.left = null;
                if (treeNode2 == null) {
                    treeNode3.parent = null;
                    treeNode3.red = false;
                    treeNode2 = treeNode3;
                } else {
                    TreeNode treeNode5;
                    int n;
                    Object object = treeNode3.key;
                    int n2 = treeNode3.hash;
                    Class<?> clazz = null;
                    TreeNode treeNode6 = treeNode2;
                    do {
                        int n3;
                        n = (n3 = treeNode6.hash) > n2 ? -1 : (n3 < n2 ? 1 : (clazz != null || (clazz = ConcurrentHashMapV8.comparableClassFor(object)) != null ? ConcurrentHashMapV8.compareComparables(clazz, object, treeNode6.key) : 0));
                        treeNode5 = treeNode6;
                    } while ((treeNode6 = n <= 0 ? treeNode6.left : treeNode6.right) != null);
                    treeNode3.parent = treeNode5;
                    if (n <= 0) {
                        treeNode5.left = treeNode3;
                    } else {
                        treeNode5.right = treeNode3;
                    }
                    treeNode2 = TreeBin.balanceInsertion(treeNode2, treeNode3);
                }
                treeNode3 = treeNode4;
            }
            this.root = treeNode2;
        }

        private final void lockRoot() {
            if (!U.compareAndSwapInt(this, LOCKSTATE, 0, 1)) {
                this.contendedLock();
            }
        }

        static {
            try {
                U = ConcurrentHashMapV8.getUnsafe();
                Class<TreeBin> clazz = TreeBin.class;
                LOCKSTATE = U.objectFieldOffset(clazz.getDeclaredField("lockState"));
            }
            catch (Exception exception) {
                throw new Error(exception);
            }
        }
    }

    static final class MapReduceEntriesToDoubleTask<K, V>
    extends BulkTask<K, V, Double> {
        MapReduceEntriesToDoubleTask<K, V> rights;
        final DoubleByDoubleToDouble reducer;
        final ObjectToDouble<Map.Entry<K, V>> transformer;
        MapReduceEntriesToDoubleTask<K, V> nextRight;
        final double basis;
        double result;

        @Override
        public final Double getRawResult() {
            return this.result;
        }

        MapReduceEntriesToDoubleTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, MapReduceEntriesToDoubleTask<K, V> mapReduceEntriesToDoubleTask, ObjectToDouble<Map.Entry<K, V>> objectToDouble, double d, DoubleByDoubleToDouble doubleByDoubleToDouble) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.nextRight = mapReduceEntriesToDoubleTask;
            this.transformer = objectToDouble;
            this.basis = d;
            this.reducer = doubleByDoubleToDouble;
        }

        @Override
        public final void compute() {
            DoubleByDoubleToDouble doubleByDoubleToDouble;
            ObjectToDouble<Map.Entry<K, V>> objectToDouble = this.transformer;
            if (objectToDouble != null && (doubleByDoubleToDouble = this.reducer) != null) {
                CountedCompleter<?> countedCompleter;
                int n;
                int n2;
                double d = this.basis;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    this.rights = new MapReduceEntriesToDoubleTask<K, V>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, this.rights, objectToDouble, d, doubleByDoubleToDouble);
                    this.rights.fork();
                }
                while ((countedCompleter = this.advance()) != null) {
                    d = doubleByDoubleToDouble.apply(d, objectToDouble.apply((Map.Entry<K, V>)((Object)countedCompleter)));
                }
                this.result = d;
                for (countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    MapReduceEntriesToDoubleTask mapReduceEntriesToDoubleTask = (MapReduceEntriesToDoubleTask)countedCompleter;
                    MapReduceEntriesToDoubleTask<K, V> mapReduceEntriesToDoubleTask2 = mapReduceEntriesToDoubleTask.rights;
                    while (mapReduceEntriesToDoubleTask2 != null) {
                        mapReduceEntriesToDoubleTask.result = doubleByDoubleToDouble.apply(mapReduceEntriesToDoubleTask.result, mapReduceEntriesToDoubleTask2.result);
                        mapReduceEntriesToDoubleTask2 = mapReduceEntriesToDoubleTask.rights = mapReduceEntriesToDoubleTask2.nextRight;
                    }
                }
            }
        }
    }

    static final class MapReduceKeysToLongTask<K, V>
    extends BulkTask<K, V, Long> {
        final long basis;
        long result;
        MapReduceKeysToLongTask<K, V> rights;
        final LongByLongToLong reducer;
        MapReduceKeysToLongTask<K, V> nextRight;
        final ObjectToLong<? super K> transformer;

        @Override
        public final Long getRawResult() {
            return this.result;
        }

        MapReduceKeysToLongTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, MapReduceKeysToLongTask<K, V> mapReduceKeysToLongTask, ObjectToLong<? super K> objectToLong, long l, LongByLongToLong longByLongToLong) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.nextRight = mapReduceKeysToLongTask;
            this.transformer = objectToLong;
            this.basis = l;
            this.reducer = longByLongToLong;
        }

        @Override
        public final void compute() {
            LongByLongToLong longByLongToLong;
            ObjectToLong<K> objectToLong = this.transformer;
            if (objectToLong != null && (longByLongToLong = this.reducer) != null) {
                CountedCompleter<?> countedCompleter;
                int n;
                int n2;
                long l = this.basis;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    this.rights = new MapReduceKeysToLongTask<K, V>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, this.rights, objectToLong, l, longByLongToLong);
                    this.rights.fork();
                }
                while ((countedCompleter = this.advance()) != null) {
                    l = longByLongToLong.apply(l, objectToLong.apply(((Node)((Object)countedCompleter)).key));
                }
                this.result = l;
                for (countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    MapReduceKeysToLongTask mapReduceKeysToLongTask = (MapReduceKeysToLongTask)countedCompleter;
                    MapReduceKeysToLongTask<K, V> mapReduceKeysToLongTask2 = mapReduceKeysToLongTask.rights;
                    while (mapReduceKeysToLongTask2 != null) {
                        mapReduceKeysToLongTask.result = longByLongToLong.apply(mapReduceKeysToLongTask.result, mapReduceKeysToLongTask2.result);
                        mapReduceKeysToLongTask2 = mapReduceKeysToLongTask.rights = mapReduceKeysToLongTask2.nextRight;
                    }
                }
            }
        }
    }

    public static interface ObjectToDouble<A> {
        public double apply(A var1);
    }

    static final class ForwardingNode<K, V>
    extends Node<K, V> {
        final Node<K, V>[] nextTable;

        @Override
        Node<K, V> find(int n, Object object) {
            Node<K, V>[] nodeArray = this.nextTable;
            block0: while (true) {
                Node<K, V> node;
                int n2;
                if (object == null || nodeArray == null || (n2 = nodeArray.length) == 0 || (node = ConcurrentHashMapV8.tabAt(nodeArray, n2 - 1 & n)) == null) {
                    return null;
                }
                do {
                    Object k;
                    int n3;
                    if ((n3 = node.hash) == n && ((k = node.key) == object || k != null && object.equals(k))) {
                        return node;
                    }
                    if (n3 >= 0) continue;
                    if (node instanceof ForwardingNode) {
                        nodeArray = ((ForwardingNode)node).nextTable;
                        continue block0;
                    }
                    return node.find(n, object);
                } while ((node = node.next) != null);
                break;
            }
            return null;
        }

        ForwardingNode(Node<K, V>[] nodeArray) {
            super(-1, null, null, null);
            this.nextTable = nodeArray;
        }
    }

    public static interface DoubleByDoubleToDouble {
        public double apply(double var1, double var3);
    }

    static final class MapReduceMappingsTask<K, V, U>
    extends BulkTask<K, V, U> {
        final BiFun<? super K, ? super V, ? extends U> transformer;
        U result;
        MapReduceMappingsTask<K, V, U> nextRight;
        final BiFun<? super U, ? super U, ? extends U> reducer;
        MapReduceMappingsTask<K, V, U> rights;

        MapReduceMappingsTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, MapReduceMappingsTask<K, V, U> mapReduceMappingsTask, BiFun<? super K, ? super V, ? extends U> biFun, BiFun<? super U, ? super U, ? extends U> biFun2) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.nextRight = mapReduceMappingsTask;
            this.transformer = biFun;
            this.reducer = biFun2;
        }

        @Override
        public final U getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            BiFun<U, U, U> biFun;
            BiFun<K, V, U> biFun2 = this.transformer;
            if (biFun2 != null && (biFun = this.reducer) != null) {
                CountedCompleter<?> countedCompleter;
                int n;
                int n2;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    this.rights = new MapReduceMappingsTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, this.rights, biFun2, biFun);
                    this.rights.fork();
                }
                Object a = null;
                while ((countedCompleter = this.advance()) != null) {
                    U u = biFun2.apply(((Node)((Object)countedCompleter)).key, ((Node)((Object)countedCompleter)).val);
                    if (u == null) continue;
                    a = a == null ? u : biFun.apply(a, u);
                }
                this.result = a;
                for (countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    MapReduceMappingsTask mapReduceMappingsTask = (MapReduceMappingsTask)countedCompleter;
                    MapReduceMappingsTask<K, V, U> mapReduceMappingsTask2 = mapReduceMappingsTask.rights;
                    while (mapReduceMappingsTask2 != null) {
                        U u = mapReduceMappingsTask2.result;
                        if (u != null) {
                            U u2 = mapReduceMappingsTask.result;
                            mapReduceMappingsTask.result = u2 == null ? u : biFun.apply(u2, u);
                        }
                        mapReduceMappingsTask2 = mapReduceMappingsTask.rights = mapReduceMappingsTask2.nextRight;
                    }
                }
            }
        }
    }

    static final class ForEachEntryTask<K, V>
    extends BulkTask<K, V, Void> {
        final Action<? super Map.Entry<K, V>> action;

        @Override
        public final void compute() {
            Action<Map.Entry<K, V>> action = this.action;
            if (action != null) {
                Node node;
                int n;
                int n2;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    new ForEachEntryTask<K, V>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, action).fork();
                }
                while ((node = this.advance()) != null) {
                    action.apply(node);
                }
                this.propagateCompletion();
            }
        }

        ForEachEntryTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, Action<? super Map.Entry<K, V>> action) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.action = action;
        }
    }

    public static interface BiFun<A, B, T> {
        public T apply(A var1, B var2);
    }

    static final class CounterCell {
        volatile long p6;
        volatile long p5;
        volatile long q0;
        volatile long q3;
        volatile long p3;
        volatile long p1;
        volatile long p2;
        volatile long q6;
        volatile long q4;
        volatile long p0;
        volatile long q5;
        volatile long value;
        volatile long q1;
        volatile long q2;
        volatile long p4;

        CounterCell(long l) {
            this.value = l;
        }
    }

    static final class SearchValuesTask<K, V, U>
    extends BulkTask<K, V, U> {
        final Fun<? super V, ? extends U> searchFunction;
        final AtomicReference<U> result;

        SearchValuesTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, Fun<? super V, ? extends U> fun, AtomicReference<U> atomicReference) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.searchFunction = fun;
            this.result = atomicReference;
        }

        @Override
        public final void compute() {
            AtomicReference<U> atomicReference;
            Fun<V, U> fun = this.searchFunction;
            if (fun != null && (atomicReference = this.result) != null) {
                int n;
                int n2;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    if (atomicReference.get() != null) {
                        return;
                    }
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    new SearchValuesTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, fun, atomicReference).fork();
                }
                while (atomicReference.get() == null) {
                    Node node = this.advance();
                    if (node == null) {
                        this.propagateCompletion();
                        break;
                    }
                    U u = fun.apply(node.val);
                    if (u == null) continue;
                    if (!atomicReference.compareAndSet(null, u)) break;
                    this.quietlyCompleteRoot();
                    break;
                }
            }
        }

        @Override
        public final U getRawResult() {
            return this.result.get();
        }
    }

    static final class SearchEntriesTask<K, V, U>
    extends BulkTask<K, V, U> {
        final Fun<Map.Entry<K, V>, ? extends U> searchFunction;
        final AtomicReference<U> result;

        SearchEntriesTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, Fun<Map.Entry<K, V>, ? extends U> fun, AtomicReference<U> atomicReference) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.searchFunction = fun;
            this.result = atomicReference;
        }

        @Override
        public final U getRawResult() {
            return this.result.get();
        }

        @Override
        public final void compute() {
            AtomicReference<U> atomicReference;
            Fun<Map.Entry<K, V>, U> fun = this.searchFunction;
            if (fun != null && (atomicReference = this.result) != null) {
                int n;
                int n2;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    if (atomicReference.get() != null) {
                        return;
                    }
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    new SearchEntriesTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, fun, atomicReference).fork();
                }
                while (atomicReference.get() == null) {
                    Node node = this.advance();
                    if (node == null) {
                        this.propagateCompletion();
                        break;
                    }
                    U u = fun.apply(node);
                    if (u == null) continue;
                    if (atomicReference.compareAndSet(null, u)) {
                        this.quietlyCompleteRoot();
                    }
                    return;
                }
            }
        }
    }

    static final class MapReduceValuesToLongTask<K, V>
    extends BulkTask<K, V, Long> {
        final long basis;
        long result;
        MapReduceValuesToLongTask<K, V> rights;
        final LongByLongToLong reducer;
        final ObjectToLong<? super V> transformer;
        MapReduceValuesToLongTask<K, V> nextRight;

        MapReduceValuesToLongTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, MapReduceValuesToLongTask<K, V> mapReduceValuesToLongTask, ObjectToLong<? super V> objectToLong, long l, LongByLongToLong longByLongToLong) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.nextRight = mapReduceValuesToLongTask;
            this.transformer = objectToLong;
            this.basis = l;
            this.reducer = longByLongToLong;
        }

        @Override
        public final Long getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            LongByLongToLong longByLongToLong;
            ObjectToLong<V> objectToLong = this.transformer;
            if (objectToLong != null && (longByLongToLong = this.reducer) != null) {
                CountedCompleter<?> countedCompleter;
                int n;
                int n2;
                long l = this.basis;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    this.rights = new MapReduceValuesToLongTask<K, V>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, this.rights, objectToLong, l, longByLongToLong);
                    this.rights.fork();
                }
                while ((countedCompleter = this.advance()) != null) {
                    l = longByLongToLong.apply(l, objectToLong.apply(((Node)((Object)countedCompleter)).val));
                }
                this.result = l;
                for (countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    MapReduceValuesToLongTask mapReduceValuesToLongTask = (MapReduceValuesToLongTask)countedCompleter;
                    MapReduceValuesToLongTask<K, V> mapReduceValuesToLongTask2 = mapReduceValuesToLongTask.rights;
                    while (mapReduceValuesToLongTask2 != null) {
                        mapReduceValuesToLongTask.result = longByLongToLong.apply(mapReduceValuesToLongTask.result, mapReduceValuesToLongTask2.result);
                        mapReduceValuesToLongTask2 = mapReduceValuesToLongTask.rights = mapReduceValuesToLongTask2.nextRight;
                    }
                }
            }
        }
    }

    static class Segment<K, V>
    extends ReentrantLock
    implements Serializable {
        private static final long serialVersionUID = 2249069246763182397L;
        final float loadFactor;

        Segment(float f) {
            this.loadFactor = f;
        }
    }

    static final class ForEachValueTask<K, V>
    extends BulkTask<K, V, Void> {
        final Action<? super V> action;

        @Override
        public final void compute() {
            Action<V> action = this.action;
            if (action != null) {
                Node node;
                int n;
                int n2;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    new ForEachValueTask<K, V>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, action).fork();
                }
                while ((node = this.advance()) != null) {
                    action.apply(node.val);
                }
                this.propagateCompletion();
            }
        }

        ForEachValueTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, Action<? super V> action) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.action = action;
        }
    }

    static final class ForEachMappingTask<K, V>
    extends BulkTask<K, V, Void> {
        final BiAction<? super K, ? super V> action;

        @Override
        public final void compute() {
            BiAction<K, V> biAction = this.action;
            if (biAction != null) {
                Node node;
                int n;
                int n2;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    new ForEachMappingTask<K, V>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, biAction).fork();
                }
                while ((node = this.advance()) != null) {
                    biAction.apply(node.key, node.val);
                }
                this.propagateCompletion();
            }
        }

        ForEachMappingTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, BiAction<? super K, ? super V> biAction) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.action = biAction;
        }
    }

    public static interface ObjectByObjectToLong<A, B> {
        public long apply(A var1, B var2);
    }

    static final class TreeNode<K, V>
    extends Node<K, V> {
        TreeNode<K, V> parent;
        TreeNode<K, V> right;
        boolean red;
        TreeNode<K, V> prev;
        TreeNode<K, V> left;

        final TreeNode<K, V> findTreeNode(int n, Object object, Class<?> clazz) {
            if (object != null) {
                TreeNode<K, V> treeNode = this;
                do {
                    TreeNode<K, V> treeNode2;
                    int n2;
                    TreeNode<K, V> treeNode3 = treeNode.left;
                    TreeNode<K, V> treeNode4 = treeNode.right;
                    int n3 = treeNode.hash;
                    if (n3 > n) {
                        treeNode = treeNode3;
                        continue;
                    }
                    if (n3 < n) {
                        treeNode = treeNode4;
                        continue;
                    }
                    Object object2 = treeNode.key;
                    if (object2 == object || object2 != null && object.equals(object2)) {
                        return treeNode;
                    }
                    if (treeNode3 == null && treeNode4 == null) break;
                    if ((clazz != null || (clazz = ConcurrentHashMapV8.comparableClassFor(object)) != null) && (n2 = ConcurrentHashMapV8.compareComparables(clazz, object, object2)) != 0) {
                        treeNode = n2 < 0 ? treeNode3 : treeNode4;
                        continue;
                    }
                    if (treeNode3 == null) {
                        treeNode = treeNode4;
                        continue;
                    }
                    if (treeNode4 == null || (treeNode2 = treeNode4.findTreeNode(n, object, clazz)) == null) {
                        treeNode = treeNode3;
                        continue;
                    }
                    return treeNode2;
                } while (treeNode != null);
            }
            return null;
        }

        @Override
        Node<K, V> find(int n, Object object) {
            return this.findTreeNode(n, object, null);
        }

        TreeNode(int n, K k, V v, Node<K, V> node, TreeNode<K, V> treeNode) {
            super(n, k, v, node);
            this.parent = treeNode;
        }
    }

    static final class ForEachTransformedMappingTask<K, V, U>
    extends BulkTask<K, V, Void> {
        final BiFun<? super K, ? super V, ? extends U> transformer;
        final Action<? super U> action;

        @Override
        public final void compute() {
            Action<U> action;
            BiFun<K, V, U> biFun = this.transformer;
            if (biFun != null && (action = this.action) != null) {
                Node node;
                int n;
                int n2;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    new ForEachTransformedMappingTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, biFun, action).fork();
                }
                while ((node = this.advance()) != null) {
                    U u = biFun.apply(node.key, node.val);
                    if (u == null) continue;
                    action.apply(u);
                }
                this.propagateCompletion();
            }
        }

        ForEachTransformedMappingTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, BiFun<? super K, ? super V, ? extends U> biFun, Action<? super U> action) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.transformer = biFun;
            this.action = action;
        }
    }

    static final class ValuesView<K, V>
    extends CollectionView<K, V, V>
    implements Collection<V>,
    Serializable {
        private static final long serialVersionUID = 2249069246763182397L;

        @Override
        public final boolean remove(Object object) {
            if (object != null) {
                Iterator<V> iterator = this.iterator();
                while (iterator.hasNext()) {
                    if (!object.equals(iterator.next())) continue;
                    iterator.remove();
                    return true;
                }
            }
            return false;
        }

        @Override
        public final Iterator<V> iterator() {
            ConcurrentHashMapV8 concurrentHashMapV8 = this.map;
            Node<K, V>[] nodeArray = concurrentHashMapV8.table;
            int n = concurrentHashMapV8.table == null ? 0 : nodeArray.length;
            return new ValueIterator(nodeArray, n, 0, n, concurrentHashMapV8);
        }

        @Override
        public final boolean add(V v) {
            throw new UnsupportedOperationException();
        }

        @Override
        public final boolean contains(Object object) {
            return this.map.containsValue(object);
        }

        @Override
        public final boolean addAll(Collection<? extends V> collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void forEach(Action<? super V> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            Node<K, V>[] nodeArray = this.map.table;
            if (this.map.table != null) {
                Node node;
                Traverser traverser = new Traverser(nodeArray, nodeArray.length, 0, nodeArray.length);
                while ((node = traverser.advance()) != null) {
                    action.apply(node.val);
                }
            }
        }

        public ConcurrentHashMapSpliterator<V> spliterator166() {
            ConcurrentHashMapV8 concurrentHashMapV8 = this.map;
            long l = concurrentHashMapV8.sumCount();
            Node<K, V>[] nodeArray = concurrentHashMapV8.table;
            int n = concurrentHashMapV8.table == null ? 0 : nodeArray.length;
            return new ValueSpliterator(nodeArray, n, 0, n, l < 0L ? 0L : l);
        }

        ValuesView(ConcurrentHashMapV8<K, V> concurrentHashMapV8) {
            super(concurrentHashMapV8);
        }
    }

    static final class KeyIterator<K, V>
    extends BaseIterator<K, V>
    implements Iterator<K>,
    Enumeration<K> {
        @Override
        public final K nextElement() {
            return this.next();
        }

        @Override
        public final K next() {
            Node node = this.next;
            if (node == null) {
                throw new NoSuchElementException();
            }
            Object k = node.key;
            this.lastReturned = node;
            this.advance();
            return k;
        }

        KeyIterator(Node<K, V>[] nodeArray, int n, int n2, int n3, ConcurrentHashMapV8<K, V> concurrentHashMapV8) {
            super(nodeArray, n, n2, n3, concurrentHashMapV8);
        }
    }

    static final class ForEachTransformedKeyTask<K, V, U>
    extends BulkTask<K, V, Void> {
        final Fun<? super K, ? extends U> transformer;
        final Action<? super U> action;

        ForEachTransformedKeyTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, Fun<? super K, ? extends U> fun, Action<? super U> action) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.transformer = fun;
            this.action = action;
        }

        @Override
        public final void compute() {
            Action<U> action;
            Fun<K, U> fun = this.transformer;
            if (fun != null && (action = this.action) != null) {
                Node node;
                int n;
                int n2;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    new ForEachTransformedKeyTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, fun, action).fork();
                }
                while ((node = this.advance()) != null) {
                    U u = fun.apply(node.key);
                    if (u == null) continue;
                    action.apply(u);
                }
                this.propagateCompletion();
            }
        }
    }

    static final class MapReduceMappingsToLongTask<K, V>
    extends BulkTask<K, V, Long> {
        final LongByLongToLong reducer;
        long result;
        final ObjectByObjectToLong<? super K, ? super V> transformer;
        MapReduceMappingsToLongTask<K, V> rights;
        final long basis;
        MapReduceMappingsToLongTask<K, V> nextRight;

        @Override
        public final void compute() {
            LongByLongToLong longByLongToLong;
            ObjectByObjectToLong<K, V> objectByObjectToLong = this.transformer;
            if (objectByObjectToLong != null && (longByLongToLong = this.reducer) != null) {
                CountedCompleter<?> countedCompleter;
                int n;
                int n2;
                long l = this.basis;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    this.rights = new MapReduceMappingsToLongTask<K, V>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, this.rights, objectByObjectToLong, l, longByLongToLong);
                    this.rights.fork();
                }
                while ((countedCompleter = this.advance()) != null) {
                    l = longByLongToLong.apply(l, objectByObjectToLong.apply(((Node)((Object)countedCompleter)).key, ((Node)((Object)countedCompleter)).val));
                }
                this.result = l;
                for (countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    MapReduceMappingsToLongTask mapReduceMappingsToLongTask = (MapReduceMappingsToLongTask)countedCompleter;
                    MapReduceMappingsToLongTask<K, V> mapReduceMappingsToLongTask2 = mapReduceMappingsToLongTask.rights;
                    while (mapReduceMappingsToLongTask2 != null) {
                        mapReduceMappingsToLongTask.result = longByLongToLong.apply(mapReduceMappingsToLongTask.result, mapReduceMappingsToLongTask2.result);
                        mapReduceMappingsToLongTask2 = mapReduceMappingsToLongTask.rights = mapReduceMappingsToLongTask2.nextRight;
                    }
                }
            }
        }

        MapReduceMappingsToLongTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, MapReduceMappingsToLongTask<K, V> mapReduceMappingsToLongTask, ObjectByObjectToLong<? super K, ? super V> objectByObjectToLong, long l, LongByLongToLong longByLongToLong) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.nextRight = mapReduceMappingsToLongTask;
            this.transformer = objectByObjectToLong;
            this.basis = l;
            this.reducer = longByLongToLong;
        }

        @Override
        public final Long getRawResult() {
            return this.result;
        }
    }

    static final class MapEntry<K, V>
    implements Map.Entry<K, V> {
        final K key;
        final ConcurrentHashMapV8<K, V> map;
        V val;

        public String toString() {
            return this.key + "=" + this.val;
        }

        @Override
        public boolean equals(Object object) {
            Object v;
            Map.Entry entry;
            Object k;
            return !(!(object instanceof Map.Entry) || (k = (entry = (Map.Entry)object).getKey()) == null || (v = entry.getValue()) == null || k != this.key && !k.equals(this.key) || v != this.val && !v.equals(this.val));
        }

        @Override
        public int hashCode() {
            return this.key.hashCode() ^ this.val.hashCode();
        }

        @Override
        public V setValue(V v) {
            if (v == null) {
                throw new NullPointerException();
            }
            V v2 = this.val;
            this.val = v;
            this.map.put(this.key, v);
            return v2;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.val;
        }

        MapEntry(K k, V v, ConcurrentHashMapV8<K, V> concurrentHashMapV8) {
            this.key = k;
            this.val = v;
            this.map = concurrentHashMapV8;
        }
    }

    public static interface BiAction<A, B> {
        public void apply(A var1, B var2);
    }

    static final class CounterHashCode {
        int code;

        CounterHashCode() {
        }
    }

    static final class ReduceKeysTask<K, V>
    extends BulkTask<K, V, K> {
        final BiFun<? super K, ? super K, ? extends K> reducer;
        ReduceKeysTask<K, V> rights;
        ReduceKeysTask<K, V> nextRight;
        K result;

        @Override
        public final K getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            BiFun<K, K, K> biFun = this.reducer;
            if (biFun != null) {
                CountedCompleter<?> countedCompleter;
                int n;
                int n2;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    this.rights = new ReduceKeysTask<K, V>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, this.rights, biFun);
                    this.rights.fork();
                }
                Object a = null;
                while ((countedCompleter = this.advance()) != null) {
                    Object k = ((Node)((Object)countedCompleter)).key;
                    a = a == null ? k : (k == null ? a : biFun.apply(a, k));
                }
                this.result = a;
                for (countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    ReduceKeysTask reduceKeysTask = (ReduceKeysTask)countedCompleter;
                    ReduceKeysTask<K, V> reduceKeysTask2 = reduceKeysTask.rights;
                    while (reduceKeysTask2 != null) {
                        K k = reduceKeysTask2.result;
                        if (k != null) {
                            K k2 = reduceKeysTask.result;
                            reduceKeysTask.result = k2 == null ? k : biFun.apply(k2, k);
                        }
                        reduceKeysTask2 = reduceKeysTask.rights = reduceKeysTask2.nextRight;
                    }
                }
            }
        }

        ReduceKeysTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, ReduceKeysTask<K, V> reduceKeysTask, BiFun<? super K, ? super K, ? extends K> biFun) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.nextRight = reduceKeysTask;
            this.reducer = biFun;
        }
    }

    public static interface ObjectToLong<A> {
        public long apply(A var1);
    }

    static final class EntryIterator<K, V>
    extends BaseIterator<K, V>
    implements Iterator<Map.Entry<K, V>> {
        EntryIterator(Node<K, V>[] nodeArray, int n, int n2, int n3, ConcurrentHashMapV8<K, V> concurrentHashMapV8) {
            super(nodeArray, n, n2, n3, concurrentHashMapV8);
        }

        @Override
        public final Map.Entry<K, V> next() {
            Node node = this.next;
            if (node == null) {
                throw new NoSuchElementException();
            }
            Object k = node.key;
            Object v = node.val;
            this.lastReturned = node;
            this.advance();
            return new MapEntry(k, v, this.map);
        }
    }

    static final class MapReduceEntriesToIntTask<K, V>
    extends BulkTask<K, V, Integer> {
        MapReduceEntriesToIntTask<K, V> nextRight;
        final ObjectToInt<Map.Entry<K, V>> transformer;
        MapReduceEntriesToIntTask<K, V> rights;
        final int basis;
        final IntByIntToInt reducer;
        int result;

        MapReduceEntriesToIntTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, MapReduceEntriesToIntTask<K, V> mapReduceEntriesToIntTask, ObjectToInt<Map.Entry<K, V>> objectToInt, int n4, IntByIntToInt intByIntToInt) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.nextRight = mapReduceEntriesToIntTask;
            this.transformer = objectToInt;
            this.basis = n4;
            this.reducer = intByIntToInt;
        }

        @Override
        public final Integer getRawResult() {
            return this.result;
        }

        @Override
        public final void compute() {
            IntByIntToInt intByIntToInt;
            ObjectToInt<Map.Entry<K, V>> objectToInt = this.transformer;
            if (objectToInt != null && (intByIntToInt = this.reducer) != null) {
                CountedCompleter<?> countedCompleter;
                int n;
                int n2;
                int n3 = this.basis;
                int n4 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n4 >>> 1) > n4) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    this.rights = new MapReduceEntriesToIntTask<K, V>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, this.rights, objectToInt, n3, intByIntToInt);
                    this.rights.fork();
                }
                while ((countedCompleter = this.advance()) != null) {
                    n3 = intByIntToInt.apply(n3, objectToInt.apply((Map.Entry<K, V>)((Object)countedCompleter)));
                }
                this.result = n3;
                for (countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    MapReduceEntriesToIntTask mapReduceEntriesToIntTask = (MapReduceEntriesToIntTask)countedCompleter;
                    MapReduceEntriesToIntTask<K, V> mapReduceEntriesToIntTask2 = mapReduceEntriesToIntTask.rights;
                    while (mapReduceEntriesToIntTask2 != null) {
                        mapReduceEntriesToIntTask.result = intByIntToInt.apply(mapReduceEntriesToIntTask.result, mapReduceEntriesToIntTask2.result);
                        mapReduceEntriesToIntTask2 = mapReduceEntriesToIntTask.rights = mapReduceEntriesToIntTask2.nextRight;
                    }
                }
            }
        }
    }

    static final class MapReduceKeysTask<K, V, U>
    extends BulkTask<K, V, U> {
        U result;
        final BiFun<? super U, ? super U, ? extends U> reducer;
        MapReduceKeysTask<K, V, U> nextRight;
        MapReduceKeysTask<K, V, U> rights;
        final Fun<? super K, ? extends U> transformer;

        @Override
        public final void compute() {
            BiFun<U, U, U> biFun;
            Fun<K, U> fun = this.transformer;
            if (fun != null && (biFun = this.reducer) != null) {
                CountedCompleter<?> countedCompleter;
                int n;
                int n2;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    this.rights = new MapReduceKeysTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, this.rights, fun, biFun);
                    this.rights.fork();
                }
                Object a = null;
                while ((countedCompleter = this.advance()) != null) {
                    U u = fun.apply(((Node)((Object)countedCompleter)).key);
                    if (u == null) continue;
                    a = a == null ? u : biFun.apply(a, u);
                }
                this.result = a;
                for (countedCompleter = this.firstComplete(); countedCompleter != null; countedCompleter = countedCompleter.nextComplete()) {
                    MapReduceKeysTask mapReduceKeysTask = (MapReduceKeysTask)countedCompleter;
                    MapReduceKeysTask<K, V, U> mapReduceKeysTask2 = mapReduceKeysTask.rights;
                    while (mapReduceKeysTask2 != null) {
                        U u = mapReduceKeysTask2.result;
                        if (u != null) {
                            U u2 = mapReduceKeysTask.result;
                            mapReduceKeysTask.result = u2 == null ? u : biFun.apply(u2, u);
                        }
                        mapReduceKeysTask2 = mapReduceKeysTask.rights = mapReduceKeysTask2.nextRight;
                    }
                }
            }
        }

        @Override
        public final U getRawResult() {
            return this.result;
        }

        MapReduceKeysTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, MapReduceKeysTask<K, V, U> mapReduceKeysTask, Fun<? super K, ? extends U> fun, BiFun<? super U, ? super U, ? extends U> biFun) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.nextRight = mapReduceKeysTask;
            this.transformer = fun;
            this.reducer = biFun;
        }
    }

    static final class SearchKeysTask<K, V, U>
    extends BulkTask<K, V, U> {
        final AtomicReference<U> result;
        final Fun<? super K, ? extends U> searchFunction;

        @Override
        public final U getRawResult() {
            return this.result.get();
        }

        @Override
        public final void compute() {
            AtomicReference<U> atomicReference;
            Fun<K, U> fun = this.searchFunction;
            if (fun != null && (atomicReference = this.result) != null) {
                int n;
                int n2;
                int n3 = this.baseIndex;
                while (this.batch > 0 && (n2 = (n = this.baseLimit) + n3 >>> 1) > n3) {
                    if (atomicReference.get() != null) {
                        return;
                    }
                    this.addToPendingCount(1);
                    this.baseLimit = n2;
                    new SearchKeysTask<K, V, U>(this, this.batch >>>= 1, this.baseLimit, n, this.tab, fun, atomicReference).fork();
                }
                while (atomicReference.get() == null) {
                    Node node = this.advance();
                    if (node == null) {
                        this.propagateCompletion();
                        break;
                    }
                    U u = fun.apply(node.key);
                    if (u == null) continue;
                    if (!atomicReference.compareAndSet(null, u)) break;
                    this.quietlyCompleteRoot();
                    break;
                }
            }
        }

        SearchKeysTask(BulkTask<K, V, ?> bulkTask, int n, int n2, int n3, Node<K, V>[] nodeArray, Fun<? super K, ? extends U> fun, AtomicReference<U> atomicReference) {
            super(bulkTask, n, n2, n3, nodeArray);
            this.searchFunction = fun;
            this.result = atomicReference;
        }
    }

    static final class ValueIterator<K, V>
    extends BaseIterator<K, V>
    implements Iterator<V>,
    Enumeration<V> {
        @Override
        public final V nextElement() {
            return this.next();
        }

        @Override
        public final V next() {
            Node node = this.next;
            if (node == null) {
                throw new NoSuchElementException();
            }
            Object v = node.val;
            this.lastReturned = node;
            this.advance();
            return v;
        }

        ValueIterator(Node<K, V>[] nodeArray, int n, int n2, int n3, ConcurrentHashMapV8<K, V> concurrentHashMapV8) {
            super(nodeArray, n, n2, n3, concurrentHashMapV8);
        }
    }
}

