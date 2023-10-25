package com.google.gson.internal;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public final class LinkedTreeMap<K, V> extends AbstractMap<K, V> implements Serializable {
  private static final Comparator<Comparable> NATURAL_ORDER = new Comparator<Comparable>() {
      public int compare(Comparable<Comparable> a, Comparable b) {
        return a.compareTo(b);
      }
    };
  
  Comparator<? super K> comparator;
  
  Node<K, V> root;
  
  int size = 0;
  
  int modCount = 0;
  
  final Node<K, V> header = new Node<K, V>();
  
  private EntrySet entrySet;
  
  private KeySet keySet;
  
  public LinkedTreeMap() {
    this((Comparator)NATURAL_ORDER);
  }
  
  public LinkedTreeMap(Comparator<? super K> comparator) {
    this.comparator = (comparator != null) ? comparator : (Comparator)NATURAL_ORDER;
  }
  
  public int size() {
    return this.size;
  }
  
  public V get(Object key) {
    Node<K, V> node = findByObject(key);
    return (node != null) ? node.value : null;
  }
  
  public boolean containsKey(Object key) {
    return (findByObject(key) != null);
  }
  
  public V put(K key, V value) {
    if (key == null)
      throw new NullPointerException("key == null"); 
    Node<K, V> created = find(key, true);
    V result = created.value;
    created.value = value;
    return result;
  }
  
  public void clear() {
    this.root = null;
    this.size = 0;
    this.modCount++;
    Node<K, V> header = this.header;
    header.next = header.prev = header;
  }
  
  public V remove(Object key) {
    Node<K, V> node = removeInternalByKey(key);
    return (node != null) ? node.value : null;
  }
  
  Node<K, V> find(K key, boolean create) {
    Node<K, V> created;
    Comparator<? super K> comparator = this.comparator;
    Node<K, V> nearest = this.root;
    int comparison = 0;
    if (nearest != null) {
      Comparable<Object> comparableKey = (comparator == NATURAL_ORDER) ? (Comparable<Object>)key : null;
      while (true) {
        comparison = (comparableKey != null) ? comparableKey.compareTo(nearest.key) : comparator.compare(key, nearest.key);
        if (comparison == 0)
          return nearest; 
        Node<K, V> child = (comparison < 0) ? nearest.left : nearest.right;
        if (child == null)
          break; 
        nearest = child;
      } 
    } 
    if (!create)
      return null; 
    Node<K, V> header = this.header;
    if (nearest == null) {
      if (comparator == NATURAL_ORDER && !(key instanceof Comparable))
        throw new ClassCastException(key.getClass().getName() + " is not Comparable"); 
      created = new Node<K, V>(nearest, key, header, header.prev);
      this.root = created;
    } else {
      created = new Node<K, V>(nearest, key, header, header.prev);
      if (comparison < 0) {
        nearest.left = created;
      } else {
        nearest.right = created;
      } 
      rebalance(nearest, true);
    } 
    this.size++;
    this.modCount++;
    return created;
  }
  
  Node<K, V> findByObject(Object key) {
    try {
      return (key != null) ? find((K)key, false) : null;
    } catch (ClassCastException e) {
      return null;
    } 
  }
  
  Node<K, V> findByEntry(Map.Entry<?, ?> entry) {
    Node<K, V> mine = findByObject(entry.getKey());
    boolean valuesEqual = (mine != null && equal(mine.value, entry.getValue()));
    return valuesEqual ? mine : null;
  }
  
  private boolean equal(Object a, Object b) {
    return (a == b || (a != null && a.equals(b)));
  }
  
  void removeInternal(Node<K, V> node, boolean unlink) {
    if (unlink) {
      node.prev.next = node.next;
      node.next.prev = node.prev;
    } 
    Node<K, V> left = node.left;
    Node<K, V> right = node.right;
    Node<K, V> originalParent = node.parent;
    if (left != null && right != null) {
      Node<K, V> adjacent = (left.height > right.height) ? left.last() : right.first();
      removeInternal(adjacent, false);
      int leftHeight = 0;
      left = node.left;
      if (left != null) {
        leftHeight = left.height;
        adjacent.left = left;
        left.parent = adjacent;
        node.left = null;
      } 
      int rightHeight = 0;
      right = node.right;
      if (right != null) {
        rightHeight = right.height;
        adjacent.right = right;
        right.parent = adjacent;
        node.right = null;
      } 
      adjacent.height = Math.max(leftHeight, rightHeight) + 1;
      replaceInParent(node, adjacent);
      return;
    } 
    if (left != null) {
      replaceInParent(node, left);
      node.left = null;
    } else if (right != null) {
      replaceInParent(node, right);
      node.right = null;
    } else {
      replaceInParent(node, null);
    } 
    rebalance(originalParent, false);
    this.size--;
    this.modCount++;
  }
  
  Node<K, V> removeInternalByKey(Object key) {
    Node<K, V> node = findByObject(key);
    if (node != null)
      removeInternal(node, true); 
    return node;
  }
  
  private void replaceInParent(Node<K, V> node, Node<K, V> replacement) {
    Node<K, V> parent = node.parent;
    node.parent = null;
    if (replacement != null)
      replacement.parent = parent; 
    if (parent != null) {
      if (parent.left == node) {
        parent.left = replacement;
      } else {
        assert parent.right == node;
        parent.right = replacement;
      } 
    } else {
      this.root = replacement;
    } 
  }
  
  private void rebalance(Node<K, V> unbalanced, boolean insert) {
    for (Node<K, V> node = unbalanced; node != null; node = node.parent) {
      Node<K, V> left = node.left;
      Node<K, V> right = node.right;
      int leftHeight = (left != null) ? left.height : 0;
      int rightHeight = (right != null) ? right.height : 0;
      int delta = leftHeight - rightHeight;
      if (delta == -2) {
        Node<K, V> rightLeft = right.left;
        Node<K, V> rightRight = right.right;
        int rightRightHeight = (rightRight != null) ? rightRight.height : 0;
        int rightLeftHeight = (rightLeft != null) ? rightLeft.height : 0;
        int rightDelta = rightLeftHeight - rightRightHeight;
        if (rightDelta == -1 || (rightDelta == 0 && !insert)) {
          rotateLeft(node);
        } else {
          assert rightDelta == 1;
          rotateRight(right);
          rotateLeft(node);
        } 
        if (insert)
          break; 
      } else if (delta == 2) {
        Node<K, V> leftLeft = left.left;
        Node<K, V> leftRight = left.right;
        int leftRightHeight = (leftRight != null) ? leftRight.height : 0;
        int leftLeftHeight = (leftLeft != null) ? leftLeft.height : 0;
        int leftDelta = leftLeftHeight - leftRightHeight;
        if (leftDelta == 1 || (leftDelta == 0 && !insert)) {
          rotateRight(node);
        } else {
          assert leftDelta == -1;
          rotateLeft(left);
          rotateRight(node);
        } 
        if (insert)
          break; 
      } else if (delta == 0) {
        node.height = leftHeight + 1;
        if (insert)
          break; 
      } else {
        assert delta == -1 || delta == 1;
        node.height = Math.max(leftHeight, rightHeight) + 1;
        if (!insert)
          break; 
      } 
    } 
  }
  
  private void rotateLeft(Node<K, V> root) {
    Node<K, V> left = root.left;
    Node<K, V> pivot = root.right;
    Node<K, V> pivotLeft = pivot.left;
    Node<K, V> pivotRight = pivot.right;
    root.right = pivotLeft;
    if (pivotLeft != null)
      pivotLeft.parent = root; 
    replaceInParent(root, pivot);
    pivot.left = root;
    root.parent = pivot;
    root.height = Math.max((left != null) ? left.height : 0, (pivotLeft != null) ? pivotLeft.height : 0) + 1;
    pivot.height = Math.max(root.height, (pivotRight != null) ? pivotRight.height : 0) + 1;
  }
  
  private void rotateRight(Node<K, V> root) {
    Node<K, V> pivot = root.left;
    Node<K, V> right = root.right;
    Node<K, V> pivotLeft = pivot.left;
    Node<K, V> pivotRight = pivot.right;
    root.left = pivotRight;
    if (pivotRight != null)
      pivotRight.parent = root; 
    replaceInParent(root, pivot);
    pivot.right = root;
    root.parent = pivot;
    root.height = Math.max((right != null) ? right.height : 0, (pivotRight != null) ? pivotRight.height : 0) + 1;
    pivot.height = Math.max(root.height, (pivotLeft != null) ? pivotLeft.height : 0) + 1;
  }
  
  public Set<Map.Entry<K, V>> entrySet() {
    EntrySet result = this.entrySet;
    return (result != null) ? result : (this.entrySet = new EntrySet());
  }
  
  public Set<K> keySet() {
    KeySet result = this.keySet;
    return (result != null) ? result : (this.keySet = new KeySet());
  }
  
  static final class Node<K, V> implements Map.Entry<K, V> {
    Node<K, V> parent;
    
    Node<K, V> left;
    
    Node<K, V> right;
    
    Node<K, V> next;
    
    Node<K, V> prev;
    
    final K key;
    
    V value;
    
    int height;
    
    Node() {
      this.key = null;
      this.next = this.prev = this;
    }
    
    Node(Node<K, V> parent, K key, Node<K, V> next, Node<K, V> prev) {
      this.parent = parent;
      this.key = key;
      this.height = 1;
      this.next = next;
      this.prev = prev;
      prev.next = this;
      next.prev = this;
    }
    
    public K getKey() {
      return this.key;
    }
    
    public V getValue() {
      return this.value;
    }
    
    public V setValue(V value) {
      V oldValue = this.value;
      this.value = value;
      return oldValue;
    }
    
    public boolean equals(Object o) {
      if (o instanceof Map.Entry) {
        Map.Entry other = (Map.Entry)o;
        return (((this.key == null) ? (other.getKey() == null) : this.key.equals(other.getKey())) && ((this.value == null) ? (other.getValue() == null) : this.value.equals(other.getValue())));
      } 
      return false;
    }
    
    public int hashCode() {
      return ((this.key == null) ? 0 : this.key.hashCode()) ^ ((this.value == null) ? 0 : this.value.hashCode());
    }
    
    public String toString() {
      return (new StringBuilder()).append(this.key).append("=").append(this.value).toString();
    }
    
    public Node<K, V> first() {
      Node<K, V> node = this;
      Node<K, V> child = node.left;
      while (child != null) {
        node = child;
        child = node.left;
      } 
      return node;
    }
    
    public Node<K, V> last() {
      Node<K, V> node = this;
      Node<K, V> child = node.right;
      while (child != null) {
        node = child;
        child = node.right;
      } 
      return node;
    }
  }
  
  private abstract class LinkedTreeMapIterator<T> implements Iterator<T> {
    LinkedTreeMap.Node<K, V> next = LinkedTreeMap.this.header.next;
    
    LinkedTreeMap.Node<K, V> lastReturned = null;
    
    int expectedModCount = LinkedTreeMap.this.modCount;
    
    public final boolean hasNext() {
      return (this.next != LinkedTreeMap.this.header);
    }
    
    final LinkedTreeMap.Node<K, V> nextNode() {
      LinkedTreeMap.Node<K, V> e = this.next;
      if (e == LinkedTreeMap.this.header)
        throw new NoSuchElementException(); 
      if (LinkedTreeMap.this.modCount != this.expectedModCount)
        throw new ConcurrentModificationException(); 
      this.next = e.next;
      return this.lastReturned = e;
    }
    
    public final void remove() {
      if (this.lastReturned == null)
        throw new IllegalStateException(); 
      LinkedTreeMap.this.removeInternal(this.lastReturned, true);
      this.lastReturned = null;
      this.expectedModCount = LinkedTreeMap.this.modCount;
    }
    
    private LinkedTreeMapIterator() {}
  }
  
  class EntrySet extends AbstractSet<Map.Entry<K, V>> {
    public int size() {
      return LinkedTreeMap.this.size;
    }
    
    public Iterator<Map.Entry<K, V>> iterator() {
      return new LinkedTreeMap<K, V>.LinkedTreeMapIterator<Map.Entry<K, V>>() {
          public Map.Entry<K, V> next() {
            return nextNode();
          }
        };
    }
    
    public boolean contains(Object o) {
      return (o instanceof Map.Entry && LinkedTreeMap.this.findByEntry((Map.Entry<?, ?>)o) != null);
    }
    
    public boolean remove(Object o) {
      if (!(o instanceof Map.Entry))
        return false; 
      LinkedTreeMap.Node<K, V> node = LinkedTreeMap.this.findByEntry((Map.Entry<?, ?>)o);
      if (node == null)
        return false; 
      LinkedTreeMap.this.removeInternal(node, true);
      return true;
    }
    
    public void clear() {
      LinkedTreeMap.this.clear();
    }
  }
  
  class KeySet extends AbstractSet<K> {
    public int size() {
      return LinkedTreeMap.this.size;
    }
    
    public Iterator<K> iterator() {
      return new LinkedTreeMap<K, V>.LinkedTreeMapIterator<K>() {
          public K next() {
            return (nextNode()).key;
          }
        };
    }
    
    public boolean contains(Object o) {
      return LinkedTreeMap.this.containsKey(o);
    }
    
    public boolean remove(Object key) {
      return (LinkedTreeMap.this.removeInternalByKey(key) != null);
    }
    
    public void clear() {
      LinkedTreeMap.this.clear();
    }
  }
  
  private Object writeReplace() throws ObjectStreamException {
    return new LinkedHashMap<K, V>(this);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\internal\LinkedTreeMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */