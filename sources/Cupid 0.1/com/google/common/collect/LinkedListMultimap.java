package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
public class LinkedListMultimap<K, V> extends AbstractMultimap<K, V> implements ListMultimap<K, V>, Serializable {
  private transient Node<K, V> head;
  
  private transient Node<K, V> tail;
  
  private transient Map<K, KeyList<K, V>> keyToKeyList;
  
  private transient int size;
  
  private transient int modCount;
  
  @GwtIncompatible("java serialization not supported")
  private static final long serialVersionUID = 0L;
  
  private static final class Node<K, V> extends AbstractMapEntry<K, V> {
    final K key;
    
    V value;
    
    Node<K, V> next;
    
    Node<K, V> previous;
    
    Node<K, V> nextSibling;
    
    Node<K, V> previousSibling;
    
    Node(@Nullable K key, @Nullable V value) {
      this.key = key;
      this.value = value;
    }
    
    public K getKey() {
      return this.key;
    }
    
    public V getValue() {
      return this.value;
    }
    
    public V setValue(@Nullable V newValue) {
      V result = this.value;
      this.value = newValue;
      return result;
    }
  }
  
  private static class KeyList<K, V> {
    LinkedListMultimap.Node<K, V> head;
    
    LinkedListMultimap.Node<K, V> tail;
    
    int count;
    
    KeyList(LinkedListMultimap.Node<K, V> firstNode) {
      this.head = firstNode;
      this.tail = firstNode;
      firstNode.previousSibling = null;
      firstNode.nextSibling = null;
      this.count = 1;
    }
  }
  
  public static <K, V> LinkedListMultimap<K, V> create() {
    return new LinkedListMultimap<K, V>();
  }
  
  public static <K, V> LinkedListMultimap<K, V> create(int expectedKeys) {
    return new LinkedListMultimap<K, V>(expectedKeys);
  }
  
  public static <K, V> LinkedListMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
    return new LinkedListMultimap<K, V>(multimap);
  }
  
  LinkedListMultimap() {
    this.keyToKeyList = Maps.newHashMap();
  }
  
  private LinkedListMultimap(int expectedKeys) {
    this.keyToKeyList = new HashMap<K, KeyList<K, V>>(expectedKeys);
  }
  
  private LinkedListMultimap(Multimap<? extends K, ? extends V> multimap) {
    this(multimap.keySet().size());
    putAll(multimap);
  }
  
  private Node<K, V> addNode(@Nullable K key, @Nullable V value, @Nullable Node<K, V> nextSibling) {
    Node<K, V> node = new Node<K, V>(key, value);
    if (this.head == null) {
      this.head = this.tail = node;
      this.keyToKeyList.put(key, new KeyList<K, V>(node));
      this.modCount++;
    } else if (nextSibling == null) {
      this.tail.next = node;
      node.previous = this.tail;
      this.tail = node;
      KeyList<K, V> keyList = this.keyToKeyList.get(key);
      if (keyList == null) {
        this.keyToKeyList.put(key, keyList = new KeyList<K, V>(node));
        this.modCount++;
      } else {
        keyList.count++;
        Node<K, V> keyTail = keyList.tail;
        keyTail.nextSibling = node;
        node.previousSibling = keyTail;
        keyList.tail = node;
      } 
    } else {
      KeyList<K, V> keyList = this.keyToKeyList.get(key);
      keyList.count++;
      node.previous = nextSibling.previous;
      node.previousSibling = nextSibling.previousSibling;
      node.next = nextSibling;
      node.nextSibling = nextSibling;
      if (nextSibling.previousSibling == null) {
        ((KeyList)this.keyToKeyList.get(key)).head = node;
      } else {
        nextSibling.previousSibling.nextSibling = node;
      } 
      if (nextSibling.previous == null) {
        this.head = node;
      } else {
        nextSibling.previous.next = node;
      } 
      nextSibling.previous = node;
      nextSibling.previousSibling = node;
    } 
    this.size++;
    return node;
  }
  
  private void removeNode(Node<K, V> node) {
    if (node.previous != null) {
      node.previous.next = node.next;
    } else {
      this.head = node.next;
    } 
    if (node.next != null) {
      node.next.previous = node.previous;
    } else {
      this.tail = node.previous;
    } 
    if (node.previousSibling == null && node.nextSibling == null) {
      KeyList<K, V> keyList = this.keyToKeyList.remove(node.key);
      keyList.count = 0;
      this.modCount++;
    } else {
      KeyList<K, V> keyList = this.keyToKeyList.get(node.key);
      keyList.count--;
      if (node.previousSibling == null) {
        keyList.head = node.nextSibling;
      } else {
        node.previousSibling.nextSibling = node.nextSibling;
      } 
      if (node.nextSibling == null) {
        keyList.tail = node.previousSibling;
      } else {
        node.nextSibling.previousSibling = node.previousSibling;
      } 
    } 
    this.size--;
  }
  
  private void removeAllNodes(@Nullable Object key) {
    Iterators.clear(new ValueForKeyIterator(key));
  }
  
  private static void checkElement(@Nullable Object node) {
    if (node == null)
      throw new NoSuchElementException(); 
  }
  
  private class NodeIterator implements ListIterator<Map.Entry<K, V>> {
    int nextIndex;
    
    LinkedListMultimap.Node<K, V> next;
    
    LinkedListMultimap.Node<K, V> current;
    
    LinkedListMultimap.Node<K, V> previous;
    
    int expectedModCount = LinkedListMultimap.this.modCount;
    
    private void checkForConcurrentModification() {
      if (LinkedListMultimap.this.modCount != this.expectedModCount)
        throw new ConcurrentModificationException(); 
    }
    
    public boolean hasNext() {
      checkForConcurrentModification();
      return (this.next != null);
    }
    
    public LinkedListMultimap.Node<K, V> next() {
      checkForConcurrentModification();
      LinkedListMultimap.checkElement(this.next);
      this.previous = this.current = this.next;
      this.next = this.next.next;
      this.nextIndex++;
      return this.current;
    }
    
    public void remove() {
      checkForConcurrentModification();
      CollectPreconditions.checkRemove((this.current != null));
      if (this.current != this.next) {
        this.previous = this.current.previous;
        this.nextIndex--;
      } else {
        this.next = this.current.next;
      } 
      LinkedListMultimap.this.removeNode(this.current);
      this.current = null;
      this.expectedModCount = LinkedListMultimap.this.modCount;
    }
    
    public boolean hasPrevious() {
      checkForConcurrentModification();
      return (this.previous != null);
    }
    
    public LinkedListMultimap.Node<K, V> previous() {
      checkForConcurrentModification();
      LinkedListMultimap.checkElement(this.previous);
      this.next = this.current = this.previous;
      this.previous = this.previous.previous;
      this.nextIndex--;
      return this.current;
    }
    
    public int nextIndex() {
      return this.nextIndex;
    }
    
    public int previousIndex() {
      return this.nextIndex - 1;
    }
    
    public void set(Map.Entry<K, V> e) {
      throw new UnsupportedOperationException();
    }
    
    public void add(Map.Entry<K, V> e) {
      throw new UnsupportedOperationException();
    }
    
    void setValue(V value) {
      Preconditions.checkState((this.current != null));
      this.current.value = value;
    }
    
    NodeIterator(int index) {
      int size = LinkedListMultimap.this.size();
      Preconditions.checkPositionIndex(index, size);
      if (index >= size / 2) {
        this.previous = LinkedListMultimap.this.tail;
        this.nextIndex = size;
        while (index++ < size)
          previous(); 
      } else {
        this.next = LinkedListMultimap.this.head;
        while (index-- > 0)
          next(); 
      } 
      this.current = null;
    }
  }
  
  private class DistinctKeyIterator implements Iterator<K> {
    final Set<K> seenKeys = Sets.newHashSetWithExpectedSize(LinkedListMultimap.this.keySet().size());
    
    LinkedListMultimap.Node<K, V> next = LinkedListMultimap.this.head;
    
    LinkedListMultimap.Node<K, V> current;
    
    int expectedModCount = LinkedListMultimap.this.modCount;
    
    private void checkForConcurrentModification() {
      if (LinkedListMultimap.this.modCount != this.expectedModCount)
        throw new ConcurrentModificationException(); 
    }
    
    public boolean hasNext() {
      checkForConcurrentModification();
      return (this.next != null);
    }
    
    public K next() {
      checkForConcurrentModification();
      LinkedListMultimap.checkElement(this.next);
      this.current = this.next;
      this.seenKeys.add(this.current.key);
      do {
        this.next = this.next.next;
      } while (this.next != null && !this.seenKeys.add(this.next.key));
      return this.current.key;
    }
    
    public void remove() {
      checkForConcurrentModification();
      CollectPreconditions.checkRemove((this.current != null));
      LinkedListMultimap.this.removeAllNodes(this.current.key);
      this.current = null;
      this.expectedModCount = LinkedListMultimap.this.modCount;
    }
    
    private DistinctKeyIterator() {}
  }
  
  private class ValueForKeyIterator implements ListIterator<V> {
    final Object key;
    
    int nextIndex;
    
    LinkedListMultimap.Node<K, V> next;
    
    LinkedListMultimap.Node<K, V> current;
    
    LinkedListMultimap.Node<K, V> previous;
    
    ValueForKeyIterator(Object key) {
      this.key = key;
      LinkedListMultimap.KeyList<K, V> keyList = (LinkedListMultimap.KeyList<K, V>)LinkedListMultimap.this.keyToKeyList.get(key);
      this.next = (keyList == null) ? null : keyList.head;
    }
    
    public ValueForKeyIterator(Object key, int index) {
      LinkedListMultimap.KeyList<K, V> keyList = (LinkedListMultimap.KeyList<K, V>)LinkedListMultimap.this.keyToKeyList.get(key);
      int size = (keyList == null) ? 0 : keyList.count;
      Preconditions.checkPositionIndex(index, size);
      if (index >= size / 2) {
        this.previous = (keyList == null) ? null : keyList.tail;
        this.nextIndex = size;
        while (index++ < size)
          previous(); 
      } else {
        this.next = (keyList == null) ? null : keyList.head;
        while (index-- > 0)
          next(); 
      } 
      this.key = key;
      this.current = null;
    }
    
    public boolean hasNext() {
      return (this.next != null);
    }
    
    public V next() {
      LinkedListMultimap.checkElement(this.next);
      this.previous = this.current = this.next;
      this.next = this.next.nextSibling;
      this.nextIndex++;
      return this.current.value;
    }
    
    public boolean hasPrevious() {
      return (this.previous != null);
    }
    
    public V previous() {
      LinkedListMultimap.checkElement(this.previous);
      this.next = this.current = this.previous;
      this.previous = this.previous.previousSibling;
      this.nextIndex--;
      return this.current.value;
    }
    
    public int nextIndex() {
      return this.nextIndex;
    }
    
    public int previousIndex() {
      return this.nextIndex - 1;
    }
    
    public void remove() {
      CollectPreconditions.checkRemove((this.current != null));
      if (this.current != this.next) {
        this.previous = this.current.previousSibling;
        this.nextIndex--;
      } else {
        this.next = this.current.nextSibling;
      } 
      LinkedListMultimap.this.removeNode(this.current);
      this.current = null;
    }
    
    public void set(V value) {
      Preconditions.checkState((this.current != null));
      this.current.value = value;
    }
    
    public void add(V value) {
      this.previous = LinkedListMultimap.this.addNode((K)this.key, value, this.next);
      this.nextIndex++;
      this.current = null;
    }
  }
  
  public int size() {
    return this.size;
  }
  
  public boolean isEmpty() {
    return (this.head == null);
  }
  
  public boolean containsKey(@Nullable Object key) {
    return this.keyToKeyList.containsKey(key);
  }
  
  public boolean containsValue(@Nullable Object value) {
    return values().contains(value);
  }
  
  public boolean put(@Nullable K key, @Nullable V value) {
    addNode(key, value, null);
    return true;
  }
  
  public List<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
    List<V> oldValues = getCopy(key);
    ListIterator<V> keyValues = new ValueForKeyIterator(key);
    Iterator<? extends V> newValues = values.iterator();
    while (keyValues.hasNext() && newValues.hasNext()) {
      keyValues.next();
      keyValues.set(newValues.next());
    } 
    while (keyValues.hasNext()) {
      keyValues.next();
      keyValues.remove();
    } 
    while (newValues.hasNext())
      keyValues.add(newValues.next()); 
    return oldValues;
  }
  
  private List<V> getCopy(@Nullable Object key) {
    return Collections.unmodifiableList(Lists.newArrayList(new ValueForKeyIterator(key)));
  }
  
  public List<V> removeAll(@Nullable Object key) {
    List<V> oldValues = getCopy(key);
    removeAllNodes(key);
    return oldValues;
  }
  
  public void clear() {
    this.head = null;
    this.tail = null;
    this.keyToKeyList.clear();
    this.size = 0;
    this.modCount++;
  }
  
  public List<V> get(@Nullable final K key) {
    return new AbstractSequentialList<V>() {
        public int size() {
          LinkedListMultimap.KeyList<K, V> keyList = (LinkedListMultimap.KeyList<K, V>)LinkedListMultimap.this.keyToKeyList.get(key);
          return (keyList == null) ? 0 : keyList.count;
        }
        
        public ListIterator<V> listIterator(int index) {
          return new LinkedListMultimap.ValueForKeyIterator(key, index);
        }
      };
  }
  
  Set<K> createKeySet() {
    return new Sets.ImprovedAbstractSet<K>() {
        public int size() {
          return LinkedListMultimap.this.keyToKeyList.size();
        }
        
        public Iterator<K> iterator() {
          return new LinkedListMultimap.DistinctKeyIterator();
        }
        
        public boolean contains(Object key) {
          return LinkedListMultimap.this.containsKey(key);
        }
        
        public boolean remove(Object o) {
          return !LinkedListMultimap.this.removeAll(o).isEmpty();
        }
      };
  }
  
  public List<V> values() {
    return (List<V>)super.values();
  }
  
  List<V> createValues() {
    return new AbstractSequentialList<V>() {
        public int size() {
          return LinkedListMultimap.this.size;
        }
        
        public ListIterator<V> listIterator(int index) {
          final LinkedListMultimap<K, V>.NodeIterator nodeItr = new LinkedListMultimap.NodeIterator(index);
          return new TransformedListIterator<Map.Entry<K, V>, V>(nodeItr) {
              V transform(Map.Entry<K, V> entry) {
                return entry.getValue();
              }
              
              public void set(V value) {
                nodeItr.setValue(value);
              }
            };
        }
      };
  }
  
  public List<Map.Entry<K, V>> entries() {
    return (List<Map.Entry<K, V>>)super.entries();
  }
  
  List<Map.Entry<K, V>> createEntries() {
    return new AbstractSequentialList<Map.Entry<K, V>>() {
        public int size() {
          return LinkedListMultimap.this.size;
        }
        
        public ListIterator<Map.Entry<K, V>> listIterator(int index) {
          return new LinkedListMultimap.NodeIterator(index);
        }
      };
  }
  
  Iterator<Map.Entry<K, V>> entryIterator() {
    throw new AssertionError("should never be called");
  }
  
  Map<K, Collection<V>> createAsMap() {
    return new Multimaps.AsMap<K, V>(this);
  }
  
  @GwtIncompatible("java.io.ObjectOutputStream")
  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    stream.writeInt(size());
    for (Map.Entry<K, V> entry : entries()) {
      stream.writeObject(entry.getKey());
      stream.writeObject(entry.getValue());
    } 
  }
  
  @GwtIncompatible("java.io.ObjectInputStream")
  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.keyToKeyList = Maps.newLinkedHashMap();
    int size = stream.readInt();
    for (int i = 0; i < size; i++) {
      K key = (K)stream.readObject();
      V value = (V)stream.readObject();
      put(key, value);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\LinkedListMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */