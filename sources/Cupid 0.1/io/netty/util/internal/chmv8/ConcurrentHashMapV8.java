package io.netty.util.internal.chmv8;

import io.netty.util.internal.IntegerHolder;
import io.netty.util.internal.InternalThreadLocalMap;
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

public class ConcurrentHashMapV8<K, V> implements ConcurrentMap<K, V>, Serializable {
  private static final long serialVersionUID = 7249069246763182397L;
  
  private static final int MAXIMUM_CAPACITY = 1073741824;
  
  private static final int DEFAULT_CAPACITY = 16;
  
  static final int MAX_ARRAY_SIZE = 2147483639;
  
  private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
  
  private static final float LOAD_FACTOR = 0.75F;
  
  static final int TREEIFY_THRESHOLD = 8;
  
  static final int UNTREEIFY_THRESHOLD = 6;
  
  static final int MIN_TREEIFY_CAPACITY = 64;
  
  private static final int MIN_TRANSFER_STRIDE = 16;
  
  static final int MOVED = -1;
  
  static final int TREEBIN = -2;
  
  static final int RESERVED = -3;
  
  static final int HASH_BITS = 2147483647;
  
  static final int NCPU = Runtime.getRuntime().availableProcessors();
  
  private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[] { new ObjectStreamField("segments", Segment[].class), new ObjectStreamField("segmentMask", int.class), new ObjectStreamField("segmentShift", int.class) };
  
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
  
  static class Node<K, V> implements Map.Entry<K, V> {
    final int hash;
    
    final K key;
    
    volatile V val;
    
    volatile Node<K, V> next;
    
    Node(int hash, K key, V val, Node<K, V> next) {
      this.hash = hash;
      this.key = key;
      this.val = val;
      this.next = next;
    }
    
    public final K getKey() {
      return this.key;
    }
    
    public final V getValue() {
      return this.val;
    }
    
    public final int hashCode() {
      return this.key.hashCode() ^ this.val.hashCode();
    }
    
    public final String toString() {
      return (new StringBuilder()).append(this.key).append("=").append(this.val).toString();
    }
    
    public final V setValue(V value) {
      throw new UnsupportedOperationException();
    }
    
    public final boolean equals(Object o) {
      Object k;
      Object v;
      Object u;
      Map.Entry<?, ?> e;
      return (o instanceof Map.Entry && (k = (e = (Map.Entry<?, ?>)o).getKey()) != null && (v = e.getValue()) != null && (k == this.key || k.equals(this.key)) && (v == (u = this.val) || v.equals(u)));
    }
    
    Node<K, V> find(int h, Object k) {
      Node<K, V> e = this;
      if (k != null)
        do {
          K ek;
          if (e.hash == h && ((ek = e.key) == k || (ek != null && k.equals(ek))))
            return e; 
        } while ((e = e.next) != null); 
      return null;
    }
  }
  
  static final int spread(int h) {
    return (h ^ h >>> 16) & Integer.MAX_VALUE;
  }
  
  private static final int tableSizeFor(int c) {
    int n = c - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return (n < 0) ? 1 : ((n >= 1073741824) ? 1073741824 : (n + 1));
  }
  
  static Class<?> comparableClassFor(Object x) {
    if (x instanceof Comparable) {
      Class<?> c;
      if ((c = x.getClass()) == String.class)
        return c; 
      Type[] ts;
      if ((ts = c.getGenericInterfaces()) != null)
        for (int i = 0; i < ts.length; i++) {
          Type[] as;
          Type t;
          ParameterizedType p;
          if (t = ts[i] instanceof ParameterizedType && (p = (ParameterizedType)t).getRawType() == Comparable.class && (as = p.getActualTypeArguments()) != null && as.length == 1 && as[0] == c)
            return c; 
        }  
    } 
    return null;
  }
  
  static int compareComparables(Class<?> kc, Object k, Object x) {
    return (x == null || x.getClass() != kc) ? 0 : ((Comparable<Object>)k).compareTo(x);
  }
  
  static final <K, V> Node<K, V> tabAt(Node<K, V>[] tab, int i) {
    return (Node<K, V>)U.getObjectVolatile(tab, (i << ASHIFT) + ABASE);
  }
  
  static final <K, V> boolean casTabAt(Node<K, V>[] tab, int i, Node<K, V> c, Node<K, V> v) {
    return U.compareAndSwapObject(tab, (i << ASHIFT) + ABASE, c, v);
  }
  
  static final <K, V> void setTabAt(Node<K, V>[] tab, int i, Node<K, V> v) {
    U.putObjectVolatile(tab, (i << ASHIFT) + ABASE, v);
  }
  
  public ConcurrentHashMapV8() {}
  
  public ConcurrentHashMapV8(int initialCapacity) {
    if (initialCapacity < 0)
      throw new IllegalArgumentException(); 
    int cap = (initialCapacity >= 536870912) ? 1073741824 : tableSizeFor(initialCapacity + (initialCapacity >>> 1) + 1);
    this.sizeCtl = cap;
  }
  
  public ConcurrentHashMapV8(Map<? extends K, ? extends V> m) {
    this.sizeCtl = 16;
    putAll(m);
  }
  
  public ConcurrentHashMapV8(int initialCapacity, float loadFactor) {
    this(initialCapacity, loadFactor, 1);
  }
  
  public ConcurrentHashMapV8(int initialCapacity, float loadFactor, int concurrencyLevel) {
    if (loadFactor <= 0.0F || initialCapacity < 0 || concurrencyLevel <= 0)
      throw new IllegalArgumentException(); 
    if (initialCapacity < concurrencyLevel)
      initialCapacity = concurrencyLevel; 
    long size = (long)(1.0D + ((float)initialCapacity / loadFactor));
    int cap = (size >= 1073741824L) ? 1073741824 : tableSizeFor((int)size);
    this.sizeCtl = cap;
  }
  
  public int size() {
    long n = sumCount();
    return (n < 0L) ? 0 : ((n > 2147483647L) ? Integer.MAX_VALUE : (int)n);
  }
  
  public boolean isEmpty() {
    return (sumCount() <= 0L);
  }
  
  public V get(Object key) {
    int h = spread(key.hashCode());
    Node<K, V> tab[], e;
    int n;
    if ((tab = this.table) != null && (n = tab.length) > 0 && (e = tabAt(tab, n - 1 & h)) != null) {
      int eh;
      if ((eh = e.hash) == h) {
        K ek;
        if ((ek = e.key) == key || (ek != null && key.equals(ek)))
          return e.val; 
      } else if (eh < 0) {
        Node<K, V> p;
        return ((p = e.find(h, key)) != null) ? p.val : null;
      } 
      while ((e = e.next) != null) {
        K ek;
        if (e.hash == h && ((ek = e.key) == key || (ek != null && key.equals(ek))))
          return e.val; 
      } 
    } 
    return null;
  }
  
  public boolean containsKey(Object key) {
    return (get(key) != null);
  }
  
  public boolean containsValue(Object value) {
    if (value == null)
      throw new NullPointerException(); 
    Node<K, V>[] t;
    if ((t = this.table) != null) {
      Traverser<K, V> it = new Traverser<K, V>(t, t.length, 0, t.length);
      Node<K, V> p;
      while ((p = it.advance()) != null) {
        V v;
        if ((v = p.val) == value || (v != null && value.equals(v)))
          return true; 
      } 
    } 
    return false;
  }
  
  public V put(K key, V value) {
    return putVal(key, value, false);
  }
  
  final V putVal(K key, V value, boolean onlyIfAbsent) {
    if (key == null || value == null)
      throw new NullPointerException(); 
    int hash = spread(key.hashCode());
    int binCount = 0;
    Node<K, V>[] tab = this.table;
    while (true) {
      Node[] arrayOfNode;
      int n;
      if (tab == null || (n = tab.length) == 0) {
        arrayOfNode = (Node[])initTable();
        continue;
      } 
      Node<K, V> f;
      int i;
      if ((f = tabAt((Node<?, ?>[])arrayOfNode, i = n - 1 & hash)) == null) {
        if (casTabAt((Node<?, ?>[])arrayOfNode, i, null, new Node<Object, Object>(hash, key, value, null)))
          break; 
        continue;
      } 
      int fh;
      if ((fh = f.hash) == -1) {
        arrayOfNode = (Node[])helpTransfer((Node<K, V>[])arrayOfNode, f);
        continue;
      } 
      V oldVal = null;
      synchronized (f) {
        if (tabAt((Node<K, V>[])arrayOfNode, i) == f)
          if (fh >= 0) {
            binCount = 1;
            for (Node<K, V> e = f;; binCount++) {
              K ek;
              if (e.hash == hash && ((ek = e.key) == key || (ek != null && key.equals(ek)))) {
                oldVal = e.val;
                if (!onlyIfAbsent)
                  e.val = value; 
                break;
              } 
              Node<K, V> pred = e;
              if ((e = e.next) == null) {
                pred.next = new Node<K, V>(hash, key, value, null);
                break;
              } 
            } 
          } else if (f instanceof TreeBin) {
            binCount = 2;
            Node<K, V> p;
            if ((p = ((TreeBin<K, V>)f).putTreeVal(hash, key, value)) != null) {
              oldVal = p.val;
              if (!onlyIfAbsent)
                p.val = value; 
            } 
          }  
      } 
      if (binCount != 0) {
        if (binCount >= 8)
          treeifyBin((Node<K, V>[])arrayOfNode, i); 
        if (oldVal != null)
          return oldVal; 
        break;
      } 
    } 
    addCount(1L, binCount);
    return null;
  }
  
  public void putAll(Map<? extends K, ? extends V> m) {
    tryPresize(m.size());
    for (Map.Entry<? extends K, ? extends V> e : m.entrySet())
      putVal(e.getKey(), e.getValue(), false); 
  }
  
  public V remove(Object key) {
    return replaceNode(key, null, null);
  }
  
  final V replaceNode(Object key, V value, Object cv) {
    int hash = spread(key.hashCode());
    Node<K, V>[] tab = this.table;
    Node<K, V> f;
    int n, i;
    while (tab != null && (n = tab.length) != 0 && (f = tabAt(tab, i = n - 1 & hash)) != null) {
      Node[] arrayOfNode;
      int fh;
      if ((fh = f.hash) == -1) {
        arrayOfNode = (Node[])helpTransfer(tab, f);
        continue;
      } 
      V oldVal = null;
      boolean validated = false;
      synchronized (f) {
        if (tabAt((Node<K, V>[])arrayOfNode, i) == f)
          if (fh >= 0) {
            validated = true;
            Node<K, V> e = f, pred = null;
            do {
              K ek;
              if (e.hash == hash && ((ek = e.key) == key || (ek != null && key.equals(ek)))) {
                V ev = e.val;
                if (cv == null || cv == ev || (ev != null && cv.equals(ev))) {
                  oldVal = ev;
                  if (value != null) {
                    e.val = value;
                    break;
                  } 
                  if (pred != null) {
                    pred.next = e.next;
                    break;
                  } 
                  setTabAt((Node<K, V>[])arrayOfNode, i, e.next);
                } 
                break;
              } 
              pred = e;
            } while ((e = e.next) != null);
          } else {
            validated = true;
            TreeBin<K, V> t = (TreeBin<K, V>)f;
            TreeNode<K, V> r, p;
            if (f instanceof TreeBin && (r = t.root) != null && (p = r.findTreeNode(hash, key, (Class<?>)null)) != null) {
              V pv = p.val;
              if (cv == null || cv == pv || (pv != null && cv.equals(pv))) {
                oldVal = pv;
                if (value != null) {
                  p.val = value;
                } else if (t.removeTreeNode(p)) {
                  setTabAt((Node<?, ?>[])arrayOfNode, i, untreeify(t.first));
                } 
              } 
            } 
          }  
      } 
      if (validated) {
        if (oldVal != null) {
          if (value == null)
            addCount(-1L, -1); 
          return oldVal;
        } 
        break;
      } 
    } 
    return null;
  }
  
  public void clear() {
    long delta = 0L;
    int i = 0;
    Node<K, V>[] tab = this.table;
    while (tab != null && i < tab.length) {
      Node[] arrayOfNode;
      Node<K, V> f = tabAt(tab, i);
      if (f == null) {
        i++;
        continue;
      } 
      int fh;
      if ((fh = f.hash) == -1) {
        arrayOfNode = (Node[])helpTransfer(tab, f);
        i = 0;
        continue;
      } 
      synchronized (f) {
        if (tabAt((Node<K, V>[])arrayOfNode, i) == f) {
          Node<K, V> p = (fh >= 0) ? f : ((f instanceof TreeBin) ? ((TreeBin)f).first : null);
          while (p != null) {
            delta--;
            p = p.next;
          } 
          setTabAt((Node<?, ?>[])arrayOfNode, i++, null);
        } 
      } 
    } 
    if (delta != 0L)
      addCount(delta, -1); 
  }
  
  public KeySetView<K, V> keySet() {
    KeySetView<K, V> ks;
    return ((ks = this.keySet) != null) ? ks : (this.keySet = new KeySetView<K, V>(this, null));
  }
  
  public Collection<V> values() {
    ValuesView<K, V> vs;
    return ((vs = this.values) != null) ? vs : (this.values = new ValuesView<K, V>(this));
  }
  
  public Set<Map.Entry<K, V>> entrySet() {
    EntrySetView<K, V> es;
    return ((es = this.entrySet) != null) ? es : (this.entrySet = new EntrySetView<K, V>(this));
  }
  
  public int hashCode() {
    int h = 0;
    Node<K, V>[] t;
    if ((t = this.table) != null) {
      Traverser<K, V> it = new Traverser<K, V>(t, t.length, 0, t.length);
      Node<K, V> p;
      while ((p = it.advance()) != null)
        h += p.key.hashCode() ^ p.val.hashCode(); 
    } 
    return h;
  }
  
  public String toString() {
    Node<K, V>[] t;
    int f = ((t = this.table) == null) ? 0 : t.length;
    Traverser<K, V> it = new Traverser<K, V>(t, f, 0, f);
    StringBuilder sb = new StringBuilder();
    sb.append('{');
    Node<K, V> p;
    if ((p = it.advance()) != null)
      while (true) {
        K k = p.key;
        V v = p.val;
        sb.append((k == this) ? "(this Map)" : k);
        sb.append('=');
        sb.append((v == this) ? "(this Map)" : v);
        if ((p = it.advance()) == null)
          break; 
        sb.append(',').append(' ');
      }  
    return sb.append('}').toString();
  }
  
  public boolean equals(Object o) {
    if (o != this) {
      if (!(o instanceof Map))
        return false; 
      Map<?, ?> m = (Map<?, ?>)o;
      Node<K, V>[] t;
      int f = ((t = this.table) == null) ? 0 : t.length;
      Traverser<K, V> it = new Traverser<K, V>(t, f, 0, f);
      Node<K, V> p;
      while ((p = it.advance()) != null) {
        V val = p.val;
        Object v = m.get(p.key);
        if (v == null || (v != val && !v.equals(val)))
          return false; 
      } 
      for (Map.Entry<?, ?> e : m.entrySet()) {
        Object mk;
        Object mv;
        Object v;
        if ((mk = e.getKey()) == null || (mv = e.getValue()) == null || (v = get(mk)) == null || (mv != v && !mv.equals(v)))
          return false; 
      } 
    } 
    return true;
  }
  
  static class Segment<K, V> extends ReentrantLock implements Serializable {
    private static final long serialVersionUID = 2249069246763182397L;
    
    final float loadFactor;
    
    Segment(float lf) {
      this.loadFactor = lf;
    }
  }
  
  private void writeObject(ObjectOutputStream s) throws IOException {
    int sshift = 0;
    int ssize = 1;
    while (ssize < 16) {
      sshift++;
      ssize <<= 1;
    } 
    int segmentShift = 32 - sshift;
    int segmentMask = ssize - 1;
    Segment[] arrayOfSegment = new Segment[16];
    for (int i = 0; i < arrayOfSegment.length; i++)
      arrayOfSegment[i] = new Segment<Object, Object>(0.75F); 
    s.putFields().put("segments", arrayOfSegment);
    s.putFields().put("segmentShift", segmentShift);
    s.putFields().put("segmentMask", segmentMask);
    s.writeFields();
    Node<K, V>[] t;
    if ((t = this.table) != null) {
      Traverser<K, V> it = new Traverser<K, V>(t, t.length, 0, t.length);
      Node<K, V> p;
      while ((p = it.advance()) != null) {
        s.writeObject(p.key);
        s.writeObject(p.val);
      } 
    } 
    s.writeObject(null);
    s.writeObject(null);
    arrayOfSegment = null;
  }
  
  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    this.sizeCtl = -1;
    s.defaultReadObject();
    long size = 0L;
    Node<K, V> p = null;
    while (true) {
      K k = (K)s.readObject();
      V v = (V)s.readObject();
      if (k != null && v != null) {
        p = new Node<K, V>(spread(k.hashCode()), k, v, p);
        size++;
        continue;
      } 
      break;
    } 
    if (size == 0L) {
      this.sizeCtl = 0;
    } else {
      int n;
      if (size >= 536870912L) {
        n = 1073741824;
      } else {
        int sz = (int)size;
        n = tableSizeFor(sz + (sz >>> 1) + 1);
      } 
      Node[] arrayOfNode = new Node[n];
      int mask = n - 1;
      long added = 0L;
      while (p != null) {
        boolean insertAtFront;
        Node<K, V> next = p.next;
        int h = p.hash, j = h & mask;
        Node<K, V> first;
        if ((first = tabAt((Node<?, ?>[])arrayOfNode, j)) == null) {
          insertAtFront = true;
        } else {
          K k = p.key;
          if (first.hash < 0) {
            TreeBin<K, V> t = (TreeBin<K, V>)first;
            if (t.putTreeVal(h, k, p.val) == null)
              added++; 
            insertAtFront = false;
          } else {
            int binCount = 0;
            insertAtFront = true;
            Node<K, V> q;
            for (q = first; q != null; q = q.next) {
              K qk;
              if (q.hash == h && ((qk = q.key) == k || (qk != null && k.equals(qk)))) {
                insertAtFront = false;
                break;
              } 
              binCount++;
            } 
            if (insertAtFront && binCount >= 8) {
              insertAtFront = false;
              added++;
              p.next = first;
              TreeNode<K, V> hd = null, tl = null;
              for (q = p; q != null; q = q.next) {
                TreeNode<K, V> t = new TreeNode<K, V>(q.hash, q.key, q.val, null, null);
                if ((t.prev = tl) == null) {
                  hd = t;
                } else {
                  tl.next = t;
                } 
                tl = t;
              } 
              setTabAt((Node<?, ?>[])arrayOfNode, j, new TreeBin<Object, Object>(hd));
            } 
          } 
        } 
        if (insertAtFront) {
          added++;
          p.next = first;
          setTabAt((Node<K, V>[])arrayOfNode, j, p);
        } 
        p = next;
      } 
      this.table = (Node<K, V>[])arrayOfNode;
      this.sizeCtl = n - (n >>> 2);
      this.baseCount = added;
    } 
  }
  
  public V putIfAbsent(K key, V value) {
    return putVal(key, value, true);
  }
  
  public boolean remove(Object key, Object value) {
    if (key == null)
      throw new NullPointerException(); 
    return (value != null && replaceNode(key, null, value) != null);
  }
  
  public boolean replace(K key, V oldValue, V newValue) {
    if (key == null || oldValue == null || newValue == null)
      throw new NullPointerException(); 
    return (replaceNode(key, newValue, oldValue) != null);
  }
  
  public V replace(K key, V value) {
    if (key == null || value == null)
      throw new NullPointerException(); 
    return replaceNode(key, value, null);
  }
  
  public V getOrDefault(Object key, V defaultValue) {
    V v;
    return ((v = get(key)) == null) ? defaultValue : v;
  }
  
  public void forEach(BiAction<? super K, ? super V> action) {
    if (action == null)
      throw new NullPointerException(); 
    Node<K, V>[] t;
    if ((t = this.table) != null) {
      Traverser<K, V> it = new Traverser<K, V>(t, t.length, 0, t.length);
      Node<K, V> p;
      while ((p = it.advance()) != null)
        action.apply(p.key, p.val); 
    } 
  }
  
  public void replaceAll(BiFun<? super K, ? super V, ? extends V> function) {
    if (function == null)
      throw new NullPointerException(); 
    Node<K, V>[] t;
    if ((t = this.table) != null) {
      Traverser<K, V> it = new Traverser<K, V>(t, t.length, 0, t.length);
      Node<K, V> p;
      label22: while ((p = it.advance()) != null) {
        V oldValue = p.val;
        K key = p.key;
        while (true) {
          V newValue = function.apply(key, oldValue);
          if (newValue == null)
            throw new NullPointerException(); 
          if (replaceNode(key, newValue, oldValue) == null) {
            if ((oldValue = get(key)) == null)
              continue label22; 
            continue;
          } 
          continue label22;
        } 
      } 
    } 
  }
  
  public V computeIfAbsent(K key, Fun<? super K, ? extends V> mappingFunction) {
    if (key == null || mappingFunction == null)
      throw new NullPointerException(); 
    int h = spread(key.hashCode());
    V val = null;
    int binCount = 0;
    Node<K, V>[] tab = this.table;
    while (true) {
      Node[] arrayOfNode;
      int n;
      if (tab == null || (n = tab.length) == 0) {
        arrayOfNode = (Node[])initTable();
        continue;
      } 
      Node<K, V> f;
      int i;
      if ((f = tabAt((Node<?, ?>[])arrayOfNode, i = n - 1 & h)) == null) {
        Node<K, V> r = new ReservationNode<K, V>();
        synchronized (r) {
          if (casTabAt((Node<K, V>[])arrayOfNode, i, null, r)) {
            binCount = 1;
            Node<K, V> node = null;
            try {
              if ((val = mappingFunction.apply(key)) != null)
                node = new Node<K, V>(h, key, val, null); 
            } finally {
              setTabAt((Node<K, V>[])arrayOfNode, i, node);
            } 
          } 
        } 
        if (binCount != 0)
          break; 
        continue;
      } 
      int fh;
      if ((fh = f.hash) == -1) {
        arrayOfNode = (Node[])helpTransfer((Node<K, V>[])arrayOfNode, f);
        continue;
      } 
      boolean added = false;
      synchronized (f) {
        if (tabAt((Node<K, V>[])arrayOfNode, i) == f)
          if (fh >= 0) {
            binCount = 1;
            for (Node<K, V> e = f;; binCount++) {
              K ek;
              if (e.hash == h && ((ek = e.key) == key || (ek != null && key.equals(ek)))) {
                val = e.val;
                break;
              } 
              Node<K, V> pred = e;
              if ((e = e.next) == null) {
                if ((val = mappingFunction.apply(key)) != null) {
                  added = true;
                  pred.next = new Node<K, V>(h, key, val, null);
                } 
                break;
              } 
            } 
          } else if (f instanceof TreeBin) {
            binCount = 2;
            TreeBin<K, V> t = (TreeBin<K, V>)f;
            TreeNode<K, V> r, p;
            if ((r = t.root) != null && (p = r.findTreeNode(h, key, (Class<?>)null)) != null) {
              val = p.val;
            } else if ((val = mappingFunction.apply(key)) != null) {
              added = true;
              t.putTreeVal(h, key, val);
            } 
          }  
      } 
      if (binCount != 0) {
        if (binCount >= 8)
          treeifyBin((Node<K, V>[])arrayOfNode, i); 
        if (!added)
          return val; 
        break;
      } 
    } 
    if (val != null)
      addCount(1L, binCount); 
    return val;
  }
  
  public V computeIfPresent(K key, BiFun<? super K, ? super V, ? extends V> remappingFunction) {
    if (key == null || remappingFunction == null)
      throw new NullPointerException(); 
    int h = spread(key.hashCode());
    V val = null;
    int delta = 0;
    int binCount = 0;
    Node<K, V>[] tab = this.table;
    while (true) {
      Node[] arrayOfNode;
      int n;
      if (tab == null || (n = tab.length) == 0) {
        arrayOfNode = (Node[])initTable();
        continue;
      } 
      Node<K, V> f;
      int i;
      if ((f = tabAt((Node<?, ?>[])arrayOfNode, i = n - 1 & h)) == null)
        break; 
      int fh;
      if ((fh = f.hash) == -1) {
        arrayOfNode = (Node[])helpTransfer((Node<K, V>[])arrayOfNode, f);
        continue;
      } 
      synchronized (f) {
        if (tabAt((Node<K, V>[])arrayOfNode, i) == f)
          if (fh >= 0) {
            binCount = 1;
            for (Node<K, V> e = f, pred = null;; binCount++) {
              K ek;
              if (e.hash == h && ((ek = e.key) == key || (ek != null && key.equals(ek)))) {
                val = remappingFunction.apply(key, e.val);
                if (val != null) {
                  e.val = val;
                  break;
                } 
                delta = -1;
                Node<K, V> en = e.next;
                if (pred != null) {
                  pred.next = en;
                  break;
                } 
                setTabAt((Node<K, V>[])arrayOfNode, i, en);
                break;
              } 
              pred = e;
              if ((e = e.next) == null)
                break; 
            } 
          } else {
            binCount = 2;
            TreeBin<K, V> t = (TreeBin<K, V>)f;
            TreeNode<K, V> r, p;
            if (f instanceof TreeBin && (r = t.root) != null && (p = r.findTreeNode(h, key, (Class<?>)null)) != null) {
              val = remappingFunction.apply(key, p.val);
              if (val != null) {
                p.val = val;
              } else {
                delta = -1;
                if (t.removeTreeNode(p))
                  setTabAt((Node<?, ?>[])arrayOfNode, i, untreeify(t.first)); 
              } 
            } 
          }  
      } 
      if (binCount != 0)
        break; 
    } 
    if (delta != 0)
      addCount(delta, binCount); 
    return val;
  }
  
  public V compute(K key, BiFun<? super K, ? super V, ? extends V> remappingFunction) {
    if (key == null || remappingFunction == null)
      throw new NullPointerException(); 
    int h = spread(key.hashCode());
    V val = null;
    int delta = 0;
    int binCount = 0;
    Node<K, V>[] tab = this.table;
    while (true) {
      Node[] arrayOfNode;
      int n;
      if (tab == null || (n = tab.length) == 0) {
        arrayOfNode = (Node[])initTable();
        continue;
      } 
      Node<K, V> f;
      int i;
      if ((f = tabAt((Node<?, ?>[])arrayOfNode, i = n - 1 & h)) == null) {
        Node<K, V> r = new ReservationNode<K, V>();
        synchronized (r) {
          if (casTabAt((Node<K, V>[])arrayOfNode, i, null, r)) {
            binCount = 1;
            Node<K, V> node = null;
            try {
              if ((val = remappingFunction.apply(key, null)) != null) {
                delta = 1;
                node = new Node<K, V>(h, key, val, null);
              } 
            } finally {
              setTabAt((Node<K, V>[])arrayOfNode, i, node);
            } 
          } 
        } 
        if (binCount != 0)
          break; 
        continue;
      } 
      int fh;
      if ((fh = f.hash) == -1) {
        arrayOfNode = (Node[])helpTransfer((Node<K, V>[])arrayOfNode, f);
        continue;
      } 
      synchronized (f) {
        if (tabAt((Node<K, V>[])arrayOfNode, i) == f)
          if (fh >= 0) {
            binCount = 1;
            for (Node<K, V> e = f, pred = null;; binCount++) {
              K ek;
              if (e.hash == h && ((ek = e.key) == key || (ek != null && key.equals(ek)))) {
                val = remappingFunction.apply(key, e.val);
                if (val != null) {
                  e.val = val;
                  break;
                } 
                delta = -1;
                Node<K, V> en = e.next;
                if (pred != null) {
                  pred.next = en;
                  break;
                } 
                setTabAt((Node<K, V>[])arrayOfNode, i, en);
                break;
              } 
              pred = e;
              if ((e = e.next) == null) {
                val = remappingFunction.apply(key, null);
                if (val != null) {
                  delta = 1;
                  pred.next = new Node<K, V>(h, key, val, null);
                } 
                break;
              } 
            } 
          } else if (f instanceof TreeBin) {
            TreeNode<K, V> p;
            binCount = 1;
            TreeBin<K, V> t = (TreeBin<K, V>)f;
            TreeNode<K, V> r;
            if ((r = t.root) != null) {
              p = r.findTreeNode(h, key, (Class<?>)null);
            } else {
              p = null;
            } 
            V pv = (p == null) ? null : p.val;
            val = remappingFunction.apply(key, pv);
            if (val != null) {
              if (p != null) {
                p.val = val;
              } else {
                delta = 1;
                t.putTreeVal(h, key, val);
              } 
            } else if (p != null) {
              delta = -1;
              if (t.removeTreeNode(p))
                setTabAt((Node<?, ?>[])arrayOfNode, i, untreeify(t.first)); 
            } 
          }  
      } 
      if (binCount != 0) {
        if (binCount >= 8)
          treeifyBin((Node<K, V>[])arrayOfNode, i); 
        break;
      } 
    } 
    if (delta != 0)
      addCount(delta, binCount); 
    return val;
  }
  
  public V merge(K key, V value, BiFun<? super V, ? super V, ? extends V> remappingFunction) {
    if (key == null || value == null || remappingFunction == null)
      throw new NullPointerException(); 
    int h = spread(key.hashCode());
    V val = null;
    int delta = 0;
    int binCount = 0;
    Node<K, V>[] tab = this.table;
    while (true) {
      Node[] arrayOfNode;
      int n;
      if (tab == null || (n = tab.length) == 0) {
        arrayOfNode = (Node[])initTable();
        continue;
      } 
      Node<K, V> f;
      int i;
      if ((f = tabAt((Node<?, ?>[])arrayOfNode, i = n - 1 & h)) == null) {
        if (casTabAt((Node<?, ?>[])arrayOfNode, i, null, new Node<Object, Object>(h, key, value, null))) {
          delta = 1;
          val = value;
          break;
        } 
        continue;
      } 
      int fh;
      if ((fh = f.hash) == -1) {
        arrayOfNode = (Node[])helpTransfer((Node<K, V>[])arrayOfNode, f);
        continue;
      } 
      synchronized (f) {
        if (tabAt((Node<K, V>[])arrayOfNode, i) == f)
          if (fh >= 0) {
            binCount = 1;
            for (Node<K, V> e = f, pred = null;; binCount++) {
              K ek;
              if (e.hash == h && ((ek = e.key) == key || (ek != null && key.equals(ek)))) {
                val = remappingFunction.apply(e.val, value);
                if (val != null) {
                  e.val = val;
                  break;
                } 
                delta = -1;
                Node<K, V> en = e.next;
                if (pred != null) {
                  pred.next = en;
                  break;
                } 
                setTabAt((Node<K, V>[])arrayOfNode, i, en);
                break;
              } 
              pred = e;
              if ((e = e.next) == null) {
                delta = 1;
                val = value;
                pred.next = new Node<K, V>(h, key, val, null);
                break;
              } 
            } 
          } else if (f instanceof TreeBin) {
            binCount = 2;
            TreeBin<K, V> t = (TreeBin<K, V>)f;
            TreeNode<K, V> r = t.root;
            TreeNode<K, V> p = (r == null) ? null : r.findTreeNode(h, key, (Class<?>)null);
            val = (p == null) ? value : remappingFunction.apply(p.val, value);
            if (val != null) {
              if (p != null) {
                p.val = val;
              } else {
                delta = 1;
                t.putTreeVal(h, key, val);
              } 
            } else if (p != null) {
              delta = -1;
              if (t.removeTreeNode(p))
                setTabAt((Node<?, ?>[])arrayOfNode, i, untreeify(t.first)); 
            } 
          }  
      } 
      if (binCount != 0) {
        if (binCount >= 8)
          treeifyBin((Node<K, V>[])arrayOfNode, i); 
        break;
      } 
    } 
    if (delta != 0)
      addCount(delta, binCount); 
    return val;
  }
  
  @Deprecated
  public boolean contains(Object value) {
    return containsValue(value);
  }
  
  public Enumeration<K> keys() {
    Node<K, V>[] t;
    int f = ((t = this.table) == null) ? 0 : t.length;
    return new KeyIterator<K, V>(t, f, 0, f, this);
  }
  
  public Enumeration<V> elements() {
    Node<K, V>[] t;
    int f = ((t = this.table) == null) ? 0 : t.length;
    return new ValueIterator<K, V>(t, f, 0, f, this);
  }
  
  public long mappingCount() {
    long n = sumCount();
    return (n < 0L) ? 0L : n;
  }
  
  public static <K> KeySetView<K, Boolean> newKeySet() {
    return new KeySetView<K, Boolean>(new ConcurrentHashMapV8<K, Boolean>(), Boolean.TRUE);
  }
  
  public static <K> KeySetView<K, Boolean> newKeySet(int initialCapacity) {
    return new KeySetView<K, Boolean>(new ConcurrentHashMapV8<K, Boolean>(initialCapacity), Boolean.TRUE);
  }
  
  public KeySetView<K, V> keySet(V mappedValue) {
    if (mappedValue == null)
      throw new NullPointerException(); 
    return new KeySetView<K, V>(this, mappedValue);
  }
  
  static final class ForwardingNode<K, V> extends Node<K, V> {
    final ConcurrentHashMapV8.Node<K, V>[] nextTable;
    
    ForwardingNode(ConcurrentHashMapV8.Node<K, V>[] tab) {
      super(-1, null, null, null);
      this.nextTable = tab;
    }
    
    ConcurrentHashMapV8.Node<K, V> find(int h, Object k) {
      ConcurrentHashMapV8.Node<K, V>[] tab = this.nextTable;
      label27: while (true) {
        ConcurrentHashMapV8.Node<K, V> e;
        int n;
        if (k == null || tab == null || (n = tab.length) == 0 || (e = ConcurrentHashMapV8.<K, V>tabAt(tab, n - 1 & h)) == null)
          return null; 
        while (true) {
          int eh;
          K ek;
          if ((eh = e.hash) == h && ((ek = e.key) == k || (ek != null && k.equals(ek))))
            return e; 
          if (eh < 0) {
            if (e instanceof ForwardingNode) {
              tab = ((ForwardingNode)e).nextTable;
              continue label27;
            } 
            return e.find(h, k);
          } 
          if ((e = e.next) == null)
            return null; 
        } 
        break;
      } 
    }
  }
  
  static final class ReservationNode<K, V> extends Node<K, V> {
    ReservationNode() {
      super(-3, null, null, null);
    }
    
    ConcurrentHashMapV8.Node<K, V> find(int h, Object k) {
      return null;
    }
  }
  
  private final Node<K, V>[] initTable() {
    Node[] arrayOfNode;
    Node<K, V>[] tab;
    while ((tab = this.table) == null || tab.length == 0) {
      int sc;
      if ((sc = this.sizeCtl) < 0) {
        Thread.yield();
        continue;
      } 
      if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
        try {
          if ((tab = this.table) == null || tab.length == 0) {
            int n = (sc > 0) ? sc : 16;
            Node[] arrayOfNode1 = new Node[n];
            this.table = (Node<K, V>[])(arrayOfNode = arrayOfNode1);
            sc = n - (n >>> 2);
          } 
        } finally {
          this.sizeCtl = sc;
        } 
        break;
      } 
    } 
    return (Node<K, V>[])arrayOfNode;
  }
  
  private final void addCount(long x, int check) {
    CounterCell[] as;
    long b;
    long s;
    if ((as = this.counterCells) != null || !U.compareAndSwapLong(this, BASECOUNT, b = this.baseCount, s = b + x)) {
      boolean uncontended = true;
      InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
      IntegerHolder hc;
      CounterCell a;
      long v;
      int m;
      if ((hc = threadLocals.counterHashCode()) == null || as == null || (m = as.length - 1) < 0 || (a = as[m & hc.value]) == null || !(uncontended = U.compareAndSwapLong(a, CELLVALUE, v = a.value, v + x))) {
        fullAddCount(threadLocals, x, hc, uncontended);
        return;
      } 
      if (check <= 1)
        return; 
      s = sumCount();
    } 
    if (check >= 0) {
      Node<K, V>[] tab;
      int sc;
      while (s >= (sc = this.sizeCtl) && (tab = this.table) != null && tab.length < 1073741824) {
        if (sc < 0) {
          Node<K, V>[] nt;
          if (sc == -1 || this.transferIndex <= this.transferOrigin || (nt = this.nextTable) == null)
            break; 
          if (U.compareAndSwapInt(this, SIZECTL, sc, sc - 1))
            transfer(tab, nt); 
        } else if (U.compareAndSwapInt(this, SIZECTL, sc, -2)) {
          transfer(tab, null);
        } 
        s = sumCount();
      } 
    } 
  }
  
  final Node<K, V>[] helpTransfer(Node<K, V>[] tab, Node<K, V> f) {
    Node[] arrayOfNode;
    if (f instanceof ForwardingNode && (arrayOfNode = ((ForwardingNode)f).nextTable) != null) {
      int sc;
      if (arrayOfNode == this.nextTable && tab == this.table && this.transferIndex > this.transferOrigin && (sc = this.sizeCtl) < -1 && U.compareAndSwapInt(this, SIZECTL, sc, sc - 1))
        transfer(tab, (Node<K, V>[])arrayOfNode); 
      return (Node<K, V>[])arrayOfNode;
    } 
    return this.table;
  }
  
  private final void tryPresize(int size) {
    int c = (size >= 536870912) ? 1073741824 : tableSizeFor(size + (size >>> 1) + 1);
    int sc;
    while ((sc = this.sizeCtl) >= 0) {
      Node<K, V>[] tab = this.table;
      int n;
      if (tab == null || (n = tab.length) == 0) {
        n = (sc > c) ? sc : c;
        if (U.compareAndSwapInt(this, SIZECTL, sc, -1))
          try {
            if (this.table == tab) {
              Node[] arrayOfNode = new Node[n];
              this.table = (Node<K, V>[])arrayOfNode;
              sc = n - (n >>> 2);
            } 
          } finally {
            this.sizeCtl = sc;
          }  
        continue;
      } 
      if (c <= sc || n >= 1073741824)
        break; 
      if (tab == this.table && U.compareAndSwapInt(this, SIZECTL, sc, -2))
        transfer(tab, null); 
    } 
  }
  
  private final void transfer(Node<K, V>[] tab, Node<K, V>[] nextTab) {
    Node[] arrayOfNode;
    int n = tab.length;
    int stride;
    if ((stride = (NCPU > 1) ? ((n >>> 3) / NCPU) : n) < 16)
      stride = 16; 
    if (nextTab == null) {
      try {
        Node[] arrayOfNode1 = new Node[n << 1];
        arrayOfNode = arrayOfNode1;
      } catch (Throwable ex) {
        this.sizeCtl = Integer.MAX_VALUE;
        return;
      } 
      this.nextTable = (Node<K, V>[])arrayOfNode;
      this.transferOrigin = n;
      this.transferIndex = n;
      ForwardingNode<K, V> rev = new ForwardingNode<K, V>(tab);
      for (int k = n; k > 0; ) {
        int nextk = (k > stride) ? (k - stride) : 0;
        int m;
        for (m = nextk; m < k; m++)
          arrayOfNode[m] = rev; 
        for (m = n + nextk; m < n + k; m++)
          arrayOfNode[m] = rev; 
        U.putOrderedInt(this, TRANSFERORIGIN, k = nextk);
      } 
    } 
    int nextn = arrayOfNode.length;
    ForwardingNode<K, V> fwd = new ForwardingNode<K, V>((Node<K, V>[])arrayOfNode);
    boolean advance = true;
    boolean finishing = false;
    int i = 0, bound = 0;
    while (true) {
      while (advance) {
        if (--i >= bound || finishing) {
          advance = false;
          continue;
        } 
        int nextIndex;
        if ((nextIndex = this.transferIndex) <= this.transferOrigin) {
          i = -1;
          advance = false;
          continue;
        } 
        int nextBound;
        if (U.compareAndSwapInt(this, TRANSFERINDEX, nextIndex, nextBound = (nextIndex > stride) ? (nextIndex - stride) : 0)) {
          bound = nextBound;
          i = nextIndex - 1;
          advance = false;
        } 
      } 
      if (i < 0 || i >= n || i + n >= nextn) {
        if (finishing) {
          this.nextTable = null;
          this.table = (Node<K, V>[])arrayOfNode;
          this.sizeCtl = (n << 1) - (n >>> 1);
          return;
        } 
        int sc;
        do {
        
        } while (!U.compareAndSwapInt(this, SIZECTL, sc = this.sizeCtl, ++sc));
        if (sc != -1)
          return; 
        finishing = advance = true;
        i = n;
        continue;
      } 
      Node<K, V> f;
      if ((f = tabAt(tab, i)) == null) {
        if (casTabAt(tab, i, null, fwd)) {
          setTabAt((Node<?, ?>[])arrayOfNode, i, null);
          setTabAt((Node<?, ?>[])arrayOfNode, i + n, null);
          advance = true;
        } 
        continue;
      } 
      int fh;
      if ((fh = f.hash) == -1) {
        advance = true;
        continue;
      } 
      synchronized (f) {
        if (tabAt(tab, i) == f)
          if (fh >= 0) {
            Node<K, V> ln, hn;
            int runBit = fh & n;
            Node<K, V> lastRun = f;
            Node<K, V> p;
            for (p = f.next; p != null; p = p.next) {
              int b = p.hash & n;
              if (b != runBit) {
                runBit = b;
                lastRun = p;
              } 
            } 
            if (runBit == 0) {
              ln = lastRun;
              hn = null;
            } else {
              hn = lastRun;
              ln = null;
            } 
            for (p = f; p != lastRun; p = p.next) {
              int ph = p.hash;
              K pk = p.key;
              V pv = p.val;
              if ((ph & n) == 0) {
                ln = new Node<K, V>(ph, pk, pv, ln);
              } else {
                hn = new Node<K, V>(ph, pk, pv, hn);
              } 
            } 
            setTabAt((Node<K, V>[])arrayOfNode, i, ln);
            setTabAt((Node<K, V>[])arrayOfNode, i + n, hn);
            setTabAt(tab, i, fwd);
            advance = true;
          } else if (f instanceof TreeBin) {
            TreeBin<K, V> t = (TreeBin<K, V>)f;
            TreeNode<K, V> lo = null, loTail = null;
            TreeNode<K, V> hi = null, hiTail = null;
            int lc = 0, hc = 0;
            for (Node<K, V> e = t.first; e != null; e = e.next) {
              int h = e.hash;
              TreeNode<K, V> p = new TreeNode<K, V>(h, e.key, e.val, null, null);
              if ((h & n) == 0) {
                if ((p.prev = loTail) == null) {
                  lo = p;
                } else {
                  loTail.next = p;
                } 
                loTail = p;
                lc++;
              } else {
                if ((p.prev = hiTail) == null) {
                  hi = p;
                } else {
                  hiTail.next = p;
                } 
                hiTail = p;
                hc++;
              } 
            } 
            Node<K, V> ln = (lc <= 6) ? untreeify(lo) : ((hc != 0) ? new TreeBin<K, V>(lo) : t);
            Node<K, V> hn = (hc <= 6) ? untreeify(hi) : ((lc != 0) ? new TreeBin<K, V>(hi) : t);
            setTabAt((Node<K, V>[])arrayOfNode, i, ln);
            setTabAt((Node<K, V>[])arrayOfNode, i + n, hn);
            setTabAt(tab, i, fwd);
            advance = true;
          }  
      } 
    } 
  }
  
  private final void treeifyBin(Node<K, V>[] tab, int index) {
    if (tab != null) {
      int n;
      if ((n = tab.length) < 64) {
        int sc;
        if (tab == this.table && (sc = this.sizeCtl) >= 0 && U.compareAndSwapInt(this, SIZECTL, sc, -2))
          transfer(tab, null); 
      } else {
        Node<K, V> b;
        if ((b = tabAt(tab, index)) != null && b.hash >= 0)
          synchronized (b) {
            if (tabAt(tab, index) == b) {
              TreeNode<K, V> hd = null, tl = null;
              for (Node<K, V> e = b; e != null; e = e.next) {
                TreeNode<K, V> p = new TreeNode<K, V>(e.hash, e.key, e.val, null, null);
                if ((p.prev = tl) == null) {
                  hd = p;
                } else {
                  tl.next = p;
                } 
                tl = p;
              } 
              setTabAt(tab, index, new TreeBin<K, V>(hd));
            } 
          }  
      } 
    } 
  }
  
  static <K, V> Node<K, V> untreeify(Node<K, V> b) {
    Node<K, V> hd = null, tl = null;
    for (Node<K, V> q = b; q != null; q = q.next) {
      Node<K, V> p = new Node<K, V>(q.hash, q.key, q.val, null);
      if (tl == null) {
        hd = p;
      } else {
        tl.next = p;
      } 
      tl = p;
    } 
    return hd;
  }
  
  static final class TreeNode<K, V> extends Node<K, V> {
    TreeNode<K, V> parent;
    
    TreeNode<K, V> left;
    
    TreeNode<K, V> right;
    
    TreeNode<K, V> prev;
    
    boolean red;
    
    TreeNode(int hash, K key, V val, ConcurrentHashMapV8.Node<K, V> next, TreeNode<K, V> parent) {
      super(hash, key, val, next);
      this.parent = parent;
    }
    
    ConcurrentHashMapV8.Node<K, V> find(int h, Object k) {
      return findTreeNode(h, k, (Class<?>)null);
    }
    
    final TreeNode<K, V> findTreeNode(int h, Object k, Class<?> kc) {
      if (k != null) {
        TreeNode<K, V> p = this;
        do {
          TreeNode<K, V> pl = p.left, pr = p.right;
          int ph;
          if ((ph = p.hash) > h) {
            p = pl;
          } else if (ph < h) {
            p = pr;
          } else {
            K pk;
            if ((pk = p.key) == k || (pk != null && k.equals(pk)))
              return p; 
            if (pl == null && pr == null)
              break; 
            int dir;
            if ((kc != null || (kc = ConcurrentHashMapV8.comparableClassFor(k)) != null) && (dir = ConcurrentHashMapV8.compareComparables(kc, k, pk)) != 0) {
              p = (dir < 0) ? pl : pr;
            } else if (pl == null) {
              p = pr;
            } else {
              TreeNode<K, V> q;
              if (pr == null || (q = pr.findTreeNode(h, k, kc)) == null) {
                p = pl;
              } else {
                return q;
              } 
            } 
          } 
        } while (p != null);
      } 
      return null;
    }
  }
  
  static final class TreeBin<K, V> extends Node<K, V> {
    ConcurrentHashMapV8.TreeNode<K, V> root;
    
    volatile ConcurrentHashMapV8.TreeNode<K, V> first;
    
    volatile Thread waiter;
    
    volatile int lockState;
    
    static final int WRITER = 1;
    
    static final int WAITER = 2;
    
    static final int READER = 4;
    
    private static final Unsafe U;
    
    private static final long LOCKSTATE;
    
    TreeBin(ConcurrentHashMapV8.TreeNode<K, V> b) {
      super(-2, null, null, null);
      this.first = b;
      ConcurrentHashMapV8.TreeNode<K, V> r = null;
      for (ConcurrentHashMapV8.TreeNode<K, V> x = b; x != null; x = next) {
        ConcurrentHashMapV8.TreeNode<K, V> next = (ConcurrentHashMapV8.TreeNode<K, V>)x.next;
        x.left = x.right = null;
        if (r == null) {
          x.parent = null;
          x.red = false;
          r = x;
        } else {
          Object key = x.key;
          int hash = x.hash;
          Class<?> kc = null;
          ConcurrentHashMapV8.TreeNode<K, V> p = r;
          while (true) {
            int dir, ph;
            if ((ph = p.hash) > hash) {
              dir = -1;
            } else if (ph < hash) {
              dir = 1;
            } else if (kc != null || (kc = ConcurrentHashMapV8.comparableClassFor(key)) != null) {
              dir = ConcurrentHashMapV8.compareComparables(kc, key, p.key);
            } else {
              dir = 0;
            } 
            ConcurrentHashMapV8.TreeNode<K, V> xp = p;
            if ((p = (ConcurrentHashMapV8.TreeNode<K, V>)((dir <= 0) ? p.left : p.right)) == null) {
              x.parent = xp;
              if (dir <= 0) {
                xp.left = x;
              } else {
                xp.right = x;
              } 
              r = balanceInsertion(r, x);
              break;
            } 
          } 
        } 
      } 
      this.root = r;
    }
    
    private final void lockRoot() {
      if (!U.compareAndSwapInt(this, LOCKSTATE, 0, 1))
        contendedLock(); 
    }
    
    private final void unlockRoot() {
      this.lockState = 0;
    }
    
    private final void contendedLock() {
      boolean waiting = false;
      while (true) {
        int s;
        while (((s = this.lockState) & 0x1) == 0) {
          if (U.compareAndSwapInt(this, LOCKSTATE, s, 1)) {
            if (waiting)
              this.waiter = null; 
            return;
          } 
        } 
        if ((s & 0x2) == 0) {
          if (U.compareAndSwapInt(this, LOCKSTATE, s, s | 0x2)) {
            waiting = true;
            this.waiter = Thread.currentThread();
          } 
          continue;
        } 
        if (waiting)
          LockSupport.park(this); 
      } 
    }
    
    final ConcurrentHashMapV8.Node<K, V> find(int h, Object k) {
      if (k != null)
        for (ConcurrentHashMapV8.Node<K, V> e = this.first; e != null; e = e.next) {
          int s;
          if (((s = this.lockState) & 0x3) != 0) {
            K ek;
            if (e.hash == h && ((ek = e.key) == k || (ek != null && k.equals(ek))))
              return e; 
          } else if (U.compareAndSwapInt(this, LOCKSTATE, s, s + 4)) {
            ConcurrentHashMapV8.TreeNode<K, V> p;
            try {
              ConcurrentHashMapV8.TreeNode<K, V> r;
              p = ((r = this.root) == null) ? null : r.findTreeNode(h, k, (Class<?>)null);
            } finally {
              int ls;
              do {
              
              } while (!U.compareAndSwapInt(this, LOCKSTATE, ls = this.lockState, ls - 4));
              Thread w;
              if (ls == 6 && (w = this.waiter) != null)
                LockSupport.unpark(w); 
            } 
            return p;
          } 
        }  
      return null;
    }
    
    final ConcurrentHashMapV8.TreeNode<K, V> putTreeVal(int h, K k, V v) {
      Class<?> kc = null;
      ConcurrentHashMapV8.TreeNode<K, V> p = this.root;
      while (true) {
        int dir;
        if (p == null) {
          this.first = this.root = new ConcurrentHashMapV8.TreeNode<K, V>(h, k, v, null, null);
          break;
        } 
        int ph;
        if ((ph = p.hash) > h) {
          dir = -1;
        } else if (ph < h) {
          dir = 1;
        } else {
          K pk;
          if ((pk = p.key) == k || (pk != null && k.equals(pk)))
            return p; 
          if ((kc == null && (kc = ConcurrentHashMapV8.comparableClassFor(k)) == null) || (dir = ConcurrentHashMapV8.compareComparables(kc, k, pk)) == 0)
            if (p.left == null) {
              dir = 1;
            } else {
              ConcurrentHashMapV8.TreeNode<K, V> q;
              ConcurrentHashMapV8.TreeNode<K, V> pr;
              if ((pr = p.right) == null || (q = pr.findTreeNode(h, k, kc)) == null) {
                dir = -1;
              } else {
                return q;
              } 
            }  
        } 
        ConcurrentHashMapV8.TreeNode<K, V> xp = p;
        if ((p = (ConcurrentHashMapV8.TreeNode<K, V>)((dir < 0) ? p.left : p.right)) == null) {
          ConcurrentHashMapV8.TreeNode<K, V> f = this.first;
          ConcurrentHashMapV8.TreeNode<K, V> x = new ConcurrentHashMapV8.TreeNode<K, V>(h, k, v, f, xp);
          if (f != null)
            f.prev = x; 
          if (dir < 0) {
            xp.left = x;
          } else {
            xp.right = x;
          } 
          if (!xp.red) {
            x.red = true;
            break;
          } 
          lockRoot();
          try {
            this.root = balanceInsertion(this.root, x);
          } finally {
            unlockRoot();
          } 
          break;
        } 
      } 
      assert checkInvariants(this.root);
      return null;
    }
    
    final boolean removeTreeNode(ConcurrentHashMapV8.TreeNode<K, V> p) {
      ConcurrentHashMapV8.TreeNode<K, V> next = (ConcurrentHashMapV8.TreeNode<K, V>)p.next;
      ConcurrentHashMapV8.TreeNode<K, V> pred = p.prev;
      if (pred == null) {
        this.first = next;
      } else {
        pred.next = next;
      } 
      if (next != null)
        next.prev = pred; 
      if (this.first == null) {
        this.root = null;
        return true;
      } 
      ConcurrentHashMapV8.TreeNode<K, V> r, rl;
      if ((r = this.root) == null || r.right == null || (rl = r.left) == null || rl.left == null)
        return true; 
      lockRoot();
      try {
        ConcurrentHashMapV8.TreeNode<K, V> replacement, pl = p.left;
        ConcurrentHashMapV8.TreeNode<K, V> pr = p.right;
        if (pl != null && pr != null) {
          ConcurrentHashMapV8.TreeNode<K, V> s = pr;
          ConcurrentHashMapV8.TreeNode<K, V> sl;
          while ((sl = s.left) != null)
            s = sl; 
          boolean c = s.red;
          s.red = p.red;
          p.red = c;
          ConcurrentHashMapV8.TreeNode<K, V> sr = s.right;
          ConcurrentHashMapV8.TreeNode<K, V> pp = p.parent;
          if (s == pr) {
            p.parent = s;
            s.right = p;
          } else {
            ConcurrentHashMapV8.TreeNode<K, V> sp = s.parent;
            if ((p.parent = sp) != null)
              if (s == sp.left) {
                sp.left = p;
              } else {
                sp.right = p;
              }  
            s.right = pr;
            pr.parent = s;
          } 
          p.left = null;
          s.left = pl;
          pl.parent = s;
          if ((p.right = sr) != null)
            sr.parent = p; 
          if ((s.parent = pp) == null) {
            r = s;
          } else if (p == pp.left) {
            pp.left = s;
          } else {
            pp.right = s;
          } 
          if (sr != null) {
            replacement = sr;
          } else {
            replacement = p;
          } 
        } else if (pl != null) {
          replacement = pl;
        } else if (pr != null) {
          replacement = pr;
        } else {
          replacement = p;
        } 
        if (replacement != p) {
          ConcurrentHashMapV8.TreeNode<K, V> pp = replacement.parent = p.parent;
          if (pp == null) {
            r = replacement;
          } else if (p == pp.left) {
            pp.left = replacement;
          } else {
            pp.right = replacement;
          } 
          p.left = p.right = p.parent = null;
        } 
        this.root = p.red ? r : balanceDeletion(r, replacement);
        if (p == replacement) {
          ConcurrentHashMapV8.TreeNode<K, V> pp;
          if ((pp = p.parent) != null) {
            if (p == pp.left) {
              pp.left = null;
            } else if (p == pp.right) {
              pp.right = null;
            } 
            p.parent = null;
          } 
        } 
      } finally {
        unlockRoot();
      } 
      assert checkInvariants(this.root);
      return false;
    }
    
    static <K, V> ConcurrentHashMapV8.TreeNode<K, V> rotateLeft(ConcurrentHashMapV8.TreeNode<K, V> root, ConcurrentHashMapV8.TreeNode<K, V> p) {
      ConcurrentHashMapV8.TreeNode<K, V> r;
      if (p != null && (r = p.right) != null) {
        ConcurrentHashMapV8.TreeNode<K, V> rl;
        if ((rl = p.right = r.left) != null)
          rl.parent = p; 
        ConcurrentHashMapV8.TreeNode<K, V> pp;
        if ((pp = r.parent = p.parent) == null) {
          (root = r).red = false;
        } else if (pp.left == p) {
          pp.left = r;
        } else {
          pp.right = r;
        } 
        r.left = p;
        p.parent = r;
      } 
      return root;
    }
    
    static <K, V> ConcurrentHashMapV8.TreeNode<K, V> rotateRight(ConcurrentHashMapV8.TreeNode<K, V> root, ConcurrentHashMapV8.TreeNode<K, V> p) {
      ConcurrentHashMapV8.TreeNode<K, V> l;
      if (p != null && (l = p.left) != null) {
        ConcurrentHashMapV8.TreeNode<K, V> lr;
        if ((lr = p.left = l.right) != null)
          lr.parent = p; 
        ConcurrentHashMapV8.TreeNode<K, V> pp;
        if ((pp = l.parent = p.parent) == null) {
          (root = l).red = false;
        } else if (pp.right == p) {
          pp.right = l;
        } else {
          pp.left = l;
        } 
        l.right = p;
        p.parent = l;
      } 
      return root;
    }
    
    static <K, V> ConcurrentHashMapV8.TreeNode<K, V> balanceInsertion(ConcurrentHashMapV8.TreeNode<K, V> root, ConcurrentHashMapV8.TreeNode<K, V> x) {
      x.red = true;
      while (true) {
        ConcurrentHashMapV8.TreeNode<K, V> xp;
        if ((xp = x.parent) == null) {
          x.red = false;
          return x;
        } 
        ConcurrentHashMapV8.TreeNode<K, V> xpp;
        if (!xp.red || (xpp = xp.parent) == null)
          return root; 
        ConcurrentHashMapV8.TreeNode<K, V> xppl;
        if (xp == (xppl = xpp.left)) {
          ConcurrentHashMapV8.TreeNode<K, V> xppr;
          if ((xppr = xpp.right) != null && xppr.red) {
            xppr.red = false;
            xp.red = false;
            xpp.red = true;
            x = xpp;
            continue;
          } 
          if (x == xp.right) {
            root = rotateLeft(root, x = xp);
            xpp = ((xp = x.parent) == null) ? null : xp.parent;
          } 
          if (xp != null) {
            xp.red = false;
            if (xpp != null) {
              xpp.red = true;
              root = rotateRight(root, xpp);
            } 
          } 
          continue;
        } 
        if (xppl != null && xppl.red) {
          xppl.red = false;
          xp.red = false;
          xpp.red = true;
          x = xpp;
          continue;
        } 
        if (x == xp.left) {
          root = rotateRight(root, x = xp);
          xpp = ((xp = x.parent) == null) ? null : xp.parent;
        } 
        if (xp != null) {
          xp.red = false;
          if (xpp != null) {
            xpp.red = true;
            root = rotateLeft(root, xpp);
          } 
        } 
      } 
    }
    
    static <K, V> ConcurrentHashMapV8.TreeNode<K, V> balanceDeletion(ConcurrentHashMapV8.TreeNode<K, V> root, ConcurrentHashMapV8.TreeNode<K, V> x) {
      while (true) {
        if (x == null || x == root)
          return root; 
        ConcurrentHashMapV8.TreeNode<K, V> xp;
        if ((xp = x.parent) == null) {
          x.red = false;
          return x;
        } 
        if (x.red) {
          x.red = false;
          return root;
        } 
        ConcurrentHashMapV8.TreeNode<K, V> xpl;
        if ((xpl = xp.left) == x) {
          ConcurrentHashMapV8.TreeNode<K, V> xpr;
          if ((xpr = xp.right) != null && xpr.red) {
            xpr.red = false;
            xp.red = true;
            root = rotateLeft(root, xp);
            xpr = ((xp = x.parent) == null) ? null : xp.right;
          } 
          if (xpr == null) {
            x = xp;
            continue;
          } 
          ConcurrentHashMapV8.TreeNode<K, V> treeNode1 = xpr.left, treeNode2 = xpr.right;
          if ((treeNode2 == null || !treeNode2.red) && (treeNode1 == null || !treeNode1.red)) {
            xpr.red = true;
            x = xp;
            continue;
          } 
          if (treeNode2 == null || !treeNode2.red) {
            if (treeNode1 != null)
              treeNode1.red = false; 
            xpr.red = true;
            root = rotateRight(root, xpr);
            xpr = ((xp = x.parent) == null) ? null : xp.right;
          } 
          if (xpr != null) {
            xpr.red = (xp == null) ? false : xp.red;
            if ((treeNode2 = xpr.right) != null)
              treeNode2.red = false; 
          } 
          if (xp != null) {
            xp.red = false;
            root = rotateLeft(root, xp);
          } 
          x = root;
          continue;
        } 
        if (xpl != null && xpl.red) {
          xpl.red = false;
          xp.red = true;
          root = rotateRight(root, xp);
          xpl = ((xp = x.parent) == null) ? null : xp.left;
        } 
        if (xpl == null) {
          x = xp;
          continue;
        } 
        ConcurrentHashMapV8.TreeNode<K, V> sl = xpl.left, sr = xpl.right;
        if ((sl == null || !sl.red) && (sr == null || !sr.red)) {
          xpl.red = true;
          x = xp;
          continue;
        } 
        if (sl == null || !sl.red) {
          if (sr != null)
            sr.red = false; 
          xpl.red = true;
          root = rotateLeft(root, xpl);
          xpl = ((xp = x.parent) == null) ? null : xp.left;
        } 
        if (xpl != null) {
          xpl.red = (xp == null) ? false : xp.red;
          if ((sl = xpl.left) != null)
            sl.red = false; 
        } 
        if (xp != null) {
          xp.red = false;
          root = rotateRight(root, xp);
        } 
        x = root;
      } 
    }
    
    static <K, V> boolean checkInvariants(ConcurrentHashMapV8.TreeNode<K, V> t) {
      ConcurrentHashMapV8.TreeNode<K, V> tp = t.parent, tl = t.left, tr = t.right;
      ConcurrentHashMapV8.TreeNode<K, V> tb = t.prev, tn = (ConcurrentHashMapV8.TreeNode<K, V>)t.next;
      if (tb != null && tb.next != t)
        return false; 
      if (tn != null && tn.prev != t)
        return false; 
      if (tp != null && t != tp.left && t != tp.right)
        return false; 
      if (tl != null && (tl.parent != t || tl.hash > t.hash))
        return false; 
      if (tr != null && (tr.parent != t || tr.hash < t.hash))
        return false; 
      if (t.red && tl != null && tl.red && tr != null && tr.red)
        return false; 
      if (tl != null && !checkInvariants(tl))
        return false; 
      if (tr != null && !checkInvariants(tr))
        return false; 
      return true;
    }
    
    static {
      try {
        U = ConcurrentHashMapV8.getUnsafe();
        Class<?> k = TreeBin.class;
        LOCKSTATE = U.objectFieldOffset(k.getDeclaredField("lockState"));
      } catch (Exception e) {
        throw new Error(e);
      } 
    }
  }
  
  static class Traverser<K, V> {
    ConcurrentHashMapV8.Node<K, V>[] tab;
    
    ConcurrentHashMapV8.Node<K, V> next;
    
    int index;
    
    int baseIndex;
    
    int baseLimit;
    
    final int baseSize;
    
    Traverser(ConcurrentHashMapV8.Node<K, V>[] tab, int size, int index, int limit) {
      this.tab = tab;
      this.baseSize = size;
      this.baseIndex = this.index = index;
      this.baseLimit = limit;
      this.next = null;
    }
    
    final ConcurrentHashMapV8.Node<K, V> advance() {
      ConcurrentHashMapV8.Node<K, V> e;
      if ((e = this.next) != null)
        e = e.next; 
      while (true) {
        if (e != null)
          return this.next = e; 
        ConcurrentHashMapV8.Node<K, V>[] t;
        int i;
        int n;
        if (this.baseIndex >= this.baseLimit || (t = this.tab) == null || (n = t.length) <= (i = this.index) || i < 0)
          return this.next = null; 
        if ((e = ConcurrentHashMapV8.<K, V>tabAt(t, this.index)) != null && e.hash < 0) {
          if (e instanceof ConcurrentHashMapV8.ForwardingNode) {
            this.tab = (ConcurrentHashMapV8.Node<K, V>[])((ConcurrentHashMapV8.ForwardingNode)e).nextTable;
            e = null;
            continue;
          } 
          if (e instanceof ConcurrentHashMapV8.TreeBin) {
            e = ((ConcurrentHashMapV8.TreeBin)e).first;
          } else {
            e = null;
          } 
        } 
        if ((this.index += this.baseSize) >= n)
          this.index = ++this.baseIndex; 
      } 
    }
  }
  
  static class BaseIterator<K, V> extends Traverser<K, V> {
    final ConcurrentHashMapV8<K, V> map;
    
    ConcurrentHashMapV8.Node<K, V> lastReturned;
    
    BaseIterator(ConcurrentHashMapV8.Node<K, V>[] tab, int size, int index, int limit, ConcurrentHashMapV8<K, V> map) {
      super(tab, size, index, limit);
      this.map = map;
      advance();
    }
    
    public final boolean hasNext() {
      return (this.next != null);
    }
    
    public final boolean hasMoreElements() {
      return (this.next != null);
    }
    
    public final void remove() {
      ConcurrentHashMapV8.Node<K, V> p;
      if ((p = this.lastReturned) == null)
        throw new IllegalStateException(); 
      this.lastReturned = null;
      this.map.replaceNode(p.key, null, null);
    }
  }
  
  static final class KeyIterator<K, V> extends BaseIterator<K, V> implements Iterator<K>, Enumeration<K> {
    KeyIterator(ConcurrentHashMapV8.Node<K, V>[] tab, int index, int size, int limit, ConcurrentHashMapV8<K, V> map) {
      super(tab, index, size, limit, map);
    }
    
    public final K next() {
      ConcurrentHashMapV8.Node<K, V> p;
      if ((p = this.next) == null)
        throw new NoSuchElementException(); 
      K k = p.key;
      this.lastReturned = p;
      advance();
      return k;
    }
    
    public final K nextElement() {
      return next();
    }
  }
  
  static final class ValueIterator<K, V> extends BaseIterator<K, V> implements Iterator<V>, Enumeration<V> {
    ValueIterator(ConcurrentHashMapV8.Node<K, V>[] tab, int index, int size, int limit, ConcurrentHashMapV8<K, V> map) {
      super(tab, index, size, limit, map);
    }
    
    public final V next() {
      ConcurrentHashMapV8.Node<K, V> p;
      if ((p = this.next) == null)
        throw new NoSuchElementException(); 
      V v = p.val;
      this.lastReturned = p;
      advance();
      return v;
    }
    
    public final V nextElement() {
      return next();
    }
  }
  
  static final class EntryIterator<K, V> extends BaseIterator<K, V> implements Iterator<Map.Entry<K, V>> {
    EntryIterator(ConcurrentHashMapV8.Node<K, V>[] tab, int index, int size, int limit, ConcurrentHashMapV8<K, V> map) {
      super(tab, index, size, limit, map);
    }
    
    public final Map.Entry<K, V> next() {
      ConcurrentHashMapV8.Node<K, V> p;
      if ((p = this.next) == null)
        throw new NoSuchElementException(); 
      K k = p.key;
      V v = p.val;
      this.lastReturned = p;
      advance();
      return new ConcurrentHashMapV8.MapEntry<K, V>(k, v, this.map);
    }
  }
  
  static final class MapEntry<K, V> implements Map.Entry<K, V> {
    final K key;
    
    V val;
    
    final ConcurrentHashMapV8<K, V> map;
    
    MapEntry(K key, V val, ConcurrentHashMapV8<K, V> map) {
      this.key = key;
      this.val = val;
      this.map = map;
    }
    
    public K getKey() {
      return this.key;
    }
    
    public V getValue() {
      return this.val;
    }
    
    public int hashCode() {
      return this.key.hashCode() ^ this.val.hashCode();
    }
    
    public String toString() {
      return (new StringBuilder()).append(this.key).append("=").append(this.val).toString();
    }
    
    public boolean equals(Object o) {
      Object k;
      Object v;
      Map.Entry<?, ?> e;
      return (o instanceof Map.Entry && (k = (e = (Map.Entry<?, ?>)o).getKey()) != null && (v = e.getValue()) != null && (k == this.key || k.equals(this.key)) && (v == this.val || v.equals(this.val)));
    }
    
    public V setValue(V value) {
      if (value == null)
        throw new NullPointerException(); 
      V v = this.val;
      this.val = value;
      this.map.put(this.key, value);
      return v;
    }
  }
  
  static final class KeySpliterator<K, V> extends Traverser<K, V> implements ConcurrentHashMapSpliterator<K> {
    long est;
    
    KeySpliterator(ConcurrentHashMapV8.Node<K, V>[] tab, int size, int index, int limit, long est) {
      super(tab, size, index, limit);
      this.est = est;
    }
    
    public ConcurrentHashMapV8.ConcurrentHashMapSpliterator<K> trySplit() {
      int i;
      int f;
      int h;
      return ((h = (i = this.baseIndex) + (f = this.baseLimit) >>> 1) <= i) ? null : new KeySpliterator(this.tab, this.baseSize, this.baseLimit = h, f, this.est >>>= 1L);
    }
    
    public void forEachRemaining(ConcurrentHashMapV8.Action<? super K> action) {
      if (action == null)
        throw new NullPointerException(); 
      ConcurrentHashMapV8.Node<K, V> p;
      while ((p = advance()) != null)
        action.apply(p.key); 
    }
    
    public boolean tryAdvance(ConcurrentHashMapV8.Action<? super K> action) {
      if (action == null)
        throw new NullPointerException(); 
      ConcurrentHashMapV8.Node<K, V> p;
      if ((p = advance()) == null)
        return false; 
      action.apply(p.key);
      return true;
    }
    
    public long estimateSize() {
      return this.est;
    }
  }
  
  static final class ValueSpliterator<K, V> extends Traverser<K, V> implements ConcurrentHashMapSpliterator<V> {
    long est;
    
    ValueSpliterator(ConcurrentHashMapV8.Node<K, V>[] tab, int size, int index, int limit, long est) {
      super(tab, size, index, limit);
      this.est = est;
    }
    
    public ConcurrentHashMapV8.ConcurrentHashMapSpliterator<V> trySplit() {
      int i;
      int f;
      int h;
      return ((h = (i = this.baseIndex) + (f = this.baseLimit) >>> 1) <= i) ? null : new ValueSpliterator(this.tab, this.baseSize, this.baseLimit = h, f, this.est >>>= 1L);
    }
    
    public void forEachRemaining(ConcurrentHashMapV8.Action<? super V> action) {
      if (action == null)
        throw new NullPointerException(); 
      ConcurrentHashMapV8.Node<K, V> p;
      while ((p = advance()) != null)
        action.apply(p.val); 
    }
    
    public boolean tryAdvance(ConcurrentHashMapV8.Action<? super V> action) {
      if (action == null)
        throw new NullPointerException(); 
      ConcurrentHashMapV8.Node<K, V> p;
      if ((p = advance()) == null)
        return false; 
      action.apply(p.val);
      return true;
    }
    
    public long estimateSize() {
      return this.est;
    }
  }
  
  static final class EntrySpliterator<K, V> extends Traverser<K, V> implements ConcurrentHashMapSpliterator<Map.Entry<K, V>> {
    final ConcurrentHashMapV8<K, V> map;
    
    long est;
    
    EntrySpliterator(ConcurrentHashMapV8.Node<K, V>[] tab, int size, int index, int limit, long est, ConcurrentHashMapV8<K, V> map) {
      super(tab, size, index, limit);
      this.map = map;
      this.est = est;
    }
    
    public ConcurrentHashMapV8.ConcurrentHashMapSpliterator<Map.Entry<K, V>> trySplit() {
      int i;
      int f;
      int h;
      return ((h = (i = this.baseIndex) + (f = this.baseLimit) >>> 1) <= i) ? null : new EntrySpliterator(this.tab, this.baseSize, this.baseLimit = h, f, this.est >>>= 1L, this.map);
    }
    
    public void forEachRemaining(ConcurrentHashMapV8.Action<? super Map.Entry<K, V>> action) {
      if (action == null)
        throw new NullPointerException(); 
      ConcurrentHashMapV8.Node<K, V> p;
      while ((p = advance()) != null)
        action.apply(new ConcurrentHashMapV8.MapEntry<K, V>(p.key, p.val, this.map)); 
    }
    
    public boolean tryAdvance(ConcurrentHashMapV8.Action<? super Map.Entry<K, V>> action) {
      if (action == null)
        throw new NullPointerException(); 
      ConcurrentHashMapV8.Node<K, V> p;
      if ((p = advance()) == null)
        return false; 
      action.apply(new ConcurrentHashMapV8.MapEntry<K, V>(p.key, p.val, this.map));
      return true;
    }
    
    public long estimateSize() {
      return this.est;
    }
  }
  
  final int batchFor(long b) {
    long n;
    if (b == Long.MAX_VALUE || (n = sumCount()) <= 1L || n < b)
      return 0; 
    int sp = ForkJoinPool.getCommonPoolParallelism() << 2;
    return (b <= 0L || (n /= b) >= sp) ? sp : (int)n;
  }
  
  public void forEach(long parallelismThreshold, BiAction<? super K, ? super V> action) {
    if (action == null)
      throw new NullPointerException(); 
    (new ForEachMappingTask<Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, action)).invoke();
  }
  
  public <U> void forEach(long parallelismThreshold, BiFun<? super K, ? super V, ? extends U> transformer, Action<? super U> action) {
    if (transformer == null || action == null)
      throw new NullPointerException(); 
    (new ForEachTransformedMappingTask<Object, Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, transformer, action)).invoke();
  }
  
  public <U> U search(long parallelismThreshold, BiFun<? super K, ? super V, ? extends U> searchFunction) {
    if (searchFunction == null)
      throw new NullPointerException(); 
    return (new SearchMappingsTask<Object, Object, U>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, searchFunction, new AtomicReference<U>())).invoke();
  }
  
  public <U> U reduce(long parallelismThreshold, BiFun<? super K, ? super V, ? extends U> transformer, BiFun<? super U, ? super U, ? extends U> reducer) {
    if (transformer == null || reducer == null)
      throw new NullPointerException(); 
    return (new MapReduceMappingsTask<Object, Object, U>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, null, transformer, reducer)).invoke();
  }
  
  public double reduceToDouble(long parallelismThreshold, ObjectByObjectToDouble<? super K, ? super V> transformer, double basis, DoubleByDoubleToDouble reducer) {
    if (transformer == null || reducer == null)
      throw new NullPointerException(); 
    return (new MapReduceMappingsToDoubleTask<Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, null, transformer, basis, reducer)).invoke().doubleValue();
  }
  
  public long reduceToLong(long parallelismThreshold, ObjectByObjectToLong<? super K, ? super V> transformer, long basis, LongByLongToLong reducer) {
    if (transformer == null || reducer == null)
      throw new NullPointerException(); 
    return (new MapReduceMappingsToLongTask<Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, null, transformer, basis, reducer)).invoke().longValue();
  }
  
  public int reduceToInt(long parallelismThreshold, ObjectByObjectToInt<? super K, ? super V> transformer, int basis, IntByIntToInt reducer) {
    if (transformer == null || reducer == null)
      throw new NullPointerException(); 
    return (new MapReduceMappingsToIntTask<Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, null, transformer, basis, reducer)).invoke().intValue();
  }
  
  public void forEachKey(long parallelismThreshold, Action<? super K> action) {
    if (action == null)
      throw new NullPointerException(); 
    (new ForEachKeyTask<Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, action)).invoke();
  }
  
  public <U> void forEachKey(long parallelismThreshold, Fun<? super K, ? extends U> transformer, Action<? super U> action) {
    if (transformer == null || action == null)
      throw new NullPointerException(); 
    (new ForEachTransformedKeyTask<Object, Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, transformer, action)).invoke();
  }
  
  public <U> U searchKeys(long parallelismThreshold, Fun<? super K, ? extends U> searchFunction) {
    if (searchFunction == null)
      throw new NullPointerException(); 
    return (new SearchKeysTask<Object, Object, U>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, searchFunction, new AtomicReference<U>())).invoke();
  }
  
  public K reduceKeys(long parallelismThreshold, BiFun<? super K, ? super K, ? extends K> reducer) {
    if (reducer == null)
      throw new NullPointerException(); 
    return (new ReduceKeysTask<K, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<K, ?>[])this.table, null, reducer)).invoke();
  }
  
  public <U> U reduceKeys(long parallelismThreshold, Fun<? super K, ? extends U> transformer, BiFun<? super U, ? super U, ? extends U> reducer) {
    if (transformer == null || reducer == null)
      throw new NullPointerException(); 
    return (new MapReduceKeysTask<Object, Object, U>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, null, transformer, reducer)).invoke();
  }
  
  public double reduceKeysToDouble(long parallelismThreshold, ObjectToDouble<? super K> transformer, double basis, DoubleByDoubleToDouble reducer) {
    if (transformer == null || reducer == null)
      throw new NullPointerException(); 
    return (new MapReduceKeysToDoubleTask<Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, null, transformer, basis, reducer)).invoke().doubleValue();
  }
  
  public long reduceKeysToLong(long parallelismThreshold, ObjectToLong<? super K> transformer, long basis, LongByLongToLong reducer) {
    if (transformer == null || reducer == null)
      throw new NullPointerException(); 
    return (new MapReduceKeysToLongTask<Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, null, transformer, basis, reducer)).invoke().longValue();
  }
  
  public int reduceKeysToInt(long parallelismThreshold, ObjectToInt<? super K> transformer, int basis, IntByIntToInt reducer) {
    if (transformer == null || reducer == null)
      throw new NullPointerException(); 
    return (new MapReduceKeysToIntTask<Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, null, transformer, basis, reducer)).invoke().intValue();
  }
  
  public void forEachValue(long parallelismThreshold, Action<? super V> action) {
    if (action == null)
      throw new NullPointerException(); 
    (new ForEachValueTask<Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, action)).invoke();
  }
  
  public <U> void forEachValue(long parallelismThreshold, Fun<? super V, ? extends U> transformer, Action<? super U> action) {
    if (transformer == null || action == null)
      throw new NullPointerException(); 
    (new ForEachTransformedValueTask<Object, Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, transformer, action)).invoke();
  }
  
  public <U> U searchValues(long parallelismThreshold, Fun<? super V, ? extends U> searchFunction) {
    if (searchFunction == null)
      throw new NullPointerException(); 
    return (new SearchValuesTask<Object, Object, U>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, searchFunction, new AtomicReference<U>())).invoke();
  }
  
  public V reduceValues(long parallelismThreshold, BiFun<? super V, ? super V, ? extends V> reducer) {
    if (reducer == null)
      throw new NullPointerException(); 
    return (new ReduceValuesTask<Object, V>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, V>[])this.table, null, reducer)).invoke();
  }
  
  public <U> U reduceValues(long parallelismThreshold, Fun<? super V, ? extends U> transformer, BiFun<? super U, ? super U, ? extends U> reducer) {
    if (transformer == null || reducer == null)
      throw new NullPointerException(); 
    return (new MapReduceValuesTask<Object, Object, U>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, null, transformer, reducer)).invoke();
  }
  
  public double reduceValuesToDouble(long parallelismThreshold, ObjectToDouble<? super V> transformer, double basis, DoubleByDoubleToDouble reducer) {
    if (transformer == null || reducer == null)
      throw new NullPointerException(); 
    return (new MapReduceValuesToDoubleTask<Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, null, transformer, basis, reducer)).invoke().doubleValue();
  }
  
  public long reduceValuesToLong(long parallelismThreshold, ObjectToLong<? super V> transformer, long basis, LongByLongToLong reducer) {
    if (transformer == null || reducer == null)
      throw new NullPointerException(); 
    return (new MapReduceValuesToLongTask<Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, null, transformer, basis, reducer)).invoke().longValue();
  }
  
  public int reduceValuesToInt(long parallelismThreshold, ObjectToInt<? super V> transformer, int basis, IntByIntToInt reducer) {
    if (transformer == null || reducer == null)
      throw new NullPointerException(); 
    return (new MapReduceValuesToIntTask<Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, null, transformer, basis, reducer)).invoke().intValue();
  }
  
  public void forEachEntry(long parallelismThreshold, Action<? super Map.Entry<K, V>> action) {
    if (action == null)
      throw new NullPointerException(); 
    (new ForEachEntryTask<Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, action)).invoke();
  }
  
  public <U> void forEachEntry(long parallelismThreshold, Fun<Map.Entry<K, V>, ? extends U> transformer, Action<? super U> action) {
    if (transformer == null || action == null)
      throw new NullPointerException(); 
    (new ForEachTransformedEntryTask<Object, Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, transformer, action)).invoke();
  }
  
  public <U> U searchEntries(long parallelismThreshold, Fun<Map.Entry<K, V>, ? extends U> searchFunction) {
    if (searchFunction == null)
      throw new NullPointerException(); 
    return (new SearchEntriesTask<Object, Object, U>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, searchFunction, new AtomicReference<U>())).invoke();
  }
  
  public Map.Entry<K, V> reduceEntries(long parallelismThreshold, BiFun<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer) {
    if (reducer == null)
      throw new NullPointerException(); 
    return (new ReduceEntriesTask<K, V>(null, batchFor(parallelismThreshold), 0, 0, this.table, null, reducer)).invoke();
  }
  
  public <U> U reduceEntries(long parallelismThreshold, Fun<Map.Entry<K, V>, ? extends U> transformer, BiFun<? super U, ? super U, ? extends U> reducer) {
    if (transformer == null || reducer == null)
      throw new NullPointerException(); 
    return (new MapReduceEntriesTask<Object, Object, U>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, null, transformer, reducer)).invoke();
  }
  
  public double reduceEntriesToDouble(long parallelismThreshold, ObjectToDouble<Map.Entry<K, V>> transformer, double basis, DoubleByDoubleToDouble reducer) {
    if (transformer == null || reducer == null)
      throw new NullPointerException(); 
    return (new MapReduceEntriesToDoubleTask<Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, null, transformer, basis, reducer)).invoke().doubleValue();
  }
  
  public long reduceEntriesToLong(long parallelismThreshold, ObjectToLong<Map.Entry<K, V>> transformer, long basis, LongByLongToLong reducer) {
    if (transformer == null || reducer == null)
      throw new NullPointerException(); 
    return (new MapReduceEntriesToLongTask<Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, null, transformer, basis, reducer)).invoke().longValue();
  }
  
  public int reduceEntriesToInt(long parallelismThreshold, ObjectToInt<Map.Entry<K, V>> transformer, int basis, IntByIntToInt reducer) {
    if (transformer == null || reducer == null)
      throw new NullPointerException(); 
    return (new MapReduceEntriesToIntTask<Object, Object>(null, batchFor(parallelismThreshold), 0, 0, (Node<?, ?>[])this.table, null, transformer, basis, reducer)).invoke().intValue();
  }
  
  static abstract class CollectionView<K, V, E> implements Collection<E>, Serializable {
    private static final long serialVersionUID = 7249069246763182397L;
    
    final ConcurrentHashMapV8<K, V> map;
    
    private static final String oomeMsg = "Required array size too large";
    
    CollectionView(ConcurrentHashMapV8<K, V> map) {
      this.map = map;
    }
    
    public ConcurrentHashMapV8<K, V> getMap() {
      return this.map;
    }
    
    public final void clear() {
      this.map.clear();
    }
    
    public final int size() {
      return this.map.size();
    }
    
    public final boolean isEmpty() {
      return this.map.isEmpty();
    }
    
    public abstract Iterator<E> iterator();
    
    public abstract boolean contains(Object param1Object);
    
    public abstract boolean remove(Object param1Object);
    
    public final Object[] toArray() {
      long sz = this.map.mappingCount();
      if (sz > 2147483639L)
        throw new OutOfMemoryError("Required array size too large"); 
      int n = (int)sz;
      Object[] r = new Object[n];
      int i = 0;
      for (E e : this) {
        if (i == n) {
          if (n >= 2147483639)
            throw new OutOfMemoryError("Required array size too large"); 
          if (n >= 1073741819) {
            n = 2147483639;
          } else {
            n += (n >>> 1) + 1;
          } 
          r = Arrays.copyOf(r, n);
        } 
        r[i++] = e;
      } 
      return (i == n) ? r : Arrays.<Object>copyOf(r, i);
    }
    
    public final <T> T[] toArray(T[] a) {
      long sz = this.map.mappingCount();
      if (sz > 2147483639L)
        throw new OutOfMemoryError("Required array size too large"); 
      int m = (int)sz;
      T[] r = (a.length >= m) ? a : (T[])Array.newInstance(a.getClass().getComponentType(), m);
      int n = r.length;
      int i = 0;
      for (E e : this) {
        if (i == n) {
          if (n >= 2147483639)
            throw new OutOfMemoryError("Required array size too large"); 
          if (n >= 1073741819) {
            n = 2147483639;
          } else {
            n += (n >>> 1) + 1;
          } 
          r = Arrays.copyOf(r, n);
        } 
        r[i++] = (T)e;
      } 
      if (a == r && i < n) {
        r[i] = null;
        return r;
      } 
      return (i == n) ? r : Arrays.<T>copyOf(r, i);
    }
    
    public final String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append('[');
      Iterator<E> it = iterator();
      if (it.hasNext())
        while (true) {
          Object e = it.next();
          sb.append((e == this) ? "(this Collection)" : e);
          if (!it.hasNext())
            break; 
          sb.append(',').append(' ');
        }  
      return sb.append(']').toString();
    }
    
    public final boolean containsAll(Collection<?> c) {
      if (c != this)
        for (Object e : c) {
          if (e == null || !contains(e))
            return false; 
        }  
      return true;
    }
    
    public final boolean removeAll(Collection<?> c) {
      boolean modified = false;
      for (Iterator<E> it = iterator(); it.hasNext();) {
        if (c.contains(it.next())) {
          it.remove();
          modified = true;
        } 
      } 
      return modified;
    }
    
    public final boolean retainAll(Collection<?> c) {
      boolean modified = false;
      for (Iterator<E> it = iterator(); it.hasNext();) {
        if (!c.contains(it.next())) {
          it.remove();
          modified = true;
        } 
      } 
      return modified;
    }
  }
  
  public static class KeySetView<K, V> extends CollectionView<K, V, K> implements Set<K>, Serializable {
    private static final long serialVersionUID = 7249069246763182397L;
    
    private final V value;
    
    KeySetView(ConcurrentHashMapV8<K, V> map, V value) {
      super(map);
      this.value = value;
    }
    
    public V getMappedValue() {
      return this.value;
    }
    
    public boolean contains(Object o) {
      return this.map.containsKey(o);
    }
    
    public boolean remove(Object o) {
      return (this.map.remove(o) != null);
    }
    
    public Iterator<K> iterator() {
      ConcurrentHashMapV8<K, V> m = this.map;
      ConcurrentHashMapV8.Node<K, V>[] t;
      int f = ((t = m.table) == null) ? 0 : t.length;
      return new ConcurrentHashMapV8.KeyIterator<K, V>(t, f, 0, f, m);
    }
    
    public boolean add(K e) {
      V v;
      if ((v = this.value) == null)
        throw new UnsupportedOperationException(); 
      return (this.map.putVal(e, v, true) == null);
    }
    
    public boolean addAll(Collection<? extends K> c) {
      boolean added = false;
      V v;
      if ((v = this.value) == null)
        throw new UnsupportedOperationException(); 
      for (K e : c) {
        if (this.map.putVal(e, v, true) == null)
          added = true; 
      } 
      return added;
    }
    
    public int hashCode() {
      int h = 0;
      for (K e : this)
        h += e.hashCode(); 
      return h;
    }
    
    public boolean equals(Object o) {
      Set<?> c;
      return (o instanceof Set && ((c = (Set)o) == this || (containsAll(c) && c.containsAll(this))));
    }
    
    public ConcurrentHashMapV8.ConcurrentHashMapSpliterator<K> spliterator166() {
      ConcurrentHashMapV8<K, V> m = this.map;
      long n = m.sumCount();
      ConcurrentHashMapV8.Node<K, V>[] t;
      int f = ((t = m.table) == null) ? 0 : t.length;
      return new ConcurrentHashMapV8.KeySpliterator<K, V>(t, f, 0, f, (n < 0L) ? 0L : n);
    }
    
    public void forEach(ConcurrentHashMapV8.Action<? super K> action) {
      if (action == null)
        throw new NullPointerException(); 
      ConcurrentHashMapV8.Node<K, V>[] t;
      if ((t = this.map.table) != null) {
        ConcurrentHashMapV8.Traverser<K, V> it = new ConcurrentHashMapV8.Traverser<K, V>(t, t.length, 0, t.length);
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = it.advance()) != null)
          action.apply(p.key); 
      } 
    }
  }
  
  static final class ValuesView<K, V> extends CollectionView<K, V, V> implements Collection<V>, Serializable {
    private static final long serialVersionUID = 2249069246763182397L;
    
    ValuesView(ConcurrentHashMapV8<K, V> map) {
      super(map);
    }
    
    public final boolean contains(Object o) {
      return this.map.containsValue(o);
    }
    
    public final boolean remove(Object o) {
      if (o != null)
        for (Iterator<V> it = iterator(); it.hasNext();) {
          if (o.equals(it.next())) {
            it.remove();
            return true;
          } 
        }  
      return false;
    }
    
    public final Iterator<V> iterator() {
      ConcurrentHashMapV8<K, V> m = this.map;
      ConcurrentHashMapV8.Node<K, V>[] t;
      int f = ((t = m.table) == null) ? 0 : t.length;
      return new ConcurrentHashMapV8.ValueIterator<K, V>(t, f, 0, f, m);
    }
    
    public final boolean add(V e) {
      throw new UnsupportedOperationException();
    }
    
    public final boolean addAll(Collection<? extends V> c) {
      throw new UnsupportedOperationException();
    }
    
    public ConcurrentHashMapV8.ConcurrentHashMapSpliterator<V> spliterator166() {
      ConcurrentHashMapV8<K, V> m = this.map;
      long n = m.sumCount();
      ConcurrentHashMapV8.Node<K, V>[] t;
      int f = ((t = m.table) == null) ? 0 : t.length;
      return new ConcurrentHashMapV8.ValueSpliterator<K, V>(t, f, 0, f, (n < 0L) ? 0L : n);
    }
    
    public void forEach(ConcurrentHashMapV8.Action<? super V> action) {
      if (action == null)
        throw new NullPointerException(); 
      ConcurrentHashMapV8.Node<K, V>[] t;
      if ((t = this.map.table) != null) {
        ConcurrentHashMapV8.Traverser<K, V> it = new ConcurrentHashMapV8.Traverser<K, V>(t, t.length, 0, t.length);
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = it.advance()) != null)
          action.apply(p.val); 
      } 
    }
  }
  
  static final class EntrySetView<K, V> extends CollectionView<K, V, Map.Entry<K, V>> implements Set<Map.Entry<K, V>>, Serializable {
    private static final long serialVersionUID = 2249069246763182397L;
    
    EntrySetView(ConcurrentHashMapV8<K, V> map) {
      super(map);
    }
    
    public boolean contains(Object o) {
      Object k;
      Object v;
      Object r;
      Map.Entry<?, ?> e;
      return (o instanceof Map.Entry && (k = (e = (Map.Entry<?, ?>)o).getKey()) != null && (r = this.map.get(k)) != null && (v = e.getValue()) != null && (v == r || v.equals(r)));
    }
    
    public boolean remove(Object o) {
      Object k;
      Object v;
      Map.Entry<?, ?> e;
      return (o instanceof Map.Entry && (k = (e = (Map.Entry<?, ?>)o).getKey()) != null && (v = e.getValue()) != null && this.map.remove(k, v));
    }
    
    public Iterator<Map.Entry<K, V>> iterator() {
      ConcurrentHashMapV8<K, V> m = this.map;
      ConcurrentHashMapV8.Node<K, V>[] t;
      int f = ((t = m.table) == null) ? 0 : t.length;
      return new ConcurrentHashMapV8.EntryIterator<K, V>(t, f, 0, f, m);
    }
    
    public boolean add(Map.Entry<K, V> e) {
      return (this.map.putVal(e.getKey(), e.getValue(), false) == null);
    }
    
    public boolean addAll(Collection<? extends Map.Entry<K, V>> c) {
      boolean added = false;
      for (Map.Entry<K, V> e : c) {
        if (add(e))
          added = true; 
      } 
      return added;
    }
    
    public final int hashCode() {
      int h = 0;
      ConcurrentHashMapV8.Node<K, V>[] t;
      if ((t = this.map.table) != null) {
        ConcurrentHashMapV8.Traverser<K, V> it = new ConcurrentHashMapV8.Traverser<K, V>(t, t.length, 0, t.length);
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = it.advance()) != null)
          h += p.hashCode(); 
      } 
      return h;
    }
    
    public final boolean equals(Object o) {
      Set<?> c;
      return (o instanceof Set && ((c = (Set)o) == this || (containsAll(c) && c.containsAll(this))));
    }
    
    public ConcurrentHashMapV8.ConcurrentHashMapSpliterator<Map.Entry<K, V>> spliterator166() {
      ConcurrentHashMapV8<K, V> m = this.map;
      long n = m.sumCount();
      ConcurrentHashMapV8.Node<K, V>[] t;
      int f = ((t = m.table) == null) ? 0 : t.length;
      return new ConcurrentHashMapV8.EntrySpliterator<K, V>(t, f, 0, f, (n < 0L) ? 0L : n, m);
    }
    
    public void forEach(ConcurrentHashMapV8.Action<? super Map.Entry<K, V>> action) {
      if (action == null)
        throw new NullPointerException(); 
      ConcurrentHashMapV8.Node<K, V>[] t;
      if ((t = this.map.table) != null) {
        ConcurrentHashMapV8.Traverser<K, V> it = new ConcurrentHashMapV8.Traverser<K, V>(t, t.length, 0, t.length);
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = it.advance()) != null)
          action.apply(new ConcurrentHashMapV8.MapEntry<K, V>(p.key, p.val, this.map)); 
      } 
    }
  }
  
  static abstract class BulkTask<K, V, R> extends CountedCompleter<R> {
    ConcurrentHashMapV8.Node<K, V>[] tab;
    
    ConcurrentHashMapV8.Node<K, V> next;
    
    int index;
    
    int baseIndex;
    
    int baseLimit;
    
    final int baseSize;
    
    int batch;
    
    BulkTask(BulkTask<K, V, ?> par, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t) {
      super(par);
      this.batch = b;
      this.index = this.baseIndex = i;
      if ((this.tab = t) == null) {
        this.baseSize = this.baseLimit = 0;
      } else if (par == null) {
        this.baseSize = this.baseLimit = t.length;
      } else {
        this.baseLimit = f;
        this.baseSize = par.baseSize;
      } 
    }
    
    final ConcurrentHashMapV8.Node<K, V> advance() {
      ConcurrentHashMapV8.Node<K, V> e;
      if ((e = this.next) != null)
        e = e.next; 
      while (true) {
        if (e != null)
          return this.next = e; 
        ConcurrentHashMapV8.Node<K, V>[] t;
        int i;
        int n;
        if (this.baseIndex >= this.baseLimit || (t = this.tab) == null || (n = t.length) <= (i = this.index) || i < 0)
          return this.next = null; 
        if ((e = ConcurrentHashMapV8.<K, V>tabAt(t, this.index)) != null && e.hash < 0) {
          if (e instanceof ConcurrentHashMapV8.ForwardingNode) {
            this.tab = (ConcurrentHashMapV8.Node<K, V>[])((ConcurrentHashMapV8.ForwardingNode)e).nextTable;
            e = null;
            continue;
          } 
          if (e instanceof ConcurrentHashMapV8.TreeBin) {
            e = ((ConcurrentHashMapV8.TreeBin)e).first;
          } else {
            e = null;
          } 
        } 
        if ((this.index += this.baseSize) >= n)
          this.index = ++this.baseIndex; 
      } 
    }
  }
  
  static final class ForEachKeyTask<K, V> extends BulkTask<K, V, Void> {
    final ConcurrentHashMapV8.Action<? super K> action;
    
    ForEachKeyTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, ConcurrentHashMapV8.Action<? super K> action) {
      super(p, b, i, f, t);
      this.action = action;
    }
    
    public final void compute() {
      ConcurrentHashMapV8.Action<? super K> action;
      if ((action = this.action) != null) {
        int f;
        int h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (new ForEachKeyTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, action)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null)
          action.apply(p.key); 
        propagateCompletion();
      } 
    }
  }
  
  static final class ForEachValueTask<K, V> extends BulkTask<K, V, Void> {
    final ConcurrentHashMapV8.Action<? super V> action;
    
    ForEachValueTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, ConcurrentHashMapV8.Action<? super V> action) {
      super(p, b, i, f, t);
      this.action = action;
    }
    
    public final void compute() {
      ConcurrentHashMapV8.Action<? super V> action;
      if ((action = this.action) != null) {
        int f;
        int h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (new ForEachValueTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, action)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null)
          action.apply(p.val); 
        propagateCompletion();
      } 
    }
  }
  
  static final class ForEachEntryTask<K, V> extends BulkTask<K, V, Void> {
    final ConcurrentHashMapV8.Action<? super Map.Entry<K, V>> action;
    
    ForEachEntryTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, ConcurrentHashMapV8.Action<? super Map.Entry<K, V>> action) {
      super(p, b, i, f, t);
      this.action = action;
    }
    
    public final void compute() {
      ConcurrentHashMapV8.Action<? super Map.Entry<K, V>> action;
      if ((action = this.action) != null) {
        int f;
        int h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (new ForEachEntryTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, action)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null)
          action.apply(p); 
        propagateCompletion();
      } 
    }
  }
  
  static final class ForEachMappingTask<K, V> extends BulkTask<K, V, Void> {
    final ConcurrentHashMapV8.BiAction<? super K, ? super V> action;
    
    ForEachMappingTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, ConcurrentHashMapV8.BiAction<? super K, ? super V> action) {
      super(p, b, i, f, t);
      this.action = action;
    }
    
    public final void compute() {
      ConcurrentHashMapV8.BiAction<? super K, ? super V> action;
      if ((action = this.action) != null) {
        int f;
        int h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (new ForEachMappingTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, action)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null)
          action.apply(p.key, p.val); 
        propagateCompletion();
      } 
    }
  }
  
  static final class ForEachTransformedKeyTask<K, V, U> extends BulkTask<K, V, Void> {
    final ConcurrentHashMapV8.Fun<? super K, ? extends U> transformer;
    
    final ConcurrentHashMapV8.Action<? super U> action;
    
    ForEachTransformedKeyTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, ConcurrentHashMapV8.Fun<? super K, ? extends U> transformer, ConcurrentHashMapV8.Action<? super U> action) {
      super(p, b, i, f, t);
      this.transformer = transformer;
      this.action = action;
    }
    
    public final void compute() {
      ConcurrentHashMapV8.Fun<? super K, ? extends U> transformer;
      ConcurrentHashMapV8.Action<? super U> action;
      if ((transformer = this.transformer) != null && (action = this.action) != null) {
        int f;
        int h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (new ForEachTransformedKeyTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, transformer, action)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null) {
          U u;
          if ((u = transformer.apply(p.key)) != null)
            action.apply(u); 
        } 
        propagateCompletion();
      } 
    }
  }
  
  static final class ForEachTransformedValueTask<K, V, U> extends BulkTask<K, V, Void> {
    final ConcurrentHashMapV8.Fun<? super V, ? extends U> transformer;
    
    final ConcurrentHashMapV8.Action<? super U> action;
    
    ForEachTransformedValueTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, ConcurrentHashMapV8.Fun<? super V, ? extends U> transformer, ConcurrentHashMapV8.Action<? super U> action) {
      super(p, b, i, f, t);
      this.transformer = transformer;
      this.action = action;
    }
    
    public final void compute() {
      ConcurrentHashMapV8.Fun<? super V, ? extends U> transformer;
      ConcurrentHashMapV8.Action<? super U> action;
      if ((transformer = this.transformer) != null && (action = this.action) != null) {
        int f;
        int h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (new ForEachTransformedValueTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, transformer, action)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null) {
          U u;
          if ((u = transformer.apply(p.val)) != null)
            action.apply(u); 
        } 
        propagateCompletion();
      } 
    }
  }
  
  static final class ForEachTransformedEntryTask<K, V, U> extends BulkTask<K, V, Void> {
    final ConcurrentHashMapV8.Fun<Map.Entry<K, V>, ? extends U> transformer;
    
    final ConcurrentHashMapV8.Action<? super U> action;
    
    ForEachTransformedEntryTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, ConcurrentHashMapV8.Fun<Map.Entry<K, V>, ? extends U> transformer, ConcurrentHashMapV8.Action<? super U> action) {
      super(p, b, i, f, t);
      this.transformer = transformer;
      this.action = action;
    }
    
    public final void compute() {
      ConcurrentHashMapV8.Fun<Map.Entry<K, V>, ? extends U> transformer;
      ConcurrentHashMapV8.Action<? super U> action;
      if ((transformer = this.transformer) != null && (action = this.action) != null) {
        int f;
        int h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (new ForEachTransformedEntryTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, transformer, action)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null) {
          U u;
          if ((u = transformer.apply(p)) != null)
            action.apply(u); 
        } 
        propagateCompletion();
      } 
    }
  }
  
  static final class ForEachTransformedMappingTask<K, V, U> extends BulkTask<K, V, Void> {
    final ConcurrentHashMapV8.BiFun<? super K, ? super V, ? extends U> transformer;
    
    final ConcurrentHashMapV8.Action<? super U> action;
    
    ForEachTransformedMappingTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, ConcurrentHashMapV8.BiFun<? super K, ? super V, ? extends U> transformer, ConcurrentHashMapV8.Action<? super U> action) {
      super(p, b, i, f, t);
      this.transformer = transformer;
      this.action = action;
    }
    
    public final void compute() {
      ConcurrentHashMapV8.BiFun<? super K, ? super V, ? extends U> transformer;
      ConcurrentHashMapV8.Action<? super U> action;
      if ((transformer = this.transformer) != null && (action = this.action) != null) {
        int f;
        int h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (new ForEachTransformedMappingTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, transformer, action)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null) {
          U u;
          if ((u = transformer.apply(p.key, p.val)) != null)
            action.apply(u); 
        } 
        propagateCompletion();
      } 
    }
  }
  
  static final class SearchKeysTask<K, V, U> extends BulkTask<K, V, U> {
    final ConcurrentHashMapV8.Fun<? super K, ? extends U> searchFunction;
    
    final AtomicReference<U> result;
    
    SearchKeysTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, ConcurrentHashMapV8.Fun<? super K, ? extends U> searchFunction, AtomicReference<U> result) {
      super(p, b, i, f, t);
      this.searchFunction = searchFunction;
      this.result = result;
    }
    
    public final U getRawResult() {
      return this.result.get();
    }
    
    public final void compute() {
      ConcurrentHashMapV8.Fun<? super K, ? extends U> searchFunction;
      AtomicReference<U> result;
      if ((searchFunction = this.searchFunction) != null && (result = this.result) != null) {
        int f;
        int h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          if (result.get() != null)
            return; 
          addToPendingCount(1);
          (new SearchKeysTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, searchFunction, result)).fork();
        } 
        while (result.get() == null) {
          ConcurrentHashMapV8.Node<K, V> p;
          if ((p = advance()) == null) {
            propagateCompletion();
            break;
          } 
          U u;
          if ((u = searchFunction.apply(p.key)) != null) {
            if (result.compareAndSet(null, u))
              quietlyCompleteRoot(); 
            break;
          } 
        } 
      } 
    }
  }
  
  static final class SearchValuesTask<K, V, U> extends BulkTask<K, V, U> {
    final ConcurrentHashMapV8.Fun<? super V, ? extends U> searchFunction;
    
    final AtomicReference<U> result;
    
    SearchValuesTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, ConcurrentHashMapV8.Fun<? super V, ? extends U> searchFunction, AtomicReference<U> result) {
      super(p, b, i, f, t);
      this.searchFunction = searchFunction;
      this.result = result;
    }
    
    public final U getRawResult() {
      return this.result.get();
    }
    
    public final void compute() {
      ConcurrentHashMapV8.Fun<? super V, ? extends U> searchFunction;
      AtomicReference<U> result;
      if ((searchFunction = this.searchFunction) != null && (result = this.result) != null) {
        int f;
        int h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          if (result.get() != null)
            return; 
          addToPendingCount(1);
          (new SearchValuesTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, searchFunction, result)).fork();
        } 
        while (result.get() == null) {
          ConcurrentHashMapV8.Node<K, V> p;
          if ((p = advance()) == null) {
            propagateCompletion();
            break;
          } 
          U u;
          if ((u = searchFunction.apply(p.val)) != null) {
            if (result.compareAndSet(null, u))
              quietlyCompleteRoot(); 
            break;
          } 
        } 
      } 
    }
  }
  
  static final class SearchEntriesTask<K, V, U> extends BulkTask<K, V, U> {
    final ConcurrentHashMapV8.Fun<Map.Entry<K, V>, ? extends U> searchFunction;
    
    final AtomicReference<U> result;
    
    SearchEntriesTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, ConcurrentHashMapV8.Fun<Map.Entry<K, V>, ? extends U> searchFunction, AtomicReference<U> result) {
      super(p, b, i, f, t);
      this.searchFunction = searchFunction;
      this.result = result;
    }
    
    public final U getRawResult() {
      return this.result.get();
    }
    
    public final void compute() {
      ConcurrentHashMapV8.Fun<Map.Entry<K, V>, ? extends U> searchFunction;
      AtomicReference<U> result;
      if ((searchFunction = this.searchFunction) != null && (result = this.result) != null) {
        int f;
        int h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          if (result.get() != null)
            return; 
          addToPendingCount(1);
          (new SearchEntriesTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, searchFunction, result)).fork();
        } 
        while (result.get() == null) {
          ConcurrentHashMapV8.Node<K, V> p;
          if ((p = advance()) == null) {
            propagateCompletion();
            break;
          } 
          U u;
          if ((u = searchFunction.apply(p)) != null) {
            if (result.compareAndSet(null, u))
              quietlyCompleteRoot(); 
            return;
          } 
        } 
      } 
    }
  }
  
  static final class SearchMappingsTask<K, V, U> extends BulkTask<K, V, U> {
    final ConcurrentHashMapV8.BiFun<? super K, ? super V, ? extends U> searchFunction;
    
    final AtomicReference<U> result;
    
    SearchMappingsTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, ConcurrentHashMapV8.BiFun<? super K, ? super V, ? extends U> searchFunction, AtomicReference<U> result) {
      super(p, b, i, f, t);
      this.searchFunction = searchFunction;
      this.result = result;
    }
    
    public final U getRawResult() {
      return this.result.get();
    }
    
    public final void compute() {
      ConcurrentHashMapV8.BiFun<? super K, ? super V, ? extends U> searchFunction;
      AtomicReference<U> result;
      if ((searchFunction = this.searchFunction) != null && (result = this.result) != null) {
        int f;
        int h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          if (result.get() != null)
            return; 
          addToPendingCount(1);
          (new SearchMappingsTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, searchFunction, result)).fork();
        } 
        while (result.get() == null) {
          ConcurrentHashMapV8.Node<K, V> p;
          if ((p = advance()) == null) {
            propagateCompletion();
            break;
          } 
          U u;
          if ((u = searchFunction.apply(p.key, p.val)) != null) {
            if (result.compareAndSet(null, u))
              quietlyCompleteRoot(); 
            break;
          } 
        } 
      } 
    }
  }
  
  static final class ReduceKeysTask<K, V> extends BulkTask<K, V, K> {
    final ConcurrentHashMapV8.BiFun<? super K, ? super K, ? extends K> reducer;
    
    K result;
    
    ReduceKeysTask<K, V> rights;
    
    ReduceKeysTask<K, V> nextRight;
    
    ReduceKeysTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, ReduceKeysTask<K, V> nextRight, ConcurrentHashMapV8.BiFun<? super K, ? super K, ? extends K> reducer) {
      super(p, b, i, f, t);
      this.nextRight = nextRight;
      this.reducer = reducer;
    }
    
    public final K getRawResult() {
      return this.result;
    }
    
    public final void compute() {
      ConcurrentHashMapV8.BiFun<? super K, ? super K, ? extends K> reducer;
      if ((reducer = this.reducer) != null) {
        int f, h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (this.rights = new ReduceKeysTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, this.rights, reducer)).fork();
        } 
        K r = null;
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null) {
          K u = p.key;
          r = (r == null) ? u : ((u == null) ? r : reducer.apply(r, u));
        } 
        this.result = r;
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
          ReduceKeysTask<K, V> t = (ReduceKeysTask)c;
          ReduceKeysTask<K, V> s = t.rights;
          while (s != null) {
            K sr;
            if ((sr = s.result) != null) {
              K tr;
              t.result = ((tr = t.result) == null) ? sr : reducer.apply(tr, sr);
            } 
            s = t.rights = s.nextRight;
          } 
        } 
      } 
    }
  }
  
  static final class ReduceValuesTask<K, V> extends BulkTask<K, V, V> {
    final ConcurrentHashMapV8.BiFun<? super V, ? super V, ? extends V> reducer;
    
    V result;
    
    ReduceValuesTask<K, V> rights;
    
    ReduceValuesTask<K, V> nextRight;
    
    ReduceValuesTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, ReduceValuesTask<K, V> nextRight, ConcurrentHashMapV8.BiFun<? super V, ? super V, ? extends V> reducer) {
      super(p, b, i, f, t);
      this.nextRight = nextRight;
      this.reducer = reducer;
    }
    
    public final V getRawResult() {
      return this.result;
    }
    
    public final void compute() {
      ConcurrentHashMapV8.BiFun<? super V, ? super V, ? extends V> reducer;
      if ((reducer = this.reducer) != null) {
        int f, h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (this.rights = new ReduceValuesTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, this.rights, reducer)).fork();
        } 
        V r = null;
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null) {
          V v = p.val;
          r = (r == null) ? v : reducer.apply(r, v);
        } 
        this.result = r;
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
          ReduceValuesTask<K, V> t = (ReduceValuesTask)c;
          ReduceValuesTask<K, V> s = t.rights;
          while (s != null) {
            V sr;
            if ((sr = s.result) != null) {
              V tr;
              t.result = ((tr = t.result) == null) ? sr : reducer.apply(tr, sr);
            } 
            s = t.rights = s.nextRight;
          } 
        } 
      } 
    }
  }
  
  static final class ReduceEntriesTask<K, V> extends BulkTask<K, V, Map.Entry<K, V>> {
    final ConcurrentHashMapV8.BiFun<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer;
    
    Map.Entry<K, V> result;
    
    ReduceEntriesTask<K, V> rights;
    
    ReduceEntriesTask<K, V> nextRight;
    
    ReduceEntriesTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, ReduceEntriesTask<K, V> nextRight, ConcurrentHashMapV8.BiFun<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer) {
      super(p, b, i, f, t);
      this.nextRight = nextRight;
      this.reducer = reducer;
    }
    
    public final Map.Entry<K, V> getRawResult() {
      return this.result;
    }
    
    public final void compute() {
      ConcurrentHashMapV8.BiFun<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer;
      if ((reducer = this.reducer) != null) {
        int f, h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (this.rights = new ReduceEntriesTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, this.rights, reducer)).fork();
        } 
        Map.Entry<K, V> r = null;
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null)
          r = (r == null) ? p : reducer.apply(r, p); 
        this.result = r;
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
          ReduceEntriesTask<K, V> t = (ReduceEntriesTask)c;
          ReduceEntriesTask<K, V> s = t.rights;
          while (s != null) {
            Map.Entry<K, V> sr;
            if ((sr = s.result) != null) {
              Map.Entry<K, V> tr;
              t.result = ((tr = t.result) == null) ? sr : reducer.apply(tr, sr);
            } 
            s = t.rights = s.nextRight;
          } 
        } 
      } 
    }
  }
  
  static final class MapReduceKeysTask<K, V, U> extends BulkTask<K, V, U> {
    final ConcurrentHashMapV8.Fun<? super K, ? extends U> transformer;
    
    final ConcurrentHashMapV8.BiFun<? super U, ? super U, ? extends U> reducer;
    
    U result;
    
    MapReduceKeysTask<K, V, U> rights;
    
    MapReduceKeysTask<K, V, U> nextRight;
    
    MapReduceKeysTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, MapReduceKeysTask<K, V, U> nextRight, ConcurrentHashMapV8.Fun<? super K, ? extends U> transformer, ConcurrentHashMapV8.BiFun<? super U, ? super U, ? extends U> reducer) {
      super(p, b, i, f, t);
      this.nextRight = nextRight;
      this.transformer = transformer;
      this.reducer = reducer;
    }
    
    public final U getRawResult() {
      return this.result;
    }
    
    public final void compute() {
      ConcurrentHashMapV8.Fun<? super K, ? extends U> transformer;
      ConcurrentHashMapV8.BiFun<? super U, ? super U, ? extends U> reducer;
      if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
        int f, h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (this.rights = new MapReduceKeysTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, this.rights, transformer, reducer)).fork();
        } 
        U r = null;
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null) {
          U u;
          if ((u = transformer.apply(p.key)) != null)
            r = (r == null) ? u : reducer.apply(r, u); 
        } 
        this.result = r;
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
          MapReduceKeysTask<K, V, U> t = (MapReduceKeysTask)c;
          MapReduceKeysTask<K, V, U> s = t.rights;
          while (s != null) {
            U sr;
            if ((sr = s.result) != null) {
              U tr;
              t.result = ((tr = t.result) == null) ? sr : reducer.apply(tr, sr);
            } 
            s = t.rights = s.nextRight;
          } 
        } 
      } 
    }
  }
  
  static final class MapReduceValuesTask<K, V, U> extends BulkTask<K, V, U> {
    final ConcurrentHashMapV8.Fun<? super V, ? extends U> transformer;
    
    final ConcurrentHashMapV8.BiFun<? super U, ? super U, ? extends U> reducer;
    
    U result;
    
    MapReduceValuesTask<K, V, U> rights;
    
    MapReduceValuesTask<K, V, U> nextRight;
    
    MapReduceValuesTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, MapReduceValuesTask<K, V, U> nextRight, ConcurrentHashMapV8.Fun<? super V, ? extends U> transformer, ConcurrentHashMapV8.BiFun<? super U, ? super U, ? extends U> reducer) {
      super(p, b, i, f, t);
      this.nextRight = nextRight;
      this.transformer = transformer;
      this.reducer = reducer;
    }
    
    public final U getRawResult() {
      return this.result;
    }
    
    public final void compute() {
      ConcurrentHashMapV8.Fun<? super V, ? extends U> transformer;
      ConcurrentHashMapV8.BiFun<? super U, ? super U, ? extends U> reducer;
      if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
        int f, h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (this.rights = new MapReduceValuesTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, this.rights, transformer, reducer)).fork();
        } 
        U r = null;
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null) {
          U u;
          if ((u = transformer.apply(p.val)) != null)
            r = (r == null) ? u : reducer.apply(r, u); 
        } 
        this.result = r;
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
          MapReduceValuesTask<K, V, U> t = (MapReduceValuesTask)c;
          MapReduceValuesTask<K, V, U> s = t.rights;
          while (s != null) {
            U sr;
            if ((sr = s.result) != null) {
              U tr;
              t.result = ((tr = t.result) == null) ? sr : reducer.apply(tr, sr);
            } 
            s = t.rights = s.nextRight;
          } 
        } 
      } 
    }
  }
  
  static final class MapReduceEntriesTask<K, V, U> extends BulkTask<K, V, U> {
    final ConcurrentHashMapV8.Fun<Map.Entry<K, V>, ? extends U> transformer;
    
    final ConcurrentHashMapV8.BiFun<? super U, ? super U, ? extends U> reducer;
    
    U result;
    
    MapReduceEntriesTask<K, V, U> rights;
    
    MapReduceEntriesTask<K, V, U> nextRight;
    
    MapReduceEntriesTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, MapReduceEntriesTask<K, V, U> nextRight, ConcurrentHashMapV8.Fun<Map.Entry<K, V>, ? extends U> transformer, ConcurrentHashMapV8.BiFun<? super U, ? super U, ? extends U> reducer) {
      super(p, b, i, f, t);
      this.nextRight = nextRight;
      this.transformer = transformer;
      this.reducer = reducer;
    }
    
    public final U getRawResult() {
      return this.result;
    }
    
    public final void compute() {
      ConcurrentHashMapV8.Fun<Map.Entry<K, V>, ? extends U> transformer;
      ConcurrentHashMapV8.BiFun<? super U, ? super U, ? extends U> reducer;
      if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
        int f, h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (this.rights = new MapReduceEntriesTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, this.rights, transformer, reducer)).fork();
        } 
        U r = null;
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null) {
          U u;
          if ((u = transformer.apply(p)) != null)
            r = (r == null) ? u : reducer.apply(r, u); 
        } 
        this.result = r;
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
          MapReduceEntriesTask<K, V, U> t = (MapReduceEntriesTask)c;
          MapReduceEntriesTask<K, V, U> s = t.rights;
          while (s != null) {
            U sr;
            if ((sr = s.result) != null) {
              U tr;
              t.result = ((tr = t.result) == null) ? sr : reducer.apply(tr, sr);
            } 
            s = t.rights = s.nextRight;
          } 
        } 
      } 
    }
  }
  
  static final class MapReduceMappingsTask<K, V, U> extends BulkTask<K, V, U> {
    final ConcurrentHashMapV8.BiFun<? super K, ? super V, ? extends U> transformer;
    
    final ConcurrentHashMapV8.BiFun<? super U, ? super U, ? extends U> reducer;
    
    U result;
    
    MapReduceMappingsTask<K, V, U> rights;
    
    MapReduceMappingsTask<K, V, U> nextRight;
    
    MapReduceMappingsTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, MapReduceMappingsTask<K, V, U> nextRight, ConcurrentHashMapV8.BiFun<? super K, ? super V, ? extends U> transformer, ConcurrentHashMapV8.BiFun<? super U, ? super U, ? extends U> reducer) {
      super(p, b, i, f, t);
      this.nextRight = nextRight;
      this.transformer = transformer;
      this.reducer = reducer;
    }
    
    public final U getRawResult() {
      return this.result;
    }
    
    public final void compute() {
      ConcurrentHashMapV8.BiFun<? super K, ? super V, ? extends U> transformer;
      ConcurrentHashMapV8.BiFun<? super U, ? super U, ? extends U> reducer;
      if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
        int f, h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (this.rights = new MapReduceMappingsTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, this.rights, transformer, reducer)).fork();
        } 
        U r = null;
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null) {
          U u;
          if ((u = transformer.apply(p.key, p.val)) != null)
            r = (r == null) ? u : reducer.apply(r, u); 
        } 
        this.result = r;
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
          MapReduceMappingsTask<K, V, U> t = (MapReduceMappingsTask)c;
          MapReduceMappingsTask<K, V, U> s = t.rights;
          while (s != null) {
            U sr;
            if ((sr = s.result) != null) {
              U tr;
              t.result = ((tr = t.result) == null) ? sr : reducer.apply(tr, sr);
            } 
            s = t.rights = s.nextRight;
          } 
        } 
      } 
    }
  }
  
  static final class MapReduceKeysToDoubleTask<K, V> extends BulkTask<K, V, Double> {
    final ConcurrentHashMapV8.ObjectToDouble<? super K> transformer;
    
    final ConcurrentHashMapV8.DoubleByDoubleToDouble reducer;
    
    final double basis;
    
    double result;
    
    MapReduceKeysToDoubleTask<K, V> rights;
    
    MapReduceKeysToDoubleTask<K, V> nextRight;
    
    MapReduceKeysToDoubleTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, MapReduceKeysToDoubleTask<K, V> nextRight, ConcurrentHashMapV8.ObjectToDouble<? super K> transformer, double basis, ConcurrentHashMapV8.DoubleByDoubleToDouble reducer) {
      super(p, b, i, f, t);
      this.nextRight = nextRight;
      this.transformer = transformer;
      this.basis = basis;
      this.reducer = reducer;
    }
    
    public final Double getRawResult() {
      return Double.valueOf(this.result);
    }
    
    public final void compute() {
      ConcurrentHashMapV8.ObjectToDouble<? super K> transformer;
      ConcurrentHashMapV8.DoubleByDoubleToDouble reducer;
      if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
        double r = this.basis;
        int f, h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (this.rights = new MapReduceKeysToDoubleTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, this.rights, transformer, r, reducer)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null)
          r = reducer.apply(r, transformer.apply(p.key)); 
        this.result = r;
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
          MapReduceKeysToDoubleTask<K, V> t = (MapReduceKeysToDoubleTask)c;
          MapReduceKeysToDoubleTask<K, V> s = t.rights;
          while (s != null) {
            t.result = reducer.apply(t.result, s.result);
            s = t.rights = s.nextRight;
          } 
        } 
      } 
    }
  }
  
  static final class MapReduceValuesToDoubleTask<K, V> extends BulkTask<K, V, Double> {
    final ConcurrentHashMapV8.ObjectToDouble<? super V> transformer;
    
    final ConcurrentHashMapV8.DoubleByDoubleToDouble reducer;
    
    final double basis;
    
    double result;
    
    MapReduceValuesToDoubleTask<K, V> rights;
    
    MapReduceValuesToDoubleTask<K, V> nextRight;
    
    MapReduceValuesToDoubleTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, MapReduceValuesToDoubleTask<K, V> nextRight, ConcurrentHashMapV8.ObjectToDouble<? super V> transformer, double basis, ConcurrentHashMapV8.DoubleByDoubleToDouble reducer) {
      super(p, b, i, f, t);
      this.nextRight = nextRight;
      this.transformer = transformer;
      this.basis = basis;
      this.reducer = reducer;
    }
    
    public final Double getRawResult() {
      return Double.valueOf(this.result);
    }
    
    public final void compute() {
      ConcurrentHashMapV8.ObjectToDouble<? super V> transformer;
      ConcurrentHashMapV8.DoubleByDoubleToDouble reducer;
      if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
        double r = this.basis;
        int f, h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (this.rights = new MapReduceValuesToDoubleTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, this.rights, transformer, r, reducer)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null)
          r = reducer.apply(r, transformer.apply(p.val)); 
        this.result = r;
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
          MapReduceValuesToDoubleTask<K, V> t = (MapReduceValuesToDoubleTask)c;
          MapReduceValuesToDoubleTask<K, V> s = t.rights;
          while (s != null) {
            t.result = reducer.apply(t.result, s.result);
            s = t.rights = s.nextRight;
          } 
        } 
      } 
    }
  }
  
  static final class MapReduceEntriesToDoubleTask<K, V> extends BulkTask<K, V, Double> {
    final ConcurrentHashMapV8.ObjectToDouble<Map.Entry<K, V>> transformer;
    
    final ConcurrentHashMapV8.DoubleByDoubleToDouble reducer;
    
    final double basis;
    
    double result;
    
    MapReduceEntriesToDoubleTask<K, V> rights;
    
    MapReduceEntriesToDoubleTask<K, V> nextRight;
    
    MapReduceEntriesToDoubleTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, MapReduceEntriesToDoubleTask<K, V> nextRight, ConcurrentHashMapV8.ObjectToDouble<Map.Entry<K, V>> transformer, double basis, ConcurrentHashMapV8.DoubleByDoubleToDouble reducer) {
      super(p, b, i, f, t);
      this.nextRight = nextRight;
      this.transformer = transformer;
      this.basis = basis;
      this.reducer = reducer;
    }
    
    public final Double getRawResult() {
      return Double.valueOf(this.result);
    }
    
    public final void compute() {
      ConcurrentHashMapV8.ObjectToDouble<Map.Entry<K, V>> transformer;
      ConcurrentHashMapV8.DoubleByDoubleToDouble reducer;
      if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
        double r = this.basis;
        int f, h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (this.rights = new MapReduceEntriesToDoubleTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, this.rights, transformer, r, reducer)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null)
          r = reducer.apply(r, transformer.apply(p)); 
        this.result = r;
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
          MapReduceEntriesToDoubleTask<K, V> t = (MapReduceEntriesToDoubleTask)c;
          MapReduceEntriesToDoubleTask<K, V> s = t.rights;
          while (s != null) {
            t.result = reducer.apply(t.result, s.result);
            s = t.rights = s.nextRight;
          } 
        } 
      } 
    }
  }
  
  static final class MapReduceMappingsToDoubleTask<K, V> extends BulkTask<K, V, Double> {
    final ConcurrentHashMapV8.ObjectByObjectToDouble<? super K, ? super V> transformer;
    
    final ConcurrentHashMapV8.DoubleByDoubleToDouble reducer;
    
    final double basis;
    
    double result;
    
    MapReduceMappingsToDoubleTask<K, V> rights;
    
    MapReduceMappingsToDoubleTask<K, V> nextRight;
    
    MapReduceMappingsToDoubleTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, MapReduceMappingsToDoubleTask<K, V> nextRight, ConcurrentHashMapV8.ObjectByObjectToDouble<? super K, ? super V> transformer, double basis, ConcurrentHashMapV8.DoubleByDoubleToDouble reducer) {
      super(p, b, i, f, t);
      this.nextRight = nextRight;
      this.transformer = transformer;
      this.basis = basis;
      this.reducer = reducer;
    }
    
    public final Double getRawResult() {
      return Double.valueOf(this.result);
    }
    
    public final void compute() {
      ConcurrentHashMapV8.ObjectByObjectToDouble<? super K, ? super V> transformer;
      ConcurrentHashMapV8.DoubleByDoubleToDouble reducer;
      if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
        double r = this.basis;
        int f, h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (this.rights = new MapReduceMappingsToDoubleTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, this.rights, transformer, r, reducer)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null)
          r = reducer.apply(r, transformer.apply(p.key, p.val)); 
        this.result = r;
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
          MapReduceMappingsToDoubleTask<K, V> t = (MapReduceMappingsToDoubleTask)c;
          MapReduceMappingsToDoubleTask<K, V> s = t.rights;
          while (s != null) {
            t.result = reducer.apply(t.result, s.result);
            s = t.rights = s.nextRight;
          } 
        } 
      } 
    }
  }
  
  static final class MapReduceKeysToLongTask<K, V> extends BulkTask<K, V, Long> {
    final ConcurrentHashMapV8.ObjectToLong<? super K> transformer;
    
    final ConcurrentHashMapV8.LongByLongToLong reducer;
    
    final long basis;
    
    long result;
    
    MapReduceKeysToLongTask<K, V> rights;
    
    MapReduceKeysToLongTask<K, V> nextRight;
    
    MapReduceKeysToLongTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, MapReduceKeysToLongTask<K, V> nextRight, ConcurrentHashMapV8.ObjectToLong<? super K> transformer, long basis, ConcurrentHashMapV8.LongByLongToLong reducer) {
      super(p, b, i, f, t);
      this.nextRight = nextRight;
      this.transformer = transformer;
      this.basis = basis;
      this.reducer = reducer;
    }
    
    public final Long getRawResult() {
      return Long.valueOf(this.result);
    }
    
    public final void compute() {
      ConcurrentHashMapV8.ObjectToLong<? super K> transformer;
      ConcurrentHashMapV8.LongByLongToLong reducer;
      if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
        long r = this.basis;
        int f, h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (this.rights = new MapReduceKeysToLongTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, this.rights, transformer, r, reducer)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null)
          r = reducer.apply(r, transformer.apply(p.key)); 
        this.result = r;
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
          MapReduceKeysToLongTask<K, V> t = (MapReduceKeysToLongTask)c;
          MapReduceKeysToLongTask<K, V> s = t.rights;
          while (s != null) {
            t.result = reducer.apply(t.result, s.result);
            s = t.rights = s.nextRight;
          } 
        } 
      } 
    }
  }
  
  static final class MapReduceValuesToLongTask<K, V> extends BulkTask<K, V, Long> {
    final ConcurrentHashMapV8.ObjectToLong<? super V> transformer;
    
    final ConcurrentHashMapV8.LongByLongToLong reducer;
    
    final long basis;
    
    long result;
    
    MapReduceValuesToLongTask<K, V> rights;
    
    MapReduceValuesToLongTask<K, V> nextRight;
    
    MapReduceValuesToLongTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, MapReduceValuesToLongTask<K, V> nextRight, ConcurrentHashMapV8.ObjectToLong<? super V> transformer, long basis, ConcurrentHashMapV8.LongByLongToLong reducer) {
      super(p, b, i, f, t);
      this.nextRight = nextRight;
      this.transformer = transformer;
      this.basis = basis;
      this.reducer = reducer;
    }
    
    public final Long getRawResult() {
      return Long.valueOf(this.result);
    }
    
    public final void compute() {
      ConcurrentHashMapV8.ObjectToLong<? super V> transformer;
      ConcurrentHashMapV8.LongByLongToLong reducer;
      if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
        long r = this.basis;
        int f, h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (this.rights = new MapReduceValuesToLongTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, this.rights, transformer, r, reducer)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null)
          r = reducer.apply(r, transformer.apply(p.val)); 
        this.result = r;
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
          MapReduceValuesToLongTask<K, V> t = (MapReduceValuesToLongTask)c;
          MapReduceValuesToLongTask<K, V> s = t.rights;
          while (s != null) {
            t.result = reducer.apply(t.result, s.result);
            s = t.rights = s.nextRight;
          } 
        } 
      } 
    }
  }
  
  static final class MapReduceEntriesToLongTask<K, V> extends BulkTask<K, V, Long> {
    final ConcurrentHashMapV8.ObjectToLong<Map.Entry<K, V>> transformer;
    
    final ConcurrentHashMapV8.LongByLongToLong reducer;
    
    final long basis;
    
    long result;
    
    MapReduceEntriesToLongTask<K, V> rights;
    
    MapReduceEntriesToLongTask<K, V> nextRight;
    
    MapReduceEntriesToLongTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, MapReduceEntriesToLongTask<K, V> nextRight, ConcurrentHashMapV8.ObjectToLong<Map.Entry<K, V>> transformer, long basis, ConcurrentHashMapV8.LongByLongToLong reducer) {
      super(p, b, i, f, t);
      this.nextRight = nextRight;
      this.transformer = transformer;
      this.basis = basis;
      this.reducer = reducer;
    }
    
    public final Long getRawResult() {
      return Long.valueOf(this.result);
    }
    
    public final void compute() {
      ConcurrentHashMapV8.ObjectToLong<Map.Entry<K, V>> transformer;
      ConcurrentHashMapV8.LongByLongToLong reducer;
      if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
        long r = this.basis;
        int f, h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (this.rights = new MapReduceEntriesToLongTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, this.rights, transformer, r, reducer)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null)
          r = reducer.apply(r, transformer.apply(p)); 
        this.result = r;
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
          MapReduceEntriesToLongTask<K, V> t = (MapReduceEntriesToLongTask)c;
          MapReduceEntriesToLongTask<K, V> s = t.rights;
          while (s != null) {
            t.result = reducer.apply(t.result, s.result);
            s = t.rights = s.nextRight;
          } 
        } 
      } 
    }
  }
  
  static final class MapReduceMappingsToLongTask<K, V> extends BulkTask<K, V, Long> {
    final ConcurrentHashMapV8.ObjectByObjectToLong<? super K, ? super V> transformer;
    
    final ConcurrentHashMapV8.LongByLongToLong reducer;
    
    final long basis;
    
    long result;
    
    MapReduceMappingsToLongTask<K, V> rights;
    
    MapReduceMappingsToLongTask<K, V> nextRight;
    
    MapReduceMappingsToLongTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, MapReduceMappingsToLongTask<K, V> nextRight, ConcurrentHashMapV8.ObjectByObjectToLong<? super K, ? super V> transformer, long basis, ConcurrentHashMapV8.LongByLongToLong reducer) {
      super(p, b, i, f, t);
      this.nextRight = nextRight;
      this.transformer = transformer;
      this.basis = basis;
      this.reducer = reducer;
    }
    
    public final Long getRawResult() {
      return Long.valueOf(this.result);
    }
    
    public final void compute() {
      ConcurrentHashMapV8.ObjectByObjectToLong<? super K, ? super V> transformer;
      ConcurrentHashMapV8.LongByLongToLong reducer;
      if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
        long r = this.basis;
        int f, h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (this.rights = new MapReduceMappingsToLongTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, this.rights, transformer, r, reducer)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null)
          r = reducer.apply(r, transformer.apply(p.key, p.val)); 
        this.result = r;
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
          MapReduceMappingsToLongTask<K, V> t = (MapReduceMappingsToLongTask)c;
          MapReduceMappingsToLongTask<K, V> s = t.rights;
          while (s != null) {
            t.result = reducer.apply(t.result, s.result);
            s = t.rights = s.nextRight;
          } 
        } 
      } 
    }
  }
  
  static final class MapReduceKeysToIntTask<K, V> extends BulkTask<K, V, Integer> {
    final ConcurrentHashMapV8.ObjectToInt<? super K> transformer;
    
    final ConcurrentHashMapV8.IntByIntToInt reducer;
    
    final int basis;
    
    int result;
    
    MapReduceKeysToIntTask<K, V> rights;
    
    MapReduceKeysToIntTask<K, V> nextRight;
    
    MapReduceKeysToIntTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, MapReduceKeysToIntTask<K, V> nextRight, ConcurrentHashMapV8.ObjectToInt<? super K> transformer, int basis, ConcurrentHashMapV8.IntByIntToInt reducer) {
      super(p, b, i, f, t);
      this.nextRight = nextRight;
      this.transformer = transformer;
      this.basis = basis;
      this.reducer = reducer;
    }
    
    public final Integer getRawResult() {
      return Integer.valueOf(this.result);
    }
    
    public final void compute() {
      ConcurrentHashMapV8.ObjectToInt<? super K> transformer;
      ConcurrentHashMapV8.IntByIntToInt reducer;
      if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
        int r = this.basis;
        int f, h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (this.rights = new MapReduceKeysToIntTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, this.rights, transformer, r, reducer)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null)
          r = reducer.apply(r, transformer.apply(p.key)); 
        this.result = r;
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
          MapReduceKeysToIntTask<K, V> t = (MapReduceKeysToIntTask)c;
          MapReduceKeysToIntTask<K, V> s = t.rights;
          while (s != null) {
            t.result = reducer.apply(t.result, s.result);
            s = t.rights = s.nextRight;
          } 
        } 
      } 
    }
  }
  
  static final class MapReduceValuesToIntTask<K, V> extends BulkTask<K, V, Integer> {
    final ConcurrentHashMapV8.ObjectToInt<? super V> transformer;
    
    final ConcurrentHashMapV8.IntByIntToInt reducer;
    
    final int basis;
    
    int result;
    
    MapReduceValuesToIntTask<K, V> rights;
    
    MapReduceValuesToIntTask<K, V> nextRight;
    
    MapReduceValuesToIntTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, MapReduceValuesToIntTask<K, V> nextRight, ConcurrentHashMapV8.ObjectToInt<? super V> transformer, int basis, ConcurrentHashMapV8.IntByIntToInt reducer) {
      super(p, b, i, f, t);
      this.nextRight = nextRight;
      this.transformer = transformer;
      this.basis = basis;
      this.reducer = reducer;
    }
    
    public final Integer getRawResult() {
      return Integer.valueOf(this.result);
    }
    
    public final void compute() {
      ConcurrentHashMapV8.ObjectToInt<? super V> transformer;
      ConcurrentHashMapV8.IntByIntToInt reducer;
      if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
        int r = this.basis;
        int f, h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (this.rights = new MapReduceValuesToIntTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, this.rights, transformer, r, reducer)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null)
          r = reducer.apply(r, transformer.apply(p.val)); 
        this.result = r;
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
          MapReduceValuesToIntTask<K, V> t = (MapReduceValuesToIntTask)c;
          MapReduceValuesToIntTask<K, V> s = t.rights;
          while (s != null) {
            t.result = reducer.apply(t.result, s.result);
            s = t.rights = s.nextRight;
          } 
        } 
      } 
    }
  }
  
  static final class MapReduceEntriesToIntTask<K, V> extends BulkTask<K, V, Integer> {
    final ConcurrentHashMapV8.ObjectToInt<Map.Entry<K, V>> transformer;
    
    final ConcurrentHashMapV8.IntByIntToInt reducer;
    
    final int basis;
    
    int result;
    
    MapReduceEntriesToIntTask<K, V> rights;
    
    MapReduceEntriesToIntTask<K, V> nextRight;
    
    MapReduceEntriesToIntTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, MapReduceEntriesToIntTask<K, V> nextRight, ConcurrentHashMapV8.ObjectToInt<Map.Entry<K, V>> transformer, int basis, ConcurrentHashMapV8.IntByIntToInt reducer) {
      super(p, b, i, f, t);
      this.nextRight = nextRight;
      this.transformer = transformer;
      this.basis = basis;
      this.reducer = reducer;
    }
    
    public final Integer getRawResult() {
      return Integer.valueOf(this.result);
    }
    
    public final void compute() {
      ConcurrentHashMapV8.ObjectToInt<Map.Entry<K, V>> transformer;
      ConcurrentHashMapV8.IntByIntToInt reducer;
      if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
        int r = this.basis;
        int f, h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (this.rights = new MapReduceEntriesToIntTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, this.rights, transformer, r, reducer)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null)
          r = reducer.apply(r, transformer.apply(p)); 
        this.result = r;
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
          MapReduceEntriesToIntTask<K, V> t = (MapReduceEntriesToIntTask)c;
          MapReduceEntriesToIntTask<K, V> s = t.rights;
          while (s != null) {
            t.result = reducer.apply(t.result, s.result);
            s = t.rights = s.nextRight;
          } 
        } 
      } 
    }
  }
  
  static final class MapReduceMappingsToIntTask<K, V> extends BulkTask<K, V, Integer> {
    final ConcurrentHashMapV8.ObjectByObjectToInt<? super K, ? super V> transformer;
    
    final ConcurrentHashMapV8.IntByIntToInt reducer;
    
    final int basis;
    
    int result;
    
    MapReduceMappingsToIntTask<K, V> rights;
    
    MapReduceMappingsToIntTask<K, V> nextRight;
    
    MapReduceMappingsToIntTask(ConcurrentHashMapV8.BulkTask<K, V, ?> p, int b, int i, int f, ConcurrentHashMapV8.Node<K, V>[] t, MapReduceMappingsToIntTask<K, V> nextRight, ConcurrentHashMapV8.ObjectByObjectToInt<? super K, ? super V> transformer, int basis, ConcurrentHashMapV8.IntByIntToInt reducer) {
      super(p, b, i, f, t);
      this.nextRight = nextRight;
      this.transformer = transformer;
      this.basis = basis;
      this.reducer = reducer;
    }
    
    public final Integer getRawResult() {
      return Integer.valueOf(this.result);
    }
    
    public final void compute() {
      ConcurrentHashMapV8.ObjectByObjectToInt<? super K, ? super V> transformer;
      ConcurrentHashMapV8.IntByIntToInt reducer;
      if ((transformer = this.transformer) != null && (reducer = this.reducer) != null) {
        int r = this.basis;
        int f, h;
        for (int i = this.baseIndex; this.batch > 0 && (h = (f = this.baseLimit) + i >>> 1) > i; ) {
          addToPendingCount(1);
          (this.rights = new MapReduceMappingsToIntTask(this, this.batch >>>= 1, this.baseLimit = h, f, this.tab, this.rights, transformer, r, reducer)).fork();
        } 
        ConcurrentHashMapV8.Node<K, V> p;
        while ((p = advance()) != null)
          r = reducer.apply(r, transformer.apply(p.key, p.val)); 
        this.result = r;
        for (CountedCompleter<?> c = firstComplete(); c != null; c = c.nextComplete()) {
          MapReduceMappingsToIntTask<K, V> t = (MapReduceMappingsToIntTask)c;
          MapReduceMappingsToIntTask<K, V> s = t.rights;
          while (s != null) {
            t.result = reducer.apply(t.result, s.result);
            s = t.rights = s.nextRight;
          } 
        } 
      } 
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
    
    CounterCell(long x) {
      this.value = x;
    }
  }
  
  static final class CounterHashCode {
    int code;
  }
  
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
  
  final long sumCount() {
    CounterCell[] as = this.counterCells;
    long sum = this.baseCount;
    if (as != null)
      for (int i = 0; i < as.length; i++) {
        CounterCell a;
        if ((a = as[i]) != null)
          sum += a.value; 
      }  
    return sum;
  }
  
  private final void fullAddCount(InternalThreadLocalMap threadLocals, long x, IntegerHolder hc, boolean wasUncontended) {
    int h;
    if (hc == null) {
      hc = new IntegerHolder();
      int s = counterHashCodeGenerator.addAndGet(1640531527);
      h = hc.value = (s == 0) ? 1 : s;
      threadLocals.setCounterHashCode(hc);
    } else {
      h = hc.value;
    } 
    boolean collide = false;
    while (true) {
      CounterCell[] as;
      int n;
      if ((as = this.counterCells) != null && (n = as.length) > 0) {
        CounterCell a;
        if ((a = as[n - 1 & h]) == null) {
          if (this.cellsBusy == 0) {
            CounterCell r = new CounterCell(x);
            if (this.cellsBusy == 0 && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
              boolean created = false;
              try {
                CounterCell[] rs;
                int m;
                int j;
                if ((rs = this.counterCells) != null && (m = rs.length) > 0 && rs[j = m - 1 & h] == null) {
                  rs[j] = r;
                  created = true;
                } 
              } finally {
                this.cellsBusy = 0;
              } 
              if (created)
                break; 
              continue;
            } 
          } 
          collide = false;
        } else if (!wasUncontended) {
          wasUncontended = true;
        } else {
          long l;
          if (U.compareAndSwapLong(a, CELLVALUE, l = a.value, l + x))
            break; 
          if (this.counterCells != as || n >= NCPU) {
            collide = false;
          } else if (!collide) {
            collide = true;
          } else if (this.cellsBusy == 0 && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
            try {
              if (this.counterCells == as) {
                CounterCell[] rs = new CounterCell[n << 1];
                for (int i = 0; i < n; i++)
                  rs[i] = as[i]; 
                this.counterCells = rs;
              } 
            } finally {
              this.cellsBusy = 0;
            } 
            collide = false;
            continue;
          } 
        } 
        h ^= h << 13;
        h ^= h >>> 17;
        h ^= h << 5;
        continue;
      } 
      if (this.cellsBusy == 0 && this.counterCells == as && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
        boolean init = false;
        try {
          if (this.counterCells == as) {
            CounterCell[] rs = new CounterCell[2];
            rs[h & 0x1] = new CounterCell(x);
            this.counterCells = rs;
            init = true;
          } 
        } finally {
          this.cellsBusy = 0;
        } 
        if (init)
          break; 
        continue;
      } 
      long v;
      if (U.compareAndSwapLong(this, BASECOUNT, v = this.baseCount, v + x))
        break; 
    } 
    hc.value = h;
  }
  
  static {
    try {
      U = getUnsafe();
      Class<?> k = ConcurrentHashMapV8.class;
      SIZECTL = U.objectFieldOffset(k.getDeclaredField("sizeCtl"));
      TRANSFERINDEX = U.objectFieldOffset(k.getDeclaredField("transferIndex"));
      TRANSFERORIGIN = U.objectFieldOffset(k.getDeclaredField("transferOrigin"));
      BASECOUNT = U.objectFieldOffset(k.getDeclaredField("baseCount"));
      CELLSBUSY = U.objectFieldOffset(k.getDeclaredField("cellsBusy"));
      Class<?> ck = CounterCell.class;
      CELLVALUE = U.objectFieldOffset(ck.getDeclaredField("value"));
      Class<?> ak = Node[].class;
      ABASE = U.arrayBaseOffset(ak);
      int scale = U.arrayIndexScale(ak);
      if ((scale & scale - 1) != 0)
        throw new Error("data type scale not a power of two"); 
      ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
    } catch (Exception e) {
      throw new Error(e);
    } 
  }
  
  private static Unsafe getUnsafe() {
    try {
      return Unsafe.getUnsafe();
    } catch (SecurityException tryReflectionInstead) {
      try {
        return AccessController.<Unsafe>doPrivileged(new PrivilegedExceptionAction<Unsafe>() {
              public Unsafe run() throws Exception {
                Class<Unsafe> k = Unsafe.class;
                for (Field f : k.getDeclaredFields()) {
                  f.setAccessible(true);
                  Object x = f.get(null);
                  if (k.isInstance(x))
                    return k.cast(x); 
                } 
                throw new NoSuchFieldError("the Unsafe");
              }
            });
      } catch (PrivilegedActionException e) {
        throw new RuntimeException("Could not initialize intrinsics", e.getCause());
      } 
    } 
  }
  
  public static interface IntByIntToInt {
    int apply(int param1Int1, int param1Int2);
  }
  
  public static interface LongByLongToLong {
    long apply(long param1Long1, long param1Long2);
  }
  
  public static interface DoubleByDoubleToDouble {
    double apply(double param1Double1, double param1Double2);
  }
  
  public static interface ObjectByObjectToInt<A, B> {
    int apply(A param1A, B param1B);
  }
  
  public static interface ObjectByObjectToLong<A, B> {
    long apply(A param1A, B param1B);
  }
  
  public static interface ObjectByObjectToDouble<A, B> {
    double apply(A param1A, B param1B);
  }
  
  public static interface ObjectToInt<A> {
    int apply(A param1A);
  }
  
  public static interface ObjectToLong<A> {
    long apply(A param1A);
  }
  
  public static interface ObjectToDouble<A> {
    double apply(A param1A);
  }
  
  public static interface BiFun<A, B, T> {
    T apply(A param1A, B param1B);
  }
  
  public static interface Fun<A, T> {
    T apply(A param1A);
  }
  
  public static interface BiAction<A, B> {
    void apply(A param1A, B param1B);
  }
  
  public static interface Action<A> {
    void apply(A param1A);
  }
  
  public static interface ConcurrentHashMapSpliterator<T> {
    ConcurrentHashMapSpliterator<T> trySplit();
    
    long estimateSize();
    
    void forEachRemaining(ConcurrentHashMapV8.Action<? super T> param1Action);
    
    boolean tryAdvance(ConcurrentHashMapV8.Action<? super T> param1Action);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\chmv8\ConcurrentHashMapV8.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */